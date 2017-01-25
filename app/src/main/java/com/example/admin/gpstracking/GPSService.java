package com.example.admin.gpstracking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;


public class GPSService extends Service{

    private LocationManager locationManager;
    private String provider;
    private MyLocationListener mylistener;
    private Criteria criteria;

    String msg="",username,url;

    String latitude,longitude,altitude,city,pincode,sublocality,country_name,country_code,locality,state,add_line1,adddata;
    double lati,longi,alti;




    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();


        username=intent.getStringExtra("username");



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        //criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default

        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setCostAllowed(false);



        provider = locationManager.getBestProvider(criteria, false);



        mylistener = new MyLocationListener();
        try {
            locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
        }
        catch(Exception e){}





        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private class MyLocationListener implements LocationListener {



        @Override

        public void onLocationChanged(Location location) {



            lati=location.getLatitude();
            alti=location.getAltitude();
            longi=location.getLongitude();

            latitude=lati+"";
            longitude=longi+"";
            altitude=alti+"";



            Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> list = gc.getFromLocation(lati,longi,1);

                String stradd = "";

                if(list == null || list.size()== 0){
                    Toast.makeText(getApplicationContext(), "No Address Found", Toast.LENGTH_LONG).show();
                }

                else{

                    Address add = list.get(0);
                    locality=add.getLocality();
                    // pincode=add.getPostalCode();
                    sublocality=add.getSubLocality();
                    country_name=add.getCountryName();
                    country_code=add.getCountryCode();
                    state=add.getAdminArea();
                    add_line1 = add.getAddressLine(0);

                    adddata=add.getAddressLine(2);
                    String[] addparts = adddata.split(" ");
                    pincode=addparts[2];




                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            String msg="Longitude : "+longitude+"\nLatitude : "+latitude+"\nAltitude : "+altitude+"\nCity : "+locality+"\nsublocality : "+sublocality+"\npincode : "+pincode;


            Toast.makeText(GPSService.this,  " Location is : \n"+msg,Toast.LENGTH_SHORT).show();


            // url="http://192.168.1.96:8080/KTCAndroidServlet/GPSInsert";
            url="http://172.16.137.151:8080/KTCAndroidServlet/GPSInsert";

            //MyServletTask t=new MyServletTask();
            //t.execute(url);


        }



        @Override

        public void onStatusChanged(String provider, int status, Bundle extras) {

            // Toast.makeText(GPSService.this, provider + "'s status changed to "+status +"!",Toast.LENGTH_SHORT).show();

        }



        @Override

        public void onProviderEnabled(String provider) {

            Toast.makeText(GPSService.this, "Provider " + provider + " enabled!",Toast.LENGTH_SHORT).show();



        }



        @Override

        public void onProviderDisabled(String provider) {

            Toast.makeText(GPSService.this, "Provider " + provider + " disabled!",Toast.LENGTH_SHORT).show();

        }

    }







}
