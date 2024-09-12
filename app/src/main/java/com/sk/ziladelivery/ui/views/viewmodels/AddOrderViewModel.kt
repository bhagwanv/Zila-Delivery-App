package com.sk.ziladelivery.ui.views.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sk.ziladelivery.data.apprepository.AppRepository
import com.sk.ziladelivery.data.model.ZilaCreateTrip
import com.sk.ziladelivery.ui.views.fragment.CreateTripModel
import com.sk.ziladelivery.utilities.Resource
import kotlinx.coroutines.Dispatchers

class AddOrderViewModel(private  val appRepository: AppRepository) : ViewModel() {

    fun getOrder(zilaTripMasterId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getOrder(zilaTripMasterId)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun addOrder(zilaTripMasterId: Int,orderId:Int,pepopleID:Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.addOrder(zilaTripMasterId,orderId,pepopleID)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun removeOrder(zilaTripMasterId: Int,orderId:Int,pepopleID:Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.removeOrder(zilaTripMasterId,orderId,pepopleID)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun zilaCreateTrip(model: ZilaCreateTrip) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.ZilaCreateTrip(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getInvoice(invoiceNumber: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getInvoice(invoiceNumber)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}


