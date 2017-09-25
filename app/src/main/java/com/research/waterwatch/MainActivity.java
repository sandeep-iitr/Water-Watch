package com.research.waterwatch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class MainActivity extends ActionBarActivity {

 Context cnt=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button map_places = (Button)findViewById(R.id.map_places);
         cnt=this;
        map_places.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Toast.makeText(cnt, "Loading places " ,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(cnt, MapsPlacesActivity.class);
                startActivity(intent);
            }
        });

        Button list_places = (Button)findViewById(R.id.list_places);

        list_places.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Toast.makeText(cnt, "Loading places " ,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(cnt, List_places.class);
                startActivity(intent);
            }
        });


        Button test = (Button)findViewById(R.id.test_graph);
        test.setVisibility(View.GONE);
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Perform action on click */
                if (cnt != null)
                {
                    Intent intent = new Intent(cnt, Graph_vis.class);
                    startActivity(intent);
                }

                //  Toast.makeText(cnt, "Loading places from cloud ", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
