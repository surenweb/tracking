package com.example.apflocation;


import java.text.SimpleDateFormat;
import java.util.Date;
import fastLibrary.ClassPatrol;
import fastLibrary.FastApp;
import fastLibrary.FastConfig;
import fastLibrary.FastGpsListener;
import fastLibrary.FastLocation;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.TextView;

public class Patrol extends Activity  {


	private TextView txtLat;
	private TextView txtLon;
	private TextView txtGpsDate;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patrol);
		
		//## RUNNING Patrol ##//
  	    if(!FastConfig.appRunningPatrolID.equalsIgnoreCase(""))
		{
    		//## Fill Patrol Form 
    		ClassPatrol patrol = new ClassPatrol(Integer.parseInt( FastConfig.appRunningPatrolID));
    		
    		TextView txtCaseTitle = (TextView)findViewById(R.id.txtCaseTitle) ;	
    		txtCaseTitle.setText(patrol.CaseTitle);
    		
    		TextView txtStartDate = (TextView)findViewById(R.id.txtStartDate) ;	
    		txtStartDate.setText(patrol.StartDate);
    		
    		TextView txtTotalTracks = (TextView)findViewById(R.id.txtTotalTracks) ;	
    		int totTrack = patrol.TotalSyncTrack + patrol.TotalUnSyncTrack ;
    		txtTotalTracks.setText(totTrack+"");
    		
    		TextView txtTotalUnsync = (TextView)findViewById(R.id.txtTotalUnsync) ;	
    		txtTotalUnsync.setText( patrol.TotalUnSyncTrack+"");
    		
    		
    	}
		
    	txtLat = (TextView)findViewById(R.id.txtLat);
 	    txtLon = (TextView)findViewById(R.id.txtLon);  
 	    txtGpsDate = (TextView)findViewById(R.id.txtGpsDate );
 	    
 	    txtLat.setText(FastLocation.Lat);
	    txtLon.setText(FastLocation.Lon);
	    txtGpsDate.setText(FastLocation.UpdateDate);      
		
	    //## Location Changed Call Backs -Update UI 
		FastApp.getLocation().setOnEventListener(new FastGpsListener() {
	             public void onLocationChangedEvent() { 
	            	 
	         	    txtLat.setText(FastLocation.Lat);
	         	    txtLon.setText(FastLocation.Lon);
	         	    txtGpsDate.setText(FastLocation.UpdateDate);  
	             }

				@Override
				public void onTrackAddedEvent() {					
					//## Update Total Track Textbox
         	    	TextView txtTotalTracks = (TextView)findViewById(R.id.txtTotalTracks); 	         	    	
         	    	int totTrack = Integer.parseInt(txtTotalTracks.getText().toString());
         	    	totTrack = totTrack + 1 ;
         	    	txtTotalTracks.setText(totTrack+"");
         	    	
         	    	//## Update Total Unsync Track Textbox
         	    	TextView txtTotalUnsync = (TextView)findViewById(R.id.txtTotalUnsync); 	         	    	
         	    	int totUnsync = Integer.parseInt(txtTotalUnsync.getText().toString());
         	    	totUnsync = totUnsync + 1 ;
         	    	txtTotalUnsync.setText(totUnsync+"");
				}
	        });
		
				
		Button btnEndPatrol =(Button) findViewById(R.id.btnEndPatrol );  //End Patrol Button 	
		btnEndPatrol.setOnClickListener(new OnClickListener() {	      
	        @Override
	       	public void onClick(View v) {		        	        	 
	        	
	        	
	        	 //End That Patrol
	        	 ClassPatrol patrol = new ClassPatrol(Integer.parseInt( FastConfig.appRunningPatrolID));
	        	 patrol.EndDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
	        	 patrol.IsFinished = 1 ;
	        	
	        	 if(patrol.Update())
	        	 {	       		
	        		Log.d("myLog","Patrol Updated as Fininshed" );

					 FastConfig.appRunningPatrolID ="";
	        		
        		 	//## Go To DashBoard	         		        		
	                finish(); 
	                return;  // Goes To Main Activity 
	                
	        	 }
	        	
	        	 
	       }
		});		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}  //## END OF CLASS 
