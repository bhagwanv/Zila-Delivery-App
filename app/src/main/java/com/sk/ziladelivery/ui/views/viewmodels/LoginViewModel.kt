package com.sk.ziladelivery.ui.views.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sk.ziladelivery.data.model.LoginModel
import com.sk.ziladelivery.data.apprepository.AppRepository
import com.sk.ziladelivery.utilities.Resource
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private  val appRepository: AppRepository) : ViewModel() {

    fun gethitLoginApi(loginModel: LoginModel?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getLogin(loginModel)))
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

    fun forgetPassword(mobilNumber: String?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getforgetPassword(mobilNumber)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}