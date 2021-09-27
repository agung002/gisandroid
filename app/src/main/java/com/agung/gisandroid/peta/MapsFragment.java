package com.agung.gisandroid.peta;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class MapsFragment extends Fragment implements OnMapReadyCallback {
   private GoogleMap mMap;
   TextView textView;


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
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-0.610663, 117.188838), 12));
            callGeoJson();
        }


    private void callGeoJson() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://skripsi.sigpalaran.com/sawah", new AsyncHttpResponseHandler() {

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
                    String luas = geoJsonData.getString("luas");

                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON");
                }


                assert geoJsonData != null;
                GeoJsonLayer layer = new GeoJsonLayer(mMap, geoJsonData);

                 GeoJsonPolygonStyle kosong = new GeoJsonPolygonStyle();
                kosong.setFillColor(Color.WHITE);
                kosong.setStrokeWidth(1);

                 GeoJsonPolygonStyle simpangpasir = new GeoJsonPolygonStyle();
                simpangpasir.setFillColor(Color.parseColor(getString(R.string.mpoly)));
                simpangpasir.setStrokeColor(Color.parseColor(getString(R.string.mpolystok)));
                simpangpasir.setStrokeWidth(1);

                 GeoJsonPolygonStyle handilbakti = new GeoJsonPolygonStyle();
                handilbakti.setFillColor(Color.parseColor(getString(R.string.englan_style)));
                handilbakti.setStrokeColor(Color.parseColor(getString(R.string.englan_stylestook)));
                handilbakti.setStrokeWidth(1);

                 GeoJsonPolygonStyle bantuas = new GeoJsonPolygonStyle();
                bantuas.setFillColor(Color.BLUE);
                bantuas.setStrokeColor(Color.BLUE);
                bantuas.setStrokeWidth(1);

                 GeoJsonPolygonStyle bukuan = new GeoJsonPolygonStyle();
                bukuan.setFillColor(Color.BLACK);
                bukuan.setStrokeColor(Color.BLACK);
                bukuan.setStrokeWidth(1);


                 GeoJsonPolygonStyle rawamakmur = new GeoJsonPolygonStyle();
                rawamakmur.setFillColor(Color.GREEN);
                rawamakmur.setStrokeColor(Color.GREEN);
                rawamakmur.setStrokeWidth(1);


                for (GeoJsonFeature feature : layer.getFeatures()) {
                    if (feature.getProperty("kelurahan").equals("Simpang Pasir")) {
                        feature.setPolygonStyle(simpangpasir);
                    }
                }

                for (GeoJsonFeature feature : layer.getFeatures()) {
                    if (feature.getProperty("kelurahan").equals("Handil Bakti")) {
                        feature.setPolygonStyle(handilbakti);
                    }
                }

                for (GeoJsonFeature feature : layer.getFeatures()) {
                    if (feature.getProperty("kelurahan").equals("Bantuas")) {
                        feature.setPolygonStyle(bantuas);
                    }
                }

                for (GeoJsonFeature feature : layer.getFeatures()) {
                    if (feature.getProperty("kelurahan").equals("Bukuan")) {
                        feature.setPolygonStyle(bukuan);
                    }
                }

                for (GeoJsonFeature feature : layer.getFeatures()) {
                    if (feature.getProperty("kelurahan").equals("kosong")) {
                        feature.setPolygonStyle(kosong);
                    }
                }

                for (GeoJsonFeature feature : layer.getFeatures()) {
                    if (feature.getProperty("kelurahan").equals("Rawa Makmur")) {
                        feature.setPolygonStyle(rawamakmur);
                    }
                }

                for (GeoJsonFeature geoJsonFeature : layer.getFeatures()){
                    if(geoJsonFeature.hasProperty("luas")){
                        String l =geoJsonFeature.getProperty("luas");
                    }
                }

                layer.addLayerToMap();
               // layer.setOnFeatureClickListener((GeoJsonLayer.GeoJsonOnFeatureClickListener)feature->
                       // Toast.makeText(getApplicationContext(),"GeoJSON polygon clicked:"+feature.getProperty("titel"),Toast.LENGTH_SHORT).show());

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
