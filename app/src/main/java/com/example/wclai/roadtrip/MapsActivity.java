package com.example.wclai.roadtrip;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.example.wclai.roadtrip.utils.PhotoMetadataProcessor;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener, GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;

    private MapsActivity thisActivity = this;

    private static final int MIN_PER_DEGREE = 60;
    private static final int SEC_PER_DEGREE = 3600;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private Map<LatLng, File> locations = Maps.newHashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        getStoragePermissions();
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        Context context = getApplicationContext();

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        try {
            locations = PhotoMetadataProcessor.getPhotoLocations(pictureDirectory, context);
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<LatLng, File> entry : locations.entrySet()) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(entry.getKey()).icon(BitmapDescriptorFactory.fromResource(R.drawable.rt_image_marker)).title(entry.getValue().getName()));
            marker.setTag(entry.getKey());
        }

        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(new PhotoInfoWindow(locations));
    }

    private void getStoragePermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            return;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng location = (LatLng)marker.getTag();
        File file = locations.get(location);

        marker.showInfoWindow();

        return true;
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
