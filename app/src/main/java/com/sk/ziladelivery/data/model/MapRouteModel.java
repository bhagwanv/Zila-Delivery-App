package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class MapRouteModel {

    @Expose
    @SerializedName("Root")
    private RootBean Root;

    public RootBean getRoot() {
        return Root;
    }

    public void setRoot(RootBean Root) {
        this.Root = Root;
    }

    public static class RootBean {
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("routes")
        private List<RoutesBean> routes;
        @Expose
        @SerializedName("geocoded_waypoints")
        private List<Geocoded_waypointsBean> geocoded_waypoints;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<RoutesBean> getRoutes() {
            return routes;
        }

        public void setRoutes(List<RoutesBean> routes) {
            this.routes = routes;
        }

        public List<Geocoded_waypointsBean> getGeocoded_waypoints() {
            return geocoded_waypoints;
        }

        public void setGeocoded_waypoints(List<Geocoded_waypointsBean> geocoded_waypoints) {
            this.geocoded_waypoints = geocoded_waypoints;
        }
    }

    public static class RoutesBean {
        @Expose
        @SerializedName("waypoint_order")
        private List<Integer> waypoint_order;
        @Expose
        @SerializedName("warnings")
        private List<String> warnings;
        @Expose
        @SerializedName("summary")
        private String summary;
        @Expose
        @SerializedName("overview_polyline")
        private Overview_polylineBean overview_polyline;
        @Expose
        @SerializedName("legs")
        private List<LegsBean> legs;
        @Expose
        @SerializedName("copyrights")
        private String copyrights;
        @Expose
        @SerializedName("bounds")
        private BoundsBean bounds;

        public List<Integer> getWaypoint_order() {
            return waypoint_order;
        }

        public void setWaypoint_order(List<Integer> waypoint_order) {
            this.waypoint_order = waypoint_order;
        }

        public List<String> getWarnings() {
            return warnings;
        }

        public void setWarnings(List<String> warnings) {
            this.warnings = warnings;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public Overview_polylineBean getOverview_polyline() {
            return overview_polyline;
        }

        public void setOverview_polyline(Overview_polylineBean overview_polyline) {
            this.overview_polyline = overview_polyline;
        }

        public List<LegsBean> getLegs() {
            return legs;
        }

        public void setLegs(List<LegsBean> legs) {
            this.legs = legs;
        }

        public String getCopyrights() {
            return copyrights;
        }

        public void setCopyrights(String copyrights) {
            this.copyrights = copyrights;
        }

        public BoundsBean getBounds() {
            return bounds;
        }

        public void setBounds(BoundsBean bounds) {
            this.bounds = bounds;
        }
    }

    public static class Overview_polylineBean {
        @Expose
        @SerializedName("points")
        private String points;

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
    }

    public static class LegsBean {
        @Expose
        @SerializedName("via_waypoint")
        private List<String> via_waypoint;
        @Expose
        @SerializedName("traffic_speed_entry")
        private List<String> traffic_speed_entry;
        @Expose
        @SerializedName("steps")
        private List<StepsBean> steps;
        @Expose
        @SerializedName("start_location")
        private Start_locationBean start_location;
        @Expose
        @SerializedName("start_address")
        private String start_address;
        @Expose
        @SerializedName("end_location")
        private End_locationBean end_location;
        @Expose
        @SerializedName("end_address")
        private String end_address;
        @Expose
        @SerializedName("duration")
        private DurationBean duration;
        @Expose
        @SerializedName("distance")
        private DistanceBean distance;

        public List<String> getVia_waypoint() {
            return via_waypoint;
        }

        public void setVia_waypoint(List<String> via_waypoint) {
            this.via_waypoint = via_waypoint;
        }

        public List<String> getTraffic_speed_entry() {
            return traffic_speed_entry;
        }

        public void setTraffic_speed_entry(List<String> traffic_speed_entry) {
            this.traffic_speed_entry = traffic_speed_entry;
        }

        public List<StepsBean> getSteps() {
            return steps;
        }

        public void setSteps(List<StepsBean> steps) {
            this.steps = steps;
        }

        public Start_locationBean getStart_location() {
            return start_location;
        }

        public void setStart_location(Start_locationBean start_location) {
            this.start_location = start_location;
        }

        public String getStart_address() {
            return start_address;
        }

        public void setStart_address(String start_address) {
            this.start_address = start_address;
        }

        public End_locationBean getEnd_location() {
            return end_location;
        }

        public void setEnd_location(End_locationBean end_location) {
            this.end_location = end_location;
        }

        public String getEnd_address() {
            return end_address;
        }

        public void setEnd_address(String end_address) {
            this.end_address = end_address;
        }

        public DurationBean getDuration() {
            return duration;
        }

        public void setDuration(DurationBean duration) {
            this.duration = duration;
        }

        public DistanceBean getDistance() {
            return distance;
        }

        public void setDistance(DistanceBean distance) {
            this.distance = distance;
        }
    }

    public static class StepsBean {
        @Expose
        @SerializedName("maneuver")
        private String maneuver;
        @Expose
        @SerializedName("travel_mode")
        private String travel_mode;
        @Expose
        @SerializedName("start_location")
        private Start_locationBean start_location;
        @Expose
        @SerializedName("polyline")
        private PolylineBean polyline;
        @Expose
        @SerializedName("html_instructions")
        private String html_instructions;
        @Expose
        @SerializedName("end_location")
        private End_locationBean end_location;
        @Expose
        @SerializedName("duration")
        private DurationBean duration;
        @Expose
        @SerializedName("distance")
        private DistanceBean distance;

        public String getManeuver() {
            return maneuver;
        }

        public void setManeuver(String maneuver) {
            this.maneuver = maneuver;
        }

        public String getTravel_mode() {
            return travel_mode;
        }

        public void setTravel_mode(String travel_mode) {
            this.travel_mode = travel_mode;
        }

        public Start_locationBean getStart_location() {
            return start_location;
        }

        public void setStart_location(Start_locationBean start_location) {
            this.start_location = start_location;
        }

        public PolylineBean getPolyline() {
            return polyline;
        }

        public void setPolyline(PolylineBean polyline) {
            this.polyline = polyline;
        }

        public String getHtml_instructions() {
            return html_instructions;
        }

        public void setHtml_instructions(String html_instructions) {
            this.html_instructions = html_instructions;
        }

        public End_locationBean getEnd_location() {
            return end_location;
        }

        public void setEnd_location(End_locationBean end_location) {
            this.end_location = end_location;
        }

        public DurationBean getDuration() {
            return duration;
        }

        public void setDuration(DurationBean duration) {
            this.duration = duration;
        }

        public DistanceBean getDistance() {
            return distance;
        }

        public void setDistance(DistanceBean distance) {
            this.distance = distance;
        }
    }

    public static class Start_locationBean {
        @Expose
        @SerializedName("lng")
        private double lng;
        @Expose
        @SerializedName("lat")
        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class PolylineBean {
        @Expose
        @SerializedName("points")
        private String points;

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
    }

    public static class End_locationBean {
        @Expose
        @SerializedName("lng")
        private double lng;
        @Expose
        @SerializedName("lat")
        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class DurationBean {
        @Expose
        @SerializedName("value")
        private int value;
        @Expose
        @SerializedName("text")
        private String text;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class DistanceBean {
        @Expose
        @SerializedName("value")
        private int value;
        @Expose
        @SerializedName("text")
        private String text;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class BoundsBean {
        @Expose
        @SerializedName("southwest")
        private SouthwestBean southwest;
        @Expose
        @SerializedName("northeast")
        private NortheastBean northeast;

        public SouthwestBean getSouthwest() {
            return southwest;
        }

        public void setSouthwest(SouthwestBean southwest) {
            this.southwest = southwest;
        }

        public NortheastBean getNortheast() {
            return northeast;
        }

        public void setNortheast(NortheastBean northeast) {
            this.northeast = northeast;
        }
    }

    public static class SouthwestBean {
        @Expose
        @SerializedName("lng")
        private double lng;
        @Expose
        @SerializedName("lat")
        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class NortheastBean {
        @Expose
        @SerializedName("lng")
        private double lng;
        @Expose
        @SerializedName("lat")
        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class Geocoded_waypointsBean {
        @Expose
        @SerializedName("types")
        private List<String> types;
        @Expose
        @SerializedName("place_id")
        private String place_id;
        @Expose
        @SerializedName("geocoder_status")
        private String geocoder_status;

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public String getGeocoder_status() {
            return geocoder_status;
        }

        public void setGeocoder_status(String geocoder_status) {
            this.geocoder_status = geocoder_status;
        }
    }
}
