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
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URL;
import java.util.ArrayList;

public class PlacesData extends ActionBarActivity {
    String nodata="No data";
    Context cnt;
    String[] Month_s = {"dummy","Jan", "Feb", "Mar", "Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_places_data2);
        cnt=this;

        //checking the internet connection first then proceeding
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false

        //read data of the place which is selected, only if internet is present
        if(isInternetPresent)
        {
            RelativeLayout layout = new RelativeLayout(this);
            ProgressBar progressBar = new ProgressBar(cnt, null, android.R.attr.progressBarStyleLarge);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.addView(progressBar, params);
            setContentView(layout);
            DownloadFilesTask dw = new DownloadFilesTask();
            dw.execute();
        }
        //end read data of the place which is selected
        else//there is not internet
        {

            Toast.makeText(cnt, "No, internet connection, please connect.", Toast.LENGTH_SHORT).show();

            if(GlobalData.refer==0)
            {//going back to main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            if(GlobalData.refer==1)
            {//going back to main activity
                Intent intent = new Intent(getApplicationContext(), MapsPlacesActivity.class);
                startActivity(intent);
            }

            if(GlobalData.refer==2)
            {//going back to main activity
                Intent intent = new Intent(getApplicationContext(), List_places.class);
                startActivity(intent);
            }

        }//end else
    }

    void show_data_layout() {

        //LinearLayout.LayoutParams (int width, int height, float weight)
      //add dynamic layout to show one most recent data reading
       final LinearLayout.LayoutParams Param_full = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

        LinearLayout P_1 = new LinearLayout(cnt);
        P_1.setOrientation(LinearLayout.VERTICAL);
        P_1.setLayoutParams(Param_full);


        LinearLayout.LayoutParams lp_2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 0);
        lp_2.setMargins(5, 5, 5, 5);
        lp_2.weight = 1;
        LinearLayout P_2 = new LinearLayout(cnt);
        P_2.setOrientation(LinearLayout.HORIZONTAL);
        P_2.setLayoutParams(lp_2);
        P_1.addView(P_2);

        //at p2 add  place name details
        P_2.setBackgroundResource(R.drawable.lightblue);
        LinearLayout.LayoutParams lpname = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
        lpname.weight=1;
        lpname.setMargins(2, 2, 2, 2);
        TextView pname = new TextView(cnt);
        pname.setText(GlobalData.current_marker+" ("+GlobalData.sensordata.size()+")");
        pname.setLayoutParams(lpname);
        pname.setTextSize(20);
        pname.setGravity(Gravity.CENTER);
        pname.setTextColor(Color.WHITE);
        P_2.addView(pname);

        //end at p2 place name details



        LinearLayout.LayoutParams lp_3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 0);
        LinearLayout.LayoutParams lsp_3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT );
        lp_3.setMargins(5, 5, 5, 5);
        lp_3.weight = 4;
        ScrollView S_3 = new ScrollView(cnt);
        S_3.setLayoutParams(lp_3);
        LinearLayout P_3 = new LinearLayout(cnt);
        P_3.setOrientation(LinearLayout.HORIZONTAL);
        P_3.setLayoutParams(lsp_3);
        S_3.setScrollbarFadingEnabled(false);
        S_3.addView(P_3);
        P_1.addView(S_3);


        LinearLayout.LayoutParams lp_4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 0);
        lp_4.setMargins(5, 5, 5, 5);
        lp_4.weight = 1;
        LinearLayout P_4 = new LinearLayout(cnt);
        P_4.setOrientation(LinearLayout.HORIZONTAL);
        P_4.setLayoutParams(lp_4);
        P_1.addView(P_4);

        LinearLayout.LayoutParams lp_5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 0);
        lp_5.setMargins(5, 5, 5, 5);
        lp_5.weight = 1;
        LinearLayout P_5 = new LinearLayout(cnt);
        P_5.setOrientation(LinearLayout.HORIZONTAL);
        P_5.setLayoutParams(lp_5);
        P_1.addView(P_5);

