package com.sk.ziladelivery.ui.views.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonElement;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.model.SortedOrdersModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.DirectionsJSONParser;
import com.sk.ziladelivery.utilities.GpsUtils;
import com.sk.ziladelivery.utilities.MarshmallowPermissions;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.DrivingDirectionViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DrivingDirectionActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionsJSONParser.directionDuraction {
    public final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    Geocoder geocoder;
    List<Address> addresses;
    private Location mLastLocation, location;
    private LocationRequest mLocationRequest;
    private double lat, lag, directionTime;
    private Marker marker;
    private boolean isMarkerRotating=false;
    private ArrayList<SortedOrdersModel> SortedOrdersModel;
    private int orderID, assginmentID;
    private TextView timeDuractionTV, tatalDistanceTV;
    private DrivingDirectionViewModel drivingDirectionViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        timeDuractionTV = findViewById(R.id.tv_time_durection);
        tatalDistanceTV = findViewById(R.id.tv_distance);
        permission();
        new GpsUtils(this).turnGPSOn(isGPSEnable -> {

        });
        drivingDirectionViewModel = ViewModelProviders.of(this).get(DrivingDirectionViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        SortedOrdersModel = (ArrayList<SortedOrdersModel>) intent.getSerializableExtra("SortedOrdersModel");
        assginmentID = intent.getIntExtra("assginmentID", 0);

       /* try {
            lat = SortedOrdersModel.get(SortedOrdersModel.size() - 1).getLat();
            lag = SortedOrdersModel.get(SortedOrdersModel.size() - 1).getLng();
            destinationLatLog = new LatLng(lat, lag);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 1, DrivingDirectionActivity.this);
        Criteria criteria = new Criteria();
        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        geocoder = new Geocoder(this, Locale.getDefault());



        drivingDirectionViewModel.getmapRoute().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponse(apiResponse);
            }
        });
        drivingDirectionViewModel.hitRouteMap(assginmentID);
    }

    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                break;
            case SUCCESS:
                renderSuccessResponse(apiResponse.data);
                break;
            case ERROR:
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void renderSuccessResponse(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
             //   Logger.d(CommonMethods.getTag(this), response.toString());
                try {
                    String waypoints = "";
                    for (int j = 0; j < SortedOrdersModel.size(); j++) {
                        SortedOrdersModel point = SortedOrdersModel.get(j);

                        if (j != SortedOrdersModel.size() - 1) {
                            waypoints += point.getLat() + "," + point.getLng() + "|";
                        }
                        orderID = point.getOrderId();

                        createMarker(new LatLng(point.getLat(), point.getLng()), "Order ID " + orderID, BitmapDescriptorFactory.HUE_BLUE);
                    }
                    JSONObject obj = new JSONObject(response.toString());
                    JSONObject result = obj.getJSONObject("Root");
                    new ParserTask().execute(result);

                    double bearing = bearingBetweenLocations(new LatLng(location.getLatitude(),location.getLongitude()),new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
                    marker.setRotation((float)bearing);
                   /* rotateMarker(marker, (bearing()));*/
                   /* rotateMarker(marker, (float) bearing);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(true);
        marker = map.addMarker(new MarkerOptions().position(new LatLng(0, 0))
                /*.title("Start point")*/.icon(BitmapDescriptorFactory.fromResource(R.drawable.dboy_truck)));
        marker.showInfoWindow();
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // Cleaning all the markers.

            }
        });
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    map.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
            new Handler().postDelayed(() -> {
//                fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//
//                        }
//                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lag), 13));
//                        //dblatlag = new LatLng(location.getLatitude(), location.getLongitude());
//                        CameraPosition cameraPosition = new CameraPosition.Builder()
//                                .target(new LatLng(lat, lag))
//                                .zoom(17)
//                                .build();
//                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                        marker.setPosition(new LatLng(lat, lag));
//                    /*String url = getUrl(dblatlag, destinationLatLog);
//                    new FetchUrl().execute(url);*/
//                    }
//                });

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lag), 13));
                // dblatlag = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lag))
                        .zoom(17)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                marker.setPosition(new LatLng(lat, lag));
            }, 1500);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DrivingDirectionActivity.this, MainActivity.class));
        finish();
    }


    /*private String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String output = "json";

        String waypoints = "";
        for (int j = 0; j < SortedOrdersModel.size(); j++) {
            SortedOrdersModel point = (SortedOrdersModel) SortedOrdersModel.get(j);

            if (j != SortedOrdersModel.size() - 1) {
                waypoints += point.getLat() + "," + point.getLng() + "|";
            }
            orderID = point.getOrderId();

            createMarker(new LatLng(point.getLat(), point.getLng()), "Order ID " + orderID, BitmapDescriptorFactory.HUE_BLUE);

        }
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + BuildConfig.DirectionApiKey;
        Log.d("TAG", "getUrl:" + url);
        return url;
    }*/

    protected Marker createMarker(LatLng destinationLatLog, String title, float iconResID) {

        return map.addMarker(new MarkerOptions()
                .position(destinationLatLog)
                .anchor(0.5f, 0.5f)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.store)));

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void direction(double changeduraction, double distance, double lat, double lag, double endlat, double endlag, String durationString, String distance2) {
        this.directionTime = changeduraction;
        this.lat = lat;
        this.lag = lag;
        if (directionTime > 60) {
            String convertTime = Utils.convertToDaysHoursMinutes((long) directionTime);
            NumberFormat formatter = new DecimalFormat("##.00");
            tatalDistanceTV.setText(formatter.format(distance) + " KM");
            timeDuractionTV.setText(convertTime);
        } else {
            String convertTime = Utils.convertToDaysHoursMinutes((long) directionTime);
            timeDuractionTV.setText("" + convertTime);
            //tatalDistanceTV.setText(distance + " KM");
            tatalDistanceTV.setText(new DecimalFormat("##.##").format(distance) + " KM");
        }

    }

    private float bearing() {
        return location.bearingTo(mLastLocation);
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            /*DrivingDirectionActivity.ParserTask parserTask = new DrivingDirectionActivity.ParserTask();
            parserTask.execute(result);*/

        }
    }

    private class ParserTask extends AsyncTask<JSONObject, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(JSONObject... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                DirectionsJSONParser parser = new DirectionsJSONParser(DrivingDirectionActivity.this);
                routes = parser.parse(jsonData[0]);
            } catch (Exception e) {

                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            try {
                ArrayList<LatLng> points;
                PolylineOptions lineOptions = null;
                if (result == null) {
                    Toast.makeText(DrivingDirectionActivity.this, "No point", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = result.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }
                    lineOptions.addAll(points);
                    lineOptions.width(20);
                    lineOptions.color(Color.RED);

                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                }
                map.addPolyline(lineOptions);
                if (lineOptions != null) {
                    map.addPolyline(lineOptions);
                } else {
                    Log.d("onPostExecute", "without Polylines drawn");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mLastLocation == null) {
            mLastLocation = location;
        }
        this.location = location;
        marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        //animateMarker(marker,new LatLng(location.getLatitude(), location.getLongitude()),false);
        double bearing = bearingBetweenLocations(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),new LatLng(location.getLatitude(),location.getLongitude()));
        /*rotateMarker(marker,(bearing()) );*/
        marker.setRotation((float)bearing());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        marker.setTitle(address);
        mLastLocation = location;
    }

    @Override
    public void onConnectionFailed(@NotNull ConnectionResult connectionResult)
    {

    }
    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }
    private void rotateMarker(final Marker marker, final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private void permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (MarshmallowPermissions.checkPermissionLocation(DrivingDirectionActivity.this)) {

            } else {
                MarshmallowPermissions.requestPermissionLocation(DrivingDirectionActivity.this, MarshmallowPermissions.PERMISSION_REQUEST_CODE_LOCATION);
            }
        } else {

        }
    }
}