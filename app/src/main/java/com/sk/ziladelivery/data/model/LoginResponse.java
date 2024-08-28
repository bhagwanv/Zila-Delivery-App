package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public  class LoginResponse {


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

    public class P {

        @SerializedName("UpdatedDate")
        private String UpdatedDate;

        @SerializedName("CreatedDate")
        private String CreatedDate;
        @SerializedName("VehicleCapacity")
        private int VehicleCapacity;
        @SerializedName("VehicleNumber")
        private String VehicleNumber;
        @SerializedName("VehicleName")
        private String VehicleName;
        @SerializedName("VehicleId")
        private int VehicleId;
        @SerializedName("Password")
        private String Password;

        @SerializedName("Mobile")
        private String Mobile;
        @SerializedName("DisplayName")
        private String DisplayName;
        @SerializedName("Email")
        private String Email;
        @SerializedName("PeopleLastName")
        private String PeopleLastName;
        @SerializedName("PeopleFirstName")
        private String PeopleFirstName;
        @SerializedName("WarehouseId")
        private int WarehouseId;
        @SerializedName("Role")
        private String Role;
        @SerializedName("CompanyId")
        private int CompanyId;

        @SerializedName("PeopleID")
        private int PeopleID;

        @SerializedName("Skcode")
        private String Skcode;
        @SerializedName("ImageUrl")
        private String ImageUrl;

        @SerializedName("OtpNumbers")
        private String OtpNumbers;

        @SerializedName("RegisteredApk")
        public UserAuth registeredApk;

        public String getRole() {
            return Role;
        }

        public void setRole(String role) {
            Role = role;
        }
        public UserAuth getRegisteredApk() {
            return registeredApk;
        }

        public void setRegisteredApk(UserAuth registeredApk) {
            this.registeredApk = registeredApk;
        }

        public String getOtpNumbers() {
            return OtpNumbers;
        }

        public void setOtpNumbers(String otpNumbers) {
            OtpNumbers = otpNumbers;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String imageUrl) {
            ImageUrl = imageUrl;
        }

        public String getSkcode() {
            return Skcode;
        }

        public void setSkcode(String skcode) {
            Skcode = skcode;
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

        public int getVehicleCapacity() {
            return VehicleCapacity;
        }

        public void setVehicleCapacity(int VehicleCapacity) {
            this.VehicleCapacity = VehicleCapacity;
        }

        public String getVehicleNumber() {
            return VehicleNumber;
        }

        public void setVehicleNumber(String VehicleNumber) {
            this.VehicleNumber = VehicleNumber;
        }

        public String getVehicleName() {
            return VehicleName;
        }

        public void setVehicleName(String VehicleName) {
            this.VehicleName = VehicleName;
        }

        public int getVehicleId() {
            return VehicleId;
        }

        public void setVehicleId(int VehicleId) {
            this.VehicleId = VehicleId;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public String getDisplayName() {
            return DisplayName;
        }

        public void setDisplayName(String DisplayName) {
            this.DisplayName = DisplayName;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getPeopleLastName() {
            return PeopleLastName;
        }

        public void setPeopleLastName(String PeopleLastName) {
            this.PeopleLastName = PeopleLastName;
        }

        public String getPeopleFirstName() {
            return PeopleFirstName;
        }

        public void setPeopleFirstName(String PeopleFirstName) {
            this.PeopleFirstName = PeopleFirstName;
        }

        public int getWarehouseId() {
            return WarehouseId;
        }

        public void setWarehouseId(int WarehouseId) {
            this.WarehouseId = WarehouseId;
        }

        public int getCompanyId() {
            return CompanyId;
        }

        public void setCompanyId(int CompanyId) {
            this.CompanyId = CompanyId;
        }

        public int getPeopleID() {
            return PeopleID;
        }

        public void setPeopleID(int PeopleID) {
            this.PeopleID = PeopleID;
        }
    }
}
