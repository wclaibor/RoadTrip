package com.example.wclai.roadtrip;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import android.text.SpannableString;

import java.io.File;
import java.util.Map;

class PhotoInfoWindow implements GoogleMap.InfoWindowAdapter {
    private Map<LatLng, File> locations;

    // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
    // "title" and "snippet".
    private final View mWindow;

    private final LayoutInflater inflater;

    public PhotoInfoWindow(Map<LatLng, File> locations, Context context) {
        this.locations = locations;

        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        mWindow = inflater.inflate(R.layout.custom_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void render(Marker marker, View view) {
        File locationPhoto = locations.get((LatLng) marker.getTag());

        Drawable d = Drawable.createFromPath(locationPhoto.getAbsolutePath());

        ((ImageView) view.findViewById(R.id.badge)).setImageDrawable(d);

        String title = marker.getTitle();
        TextView titleUi = view.findViewById(R.id.title);
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }
    }
}
