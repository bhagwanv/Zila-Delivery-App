package com.sk.ziladelivery.ui.views.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sk.ziladelivery.data.apprepository.AppRepository
import com.sk.ziladelivery.data.model.QRCodeResquestModel
import com.sk.ziladelivery.utilities.Resource
import kotlinx.coroutines.Dispatchers

class QRCodeViewModel(private  val appRepository: AppRepository) : ViewModel() {

    fun getQrCode(model: QRCodeResquestModel) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getQR(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun getCheckTransactionStatus(orderID:Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getCheckTransctionSatus(orderID)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun callTransactionDetail(tranNO: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getTransactionDetail(tranNO)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


}