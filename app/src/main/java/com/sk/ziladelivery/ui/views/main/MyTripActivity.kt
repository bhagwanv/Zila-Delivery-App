package com.sk.ziladelivery.ui.views.main

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.MyTripFragmentBinding
import com.sk.ziladelivery.databinding.ReachedPopupBinding
import com.sk.ziladelivery.listener.SearchMyTripInterface
import com.sk.ziladelivery.ui.views.adapter.AssignmentTabAdapter
import com.sk.ziladelivery.ui.views.fragment.MySingleTripMapViewFragment
import com.sk.ziladelivery.ui.views.fragment.MyTripListViewFragment
import com.sk.ziladelivery.utilities.Constant
import com.sk.ziladelivery.utilities.Utils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MyTripActivity : AppCompatActivity() {
    private var mBinding: MyTripFragmentBinding? = null
    private var tabLayout: TabLayout? = null
    private val mHandler = Handler()
    private var reachedDialog: Dialog? = null
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    private var ZilaTripMasterId = 0
    private var TripPlannerConfirmedDetailId = 0
    private var millisUntilFinished: Long = 0
    private var isViewLoaded = false
    private var CustomerTripStatus = 0
    private var indicatorWidth = 0
    private var time: String? = null
    private var ORDER_ID = 0
    private var isUserRagistred = false
    private var mySingleTripMapViewFragment: MySingleTripMapViewFragment? = null
    private var isNotLastMileApp: Boolean = false
    private var IsLocationEnabled: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mBinding == null) {
            mBinding = DataBindingUtil.setContentView(this, R.layout.my_trip_fragment)
        }
        getIntenValue()
        initView()
    }

    private fun getIntenValue() {
        if (intent.extras != null) {
            ZilaTripMasterId = intent.getIntExtra("ZilaTripMasterId", 0)
            TripPlannerConfirmedDetailId = intent.getIntExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, 0)
            CustomerTripStatus = intent.getIntExtra("CustomerTripStatus", 0)
            ORDER_ID = intent.getIntExtra("ORDER_ID", 0)
            time = intent.getStringExtra("time")
            isUserRagistred = intent.getBooleanExtra("isUserRagistred", false)
            isNotLastMileApp = intent.getBooleanExtra("isNotLstMileApp", false)
            IsLocationEnabled = intent.getBooleanExtra("IsLocationEnabled", false)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@MyTripActivity, MainActivity::class.java))
        finish()
    }

    public override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this@MyTripActivity).unregisterReceiver(mMessageReceiver)
        if (reachedDialog != null) {
            if (reachedDialog!!.isShowing) {
                reachedDialog!!.dismiss()
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, IntentFilter("LocationFound"))
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mySingleTripMapViewFragment!!.onActivityResult(requestCode, resultCode, data)
    }

    private fun initView() {
        reachedDialog = Dialog(this)
        if (isUserRagistred) {
            mBinding!!.llContener.visibility = View.VISIBLE
            mBinding!!.llPager.visibility = View.GONE
            mBinding!!.etSearchCust.visibility = View.VISIBLE
            setDefaultFragment(MyTripListViewFragment())
        } else {
            mBinding!!.llContener.visibility = View.GONE
            mBinding!!.llPager.visibility = View.VISIBLE
            if (!isViewLoaded) {
                setTabLayout()
                isViewLoaded = true
            }
        }
        mBinding!!.etSearchCust.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length > 0) {
                    searchMyTripInterface!!.onSearch(s.toString())
                } else {
                    searchMyTripInterface!!.onSearch("")
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        mBinding!!.ivBack.setOnClickListener { onBackPressed() }
        timer(time)
    }


    private fun setTabLayout() {
        tabLayout = mBinding!!.tabs
        val viewPager = mBinding!!.viewPager
        tabLayout!!.setupWithViewPager(viewPager)
        setupViewPager(viewPager)
        // Set default tab (assuming ListView tab is at position 1)
       /* val defaultTabPosition = 1 // Set this to the position of your ListView tab
        viewPager.currentItem = defaultTabPosition
        tabLayout!!.selectTab(tabLayout!!.getTabAt(defaultTabPosition))*/


        tabLayout!!.post {
            indicatorWidth = tabLayout!!.width / tabLayout!!.tabCount
            //Assign new width
            val indicatorParams = mBinding!!.indicator.layoutParams as FrameLayout.LayoutParams
            indicatorParams.width = indicatorWidth
            mBinding!!.indicator.layoutParams = indicatorParams
            if (CustomerTripStatus == 10) {
                tabLayout!!.selectTab(tabLayout!!.getTabAt(1))
            }
        }
        mBinding!!.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(i: Int, positionOffset: Float, positionOffsetPx: Int) {
                val params = mBinding!!.indicator.layoutParams as FrameLayout.LayoutParams
                val translationOffset = (positionOffset + i) * indicatorWidth
                params.leftMargin = translationOffset.toInt()
                mBinding!!.indicator.layoutParams = params
            }

            override fun onPageSelected(i: Int) {}
            override fun onPageScrollStateChanged(i: Int) {}
        })
    }
  /* private fun setTabLayout() {
       tabLayout = mBinding!!.tabs
       val viewPager = mBinding!!.viewPager
       tabLayout!!.setupWithViewPager(viewPager)
       setupViewPager(viewPager)
       // Set the default tab to the ListView tab (assuming it's at position 1)
       val defaultTabPosition = 1 // Position of the ListView tab
       viewPager.currentItem = defaultTabPosition
       tabLayout!!.selectTab(tabLayout!!.getTabAt(defaultTabPosition))

       // Adjust the indicator width based on the number of tabs
       tabLayout!!.post {
           indicatorWidth = tabLayout!!.width / tabLayout!!.tabCount

           // Set the initial width and position of the indicator
           val indicatorParams = mBinding!!.indicator.layoutParams as FrameLayout.LayoutParams
           indicatorParams.width = indicatorWidth

           // Set the left margin of the indicator to match the selected tab (position 1)
           indicatorParams.leftMargin = indicatorWidth * defaultTabPosition
           mBinding!!.indicator.layoutParams = indicatorParams

           // If CustomerTripStatus is 10, select the ListView tab (optional check)
           if (CustomerTripStatus == 10) {
               tabLayout!!.selectTab(tabLayout!!.getTabAt(1))
           }
       }

       // Handle the custom indicator translation during tab swipes
       mBinding!!.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
           override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPx: Int) {
               // Calculate the translation offset for the indicator
               val params = mBinding!!.indicator.layoutParams as FrameLayout.LayoutParams
               val translationOffset = (positionOffset + position) * indicatorWidth
               params.leftMargin = translationOffset.toInt()
               mBinding!!.indicator.layoutParams = params
           }

           override fun onPageSelected(position: Int) {
               // Handle actions when a new page is selected (if needed)
           }

           override fun onPageScrollStateChanged(state: Int) {
               // Handle page scroll state changes (if needed)
           }
       })
   }*/



    private fun callDestReachPopup() {
        val reachedPopupBinding = DataBindingUtil.inflate<ReachedPopupBinding>(
            layoutInflater, R.layout.reached_popup, null, false
        )
        reachedDialog = Dialog(this, R.style.CustomDialog)
        reachedDialog!!.setContentView(reachedPopupBinding.root)
        reachedDialog!!.setCancelable(true)
        reachedDialog!!.show()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = AssignmentTabAdapter(supportFragmentManager, mFragmentList, mFragmentTitleList)
        mySingleTripMapViewFragment = MySingleTripMapViewFragment()

        //addFragment(mySingleTripMapViewFragment!!, "MapView")
        addFragment(MyTripListViewFragment(), "ListView")

       /* if (!isNotLastMileApp && !IsLocationEnabled) {
            addFragment(mySingleTripMapViewFragment!!, "MapView")
            addFragment(MyTripListViewFragment(), "ListView")
        } else {
            addFragment(MyTripListViewFragment(), "ListView")
        }*/

        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 1) {
                    mBinding!!.etSearchCust.visibility = View.VISIBLE
                } else {
                    mBinding!!.etSearchCust.visibility = View.GONE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun addFragment(fragment: Fragment, title: String) {
        val bundle = Bundle()
        bundle.putInt("ZilaTripMasterId", ZilaTripMasterId)
        bundle.putInt(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, TripPlannerConfirmedDetailId)
        bundle.putInt("CustomerTripStatus", CustomerTripStatus)
        bundle.putInt("ORDER_ID", ORDER_ID)
        bundle.putString("time", time)
        bundle.putBoolean("isNotLastMileApp", isNotLastMileApp)
        fragment.arguments = bundle
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun setDefaultFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putInt("ZilaTripMasterId", ZilaTripMasterId)
        bundle.putInt(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, TripPlannerConfirmedDetailId)
        bundle.putInt("CustomerTripStatus", CustomerTripStatus)
        bundle.putInt("ORDER_ID", ORDER_ID)
        bundle.putString("time", time)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.content, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }

    // to start the handler
    fun start() {
        mHandler.postDelayed(my_runnable, 1000)
    }

    // to stop the handler
    fun stop() {
        mHandler.removeCallbacks(my_runnable)
        mBinding!!.tvTimmer.text = "00:00:00"
        //  activity.tvTimerAsd.setVisibility(View.GONE);
    }

    // to reset the handler
    private fun restart() {
        mHandler.removeCallbacks(my_runnable)
        mHandler.postDelayed(my_runnable, 1000)
    }

    fun timer(time: String?) {
        try {
            val currMillis = System.currentTimeMillis()
            val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
            sdf1.timeZone = TimeZone.getDefault()
            if (time != null) {
                val startTime = sdf1.parse(time)
                val startEpoch = startTime.time
                millisUntilFinished = currMillis - startEpoch
            }
            start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent != null) {
                if (reachedDialog != null) {
                    if (reachedDialog!!.isShowing) {
                        reachedDialog!!.dismiss()
                    }
                }
            }
            callDestReachPopup()
        }
    }
    private val my_runnable = Runnable { // your code here
        val mills = Math.abs(millisUntilFinished)
        val FORMAT = "%02d:%02d:%02d"
        val hms = String.format(
            FORMAT,
            TimeUnit.MILLISECONDS.toHours(mills),
            TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(mills)
            ),
            TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(mills)
            )
        )
        mBinding!!.tvTimmer.text = "" + hms
        millisUntilFinished += 1000
        restart()
    }

    companion object {
        private var searchMyTripInterface: SearchMyTripInterface? = null

        @JvmStatic
        fun setInterface(getMatchingJobsArray: SearchMyTripInterface?) {
            searchMyTripInterface = getMatchingJobsArray
        }
    }
}