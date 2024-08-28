package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class MySingleTripOrderResponseModel {

    @Expose
    @SerializedName("singleOrderMapviewInfoDC")
    private SingleOrderMapviewInfoDC singleOrderMapviewInfoDC;
    @Expose
    @SerializedName("customerOrderinfoDc")
    private CustomerOrderinfoDc customerOrderinfoDc;

    public SingleOrderMapviewInfoDC getSingleOrderMapviewInfoDC() {
        return singleOrderMapviewInfoDC;
    }

    public void setSingleOrderMapviewInfoDC(SingleOrderMapviewInfoDC singleOrderMapviewInfoDC) {
        this.singleOrderMapviewInfoDC = singleOrderMapviewInfoDC;
    }

    public CustomerOrderinfoDc getCustomerOrderinfoDc() {
        return customerOrderinfoDc;
    }

    public void setCustomerOrderinfoDc(CustomerOrderinfoDc customerOrderinfoDc) {
        this.customerOrderinfoDc = customerOrderinfoDc;
    }

    public  class SingleOrderMapviewInfoDC {
        @Expose
        @SerializedName("OrderCompletionTime")
        private int OrderCompletionTime;
        @Expose
        @SerializedName("UnloadingTime")
        private int UnloadingTime;

        @Expose
        @SerializedName("WarehouesLat")
        private double WarehouesLat;
        @Expose
        @SerializedName("WarehouesLng")
        private double WarehouesLng;
        @Expose
        @SerializedName("TotalDistanceInMeter")
        private int TotalDistanceInMeter;
        @Expose
        @SerializedName("IsProcess")
        private boolean IsProcess;

        public double getWarehouesLat() {
            return WarehouesLat;
        }

        public void setWarehouesLat(double warehouesLat) {
            WarehouesLat = warehouesLat;
        }

        public double getWarehouesLng() {
            return WarehouesLng;
        }

        public void setWarehouesLng(double warehouesLng) {
            WarehouesLng = warehouesLng;
        }
        public int getOrderCompletionTime() {
            return OrderCompletionTime;
        }

        public void setOrderCompletionTime(int orderCompletionTime) {
            OrderCompletionTime = orderCompletionTime;
        }

        public int getUnloadingTime() {
            return UnloadingTime;
        }

        public void setUnloadingTime(int unloadingTime) {
            UnloadingTime = unloadingTime;
        }

        public int getTotalDistanceInMeter() {
            return TotalDistanceInMeter;
        }

        public void setTotalDistanceInMeter(int totalDistanceInMeter) {
            TotalDistanceInMeter = totalDistanceInMeter;
        }

        public boolean isProcess() {
            return IsProcess;
        }

        public void setProcess(boolean process) {
            IsProcess = process;
        }
    }
    public  class CustomerOrderinfoDc {
        @Expose
        @SerializedName("TripPlannerConfirmedDetailId")
        private int TripPlannerConfirmedDetailId;
        @Expose
        @SerializedName("TripPlannerConfirmedOrderId")
        private int TripPlannerConfirmedOrderId;
        @Expose
        @SerializedName("SequenceNo")
        private int SequenceNo;
        @Expose
        @SerializedName("CustomerId")
        private int CustomerId;
        @Expose
        @SerializedName("OrderCount")
        private int OrderCount;
        @Expose
        @SerializedName("IsProcess")
        private boolean IsProcess;
        @Expose
        @SerializedName("Lat")
        private double Lat;
        @Expose
        @SerializedName("Lng")
        private double Lng;

        @Expose
        @SerializedName("MobileNumber")
        private String MobileNumber;

        @Expose
        @SerializedName("VoiceNote")
        private String VoiceNote;

        @Expose
        @SerializedName("IsTakeShopImage")
        private boolean IsTakeShopImage ;

        @Expose
        @SerializedName("Skcode")
        private String Skcode;

        @Expose
        @SerializedName("ShopName")
        private String ShopName;
        @Expose
        @SerializedName("ShippingAddress")
        private String ShippingAddress;

        @Expose
        @SerializedName("IsReturnOrder")
        public boolean isReturnOrder;

        @Expose
        @SerializedName("IsGeneralOrder")
        public boolean isGeneralOrder;

        public String getShippingAddress() {
            return ShippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            ShippingAddress = shippingAddress;
        }

        public String getSkcode() {
            return Skcode;
        }

        public void setSkcode(String skcode) {
            Skcode = skcode;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String shopName) {
            ShopName = shopName;
        }

        public boolean isTakeShopImage() {
            return IsTakeShopImage;
        }

        public void setTakeShopImage(boolean takeShopImage) {
            IsTakeShopImage = takeShopImage;
        }

        public String getMobileNumber() {
            return MobileNumber;
        }

        public String getVoiceNote() {
            return VoiceNote;
        }

        public void setVoiceNote(String voiceNote) {
            VoiceNote = voiceNote;
        }


        public void setMobileNumber(String mobileNumber) {
            MobileNumber = mobileNumber;
        }

        public int getTripPlannerConfirmedOrderId() {
            return TripPlannerConfirmedOrderId;
        }

        public void setTripPlannerConfirmedOrderId(int tripPlannerConfirmedOrderId) {
            TripPlannerConfirmedOrderId = tripPlannerConfirmedOrderId;
        }
        public int getTripPlannerConfirmedDetailId() {
            return TripPlannerConfirmedDetailId;
        }

        public void setTripPlannerConfirmedDetailId(int tripPlannerConfirmedDetailId) {
            TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        }

        public int getSequenceNo() {
            return SequenceNo;
        }

        public void setSequenceNo(int sequenceNo) {
            SequenceNo = sequenceNo;
        }

        public int getCustomerId() {
            return CustomerId;
        }

        public void setCustomerId(int customerId) {
            CustomerId = customerId;
        }

        public int getOrderCount() {
            return OrderCount;
        }

        public void setOrderCount(int orderCount) {
            OrderCount = orderCount;
        }

        public boolean isProcess() {
            return IsProcess;
        }

        public void setProcess(boolean process) {
            IsProcess = process;
        }

        public double getLat() {
            return Lat;
        }

        public void setLat(double lat) {
            Lat = lat;
        }

        public double getLng() {
            return Lng;
        }

        public void setLng(double lng) {
            Lng = lng;
        }
    }

}
