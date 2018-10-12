package com.example.wclai.roadtrip;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.File;
import java.util.Map;

class PhotoInfoWindow implements GoogleMap.InfoWindowAdapter {
    private Map<LatLng, File> locations;

    public PhotoInfoWindow(Map<LatLng, File> locations) {
        this.locations = locations;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
