package com.sk.ziladelivery.ui.views.fragment.unloadReturnItem


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.ActivityUnloadReturnItemsBinding
import com.sk.ziladelivery.listener.UnloadItemReturnInterface
import com.sk.ziladelivery.ui.views.main.MainActivity
import com.sk.ziladelivery.ui.views.viewmodels.UnloadItemViewModel
import com.sk.ziladelivery.utilities.ApiResponse
import com.sk.ziladelivery.utilities.Constant
import com.sk.ziladelivery.utilities.CustomRunnable
import com.sk.ziladelivery.utilities.EasyWayLocation
import com.sk.ziladelivery.utilities.GenericTextWatcher
import com.sk.ziladelivery.utilities.Listener
import com.sk.ziladelivery.utilities.ProgressDialog
import com.sk.ziladelivery.utilities.SharePrefs
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.utilities.Utils.activity
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class UnloadItemReturnActivity : AppCompatActivity(), UnloadItemReturnInterface,
    View.OnClickListener, Listener {
    var mBinding: ActivityUnloadReturnItemsBinding? = null
    private var unloadItemViewModel: UnloadItemViewModel? = null
    private var tripPlannerConfirmedDetailId = 0
    private var time: String? = null
    private var type: String? = null
    private var handler: Handler? = null
    private var customRunnable: CustomRunnable? = null
    var unloadItemReturnAdapter: UnloadItemReturnAdapter? = null
    var filteredlist: ArrayList<ReturnItemListResponseModel>? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var imageFilePath: String? = null
    private var customerDocUpload: String? = null
    var uplodImageRespnseModel: UplodImageRespnseModel? = null
    var model: List<ReturnOrderCreditNoteRequestModel>? = null
    var message = ""
    var returnItemList = ArrayList<ReturnItemListResponseModel>()
    var newOrderId = 0
    var returnOrderCreditNoteResponseModel: ReturnOrderCreditNoteResponseModel? = null
    var pickedReturnOrderByDBoyResponseModel: PickedReturnOrderByDBoyResponseModel? = null
    private var finallyCompleetReturnDilog: BottomSheetDialog? = null
    private var timerTime: Long = 900000
    private var otpDialog: BottomSheetDialog? = null
    private var btnResendOtp: Button? = null
    private var tvTimerOTP: TextView? = null
    private var tvOrderId: TextView? = null
    private var cTimer: CountDownTimer? = null
    var returnItemSendOtpResponseModel: GenerateOTPForReturnOrderResponse? = null
    val orderIDList: MutableList<Int> = ArrayList()
    var status = ""
    var returnAmount = ""
    var returnItemListModelFinal: ReturnItemListResponseModel? = null
    private var easyWayLocation: EasyWayLocation? = null
    private var customerLatLng: LatLng? = null
    val mOrderDetaileList: ArrayList<OrderDetaileList> = ArrayList()



    companion object {
        var isImageCapture = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_unload_return_items)
        unloadItemViewModel = ViewModelProvider(this)[UnloadItemViewModel::class.java]
        dataFromIntent
        setActionBarConfiguration()
        callApis()
        initView()
    }

    private fun initView() {
        mBinding!!.btCompleetReturn.setOnClickListener(this)
        unloadItemViewModel!!.returnItemList(tripPlannerConfirmedDetailId)

        mBinding!!.rvItemList.layoutManager = LinearLayoutManager(this)
        mBinding!!.etSearchCust.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (returnItemList.size > 0) {
                    if (s.toString().isNotEmpty()) {
                        filter(s.toString())
                    } else {
                        unloadItemReturnAdapter!!.updateList(
                            returnItemList,
                        )
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                mBinding!!.tvNotDataFound.visibility = View.GONE
            }
            override fun afterTextChanged(s: Editable) {

            }
        })

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    CoroutineScope(Dispatchers.IO).launch {
                        uploadFilePth()
                    }
                } else {
                    returnItemList.forEach {
                        if (it.NewOrderId == returnItemListModelFinal!!.NewOrderId) {
                            it.isChecked = false
                        }
                    }
                    unloadItemReturnAdapter!!.updateList(returnItemList)
                    compleetReturnBt()
                }

            }
        customerLatLng = LatLng(0.0, 0.0)
        easyWayLocation = EasyWayLocation(this, createLocationRequest(), false, true, this)
        easyWayLocation!!.startLocation()
    }

    override fun locationOn() {}
    override fun currentLocation(location: Location) {
        easyWayLocation!!.endUpdates()
        customerLatLng = LatLng(location.latitude, location.longitude)

    }
    override fun locationCancelled() {}

    private fun createLocationRequest() = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.MINUTES.toMillis(5)
    ).apply {
        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        setDurationMillis(TimeUnit.MINUTES.toMillis(5))
        setWaitForAccurateLocation(true)
        setMaxUpdates(1)
    }.build()

    private fun filter(text: String) {
        filteredlist = ArrayList()
        for (item in returnItemList) {
            if (item.Itemname.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filteredlist!!.add(item)
            }
        }
        if (filteredlist!!.isEmpty()) {
            unloadItemReturnAdapter!!.updateList(filteredlist!!)
            mBinding!!.llItemNameView.visibility = View.GONE
            mBinding!!.tvNotDataFound.visibility = View.VISIBLE
        } else {
            mBinding!!.tvNotDataFound.visibility = View.GONE
            mBinding!!.llItemNameView.visibility = View.VISIBLE
            unloadItemReturnAdapter!!.updateList(filteredlist!!)
        }
    }

    private fun callApis() {
        unloadItemViewModel!!.returnItemListLiveData().observe(this) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeResponseForStatusUpdate(
                    it
                )
            }
        }

        unloadItemViewModel!!.unloadItemsImageDataLiveData.observe(this) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeResponseForUnloadImage(
                    it
                )
            }
        }

        unloadItemViewModel!!.returnOrderCreditNoteLiveData()
            .observe(this) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    consumeResponseFoPickedReturnOrder(
                        it
                    )
                }
            }

        unloadItemViewModel!!.pickedReturnOrderByDBoyLiveData()
            .observe(this) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    pickedReturnOrderByDBoy(
                        it
                    )
                }
            }

        unloadItemViewModel!!.generateOTPForReturnOrderLiveData()
            .observe(this) { apiResponse: ApiResponse? ->
                apiResponse?.let {
                    returnOrderSendOtp(
                        it
                    )
                }
            }
    }

    private fun setActionBarConfiguration() {
        if (type == null) {
            mBinding!!.tvTimmer.visibility = View.VISIBLE
            timer(time, mBinding!!.tvTimmer)
        } else {
            mBinding!!.tvTimmer.visibility = View.GONE
        }
        mBinding!!.ivBack.setOnClickListener {
            finish()
        }
    }

    private val dataFromIntent: Unit
        private get() {
            if (intent.extras != null) {
                time = intent.getStringExtra("time")
                tripPlannerConfirmedDetailId =
                    intent.getIntExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, 0)
                type = intent.getStringExtra("type")
            }
        }

    private fun consumeResponseForStatusUpdate(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                assert(apiResponse.data != null)
                renderSuccessResponseForUnloadItem(apiResponse.data)
            }

            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
            }

            else -> {}
        }
    }

    private fun pickedReturnOrderByDBoy(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                assert(apiResponse.data != null)
                pickedReturnOrder(apiResponse.data)
            }

            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
            }

            else -> {}
        }
    }

    private fun returnOrderSendOtp(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                assert(apiResponse.data != null)
                sendOtpReturnOrder(apiResponse.data)

            }

            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
            }

            else -> {}
        }
    }

    private fun renderSuccessResponseForUnloadItem(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonString = response.toString()
                    val gson = Gson()
                    val listType =
                        object : TypeToken<ArrayList<ReturnItemListResponseModel>>() {}.type

                    returnItemList = gson.fromJson(jsonString, listType)

                    if (returnItemList.size > 0) {

                        returnItemList.forEach {
                            status = it.Status
                        }
                        unloadItemReturnAdapter = UnloadItemReturnAdapter(
                            this,
                            returnItemList,
                            this,
                        )
                        mBinding!!.rvItemList.adapter = unloadItemReturnAdapter
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickedReturnOrder(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    // returnItemListResponseModel = Gson().fromJson<ArrayList<ReturnItemListResponseModel>>(response.toString(), ArrayList::class.java)
                    val jsonObject = JSONObject(response.toString())
                    pickedReturnOrderByDBoyResponseModel = Gson().fromJson(
                        jsonObject.toString(),
                        PickedReturnOrderByDBoyResponseModel::class.java
                    )

                    if (pickedReturnOrderByDBoyResponseModel!!.Status) {
                        Toast.makeText(
                            this,
                            pickedReturnOrderByDBoyResponseModel!!.Message,
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, MainActivity::class.java))

                        otpDialog!!.dismiss()

                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            pickedReturnOrderByDBoyResponseModel!!.Message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendOtpReturnOrder(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    returnItemSendOtpResponseModel = Gson().fromJson(
                        jsonObject.toString(),
                        GenerateOTPForReturnOrderResponse::class.java
                    )

                    if (returnItemSendOtpResponseModel!!.Status) {
                        showOtpDialog(30)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun consumeResponseForUnloadImage(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                renderSuccessResponseForUnloadItemImage(apiResponse.data)
            }

            Status.ERROR -> ProgressDialog.getInstance().dismiss()
            else -> {}
        }
    }

    private fun consumeResponseFoPickedReturnOrder(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                renderSuccessResponseForReturnOrder(apiResponse.data)
            }

            Status.ERROR -> ProgressDialog.getInstance().dismiss()
            else -> {}
        }
    }


    private fun renderSuccessResponseForUnloadItemImage(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    uplodImageRespnseModel = Gson().fromJson(jsonObject.toString(), UplodImageRespnseModel::class.java)
                    isImageCapture = true
                    returnItemList.forEach {
                        if (it.NewOrderId == returnItemListModelFinal!!.NewOrderId && it.BatchCode==returnItemListModelFinal!!.BatchCode) {
                            it.ImageURlmessage = uplodImageRespnseModel?.Message.toString()
                            it.ImageURl = SharePrefs.getInstance(activity).getString(SharePrefs.BASEURL) + uplodImageRespnseModel?.ImageUrl!!
                            unloadItemReturnAdapter?.notifyDataSetChanged()
                        }
                    }

                   // unloadItemReturnAdapter!!.updateList(returnItemList)

                    Toast.makeText(this, uplodImageRespnseModel?.Message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(this, uplodImageRespnseModel?.Message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderSuccessResponseForReturnOrder(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    returnOrderCreditNoteResponseModel = Gson().fromJson(
                        jsonObject.toString(),
                        ReturnOrderCreditNoteResponseModel::class.java
                    )

                    if (returnOrderCreditNoteResponseModel!!.Status) {
                        returnAmount = returnOrderCreditNoteResponseModel!!.Data.toString()
                        finallyCompleetReturnDilog()
                    }

                    Toast.makeText(
                        this,
                        returnOrderCreditNoteResponseModel?.Message,
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(this, uplodImageRespnseModel?.Message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun checkBoxClick(
        isChecked: Boolean,
        returnItemListModel: ReturnItemListResponseModel,
        postion: Int,
    ) {
        orderIDList.clear()
        returnItemListModelFinal = returnItemListModel
        compleetReturnBt()
        unloadItemReturnAdapter?.notifyDataSetChanged()
    }

    override fun llCamera(
        model: ReturnItemListResponseModel,
        cheked: Boolean
    ) {
        requestWritePermission()
    }

    fun timer(time: String?, timer: TextView?) {
        var millse: Long = 0
        try {
            val currMillis = System.currentTimeMillis()
            val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
            sdf1.timeZone = TimeZone.getDefault()
            if (time != null) {
                val startTime = sdf1.parse(time)!!
                val startEpoch = startTime.time
                millse = currMillis - startEpoch
            }
            if (handler == null) {
                handler = Handler()
            }
            if (customRunnable != null) {
                customRunnable = null
            }
            customRunnable = CustomRunnable(handler, timer, 10000)
            handler!!.removeCallbacks(customRunnable!!)
            customRunnable!!.holder = timer
            customRunnable!!.millisUntilFinished = millse //Current time - received time
            handler!!.postDelayed(customRunnable!!, 1000)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.btCompleetReturn -> {
                val mDetailsList: ArrayList<ReturnOrderCreditNoteRequestModel> = ArrayList()
                returnItemList.forEach {
                    if (it.isChecked) {
                        orderIDList.add(it.NewOrderId)
                        var image: String = if (it.returnQty == 0 || it.ImageURl==null) "" else it.ImageURl
                        mDetailsList.add(
                            ReturnOrderCreditNoteRequestModel(
                                it.NewOrderId,
                                it.NewOrderDetailId,
                                it.returnQty,
                                it.UnitPrice,
                                image
                            )
                        )
                        mOrderDetaileList.add(OrderDetaileList(it.NewOrderId,it.NewOrderDetailId,it.returnQty,it.UnitPrice,image,it.BatchCode,it.BatchId,it.KKRRRequestId))
                    }

                }
                if (mDetailsList.size != 0) {
                    unloadItemViewModel?.returnOrderCreditNoteObserver(mDetailsList)
                }
            }
        }
    }

    fun pickFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File
        try {
            photoFile = createImageFile()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        val photoUri = FileProvider.getUriForFile(
            (this),
            this.packageName + ".provider",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        resultLauncher?.launch(intent)
    }

    private fun uploadFilePth() {
        val fileToUpload = File(imageFilePath)
        Compressor(this)
            .setQuality(90)
            .setCompressFormat(Bitmap.CompressFormat.JPEG)
            .compressToFileAsFlowable(fileToUpload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ file: File ->
                ImageFile(file)
            }) { throwable: Throwable ->
                throwable.printStackTrace()
                Utils.setToast(this, "" + throwable.message)

            }
    }

    private fun createImageFile(): File {
        customerDocUpload = "trip" + "image" +System.currentTimeMillis()+ ".jpg"
        val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File(Environment.getExternalStorageDirectory().toString() + "/ShopKirana")
        myDir.mkdirs()
        val file = File(storageDir, customerDocUpload)
        imageFilePath = file.absolutePath
        return file
    }

    private fun requestWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                ), 1
            )
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_IMAGES
                ), 1
            )
        } else {
                pickFromCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@UnloadItemReturnActivity,
                            Manifest.permission.CAMERA) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                        pickFromCamera()
                    }
                } else {
                    requestWritePermission()
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


    fun ImageFile(file: File) {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body: MultipartBody.Part = createFormData("file", file.name, requestFile)
        unloadItemViewModel!!.getUnloadItemeItemImageObserver(body)
    }

    private fun compleetReturnBt() {
        var isCheckAll = true
        for (i in returnItemList!!.indices) {
            if (!returnItemList!![i].isChecked) {
                isCheckAll = false
                break
            }
        }
        if (isCheckAll) {
            mBinding!!.btCompleetReturn.background =
                resources.getDrawable(R.drawable.button_bg_blue)
            mBinding!!.btCompleetReturn.isClickable = true
            mBinding!!.btCompleetReturn.isEnabled = true
        } else {
            mBinding!!.btCompleetReturn.background =
                resources.getDrawable(R.drawable.button_bg_drawable)
            mBinding!!.btCompleetReturn.isClickable = false
            mBinding!!.btCompleetReturn.isEnabled = false
        }
    }

    private fun finallyCompleetReturnDilog() {
        ProgressDialog.getInstance().dismiss()
        finallyCompleetReturnDilog = BottomSheetDialog(this)
        finallyCompleetReturnDilog!!.setContentView(R.layout.dialog_finally_compleet)
        finallyCompleetReturnDilog!!.setCanceledOnTouchOutside(false)
        val tvHead = finallyCompleetReturnDilog!!.findViewById<TextView>(R.id.tvHead)
        val crossIV = finallyCompleetReturnDilog!!.findViewById<ImageView>(R.id.ivCross)
        var tvTotalReturnAmount =
            finallyCompleetReturnDilog!!.findViewById<TextView>(R.id.tvTotalReturnAmount)
        val df = DecimalFormat("#.#")
        tvTotalReturnAmount!!.setText("Return Amount : " + "₹ " + df.format(returnAmount.toDouble()))
        val btnReturnComplete =
            finallyCompleetReturnDilog!!.findViewById<Button>(R.id.btnReturnComplete)

        crossIV!!.setOnClickListener {
            finallyCompleetReturnDilog!!.dismiss()
            finish()
        }
        finallyCompleetReturnDilog!!.show()


        btnReturnComplete!!.setOnClickListener {
            val generateOTPForReturnOrder = GenerateOTPForReturnOrder(orderIDList, status)
            unloadItemViewModel!!.generateOTPForReturnOrder(generateOTPForReturnOrder)
        }

        finallyCompleetReturnDilog!!.setOnDismissListener {
            try {

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun showOtpDialog(
        otpTimer: Long
    ) {
        ProgressDialog.getInstance().dismiss()
        timerTime = otpTimer * 1000
        otpDialog = BottomSheetDialog(this)
        otpDialog!!.setContentView(R.layout.dialog_return_otp_verification)
        otpDialog!!.setCanceledOnTouchOutside(false)
        val otpEditBox1 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box1)
        val otpEditBox2 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box2)
        val otpEditBox3 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box3)
        val otpEditBox4 = otpDialog!!.findViewById<EditText>(R.id.otp_edit_box4)
        val tvHead = otpDialog!!.findViewById<TextView>(R.id.tvHead)
        val crossIV = otpDialog!!.findViewById<ImageView>(R.id.ivCross)
        tvOrderId = otpDialog!!.findViewById<TextView>(R.id.tvOrderId)
        // tvHead!!.text = "Verification Code\nOrder Id - $orderIdInterface"
        btnResendOtp = otpDialog!!.findViewById(R.id.btnResendOtp)
        val btnVerifyOtp = otpDialog!!.findViewById<Button>(R.id.btnVerifyOtp)
        tvTimerOTP = otpDialog!!.findViewById(R.id.tvTimer)

        tvOrderId!!.setText("OrderID : ${orderIDList.distinct()}")

        btnResendOtp!!.isEnabled = false
        btnResendOtp!!.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.gray
            )
        )
        timerOtp(timerTime, tvTimerOTP, btnResendOtp, true)
        val edit = arrayOf(otpEditBox1, otpEditBox2, otpEditBox3, otpEditBox4)
        otpEditBox1!!.addTextChangedListener(GenericTextWatcher(otpEditBox1, edit))
        otpEditBox2!!.addTextChangedListener(GenericTextWatcher(otpEditBox2, edit))
        otpEditBox3!!.addTextChangedListener(GenericTextWatcher(otpEditBox3, edit))
        otpEditBox4!!.addTextChangedListener(GenericTextWatcher(otpEditBox4, edit))

        crossIV!!.setOnClickListener {
            otpDialog!!.dismiss()
        }
        btnResendOtp!!.setOnClickListener {
            val generateOTPForReturnOrder = GenerateOTPForReturnOrder(orderIDList, status)
            unloadItemViewModel!!.generateOTPForReturnOrder(generateOTPForReturnOrder)
            otpDialog!!.dismiss()
        }
        btnVerifyOtp!!.setOnClickListener {
            val otp = (otpEditBox1.text.toString() +
                    otpEditBox2.text.toString() +
                    otpEditBox3.text.toString() +
                    otpEditBox4.text.toString())
            if (otp.length < 4) {
                Utils.setToast(this@UnloadItemReturnActivity, "Enter Valid OTP")
            } else {
                var PickedReturnOrderByDBoyRequestModel = PickedReturnOrderByDBoyRequestModel(
                    mOrderDetaileList,
                    otp,
                    SharePrefs.getInstance(this).getInt(SharePrefs.TRIP_MASTER_ID),
                    customerLatLng!!.latitude,
                    customerLatLng!!.longitude,
                    SharePrefs.getInstance(this).getInt(SharePrefs.PEOPLE_ID)
                )

                unloadItemViewModel!!.pickedReturnOrderByDBoyObserver(
                    PickedReturnOrderByDBoyRequestModel
                )
            }
        }
        otpDialog!!.show()
        otpDialog!!.setOnDismissListener {
            try {
                //  customerWiseOrderListApi()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun timerOtp(time: Long, timer: TextView?, btResend: Button?, isChangeColor: Boolean) {
        cancelTimerOtp()
        cTimer = object : CountDownTimer(time, 100) {
            override fun onTick(mills: Long) {
                if (mills >= 60000) {
                    timer!!.text = ("" +
                            (TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(mills)
                            )) + ":" +
                            (TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(mills)
                            )) + " Min")
                } else {
                    timer!!.text =
                        ("" + (TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(mills)
                        )) + " Sec")
                }
                SharePrefs.getInstance(this@UnloadItemReturnActivity)
                    .putLong(SharePrefs.REMAININGTIME, mills)
            }

            override fun onFinish() {
                btResend!!.isEnabled = true
                if (isChangeColor) {
                    btResend.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.orange_dark
                        )
                    )
                }
                timer!!.visibility = View.GONE
                btResend!!.visibility = View.VISIBLE
            }
        }
        cTimer!!.start()
    }

    private fun cancelTimerOtp() {
        if (cTimer != null) cTimer!!.cancel()
    }

}

