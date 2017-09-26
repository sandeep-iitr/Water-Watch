package com.research.waterwatch;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MapsPlacesActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Context cnt;

    private Map<Marker, Places> allMarkersMap = new HashMap<Marker, Places>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cnt=this;

        //setting refer, we will go from this activity to other activities
        GlobalData.refer=1;

        //checking the internet connection first then proceeding
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false

        //loading data of places from json file
        if(GlobalData.numPlaces==0&&isInternetPresent)//read data if not read already
        {
            RelativeLayout layout = new RelativeLayout(this);
            ProgressBar progressBar = new ProgressBar(cnt,null,android.R.attr.progressBarStyleLarge);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.addView(progressBar,params);
            setContentView(layout);
            DownloadFilesTask dw=new DownloadFilesTask();
            dw.execute();

        }

        else if(GlobalData.numPlaces>0)//internet may or may not present, it is not necessary
        {
            setContentView(R.layout.activity_maps_places);
            setUpMapIfNeeded();
        }

        else//there is no internet connection
        {
            Toast.makeText(cnt, "No, internet connection, please connect.", Toast.LENGTH_SHORT).show();

            //going back to main activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }
    }






    @Override
    protected void onResume() {
        super.onResume();
     //   setUpMapIfNeeded();
    }

    @Override
    public void onBackPressed()
    {

        //going back to main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        super.onBackPressed();  // optional depending on your needs
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
       // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        mMap.setMyLocationEnabled(true); // false to disable..showing user current loc
        mMap.getUiSettings().setMyLocationButtonEnabled(true);//My location button will be used to move map to your current location

        //moving cameras of map to first place

        if(GlobalData.place.size()>0)
        {
            LatLng place = new LatLng(Double.parseDouble(GlobalData.place.get(0).Lat),Double.parseDouble(GlobalData.place.get(0).Lng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.latitude, place.longitude), 6));
        }//end if

        //adding markers of all places present in the global file
        for (Places P : GlobalData.place) {
            LatLng place = new LatLng(Double.parseDouble(P.Lat),Double.parseDouble(P.Lng));
            Marker marker1=mMap.addMarker(new MarkerOptions().position(place).title(P.Place_name));
            allMarkersMap.put(marker1, P);


        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                    Toast.makeText(MapsPlacesActivity.this, arg0.getTitle()+" Data is loading", Toast.LENGTH_SHORT).show();// display toast
                    GlobalData.current_marker=arg0.getTitle();
                    LatLng L =arg0.getPosition();

                     Places p = allMarkersMap.get(arg0);

                    GlobalData.current_Lat=Double.toString(L.latitude);
                    GlobalData.current_Lng=Double.toString(L.longitude);
                    GlobalData.current_range=p.range;



                Intent intent = new Intent(MapsPlacesActivity.this,PlacesData.class);
                startActivity(intent);
                return true;
            }

        });

    }


    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            //  int count = urls.length;
            long totalSize = 0;

            //Downloading Plces
            Download_Places DP= new Download_Places();
            DP.download();

            //Downloading Limits
            Download_Limits LD=new Download_Limits();
            LD.download();

            return totalSize;
        }//end function

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            // showDialog("Downloaded " + result + " bytes");

            setContentView(R.layout.activity_maps_places);
            setUpMapIfNeeded();
        }
    }



}
