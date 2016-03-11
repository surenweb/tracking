package com.example.apflocation;

import fastLibrary.*;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;

public class SplashScreen extends Activity {
	 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
 
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	
            	Prepare(); //Initialization of Application 
            	
            	Intent i = new Intent(SplashScreen.this, MainActivity.class);
            	startActivity(i);
                finish();
                
            }
        }, SPLASH_TIME_OUT);
    }
 
    private void Prepare()
    {
    	FastDb db = new FastDb("dbLocation",getApplicationContext());
    	//## Create Database-Table if Not Exists 
    	//## TblPatrol
    	String sql = "CREATE TABLE IF NOT EXISTS patrol " +
    			"(ID INTEGER PRIMARY KEY, MobileID TEXT,CaseTitle TEXT,PatrolType TEXT,PatrolOn TEXT,TotalNafri TEXT, " + 
    			" StartDate TEXT,EndDate TEXT,IsFinished INTEGER default 0,SyncStatus INTEGER default 0 );";

		if(!db.RunDml(sql))
			Log.d("myApp","Could't Create Patrol Table");
    	
    	//## TblTrack
    	sql = "CREATE TABLE IF NOT EXISTS patrol_track " +
    			"(ID INTEGER PRIMARY KEY, PatrolID INTEGER,Lat TEXT,Lon TEXT,GpsDate TEXT,Accuracy double,SyncStatus INTEGER default 0);";

		if(!db.RunDml(sql))
            Log.d("myApp","Could't Create Track Table");
    	
    	//## Load Application Config File
    	FastConfig.LoadConfig();

    	if(FastConfig.appMobileID.equalsIgnoreCase("-1") ) //## IF NO Setting
    	{
			FastConfig.appMobileID = "9999";
			FastConfig.appServerUrl = "http://172.16.0.214/apfLocation/upload_service.php";
			FastConfig.appRefreshTime = "10" ;
			FastConfig.SaveChanges();

			Log.d("myLog","Default MobileId Given :" + FastConfig.appMobileID);
    	}
    	else
    	{
			Log.d("myLog","MobileID : "+ FastConfig.appMobileID );
    	}   	
    	
    	//Fill Running Info
		ClassPatrol patrol = new ClassPatrol() ;
		FastConfig.appRunningPatrolID = patrol.GetRunningPatrolID();
    	
    }
    
   
}