//LinearLayout.LayoutParams (int width, int height, float weight)
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
        lp.setMargins(5, 5, 5, 5);
        lp.weight = 1;
        LinearLayout P_3a = new LinearLayout(cnt);
        P_3a.setOrientation(LinearLayout.VERTICAL);
        P_3a.setLayoutParams(lp);
        LinearLayout P_3b = new LinearLayout(cnt);
        P_3b.setOrientation(LinearLayout.VERTICAL);
        P_3b.setLayoutParams(lp);
   //     LinearLayout P_3c = new LinearLayout(cnt);
   //    P_3c.setOrientation(LinearLayout.VERTICAL);
   //   P_3c.setLayoutParams(lp);

        P_3.addView(P_3a);
        P_3.addView(P_3b);
    //    P_3.addView(P_3c);

        //LinearLayout.LayoutParams (int width, int height, float weight)

      //  LinearLayout.LayoutParams Param_row = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 130);
        final float scale = getResources().getDisplayMetrics().density;
        int pixels_layout_Param_row = (int) (60 * scale + 0.5f);//multiple screens
        LinearLayout.LayoutParams Param_row = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, pixels_layout_Param_row);

        int pixels_layout_Param_row_wqe = (int) (80 * scale + 1f);//multiple screens
        LinearLayout.LayoutParams Param_row_wqe = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, pixels_layout_Param_row_wqe);

        int pixels_margin_Param_row = (int) (3 * scale + 0.5f);//multiple screens
        Param_row.setMargins(pixels_margin_Param_row,pixels_margin_Param_row,pixels_margin_Param_row,pixels_margin_Param_row);
        int pixels_text_size_Param_row = 20;//(int) (10 * scale + 0.5f);//multiple screens, size for the text data
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels_text_size_Param_row, getResources().getDisplayMetrics());
        //  Param_row.gravity= Gravity.CENTER;

        //adding last rows, date, map and places
        {
            LinearLayout.LayoutParams lt_5 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
            lt_5.weight = 1;
            lt_5.setMargins(2,2,2,2);
            final TextView Date = new TextView(cnt);
            Date.setText("Date");
            Date.setLayoutParams(lt_5);
            //pname.setLayoutParams(lpname);
            Date.setTextSize(20);
            Date.setGravity(Gravity.CENTER);
            Date.setTextColor(Color.WHITE);
            Date.setBackgroundResource(R.drawable.lightblue);
            //P_5.addView(Date);

            final TextView Place = new TextView(cnt);
            Place.setText("Places");
            Place.setLayoutParams(lt_5);
            //pname.setLayoutParams(lpname);
            Place.setTextSize(20);
            Place.setGravity(Gravity.CENTER);
            Place.setTextColor(Color.WHITE);
            Place.setBackgroundResource(R.drawable.lightblue);
            P_5.addView(Place);


            final TextView Map = new TextView(cnt);
            Map.setText("Map");
            //pname.setLayoutParams(lpname);
            Map.setLayoutParams(lt_5);
            Map.setTextSize(20);
            Map.setGravity(Gravity.CENTER);
            Map.setTextColor(Color.WHITE);
            Map.setBackgroundResource(R.drawable.lightblue);
            P_5.addView(Map);

            Map.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Intent intent = new Intent(cnt, MapsPlacesActivity.class);
                    startActivity(intent);

                }
            });

            Place.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Intent intent = new Intent(cnt, List_places.class);
                    startActivity(intent);

                }
            });

            Date.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click

                    LinearLayout layout = new LinearLayout(cnt);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    // layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lparams.gravity = Gravity.CENTER;
                    layout.setLayoutParams(lparams);

                    LinearLayout.LayoutParams ltext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ltext.setMargins(4, 4, 4, 4);

                    TextView Start_date = new TextView(cnt);
                    Start_date.setText("Start date");
                    //pname.setLayoutParams(lpname);
                    Start_date.setLayoutParams(ltext);
                    Start_date.setTextSize(20);
                    Start_date.setGravity(Gravity.CENTER);
                    Start_date.setTextColor(Color.WHITE);
                    Start_date.setBackgroundResource(R.drawable.lightblue);
                    layout.addView(Start_date);

                    Start_date.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Perform action on click
                            //DialogFragment newFragment = new SelectDateFragment();
                            //newFragment.show(getSupportFragmentManager(), "DatePicker");
                        }
                    });

                    if(GlobalData.start_Day!=-1)
                    {
                        TextView dStart_date = new TextView(cnt);
                        dStart_date.setText(GlobalData.start_Day+"/"+GlobalData.start_month+"/"+GlobalData.start_Year);
                        //pname.setLayoutParams(lpname);
                        dStart_date.setLayoutParams(ltext);
                        dStart_date.setTextSize(20);
                        dStart_date.setGravity(Gravity.CENTER);
                        dStart_date.setTextColor(Color.WHITE);
                        dStart_date.setBackgroundResource(R.drawable.lightblue);
                        layout.addView(dStart_date);
                    }

                    TextView End_date = new TextView(cnt);
                    End_date.setText("End date");
                    //pname.setLayoutParams(lpname);
                    End_date.setLayoutParams(ltext);
                    End_date.setTextSize(20);
                    End_date.setGravity(Gravity.CENTER);
                    End_date.setTextColor(Color.WHITE);
                    End_date.setBackgroundResource(R.drawable.lightblue);
                    layout.addView(End_date);

                    End_date.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Perform action on click
                           // DialogFragment newFragment = new SelectEndDateFragment();
                           // newFragment.show(getSupportFragmentManager(), "DatePicker");
                        }
                    });

                    if(GlobalData.end_Day!=-1)
                    {
                        TextView dEnd_date = new TextView(cnt);
                        dEnd_date.setText(GlobalData.end_Day+"/"+GlobalData.end_month+"/"+GlobalData.end_Year);
                        //pname.setLayoutParams(lpname);
                        dEnd_date.setLayoutParams(ltext);
                        dEnd_date.setTextSize(20);
                        dEnd_date.setGravity(Gravity.CENTER);
                        dEnd_date.setTextColor(Color.WHITE);
                        dEnd_date.setBackgroundResource(R.drawable.lightblue);
                        layout.addView(dEnd_date);
                    }

                    final Dialog dialog = new Dialog(cnt);
                    dialog.setContentView(layout);
                    dialog.setTitle("Start And End Time");
                    dialog.show();

                    TextView done = new TextView(cnt);
                    done.setText("Done");
                    //pname.setLayoutParams(lpname);
                    done.setLayoutParams(ltext);
                    done.setTextSize(20);
                    done.setGravity(Gravity.CENTER);
                    done.setTextColor(Color.WHITE);
                    done.setBackgroundResource(R.drawable.lightblue);
                    layout.addView(done);

                    done.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Perform action on click
                            dialog.hide();
                        }
                    });


                }
            });
        }
        //end adding last rows, date, map and places

        //adding purpose selection button

        LinearLayout.LayoutParams lt_5 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
        lt_5.weight = 1;
        lt_5.setMargins(2,2,2,2);


        final TextView Purpose = new TextView(cnt);
        Purpose.setText("Purpose:" + GlobalData.Purpose);
        Purpose.setLayoutParams(lt_5);
        Purpose.setTextSize(20);
        Purpose.setGravity(Gravity.CENTER);
        Purpose.setTextColor(Color.WHITE);
        Purpose.setBackgroundResource(R.drawable.lightblue);
        P_4.addView(Purpose);

        final TextView Limits_units = new TextView(cnt);
        Limits_units.setText("Limits/Units");
        Limits_units.setLayoutParams(lt_5);
        Limits_units.setTextSize(20);
        Limits_units.setGravity(Gravity.CENTER);
        Limits_units.setTextColor(Color.WHITE);
        Limits_units.setBackgroundResource(R.drawable.lightblue);
        P_4.addView(Limits_units);

        Purpose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ScrollView S_4 = new ScrollView(cnt);


                LinearLayout layout = new LinearLayout(cnt);
                layout.setOrientation(LinearLayout.VERTICAL);
                // layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                lparams2.gravity = Gravity.CENTER;
                S_4.setLayoutParams(lparams2);
                //S_4.addView(layout);
                S_4.setScrollbarFadingEnabled(false);
                layout.setLayoutParams(lparams2);

                lparams.setMargins(30, 20, 30, 0);

                Button Irrigation = new Button(cnt);

                Button ALL = new Button(cnt);
                // Button BATHING = new Button(cnt);
                Button Drinking = new Button(cnt);

                Button Dyeing = new Button(cnt);
                Button Pesticides = new Button(cnt);
                Button Power_plants = new Button(cnt);
                Button Pulp_paper = new Button(cnt);
                Button Tannery = new Button(cnt);
                Button Distillery = new Button(cnt);
                Button Sugar = new Button(cnt);
                Button Dairy = new Button(cnt);
                Button Slaughter_house = new Button(cnt);
                Button Beverage = new Button(cnt);

                Irrigation.setLayoutParams(lparams);
                Irrigation.setText("Irrigation");
                Irrigation.setBackgroundResource(R.drawable.roundedbutton);
                ALL.setLayoutParams(lparams);
                ALL.setText("ALL");
                ALL.setBackgroundResource(R.drawable.roundedbutton);
                //BATHING.setLayoutParams(lparams);
                //BATHING.setText("BATHING");
                //BATHING.setBackgroundResource(R.drawable.roundedbutton);
                Drinking.setLayoutParams(lparams);
                Drinking.setText("Drinking");
                Drinking.setBackgroundResource(R.drawable.roundedbutton);

                Dyeing.setLayoutParams(lparams);
                Dyeing.setText("Dyeing");
                Dyeing.setBackgroundResource(R.drawable.roundedbutton);

                Pesticides.setLayoutParams(lparams);
                Pesticides.setText("Pesticides");
                Pesticides.setBackgroundResource(R.drawable.roundedbutton);

                Power_plants.setLayoutParams(lparams);
                Power_plants.setText("Power plants");
                Power_plants.setBackgroundResource(R.drawable.roundedbutton);

                Pulp_paper.setLayoutParams(lparams);
                Pulp_paper.setText("Pulp and paper");
                Pulp_paper.setBackgroundResource(R.drawable.roundedbutton);

                Tannery.setLayoutParams(lparams);
                Tannery.setText("Tannery");
                Tannery.setBackgroundResource(R.drawable.roundedbutton);

                Distillery.setLayoutParams(lparams);
                Distillery.setText("Distillery");
                Distillery.setBackgroundResource(R.drawable.roundedbutton);

                Sugar.setLayoutParams(lparams);
                Sugar.setText("Sugar");
                Sugar.setBackgroundResource(R.drawable.roundedbutton);

                Dairy.setLayoutParams(lparams);
                Dairy.setText("Dairy");
                Dairy.setBackgroundResource(R.drawable.roundedbutton);

                Slaughter_house.setLayoutParams(lparams);
                Slaughter_house.setText("Slaughter house");
                Slaughter_house.setBackgroundResource(R.drawable.roundedbutton);

                Beverage.setLayoutParams(lparams);
                Beverage.setText("Beverage");
                Beverage.setBackgroundResource(R.drawable.roundedbutton);


                //BATHING.setId(10000);
                ALL.setId(101000);
                Drinking.setId(102000);
                Irrigation.setId(103000);

                Dyeing.setId(10400);
                Pesticides.setId(10500);
                Power_plants.setId(10600);
                Pulp_paper.setId(10700);
                Tannery.setId(10800);
                Distillery.setId(10900);
                Sugar.setId(10010);
                Dairy.setId(10020);
                Slaughter_house.setId(10030);
                Beverage.setId(10040);


                layout.addView(ALL);
                layout.addView(Beverage);
                layout.addView(Dairy);
                layout.addView(Distillery);
                layout.addView(Drinking);
                // layout.addView(BATHING);
                layout.addView(Dyeing);
                layout.addView(Irrigation);

                layout.addView(Pesticides);
                layout.addView(Power_plants);
                layout.addView(Pulp_paper);
                layout.addView(Slaughter_house);
                layout.addView(Sugar);
                layout.addView(Tannery);


                S_4.addView(layout);

                layout.setBackgroundResource(R.drawable.female1);
                final Dialog dialog = new Dialog(cnt);
                dialog.setContentView(S_4);
                dialog.setTitle("Select Purpose");

                dialog.show();

                Irrigation.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        // Purpose.setText("Purpose : Irrigation");
                        GlobalData.Purpose = "Irrigation";
                        dialog.hide();
                        show_data_layout();

                    }
                });

                ALL.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        // Purpose.setText("Purpose : All");
                        GlobalData.Purpose = "All";
                        dialog.hide();
                        show_data_layout();

                    }
                });

                Drinking.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //  Purpose.setText("Purpose : Drinking");
                        GlobalData.Purpose = "Drinking";
                        dialog.hide();
                        show_data_layout();
                    }
                });

               /* BATHING.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                      //  Purpose.setText("Purpose : Bathing");
                        GlobalData.Purpose="Bathing";
                        dialog.hide();
                        show_data_layout();
                    }
                });
                */
                Dyeing.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //  Purpose.setText("Purpose : Bathing");
                        GlobalData.Purpose = "Dyeing";
                        dialog.hide();
                        show_data_layout();
                    }
                });
                Pesticides.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //  Purpose.setText("Purpose : Bathing");
                        GlobalData.Purpose = "Pesticides";
                        dialog.hide();
                        show_data_layout();
                    }
                });
                Power_plants.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //  Purpose.setText("Purpose : Bathing");
                        GlobalData.Purpose = "Power plants";
                        dialog.hide();
                        show_data_layout();
                    }
                });
                Pulp_paper.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //  Purpose.setText("Purpose : Bathing");
                        GlobalData.Purpose = "Pulp and paper";
                        dialog.hide();
                        show_data_layout();
                    }
                });
                Tannery.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //  Purpose.setText("Purpose : Bathing");
                        GlobalData.Purpose = "Tannery";
                        dialog.hide();
                        show_data_layout();
                    }
                });
                Distillery.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //  Purpose.setText("Purpose : Bathing");
                        GlobalData.Purpose = "Distillery";
                        dialog.hide();
                        show_data_layout();
                    }
                });
                Sugar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //  Purpose.setText("Purpose : Bathing");
                        GlobalData.Purpose = "Sugar";
                        dialog.hide();
                        show_data_layout();
                    }
                });

                Dairy.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //  Purpose.setText("Purpose : Bathing");
                        GlobalData.Purpose = "Dairy";
                        dialog.hide();
                        show_data_layout();
                    }
                });
                Slaughter_house.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //  Purpose.setText("Purpose : Bathing");
                        GlobalData.Purpose = "Slaughter house";
                        dialog.hide();
                        show_data_layout();
                    }
                });
                Beverage.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //  Purpose.setText("Purpose : Bathing");
                        GlobalData.Purpose = "Beverage";
                        dialog.hide();
                        show_data_layout();
                    }
                });

            }
        });//end select_purpose.onclick

        //begin Limits_units on click

        Limits_units.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ScrollView S_4 = new ScrollView(cnt);

                LinearLayout layout = new LinearLayout(cnt);
                layout.setOrientation(LinearLayout.VERTICAL);
                // layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                lparams2.gravity = Gravity.CENTER;
                S_4.setLayoutParams(lparams2);
                //S_4.addView(layout);
                S_4.setScrollbarFadingEnabled(false);
                layout.setLayoutParams(lparams2);

                lparams.setMargins(30, 20, 30, 0);


                Button Close = new Button(cnt);
                Close.setId(130000);
                Close.setLayoutParams(lparams);
                Close.setText("Close");
                Close.setBackgroundColor(Color.parseColor("#FFDAB9"));
                layout.addView(Close);

                Button DO = new Button(cnt);
                DO.setId(101000);
                DO.setLayoutParams(lparams);
                DO.setText("DO");
                DO.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(DO);

                TextView DO_U = new TextView(cnt);//units
                DO_U.setId(101001);
                DO_U.setLayoutParams(lparams);
                DO_U.setText("Units:mg/L");
                DO_U.setTextSize(20);
                DO_U.setGravity(Gravity.CENTER);
                layout.addView(DO_U);

                //fetching limits from global str
                String do_l="Not Set";
                if (!GlobalData.lg.DO_min.isEmpty()) {
                    do_l="Min: "+GlobalData.lg.DO_min;
                }
                if (!GlobalData.lg.DO_max.isEmpty()) {
                    if(do_l.equals("Not Set"))
                        do_l="Max: "+GlobalData.lg.DO_max;
                    else
                        do_l=do_l+", Max: "+GlobalData.lg.DO_max;
                }

                TextView DO_L = new TextView(cnt);//Limits
                DO_L.setId(101002);
                DO_L.setLayoutParams(lparams);
                DO_L.setText("Limits: " + do_l);
                DO_L.setTextSize(20);
                DO_L.setGravity(Gravity.CENTER);
                layout.addView(DO_L);



                Button pH = new Button(cnt);
                pH.setId(102000);
                pH.setLayoutParams(lparams);
                pH.setText("pH");
                pH.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(pH);

                TextView pH_U = new TextView(cnt);//units
                pH_U.setId(102001);
                pH_U.setLayoutParams(lparams);
                pH_U.setText("Units:Decimal logarithm");
                pH_U.setTextSize(20);
                pH_U.setGravity(Gravity.CENTER);
                layout.addView(pH_U);

                //fetching limits from global str
                String ph_l="Not Set";
                if (!GlobalData.lg.pH_min.isEmpty()) {
                    ph_l="Min: "+GlobalData.lg.pH_min;
                }
                if (!GlobalData.lg.pH_max.isEmpty()) {
                    if(ph_l.equals("Not Set"))
                        ph_l="Max: "+GlobalData.lg.pH_max;
                    else
                        ph_l=ph_l+", Max: "+GlobalData.lg.pH_max;
                }

                TextView pH_L = new TextView(cnt);//Limits
                pH_L.setId(102002);
                pH_L.setLayoutParams(lparams);
                pH_L.setText("Limits: " + ph_l);
                pH_L.setTextSize(20);
                pH_L.setGravity(Gravity.CENTER);
                layout.addView(pH_L);


                Button Turbidity = new Button(cnt);
                Turbidity.setId(103000);
                Turbidity.setLayoutParams(lparams);
                Turbidity.setText("Turbidity");
                Turbidity.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Turbidity);

                TextView Turbidity_U = new TextView(cnt);//units
                Turbidity_U.setId(103001);
                Turbidity_U.setLayoutParams(lparams);
                Turbidity_U.setText("Units:NTU or FNU");
                Turbidity_U.setTextSize(20);
                Turbidity_U.setGravity(Gravity.CENTER);
                layout.addView(Turbidity_U);

                //fetching limits from global str
                String turbidity_l="Not Set";
                if (!GlobalData.lg.Turbidity_min.isEmpty()) {
                    turbidity_l="Min: "+GlobalData.lg.Turbidity_min;
                }
                if (!GlobalData.lg.pH_max.isEmpty()) {
                    if(turbidity_l.equals("Not Set"))
                        turbidity_l="Max: "+GlobalData.lg.Turbidity_max;
                    else
                    turbidity_l=turbidity_l+", Max: "+GlobalData.lg.Turbidity_max;
                }

                TextView Turbidity_L = new TextView(cnt);//Limits
                Turbidity_L.setId(103002);
                Turbidity_L.setLayoutParams(lparams);
                Turbidity_L.setText("Limits: " + turbidity_l);
                Turbidity_L.setTextSize(20);
                Turbidity_L.setGravity(Gravity.CENTER);
                layout.addView(Turbidity_L);


                Button Conductivity = new Button(cnt);
                Conductivity.setId(104000);
                Conductivity.setLayoutParams(lparams);
                Conductivity.setText("Conductivity");
                Conductivity.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Conductivity);

                TextView Conductivity_U = new TextView(cnt);//units
                Conductivity_U.setId(104001);
                Conductivity_U.setLayoutParams(lparams);
                Conductivity_U.setText("Units:micro siemens/cm");
                Conductivity_U.setTextSize(20);
                Conductivity_U.setGravity(Gravity.CENTER);
                layout.addView(Conductivity_U);

                //fetching limits from global str
                String conductivity_l="Not Set";
                if (!GlobalData.lg.Conductivity_min.isEmpty()) {
                    conductivity_l="Min: "+GlobalData.lg.Conductivity_min;
                }
                if (!GlobalData.lg.Conductivity_max.isEmpty()) {
                    if(conductivity_l.equals("Not Set"))
                        conductivity_l="Max: "+GlobalData.lg.Conductivity_max;
                    else
                        conductivity_l=conductivity_l+", Max: "+GlobalData.lg.Conductivity_max;
                }

                TextView Conductivity_L = new TextView(cnt);//Limits
                Conductivity_L.setId(104002);
                Conductivity_L.setLayoutParams(lparams);
                Conductivity_L.setText("Limits: " + conductivity_l);
                Conductivity_L.setTextSize(20);
                Conductivity_L.setGravity(Gravity.CENTER);
                layout.addView(Conductivity_L);



                Button Sulphate = new Button(cnt);
                Sulphate.setId(105000);
                Sulphate.setLayoutParams(lparams);
                Sulphate.setText("Sulphate");
                Sulphate.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Sulphate);

                TextView Sulphate_U = new TextView(cnt);//units
                Sulphate_U.setId(105001);
                Sulphate_U.setLayoutParams(lparams);
                Sulphate_U.setText("Units: mg/L");
                Sulphate_U.setTextSize(20);
                Sulphate_U.setGravity(Gravity.CENTER);
                layout.addView(Sulphate_U);

                //fetching limits from global str
                String sulphate_l="Not Set";
                if (!GlobalData.lg.Sulphate_min.isEmpty()) {
                    sulphate_l="Min: "+GlobalData.lg.Sulphate_min;
                }
                if (!GlobalData.lg.Sulphate_max.isEmpty()) {
                    if(sulphate_l.equals("Not Set"))
                        sulphate_l="Max: "+GlobalData.lg.Sulphate_max;
                    else
                        sulphate_l=sulphate_l+", Max: "+GlobalData.lg.Sulphate_max;
                }

                TextView Sulphate_L = new TextView(cnt);//Limits
                Sulphate_L.setId(105002);
                Sulphate_L.setLayoutParams(lparams);
                Sulphate_L.setText("Limits: " + sulphate_l);
                Sulphate_L.setTextSize(20);
                Sulphate_L.setGravity(Gravity.CENTER);
                layout.addView(Sulphate_L);


                Button Cadmium = new Button(cnt);
                Cadmium.setId(106000);
                Cadmium.setLayoutParams(lparams);
                Cadmium.setText("Cadmium");
                Cadmium.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Cadmium);

                TextView Cadmium_U = new TextView(cnt);//units
                Cadmium_U.setId(106001);
                Cadmium_U.setLayoutParams(lparams);
                Cadmium_U.setText("Units: mg/L");
                Cadmium_U.setTextSize(20);
                Cadmium_U.setGravity(Gravity.CENTER);
                layout.addView(Cadmium_U);

                //fetching limits from global str
                String cadmium_l="Not Set";
                if (!GlobalData.lg.Cadmium_min.isEmpty()) {
                    cadmium_l="Min: "+GlobalData.lg.Cadmium_min;
                }
                if (!GlobalData.lg.Cadmium_max.isEmpty()) {
                    if(cadmium_l.equals("Not Set"))
                        cadmium_l="Max: "+GlobalData.lg.Cadmium_max;
                    else
                        cadmium_l=cadmium_l+", Max: "+GlobalData.lg.Cadmium_max;
                }

                TextView Cadmium_L = new TextView(cnt);//Limits
                Cadmium_L.setId(106002);
                Cadmium_L.setLayoutParams(lparams);
                Cadmium_L.setText("Limits: " + cadmium_l);
                Cadmium_L.setTextSize(20);
                Cadmium_L.setGravity(Gravity.CENTER);
                layout.addView(Cadmium_L);




                Button Chromium = new Button(cnt);
                Chromium.setId(107000);
                Chromium.setLayoutParams(lparams);
                Chromium.setText("Chromium");
                Chromium.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Chromium);

                TextView Chromium_U = new TextView(cnt);//units
                Chromium_U.setId(107001);
                Chromium_U.setLayoutParams(lparams);
                Chromium_U.setText("Units: mg/L");
                Chromium_U.setTextSize(20);
                Chromium_U.setGravity(Gravity.CENTER);
                layout.addView(Chromium_U);

                //fetching limits from global str
                String chromium_l="Not Set";
                if (!GlobalData.lg.Chromium_min.isEmpty()) {
                    chromium_l="Min: "+GlobalData.lg.Chromium_min;
                }
                if (!GlobalData.lg.Chromium_max.isEmpty()) {
                    if(chromium_l.equals("Not Set"))
                        chromium_l="Max: "+GlobalData.lg.Chromium_max;
                    else
                        chromium_l=chromium_l+", Max: "+GlobalData.lg.Chromium_max;
                }

                TextView Chromium_L = new TextView(cnt);//Limits
                Chromium_L.setId(107002);
                Chromium_L.setLayoutParams(lparams);
                Chromium_L.setText("Limits: " + chromium_l);
                Chromium_L.setTextSize(20);
                Chromium_L.setGravity(Gravity.CENTER);
                layout.addView(Chromium_L);




                Button Nickel = new Button(cnt);
                Nickel.setId(108000);
                Nickel.setLayoutParams(lparams);
                Nickel.setText("Nickel");
                Nickel.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Nickel);

                TextView Nickel_U = new TextView(cnt);//units
                Nickel_U.setId(108001);
                Nickel_U.setLayoutParams(lparams);
                Nickel_U.setText("Units: mg/L");
                Nickel_U.setTextSize(20);
                Nickel_U.setGravity(Gravity.CENTER);
                layout.addView(Nickel_U);

                //fetching limits from global str
                String nickel_l="Not Set";
                if (!GlobalData.lg.Nickel_min.isEmpty()) {
                    nickel_l="Min: "+GlobalData.lg.Nickel_min;
                }
                if (!GlobalData.lg.Nickel_max.isEmpty()) {
                    if(nickel_l.equals("Not Set"))
                        nickel_l="Max: "+GlobalData.lg.Nickel_max;
                    else
                        nickel_l=nickel_l+", Max: "+GlobalData.lg.Nickel_max;
                }

                TextView Nickel_L = new TextView(cnt);//Limits
                Nickel_L.setId(108002);
                Nickel_L.setLayoutParams(lparams);
                Nickel_L.setText("Limits: " + nickel_l);
                Nickel_L.setTextSize(20);
                Nickel_L.setGravity(Gravity.CENTER);
                layout.addView(Nickel_L);




                Button Iron = new Button(cnt);
                Iron.setId(105000);
                Iron.setLayoutParams(lparams);
                Iron.setText("Iron");
                Iron.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Iron);

                TextView Iron_U = new TextView(cnt);//units
                Iron_U.setId(105001);
                Iron_U.setLayoutParams(lparams);
                Iron_U.setText("Units: mg/L");
                Iron_U.setTextSize(20);
                Iron_U.setGravity(Gravity.CENTER);
                layout.addView(Iron_U);

                //fetching limits from global str
                String iron_l="Not Set";
                if (!GlobalData.lg.Iron_min.isEmpty()) {
                    iron_l="Min: "+GlobalData.lg.Iron_min;
                }
                if (!GlobalData.lg.Iron_max.isEmpty()) {
                    if(iron_l.equals("Not Set"))
                        iron_l="Max: "+GlobalData.lg.Iron_max;
                    else
                        iron_l=iron_l+", Max: "+GlobalData.lg.Iron_max;
                }

                TextView Iron_L = new TextView(cnt);//Limits
                Iron_L.setId(105002);
                Iron_L.setLayoutParams(lparams);
                Iron_L.setText("Limits: " + iron_l);
                Iron_L.setTextSize(20);
                Iron_L.setGravity(Gravity.CENTER);
                layout.addView(Iron_L);



                Button Nitrate = new Button(cnt);
                Nitrate.setId(105000);
                Nitrate.setLayoutParams(lparams);
                Nitrate.setText("Nitrate");
                Nitrate.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Nitrate);

                TextView Nitrate_U = new TextView(cnt);//units
                Nitrate_U.setId(105001);
                Nitrate_U.setLayoutParams(lparams);
                Nitrate_U.setText("Units: ppm");
                Nitrate_U.setTextSize(20);
                Nitrate_U.setGravity(Gravity.CENTER);
                layout.addView(Nitrate_U);

                //fetching limits from global str
                String Nitrate_l="Not Set";
                if (!GlobalData.lg.Nitrate_min.isEmpty()) {
                    Nitrate_l="Min: "+GlobalData.lg.Nitrate_min;
                }
                if (!GlobalData.lg.Nitrate_max.isEmpty()) {
                    if(Nitrate_l.equals("Not Set"))
                        Nitrate_l="Max: "+GlobalData.lg.Nitrate_max;
                    else
                        Nitrate_l=Nitrate_l+", Max: "+GlobalData.lg.Nitrate_max;
                }

                TextView Nitrate_L = new TextView(cnt);//Limits
                Nitrate_L.setId(105002);
                Nitrate_L.setLayoutParams(lparams);
                Nitrate_L.setText("Limits: " + Nitrate_l);
                Nitrate_L.setTextSize(20);
                Nitrate_L.setGravity(Gravity.CENTER);
                layout.addView(Nitrate_L);



                Button BOD = new Button(cnt);
                BOD.setId(105000);
                BOD.setLayoutParams(lparams);
                BOD.setText("BOD");
                BOD.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(BOD);

                TextView BOD_U = new TextView(cnt);//units
                BOD_U.setId(105001);
                BOD_U.setLayoutParams(lparams);
                BOD_U.setText("Units: mg/L");
                BOD_U.setTextSize(20);
                BOD_U.setGravity(Gravity.CENTER);
                layout.addView(BOD_U);

                //fetching limits from global str
                String BOD_l="Not Set";
                if (!GlobalData.lg.BOD_min.isEmpty()) {
                    BOD_l="Min: "+GlobalData.lg.BOD_min;
                }
                if (!GlobalData.lg.BOD_max.isEmpty()) {
                    if(BOD_l.equals("Not Set"))
                        BOD_l="Max: "+GlobalData.lg.BOD_max;
                    else
                        BOD_l=BOD_l+", Max: "+GlobalData.lg.BOD_max;
                }

                TextView BOD_L = new TextView(cnt);//Limits
                BOD_L.setId(105002);
                BOD_L.setLayoutParams(lparams);
                BOD_L.setText("Limits: "+BOD_l);
                BOD_L.setTextSize(20);
                BOD_L.setGravity(Gravity.CENTER);
                layout.addView(BOD_L);


                Button COD = new Button(cnt);
                COD.setId(105000);
                COD.setLayoutParams(lparams);
                COD.setText("COD");
                COD.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(COD);

                TextView COD_U = new TextView(cnt);//units
                COD_U.setId(105001);
                COD_U.setLayoutParams(lparams);
                COD_U.setText("Units: mg/L");
                COD_U.setTextSize(20);
                COD_U.setGravity(Gravity.CENTER);
                layout.addView(COD_U);

                //fetching limits from global str
                String COD_l="Not Set";
                if (!GlobalData.lg.COD_min.isEmpty()) {
                    COD_l="Min: "+GlobalData.lg.COD_min;
                }
                if (!GlobalData.lg.COD_max.isEmpty()) {
                    if(COD_l.equals("Not Set"))
                        COD_l="Max: "+GlobalData.lg.COD_max;
                    else
                        COD_l=COD_l+", Max: "+GlobalData.lg.COD_max;
                }

                TextView COD_L = new TextView(cnt);//Limits
                COD_L.setId(105002);
                COD_L.setLayoutParams(lparams);
                COD_L.setText("Limits: " + COD_l);
                COD_L.setTextSize(20);
                COD_L.setGravity(Gravity.CENTER);
                layout.addView(COD_L);




                Button TC = new Button(cnt);
                TC.setId(105000);
                TC.setLayoutParams(lparams);
                TC.setText("TOTAL COLIFORM BACTERIA(TC)");
                TC.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(TC);

                TextView TC_U = new TextView(cnt);//units
                TC_U.setId(105001);
                TC_U.setLayoutParams(lparams);
                TC_U.setText("Units: Colonies/100 ml");
                TC_U.setTextSize(20);
                TC_U.setGravity(Gravity.CENTER);
                layout.addView(TC_U);

                //fetching limits from global str
                String TC_l="Not Set";
                if (!GlobalData.lg.TC_min.isEmpty()) {
                    TC_l="Min: "+GlobalData.lg.TC_min;
                }
                if (!GlobalData.lg.TC_max.isEmpty()) {
                    if(TC_l.equals("Not Set"))
                        TC_l="Max: "+GlobalData.lg.TC_max;
                    else
                        TC_l=TC_l+", Max: "+GlobalData.lg.TC_max;
                }

                TextView TC_L = new TextView(cnt);//Limits
                TC_L.setId(105002);
                TC_L.setLayoutParams(lparams);
                TC_L.setText("Limits: " + TC_l);
                TC_L.setTextSize(20);
                TC_L.setGravity(Gravity.CENTER);
                layout.addView(TC_L);


                Button Water_Temperature = new Button(cnt);
                Water_Temperature.setId(105000);
                Water_Temperature.setLayoutParams(lparams);
                Water_Temperature.setText("Water Temperature");
                Water_Temperature.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Water_Temperature);

                TextView Water_Temperature_U = new TextView(cnt);//units
                Water_Temperature_U.setId(105001);
                Water_Temperature_U.setLayoutParams(lparams);
                Water_Temperature_U.setText("Units:  Degree celsius");
                Water_Temperature_U.setTextSize(20);
                Water_Temperature_U.setGravity(Gravity.CENTER);
                layout.addView(Water_Temperature_U);

                //fetching limits from global str
                String Water_Temperature_l="Not Set";
                if (!GlobalData.lg.Water_Temperature_min.isEmpty()) {
                    Water_Temperature_l="Min: "+GlobalData.lg.Water_Temperature_min;
                }
                if (!GlobalData.lg.Water_Temperature_max.isEmpty()) {
                    if(Water_Temperature_l.equals("Not Set"))
                        Water_Temperature_l="Max: "+GlobalData.lg.Water_Temperature_max;
                    else
                        Water_Temperature_l=Water_Temperature_l+", Max: "+GlobalData.lg.Water_Temperature_max;
                }

                TextView Water_Temperature_L = new TextView(cnt);//Limits
                Water_Temperature_L.setId(105002);
                Water_Temperature_L.setLayoutParams(lparams);
                Water_Temperature_L.setText("Limits: " + Water_Temperature_l);
                Water_Temperature_L.setTextSize(20);
                Water_Temperature_L.setGravity(Gravity.CENTER);
                layout.addView(Water_Temperature_L);



                Button Air_Temperature = new Button(cnt);
                Air_Temperature.setId(105000);
                Air_Temperature.setLayoutParams(lparams);
                Air_Temperature.setText("Air Temperature");
                Air_Temperature.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Air_Temperature);

                TextView Air_Temperature_U = new TextView(cnt);//units
                Air_Temperature_U.setId(105001);
                Air_Temperature_U.setLayoutParams(lparams);
                Air_Temperature_U.setText("Units: Degree celsius");
                Air_Temperature_U.setTextSize(20);
                Air_Temperature_U.setGravity(Gravity.CENTER);
                layout.addView(Air_Temperature_U);

                //fetching limits from global str
                String Air_Temperature_l="Not Set";
                if (!GlobalData.lg.Air_Temperature_min.isEmpty()) {
                    Air_Temperature_l="Min: "+GlobalData.lg.Air_Temperature_min;
                }
                if (!GlobalData.lg.Air_Temperature_max.isEmpty()) {
                    if(Air_Temperature_l.equals("Not Set"))
                        Air_Temperature_l="Max: "+GlobalData.lg.Air_Temperature_max;
                    else
                        Air_Temperature_l=Air_Temperature_l+", Max: "+GlobalData.lg.Air_Temperature_max;
                }

                TextView Air_Temperature_L = new TextView(cnt);//Limits
                Air_Temperature_L.setId(105002);
                Air_Temperature_L.setLayoutParams(lparams);
                Air_Temperature_L.setText("Limits: " + Air_Temperature_l);
                Air_Temperature_L.setTextSize(20);
                Air_Temperature_L.setGravity(Gravity.CENTER);
                layout.addView(Air_Temperature_L);


                Button FC = new Button(cnt);
                FC.setId(105000);
                FC.setLayoutParams(lparams);
                FC.setText("FECAL COLIFORM BACTERIA(TC)");
                FC.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(FC);

                TextView FC_U = new TextView(cnt);//units
                FC_U.setId(105001);
                FC_U.setLayoutParams(lparams);
                FC_U.setText("Units: Colonies/100 ml");
                FC_U.setTextSize(20);
                FC_U.setGravity(Gravity.CENTER);
                layout.addView(FC_U);

                //fetching limits from global str
                String FC_l="Not Set";
                if (!GlobalData.lg.FC_min.isEmpty()) {
                    FC_l="Min: "+GlobalData.lg.FC_min;
                }
                if (!GlobalData.lg.FC_max.isEmpty()) {
                    if(FC_l.equals("Not Set"))
                        FC_l="Max: "+GlobalData.lg.FC_max;
                    else
                        FC_l=FC_l+", Max: "+GlobalData.lg.FC_max;
                }

                TextView FC_L = new TextView(cnt);//Limits
                FC_L.setId(105002);
                FC_L.setLayoutParams(lparams);
                FC_L.setText("Limits: " + FC_l);
                FC_L.setTextSize(20);
                FC_L.setGravity(Gravity.CENTER);
                layout.addView(FC_L);




                Button FS = new Button(cnt);
                FS.setId(105000);
                FS.setLayoutParams(lparams);
                FS.setText("FECAL STREPTOCOCCI BACTERIA(FC)");
                FS.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(FS);

                TextView FS_U = new TextView(cnt);//units
                FS_U.setId(105001);
                FS_U.setLayoutParams(lparams);
                FS_U.setText("Units: Colonies/100 ml");
                FS_U.setTextSize(20);
                FS_U.setGravity(Gravity.CENTER);
                layout.addView(FS_U);

                //fetching limits from global str
                String FS_l="Not Set";
                if (!GlobalData.lg.FS_min.isEmpty()) {
                    FS_l="Min: "+GlobalData.lg.FS_min;
                }
                if (!GlobalData.lg.FS_max.isEmpty()) {
                    if(FS_l.equals("Not Set"))
                        FS_l="Max: "+GlobalData.lg.FS_max;
                    else
                        FS_l=FS_l+", Max: "+GlobalData.lg.FS_max;
                }

                TextView FS_L = new TextView(cnt);//Limits
                FS_L.setId(105002);
                FS_L.setLayoutParams(lparams);
                FS_L.setText("Limits: " + FS_l);
                FS_L.setTextSize(20);
                FS_L.setGravity(Gravity.CENTER);
                layout.addView(FS_L);


                Button Velocity = new Button(cnt);
                Velocity.setId(105000);
                Velocity.setLayoutParams(lparams);
                Velocity.setText("VELOCITY");
                Velocity.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Velocity);

                TextView Velocity_U = new TextView(cnt);//units
                Velocity_U.setId(105001);
                Velocity_U.setLayoutParams(lparams);
                Velocity_U.setText("Units: m/s");
                Velocity_U.setTextSize(20);
                Velocity_U.setGravity(Gravity.CENTER);
                layout.addView(Velocity_U);

                //fetching limits from global str
                String Velocity_l="Not Set";
                if (!GlobalData.lg.Velocity_min.isEmpty()) {
                    Velocity_l="Min: "+GlobalData.lg.Velocity_min;
                }
                if (!GlobalData.lg.Velocity_max.isEmpty()) {
                    if(Velocity_l.equals("Not Set"))
                        Velocity_l="Max: "+GlobalData.lg.Velocity_max;
                    else
                        Velocity_l=Velocity_l+", Max: "+GlobalData.lg.Velocity_max;
                }

                TextView Velocity_L = new TextView(cnt);//Limits
                Velocity_L.setId(105002);
                Velocity_L.setLayoutParams(lparams);
                Velocity_L.setText("Limits: " + Velocity_l);
                Velocity_L.setTextSize(20);
                Velocity_L.setGravity(Gravity.CENTER);
                layout.addView(Velocity_L);



                Button Hardness = new Button(cnt);
                Hardness.setId(105000);
                Hardness.setLayoutParams(lparams);
                Hardness.setText("Hardness");
                Hardness.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Hardness);

                TextView Hardness_U = new TextView(cnt);//units
                Hardness_U.setId(105001);
                Hardness_U.setLayoutParams(lparams);
                Hardness_U.setText("Units: mg/L");
                Hardness_U.setTextSize(20);
                Hardness_U.setGravity(Gravity.CENTER);
                layout.addView(Hardness_U);

                //fetching limits from global str
                String Hardness_l="Not Set";
                if (!GlobalData.lg.Hardness_min.isEmpty()) {
                    Hardness_l="Min: "+GlobalData.lg.Hardness_min;
                }
                if (!GlobalData.lg.Hardness_max.isEmpty()) {
                    if(Hardness_l.equals("Not Set"))
                        Hardness_l="Max: "+GlobalData.lg.Hardness_max;
                    else
                        Hardness_l=Hardness_l+", Max: "+GlobalData.lg.Hardness_max;
                }

                TextView Hardness_L = new TextView(cnt);//Limits
                Hardness_L.setId(105002);
                Hardness_L.setLayoutParams(lparams);
                Hardness_L.setText("Limits: " + Hardness_l);
                Hardness_L.setTextSize(20);
                Hardness_L.setGravity(Gravity.CENTER);
                layout.addView(Hardness_L);



                Button Alkalinity = new Button(cnt);
                Alkalinity.setId(105000);
                Alkalinity.setLayoutParams(lparams);
                Alkalinity.setText("ALKALINITY");
                Alkalinity.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Alkalinity);

                TextView Alkalinity_U = new TextView(cnt);//units
                Alkalinity_U.setId(105001);
                Alkalinity_U.setLayoutParams(lparams);
                Alkalinity_U.setText("Units: mg/L");
                Alkalinity_U.setTextSize(20);
                Alkalinity_U.setGravity(Gravity.CENTER);
                layout.addView(Alkalinity_U);

                //fetching limits from global str
                String Alkalinity_l="Not Set";
                if (!GlobalData.lg.Alkalinity_min.isEmpty()) {
                    Alkalinity_l="Min: "+GlobalData.lg.Alkalinity_min;
                }
                if (!GlobalData.lg.Alkalinity_max.isEmpty()) {
                    if(Alkalinity_l.equals("Not Set"))
                        Alkalinity_l="Max: "+GlobalData.lg.Alkalinity_max;
                    else
                        Alkalinity_l=Alkalinity_l+", Max: "+GlobalData.lg.Alkalinity_max;
                }

                TextView Alkalinity_L = new TextView(cnt);//Limits
                Alkalinity_L.setId(105002);
                Alkalinity_L.setLayoutParams(lparams);
                Alkalinity_L.setText("Limits: " + Alkalinity_l);
                Alkalinity_L.setTextSize(20);
                Alkalinity_L.setGravity(Gravity.CENTER);
                layout.addView(Alkalinity_L);



                Button Chloride = new Button(cnt);
                Chloride.setId(105000);
                Chloride.setLayoutParams(lparams);
                Chloride.setText("Chloride");
                Chloride.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Chloride);

                TextView Chloride_U = new TextView(cnt);//units
                Chloride_U.setId(105001);
                Chloride_U.setLayoutParams(lparams);
                Chloride_U.setText("Units: mg/L");
                Chloride_U.setTextSize(20);
                Chloride_U.setGravity(Gravity.CENTER);
                layout.addView(Chloride_U);

                //fetching limits from global str
                String Chloride_l="Not Set";
                if (!GlobalData.lg.Chloride_min.isEmpty()) {
                    Chloride_l="Min: "+GlobalData.lg.Chloride_min;
                }
                if (!GlobalData.lg.Chloride_max.isEmpty()) {
                    if(Chloride_l.equals("Not Set"))
                        Chloride_l="Max: "+GlobalData.lg.Chloride_max;
                    else
                        Chloride_l=Chloride_l+", Max: "+GlobalData.lg.Chloride_max;
                }

                TextView Chloride_L = new TextView(cnt);//Limits
                Chloride_L.setId(105002);
                Chloride_L.setLayoutParams(lparams);
                Chloride_L.setText("Limits: " + Chloride_l);
                Chloride_L.setTextSize(20);
                Chloride_L.setGravity(Gravity.CENTER);
                layout.addView(Chloride_L);



                Button Na = new Button(cnt);
                Na.setId(105000);
                Na.setLayoutParams(lparams);
                Na.setText("SODIUM(Na)");
                Na.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Na);

                TextView Na_U = new TextView(cnt);//units
                Na_U.setId(105001);
                Na_U.setLayoutParams(lparams);
                Na_U.setText("Units: mg/L");
                Na_U.setTextSize(20);
                Na_U.setGravity(Gravity.CENTER);
                layout.addView(Na_U);

                //fetching limits from global str
                String Na_l="Not Set";
                if (!GlobalData.lg.Na_min.isEmpty()) {
                    Na_l="Min: "+GlobalData.lg.Na_min;
                }
                if (!GlobalData.lg.Na_max.isEmpty()) {
                    if(Na_l.equals("Not Set"))
                        Na_l="Max: "+GlobalData.lg.Na_max;
                    else
                        Na_l=Na_l+", Max: "+GlobalData.lg.Na_max;
                }

                TextView Na_L = new TextView(cnt);//Limits
                Na_L.setId(105002);
                Na_L.setLayoutParams(lparams);
                Na_L.setText("Limits: " + Na_l);
                Na_L.setTextSize(20);
                Na_L.setGravity(Gravity.CENTER);
                layout.addView(Na_L);


                Button Lead = new Button(cnt);
                Lead.setId(105000);
                Lead.setLayoutParams(lparams);
                Lead.setText("Lead");
                Lead.setBackgroundResource(R.drawable.roundedbutton);
                layout.addView(Lead);

                TextView Lead_U = new TextView(cnt);//units
                Lead_U.setId(105001);
                Lead_U.setLayoutParams(lparams);
                Lead_U.setText("Units: mg/L");
                Lead_U.setTextSize(20);
                Lead_U.setGravity(Gravity.CENTER);
                layout.addView(Lead_U);

                //fetching limits from global str
                String Lead_l="Not Set";
                if (!GlobalData.lg.Lead_min.isEmpty()) {
                    Lead_l="Min: "+GlobalData.lg.Lead_min;
                }
                if (!GlobalData.lg.Lead_max.isEmpty()) {
                    if(Lead_l.equals("Not Set"))
                        Lead_l="Max: "+GlobalData.lg.Lead_max;
                    else
                        Lead_l=Lead_l+", Max: "+GlobalData.lg.Lead_max;
                }

                TextView Lead_L = new TextView(cnt);//Limits
                Lead_L.setId(105002);
                Lead_L.setLayoutParams(lparams);
                Lead_L.setText("Limits: " + Lead_l);
                Lead_L.setTextSize(20);
                Lead_L.setGravity(Gravity.CENTER);
                layout.addView(Lead_L);



                S_4.addView(layout);

                layout.setBackgroundResource(R.drawable.female1);
                final Dialog dialog = new Dialog(cnt);
                dialog.setContentView(S_4);
                dialog.setTitle("Limits And Units of Parameters");

                dialog.show();

                Close.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        dialog.hide();
                        show_data_layout();
                    }
                });

            }
        });

         //end Limits_units on click
        //end adding purpose selection button





        //take the first recent data point from global data
          if(GlobalData.sensordata.size()>0)
        {
          SensorData s=GlobalData.sensordata.get(0);//take the first, it will be most recent one


            //add time to p2
            TextView stime = new TextView(cnt);
            if(s.source.equals("live_sensor_data"))
            stime.setText(s.date+"\n("+s.time+")");

            else if(s.source.equals("Historical"))
            {  try {
                stime.setText(s.Year + "\n(" + Month_s[Integer.parseInt(s.Month)] + ")");
                   }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            stime.setLayoutParams(lpname);
            stime.setTextSize(20);
            stime.setGravity(Gravity.CENTER);
            stime.setTextColor(Color.WHITE);
            P_2.addView(stime);


            /*
            Adding Water Quality Index box in the data
             */





            int pixels_text_size_Param_wqe = 16;//(int) (10 * scale + 0.5f);//multiple screens, size for the text data
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels_text_size_Param_wqe, getResources().getDisplayMetrics());

            TextView WQE = new TextView(cnt);
            WQE.setLayoutParams(Param_row_wqe);
            WQE.setTextSize(pixels_text_size_Param_wqe);
            WQE.setText("Water Quality Index\n" + nodata);
            WQE.setBackgroundColor(Color.parseColor("#ded8ec"));
            WQE.setGravity(Gravity.CENTER);
            P_3a.addView(WQE);


            //adding action on clicking WQE
            WQE.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

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
                    Close.setId(130017);
                    Close.setLayoutParams(lparams);
                    Close.setText("Close");
                    Close.setBackgroundColor(Color.parseColor("#FFDAB9"));
                    layout.addView(Close);

                    TextView WQE_exp = new TextView(cnt);//WQE explanation
                    WQE_exp.setId(1050032);
                    WQE_exp.setLayoutParams(lparams);
                    WQE_exp.setText("Water Quality Index (WQI) depicts the overall water quality. The index calculated is called weighted arithmetic WQI. \n" +
                            " The lower value of WQI depcits the better water quality. \n" +
                            "WQI lower than 25 is considered excellent water quality.\n" +
                            "WQI between 26 - 50 is considered good water quality.\n" +
                            "WQI between 51 - 75 is considered poor water quality.\n" +
                            "WQI between 76 - 100 is considered very poor water quality.\n" +
                            "WQI greater then 100 is considered unsuitable for drinking purpose. \n" +
                            "More details can be find on the APP project page.");
                    WQE_exp.setTextSize(20);
                    WQE_exp.setGravity(Gravity.CENTER);
                    layout.addView(WQE_exp);


                    //end explanation of WQE



                    S_4.addView(layout);

                    layout.setBackgroundResource(R.drawable.female1);
                    final Dialog dialog = new Dialog(cnt);
                    dialog.setContentView(S_4);
                    dialog.setTitle("Water Quality Index");

                    dialog.show();

                    Close.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            dialog.hide();
                            show_data_layout();
                        }
                    });
                }
            });


            //end adding action on clicking WQE


            ArrayList <Double>S_wqi = new ArrayList<Double>();//(limits) standard value in this list
            ArrayList <Double>V_wqi = new ArrayList<Double>();//ideal value in this list; ideal =0; except for Ph=7.0 and for DO = 14.6
            ArrayList <Double>M_wqi = new ArrayList<Double>();//measured value of the parameter
            ArrayList<String> par_wqi = new ArrayList<String>();//stores the corresponding parameter name
            double K_contant=0,K_inv=0; //K is the contant
            ArrayList <Double>Qi = new ArrayList<Double>();//quality rating score for each parameter
            ArrayList <Double>Wi = new ArrayList<Double>();//weight for each parameter
            double WQI=0,W_sum=0;

            try {
            //computing the value of WQE: Weighted arithmetic WQI calculation is done here.
            //Do
            if(!s.DO.isEmpty()&&!s.DO.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.DO));//measured value
                S_wqi.add(6.0);//minimum value of DO in Limits is 6.0
                V_wqi.add(14.6);//ideal value of Do is 14.6
                par_wqi.add("Do");//this is value for parameter Do
                //Toast.makeText(cnt, "Do is:"+M.get(0), Toast.LENGTH_SHORT).show();
            }

            //pH
            if(!s.pH.isEmpty()&&!s.pH.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.pH));//measured value

                if(Double.parseDouble(s.pH)<6.5)
                S_wqi.add(6.5);//min or max depend on the measure value of Ph in Limits

                else   //range is between 6.5 and 8.5, picking the standard near, elase 8.5
                    S_wqi.add(8.5);//To do: These values should come from the Limits files, and be specific to purpose.

                V_wqi.add(7.0);//ideal value of ph is 7.0
                par_wqi.add("pH");//this is value for parameter Do

            }

            //Turbidity
            if(!s.Turbidity.isEmpty()&&!s.Turbidity.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.Turbidity));//measured value
                S_wqi.add(10.0);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("Turbidity");//this is value for parameter

            }


            //Conductivity
            if(!s.Conductivity.isEmpty()&&!s.Conductivity.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.Conductivity));//measured value
                S_wqi.add(2250.0);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("Conductivity");//this is value for parameter

            }

            //Sulphate
            if(!s.Sulphate.isEmpty()&&!s.Sulphate.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.Sulphate));//measured value
                S_wqi.add(200.0);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("Sulphate");//this is value for parameter

            }

            //Cadmium
            if(!s.Cadmium.isEmpty()&&!s.Cadmium.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.Cadmium));//measured value
                S_wqi.add(0.01);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("Cadmium");//this is value for parameter

            }

            //Chromium
            if(!s.Chromium.isEmpty()&&!s.Chromium.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.Chromium));//measured value
                S_wqi.add(0.05);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("Chromium");//this is value for parameter

            }

            //Nickel
            if(!s.Nickel.isEmpty()&&!s.Nickel.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.Nickel));//measured value
                S_wqi.add(0.02);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("Nickel");//this is value for parameter

            }

            //Iron
            if(!s.Iron.isEmpty()&&!s.Iron.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.Iron));//measured value
                S_wqi.add(0.3);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("Iron");//this is value for parameter

            }

            //Nitrate has not limits so skipping it

            //BOD
            if(!s.BOD.isEmpty()&&!s.BOD.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.BOD));//measured value
                S_wqi.add(3.0);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("BOD");//this is value for parameter

            }

            //TC
            if(!s.TC.isEmpty()&&!s.TC.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.TC));//measured value
                S_wqi.add(500.0);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("TC");//this is value for parameter

            }

            //skipping the FC bacteria



            //Hardness
            if(!s.Hardness.isEmpty()&&!s.Hardness.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.Hardness));//measured value
                S_wqi.add(300.0);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("Hardness");//this is value for parameter

            }


            //Alkalinity
            if(!s.Alkalinity.isEmpty()&&!s.Alkalinity.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.Alkalinity));//measured value
                S_wqi.add(200.0);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("Alkalinity");//this is value for parameter

            }

            //Chloride
            if(!s.Chloride.isEmpty()&&!s.Chloride.equals("-1.0"))
            {
                //parameter is there
                M_wqi.add(Double.parseDouble(s.Chloride));//measured value
                S_wqi.add(250.0);//Limit on value
                V_wqi.add(0.0);//ideal value
                par_wqi.add("Chloride");//this is value for parameter

            }

               // System.out.println("Testing --Sandeep");



                //computing K contant
                for(int m=0;m<S_wqi.size();m++)
                {
                    K_inv = K_inv+(1.0/(S_wqi.get(m)));

                }//end for

                K_contant=(1.0/K_inv);

              //  System.out.println("Testing --K is:"+K_contant);

                //computing weight for each parameter
                for(int m=0;m<S_wqi.size();m++)
                {
                    Wi.add(K_contant/S_wqi.get(m));

                }//end for


                /*
                ArrayList <Double>S_wqi = new ArrayList<Double>();//(limits) standard value in this list
            ArrayList <Double>V_wqi = new ArrayList<Double>();//ideal value in this list; ideal =0; except for Ph=7.0 and for DO = 14.6
            ArrayList <Double>M_wqi = new ArrayList<Double>();//measured value of the parameter
                 */
                //computing Qi rating scale value for each parameter
                for(int m=0;m<S_wqi.size();m++)
                {
                    Qi.add(100.0*(M_wqi.get(m)-V_wqi.get(m))/(S_wqi.get(m)-V_wqi.get(m)));

                }//end for


                //computing WQI
                for(int m=0;m<S_wqi.size();m++)
                {
                    WQI =WQI+Qi.get(m)*Wi.get(m);
                    W_sum=W_sum+Wi.get(m);
                }//end for

                WQI=WQI/W_sum;

                //displaying in the GUI
                String WQE_text="Water Quality Index";
                final SpannableStringBuilder sb_WQE = new SpannableStringBuilder(WQE_text+"\n" +(int)WQI);
                final ForegroundColorSpan fcs_WQE = new ForegroundColorSpan(Color.parseColor("#142860"));
                sb_WQE.setSpan(fcs_WQE, WQE_text.length(), sb_WQE.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                WQE.setText(sb_WQE);//displaying the user

                /*

                for(int m=0;m<S_wqi.size();m++)
            {
                System.out.println(par_wqi.get(m));
               // System.out.println(M_wqi.get(m));
               // System.out.println(S_wqi.get(m));
              //  System.out.println(V_wqi.get(m));
                System.out.println(Wi.get(m));
                System.out.println(Qi.get(m));
            }
            */

               // System.out.println("Weight Quality Index:"+WQI);
               // System.out.println("W_sum:"+W_sum);
//
            }//end try: Computing the WQI
            catch (Exception e)
            {
                e.printStackTrace();
            }


            /*
            End adding Water Quality index box
             */


         if(!s.DO.isEmpty())
         {
            // String padding;
             //padding="("+GlobalData.lg.DO_min +"->"+GlobalData.lg.DO_max +")";
             //sandy you are here
             String par="Do";//parameter name
             final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.DO);
             // Span to set text color to some RGB value
             boolean range=true;//used to chekc if DO is in range or not

             //checking range of DO
             if (!GlobalData.lg.DO_min.isEmpty()) {
                 if (Double.parseDouble(s.DO) < Double.parseDouble(GlobalData.lg.DO_min))
                     range=false;
             }

             if (!GlobalData.lg.DO_max.isEmpty()) {
                 if (Double.parseDouble(s.DO) > Double.parseDouble(GlobalData.lg.DO_max))
                     range=false;
             }

             if(range==false) //changing the colour of value to red if not  in range
             {
                 final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                 sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
             }

             TextView tv = new TextView(cnt);
             if(!s.DO.equals("-1.0"))
            // tv.setText("Do\n" + s.DO + " "+ padding);
                 tv.setText(sb);
             else
                 tv.setText("Do\n" + nodata);


             tv.setLayoutParams(Param_row);
             tv.setTextSize(pixels_text_size_Param_row);

             //tv.setBackgroundResource(R.drawable.lightblue);
             if(GlobalData.Purpose.equals("Drinking"))
                 tv.setBackgroundColor(Color.parseColor("#FFDAB9"));
             else
                 tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
             tv.setGravity(Gravity.CENTER);

             tv.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v)
                 {
                     ArrayList<BarEntry> entries = new ArrayList<>();
                     ArrayList<String> labels = new ArrayList<String>();
                     for(int i=0;i<GlobalData.sensordata.size();i++)
                     {
                         Double dObj = new Double(GlobalData.sensordata.get(i).DO);
                         float a=dObj.floatValue();
                         if(!GlobalData.sensordata.get(i).DO.equals("-1.0"))
                         entries.add(new BarEntry(a,i));
                     }
                     BarDataSet dataset = new BarDataSet(entries, "Dissolved Oxygen");
                     for(int i=0;i<GlobalData.sensordata.size();i++)
                     {
                         if(GlobalData.sensordata.get(i).source.equals("live_sensor_data")&&!GlobalData.sensordata.get(i).DO.equals("-1.0"))
                         labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                         if(GlobalData.sensordata.get(i).source.equals("Historical")&&!GlobalData.sensordata.get(i).DO.equals("-1.0"))
                             labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                     }

                     setContentView(R.layout.layout_graph);
                     BarChart chart = (BarChart) findViewById(R.id.chart);
                     BarData data = new BarData(labels, dataset);
                     chart.setData(data);
                     chart.setDescription("Do Variation With Time");
                     chart.setVisibleXRangeMaximum(4);
                     chart.setVisibleXRangeMinimum(2);
                     chart.moveViewToX(-1);
                    // Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                     Button close_chart=(Button) findViewById(R.id.close_chart);

                     close_chart.setOnClickListener(new View.OnClickListener() {
                         public void onClick(View v) {

                             show_data_layout();
                         }
                     });

                 }
             });
             P_3a.addView(tv);
         }//end if(s.Do!=-1)

            if(!s.pH.isEmpty())
            {

                String par="pH";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.pH);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.pH_min.isEmpty()) {
                    if (Double.parseDouble(s.pH) < Double.parseDouble(GlobalData.lg.pH_min))
                        range=false;
                }

                if (!GlobalData.lg.pH_max.isEmpty()) {
                    if (Double.parseDouble(s.pH) > Double.parseDouble(GlobalData.lg.pH_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }



                TextView tv = new TextView(cnt);
                tv.setText(sb);
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                if(!GlobalData.Purpose.equals("All"))
                    tv.setBackgroundColor(Color.parseColor("#FFDAB9"));
                else
                    tv.setBackgroundColor(Color.parseColor("#DAE5E8"));

                //tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).pH);
                            float a=dObj.floatValue();
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "pH");
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("pH Variation With Time");
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                     //   Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3a.addView(tv);
            }//end if(s.Do!=-1)

            if(!s.Turbidity.isEmpty())
            {

                String par="Turbidity";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Turbidity);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Turbidity_min.isEmpty()) {
                    if (Double.parseDouble(s.Turbidity) < Double.parseDouble(GlobalData.lg.Turbidity_min))
                        range=false;
                }

                if (!GlobalData.lg.Turbidity_max.isEmpty()) {
                    if (Double.parseDouble(s.Turbidity) > Double.parseDouble(GlobalData.lg.Turbidity_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }



                TextView tv = new TextView(cnt);
                tv.setText(sb);


                //TextView tv = new TextView(cnt);

                if(!s.Turbidity.equals("-1.0"))
                    tv.setText(sb);
                    //tv.setText("Turbidity\n" + s.Turbidity);
                else
                    tv.setText("Turbidity\n" + nodata);

                //tv.setText("Turbidity\n" + s.Turbidity);
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Turbidity);
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Turbidity.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Turbidity");
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data")&&!GlobalData.sensordata.get(i).Turbidity.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical")&&!GlobalData.sensordata.get(i).Turbidity.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Turbidity Variation With Time");
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                     //   Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3a.addView(tv);
            }//end if(s.Do!=-1)


            if(!s.Conductivity.isEmpty())
            {

                String par="Conductivity";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Conductivity);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Conductivity_min.isEmpty()) {
                    if (Double.parseDouble(s.Conductivity) < Double.parseDouble(GlobalData.lg.Conductivity_min))
                        range=false;
                }

                if (!GlobalData.lg.Conductivity_max.isEmpty()) {
                    if (Double.parseDouble(s.Conductivity) > Double.parseDouble(GlobalData.lg.Conductivity_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }



                TextView tv = new TextView(cnt);
                tv.setText(sb);


               // TextView tv = new TextView(cnt);
                //tv.setText("Conductivity\n" + s.Conductivity);
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Conductivity);
                            float a=dObj.floatValue();
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Conductivity");
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Conductivity Variation With Time");
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                     //   Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3a.addView(tv);
            }//end if(s.Do!=-1)


            if(!s.TC.isEmpty())
            {
                String par="TC";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.TC);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.TC_min.isEmpty()) {
                    if (Double.parseDouble(s.TC) < Double.parseDouble(GlobalData.lg.TC_min))
                        range=false;
                }

                if (!GlobalData.lg.TC_max.isEmpty()) {
                    if (Double.parseDouble(s.TC) > Double.parseDouble(GlobalData.lg.TC_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }




                TextView tv = new TextView(cnt);
                if(!s.TC.equals("-1.0"))
                //    tv.setText("TC\n" + s.TC);
                    tv.setText(sb);
                else
                    tv.setText("TC\n" + nodata);


                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).TC);
                            float a=dObj.floatValue()/1000;
                            if(!GlobalData.sensordata.get(i).TC.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "TC*1000");
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") &&!GlobalData.sensordata.get(i).TC.equals("-1.0") )
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical")&&!GlobalData.sensordata.get(i).TC.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("TC*1000 Variation With Time");
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                      //  Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)




            if(!s.Water_Temperature.isEmpty())
            {
                String par="Water Temp";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Water_Temperature);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Water_Temperature_min.isEmpty()) {
                    if (Double.parseDouble(s.Water_Temperature) < Double.parseDouble(GlobalData.lg.Water_Temperature_min))
                        range=false;
                }

                if (!GlobalData.lg.Water_Temperature_max.isEmpty()) {
                    if (Double.parseDouble(s.Water_Temperature) > Double.parseDouble(GlobalData.lg.Water_Temperature_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }

                TextView tv = new TextView(cnt);
                if(!s.Water_Temperature.equals("-1.0"))
                    tv.setText(sb);
                   // tv.setText("Water Temp\n" + s.Water_Temperature);
                else
                    tv.setText("Water Temp\n" + nodata);

              //  tv.setText("Water Temp\n" + s.Water_Temperature);
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                if(GlobalData.Purpose.equals("Power plants"))
                    tv.setBackgroundColor(Color.parseColor("#FFDAB9"));
                else
                    tv.setBackgroundColor(Color.parseColor("#DAE5E8"));


                //tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Water_Temperature);
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Water_Temperature.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Water_Temperature");
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") &&!GlobalData.sensordata.get(i).Water_Temperature.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical") &&!GlobalData.sensordata.get(i).Water_Temperature.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Water_Temperature Variation With Time");
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                       // Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)

            if(!s.TDS.isEmpty())//.......................
            {//no limits implemented for TDS
                TextView tv = new TextView(cnt);
                if(!s.TDS.equals("-1.0"))
                    tv.setText("TDS\n" + s.TDS);
                else
                    tv.setText("TDS\n" + nodata);

                //tv.setText("TDS\n" + s.TDS);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).TDS);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).TDS.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "TDS");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data")&&!GlobalData.sensordata.get(i).TDS.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical") && !GlobalData.sensordata.get(i).TDS.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("TDS Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                   //     Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)

            if(!s.Air_Temperature.isEmpty())
            {//limits data str is there, but I am not using it in Air

                TextView tv = new TextView(cnt);
               if(!s.Air_Temperature.equals("-1.0"))
                tv.setText("Air Temp\n" + s.Air_Temperature);
                else
                   tv.setText("Air Temp\n" + nodata);

                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Air_Temperature);
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Air_Temperature.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Air_Temperature");
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") && !GlobalData.sensordata.get(i).Air_Temperature.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical") && !GlobalData.sensordata.get(i).Air_Temperature.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Air_Temperature Variation With Time");
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
        //                Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)


            if(!s.FC.isEmpty())
            {
                String par="FC";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.FC);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.FC_min.isEmpty()) {
                    if (Double.parseDouble(s.FC) < Double.parseDouble(GlobalData.lg.FC_min))
                        range=false;
                }

                if (!GlobalData.lg.FC_max.isEmpty()) {
                    if (Double.parseDouble(s.FC) > Double.parseDouble(GlobalData.lg.FC_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }

                TextView tv = new TextView(cnt);
                if(!s.FC.equals("-1.0"))
                    tv.setText(sb);
                  //  tv.setText("FC\n" + s.FC);
                else
                    tv.setText("FC\n" + nodata);

                //tv.setText("FC\n" + s.FC);
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                if(GlobalData.Purpose.equals("Drinking"))
                    tv.setBackgroundColor(Color.parseColor("#FFDAB9"));
                else
                    tv.setBackgroundColor(Color.parseColor("#DAE5E8"));

                //tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).FC);
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).FC.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "FC");
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") && !GlobalData.sensordata.get(i).FC.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical") && !GlobalData.sensordata.get(i).FC.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("FC Variation With Time");
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                        //Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)


            if(!s.FS.isEmpty())
            {
                String par="FS";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.FS);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.FS_min.isEmpty()) {
                    if (Double.parseDouble(s.FS) < Double.parseDouble(GlobalData.lg.FS_min))
                        range=false;
                }

                if (!GlobalData.lg.FS_max.isEmpty()) {
                    if (Double.parseDouble(s.FS) > Double.parseDouble(GlobalData.lg.FS_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }


                TextView tv = new TextView(cnt);
                if(!s.FS.equals("-1.0"))
                    tv.setText(sb);
                   // tv.setText("FS\n" + s.FS);
                else
                    tv.setText("FS\n" + nodata);

                //tv.setText("FS\n" + s.FS);
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).FS);
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).FS.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "FS");
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") && !GlobalData.sensordata.get(i).FS.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical") && !GlobalData.sensordata.get(i).FS.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("FS Variation With Time");
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                       // Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)


            if(!s.Velocity.isEmpty())
            {//limit data str is there, but not implementing concept of velocity min max
                TextView tv = new TextView(cnt);

                if(!s.Velocity.equals("-1.0"))
                    tv.setText("Velocity\n" + s.Velocity);
                else
                    tv.setText("Velocity\n" + nodata);

                //tv.setText("Velocity\n" + s.Velocity);
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);

                //dist, sugar, Dairy, sla,bev,Pulp,Tannery
                if(GlobalData.Purpose.equals("Distillery")||GlobalData.Purpose.equals("Sugar")||GlobalData.Purpose.equals("Dairy")||GlobalData.Purpose.equals("Slaughter house")||GlobalData.Purpose.equals("Beverage")||GlobalData.Purpose.equals("Pulp and paper")||GlobalData.Purpose.equals("Tannery"))
                    tv.setBackgroundColor(Color.parseColor("#FFDAB9"));
                else
                    tv.setBackgroundColor(Color.parseColor("#DAE5E8"));


                //tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Velocity);
                            float a=dObj.floatValue();

                            if(!GlobalData.sensordata.get(i).Velocity.equals("-1.0"))
                                entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Velocity");
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") && !GlobalData.sensordata.get(i).Velocity.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical") && !GlobalData.sensordata.get(i).Velocity.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Velocity Variation With Time");
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                        //Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)


            if(!s.Hardness.isEmpty())//.......................
            {
                String par="Hardness";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Hardness);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Hardness_min.isEmpty()) {
                    if (Double.parseDouble(s.Hardness) < Double.parseDouble(GlobalData.lg.Hardness_min))
                        range=false;
                }

                if (!GlobalData.lg.Hardness_max.isEmpty()) {
                    if (Double.parseDouble(s.Hardness) > Double.parseDouble(GlobalData.lg.Hardness_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }



                TextView tv = new TextView(cnt);
                if(!s.Hardness.equals("-1.0"))
                    tv.setText(sb);
                    //tv.setText("Hardness\n" + s.Hardness);
                else
                    tv.setText("Hardness\n" + nodata);

              //  tv.setText("Hardness\n" + s.Hardness);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Hardness);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Hardness.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Hardness");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") &&!GlobalData.sensordata.get(i).Hardness.equals("-1.0"))//..............
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical") && !GlobalData.sensordata.get(i).Hardness.equals("-1.0"))//...............
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Hardness Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                    //    Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)

            if(!s.Alkalinity.isEmpty())//.......................
            {

                String par="Alkalinity";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Alkalinity);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Alkalinity_min.isEmpty()) {
                    if (Double.parseDouble(s.Alkalinity) < Double.parseDouble(GlobalData.lg.Alkalinity_min))
                        range=false;
                }

                if (!GlobalData.lg.Alkalinity_max.isEmpty()) {
                    if (Double.parseDouble(s.Alkalinity) > Double.parseDouble(GlobalData.lg.Alkalinity_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }



                TextView tv = new TextView(cnt);
                if(!s.Alkalinity.equals("-1.0"))
                    tv.setText(sb);
                    // tv.setText("Alkalinity\n" + s.Alkalinity);
                else
                    tv.setText("Alkalinity\n" + nodata);

                //tv.setText("Alkalinity\n" + s.Alkalinity);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Alkalinity);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Alkalinity.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Alkalinity");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") && !GlobalData.sensordata.get(i).Alkalinity.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical")&& !GlobalData.sensordata.get(i).Alkalinity.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Alkalinity Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
//                        Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)


            if(!s.Chloride.isEmpty())//.......................
            {
                String par="Chloride";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Chloride);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Chloride_min.isEmpty()) {
                    if (Double.parseDouble(s.Chloride) < Double.parseDouble(GlobalData.lg.Chloride_min))
                        range=false;
                }

                if (!GlobalData.lg.Chloride_max.isEmpty()) {
                    if (Double.parseDouble(s.Chloride) > Double.parseDouble(GlobalData.lg.Chloride_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }



                TextView tv = new TextView(cnt);

                if(!s.Chloride.equals("-1.0"))
                    tv.setText(sb);
                    //tv.setText("Chloride\n" + s.Chloride);
                else
                    tv.setText("Chloride\n" + nodata);

               // tv.setText("Chloride\n" + s.Chloride);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Chloride);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Chloride.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Chloride");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") &&!GlobalData.sensordata.get(i).Chloride.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical") && !GlobalData.sensordata.get(i).Chloride.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Chloride Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                        //Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)

            if(!s.Sulphate.isEmpty())//.......................
            {
                String par="Sulphate";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Sulphate);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Sulphate_min.isEmpty()) {
                    if (Double.parseDouble(s.Sulphate) < Double.parseDouble(GlobalData.lg.Sulphate_min))
                        range=false;
                }

                if (!GlobalData.lg.Sulphate_max.isEmpty()) {
                    if (Double.parseDouble(s.Sulphate) > Double.parseDouble(GlobalData.lg.Sulphate_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }


                TextView tv = new TextView(cnt);
                if(!s.Sulphate.equals("-1.0"))
                    tv.setText(sb);
                    //tv.setText("Sulphate\n" + s.Sulphate);
                else
                    tv.setText("Sulphate\n" + nodata);

               // tv.setText("Sulphate\n" + s.Sulphate);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Sulphate);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Sulphate.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Sulphate");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") && !GlobalData.sensordata.get(i).Sulphate.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical") && !GlobalData.sensordata.get(i).Sulphate.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Sulphate Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                       // Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3a.addView(tv);
            }//end if(s.Do!=-1)

            if(!s.Cadmium.isEmpty())//.......................
            {


                String par="Cadmium";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Cadmium);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Cadmium_min.isEmpty()) {
                    if (Double.parseDouble(s.Cadmium) < Double.parseDouble(GlobalData.lg.Cadmium_min))
                        range=false;
                }

                if (!GlobalData.lg.Cadmium_max.isEmpty()) {
                    if (Double.parseDouble(s.Cadmium) > Double.parseDouble(GlobalData.lg.Cadmium_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }


                TextView tv = new TextView(cnt);

                if(!s.Cadmium.equals("-1.0"))
                    tv.setText(sb);
                   // tv.setText("Cadmium\n" + s.FS);
                else
                    tv.setText("Cadmium\n" + nodata);


               // tv.setText("Cadmium\n" + s.Cadmium);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Cadmium);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Cadmium.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Cadmium");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") && !GlobalData.sensordata.get(i).Cadmium.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical") && !GlobalData.sensordata.get(i).Cadmium.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Cadmium Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                       // Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3a.addView(tv);
            }//end if(s.Do!=-1)



            if(!s.Chromium.isEmpty())//.......................
            {

                String par="Chromium";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Chromium);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Chromium_min.isEmpty()) {
                    if (Double.parseDouble(s.Chromium) < Double.parseDouble(GlobalData.lg.Chromium_min))
                        range=false;
                }

                if (!GlobalData.lg.Chromium_max.isEmpty()) {
                    if (Double.parseDouble(s.Chromium) > Double.parseDouble(GlobalData.lg.Chromium_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }


                TextView tv = new TextView(cnt);

                if(!s.Chromium.equals("-1.0"))
                    tv.setText(sb);
                  //  tv.setText("Chromium\n" + s.Chromium);
                else
                    tv.setText("Chromium\n" + nodata);


               // tv.setText("Chromium\n" + s.Chromium);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);

                if(GlobalData.Purpose.equals("Drinking")||GlobalData.Purpose.equals("Dyeing")||GlobalData.Purpose.equals("Pesticides")||GlobalData.Purpose.equals("Tannery"))
                    tv.setBackgroundColor(Color.parseColor("#FFDAB9"));
                else
                    tv.setBackgroundColor(Color.parseColor("#DAE5E8"));

               // tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Chromium);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Chromium.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Chromium");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data") && !GlobalData.sensordata.get(i).Chromium.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical") && !GlobalData.sensordata.get(i).Chromium.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Chromium Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                      //  Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3a.addView(tv);
            }//end if(s.Do!=-1)


            if(!s.Nickel.isEmpty())//.......................
            {

                String par="Nickel";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Nickel);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Nickel_min.isEmpty()) {
                    if (Double.parseDouble(s.Nickel) < Double.parseDouble(GlobalData.lg.Nickel_min))
                        range=false;
                }

                if (!GlobalData.lg.Nickel_max.isEmpty()) {
                    if (Double.parseDouble(s.Nickel) > Double.parseDouble(GlobalData.lg.Nickel_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }


                TextView tv = new TextView(cnt);

                if(!s.Nickel.equals("-1.0"))
                    tv.setText(sb);
                    //tv.setText("Nickel\n" + s.Nickel);
                else
                    tv.setText("Nickel\n" + nodata);


               // tv.setText("Nickel\n" + s.Nickel);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Nickel);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Nickel.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Nickel");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data")&&!GlobalData.sensordata.get(i).Nickel.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical")&&!GlobalData.sensordata.get(i).Nickel.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Nickel Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                       // Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3a.addView(tv);
            }//end if(s.Do!=-1)


            if(!s.Iron.isEmpty())//.......................
            {

                String par="Iron";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Iron);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Iron_min.isEmpty()) {
                    if (Double.parseDouble(s.Iron) < Double.parseDouble(GlobalData.lg.Iron_min))
                        range=false;
                }

                if (!GlobalData.lg.Iron_max.isEmpty()) {
                    if (Double.parseDouble(s.Iron) > Double.parseDouble(GlobalData.lg.Iron_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }


                TextView tv = new TextView(cnt);

                if(!s.Iron.equals("-1.0"))
                    tv.setText(sb);
                    //tv.setText("Iron\n" + s.Iron);
                else
                    tv.setText("Iron\n" + nodata);


               // tv.setText("Iron\n" + s.Iron);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Nickel);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Iron.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Iron");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data")&&!GlobalData.sensordata.get(i).Iron.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical")&&!GlobalData.sensordata.get(i).Iron.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Iron Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                       // Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3a.addView(tv);
            }//end if(s.Do!=-1)



            if(!s.Na.isEmpty())//.......................
            {

                String par="Na";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Na);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Na_min.isEmpty()) {
                    if (Double.parseDouble(s.Na) < Double.parseDouble(GlobalData.lg.Na_min))
                        range=false;
                }

                if (!GlobalData.lg.Na_max.isEmpty()) {
                    if (Double.parseDouble(s.Na) > Double.parseDouble(GlobalData.lg.Na_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }


                TextView tv = new TextView(cnt);

                if(!s.Na.equals("-1.0"))
                    tv.setText(sb);
                    //tv.setText("Na\n" + s.Na);
                else
                    tv.setText("Na\n" + nodata);


                //tv.setText("Na\n" + s.Na);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Na);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Na.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Na");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data")&&!GlobalData.sensordata.get(i).Na.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical")&&!GlobalData.sensordata.get(i).Na.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Na Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                       // Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)





            if(!s.Lead.isEmpty())//.......................
            {

                String par="Lead";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.Lead);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.Lead_min.isEmpty()) {
                    if (Double.parseDouble(s.Lead) < Double.parseDouble(GlobalData.lg.Lead_min))
                        range=false;
                }

                if (!GlobalData.lg.Lead_max.isEmpty()) {
                    if (Double.parseDouble(s.Lead) > Double.parseDouble(GlobalData.lg.Lead_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }


                TextView tv = new TextView(cnt);

                if(!s.Lead.equals("-1.0"))
                    tv.setText(sb);
                    //tv.setText("Na\n" + s.Na);
                else
                    tv.setText("Lead\n" + nodata);


                //tv.setText("Na\n" + s.Na);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Na);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Lead.equals("-1.0"))
                                entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Lead");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data")&&!GlobalData.sensordata.get(i).Lead.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical")&&!GlobalData.sensordata.get(i).Lead.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Lead Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                        // Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3b.addView(tv);
            }//end if(s.Do!=-1)





            if(!s.Nitrate.isEmpty())//.......................
            {//currently there is no nitrate data str on server, it is not coming from server, no limits data str too
                TextView tv = new TextView(cnt);

                if(!s.Nitrate.equals("-1.0"))
                    tv.setText("Nitrate\n" + s.Nitrate);
                else
                    tv.setText("Nitrate\n" + nodata);

                //tv.setText("Nitrate\n" + s.Nitrate);//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).Nitrate);//.......................
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).Nitrate.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "Nitrate");//.......................
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data")&&!GlobalData.sensordata.get(i).Nitrate.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical")&&!GlobalData.sensordata.get(i).Nitrate.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("Nitrate Variation With Time");//.......................
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                     //   Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3a.addView(tv);
            }//end if(s.Do!=-1)



            if(!s.BOD.isEmpty())
            {

                String par="BOD";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.BOD);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.BOD_min.isEmpty()) {
                    if (Double.parseDouble(s.BOD) < Double.parseDouble(GlobalData.lg.BOD_min))
                        range=false;
                }

                if (!GlobalData.lg.BOD_max.isEmpty()) {
                    if (Double.parseDouble(s.BOD) > Double.parseDouble(GlobalData.lg.BOD_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }


                TextView tv = new TextView(cnt);

                if(!s.BOD.equals("-1.0"))
                    tv.setText(sb);
                   // tv.setText("BOD\n" + s.BOD);
                else
                    tv.setText("BOD\n" + nodata);

             //   tv.setText("BOD\n" + s.BOD);
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                if(GlobalData.Purpose.equals("Distillery")||GlobalData.Purpose.equals("Sugar")||GlobalData.Purpose.equals("Dairy")||GlobalData.Purpose.equals("Slaughter house")||GlobalData.Purpose.equals("Beverage")||GlobalData.Purpose.equals("Pesticides")||GlobalData.Purpose.equals("Pulp and paper")||GlobalData.Purpose.equals("Tannery"))
                    tv.setBackgroundColor(Color.parseColor("#FFDAB9"));
                else
                    tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                //tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).BOD);
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).BOD.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "BOD");
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data")&&!GlobalData.sensordata.get(i).BOD.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical")&&!GlobalData.sensordata.get(i).BOD.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("BOD Variation With Time");
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                       // Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3a.addView(tv);
            }//end if(s.Do!=-1)



            if(!s.COD.isEmpty())
            {
                String par="COD";//parameter name
                final SpannableStringBuilder sb = new SpannableStringBuilder(par+"\n" + s.COD);
                // Span to set text color to some RGB value
                boolean range=true;//used to chekc if DO is in range or not

                //checking range of DO
                if (!GlobalData.lg.COD_min.isEmpty()) {
                    if (Double.parseDouble(s.COD) < Double.parseDouble(GlobalData.lg.COD_min))
                        range=false;
                }

                if (!GlobalData.lg.COD_max.isEmpty()) {
                    if (Double.parseDouble(s.COD) > Double.parseDouble(GlobalData.lg.COD_max))
                        range=false;
                }

                if(range==false) //changing the colour of value to red if not  in range
                {
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(239, 13, 13));
                    sb.setSpan(fcs, par.length(), sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }


                TextView tv = new TextView(cnt);

                if(!s.COD.equals("-1.0"))
                    tv.setText(sb);
                    //tv.setText("COD\n" + s.COD);
                else
                    tv.setText("COD\n" + nodata);


               // tv.setText("COD\n" + s.COD);
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                if(GlobalData.Purpose.equals("Distillery")||GlobalData.Purpose.equals("Sugar")||GlobalData.Purpose.equals("Dairy")||GlobalData.Purpose.equals("Slaughter house")||GlobalData.Purpose.equals("Beverage")||GlobalData.Purpose.equals("Dying")||GlobalData.Purpose.equals("Pesticides")||GlobalData.Purpose.equals("Pulp and paper")||GlobalData.Purpose.equals("Tannery"))
                    tv.setBackgroundColor(Color.parseColor("#FFDAB9"));
                else
                    tv.setBackgroundColor(Color.parseColor("#DAE5E8"));

                //tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            Double dObj = new Double(GlobalData.sensordata.get(i).COD);
                            float a=dObj.floatValue();
                            if(!GlobalData.sensordata.get(i).COD.equals("-1.0"))
                            entries.add(new BarEntry(a,i));
                        }
                        BarDataSet dataset = new BarDataSet(entries, "COD");
                        for(int i=0;i<GlobalData.sensordata.size();i++)
                        {
                            if(GlobalData.sensordata.get(i).source.equals("live_sensor_data")&&!GlobalData.sensordata.get(i).COD.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).date+"\n"+GlobalData.sensordata.get(i).time);

                            if(GlobalData.sensordata.get(i).source.equals("Historical")&&!GlobalData.sensordata.get(i).COD.equals("-1.0"))
                                labels.add(GlobalData.sensordata.get(i).Year+"\n"+Month_s[Integer.parseInt(GlobalData.sensordata.get(i).Month)]);
                        }

                        setContentView(R.layout.layout_graph);
                        BarChart chart = (BarChart) findViewById(R.id.chart);
                        BarData data = new BarData(labels, dataset);
                        chart.setData(data);
                        chart.setDescription("COD Variation With Time");
                        chart.setVisibleXRangeMaximum(4);
                        chart.setVisibleXRangeMinimum(2);
                        chart.moveViewToX(-1);
                       // Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();

                        Button close_chart=(Button) findViewById(R.id.close_chart);

                        close_chart.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                show_data_layout();
                            }
                        });

                    }
                });
                P_3a.addView(tv);
            }//end if(s.Do!=-1)



        }//end if(GlobalData.sensordata.size()>0)

        //add in else that there is no data
        else
        {

                TextView tv = new TextView(cnt);
                tv.setText("No data yet available\nwill update soon");//.......................
                tv.setLayoutParams(Param_row);
                tv.setTextSize(pixels_text_size_Param_row);
                //tv.setBackgroundResource(R.drawable.lightblue);
                tv.setBackgroundColor(Color.parseColor("#DAE5E8"));
                tv.setGravity(Gravity.CENTER);


                P_3.addView(tv);


        }

        setContentView(P_1);
