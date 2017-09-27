/*
This file is part of Water Watch.

    Water Watch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Water Watch  is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.

*/

/*
Authors:
Sandeep Singh Sandha
UCLA, https://sites.google.com/view/sandeep-/home

Biplav Srivastava
IBM Research
 */


package com.research.waterwatch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


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




    }



    void show_data_layout() {

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

            ScrollView S_4 = new ScrollView(cnt);

            LinearLayout layout = new LinearLayout(cnt);
            layout.setOrientation(LinearLayout.VERTICAL);
            // layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            lparams2.gravity = Gravity.CENTER;
            lparams.gravity=Gravity.FILL_VERTICAL;
            S_4.setLayoutParams(lparams2);
            //S_4.addView(layout);
            S_4.setScrollbarFadingEnabled(false);
            layout.setLayoutParams(lparams2);

            lparams.setMargins(30, 20, 30, 0);


            //explanation of WQE is displayed to user

            Button Close = new Button(cnt);
            Close.setId(190017);
            Close.setLayoutParams(lparams);
            Close.setText("Close");
            Close.setBackgroundColor(Color.parseColor("#FFDAB9"));
            layout.addView(Close);

            TextView WQE_exp = new TextView(cnt);//WQE explanation
            WQE_exp.setId(1090032);
            WQE_exp.setLayoutParams(lparams);
            WQE_exp.setText("Water Water uses BlueWater platform. BlueWater is developed for water data by IBM. " +
                    "The code of Water Watch is available open source on GIT under GNU General Public License." +
                    "Water Watch is developed by Sandeep (UCLA, PhD Student) and Biplav Srivastava (IBM Research). " +
                    "Please share feedback on sandha.iitr@gmail.com");
            WQE_exp.setTextSize(20);
            WQE_exp.setGravity(Gravity.CENTER);
            layout.addView(WQE_exp);


            //end explanation of WQE



            S_4.addView(layout);

            layout.setBackgroundResource(R.drawable.female1);
            final Dialog dialog = new Dialog(cnt);
            dialog.setContentView(S_4);
            dialog.setTitle("Credits");

            dialog.show();

            Close.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    dialog.hide();
                    show_data_layout();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
