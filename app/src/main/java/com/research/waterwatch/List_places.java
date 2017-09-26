package com.research.waterwatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class List_places extends Activity {

    ListView listView ;
   Context cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cnt=this;

        //setting refer, we will go from this activity to other activities
        GlobalData.refer=2;

        //checking the internet connection first then proceeding
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false

        //loading data of places using the REST API: place Names and Limits are loaded
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



        }//end if
        else if(GlobalData.numPlaces>0)//places are already loaded
        {

            //sort places, alphabetically
            if (GlobalData.place.size() > 0) {
                Collections.sort(GlobalData.place, new Comparator<Places>() {
                    @Override
                    public int compare(final Places P1, final Places P2) {
                        return P1.Place_name.toUpperCase().compareTo(P2.Place_name.toUpperCase());
                    }
                });
            }
            //end sort places

            setContentView(R.layout.activity_list_places);
            listView = (ListView) findViewById(R.id.list_of_places);
            //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
            ArrayList<String> listItems = new ArrayList<String>();
            //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
            ArrayAdapter<String> adapter;

            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1,
                    listItems);

            // Assign adapter to ListView
            listView.setAdapter(adapter);
            AddListItems(listItems);
            adapter.notifyDataSetChanged();
            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item index
                    int itemPosition = position;
                    // ListView Clicked item value
                    String itemValue = (String) listView.getItemAtPosition(position);
                    // Show Alert
                   // Toast.makeText(cnt,"Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();
                    //GlobalData.current_marker = itemValue;
                    //GlobalData.place_position=itemPosition;

                    Places p =GlobalData.place.get(itemPosition);
                    GlobalData.current_Lat=p.Lat;
                    GlobalData.current_Lng=p.Lng;
                    GlobalData.current_marker = p.Place_name.toUpperCase();
                    GlobalData.current_range=p.range;


                    Intent intent = new Intent(getApplicationContext(), PlacesData.class);
                    startActivity(intent);

                }

            });

        }//end else if(GlobalData.numPlaces>0)

        else//there is no internet connection
        {
            Toast.makeText(cnt, "No, internet connection, please connect.", Toast.LENGTH_SHORT).show();

             //going back to main activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

    }


    @Override
    public void onBackPressed()
    {

        //going back to main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        super.onBackPressed();  // optional depending on your needs
    }


    void AddListItems(ArrayList<String> listItems)
    {


        //adding markers of all places present in the global file
        for (Places P : GlobalData.place) {


            if(P.type.equals("manual"))//only manual places are added, auto places don't have name, so not displayed in list view
            {
                String padding = "";
                if (P.data_type == 1)
                    padding = " (*)";
                else if (P.data_type == 2)
                    padding = " (+)";

                listItems.add(P.Place_name.toUpperCase() + padding);
            }//end if

        }//end for
    }


    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {

            //Downloading Plces
            Download_Places DP= new Download_Places();
            DP.download();

            //Downloading Limits
            Download_Limits LD=new Download_Limits();
            LD.download();

            long totalSize = 0;
            return totalSize;
        }//end function

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            // showDialog("Downloaded " + result + " bytes");

           // Intent intent = new Intent(cnt, List_places.class);
            //startActivity(intent);
            setContentView(R.layout.activity_list_places);

            //sort places, alphabetically
            if (GlobalData.place.size() > 0) {
                Collections.sort(GlobalData.place, new Comparator<Places>() {
                    @Override
                    public int compare(final Places P1, final Places P2) {
                        return P1.Place_name.toUpperCase().compareTo(P2.Place_name.toUpperCase());
                    }
                });
            }
            //end sort places


            listView = (ListView) findViewById(R.id.list_of_places);
            //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
            ArrayList<String> listItems = new ArrayList<String>();
            //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
            ArrayAdapter<String> adapter;

            adapter = new ArrayAdapter<String>(cnt,
                    android.R.layout.simple_list_item_1, android.R.id.text1,
                    listItems);

            // Assign adapter to ListView
            listView.setAdapter(adapter);
            AddListItems(listItems);
            adapter.notifyDataSetChanged();
            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item index
                    int itemPosition = position;
                    // ListView Clicked item value
                    String itemValue = (String) listView.getItemAtPosition(position);
                    // Show Alert
                   // Toast.makeText(cnt,"Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();
                    //GlobalData.current_marker = itemValue;

                    Places p =GlobalData.place.get(itemPosition);
                    GlobalData.current_Lat=p.Lat;
                    GlobalData.current_Lng=p.Lng;
                    GlobalData.current_marker = p.Place_name.toUpperCase();
                    GlobalData.current_range=p.range;

                    Intent intent = new Intent(getApplicationContext(), PlacesData.class);
                    startActivity(intent);


                }

            });
        }//end onpost
    }


}
