package com.zila.storelastmile.ui.views.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.zila.storelastmile.data.apprepository.AppRepository
import com.zila.storelastmile.ui.views.fragment.CreateTripModel
import com.zila.storelastmile.utilities.Resource
import kotlinx.coroutines.Dispatchers

class AllTripViewModel(private  val appRepository: AppRepository) : ViewModel() {

    fun getAllTripID(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getTripID(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getTokenData(password: String?, username: String?, Password: String?) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = appRepository.getToken(password, username, Password)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    fun createTrip(model: CreateTripModel) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.createTrip(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}


