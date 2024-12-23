package com.zila.storelastmile.viewfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zila.storelastmile.data.api.ApiHelper
import com.zila.storelastmile.data.apprepository.AppRepository
import com.zila.storelastmile.ui.views.viewmodels.OrderDetailsViewModel

class OrderDetailsFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderDetailsViewModel::class.java)) {
            return OrderDetailsViewModel(AppRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

