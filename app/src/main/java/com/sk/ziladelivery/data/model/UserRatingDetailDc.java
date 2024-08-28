package com.sk.ziladelivery.data.model;
import com.google.gson.annotations.SerializedName;
public class UserRatingDetailDc {
    @SerializedName("Detail")
    public String Detail;
    public boolean isSelect;

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
