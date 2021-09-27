package com.agung.gisandroid.peta;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.agung.gisandroid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPolygonStyle;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class intersection extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapsjagung, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-0.610663, 117.188838), 12));
            callGeoJson();
        }


        private void callGeoJson() {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://skripsi.sigpalaran.com/intersection", new AsyncHttpResponseHandler() {

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
                      //  String luas = geoJsonData.getString("luas");

                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON");
                    }


                    assert geoJsonData != null;
                    GeoJsonLayer layer = new GeoJsonLayer(mMap, geoJsonData);
                    GeoJsonPolygonStyle sawah = new GeoJsonPolygonStyle();
                    sawah.setFillColor(Color.DKGRAY);
                    sawah.setStrokeColor(Color.BLACK);
                    sawah.setStrokeWidth(1);

                    GeoJsonPolygonStyle jagung = new GeoJsonPolygonStyle();
                    jagung.setFillColor(Color.parseColor(getString(R.string.englan_style)));
                    jagung.setStrokeColor(Color.parseColor(getString(R.string.englan_stylestook)));
                    jagung.setStrokeWidth(1);

                    GeoJsonPolygonStyle padiladang = new GeoJsonPolygonStyle();
                    padiladang.setFillColor(Color.BLUE);
                    padiladang.setStrokeColor(Color.BLUE);
                    padiladang.setStrokeWidth(1);

                    for (GeoJsonFeature feature : layer.getFeatures()) {
                        if (feature.getProperty("kelurahan1").equals("sawah")) {
                            feature.setPolygonStyle(sawah);
                        }
                    }

                    for (GeoJsonFeature feature : layer.getFeatures()) {
                        if (feature.getProperty("kelurahan1").equals("jagung")) {
                            feature.setPolygonStyle(jagung);
                        }
                    }

                    for (GeoJsonFeature feature : layer.getFeatures()) {
                        if (feature.getProperty("kelurahan1").equals("padi_ladang")) {
                            feature.setPolygonStyle(padiladang);
                        }
                    }

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