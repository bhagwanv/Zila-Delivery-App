package com.sk.ziladelivery.ui.views.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.*
import com.sk.ziladelivery.data.model.MyTripMapOrderResponseModel.CustomerListDc
import com.sk.ziladelivery.databinding.CancelReasonBinding
import com.sk.ziladelivery.databinding.FragmentSingleTripMapViewBinding
import com.sk.ziladelivery.databinding.ShopImageUploadPopupBinding
import com.sk.ziladelivery.databinding.TripCompletedPopupBinding
import com.sk.ziladelivery.listener.SubmitClick
import com.sk.ziladelivery.ui.views.adapter.EntireRouteAdapter
import com.sk.ziladelivery.ui.views.adapter.TripCompleteAdapter
import com.sk.ziladelivery.ui.views.fragment.unloadReturnItem.OrderDetailReturnActivity
import com.sk.ziladelivery.ui.views.main.*
import com.sk.ziladelivery.ui.views.viewmodels.MySingleTripMapViewModel
import com.sk.ziladelivery.utilities.*
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class MySingleTripMapViewFragment : Fragment(), OnMapReadyCallback, DirectionsJSONParserNew.directionDuraction, SubmitClick {
    private val TAG = this.javaClass.simpleName
    val MY_PERMISSIONS_REQUEST_LOCATION = 99
    val REQUEST_IMAGE = 100
    private var imageFilePath: String? = null
    private var origin: LatLng? = null
    private var dest: LatLng? = null
    private var waypoints: LatLng? = null
    private var customerLatLng: LatLng? = null
    private var lineOptions: PolylineOptions? = null
    private var points = ArrayList<LatLng>()
    private var shippingaddressList: ArrayList<RoutModel>? = null
    private var shippingaddressListForMap: ArrayList<String>? = null
    private var marker: Marker? = null
    private var googleMapMain: GoogleMap? = null
    private var activity: MyTripActivity? = null
    private var player: MediaPlayer? = null
    private var mBinding: FragmentSingleTripMapViewBinding? = null
    private var sheetBehavior: BottomSheetBehavior<*>? = null
    private var orderResponseModel: MySingleTripOrderResponseModel? = null
    private var OrderList: ArrayList<CustomerListDc>? = null
    private var imageUploadPopupBinding: ShopImageUploadPopupBinding? = null
    private var cancelReasonBinding: CancelReasonBinding? = null
    private var distance = 0.0
    private var warehouseLat = 0.0
    private var warehouseLong = 0.0
    private var mySingleTripMapViewModel: MySingleTripMapViewModel? = null
    private var tripCompletedPopupBinding: TripCompletedPopupBinding? = null
    private var TripPlannerConfirmedMasterId = 0
    private var CustomerTripStatus = 0
    private var ORDER_ID = 0
    private var TripPlannerConfirmedDetailId = 0
    private var TripPlannerConfirmedOrderId = 0
    private var deliveryIssuenceID = 0
    private var assignId = 0
    private var redispatchCount = 0
    private var orderDispatchedMasterId = 0
    private var orderId = 0
    private var orderCount = 0
    var isReturnOrder = false
    var isGeneralOrder = false
    var isMore = false
    var isOtpVerified = false
    var isEntireRoute = false
    private var rvStatus: RecyclerView? = null
    private var geocoder: Geocoder? = null
    private var addresses: List<Address>? = null
    private var customDialog: Dialog? = null
    private var ImageUploadCustomDialog: Dialog? = null
    private var TripCompleteDialog: Dialog? = null
    var isImageUploaded = false
    private var dialog: BottomSheetDialog? = null
    private var orderStatus = ""
    private var orderComment: String? = ""
    private var voiceNoteString: String? = null
    private var Skcode: String? = null
    private var shopName: String? = null
    private var date: String? = null
    private var imageUrl: String? = null
    private var time: String? = null
    private var isTakeShopImage = false
    private var orderPlacedModel: OrderPlacedModel? = null
    private var gpsTracker: GPSTracker? = null
    private var isNotLastMileApp = false
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MyTripActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_trip_map_view, container, false)
        shippingaddressList = ArrayList()
        shippingaddressListForMap = ArrayList()
        val bundle = this.arguments
        TripPlannerConfirmedMasterId = bundle!!.getInt("ZilaTripMasterId")
        TripPlannerConfirmedDetailId = bundle.getInt(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id)
        ORDER_ID = bundle.getInt("ORDER_ID")
        CustomerTripStatus = bundle.getInt("CustomerTripStatus")
        time = bundle.getString("time")
        isNotLastMileApp = bundle.getBoolean("isNotLastMileApp")
        gpsTracker = GPSTracker(getActivity())
        mySingleTripMapViewModel = ViewModelProviders.of(requireActivity()).get(
            MySingleTripMapViewModel::class.java
        )
        mySingleTripMapViewModel!!.getmySingleTripMapOrderUpdateStatus()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    consumeResponseForStatusUpdate(it)
                }
            }
        mySingleTripMapViewModel!!.getmySinngleTripOrderResponseData()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    SingleTripconsumeResponse(it)
                }
            }
        mySingleTripMapViewModel!!.validateOrderOTP()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    consumeVerifyOrderOtpResponse(it)
                }
            }
        mySingleTripMapViewModel!!.GetMySingleTripMapOrderModel(
            TripPlannerConfirmedMasterId,
            gpsTracker!!.latitude,
            gpsTracker!!.longitude
        )
        mySingleTripMapViewModel!!.getmyTripOrderResponseData()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    consumeResponse(it)
                }
            }
        mySingleTripMapViewModel!!.getunloadingOrderResponseData()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    UnloadingconsumeResponse(it)
                }
            }
        mySingleTripMapViewModel!!.orderPlacedResponseData.observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeResponseForOrderPlace(
                    it
                )
            }
        }
        mySingleTripMapViewModel!!.orderDetailsData.observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeResponseofOrderDetail(
                    it
                )
            }
        }
        mySingleTripMapViewModel!!.checkOrderOTPExist()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    consumeCheckOrderOtpResponse(it)
                }
            }
        mySingleTripMapViewModel!!.generateOrderOTP()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    consumeOrderOtpGenerateResponse(it)
                }
            }
        mySingleTripMapViewModel!!.tripRecodingResponce.observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeRecodingVoiceResponse(
                    it
                )
            }
        }
        mySingleTripMapViewModel!!.completetrip.observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeCompleteTrip(
                    it
                )
            }
        }
        mySingleTripMapViewModel!!.orderCountStatus.observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeOrderCount(
                    it
                )
            }
        }
        mySingleTripMapViewModel!!.postCustomerUnloadApi()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    customerUnloadResponse(it)
                }
            }
        mySingleTripMapViewModel!!.sendNotifications()
            .observe(requireActivity()) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    consumeNotifiction(it)
                }
            }
        initViews()
        return mBinding!!.getRoot()
    }

    private fun createLocationRequest() = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.MINUTES.toMillis(5)).apply {
        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        setDurationMillis(TimeUnit.MINUTES.toMillis(5))
        setWaitForAccurateLocation(true)
        setMaxUpdates(1)
    }.build()

    override fun onDestroyView() {
        super.onDestroyView()
        if (player != null) {
            player!!.stop()
            player!!.release()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EasyWayLocation.LOCATION_SETTING_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e(TAG, "onActivityResult: OK")
            } else {
                startActivity(Intent(activity, MainActivity::class.java))
            }
        } else if (requestCode == 401) {
            if (customerLatLng!!.latitude != 0.0 && customerLatLng!!.longitude != 0.0) {
                val model = BackgroundServiceModel(
                    SharePrefs.getInstance(activity).getInt(SharePrefs.TripPlannerVehicleId),
                    customerLatLng!!.latitude, customerLatLng!!.longitude, 3, Date().toString(),
                    SharePrefs.getInstance(activity)
                        .getInt(SharePrefs.TripPlannerConfirmedDetailId), 0
                )
                SharePrefs.setCashmanagmentSharedPreference(
                    activity,
                    SharePrefs.BACKGROUND_DATA,
                    Gson().toJson(model)
                )
            }
            TripCompletePopup()
        } else if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            uploadMultipart()
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
                    googleMapMain!!.isMyLocationEnabled = true
                }
            } else {
                Toast.makeText(activity, "permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapMain = googleMap
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleMapMain!!.isMyLocationEnabled = true
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(
            requireActivity()
        )
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener(requireActivity()) { location: Location? ->
                location?.let {
                    currentLocation(
                        it
                    )
                }
            }
        googleMapMain!!.setOnCameraIdleListener {
            mBinding!!.coordinateLayout.rlVoiceView.visibility = View.VISIBLE
            mBinding!!.bottomLayout.bottomsheet.visibility = View.VISIBLE
        }
        googleMapMain!!.setOnCameraMoveListener {
            mBinding!!.coordinateLayout.rlVoiceView.visibility = View.GONE
            mBinding!!.bottomLayout.bottomsheet.visibility = View.GONE
        }
    }

    private fun currentLocation(location: Location?) {
        var waypointsString = ""
        if (isEntireRoute) {
            try {
                origin = LatLng(warehouseLat, warehouseLong)
                dest = LatLng(
                    OrderList!![OrderList!!.size - 1].lat, OrderList!![OrderList!!.size - 1].lng
                )
                val originDraw1 = resources.getDrawable(R.drawable.store) as BitmapDrawable
                val bb = originDraw1.bitmap
                val originMarker1 = Bitmap.createScaledBitmap(bb, bb.width, bb.height, false)
                marker = googleMapMain!!.addMarker(
                    MarkerOptions()
                        .position(origin!!)
                        .title("Warehouse")
                        .icon(BitmapDescriptorFactory.fromBitmap(originMarker1))
                        .rotation(20f)
                )
                for (i in OrderList!!.indices) {
                    if (OrderList!![i].customerId != 0) {
                        val wHaddress = shippingaddressListForMap!![i]
                        var markerstop =
                            BitmapFactory.decodeResource(resources, R.drawable.placeholder)
                        markerstop = scaleBitmap(markerstop, 60, 80, (i + 1).toString() + "")
                        waypoints = LatLng(OrderList!![i].lat, OrderList!![i].lng)
                        waypointsString =
                            waypointsString + (if (waypointsString == "") "" else "%7C") + OrderList!![i].lat + "," + OrderList!![i].lng
                        marker = googleMapMain!!.addMarker(
                            MarkerOptions().position(waypoints!!).title(
                                OrderList!![i].skcode + "  " + wHaddress
                            )
                                .snippet(OrderList!![i].skcode + " , " + OrderList!![i].customerName)
                                .icon(BitmapDescriptorFactory.fromBitmap(markerstop))
                        )
                    } else {
                        val wareaddresses =
                            geocoder!!.getFromLocation(warehouseLat, warehouseLong, 1)
                        val wHaddress = wareaddresses!![0].getAddressLine(0)
                        val originDraw = ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.store
                        ) as BitmapDrawable?
                        val bitmap = originDraw!!.bitmap
                        val originMarker =
                            Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
                        marker = googleMapMain!!.addMarker(
                            MarkerOptions().position(origin!!)
                                .title(wHaddress)
                                .icon(BitmapDescriptorFactory.fromBitmap(originMarker))
                                .rotation(20f)
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            if (location != null) {
                googleMapMain!!.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            location.latitude,
                            location.longitude
                        ), 13f
                    )
                )
                geocoder = Geocoder(requireActivity(), Locale.getDefault())
                customerLatLng = LatLng(location.latitude, location.longitude)
                val cameraPosition = CameraPosition.Builder()
                    .target(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    ) // Sets the center of the map to location user
                    .zoom(50f)
                    .bearing(90f)
                    .tilt(40f)
                    .build()
                googleMapMain!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
            origin = LatLng(customerLatLng!!.latitude, customerLatLng!!.longitude)
            dest = LatLng(
                orderResponseModel!!.customerOrderinfoDc.lat,
                orderResponseModel!!.customerOrderinfoDc.lng
            )
            try {
                var markerstop = BitmapFactory.decodeResource(resources, R.drawable.placeholder)
                markerstop = scaleBitmap(markerstop, 60, 80, "W")
                waypoints = LatLng(
                    orderResponseModel!!.customerOrderinfoDc.lat,
                    orderResponseModel!!.customerOrderinfoDc.lng
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (orderResponseModel!!.customerOrderinfoDc.lat != 0.0 && orderResponseModel!!.customerOrderinfoDc.lng != 0.0) {
                try {
                    addresses = geocoder!!.getFromLocation(
                        orderResponseModel!!.customerOrderinfoDc.lat,
                        orderResponseModel!!.customerOrderinfoDc.lng,
                        1
                    )
                    val address = addresses!![0].getAddressLine(0)
                    val originDraw = ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.truck
                    ) as BitmapDrawable?
                    val bitmap = originDraw!!.bitmap
                    val originMarker =
                        Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
                    marker = googleMapMain!!.addMarker(
                        MarkerOptions().position(origin!!)
                            .title("").icon(BitmapDescriptorFactory.fromBitmap(originMarker))
                            .rotation(20f).flat(true).anchor(0.5f, 0.5f)
                    )
                    marker = if (orderResponseModel!!.customerOrderinfoDc.customerId != 0) {
                        val destDraw = ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.placeholder
                        ) as BitmapDrawable?
                        val bb = destDraw!!.bitmap
                        val destMarker = Bitmap.createScaledBitmap(bb, 60, 80, false)
                        googleMapMain!!.addMarker(
                            MarkerOptions().position(dest!!)
                                .snippet(orderResponseModel!!.customerOrderinfoDc.skcode + " , " + orderResponseModel!!.customerOrderinfoDc.shopName)
                                .title(address).icon(BitmapDescriptorFactory.fromBitmap(destMarker))
                        )
                    } else {
                        val destDraw = ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.store
                        ) as BitmapDrawable?
                        val singleBitmap = destDraw!!.bitmap
                        val destMarker = Bitmap.createScaledBitmap(
                            singleBitmap,
                            singleBitmap.width,
                            singleBitmap.height,
                            false
                        )
                        googleMapMain!!.addMarker(
                            MarkerOptions().position(dest!!).title(address)
                                .icon(BitmapDescriptorFactory.fromBitmap(destMarker))
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                val builder = AlertDialog.Builder(
                    requireActivity()
                )
                builder.setMessage("Customer Latitude Longitude is not Available")
                builder.setPositiveButton(R.string.ok) { dialog: DialogInterface, which: Int -> dialog.cancel() }
                builder.show()
            }
        }
        drawRoute(origin, dest, waypointsString)
    }

    override fun direction(
        durationString: String,
        distance: String,
        durationvalue: Int,
        distancevalue: String
    ) {
        try {
            requireActivity().runOnUiThread {
                if (!isEntireRoute) {
                    val inminutes = durationvalue % 3600 / 60
                    val ordercompletionTimehr =
                        Math.round((orderResponseModel!!.singleOrderMapviewInfoDC.unloadingTime + inminutes).toFloat()) / 60
                    val ordercompletionTimemin =
                        Math.round(((orderResponseModel!!.singleOrderMapviewInfoDC.unloadingTime + inminutes) % 60).toFloat())
                    mBinding!!.bottomLayout.tvTime.text = durationString
                    mBinding!!.bottomLayout.tvDistance.text = "($distance)"
                    mBinding!!.bottomLayout.orderTime.text = ordercompletionTimehr.toString() + "h: " + ordercompletionTimemin + "m"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun submitBtnClicked(status: String) {
        orderComment = status
        if (orderComment.equals("Others", ignoreCase = true)) {
            cancelReasonBinding!!.llComment.visibility = View.VISIBLE
        } else {
            cancelReasonBinding!!.llComment.visibility = View.GONE
        }
    }


    private fun initViews() {
        sheetBehavior = BottomSheetBehavior.from(mBinding!!.bottomLayout.bottomsheet)
        sheetBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
        mBinding!!.bottomLayout.llCompleteTrip.visibility = View.VISIBLE
        mBinding!!.bottomLayout.llCompleteReturnTrip.visibility = View.VISIBLE
        mBinding!!.bottomLayout.llNavigation.visibility = View.VISIBLE
        if (!com.sk.ziladelivery.utilities.TextUtils.isNullOrEmpty(voiceNoteString)) {
            mBinding!!.coordinateLayout.tvComment.text = voiceNoteString
        }
        mBinding!!.coordinateLayout.tvVoiceRecoding.setOnClickListener { view: View? ->
            mySingleTripMapViewModel!!.getVoiceRecodeObserver(
                SharePrefs.getInstance(activity).getInt(SharePrefs.TRIP_MASTER_ID),
                orderResponseModel!!.customerOrderinfoDc.customerId
            )
        }
        mBinding!!.coordinateLayout.tvCalling.setOnClickListener { view: View? ->
            if (!com.sk.ziladelivery.utilities.TextUtils.isNullOrEmpty(
                    orderResponseModel!!.customerOrderinfoDc.mobileNumber
                )
            ) {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:" + "+91" + orderResponseModel!!.customerOrderinfoDc.mobileNumber)
                )
                startActivity(intent)
            } else {
                Utils.setToast(activity, "Customer Mobile Number Is not Available.")
            }
        }
       /* if (CustomerTripStatus == 0) {
            mBinding!!.bottomLayout.tvComplete.text = getText(R.string.complete_trip)
        } else {
            mBinding!!.bottomLayout.tvComplete.text = "Continue"
        }*/
        if (CustomerTripStatus == 0) {
            mBinding!!.bottomLayout.tvComplete.text = getText(R.string.complete_trip)
            mBinding!!.bottomLayout.llCompleteTrip.visibility=View.VISIBLE
        } else {
            mBinding!!.bottomLayout.llCompleteTrip.visibility=View.GONE
        }
        mBinding!!.bottomLayout.llCompleteTrip.setOnClickListener {
            if (orderCount != 0) {
                if (CustomerTripStatus == Constant.PENDING && isTakeShopImage) {
                    ImageUploadPopup()
                    if (!isNotLastMileApp) {
                        ImageUploadPopup()
                    } else {
                        if (customerLatLng != null) {
                            val unloadLocationModel = UnloadLocationModel(
                                orderResponseModel!!.customerOrderinfoDc.customerId,
                                "",
                                customerLatLng!!.latitude,
                                customerLatLng!!.longitude,
                                SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)
                            )
                            mySingleTripMapViewModel!!.customerUnloadLocation(unloadLocationModel)
                        }
                    }
                } else if (CustomerTripStatus == Constant.PENDING && !isTakeShopImage) {
                    mySingleTripMapViewModel!!.getCompletTripStatus(
                        TripPlannerConfirmedDetailId,
                        customerLatLng!!.latitude,
                        customerLatLng!!.longitude
                    )
                    mySingleTripMapViewModel!!.sendNotificationsObserver(orderResponseModel!!.customerOrderinfoDc.customerId)
                } else if (CustomerTripStatus == Constant.UNLOADING) {
                    unloadingCall()
                } else if (CustomerTripStatus == Constant.REACHED_DISTINATION) {
                    reachedDistinationCall()
                } else if (CustomerTripStatus == Constant.COLLECTING_PAYMENT) {
                    callectingPayment()
                } else if (CustomerTripStatus == Constant.VERIFYING_OTP) {
                    verifyingOTPCall()
                } else if (CustomerTripStatus == Constant.NOTIFY_DELIVERY_CANCELLED) {
                    notifyDeliveryCancle()
                } else if (CustomerTripStatus == Constant.REDISPATCH_ANNORDER_CANCEL_VERIFYINN_OTP) {
                    redispatchAnnorderCancelVerifyinOTP()
                }
            } else {
                if (customerLatLng != null) {
                    val startPoint = Location("locationA")
                    startPoint.latitude = customerLatLng!!.latitude
                    startPoint.longitude = customerLatLng!!.longitude
                    val endPoint = Location("locationB")
                    endPoint.latitude = warehouseLat
                    endPoint.longitude = warehouseLong
                    distance = startPoint.distanceTo(endPoint).toDouble()
                    if (distance <= 200) {
                        mySingleTripMapViewModel!!.getOderCountObserver(
                            TripPlannerConfirmedDetailId,
                            customerLatLng!!.latitude,
                            customerLatLng!!.longitude
                        )
                    } else {
                        Utils.setToast(
                            activity,
                            "You can complete trip within 200 mtr of warehouse."
                        )
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
        }

        mBinding!!.bottomLayout.llContinue.setOnClickListener {
            if (orderCount != 0) {
                if (CustomerTripStatus == Constant.PENDING && isTakeShopImage) {
                    ImageUploadPopup()
                    if (!isNotLastMileApp) {
                        ImageUploadPopup()
                    } else {
                        if (customerLatLng != null) {
                            val unloadLocationModel = UnloadLocationModel(
                                orderResponseModel!!.customerOrderinfoDc.customerId,
                                "",
                                customerLatLng!!.latitude,
                                customerLatLng!!.longitude,
                                SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)
                            )
                            mySingleTripMapViewModel!!.customerUnloadLocation(unloadLocationModel)
                        }
                    }
                } else if (CustomerTripStatus == Constant.PENDING && !isTakeShopImage) {
                    mySingleTripMapViewModel!!.getCompletTripStatus(
                        TripPlannerConfirmedDetailId,
                        customerLatLng!!.latitude,
                        customerLatLng!!.longitude
                    )
                    mySingleTripMapViewModel!!.sendNotificationsObserver(orderResponseModel!!.customerOrderinfoDc.customerId)
                } else if (CustomerTripStatus == Constant.UNLOADING) {
                    unloadingCall()
                } else if (CustomerTripStatus == Constant.REACHED_DISTINATION) {
                    reachedDistinationCall()
                } else if (CustomerTripStatus == Constant.COLLECTING_PAYMENT) {
                    callectingPayment()
                } else if (CustomerTripStatus == Constant.VERIFYING_OTP) {
                    verifyingOTPCall()
                } else if (CustomerTripStatus == Constant.NOTIFY_DELIVERY_CANCELLED) {
                    notifyDeliveryCancle()
                } else if (CustomerTripStatus == Constant.REDISPATCH_ANNORDER_CANCEL_VERIFYINN_OTP) {
                    redispatchAnnorderCancelVerifyinOTP()
                }
            } else {
                if (customerLatLng != null) {
                    val startPoint = Location("locationA")
                    startPoint.latitude = customerLatLng!!.latitude
                    startPoint.longitude = customerLatLng!!.longitude
                    val endPoint = Location("locationB")
                    endPoint.latitude = warehouseLat
                    endPoint.longitude = warehouseLong
                    distance = startPoint.distanceTo(endPoint).toDouble()
                    if (distance <= 200) {
                        mySingleTripMapViewModel!!.getOderCountObserver(
                            TripPlannerConfirmedDetailId,
                            customerLatLng!!.latitude,
                            customerLatLng!!.longitude
                        )
                    } else {
                        Utils.setToast(
                            activity,
                            "You can complete trip within 200 mtr of warehouse."
                        )
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
        }
        try {
            callGPSMethod()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mBinding!!.bottomLayout.llCompleteReturnTrip.setOnClickListener {
            returnReachedDistinationCall()
        }
        mBinding!!.coordinateLayout.tvClickhere.setOnClickListener { v: View? ->
            if (!isEntireRoute) {
                isEntireRoute = true
                isMore = false
                if (!isMore) {
                    mBinding!!.bottomLayout.tvMore.text = "More"
                } else {
                    mBinding!!.bottomLayout.tvMore.text = "Less"
                }
                mBinding!!.bottomLayout.llCompleteTrip.visibility = View.GONE
               // mBinding!!.bottomLayout.llCompleteReturnTrip.visibility = View.GONE
                mBinding!!.bottomLayout.llNavigation.visibility = View.GONE
                mBinding!!.bottomLayout.llMore.visibility = View.VISIBLE
                mySingleTripMapViewModel!!.GetMyTripMapOrderModel(TripPlannerConfirmedMasterId)
                googleMapMain!!.clear()
                mBinding!!.coordinateLayout.tvRoute.text =
                    "Do you want to See Current customer Route?"
            } else {
                isEntireRoute = false
                mBinding!!.bottomLayout.llCompleteTrip.visibility = View.VISIBLE
              //  mBinding!!.bottomLayout.llCompleteReturnTrip.visibility = View.VISIBLE
                mBinding!!.bottomLayout.llNavigation.visibility = View.VISIBLE
                mBinding!!.bottomLayout.llMore.visibility = View.GONE
                mBinding!!.bottomLayout.rvLocation.visibility = View.GONE
                mySingleTripMapViewModel!!.GetMySingleTripMapOrderModel(
                    TripPlannerConfirmedMasterId,
                    gpsTracker!!.latitude,
                    gpsTracker!!.longitude
                )
                googleMapMain!!.clear()
                mBinding!!.coordinateLayout.tvRoute.text = getString(R.string.do_you_want_to_see_entire_route)
            }
        }
        mBinding!!.bottomLayout.llMore.setOnClickListener { v: View? ->
            if (!isMore) {
                isMore = true
                mBinding!!.bottomLayout.rvLocation.visibility = View.VISIBLE
                mBinding!!.bottomLayout.tvMore.text = "Less"
            } else {
                isMore = false
                mBinding!!.bottomLayout.rvLocation.visibility = View.GONE
                mBinding!!.bottomLayout.tvMore.text = "More"
            }
        }
        mBinding!!.bottomLayout.llNavigation.setOnClickListener {
            Log.e(TAG, "initViews111111111: ${"google.navigation:q=" + orderResponseModel!!.customerOrderinfoDc}", )
            val navigation = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + orderResponseModel!!.customerOrderinfoDc.lat + "," + orderResponseModel!!.customerOrderinfoDc.lng)
            )
            startActivity(navigation)
        }
    }

    fun drawRoute(origin: LatLng?, dest: LatLng?, waypointsLatLong: String?) {
        val url = Utils.getUrl(activity, origin, dest, waypointsLatLong)
        val FetchUrl = FetchUrl()
        // Start downloading json data from Google Directions API
        FetchUrl.execute(url)
        //move map camera
        googleMapMain!!.moveCamera(CameraUpdateFactory.newLatLngZoom(origin!!, 13f))
    }

    private fun callGPSMethod() {
        val builder = LocationSettingsRequest.Builder()
        builder.setAlwaysShow(true)
        builder.addLocationRequest(createLocationRequest())
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { }
        task.addOnFailureListener { e: Exception? ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(requireActivity(),
                        EasyWayLocation.LOCATION_SETTING_REQUEST_CODE
                    )
                } catch (sendEx: SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    private fun ImageUploadPopup() {
        imageUploadPopupBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.shop_image_upload_popup, null, false
        )
        ImageUploadCustomDialog = Dialog(requireActivity(), R.style.CustomDialog)
        ImageUploadCustomDialog!!.setContentView(imageUploadPopupBinding!!.getRoot())
        ImageUploadCustomDialog!!.setCancelable(false)
        val submit = imageUploadPopupBinding!!.submit
        val image = imageUploadPopupBinding!!.ivImage
        imageUploadPopupBinding!!.ivCross.setOnClickListener { view: View? -> ImageUploadCustomDialog!!.dismiss() }
        image.setOnClickListener { callForImage() }
        submit.setOnClickListener { v: View? ->
            if (!isImageUploaded) {
                Utils.setToast(activity, "Please Capture Shop Image")
            } else {
                if (!Utils.checkInternetConnection(activity)) {
                    Utils.setToast(activity, resources.getString(R.string.network_error))
                } else {
                    if (customerLatLng != null) {
                        val unloadLocationModel = UnloadLocationModel(
                            orderResponseModel!!.customerOrderinfoDc.customerId,
                            imageUrl,
                            customerLatLng!!.latitude,
                            customerLatLng!!.longitude,
                            SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)
                        )
                        mySingleTripMapViewModel!!.customerUnloadLocation(unloadLocationModel)
                    }
                }
            }
        }
        ImageUploadCustomDialog!!.show()
    }

    private fun customerUnloadSuccessResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getBoolean("Status")) {
                        if (ImageUploadCustomDialog != null) {
                            ImageUploadCustomDialog!!.dismiss()
                        }
                        mySingleTripMapViewModel!!.getCompletTripStatus(
                            TripPlannerConfirmedDetailId,
                            customerLatLng!!.latitude,
                            customerLatLng!!.longitude
                        )
                        mySingleTripMapViewModel!!.sendNotificationsObserver(orderResponseModel!!.customerOrderinfoDc.customerId)
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

    private fun callForImage() {
        try {
            val permissions: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
            val rationale =
                "Direct last mile collects location data to enable to tracking delivery boy for order delivered even when the app is closed or not in use \n open the Settings app\nTap Privacy And then Permission manager."
            val options = Permissions.Options()
                .setRationaleDialogTitle("Background Location")
                .setSettingsDialogTitle("Warning")
            Permissions.check(
                activity,
                permissions,
                rationale,
                options,
                object : PermissionHandler() {
                    override fun onGranted() {
                        pickFromCamera()
                    }

                    override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {}
                })
        } catch (e: Exception) {
            Logger.e(CommonMethods.getTag(this), e.toString())
        }
    }

    fun pickFromCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File
        photoFile = try {
            createImageFile()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        val photoUri =
            FileProvider.getUriForFile(requireActivity(), requireActivity().packageName + ".provider", photoFile)
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(pictureIntent, REQUEST_IMAGE)
    }

    private fun createImageFile(): File {
        val customerDocUpload = System.currentTimeMillis().toString() + "_image" + ".jpg"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File(Environment.getExternalStorageDirectory().toString() + "/ShopKirana")
        myDir.mkdirs()
        val file = File(storageDir, customerDocUpload)
        imageFilePath = file.absolutePath
        return file
    }

    fun uploadMultipart() {
        val fileToUpload = File(imageFilePath)
        Compressor(activity)
            .compressToFileAsFlowable(fileToUpload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ file: File? ->
                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file!!)
                val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestFile)
                UploadMiloMeterImage(getimageResponse, body)
            }) { throwable: Throwable -> throwable.printStackTrace() }
    }

    private fun TripCompletePopup() {
        tripCompletedPopupBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.trip_completed_popup, null, false
        )
        TripCompleteDialog = Dialog(requireActivity(), R.style.CustomDialog)
        TripCompleteDialog!!.setContentView(tripCompletedPopupBinding!!.getRoot())
        TripCompleteDialog!!.setCancelable(true)
        rvStatus = tripCompletedPopupBinding!!.rvStatus
        tripCompletedPopupBinding!!.rvStatus.layoutManager = LinearLayoutManager(activity)
        mySingleTripMapViewModel!!.GetMySingleTripMapOrderForUpdateSttaus(
            TripPlannerConfirmedDetailId
        )
        tripCompletedPopupBinding!!.ivCross.setOnClickListener { v: View? -> TripCompleteDialog!!.dismiss() }
        TripCompleteDialog!!.show()
    }

    private fun CheckDate(etDate: EditText) {
        try {
            val c1 = Calendar.getInstance()
            val mYear = c1[Calendar.YEAR]
            val mMonth = c1[Calendar.MONTH]
            val mDay = c1[Calendar.DAY_OF_MONTH]
            println("the selected $mDay")
            val dialog = DatePickerDialog(
                requireActivity(),
                { view: DatePicker?, mYear1: Int, mMonth1: Int, mDay1: Int ->
                    try {
                        etDate.setText(
                            StringBuilder() // Month is 0 based so add 1
                                .append(mDay1).append("/").append(mMonth1 + 1).append("/")
                                .append(mYear1).append(" ")
                        )
                        date = Utils.getSimpleDateFormat(etDate.text.toString().trim { it <= ' ' })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                mYear,
                mMonth,
                mDay
            )
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun consumeResponseofOrderDetail(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
               ProgressDialog.getInstance().show(getActivity())
            }
            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                assert(apiResponse.data != null)
                renderSuccessResponseofOrderDetail(apiResponse.data)
            }
            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(activity, resources.getString(R.string.errorString))
            }
            else -> {}
        }
    }

    private fun consumeResponseForOrderPlace(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
               ProgressDialog.getInstance().show(getActivity())
            }
            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                assert(apiResponse.data != null)
                renderSuccessResponseForOrderPlaced(apiResponse.data)
            }
            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(activity, resources.getString(R.string.errorString))
            }
            else -> {}
        }
    }

    private fun renderSuccessResponseForOrderPlaced(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                isOtpVerified = false
                if (customDialog != null) {
                    customDialog!!.dismiss()
                }
                Logger.d(CommonMethods.getTag(this), response.toString())
                try {
                    val orderResponsesModel =
                        Gson().fromJson(response, OrderResponsesModel::class.java)
                    if (orderResponsesModel.isStatus) {
                        Utils.setToast(activity, orderResponsesModel.message)
                        TripCompletePopup()
                    } else {
                        Utils.setToast(activity, orderResponsesModel.message)
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

    private fun renderSuccessResponseofOrderDetail(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                Logger.e(CommonMethods.getTag(this), response.toString())
                try {
                    val obj = JSONObject(response.toString())
                    val orderDetailsModel =
                        Gson().fromJson(obj.toString(), OrderDetailsModel::class.java)
                    if (orderDetailsModel.isStatus) {
                        orderDispatchedMasterId =
                            orderDetailsModel.orderDispatchedObj.orderdispatchedmasterid
                        redispatchCount = orderDetailsModel.orderDispatchedObj.redispatchcount
                        deliveryIssuenceID = orderDetailsModel.orderDispatchedObj.deliveryissuanceid
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

    private fun SingleTripconsumeResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
               ProgressDialog.getInstance().show(getActivity())
            }
            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss();
                ProgressDialog.getInstance().dismiss();
                assert(apiResponse.data != null)
                SingleTriprenderSuccessResponse(apiResponse.data)
            }
            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(activity, resources.getString(R.string.errorString))
            }
            else -> {}
        }
    }

    private fun consumeResponseForStatusUpdate(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
               ProgressDialog.getInstance().show(getActivity())
            }
            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                assert(apiResponse.data != null)
                renderSuccessResponseForStatusUpdate(apiResponse.data)
            }
            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(activity, resources.getString(R.string.errorString))
            }
            else -> {}
        }
    }

    private fun consumeCheckOrderOtpResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                assert(apiResponse.data != null)
                showOtpDialog(apiResponse.data!!.asBoolean)
                if (!apiResponse.data.asBoolean) {
                    mySingleTripMapViewModel!!.generateOrderOTP(
                        orderId,
                        orderStatus,
                        Utils.getDoubleLat(activity),
                        Utils.getDoubleLag(activity)
                    )
                }
            }
            Status.ERROR -> Utils.setToast(activity, resources.getString(R.string.errorString))
            else -> {}
        }
    }

    private fun consumeOrderOtpGenerateResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> Utils.setToast(activity, "OTP sent successfully!")
            Status.ERROR -> Utils.setToast(activity, resources.getString(R.string.errorString))
            else -> {}
        }
    }

    private fun consumeCompleteTrip(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                assert(apiResponse.data != null)
                renderSuccessTripStatusResponse(apiResponse.data)
            }
            Status.ERROR -> Utils.setToast(activity, resources.getString(R.string.errorString))
            else -> {}
        }
    }

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

    private fun customerUnloadResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
             ProgressDialog.getInstance().show(getActivity())
            }
            Status.SUCCESS -> {
                assert(apiResponse.data != null)
                ProgressDialog.getInstance().dismiss()
                customerUnloadSuccessResponse(apiResponse.data)
            }
            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(activity, resources.getString(R.string.errorString))
            }
            else -> {}
        }
    }

    private fun consumeNotifiction(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                assert(apiResponse.data != null)
                renderSuccessNotifiction(apiResponse.data)
            }
            Status.ERROR -> Utils.setToast(activity, resources.getString(R.string.errorString))
            else -> {}
        }
    }

    private fun consumeRecodingVoiceResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                assert(apiResponse.data != null)
                renderSuccessVoiceResponse(apiResponse.data)
            }
            Status.ERROR -> Utils.setToast(activity, resources.getString(R.string.errorString))
            else -> {}
        }
    }

    private fun consumeVerifyOrderOtpResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                assert(apiResponse.data != null)
                if (apiResponse.data!!.asBoolean) {
                    if (dialog != null) {
                        dialog!!.dismiss()
                    }

                        ProgressDialog.getInstance().dismiss()
                    isOtpVerified = true
                    val deliveryOptimizationDc = DeliveryOptimizationDc(
                        TripPlannerConfirmedOrderId,
                        orderId,
                        Utils.getDoubleLat(activity),
                        Utils.getDoubleLag(activity)
                    )
                    orderPlacedModel = OrderPlacedModel(
                        orderDispatchedMasterId,
                        orderId,
                        orderStatus,
                        orderComment,
                        deliveryIssuenceID,
                        "0",
                        0.0,
                        0.0,
                        "",
                        SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE).toString(),
                        SharePrefs.getInstance(activity).getString(SharePrefs.PEAOPLE_FIRST_NAME),
                        redispatchCount,
                        SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID),
                        "",
                        Utils.getDoubleLat(activity).toString(),
                        Utils.getDoubleLag(activity).toString(),
                        null,
                        deliveryOptimizationDc,
                        date
                    )
                    val gson = Gson()
                    println("ChequeJson" + gson.toJson(orderPlacedModel))
                    mySingleTripMapViewModel!!.hitOrderPlacedApi(orderPlacedModel)
                } else {
                    ProgressDialog.getInstance().dismiss()
                    Utils.setToast(activity, resources.getString(R.string.enter_correct_otp))
                }
            }
            Status.ERROR -> Utils.setToast(activity, resources.getString(R.string.errorString))
            else -> {}
        }
    }

    private fun renderSuccessTripStatusResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getBoolean("Status")) {
                        startActivity(
                            Intent(activity, OrderDetailActivity::class.java).putExtra(
                                Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
                                TripPlannerConfirmedDetailId
                            ).putExtra("isGeneralOrder", isGeneralOrder)
                        )
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

    private fun renderSuccessNotifiction(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
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

    private fun renderSuccessVoiceResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    val voiceRecodingModel =
                        Gson().fromJson(jsonObject.toString(), VoiceRecodingModel::class.java)
                    if (voiceRecodingModel != null) {
                        if (!com.sk.ziladelivery.utilities.TextUtils.isNullOrEmpty(
                                voiceRecodingModel.recordingUrl
                            )
                        ) {
                            mBinding!!.coordinateLayout.tvVoiceRecoding.setImageDrawable(
                                resources.getDrawable(
                                    R.drawable.ic_baseline_play_arrow_24
                                )
                            )
                            mBinding!!.coordinateLayout.tvVoiceRecoding.isEnabled = false
                            playVoiceRecoding(voiceRecodingModel.recordingUrl)
                        }
                        if (!com.sk.ziladelivery.utilities.TextUtils.isNullOrEmpty(
                                voiceRecodingModel.comment
                            )
                        ) {
                            mBinding!!.coordinateLayout.tvComment.text = voiceRecodingModel.comment
                        }
                    } else {
                        Utils.setToast(activity, "No Data found")
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
            Utils.setToast(activity, "No Voice Recoding Found")
        }
    }

    private fun playVoiceRecoding(recordingUrl: String) {
        try {
            val uri = Uri.parse(recordingUrl)
            player = MediaPlayer()
            player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player!!.setDataSource(requireActivity(), uri)
            player!!.setOnCompletionListener { mediaPlayer: MediaPlayer? ->
                // Do something when media player end playing
                mBinding!!.coordinateLayout.tvVoiceRecoding.isEnabled = true
                mBinding!!.coordinateLayout.tvVoiceRecoding.setImageDrawable(resources.getDrawable(R.drawable.ic_recoding))
            }
            player!!.prepare()
            player!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showOtpDialog(isOtpSent: Boolean) {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_order_otp, null)
        dialog = BottomSheetDialog(requireActivity())
        dialog!!.setContentView(view)
        val ivClose = dialog!!.findViewById<AppCompatImageView>(R.id.iv_close)
        val tiOtp = dialog!!.findViewById<TextInputLayout>(R.id.ti_otp)
        val etOtp = dialog!!.findViewById<AppCompatEditText>(R.id.et_otp)
        val btnVerify = dialog!!.findViewById<AppCompatButton>(R.id.btn_verify)
        val tvTime = dialog!!.findViewById<AppCompatTextView>(R.id.tv_time)
        val btnResend = dialog!!.findViewById<AppCompatButton>(R.id.btn_resend)
        val timer: CountDownTimer = object : CountDownTimer((60 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )
                tvTime!!.text = "Resend OTP in $hms"
            }

            override fun onFinish() {
                assert(tvTime != null)
                tvTime!!.text = "" //On finish change timer text
                assert(btnResend != null)
                btnResend!!.isEnabled = true
                cancel()
            }
        }
        assert(btnVerify != null)
        btnVerify!!.setOnClickListener { v: View? ->
            val otp = Objects.requireNonNull(
                etOtp!!.text
            ).toString()
            if (TextUtils.isEmpty(otp)) {
                assert(tiOtp != null)
                tiOtp!!.error = resources.getString(R.string.enteotp)
            } else {
                mySingleTripMapViewModel!!.verifyOrderOTP(orderId, orderStatus, otp)
                ProgressDialog.getInstance().show(requireContext())
            }
        }
        assert(btnResend != null)
        btnResend!!.setOnClickListener { v: View? ->
            mySingleTripMapViewModel!!.generateOrderOTP(
                orderId,
                orderStatus,
                Utils.getDoubleLat(activity),
                Utils.getDoubleLag(activity)
            )
            btnResend.isEnabled = false
            timer.start()
        }
        assert(ivClose != null)
        ivClose!!.setOnClickListener { v: View? -> dialog!!.dismiss() }
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    private fun SingleTriprenderSuccessResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    orderResponseModel = Gson().fromJson(jsonObject.toString(), MySingleTripOrderResponseModel::class.java)
                    if(orderResponseModel!=null){
                        if(orderResponseModel!!.customerOrderinfoDc!=null){
                            if (orderResponseModel!!.customerOrderinfoDc.customerId != 0) {
                                mBinding!!.coordinateLayout.tvVoiceRecoding.visibility = View.VISIBLE
                            }
                            orderCount = orderResponseModel!!.customerOrderinfoDc.orderCount

                            if (orderResponseModel!!.customerOrderinfoDc.isReturnOrder){
                                mBinding!!.bottomLayout.llCompleteReturnTrip.visibility = View.VISIBLE
                            }else{
                                mBinding!!.bottomLayout.llCompleteReturnTrip.visibility = View.GONE
                            }
                            if (orderResponseModel!!.customerOrderinfoDc.isGeneralOrder){
                                mBinding!!.bottomLayout.llContinue.visibility = View.VISIBLE
                            }else{
                                mBinding!!.bottomLayout.llContinue.visibility = View.GONE
                            }

                            isReturnOrder = orderResponseModel!!.customerOrderinfoDc.isReturnOrder
                            isGeneralOrder = orderResponseModel!!.customerOrderinfoDc.isGeneralOrder

                            val unloadinghr = Math.round((orderResponseModel!!.singleOrderMapviewInfoDC.unloadingTime / 60).toFloat())
                            val unloadingmin = Math.round((orderResponseModel!!.singleOrderMapviewInfoDC.unloadingTime % 60).toFloat())
                            TripPlannerConfirmedDetailId = orderResponseModel!!.customerOrderinfoDc.zilaTripDetailId
                            TripPlannerConfirmedOrderId = orderResponseModel!!.customerOrderinfoDc.tripPlannerConfirmedOrderId
                            warehouseLat = orderResponseModel!!.singleOrderMapviewInfoDC.warehouesLat
                            warehouseLong = orderResponseModel!!.singleOrderMapviewInfoDC.warehouesLng
                            voiceNoteString = orderResponseModel!!.customerOrderinfoDc.voiceNote
                            isTakeShopImage = orderResponseModel!!.customerOrderinfoDc.isTakeShopImage
                            mBinding!!.bottomLayout.tvShopName.text = orderResponseModel!!.customerOrderinfoDc.shopName
                            mBinding!!.bottomLayout.tvShopSkcode.text = orderResponseModel!!.customerOrderinfoDc.skcode
                            if (!com.sk.ziladelivery.utilities.TextUtils.isNullOrEmpty(orderResponseModel!!.getCustomerOrderinfoDc().shippingAddress)) {
                                mBinding!!.bottomLayout.tvAddresh.text = "Address: " + orderResponseModel!!.getCustomerOrderinfoDc().shippingAddress
                            } else {
                                mBinding!!.bottomLayout.tvAddresh.visibility = View.INVISIBLE
                            }
                            mBinding!!.bottomLayout.unloadingTime.text = unloadinghr.toString() + "h: " + unloadingmin + "m"
                            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                            mapFragment!!.getMapAsync(this)
                        }else{

                        }

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

    private fun renderSuccessResponseForStatusUpdate(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    var isShipped = false
                    val tripOrderStatusUpdateModel = Gson().fromJson(
                        jsonObject.toString(),
                        TripOrderStatusUpdateModel::class.java
                    )
                    Skcode = tripOrderStatusUpdateModel.customerinfo.skcode
                    shopName = tripOrderStatusUpdateModel.customerinfo.shopname
                    tripCompletedPopupBinding!!.shopName.text = shopName
                    tripCompletedPopupBinding!!.skCode.text = Skcode
                    for (i in tripOrderStatusUpdateModel.customerorderinfo.indices) {
                        if (tripOrderStatusUpdateModel.customerorderinfo[i].status.equals(
                                "Shipped",
                                ignoreCase = true
                            )
                        ) {
                            isShipped = true
                            break
                        }
                    }
                    if (isShipped) {
                        val tripCompleteAdapter = TripCompleteAdapter(
                            activity,
                            tripOrderStatusUpdateModel.customerorderinfo
                        )
                        rvStatus!!.adapter = tripCompleteAdapter
                    } else {
                        if (customDialog != null) {
                            customDialog!!.dismiss()
                        }
                        requireActivity().onBackPressed()
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

    private fun consumeResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
               ProgressDialog.getInstance().show(getActivity())
            }
            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                assert(apiResponse.data != null)
                renderSuccessResponse(apiResponse.data)
            }
            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(activity, resources.getString(R.string.errorString))
            }
            else -> {}
        }
    }

    private fun UnloadingconsumeResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
               ProgressDialog.getInstance().show(getActivity())
            }
            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                assert(apiResponse.data != null)
                UnloadingrenderSuccessResponse(apiResponse.data)
            }
            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(activity, resources.getString(R.string.errorString))
            }
            else -> {}
        }
    }

    private fun renderSuccessResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    shippingaddressList!!.clear()
                    val jsonObject = JSONObject(response.toString())
                    val routeorderResponseModel = Gson().fromJson(
                        jsonObject.toString(),
                        MyTripMapOrderResponseModel::class.java
                    )
                    OrderList = routeorderResponseModel.customerListDcs
                    warehouseLat = routeorderResponseModel.mapViewModel.warehouesLat
                    warehouseLong = routeorderResponseModel.mapViewModel.warehouesLng
                    val ordercompletionTimehr =
                        routeorderResponseModel.mapViewModel.totalOrderCompletionTime / 60
                    val ordercompletionTimemin =
                        routeorderResponseModel.mapViewModel.totalOrderCompletionTime % 60
                    val unloadinghr = routeorderResponseModel.mapViewModel.totalunlodingTime / 60
                    val unloadingmin = routeorderResponseModel.mapViewModel.totalunlodingTime % 60
                    val totaltimetoreach =
                        routeorderResponseModel.mapViewModel.totalOrderCompletionTime - routeorderResponseModel.mapViewModel.totalunlodingTime
                    val totaltimetoreachhr = totaltimetoreach / 60
                    val totaltimetoreachm = totaltimetoreach % 60
                    mBinding!!.bottomLayout.tvDistance.text =
                        "(" + routeorderResponseModel.mapViewModel.totalKM / 1000 + "KM" + ")"
                    mBinding!!.bottomLayout.orderTime.text = ordercompletionTimehr.toString() + "h: " + ordercompletionTimemin + "m"
                    mBinding!!.bottomLayout.unloadingTime.text = unloadinghr.toString() + "h: " + unloadingmin + "m"
                    mBinding!!.bottomLayout.tvTime.text = totaltimetoreachhr.toString() + "h: " + totaltimetoreachm + "m"
                    addresses = geocoder!!.getFromLocation(
                        routeorderResponseModel.mapViewModel.warehouesLat,
                        routeorderResponseModel.mapViewModel.warehouesLng,
                        1
                    )
                    for (i in routeorderResponseModel.customerListDcs.indices) {
                        shippingaddressList!!.add(
                            RoutModel(
                                routeorderResponseModel.customerListDcs[i].skcode,
                                routeorderResponseModel.customerListDcs[i].customerName,
                                routeorderResponseModel.customerListDcs[i].shippingAddress
                            )
                        )
                        shippingaddressListForMap!!.add(routeorderResponseModel.customerListDcs[i].shippingAddress)
                    }
                    val entireRouteAdapter = EntireRouteAdapter(requireActivity(), shippingaddressList!!)
                    mBinding!!.bottomLayout.rvLocation.layoutManager = LinearLayoutManager(activity)
                    mBinding!!.bottomLayout.rvLocation.adapter = entireRouteAdapter
                    println("RESPONSE>>>> " + routeorderResponseModel.mapViewModel.totalKM)
                    val mapFragment =
                        childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                    mapFragment!!.getMapAsync(this)
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

    private fun UnloadingrenderSuccessResponse(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getBoolean("Status")) {
                        if (customerLatLng!!.latitude != 0.0 && customerLatLng!!.longitude != 0.0) {
                            val model = BackgroundServiceModel(
                                SharePrefs.getInstance(activity)
                                    .getInt(SharePrefs.TripPlannerVehicleId),
                                customerLatLng!!.latitude,
                                customerLatLng!!.longitude,
                                5,
                                Date().toString(),
                                SharePrefs.getInstance(activity)
                                    .getInt(SharePrefs.TripPlannerConfirmedDetailId),
                                0
                            )
                            SharePrefs.setCashmanagmentSharedPreference(
                                activity,
                                SharePrefs.BACKGROUND_DATA,
                                Gson().toJson(model)
                            )
                        }
                        val intent = Intent(activity, NewOrderPlaceActivity::class.java)
                        intent.putExtra("ORDER_ID", orderId)
                        intent.putExtra("SkCode", Skcode)
                        intent.putExtra("CUSTOMER_NAME", shopName)
                        intent.putExtra(
                            Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
                            TripPlannerConfirmedOrderId
                        )
                        intent.putExtra("assignmentID", assignId)
                        intent.putExtra("from", "MySingleTripFragment")
                        startActivityForResult(intent, 401)
                    } else {
                        Utils.setToast(activity, jsonObject.getString("Message"))
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

    private fun redispatchAnnorderCancelVerifyinOTP() {
        val intent = Intent(activity, OrderDetailActivity::class.java)
        intent.putExtra("isGeneralOrder", isGeneralOrder)
        intent.putExtra("time", time)
        intent.putExtra(
            "NotifyDeliveryCancelled",
            Constant.REDISPATCH_ANNORDER_CANCEL_VERIFYINN_OTP
        )
        intent.putExtra("ORDER_ID", ORDER_ID)
        intent.putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, TripPlannerConfirmedDetailId)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun notifyDeliveryCancle() {
        val intent = Intent(activity, OrderDetailActivity::class.java)
        intent.putExtra("isGeneralOrder", isGeneralOrder)
        intent.putExtra("time", time)
        intent.putExtra("NotifyDeliveryCancelled", Constant.NOTIFY_DELIVERY_CANCELLED)
        intent.putExtra("ORDER_ID", ORDER_ID)
        intent.putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, TripPlannerConfirmedDetailId)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun verifyingOTPCall() {
        val intent = Intent(activity, CollectPaymentActivity::class.java)
        intent.putExtra("time", time)
        intent.putExtra("CustomerTripStatus", Constant.VERIFYING_OTP)
        intent.putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, TripPlannerConfirmedDetailId)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun callectingPayment() {
        val intent = Intent(activity, CollectPaymentActivity::class.java)
        intent.putExtra("time", time)
        intent.putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, TripPlannerConfirmedDetailId)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun reachedDistinationCall() {
        val intent = Intent(activity, OrderDetailActivity::class.java)
        intent.putExtra("time", time)
        intent.putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, TripPlannerConfirmedDetailId)
        intent.putExtra("isGeneralOrder", isGeneralOrder)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun returnReachedDistinationCall() {
        val intent = Intent(activity, OrderDetailReturnActivity::class.java)
        intent.putExtra("isReturnOrder", isReturnOrder)
        intent.putExtra("time", time)
        intent.putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, TripPlannerConfirmedDetailId)
        intent.putExtra("isReturnOrder", isReturnOrder)
        startActivity(intent)
        requireActivity().finish()
    }
    private fun unloadingCall() {
        val intent = Intent(activity, UnloadItemActivity::class.java)
        intent.putExtra("time", time)
        intent.putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, TripPlannerConfirmedDetailId)
        startActivity(intent)
        requireActivity().finish()
    }

    private val getimageResponse: DisposableObserver<String?> =
        object : DisposableObserver<String?>() {
            override fun onNext(response: String) {
                try {
                    if (response != null) {
                        isImageUploaded = true
                        imageUrl = response
                        if (ImageUploadCustomDialog != null) {
                            Picasso.get().load(
                                SharePrefs.getInstance(MyApplication.getInstance())
                                    .getString(SharePrefs.BASEURL) + response
                            ).into(
                                imageUploadPopupBinding!!.ivImage
                            )
                            Log.e("Success", "Success$response")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {
                Utils.hideProgressDialog(activity)
            }
        }

    private fun UploadMiloMeterImage(
        observer: DisposableObserver<String?>, body: MultipartBody.Part
    ) {
        RestClient.getInstance().service.miloMeterImageUpload(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    ProgressDialog.getInstance().show(requireContext())
                }

                override fun onNext(`object`: String) {
                    observer.onNext(`object`)
                    ProgressDialog.getInstance().dismiss()
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "Error:$e")
                    observer.onError(e)
                    ProgressDialog.getInstance().dismiss()
                }

                override fun onComplete() {
                    observer.onComplete()
                    ProgressDialog.getInstance().dismiss()
                }
            })
    }

    @SuppressLint("StaticFieldLeak")
    private inner class FetchUrl : AsyncTask<String?, Void?, String>() {
        
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            ProgressDialog.getInstance().dismiss()
            val parserTask = ParserTask()
            parserTask.execute(result)
        }

        override fun doInBackground(vararg params: String?): String {
            var data = ""
            try {
                data = Utils.downloadUrl(params[0])
                Log.e("Background Task data", data)
            } catch (e: Exception) {
                Log.d("Background Task", e.toString())
            }
            return data
        }

    }

    @SuppressLint("StaticFieldLeak")
    private inner class ParserTask :
        AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {
        
        override fun doInBackground(vararg params: String?): List<List<HashMap<String, String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null
            ProgressDialog.getInstance().dismiss()
            try {
                jObject = JSONObject(params[0])
                val parser = DirectionsJSONParserNew(this@MySingleTripMapViewFragment)
                routes = parser.parse(jObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return routes
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            lineOptions = null
            Log.d("ParserTask123", result.toString())
            for (i in result!!.indices) {
                points = ArrayList()
                lineOptions = PolylineOptions()
                val path = result[i]
                for (j in path.indices) {
                    val point = path[j]
                    val lat = point["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
                lineOptions!!.addAll(points)
                lineOptions!!.width(12f)
                lineOptions!!.color(resources.getColor(R.color.colorLightBlueHeader))
            }
            if (lineOptions != null) {
                googleMapMain!!.addPolyline(lineOptions!!)
            } else {
                Log.d("onPostExecute", "without Polylines drawn")
            }
        }
    }

    companion object {
        fun scaleBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int, skcode: String?): Bitmap {
            val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
            val scaleX = newWidth / bitmap.width.toFloat()
            val scaleY = newHeight / bitmap.height.toFloat()
            val pivotX = 0f
            val pivotY = 0f
            val scaleMatrix = Matrix()
            scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY)
            val canvas = Canvas(scaledBitmap)
            canvas.setMatrix(scaleMatrix)
            canvas.drawBitmap(bitmap, 0f, 0f, Paint(Paint.FILTER_BITMAP_FLAG))
            val color = Paint()
            color.textSize = 305f
            color.color = Color.WHITE
            canvas.drawText(skcode!!, 180f, 400f, color)
            return scaledBitmap
        }
    }
}