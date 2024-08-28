package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public  class CurrencyResponseModel {


    @SerializedName("Message")
    private String Message;

    @SerializedName("Status")
    private boolean Status;

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

   /* @SerializedName("DBoyorders")
    private DBoyorders DBoyorders;



    public DBoyorders getDBoyorders() {
        return DBoyorders;
    }

    public void setDBoyorders(DBoyorders DBoyorders) {
        this.DBoyorders = DBoyorders;
    }

    public  class DBoyorders {
        @Expose
        @SerializedName("check")
        private boolean check;
        @Expose
        @SerializedName("checkrec")
        private boolean checkrec;
        @Expose
        @SerializedName("Deleted")
        private boolean Deleted;
        @Expose
        @SerializedName("Peoplename")
        private String Peoplename;
        @Expose
        @SerializedName("Status")
        private String Status;
        @Expose
        @SerializedName("UpdatedDate")
        private String UpdatedDate;
        @Expose
        @SerializedName("CreatedDate")
        private String CreatedDate;
        @Expose
        @SerializedName("checkamount")
        private String checkamount;
        @Expose
        @SerializedName("checknumber")
        private String checknumber;
        @Expose
        @SerializedName("checkTotalAmount")
        private int checkTotalAmount;
        @Expose
        @SerializedName("TotalAmount")
        private int TotalAmount;
        @Expose
        @SerializedName("Dueamount")
        private int Dueamount;
        @Expose
        @SerializedName("twoTHrscount")
        private int twoTHrscount;
        @Expose
        @SerializedName("twoTHRupee")
        private int twoTHRupee;
        @Expose
        @SerializedName("fivehrscount")
        private int fivehrscount;
        @Expose
        @SerializedName("fiveHRupee")
        private int fiveHRupee;
        @Expose
        @SerializedName("twohunrscount")
        private int twohunrscount;
        @Expose
        @SerializedName("twoHunRupee")
        private int twoHunRupee;
        @Expose
        @SerializedName("hunrscount")
        private int hunrscount;
        @Expose
        @SerializedName("HunRupee")
        private int HunRupee;
        @Expose
        @SerializedName("fiftyrscount")
        private int fiftyrscount;
        @Expose
        @SerializedName("fiftyRupee")
        private int fiftyRupee;
        @Expose
        @SerializedName("Twentyrscount")
        private int Twentyrscount;
        @Expose
        @SerializedName("TwentyRupee")
        private int TwentyRupee;
        @Expose
        @SerializedName("TenNoteCount")
        private int TenNoteCount;
        @Expose
        @SerializedName("TenNote")
        private int TenNote;
        @Expose
        @SerializedName("tenrscount")
        private int tenrscount;
        @Expose
        @SerializedName("TenRupee")
        private int TenRupee;
        @Expose
        @SerializedName("FiveNoteCount")
        private int FiveNoteCount;
        @Expose
        @SerializedName("FiveNote")
        private int FiveNote;
        @Expose
        @SerializedName("fiverscount")
        private int fiverscount;
        @Expose
        @SerializedName("FiveRupee")
        private int FiveRupee;
        @Expose
        @SerializedName("tworscount")
        private int tworscount;
        @Expose
        @SerializedName("TwoRupee")
        private int TwoRupee;
        @Expose
        @SerializedName("onerscount")
        private int onerscount;
        @Expose
        @SerializedName("OneRupee")
        private int OneRupee;
        @Expose
        @SerializedName("PeopleId")
        private int PeopleId;
        @Expose
        @SerializedName("DeliveryIssuanceId")
        private int DeliveryIssuanceId;
        @Expose
        @SerializedName("DBoyCId")
        private int DBoyCId;

        public boolean getCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public boolean getCheckrec() {
            return checkrec;
        }

        public void setCheckrec(boolean checkrec) {
            this.checkrec = checkrec;
        }

        public boolean getDeleted() {
            return Deleted;
        }

        public void setDeleted(boolean Deleted) {
            this.Deleted = Deleted;
        }

        public String getPeoplename() {
            return Peoplename;
        }

        public void setPeoplename(String Peoplename) {
            this.Peoplename = Peoplename;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
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

        public String getCheckamount() {
            return checkamount;
        }

        public void setCheckamount(String checkamount) {
            this.checkamount = checkamount;
        }

        public String getChecknumber() {
            return checknumber;
        }

        public void setChecknumber(String checknumber) {
            this.checknumber = checknumber;
        }

        public int getCheckTotalAmount() {
            return checkTotalAmount;
        }

        public void setCheckTotalAmount(int checkTotalAmount) {
            this.checkTotalAmount = checkTotalAmount;
        }

        public int getTotalAmount() {
            return TotalAmount;
        }

        public void setTotalAmount(int TotalAmount) {
            this.TotalAmount = TotalAmount;
        }

        public int getDueamount() {
            return Dueamount;
        }

        public void setDueamount(int Dueamount) {
            this.Dueamount = Dueamount;
        }

        public int getTwoTHrscount() {
            return twoTHrscount;
        }

        public void setTwoTHrscount(int twoTHrscount) {
            this.twoTHrscount = twoTHrscount;
        }

        public int getTwoTHRupee() {
            return twoTHRupee;
        }

        public void setTwoTHRupee(int twoTHRupee) {
            this.twoTHRupee = twoTHRupee;
        }

        public int getFivehrscount() {
            return fivehrscount;
        }

        public void setFivehrscount(int fivehrscount) {
            this.fivehrscount = fivehrscount;
        }

        public int getFiveHRupee() {
            return fiveHRupee;
        }

        public void setFiveHRupee(int fiveHRupee) {
            this.fiveHRupee = fiveHRupee;
        }

        public int getTwohunrscount() {
            return twohunrscount;
        }

        public void setTwohunrscount(int twohunrscount) {
            this.twohunrscount = twohunrscount;
        }

        public int getTwoHunRupee() {
            return twoHunRupee;
        }

        public void setTwoHunRupee(int twoHunRupee) {
            this.twoHunRupee = twoHunRupee;
        }

        public int getHunrscount() {
            return hunrscount;
        }

        public void setHunrscount(int hunrscount) {
            this.hunrscount = hunrscount;
        }

        public int getHunRupee() {
            return HunRupee;
        }

        public void setHunRupee(int HunRupee) {
            this.HunRupee = HunRupee;
        }

        public int getFiftyrscount() {
            return fiftyrscount;
        }

        public void setFiftyrscount(int fiftyrscount) {
            this.fiftyrscount = fiftyrscount;
        }

        public int getFiftyRupee() {
            return fiftyRupee;
        }

        public void setFiftyRupee(int fiftyRupee) {
            this.fiftyRupee = fiftyRupee;
        }

        public int getTwentyrscount() {
            return Twentyrscount;
        }

        public void setTwentyrscount(int Twentyrscount) {
            this.Twentyrscount = Twentyrscount;
        }

        public int getTwentyRupee() {
            return TwentyRupee;
        }

        public void setTwentyRupee(int TwentyRupee) {
            this.TwentyRupee = TwentyRupee;
        }

        public int getTenNoteCount() {
            return TenNoteCount;
        }

        public void setTenNoteCount(int TenNoteCount) {
            this.TenNoteCount = TenNoteCount;
        }

        public int getTenNote() {
            return TenNote;
        }

        public void setTenNote(int TenNote) {
            this.TenNote = TenNote;
        }

        public int getTenrscount() {
            return tenrscount;
        }

        public void setTenrscount(int tenrscount) {
            this.tenrscount = tenrscount;
        }

        public int getTenRupee() {
            return TenRupee;
        }

        public void setTenRupee(int TenRupee) {
            this.TenRupee = TenRupee;
        }

        public int getFiveNoteCount() {
            return FiveNoteCount;
        }

        public void setFiveNoteCount(int FiveNoteCount) {
            this.FiveNoteCount = FiveNoteCount;
        }

        public int getFiveNote() {
            return FiveNote;
        }

        public void setFiveNote(int FiveNote) {
            this.FiveNote = FiveNote;
        }

        public int getFiverscount() {
            return fiverscount;
        }

        public void setFiverscount(int fiverscount) {
            this.fiverscount = fiverscount;
        }

        public int getFiveRupee() {
            return FiveRupee;
        }

        public void setFiveRupee(int FiveRupee) {
            this.FiveRupee = FiveRupee;
        }

        public int getTworscount() {
            return tworscount;
        }

        public void setTworscount(int tworscount) {
            this.tworscount = tworscount;
        }

        public int getTwoRupee() {
            return TwoRupee;
        }

        public void setTwoRupee(int TwoRupee) {
            this.TwoRupee = TwoRupee;
        }

        public int getOnerscount() {
            return onerscount;
        }

        public void setOnerscount(int onerscount) {
            this.onerscount = onerscount;
        }

        public int getOneRupee() {
            return OneRupee;
        }

        public void setOneRupee(int OneRupee) {
            this.OneRupee = OneRupee;
        }

        public int getPeopleId() {
            return PeopleId;
        }

        public void setPeopleId(int PeopleId) {
            this.PeopleId = PeopleId;
        }

        public int getDeliveryIssuanceId() {
            return DeliveryIssuanceId;
        }

        public void setDeliveryIssuanceId(int DeliveryIssuanceId) {
            this.DeliveryIssuanceId = DeliveryIssuanceId;
        }

        public int getDBoyCId() {
            return DBoyCId;
        }

        public void setDBoyCId(int DBoyCId) {
            this.DBoyCId = DBoyCId;
        }
    }*/
}
