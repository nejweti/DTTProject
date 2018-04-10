package com.example.user.dttproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.user.dttproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by user on 3/23/2018.
 */

public class customInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    Context mContext;
    View mView;

    public customInfoWindowAdapter(Context mContext) {
        this.mContext = mContext;

        mView = LayoutInflater.from(mContext).inflate(R.layout.snippet,null);

    }

    public void renderWindow(Marker marker, View view){
        TextView textView = view.findViewById(R.id.location);
        textView.setText(marker.getSnippet());

    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindow(marker,mView);
        return mView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindow(marker,mView);
        return mView;
    }
}
