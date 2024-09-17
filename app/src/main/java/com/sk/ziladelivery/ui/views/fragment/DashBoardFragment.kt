package com.sk.ziladelivery.ui.views.fragment

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender.SendIntentException
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.AcceptModel
import com.sk.ziladelivery.data.model.AssignmentAcceptModel
import com.sk.ziladelivery.data.model.AssignmentEwayBillModel
import com.sk.ziladelivery.data.model.BackgroundServiceModel
import com.sk.ziladelivery.data.model.DashBoardResponseModel
import com.sk.ziladelivery.data.model.DashBoardResponseModel.AssignmentlistEntity
import com.sk.ziladelivery.data.model.SendCloseKmApproval
import com.sk.ziladelivery.data.model.StartAssignmentPostModel
import com.sk.ziladelivery.data.model.TokenResponse
import com.sk.ziladelivery.databinding.ActivityTransparentBinding
import com.sk.ziladelivery.databinding.BottomAcceptAssignmentBinding
import com.sk.ziladelivery.databinding.DeliveryHomeFragmentBinding
import com.sk.ziladelivery.databinding.ImageUploadPopupBinding
import com.sk.ziladelivery.databinding.ReachedPopupBinding
import com.sk.ziladelivery.databinding.RejectResonPopupBinding
import com.sk.ziladelivery.databinding.StartTripImageUploadPopupBinding
import com.sk.ziladelivery.listener.NewAcceptRejectAssignmenClick
import com.sk.ziladelivery.service.LocationServiceForBeat
import com.sk.ziladelivery.ui.views.adapter.AssinmentEbayBillAdapter
import com.sk.ziladelivery.ui.views.adapter.PendingTaskAdapter
import com.sk.ziladelivery.ui.views.main.LoginActivity
import com.sk.ziladelivery.ui.views.main.MainActivity
import com.sk.ziladelivery.ui.views.main.MainActivity.Companion.myTripView
import com.sk.ziladelivery.ui.views.main.MainActivity.Companion.startBreak
import com.sk.ziladelivery.ui.views.main.MainActivity.Companion.stopBreak
import com.sk.ziladelivery.ui.views.main.MainActivity.Companion.tvStartTime
import com.sk.ziladelivery.ui.views.main.MyTripActivity
import com.sk.ziladelivery.ui.views.viewmodels.DashBoardViewModel
import com.sk.ziladelivery.utilities.CommonMethods
import com.sk.ziladelivery.utilities.Constant
import com.sk.ziladelivery.utilities.EasyWayLocation
import com.sk.ziladelivery.utilities.Logger
import com.sk.ziladelivery.utilities.MyApplication
import com.sk.ziladelivery.utilities.ProgressDialog
import com.sk.ziladelivery.utilities.RxBus
import com.sk.ziladelivery.utilities.SharePrefs
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.TextUtils
import com.sk.ziladelivery.utilities.TripStartEnum
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.utilities.YourService
import com.sk.ziladelivery.viewfactory.DashBoardViewFactory
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class DashBoardFragment : Fragment(), NewAcceptRejectAssignmenClick {
    private var activity: MainActivity? = null
    private var mBinding: DeliveryHomeFragmentBinding? = null
    private var dashBoardViewModel: DashBoardViewModel? = null
    private var assignmentList: ArrayList<AssignmentlistEntity>? = null
    private var isview = false
    private var isStop = false
    private var isImageUploaded = false
    private val isUserRagistred = false
    private var isTimerPaused = false
    private var isStartTripImageTaking = false
    private var customDialog: Dialog? = null
    private var popupDialog: Dialog? = null
    private var ZilaTripMasterId = 0
    private var tripPlannerConfirmedDetailId = 0
    private var tripId = 0
    private var breakTime = 0
    private var maxEndKm = 0
    private var time: String? = null
    private var imageUrl: String? = null
    private var startTripImageURL: String? = null
    private var hms: String? = null
    private var imageFilePath: String? = null
    private var imageStartTripFilePath: String? = null
    private var customerDocUpload = ""
    private var startTripDocUpload: String? = null
    private var milovalue = ""
    private var tripMiloValue = ""
    private val FORMAT = "%02d:%02d:%02d"
    private var startKM = 0.0
    private var reachedDialog: Dialog? = null
    private var millisUntilFinished: Long = 0
    private var handler: Handler? = Handler(Looper.getMainLooper())
    private var mServiceIntent: Intent? = null
    private var locationServiceForBeat: LocationServiceForBeat? = null
    private var compressionDisposable: Disposable? = null



    companion object {
        private const val CAPTURE_IMAGE_CAMERA = 1111
        private const val CAPTURE_IMAGE_CAMERA_START = 2222
        const val REQUEST_IMAGE = 100
        const val REQUEST_IMAGE_START_TRIP = 101
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DeliveryHomeFragmentBinding.inflate(inflater, container, false)
        initView()
        return mBinding!!.root
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance((activity)!!).registerReceiver(mMessageReceiver, IntentFilter("LocationFound"))
    }

    override fun onPause() {
        super.onPause()
        stop()
        LocalBroadcastManager.getInstance((activity)!!).unregisterReceiver(mMessageReceiver)
        if (reachedDialog != null) {
            if (reachedDialog!!.isShowing) {
                reachedDialog!!.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (compressionDisposable != null) {
            compressionDisposable!!.dispose()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EasyWayLocation.LOCATION_SETTING_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                currentTripStatus(ZilaTripMasterId)
            } else {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(activity, "Please Turn On GPS")
            }
        } else if (requestCode == EasyWayLocation.PENDDING_LOCTION) {
            if (resultCode == Activity.RESULT_OK) {
                callForImage(CAPTURE_IMAGE_CAMERA_START)
            } else {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(getActivity(), "Please Turn On GPS")
            }
        } else if (resultCode == EasyWayLocation.METER_READING_LOCTION) {
            if (resultCode == Activity.RESULT_OK) {
                callForImage(CAPTURE_IMAGE_CAMERA)
            } else {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(getActivity(), "Please Turn On GPS")
            }
        } else {
            if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
                uploadMultipart()
            } else if (requestCode == REQUEST_IMAGE_START_TRIP && resultCode == Activity.RESULT_OK) {
                uploadStartTripFilePth()
            }
        }
    }


    override fun viewAssignmentClicked(deliveryIssuanceId: Int) {
        val bundle = Bundle()
        val fragment: Fragment = AssignmentDetailFragment()
        bundle.putInt("deliveryIssuanceId", deliveryIssuanceId)
        bundle.putString("time", time)
        fragment.arguments = bundle
        switchContentWithStack(fragment)
    }

    override fun acceptClicked(deliveryIssuanceId: Int, arValue: String) {
        if (!Utils.checkInternetConnection(getActivity())) {
            Utils.setToast(getActivity(), resources.getString(R.string.network_error))
        } else {
            val acceptModel = AcceptModel(deliveryIssuanceId, arValue, "")
            acceptPenddingTaskApi(acceptModel)

        }
    }

    override fun rejectClicked(deliveryIssuanceId: Int, aTrue: String) {
        rejectPopup(deliveryIssuanceId, aTrue)
    }


    private fun initView() {
        assignmentList = ArrayList()
        mBinding!!.rvAssignment.layoutManager = LinearLayoutManager(activity)

        dashBoardViewModel = ViewModelProvider(
            viewModelStore,
            DashBoardViewFactory(ApiHelper(RestClient.getInstance().service))
        )[DashBoardViewModel::class.java]
        setActionBarConfiguration()
        notificationEvent
        clickListenerEvent()
        dashBoardDataApi(SharePrefs.getInstance(getActivity()).getLong(SharePrefs.ALL_TRIP_SLECTED))

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

    private fun clickListenerEvent() {
        mBinding!!.llView.setOnClickListener {
            if (!isview) {
                mBinding!!.rvAssignment.visibility = View.GONE
                isview = true
                mBinding!!.ivArrow.rotation = 270f
            } else {
                mBinding!!.rvAssignment.visibility = View.VISIBLE
                isview = false
                mBinding!!.ivArrow.rotation = 0f
            }
        }
        mBinding!!.startTrip.setOnClickListener {
            /*checkGPSNetworkMethod()*/

            val postModel = StartAssignmentPostModel(
                                     ZilaTripMasterId,
                                     Utils.getDoubleLat(activity),
                                     Utils.getDoubleLag(activity),
                                     "",
                                     0,
                                     SharePrefs.getInstance(activity)
                                         .getInt(SharePrefs.PEOPLE_ID)
                                 )
                                 startTripApi(postModel)
        }
        mBinding!!.tripEnd.setOnClickListener {
            AlertDialog.Builder((getActivity())!!)
                .setTitle("Alert")
                .setMessage("Are you sure you want to end the trip ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes) { dialog, whichButton ->
                    if (!Utils.checkInternetConnection(getActivity())) {
                        Utils.setToast(
                            getActivity(),
                            resources.getString(R.string.network_error)
                        )
                    } else {
                        val postModel = StartAssignmentPostModel(
                            ZilaTripMasterId.toLong(),
                            Utils.getDoubleLat(activity),
                            Utils.getDoubleLag(activity),
                            true,
                            SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)
                        )

                        tripEndApi(postModel)
                        SharePrefs.setMapRouteSharedPreference(
                            activity,
                            SharePrefs.MAP_ROUTE,
                            null
                        )
                    }
                }
                .setNegativeButton(
                    android.R.string.no
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                    // customDialog.dismiss();
                }.show()
        }

        mBinding!!.tvAddMeterReading.setOnClickListener { checkAddMeterGPSNetworkMethod() }
        mBinding!!.swipeContainer.setOnRefreshListener {
            dashBoardDataApi(
                SharePrefs.getInstance(getActivity()).getLong(SharePrefs.ALL_TRIP_SLECTED)
            )
            mBinding!!.swipeContainer.isRefreshing = false
        }
        mBinding!!.inprogressTrip.setOnClickListener {
            ProgressDialog.getInstance().show(getActivity())
            permissionsDialog()
        }

        startBreak!!.setOnClickListener {
            AlertDialog.Builder((activity)!!)
                .setTitle("")
                .setMessage("Do you really want to Start The Break?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                    android.R.string.yes
                ) { dialog, whichButton ->
                    if (!Utils.checkInternetConnection(getActivity())) {
                        Utils.setToast(
                            getActivity(),
                            resources.getString(R.string.network_error)
                        )
                    } else {
                        if (Utils.getDoubleLat(activity) != 0.0 && Utils.getDoubleLag(
                                activity
                            ) != 0.0
                        ) {
                            val postModel = StartAssignmentPostModel(
                                ZilaTripMasterId,
                                Utils.getDoubleLat(activity),
                                Utils.getDoubleLag(activity)
                            )
                            postStartBreakApi(postModel)
                        } else {
                            Log.e("TAG", "Lat Long Null: ")
                        }
                    }
                }
                .setNegativeButton(android.R.string.no, null).show()
        }
        stopBreak!!.setOnClickListener {
            AlertDialog.Builder((requireActivity()))
                .setTitle("")
                .setMessage("Do you really want to Stop The Break?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                    android.R.string.yes
                ) { dialog, whichButton ->
                    if (!Utils.checkInternetConnection(getActivity())) {
                        Utils.setToast(
                            getActivity(),
                            resources.getString(R.string.network_error)
                        )
                    } else {
                        if (Utils.getDoubleLat(activity) != 0.0 && Utils.getDoubleLag(
                                activity
                            ) != 0.0
                        ) {
                            val postModel = StartAssignmentPostModel(
                                ZilaTripMasterId,
                                Utils.getDoubleLat(activity),
                                Utils.getDoubleLag(activity)
                            )
                            stopBreakTrip(postModel)

                        } else {
                            Log.e("TAG", "Lat Long Null: ")
                        }
                    }
                }
                .setNegativeButton(android.R.string.no, null).show()
        }
        myTripView!!.setOnClickListener {
            stop()
            //activity?.switchContent(AddOrderFragment())
            activity?.addFragment(AddOrderFragment(), false, null)
        }

    }

    private fun tripEndApi(postModel: StartAssignmentPostModel) {
        dashBoardViewModel!!.PostEndTripAPI(
            postModel
        ).observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        resource.data?.let { endTripResponce ->
                            endTripMethod(endTripResponce)
                        }
                    }

                    Status.ERROR -> {
                        ProgressDialog.getInstance().dismiss()
                        // Utils.setToast(requireActivity(), it.message)
                    }

                    Status.LOADING -> {
                        ProgressDialog.getInstance().show(getActivity())

                    }
                }
            }
        }
    }

    private fun endTripMethod(response: JsonObject) {
        if (response.asJsonObject["Status"].asBoolean) {
            Utils.setToast(requireActivity(), response.asJsonObject["Message"].asString)
            isStop = true
            val myService = Intent(activity, YourService::class.java)
            requireActivity().stopService(myService)
            val mLocationIntent = Intent(activity, LocationServiceForBeat::class.java)
            requireActivity().stopService(mLocationIntent)
            locationServiceForBeat = null
            startActivity(Intent(getActivity(), MainActivity::class.java))
        } else {
            Utils.setToast(requireActivity(), response.asJsonObject["Message"].asString)
        }

    }

    private fun stopBreakTrip(postModel: StartAssignmentPostModel) {
        dashBoardViewModel!!.PoststopBreakAPI(
            postModel
        ).observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        resource.data?.let { stopBreakApiResponce ->
                            getBrakMethod(stopBreakApiResponce)

                        }
                    }

                    Status.ERROR -> {
                        ProgressDialog.getInstance().dismiss()
                        Utils.setToast(requireActivity(), it.message)
                    }

                    Status.LOADING -> {
                        ProgressDialog.getInstance().show(getActivity())

                    }
                }
            }
        }
    }

    private fun postStartBreakApi(postModel: StartAssignmentPostModel) {
        dashBoardViewModel!!.PostStartBreakAPI(
            postModel
        ).observe(requireActivity()) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        resource.data?.let { strtBreakApiResponce ->
                            getStartBreakTrip(strtBreakApiResponce)
                        }
                    }

                    Status.ERROR -> {
                        ProgressDialog.getInstance().dismiss()
                        Utils.setToast(requireActivity(), it.message)
                    }

                    Status.LOADING -> {
                        ProgressDialog.getInstance().show(getActivity())

                    }
                }
            }
        }
    }

    private fun getStartBreakTrip(response: JsonObject) {
        if (!response.isJsonNull()) {
            isTimerPaused = true
            val model = BackgroundServiceModel(
                SharePrefs.getInstance(activity).getInt(SharePrefs.TripPlannerVehicleId),
                Utils.getLat(activity),
                Utils.getLog(activity),
                4,
                Date().toString(),
                SharePrefs.getInstance(activity).getInt(SharePrefs.TripPlannerConfirmedDetailId),
                0
            )
            SharePrefs.setCashmanagmentSharedPreference(
                activity,
                SharePrefs.BACKGROUND_DATA,
                Gson().toJson(model)
            )
            BreakPopup()
        } else {
            Utils.setToast(requireActivity(), resources.getString(R.string.errorString))
        }
    }

    private fun getBrakMethod(response: JsonObject) {
        isTimerPaused = false
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull) {
                if (popupDialog != null) {
                    popupDialog!!.dismiss()
                    startActivity(Intent(activity, MainActivity::class.java))
                }
            } else {
                Utils.setToast(requireActivity(), resources.getString(R.string.errorString))
            }
        } else {
            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTripApi(postModel: StartAssignmentPostModel) {
        dashBoardViewModel!!.PostStartTripAPI(
            postModel
        ).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        resource.data?.let { startTripResponce ->
                            startTriprenderSuccessResponse(startTripResponce)
                        }
                    }

                    Status.ERROR -> {
                        ProgressDialog.getInstance().dismiss()
                        Utils.setToast(requireActivity(), it.message)
                    }

                    Status.LOADING -> {
                        ProgressDialog.getInstance().show(getActivity())


                    }
                }
            }
        }

    }

    private fun dashBoardDataApi(ZilaTripMasterId: Long) {
        dashBoardViewModel!!.getDashboardData(
            SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID),
            ZilaTripMasterId
        ).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        resource.data?.let { dashBoardDataResponce ->
                            renderSuccessResponse(dashBoardDataResponce)
                        }
                    }

                    Status.ERROR -> {
                        ProgressDialog.getInstance().dismiss()
                        if (it.message!! == "401") {
                            if (TextUtils.isNullOrEmpty(
                                    SharePrefs.getInstance(activity)
                                        .getString(SharePrefs.TOKEN_NAME)
                                )
                            ) {
                                PreferenceManager.getDefaultSharedPreferences(activity).edit()
                                    .clear().apply()
                                SharePrefs.getInstance(activity)
                                    .putBoolean(SharePrefs.LOGGED, false)
                                val intent = Intent(activity, LoginActivity::class.java)
                                intent.putExtra("Type", "ResetPasswordActivity")
                                startActivity(intent)
                                requireActivity().finish()
                            } else {
                                checkToken()
                            }
                        }
                    }

                    Status.LOADING -> {
                        ProgressDialog.getInstance().show(getActivity())
                    }
                }
            }
        }
    }

    private fun BreakPopup() {
        val activityTransparentBinding = DataBindingUtil.inflate<ActivityTransparentBinding>(
            layoutInflater,
            R.layout.activity_transparent,
            null,
            false
        )
        popupDialog = Dialog((getActivity())!!, R.style.CustomDialog)
        popupDialog!!.setContentView(activityTransparentBinding.root)
        popupDialog!!.setCancelable(false)
        activityTransparentBinding.cvStop.setOnClickListener {
            val postModel = StartAssignmentPostModel(
                ZilaTripMasterId,
                Utils.getDoubleLat(activity),
                Utils.getDoubleLag(activity)
            )
            stopBreakTrip(postModel)
            popupDialog!!.dismiss()
        }
        popupDialog!!.show()
    }

    private fun currentStatusrenderSuccessResponse(response: JsonObject) {
        if (!response.isJsonNull) {
            try {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject != null) {
                    val CustomerTripStatus = jsonObject.getInt("CustomerTripStatus")
                    val intent = Intent(activity, MyTripActivity::class.java)
                    intent.putExtra("ZilaTripMasterId", ZilaTripMasterId)
                    intent.putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, tripPlannerConfirmedDetailId)
                    intent.putExtra("ORDER_ID", jsonObject.getInt("OrderId"))
                    intent.putExtra("CustomerTripStatus", CustomerTripStatus)
                    intent.putExtra("time", time)
                    intent.putExtra("isUserRagistred", isUserRagistred)
                    requireActivity().startActivity(intent)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Utils.setToast(requireActivity(), resources.getString(R.string.errorString))
        }
    }

    private fun acceptPenddingTaskApi(acceptModel: AcceptModel) {
        dashBoardViewModel!!.getAcceptPendingMyTaskAdi(acceptModel)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { acceptPendingTaskResponce ->
                                acceptPendingData(acceptPendingTaskResponce)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(requireActivity(), it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(getActivity())
                        }
                    }
                }
            }
    }

    private fun acceptPendingData(assignmentAcceptModel: AssignmentAcceptModel) {
        if (assignmentAcceptModel.isStatus()) {
            if (assignmentAcceptModel.ewayBillModels != null && assignmentAcceptModel.ewayBillModels.size > 0
            ) {
                assignmentAcceptBottomBar(assignmentAcceptModel.ewayBillModels)
            } else {
                startActivity(Intent(activity, MainActivity::class.java))
            }
        } else {
            Utils.setToast(requireActivity(), assignmentAcceptModel.message)
        }

    }

    private val notificationEvent: Unit
        get() {
            RxBus.getInstance().event.observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Any?>() {
                    override fun onNext(o: Any) {
                        if (o is Boolean) {
                            if (o) {
                                Utils.setToast(activity, "Accepted")
                                dashBoardDataApi(
                                    SharePrefs.getInstance(getActivity())
                                        .getLong(SharePrefs.ALL_TRIP_SLECTED)
                                )
                            } else {
                                Utils.setToast(activity, "Rejected")
                                dashBoardDataApi(
                                    SharePrefs.getInstance(getActivity())
                                        .getLong(SharePrefs.ALL_TRIP_SLECTED)
                                )
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onComplete() {}
                })
        }


    private fun miloLimitSuccessResponse(response: JsonObject) {
        if (!response.isJsonNull) {
            try {
                val jsonObject = JSONObject(response.toString())
                maxEndKm = jsonObject.getInt("MaxEndKm")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Utils.setToast(requireActivity(), resources.getString(R.string.errorString))
        }

    }

    private fun closeKmApprovalRequestResponse(response: Boolean) {
        if (response) {
            try {
                dashBoardDataApi(
                    SharePrefs.getInstance(getActivity()).getLong(SharePrefs.ALL_TRIP_SLECTED)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun tripStartStatus(tripStatus: Int) {
        val workingStatus = TripStartEnum.from(tripStatus)
        when (workingStatus) {
            TripStartEnum.ACCEPT -> ViewHideShow(
                View.GONE,
                View.GONE,
                View.GONE,
                View.GONE,
                View.GONE,
                "",
                View.GONE,
                View.GONE,
            )

            TripStartEnum.ACCEPT_ASSIGNMENT_PENDING -> ViewHideShow(
                View.GONE,
                View.GONE,
                View.GONE,
                View.GONE,
                View.VISIBLE,
                "Accept Assignment Pending",
                View.GONE,
                View.GONE,
            )

            TripStartEnum.START_TRIP -> {
                mBinding!!.startTrip.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.start_green
                )
                mBinding!!.startTrip.isClickable = true
                mBinding!!.startTrip.isFocusable = true
                ViewHideShow(
                    View.VISIBLE,
                    View.GONE,
                    View.GONE,
                    View.GONE,
                    View.GONE,
                    "",
                    View.GONE,
                    View.GONE,
                )
            }

            TripStartEnum.START_KM_APPROVAL_PENDING -> ViewHideShow(
                View.GONE,
                View.GONE,
                View.GONE,
                View.GONE,
                View.VISIBLE,
                "Approval Pending",
                View.GONE,
                View.GONE,
            )

            TripStartEnum.INPROGRESS -> {
                ViewHideShow(
                    View.GONE,
                    View.VISIBLE,
                    View.GONE,
                    View.GONE,
                    View.GONE,
                    "",
                    View.VISIBLE,
                    View.GONE,
                )
               // mServiceIntent = Intent(activity, YourService::class.java)
                /*if (!isMyServiceRunning(YourService::class.java)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requireActivity().startForegroundService(mServiceIntent)
                    } else {
                        requireActivity().startService(mServiceIntent)
                    }
                }*/
                startLocationService()
            }

            TripStartEnum.SEND_CLOSE_KM_APPROVAL -> ViewHideShow(
                View.GONE,
                View.GONE,
                View.GONE,
                View.VISIBLE,
                View.GONE,
                "Add Milo Meter KM",
                View.GONE,
                View.GONE,
            )

            TripStartEnum.CLOSE_KM_APPROVAL_PENDING -> ViewHideShow(
                View.GONE,
                View.GONE,
                View.GONE,
                View.GONE,
                View.VISIBLE,
                "Close Km Approval Pending ",
                View.GONE,
                View.GONE,
            )

            TripStartEnum.END_TRIP -> ViewHideShow(
                View.GONE,
                View.GONE,
                View.VISIBLE,
                View.GONE,
                View.GONE,
                "",
                View.GONE,
                View.GONE,
            )

            TripStartEnum.ISSUE_IN_TRIP -> {}
            TripStartEnum.NOT_LAST_MILE_APP -> ViewHideShow(
                View.VISIBLE,
                View.GONE,
                View.GONE,
                View.GONE,
                View.GONE,
                "",
                View.GONE,
                View.GONE,
            )
            else -> {}
        }
    }

    private fun ViewHideShow(
        tStart: Int,
        inprogress: Int,
        tEnd: Int,
        MeterReading: Int,
        ApprovalPendding: Int,
        status: String,
        startBrak: Int,
        rearrange: Int,
    ) {
        mBinding!!.startTrip.visibility = tStart
        mBinding!!.inprogressTrip.visibility = inprogress
        mBinding!!.tripEnd.visibility = tEnd
        mBinding!!.tvAddMeterReading.visibility = MeterReading
        mBinding!!.tripApprovalPendding.visibility = ApprovalPendding
        mBinding!!.tripApprovalPendding.text = status
        startBreak!!.visibility = startBrak
    }

    private fun assignmentAcceptBottomBar(ewayBillList: ArrayList<AssignmentEwayBillModel>) {
        val assignmentBinding = DataBindingUtil.inflate<BottomAcceptAssignmentBinding>(
            layoutInflater, R.layout.bottom_accept_assignment, null, false
        )
        val dialog = Dialog((getActivity())!!, R.style.CustomDialog)
        dialog.setContentView(assignmentBinding.root)
        dialog.setCancelable(true)
        val ebayBillAdapter = AssinmentEbayBillAdapter(getActivity(), ewayBillList)
        assignmentBinding.rvEbayAssinment.adapter = ebayBillAdapter
        assignmentBinding.tvCancle.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun startTriprenderSuccessResponse(response: JsonObject) {
        if (response.asJsonObject["Status"].asBoolean) {
            Utils.setToast(requireActivity(), response.asJsonObject["Message"].asString)
            startActivity(Intent(getActivity(), MainActivity::class.java))
          /*  mServiceIntent = Intent(activity, YourService::class.java)
            if (!isMyServiceRunning(YourService::class.java)) {
                requireActivity().startService(mServiceIntent)
            }*/
        } else {
            Utils.setToast(requireActivity(), response.asJsonObject["Message"].asString)
        }
    }

    private fun pickFromCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File
        try {
            photoFile = createImageFile()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        val photoUri = FileProvider.getUriForFile(
            (activity)!!,
            requireActivity().packageName + ".provider",
            photoFile
        )
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        startActivityForResult(pictureIntent, REQUEST_IMAGE)
    }

    private fun createImageFile(): File {
        customerDocUpload = "trip" + tripId + "image" + ".jpg"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(storageDir, customerDocUpload)
        imageFilePath = file.absolutePath
        return file
    }

    private fun createImageFileStartTrip(): File {
        startTripDocUpload = "trip" + tripId + "image" + ".jpg"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(storageDir, startTripDocUpload)
        imageStartTripFilePath = file.absolutePath
        return file
    }

    private fun checkToken() {
        val username = SharePrefs.getInstance(activity).getString(SharePrefs.TOKEN_NAME)
        val password = SharePrefs.getInstance(activity).getString(SharePrefs.TOKEN_PASSWORD)
        hitTokenAPI("password", username, password)
    }

    private fun hitTokenAPI(password: String?, username: String?, Password: String?) {
        dashBoardViewModel!!.getTokenData(password, username, Password)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { tokenResponce ->
                                getTokenResponce(tokenResponce)
                            }
                        }

                        Status.ERROR -> {
                            //Utils.setToast(requireActivity(), it.message)
                        }

                        Status.LOADING -> {

                        }
                    }
                }
            }


    }

    private fun getTokenResponce(tokenResponse: TokenResponse) {
        SharePrefs.getInstance(activity).putString(SharePrefs.TOKEN, tokenResponse.access_token)
        startActivity(Intent(activity, MainActivity::class.java))
        requireActivity().finish()
    }

    private fun renderSuccessResponse(dashBoardResponseModel: DashBoardResponseModel) {
        if (dashBoardResponseModel.status && dashBoardResponseModel.tripdashboarddc != null) {
            assignmentList!!.clear()
            mBinding!!.tvNoTrip.visibility = View.GONE
            mBinding!!.layout1.visibility = View.VISIBLE
            if (dashBoardResponseModel.tripdashboarddc.currentstatus == 4) {
                BreakPopup()
            }
            var remainingprogress = 0.0
            var travelProgress = 0.0
            if (dashBoardResponseModel.tripdashboarddc.tripplannerdistance.reminingtime != 0 && dashBoardResponseModel.tripdashboarddc.tripplannerdistance.totalTime != 0) {
                remainingprogress =
                    ((dashBoardResponseModel.tripdashboarddc.tripplannerdistance.reminingtime * 100) / dashBoardResponseModel.tripdashboarddc.tripplannerdistance.totalTime).toDouble()
            }
            if (dashBoardResponseModel.tripdashboarddc.tripplannerdistance.traveltime != 0 && dashBoardResponseModel.tripdashboarddc.tripplannerdistance.totalTime != 0) {
                travelProgress =
                    ((dashBoardResponseModel.tripdashboarddc.tripplannerdistance.traveltime * 100) / dashBoardResponseModel.tripdashboarddc.tripplannerdistance.totalTime).toDouble()
            }
            startKM = dashBoardResponseModel.tripdashboarddc.startKm
            mBinding!!.progressBarTravel.progress = travelProgress.toInt()
            mBinding!!.progressBar.progress = remainingprogress.toInt()
            val df = DecimalFormat("#.#")
            SharePrefs.getInstance(activity).putInt(
                SharePrefs.TripPlannerVehicleId,
                dashBoardResponseModel.tripdashboarddc.tripPlannerVehicleId
            )
            SharePrefs.getInstance(activity).putInt(
                SharePrefs.TRIP_ID,
                dashBoardResponseModel.tripdashboarddc.mytrip.tripid
            )
            SharePrefs.getInstance(activity).putInt(
                SharePrefs.TripPlannerConfirmedDetailId,
                dashBoardResponseModel.tripdashboarddc.tripPlannerConfirmedDetailId
            )
            SharePrefs.getInstance(activity).putInt(
                SharePrefs.RecordStatus,
                dashBoardResponseModel.tripdashboarddc.currentstatus
            )
            SharePrefs.getInstance(activity).putInt(
                SharePrefs.OTP_TIME,
                dashBoardResponseModel.tripdashboarddc.approveNotifyTimeLeftInMinute
            )
            SharePrefs.getInstance(activity).putInt(
                SharePrefs.TRIP_MASTER_ID,
                dashBoardResponseModel.tripdashboarddc.tripplannerconfirmedmasterid
            )
            mBinding!!.tvDistanceTraveled.text =
                df.format(dashBoardResponseModel.tripdashboarddc.tripplannerdistance.distancetraveled) + " KM"
            mBinding!!.tvDisleft.text =
                dashBoardResponseModel.tripdashboarddc.tripplannerdistance.distanceleft.toString() + " KM"
            mBinding!!.tvTotalOrder.text =
                dashBoardResponseModel.tripdashboarddc.mytrip.totalorder.toString() + ""
            mBinding!!.tvTotalCustomer.text =
                dashBoardResponseModel.tripdashboarddc.mytrip.customerCount.toString() + ""
            mBinding!!.tvTotalAmt.text =
                Math.round(dashBoardResponseModel.tripdashboarddc.mytrip.totalamount)
                    .toString() + ""
            mBinding!!.tvTripId.text =
                "Trip ID: " + dashBoardResponseModel.tripdashboarddc.mytrip.tripid
            val travelhrs =
                dashBoardResponseModel.tripdashboarddc.tripplannerdistance.traveltime / 60
            val travelmin =
                dashBoardResponseModel.tripdashboarddc.tripplannerdistance.traveltime % 60
            val remaininghrs =
                dashBoardResponseModel.tripdashboarddc.tripplannerdistance.reminingtime / 60
            val remainingmin =
                dashBoardResponseModel.tripdashboarddc.tripplannerdistance.reminingtime % 60
            mBinding!!.tvTravelTime.text = "$travelhrs h : $travelmin m"
            mBinding!!.tvRemainingTime.text = "$remaininghrs h : $remainingmin m"
            mBinding!!.tvOrderCount.text =
                dashBoardResponseModel.tripdashboarddc.orderstatuslist.totaldeliveryredispatchorder.toString() + ""
            mBinding!!.tvOrderAmt.text =
                "₹" + dashBoardResponseModel.tripdashboarddc.orderstatuslist.totaldeliveryredispatchamount + ""
            mBinding!!.tvOrderCountCancelled.text =
                dashBoardResponseModel.tripdashboarddc.orderstatuslist.totaldeliverycanceledorder.toString() + ""
            mBinding!!.tvOrderAmtCancelled.text =
                "₹" + dashBoardResponseModel.tripdashboarddc.orderstatuslist.totaldeliverycanceledamount + ""
            mBinding!!.tvOrderCountDelivered.text =
                dashBoardResponseModel.tripdashboarddc.orderstatuslist.totaldeliveredorder.toString() + ""
            mBinding!!.tvOrderAmtDelivered.text =
                "₹" + dashBoardResponseModel.tripdashboarddc.orderstatuslist.totaldeliveredamount + ""
            mBinding!!.tvTotalShippedAmt.text =
                "₹" + dashBoardResponseModel.tripdashboarddc.orderstatuslist.totalshippedamount + ""
            mBinding!!.tvShippedOrderCount.text =
                dashBoardResponseModel.tripdashboarddc.orderstatuslist.totalshippedorder.toString() + ""
            mBinding!!.tvReOrderCount.text =
                dashBoardResponseModel.tripdashboarddc.orderstatuslist.totalReAttemptOrder.toString() + ""
            mBinding!!.tvReOrderAmt.text =
                dashBoardResponseModel.tripdashboarddc.orderstatuslist.totalReAttemptAmount.toString() + ""
            if (dashBoardResponseModel.tripdashboarddc.tripplannerdistance.starttime != null) {
                mBinding!!.tvTripStartTime.text =
                    Utils.formatToYesterdayOrToday(dashBoardResponseModel.tripdashboarddc.tripplannerdistance.starttime) + " " + Utils.getTimeForChat(
                        dashBoardResponseModel.tripdashboarddc.tripplannerdistance.starttime
                    )
            }
            tripId = dashBoardResponseModel.tripdashboarddc.mytrip.tripid
            ZilaTripMasterId =
                dashBoardResponseModel.tripdashboarddc.tripplannerconfirmedmasterid
            tripPlannerConfirmedDetailId =
                dashBoardResponseModel.tripdashboarddc.tripPlannerConfirmedDetailId
            SharePrefs.getInstance(activity)
                .putInt(SharePrefs.TripPlannerConfirmedMasterId, ZilaTripMasterId)
            time = dashBoardResponseModel.tripdashboarddc.tripplannerdistance.starttime
            breakTime = dashBoardResponseModel.tripdashboarddc.breakTimeInSec
            assignmentList = dashBoardResponseModel.tripdashboarddc.assignmentlist
            if (Utils.getDoubleLat(activity) != 0.0 && Utils.getDoubleLag(activity) != 0.0) {
                val model = BackgroundServiceModel(
                    dashBoardResponseModel.tripdashboarddc.tripPlannerVehicleId,
                    Utils.getDoubleLat(activity),
                    Utils.getDoubleLag(activity),
                    dashBoardResponseModel.tripdashboarddc.currentstatus,
                    Date().toString(),
                    dashBoardResponseModel.tripdashboarddc.tripPlannerConfirmedDetailId,
                    0
                )
                SharePrefs.setCashmanagmentSharedPreference(
                    activity,
                    SharePrefs.BACKGROUND_DATA,
                    Gson().toJson(model)
                )
            }
            tripStartStatus(dashBoardResponseModel.tripdashboarddc.tripWorkingStatus)
            setActionBarConfiguration()
            val adapter = PendingTaskAdapter((getActivity())!!, assignmentList, this)
            mBinding!!.rvAssignment.adapter = adapter
        } else {
            mBinding!!.tvNoTrip.visibility = View.VISIBLE
            mBinding!!.layout1.visibility = View.GONE
            Toast.makeText(activity, dashBoardResponseModel.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service: ActivityManager.RunningServiceInfo in manager.getRunningServices(Int.MAX_VALUE)) {
            if ((serviceClass.name == service.service.className)) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }

    private fun rejectPopup(deliveryIssuanceId: Int, aTrue: String) {
        val rejectResonPopupBinding = DataBindingUtil.inflate<RejectResonPopupBinding>(
            layoutInflater, R.layout.reject_reson_popup, null, false
        )
        customDialog = Dialog((getActivity())!!, R.style.CustomDialog)
        customDialog!!.setContentView(rejectResonPopupBinding.root)
        val submit = rejectResonPopupBinding.submit
        val dissmiss = rejectResonPopupBinding.dissmiss
        val spReason = rejectResonPopupBinding.spReason
        val statusAdapter = ArrayAdapter.createFromResource(
            (getActivity())!!,
            R.array.reason,
            android.R.layout.simple_spinner_dropdown_item
        )
        spReason.adapter = statusAdapter
        spReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if (spReason.selectedItem.toString().equals("Others", ignoreCase = true)) {
                    rejectResonPopupBinding.llEt.visibility = View.VISIBLE
                } else {
                    rejectResonPopupBinding.llEt.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }
        submit.setOnClickListener { v: View? ->
            val sReason: String = spReason.getSelectedItem().toString()
            val comment: String = rejectResonPopupBinding.etComment.getText().toString()
            if (sReason.equals("Select Reason", ignoreCase = true)) {
                Utils.setToast(getActivity(), getResources().getString(R.string.plz_select_reason))
            } else if (rejectResonPopupBinding.llEt.visibility == View.VISIBLE && comment.isEmpty()) {
                Utils.setToast(activity, " First enter Remarks")
            } else {
                if (!Utils.checkInternetConnection(getActivity())) {
                    Utils.setToast(getActivity(), getResources().getString(R.string.network_error))
                } else {
                    val acceptModel = AcceptModel(deliveryIssuanceId, aTrue, sReason)
                    rejectAssignmentApi(acceptModel)
                    customDialog!!.dismiss()
                }
            }
        }
        dissmiss.setOnClickListener { v: View? -> customDialog!!.dismiss() }
        customDialog!!.show()
    }

    private fun rejectAssignmentApi(acceptModel: AcceptModel) {
        dashBoardViewModel!!.getRejectAssignment(acceptModel)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { rejectPendingTaskResponce ->
                                if (rejectPendingTaskResponce.asJsonObject["Status"].asBoolean) {
                                    Utils.setToast(
                                        requireActivity(),
                                        rejectPendingTaskResponce.asJsonObject["Message"].asString
                                    )
                                    startActivity(Intent(getActivity(), MainActivity::class.java))
                                } else {
                                    Utils.setToast(
                                        requireActivity(),
                                        rejectPendingTaskResponce.asJsonObject["Message"].asString
                                    )
                                }
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(requireActivity(), it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(getActivity())
                        }
                    }
                }
            }
    }

    private fun callForImage(imageResource: Int) {
        try {
            val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } else {
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
            Permissions.check(
                activity,
                permissions,
                null,
                null,
                object : PermissionHandler() {
                    override fun onGranted() {
                        if (imageResource == CAPTURE_IMAGE_CAMERA) {
                            pickFromCamera()
                        } else {
                            pickImageFromStartTrip()
                        }
                    }

                    override fun onDenied(
                        context: Context,
                        deniedPermissions: ArrayList<String>
                    ) {
                    }
                })

        } catch (e: Exception) {
            Logger.e(CommonMethods.getTag(this), e.toString())
        }
    }

    private fun pickImageFromStartTrip() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File
        try {
            photoFile = createImageFileStartTrip()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        val photoUri = FileProvider.getUriForFile(
            (activity)!!,
            requireActivity().packageName + ".provider",
            photoFile
        )
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(pictureIntent, REQUEST_IMAGE_START_TRIP)
    }

    private fun currentTripStatus(ZilaTripMasterId: Int) {
        dashBoardViewModel!!.GetTripCurrentStatusAPI(ZilaTripMasterId)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { currentTripStatus ->
                                currentStatusrenderSuccessResponse(currentTripStatus)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(requireActivity(), it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(getActivity())
                        }
                    }
                }
            }

    }

    private fun uploadStartTripFilePth() {
        val fileToUpload = imageStartTripFilePath?.let { File(it) }

        if (fileToUpload == null || imageStartTripFilePath.isNullOrEmpty()) {
            return
        }

        compressionDisposable = Compressor(requireActivity())
            .setQuality(90)
            .setCompressFormat(Bitmap.CompressFormat.JPEG)
            .compressToFileAsFlowable(fileToUpload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ file: File ->
                uploadImagePath(file)
            }, { throwable: Throwable ->
                throwable.printStackTrace()
                Utils.setToast(requireActivity(), "Image compression failed: ${throwable.message}")
            })
    }


    private fun uploadImagePath(file: File) {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body: MultipartBody.Part = createFormData("file", file.name, requestFile)

        dashBoardViewModel!!.UploadStartTripMiloImage(body)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { startTripImagePath ->
                                startTripUploadPath(startTripImagePath)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(requireActivity(), it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(getActivity())
                        }
                    }
                }
            }
    }

    private fun startTripUploadPath(startTripImagePath: String) {
        isStartTripImageTaking = true
        startTripImageURL = startTripImagePath
        startTripImageUploadPopup(startTripImagePath)
    }

    private fun uploadMultipart() {
        val fileToUpload = File(imageFilePath)
        if (fileToUpload == null && imageFilePath.isNullOrEmpty())
            return
        Compressor(requireActivity())
            .setQuality(90)
            .setCompressFormat(Bitmap.CompressFormat.JPEG)
            .compressToFileAsFlowable(fileToUpload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ file: File ->
                miloMeterReading(file)
            }) { throwable: Throwable ->
                throwable.printStackTrace()
                Utils.setToast(requireActivity(), "" + throwable.message)

            }
    }

    private fun miloMeterReading(file: File) {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body: MultipartBody.Part = createFormData("file", file.name, requestFile)

        dashBoardViewModel!!.miloMeterImageUpload(body)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { miloMeterImagePath ->
                                miloMterImage(miloMeterImagePath)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(requireActivity(), it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(getActivity())
                        }
                    }
                }
            }


    }

    private fun miloMterImage(miloMeterImagePath: String) {
        isImageUploaded = true
        imageUrl = miloMeterImagePath
        Log.e("Success", "Success$miloMeterImagePath")
        imageUploadPopup(miloMeterImagePath)

    }

    private fun startTripImageUploadPopup(response: String) {
        val imageUploadPopupBinding = DataBindingUtil.inflate<StartTripImageUploadPopupBinding>(
            layoutInflater,
            R.layout.start_trip_image_upload_popup,
            null,
            false
        )
        customDialog = Dialog((getActivity())!!, R.style.CustomDialog)
        customDialog!!.setContentView(imageUploadPopupBinding.root)
        customDialog!!.setCancelable(false)
        val submit = imageUploadPopupBinding.submit
        val image = imageUploadPopupBinding.ivImage
        val startKm = imageUploadPopupBinding.etStartkm
        Picasso.get().load(
            SharePrefs.getInstance(MyApplication.getInstance())
                .getString(SharePrefs.BASEURL) + response
        ).into(image)
        imageUploadPopupBinding.ivCross.setOnClickListener { customDialog!!.dismiss() }
        submit.setOnClickListener { v: View? ->
            tripMiloValue = startKm.text.toString()
            if (tripMiloValue.equals("", ignoreCase = true) || tripMiloValue.isEmpty()) {
                Utils.setToast(activity, "Enter start km Value")
            } else if (tripMiloValue.length > 0 && tripMiloValue.toDouble() == 0.0) {
                Utils.setToast(activity, "Value can not be zero")
            } else if (!isStartTripImageTaking) {
                Utils.setToast(activity, "Upload MiloMeter Image First!!")
            } else {
                AlertDialog.Builder((getActivity())!!)
                    .setTitle("Alert")
                    .setMessage("Are you sure this is real km?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(
                        android.R.string.yes
                    ) { dialog, whichButton ->
                        if (!Utils.checkInternetConnection(getActivity())) {
                            Utils.setToast(
                                getActivity(),
                                getResources().getString(R.string.network_error)
                            )
                        } else {
                            if (Utils.getDoubleLat(activity) != 0.0 && Utils.getDoubleLag(
                                    activity
                                ) != 0.0
                            ) {


                                Log.e("TAG", "startTripImageUploadPopup: ");
                                val postModel = StartAssignmentPostModel(
                                    ZilaTripMasterId,
                                    Utils.getDoubleLat(activity),
                                    Utils.getDoubleLag(activity),
                                    response,
                                    tripMiloValue.toInt(),
                                    SharePrefs.getInstance(activity)
                                        .getInt(SharePrefs.PEOPLE_ID)
                                )
                                startTripApi(postModel)
                                customDialog!!.dismiss()
                            }
                        }
                    }
                    .setNegativeButton(
                        android.R.string.no
                    ) { dialogInterface, i ->
                        dialogInterface.dismiss()
                        customDialog!!.dismiss()
                    }.show()
            }
        }
        customDialog!!.show()
    }

    private fun imageUploadPopup(response: String) {
        val imageUploadPopupBinding = DataBindingUtil.inflate<ImageUploadPopupBinding>(
            layoutInflater,
            R.layout.image_upload_popup,
            null,
            false
        )
        customDialog = Dialog((getActivity())!!, R.style.CustomDialog)
        customDialog!!.setContentView(imageUploadPopupBinding.root)
        customDialog!!.setCancelable(false)
        val submit = imageUploadPopupBinding.submit
        val image = imageUploadPopupBinding.ivImage
        val miloMeter = imageUploadPopupBinding.etMilo
        val startKm = imageUploadPopupBinding.etStartkm
        Picasso.get().load(
            SharePrefs.getInstance(MyApplication.getInstance())
                .getString(SharePrefs.BASEURL) + response
        ).into(image)
        startKm.setText(startKM.toString() + "")
        postMiloMeterData(ZilaTripMasterId)

        imageUploadPopupBinding.ivCross.setOnClickListener { customDialog!!.dismiss() }
        miloMeter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length > 0) {
                    milovalue = miloMeter.text.toString()
                } else {
                    milovalue = ""
                }
            }
        })
        submit.setOnClickListener {
            milovalue = miloMeter.text.toString()
            if (milovalue.equals("", ignoreCase = true) || milovalue.isEmpty()) {
                Utils.setToast(activity, "Enter end km Value")
            } else if (milovalue.toInt() > maxEndKm) {
                Utils.setToast(activity, "Please Enter Accurate Km")
            } else if (milovalue.toDouble() < startKM.toInt()) {
                Utils.setToast(activity, "End trip KM can not less than Start Trip KM")
            } else if (milovalue.toDouble() <= startKM.toInt()) {
                Utils.setToast(activity, "Start and end km can not be same")
            } else if (tripMiloValue.length > 0 && tripMiloValue.toDouble() == 0.0) {
                Utils.setToast(activity, "Value can not be zero")
            } else if (!isImageUploaded) {
                Utils.setToast(activity, "Upload MiloMeter Image First!!")
            } else {
                AlertDialog.Builder((getActivity())!!)
                    .setTitle("Alert")
                    .setMessage("Are you sure this is real km ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(
                        android.R.string.yes
                    ) { dialog, whichButton ->
                        if (!Utils.checkInternetConnection(getActivity())) {
                            Utils.setToast(
                                getActivity(),
                                resources.getString(R.string.network_error)
                            )
                        } else {
                            if (Utils.getDoubleLat(activity) != 0.0 && Utils.getDoubleLag(activity) != 0.0) {

                                sendCloseKmApproval(
                                    SendCloseKmApproval(
                                        ZilaTripMasterId,
                                        milovalue.toInt(),
                                        response,
                                        SharePrefs.getInstance(activity)
                                            .getInt(SharePrefs.PEOPLE_ID)
                                    )
                                )

                                customDialog!!.dismiss()
                            }
                        }
                    }
                    .setNegativeButton(
                        android.R.string.no
                    ) { dialogInterface, i ->
                        dialogInterface.dismiss()
                        customDialog!!.dismiss()
                    }.show()
            }
        }
        customDialog!!.show()
    }

    private fun sendCloseKmApproval(sendCloseKmApproval: SendCloseKmApproval) {
        dashBoardViewModel!!.SendCloseKmApprovalRequest(sendCloseKmApproval)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            resource.data?.let { sendCloseKMResponce ->
                                closeKmApprovalRequestResponse(sendCloseKMResponce)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(requireActivity(), it.message)
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(getActivity())
                        }
                    }
                }
            }
    }

    private fun postMiloMeterData(TripPlannerConfirmedMasterId: Int) {
        dashBoardViewModel!!.PostEnterMilometerLimit(TripPlannerConfirmedMasterId)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { milometerResponce ->
                                miloLimitSuccessResponse(milometerResponce)
                            }
                        }

                        Status.ERROR -> {
                            Utils.setToast(requireActivity(), it.message)
                        }

                        Status.LOADING -> {
                        }
                    }
                }
            }

    }

    private var myRunnable: Runnable = Runnable {
        if (!isTimerPaused) {
            val mills = Math.abs(millisUntilFinished)
            hms = String.format(
                FORMAT,
                TimeUnit.MILLISECONDS.toHours(mills),
                TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(mills)
                ),
                TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(mills)
                )
            )
            tvStartTime!!.text = "" + hms
            millisUntilFinished += 1000
            restart()
        }
    }

    fun start() {
        handler!!.postDelayed(myRunnable, 1000)
    }

    fun stop() {
        handler!!.removeCallbacks(myRunnable)
        tvStartTime!!.text = "00:00:00"
    }

    private fun startLocationService() {
        if (locationServiceForBeat == null) {
            locationServiceForBeat = LocationServiceForBeat()
            if (!isMyServiceRunning(locationServiceForBeat!!.javaClass)) {
                val mLocationIntent = Intent(activity, locationServiceForBeat!!.javaClass)
                requireActivity().startService(mLocationIntent)
            }
        }
    }

    private fun restart() {
        handler!!.removeCallbacks(myRunnable)
        handler!!.postDelayed(myRunnable, 1000)
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var address: String? = ""
            address = intent.getStringExtra("address")
            if (reachedDialog != null) {
                if (reachedDialog!!.isShowing) {
                    reachedDialog!!.dismiss()
                }
            }
            callDestReachPopup(address)
        }
    }

    private fun callDestReachPopup(address: String?) {
        reachedDialog = Dialog((activity)!!)
        val reachedPopupBinding = DataBindingUtil.inflate<ReachedPopupBinding>(
            layoutInflater, R.layout.reached_popup, null, false
        )
        reachedDialog = Dialog((getActivity())!!, R.style.CustomDialog)
        reachedDialog!!.setContentView(reachedPopupBinding.root)
        reachedDialog!!.setCancelable(true)
        reachedPopupBinding.tvAddress.text = address
        reachedPopupBinding.ivCross.setOnClickListener { reachedDialog!!.dismiss() }
        reachedDialog!!.show()
    }

    fun timer(time: String?) {
        try {
            val currMillis = System.currentTimeMillis()
            val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
            sdf1.timeZone = TimeZone.getDefault()
            if (time != null) {
                val startTime = sdf1.parse(time)
                val startEpoch = startTime.time
                millisUntilFinished = ((currMillis - (breakTime * 1000)) - startEpoch)
            }
            start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setActionBarConfiguration() {
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.container)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        val layout = requireActivity().findViewById<RelativeLayout>(R.id.titlebar)
        layout.visibility = View.VISIBLE
        val linearLayout = requireActivity().findViewById<LinearLayout>(R.id.ll_oder_id_view)
        linearLayout.visibility = View.GONE
        val startTimerbtn = requireActivity().findViewById<TextView>(R.id.start_timer)
        val tvAssignmentid = requireActivity().findViewById<TextView>(R.id.assignmentid)
        tvAssignmentid.visibility = View.GONE
        val tvTimmer = requireActivity().findViewById<TextView>(R.id.tv_timmer)
        tvTimmer.visibility = View.GONE
        val tittleTextView = requireActivity().findViewById<TextView>(R.id.toolbar_title)
        val tvHistory = requireActivity().findViewById<TextView>(R.id.tv_history)
        tvHistory.visibility = View.GONE
        startTimerbtn.visibility = View.GONE
        startTimerbtn.text = "00:00:00"
        tittleTextView.visibility = View.VISIBLE
        tittleTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
        tittleTextView.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.ic_circle_green_light)
        tittleTextView.text = "All Trips"
        startBreak!!.visibility = View.GONE
        if (time != null && time!!.isNotEmpty()) {
            timer(time)
        }
        toolbar.setNavigationOnClickListener {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun permissionsDialog() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val rationale =
            "Direct last mile collects location data to enable to tracking delivery boy for order delivered even when the app is closed or not in use \n open the Settings app\nTap Privacy And then Permission manager."
        val options = Permissions.Options()
            .setRationaleDialogTitle("Background Location")
            .setSettingsDialogTitle("Warning")
        Permissions.check(
            getActivity() /*context*/,
            permissions,
            rationale,
            options,
            object : PermissionHandler() {
                override fun onGranted() {
                    if (!Utils.checkInternetConnection(getActivity())) {
                        Utils.setToast(getActivity(), resources.getString(R.string.network_error))
                    } else {
                        callGPSMethod()
                    }
                }

                override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {}
            })
    }

    private fun callGPSMethod() {
        val builder = LocationSettingsRequest.Builder()
        builder.setAlwaysShow(true)
        builder.addLocationRequest((createLocationRequest()))
        val client = LocationServices.getSettingsClient((getActivity())!!)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            currentTripStatus(ZilaTripMasterId)
        }
        task.addOnFailureListener { e: Exception? ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(
                        (getActivity())!!,
                        EasyWayLocation.LOCATION_SETTING_REQUEST_CODE
                    )
                } catch (sendEx: SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    private fun checkGPSNetworkMethod() {
        val builder = LocationSettingsRequest.Builder()
        builder.setAlwaysShow(true)
        builder.addLocationRequest((createLocationRequest()))
        val client = LocationServices.getSettingsClient((getActivity())!!)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            callForImage(
                CAPTURE_IMAGE_CAMERA_START
            )
        }
        task.addOnFailureListener { e: Exception? ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult((getActivity())!!, EasyWayLocation.PENDDING_LOCTION)
                } catch (sendEx: SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    private fun checkAddMeterGPSNetworkMethod() {
        val builder = LocationSettingsRequest.Builder()
        builder.setAlwaysShow(true)
        builder.addLocationRequest((createLocationRequest()))
        val client = LocationServices.getSettingsClient((getActivity())!!)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            callForImage(
                CAPTURE_IMAGE_CAMERA
            )
        }
        task.addOnFailureListener { e: Exception? ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(
                        (getActivity())!!,
                        EasyWayLocation.METER_READING_LOCTION
                    )
                } catch (sendEx: SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    fun switchContentWithStack(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }
}