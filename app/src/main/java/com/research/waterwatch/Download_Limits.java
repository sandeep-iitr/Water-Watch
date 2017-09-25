package com.research.waterwatch;

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
 * Created by administrator on 1/21/17.
 */

public class Download_Limits {

    void download()
    {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpgetreq = new HttpGet(GlobalData.Serveraddress+"query?Limits-By-User="+GlobalData.username+"&Lic="+GlobalData.LicKey);
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

            System.out.println("Limits from Server: "+response);
           // Log.d("cloud server", "stream from server" + response);

            //places are in response string
            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo_inside = jsonArray.getJSONObject(i);
                    //if(jo_inside.has("DO_min"))
                    //Log.d("Details limits-->", jo_inside.getString("DO_min"));
                    GlobalData.lg=new Limits_general();

                    if(jo_inside.has("Air_Temperature_max"))
                        GlobalData.lg.Air_Temperature_max=jo_inside.getString("Air_Temperature_max");
                    if(jo_inside.has("Water_Temperature_max"))
                        GlobalData.lg.Water_Temperature_max=jo_inside.getString("Water_Temperature_max");
                    if(jo_inside.has("DO_max"))
                        GlobalData.lg.DO_max=jo_inside.getString("DO_max");
                    if(jo_inside.has("BOD_max"))
                        GlobalData.lg.BOD_max=jo_inside.getString("BOD_max");
                    if(jo_inside.has("COD_max"))
                        GlobalData.lg.COD_max=jo_inside.getString("COD_max");
                    if(jo_inside.has("pH_max"))
                        GlobalData.lg.pH_max=jo_inside.getString("pH_max");
                    if(jo_inside.has("Turbidity_max"))
                        GlobalData.lg.Turbidity_max=jo_inside.getString("Turbidity_max");
                    if(jo_inside.has("Conductivity_max"))
                        GlobalData.lg.Conductivity_max=jo_inside.getString("Conductivity_max");
                    if(jo_inside.has("TC_max"))
                        GlobalData.lg.TC_max=jo_inside.getString("TC_max");
                    if(jo_inside.has("FC_max"))
                        GlobalData.lg.FC_max=jo_inside.getString("FC_max");
                    if(jo_inside.has("FS_max"))
                        GlobalData.lg.FS_max=jo_inside.getString("FS_max");
                    if(jo_inside.has("Velocity_max"))
                        GlobalData.lg.Velocity_max=jo_inside.getString("Velocity_max");
                    if(jo_inside.has("Hardness_max"))
                        GlobalData.lg.Hardness_max=jo_inside.getString("Hardness_max");
                    if(jo_inside.has("Alkalinity_max"))
                        GlobalData.lg.Alkalinity_max=jo_inside.getString("Alkalinity_max");
                    if(jo_inside.has("Chloride_max"))
                        GlobalData.lg.Chloride_max=jo_inside.getString("Chloride_max");
                    if(jo_inside.has("Cadmium_max"))
                        GlobalData.lg.Cadmium_max=jo_inside.getString("Cadmium_max");
                    if(jo_inside.has("Chromium_max"))
                        GlobalData.lg.Chromium_max=jo_inside.getString("Chromium_max");
                    if(jo_inside.has("Sulphate_max"))
                        GlobalData.lg.Sulphate_max=jo_inside.getString("Sulphate_max");
                    if(jo_inside.has("Nickel_max"))
                        GlobalData.lg.Nickel_max=jo_inside.getString("Nickel_max");
                    if(jo_inside.has("Iron_max"))
                        GlobalData.lg.Iron_max=jo_inside.getString("Iron_max");
                    if(jo_inside.has("Na_max"))
                        GlobalData.lg.Na_max=jo_inside.getString("Na_max");

                    if(jo_inside.has("Air_Temperature_min"))
                        GlobalData.lg.Air_Temperature_min=jo_inside.getString("Air_Temperature_min");
                    if(jo_inside.has("Water_Temperature_min"))
                        GlobalData.lg.Water_Temperature_min=jo_inside.getString("Water_Temperature_min");
                    if(jo_inside.has("DO_min"))
                        GlobalData.lg.DO_min=jo_inside.getString("DO_min");
                    if(jo_inside.has("BOD_min"))
                        GlobalData.lg.BOD_min=jo_inside.getString("BOD_min");
                    if(jo_inside.has("COD_min"))
                        GlobalData.lg.COD_min=jo_inside.getString("COD_min");
                    if(jo_inside.has("pH_min"))
                        GlobalData.lg.pH_min=jo_inside.getString("pH_min");
                    if(jo_inside.has("Turbidity_min"))
                        GlobalData.lg.Turbidity_min=jo_inside.getString("Turbidity_min");
                    if(jo_inside.has("Conductivity_min"))
                        GlobalData.lg.Conductivity_min=jo_inside.getString("Conductivity_min");
                    if(jo_inside.has("TC_min"))
                        GlobalData.lg.TC_min=jo_inside.getString("TC_min");
                    if(jo_inside.has("FC_min"))
                        GlobalData.lg.FC_min=jo_inside.getString("FC_min");
                    if(jo_inside.has("FS_min"))
                        GlobalData.lg.FS_min=jo_inside.getString("FS_min");
                    if(jo_inside.has("Velocity_min"))
                        GlobalData.lg.Velocity_min=jo_inside.getString("Velocity_min");
                    if(jo_inside.has("Hardness_min"))
                        GlobalData.lg.Hardness_min=jo_inside.getString("Hardness_min");
                    if(jo_inside.has("Alkalinity_min"))
                        GlobalData.lg.Alkalinity_min=jo_inside.getString("Alkalinity_min");
                    if(jo_inside.has("Chloride_min"))
                        GlobalData.lg.Chloride_min=jo_inside.getString("Chloride_min");
                    if(jo_inside.has("Cadmium_min"))
                        GlobalData.lg.Cadmium_min=jo_inside.getString("Cadmium_min");
                    if(jo_inside.has("Chromium_min"))
                        GlobalData.lg.Chromium_min=jo_inside.getString("Chromium_min");
                    if(jo_inside.has("Sulphate_min"))
                        GlobalData.lg.Sulphate_min=jo_inside.getString("Sulphate_min");
                    if(jo_inside.has("Nickel_min"))
                        GlobalData.lg.Nickel_min=jo_inside.getString("Nickel_min");
                    if(jo_inside.has("Iron_min"))
                        GlobalData.lg.Iron_min=jo_inside.getString("Iron_min");
                    if(jo_inside.has("Na_min"))
                        GlobalData.lg.Na_min=jo_inside.getString("Na_min");
                }

            } catch (JSONException e) {
                //   e.printStackTrace();
            }


        } catch (IOException e) {
            // e.printStackTrace();
        }
//end loading limits of general parameters


    }//end download
}//end Download_Limits
