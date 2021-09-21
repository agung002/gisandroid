package com.agung.gisandroid.geojsonview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.agung.gisandroid.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class googlemapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<String> jsonUriStrings;
    private final ArrayList<Uri> jsonUris = new ArrayList<>();
    private ArrayList<Integer> jsonColors = new ArrayList<>();
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getIntentExtras(getIntent());
        setContentView(R.layout.activity_googlemaps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapgoogle);
        mapFragment.getMapAsync(this);
    }

    private void getIntentExtras(Intent intent) {
        context = getApplicationContext();
        jsonUriStrings = intent.getStringArrayListExtra(GeoJsonViewerConstants.INTENT_EXTRA_JSON_URI);
        jsonColors = intent.getIntegerArrayListExtra(GeoJsonViewerConstants.INTENT_EXTRA_JSON_COLORS);

        for (String uri:jsonUriStrings){
            jsonUris.add(Uri.parse(uri));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            GeoJsonify.geoJsonifyMap(googleMap,
                    this.getJsonUris(),
                    this.getJsonColors(),
                    this.getContext());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(), R.string.geojson_opener_unable_to_read, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(), R.string.geojson_opener_unable_to_parse, Toast.LENGTH_SHORT).show();
        }
    }
    public ArrayList<String> getJsonUriStrings() {
        return jsonUriStrings;
    }

    public ArrayList<Uri> getJsonUris() {
        return jsonUris;
    }

    public ArrayList<Integer> getJsonColors() {
        return jsonColors;
    }

    public Context getContext() {
        return context;
    }
}