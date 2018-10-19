package com.example.wclai.roadtrip.animations;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

public class FlyoverRunnable implements Runnable {

    private GoogleMap map;
    private List<Marker> markers;
    private List<Marker> unvisited;

    public FlyoverRunnable(GoogleMap map, List<Marker> markers) {
        this.map = map;
        this.markers = markers;
        this.unvisited = Lists.newArrayList(this.markers);
    }
    @Override
    public void run() {
        // Just in case some animation is currently happening
        map.stopAnimation();
        // Close all open marker infoWindows
        this.markers.forEach(Marker::hideInfoWindow);
        // Get next marker
        Marker target = getNextMarker();
        // Start animation
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(target.getPosition(), 10), 1000, new FlyoverCallback(map, target));

    }

    private Marker getNextMarker() {
        if (unvisited.isEmpty()) {
            this.unvisited = Lists.newArrayList(this.markers);
        }
        Random random = new Random();
        int randomIndex = random.ints(0, unvisited.size() - 1).findFirst().orElse(0);
        Marker nextTarget = unvisited.get(randomIndex);
        unvisited.remove(nextTarget);
        return nextTarget;
    }
}
