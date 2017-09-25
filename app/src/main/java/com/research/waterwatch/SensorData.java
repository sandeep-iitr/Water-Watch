package com.research.waterwatch;

/**
 * Created by admin on 5/18/2015.
 */
public class SensorData {

    String	Place_id="";
    String	Place_name="";
    String	Lat="";
    String	Lng="";
    String	Year="";
    String	Month="";
    String  Datetime="";
    String	Air_Temperature="-1.0";//
    String	Water_Temperature="-1.0";//
    String	DO="-1.0";//
    String	BOD="-1.0";//
    String	COD="-1.0";//
    String	pH="-1.0";//
    String	Turbidity="-1.0";//
    String	Conductivity="-1.0";//
    String	TC="-1.0";//
    String	FC="-1.0";//
    String	FS="-1.0";//
    String	Velocity="-1.0";//
    String	Hardness="-1.0";//
    String	Alkalinity="-1.0";//
    String	Chloride="-1.0";//
    String	Sulphate="-1.0";//
    String	Cadmium="-1.0";//
    String	Chromium="-1.0";//
    String	Nickel="-1.0";//
    String	Iron="-1.0";//
    String	Na="-1.0";//

    String	TDS="-1.0";
    String	Nitrate="-1.0";
    String Lead="-1.0";//added on 25-Dec-2015

    //live sensor parameters
    String	source="";
    double	lat=-1;
    double	lng=-1;
    String  date="";
    String  time ="";

    Double	temp=-1.0;
    Double	ph=-1.0;
    Double  mvph=-1.0;
    Double	orpmv=-1.0;
    Double	ecscm=-1.0;
    Double  ecabsscm=-1.0;
    Double  resohmcm=-1.0;
    Double	tdsppm=-1.0;
    Double	salpsu=-1.0;

    Double	sigmat=-1.0;
    Double	presspsi=-1.0;
    Double	Do=-1.0;//do is not allowed
    Double	doppm=-1.0;
    Double	domgl=-1.0;
    Double	turbfnu=-1.0;
    Double	ppmni=-1.0;

}
