package com.sk.ziladelivery.ui.views.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sk.ziladelivery.data.apprepository.AppRepository
import com.sk.ziladelivery.data.model.AcceptModel
import com.sk.ziladelivery.data.model.SendCloseKmApproval
import com.sk.ziladelivery.data.model.StartAssignmentPostModel
import com.sk.ziladelivery.utilities.Resource
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody


class DashBoardViewModel(private val appRepository: AppRepository) : ViewModel() {

    fun getDashboardData(id: Int,TripPlannerConfirmedMasterId:Long) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.DashboardData(id,TripPlannerConfirmedMasterId)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getAcceptPendingMyTaskAdi(acceptModel: AcceptModel?) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.acceptPenddingTask(acceptModel)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getRejectAssignment(acceptModel: AcceptModel?) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.RejectAssignment(acceptModel)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun PostStartTripAPI(model: StartAssignmentPostModel?) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.startTrip(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun PostStartBreakAPI(model: StartAssignmentPostModel?) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.postBreak(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun PoststopBreakAPI(model: StartAssignmentPostModel?) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.postStop(model)))
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

    fun PostEndTripAPI(model: StartAssignmentPostModel?) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        try {

            emit(Resource.success(data = appRepository.postEndTrip(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun GetTripCurrentStatusAPI(id: Int) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getCurrentTripStatus(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun PostEnterMilometerLimit(tripPlannerConfirmedMasterId: Int) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = appRepository.postMiloMeterLimit(
                        tripPlannerConfirmedMasterId
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun SendCloseKmApprovalRequest(sendCloseKmApproval: SendCloseKmApproval) =
        liveData(Dispatchers.Main) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = appRepository.sendCloseKmApprovel(sendCloseKmApproval)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }


    fun UploadStartTripMiloImage(body: MultipartBody.Part) =
        liveData(Dispatchers.Main) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = appRepository.uploadStripImage(body)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    fun miloMeterImageUpload(body: MultipartBody.Part) =
        liveData(Dispatchers.Main) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = appRepository.uploadMiloMeterRading(body)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    fun postTripHistory(TripPlannerVehicleId : Int) =
        liveData(Dispatchers.Main) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = appRepository.uploadTripHistory(TripPlannerVehicleId)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }


}