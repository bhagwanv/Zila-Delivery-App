package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DashBoardResponseModel {
    @Expose
    @SerializedName("Message")
    private String message;
    @Expose
    @SerializedName("Status")
    private boolean status;
    @Expose
    @SerializedName("TripDashboardDC")
    private TripdashboarddcEntity tripdashboarddc;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public TripdashboarddcEntity getTripdashboarddc() {
        return tripdashboarddc;
    }

    public void setTripdashboarddc(TripdashboarddcEntity tripdashboarddc) {
        this.tripdashboarddc = tripdashboarddc;
    }

    public class TripdashboarddcEntity {

        @Expose
        @SerializedName("ApproveNotifyTimeLeftInMinute")
        private int approveNotifyTimeLeftInMinute;

        @Expose
        @SerializedName("OrderStatuslist")
        private OrderstatuslistEntity orderstatuslist;
        @Expose
        @SerializedName("assignmentList")
        private ArrayList<AssignmentlistEntity> assignmentlist;
        @Expose
        @SerializedName("myTrip")
        private MytripEntity mytrip;
        @Expose
        @SerializedName("tripPlannerDistance")
        private TripplannerdistanceEntity tripplannerdistance;
        @Expose
        @SerializedName("CurrentStatus")
        private int currentstatus;
        @Expose
        @SerializedName("TripPlannerVehicleId")
        private int TripPlannerVehicleId;
        @Expose
        @SerializedName("BreakTimeInSec")
        private int BreakTimeInSec;
        @Expose
        @SerializedName("TripPlannerConfirmedDetailId")
        private int TripPlannerConfirmedDetailId;
        @Expose
        @SerializedName("IsTripEnd")
        private boolean IsTripEnd;

        @Expose
        @SerializedName("StartKm")
        private double StartKm;
        @Expose
        @SerializedName("ZilaTripMasterId")
        private int ZilaTripMasterId;

        @Expose
        @SerializedName("TripWorkingStatus")
        private int TripWorkingStatus;

        @Expose
        @SerializedName("IsSKFixVehicle")
        private boolean IsSKFixVehicle;

        public boolean isSKFixVehicle() {
            return IsSKFixVehicle;
        }

        public void setSKFixVehicle(boolean SKFixVehicle) {
            IsSKFixVehicle = SKFixVehicle;
        }



        public double getStartKm() {
            return StartKm;
        }

        public void setStartKm(double startKm) {
            StartKm = startKm;
        }


        public int getTripWorkingStatus() {
            return TripWorkingStatus;
        }

        public void setTripWorkingStatus(int tripWorkingStatus) {
            TripWorkingStatus = tripWorkingStatus;
        }


        public int getApproveNotifyTimeLeftInMinute() {
            return approveNotifyTimeLeftInMinute;
        }

        public void setApproveNotifyTimeLeftInMinute(int approveNotifyTimeLeftInMinute) {
            this.approveNotifyTimeLeftInMinute = approveNotifyTimeLeftInMinute;
        }

        public int getBreakTimeInSec() {
            return BreakTimeInSec;
        }

        public void setBreakTimeInSec(int breakTimeInSec) {
            BreakTimeInSec = breakTimeInSec;
        }

        public int getTripPlannerVehicleId() {
            return TripPlannerVehicleId;
        }

        public void setTripPlannerVehicleId(int tripPlannerVehicleId) {
            TripPlannerVehicleId = tripPlannerVehicleId;
        }

        public int getTripPlannerConfirmedDetailId() {
            return TripPlannerConfirmedDetailId;
        }

        public void setTripPlannerConfirmedDetailId(int tripPlannerConfirmedDetailId) {
            TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        }

        public boolean getIsTripEnd() {
            return IsTripEnd;
        }

        public void setIsTripEnd(boolean isTripEnd) {
            IsTripEnd = isTripEnd;
        }

        public OrderstatuslistEntity getOrderstatuslist() {
            return orderstatuslist;
        }

        public void setOrderstatuslist(OrderstatuslistEntity orderstatuslist) {
            this.orderstatuslist = orderstatuslist;
        }

        public ArrayList<AssignmentlistEntity> getAssignmentlist() {
            return assignmentlist;
        }

        public void setAssignmentlist(ArrayList<AssignmentlistEntity> assignmentlist) {
            this.assignmentlist = assignmentlist;
        }

        public MytripEntity getMytrip() {
            return mytrip;
        }

        public void setMytrip(MytripEntity mytrip) {
            this.mytrip = mytrip;
        }

        public TripplannerdistanceEntity getTripplannerdistance() {
            return tripplannerdistance;
        }

        public void setTripplannerdistance(TripplannerdistanceEntity tripplannerdistance) {
            this.tripplannerdistance = tripplannerdistance;
        }

        public int getCurrentstatus() {
            return currentstatus;
        }

        public void setCurrentstatus(int currentstatus) {
            this.currentstatus = currentstatus;
        }

        public int getTripplannerconfirmedmasterid() {
            return ZilaTripMasterId;
        }

        public void setTripplannerconfirmedmasterid(int tripplannerconfirmedmasterid) {
            this.ZilaTripMasterId = tripplannerconfirmedmasterid;
        }
    }

    public class OrderstatuslistEntity {
        @Expose
        @SerializedName("TotalDeliveryRedispatchAmount")
        private int totaldeliveryredispatchamount;
        @Expose
        @SerializedName("TotalDeliveryRedispatchOrder")
        private int totaldeliveryredispatchorder;
        @Expose
        @SerializedName("TotalDeliveryCanceledAmount")
        private int totaldeliverycanceledamount;
        @Expose
        @SerializedName("TotalDeliveryCanceledOrder")
        private int totaldeliverycanceledorder;
        @Expose
        @SerializedName("TotalDeliveredAmount")
        private int totaldeliveredamount;
        @Expose
        @SerializedName("TotalDeliveredOrder")
        private int totaldeliveredorder;
        @Expose
        @SerializedName("TotalShippedAmount")
        private int totalshippedamount;
        @Expose
        @SerializedName("TotalShippedOrder")
        private int totalshippedorder;

        @Expose
        @SerializedName("TotalReAttemptOrder")
        private int TotalReAttemptOrder;

        @Expose
        @SerializedName("TotalReAttemptAmount")
        private int TotalReAttemptAmount;

        public int getTotalReAttemptOrder() {
            return TotalReAttemptOrder;
        }

        public void setTotalReAttemptOrder(int totalReAttemptOrder) {
            TotalReAttemptOrder = totalReAttemptOrder;
        }

        public int getTotalReAttemptAmount() {
            return TotalReAttemptAmount;
        }

        public void setTotalReAttemptAmount(int totalReAttemptAmount) {
            TotalReAttemptAmount = totalReAttemptAmount;
        }

        public int getTotaldeliveryredispatchamount() {
            return totaldeliveryredispatchamount;
        }

        public void setTotaldeliveryredispatchamount(int totaldeliveryredispatchamount) {
            this.totaldeliveryredispatchamount = totaldeliveryredispatchamount;
        }

        public int getTotaldeliveryredispatchorder() {
            return totaldeliveryredispatchorder;
        }

        public void setTotaldeliveryredispatchorder(int totaldeliveryredispatchorder) {
            this.totaldeliveryredispatchorder = totaldeliveryredispatchorder;
        }

        public int getTotaldeliverycanceledamount() {
            return totaldeliverycanceledamount;
        }

        public void setTotaldeliverycanceledamount(int totaldeliverycanceledamount) {
            this.totaldeliverycanceledamount = totaldeliverycanceledamount;
        }

        public int getTotaldeliverycanceledorder() {
            return totaldeliverycanceledorder;
        }

        public void setTotaldeliverycanceledorder(int totaldeliverycanceledorder) {
            this.totaldeliverycanceledorder = totaldeliverycanceledorder;
        }

        public int getTotaldeliveredamount() {
            return totaldeliveredamount;
        }

        public void setTotaldeliveredamount(int totaldeliveredamount) {
            this.totaldeliveredamount = totaldeliveredamount;
        }

        public int getTotaldeliveredorder() {
            return totaldeliveredorder;
        }

        public void setTotaldeliveredorder(int totaldeliveredorder) {
            this.totaldeliveredorder = totaldeliveredorder;
        }

        public int getTotalshippedamount() {
            return totalshippedamount;
        }

        public void setTotalshippedamount(int totalshippedamount) {
            this.totalshippedamount = totalshippedamount;
        }

        public int getTotalshippedorder() {
            return totalshippedorder;
        }

        public void setTotalshippedorder(int totalshippedorder) {
            this.totalshippedorder = totalshippedorder;
        }
    }

    public class AssignmentlistEntity {
        @Expose
        @SerializedName("CreateDate")
        private String createdate;
        @Expose
        @SerializedName("AssignmentStatus")
        private String AssignmentStatus;
        @Expose
        @SerializedName("Amount")
        private double amount;
        @Expose
        @SerializedName("NoOfOrder")
        private int nooforder;
        @Expose
        @SerializedName("AssignmentId")
        private int assignmentid;

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getNooforder() {
            return nooforder;
        }

        public void setNooforder(int nooforder) {
            this.nooforder = nooforder;
        }

        public int getAssignmentid() {
            return assignmentid;
        }

        public void setAssignmentid(int assignmentid) {
            this.assignmentid = assignmentid;
        }

        public String getAssignmentStatus() {
            return AssignmentStatus;
        }

        public void setAssignmentStatus(String assignmentStatus) {
            AssignmentStatus = assignmentStatus;
        }
    }

    public class MytripEntity {
        @Expose
        @SerializedName("TripId")
        private int tripid;
        @Expose
        @SerializedName("TotalAmount")
        private double totalamount;
        @Expose
        @SerializedName("TotalOrder")
        private int totalorder;

        @Expose
        @SerializedName("CustomerCount")
        private int CustomerCount;

        public int getCustomerCount() {
            return CustomerCount;
        }

        public void setCustomerCount(int customerCount) {
            CustomerCount = customerCount;
        }

        public int getTripid() {
            return tripid;
        }

        public void setTripid(int tripid) {
            this.tripid = tripid;
        }

        public double getTotalamount() {
            return totalamount;
        }

        public void setTotalamount(double totalamount) {
            this.totalamount = totalamount;
        }

        public int getTotalorder() {
            return totalorder;
        }

        public void setTotalorder(int totalorder) {
            this.totalorder = totalorder;
        }
    }

    public class TripplannerdistanceEntity {
        @Expose
        @SerializedName("StartTime")
        private String starttime;
        @Expose
        @SerializedName("DistanceLeft")
        private double distanceleft;
        @Expose
        @SerializedName("DistanceTraveled")
        private double distancetraveled;
        @Expose
        @SerializedName("ReminingTime")
        private int reminingtime;
        @Expose
        @SerializedName("TravelTime")
        private int traveltime;
        @Expose
        @SerializedName("TotalTime")
        private int TotalTime;


        public int getTotalTime() {
            return TotalTime;
        }

        public void setTotalTime(int totalTime) {
            TotalTime = totalTime;
        }


        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public double getDistanceleft() {
            return distanceleft;
        }

        public void setDistanceleft(double distanceleft) {
            this.distanceleft = distanceleft;
        }

        public double getDistancetraveled() {
            return distancetraveled;
        }

        public void setDistancetraveled(double distancetraveled) {
            this.distancetraveled = distancetraveled;
        }

        public int getReminingtime() {
            return reminingtime;
        }

        public void setReminingtime(int reminingtime) {
            this.reminingtime = reminingtime;
        }

        public int getTraveltime() {
            return traveltime;
        }

        public void setTraveltime(int traveltime) {
            this.traveltime = traveltime;
        }
    }
}
