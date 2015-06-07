package com.example.kudotoshiya.mapapp.Activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.kudotoshiya.mapapp.BusHolder;
import com.example.kudotoshiya.mapapp.GoogleGeoCodeResponse;
import com.example.kudotoshiya.mapapp.R;
import com.example.kudotoshiya.mapapp.Fragment.StoreSearchFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private MarkerOptions option;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        View container = LayoutInflater.from(this).inflate(R.layout.toolbar_store_view, toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.addView(container, lp);


        final SupportMapFragment fragment = SupportMapFragment.newInstance();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container,fragment,"map");
        transaction.commit();

        searchView = (SearchView)container.findViewById(R.id.action_search);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    if (manager.findFragmentByTag("list") == null) {
                        StoreSearchFragment mapFragment = StoreSearchFragment.newInstance(null, null);
                        transaction.add(R.id.container, mapFragment, "list");
                        transaction.hide(manager.findFragmentByTag("map"));
                        transaction.commit();
                    }else {
                        transaction.show(manager.findFragmentByTag("list"));
                        transaction.hide(manager.findFragmentByTag("map"));
                        transaction.commit();
                    }

                }else{
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        BusHolder.get().register(this);
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusHolder.get().unregister(this);
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentByTag("map"))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }

    }

    @Subscribe
    public void subscribe(GoogleGeoCodeResponse result) {

        double lat = result.results[0].geometry.location.lat;
        double lng = result.results[0].geometry.location.lng;

        searchView.clearFocus();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(manager.findFragmentByTag("list"));
        transaction.show(manager.findFragmentByTag("map"));
        transaction.commit();
        CameraPosition camerapos = new CameraPosition.Builder()
                .target(new LatLng(lat, lng)).zoom(15.5f).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camerapos));

        option.position(new LatLng(lat, lng));
        mMap.addMarker(option);

    }

    //マップ初期設定
    private void setUpMap() {
        //マーカーに使う画像
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.komeda);

        option = new MarkerOptions();

        mMap.setMyLocationEnabled(true);

        //LocationManagerの取得
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        //GPSから現在地の情報を取得
        Location myLocate = locationManager.getLastKnownLocation("gps");

        LatLng latLng =new LatLng(myLocate.getLatitude(), myLocate.getLongitude());

        option.position(latLng);

        option.icon(icon);

        mMap.addMarker(option);
        // 現在地ボタンをセット

        CameraPosition camerapos = new CameraPosition.Builder()
                .target(latLng).zoom(15.5f).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camerapos));
    }

}
