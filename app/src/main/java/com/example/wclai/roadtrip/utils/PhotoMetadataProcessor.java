package com.example.wclai.roadtrip.utils;

import android.content.Context;
import android.util.Log;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PhotoMetadataProcessor {

    private static List<String> photoExtensions = Lists.newArrayList("jpeg", "jpg", "gif", "bmp", "png");

    public static Map<LatLng, File> getPhotoLocations(File baseDir, Context context) throws ImageProcessingException, IOException {
        Map locations = Maps.newHashMap();

        for (File file : baseDir.listFiles()) {
            if (photoExtensions.contains(Files.getFileExtension(file.getName()))) {
                Metadata metadata = ImageMetadataReader.readMetadata(file);
                // See whether it has GPS data
                Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
                for (GpsDirectory gpsDirectory : gpsDirectories) {
                    // Try to read out the location, making sure it's non-zero
                    GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                    if (geoLocation != null && !geoLocation.isZero()) {
                        // Add to our collection for use below
                        locations.put(new LatLng(geoLocation.getLatitude(), geoLocation.getLongitude()), file);
                        break;
                    }
                }
            }
        }

        return locations;
    }
}
