package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class PostStartTimeResponseModel {



    @SerializedName("Message")
    private String Message;

    @SerializedName("Status")
    private boolean Status;

    @SerializedName("P")
    private P P;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }

    public P getP() {
        return P;
    }

    public void setP(P P) {
        this.P = P;
    }

    public  class P {
        @Expose
        @SerializedName("Deleted")
        private boolean Deleted;
        @Expose
        @SerializedName("UpdatedDate")
        private String UpdatedDate;
        @Expose
        @SerializedName("CreatedDate")
        private String CreatedDate;
        @Expose
        @SerializedName("EndCoordinates")
        private String EndCoordinates;
        @Expose
        @SerializedName("End")
        private boolean End;
        @Expose
        @SerializedName("StartCoordinates")
        private String StartCoordinates;
        @Expose
        @SerializedName("Start")
        private boolean Start;
        @Expose
        @SerializedName("StartDateTime")
        private String StartDateTime;
        @Expose
        @SerializedName("PeopleID")
        private int PeopleID;
        @Expose
        @SerializedName("DeliveryIssuanceId")
        private int DeliveryIssuanceId;
        @Expose
        @SerializedName("ETId")
        private int ETId;

        public boolean getDeleted() {
            return Deleted;
        }

        public void setDeleted(boolean Deleted) {
            this.Deleted = Deleted;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getEndCoordinates() {
            return EndCoordinates;
        }

        public void setEndCoordinates(String EndCoordinates) {
            this.EndCoordinates = EndCoordinates;
        }

        public boolean getEnd() {
            return End;
        }

        public void setEnd(boolean End) {
            this.End = End;
        }

        public String getStartCoordinates() {
            return StartCoordinates;
        }

        public void setStartCoordinates(String StartCoordinates) {
            this.StartCoordinates = StartCoordinates;
        }

        public boolean getStart() {
            return Start;
        }

        public void setStart(boolean Start) {
            this.Start = Start;
        }

        public String getStartDateTime() {
            return StartDateTime;
        }

        public void setStartDateTime(String StartDateTime) {
            this.StartDateTime = StartDateTime;
        }

        public int getPeopleID() {
            return PeopleID;
        }

        public void setPeopleID(int PeopleID) {
            this.PeopleID = PeopleID;
        }

        public int getDeliveryIssuanceId() {
            return DeliveryIssuanceId;
        }

        public void setDeliveryIssuanceId(int DeliveryIssuanceId) {
            this.DeliveryIssuanceId = DeliveryIssuanceId;
        }

        public int getETId() {
            return ETId;
        }

        public void setETId(int ETId) {
            this.ETId = ETId;
        }
    }
}
