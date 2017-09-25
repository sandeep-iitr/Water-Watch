package com.research.waterwatch;

/**
 * Created by admin on 5/18/2015.
 */
public class Places {

    int Place_id;
    String Place_name;
    String Lat;
    String Lng;
    String Desc;

    String type;//maual or auto

    int data_type;// 0 or Null or not stored in server when only historical
    //1 when we have live sensor data
    //2 when we have both type of data for this place

    double range;

    Places()
    {
        Place_name="";
        Lat="";
        Lng="";
        Desc="";
        Place_id=-1;
        data_type=0;
        range=-1;
    }
}
