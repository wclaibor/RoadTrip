package com.example.wclai.roadtrip;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.drew.imaging.ImageProcessingException;
import com.example.wclai.roadtrip.utils.PhotoMetadataProcessor;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.Maps;

import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static android.os.Environment.getExternalStorageDirectory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private MapsActivity thisActivity = this;

    private static final int MIN_PER_DEGREE = 60;
    private static final int SEC_PER_DEGREE = 3600;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

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

        Map<LatLng, File> locations = Maps.newHashMap();
        try {
            locations = PhotoMetadataProcessor.getPhotoLocations(pictureDirectory, context);
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<LatLng, File> entry : locations.entrySet()) {
            mMap.addMarker(new MarkerOptions().position(entry.getKey()).icon(BitmapDescriptorFactory.fromResource(R.drawable.rt_image_marker)).title(entry.getValue().getName()));
        }

//        InputStream in = null;
//        try {
//            in = getAssets().open("photos/test_photo.jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        ExifInterface exif = null;
//        try {
//            exif = new ExifInterface(in);
//        } catch (IOException e) {
//            Log.d("wclaib", "Didn't find the file");
//            e.printStackTrace();
//        }
//        if(exif != null) {
//            String lat = ExifInterface.TAG_GPS_LATITUDE;
//            String latRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
//            String longRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
//            String lat_data = exif.getAttribute(lat);
//            String long_data = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//            Log.d("wclaib", lat_data);
//            Log.d("wclaib", long_data);
//
//            double latDegrees = exifDataToDegrees(lat_data);
//            if (latRef.equals("S")) {
//                latDegrees = -latDegrees;
//            }
//            double longDegrees = exifDataToDegrees(long_data);
//            if (longRef.equals("W")) {
//                longDegrees = -longDegrees;
//            }
//
//
//
//            Log.d("wclaib", latDegrees + "° " + latRef + " " + longDegrees + "° " + longRef);
//            LatLng freemontTroll = new LatLng(latDegrees, longDegrees);
//            mMap.addMarker(new MarkerOptions().position(freemontTroll).title("Freemont Troll"));
//            System.out.println("this is newer");
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(freemontTroll));
//        }

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//        LatLng boise = new LatLng(43.6150, -116.2023);
//        mMap.addMarker(new MarkerOptions().position(boise).title("Marker in FreemontTroll"));
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(boise));
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


    // = Degrees + Minutes/60 + Seconds/3600
    private double exifDataToDegrees(String latData) {
        String[] pieces = latData.split(",");

        String[] degreePieces = pieces[0].split("/");
        double degrees = Integer.parseInt(degreePieces[0]) / Integer.parseInt(degreePieces[1]);

        String[] minutePieces = pieces[1].split("/");
        double minutes = Integer.parseInt(minutePieces[0]) / Integer.parseInt(minutePieces[1]);

        String[] secondPieces = pieces[2].split("/");
        double seconds = Integer.parseInt(secondPieces[0]) / Integer.parseInt(secondPieces[1]);

        return degrees + (minutes / MIN_PER_DEGREE) + (seconds / SEC_PER_DEGREE);
    }
}
