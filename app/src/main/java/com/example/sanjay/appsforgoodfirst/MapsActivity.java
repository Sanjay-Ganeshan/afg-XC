package com.example.sanjay.appsforgoodfirst;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrLocation;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        buildGoogleApiClient();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("YOU ARE HERE"));
        mMap.addPolyline(new PolylineOptions().add(new LatLng(42.275863,-71.799527),new LatLng(42.276752,-71.799581),new LatLng(42.276688,-71.798755),new LatLng(42.276268,-71.798755),new LatLng(42.274608,-71.798090),new LatLng(42.274370,-71.798261),new LatLng(42.274251,-71.799216),new LatLng(42.275863,-71.799527)).width(5));
        mMap.setMyLocationEnabled(true);
    }
    private void zoomToCurrentLocation() {
        try {
            double lat = mCurrLocation.getLatitude();
            double lng = mCurrLocation.getLongitude();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 20));
        }
        catch(Exception e) {

        }

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
       //Intent intent = new Intent(MapsActivity.this,MainActivity.class);
       //startActivity(intent);
       System.out.println("OnConnected");
       if(mCurrLocation != null) {
           zoomToCurrentLocation();
       }
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("OnSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Intent intent = new Intent(MapsActivity.this,MainActivity.class);
        //startActivity(intent);
        System.out.println("OnConnectionFailed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrLocation = location;
        if (mCurrLocation != null)
            zoomToCurrentLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        System.out.println("OnStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        System.out.println("OnProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        System.out.println("OnProviderDisabled");
    }
}
