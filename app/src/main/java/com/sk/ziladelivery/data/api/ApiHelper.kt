package com.sk.ziladelivery.data.api

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
import com.sk.ziladelivery.data.model.RearrangModel
import com.sk.ziladelivery.data.model.SendCloseKmApproval
import com.sk.ziladelivery.data.model.StartAssignmentPostModel
import com.sk.ziladelivery.ui.views.fragment.CreateTripModel
import okhttp3.MultipartBody

class ApiHelper(private val apiService: APIServices) {

    suspend fun getVersion() = apiService.doSplashVersion()
    suspend fun getCompanyDetails() = apiService.companyInfo()
    suspend fun getUserActivie(mobileNo: String, type: Int) =
        apiService.getUserActive(mobileNo, type)

    suspend fun getOtpResponce(otpModel: OTPModel?) = apiService.genotpForDeliveryBoy(otpModel)
    suspend fun gteTokenData(password: String?, username: String?, Password: String?) =
        apiService.getTokenData(password, username, Password)

    suspend fun getLogin(loginModel: LoginModel?) = apiService.doLogin(loginModel)
    suspend fun getForgetpassword(mobilNumber: String?) = apiService.postforgetPassword(mobilNumber)
    suspend fun getDashboard(id: Int, TripPlannerConfirmedMasterId: Long) =
        apiService.getDashBoard(id, TripPlannerConfirmedMasterId)

    suspend fun getAllTripID(id: Int) = apiService.getTripIDAll(id)
    suspend fun getOrder(zilaTripMasterId: Int) = apiService.GetZilaTrip(zilaTripMasterId)
    suspend fun addOrder(zilaTripMasterId: Int,orderId: Int) = apiService.addOrder(zilaTripMasterId,orderId)
    suspend fun createTrip(model: CreateTripModel) = apiService.createTrip(model)
    suspend fun getAcceptPenddingTask(acceptModel: AcceptModel?) =
        apiService.acceptMyPendingTaskNew(acceptModel)

    suspend fun getRejectAssignment(acceptModel: AcceptModel?) =
        apiService.rejectassignment(acceptModel)

    suspend fun getStartTrip(model: StartAssignmentPostModel?) = apiService.postStartTrip(model)
    suspend fun getBreak(model: StartAssignmentPostModel?) = apiService.startBreak(model)
    suspend fun getPostStop(model: StartAssignmentPostModel?) = apiService.stopBreak(model)
    suspend fun getPostEndTrip(model: StartAssignmentPostModel?) = apiService.postEndTrip(model)
    suspend fun getCurrentStatus(id: Int) = apiService.checkStripStatus(id)
    suspend fun getMiloMeterLmit(id: Int) = apiService.milometerLimit(id)
    suspend fun getSendKmApproval(sendCloseKmApproval: SendCloseKmApproval) =
        apiService.sendCloseKmApprovalRequest(sendCloseKmApproval)

    suspend fun getUplodStartTripImage(body: MultipartBody.Part) =
        apiService.startTripMiloImageUpload(body)

    suspend fun getMiloMeterReading(body: MultipartBody.Part) =
        apiService.miloMeterImageUploadNew(body)

    suspend fun getTripHistroy(id: Int) = apiService.moveTripHistory(id)
    suspend fun getQRCode(model: QRCodeResquestModel) = apiService.GenerateOrderAmtQRCode(model)
    suspend fun getQRCode(Tnx: String) = apiService.getTransactionDetail(Tnx)
    suspend fun checkTransactionStatus(orderid: Int) = apiService.getCheckTransactionStatus(orderid)
    suspend fun getRearrange(tripPlannerConfirmMasterId: Int) =
        apiService.GetTripTouchPointToRearrange(tripPlannerConfirmMasterId)

    suspend fun getSkipRearrange(tripPlannerConfirmMasterId: Int) =
        apiService.SkipRearrange(tripPlannerConfirmMasterId)

    suspend fun getRearrangeupdate(model: ArrayList<RearrangModel>) =
        apiService.UpdateTripTouchPointToRearrange(model)

    suspend fun getOrderList(id: Int, returnOrder: Boolean) =
        apiService.getTripOrderForUpdateStatus(id, returnOrder)

    suspend fun getUnloaditemDetails(model: OrderDetailsRequestModel) =
        apiService.getUnlodingItem(model)

    suspend fun getOTPNotifyReDispatchReAttempt(otp: String?, orderId: Int, status: String?) =
        apiService.notifyOTPReDispatchAndReAttempt(otp, orderId, status)

    suspend fun generateOrderOTP(orderId: Int, status: String?, lat: Double, lg: Double, videoUrl: String?) =
        apiService.getOtpForOrder(
            orderId,
            status,
            lat,
            lg,
            videoUrl
        )

    suspend fun notifyRdispachAttamp(model: ReDispatchModel) =
        apiService.notifyReDispatchAndReAttempt(model)

    suspend fun notifyRdispachAttampReturn(model: ReDispatchModel) =
        apiService.notifyReDispatchAndReAttemptReturn(model)

    suspend fun confirmOrderNotify(model: ReDispatchOTPModel) =
        apiService.confirmOtpRedispatchReattempt(model)

    suspend fun cancelOrderNotify(model: CancelOrderModel) = apiService.cancelOrderNotify(model)
    suspend fun cancelOrderSendOtpNotifyUpdate(model: CancelOrderSendOtpModel) =
        apiService.cancelOrderSendOtp(model)

    suspend fun callSkipAllAPI(id: Int) = apiService.getCallSkipAll(id)
    suspend fun removeCurrentSatusAPI(
        TripPlannerConfirmedDetailId: Int,
        TripPlannerConfirmedOrderId: Int
    ) = apiService.BackOrderUpdate(TripPlannerConfirmedDetailId, TripPlannerConfirmedOrderId)

    suspend fun changePassword(peopleId: Int, newPassword: String?) =
        apiService.changePassword(peopleId, newPassword)

    suspend fun GetHolidayOnRedispatch(id: Int, IsReattemt: Boolean) =
        apiService.getHolidayOnRedispatch(id, IsReattemt)

    suspend fun generateOTPofSalesPersonforReattempt(model: GenerateOTPofSalesPersonforReattemptRequestModel) =
        apiService.generateOTPofSalesPersonforReattempt(model)

    suspend fun uploadVideo(body: MultipartBody.Part) = apiService.uploadVideo(body)

    suspend fun notifyDeliveryAction(model: NotifyDeliveryActionRequestModel) = apiService.notifyDeliveryAction(model)
}