package com.agung.gisandroid.peta;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.agung.gisandroid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Mapsjagung extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;


    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapsjagung, container, false);
    }

    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsc);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (mMap != null) {
            return;
        }
        mMap = map;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-0.5265873792000374, 117.13881944965522), 12));
        callGeoJson();
    }
    private void callGeoJson() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://skripsi.sigpalaran.com/jagung", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String s = new String(response);
                JSONObject geoJsonData = null;
                try {
                    geoJsonData = new JSONObject(s);
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON");
                }

                assert geoJsonData != null;
                GeoJsonLayer layer = new GeoJsonLayer(mMap, geoJsonData);

                layer.addLayerToMap();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable e) {

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

        });
    }
}
