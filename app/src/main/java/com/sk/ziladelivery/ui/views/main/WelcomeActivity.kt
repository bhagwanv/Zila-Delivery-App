package com.sk.ziladelivery.ui.views.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.firebase.messaging.FirebaseMessaging
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.sk.ziladelivery.BuildConfig
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.LoginResponse
import com.sk.ziladelivery.data.model.OTPModel
import com.sk.ziladelivery.data.model.TokenResponse
import com.sk.ziladelivery.databinding.ActivityWelcomeBinding
import com.sk.ziladelivery.ui.views.viewmodels.OTPViewModel
import com.sk.ziladelivery.utilities.ExtendedViewPager
import com.sk.ziladelivery.utilities.RxBus
import com.sk.ziladelivery.utilities.SharePrefs
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.TextUtils
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.viewfactory.WellComeFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver

class WelcomeActivity : AppCompatActivity() {
    private var mBinding: ActivityWelcomeBinding? = null
    private var viewPager: ViewPager? = null
    private val cityspinner: Spinner? = null
    private var dotsLayout: LinearLayout? = null
    private var btnback: Button? = null
    private var btnNext: Button? = null
    private var phoneno: EditText? = null
    private var otp: EditText? = null
    private var tvResendOtpTimer: TextView? = null
    private var resendotp: TextView? = null
    private var tv_oldnumber: TextView? = null
    private lateinit var layouts: IntArray
    private val customerid = 0
    private val Mobileno = "Mobileno"
    private var comFromClass: String? = null
    private val selectcity_Id: String? = ""
    private var cTimer: CountDownTimer? = null
    private var fcmToken: String? = null
    private var deviceID: String? = null
    private var otpViewModel: OTPViewModel? = null
    private var OTPNumberString: String? = null
    private var login: Button? = null
    private var enterbypotp: Button? = null
    private var IMEI: String? = null
    private var telephonyManager: TelephonyManager? = null
    lateinit var timerProgressBar: ProgressBar
    lateinit var rlTimer: RelativeLayout
    private var multipleClick = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        mBinding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(mBinding!!.root)
        otpViewModel = ViewModelProviders.of(
            this,
            WellComeFactory(ApiHelper(RestClient.getInstance().service))
        )[OTPViewModel::class.java]

        // init
        initialization()
        permission()

