package com.zila.storelastmile.viewfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zila.storelastmile.data.api.ApiHelper
import com.zila.storelastmile.data.apprepository.AppRepository
import com.zila.storelastmile.ui.views.viewmodels.*

class AddOrderFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddOrderViewModel::class.java)) {
            return AddOrderViewModel(AppRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

