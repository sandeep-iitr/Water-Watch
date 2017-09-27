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

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by administrator on 1/4/17.
 */

public class Download_Places {

    void download()
    {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpgetreq = new HttpGet(GlobalData.Serveraddress+"query?Places-By-User="+GlobalData.username+"&Lic="+GlobalData.LicKey);

        //System.out.println("Places Query is:"+GlobalData.Serveraddress+"query?Places-By-User="+GlobalData.username+"&Lic="+GlobalData.LicKey);

        try {

            HttpResponse httpresponse = httpclient.execute(httpgetreq);

            InputStream stream=httpresponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    stream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            String response = sb.toString();
            stream.close();
            //
            //Toast.makeText(cnt, stream.toString(), Toast.LENGTH_SHORT).show();
            //Log.d("cloud server new2", "stream from server new2" + response);

            //places are in response string
            try {
                JSONArray jsonArray = new JSONArray(response);

                //clearing already read places in the memory
                if(GlobalData.numPlaces>0)
                {
                    GlobalData.numPlaces=0;
                    GlobalData.place.clear();
                }


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo_inside = jsonArray.getJSONObject(i);
                    //  Log.d("Details-->", jo_inside.getString("Place_name"));
                    Places p1=new Places();

                    //p1.Place_id = jo_inside.getInt("Place_id");
                    //if(p1.Place_id>0)//some data of place is there then only add it
                    {
                        if(jo_inside.has("Place_id"))
                            p1.Place_id = jo_inside.getInt("Place_id");
                        if(jo_inside.has("Lat"))
                            p1.Lat = jo_inside.getString("Lat");
                        if(jo_inside.has("Lng"))
                            p1.Lng = jo_inside.getString("Lng");
                        if(jo_inside.has("Place_name"))
                            p1.Place_name = jo_inside.getString("Place_name");
                        else
                            p1.Place_name="ZZAuto";

                        if(jo_inside.has("data_type"))
                            p1.data_type=jo_inside.getInt("data_type");

                        if(jo_inside.has("Range"))
                            p1.range=jo_inside.getDouble("Range");

                        if(jo_inside.has("type"))
                            p1.type = jo_inside.getString("type");

                        Log.d("Place Added", "Place" + p1.Place_name);

                        GlobalData.place.add(p1);
                        GlobalData.numPlaces++;
                    }//end if
                }//end for

            } catch (JSONException e) {
                  e.printStackTrace();
            }


        } catch (IOException e) {
              e.printStackTrace();
        }
    }//end download

}//end class
