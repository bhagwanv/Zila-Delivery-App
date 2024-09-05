package com.sk.ziladelivery.data.apprepository

import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.model.AcceptModel
import com.sk.ziladelivery.data.model.CancelOrderModel
import com.sk.ziladelivery.data.model.CancelOrderSendOtpModel
import com.sk.ziladelivery.data.model.GenerateOTPofSalesPersonforReattemptRequestModel
import com.sk.ziladelivery.data.model.LoginModel
import com.sk.ziladelivery.data.model.NotifyDeliveryActionRequestModel
import com.sk.ziladelivery.data.model.OTPModel
import com.sk.ziladelivery.data.model.OrderDetailsRequestModel
import com.sk.ziladelivery.data.model.QRCodeResquestModel
import com.sk.ziladelivery.data.model.ReDispatchModel
import com.sk.ziladelivery.data.model.ReDispatchOTPModel
import com.sk.ziladelivery.data.model.SendCloseKmApproval
import com.sk.ziladelivery.data.model.StartAssignmentPostModel
import com.sk.ziladelivery.data.model.ZilaCreateTrip
import com.sk.ziladelivery.ui.views.fragment.CreateTripModel
import okhttp3.MultipartBody

class AppRepository(private val apiHelper: ApiHelper) {
    suspend fun getVersion() = apiHelper.getVersion()
    suspend fun getCompanyDetails() = apiHelper.getCompanyDetails()
    suspend fun getUserActivie(mobileNo: String, type: Int) =
        apiHelper.getUserActivie(mobileNo, type)

    suspend fun getOtpResponce(otpModel: OTPModel?) = apiHelper.getOtpResponce(otpModel)
    suspend fun getToken(password: String?, username: String?, Password: String?) =
        apiHelper.gteTokenData(password, username, Password)

    suspend fun getLogin(loginModel: LoginModel?) = apiHelper.getLogin(loginModel)
    suspend fun getforgetPassword(mobilNumber: String?) = apiHelper.getForgetpassword(mobilNumber)
    suspend fun DashboardData(id: Int, ZilaTripMasterId: Long) =
        apiHelper.getDashboard(id, ZilaTripMasterId)

    suspend fun getTripID(id: Int) = apiHelper.getAllTripID(id)
    suspend fun acceptPenddingTask(acceptModel: AcceptModel?) = apiHelper.getAcceptPenddingTask(acceptModel)

    suspend fun RejectAssignment(acceptModel: AcceptModel?) =
        apiHelper.getRejectAssignment(acceptModel)

    suspend fun startTrip(model: StartAssignmentPostModel?) = apiHelper.getStartTrip(model)
    suspend fun postBreak(model: StartAssignmentPostModel?) = apiHelper.getBreak(model)
    suspend fun postStop(model: StartAssignmentPostModel?) = apiHelper.getPostStop(model)
    suspend fun postEndTrip(model: StartAssignmentPostModel?) = apiHelper.getPostEndTrip(model)
    suspend fun getCurrentTripStatus(id: Int) = apiHelper.getCurrentStatus(id)
    suspend fun postMiloMeterLimit(id: Int) = apiHelper.getMiloMeterLmit(id)
    suspend fun sendCloseKmApprovel(sendCloseKmApproval: SendCloseKmApproval) =
        apiHelper.getSendKmApproval(sendCloseKmApproval)

    suspend fun uploadStripImage(body: MultipartBody.Part) = apiHelper.getUplodStartTripImage(body)
    suspend fun uploadMiloMeterRading(body: MultipartBody.Part) =
        apiHelper.getMiloMeterReading(body)

    suspend fun uploadTripHistory(id: Int) = apiHelper.getTripHistroy(id)

    suspend fun getQR(model: QRCodeResquestModel) = apiHelper.getQRCode(model)
    suspend fun getTransactionDetail(transction: String) = apiHelper.getQRCode(transction)
    suspend fun getCheckTransctionSatus(orderid: Int) = apiHelper.checkTransactionStatus(orderid)

    suspend fun getRearangeSkip(tripPlannerConfirmMasterId: Int) =
        apiHelper.getSkipRearrange(tripPlannerConfirmMasterId)


    //Order Details Page Api
    suspend fun getOrderList(id: Int, returnOrder: Boolean) =
        apiHelper.getOrderList(id, returnOrder)


    suspend fun unloadItemDetails(model: OrderDetailsRequestModel) =
        apiHelper.getUnloaditemDetails(model)

    suspend fun getOTPNotifyReDispatchReAttempt(otp: String?, orderId: Int, status: String?) =
        apiHelper.getOTPNotifyReDispatchReAttempt(otp, orderId, status)

    suspend fun generateOrderOTP(orderId: Int, status: String?, lat: Double, lg: Double, videoUrl: String?) =
        apiHelper.generateOrderOTP(orderId, status, lat, lg, videoUrl)

    suspend fun notifyRdispachAttamp(model: ReDispatchModel) = apiHelper.notifyRdispachAttamp(model)
    suspend fun notifyRdispachAttampReturn(model: ReDispatchModel) = apiHelper.notifyRdispachAttampReturn(model)
    suspend fun confirmOrderNotify(model: ReDispatchOTPModel) = apiHelper.confirmOrderNotify(model)
    suspend fun cancelOrderNotifyUpdate(model: CancelOrderModel) =
        apiHelper.cancelOrderNotify(model)

    suspend fun cancelOrderSendOtpNotifyUpdate(model: CancelOrderSendOtpModel) =
        apiHelper.cancelOrderSendOtpNotifyUpdate(model)

    suspend fun callSkipAllAPI(id: Int) = apiHelper.callSkipAllAPI(id)
    suspend fun removeCurrentSatusAPI(
        TripPlannerConfirmedDetailId: Int,
        TripPlannerConfirmedOrderId: Int
    ) = apiHelper.removeCurrentSatusAPI(TripPlannerConfirmedDetailId, TripPlannerConfirmedOrderId)

    suspend fun chnagePassword(peopleId: Int, newPassword: String?) =
        apiHelper.changePassword(peopleId, newPassword)

    suspend fun getHolidayOnRedispatch(id: Int, IsReattemt: Boolean) =
        apiHelper.GetHolidayOnRedispatch(id, IsReattemt)

    suspend fun generateOTPofSalesPersonforReattempt(model: GenerateOTPofSalesPersonforReattemptRequestModel) =
        apiHelper.generateOTPofSalesPersonforReattempt(model)

    suspend fun uploadVideo(body: MultipartBody.Part) = apiHelper.uploadVideo(body)
    suspend fun notifyDeliveryAction(model: NotifyDeliveryActionRequestModel) =
        apiHelper.notifyDeliveryAction(model)


    /*ZIla*/

    suspend fun getOrder(zilaTripMasterId: Int) = apiHelper.getOrder(zilaTripMasterId)
    suspend fun addOrder(zilaTripMasterId: Int,orderId: Int) = apiHelper.addOrder(zilaTripMasterId,orderId)
    suspend fun removeOrder(zilaTripMasterId: Int,orderId: Int) = apiHelper.removeOrder(zilaTripMasterId,orderId)
    suspend fun createTrip(model: CreateTripModel) = apiHelper.createTrip(model)
    suspend fun ZilaCreateTrip(model: ZilaCreateTrip) = apiHelper.ZilaCreateTrip(model)


}
