package com.sk.ziladelivery.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnOrderListModel implements Parcelable {
    @Expose
    @SerializedName("Skcode")
    public String skCode;
    @Expose
    @SerializedName("ShippingAddress")
    public String shippingAddress;
    @Expose
    @SerializedName("Mobile")
    public String mobile;
    @Expose
    @SerializedName("Name")
    public String name;
    @Expose
    @SerializedName("ShopName")
    public String shopName;
    @Expose
    @SerializedName("KKRequstId")
    public int kkRequstId;
    @Expose
    @SerializedName("RequestType")
    public int requestType;
    @Expose
    @SerializedName("Status")
    public String status;
    @Expose
    @SerializedName("ModifiedDate")
    public String modifiedDate;
    @Expose
    @SerializedName("OrderId")
    public int orderId;

    private ReturnOrderListModel(Parcel in) {
        skCode = in.readString();
        shippingAddress = in.readString();
        mobile = in.readString();
        name = in.readString();
        shopName = in.readString();
        kkRequstId = in.readInt();
        requestType = in.readInt();
        status = in.readString();
        modifiedDate = in.readString();
        orderId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(skCode);
        dest.writeString(shippingAddress);
        dest.writeString(mobile);
        dest.writeString(name);
        dest.writeString(shopName);
        dest.writeInt(kkRequstId);
        dest.writeInt(requestType);
        dest.writeString(status);
        dest.writeString(modifiedDate);
        dest.writeInt(orderId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReturnOrderListModel> CREATOR = new Creator<ReturnOrderListModel>() {
        @Override
        public ReturnOrderListModel createFromParcel(Parcel in) {
            return new ReturnOrderListModel(in);
        }

        @Override
        public ReturnOrderListModel[] newArray(int size) {
            return new ReturnOrderListModel[size];
        }
    };

    public String getSkCode() {
        return skCode;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getShopName() {
        return shopName;
    }

    public int getKkRequstId() {
        return kkRequstId;
    }

    public int getRequestType() {
        return requestType;
    }

    public String getStatus() {
        return status;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public int getOrderId() {
        return orderId;
    }
}
