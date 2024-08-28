package com.sk.ziladelivery.viewfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.apprepository.AppRepository
import com.sk.ziladelivery.ui.views.viewmodels.SplashViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(AppRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

