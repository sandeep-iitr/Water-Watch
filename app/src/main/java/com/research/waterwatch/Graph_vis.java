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

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class Graph_vis extends ActionBarActivity {

    Context cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_vis);

       cnt =this;


        RelativeLayout layout = new RelativeLayout(this);
        ProgressBar progressBar = new ProgressBar(cnt,null,android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        setContentView(layout);
        DownloadFilesTask dw=new DownloadFilesTask();
        dw.execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph_vis, menu);
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


    void show_graph()
    {
        ArrayList<BarEntry> entries = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0;i<GlobalData.IBMsensordata.size();i++)
        {
            Double dObj = new Double(GlobalData.IBMsensordata.get(i).Do);

            float a=dObj.floatValue();

            entries.add(new BarEntry(a,i));

        }

        /*
    entries.add(new BarEntry(4f, 0));
    entries.add(new BarEntry(8f, 1));
    entries.add(new BarEntry(6f, 2));
    entries.add(new BarEntry(12f, 3));
    entries.add(new BarEntry(18f, 4));
    entries.add(new BarEntry(9f, 5));
 */
    BarDataSet dataset = new BarDataSet(entries, "# of Calls");


        for(int i=0;i<GlobalData.IBMsensordata.size();i++)
        {

            labels.add(GlobalData.IBMsensordata.get(i).time);
        }

/*
    labels.add("January");
    labels.add("February");
    labels.add("March");
    labels.add("April");
    labels.add("May");
    labels.add("June");
*/
    BarChart chart = new BarChart(cnt);
    setContentView(chart);

    BarData data = new BarData(labels, dataset);
    chart.setData(data);

    chart.setDescription("Water Quality Parameters :"+GlobalData.IBMsensordata.size());

        Toast.makeText(cnt,"Datapoints"+GlobalData.IBMsensordata.size() , Toast.LENGTH_SHORT).show();
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            //  int count = urls.length;
            long totalSize = 0;
            DefaultHttpClient httpclient = new DefaultHttpClient();

            //////////////////////////////////////////////////////////////
            // loading data of places, thie place which is clicked is set in the global data structures

            HttpPost httppostreq = new HttpPost("http://cdata.mybluemix.net/Testing_graph_vis");
            try {


                HttpResponse httpresponse = httpclient.execute(httppostreq);

                InputStream stream=httpresponse.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        stream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String response2 = sb.toString();
                stream.close();
                //
                //Toast.makeText(cnt, stream.toString(), Toast.LENGTH_SHORT).show();
                Log.d("cloud server", "stream from server" + response2);

                //places are in response string
                try {
                    JSONArray jsonArray = new JSONArray(response2);

                    //clearing already read places in the memory
                    if(GlobalData.Datapoint>0)
                    {
                        GlobalData.Datapoint=0;
                        GlobalData.sensordata.clear();
                    }




                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo_inside = jsonArray.getJSONObject(i);
                        if(jo_inside.has("Place_name"))
                            Log.d("Details-data->", jo_inside.getString("Place_name"));

                        Data_IBM_sensors s=new Data_IBM_sensors();

                        if(jo_inside.has("lat"))
                            s.lat = jo_inside.getDouble("lat");
                        if(jo_inside.has("lng"))
                            s.lng=jo_inside.getDouble("lng");
                        if(jo_inside.has("Do"))
                            s.Do=jo_inside.getDouble("Do");
                        if(jo_inside.has("time"))
                            s.time=jo_inside.getString("time");

                        GlobalData.IBMsensordata.add(s);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }





            return totalSize;
        }//end function

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            // showDialog("Downloaded " + result + " bytes");

            // Intent intent = new Intent(cnt, List_places.class);
            //startActivity(intent);


            show_graph();


        }//end onpost
    }


}
