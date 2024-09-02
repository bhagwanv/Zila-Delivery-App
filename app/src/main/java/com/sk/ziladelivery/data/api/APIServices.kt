package com.sk.ziladelivery.data.api

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.sk.ziladelivery.data.localdatabase.UserLatLngModel
import com.sk.ziladelivery.data.model.*
import com.sk.ziladelivery.ui.views.fragment.CreateTripModel
import com.sk.ziladelivery.ui.views.fragment.unloadReturnItem.GenerateOTPForReturnOrder
import com.sk.ziladelivery.ui.views.fragment.unloadReturnItem.PickedReturnOrderByDBoyRequestModel
import com.sk.ziladelivery.ui.views.fragment.unloadReturnItem.ReturnOrderCreditNoteRequestModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*


/**
 * Created by User on 03-11-2018.
 */
interface APIServices {
    @POST("/api/DBSignup/NewDeliveryV2")
    suspend fun doLogin(@Body loginModel: LoginModel?): LoginResponse

    @GET("/api/appVersion/NewDeliveryApp")
    suspend fun doSplashVersion(): List<AppVersionModel>

    @GET("/api/RetailerApp/GetCompanyDetailsForRetailer")
    suspend fun companyInfo(): CompanyInfoResponse

    @GET("/api/DeliveryIssuance/DeliveryAppProfile")
    suspend fun getUserActive(@Query("Mobile") Mobile: String, @Query("AppType") AppType: Int): Boolean

    @POST("/api/DBSignup/GenotpForDeliveryBoyApp")
    suspend fun genotpForDeliveryBoy(@Body otpModel: OTPModel?): LoginResponse

