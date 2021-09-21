package com.agung.gisandroid.peta;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.agung.gisandroid.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public abstract class PetaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mIsRestore;

    protected int getLayoutId() {
        return R.layout.activity_peta;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsRestore = savedInstanceState != null;
        setContentView(getLayoutId());
        setUpMap();
    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap map) {

        if (mMap != null) {
            return;
        }
        mMap = map;
        startDemo(mIsRestore);
    }

    protected abstract void startDemo(boolean mIsRestore);

    protected GoogleMap getMap() {
        return mMap;
    }

}    

 
