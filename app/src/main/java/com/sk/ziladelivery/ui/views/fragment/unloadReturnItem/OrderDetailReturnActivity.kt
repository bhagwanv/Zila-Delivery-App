package com.sk.ziladelivery.ui.views.fragment.unloadReturnItem

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.*
import com.sk.ziladelivery.databinding.ActivityOrderReturnDetailsBinding
import com.sk.ziladelivery.databinding.BottomSkipLayoutBinding
import com.sk.ziladelivery.databinding.DialogConfirmationBinding
import com.sk.ziladelivery.databinding.DialogVideoUploadBinding
import com.sk.ziladelivery.listener.OrderDetailReturnInterface
import com.sk.ziladelivery.ui.views.VideoRecorderExample
import com.sk.ziladelivery.ui.views.main.CustomerRatingActivity
import com.sk.ziladelivery.ui.views.main.MainActivity
import com.sk.ziladelivery.ui.views.viewmodels.OrderDetailsViewModel
import com.sk.ziladelivery.utilities.*
import com.sk.ziladelivery.viewfactory.OrderDetailsFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class OrderDetailReturnActivity : AppCompatActivity(), OrderDetailReturnInterface,
    View.OnClickListener, Listener {
    private val REQUEST_VIDEO_CAPTURE = 101
    private val PERMISSION_REQUEST_CODE = 100
    private var mBinding: ActivityOrderReturnDetailsBinding? = null
    private var orderDetailsViewModel: OrderDetailsViewModel? = null
    private var tripOrderStatusUpdateModel: TripOrderStatusUpdateModel? = null
    private var customerorderinfoEntityMaster: TripOrderStatusUpdateModel.CustomerorderinfoEntity? = null
    private var grossAmount = 0.0
    private var remainingAmount = 0.0
    private var shippedAmount = 0.0
    private var deliveredAmount = 0.0
    private var tripPlannerConfirmedDetailId = 0
    private var TripPlannerConfirmedOrderId = 0
    private var notifyDeliveryCancelled = 0
    private var salesPersonName: String? = null
    private var salesPersonMobile: String? = null
    private var selectedDate: String? = null
    private var selectedStatus: String? = null
    private var time: String? = null
    private var reason = ""
    private var easyWayLocation: EasyWayLocation? = null
    private var customerLatLng: LatLng? = null
    private var cTimer: CountDownTimer? = null
    private var orderIdInterface = 0
    private var isReattempt = false
    private var tvCancelOrderTimer: TextView? = null
    private var tvSalesPersonName: TextView? = null
    private var tvTimerOTP: TextView? = null
    private var btnCancelOrderSendOtp: Button? = null
    private var btnResendOtp: Button? = null
    private var rLSalesDetails: RelativeLayout? = null
    private var timerTime: Long = 900000
    private var cancelDialog: BottomSheetDialog? = null
    private var otpDialog: BottomSheetDialog? = null
    private var genrateOtp: String? = null
    var isReturnOrder = false
    private var orderIDList: MutableList<Int> = ArrayList()
    private var rdDateList: List<String> = emptyList()
    val reattemptReasonList = ArrayList<String>()
    private lateinit var orderReturnDetailsAdapter: OrderReturnDetailsAdapter
    private var choosePersonDilog: BottomSheetDialog? = null
    private var reatteptVideoRequired: Boolean = false
    private var videoPath: Uri? = null
    private var redispatchDialog: BottomSheetDialog? = null

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityOrderReturnDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        viewCreated()
    }

    private fun viewCreated() {
        orderDetailsViewModel = ViewModelProvider(
            viewModelStore,
            OrderDetailsFactory(ApiHelper(RestClient.getInstance().service))
        )[OrderDetailsViewModel::class.java]
        dataFromIntent()
        setActionBarConfiguration()
        if (!Utils.checkInternetConnection(applicationContext)) {
            Utils.setToast(applicationContext, resources.getString(R.string.network_error))
        } else {
            customerWiseOrderListApi()
        }

        initView()
    }

    private fun dataFromIntent() {
        isReturnOrder = intent.getBooleanExtra("isReturnOrder", true)
        tripPlannerConfirmedDetailId =
            intent.getIntExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, 0)
        time = intent.getStringExtra("time")
        notifyDeliveryCancelled = intent.getIntExtra("NotifyDeliveryCancelled", 0)

    }

    private fun initView() {
        RxBus.getInstance().event.observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Any?>() {
                override fun onNext(o: Any) {
                    try {
                        if (o is String) {
                            if (o.equals("OrderId", ignoreCase = true)) {
                                if (cancelDialog != null && cancelDialog!!.isShowing) {
                                    cancelDialog!!.dismiss()
                                    showOtpDialog(
                                        60,
                                        0,
                                        0
                                    )
                                }
                            } else if (o.equals("Reject", ignoreCase = true)) {
                                if (cancelDialog != null && cancelDialog!!.isShowing) {
                                    cancelDialog!!.dismiss()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onComplete() {}
            })
        mBinding!!.btReturnItem.setOnClickListener(this)
        mBinding!!.llSkip.setOnClickListener(this)
        mBinding!!.rvItemList.layoutManager = LinearLayoutManager(this)
        customerLatLng = LatLng(0.0, 0.0)
        easyWayLocation = EasyWayLocation(this, createLocationRequest(), false, true, this)
        easyWayLocation!!.startLocation()
        timerMethod()
    }

    private fun createLocationRequest() = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.MINUTES.toMillis(5)
    ).apply {
        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        setDurationMillis(TimeUnit.MINUTES.toMillis(5))
        setWaitForAccurateLocation(true)
        setMaxUpdates(1)
    }.build()

    private fun timerMethod() {
        val savedTime = SharePrefs.getInstance(this).getInt(SharePrefs.OTP_TIME)
        timerTime = (savedTime * 60 * 1000).toLong()
    }

    private fun customerWiseOrderListApi() {
        orderDetailsViewModel!!.getCustomerWiseOrderList(tripPlannerConfirmedDetailId, true)
            .observe(this@OrderDetailReturnActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { customerWiseOrderList ->
                                customerOrderList(customerWiseOrderList)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(this)
                        }
                    }
                }
            }
    }

    private fun customerOrderList(customerWiseOrderList: JsonObject) {

        try {
            var isShipped = false
            tripOrderStatusUpdateModel = Gson().fromJson(
                customerWiseOrderList.toString(),
                TripOrderStatusUpdateModel::class.java
            )
            mBinding!!.tvStoreSkCode.text = tripOrderStatusUpdateModel!!.customerinfo!!.skcode
            mBinding!!.tvCRMTAg.text = tripOrderStatusUpdateModel!!.customerinfo!!.crmTags
            mBinding!!.tvStoreName.text =
                tripOrderStatusUpdateModel!!.customerinfo!!.shopname
            mBinding!!.tvStoreAmount.text =
                tripOrderStatusUpdateModel!!.customerinfo!!.grossAmount.toString()

            if (!TextUtils.isNullOrEmpty(tripOrderStatusUpdateModel!!.customerinfo!!.billingAddress)) {
                mBinding!!.tvStoreAddresh.text =
                    tripOrderStatusUpdateModel!!.customerinfo!!.billingAddress.toString()
            }
            for (i in tripOrderStatusUpdateModel!!.customerorderinfo!!.indices) {
                if (tripOrderStatusUpdateModel!!.customerorderinfo!![i].status.equals(
                        "Shipped",
                        ignoreCase = true
                    )
                ) {
                    isShipped = true
                    break
                }
            }
            remainingAmount = 0.0
            for (i in tripOrderStatusUpdateModel!!.customerorderinfo!!.indices) {
                if (tripOrderStatusUpdateModel!!.customerorderinfo!![i].status == "Shipped") {
                    shippedAmount += tripOrderStatusUpdateModel!!.customerorderinfo!![i].grossamount
                    if (tripOrderStatusUpdateModel!!.customerorderinfo!![i].isBoolWorkingStatus) {
                        grossAmount += tripOrderStatusUpdateModel!!.customerorderinfo!![i].grossamount
                        remainingAmount += tripOrderStatusUpdateModel!!.customerorderinfo!![i].remaningAmount
                    }
                }
                if (tripOrderStatusUpdateModel!!.customerorderinfo!![i].isBoolWorkingStatus) {
                    mBinding!!.btReturnItem.background = ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.button_bg_blue
                    )
                    mBinding!!.btReturnItem.isClickable = true
                    mBinding!!.btReturnItem.isEnabled = true
                }
            }
            deliveredAmount =
                tripOrderStatusUpdateModel!!.customerinfo!!.grossAmount - shippedAmount
            mBinding!!.tvDelivertAmount.text =
                "" + tripOrderStatusUpdateModel!!.customerinfo!!.totalDeliverd
            for (i in tripOrderStatusUpdateModel!!.customerorderinfo!!.indices) {
                if (tripOrderStatusUpdateModel?.customerorderinfo!![i].status == "Delivery Redispatch" || tripOrderStatusUpdateModel!!.customerorderinfo!![i].status == "Delivery Canceled" || tripOrderStatusUpdateModel!!.customerorderinfo!![i].status == "Delivered") {
                    mBinding!!.llSkip.visibility = View.GONE
                }
            }
            mBinding!!.tvTotalPaybleAmount.text = remainingAmount.toString()
            if (isShipped) {
                orderReturnDetailsAdapter = OrderReturnDetailsAdapter(
                    this,
                    tripOrderStatusUpdateModel!!.customerorderinfo!!, this
                )
                mBinding!!.rvItemList.adapter = orderReturnDetailsAdapter
            } else {
                cancelTimer()
                if (tripOrderStatusUpdateModel!!.customerinfo!!.isReAttempt) {
                    startActivity(Intent(this, MainActivity::class.java))
                    Utils.rightTransaction(this)
                    finish()
                } else {
                    startActivity(
                        Intent(this, CustomerRatingActivity::class.java).putExtra(
                            Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
                            tripPlannerConfirmedDetailId
                        )
                    )
                    Utils.rightTransaction(this)
                    finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setActionBarConfiguration() {
        mBinding!!.tvOrderno.text = "Return Order Details"
        mBinding!!.ivBack.setOnClickListener {
            cancelTimer()
            onBackPressed()
        }
    }

    override fun onDestroy() {
        cancelTimer()
        releasePlayer()
        super.onDestroy()
    }

    override fun checkBoxClicked(
        isChecked: Boolean,
        postion: Int,
    ) {
        if (isChecked) {
            tripOrderStatusUpdateModel!!.customerorderinfo!![postion].isBoolWorkingStatus = true
            btnEnable()
            grossAmount += tripOrderStatusUpdateModel!!.customerorderinfo!![postion].grossamount
            remainingAmount += tripOrderStatusUpdateModel!!.customerorderinfo!![postion].remaningAmount
        } else {
            grossAmount -= tripOrderStatusUpdateModel!!.customerorderinfo!![postion].grossamount
            remainingAmount -= tripOrderStatusUpdateModel!!.customerorderinfo!![postion].remaningAmount
            tripOrderStatusUpdateModel!!.customerorderinfo!![postion].isBoolWorkingStatus = false
            if (remainingAmount == 0.0) {
                mBinding!!.btReturnItem.background = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.button_bg_drawable
                )
                mBinding!!.btReturnItem.isClickable = false
                mBinding!!.btReturnItem.isEnabled = false
            }
            btnEnable()
        }
        mBinding!!.tvTotalPaybleAmount.text = remainingAmount.toString()
    }

    private fun btnEnable() {
        var itemChecked = false
        tripOrderStatusUpdateModel!!.customerorderinfo!!.forEach {
            if (it.isBoolWorkingStatus) {
                itemChecked = true
            }
        }
        if (itemChecked) {
            mBinding!!.btReturnItem.background = ContextCompat.getDrawable(
                applicationContext,
                R.drawable.button_bg_blue
            )
            mBinding!!.btReturnItem.isClickable = true
            mBinding!!.btReturnItem.isEnabled = true
        } else {
            mBinding!!.btReturnItem.background = ContextCompat.getDrawable(
                applicationContext,
                R.drawable.button_bg_drawable
            )
            mBinding!!.btReturnItem.isClickable = false
            mBinding!!.btReturnItem.isEnabled = false
        }
    }

    override fun onButtonClick(
        buttonText: String,
        postion: Int,
        orderId: Int,
        customerorderinfoEntity: TripOrderStatusUpdateModel.CustomerorderinfoEntity
    ) {


        orderIdInterface = orderId
        customerorderinfoEntityMaster = customerorderinfoEntity
        salesPersonMobile = customerorderinfoEntity.salePersonMobile
        salesPersonName = customerorderinfoEntity.salePersonName
        TripPlannerConfirmedOrderId = customerorderinfoEntity.tripPlannerConfirmedOrderId
        SharePrefs.getInstance(this@OrderDetailReturnActivity).putInt(SharePrefs.ORDERID, orderId)
        if (buttonText == "Cancel Order") {
            isReattempt = false
            selectedStatus = "Delivery Canceled"
            timerMethod()
            SharePrefs.getInstance(this@OrderDetailReturnActivity)
                .putString(SharePrefs.SELECTED_STATUS, selectedStatus)
            val stringArrayList = ArrayList<String>()
            stringArrayList.add("Delay delivery")
            stringArrayList.add("Item price issue")
            stringArrayList.add("Order not placed by customer")
            stringArrayList.add("Double order placed by customer")
            stringArrayList.add("Item damaged or expired")
            stringArrayList.add("Wrong item was sent")
            stringArrayList.add("Payment issue")
            stringArrayList.add("Other")
            if (customerorderinfoEntity.workingStatus == 6 && !customerorderinfoEntity.isOTPSent) {
                timerTime = customerorderinfoEntity.remaningTimeInMins * 1000
                showCancelOrderDialog(
                    "Approval Sent",
                    customerorderinfoEntity.orderid,
                    stringArrayList,
                    true,
                    customerorderinfoEntity
                )

            } else if (customerorderinfoEntity.workingStatus == 6 && customerorderinfoEntity.isOTPSent) {
                timerTime = customerorderinfoEntity.otpSentRemaningTimeInSec.toLong()
                if (customerorderinfoEntity.reason != null) {
                    if (!TextUtils.isNullOrEmpty(customerorderinfoEntity.reason!!)) {
                        reason = customerorderinfoEntity.reason!!
                    }
                }
                showOtpDialog(
                    timerTime,
                    customerorderinfoEntity.tripPlannerConfirmedDetailId,
                    customerorderinfoEntity.tripPlannerConfirmedOrderId
                )
            } else {
                confirmationDialog("Cancel", customerorderinfoEntity, stringArrayList)
            }
        } else if (buttonText == "Re Attempt") {
            reason = ""
            reattemptReasonList.clear()
            //new comments given by Bhupendra
            reattemptReasonList.add("वाहन में जगह नहीं है")
            reattemptReasonList.add("दुकान तक नहीं पहुंच पाया")
            reattemptReasonList.add("वाहन उपलब्ध नहीं है")
            reattemptReasonList.add("प्लानर -प्लान समस्या")
            reattemptReasonList.add("दुकान बंद है")
            reattemptReasonList.add("वाहन ख़राब हो गया")
            reattemptReasonList.add("दुकान नहीं मिली")
            reattemptReasonList.add("कस्टमर उपलब्ध नहीं है")
            reattemptReasonList.add("बारिश हो रही है /ट्रैफिक जाम")
            reattemptReasonList.add("Other")

            selectedStatus = "Delivery Redispatch"
            isReattempt = true
            selectedStatus = "Delivery Redispatch"
            isReattempt = true
            if (customerorderinfoEntity.workingStatus == 8 && customerorderinfoEntity.isOTPSent) {
                timerTime = customerorderinfoEntity.otpSentRemaningTimeInSec.toLong()
                showOTPofSalesPersonDilog(timerTime, "", customerorderinfoEntity.tripPlannerConfirmedDetailId, customerorderinfoEntity.tripPlannerConfirmedOrderId)
            } else {
                confirmationDialog("Re Attempt", customerorderinfoEntity, reattemptReasonList)
            }


        } else if (buttonText == "Re Dispatch") {
            isReattempt = false
            selectedStatus = "Delivery Redispatch"
            timerMethod()

            SharePrefs.getInstance(this@OrderDetailReturnActivity)
                .putString(SharePrefs.SELECTED_STATUS, selectedStatus)
            val stringArrayList = ArrayList<String>()
            stringArrayList.add("Concern Customer Unavailable")
            stringArrayList.add("Customer Busy")
            stringArrayList.add("Don't Need")
            stringArrayList.add("Other")
            if (customerorderinfoEntity.workingStatus == 7 && customerorderinfoEntity.isOTPSent) {
                timerTime = customerorderinfoEntity.otpSentRemaningTimeInSec.toLong()
                if (customerorderinfoEntity.reason != null) {
                    if (!TextUtils.isNullOrEmpty(customerorderinfoEntity.reason!!)) {
                        reason = customerorderinfoEntity.reason!!
                    }
                }
                showOtpDialog(
                    timerTime,
                    customerorderinfoEntity.tripPlannerConfirmedDetailId,
                    customerorderinfoEntity.tripPlannerConfirmedOrderId
                )
            } else {
                confirmationDialog("Re Dispatch", customerorderinfoEntity, stringArrayList)
            }
        }
    }

    private fun showCancelOrderDialog(
        head: String,
        orderId: Int,
        stringArrayList: ArrayList<String>,
        isTimed: Boolean,
        customerOrderInfoEntity: TripOrderStatusUpdateModel.CustomerorderinfoEntity
    ) {
        cancelDialog = BottomSheetDialog(this)
        cancelDialog!!.setContentView(R.layout.dialog_cancel_order)
        cancelDialog!!.setCanceledOnTouchOutside(false)
        val etEnterComment = cancelDialog!!.findViewById<EditText>(R.id.etEnterComment)
        val tvHead = cancelDialog!!.findViewById<TextView>(R.id.tvHead)
        val tvCancelReason = cancelDialog!!.findViewById<TextView>(R.id.tvCancelReason)
        tvSalesPersonName = cancelDialog!!.findViewById(R.id.tvSalesPersonName)
        val imSalesCallPerson = cancelDialog!!.findViewById<ImageView>(R.id.imSalesCallPerson)
        rLSalesDetails = cancelDialog!!.findViewById(R.id.RLSalesDetails)
        val ilReasonLayout = cancelDialog!!.findViewById<LinearLayout>(R.id.LLReasonLayout)
        val fbCancelOrder = cancelDialog!!.findViewById<FlexboxLayout>(R.id.fbCancelOrder)
        val btnSendNotification = cancelDialog!!.findViewById<Button>(R.id.btnSendNotification)
        val ivCrossCancleOrder = cancelDialog!!.findViewById<ImageView>(R.id.ivCross)
        btnSendNotification!!.visibility = View.VISIBLE
        btnCancelOrderSendOtp = cancelDialog!!.findViewById(R.id.btnSendOtp)
        tvCancelOrderTimer = cancelDialog!!.findViewById(R.id.tvTimer)
        if (salesPersonName == null) {
            salesPersonName = ""
        }
        tvSalesPersonName!!.text = "Sales Person Name \n$salesPersonName"

        ivCrossCancleOrder!!.setOnClickListener {
            cancelDialog!!.dismiss()
            getRemoveCurrentSatusApi(
                customerOrderInfoEntity.tripPlannerConfirmedDetailId,
                customerOrderInfoEntity.tripPlannerConfirmedOrderId
            )
        }
        imSalesCallPerson!!.setOnClickListener {
            if (!TextUtils.isNullOrEmpty(salesPersonMobile)) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91$salesPersonMobile"))
                startActivity(intent)
            } else {
                Utils.setToast(this@OrderDetailReturnActivity, "Number Not Available")
            }
        }
        if (head.equals("Approval Sent", ignoreCase = true)) {
            fbCancelOrder!!.visibility = View.GONE
            ilReasonLayout!!.visibility = View.VISIBLE
            rLSalesDetails!!.visibility = View.VISIBLE
            tvCancelReason!!.text = reason
            tvHead!!.text = "$head\nOrder Id - $orderId"
        } else {
            fbCancelOrder!!.visibility = View.VISIBLE
            ilReasonLayout!!.visibility = View.GONE
            rLSalesDetails!!.visibility = View.GONE
            tvHead!!.text = "Order Id - $orderId\n$head"
        }
        updateViews(fbCancelOrder, stringArrayList, etEnterComment)
        btnCancelOrderSendOtp!!.setOnClickListener {
            val cancelOrderSendOtpModel = CancelOrderSendOtpModel(
                orderIdInterface,
                null,
                tripPlannerConfirmedDetailId.toLong(),
                customerLatLng!!.latitude,
                customerLatLng!!.longitude
            )
            cancelOrderSendOtpNotifyUpdateAPi(cancelOrderSendOtpModel)
            cancelDialog!!.dismiss()
        }
        if (isTimed) {
            btnSendNotification.visibility = View.INVISIBLE
            tvCancelOrderTimer!!.visibility = View.VISIBLE
            rLSalesDetails!!.visibility = View.VISIBLE
            timer(timerTime, tvCancelOrderTimer, btnCancelOrderSendOtp, false)
        } else {
            btnSendNotification.visibility = View.VISIBLE
            tvCancelOrderTimer!!.visibility = View.INVISIBLE
        }
        btnSendNotification.setOnClickListener {
            if ((reason == "Other")) {
                reason = etEnterComment!!.text.toString()
            }
            if ((reason == "")) {
                Utils.setToast(this, "Please Select/Enter Reason")
            } else {
                val model = CancelOrderModel(
                    tripPlannerConfirmedDetailId.toLong(), reason, orderId,
                    customerLatLng!!.latitude, customerLatLng!!.longitude
                )
                cancelOrderNotifyUpdate(model)
                btnSendNotification.visibility = View.INVISIBLE
                fbCancelOrder.visibility = View.GONE
            }
        }
        cancelDialog!!.show()
        cancelDialog!!.setOnDismissListener {
            try {
                customerWiseOrderListApi()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getRemoveCurrentSatusApi(
        tripPlannerConfirmedDetailId: Int,
        tripPlannerConfirmedOrderId: Int
    ) {
        orderDetailsViewModel!!.getRemoveCurrentSatusAPI(
            tripPlannerConfirmedDetailId,
            tripPlannerConfirmedOrderId
        )
            .observe(this@OrderDetailReturnActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { backRemoveResponce ->
                                if (backRemoveResponce) {
                                    customerWiseOrderListApi()
                                }
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(this)
                        }
                    }
                }
            }
    }

    private fun cancelOrderSendOtpNotifyUpdateAPi(cancelOrderSendOtpModel: CancelOrderSendOtpModel) {
        orderDetailsViewModel!!.cancelOrderSendOtpNotifyUpdateApi(cancelOrderSendOtpModel)
            .observe(this@OrderDetailReturnActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { cancelOrderSendOtpNotifyUpdate ->
                                showOtpDialog(
                                    60,
                                    tripPlannerConfirmedDetailId,
                                    TripPlannerConfirmedOrderId
                                )

                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(this)
                        }
                    }
                }
            }
    }

    private fun cancelOrderNotifyUpdate(model: CancelOrderModel) {
        orderDetailsViewModel!!.cancelOrderNotifyUpdateApi(model)
            .observe(this@OrderDetailReturnActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { cancelOrderNotifyUpdate ->
                                cancelOrderNotify(cancelOrderNotifyUpdate)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(this)
                        }
                    }
                }
            }
    }

    private fun cancelOrderNotify(cancelOrderNotifyUpdate: JsonObject) {
        try {
            val jsonObject = JSONObject(cancelOrderNotifyUpdate.toString())
            val job = jsonObject.getJSONObject("salesPersonDc")
            salesPersonName = job.getString("SalePersonName")
            salesPersonMobile = job.getString("SalePersonMobile")
            if (tvSalesPersonName != null) {
                if (salesPersonName == null) {
                    salesPersonName = ""
                    btnCancelOrderSendOtp!!.visibility = View.VISIBLE
                    tvCancelOrderTimer!!.visibility = View.GONE
                }
                tvSalesPersonName!!.text = "Sales Person Name \n$salesPersonName"
            }
            timer(timerTime, tvCancelOrderTimer, btnCancelOrderSendOtp, false)
            rLSalesDetails!!.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showRedispatchDialog(
        orderId: Int,
        stringArrayList: ArrayList<String>,
        IsReattemt: Boolean,
        videoURL: String
    ) {
        redispatchDialog = BottomSheetDialog(this)
        redispatchDialog!!.setContentView(R.layout.dialog_redispatch_order)
        redispatchDialog!!.setCanceledOnTouchOutside(false)
        val etEnterComment = redispatchDialog!!.findViewById<EditText>(R.id.etEnterComment)
        val tvHead = redispatchDialog!!.findViewById<TextView>(R.id.tvHead)

        orderDetailsViewModel!!.getHolidayOnRedispatch(orderId, false)
            .observe(this@OrderDetailReturnActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { holidayOnRedispatchlist ->
                                rdDateList = DateUtils.filterDates(holidayOnRedispatchlist)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(this)
                        }
                    }
                }
            }

        if (isReattempt) {
            tvHead!!.text = "Re-Attempt Reason"
        } else {
            tvHead!!.text = "Re-Dispatch Reason"
        }

        val tvSelectDate = redispatchDialog!!.findViewById<TextView>(R.id.tvSelectDate)
        val fbCancelOrder = redispatchDialog!!.findViewById<FlexboxLayout>(R.id.fbCancelOrder)
        val btnSendNotification = redispatchDialog!!.findViewById<Button>(R.id.btnSendNotification)
        updateViews(fbCancelOrder, stringArrayList, etEnterComment)
        tvSelectDate!!.setOnClickListener { showDatePicker(tvSelectDate) }
        val llPlayVideo = redispatchDialog!!!!.findViewById<LinearLayout>(R.id.llPlayVideo)
        val llRetakeVideo = redispatchDialog!!!!.findViewById<LinearLayout>(R.id.llRetakeVideo)

        updateViews(fbCancelOrder, stringArrayList, etEnterComment, llPlayVideo, llRetakeVideo)
        var vURL = videoURL
        btnSendNotification!!.setOnClickListener {
            if ((reason == "Other")) {
                reason = etEnterComment!!.text.toString()
            }
            if ((reason == "")) {
                Utils.setToast(this, "Please Select/Enter Reason")
            } else if ((tvSelectDate.text.toString().trim { it <= ' ' } == "")) {
                Utils.setToast(this, "Please Select Date")
            } else {
                selectedDate = (tvSelectDate.text).toString().trim { it <= ' ' }
                SharePrefs.getInstance(this@OrderDetailReturnActivity)
                    .putString(SharePrefs.SELECTED_DATE, selectedDate)
                SharePrefs.getInstance(this@OrderDetailReturnActivity).putString(SharePrefs.REASON, reason)
                SharePrefs.getInstance(this@OrderDetailReturnActivity)
                    .putBoolean(SharePrefs.ISREATTEMPT, isReattempt)
                generateOTPofSalesPersonforReattempt(
                    orderIdInterface,
                    selectedStatus!!,
                    customerLatLng!!.latitude,
                    customerLatLng!!.longitude,
                    reason,
                    vURL
                )
                // getNotifyReDispatchReAttemptMethod(reDispatchModel)
                redispatchDialog!!.dismiss()

            }
        }
        if (customerorderinfoEntityMaster!!.orderType != "Non-Sellable Order" && customerorderinfoEntityMaster!!.orderType != "Damage Order") {
            if (vURL.isNotEmpty()) {
                if (llPlayVideo != null) {
                    llPlayVideo.visibility = View.VISIBLE
                }
                if (llRetakeVideo != null) {
                    llRetakeVideo.visibility = View.VISIBLE
                }
            } else {
                if (llPlayVideo != null) {
                    llPlayVideo.visibility = View.GONE
                }
                if (llRetakeVideo != null) {
                    llRetakeVideo.visibility = View.GONE
                }
            }
        }

        llPlayVideo!!.setOnClickListener {
            videoDialog(videoURL)
        }
        llRetakeVideo!!.setOnClickListener {
            showRetakeVideoAlert(redispatchDialog!!)
        }

        redispatchDialog!!.show()
        redispatchDialog!!.setOnDismissListener {
            try {
                mBinding!!.tvTotalPaybleAmount.text = "0"
                customerWiseOrderListApi()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun generateOTPofSalesPersonforReattempt(
        orderid: Int,
        status: String,
        lat: Double,
        lg: Double,
        reason: String,
        videoURL: String
    ) {
        orderIDList.clear()
        orderIDList.add(orderid)
        var model = GenerateOTPofSalesPersonforReattemptRequestModel(
            orderIDList,
            status,
            lat,
            lg,
            reason,
            videoURL
        )
        orderDetailsViewModel!!.generateOTPofSalesPersonforReattempt(model)
            .observe(this@OrderDetailReturnActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { generateOTPofSalesPerson ->
                                generateOTPofSalesPerson(generateOTPofSalesPerson)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(this)
                        }
                    }
                }
            }

    }

    private fun generateOTPofSalesPerson(generateOTPofSalesPerson: JsonObject) {
        try {
            val message = generateOTPofSalesPerson.asJsonObject["msg"].asString
            val res = generateOTPofSalesPerson.asJsonObject["res"].asBoolean
            if (res) {
                showOTPofSalesPersonDilog(
                    60,
                    message,
                    tripPlannerConfirmedDetailId,
                    TripPlannerConfirmedOrderId
                )
            } else {
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showOTPofSalesPersonDilog(
        otpTimer: Long,
        message: String,
        tripPlannerConfirmedDetailId: Int,
        tripPlannerConfirmedOrderId: Int,
    ) {
        ProgressDialog.getInstance().dismiss()
        timerTime = otpTimer * 1000
        otpDialog = BottomSheetDialog(this)
        otpDialog!!.setContentView(R.layout.dialog_otp_verification)
        otpDialog!!.setCanceledOnTouchOutside(true)
        val otpEditBox1 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box1)
        val otpEditBox2 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box2)
        val otpEditBox3 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box3)
        val otpEditBox4 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box4)
        val tvHead = otpDialog!!.findViewById<TextView>(R.id.tvHead)
        val crossIV = otpDialog!!.findViewById<ImageView>(R.id.ivCross)
        tvHead!!.text = "Verification Code\nOrder Id - $orderIdInterface"
        val tvName = otpDialog!!.findViewById<TextView>(R.id.tvName)
        val tvTextViewSms = otpDialog!!.findViewById<TextView>(R.id.tvTextViewSms)
        tvTextViewSms!!.visibility = View.GONE
        tvName!!.visibility = View.VISIBLE
        tvName!!.text = message

        btnResendOtp = otpDialog!!.findViewById(R.id.btnResendOtp)
        val btnVerifyOtp = otpDialog!!.findViewById<Button>(R.id.btnVerifyOtp)
        tvTimerOTP = otpDialog!!.findViewById(R.id.tvTimer)
        btnResendOtp!!.isEnabled = false
        btnResendOtp!!.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.gray
            )
        )
        timer(timerTime, tvTimerOTP, btnResendOtp, true)
        val edit = arrayOf(otpEditBox1, otpEditBox2, otpEditBox3, otpEditBox4)
        otpEditBox1!!.addTextChangedListener(GenericTextWatcher(otpEditBox1, edit))
        otpEditBox2!!.addTextChangedListener(GenericTextWatcher(otpEditBox2, edit))
        otpEditBox3!!.addTextChangedListener(GenericTextWatcher(otpEditBox3, edit))
        otpEditBox4!!.addTextChangedListener(GenericTextWatcher(otpEditBox4, edit))

        crossIV!!.setOnClickListener {
            otpDialog!!.dismiss()
            getRemoveCurrentSatusApi(
                tripPlannerConfirmedDetailId,
                tripPlannerConfirmedOrderId
            )
        }

        isReattempt = true
        selectedStatus = "Delivery Redispatch"
        if (isReattempt) {
            btnResendOtp!!.text = "Retry"
        } else {
            btnResendOtp!!.text = "Resend OTP"
        }

        btnResendOtp!!.setOnClickListener {
            otpDialog!!.dismiss()
            var vUrl = SharePrefs.getInstance(this@OrderDetailReturnActivity).getString(SharePrefs.VIDEO_URL)
            if (customerorderinfoEntityMaster!!.orderType == "Non-Sellable Order" || customerorderinfoEntityMaster!!.orderType == "Damage Order" || !reatteptVideoRequired) {
                vUrl = ""
            }
            otpDialog!!.dismiss()
            generateOTPofSalesPersonforReattempt(
                orderIdInterface,
                selectedStatus!!,
                customerLatLng!!.latitude,
                customerLatLng!!.longitude,
                SharePrefs.getInstance(this).getString(SharePrefs.REASON),
                vUrl
            )
        }
        btnVerifyOtp!!.setOnClickListener {
            genrateOtp = (otpEditBox1.text.toString() +
                    otpEditBox2.text.toString() +
                    otpEditBox3.text.toString() +
                    otpEditBox4.text.toString())
            if (genrateOtp!!.length < 4) {
                Utils.setToast(this@OrderDetailReturnActivity, "Enter Valid OTP")
            } else {
                getOTPNotifyReDispatchReAttemptMethod(
                    genrateOtp!!,
                    orderIdInterface,
                    selectedStatus
                )
            }
        }
        otpDialog!!.show()
        otpDialog!!.setOnDismissListener {
            try {
                customerWiseOrderListApi()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    /* private fun getNotifyReDispatchReAttemptMethod(reDispatchModel: ReDispatchModel) {
         orderDetailsViewModel!!.getNotifyReDispatchReAttemptApi(reDispatchModel)
             .observe(this@OrderDetailReturnActivity) {
                 it?.let { resource ->
                     when (resource.status) {
                         Status.SUCCESS -> {
                             ProgressDialog.getInstance().dismiss()
                             resource.data?.let { notifyReDispatchReAttempt ->
                                 notifyReDispatchRe(notifyReDispatchReAttempt)
                             }
                         }

                         Status.ERROR -> {
                             ProgressDialog.getInstance().dismiss()
                             Utils.setToast(applicationContext, it.message)
                         }

                         Status.LOADING -> {
                             ProgressDialog.getInstance().show(this)
                         }
                     }
                 }
             }

     }*/


    private fun confirmOrderNotifyMethod(reDispatchOTPModel: ReDispatchOTPModel) {
        orderDetailsViewModel!!.confirmOrderNotifyApi(reDispatchOTPModel)
            .observe(this@OrderDetailReturnActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { notifyReDispatchReAttempt ->
                                customerWiseOrderListApi()
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(this)
                        }
                    }
                }
            }


    }

    private fun updateViews(
        flexboxLayout: FlexboxLayout?, ratingList: ArrayList<String>, et: EditText?
    ) {
        flexboxLayout!!.removeAllViews()
        if (ratingList.size > 0) {
            val viewList: MutableList<TextView> = ArrayList()
            for (i in ratingList.indices) {
                val params = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(15, 10, 15, 10)
                val textView = TextView(this)
                textView.id = i
                textView.layoutParams = params
                textView.background = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.rectangle_grey_new
                )
                textView.setTextColor(this.resources.getColor(R.color.Darkgrey))
                textView.setPadding(40, 20, 40, 20)
                textView.text = "" + ratingList[i]
                viewList.add(textView)
                textView.setOnClickListener { view: View ->
                    reason = textView.text.toString()
                    if ((reason == "Other")) {
                        et!!.visibility = View.VISIBLE
                    } else {
                        et!!.visibility = View.GONE
                    }
                    println("Reason - $reason")
                    for (view1: TextView in viewList) {
                        view1.background = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.rectangle_grey_new
                        )
                        view1.setTextColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.Darkgrey
                            )
                        )
                    }
                    view.background = ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.rectangle_blue
                    )
                    textView.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.blue_dialog
                        )
                    )
                }
                flexboxLayout.addView(textView)
            }
        }
    }

    private fun updateViews(
        flexboxLayout: FlexboxLayout?,
        ratingList: ArrayList<String>,
        et: EditText?,
        llPlayVideo: LinearLayout?,
        llRetakeVideo: LinearLayout?
    ) {
        flexboxLayout?.removeAllViews()
        if (ratingList.isNotEmpty()) {
            val viewList: MutableList<TextView> = ArrayList()
            for (i in ratingList.indices) {
                val params = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(15, 10, 15, 10)
                }
                val textView = TextView(this).apply {
                    id = i
                    layoutParams = params
                    background = ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.rectangle_grey_new
                    )
                    setTextColor(this@OrderDetailReturnActivity.resources.getColor(R.color.Darkgrey))
                    setPadding(40, 20, 40, 20)
                    text = ratingList[i]
                    setOnClickListener { view ->
                        reason = text.toString()
                        et?.visibility = if (reason == "Other") View.VISIBLE else View.GONE
                        println("Reason - $reason")
                        if (reason == "दुकान बंद है" || reason == "कस्टमर उपलब्ध नहीं है" || reason == "वाहन ख़राब हो गया" || reason == "बारिश हो रही है /ट्रैफिक जाम" || reason == "वाहन में जगह नहीं है") {
                            reatteptVideoRequired = true;
                        } else {
                            reatteptVideoRequired = false;
                        }
                        if (reatteptVideoRequired) {
                            redispatchDialog?.dismiss()
                            dispatchTakeVideoIntent()
                        }
                        viewList.forEach { view1 ->
                            view1.background = ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.rectangle_grey_new
                            )
                            view1.setTextColor(
                                ContextCompat.getColor(applicationContext, R.color.Darkgrey)
                            )
                        }
                        view.background = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.rectangle_blue
                        )
                        setTextColor(
                            ContextCompat.getColor(applicationContext, R.color.blue_dialog)
                        )

                        if (reatteptVideoRequired) {
                            llPlayVideo?.visibility = View.VISIBLE
                            llRetakeVideo?.visibility = View.VISIBLE
                        } else {
                            llPlayVideo?.visibility = View.GONE
                            llRetakeVideo?.visibility = View.GONE
                        }
                    }
                }
                Log.e("TAG", "updateViews: 00000 $reason ||| ${ratingList[i]}")
                if (reason == ratingList[i]) {
                    Log.e("TAG", "updateViews: 11111111")
                    textView.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.rectangle_blue
                        )
                    )
                    textView.setTextColor(
                        ContextCompat.getColor(applicationContext, R.color.blue_dialog)
                    )
                }
                viewList.add(textView)
                flexboxLayout!!.addView(textView)
            }
        }
    }


    private fun showOtpDialog(
        otpTimer: Long,
        tripPlannerConfirmedDetailId: Int,
        tripPlannerConfirmedOrderId: Int
    ) {
        ProgressDialog.getInstance().dismiss()
        timerTime = otpTimer * 1000
        otpDialog = BottomSheetDialog(this)
        otpDialog!!.setContentView(R.layout.dialog_otp_verification)
        otpDialog!!.setCanceledOnTouchOutside(true)
        val otpEditBox1 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box1)
        val otpEditBox2 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box2)
        val otpEditBox3 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box3)
        val otpEditBox4 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box4)
        val tvHead = otpDialog!!.findViewById<TextView>(R.id.tvHead)
        val crossIV = otpDialog!!.findViewById<ImageView>(R.id.ivCross)
        tvHead!!.text = "Verification Code\nOrder Id - $orderIdInterface"
        btnResendOtp = otpDialog!!.findViewById(R.id.btnResendOtp)
        val btnVerifyOtp = otpDialog!!.findViewById<Button>(R.id.btnVerifyOtp)
        tvTimerOTP = otpDialog!!.findViewById(R.id.tvTimer)
        btnResendOtp!!.isEnabled = false
        btnResendOtp!!.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.gray
            )
        )
        timer(timerTime, tvTimerOTP, btnResendOtp, true)
        val edit = arrayOf(otpEditBox1, otpEditBox2, otpEditBox3, otpEditBox4)
        otpEditBox1!!.addTextChangedListener(GenericTextWatcher(otpEditBox1, edit))
        otpEditBox2!!.addTextChangedListener(GenericTextWatcher(otpEditBox2, edit))
        otpEditBox3!!.addTextChangedListener(GenericTextWatcher(otpEditBox3, edit))
        otpEditBox4!!.addTextChangedListener(GenericTextWatcher(otpEditBox4, edit))

        crossIV!!.setOnClickListener {
            otpDialog!!.dismiss()
            getRemoveCurrentSatusApi(tripPlannerConfirmedDetailId, tripPlannerConfirmedOrderId)
        }
        btnResendOtp!!.setOnClickListener {
            generateOrderOTP(
                orderIdInterface,
                selectedStatus,
                customerLatLng!!.latitude,
                customerLatLng!!.longitude
            )
        }
        btnVerifyOtp!!.setOnClickListener {
            genrateOtp = (otpEditBox1.text.toString() +
                    otpEditBox2.text.toString() +
                    otpEditBox3.text.toString() +
                    otpEditBox4.text.toString())
            if (genrateOtp!!.length < 4) {
                Utils.setToast(this@OrderDetailReturnActivity, "Enter Valid OTP")
            } else {
                getOTPNotifyReDispatchReAttemptMethod(
                    genrateOtp!!,
                    orderIdInterface,
                    selectedStatus
                )
            }
        }
        otpDialog!!.show()
        otpDialog!!.setOnDismissListener {
            try {
                customerWiseOrderListApi()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun generateOrderOTP(
        orderIdInterface: Int,
        selectedStatus: String?,
        latitude: Double,
        longitude: Double
    ) {
        orderDetailsViewModel!!.generateOrderOTPApi(
            orderIdInterface,
            selectedStatus,
            latitude,
            longitude,
            ""
        )
            .observe(this@OrderDetailReturnActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { generateOrderOTP ->
                                generateOrderOtpResponce()
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(this)
                        }
                    }
                }
            }
    }

    private fun generateOrderOtpResponce() {
        try {
            btnResendOtp!!.isEnabled = false
            btnResendOtp!!.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.gray
                )
            )
            timerTime = 60000
            timer(timerTime, tvTimerOTP, btnResendOtp, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getOTPNotifyReDispatchReAttemptMethod(
        otp: String,
        orderIdInterface: Int,
        selectedStatus: String?
    ) {

        orderDetailsViewModel!!.getOTPNotifyReDispatchReAttemptAPi(
            otp,
            orderIdInterface,
            selectedStatus
        )
            .observe(this@OrderDetailReturnActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { oTPNotifyReDispatchReAttempt ->
                                otpNotifyReDispatchReAttempt(oTPNotifyReDispatchReAttempt)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(this)
                        }
                    }
                }
            }
    }

    private fun otpNotifyReDispatchReAttempt(oTPNotifyReDispatchReAttempt: JsonObject) {
        val message = oTPNotifyReDispatchReAttempt.asJsonObject["Message"].asString
        val status = oTPNotifyReDispatchReAttempt.asJsonObject["Status"].asBoolean
        if (status) {
            try {
                otpDialog!!.dismiss()
                val reDispatchOTPModel = ReDispatchOTPModel(
                    genrateOtp,
                    orderIdInterface,
                    tripPlannerConfirmedDetailId.toLong(),
                    SharePrefs.getInstance(this).getString(SharePrefs.MOBILE),
                    selectedStatus,
                    reason,
                    customerLatLng!!.latitude,
                    customerLatLng!!.longitude,
                    SharePrefs.getInstance(this).getString(SharePrefs.SELECTED_DATE),
                    SharePrefs.getInstance(this).getInt(SharePrefs.PEOPLE_ID),
                    isReattempt, true
                )
                confirmOrderNotifyMethod(reDispatchOTPModel)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            ProgressDialog.getInstance().dismiss()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun confirmationDialog(
        title: String,
        customerOrderInfoEntity: TripOrderStatusUpdateModel.CustomerorderinfoEntity,
        stringArrayList: ArrayList<String>
    ) {
        val dialogConfirmationBinding = DataBindingUtil.inflate<DialogConfirmationBinding>(
            layoutInflater, R.layout.dialog_confirmation, null, false
        )
        val dialog = Dialog(this, R.style.CustomDialog)
        dialog.setContentView(dialogConfirmationBinding.root)
        dialog.setCancelable(true)
        dialogConfirmationBinding.tvTitleDesc.text = "Do you want to $title this order?"
        dialogConfirmationBinding.btNo.setOnClickListener { dialog.dismiss() }
        dialogConfirmationBinding.btYes.setOnClickListener {
            dialog.dismiss()
            if (title.equals("Cancel", ignoreCase = true)) {
                reason = ""
                showCancelOrderDialog(
                    "Cancellation Reason",
                    customerOrderInfoEntity.orderid,
                    stringArrayList,
                    false, customerOrderInfoEntity
                )
            } else if (title.equals("Re Attempt", ignoreCase = true)) {
                isReattempt = true
                selectedStatus = "Delivery Redispatch"
                showRedispatchDialog(customerOrderInfoEntity.orderid, stringArrayList, true, "")


            } /*else if (title.equals("Re Dispatch", ignoreCase = true)) {
                selectedStatus = "Re Attempt"
                showRedispatchDialog(customerOrderInfoEntity.orderid, stringArrayList)
            }*/
        }
        dialog.show()
    }

    private fun getUnloadItemDataApi(model: OrderDetailsRequestModel) {
        orderDetailsViewModel!!.getUnloadItemDetailsApi(model)
            .observe(this@OrderDetailReturnActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { unloadItemDetails ->
                                unloadItemDetail(unloadItemDetails)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(this)
                        }
                    }
                }
            }
    }

    private fun unloadItemDetail(unloadItemDetails: JsonObject) {
        val jsonObject = JSONObject(unloadItemDetails.toString())
        if (jsonObject.getBoolean("Status")) {
            startActivity(
                Intent(this, UnloadItemReturnActivity::class.java).putExtra(
                    Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
                    tripPlannerConfirmedDetailId
                ).putExtra("time", time)
            )
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btReturnItem -> {
                val orderIDList: MutableList<Int> = ArrayList()
                var i = 0
                while (i < tripOrderStatusUpdateModel!!.customerorderinfo!!.size) {
                    if (tripOrderStatusUpdateModel!!.customerorderinfo!![i].isBoolWorkingStatus && tripOrderStatusUpdateModel!!.customerorderinfo!![i].status == "Shipped") {
                        orderIDList.add(tripOrderStatusUpdateModel!!.customerorderinfo!![i].orderid)
                    }
                    i++
                }
                val model = OrderDetailsRequestModel(tripPlannerConfirmedDetailId, orderIDList)
                getUnloadItemDataApi(model)
            }

            R.id.ll_skip -> skipBottomBar()
        }
    }

    private fun skipBottomBar() {
        val bottomSkipLayoutBinding = DataBindingUtil.inflate<BottomSkipLayoutBinding>(
            layoutInflater, R.layout.bottom_skip_layout, null, false
        )
        val bottomSkip = Dialog(this, R.style.CustomDialog)
        bottomSkip.setContentView(bottomSkipLayoutBinding.root)
        bottomSkip.setCancelable(true)
        bottomSkipLayoutBinding.shopName.text = tripOrderStatusUpdateModel!!.customerinfo!!.shopname
        bottomSkipLayoutBinding.btNo.setOnClickListener { bottomSkip.dismiss() }
        bottomSkipLayoutBinding.btYes.setOnClickListener {
            bottomSkip.dismiss()
            callSkipAllApi(tripPlannerConfirmedDetailId)
        }
        bottomSkip.show()
    }

    private fun callSkipAllApi(tripPlannerConfirmedDetailId: Int) {
        orderDetailsViewModel!!.callSkipAllAPI(tripPlannerConfirmedDetailId)
            .observe(this@OrderDetailReturnActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { skipAllResponce ->
                                skipAllData(skipAllResponce)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(this)
                        }
                    }
                }
            }
    }

    private fun skipAllData(skipAllResponce: JsonObject) {
        try {
            val `object` = JSONObject("" + skipAllResponce)
            val status = `object`.getBoolean("Status")
            Utils.setToast(this, `object`.getString("Message"))
            if (status) {
                cancelTimer()
                startActivity(Intent(this, MainActivity::class.java))
                Utils.rightTransaction(this)
                finish()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun locationOn() {}
    override fun currentLocation(location: Location) {
        easyWayLocation!!.endUpdates()
        customerLatLng = LatLng(location.latitude, location.longitude)

    }

    override fun locationCancelled() {}
    fun timer(time: Long, timer: TextView?, btResend: Button?, isChangeColor: Boolean) {
        cancelTimer()
        if (TextUtils.isNullOrEmpty(salesPersonName)) {
            timer!!.visibility = View.VISIBLE
            btResend!!.visibility = View.VISIBLE
        } else {
            timer!!.visibility = View.VISIBLE
            btResend!!.visibility = View.GONE
        }
        cTimer = object : CountDownTimer(time, 100) {
            override fun onTick(mills: Long) {
                if (mills >= 60000) {
                    timer.text = ("" +
                            (TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(mills)
                            )) + ":" +
                            (TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(mills)
                            )) + " Min")
                } else {
                    timer.text =
                        ("" + (TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(mills)
                        )) + " Sec")
                }
                SharePrefs.getInstance(this@OrderDetailReturnActivity)
                    .putLong(SharePrefs.REMAININGTIME, mills)
            }

            override fun onFinish() {
                btResend.isEnabled = true
                if (isChangeColor) {
                    btResend.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.orange_dark
                        )
                    )
                }
                timer.visibility = View.GONE
                btResend.visibility = View.VISIBLE
            }
        }
        cTimer!!.start()
    }

    private fun cancelTimer() {
        if (cTimer != null) cTimer!!.cancel()
    }

    private fun showDatePicker(dateTextView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog: DatePickerDialog = @RequiresApi(Build.VERSION_CODES.O)
        object : DatePickerDialog(
            this,
            OnDateSetListener { view: DatePicker?, yearSelected: Int, monthOfYear: Int, dayOfMonthSelected: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate[yearSelected, monthOfYear] = dayOfMonthSelected
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = outputFormat.format(selectedDate.time)
                Log.e("TAG", "showDatePicker: $formattedDate")
                if (rdDateList.contains(formattedDate)) {
                    try {
                        //dateTextView.text = StringBuilder().append(dayOfMonthSelected).append("/").append(month + 1).append("/").append(year).append(" ")
                        //  dateTextView.text = formattedDate

                        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val outputFormat1 = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        val selectedDate1: LocalDate = LocalDate.parse(formattedDate, inputFormat)
                        val convertedDateStr = outputFormat1.format(selectedDate1)
                        dateTextView.text = convertedDateStr

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    // Date is not valid, show an error message or handle accordingly
                }
            },
            year,
            month,
            dayOfMonth
        ) {
            override fun onDateChanged(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
                val selectedDate = Calendar.getInstance()
                selectedDate[year, month] = dayOfMonth
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = outputFormat.format(selectedDate.time)
                if (!rdDateList.contains(formattedDate)) {
                    val previousValidDate = getPreviousValidDate(selectedDate)
                    view.updateDate(
                        previousValidDate[Calendar.YEAR],
                        previousValidDate[Calendar.MONTH],
                        previousValidDate[Calendar.DAY_OF_MONTH]
                    )
                }
            }
        }

        datePickerDialog.datePicker.minDate = getMinDate().timeInMillis
        datePickerDialog.datePicker.maxDate = getMaxDate().timeInMillis
        datePickerDialog.show()
    }

    private fun getMinDate(): Calendar {
        val minDateStr = rdDateList[0]
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val minDate: Date = dateFormat.parse(minDateStr)!!
            val calendar = Calendar.getInstance()
            calendar.time = minDate
            return calendar
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Calendar.getInstance()
    }

    private fun getMaxDate(): Calendar {
        val maxDateStr = rdDateList[rdDateList.size - 1]
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val maxDate: Date = dateFormat.parse(maxDateStr)!!
            val calendar = Calendar.getInstance()
            calendar.time = maxDate
            return calendar
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Calendar.getInstance()
    }

    private fun choosePersonDilog(
        title: String,
        customerOrderInfoEntity: TripOrderStatusUpdateModel.CustomerorderinfoEntity,
        stringArrayList: ArrayList<String>
    ) {
        ProgressDialog.getInstance().dismiss()
        choosePersonDilog = BottomSheetDialog(this)
        choosePersonDilog!!.setContentView(R.layout.dialog_choose_person)
        choosePersonDilog!!.setCanceledOnTouchOutside(false)
        val crossIV = choosePersonDilog!!.findViewById<ImageView>(R.id.ivCross)
        var btnSalesPerson =
            choosePersonDilog!!.findViewById<Button>(R.id.btnSalesPerson)

        val btnDeliveryBoy =
            choosePersonDilog!!.findViewById<Button>(R.id.btnDeliveryBoy)

        crossIV!!.setOnClickListener {
            choosePersonDilog!!.dismiss()
        }
        choosePersonDilog!!.show()


        btnSalesPerson!!.setOnClickListener {
            /* val generateOTPForReturnOrder = GenerateOTPForReturnOrder(orderIDList, status)
             unloadItemViewModel!!.generateOTPForReturnOrder(generateOTPForReturnOrder)*/
            choosePersonDilog!!.dismiss()
        }

        btnDeliveryBoy!!.setOnClickListener {
            choosePersonDilog!!.dismiss()
            /* timerMethod()
             SharePrefs.getInstance(this@OrderDetailReturnActivity)
                 .putString(SharePrefs.SELECTED_STATUS, selectedStatus)
             showRedispatchDialog(customerOrderInfoEntity.orderid, stringArrayList)*/

        }

        choosePersonDilog!!.setOnDismissListener {
            try {

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getPreviousValidDate(currentDate: Calendar): Calendar {
        val previousValidDate = currentDate.clone() as Calendar
        while (!rdDateList.contains(formatDate(previousValidDate)!!)) {
            previousValidDate.add(Calendar.DAY_OF_MONTH, -1)
            Utils.setToast(
                applicationContext,
                "वेयरहाउस/ दुकान की छुट्टी है कृपया कोई अन्य तिथि चुनें"
            )
        }
        return previousValidDate
    }

    private fun formatDate(date: Calendar): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(date.time)
    }

    private fun dispatchTakeVideoIntent() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permissions if not granted
                requestPermissions()
            } else {
                // Permissions are granted, proceed with video capture
                startVideoCapture()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startVideoCapture()
            } else {
                requestPermissions()
            }
        }
    }

    private fun startVideoCapture() {
        val takeVideoIntent = Intent(applicationContext, VideoRecorderExample::class.java)
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
    }

    private fun openAppSettings() {
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }


    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                ),
                REQUEST_VIDEO_CAPTURE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            if (data == null) {
                Log.d("TAG", "Data returned is null")
                return
            }
            videoPath = data.getStringExtra("path")!!.toUri()
            println("path $videoPath")
            if (videoPath != null) {
                val file = File(videoPath.toString())
                val length = file.length() / 1024
                Log.e("TAG", "onActivityResult Video Size: $length KB")
                val requestFile = RequestBody.create("video/*".toMediaTypeOrNull(), file)
                val body: MultipartBody.Part = createFormData("file", file.name, requestFile)
                orderDetailsViewModel?.postVideoBackgroundData(
                    body,
                    SharePrefs.getInstance(this@OrderDetailReturnActivity)
                        .getString(SharePrefs.VIDEOUPLOADBASEURL)
                )
                    ?.observe(this@OrderDetailReturnActivity) {
                        it?.let { resource ->
                            when (resource.status) {
                                Status.SUCCESS -> {
                                    ProgressDialog.getInstance().dismiss()
                                    resource.data?.let { response ->
                                        println(response)
                                        SharePrefs.getInstance(this)
                                            .putString(SharePrefs.VIDEO_URL, response)

                                        showRedispatchDialog(
                                            orderIdInterface,
                                            reattemptReasonList,
                                            true,
                                            response
                                        )

                                        deleteVideoFile()
                                    }
                                }

                                Status.ERROR -> {
                                    ProgressDialog.getInstance().dismiss()
                                    Log.e("TAG", "onActivityResult: " + it.message)
                                    Utils.setToast(applicationContext, it.message)
                                }

                                Status.LOADING -> {
                                    ProgressDialog.getInstance().show(this)
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun deleteVideoFile() {
        try {
            val file = File(videoPath.toString())
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun videoDialog(videoURL: String) {
        val dialogVideoUploadBinding = DataBindingUtil.inflate<DialogVideoUploadBinding>(
            layoutInflater, R.layout.dialog_video_upload, null, false
        )
        val dialog = Dialog(this@OrderDetailReturnActivity, R.style.CustomDialog)
        dialog.setContentView(dialogVideoUploadBinding.root)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        initializePlayer(dialogVideoUploadBinding, videoURL)
        dialogVideoUploadBinding.ivCross.setOnClickListener {
            try {
                dialog.dismiss()
                releasePlayer()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        dialog.setOnDismissListener {
            try {
                dialog.dismiss()
                releasePlayer()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dialog.show()
    }

    private fun showRetakeVideoAlert(cancelDialog: BottomSheetDialog) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Retake video")
        builder.setMessage("Are you sure, you want to replace video?")

        builder.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()

            dispatchTakeVideoIntent()

            if (cancelDialog != null) {
                cancelDialog.dismiss()
            }
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun initializePlayer(
        dialogVideoUploadBinding: DialogVideoUploadBinding,
        videoURL: String
    ) {
        player = ExoPlayer.Builder(this) // <- context
            .build()

        val mediaItem = MediaItem.Builder()
            /*http://192.168.1.50:7011/Uploads/Videos/62c01675-6045-4fd6-98a2-a74b4a0250c7.mp4*/
            .setUri(videoURL)
            .setMimeType(MimeTypes.APPLICATION_MP4)
            .build()
        dialogVideoUploadBinding.playerView.useController = true
        val mediaSource =
            ProgressiveMediaSource.Factory(DefaultDataSource.Factory(this))
                .createMediaSource(mediaItem)

        // Finally assign this media source to the player
        player!!.apply {
            setMediaSource(mediaSource)
            playWhenReady = true
            seekTo(0, 0L)
            prepare()
        }.also {
            dialogVideoUploadBinding.playerView.player = it
        }

        // Add listener to ensure player readiness and get video duration
        player?.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_READY) {
                }
            }
        })

        player!!.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                if (state == ExoPlayer.STATE_ENDED) {
                    var duration = player!!.duration
                    var position = player!!.currentPosition
                    var percentageWatched = (100 * position / duration)
                    println("Player State: ${state} duration: ${duration} position: ${position} Watched: ${percentageWatched}%")
                    if (percentageWatched.toInt() == 100) {
                        dialogVideoUploadBinding.playerView.useController = true
                    }
                    dialogVideoUploadBinding.progress.visibility = View.GONE
                } else if (state == Player.STATE_BUFFERING) {
                    dialogVideoUploadBinding.progress.visibility = View.VISIBLE
                } else {
                    // Hide loader
                    dialogVideoUploadBinding.progress.visibility = View.GONE

                }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Log.e("ExoPlayerError", "Error during playback", error)
                Toast.makeText(
                    this@OrderDetailReturnActivity,
                    "Playback error occurred",
                    Toast.LENGTH_SHORT
                )
                    .show()

            }
        })
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
        }
    }

}