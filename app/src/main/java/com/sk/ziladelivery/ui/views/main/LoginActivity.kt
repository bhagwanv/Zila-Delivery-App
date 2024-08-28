package com.sk.ziladelivery.ui.views.main

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.sk.ziladelivery.BuildConfig
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.ForgetpassresponseModel
import com.sk.ziladelivery.data.model.LoginModel
import com.sk.ziladelivery.data.model.LoginResponse
import com.sk.ziladelivery.data.model.TokenResponse
import com.sk.ziladelivery.databinding.LoginActivityBinding
import com.sk.ziladelivery.ui.views.viewmodels.LoginViewModel
import com.sk.ziladelivery.utilities.CommonMethods
import com.sk.ziladelivery.utilities.ProgressDialog
import com.sk.ziladelivery.utilities.SharePrefs
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.TextUtils
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.utilities.Validation
import com.sk.ziladelivery.viewfactory.LoginFactory

class LoginActivity : AppCompatActivity() {
    private var mBinding: LoginActivityBinding? = null
    private var utils: Utils? = null
    private var loginViewModel: LoginViewModel? = null
    private var fcmToken: String? = null
    private var email: String? = null
    private var password: String? = null
    private var androidId: String? = null
    private var customDialog: Dialog? = null
    private var versionName: String? = null
    private var deviceOs: String? = null
    private var devicename: String? = null
    private var iMEI: String? = null
    private var telephonyManager: TelephonyManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        loadingData()
    }


    private fun loadingData() {
        initialization()
        permission()
        mBinding!!.tvForgotpassword.setOnClickListener { forgotPasswordPopup() }
        mBinding!!.btnLogin.setOnClickListener {
            if (isValid()) {
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Utils.setToast(this, resources.getString(R.string.network_error))
                } else {
                    CommonMethods.hideKeyboard(this, it)
                    val loginModel = LoginModel(
                        mBinding!!.etMobile.text.toString(),
                        mBinding!!.etPassword.text.toString(),
                        androidId,
                        fcmToken,
                        versionName,
                        deviceOs,
                        devicename,
                        iMEI
                    )

                    callLoginUser(loginModel)
                }
            }
        }
        mBinding!!.etPassword.setOnEditorActionListener { v, actionId, event ->
            CommonMethods.hideKeyboard(this, v)
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (isValid()) {
                    if (!Utils.checkInternetConnection(applicationContext)) {
                        Toast.makeText(
                            applicationContext,
                            resources.getString(R.string.network_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val loginModel = LoginModel(
                            mBinding!!.etMobile.text.toString(),
                            mBinding!!.etPassword.text.toString(),
                            androidId,
                            fcmToken,
                            versionName,
                            deviceOs,
                            devicename,
                            iMEI
                        )
                        callLoginUser(loginModel)
                    }
                }
            }
            false
        }
    }

    private fun callLoginUser(loginModel: LoginModel) {
        loginViewModel!!.gethitLoginApi(loginModel).observe(this@LoginActivity) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        resource.data?.let { loginDataResponce ->
                            getLoginDataResponce(loginDataResponce)
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

    private fun getLoginDataResponce(loginResponse: LoginResponse) {
        if (loginResponse.status) {
            SharePrefs.getInstance(applicationContext)
                .putBoolean(SharePrefs.LOGGED, true)
            SharePrefs.getInstance(applicationContext)
                .putInt(SharePrefs.PEOPLE_ID, loginResponse.p.peopleID)
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.SK_CODE, loginResponse.p.skcode)
            SharePrefs.getInstance(applicationContext)
                .putInt(SharePrefs.WAREHOUSE_ID, loginResponse.p.warehouseId)
            SharePrefs.getInstance(applicationContext)
                .putInt(SharePrefs.COMPANY_ID, loginResponse.p.companyId)
            SharePrefs.getInstance(applicationContext).putString(
                SharePrefs.PEAOPLE_FIRST_NAME,
                loginResponse.p.peopleFirstName
            )
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.PEAOPLE_LAST_NAME, loginResponse.p.peopleLastName)
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.PEAOPLE_EMAIL, loginResponse.p.email)
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.PEAOPLE_IMAGE, loginResponse.p.imageUrl)
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.MOBILE, loginResponse.p.mobile)
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.EMAILID, loginResponse.p.email)
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.VEHICLE_NAME, loginResponse.p.vehicleName)
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.VEHICLE_NUMBER, loginResponse.p.vehicleNumber)
            SharePrefs.getInstance(applicationContext)
                .putInt(SharePrefs.ASSIGNMENT_ID, 0)

            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.LOGIN_PASSWORD, loginResponse.p.password)

            Utils.setToast(
                applicationContext,
                "Welcome " + loginResponse.p.peopleFirstName.trim { it <= ' ' })
            val role = loginResponse.p.role
            val elements = role.split(",".toRegex()).toTypedArray()
            val fixedLengthList = listOf(*elements)
            val listOfRole = ArrayList(fixedLengthList)
            for (i in listOfRole.indices) {
                if (listOfRole[i].equals("Online Order Cancel", ignoreCase = true)) {
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.ROLE, listOfRole[i])
                    break
                } else {
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.ROLE, "")
                }
            }
            val regApk = loginResponse.p.getRegisteredApk()
            if (regApk != null) {
                val Password = regApk.password
                val username = regApk.userName
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.TOKEN_NAME, username)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.TOKEN_PASSWORD, Password)
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.network_error),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    getTokenData("password", username, Password)
                }
            }
        } else {
            Utils.setToast(applicationContext, loginResponse.message)
        }
    }

    private fun getTokenData(pass: String, username: String, Password: String) {
        loginViewModel!!.getTokenData(pass, username, Password)
            .observe(this@LoginActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { tokenResponce ->
                                getTokenResponce(tokenResponce)
                            }
                        }

                        Status.ERROR -> {
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {

                        }
                    }
                }
            }
    }

    private fun getTokenResponce(tokenResponce: TokenResponse) {
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.TOKEN, tokenResponce.access_token)
        finish()
        startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
        Utils.leftTransaction(this)
    }

    private fun isValid(): Boolean {
        email = mBinding!!.etMobile.text.toString().trim { it <= ' ' }
        password = mBinding!!.etPassword.text.toString().trim { it <= ' ' }
        if (TextUtils.isNullOrEmpty(email)) {
            Utils.setToast(this, resources.getString(R.string.enter_mobile_number))
            return false
        } else if (Validation.chkmobileNo(email)) {
            Utils.setToast(this, resources.getString(R.string.enter_valid_mobile))
            return false
        } else if (TextUtils.isNullOrEmpty(password!!.trim { it <= ' ' })) {
            Utils.setToast(this, resources.getString(R.string.enter_password))
            return false
        } else if (password!!.trim { it <= ' ' }.length < 6) {
            Utils.setToast(this, resources.getString(R.string.enter_valid_password))
            return false
        }
        return true
    }

    private fun initialization() {
        loginViewModel = ViewModelProvider(
            viewModelStore,
            LoginFactory(ApiHelper(RestClient.getInstance().service))
        )[LoginViewModel::class.java]
        mBinding!!.loginViewModel = loginViewModel
        mBinding!!.lifecycleOwner = this
        versionName = BuildConfig.VERSION_NAME
        deviceOs = Build.VERSION.RELEASE
        println("version$deviceOs")
        devicename = Build.MODEL
        customDialog = Dialog(this, R.style.CustomDialog)
        FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
            if (result != null) {
                fcmToken = result
            }
        }

        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        utils = Utils(this)
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
    }

    private fun permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            val rationale =
                "Direct last mile collects location data to enable to tracking delivery boy for order delivered even when the app is closed or not in use \n open the Settings app\nTap Privacy And then Permission manager."
            val options = Permissions.Options()
                .setRationaleDialogTitle("Background Location")
                .setSettingsDialogTitle("Warning")

            Permissions.check(this, permissions, rationale, options, object : PermissionHandler() {
                override fun onGranted() {
                    iMEI = Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                }

                override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {}
            })
        } else {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            )
            val rationale =
                "Direct last mile collects location data to enable to tracking delivery boy for order delivered even when the app is closed or not in use \n open the Settings app\nTap Privacy And then Permission manager."
            val options = Permissions.Options()
                .setRationaleDialogTitle("Background Location")
                .setSettingsDialogTitle("Warning")
            Permissions.check(this, permissions, rationale, options, object : PermissionHandler() {
                override fun onGranted() {
                    iMEI = Settings.Secure.getString(
                        this@LoginActivity.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                }

                override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {

                }
            })
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        Utils.hideProgressDialog()
    }

    private fun forgotPasswordPopup() {
        val inflater = layoutInflater
        val mView = inflater.inflate(R.layout.forgot_password_popup, null)
        customDialog = Dialog(this, R.style.CustomDialog)
        customDialog!!.setContentView(mView)
        val okBtn = mView.findViewById<LinearLayout>(R.id.ok_btn)
        val cancelBtn = mView.findViewById<LinearLayout>(R.id.cancel_btn)
        val et_Mobile_No = mView.findViewById<EditText>(R.id.et_Mobile_No)
        okBtn.setOnClickListener { v: View? ->
            if (et_Mobile_No.text.toString().trim { it <= ' ' }
                    .equals("", ignoreCase = true)) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.enter_mobile_number),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (TextUtils.isValidMobileNo(et_Mobile_No.text.toString().trim { it <= ' ' })) {
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.network_error),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    forgetPasswordData(et_Mobile_No.text.toString().trim { it <= ' ' })

                }
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.validMobilenumbe),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        cancelBtn.setOnClickListener { v: View? -> customDialog!!.dismiss() }
        customDialog!!.show()
    }

    private fun forgetPasswordData(mobile: String) {
        loginViewModel!!.forgetPassword(mobile)
            .observe(this@LoginActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { forgetPassword ->
                                getForgetPasswored(forgetPassword)
                            }
                        }

                        Status.ERROR -> {
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {

                        }
                    }
                }
            }


    }

    private fun getForgetPasswored(forgetpassresponseModel: ForgetpassresponseModel) {
        if (forgetpassresponseModel.status) {
            Utils.setToast(applicationContext, forgetpassresponseModel.message)
            if (customDialog != null) {
                customDialog!!.dismiss()
            }
        } else {
            Utils.setToast(applicationContext, forgetpassresponseModel.message)
        }
    }
}