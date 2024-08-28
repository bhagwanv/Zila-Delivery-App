package com.sk.ziladelivery.ui.views.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sk.ziladelivery.data.apprepository.AppRepository
import com.sk.ziladelivery.data.model.RearrangModel
import com.sk.ziladelivery.utilities.Resource
import kotlinx.coroutines.Dispatchers

class RearrangeViewModel(private  val appRepository: AppRepository) : ViewModel() {

    fun getRearrange(tripPlannerConfirmMasterId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getRearangeList(tripPlannerConfirmMasterId)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getSkipRearrange(tripPlannerConfirmMasterId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getRearangeSkip(tripPlannerConfirmMasterId)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun updateRearrange(model: ArrayList<RearrangModel>) = liveData(Dispatchers.Default) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getRearangeUpdateList(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }



}