package com.zila.storelastmile.ui.views.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.zila.storelastmile.data.apprepository.AppRepository
import com.zila.storelastmile.utilities.Resource
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