package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName


class CompanyInfoResponse {
    @SerializedName("CompanyDetails")
    val companyDetails: CompanyDetails? = null

    @SerializedName("Status")
    val isStatus = false

    @SerializedName("Message")
    val message: String? = null

    class CompanyDetails {
        @SerializedName("Id")
        val id = 0

        @SerializedName("Name")
        val name: String? = null

        @SerializedName("Contact")
        val contact: String? = null

        @SerializedName("Email")
        val email: String? = null

        @SerializedName("Website")
        val website: String? = null

        @SerializedName("LogoUrl")
        val logoUrl: String? = null

        @SerializedName("GatewayName")
        val gatewayName: String? = null

        @SerializedName("GatewayURL")
        val gatewayURL: String? = null

        @SerializedName("HDFCWorkingKey")
        val hDFCWorkingKey: String? = null

        @SerializedName("HDFCAccessCode")
        val hDFCAccessCode: String? = null

        @SerializedName("HDFCMerchantId")
        val hDFCMerchantId: String? = null

        @SerializedName("TrupayAccessToken")
        private val TrupayAccessToken: String? = null

        @SerializedName("TruepayCollectorId")
        private val TruepayCollectorId: String? = null

        @SerializedName("TruepayPaymentMethod")
        private val TruepayPaymentMethod: String? = null

        @SerializedName("ePayLaterEndpoint")
        val ePayLaterEndpoint: String? = null

        @SerializedName("E_PAY_LATER_URL")
        val ePAYLATERURL: String? = null

        @SerializedName("ENCODED_KEY")
        val eNCODEDKEY: String? = null

        @SerializedName("BEARER_TOKEN")
        val bEARERTOKEN: String? = null

        @SerializedName("IV")
        val iV: String? = null

        @SerializedName("M_CODE")
        val mCODE: String? = null

        @SerializedName("category")
        val category: String? = null

        @SerializedName("CreatedDate")
        private val CreatedDate: String? = null

        @SerializedName("CreatedBy")
        private val CreatedBy = 0

        @SerializedName("ModifiedBy")
        private val ModifiedBy: String? = null

        @SerializedName("redirect_url")
        val redirect_url: String? = null

        @SerializedName("cancel_url")
        val cancel_url: String? = null

        @SerializedName("IsShowLedger")
        val isShowLedger = false

        @SerializedName("IsShowTarget")
        val isShowTarget = false

        @SerializedName("MaxWalletPointUsed")
        val maxWalletPointUsed: String? = null

        @SerializedName("WebViewBaseUrl")
        val webViewBaseUrl: String? = null

        @SerializedName("IsShowReturn")
        val isShowReturn = false

        @SerializedName("IsShowHisabKitab")
        val isShowHisabKitab = false

        @SerializedName("TradeWebViewBaseUrl")
        val tradeWebViewURL: String? = null

        @SerializedName("IsShowTicketMenu")
        val isShowTicketMenu = false

        @SerializedName("IsShowCreditOption")
        val isShowCreditOption = false

        @SerializedName("IsRazorpayEnable")
        val isRazorpayEnable = false

        @SerializedName("CreditOptionName")
        val creditOptionName: String? = null

        @SerializedName("CreditGatewayURL")
        val creditGatewayURL: String? = null

        @SerializedName("CreditWorkingKey")
        val creditWorkingKey: String? = null

        @SerializedName("CreditAccessCode")
        val creditAccessCode: String? = null

        @SerializedName("CreditMerchantId")
        val creditMerchantId: String? = null

        @SerializedName("Creditredirect_url")
        val creditRedirectUrl: String? = null

        @SerializedName("Creditcancel_url")
        val creditCancelUrl: String? = null

        @SerializedName("IsPrimeActive")
        val isPremiumActive = false

        @SerializedName("PrimeAmount")
        val primeAmount = 0.0

        @SerializedName("PrimeMemberShipInMonth")
        val primeMemberShipInMonth = 0

        @SerializedName("MemberShipName")
        val memberShipName: String? = null

        @SerializedName("MemberShipHindiName")
        val memberShipHindiName: String? = null

        @SerializedName("SecondaryAPIUrl")
        var secondaryAPIUrl: String? = null

        @SerializedName("LogDboyLoctionMeter")
        var logDboyLoctionMeter = 0

        @SerializedName("MediaApiUrl")
        var MediaApiUrl: String? = ""
    }
}