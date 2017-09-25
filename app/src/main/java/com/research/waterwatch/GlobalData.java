package com.research.waterwatch;

import java.util.ArrayList;

/**
 * Created by admin on 5/18/2015.
 */
public class GlobalData {

    public static ArrayList<Places> place = new ArrayList<Places>();

    public static ArrayList<SensorData> sensordata = new ArrayList<SensorData>();

    public static ArrayList<Data_IBM_sensors> IBMsensordata = new ArrayList<Data_IBM_sensors>();

    public static int numPlaces=0;

    public static int Datapoint=0;

    public static int month=1;

    public static int Year=-1;

    public static String current_marker="";//name of the current selected place, used in to show data
   // public static int place_position=-1;//position of current selected place in the Places arraylist,used in to show data
    public static String current_Lat;//lat of current selected place
    public static String current_Lng;//lng of current selected place
    public static String range="-1.0";
    public static double current_range=-1;//range of the current selected place

    public static int general_limit=0;//default 0, means no limit has been read yet
    public static Limits_general lg=new Limits_general();

    public static String Purpose="All";


    public static int start_Day=-1;

    public static int start_month=1;

    public static int start_Year=-1;


    public static int end_Day=-1;

    public static int end_month=1;

    public static int end_Year=-1;

    public static int refer=0;//1==fromMap to place data, 2==from listplaces to place data

    public static String LicKey="C2eV6witgr";
    public static String Serveraddress="https://bluewater.mybluemix.net/";
    public static String username="ibmadmin";//get the data by a particular user
}

