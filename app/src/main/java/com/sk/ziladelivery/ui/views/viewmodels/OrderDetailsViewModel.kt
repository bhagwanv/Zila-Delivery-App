package com.sk.ziladelivery.ui.views.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sk.ziladelivery.data.api.APIServices
import com.sk.ziladelivery.data.apprepository.AppRepository
import com.sk.ziladelivery.data.model.CancelOrderModel
import com.sk.ziladelivery.data.model.CancelOrderSendOtpModel
import com.sk.ziladelivery.data.model.GenerateOTPofSalesPersonforReattemptRequestModel
import com.sk.ziladelivery.data.model.NotifyDeliveryActionRequestModel
import com.sk.ziladelivery.data.model.OrderDetailsRequestModel
import com.sk.ziladelivery.data.model.ReDispatchModel
import com.sk.ziladelivery.data.model.ReDispatchOTPModel
import com.sk.ziladelivery.utilities.Resource
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderDetailsViewModel(private val appRepository: AppRepository) : ViewModel() {

    fun getCustomerWiseOrderList(id: Int, returnOrder: Boolean) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getOrderList(id, returnOrder)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getUnloadItemDetailsApi(model: OrderDetailsRequestModel) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.unloadItemDetails(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getNotifyReDispatchReAttemptApi(model: ReDispatchModel) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.notifyRdispachAttamp(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getNotifyReDispatchReAttemptReturnApi(model: ReDispatchModel) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.notifyRdispachAttampReturn(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getOTPNotifyReDispatchReAttemptAPi(otp: String?, orderId: Int, status: String?) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(
                    Resource.success(
                        data = appRepository.getOTPNotifyReDispatchReAttempt(
                            otp,
                            orderId,
                            status
                        )
                    )
                )
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    fun generateOrderOTPApi(orderId: Int, status: String?, lat: Double, lg: Double,videoUrl: String?) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(
                    Resource.success(
                        data = appRepository.generateOrderOTP(
                            orderId,
                            status,
                            lat,
                            lg,
                            videoUrl
                        )
                    )
                )
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    fun confirmOrderNotifyApi(model: ReDispatchOTPModel) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.confirmOrderNotify(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun cancelOrderNotifyUpdateApi(model: CancelOrderModel) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.cancelOrderNotifyUpdate(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun cancelOrderSendOtpNotifyUpdateApi(model: CancelOrderSendOtpModel) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = appRepository.cancelOrderSendOtpNotifyUpdate(model)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    fun callSkipAllAPI(int: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.callSkipAllAPI(int)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getRemoveCurrentSatusAPI(
        TripPlannerConfirmedDetailId: Int,
        TripPlannerConfirmedOrderId: Int
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = appRepository.removeCurrentSatusAPI(
                        TripPlannerConfirmedDetailId,
                        TripPlannerConfirmedOrderId
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getHolidayOnRedispatch(orderID: Int, IsReattemt: Boolean) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getHolidayOnRedispatch(orderID, IsReattemt)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun generateOTPofSalesPersonforReattempt(model: GenerateOTPofSalesPersonforReattemptRequestModel) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(
                    Resource.success(
                        data = appRepository.generateOTPofSalesPersonforReattempt(
                            model
                        )
                    )
                )
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    fun postVideoBackgroundData(body: MultipartBody.Part, videoBaseUrl: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            //http://192.168.1.50:7011
            //http://media.er15.xyz/
            val retrofit = Retrofit.Builder()
                .baseUrl(videoBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(APIServices::class.java)
            emit(
                Resource.success(
                    data = apiService.uploadVideo(body)
                )
            )

        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
    fun notifyDeliveryAction(model: NotifyDeliveryActionRequestModel) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.notifyDeliveryAction(model)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


}