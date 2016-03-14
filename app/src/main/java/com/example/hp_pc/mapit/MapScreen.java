package com.example.hp_pc.mapit;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;


public class MapScreen extends FragmentActivity implements
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener

{
    protected Location mLastLocation;


    final int RQS_GooglePlayServices = 1;
    GoogleMap mymap;
    Location myLocation;
    TextView tvlocinfo;

    boolean markerClicked;
    PolylineOptions rectOptions;
    Polyline polyline;
    SearchView searchView;
    ImageButton closebutton;
    Marker marker;
    int x;
    int y;
    MotionEvent event;
    LatLng latLng;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),AfterSignUp.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);

        FragmentManager myfragmentManager = getFragmentManager();
        MapFragment mymapFragment = (MapFragment) myfragmentManager.findFragmentById(R.id.fragment);

        // Getting a reference to the map
        mymap = mymapFragment.getMap();

        mymap.setMyLocationEnabled(true);
        mymap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mymap.setOnMapClickListener(this);
        mymap.setOnMarkerClickListener(this);
        mymap.setOnMapLongClickListener(this);
        mymap.setOnInfoWindowClickListener(this);
        markerClicked = false;
        mymap.getUiSettings().setMyLocationButtonEnabled(true);
       /* // Defining button click event listener for the find button
        OnClickListener findClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location
                EditText etLocation = (EditText) findViewById(R.id.et_location);

                // Getting user input location
                String location = etLocation.getText().toString();

                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        };
        searchView.setOnClickListener(findClickListener);*/
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etLocation = (EditText) findViewById(R.id.et_location);
                String location = etLocation.getText().toString();
                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        });

        MainDbAdapter mainDbAdapter = new MainDbAdapter(getApplicationContext());
        Cursor location_cursor = mainDbAdapter.showLocations();

        while (location_cursor.moveToNext()) {
            String lati = location_cursor.getString(location_cursor.getColumnIndex(MainDbAdapter.DbAdapter.Lat));
            String longi = location_cursor.getString(location_cursor.getColumnIndex(MainDbAdapter.DbAdapter.Lon));
            double lat = Double.parseDouble(lati);
            double lon = Double.parseDouble(longi);
            latLng = new LatLng(lat, lon);
            mymap.addMarker(new MarkerOptions().position(latLng).title("Click to add ")).showInfoWindow();

        }
        //   searchView = (SearchView) findViewById(R.id.searchView);
        // tvlocinfo = (TextView) findViewById(R.id.locinfo);


//common
        //  SharedPreferences sharedPreference = getSharedPreferences("locations", MODE_APPEND);
//write
        //SharedPreferences.Editor editor = sharedPreference.edit();

//        editor.putLong("key1", 212121);
        //      editor.commit();

//read
        //  long a=savedInstanceState.getLong("key1");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapClick(LatLng victoria) {
        //victoria = new LatLng(22.544872, 88.342515);
        mymap.animateCamera(CameraUpdateFactory.newLatLng(victoria));
        // Toast.makeText(getApplicationContext(), (int) victoria.latitude,Toast.LENGTH_LONG);
        markerClicked = false;
    }

    String lat;
    String lon;

    @Override
    public void onMapLongClick(LatLng latLng) {


        mymap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                String lat = String.valueOf(latLng.latitude);
                String lon = String.valueOf(latLng.longitude);

                marker = mymap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).visible(true).title("Click to Edit"));
                MainDbAdapter mainDbAdapter = new MainDbAdapter(getApplicationContext());
                long loc = mainDbAdapter.storeLocations(lat, lon);
                if (loc > 0) {
                    Toast.makeText(getApplicationContext(), "Location Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Location Save failed", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(getApplicationContext(), AfterInfoClick.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                startActivity(intent);
            }
        });
        /*Location location=new Location(myLocation);
        location.setLatitude(victoria.latitude);
        location.setLongitude(victoria.longitude);
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        Toast.makeText(getApplicationContext(), (int) victoria.latitude,Toast.LENGTH_SHORT);
        //Creating a Marker
        MarkerOptions markerOptions = new MarkerOptions();

        //Placing a marker on touched location
       marker = mymap.addMarker(markerOptions.position(latLng).title("Click to Add Photos,Date and A Review"));


        marker.showInfoWindow();
   */
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        marker.showInfoWindow();

      /*  if (markerClicked) {
            if (polyline != null) {
                // polyline.remove();
                // polyline=null;
            }
            rectOptions.add(marker.getPosition());
            rectOptions.color(Color.GREEN);
            polyline = mymap.addPolyline(rectOptions);
        } else {
            if (polyline != null) {
                // polyline.remove();
                //  polyline=null;
            }
            rectOptions = new PolylineOptions().add(marker.getPosition());
            markerClicked = true;
        }
        */
        return true;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        mymap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                LatLng latLng1 = marker.getPosition();
                String lat = String.valueOf(latLng1.latitude);
                String lon = String.valueOf(latLng1.longitude);
                Intent intent = new Intent(getApplicationContext(), OnInfoClick.class);
                intent.putExtra("lat1", lat);
                intent.putExtra("lon1", lon);
                startActivity(intent);



            }
        });

    }
/*
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }*/
}
// An AsyncTask class for accessing the GeoCoding Web Service
// An AsyncTask class for accessing the GeoCoding Web Service
class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
    private Context mContext;
    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;

    public GeocoderTask(Context context) {
        super();
        mContext = context;
    }

    public GeocoderTask() {

    }

    @Override
    protected List<Address> doInBackground(String... locationName) {
        // Creating an instance of Geocoder class
        Geocoder geocoder = new Geocoder(mContext);
        List<Address> addresses = null;

        try {
            // Getting a maximum of 3 Address that matches the input text
            addresses = geocoder.getFromLocationName(locationName[0], 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }
    @Override
    protected void onPostExecute(List<Address> addresses) {

        if(addresses==null || addresses.size()==0){
            Toast.makeText(mContext, "No Location found", Toast.LENGTH_SHORT).show();
        }
        // Clears all the existing markers on the map
        googleMap.clear();
        // Adding Markers on Google Map for each matching address
        for(int i=0;i<addresses.size();i++){

            Address address = (Address) addresses.get(i);

            // Creating an instance of GeoPoint, to display in Google Map
            latLng = new LatLng(address.getLatitude(), address.getLongitude());

            String addressText = String.format("%s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getCountryName());

            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(addressText);

            googleMap.addMarker(markerOptions);

            // Locate the first location
            if(i==0)
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }

}
}

