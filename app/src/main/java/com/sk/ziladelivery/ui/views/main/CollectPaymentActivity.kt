package com.sk.ziladelivery.ui.views.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.*
import com.sk.ziladelivery.data.model.CollectionPaymentModel.PaymentGroupwisesListModel
import com.sk.ziladelivery.databinding.ActivityCollectPaymentBinding
import com.sk.ziladelivery.databinding.BottomDeliveryOtpBinding
import com.sk.ziladelivery.databinding.BottomOrderDeliveredBinding
import com.sk.ziladelivery.databinding.BottomRemoveOrderLayoutBinding
import com.sk.ziladelivery.listener.CollectPaymentInterface
import com.sk.ziladelivery.ui.views.adapter.CollectPayemtAdapter
import com.sk.ziladelivery.ui.views.adapter.CollectionGroupPaymentAdapter
import com.sk.ziladelivery.ui.views.viewmodels.CollectPaymentViewModel
import com.sk.ziladelivery.utilities.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class CollectPaymentActivity : AppCompatActivity(), CollectPaymentInterface, View.OnClickListener, Listener {
    private var mBinding: ActivityCollectPaymentBinding? = null
    private var collectPaymentViewModel: CollectPaymentViewModel? = null
    private var TripPlannerConfirmedDetailId = 0
    private var tripOrderStatusUpdateModel: CollectionPaymentModel? = null
    private var curentStatus = 0
    private var remainingAmount = 0.0
    private var onlineAmount = 0.0
    private var grossAmount = 0.0
    private var time: String? = null
    private var timerTime: Long = 900000
    private var collectPayemtAdapter: CollectionGroupPaymentAdapter? = null
    private var isCheckPaymentDone = true
    private var easyWayLocation: EasyWayLocation? = null
    private var customerLatLng: LatLng? = null
    private var cTimer: CountDownTimer? = null
    private var bottomBar: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_collect_payment)
        collectPaymentViewModel = ViewModelProviders.of(this)[CollectPaymentViewModel::class.java]
        mBinding!!.collectPayment = collectPaymentViewModel
        dataFromIntent
        setActionBarConfiguration()
        callApis()
        initView()
    }

    private fun initView() {
        mBinding!!.btCollectPayment.setOnClickListener(this)
        collectPaymentViewModel!!.getCollectPaymentObserver(TripPlannerConfirmedDetailId)
        mBinding!!.rvItemList.layoutManager = LinearLayoutManager(this)
        customerLatLng = LatLng(0.0, 0.0)
        easyWayLocation = EasyWayLocation(this, createLocationRequest(), false, true, this)
        easyWayLocation!!.startLocation()
        timerMethod()
    }

    private fun createLocationRequest() = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.MINUTES.toMillis(5)).apply {
        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        setDurationMillis(TimeUnit.MINUTES.toMillis(5))
        setWaitForAccurateLocation(true)
        setMaxUpdates(1)
    }.build()

    private fun callApis() {
        collectPaymentViewModel!!.collectPayment.observe(this) { apiResponse: ApiResponse? ->
            if (apiResponse != null) {
                consumeResponseForStatusUpdate(apiResponse)
            }
        }
        collectPaymentViewModel!!.collectionOrderList.observe(this) { apiResponse: ApiResponse? ->
            if (apiResponse != null) {
                consumeResponseOrderList(apiResponse)
            }
        }
        collectPaymentViewModel!!.checkRemaingOrder.observe(this) { apiResponse: ApiResponse? ->
            if (apiResponse != null) {
                ResponseCheckRemaingOrder(apiResponse)
            }
        }
        collectPaymentViewModel!!.deliveryOtp.observe(this) { apiResponse: ApiResponse? ->
            if (apiResponse != null) {
                ResponseDeliveryOTp(apiResponse)
            }
        }
        collectPaymentViewModel!!.deliveryConfrm.observe(this) { apiResponse: ApiResponse? ->
            if (apiResponse != null) {
                ResponseDeliveryConfirm(apiResponse)
            }
        }
        collectPaymentViewModel!!.removeOrderData().observe(this) { apiResponse: ApiResponse? ->
            if (apiResponse != null) {
                ResponseRemoveOrder(apiResponse)
            }
        }
        collectPaymentViewModel!!.completetrip.observe(this) { apiResponse: ApiResponse? ->
            if (apiResponse != null) {
                consumeComnpletTriip(apiResponse)
            }
        }
    }

    private fun setActionBarConfiguration() {
        mBinding!!.tvActionBar.tvOrderno.text = "Collect Payment"
        mBinding!!.tvActionBar.ivBack.setOnClickListener { view: View? -> onBackPressed() }
    }

    fun timerMethod() {
        val savedTime = SharePrefs.getInstance(this).getInt(SharePrefs.OTP_TIME)
        timerTime = (savedTime * 60 * 1000).toLong()
    }

    private val dataFromIntent: Unit
        get() {
            if (intent.extras != null) {
                TripPlannerConfirmedDetailId =
                    intent.getIntExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, 0)
                time = intent.getStringExtra("time")
                curentStatus = intent.getIntExtra("CustomerTripStatus", 0)
            }
        }

    private fun consumeResponseOrderList(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                renderSuccessOrderListResponce(apiResponse.data)
            }

            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(this, resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun ResponseCheckRemaingOrder(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                renderSuccessResponse(apiResponse.data)
            }

            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(this, resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun ResponseDeliveryOTp(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                renderSuccessResponseDeliveryOTP(apiResponse.data)
            }

            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(this, resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun ResponseDeliveryConfirm(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                renderSuccessResponseDeliveryConfirm(apiResponse.data)
            }

            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(this, resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun consumeResponseForStatusUpdate(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                renderSuccessResponseForStatusUpdate(apiResponse.data)
            }

            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(this, resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun renderSuccessResponseForStatusUpdate(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    collectPaymentViewModel!!.getCollectionOrderListObserver(
                        TripPlannerConfirmedDetailId
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.errorString), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderSuccessOrderListResponce(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    tripOrderStatusUpdateModel =
                        Gson().fromJson(jsonObject.toString(), CollectionPaymentModel::class.java)
                    if (tripOrderStatusUpdateModel!!.customerinfo != null) {
                        mBinding!!.tvStoreAmount.text =
                            tripOrderStatusUpdateModel!!.customerinfo!!.grossAmount.toString()
                        mBinding!!.tvCRMTAg.text =
                            tripOrderStatusUpdateModel!!.customerinfo!!.CRMTags
                        mBinding!!.tvStoreAddresh.text =
                            tripOrderStatusUpdateModel!!.customerinfo!!.billingAddress.toString()
                        SharePrefs.getInstance(this).putInt(
                            SharePrefs.OTP_TIME,
                            tripOrderStatusUpdateModel!!.customerinfo!!.oTPSentRemaningTimeInSec
                        )
                        if (curentStatus == 3) {
                            paymentRecivedMethod(tripOrderStatusUpdateModel!!.customerinfo!!.oTPSentRemaningTimeInSec.toLong())
                        }
                        if (tripOrderStatusUpdateModel!!.customerinfo!!.skcode != null) {
                            mBinding!!.tvStoreSkCode.text =
                                tripOrderStatusUpdateModel!!.customerinfo!!.skcode
                        }
                        if (tripOrderStatusUpdateModel!!.customerinfo!!.shopname != null) {
                            mBinding!!.tvStoreName.text =
                                tripOrderStatusUpdateModel!!.customerinfo!!.shopname
                        }
                        collectPayemtAdapter = CollectionGroupPaymentAdapter(
                            this,
                            tripOrderStatusUpdateModel!!.groupwisesListModels!!,
                            this
                        )
                        mBinding!!.rvItemList.adapter = collectPayemtAdapter
                        remainingAmount = 0.0
                        for (i in tripOrderStatusUpdateModel!!.groupwisesListModels!!.indices) {
                            if (TextUtils.isNullOrEmpty(tripOrderStatusUpdateModel!!.groupwisesListModels!![i].paymentMode)) {
                                if (isCheckPaymentDone) {
                                    isCheckPaymentDone = false
                                }
                            }
                            for (j in tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!!.indices) {
                                if (tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].isBoolWorkingStatus) {
                                    tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].isBoolWorkingStatus =
                                        true
                                    grossAmount += tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].grossamount
                                    remainingAmount += tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].remaningOrderAmount
                                    onlineAmount += tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].onlineAmount
                                }
                                if (tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].isPaymantDone) {
                                    remainingAmount -= tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].remaningOrderAmount
                                }
                            }
                        }
                        if (tripOrderStatusUpdateModel!!.customerinfo!!.isAllPaymentDone) {
                            for (i in tripOrderStatusUpdateModel!!.groupwisesListModels!!.indices) {
                                for (j in tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!!.indices) {
                                    remainingAmount += tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].remaningOrderAmount
                                }
                            }
                        }
                        if (isCheckPaymentDone) {
                            mBinding!!.btCollectPayment.background = resources.getDrawable(R.drawable.button_bg_blue)
                            mBinding!!.btCollectPayment.isClickable = true
                            mBinding!!.btCollectPayment.isEnabled = true
                        } else {
                            mBinding!!.btCollectPayment.background = resources.getDrawable(R.drawable.button_bg_drawable)
                            mBinding!!.btCollectPayment.isClickable = false
                            mBinding!!.btCollectPayment.isEnabled = false
                        }
                        mBinding!!.tvTotalPaybleAmount.text = remainingAmount.toString()
                    } else {
                        // Utils.setToast(this, "No Data Found")
                    }
                    if (tripOrderStatusUpdateModel!!.groupwisesListModels!!.isEmpty()) {
                        startActivity(
                            Intent(
                                applicationContext,
                                OrderDetailActivity::class.java
                            ).putExtra(
                                Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
                                TripPlannerConfirmedDetailId
                            )
                        )
                        finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.errorString), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderSuccessResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    val orderStatusModel =
                        Gson().fromJson(jsonObject.toString(), CheckOrderStatusModel::class.java)
                    if (orderStatusModel.status) {
                        OrderDeliveredPopup(orderStatusModel)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.errorString), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderSuccessResponseDeliveryConfirm(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    val orderConfirmOtpModel =
                        Gson().fromJson(jsonObject.toString(), OrderConfirmOtpModel::class.java)
                    if (orderConfirmOtpModel.isStatus) {
                        bottomBar!!.dismiss()
                        collectPaymentViewModel!!.getCheckOrderRemaingObserver(
                            TripPlannerConfirmedDetailId
                        )
                    } else {
                        showPopup(orderConfirmOtpModel.message)

                       // Utils.setToast(this@CollectPaymentActivity, orderConfirmOtpModel.message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.errorString), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPopup(massage:String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(massage)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun renderSuccessResponseDeliveryOTP(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    paymentRecivedMethod(60)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.errorString), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderSuccessResponseRemoveOrder(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    collectPaymentViewModel!!.getCollectionOrderListObserver(
                        TripPlannerConfirmedDetailId
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.errorString), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun consumeComnpletTriip(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                assert(apiResponse.data != null)
                renderSuccessTripStatusResponse(apiResponse.data)
            }

            Status.ERROR -> Utils.setToast(this, resources.getString(R.string.errorString))
            else -> {}
        }
    }

    private fun ResponseRemoveOrder(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                renderSuccessResponseRemoveOrder(apiResponse.data)
            }

            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(this, resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun renderSuccessTripStatusResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                // mBinding.proRelatedItem.setVisibility(View.GONE);
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getBoolean("Status")) {
                        startActivity(
                            Intent(
                                this,
                                OrderDetailActivity::class.java
                            ).putExtra("TripPlannerConfirmedDetailId", TripPlannerConfirmedDetailId)
                        )
                        finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.errorString), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun checkBoxClicked(
        isChecked: Boolean,
        adapterPosition: Int,
        postion: Int,
        collectionAdapter: CollectPayemtAdapter
    ) {
        if (isChecked) {
            tripOrderStatusUpdateModel!!.groupwisesListModels!!.get(postion).customerorderinfo!!.get(
                adapterPosition
            ).isBoolWorkingStatus = true
            mBinding!!.btCollectPayment.background =
                resources.getDrawable(R.drawable.button_bg_blue)
            mBinding!!.btCollectPayment.isClickable = true
            mBinding!!.btCollectPayment.isEnabled = true
            grossAmount += tripOrderStatusUpdateModel!!.groupwisesListModels!![postion].customerorderinfo!![adapterPosition].grossamount
            remainingAmount += tripOrderStatusUpdateModel!!.groupwisesListModels!![postion].customerorderinfo!![adapterPosition].remaningOrderAmount
            onlineAmount += tripOrderStatusUpdateModel!!.groupwisesListModels!![postion].customerorderinfo!![adapterPosition].onlineAmount
            if (isCheckPaymentDone) {
                mBinding!!.btCollectPayment.background =
                    resources.getDrawable(R.drawable.button_bg_blue)
                mBinding!!.btCollectPayment.isClickable = true
                mBinding!!.btCollectPayment.isEnabled = true
            } else {
                mBinding!!.btCollectPayment.background =
                    resources.getDrawable(R.drawable.button_bg_drawable)
                mBinding!!.btCollectPayment.isClickable = false
                mBinding!!.btCollectPayment.isEnabled = false
            }
        } else {
            grossAmount -= tripOrderStatusUpdateModel!!.groupwisesListModels!![postion].customerorderinfo!![adapterPosition].grossamount
            remainingAmount -= tripOrderStatusUpdateModel!!.groupwisesListModels!![postion].customerorderinfo!![adapterPosition].remaningOrderAmount
            onlineAmount -= tripOrderStatusUpdateModel!!.groupwisesListModels!![postion].customerorderinfo!![adapterPosition].onlineAmount
            tripOrderStatusUpdateModel!!.groupwisesListModels!!.get(postion).customerorderinfo!!.get(
                adapterPosition
            ).isBoolWorkingStatus = false
        }
        if (remainingAmount == 0.0) {
            mBinding!!.btCollectPayment.background =
                resources.getDrawable(R.drawable.button_bg_drawable)
            mBinding!!.btCollectPayment.isClickable = false
            mBinding!!.btCollectPayment.isEnabled = false
        }
        mBinding!!.tvTotalPaybleAmount.text = remainingAmount.toString()
    }

    override fun selectPaymentType(postion: Int, model: PaymentGroupwisesListModel) {
        for (item in model.customerorderinfo!!) {
            if (item.isBoolWorkingStatus) {
                collectPaymentMethod()
                break
            } else {
                Utils.setToast(this, "Please check Checkbox")
            }
        }
    }

    override fun removeOrder(
        customerorderinfoEntity: CollectionPaymentModel.CustomerorderinfoEntity?,
        postion: Int,
        isPaymentDone: Boolean
    ) {
        removeOrderBottomBar(customerorderinfoEntity!!.tripPlannerConfirmedOrderId, isPaymentDone)
    }

    private fun removeOrderBottomBar(TripPlannerConfirmOrderId: Int, isPaymentDone: Boolean) {
        val removeOrderLayoutBinding = DataBindingUtil.inflate<BottomRemoveOrderLayoutBinding>(
            layoutInflater, R.layout.bottom_remove_order_layout, null, false
        )
        val dialog = Dialog(this, R.style.CustomDialog)
        dialog.setContentView(removeOrderLayoutBinding.root)
        dialog.setCancelable(true)
        removeOrderLayoutBinding.shopName.text =
            tripOrderStatusUpdateModel!!.customerinfo!!.shopname
        removeOrderLayoutBinding.btNo.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        removeOrderLayoutBinding.btYes.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                collectPaymentViewModel!!.getRemoveOrderObserver(
                    TripPlannerConfirmOrderId,
                    isPaymentDone
                )
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bt_collect_payment -> collectPaymentViewModel!!.getDeliveryOTPObserver(
                TripPlannerConfirmedDetailId,
                "Delivered",
                customerLatLng!!.latitude,
                customerLatLng!!.longitude
            )
        }
    }

    private fun paymentRecivedMethod(otpTimer: Long) {
        timerTime = otpTimer * 1000
        val bottomSkipLayoutBinding = DataBindingUtil.inflate<BottomDeliveryOtpBinding>(
            layoutInflater, R.layout.bottom_delivery_otp, null, false
        )
        bottomBar = Dialog(this, R.style.CustomDialog)
        bottomBar!!.setContentView(bottomSkipLayoutBinding.root)
        bottomBar!!.setCancelable(true)
        val otp_edit_box1 = bottomBar!!.findViewById<EditText>(R.id.otp_edit_box1)
        val otp_edit_box2 = bottomBar!!.findViewById<EditText>(R.id.otp_edit_box2)
        val otp_edit_box3 = bottomBar!!.findViewById<EditText>(R.id.otp_edit_box3)
        val otp_edit_box4 = bottomBar!!.findViewById<EditText>(R.id.otp_edit_box4)
        val btnResendOtp = bottomBar!!.findViewById<Button>(R.id.btnResendOtp)
        val btnVerifyOtp = bottomBar!!.findViewById<Button>(R.id.btnVerifyOtp)
        val tvTimer = bottomBar!!.findViewById<TextView>(R.id.tvTimer)
        btnResendOtp.isEnabled = false
        btnResendOtp.setTextColor(this.resources.getColor(R.color.gray))
        timer(timerTime, tvTimer, btnResendOtp)
        val edit = arrayOf(otp_edit_box1, otp_edit_box2, otp_edit_box3, otp_edit_box4)
        otp_edit_box1.addTextChangedListener(GenericTextWatcher(otp_edit_box1, edit))
        otp_edit_box2.addTextChangedListener(GenericTextWatcher(otp_edit_box2, edit))
        otp_edit_box3.addTextChangedListener(GenericTextWatcher(otp_edit_box3, edit))
        otp_edit_box4.addTextChangedListener(GenericTextWatcher(otp_edit_box4, edit))
        bottomSkipLayoutBinding.btnVerifyOtp.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val otp = (otp_edit_box1.text.toString() +
                        otp_edit_box2.text.toString() +
                        otp_edit_box3.text.toString() +
                        otp_edit_box4.text.toString())
                if (otp.length < 4) {
                    Utils.setToast(this@CollectPaymentActivity, "Enter Otp")
                } else {
                    val model = OrderConfirmModel(
                        otp,
                        TripPlannerConfirmedDetailId,
                        SharePrefs.getInstance(this@CollectPaymentActivity)
                            .getString(SharePrefs.MOBILE),
                        "Delivered",
                        "Delivered",
                        customerLatLng!!.latitude,
                        customerLatLng!!.longitude,
                        "",
                        TripPlannerConfirmedDetailId,
                        false
                    )
                    val gson = Gson()
                    Log.e("ChequeJson", "ChequeJson" + gson.toJson(model))
                    collectPaymentViewModel!!.getDeliveryConfirmObserver(model)
                }
            }
        })
        bottomSkipLayoutBinding.btnResendOtp.setOnClickListener {
            bottomBar!!.cancel()
            collectPaymentViewModel!!.getDeliveryOTPObserver(
                TripPlannerConfirmedDetailId,
                "Delivered",
                customerLatLng!!.latitude,
                customerLatLng!!.longitude
            )
        }
        if (bottomBar != null && bottomBar!!.isShowing) {
            bottomBar!!.cancel()
        } else {
            bottomBar!!.show()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (bottomBar != null && bottomBar!!.isShowing) {
            bottomBar!!.cancel()
        }
    }

    fun timer(time: Long, timer: TextView, btResend: Button) {
        cancelTimer()
        timer.visibility = View.VISIBLE
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
                    timer.text = ("" +
                            (TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(mills)
                            )) + " Sec")
                }
            }

            override fun onFinish() {
                btResend.isEnabled = true
                btResend.setTextColor(resources.getColor(R.color.orange_dark))
                timer.visibility = View.GONE
            }
        }
        cTimer!!.start()
    }

    private fun cancelTimer() {
        if (cTimer != null) cTimer!!.cancel()
    }

    private fun collectPaymentMethod() {
        var model: CollectPaymentRequestModel? = null
        var orderCount: Int = 0
        var remaningOrderAmount = 0.0
        val collectPaymentList = ArrayList<CollectPaymentRequestModel?>()
        for (i in tripOrderStatusUpdateModel!!.groupwisesListModels!!.indices) {
            for (j in tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!!.indices) {
                if (tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].isBoolWorkingStatus) {
                    remaningOrderAmount += tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].remaningOrderAmount
                    orderCount++
                    model = CollectPaymentRequestModel(
                        tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].tripPlannerConfirmedOrderId,
                        tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].orderid,
                        tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].remaningOrderAmount,
                        tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].paymentFrom,
                        ""
                    )
                    collectPaymentList.add(model)
                } else {
                    remaningOrderAmount -= tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].remaningOrderAmount
                }
                if (tripOrderStatusUpdateModel!!.groupwisesListModels!![i].customerorderinfo!![j].isPaymantDone) {
                    collectPaymentList.remove(model)
                }
            }
        }
        SharePrefs.getInstance(this)
            .putInt(SharePrefs.TRIPPLANERMASTERID, TripPlannerConfirmedDetailId)
        var deliveryIssuanceId = 0
        for (k in tripOrderStatusUpdateModel!!.groupwisesListModels!!.indices) {
            for (l in tripOrderStatusUpdateModel!!.groupwisesListModels!![k].customerorderinfo!!.indices) {
                deliveryIssuanceId =
                    tripOrderStatusUpdateModel!!.groupwisesListModels!![k].customerorderinfo!![l].deliveryIssuanceId
            }
        }


        startActivityForResult(
            Intent(this, NewOrderPlaceActivity::class.java)
                .putExtra("time", time)
                .putExtra("RemingAmount", remainingAmount)
                .putExtra("onlineAmount", onlineAmount)
                .putExtra("orderList", collectPaymentList)
                .putExtra("CustomerID", tripOrderStatusUpdateModel!!.customerinfo!!.customerId)
                .putExtra("IsQREnabled", tripOrderStatusUpdateModel!!.customerinfo!!.IsQREnabled)
                .putExtra("DeliveryIssuanceId", deliveryIssuanceId)
                .putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, TripPlannerConfirmedDetailId),
            1001
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == 1001 && resultCode == RESULT_OK) {
                val requiredValue = data!!.getIntExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, 0)
                //collectPay3213
                isCheckPaymentDone = true
                remainingAmount = 0.0
                onlineAmount = 0.0
                collectPaymentViewModel!!.getCollectPaymentObserver(requiredValue)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun locationOn() {}

    override fun currentLocation(location: Location) {
        easyWayLocation!!.endUpdates()
        customerLatLng = LatLng(location.latitude, location.longitude)
        println("Location - $customerLatLng")
    }

    override fun locationCancelled() {}

    private fun OrderDeliveredPopup(orderStatusModel: CheckOrderStatusModel) {
        val deliveryOtpBinding = DataBindingUtil.inflate<BottomOrderDeliveredBinding>(
            layoutInflater, R.layout.bottom_order_delivered, null, false
        )
        val dialog = Dialog(this, R.style.CustomDialog)
        dialog.setContentView(deliveryOtpBinding.root)
        dialog.setCancelable(false)
        if (orderStatusModel.checkremaingorderstatusdc == null) {
            deliveryOtpBinding.tvOrderCount.text = "Orders are Left " + "0"
            deliveryOtpBinding.tvRemaingAmount.text = "Remaining Payable  " + "0"
        } else {
            deliveryOtpBinding.tvOrderCount.text =
                "Orders are Left " + orderStatusModel.checkremaingorderstatusdc.ordercount
            deliveryOtpBinding.tvRemaingAmount.text =
                "Remaining Payable  " + orderStatusModel.checkremaingorderstatusdc.totalamount
        }
        deliveryOtpBinding.btGotIt.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (orderStatusModel.checkremaingorderstatusdc == null) {
                    val intent = Intent(this@CollectPaymentActivity, CustomerRatingActivity::class.java)
                    intent.putExtra(
                        Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
                        TripPlannerConfirmedDetailId
                    )
                    startActivity(intent)
                } else {
                    val intent =
                        Intent(this@CollectPaymentActivity, OrderDetailActivity::class.java)
                    intent.putExtra(
                        Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
                        TripPlannerConfirmedDetailId
                    )
                    startActivity(intent)
                }
                dialog.dismiss()
                finishAffinity()
            }
        })
        dialog.show()
    }

    override fun onBackPressed() {
        val i = Intent(this@CollectPaymentActivity, MainActivity::class.java)
        startActivity(i)
    }
}