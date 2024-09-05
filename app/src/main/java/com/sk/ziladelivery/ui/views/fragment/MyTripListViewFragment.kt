package com.sk.ziladelivery.ui.views.fragment

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.*
import com.sk.ziladelivery.databinding.BottomPopupViewBinding
import com.sk.ziladelivery.databinding.BottomSkipLayoutBinding
import com.sk.ziladelivery.databinding.DialogConfirmationBinding
import com.sk.ziladelivery.databinding.FragmentMytripListBinding
import com.sk.ziladelivery.listener.MyPopupViewListener
import com.sk.ziladelivery.listener.SearchMyTripInterface
import com.sk.ziladelivery.listener.orderdetailClick
import com.sk.ziladelivery.ui.views.adapter.MyListViewAdapter
import com.sk.ziladelivery.ui.views.main.MainActivity
import com.sk.ziladelivery.ui.views.main.MyTripActivity
import com.sk.ziladelivery.ui.views.main.MyTripActivity.Companion.setInterface
import com.sk.ziladelivery.ui.views.viewmodels.MyTripViewModel
import com.sk.ziladelivery.utilities.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class MyTripListViewFragment : Fragment(), orderdetailClick, MyPopupViewListener, SearchMyTripInterface, Listener {
    val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private var myTripViewModel: MyTripViewModel? = null
    private var ZilaTripMasterId = 0
    private var TripPlannerConfirmedDetailId = 0
    private var TripPlannerConfirmedDetailIdDashbord = 0
    private var TripPlannerConfDetailId = 0
    private var otp = ""
    private var isReattempt = false
    private var timerTime: Long = 9000
    private var reason: String? = ""
    private var selectedDate: String? = null
    private var customerLatLng: LatLng? = null
    private var tvTimer: TextView? = null
    private var btnResendOtp: Button? = null
    private var btnVerifyOtp: Button? = null
    private var otpDialog: BottomSheetDialog? = null
    private var selectedStatus: String? = null
    private var progressDialog: ProgressDialog? = null
    private var activity: MyTripActivity? = null
    private var mBinding: FragmentMytripListBinding? = null
    private var myListViewAdapter: MyListViewAdapter? = null
    private var OrderList: ArrayList<MyTripOrderResponseModel?>? = null
    private var easyWayLocation: EasyWayLocation? = null
    private var cTimer: CountDownTimer? = null
    private var CustomerTripStatus = 0
    private var voiceImageView: ImageView? = null
    private var lastVoiceImageView: ImageView? = null
    private var imPlayImage: ImageView? = null
    private var lastPlayImage: ImageView? = null
    private var player: MediaPlayer? = null
    private var orderCount = 0
    private var gpsTracker: GPSTracker? = null
    private var distance = 0.0
    private var warehouseLat = 0.0
    private var warehouseLong = 0.0
    private var rdDateList: List<String> = emptyList()
    private var choosePersonDilog: BottomSheetDialog? = null
    private var redispatchDialog: BottomSheetDialog? = null
    lateinit var model: GenerateOTPofSalesPersonforReattemptRequestModel
    private var generateOTPofSalesPersonforReattemptResponse: GenerateOTPofSalesPersonforReattemptResponse? = null
    private var tvTimerOTP: TextView? = null
    private var orderIDList: List<Int> = ArrayList()
    private var status = ""
    private var orderIdInterface = 0
    // private var selectPersonTypeFlag = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MyTripActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (mBinding == null) {
            mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_mytrip_list, container, false)
        }
        val bundle = this.arguments
        ZilaTripMasterId = bundle!!.getInt("ZilaTripMasterId")
        TripPlannerConfirmedDetailIdDashbord = bundle.getInt(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id)
        CustomerTripStatus = bundle.getInt("CustomerTripStatus")
        initView()
        return mBinding!!.root
    }

    private fun initView() {
        progressDialog = ProgressDialog(activity)
        myTripViewModel = ViewModelProviders.of(this)[MyTripViewModel::class.java]
        mBinding!!.rvPenddingTask.layoutManager = LinearLayoutManager(activity)
        customerLatLng = LatLng(0.0, 0.0)
        OrderList = ArrayList()
        setInterface { s: String -> onSearch(s) }
        myListViewAdapter =
            MyListViewAdapter(
                requireActivity(),
                OrderList!!, this, TripPlannerConfirmedDetailIdDashbord
            )
        mBinding!!.rvPenddingTask.adapter = myListViewAdapter
        gpsTracker = GPSTracker(getActivity())
        easyWayLocation = EasyWayLocation(activity, createLocationRequest(), false, true, this)
        easyWayLocation!!.startLocation()
        player = MediaPlayer()
        myTripViewModel!!.getmyTripOrderResponseData()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    consumeResponse(it)
                }
            }
        myTripViewModel!!.notifyReDispatchReAttempt.observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                notifyRedispatchReattemptUpdate(
                    it
                )
            }
        }
        myTripViewModel!!.allNotifyReDispatchReAttempt.observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                resendOtpUpdateRequest(
                    it
                )
            }
        }
        myTripViewModel!!.cofirmOrder().observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                confirmOrderRequest(it)
            }
        }
        myTripViewModel!!.generateOrderOTP()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    resendOtpUpdateRequest(it)
                }
            }
        myTripViewModel!!.tripRecodingResponce.observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeRecodingVoiceResponse(
                    it
                )
            }
        }

        myTripViewModel!!.holidayReAttempt.observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeholidayRettemptResponse(
                    it
                )
            }
        }
        myTripViewModel!!.GetMyTripOrderModel(ZilaTripMasterId)
        myTripViewModel!!.getmySinngleTripOrderResponseData()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    SingleTripconsumeResponse(it)
                }
            }
        myTripViewModel!!.orderCountStatus.observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeOrderCount(
                    it
                )
            }
        }
        myTripViewModel!!.GetMySingleTripMapOrderModel(
            ZilaTripMasterId,
            gpsTracker!!.latitude,
            gpsTracker!!.longitude
        )
        mBinding!!.btCreateTrip.setOnClickListener { v: View? ->
            if (customerLatLng != null) {
                val startPoint = Location("locationA")
                startPoint.latitude = customerLatLng!!.latitude
                startPoint.longitude = customerLatLng!!.longitude
                val endPoint = Location("locationB")
                endPoint.latitude = warehouseLat
                endPoint.longitude = warehouseLong
                distance = startPoint.distanceTo(endPoint).toDouble()

                if (distance <= 200) {
                    myTripViewModel!!.getOderCountObserver(
                        TripPlannerConfDetailId,
                        customerLatLng!!.latitude,
                        customerLatLng!!.longitude
                    )
                } else {
                    Utils.setToast(activity, "You can complete trip within 200 mtr of warehouse.")
                }
            } else {
                try {
                    callGPSMethod()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkLocationPermission()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        myTripViewModel!!.GenerateOTPofSalesPersonforReattempt()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    GenerateOTPofSalesPersonforReattempt(
                        it
                    )
                }
            }
        myTripViewModel!!.validateOrderOTP()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    consumeVerifyOrderOtpResponse(it)
                }
            }
    }

    private fun consumeVerifyOrderOtpResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                mBinding!!.proRelatedItem.visibility = View.VISIBLE
            }

            Status.SUCCESS -> {
                mBinding!!.proRelatedItem.visibility = View.GONE
                assert(apiResponse.data != null)
                val message = apiResponse.data!!.asJsonObject["Message"].asString
                val status = apiResponse.data.asJsonObject["Status"].asBoolean
                if (status) {
                    otpDialog!!.dismiss()
                    val reDispatchModel = ReDispatchModel(
                        isReattempt,
                        reason,
                        SharePrefs.getInstance(activity).getString(SharePrefs.SELECTED_DATE),
                        customerLatLng!!.latitude,
                        customerLatLng!!.longitude,
                        TripPlannerConfirmedDetailId.toLong()
                    )
                    myTripViewModel!!.getNotifyALLReDispatchAndReAttempt(reDispatchModel)
                    redispatchDialog!!.dismiss()

                } else {
                    com.sk.ziladelivery.utilities.ProgressDialog.getInstance().dismiss()
                    Utils.setToast(activity, message)
                }
            }

            Status.ERROR -> Utils.setToast(activity, resources.getString(R.string.errorString))
            else -> {}
        }
    }

    private fun createLocationRequest() = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.MINUTES.toMillis(5)).apply {
        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        setDurationMillis(TimeUnit.MINUTES.toMillis(5))
        setWaitForAccurateLocation(true)
        setMaxUpdates(1)
    }.build()

    private fun consumeOrderCount(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                assert(apiResponse.data != null)
                renderSuccessOrderStus(apiResponse.data)
            }

            Status.ERROR -> Utils.setToast(activity, resources.getString(R.string.errorString))
            else -> {}
        }
    }

    private fun SingleTripconsumeResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                assert(apiResponse.data != null)
                SingleTriprenderSuccessResponse(apiResponse.data)
            }

            Status.ERROR -> Utils.setToast(activity, resources.getString(R.string.errorString))
            else -> {}
        }
    }

    private fun resendOtpUpdateRequest(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                progressDialog!!.setMessage("Doing something, please wait.")
                progressDialog!!.show()
            }

            Status.SUCCESS -> {
                progressDialog!!.dismiss()
                assert(apiResponse.data != null)
                resendOtpRequestStatusUpdate(apiResponse.data)
            }

            Status.ERROR -> {
                progressDialog!!.dismiss()
                Utils.setToast(activity, resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun confirmOrderRequest(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                progressDialog!!.setMessage("Doing something, please wait.")
                progressDialog!!.show()
            }

            Status.SUCCESS -> {
                progressDialog!!.dismiss()
                assert(apiResponse.data != null)
                confirmOrderStatusUpdate(apiResponse.data)
            }

            Status.ERROR -> {
                progressDialog!!.dismiss()
                Utils.setToast(activity, resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun renderSuccessOrderStus(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getBoolean("Status")) {
                        startActivity(Intent(activity, MainActivity::class.java))
                        requireActivity().finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.errorString),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmOrderStatusUpdate(response: JsonElement?) {
        try {
            val `object` = JSONObject(response.toString())
            val skippAllApiResponse = Gson().fromJson(`object`.toString(), SkippAllApiResponse::class.java)
            Utils.setToast(activity, skippAllApiResponse.message)
            if (skippAllApiResponse.isStatus) {
                OrderList = ArrayList()
                if (otpDialog != null) {
                    if (otpDialog!!.isShowing) {
                        otpDialog!!.dismiss()
                    }
                }
                val i = Intent(activity, MainActivity::class.java)
                requireActivity().startActivity(i)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun resendOtpRequestStatusUpdate(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    JSONObject(response.toString())
                    if (isReattempt) {
                        val peopleId = SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID)
                        val reDispatchOTPModel = ReDispatchOTPModelforMyTrip(
                            otp,
                            TripPlannerConfirmedDetailId,
                            reason,
                            customerLatLng!!.latitude,
                            customerLatLng!!.longitude,
                            SharePrefs.getInstance(activity).getString(SharePrefs.SELECTED_DATE),
                            isReattempt,
                            peopleId
                        )
                        myTripViewModel!!.confirmOrderNotifyMethod(reDispatchOTPModel)
                    } else {
                        showOtpDialog(60)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.errorString),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun SingleTriprenderSuccessResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    val orderResponseModel = Gson().fromJson(
                        jsonObject.toString(),
                        MySingleTripOrderResponseModel::class.java
                    )
                    orderCount = orderResponseModel.customerOrderinfoDc.orderCount
                    warehouseLat = orderResponseModel.singleOrderMapviewInfoDC.warehouesLat
                    warehouseLong = orderResponseModel.singleOrderMapviewInfoDC.warehouesLng
                    TripPlannerConfDetailId = orderResponseModel.customerOrderinfoDc.zilaTripDetailId
                    if (orderCount == 0) {
                        mBinding!!.btCreateTrip.visibility = View.VISIBLE
                    } else {
                        mBinding!!.btCreateTrip.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.errorString),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun notifyRedispatchReattemptUpdate(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                progressDialog!!.setMessage("Doing something, please wait.")
                progressDialog!!.show()
            }

            Status.SUCCESS -> {
                progressDialog!!.dismiss()
                assert(apiResponse.data != null)
                notifyRedispatchrenderSuccessResponseForStatusUpdate(apiResponse.data)
            }

            Status.ERROR -> {
                progressDialog!!.dismiss()
                Utils.setToast(activity, resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun notifyRedispatchrenderSuccessResponseForStatusUpdate(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    JSONObject(response.toString())
                    btnResendOtp!!.isEnabled = false
                    btnResendOtp!!.setTextColor(requireActivity().resources.getColor(R.color.gray))
                    timerTime = 60000
                    timer(timerTime, tvTimer, btnResendOtp, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                showOtpDialog(60)
                Toast.makeText(
                    activity,
                    resources.getString(R.string.errorString),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showOtpDialog(otpTimer: Long) {
        timerTime = otpTimer * 1000
        otpDialog = BottomSheetDialog(requireActivity())
        otpDialog!!.setContentView(R.layout.dialog_otp_verification)
        otpDialog!!.setCanceledOnTouchOutside(false)
        val otp_edit_box1 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box1)
        val otp_edit_box2 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box2)
        val otp_edit_box3 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box3)
        val otp_edit_box4 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box4)
        btnResendOtp = otpDialog!!.findViewById(R.id.btnResendOtp)
        btnVerifyOtp = otpDialog!!.findViewById(R.id.btnVerifyOtp)
        tvTimer = otpDialog!!.findViewById(R.id.tvTimer)
        val tvName = otpDialog!!.findViewById<TextView>(R.id.tvName)
        tvName!!.visibility = View.GONE
        val tvTextViewSms = otpDialog!!.findViewById<TextView>(R.id.tvTextViewSms)
        tvTextViewSms!!.visibility = View.VISIBLE
        btnResendOtp!!.isEnabled = false
        btnResendOtp!!.setTextColor(this.resources.getColor(R.color.gray))
        timer(timerTime, tvTimer, btnResendOtp, true)
        val edit = arrayOf(otp_edit_box1, otp_edit_box2, otp_edit_box3, otp_edit_box4)
        otp_edit_box1!!.addTextChangedListener(GenericTextWatcher(otp_edit_box1, edit))
        otp_edit_box2!!.addTextChangedListener(GenericTextWatcher(otp_edit_box2, edit))
        otp_edit_box3!!.addTextChangedListener(GenericTextWatcher(otp_edit_box3, edit))
        otp_edit_box4!!.addTextChangedListener(GenericTextWatcher(otp_edit_box4, edit))
        btnResendOtp!!.setOnClickListener { //  myTripViewModel.generateOrderOTPforAll(orderIdInterface,selectedStatus, customerLatLng.latitude, customerLatLng.longitude);
            //selectedDate = Utils.getSimpleDateFormat((tvSelectDate.getText()).toString().trim());
            val reDispatchModel = ReDispatchModel(
                isReattempt,
                reason,
                SharePrefs.getInstance(activity).getString(SharePrefs.SELECTED_DATE),
                customerLatLng!!.latitude,
                customerLatLng!!.longitude,
                TripPlannerConfirmedDetailId.toLong()
            )
            myTripViewModel!!.getNotifyReDispatchReAttemptMethod(reDispatchModel)
        }
        btnVerifyOtp!!.setOnClickListener {
            otp = otp_edit_box1.text.toString() +
                    otp_edit_box2.text.toString() +
                    otp_edit_box3.text.toString() + otp_edit_box4.text.toString()
            if (otp.length < 4) {
                Utils.setToast(activity, "Enter Valid OTP")
            } else {
                try {
                    val peopleId =
                        SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID)
                    val reDispatchOTPModel = ReDispatchOTPModelforMyTrip(
                        otp,
                        TripPlannerConfirmedDetailId,
                        reason,
                        customerLatLng!!.latitude,
                        customerLatLng!!.longitude,
                        SharePrefs.getInstance(activity).getString(SharePrefs.SELECTED_DATE),
                        isReattempt,
                        peopleId
                    )
                    myTripViewModel!!.confirmOrderNotifyMethod(reDispatchOTPModel)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // myTripViewModel.getOTPNotifyReDispatchReAttemptMethod(otp,orderIdInterface,  selectedStatus);
            }
        }
        otpDialog!!.show()
    }

    private fun consumeResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> mBinding!!.proRelatedItem.visibility = View.VISIBLE
            Status.SUCCESS -> {
                mBinding!!.proRelatedItem.visibility = View.GONE
                assert(apiResponse.data != null)
                renderSuccessResponse(apiResponse.data)
            }

            Status.ERROR -> {
                mBinding!!.proRelatedItem.visibility = View.GONE
                Utils.setToast(getActivity(), resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun consumeRecodingVoiceResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> mBinding!!.proRelatedItem.visibility = View.VISIBLE
            Status.SUCCESS -> {
                mBinding!!.proRelatedItem.visibility = View.GONE
                assert(apiResponse.data != null)
                renderSuccessVoiceResponse(apiResponse.data)
            }

            Status.ERROR -> {
                mBinding!!.proRelatedItem.visibility = View.GONE
                Utils.setToast(getActivity(), resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun consumeholidayRettemptResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> mBinding!!.proRelatedItem.visibility = View.VISIBLE
            Status.SUCCESS -> {
                mBinding!!.proRelatedItem.visibility = View.GONE
                assert(apiResponse.data != null)
                renderHolidayReAttemptResponse(apiResponse.data)
            }

            Status.ERROR -> {
                mBinding!!.proRelatedItem.visibility = View.GONE
                Utils.setToast(getActivity(), resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    private fun GenerateOTPofSalesPersonforReattempt(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                mBinding!!.proRelatedItem.visibility = View.VISIBLE
                com.sk.ziladelivery.utilities.ProgressDialog.getInstance().show(requireActivity())
            }

            Status.SUCCESS -> {
                com.sk.ziladelivery.utilities.ProgressDialog.getInstance().dismiss()
                mBinding!!.proRelatedItem.visibility = View.GONE
                assert(apiResponse.data != null)
                generateOTPofSalesPersonforReattemptResponse(apiResponse.data)
            }

            Status.ERROR -> {
                mBinding!!.proRelatedItem.visibility = View.GONE
                Utils.setToast(getActivity(), resources.getString(R.string.errorString))
            }

            else -> {}
        }
    }

    fun filter(text: String) {
        val temp: ArrayList<MyTripOrderResponseModel?> = ArrayList<MyTripOrderResponseModel?>()
        for (d in OrderList!!) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d!!.shopname != null && d.shopname?.lowercase(Locale.getDefault())!!.contains(
                    text.lowercase(
                        Locale.getDefault()
                    )
                )
            ) {
                temp.add(d)
            } else if (d.skcode != null && d.skcode?.lowercase(Locale.getDefault())!!.contains(
                    text.lowercase(
                        Locale.getDefault()
                    )
                )
            ) {
                temp.add(d)
            }
        }

        //update recyclerview
        if (temp.size > 0) {
            myListViewAdapter!!.updateList(temp)
        }
    }

    private fun renderSuccessResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                mBinding!!.proRelatedItem.visibility = View.GONE
                try {
                    val array = JSONArray(response.toString())
                    OrderList = Gson().fromJson(array.toString(), object : TypeToken<ArrayList<MyTripOrderResponseModel?>>() {}.type
                    )
                    myListViewAdapter!!.updateList(OrderList!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    getActivity(),
                    resources.getString(R.string.errorString),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderSuccessVoiceResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                mBinding!!.proRelatedItem.visibility = View.GONE
                try {
                    val jsonObject = JSONObject(response.toString())
                    val voiceRecodingModel =
                        Gson().fromJson(jsonObject.toString(), VoiceRecodingModel::class.java)
                    if (voiceRecodingModel.recordingUrl != null) {
                        voiceImageView!!.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_play_arrow_24))
                        playVoiceRecoding(voiceRecodingModel.recordingUrl)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    getActivity(),
                    resources.getString(R.string.errorString),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Utils.setToast(getActivity(), "No Voice Recoding Found")
        }
    }

    private fun generateOTPofSalesPersonforReattemptResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                mBinding!!.proRelatedItem.visibility = View.GONE
                try {
                    val jsonObject = JSONObject(response.toString())
                    generateOTPofSalesPersonforReattemptResponse = Gson().fromJson(
                        jsonObject.toString(),
                        GenerateOTPofSalesPersonforReattemptResponse::class.java
                    )
                    if (generateOTPofSalesPersonforReattemptResponse!!.res) {
                        redispatchDialog!!.dismiss()
                        showOTPofSalesPersonDilog(
                            60,
                            generateOTPofSalesPersonforReattemptResponse!!.msg
                        )

                    } else {
                        Toast.makeText(
                            requireActivity(),
                            generateOTPofSalesPersonforReattemptResponse!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    getActivity(),
                    resources.getString(R.string.errorString),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Utils.setToast(getActivity(), "No Voice Recoding Found")
        }
    }

    private fun renderHolidayReAttemptResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                mBinding!!.proRelatedItem.visibility = View.GONE
                try {
                    val jsonObject = JSONArray(response.toString())
                    val gson = Gson()
                    val objectList = gson.fromJson(jsonObject.toString(), Array<String>::class.java).asList()
                    rdDateList = DateUtils.filterDates(objectList)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    getActivity(),
                    resources.getString(R.string.errorString),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Utils.setToast(getActivity(), "No Voice Recoding Found")
        }
    }

    private fun playVoiceRecoding(recordingUrl: String) {
        try {
            val uri = Uri.parse(recordingUrl)
            player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player!!.setDataSource(requireContext(), uri)
            player!!.setOnCompletionListener {
                imPlayImage!!.visibility = View.GONE
                voiceImageView!!.visibility = View.VISIBLE
            }
            player!!.prepare()
            player!!.start()
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun onlineLayHideUnHideClicked(param: Boolean) {}
    override fun callPopupClicked(
        Position: Int,
        myTripOrderResponseModel: MyTripOrderResponseModel,
        orderlist: ArrayList<MyTripOrderResponseModel.OrderlistEntity>

    ) {
        TripPlannerConfirmedDetailId = myTripOrderResponseModel.tripPlannerConfirmedDetailId
        PopupBottomBar(myTripOrderResponseModel)
        orderIDList = orderlist.map { it.orderid }
        status = orderlist[0].status!!
        orderIdInterface = orderlist[0].orderid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (player != null) {
            player!!.stop()
            player!!.release()
        }
    }

    override fun callVoiceRecoding(
        Position: Int,
        myTripOrderResponseModel: MyTripOrderResponseModel,
        tvVoice: ImageView,
        imPlayIcon: ImageView
    ) {
        voiceImageView = tvVoice
        imPlayImage = imPlayIcon
        if (player != null) {
            player!!.stop()
            player!!.release()
            player = MediaPlayer()
            if (lastVoiceImageView != null) {
                lastVoiceImageView!!.visibility = View.VISIBLE
                lastPlayImage!!.visibility = View.GONE
            }
            lastVoiceImageView = tvVoice
            lastPlayImage = imPlayIcon
        }
        if (myTripOrderResponseModel.recordingUrl != null) {
            playVoiceRecoding(myTripOrderResponseModel.recordingUrl!!)
            imPlayIcon.visibility = View.VISIBLE
            tvVoice.visibility = View.GONE
        }
    }

    private fun PopupBottomBar(myTripOrderResponseModel: MyTripOrderResponseModel) {
        val bottomSkipLayoutBinding = DataBindingUtil.inflate<BottomPopupViewBinding>(
            layoutInflater,
            R.layout.bottom_popup_view,
            null,
            false
        )
        val bottomBar = Dialog(requireContext(), R.style.CustomDialog)
        bottomBar.setContentView(bottomSkipLayoutBinding.root)
        bottomBar.setCancelable(true)
        if (myTripOrderResponseModel.workingStatus == 7) {
            bottomSkipLayoutBinding.LLMainLayout.weightSum = 1f
            bottomSkipLayoutBinding.tvSkip.visibility = View.GONE
            bottomSkipLayoutBinding.tvReAttmpt.visibility = View.GONE
        } else {
            bottomSkipLayoutBinding.LLMainLayout.weightSum = 2f
        }
        if (myTripOrderResponseModel.isReAttemptShow) {
            bottomSkipLayoutBinding.LLMainLayout.weightSum = 1f
            bottomSkipLayoutBinding.tvReAttmpt.visibility = View.GONE
        }
        if (myTripOrderResponseModel.isReDispatchShow) {
            bottomSkipLayoutBinding.LLMainLayout.weightSum = 2f
            bottomSkipLayoutBinding.tvReDispacth.visibility = View.GONE
        }
        if (myTripOrderResponseModel.isReAttemptShow && myTripOrderResponseModel.isReDispatchShow) {
            bottomSkipLayoutBinding.tvReDispacth.visibility = View.GONE
            bottomSkipLayoutBinding.tvReAttmpt.visibility = View.GONE
            bottomSkipLayoutBinding.LLMainLayout.weightSum = 1f
        }

        bottomSkipLayoutBinding.tvReDispacth.visibility = View.GONE
        bottomSkipLayoutBinding.tvReAttmpt.visibility = View.GONE

        bottomSkipLayoutBinding.tvSkip.setOnClickListener {
            skipBottomBar(myTripOrderResponseModel)
            bottomBar.dismiss()
        }
        bottomSkipLayoutBinding.tvReAttmpt.setOnClickListener {
            bottomBar.dismiss()
            callReAttemtMethod("Re Attempt", myTripOrderResponseModel)
        }
        bottomSkipLayoutBinding.tvReDispacth.setOnClickListener {
            bottomBar.dismiss()
            callReAttemtMethod("Re Dispatch", myTripOrderResponseModel)
        }
        bottomSkipLayoutBinding.ivCancle.setOnClickListener { bottomBar.dismiss() }
        bottomBar.show()
    }

    private fun callReAttemtMethod(
        buttonText: String,
        myTripOrderResponseModel: MyTripOrderResponseModel
    ) {
        if (buttonText == "Re Attempt") {
            isReattempt = true
            selectedStatus = "Delivery Redispatch"
            val stringArrayList = ArrayList<String>()
            SharePrefs.getInstance(getActivity())
                .putString(SharePrefs.SELECTED_STATUS, selectedStatus)
            stringArrayList.add("Could not attempt")
            stringArrayList.add("Customer not available")
            stringArrayList.add("Shop not found")
            stringArrayList.add("Natural Calamities/Rain")
            stringArrayList.add("Vehicle break down")
            stringArrayList.add("Shop is closed")
            stringArrayList.add("Other")
            confirmationDialog(
                "Re Attempt",
                stringArrayList,
                myTripOrderResponseModel.orderlist!![0].orderid
            )
        } else if (buttonText == "Re Dispatch") {
            isReattempt = false
            timerMethod()
            selectedStatus = "Delivery Redispatch"
            val stringArrayList = ArrayList<String>()
            SharePrefs.getInstance(getActivity())
                .putString(SharePrefs.SELECTED_STATUS, selectedStatus)
            stringArrayList.add("Concern Customer Unavailable")
            stringArrayList.add("Customer Busy")
            stringArrayList.add("Don't Need")
            stringArrayList.add("Other")
            if (myTripOrderResponseModel.workingStatus == 7 && myTripOrderResponseModel.isOTPSent) {
                timerTime = myTripOrderResponseModel.oTPSentRemaningTimeInSec.toLong()
                reason = myTripOrderResponseModel.reason
                showOtpDialog(timerTime)
            } else {
                confirmationDialog(
                    "Re Dispatch",
                    stringArrayList,
                    myTripOrderResponseModel.orderlist!![0].orderid
                )
            }
        }
    }

    fun timerMethod() {
        val savedTime = SharePrefs.getInstance(activity).getInt(SharePrefs.OTP_TIME)
        timerTime = (savedTime * 60 * 1000).toLong()
    }

    private fun showRedispatchDialog(stringArrayList: ArrayList<String>, orderId: Int) {
        redispatchDialog = BottomSheetDialog(requireActivity())
        redispatchDialog!!.setContentView(R.layout.dialog_redispatch_order)
        redispatchDialog!!.setCanceledOnTouchOutside(false)
        val etEnterComment = redispatchDialog!!.findViewById<EditText>(R.id.etEnterComment)
        val tvHead = redispatchDialog!!.findViewById<TextView>(R.id.tvHead)
        myTripViewModel!!.getReAttempt(orderId)
        if (isReattempt) {
            tvHead!!.text = "Re-Attempt Reason"
            reason = ""
        } else {
            tvHead!!.text = "Re-Dispatch Reason"
            reason = ""
        }
        val tvSelectDate = redispatchDialog!!.findViewById<TextView>(R.id.tvSelectDate)
        val fbCancelOrder = redispatchDialog!!.findViewById<FlexboxLayout>(R.id.fbCancelOrder)
        val btnSendNotification = redispatchDialog!!.findViewById<Button>(R.id.btnSendNotification)
        updateViews(fbCancelOrder, stringArrayList, etEnterComment)
        tvSelectDate!!.setOnClickListener { view: View? -> selectDate(tvSelectDate) }
        btnSendNotification!!.setOnClickListener { v: View? ->
            if (reason == "Other") {
                reason = etEnterComment!!.text.toString()
            }
            if (reason == "") {
                Utils.setToast(activity, "Please Select/Enter Reason")
            } else if (tvSelectDate.text.toString().trim { it <= ' ' } == "") {
                Utils.setToast(activity, "Please Select Date")
            } else {
                // selectedDate = tvSelectDate.text.toString().trim { it <= ' ' }

                selectedDate = (tvSelectDate.text).toString().trim()
                SharePrefs.getInstance(getActivity())
                    .putString(SharePrefs.SELECTED_DATE, selectedDate)
                SharePrefs.getInstance(getActivity()).putString(SharePrefs.REASON, reason)
                SharePrefs.getInstance(getActivity())
                    .putBoolean(SharePrefs.ISREATTEMPT, isReattempt)
                val reDispatchModel = ReDispatchModel(
                    isReattempt,
                    reason,
                    SharePrefs.getInstance(activity).getString(SharePrefs.SELECTED_DATE),
                    customerLatLng!!.latitude,
                    customerLatLng!!.longitude,
                    TripPlannerConfirmedDetailId.toLong()
                )
                val gson = Gson()
                Log.e("ChequeJson", "ChequeJson" + gson.toJson(reDispatchModel))
                myTripViewModel!!.getNotifyALLReDispatchAndReAttempt(reDispatchModel)
                redispatchDialog!!.dismiss()

                /* if (selectPersonTypeFlag) {

                 } else {

                     val model = GenerateOTPofSalesPersonforReattemptRequestModel(
                         orderIDList,
                         selectedStatus!!,
                         customerLatLng!!.latitude,
                         customerLatLng!!.longitude
                     )
                     myTripViewModel!!.generateOTPofSalesPersonforReattempt(model)

                 }*/
            }
        }
        redispatchDialog!!.show()
    }

    private fun confirmationDialog(
        title: String,
        stringArrayList: ArrayList<String>,
        orderId: Int
    ) {
        val dialogConfirmationBinding = DataBindingUtil.inflate<DialogConfirmationBinding>(
            layoutInflater, R.layout.dialog_confirmation, null, false
        )
        val dialog = Dialog(requireActivity(), R.style.CustomDialog)
        dialog.setContentView(dialogConfirmationBinding.root)
        dialog.setCancelable(true)
        dialogConfirmationBinding.tvTitleDesc.text =
            "Do you want to $title (By Operations) this order?"
        dialogConfirmationBinding.btNo.setOnClickListener { dialog.dismiss() }
        dialogConfirmationBinding.btYes.setOnClickListener {
            dialog.dismiss()

            if (title.equals("Re Attempt", ignoreCase = true)) {
                selectedStatus = "Delivery Redispatch"
                //choosePersonDilog(title, stringArrayList, orderId)
                showRedispatchDialog(stringArrayList, orderId)
            } else if (title.equals("Re Dispatch", ignoreCase = true)) {
                showRedispatchDialog(stringArrayList, orderId)
            }
        }
        dialog.show()
    }

    private fun selectDate(etDate: TextView?) {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog: DatePickerDialog = @RequiresApi(Build.VERSION_CODES.O)
        object : DatePickerDialog(
            requireActivity(),
            OnDateSetListener { view: DatePicker?, yearSelected: Int, monthOfYear: Int, dayOfMonthSelected: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate[yearSelected, monthOfYear] = dayOfMonthSelected
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = outputFormat.format(selectedDate.time)
                if (rdDateList.contains(formattedDate)) {
                    try {
                        // etDate!!.text = StringBuilder().append(dayOfMonthSelected).append("/").append(month + 1).append("/").append(year).append(" ")
                        //  etDate!!.text = formattedDate
                        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val outputFormat1 = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        val selectedDate1: LocalDate = LocalDate.parse(formattedDate, inputFormat)
                        val convertedDateStr = outputFormat1.format(selectedDate1)
                        etDate!!.text = convertedDateStr

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
                    // Date is not valid, reset the DatePicker to the previous valid date
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

    private fun updateViews(
        flexboxLayout: FlexboxLayout?,
        ratingList: ArrayList<String>,
        et: EditText?
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
                val textView = TextView(activity)
                textView.id = i
                textView.layoutParams = params
                textView.background = this.resources.getDrawable(R.drawable.rectangle_grey_new)
                textView.setTextColor(this.resources.getColor(R.color.Darkgrey))
                textView.setPadding(40, 20, 40, 20)
                textView.text = "" + ratingList[i]
                viewList.add(textView)
                textView.setOnClickListener { view: View ->
                    reason = textView.text.toString()
                    if (reason == "Other") {
                        et!!.visibility = View.VISIBLE
                    } else {
                        et!!.visibility = View.GONE
                    }
                    println("Reason - $reason")
                    for (view1 in viewList) {
                        view1.background = this.resources.getDrawable(R.drawable.rectangle_grey_new)
                        view1.setTextColor(this.resources.getColor(R.color.Darkgrey))
                    }
                    view.background = this.resources.getDrawable(R.drawable.rectangle_blue)
                    textView.setTextColor(this.resources.getColor(R.color.blue_dialog))
                }
                flexboxLayout.addView(textView)
            }
        }
    }

    private fun skipBottomBar(myTripOrderResponseModel: MyTripOrderResponseModel) {
        val bottomSkipLayoutBinding = DataBindingUtil.inflate<BottomSkipLayoutBinding>(
            layoutInflater, R.layout.bottom_skip_layout, null, false
        )
        val bottomSkip = Dialog(requireContext(), R.style.CustomDialog)
        bottomSkip.setContentView(bottomSkipLayoutBinding.root)
        bottomSkip.setCancelable(true)
        bottomSkipLayoutBinding.shopName.text = myTripOrderResponseModel.shopname
        bottomSkip.setCancelable(true)
        bottomSkipLayoutBinding.btNo.setOnClickListener { bottomSkip.dismiss() }
        bottomSkipLayoutBinding.btYes.setOnClickListener {
            myTripViewModel!!.getSkipAll(TripPlannerConfirmedDetailId)
                .observe(requireActivity()) { apiResponse: ApiResponse ->
                    if (apiResponse.data != null) {
                        try {
                            val `object` = JSONObject(apiResponse.data.toString())
                            val skippAllApiResponse = Gson().fromJson(
                                `object`.toString(),
                                SkippAllApiResponse::class.java
                            )
                            Utils.setToast(activity, skippAllApiResponse.message)
                            if (skippAllApiResponse.isStatus) {
                                val i = Intent(activity, MainActivity::class.java)
                                requireActivity().startActivity(i)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            bottomSkip.dismiss()
        }
        bottomSkip.show()
    }

    override fun onSearch(s: String) {
        if (OrderList != null) {
            if (s.length > 0) {
                filter(s)
            } else {
                myListViewAdapter!!.updateList(OrderList!!)
            }
        } else {
            Log.e("List size", "null")
        }
    }

    override fun locationOn() {}
    override fun currentLocation(location: Location) {
        easyWayLocation!!.endUpdates()
        customerLatLng = LatLng(location.latitude, location.longitude)
        println("Location - $customerLatLng")
    }

    override fun locationCancelled() {}
    fun timer(time: Long, timer: TextView?, btResend: Button?, ischangeColor: Boolean) {
        cancelTimer()
        timer!!.visibility = View.VISIBLE
        btResend!!.visibility = View.GONE
        cTimer = object : CountDownTimer(time, 100) {
            override fun onTick(mills: Long) {
                if (mills >= 60000) {
                    timer.text = "" +
                            (TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(mills)
                            )) + ":" +
                            (TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(mills)
                            )) + " Min"
                } else {
                    timer.text = "" +
                            (TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(mills)
                            )) + " Sec"
                }
                /*if (SharePrefs.getInstance(OrderDetailActivity.this).getBoolean(SharePrefs.ISORDERCANCELED)) {
                    SharePrefs.getInstance(OrderDetailActivity.this).putLong(SharePrefs.REMAININGTIME, mills);
                }*/
            }

            override fun onFinish() {
                btResend.isEnabled = true
                if (ischangeColor) {
                    btResend.setTextColor(requireActivity().resources.getColor(R.color.orange_dark))
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

    private fun callGPSMethod() {
        val builder = LocationSettingsRequest.Builder()
        builder.setAlwaysShow(true)
        builder.addLocationRequest(createLocationRequest())
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse: LocationSettingsResponse? -> }
        task.addOnFailureListener { e: Exception? ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(
                        requireActivity(),
                        EasyWayLocation.LOCATION_SETTING_REQUEST_CODE
                    )
                } catch (sendEx: SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    // googleMapMain.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(activity, "permission denied", Toast.LENGTH_LONG).show()
            }
        }
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

    private fun getPreviousValidDate(currentDate: Calendar): Calendar {
        val previousValidDate = currentDate.clone() as Calendar
        while (!rdDateList.contains(formatDate(previousValidDate)!!)) {
            previousValidDate.add(Calendar.DAY_OF_MONTH, -1)
            Utils.setToast(getActivity(), "वेयरहाउस/ दुकान की छुट्टी है कृपया कोई अन्य तिथि चुनें")
        }
        return previousValidDate
    }

    private fun formatDate(date: Calendar): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(date.time)
    }

    /*  private fun choosePersonDilog(
          title: String,
          stringArrayList: ArrayList<String>,
          orderId: Int,

          ) {
          progressDialog!!.dismiss()
          choosePersonDilog = BottomSheetDialog(requireContext())
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
              showRedispatchDialog(stringArrayList, orderId)
          }

          btnDeliveryBoy!!.setOnClickListener {
              choosePersonDilog!!.dismiss()
              selectPersonTypeFlag = true
              showRedispatchDialog(stringArrayList, orderId)
          }

          choosePersonDilog!!.setOnDismissListener {
              try {

              } catch (e: Exception) {
                  e.printStackTrace()
              }
          }
      }
  */

    private fun showOTPofSalesPersonDilog(
        otpTimer: Long,
        message: String,
    ) {
        com.sk.ziladelivery.utilities.ProgressDialog.getInstance().dismiss()
        timerTime = otpTimer * 1000
        otpDialog = BottomSheetDialog(requireActivity())
        otpDialog!!.setContentView(R.layout.dialog_otp_verification)
        otpDialog!!.setCanceledOnTouchOutside(true)
        val otpEditBox1 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box1)
        val otpEditBox2 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box2)
        val otpEditBox3 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box3)
        val otpEditBox4 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box4)
        val tvHead = otpDialog!!.findViewById<TextView>(R.id.tvHead)
        val crossIV = otpDialog!!.findViewById<ImageView>(R.id.ivCross)
        val tvName = otpDialog!!.findViewById<TextView>(R.id.tvName)
        val tvTextViewSms = otpDialog!!.findViewById<TextView>(R.id.tvTextViewSms)
        tvTextViewSms!!.visibility = View.GONE
        tvName!!.visibility = View.VISIBLE
        tvHead!!.text = "Verification Code\nOrder Id - $orderIdInterface"
        tvName.text = message


        btnResendOtp = otpDialog!!.findViewById(R.id.btnResendOtp)
        val btnVerifyOtp = otpDialog!!.findViewById<Button>(R.id.btnVerifyOtp)
        tvTimerOTP = otpDialog!!.findViewById(R.id.tvTimer)
        btnResendOtp!!.isEnabled = false
        btnResendOtp!!.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
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
        }
        btnResendOtp!!.setOnClickListener {
            otpDialog!!.dismiss()
            val model = GenerateOTPofSalesPersonforReattemptRequestModel(
                orderIDList,
                selectedStatus!!,
                customerLatLng!!.latitude,
                customerLatLng!!.longitude
            )
            myTripViewModel!!.generateOTPofSalesPersonforReattempt(model)
        }
        btnVerifyOtp!!.setOnClickListener {
            val otp = (otpEditBox1.text.toString() +
                    otpEditBox2.text.toString() +
                    otpEditBox3.text.toString() + otpEditBox4.text.toString())
            if (otp.length < 4) {
                Utils.setToast(requireActivity(), "Enter Valid OTP")
            } else {
                myTripViewModel!!.verifyOrderOTP(orderIdInterface, selectedStatus, otp)
            }
        }
        otpDialog!!.show()
        otpDialog!!.setOnDismissListener {
            try {
                // customerWiseOrderListApi()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
