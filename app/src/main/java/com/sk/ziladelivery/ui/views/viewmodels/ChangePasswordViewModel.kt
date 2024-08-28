package com.sk.ziladelivery.ui.views.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sk.ziladelivery.data.apprepository.AppRepository
import com.sk.ziladelivery.utilities.Resource
import kotlinx.coroutines.Dispatchers

class ChangePasswordViewModel(private  val appRepository: AppRepository) : ViewModel() {


    fun changePassword(peopleId: Int,newPassword: String?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.chnagePassword(peopleId,newPassword)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}