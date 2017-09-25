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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by administrator on 1/21/17.
 */

public class Download_Places_Data {

    void download()
    {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String Server_Get_API=GlobalData.Serveraddress+"query?Lic="+GlobalData.LicKey+"&";
        Server_Get_API=Server_Get_API+"Latitude="+GlobalData.current_Lat;
        Server_Get_API=Server_Get_API+"&Longitude="+GlobalData.current_Lng;
        Server_Get_API=Server_Get_API+"&Range="+GlobalData.current_range;

        System.out.println("Places Data String is:"+Server_Get_API);

        HttpGet httpgetreq = new HttpGet(Server_Get_API);

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
            String response2 = sb.toString();
            stream.close();

              System.out.println("Data from Server is:"+response2);

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
                    //  if(jo_inside.has("Place_name"))
                    //     Log.d("Details-data->", jo_inside.getString("Place_name"));

                    SensorData s=new SensorData();

                    if(jo_inside.has("source"))
                        s.source=jo_inside.getString("source");

                    if(s.source.equals("Historical"))
                    {
                        if (jo_inside.has("Place_id"))
                            s.Place_id = jo_inside.getString("Place_id");
                        if (jo_inside.has("Place_name"))
                            s.Place_name = jo_inside.getString("Place_name");

                        if(jo_inside.has("lat"))
                            s.lat = jo_inside.getDouble("lat");//compatibility
                        if(jo_inside.has("lng"))
                            s.lng=jo_inside.getDouble("lng");//compatibility

                        if (jo_inside.has("datetime"))
                        {
                            s.Datetime = jo_inside.getString("datetime");
                            //generate year and month from datetime string
                            try{
                                long unixSeconds = Long.parseLong(s.Datetime);
                                Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to millisec// onds
                                TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                int Month=cal.get(Calendar.MONTH)+1;//month is numbered from 0-11
                                int Year=cal.get(Calendar.YEAR);

                                s.Month= String.valueOf(Month);
                                s.Year=String.valueOf(Year);
                              }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }//end if (jo_inside.has("datetime"))


                        if (jo_inside.has("Air_Temperature"))
                            s.Air_Temperature = jo_inside.getString("Air_Temperature");
                        if (jo_inside.has("Water_Temperature"))
                            s.Water_Temperature = jo_inside.getString("Water_Temperature");
                        if (jo_inside.has("DO"))
                            s.DO = jo_inside.getString("DO");
                        if (jo_inside.has("BOD"))
                            s.BOD = jo_inside.getString("BOD");
                        if (jo_inside.has("COD"))
                            s.COD = jo_inside.getString("COD");
                        if (jo_inside.has("pH"))
                            s.pH = jo_inside.getString("pH");
                        if (jo_inside.has("Turbidity"))
                            s.Turbidity = jo_inside.getString("Turbidity");
                        if (jo_inside.has("Conductivity"))
                            s.Conductivity = jo_inside.getString("Conductivity");
                        if (jo_inside.has("EC"))
                            s.Conductivity = jo_inside.getString("EC");

                        if (jo_inside.has("TC"))
                            s.TC = jo_inside.getString("TC");
                        if (jo_inside.has("FC"))
                            s.FC = jo_inside.getString("FC");
                        if (jo_inside.has("FS"))
                            s.FS = jo_inside.getString("FS");
                        if (jo_inside.has("Velocity"))
                            s.Velocity = jo_inside.getString("Velocity");
                        if (jo_inside.has("Hardness"))
                            s.Hardness = jo_inside.getString("Hardness");
                        if (jo_inside.has("Alkalinity"))
                            s.Alkalinity = jo_inside.getString("Alkalinity");
                        if (jo_inside.has("Chloride"))
                            s.Chloride = jo_inside.getString("Chloride");
                        if (jo_inside.has("Cadmium"))
                            s.Cadmium = jo_inside.getString("Cadmium");
                        if (jo_inside.has("Chromium"))
                            s.Chromium = jo_inside.getString("Chromium");
                        if (jo_inside.has("Sulphate"))
                            s.Sulphate = jo_inside.getString("Sulphate");
                        if (jo_inside.has("Nickel"))
                            s.Nickel = jo_inside.getString("Nickel");
                        if (jo_inside.has("Iron"))
                            s.Iron = jo_inside.getString("Iron");
                        if (jo_inside.has("Nitrate"))
                            s.Nitrate = jo_inside.getString("Nitrate");
                        if (jo_inside.has("Na"))
                            s.Na = jo_inside.getString("Na");
                        if (jo_inside.has("Lead"))
                            s.Lead = jo_inside.getString("Lead");

                        GlobalData.sensordata.add(s);
                        GlobalData.Datapoint++;

                    }//end if(s.source.equal("historical")

                    else if(s.source.equals("live_sensor_data"))
                    {
                        if(jo_inside.has("lat"))
                            s.lat = jo_inside.getDouble("lat");
                        if(jo_inside.has("lng"))
                            s.lng=jo_inside.getDouble("lng");
                        //if(jo_inside.has("Do"))
                        //   s.Do=jo_inside.getDouble("Do");
                        if(jo_inside.has("time"))
                            s.time = jo_inside.getString("time");
                        if(jo_inside.has("date"))
                            s.date=jo_inside.getString("date");

                        {//assigning first dummy one's,
                            //some of the parameters will be later modified correctly

                            if (jo_inside.has("Water_Temperature"))
                                s.Water_Temperature = jo_inside.getString("Water_Temperature");
                            if (jo_inside.has("DO"))
                                s.DO = jo_inside.getString("DO");
                            if (jo_inside.has("BOD"))
                                s.BOD = jo_inside.getString("BOD");

                            if (jo_inside.has("pH"))
                                s.pH = jo_inside.getString("pH");
                            if (jo_inside.has("Turbidity"))
                                s.Turbidity = jo_inside.getString("Turbidity");
                            if (jo_inside.has("Conductivity"))
                                s.Conductivity = jo_inside.getString("Conductivity");
                            if (jo_inside.has("TC"))
                                s.TC = jo_inside.getString("TC");
                            if (jo_inside.has("FC"))
                                s.FC = jo_inside.getString("FC");
                            if (jo_inside.has("FS"))
                                s.FS = jo_inside.getString("FS");

                            if (jo_inside.has("Nitrate"))
                                s.Nitrate = jo_inside.getString("Nitrate");


                        }//end assigning first dummy one's,


                        if(jo_inside.has("pH"))
                            s.pH=jo_inside.getString("pH");
                        // s.ph=jo_inside.getDouble("ph");

                        if(jo_inside.has("temp"))
                            s.Water_Temperature=jo_inside.getString("temp");
                        //s.temp=jo_inside.getDouble("temp");

                        if(jo_inside.has("ORP"))//oxygen reduction potential
                            s.orpmv=jo_inside.getDouble("ORP");

                        if(jo_inside.has("EC"))
                            s.Conductivity=jo_inside.getString("EC");
                        //s.ecscm=jo_inside.getDouble("ecscm");

                        if(jo_inside.has("ecabsscm"))
                            s.ecabsscm=jo_inside.getDouble("ecabsscm");

                        if(jo_inside.has("resohmcm"))
                            s.resohmcm=jo_inside.getDouble("Resitivity");

                        if(jo_inside.has("tdsppm"))
                            s.TDS =jo_inside.getString("TDS");
                        //s.tdsppm=jo_inside.getDouble("tdsppm");

                        if(jo_inside.has("salpsu"))
                            s.salpsu=jo_inside.getDouble("Saility");

                        if(jo_inside.has("sigmat"))
                            s.sigmat=jo_inside.getDouble("sigmat");

                        if(jo_inside.has("Pressure"))
                            s.presspsi=jo_inside.getDouble("Pressure");

                        //if(jo_inside.has("Do"))
                        //   s.Do=jo_inside.getDouble("Do");

                        if(jo_inside.has("doppm"))
                            s.DO=jo_inside.getString("doppm");
                        //s.doppm=jo_inside.getDouble("doppm");

                        if(s.DO.equals("-1.0")&&jo_inside.has("domgl"))//if no DO in DOppm, then only update this
                            // s.DO=jo_inside.getString("domgl");
                            s.DO=jo_inside.getString("domgl");

                        if(jo_inside.has("turbfnu"))
                            s.Turbidity=jo_inside.getString("turbfnu");
                        //s.turbfnu=jo_inside.getDouble("turbfnu");

                        if(jo_inside.has("ppmni"))
                            s.Nitrate=jo_inside.getString("ppmni");
                        //s.ppmni=jo_inside.getDouble("ppmni");

                        GlobalData.sensordata.add(s);
                        GlobalData.Datapoint++;
                    }//end else if(s.source.equals("live_sensor_data"))


                }

            } catch (JSONException e) {
                //   e.printStackTrace();
            }


        } catch (IOException e) {
            //e.printStackTrace();
        }



    }//end download

}//end Download_Places_Data
