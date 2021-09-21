package com.agung.gisandroid.geojsonview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.agung.gisandroid.R;

import java.util.ArrayList;

import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;

public class GeojsonviewActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private static final int DEFAULT_LAYER_COLOR = Color.argb(255, 0, 0, 0);
    private View mapPickerView;
    private View colorPickedView;
    private TextView openWithTextView;
    private ArrayList<String> fileUris = new ArrayList<>();
    private ArrayList<Integer> layerColors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geojsonview);

        colorPickedView = findViewById(R.id.activity_main_color_picked);
        openWithTextView = (TextView) findViewById(R.id.activity_main_text_open_with);
        mapPickerView = findViewById(R.id.activity_main_layout_map_picker);
        colorPickedView.setBackgroundColor(DEFAULT_LAYER_COLOR);

        checkPermissions();
        
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Toast.makeText(getApplicationContext(), R.string.activity_geojsonview_ask_permission, Toast.LENGTH_LONG).show();
                askPermissions();
            } else {
                askPermissions();
            }
        }
    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }



    public void openGoogleMaps(View view)
        { openMap(googlemapsActivity.class);}

    public void openFilePicker(View v) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/octet-stream");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    private void openMap(Class<googlemapsActivity> googlemapsActivityClass) {
        if (fileUris != null) {
            Intent mapsIntent = new Intent(this,googlemapsActivityClass);
            mapsIntent.putStringArrayListExtra(GeoJsonViewerConstants.INTENT_EXTRA_JSON_URI, fileUris);
            mapsIntent.putIntegerArrayListExtra(GeoJsonViewerConstants.INTENT_EXTRA_JSON_COLORS, layerColors);
            startActivity(mapsIntent);
        } else {
            Toast.makeText(getApplicationContext(), R.string.geojson_opener_unable_to_read, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void onLayerColorPressed(View view) {
        new ChromaDialog.Builder()
                .initialColor(layerColors.get(layerColors.size()-1))
                .colorMode(ColorMode.RGB)
                .onColorSelected(new ColorSelectListener() {
                    @Override
                    public void onColorSelected(int i) {
                        if (layerColors.size()!=0){
                            layerColors.add(fileUris.size()-1, i);
                            colorPickedView.setBackgroundColor(i);
                        }
                    }
                })
                .create()
                .show(getSupportFragmentManager(), "ChromaDialog");
    }

    public void onBackPressed(View view) {
        if (mapPickerView.getVisibility() == View.VISIBLE) {
            mapPickerView.setVisibility(View.INVISIBLE);
            fileUris = new ArrayList<>();
            layerColors = new ArrayList<>();

            finish();
        }
    }

    @Override
    public void onBackPressed() { onBackPressed(null);}

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                fileUris.add(resultData.getData().toString());
                if (fileUris.size() > 1) {
                    String openWithString = fileUris.size() + " " + getString(R.string.activity_geojsonview_open_multiple);
                    openWithTextView.setText(openWithString);
                }
                colorPickedView.setBackgroundColor(DEFAULT_LAYER_COLOR);
                mapPickerView.setVisibility(View.VISIBLE);

                layerColors.add(fileUris.size() - 1, DEFAULT_LAYER_COLOR);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission granted

                } else {
                    finish();
                }
                return;
            }
        }
    }

    public void onAddJsonPressed(View view) {
        openFilePicker(view);
    }
}