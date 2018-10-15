package com.example.wclai.roadtrip.animations;

import android.graphics.Point;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;
import java.util.Random;

public class FlyoverCallback implements GoogleMap.CancelableCallback {
    private GoogleMap mMap;
    private List<Marker> markers;
    private Marker target;

    public FlyoverCallback(GoogleMap mMap, List<Marker> markers, Marker target) {
        this.mMap = mMap;
        this.markers = markers;
        this.target = target;
    }

    @Override
    public void onFinish() {
        Random random = new Random();
        int randomIndex = random.ints(0, (markers.size())).findFirst().getAsInt();
        Marker nextTarget = markers.get(randomIndex);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                target.showInfoWindow();

                final Projection projection = mMap.getProjection();
                final Point markerPoint = projection.toScreenLocation(
                        target.getPosition()
                );
                // Shift the point we will use to center the map
                markerPoint.offset(10, -500);
                final LatLng newLatLng = projection.fromScreenLocation(markerPoint);

                mMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng), 500, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nextTarget.getPosition(), 10), 1000, new FlyoverCallback(mMap, markers, nextTarget));
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }

            @Override
            public void onCancel() {}
        });
    }

    @Override
    public void onCancel() {}
}
