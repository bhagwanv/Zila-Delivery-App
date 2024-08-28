package com.sk.ziladelivery.utilities;

import android.location.Location;

public interface Listener {
    public String TAG = "Location_Sample_Logs";
    void locationOn();

    void currentLocation(Location location);

    void locationCancelled();
}