    @FormUrlEncoded
    @POST("/token")
    fun getToken(
        @Field("grant_type") grant_type: String?,
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Observable<JsonObject?>


    @FormUrlEncoded
    @POST("/token")
    fun getCommonToken(
        @Field("grant_type") grant_type: String?,
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Observable<TokenResponse?>?

    @FormUrlEncoded
    @POST("/token")
    suspend fun getTokenData(
        @Field("grant_type") grant_type: String?,
        @Field("username") username: String?,
        @Field("password") password: String?
    ): TokenResponse

    @GET("/api/Customers/Forgrt/V2")
    suspend fun postforgetPassword(@Query("Mobile") Mobile: String?): ForgetpassresponseModel

    @GET("api/DeliveryApp/DeliveryDashboard")
    suspend fun getDashBoard(
        @Query("PeopleId") PeopleId: Int,
        @Query("TripPlannerConfirmedMasterId") TripPlannerConfirmedMasterId: Long
    ): DashBoardResponseModel

    @GET("api/ZilaDeliveryApp/GetAllTrip")
    suspend fun getTripIDAll(@Query("DboyId") DboyId: Int): JsonArray

    @POST("api/ZilaDeliveryApp/CreateCustomTripV1")
    suspend fun createTrip(@Body model: CreateTripModel?): JsonObject

    @GET("api/ZilaDeliveryApp/GetZilaTrip")
    suspend fun GetZilaTrip(@Query("zilaTripMasterId") zilaTripMasterId: Int): GetZilaTripResponse

    @POST("api/ZilaDeliveryApp/AddOrder")
    suspend fun addOrder(@Query("zilaTripMasterId") zilaTripMasterId: Int,@Query("orderId") orderId: Int): AddOrderResponse


    @PUT("/api/DeliveryIssuance/AssignmentAcceptNew")
    suspend fun acceptMyPendingTaskNew(@Body acceptModel: AcceptModel?): AssignmentAcceptModel

    @PUT("/api/DeliveryIssuance/AssignmentAcceptNew")
    fun acceptMyPendingTask(@Body acceptModel: AcceptModel?): Observable<JsonObject>

    @POST("api/DeliveryApp/RejectAssignmentUpdateTrip")
    suspend fun rejectassignment(@Body acceptModel: AcceptModel?): JsonObject

    @POST("api/DeliveryApp/StartAssignment")
    suspend fun postStartTrip(@Body model: StartAssignmentPostModel?): JsonObject

    @POST("api/DeliveryApp/TripOnBreak")
    suspend fun startBreak(@Body model: StartAssignmentPostModel?): JsonObject

    @POST("api/DeliveryApp/TripOnBreakstart")
    suspend fun stopBreak(@Body model: StartAssignmentPostModel?): JsonObject

    @POST("api/DeliveryApp/EndAssignment")
    suspend fun postEndTrip(@Body model: StartAssignmentPostModel?): JsonObject

    @GET("api/DeliveryApp/CheckTripOrderCurrentStatus")
    suspend fun checkStripStatus(@Query("TripPlannerConfirmedMasterId") TripPlannerConfirmedMasterId: Int): JsonObject

    /*Remove when its not use*/
    @GET("api/DeliveryApp/CheckTripOrderCurrentStatus")
    fun getCheckStripStatus(@Query("TripPlannerConfirmedMasterId") TripPlannerConfirmedMasterId: Int): Observable<JsonElement?>?

    @GET("api/DeliveryApp/EnterMilometerLimit")
    suspend fun milometerLimit(@Query("tripPlannerConfirmedMasterId") TripPlannerConfirmOrderId: Int): JsonObject

    @POST("api/DeliveryApp/SendCloseKmApprovalRequest")
    suspend fun sendCloseKmApprovalRequest(@Body sendCloseKmApproval: SendCloseKmApproval?): Boolean

    @Multipart
    @POST("api/DeliveryApp/MilometerDocumentImageUpload")
    suspend fun startTripMiloImageUpload(@Part image: MultipartBody.Part): String

    @Multipart
    @POST("api/DeliveryApp/MilometerDocumentImageUpload")
    suspend fun miloMeterImageUploadNew(@Part image: MultipartBody.Part): String

    @Multipart
    @POST("api/DeliveryApp/MilometerDocumentImageUpload")
    fun miloMeterImageUpload(@Part image: MultipartBody.Part): Observable<String>

    @GET("/api/VehicleHistory/MoveHistory/{tripPlannerVehicleId}")
    suspend fun moveTripHistory(@Path("tripPlannerVehicleId") tripPlannerVehicleId: Int): JsonObject

    @GET("UPI/TransactionDetail")
    suspend fun getTransactionDetail(@Query("UPITxnID") CustomerId: String): JsonObject

    @POST("UPI/GenerateOrderAmtQRCode")
    suspend fun GenerateOrderAmtQRCode(@Body model: QRCodeResquestModel): JsonObject

    @GET("UPI/CheckTransactionStatus")
    suspend fun getCheckTransactionStatus(@Query("OrderId") CustomerId: Int): JsonObject

    @GET("/api/DeliveryTask/AssignmentOrder/V1")
    fun getMyTaskData(
        @Query("DeliveryIssuanceId") DeliveryIssuanceId: Int,
        @Query("mob") mobile: String?,
        @Query("page") page: Int,
        @Query("list") list: Int,
        @Query("OrderId") OrderId: Int
    ): Observable<JsonElement?>?

    @GET("/api/DeliveryTask/OrderDetail/V2")
    fun getOrderDetails(@Query("OrderId") OrderId: Int): Observable<JsonElement?>?


    @GET("api/DeliveryIssuance/PendingAssignment/V1")
    fun getPepoleMyTaskData(@Query("id") mobile: String?): Observable<JsonElement?>?

    @POST("/api/DeliveryTask/PostOrder/V2")
    fun postOrderPostData(@Body orderPlacedModel: OrderPlacedModel?): Observable<JsonElement?>?


    @GET("/api/DeliveryTask/V1")
    fun getPepoleMyTaskData(
        @Query("mob") mob: String?,
        @Query("start") start: String?,
        @Query("end") end: String?,
        @Query("dboyId") dboyId: String?
    ): Observable<JsonObject?>?

    @POST("/api/MobileDelivery/DBoyCashCollection")
    fun postCurrencyData(@Body currencyPostDataModel: CurrencyPostDataModel?): Observable<JsonElement?>?

    @POST("/api/DBSignup/DeliveryEclipseTime")
    fun postCurentTime(@Body CurrentEndTimeModel: CurrentEndTimeModel?): Observable<JsonElement?>?


    @POST("/api/DBSignup/DeliveryEclipseTime")
    fun getCurentTime(@Body CurrentEndTimeModel: TimerDetailsModel?): Observable<JsonElement?>?

    @GET("api/DeliveryIssuance/getAssignid")
    fun searchAssignmentIDResponse(
        @Query("AssignmentId") AssignmentId: String?,
        @Query("DBoyId") DBoyId: Int
    ): Observable<JsonElement?>?


    @GET("api/DeliveryIssuance/getorderid")
    fun searchOrderIDResponse(
        @Query("orderId") orderId: String?,
        @Query("DBoyId") DBoyId: Int
    ): Observable<JsonElement?>?

    @GET("/api/DeliveryIssuance/SubmittedAssignment/V1")
    fun DeliveryIssuance(@Query("id") id: String?): Observable<JsonElement?>?

    @GET("api/DeliveryTask/PaymentAssignment/V1")
    fun AssignmentID(@Query("DeliveryIssuanceId") id: String?): Observable<JsonElement?>?

    @PUT("api/DeliveryTask/PaymentSubmittedAssignment/V1")
    fun sendAssignmentData(
        @Query("id") Dboyid: String?,
        @Query("DeliveryIssuanceId") id: Int,
        @Query("FileName") FileName: String?
    ): Observable<JsonElement?>?

    @PUT("api/DeliveryOrderAssignmentChange/Rejected")
    fun rejectAssignment(@Body deliveryIssuanceM: DeliveryIssuanceM?): Observable<JsonElement?>?


    @POST("api/TestCashCollection/GenerateOTPForCurrency")
    fun getGanrateCode(@Body model: GanrateCodeModel): Observable<JsonElement?>?


    @GET("api/CancellationReport/GetDboy")
    fun getAssignmentCancelationData(
        @Query("WarehouseId") WarehouseId: Int,
        @Query("MobileNo") MobileNo: String?
    ): Observable<JsonElement?>?

    @POST("api/SalesAppCounter")
    fun postlatlong(@Body model: LatLongModel?): Observable<JsonObject?>?

    @GET("/api/MobileDelivery/PaymentSubmittedAssignment/V1?")
    fun getAssignmentIDDeatil(
        @Query("peopleId") peopleId: Int,
        @Query("warehouseid") warehouseid: Int
    ): Observable<JsonObject?>?

    @get:GET("/api/MobileDelivery/CurrencyDenomination")
    val currency: Observable<JsonObject?>?

    @GET("/api/MobileDelivery/GetLastTwoDayCurrencyCollection")
    fun getHistory(
        @Query("peopleId") peopleId: Int,
        @Query("warehouseid") warehouseid: Int
    ): Observable<JsonObject?>?

    @GET("/api/MobileDelivery/GetCurrencyCollectionById")
    fun getHistoryByid(@Query("currencyCollectionId") currencyCollectionId: Int): Observable<JsonObject?>?

    @GET("/api/DeliveryIssuance/AcceptedInprogress/V1")
    fun getAcceptedAssignment(@Query("id") id: Int): Observable<JsonObject?>?

    @get:GET("/api/MobileDelivery/GetBankName")
    val bankName: Observable<JsonElement?>?


    @POST("/api/LatLng/SortOrdersInAssignment")
    fun getShortPath(@Body sortOrderModel: SortOrderModel?): Observable<JsonElement?>?


    @GET("/api/Customers/CheckCustomerCriticalInfo")
    fun getcustInfo(@Query("customerId") customerId: Int): Observable<String?>?



    @GET("/api/DeliveryTask/OTPExistsForOrder")
    fun checkOrderOtpExist(
        @Query("OrderId") orderId: Int,
        @Query("Status") status: String?,
        @Query("comments") comments: String?
    ): Observable<JsonElement?>?

    @GET("api/DeliveryApp/MytripOrderList")
    fun getMytripOrder(@Query("TripPlannerConfirmedMasterId") TripPlannerConfirmedMasterId: Int): Observable<JsonElement?>?

    @POST("api/DeliveryApp/OrderUnloding")
    fun getUnloadingAPI(@Body model: PostUnloadingModel?): Observable<JsonElement?>?


    @GET("/api/DeliveryTask/GenerateOTPForOrder")
    fun getOtpForOrderTwo(
        @Query("OrderId") orderId: Int,
        @Query("Status") status: String?,
        @Query("lat") lat: Double,
        @Query("lg") lg: Double
    ): Observable<JsonElement?>?

    @GET("/api/DeliveryTask/ValidateOTPForOrder")
    fun validateOrderOtp(
        @Query("OrderId") orderId: Int,
        @Query("Status") status: String?,
        @Query("otp") otp: String?
    ): Observable<JsonElement?>?

    @GET("/api/Customers/IsChequeAccepted")
    fun getChequepermission(@Query("CustomerId") CustomerId: Int): Observable<JsonElement?>?

    @GET("/api/KKReturnReplace/GetReturnReplaceOrderForDBoy")
    fun getReturnOrderList(@Query("DboyId") dBoyId: Int): Observable<JsonElement?>?

    @GET("/api/KKReturnReplace/GetWarehouseRejectOrderForDBoy")
    fun getWarehouseRejectOrderList(@Query("DboyId") dBoyId: Int): Observable<JsonElement?>?

    @GET("/api/DeliveryTask/GetDeclineAssignmentOrder")
    fun GetDeclineAssignment(
        @Query("DeliveryIssuanceId") DeliveryIssuanceId: Int,
        @Query("mob") Mobile: String?
    ): Observable<JsonElement?>?

    @GET("api/DeliveryTask/GetRejectedAssignment")
    fun GetRejectedAssignment(@Query("id") id: Int): Observable<JsonObject?>?
    @GET("api/KKReturnReplaceHistory/GetReturnReplaceItemList")
    fun getReturnReplaceItemList(@Query("KKRequestId") KKRequestId: Int): Observable<JsonElement?>?

    @GET("/api/DBoyAssignmentDeposit/GetDBoySattlementAssignment")
    fun GetAssginmentToSettle(@Query("BoyId") BoyId: Int): Observable<JsonElement?>?

    @GET("/api/DBoyAssignmentDeposit/getSignedDepositSlip")
    fun GetAssginmentHistoryToSettle(
        @Query("SlipId") SlipId: Int,
        @Query("AssignmentId") AssignmentId: Int,
        @Query("DboyId") DboyId: Int,
        @Query("StartDate") StartDate: String?,
        @Query("EndDate") EndDate: String?,
        @Query("PageNumber") PageNumber: Int,
        @Query("PageSize") PageSize: Int
    ): Observable<JsonElement?>?

    @POST("/api/DBoyAssignmentDeposit/UpdateAssignment")
    fun PostAssginmentToSettle(@Body model: AssginmentSettleResponseModel?): Observable<JsonObject?>?

    @Multipart
    @POST("/api/MobileDelivery/CurrencyUploadChequeImageForMobile")
    fun UploadCheque(@Part image: MultipartBody.Part): Observable<String?>?

    @Multipart
    @POST("/api/AssignmentCopyUpload")
    fun AssignmentCopyUpload(@Part image: MultipartBody.Part): Observable<JsonObject?>?


    @Multipart
    @POST("/api/DBoyAssignmentDeposit/UploadDboySignature")
    fun UploadDboySignature(@Part image: MultipartBody.Part): Observable<String?>?

    @Multipart
    @POST("/api/itemimageupload/UploadKKReturnReplaceImages")
    fun uploadKKReturnImage(@Part image: MultipartBody.Part): Observable<String?>?

    @GET("api/KKReturnReplace/ChangeStatus")
    fun updateReturnRequestStatus(
        @Query("KKReturnReplaceId") KKRequestId: Int,
        @Query("Status") status: String?,
        @Query("dboyId") dBoyId: Int,
        @Query("picker_comment") pickerComment: String?,
        @Query("ReturnReplaceImage") returnReplaceImage: String?
    ): Observable<JsonElement?>?

    @GET("/api/DeliveryTask/GenerateRazorpayQrCode")
    fun getQRCodeUPI(
        @Query("orderId") orderId: Int,
        @Query("customerId") customerId: Int,
        @Query("cashAmount") cashAmount: Int
    ): Observable<String?>?

    @GET("api/Razorpay/VerifyQRPayment")
    fun verifyQRCodeUPI(
        @Query("orderId") orderId: Int,
        @Query("cashAmount") cashAmount: Int
    ): Observable<JsonElement?>?

    @GET("/api/DeliveryTask/AssignmentDirection")
    fun getRouteMap(@Query("assignmentId") assignmentId: Int): Observable<JsonElement?>?

    @get:GET("/api/DeliveryTask/GetAcceptAssignmentDistance")
    val acceptAssignmentDistance: Observable<JsonElement?>?

    @GET("api/DeliveryApp/MapViewOrderlist")
    fun getMapViewOrderList(@Query("TripPlannerConfirmedMasterId") masterId: Int): Observable<JsonElement?>?

    @GET("api/DeliveryApp/SingleOrderMapviewInfo")
    fun getSingleMapViewOrderList(
        @Query("TripPlannerConfirmedMasterId") masterId: Int,
        @Query("lat") lat: Double,
        @Query("lng") lag: Double
    ): Observable<JsonElement?>?

    @GET("api/DeliveryApp/GetCustomerWiseOrderList")
    fun getTripOrderForUpdateStatusTwo(@Query("TripPlannerConfirmedDetailId") masterId: Int): Observable<JsonElement?>?

    @GET("/api/DeliveryApp/TripVerifyAssignmentRefNo")
    fun checkRefNo(
        @Query("DeliveryIssuanceId") DeliveryIssuanceId: Int,
        @Query("RefNo") RefNo: String?,
        @Query("custId") custId: Int
    ): Observable<Boolean?>?

    @POST("api/VehicleHistory/InsertFirestoreData")
    fun postBackgroundData(@Body model: BackgroundServiceModel?): Observable<JsonElement?>?



    @GET("api/DeliveryApp/GetUnloadItemListPage")
    fun GetUnloadItemListPage(@Query("TripPlannerConfirmedDetailId") masterId: Int): Observable<JsonElement?>?

    @POST("api/DeliveryApp/CheckAndUnCheckedUnloadingItem")
    fun GetCheckUnloadItemListPage(@Body model: SelectAllResponceModel): Observable<JsonElement?>?

    @GET("api/TripCustVoiceRecord/GetTripCustVoiceRecordListByTripIdCustId")
    fun TripCustVoiceRecord(
        @Query("TripId") TripId: Int,
        @Query("CustomerId") CustomerId: Int
    ): Observable<JsonElement?>?

    @GET("api/DeliveryApp/CollectPaymentOrderStatusChange")
    fun getCollectPayment(@Query("TripPlannerConfirmedDetailId") TripPlannerConfirmedDetailId: Int): Observable<JsonElement?>?

    @GET("api/DeliveryApp/GetCollectPaymentOrderList")
    fun GetCollectPaymentOrderList(@Query("TripPlannerConfirmedDetailId") TripPlannerConfirmedMasterId: Int): Observable<JsonElement?>?


    @POST("api/DeliveryApp/SubmitPayment")
    fun submitPaymentApi(@Body paymentRequestModel: PaymentRequestModel?): Observable<JsonElement?>?





    @POST("api/DeliveryApp/DeliveredGenerateOtp")
    fun getDeliveredGenerateOtp(
        @Query("TripplannerConfirmdetailedId") id: Int,
        @Query("Status") status: String?,
        @Query("lat") lat: Double,
        @Query("lg") lag: Double
    ): Observable<JsonElement?>?


    @POST("api/DeliveryApp/DeliverdConfirmOtpNew")
    fun getDeliverdConfirmOtp(@Body model: OrderConfirmModel?): Observable<JsonElement?>?



    @GET("api/DeliveryApp/TripGetDboyRatingOrder")
    fun getRattingData(@Query("id") id: Int): Observable<CustomerRattingModel?>?

    @POST("/api/RetailerApp/AddRating")
    fun insertRating(@Body model: RatingModel?): Observable<Boolean?>?

    @POST("api/DeliveryApp/SkipAll")
    fun getCallSkipAllTwo(@Query("TripplannerConfirmdetailedId") TripplannerConfirmdetailedId: Int): Observable<JsonElement?>?

    @POST("api/DeliveryApp/RemoveOrder")
    fun getRemoveOrder(
        @Query("TripPlannerConfirmOrderId") TripPlannerConfirmOrderId: Int,
        @Query("IsPaymentDone") IsPaymentDone: Boolean
    ): Observable<JsonElement?>?

    @GET("api/DeliveryApp/CheckRemaingOrderStatus")
    fun getCheckRemaingOrderStatus(@Query("TripplannerConfirmdetailedId") TripPlannerConfirmOrderId: Int): Observable<JsonElement?>?

    @POST("api/DeliveryApp/NotifyALLReDispatchAndReAttempt")
    fun notifyALLReDispatchAndReAttempt(@Body model: ReDispatchModel?): Observable<JsonElement?>?

    //api/DeliveryApp/AllRedispatReattemptGenerateOtp
    @POST("api/DeliveryApp/AllRedispatReattemptGenerateOtp")
    fun notifyReDispatchAndReAttemptAll(@Body model: ReDispatchModel?): Observable<JsonElement?>?

    //
    @POST("api/DeliveryApp/AllRedispatReattemptConfirmOtp")
    fun confirmOtpRedispatchReattemptAll(@Body model: ReDispatchOTPModelforMyTrip?): Observable<JsonElement?>?

    @GET("api/DeliveryApp/CompleteTripStatusChange")
    fun getCompleteTripStatusChange(
        @Query("TripplannerConfirmdetailedId") TripPlannerConfirmOrderId: Int,
        @Query("lat") lat: Double,
        @Query("lng") lag: Double
    ): Observable<JsonElement?>?

    @GET("api/DeliveryApp/NotifyCustomer")
    fun NotifyCustomer(@Query("CustomerId") CustomerId: Int): Observable<JsonElement?>?

    @POST("api/DeliveryApp/CustomerUnloadLocation")
    fun customerUnloadLocationApi(@Body model: UnloadLocationModel?): Observable<JsonElement?>?

    @GET("/api/Payments/GetCustomerRTGSAmount")
    fun GetCustomerRTGSAmount(@Query("customerId") CustomerId: Int): Observable<JsonElement?>?

    @POST("api/VehicleHistory/InsertManyFirestoreData")
    fun postLocalBackgroundData(@Body model: List<UserLatLngModel>): Observable<JsonElement?>?

    @GET("/api/MobileDelivery/UpdateRefNumberForOrder")
    fun UpdateRefNumberForOrder(
        @Query("OrderId") OrderId: Int?,
        @Query("RefNo") RefNo: String?,
        @Query("PaymentResponseRetailerAppId") paymentResponseRetailerAppId: Int?
    ): Observable<Boolean?>?

    @GET("api/DeliveryApp/GetTripTouchPointToRearrange")
    suspend fun GetTripTouchPointToRearrange(@Query("tripPlannerConfirmMasterId") tripPlannerConfirmMasterId: Int): ArrayList<RearrangModel>

    @GET("api/DeliveryApp/SkipRearrange")
    suspend fun SkipRearrange(@Query("TripPlannerConfMasterID") tripPlannerConfirmMasterId: Int): Boolean

    @POST("api/DeliveryApp/UpdateTripTouchPointToRearrange")
    suspend fun UpdateTripTouchPointToRearrange(@Body model: ArrayList<RearrangModel>): Boolean

    @GET("api/DeliveryApp/GetCustomerWiseOrderList")
    suspend fun getTripOrderForUpdateStatus(@Query("TripPlannerConfirmedDetailId") masterId: Int ,@Query("ReturnOrder") returnOrder: Boolean): JsonObject

    @POST("api/DeliveryApp/UnlodingItem")
    suspend fun getUnlodingItem(@Body model: OrderDetailsRequestModel?): JsonObject

    @POST("api/DeliveryApp/NotifyReDispatchAndReAttempt")
    suspend fun notifyReDispatchAndReAttempt(@Body model: ReDispatchModel?):NotifyReDispatchReAttemptModel

    @POST("api/DeliveryApp/NotifyReDispatchAndReAttempt")
    suspend fun notifyReDispatchAndReAttemptReturn(@Body model: ReDispatchModel?):NotifyReDispatchReAttemptModel


    @GET("api/DeliveryTask/ValidateOTPForOrder")
    suspend fun notifyOTPReDispatchAndReAttempt(
        @Query("otp") otp: String?,
        @Query("OrderId") orderId: Int,
        @Query("Status") status: String?
    ): JsonObject

    @GET("/api/DeliveryTask/GenerateOTPForOrder")
    suspend fun getOtpForOrder(
        @Query("OrderId") orderId: Int,
        @Query("Status") status: String?,
        @Query("lat") lat: Double,
        @Query("lg") lg: Double,
        @Query("VideoUrl") videoUrl: String?
    ): Boolean

    @POST("api/DeliveryApp/ReDispatReAttemptAndOrderCancelConfirmOtp")
    suspend fun confirmOtpRedispatchReattempt(@Body model: ReDispatchOTPModel?): JsonObject

    @POST("api/DeliveryApp/NotifyDeliveryCancled")
    suspend fun cancelOrderNotify(@Body model: CancelOrderModel?): JsonObject

    @POST("api/DeliveryApp/DeliveryCancledConfirmOtp")
    suspend fun cancelOrderSendOtp(@Body model: CancelOrderSendOtpModel?): JsonObject

    @POST("api/DeliveryApp/SkipAll")
    suspend fun getCallSkipAll(@Query("TripplannerConfirmdetailedId") TripplannerConfirmdetailedId: Int): JsonObject

    @GET("api/DeliveryApp/BackOrderUpdate")
    suspend fun BackOrderUpdate(@Query("TripPlannerConfirmedDetailId") TripPlannerConfirmedDetailId: Int,@Query("TripPlannerConfirmedOrderId") TripPlannerConfirmedOrderId: Int): Boolean

    @GET("api/PlanMaster/ChangePeoplePassword")
    suspend fun changePassword(@Query("peopleId") peopleId: Int, @Query("newPassword") newPassword: String?,): ChnagePasswordResponseModel

    @GET("api/DeliveryApp/GetHolidayOnRedispatch")
    suspend fun getHolidayOnRedispatch(@Query("orderId") TripplannerConfirmdetailedId: Int,@Query("IsReattemt") IsReattemt: Boolean): List<String>

    @GET("api/DeliveryApp/GetHolidayOnRedispatch")
    fun  getHolidayOnReAttempt(@Query("orderId") TripplannerConfirmdetailedId: Int): Observable<JsonElement?>?

    @Multipart
    @POST("/api/DeliveryCancelledDraft/Image")
    fun DeliveryCancelledDraftUploadImage(@Part image: MultipartBody.Part): Observable<JsonObject>

    @POST("api/KKReturnReplace/ReturnOrderCreditNote")
    fun returnOrderCreditNote(@Body model: List<ReturnOrderCreditNoteRequestModel>):  Observable<JsonObject>

    @GET("api/DeliveryApp/ReturnItemList")
    fun returnItemList(@Query("TripPlannerConfirmedDetailId") TripPlannerConfirmedDetailId: Int): Observable<JsonArray>

    @POST("api/KKReturnReplace/PickedReturnOrderByDBoy")
    fun pickedReturnOrderByDBoy(@Body model:PickedReturnOrderByDBoyRequestModel):  Observable<JsonObject>

    @POST("api/KKReturnReplace/GenerateOTPForReturnOrder")
    fun generateOTPForReturnOrder(@Body model: GenerateOTPForReturnOrder?): Observable<JsonObject>

    @POST("api/DeliveryApp/GenerateOTPofSalesPersonforReattempt")
    suspend fun  generateOTPofSalesPersonforReattempt(@Body model: GenerateOTPofSalesPersonforReattemptRequestModel?): JsonObject

    @POST("api/DeliveryApp/GenerateOTPofSalesPersonforReattempt")
    fun  GenerateOTPofSalesPersonforReattempt(@Body model: GenerateOTPofSalesPersonforReattemptRequestModel?): Observable<JsonObject>


    @Multipart
    @POST("api/Video/Upload")
    suspend fun uploadVideo(@Part image: MultipartBody.Part): String

    @POST("api/DeliveryApp/NotifyDeliveryAction")
    suspend fun notifyDeliveryAction(@Body model: NotifyDeliveryActionRequestModel?): JsonObject
}