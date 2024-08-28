package com.sk.ziladelivery.ui.views.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.ChnagePasswordResponseModel
import com.sk.ziladelivery.databinding.ActivityChangePasswordBinding
import com.sk.ziladelivery.ui.views.viewmodels.ChangePasswordViewModel
import com.sk.ziladelivery.utilities.SharePrefs
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.TextUtils
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.utilities.Utils.customDialog
import com.sk.ziladelivery.viewfactory.ChangePasswordFactory

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityChangePasswordBinding
    lateinit var changePasswordViewModel: ChangePasswordViewModel
    lateinit var edtCureentPassword: EditText
    lateinit var confirmPassword: EditText
    var currentPassword = ""
    var newPassword: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)

        initView()
    }

    private fun initView() {
        changePasswordViewModel = ViewModelProvider(
            viewModelStore,
            ChangePasswordFactory(ApiHelper(RestClient.getInstance().service))
        )[ChangePasswordViewModel::class.java]
        mBinding.lifecycleOwner = this


        mBinding.back.setOnClickListener {
            onBackPressed()
        }
        currentPassword = SharePrefs.getInstance(this).getString(SharePrefs.LOGIN_PASSWORD)
        mBinding.edtCurrentPassword.setText(currentPassword)
        val peopleId = SharePrefs.getInstance(this).getInt(SharePrefs.PEOPLE_ID)
        edtCureentPassword = mBinding.edtCurrentPassword
        confirmPassword = mBinding.edtConfirmPassword

        mBinding.btnChangePassword.setOnClickListener {
            if (validation()) {
                newPassword = mBinding.edtNewPassword.text.toString()
                if (!Utils.checkInternetConnection(applicationContext)) {
                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.network_error),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (peopleId != null && peopleId!=0) {
                        changePasswordViewModel.changePassword(peopleId, newPassword)
                            .observe(this@ChangePasswordActivity) {
                                it?.let { resource ->
                                    when (resource.status) {
                                        Status.SUCCESS -> {
                                            resource.data?.let { changePassword ->
                                                getChangePassword(changePassword)
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

                }
            }
        }

    }

    fun validation(): Boolean {
        val newPassWord = mBinding.edtNewPassword.text.toString().trim { it <= ' ' }
        val ReEnterPassword = confirmPassword.text.toString().trim { it <= ' ' }
        val edtPassword = edtCureentPassword.text.toString().trim { it <= ' ' }


        if (edtPassword.isEmpty()) {
            Utils.setToast(this, "Password is required")
            return false
        }
        if (!edtPassword.equals(currentPassword.trim{it<=' '})) {
            Utils.setToast(this, "Current Password is not Match")
            return false
        }
        if ( TextUtils.isNullOrEmpty(newPassWord.trim { it <= ' ' })) {
            Utils.setToast(this, "New Password is required")
            return false
        }
        if (ReEnterPassword.isEmpty()) {
            Utils.setToast(this, "Confirm Password is required")
            return false
        }

        if (!newPassWord.equals(ReEnterPassword.trim{it<=' '})) {
            Utils.setToast(this, "Both Password are not Match")
            return false
        }

        return true

    }

    private fun getChangePassword(changePasswordResponseModel: ChnagePasswordResponseModel) {
        if (changePasswordResponseModel.status!!) {
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.LOGIN_PASSWORD, newPassword)

            Utils.setToast(this@ChangePasswordActivity, changePasswordResponseModel.Message)
            onBackPressed()
            if (customDialog != null) {
                customDialog!!.dismiss()
            }
        } else {
            Utils.setToast(this@ChangePasswordActivity, changePasswordResponseModel.Message)
        }
    }

}