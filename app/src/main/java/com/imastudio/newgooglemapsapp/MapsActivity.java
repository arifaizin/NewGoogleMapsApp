package com.imastudio.newgooglemapsapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.imastudio.newgooglemapsapp.drawroutemap.DrawRouteMaps;
import com.imastudio.newgooglemapsapp.response.ResponseMaps;
import com.imastudio.newgooglemapsapp.response.ResultsItem;
import com.imastudio.newgooglemapsapp.retrofit.ApiConfig;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE = 1;
    private GoogleMap mMap;

    private FusedLocationProviderClient locationProviderClient;
    private LatLng lokasiku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("0BD941FD184E70CE2B1871ED82A7C2C6")
                .build();
        mAdView.loadAd(adRequest);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //setting
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true);

        }

//         Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //todo: 1 bikin marker
        LatLng imastudio = new LatLng(-7.0539367,110.4318731);
        mMap.addMarker(new MarkerOptions()
                .position(imastudio)
                .title("Imastudio")
                .snippet("ini snippet")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
        );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(imastudio, 15));

        locationProviderClient.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    double latitude = location.getLatitude();
                    double longitude  = location.getLongitude();
                    String lokasikita = latitude+","+longitude;
                    getDataOnline(lokasikita);


                    lokasiku = new LatLng(latitude, longitude);
                    String alamat = convertAddress(latitude, longitude);
                    LatLng imastudio = new LatLng(latitude,longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(imastudio)
                            .title("My Location")
                            .snippet(alamat)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(imastudio, 15));

                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                TextView tvNama = v.findViewById(R.id.tvnama);
                TextView tvalamat = v.findViewById(R.id.tvalamat);
                ImageView ivFoto = v.findViewById(R.id.ivFoto);

                tvNama.setText(marker.getTitle());
                tvalamat.setText(marker.getSnippet());
                if (marker.getTag() != null) {
//                    Glide.with(MapsActivity.this).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + marker.getTag() + "&key=" + getString(R.string.google_maps_key));

                    Glide.with(MapsActivity.this).load(marker.getTag());
                }
                return v;

            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "marker klik", Toast.LENGTH_SHORT).show();
                LatLng imastudio = new LatLng(-7.0539367,110.4318731);
                DrawRouteMaps.getInstance(MapsActivity.this).draw(lokasiku, imastudio, mMap);
//                LatLngBounds bounds = new LatLngBounds.Builder()
//                        .include(lokasiku)
//                        .include(imastudio).build();
//                int width = getResources().getDisplayMetrics().widthPixels;
//                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
//                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, 200, padding));
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "info klik", Toast.LENGTH_SHORT).show();
                LatLng imastudio = new LatLng(-7.0539367,110.4318731);
                DrawRouteMaps.getInstance(MapsActivity.this).draw(lokasiku, marker.getPosition(), mMap);
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(lokasiku)
                        .include(imastudio).build();
                int width = getResources().getDisplayMetrics().widthPixels;
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, 200, padding));
            }
        });
    }




    private void getDataOnline(final String lokasikita) {
        Call<ResponseMaps> rekues = ApiConfig.getApiService().getDataMaps(lokasikita, getString(R.string.google_maps_key));
        rekues.enqueue(new Callback<ResponseMaps>() {
            @Override
            public void onResponse(Call<ResponseMaps> call, Response<ResponseMaps> response) {
                if (response.isSuccessful()){
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        ResultsItem item = response.body().getResults().get(i);
                        Double latitude = item.getGeometry().getLocation().getLat();
                        Double longitude = item.getGeometry().getLocation().getLng();
                        LatLng lokasi = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions()
                                .position(lokasi)
                                .title(item.getName())
                                .snippet(item.getVicinity())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))

//                                .setTag(item.getPhotos().get(0).getPhotoReference());
                                .setTag(item.getIcon());


                                 builder.include(lokasi);

                    }

                    LatLngBounds bounds = builder.build();

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int padding = (int) (width*0.2);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                }
            }

            @Override
            public void onFailure(Call<ResponseMaps> call, Throwable t) {

            }
        });
    }


    private String convertAddress(Double latitude, Double longitude){
        Geocoder geo = new Geocoder(MapsActivity.this, Locale.getDefault());
        try{
            List<Address> list = geo.getFromLocation(latitude, longitude, 1);
            String alamat = list.get(0).getAddressLine(0)+","+list.get(0).getCountryName();
            return alamat;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
