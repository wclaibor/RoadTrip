package com.example.wclai.roadtrip.dao;

import android.content.Context;
import android.os.Environment;

import com.drew.imaging.ImageProcessingException;
import com.example.wclai.roadtrip.utils.PhotoMetadataProcessor;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PhotoDao {

    public Map<LatLng, File> getPhotos(Context context) {

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        Map<LatLng, File> photos = Maps.newHashMap();

        try {
            photos = PhotoMetadataProcessor.getPhotoLocations(pictureDirectory, context);
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photos;
    }
}
