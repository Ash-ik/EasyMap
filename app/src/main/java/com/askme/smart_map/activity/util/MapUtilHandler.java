package com.askme.smart_map.activity.util;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Fahim Al Mahmud on 6/14/2016.
 */
public class MapUtilHandler {
    Context context;
    GoogleMap googleMap;
    Circle circle;
    public MapUtilHandler(Context context, GoogleMap googleMap) {
        this.context = context;
        this.googleMap = googleMap;
    }


    public void animateCamera(LatLng location, float zoomFrom, float zoomTo) {

        CameraPosition position = CameraPosition.builder()
                .target(location)
                .zoom(zoomFrom)
                .bearing(0.0f)
                .tilt(60)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoomTo), 2000, null);

    }

    public void circleAnimation(LatLng location,int color, final int radious, int duration) {
        circle = googleMap.addCircle(
                new CircleOptions()
                        .center(location)
                        .strokeColor(Color.BLUE)
                        .radius(radious)
                        .strokeWidth(8));

        ValueAnimator vAnimator = new ValueAnimator();
        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vAnimator.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
        vAnimator.setIntValues(0, 100);
        vAnimator.setDuration(duration);
        vAnimator.setEvaluator(new IntEvaluator());
        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                // Log.e("", "" + animatedFraction);
                circle.setRadius(animatedFraction * radious);
            }
        });
        vAnimator.start();
    }

}
