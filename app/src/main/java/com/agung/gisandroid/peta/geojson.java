package com.agung.gisandroid.peta;

import android.os.AsyncTask;

import com.agung.gisandroid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geojson.GeoJsonLayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class geojson extends PetaActivity    {


    protected int getLayoutId() {
        return R.layout.activity_peta;
    }
    @Override
    protected void startDemo(boolean isRestore) {
        if (!isRestore) {
            getMap().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(117.13881944965522, -0.5265873792000374)));
        }
        // Download the GeoJSON file.
        retrieveFileFromUrl();
        // Alternate approach of loading a local GeoJSON file.
        //retrieveFileFromResource();
    }

    private void retrieveFileFromUrl() {
        new DownloadGeoJsonFile().execute(getString(R.string.geojson_url));
    }

    private class DownloadGeoJsonFile extends AsyncTask<String, Void, GeoJsonLayer> {
        @Override
        protected GeoJsonLayer doInBackground(String... params) {
            try {
                // Open a stream from the URL
                InputStream stream = new URL(params[0]).openStream();

                String line;
                StringBuilder result = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                while ((line = reader.readLine()) != null) {
                    // Read and save each line of the stream
                    result.append(line);
                }

                // Close the stream
                reader.close();
                stream.close();

                return new GeoJsonLayer(getMap(), new JSONObject(result.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                
            }
            return null;
        }

        @Override
        protected void onPostExecute(GeoJsonLayer layer) {
            if (layer != null) {
                addGeoJsonLayerToMap(layer);
            }
        }
    }

    private void addGeoJsonLayerToMap(GeoJsonLayer layer) {
        layer.addLayerToMap();
    }
}
