package com.sk.ziladelivery.ui.views.main

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import android.content.SharedPreferences
import android.widget.RelativeLayout
import android.widget.LinearLayout
import android.widget.TextView
import io.reactivex.disposables.Disposable
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import androidx.core.view.GravityCompat
import android.content.Intent

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import com.sk.ziladelivery.listener.MainListener
import com.sk.ziladelivery.ui.views.viewmodels.MainModel
import android.preference.PreferenceManager
import android.os.Build
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sk.ziladelivery.databinding.ActivityMainBinding
import com.sk.ziladelivery.ui.views.fragment.*
import com.sk.ziladelivery.utilities.*
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private var mBinding: ActivityMainBinding? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    @JvmField
    var toggle: ActionBarDrawerToggle? = null
    private var sharedPreferences: SharedPreferences? = null
    var titlebar: RelativeLayout? = null
    var orderItemView: LinearLayout? = null
    var orderID: TextView? = null
    var AssignmentID: TextView? = null
    var tvTimmer: TextView? = null
    var ivBack: ImageView? = null
    private var acceptAssignDes: Disposable? = null
    private val handler: Handler? = Handler()
    private var doubleBackToExitPressedOnce = false
    private var dashBoardFragment: DashBoardFragment? = null
    private var tripFragment: TripFragment? = null

    companion object{
        @JvmField
        var tvTimerAsd: TextView? = null
        @JvmField
        var tvStartTime: TextView? = null
        @JvmField
        var startBreak: TextView? = null
        @JvmField
        var stopBreak: TextView? = null
        @JvmField
        var myTripView: TextView? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        tvTimerAsd = mBinding!!.appbarMain.tvTimer
        startBreak = mBinding!!.appbarMain.startBreak
        stopBreak = mBinding!!.appbarMain.stopBreak
        tvStartTime = mBinding!!.appbarMain.startTimer
        myTripView = mBinding!!.appbarMain.toolbarTitle
        myTripView!!.setTextColor(ContextCompat.getColor(this, R.color.Black));
        permission()
        handleAcceptAssignMessage()
        if (handler != null) {
            // Auto Refresh
            // handler.post(timedTask);
        }
        setValue()
        listeners()
        notificationEvent
        createViews()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.HAS_RETURN_ORDER)) {
            mBinding!!.ivReturnCount.visibility = View.VISIBLE
        }
    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val id = menuItem.itemId
        if (id == R.id.my_task) {
            return false
        }
        mDrawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.drawer_menu -> {
                openDrawer()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(mBinding!!.appbarMain.drawerMenu.windowToken, 0)
            }
            R.id.profile -> {
                switchContentWithStack(ProfileFragment())
                closeDrawer()
            }
            R.id.li_return_order -> {
                closeDrawer()
                //startActivity(new Intent(getApplicationContext(), ReturnOrderListActivity.class));
                SharePrefs.getInstance(applicationContext)
                    .putBoolean(SharePrefs.HAS_RETURN_ORDER, false)
                mBinding!!.ivReturnCount.visibility = View.GONE
                Utils.leftTransaction(this)
            }
        }
    }

    private val notificationEvent: Unit
        private get() {
            RxBus.getInstance().event.observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Any?>() {
                    override fun onNext(o: Any) {
                        if (o is String) {
                            if (o.equals("true", ignoreCase = true)) {
                                logout()
                                Utils.setToast(
                                    applicationContext,
                                    "you are logged in on Another Device"
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



    private fun listeners() {
        mBinding!!.mainListener = object : MainListener {
            override fun myTaskClicked() {

                switchContentWithStack(TripFragment())
                closeDrawer()
            }

            override fun myAssignmentClicked() {
                try {
                    if (!Utils.checkInternetConnection(
                            applicationContext
                        )
                    ) {
                        Utils.setToast(
                            applicationContext,
                            resources.getString(R.string.network_error)
                        )
                    } else {
                        switchContentWithStack(MyAssignmentFragment())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //  checkBackStackStatus();
                closeDrawer()
            }

            override fun myAcceptedAssignmentClicked() {
                checkBackStackStatus()
                closeDrawer()
            }

            override fun orderCurrencyClicked() {
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Utils.setToast(applicationContext, resources.getString(R.string.network_error))
                } else {
                    switchContentWithStack(SettleAssignmentFragment())
                }
                closeDrawer()
            }

            override fun historyClicked() {
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Utils.setToast(applicationContext, resources.getString(R.string.network_error))
                } else {
                    switchContentWithStack(HistoryFragment())
                    // checkBackStackStatus();
                }
                closeDrawer()
            }

            override fun performanceClicked() {}
            override fun rateUsClicked() {
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Utils.setToast(applicationContext, resources.getString(R.string.network_error))
                } else {
                    switchContentWithStack(SettleAssignmentFragment())
                }
                closeDrawer()
            }

            override fun settingClicked() {}
            override fun logOutClicked() {
                logout()
                closeDrawer()
            }

            override fun AssignmentBilling() {
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Utils.setToast(applicationContext, resources.getString(R.string.network_error))
                } else {
                    switchContentWithStack(AssignmentBillingFragment())
                }
                closeDrawer()
            }

            override fun ChangeLanguage() {
                startActivity(Intent(applicationContext, ChangeLanguageActivity::class.java))
            }

            override fun RejectAssginmentClicked() {
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Utils.setToast(applicationContext, resources.getString(R.string.network_error))
                } else {
                    switchContentWithStack(RejectAssginmentFragment())
                }
                closeDrawer()
            }

            override fun AssginmentSettle() {
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Utils.setToast(applicationContext, resources.getString(R.string.network_error))
                } else {
                    switchContentWithStack(AssginmentSettleFragment())
                }
                closeDrawer()
            }

            override fun changePassword() {
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Utils.setToast(applicationContext, resources.getString(R.string.network_error))
                } else {
                   startActivity(Intent(this@MainActivity,ChangePasswordActivity::class.java))
                }
                closeDrawer()
            }
        }
    }

    private fun setValue() {
        try {
            val imageProfile =
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.PEAOPLE_IMAGE)
            val name =
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.PEAOPLE_FIRST_NAME)
            val mobile = SharePrefs.getInstance(applicationContext).getString(SharePrefs.MOBILE)
            val mainModel = MainModel(this)
            mainModel.name = name
            mainModel.number = "" + mobile
            if (!TextUtils.isNullOrEmpty(imageProfile)) {
                mainModel.profileImage = imageProfile
            }
            mBinding!!.Home.mainModel = mainModel
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createViews() {
        mDrawerLayout = mBinding!!.container
        navigationView = mBinding!!.navView
        toggle = ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        mDrawerLayout!!.addDrawerListener(toggle!!)
        toggle!!.syncState()
        configureDrawerMenuItem()
        dashBoardFragment = DashBoardFragment()
        tripFragment = TripFragment()
        if (SharePrefs.getInstance(applicationContext).getLong(SharePrefs.ALL_TRIP_SLECTED)>0) {
            setDefaultFragment(dashBoardFragment!!)
        }else{
            setDefaultFragment(tripFragment!!)
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // setView
        viewVisibleTextView(false)
    }

    private fun permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@MainActivity, arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ), 1
                    )
                }
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        1
                    )
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@MainActivity, arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ), 1
                    )
                }
            }
        }
    }

    fun configureDrawerMenuItem() {
        val drawerMenuIV = mBinding!!.appbarMain.drawerMenu
        navigationView!!.setNavigationItemSelectedListener(this)
        drawerMenuIV.setOnClickListener(this)
        mBinding!!.Home.profile.setOnClickListener(this)
        mBinding!!.liReturnOrder.setOnClickListener(this)
    }

    fun setDefaultFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.content, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }

    fun switchContent(fragment: Fragment?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment!!).addToBackStack(null)
            .commit()
    }


      @JvmOverloads
    fun switchContentWithStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }


    fun checkBackStackStatus() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack(TripFragment().javaClass.simpleName, 0)
        }
    }

    fun viewVisibleTextView(llHide: Boolean) {
        titlebar = mBinding!!.appbarMain.titlebar
        tvTimmer = mBinding!!.appbarMain.tvTimmer
        orderItemView = mBinding!!.appbarMain.llOderIdView
        if (llHide) {
            titlebar!!.visibility = View.GONE
            orderItemView!!.visibility = View.VISIBLE
        } else {
            titlebar!!.visibility = View.VISIBLE
            orderItemView!!.visibility = View.GONE
        }
        AssignmentID = mBinding!!.appbarMain.tvAssitId
        orderID = mBinding!!.appbarMain.tvOrderno
        ivBack = mBinding!!.appbarMain.ivBack
    }

    fun closeDrawer() {
        mDrawerLayout!!.closeDrawer(GravityCompat.START)
    }

    fun openDrawer() {
        mDrawerLayout!!.openDrawer(GravityCompat.START)
    }


    override fun onBackPressed() {
        mDrawerLayout = mBinding!!.container
        if (mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout!!.closeDrawer(GravityCompat.START)
        } else if (supportFragmentManager.backStackEntryCount <= 1) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                finishAffinity()
                return
            }
            doubleBackToExitPressedOnce = true
            Snackbar.make(mDrawerLayout!!, "Please click back again to exit", Snackbar.LENGTH_SHORT)
                .show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
        }
    }

    private fun logout() {
        sharedPreferences!!.edit().clear().apply()
        stopService(Intent(applicationContext, YourService::class.java))
        SharePrefs.getInstance(applicationContext).putBoolean(SharePrefs.LOGGED, false)
        SharePrefs.getInstance(applicationContext).putLong(SharePrefs.ALL_TRIP_SLECTED, 0)
        val intent = Intent(applicationContext, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        Utils.rightTransaction(this)
    }

    /**
     * get accept assigned condition
     */
    fun handleAcceptAssignMessage() {
        acceptAssignDes = RxBus.getInstance().event
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o: Any? -> }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (acceptAssignDes != null) {
            acceptAssignDes!!.dispose()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        dashBoardFragment!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}