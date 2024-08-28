package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class CurrentEndTimeModel {



    @SerializedName("DeliveryIssuanceId")
    public int DeliveryIssuanceId  ;

        @SerializedName("PeopleID")
    public int PeopleID  ;


    @SerializedName("Start")
    public boolean Start ;

    @SerializedName("End")
    public boolean End ;

    @SerializedName("EndCoordinates")
    public String EndCoordinates;

    @SerializedName("StartCoordinates")
    public String StartCoordinates;

    public CurrentEndTimeModel(int deliveryIssuanceId, int peopleID,  boolean start, String startCoordinates,String EndCoordinates,boolean end )
    {
        this.DeliveryIssuanceId = deliveryIssuanceId;
        this.PeopleID = peopleID;
        this.Start = start;
        this.StartCoordinates = startCoordinates;
        this.EndCoordinates = EndCoordinates;
        this.End = end;
    }
    public CurrentEndTimeModel(int deliveryIssuanceId)
    {
        this.DeliveryIssuanceId = deliveryIssuanceId;

    }
}

