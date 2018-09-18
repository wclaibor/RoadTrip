package com.example.wclai.roadtrip;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int MIN_PER_DEGREE = 60;
    private static final int SEC_PER_DEGREE = 3600;

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
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        InputStream in = null;
        try {
            in = getAssets().open("photos/test_photo.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(in);
        } catch (IOException e) {
            Log.d("wclaib", "Didn't find the file");
            e.printStackTrace();
        }
        if(exif != null) {
            String lat = ExifInterface.TAG_GPS_LATITUDE;
            String latRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String longRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            String lat_data = exif.getAttribute(lat);
            String long_data = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            Log.d("wclaib", lat_data);
            Log.d("wclaib", long_data);

            double latDegrees = exifDataToDegrees(lat_data);
            if (latRef.equals("S")) {
                latDegrees = -latDegrees;
            }
            double longDegrees = exifDataToDegrees(long_data);
            if (longRef.equals("W")) {
                longDegrees = -longDegrees;
            }

            Log.d("wclaib", latDegrees + "° " + latRef + " " + longDegrees + "° " + longRef);
            LatLng freemontTroll = new LatLng(latDegrees, longDegrees);
            mMap.addMarker(new MarkerOptions().position(freemontTroll).title("Freemont Troll"));
            System.out.println("this is new");
            mMap.animateCamera(CameraUpdateFactory.newLatLng(freemontTroll));
        }

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//        LatLng boise = new LatLng(43.6150, -116.2023);
//        mMap.addMarker(new MarkerOptions().position(boise).title("Marker in FreemontTroll"));
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(boise));
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
