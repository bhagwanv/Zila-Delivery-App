package com.sk.ziladelivery.ui.views.main

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
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
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
import com.sk.ziladelivery.data.model.CancelOrderModel
import com.sk.ziladelivery.data.model.CancelOrderSendOtpModel
import com.sk.ziladelivery.data.model.GenerateOTPofSalesPersonforReattemptRequestModel
import com.sk.ziladelivery.data.model.NotifyDeliveryActionRequestModel
import com.sk.ziladelivery.data.model.NotifyReDispatchReAttemptModel
import com.sk.ziladelivery.data.model.OrderDetailsRequestModel
import com.sk.ziladelivery.data.model.ReDispatchModel
import com.sk.ziladelivery.data.model.ReDispatchOTPModel
import com.sk.ziladelivery.data.model.TripOrderStatusUpdateModel
import com.sk.ziladelivery.databinding.ActivityOrderDetailsBinding
import com.sk.ziladelivery.databinding.BottomSkipLayoutBinding
import com.sk.ziladelivery.databinding.DialogConfirmationBinding
import com.sk.ziladelivery.databinding.DialogVideoUploadBinding
import com.sk.ziladelivery.listener.OrderDetailInterface
import com.sk.ziladelivery.ui.views.VideoRecorderExample
import com.sk.ziladelivery.ui.views.adapter.OrderDetailsAdapter
import com.sk.ziladelivery.ui.views.viewmodels.OrderDetailsViewModel
import com.sk.ziladelivery.utilities.Constant
import com.sk.ziladelivery.utilities.DateUtils
import com.sk.ziladelivery.utilities.EasyWayLocation
import com.sk.ziladelivery.utilities.GenericTextWatcher
import com.sk.ziladelivery.utilities.Listener
import com.sk.ziladelivery.utilities.ProgressDialog
import com.sk.ziladelivery.utilities.RxBus
import com.sk.ziladelivery.utilities.SharePrefs
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.TextUtils
import com.sk.ziladelivery.utilities.Utils
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
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class OrderDetailActivity : AppCompatActivity(), OrderDetailInterface, View.OnClickListener, Listener {
    private var mBinding: ActivityOrderDetailsBinding? = null
    private var orderDetailsViewModel: OrderDetailsViewModel? = null
    private var tripOrderStatusUpdateModel: TripOrderStatusUpdateModel? = null
    private var customerorderinfoEntityMaster: TripOrderStatusUpdateModel.CustomerorderinfoEntity? = null
    private var grossAmount = 0.0
    private var remainingAmount = 0.0
    private var shippedAmount = 0.0
    private var deliveredAmount = 0.0
    private var tripPlannerConfirmedDetailId = 0
    private var TripPlannerConfirmedOrderId = 0
    private var isGeneralOrder = false
    private var notifyDeliveryCancelled = 0
    private var salesPersonName: String? = null
    private var salesPersonMobile: String? = null
    private var selectedDate: String? = null
    private var selectedStatus: String? = null
    private var genrateOtp: String? = null
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
    private var choosePersonDilog: BottomSheetDialog? = null
    private var selectPersonTypeFlag = true
    private var redispatchDialog: BottomSheetDialog? = null

    private var rdDateList: List<String> = emptyList()
    private var orderIDList: MutableList<Int> = ArrayList()

    // video
    private val REQUEST_VIDEO_CAPTURE = 101
    private val PERMISSION_REQUEST_CODE = 100
    private var player: ExoPlayer? = null
    private var head1: String = ""
    private var orderId1: Int = 0
    private var stringArrayList1: ArrayList<String> = ArrayList()
    private var isTimed1: Boolean = false
    private lateinit var customerOrderInfoEntity1: TripOrderStatusUpdateModel.CustomerorderinfoEntity
    private var IsReattemt1: Boolean = false
    private var title1: String = ""
    private var videoPath: Uri? = null
    private var reatteptVideoRequired: Boolean = false

    val reattemptReasonList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        orderDetailsViewModel = ViewModelProvider(
            viewModelStore,
            OrderDetailsFactory(ApiHelper(RestClient.getInstance().service))
        )[OrderDetailsViewModel::class.java]

        dataFromIntent()
        setActionBarConfiguration()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initView()
        if (!Utils.checkInternetConnection(applicationContext)) {
            Utils.setToast(applicationContext, resources.getString(R.string.network_error))
        } else {
            customerWiseOrderListApi()
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
                orderDetailsViewModel?.postVideoBackgroundData(body, SharePrefs.getInstance(this@OrderDetailActivity).getString(SharePrefs.VIDEOUPLOADBASEURL))
                    ?.observe(this@OrderDetailActivity) {
                        it?.let { resource ->
                            when (resource.status) {
                                Status.SUCCESS -> {
                                    ProgressDialog.getInstance().dismiss()
                                    resource.data?.let { response ->
                                        println(response)
                                        SharePrefs.getInstance(this).putString(SharePrefs.VIDEO_URL, response)
                                        if (title1.equals("Re Dispatch", ignoreCase = true)) {
                                            showRedispatchDialog(response)
                                        } else if (title1.equals("Cancel", ignoreCase = true)) {
                                            showCancelOrderDialog(response)
                                        } else {
                                            showRedispatchDialog(orderIdInterface, reattemptReasonList, true, response)
                                        }
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

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bt_unloaditem -> {
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

    override fun onDestroy() {
        super.onDestroy()
        cancelTimer()
        releasePlayer()
    }


    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (requestCode == PERMISSION_REQUEST_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // Permissions granted, start video capture
                    startVideoCapture()
                } else {
                    // Permissions denied, inform the user
                    Toast.makeText(
                        this,
                        "Permissions are required to record videos.",
                        Toast.LENGTH_SHORT
                    ).show()

                    openAppSettings()
                }
            }
        } else {
            if (requestCode == REQUEST_VIDEO_CAPTURE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with video capture
                    startVideoCapture()
                } else {
                    Toast.makeText(
                        this,
                        "Camera Permission are required to record videos.",
                        Toast.LENGTH_SHORT
                    ).show()

                    openAppSettings()
                }
            }
        }
    }


    override fun checkBoxClicked(isChecked: Boolean, postion: Int) {
        if (isChecked) {
            tripOrderStatusUpdateModel!!.customerorderinfo!![postion].isBoolWorkingStatus = true
            mBinding!!.btUnloaditem.background = ContextCompat.getDrawable(
                applicationContext,
                R.drawable.button_bg_blue
            )
            mBinding!!.btUnloaditem.isClickable = true
            mBinding!!.btUnloaditem.isEnabled = true
            grossAmount += tripOrderStatusUpdateModel!!.customerorderinfo!![postion].grossamount
            remainingAmount += tripOrderStatusUpdateModel!!.customerorderinfo!![postion].remaningAmount
        } else {
            grossAmount -= tripOrderStatusUpdateModel!!.customerorderinfo!![postion].grossamount
            remainingAmount -= tripOrderStatusUpdateModel!!.customerorderinfo!![postion].remaningAmount
            tripOrderStatusUpdateModel!!.customerorderinfo!![postion].isBoolWorkingStatus = false
            if (remainingAmount == 0.0) {
                mBinding!!.btUnloaditem.background = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.button_bg_drawable
                )
                mBinding!!.btUnloaditem.isClickable = false
                mBinding!!.btUnloaditem.isEnabled = false
            }
        }
        mBinding!!.tvTotalPaybleAmount.text = remainingAmount.toString()
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
        SharePrefs.getInstance(applicationContext).putInt(SharePrefs.ORDERID, orderId)

        if (buttonText == "Cancel Order") {
            selectPersonTypeFlag = true
            isReattempt = false
            selectedStatus = "Delivery Canceled"
            timerMethod()
            SharePrefs.getInstance(this@OrderDetailActivity)
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
                //reason = customerorderinfoEntity.reason!!
                head1 = "Approval Sent"
                orderId1 = customerorderinfoEntity.orderid
                stringArrayList1 = stringArrayList
                isTimed1 = true
                customerOrderInfoEntity1 = customerorderinfoEntity
                if (customerorderinfoEntityMaster!!.orderType == "Non-Sellable Order" || customerorderinfoEntityMaster!!.orderType == "Damage Order") {
                    showCancelOrderDialog("")
                } else {
                    showCancelOrderDialog(
                        SharePrefs.getInstance(this@OrderDetailActivity).getString(SharePrefs.VIDEO_URL)
                    )
                }

                /* showCancelOrderDialog(
                     "Approval Sent",
                     customerorderinfoEntity.orderid,
                     stringArrayList,
                     true,
                     customerorderinfoEntity
                 )*/

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
            val stringArrayList = ArrayList<String>()
           /* stringArrayList.add("Shop not attempt")
            stringArrayList.add("Customer not available")
            stringArrayList.add("Vehicle not available")
            stringArrayList.add("Vehicle space issue")
            stringArrayList.add("Shop not found")
            stringArrayList.add("Natural Calamities/Rain")
            stringArrayList.add("Vehicle break down")
            stringArrayList.add("Shop is closed")
            stringArrayList.add("Planner/Planning issue")
            stringArrayList.add("Other")*/
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
            if (customerorderinfoEntity.workingStatus == 8 && customerorderinfoEntity.isOTPSent) {
                // timerTime = customerorderinfoEntity.remaningTimeInMins * 1000
                timerTime = customerorderinfoEntity.otpSentRemaningTimeInSec.toLong()
                showOTPofSalesPersonDilog(
                    timerTime,
                    "", customerorderinfoEntity.tripPlannerConfirmedDetailId,
                    customerorderinfoEntity.tripPlannerConfirmedOrderId
                )
            } else {
                confirmationDialog("Re Attempt", customerorderinfoEntity, reattemptReasonList)
            }
        } else if (buttonText == "Re Dispatch") {
            isReattempt = false
            selectedStatus = "Delivery Redispatch"
            timerMethod()
            SharePrefs.getInstance(this@OrderDetailActivity)
                .putString(SharePrefs.SELECTED_STATUS, selectedStatus)
            val stringArrayList = ArrayList<String>()
            stringArrayList.add("Concern Customer Unavailable")
            stringArrayList.add("Customer Busy")
            stringArrayList.add("Customer wants next day delivery")
            stringArrayList.add("Don't Need")
            stringArrayList.add("Payment issue")
            stringArrayList.add("Shop close")
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

    override fun locationOn() {}

    override fun currentLocation(location: Location) {
        easyWayLocation!!.endUpdates()
        customerLatLng = LatLng(location.latitude, location.longitude)

    }

    override fun locationCancelled() {}


    private fun dataFromIntent() {
        tripPlannerConfirmedDetailId = intent.getIntExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, 0)
        isGeneralOrder = intent.getBooleanExtra("isGeneralOrder", false)
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
                                    Utils.setToast(this@OrderDetailActivity, "Accpect")
                                    showOtpDialog(
                                        60,
                                        tripPlannerConfirmedDetailId,
                                        TripPlannerConfirmedOrderId
                                    )
                                }
                            } else if (o.equals("Reject", ignoreCase = true)) {
                                Utils.setToast(this@OrderDetailActivity, "Reject")
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
        mBinding!!.btUnloaditem.setOnClickListener(this)
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
        orderDetailsViewModel!!.getCustomerWiseOrderList(
            tripPlannerConfirmedDetailId,
            false
        )
            .observe(this@OrderDetailActivity) {
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
            mBinding!!.tvStoreName.text = tripOrderStatusUpdateModel!!.customerinfo!!.shopname
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
                    mBinding!!.btUnloaditem.background = ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.button_bg_blue
                    )
                    mBinding!!.btUnloaditem.isClickable = true
                    mBinding!!.btUnloaditem.isEnabled = true
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
                val orderDetailsAdapter = OrderDetailsAdapter(
                    this,
                    tripOrderStatusUpdateModel!!.customerorderinfo!!,
                    this
                )
                mBinding!!.rvItemList.adapter = orderDetailsAdapter
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
        mBinding!!.tvOrderno.text = "Order Details"
        mBinding!!.ivBack.setOnClickListener {
            cancelTimer()
            onBackPressed()
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
        }
    }

    private fun showCancelOrderDialog(videoURL: String) {
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
        val llPlayVideo = cancelDialog!!.findViewById<LinearLayout>(R.id.llPlayVideo)
        val llRetakeVideo = cancelDialog!!.findViewById<LinearLayout>(R.id.llRetakeVideo)
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
                customerOrderInfoEntity1.tripPlannerConfirmedDetailId,
                customerOrderInfoEntity1.tripPlannerConfirmedOrderId
            )
        }
        imSalesCallPerson!!.setOnClickListener {
            if (!TextUtils.isNullOrEmpty(salesPersonMobile)) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91$salesPersonMobile"))
                startActivity(intent)
            } else {
                Utils.setToast(this@OrderDetailActivity, "Number Not Available")
            }
        }

        if (customerorderinfoEntityMaster!!.orderType == "Non-Sellable Order" || customerorderinfoEntityMaster!!.orderType == "Damage Order") {
            btnSendNotification!!.text = "Send otp"
        }
        if (head1.equals("Approval Sent", ignoreCase = true)) {
            fbCancelOrder!!.visibility = View.GONE
            ilReasonLayout!!.visibility = View.VISIBLE
            rLSalesDetails!!.visibility = View.VISIBLE
            tvCancelReason!!.text = reason
            tvHead!!.text = "$head1\nOrder Id - $orderId1"
        } else {
            fbCancelOrder!!.visibility = View.VISIBLE
            ilReasonLayout!!.visibility = View.GONE
            rLSalesDetails!!.visibility = View.GONE
            tvHead!!.text = "Order Id - $orderId1\n$head1"
        }
        updateViews(fbCancelOrder, stringArrayList1, etEnterComment)
        btnCancelOrderSendOtp!!.setOnClickListener {
            var vUrl = SharePrefs.getInstance(this@OrderDetailActivity)
                .getString(SharePrefs.VIDEO_URL)
            if (customerorderinfoEntityMaster!!.orderType == "Non-Sellable Order" || customerorderinfoEntityMaster!!.orderType == "Damage Order") {
                vUrl = ""
            }
            generateOrderOTP1(
                orderIdInterface,
                selectedStatus,
                customerLatLng!!.latitude,
                customerLatLng!!.longitude,
                vUrl
            )
            cancelDialog!!.dismiss()
        }
        if (isTimed1) {
            btnSendNotification.visibility = View.INVISIBLE
            tvCancelOrderTimer!!.visibility = View.VISIBLE
            rLSalesDetails!!.visibility = View.VISIBLE
            timer(timerTime, tvCancelOrderTimer, btnCancelOrderSendOtp, false)
        } else {
            btnSendNotification.visibility = View.VISIBLE
            tvCancelOrderTimer!!.visibility = View.INVISIBLE
        }
        btnSendNotification.setOnClickListener {
            if (reason == "Other") {
                if (etEnterComment?.text.toString().trim().isNotEmpty()) {
                    reason = etEnterComment?.text.toString().trim()
                } else {
                    Utils.setToast(applicationContext, "Please Select/Enter Reason")
                    return@setOnClickListener
                }
            } else if (reason.isNullOrEmpty()) {
                Utils.setToast(applicationContext, "Please Select/Enter Reason")
            } else {
                val model = CancelOrderModel(
                    tripPlannerConfirmedDetailId.toLong(), reason, orderId1,
                    customerLatLng!!.latitude, customerLatLng!!.longitude
                )
                cancelOrderNotifyUpdate(model, videoURL, false)
                btnSendNotification.visibility = View.INVISIBLE
                fbCancelOrder.visibility = View.GONE
                if (llPlayVideo != null) {
                    llPlayVideo.visibility = View.GONE
                }
                if (llRetakeVideo != null) {
                    llRetakeVideo.visibility = View.GONE
                }
            }

            if (customerorderinfoEntityMaster!!.orderType == "Non-Sellable Order" || customerorderinfoEntityMaster!!.orderType == "Damage Order") {
                cancelDialog!!.dismiss()
            } else {
            }
        }


        if (customerorderinfoEntityMaster!!.orderType != "Non-Sellable Order" && customerorderinfoEntityMaster!!.orderType != "Damage Order") {
            if (videoURL.isNotEmpty()) {
                if (!head1.equals("Approval Sent", ignoreCase = true)) {
                    if (llPlayVideo != null) {
                        llPlayVideo.visibility = View.VISIBLE
                    }

                    if (llRetakeVideo != null) {
                        llRetakeVideo.visibility = View.VISIBLE
                    }
                }
            } else {
                if (!head1.equals("Approval Sent", ignoreCase = true)) {
                    if (llPlayVideo != null) {
                        llPlayVideo.visibility = View.GONE
                    }
                    if (llRetakeVideo != null) {
                        llRetakeVideo.visibility = View.VISIBLE
                    }
                }
            }
        }

        llPlayVideo!!.setOnClickListener {
            videoDialog(videoURL)
        }

        llRetakeVideo!!.setOnClickListener {
            showRetakeVideoAlert(cancelDialog!!)
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
            .observe(this@OrderDetailActivity) {
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
            .observe(this@OrderDetailActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            // ProgressDialog.getInstance().dismiss()
                            mBinding!!.progress.visibility = View.GONE
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
                            // ProgressDialog.getInstance().show(this)
                            mBinding!!.progress.visibility = View.VISIBLE
                        }
                    }
                }
            }
    }

    private fun cancelOrderNotifyUpdate(model: CancelOrderModel) {
        orderDetailsViewModel!!.cancelOrderNotifyUpdateApi(model)
            .observe(this@OrderDetailActivity) {
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

    private fun cancelOrderNotifyUpdate(
        model: CancelOrderModel,
        videoURL: String,
        isAlreadyApprovalSent: Boolean
    ) {
        var data = NotifyDeliveryActionRequestModel(
            TripPlannerConfirmedDetailId = model.tripPlannerConfirmedDetailId,
            Reason = model.reason,
            OrderId = model.orderId,
            lat = model.lat,
            lg = model.lg,
            VideoUrl = videoURL,
            Action = "Delivery Canceled",
            NextRedispatchedDate = null
        )
        orderDetailsViewModel!!.notifyDeliveryAction(data)
            .observe(this@OrderDetailActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { cancelOrderNotifyUpdate ->
                                if (isAlreadyApprovalSent || customerorderinfoEntityMaster!!.orderType == "Non-Sellable Order" || customerorderinfoEntityMaster!!.orderType == "Damage Order") {
                                    showOtpDialog(
                                        60,
                                        tripPlannerConfirmedDetailId,
                                        TripPlannerConfirmedOrderId
                                    )
                                } else {
                                    cancelOrderNotify(cancelOrderNotifyUpdate)
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

    private fun videoDialog(videoURL: String) {
        val dialogVideoUploadBinding = DataBindingUtil.inflate<DialogVideoUploadBinding>(
            layoutInflater, R.layout.dialog_video_upload, null, false
        )
        val dialog = Dialog(this@OrderDetailActivity, R.style.CustomDialog)
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
                    this@OrderDetailActivity,
                    "Playback error occurred",
                    Toast.LENGTH_SHORT
                )
                    .show()

            }
        })
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

        orderDetailsViewModel!!.getHolidayOnRedispatch(orderId, IsReattemt)
            .observe(this@OrderDetailActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { holidayOnRedispatchlist ->
                                if (holidayOnRedispatchlist.size > 0) {
                                    rdDateList = DateUtils.filterDates(holidayOnRedispatchlist)
                                    // rdDateList = holidayOnRedispatchlist
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

        if (isReattempt) {
            tvHead!!.text = "Re-Attempt Reason"
        } else {
            tvHead!!.text = "Re-Dispatch Reason"
        }

        val tvSelectDate = redispatchDialog!!.findViewById<TextView>(R.id.tvSelectDate)
        val fbCancelOrder = redispatchDialog!!.findViewById<FlexboxLayout>(R.id.fbCancelOrder)
        val btnSendNotification = redispatchDialog!!.findViewById<Button>(R.id.btnSendNotification)
        val llPlayVideo = redispatchDialog!!.findViewById<LinearLayout>(R.id.llPlayVideo)
        val llRetakeVideo = redispatchDialog!!.findViewById<LinearLayout>(R.id.llRetakeVideo)
        updateViews(fbCancelOrder, stringArrayList, etEnterComment, llPlayVideo, llRetakeVideo)
        tvSelectDate!!.setOnClickListener {
            if (!rdDateList.isNullOrEmpty()) {
                showDatePicker(tvSelectDate)
            } else {
                Utils.setToast(this@OrderDetailActivity, "Holiday date not found")
            }
        }
        var vURL = videoURL
        btnSendNotification!!.setOnClickListener {
            if (reason == "Other") {
                if (etEnterComment?.text.toString().trim().isNotEmpty()) {
                    reason = etEnterComment?.text.toString().trim()
                } else {
                    Utils.setToast(applicationContext, "Please Select/Enter Reason")
                    return@setOnClickListener
                }
            } else if (reason.isNullOrEmpty()) {
                Utils.setToast(applicationContext, "Please Select/Enter Reason")
            } else if (tvSelectDate.text.toString().trim() == "") {
                Utils.setToast(this, "Please Select Date")
            } else {
                selectedDate = (tvSelectDate.text).toString().trim()
                SharePrefs.getInstance(this@OrderDetailActivity)
                    .putString(SharePrefs.SELECTED_DATE, selectedDate)
                SharePrefs.getInstance(this@OrderDetailActivity)
                    .putString(SharePrefs.REASON, reason)
                SharePrefs.getInstance(this@OrderDetailActivity)
                    .putBoolean(SharePrefs.ISREATTEMPT, isReattempt)

                if(!reatteptVideoRequired) {
                    vURL = "";
                }

                if (selectPersonTypeFlag) {
                    var vUrl = SharePrefs.getInstance(this@OrderDetailActivity).getString(SharePrefs.VIDEO_URL)
                    if (customerorderinfoEntityMaster!!.orderType == "Non-Sellable Order" || customerorderinfoEntityMaster!!.orderType == "Damage Order") {
                        vUrl = ""
                    }
                    val reDispatchModel = ReDispatchModel(
                        isReattempt,
                        reason,
                        orderId,
                        SharePrefs.getInstance(this).getString(SharePrefs.SELECTED_DATE),
                        customerLatLng!!.latitude,
                        customerLatLng!!.longitude,
                        tripPlannerConfirmedDetailId.toLong(),
                        vURL
                    )
                    Log.e("TAG", "showRedispatchDialog: 22222" );
                    getNotifyReDispatchReAttemptMethod(reDispatchModel)
                    redispatchDialog!!.dismiss()
                } else {
                    Log.e("TAG", "showRedispatchDialog: 11111" );
                    generateOTPofSalesPersonforReattempt(
                        orderIdInterface,
                        selectedStatus!!,
                        customerLatLng!!.latitude,
                        customerLatLng!!.longitude,
                        reason,
                        vURL
                    )
                }
                reason = ""

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

    private fun showRedispatchDialog(videoURL: String) {
        redispatchDialog = BottomSheetDialog(this)
        redispatchDialog!!.setContentView(R.layout.dialog_redispatch_order)
        redispatchDialog!!.setCanceledOnTouchOutside(false)
        val etEnterComment = redispatchDialog!!.findViewById<EditText>(R.id.etEnterComment)
        val tvHead = redispatchDialog!!.findViewById<TextView>(R.id.tvHead)

        orderDetailsViewModel!!.getHolidayOnRedispatch(orderId1, IsReattemt1)
            .observe(this@OrderDetailActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { holidayOnRedispatchlist ->
                                if (holidayOnRedispatchlist.size > 0) {
                                    rdDateList = DateUtils.filterDates(holidayOnRedispatchlist)
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

        val tvSelectDate = redispatchDialog!!.findViewById<TextView>(R.id.tvSelectDate)
        val fbCancelOrder = redispatchDialog!!.findViewById<FlexboxLayout>(R.id.fbCancelOrder)
        val btnSendNotification = redispatchDialog!!.findViewById<Button>(R.id.btnSendNotification)
        val llPlayVideo = redispatchDialog!!.findViewById<LinearLayout>(R.id.llPlayVideo)
        val llRetakeVideo = redispatchDialog!!.findViewById<LinearLayout>(R.id.llRetakeVideo)

        if (isReattempt) {
            tvHead!!.text = "Re-Attempt Reason"
            reason = ""
        } else {
            tvHead!!.text = "Re-Dispatch Reason"
            reason = ""
            if (customerorderinfoEntityMaster!!.orderType == "Non-Sellable Order" || customerorderinfoEntityMaster!!.orderType == "Damage Order") {
                btnSendNotification!!.text = "Send otp"
            }
        }

        updateViews(fbCancelOrder, stringArrayList1, etEnterComment)

        tvSelectDate!!.setOnClickListener {
            if (!rdDateList.isNullOrEmpty()) {
                showDatePicker(tvSelectDate)
            } else {
                Utils.setToast(this@OrderDetailActivity, "Holiday date not found")
            }
        }
        btnSendNotification!!.setOnClickListener {
            if (reason == "Other") {
                if (etEnterComment?.text.toString().trim().isNotEmpty()) {
                    reason = etEnterComment?.text.toString().trim()
                } else {
                    Utils.setToast(applicationContext, "Please Select/Enter Reason")
                    return@setOnClickListener
                }
            } else if (reason.isNullOrEmpty()) {
                Utils.setToast(applicationContext, "Please Select/Enter Reason")
            } else if (tvSelectDate.text.toString().trim() == "") {
                Utils.setToast(this, "Please Select Date")
            } else {
                selectedDate = (tvSelectDate.text).toString().trim()
                SharePrefs.getInstance(this@OrderDetailActivity)
                    .putString(SharePrefs.SELECTED_DATE, selectedDate)
                SharePrefs.getInstance(this@OrderDetailActivity)
                    .putString(SharePrefs.REASON, reason)
                SharePrefs.getInstance(this@OrderDetailActivity)
                    .putBoolean(SharePrefs.ISREATTEMPT, isReattempt)

                var vUrl = videoURL
                if (isReattempt) {
                    if(!reatteptVideoRequired) {
                        vUrl = ""
                    }
                }
                if (customerorderinfoEntityMaster!!.orderType == "Non-Sellable Order" || customerorderinfoEntityMaster!!.orderType == "Damage Order") {
                    vUrl = ""
                }

                if (selectPersonTypeFlag) {
                    val reDispatchModel = ReDispatchModel(
                        isReattempt,
                        reason,
                        orderId1,
                        SharePrefs.getInstance(this).getString(SharePrefs.SELECTED_DATE),
                        customerLatLng!!.latitude,
                        customerLatLng!!.longitude,
                        tripPlannerConfirmedDetailId.toLong(),
                        vUrl
                    )
                    getNotifyReDispatchReAttemptMethod(reDispatchModel)
                    redispatchDialog!!.dismiss()
                } else {
                    generateOTPofSalesPersonforReattempt(
                        orderIdInterface,
                        selectedStatus!!,
                        customerLatLng!!.latitude,
                        customerLatLng!!.longitude,
                        reason,
                        vUrl
                    )
                }
            }
        }

        if (customerorderinfoEntityMaster!!.orderType != "Non-Sellable Order" && customerorderinfoEntityMaster!!.orderType != "Damage Order") {
            if (videoURL.isNotEmpty()) {
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
                    llRetakeVideo.visibility = View.VISIBLE
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

    private fun getNotifyReDispatchReAttemptMethod(reDispatchModel: ReDispatchModel) {
        orderDetailsViewModel!!.getNotifyReDispatchReAttemptApi(reDispatchModel)
            .observe(this@OrderDetailActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            // ProgressDialog.getInstance().dismiss()
                            mBinding!!.progress.visibility = View.GONE
                            resource.data?.let { notifyReDispatchReAttempt ->
                                notifyReDispatchRe(notifyReDispatchReAttempt)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {
                            //  ProgressDialog.getInstance().show(this)
                            mBinding!!.progress.visibility = View.VISIBLE
                        }
                    }
                }
            }
    }

    private fun notifyReDispatchRe(notifyReDispatchReAttempt: NotifyReDispatchReAttemptModel) {
        try {
            ProgressDialog.getInstance().show(this)
            if (notifyReDispatchReAttempt.Status!!) {
                ProgressDialog.getInstance().dismiss()
                if (isReattempt) {
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
                        isReattempt, false
                    )
                    confirmOrderNotifyMethod(reDispatchOTPModel)
                } else {
                    showOtpDialog(
                        60,
                        tripPlannerConfirmedDetailId,
                        TripPlannerConfirmedOrderId
                    )
                }
                Utils.setToast(applicationContext, notifyReDispatchReAttempt.Message)
            } else {
                Utils.setToast(applicationContext, notifyReDispatchReAttempt.Message)
                ProgressDialog.getInstance().dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun confirmOrderNotifyMethod(reDispatchOTPModel: ReDispatchOTPModel) {
        orderDetailsViewModel!!.confirmOrderNotifyApi(reDispatchOTPModel)
            .observe(this@OrderDetailActivity) {
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
        flexboxLayout: FlexboxLayout?, ratingList: ArrayList<String>, et: EditText?, llPlayVideo: LinearLayout?,
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
                    setTextColor(this@OrderDetailActivity.resources.getColor(R.color.Darkgrey))
                    setPadding(40, 20, 40, 20)
                    text = ratingList[i]
                    setOnClickListener { view ->
                        reason = text.toString()
                        et?.visibility = if (reason == "Other") View.VISIBLE else View.GONE
                        println("Reason - $reason")
                        if(reason == "दुकान बंद है" || reason == "कस्टमर उपलब्ध नहीं है" || reason == "वाहन ख़राब हो गया" || reason == "बारिश हो रही है /ट्रैफिक जाम" || reason == "वाहन में जगह नहीं है") {
                            reatteptVideoRequired = true;
                        } else {
                            reatteptVideoRequired = false;
                        }
                        if(reatteptVideoRequired) {
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

                        if(reatteptVideoRequired){
                            llPlayVideo?.visibility = View.VISIBLE
                            llRetakeVideo?.visibility = View.VISIBLE
                        }else {
                            llPlayVideo?.visibility = View.GONE
                            llRetakeVideo?.visibility = View.GONE
                        }
                    }
                }
                Log.e("TAG", "updateViews: 00000 $reason ||| ${ratingList[i]}", )
                if(reason == ratingList[i]) {
                    Log.e("TAG", "updateViews: 11111111", )
                    textView.setBackgroundDrawable(ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.rectangle_blue
                    ))
                    textView.setTextColor(
                        ContextCompat.getColor(applicationContext, R.color.blue_dialog)
                    )
                }
                viewList.add(textView)
                flexboxLayout!!.addView(textView)
            }
        }
    }

    private fun updateViews(
        flexboxLayout: FlexboxLayout?, ratingList: ArrayList<String>, et: EditText?
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
                    setTextColor(this@OrderDetailActivity.resources.getColor(R.color.Darkgrey))
                    setPadding(40, 20, 40, 20)
                    text = ratingList[i]
                    setOnClickListener { view ->
                        reason = text.toString()
                        et?.visibility = if (reason == "Other") View.VISIBLE else View.GONE
                        println("Reason - $reason")
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
                    }
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
        mBinding!!.progress.visibility = View.GONE
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
        tvName!!.visibility = View.GONE
        val tvTextViewSms = otpDialog!!.findViewById<TextView>(R.id.tvTextViewSms)
        tvTextViewSms!!.visibility = View.VISIBLE
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
            var vUrl = SharePrefs.getInstance(this@OrderDetailActivity)
                .getString(SharePrefs.VIDEO_URL)
            if (customerorderinfoEntityMaster!!.orderType == "Non-Sellable Order" || customerorderinfoEntityMaster!!.orderType == "Damage Order") {
                vUrl = ""
            }
            generateOrderOTP(
                orderIdInterface,
                selectedStatus,
                customerLatLng!!.latitude,
                customerLatLng!!.longitude,
                vUrl
            )
        }
        btnVerifyOtp!!.setOnClickListener {

            genrateOtp = (otpEditBox1.text.toString() +
                    otpEditBox2.text.toString() +
                    otpEditBox3.text.toString() +
                    otpEditBox4.text.toString())
            if (genrateOtp!!.length < 4) {
                Utils.setToast(this@OrderDetailActivity, "Enter Valid OTP")
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
        longitude: Double,
        videoUrl: String?
    ) {
        orderDetailsViewModel!!.generateOrderOTPApi(
            orderIdInterface,
            selectedStatus,
            latitude,
            longitude,
            videoUrl
        )
            .observe(this@OrderDetailActivity) {
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

    private fun generateOrderOTP1(
        orderIdInterface: Int,
        selectedStatus: String?,
        latitude: Double,
        longitude: Double,
        videoUrl: String?
    ) {
        orderDetailsViewModel!!.generateOrderOTPApi(
            orderIdInterface,
            selectedStatus,
            latitude,
            longitude,
            videoUrl
        )
            .observe(this@OrderDetailActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { generateOrderOTP ->
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
            .observe(this@OrderDetailActivity) {
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

                if (!selectPersonTypeFlag) {
                    otpDialog!!.dismiss()
                    val reDispatchModel = ReDispatchModel(
                        isReattempt,
                        reason,
                        orderIdInterface,
                        SharePrefs.getInstance(this).getString(SharePrefs.SELECTED_DATE),
                        customerLatLng!!.latitude,
                        customerLatLng!!.longitude,
                        tripPlannerConfirmedDetailId.toLong(),
                        SharePrefs.getInstance(this@OrderDetailActivity)
                            .getString(SharePrefs.VIDEO_URL)
                    )
                    getNotifyReDispatchReAttemptMethod(reDispatchModel)
                } else {
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
                        isReattempt, false // resolve this isReattempt issue
                    )
                    confirmOrderNotifyMethod(reDispatchOTPModel)
                    customerWiseOrderListApi()
                }


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
            title1 = title
            if (title.equals("Cancel", ignoreCase = true)) {
                reason = ""
                head1 = "Cancellation Reason"
                orderId1 = customerOrderInfoEntity.orderid
                stringArrayList1 = stringArrayList
                isTimed1 = false
                customerOrderInfoEntity1 = customerOrderInfoEntity
                selectedStatus = "Delivery Canceled"

                if(customerorderinfoEntityMaster!!.orderType != "Non-Sellable Order" && customerorderinfoEntityMaster!!.orderType != "Damage Order") {
                    dispatchTakeVideoIntent()
                } else {
                    showCancelOrderDialog("")
                }


            } else if (title.equals("Re Attempt", ignoreCase = true)) {
                isReattempt = true
                selectedStatus = "Delivery Redispatch"
                orderId1 = customerOrderInfoEntity.orderid
                // choosePersonDilog(customerOrderInfoEntity, stringArrayList, true)
                /* timerMethod()
                 SharePrefs.getInstance(this@OrderDetailActivity)
                     .putString(SharePrefs.SELECTED_STATUS, selectedStatus)
                 showRedispatchDialog(customerOrderInfoEntity.orderid, stringArrayList)*/
                selectPersonTypeFlag = false
                showRedispatchDialog(customerOrderInfoEntity.orderid, stringArrayList, true, "")

            } else if (title.equals("Re Dispatch", ignoreCase = true)) {
                selectPersonTypeFlag = true
                selectedStatus = "Delivery Redispatch"
                orderId1 = customerOrderInfoEntity.orderid
                stringArrayList1 = stringArrayList
                IsReattemt1 = false
                if(customerorderinfoEntityMaster!!.orderType != "Non-Sellable Order" && customerorderinfoEntityMaster!!.orderType != "Damage Order") {
                    dispatchTakeVideoIntent()
                } else {
                    showRedispatchDialog("")
                }
            }
        }
        dialog.show()
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


    private fun getUnloadItemDataApi(model: OrderDetailsRequestModel) {
        orderDetailsViewModel!!.getUnloadItemDetailsApi(model)
            .observe(this@OrderDetailActivity) {
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
                Intent(this, UnloadItemActivity::class.java).putExtra(
                    Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
                    tripPlannerConfirmedDetailId
                ).putExtra("time", time)
            )
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
            .observe(this@OrderDetailActivity) {
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


    fun timer(time: Long, timer: TextView?, btResend: Button?, isChangeColor: Boolean) {
        cancelTimer()
        if (TextUtils.isNullOrEmpty(salesPersonName)) {
            timer!!.visibility = View.GONE
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
                SharePrefs.getInstance(this@OrderDetailActivity)
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
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val formattedDate = outputFormat.format(selectedDate.time)
                Log.e("TAG", "showDatePicker: $formattedDate")
                try {
                    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val outputFormat1 = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val selectedDate1: LocalDate = LocalDate.parse(formattedDate, inputFormat)
                    val convertedDateStr = outputFormat1.format(selectedDate1)
                    dateTextView.text = convertedDateStr

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            year,
            month,
            dayOfMonth
        ) {
            override fun onDateChanged(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
                val selectedDate = Calendar.getInstance()
                selectedDate[year, month] = dayOfMonth
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return dateFormat.format(date.time)
    }

    private fun choosePersonDilog(
        customerOrderInfoEntity: TripOrderStatusUpdateModel.CustomerorderinfoEntity,
        stringArrayList: ArrayList<String>, IsReattemt: Boolean
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
            choosePersonDilog!!.dismiss()
            selectPersonTypeFlag = false
            timerMethod()
            SharePrefs.getInstance(this@OrderDetailActivity)
                .putString(SharePrefs.SELECTED_STATUS, selectedStatus)

            dispatchTakeVideoIntent()


            orderId1 = customerOrderInfoEntity.orderid
            stringArrayList1 = stringArrayList
            IsReattemt1 = IsReattemt
            //  showRedispatchDialog(customerOrderInfoEntity.orderid, stringArrayList, IsReattemt)

        }

        btnDeliveryBoy!!.setOnClickListener {
            selectPersonTypeFlag = true
            choosePersonDilog!!.dismiss()
            timerMethod()
            SharePrefs.getInstance(this@OrderDetailActivity)
                .putString(SharePrefs.SELECTED_STATUS, selectedStatus)
            showRedispatchDialog(customerOrderInfoEntity.orderid, stringArrayList, true, "")

        }

        choosePersonDilog!!.setOnDismissListener {
            try {

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /*private fun generateOTPofSalesPersonforReattempt(
        orderid: Int,
        status: String,
        lat: Double,
        lg: Double
    ) {

        orderIDList.clear()
        orderIDList.add(orderid)
        var model = GenerateOTPofSalesPersonforReattemptRequestModel(orderIDList, status, lat, lg)
        orderDetailsViewModel!!.generateOTPofSalesPersonforReattempt(model)
            .observe(this@OrderDetailActivity) {
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

    }*/

    private fun generateOTPofSalesPersonforReattempt(orderid: Int, status: String, lat: Double, lg: Double, reason: String, videoURL: String) {
        orderIDList.clear()
        orderIDList.add(orderid)
        var model = GenerateOTPofSalesPersonforReattemptRequestModel(orderIDList, status, lat, lg, reason, videoURL)
        orderDetailsViewModel!!.generateOTPofSalesPersonforReattempt(model)
            .observe(this@OrderDetailActivity) {
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
                if(redispatchDialog != null) {
                    redispatchDialog!!.dismiss()
                }
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
            selectPersonTypeFlag = true
            otpDialog!!.dismiss()
            getRemoveCurrentSatusApi(
                tripPlannerConfirmedDetailId,
                tripPlannerConfirmedOrderId
            )
        }

        isReattempt = true
        selectedStatus = "Delivery Redispatch"
        // choosePersonDilog(customerOrderInfoEntity, stringArrayList, true)
        /* timerMethod()
         SharePrefs.getInstance(this@OrderDetailActivity)
             .putString(SharePrefs.SELECTED_STATUS, selectedStatus)
         showRedispatchDialog(customerOrderInfoEntity.orderid, stringArrayList)*/


        if(isReattempt) {
            btnResendOtp!!.text = "Retry"
        } else {
            btnResendOtp!!.text = "Resend OTP"
        }

        btnResendOtp!!.setOnClickListener {
            if(isReattempt) {
                otpDialog!!.dismiss()
                selectPersonTypeFlag = false
                showRedispatchDialog(orderIdInterface, reattemptReasonList, true, "")
            } else {
                var vUrl = SharePrefs.getInstance(this@OrderDetailActivity).getString(SharePrefs.VIDEO_URL)
                if (customerorderinfoEntityMaster!!.orderType == "Non-Sellable Order" || customerorderinfoEntityMaster!!.orderType == "Damage Order" || !reatteptVideoRequired) {
                    vUrl = ""
                }
                otpDialog!!.dismiss()
                generateOTPofSalesPersonforReattempt(
                    orderIdInterface,
                    selectedStatus!!,
                    customerLatLng!!.latitude,
                    customerLatLng!!.longitude,
                    reason,
                    vUrl
                )
            }
        }
        btnVerifyOtp!!.setOnClickListener {
            genrateOtp = (otpEditBox1.text.toString() +
                    otpEditBox2.text.toString() +
                    otpEditBox3.text.toString() +
                    otpEditBox4.text.toString())
            if (genrateOtp!!.length < 4) {
                Utils.setToast(this@OrderDetailActivity, "Enter Valid OTP")
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

    private fun deleteVideoFile() {
        try {
            val file = File(videoPath.toString())
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}