        val cvp = findViewById<ExtendedViewPager>(R.id.view_pager_view)
        val myViewPagerAdapter = MyViewPagerAdapter(this, cvp, cityspinner, layouts)
        cvp.adapter = myViewPagerAdapter
        cvp.offscreenPageLimit = 5
        addBottomDots(0)
        changeStatusBarColor()
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)
        btnNext!!.visibility = View.GONE
        if (comFromClass != null) {
            if (comFromClass == "LoginActivity") {
                viewPager!!.currentItem = 2
            }
        }
        btnback!!.setOnClickListener { v: View? ->
            val current = getItem(-1)
            if (current < layouts.size) {
                viewPager!!.currentItem = current
            }
        }
        btnNext!!.setOnClickListener { v: View? ->
            try {
                val current = getItem(+1)
                if (current < layouts.size) {
                    viewPager!!.currentItem = current
                } else {
                    if (selectcity_Id == null) {
                        Toast.makeText(
                            applicationContext,
                            "Please select city",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (customerid == 0) {
                        Toast.makeText(
                            applicationContext,
                            "Please Register your Number",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Utils.setToast(
                            applicationContext,
                            getString(R.string.internet_connection)
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getTokenDataResponce(pass: String, username: String, Password: String) {
        otpViewModel!!.getTokenData(pass, username, Password)
            .observe(this@WelcomeActivity, Observer {
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
            })

    }

    private fun getTokenResponce(tokenResponse: TokenResponse) {
        if (tokenResponse != null) {
            startTimer(tvResendOtpTimer, resendotp)
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.TOKEN, tokenResponse.access_token)
            val current = getItem(+1)
            if (current < layouts.size) {
                // move to next screen
                viewPager!!.currentItem = current
            }
            Utils.leftTransaction(this)
        }
    }

    private fun getDataFromIntent() {
        if (intent != null) {
            comFromClass = intent.getStringExtra("ComeFrom")
        }
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    private fun initialization() {
        viewPager = mBinding!!.viewPagerView
        btnback = mBinding!!.btnBack
        btnNext = mBinding!!.btnNext
        dotsLayout = mBinding!!.layoutDots
        viewPager!!.beginFakeDrag()
        layouts = intArrayOf(
            /*  R.layout.view_welcome_screen,*/
            R.layout.view_otp_genrate,
            R.layout.view_otp_verify
        )

        FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
            if (result != null) {
                fcmToken = result
            }

        }
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
    }

    private fun permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
                    IMEI = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
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

                }

                override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {}
            })
        }
    }

    private fun addBottomDots(currentPage: Int) {
        try {
            val dots = arrayOfNulls<TextView>(layouts.size)
            val colorsActive = resources.getIntArray(R.array.array_dot_active)
            val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
            dotsLayout!!.removeAllViews()
            for (i in dots.indices) {
                dots[i] = TextView(this)
                dots[i]!!.text = Html.fromHtml("&#8226;")
                dots[i]!!.textSize = 35f
                dots[i]!!.setTextColor(colorsInactive[currentPage])
                dotsLayout!!.addView(dots[i])
            }
            if (dots.size > 0) dots[currentPage]!!.setTextColor(colorsActive[currentPage])
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem + i
    }

    private var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            if (position == 0) {
                btnback!!.visibility = View.GONE
            } else if (position == layouts.size - 1) {
                btnNext!!.visibility = View.GONE
            } else if (position == 1 || position == 2 || position == 3) {
                btnNext!!.visibility = View.GONE
                btnback!!.visibility = View.GONE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    public override fun onResume() {
        super.onResume()
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    inner class MyViewPagerAdapter(
        var context: Context,
        var cvp: ExtendedViewPager,
        var cityspinner: Spinner?,
        layouts: IntArray?
    ) : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        var phonenumber: String? = null

        //var backArrow: ImageView? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts[position], container, false)

            if (position == 0) {


                phoneno = view.findViewById(R.id.et_phoneno)
                /* backArrow = view.findViewById(R.id.back_arrow)
                 backArrow!!.setOnClickListener {
                     startActivity(Intent(applicationContext, WelcomeActivity::class.java))
                     finish()
                 }*/
                val submit_number = view.findViewById<ImageView>(R.id.submit_number)
                submit_number.setOnClickListener { v: View? ->
                    if (multipleClick) {
                        multipleClick = false
                        phonenumber = phoneno!!.getText().toString()
                        if (phonenumber == "") {
                            multipleClick = true
                            Utils.setToast(
                                applicationContext,
                                getString(R.string.entermobilenumber)
                            )
                        } else if (!TextUtils.isValidMobileNo(phonenumber)) {
                            multipleClick = true
                            Utils.setToast(applicationContext, getString(R.string.validMobilenumbe))
                        } else if (Utils.isNetworkAvailable(applicationContext)) {

                            deviceID = Settings.Secure.getString(
                                this@WelcomeActivity.contentResolver,
                                Settings.Secure.ANDROID_ID
                            )
                            val otpModel = OTPModel(
                                phonenumber,
                                deviceID,
                                fcmToken,
                                BuildConfig.VERSION_NAME,
                                Build.VERSION.RELEASE,
                                Build.MODEL,
                                IMEI
                            )

                            callOtpData(otpModel)

                        } else {
                            Utils.setToast(
                                applicationContext,
                                getString(R.string.internet_connection)
                            )
                        }
                    } else {
                    }
                }

            }/* else if (position == 0) {
                login = view.findViewById(R.id.login)
                enterbypotp = view.findViewById(R.id.enterbypotp)


                enterbypotp!!.setBackgroundColor(Color.parseColor("#FFFF4500"))
                cvp.setAllowedSwipeDirection(ExtendedViewPager.SwipeDirection.NONE)
                setChecked(enterbypotp)
                setUnChecked(login)
                enterbypotp!!.setOnClickListener {
                    setChecked(enterbypotp)
                    setUnChecked(login)
                    val current = getItem(+1)
                    if (current < layouts.size) {
                        viewPager!!.currentItem = current
                    }
                }
                login!!.setOnClickListener {
                    setChecked(login)
                    setUnChecked(enterbypotp)
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } */ else if (position == 1) {
                otp = view.findViewById(R.id.otp)
                tvResendOtpTimer = view.findViewById(R.id.resend_otp_timer)
                resendotp = view.findViewById(R.id.resendotp)
                tv_oldnumber = view.findViewById(R.id.tv_oldnumber)
                val changenumber = view.findViewById<TextView>(R.id.btn_chngnumber)
                val verify_otp = view.findViewById<ImageView>(R.id.verify_otp)
                timerProgressBar = view.findViewById<ProgressBar>(R.id.progressbar1_timerview)
                rlTimer = view.findViewById<RelativeLayout>(R.id.RLTimer)

                RxBus.getInstance().event.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : DisposableObserver<Any?>() {
                        override fun onNext(o: Any) {
                            if (o is String) {
                                if (o.equals(Mobileno, ignoreCase = true)) {
                                    tv_oldnumber!!.setText(phonenumber)
                                    resendotp!!.setEnabled(false)
                                    resendotp!!.setTextColor(context.resources.getColor(R.color.grey))
                                }
                            }
                        }

                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })
                changenumber.setOnClickListener {
                    multipleClick = true
                    otp!!.setText("")
                    val current = getItem(-1)
                    if (current < layouts.size) {
                        // move to next screen
                        viewPager!!.currentItem = current
                    }
                    cancelTimer()
                }
                verify_otp.setOnClickListener {
                    if (otp!!.getText().toString().equals("", ignoreCase = true)) {
                        Utils.setToast(applicationContext, getString(R.string.enteotp))
                    } else if (OTPNumberString == otp!!.getText().toString()) {
                        SharePrefs.getInstance(applicationContext)
                            .putBoolean(SharePrefs.LOGGED, true)
                        otp!!.setText("")
                        Utils.setToast(applicationContext, getString(R.string.verified))
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    } else {
                        otp!!.setText("")
                        Toast.makeText(
                            context,
                            getString(R.string.enter_correct_otp),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                resendotp!!.setOnClickListener {
                    resendotp!!.visibility = View.GONE
                    rlTimer!!.visibility = View.VISIBLE

                    /* otp!!.setText("")
                     val current = getItem(-1)
                     if (current < layouts.size) {
                         // move to next screen
                         viewPager!!.currentItem = current
                     }*/

                    val otpModel = OTPModel(
                        phonenumber,
                        deviceID,
                        fcmToken,
                        BuildConfig.VERSION_NAME,
                        Build.VERSION.RELEASE,
                        Build.MODEL,
                        IMEI
                    )
                    callOtpData(otpModel)

                    cancelTimer()
                }
            }
            container.addView(view)
            return view
        }

        private fun callOtpData(otpModel: OTPModel) {
            otpViewModel!!.OtpResponse(otpModel).observe(this@WelcomeActivity, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { otpData ->
                                multipleClick = true
                                getOtpData(otpData)
                            }
                        }

                        Status.ERROR -> {
                            multipleClick = true
                            Utils.setToast(applicationContext, it.message)
                        }

                        Status.LOADING -> {

                        }
                    }
                }
            })


        }

        //cancel timer
        fun cancelTimer() {
            if (cTimer != null) cTimer!!.cancel()
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }

        fun setChecked(button: Button?) {
            button!!.background = resources.getDrawable(R.drawable.orangerectangle)
            button.setTextColor(resources.getColor(R.color.white))
        }

        fun setUnChecked(button: Button?) {
            button!!.background = resources.getDrawable(R.drawable.border_green)
            button.setTextColor(resources.getColor(R.color.colorAccent))
        }
    }

    private fun getOtpData(loginResponse: LoginResponse) {
        if (loginResponse.status) {
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
            OTPNumberString = loginResponse.p.otpNumbers
            val mobileNumberSTString = loginResponse.p.mobile
            tv_oldnumber!!.text = mobileNumberSTString
            Log.e("OTP", "OTP$OTPNumberString")
            val regApk = loginResponse.p.getRegisteredApk()
            if (regApk != null) {
                val Password = regApk.password
                val username = regApk.userName
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.network_error),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    getTokenDataResponce("password", username, Password)

                }
            }
        } else {
            Utils.setToast(applicationContext, loginResponse.message)
        }

        //only debug build
        if (BuildConfig.DEBUG) {
            otp!!.setText(OTPNumberString)
        }
    }

    private fun startTimer(tvResendOtpTimer: TextView?, resendotp: TextView?) {
        timerProgressBar.setMax(30000)
        cTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                /* tvResendOtpTimer!!.text =
                     getString(R.string.resendotp) + ":" + "" + millisUntilFinished / 1000*/

                tvResendOtpTimer!!.text = (millisUntilFinished / 1000).toString()
                timerProgressBar.setProgress((millisUntilFinished).toInt())
            }

            override fun onFinish() {
                resendotp!!.isEnabled = true
                resendotp.visibility = View.VISIBLE
                rlTimer.visibility = View.GONE
                resendotp.setBackgroundResource(R.drawable.rectangle)
                resendotp.setPadding(8, 8, 8, 8)

            }
        }
        cTimer!!.start()
    }
}