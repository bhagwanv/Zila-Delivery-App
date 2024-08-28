package com.sk.ziladelivery.ui.views.main

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.sk.ziladelivery.BuildConfig
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.AppVersionModel
import com.sk.ziladelivery.data.model.CompanyInfoResponse
import com.sk.ziladelivery.databinding.ActivitySplashScreenBinding
import com.sk.ziladelivery.ui.views.viewmodels.SplashViewModel
import com.sk.ziladelivery.utilities.MyApplication
import com.sk.ziladelivery.utilities.ProgressDialog
import com.sk.ziladelivery.utilities.SharePrefs
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.viewfactory.ViewModelFactory

class SplashScreenActivity : AppCompatActivity() {
    private var splashViewModel: SplashViewModel? = null
    private var mBinding: ActivitySplashScreenBinding? = null
    private var appVersion: String? = null
    private var isCompulsory = false
    private var isPresent = false
    private var sharedPreferences: SharedPreferences? = null
    private var url: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
           // SharePrefs.getInstance(applicationContext).putString(SharePrefs.BASEURL, "https://das.shopkirana.in/")
            SharePrefs.getInstance(applicationContext).putString(SharePrefs.BASEURL, "https://uat.shopkirana.in/")
            //SharePrefs.getInstance(applicationContext).putString(SharePrefs.BASEURL, "https://er15.xyz:4436/")
            loadingData()
        } else {
            checkUrlMethod()
        }
    }

    private fun checkUrlMethod() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(1)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        //set default values
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task: Task<Boolean?> ->
                if (task.isSuccessful) {
                    url = mFirebaseRemoteConfig.getString("BaseUrl")
                    SharePrefs.getInstance(MyApplication.getInstance())
                        .putString(SharePrefs.BASEURL, url)
                    loadingData()
                } else {
                    url = mFirebaseRemoteConfig.getString("BaseUrl")
                    SharePrefs.getInstance(MyApplication.getInstance())
                        .putString(SharePrefs.BASEURL, url)
                    loadingData()
                }
            }.addOnFailureListener { loadingData() }
    }

    private fun loadingData() {
        splashViewModel = ViewModelProvider(
            viewModelStore,
            ViewModelFactory(ApiHelper(RestClient.getInstance().service))
        )[SplashViewModel::class.java]
        if (!Utils.checkInternetConnection(applicationContext)) {
            Utils.setToast(this, resources.getString(R.string.network_error))
        } else {
            getCompanyDetailObserver()
        }

    }

    private fun getCompanyDetailObserver() {
        splashViewModel!!.getCompanyDetails().observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        resource.data?.let { companyDetails ->
                            getCompanyDetails(companyDetails)
                        }
                    }

                    Status.ERROR -> {
                        ProgressDialog.getInstance().dismiss()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }

                    Status.LOADING -> {
                        ProgressDialog.getInstance().show(this)
                    }
                }
            }
        }
    }

    private fun getCompanyDetails(companyDetails: CompanyInfoResponse) {
        if (companyDetails.isStatus) {
            SharePrefs.getInstance(applicationContext).putBoolean(
                SharePrefs.ISRAZORPAYENABLE,
                companyDetails.companyDetails!!.isRazorpayEnable
            )
            SharePrefs.getInstance(applicationContext).putInt(
                SharePrefs.LogDboyLoctionMeter,
                companyDetails.companyDetails.logDboyLoctionMeter
            )
            SharePrefs.getInstance(applicationContext).putString(
                SharePrefs.SecondaryAPIUrl,
                companyDetails.companyDetails.secondaryAPIUrl
            )
            SharePrefs.getInstance(applicationContext).putString(
                SharePrefs.VIDEOUPLOADBASEURL,
                companyDetails.companyDetails.MediaApiUrl
            )
        }

        if (!Utils.checkInternetConnection(applicationContext)) {
            Utils.setToast(this, resources.getString(R.string.network_error))
        } else {
            getVersionObserver()
        }


    }

    private fun getVersionObserver() {
        splashViewModel!!.getVersion().observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        resource.data?.let { versionData ->
                            getVersionDat(versionData)
                        }
                    }

                    Status.ERROR -> {
                        ProgressDialog.getInstance().dismiss()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }

                    Status.LOADING -> {
                        ProgressDialog.getInstance().show(this)
                    }
                }
            }
        }
    }

    private fun getVersionDat(versionList: List<AppVersionModel>) {
        try {
            if (versionList != null && versionList.size != 0) {
                for (i in versionList.indices) {
                    appVersion = versionList[i].app_version
                    isCompulsory = versionList[i].isCompulsory()
                    if (BuildConfig.VERSION_NAME.equals(
                            appVersion,
                            ignoreCase = true
                        ) && isCompulsory
                    ) {
                        isPresent = true
                        mBinding!!.tvAppVersion.text = "App Version: $appVersion"
                        break
                    } else {
                        isPresent = false
                    }
                }
                if (isPresent) {
                    if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.LOGGED)) {
                        launchHomeScreen()
                        checkUserActiveApi()
                        finish()
                    } else {
                        startActivity(
                            Intent(
                                applicationContext,
                                WelcomeActivity::class.java
                            )
                        )
                        Utils.leftTransaction(this)
                        finish()
                    }
                } else {
                    if (isCompulsory) {
                        val builder = AlertDialog.Builder(this@SplashScreenActivity)
                        builder.setTitle(getString(R.string.youAreNotUpdatedTitle))
                        builder.setMessage(
                            getString(R.string.youAreNotUpdatedMessage) + " " + appVersion + getString(
                                R.string.youAreNotUpdatedMessage1
                            )
                        )
                        builder.setCancelable(false)
                        builder.setPositiveButton(R.string.update) { dialog: DialogInterface, id: Int ->
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW, Uri.parse(
                                        BuildConfig.playStoreURL
                                    )
                                )
                            )
                            logout()
                            dialog.cancel()
                        }
                        builder.setNegativeButton("Cancel") { dialog: DialogInterface, i: Int ->
                            dialog.cancel()
                            finish()
                        }
                        builder.show()
                    } else {
                        val builder = AlertDialog.Builder(this@SplashScreenActivity)
                        builder.setTitle(getString(R.string.youAreNotUpdatedTitle))
                        builder.setMessage(
                            getString(R.string.youAreNotUpdatedMessage) + " " + appVersion + getString(
                                R.string.youAreNotUpdatedMessage1
                            )
                        )
                        builder.setCancelable(false)
                        builder.setPositiveButton(R.string.update) { dialog: DialogInterface, id: Int ->
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW, Uri.parse(
                                        BuildConfig.playStoreURL
                                    )
                                )
                            )
                            logout()
                            dialog.cancel()
                        }
                        builder.setNegativeButton("Cancel") { dialog: DialogInterface, i: Int ->
                            dialog.cancel()
                            finish()
                        }
                        builder.show()
                    }
                }
            } else {
                Utils.setToast(applicationContext, getString(R.string.improper_response_server))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkUserActiveApi() {
        splashViewModel!!.getUserActivie(
            SharePrefs.getInstance(this).getString(SharePrefs.MOBILE),
            1
        ).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        resource.data?.let { userActiveReprocess ->
                            getUserIsActive(userActiveReprocess)
                        }
                    }

                    Status.ERROR -> {
                        ProgressDialog.getInstance().dismiss()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }

                    Status.LOADING -> {
                        //ProgressDialog.getInstance().show(this)
                    }
                }
            }
        }

    }

    private fun getUserIsActive(userActiveReprocess: Boolean) {
        if (!userActiveReprocess) {
            startActivity(Intent(applicationContext, WelcomeActivity::class.java))
            Utils.leftTransaction(this)
            finish()
        }

    }

    private fun launchHomeScreen() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        Utils.leftTransaction(this)
        finish()
    }

    private fun logout() {
        sharedPreferences!!.edit().clear().apply()
    }
}