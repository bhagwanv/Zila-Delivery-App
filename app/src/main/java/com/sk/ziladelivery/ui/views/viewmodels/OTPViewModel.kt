package com.sk.ziladelivery.ui.views.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sk.ziladelivery.data.model.OTPModel
import com.sk.ziladelivery.data.apprepository.AppRepository
import com.sk.ziladelivery.utilities.Resource
import kotlinx.coroutines.Dispatchers

class OTPViewModel(private  val appRepository: AppRepository) : ViewModel() {

    fun OtpResponse(otpModel: OTPModel?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getOtpResponce(otpModel)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun getTokenData(password: String?, username: String?, Password: String?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getToken(password, username, Password)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }



}