/*
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0;i<GlobalData.sensordata.size();i++)
        {
            Double dObj = new Double(GlobalData.sensordata.get(i).Do);

            float a=dObj.floatValue();

            entries.add(new BarEntry(a,i));
        }


        BarDataSet dataset = new BarDataSet(entries, "# of Calls");


        for(int i=0;i<GlobalData.sensordata.size();i++)
        {

            labels.add(GlobalData.sensordata.get(i).time);
        }

        BarChart chart = new BarChart(cnt);
        setContentView(chart);

        BarData data = new BarData(labels, dataset);
        chart.setData(data);

        chart.setDescription("Water Quality Parameters :"+GlobalData.sensordata.size());

        Toast.makeText(cnt, "Datapoints" + GlobalData.sensordata.size(), Toast.LENGTH_SHORT).show();
*/

    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            //  int count = urls.length;
            long totalSize = 0;
            DefaultHttpClient httpclient = new DefaultHttpClient();

            //////////////////////////////////////////////////////////////
            // loading data of places, thie place which is clicked is set in the global data structures
            try
            {
                Download_Places_Data PD=new Download_Places_Data();
                PD.download();

            }
           catch(Exception e)
           {
               e.printStackTrace();
           }
            //end loading data of this place




            return totalSize;
        }//end function

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            // showDialog("Downloaded " + result + " bytes");

            // Intent intent = new Intent(cnt, List_places.class);
            //startActivity(intent);
            try {
                show_data_layout();
                }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }//end onpost
    }

    public void populateSetDate(int year, int month, int day) {
        // Button bt = (Button)findViewById(R.id.select_date);
        //bt.setText(month + "/" + day + "/" + year);
        GlobalData.start_Day=day;
        GlobalData.start_month=month;
        GlobalData.start_Year=year;
     //   Intent intent =new Intent(cnt,PlacesData.class);
     //   startActivity(intent);
        Toast.makeText(cnt, "Start Date Set:"+GlobalData.start_Day+"/"+GlobalData.start_month+"/"+GlobalData.start_Year, Toast.LENGTH_SHORT).show();
    }

    public void populateEndDate(int year, int month, int day) {
        // Button bt = (Button)findViewById(R.id.select_date);
        //bt.setText(month + "/" + day + "/" + year);
        GlobalData.end_Day=day;
        GlobalData.end_month=month;
        GlobalData.end_Year=year;

        Toast.makeText(cnt, "End Date Set:"+GlobalData.end_Day+"/"+GlobalData.end_month+"/"+GlobalData.end_Year, Toast.LENGTH_SHORT).show();
        //   Intent intent =new Intent(cnt,PlacesData.class);
        //   startActivity(intent);

    }
/*
    public  class SelectEndDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);

        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateEndDate(yy, mm + 1, dd);
        }
    }

    public  class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }
    }

*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_places_data2, menu);
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
