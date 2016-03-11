package com.example.apflocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import fastLibrary.ClassPatrol;
import fastLibrary.FastApp;
import fastLibrary.FastConfig;

public class PatrolCreate extends Activity  {


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patrol_create);
		
		Button btnStartPatrol =(Button) findViewById(R.id.btnStartPatrol );  //Create Patrol Button 	

		//## Patrol Type Fill 
		Spinner spinPatrolType = (Spinner) findViewById(R.id.spinPatrolType);
	    ArrayAdapter<String> adapter;
	    List<String> list;

	    list = new ArrayList<String>();
	    list.add("Regular");
	    list.add("LRP");
	    list.add("Hasty");
	    
	    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
	    adapter.setDropDownViewResource(R.layout.spinner_dropwn_item);
	    spinPatrolType.setAdapter(adapter);
	    
	    //## Patrol On Fill 
  		Spinner spinPatrolOn = (Spinner) findViewById(R.id.spinPatrolOn);
  	    ArrayAdapter<String> adapterOn;
  	    List<String> listOn;

	  	listOn = new ArrayList<String>();
	  	listOn.add("Foot");
	  	listOn.add("Cycle");
	  	listOn.add("Motercycle");
	  	listOn.add("Vehicle");
	    
	  	adapterOn = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, listOn);
  		adapterOn.setDropDownViewResource(R.layout.spinner_dropwn_item);
  	    spinPatrolOn.setAdapter(adapterOn);
	    
	    
		//## IF RUNNING Patrol Go To That Patrol ##//		
  	    if(!FastConfig.appRunningPatrolID.equalsIgnoreCase(""))
		{    		
    		//## Go To That Patrol 
  	    	Toast.makeText(FastApp.getContext(), "Patrol Already Running", Toast.LENGTH_LONG).show();    		
  	    	//## Go To DashBoard	         		        		
            finish(); 
            return;  // Goes To Main Activity 
		}
    	

    	
    	btnStartPatrol.setOnClickListener(new OnClickListener() {	      
	        @Override
	       	public void onClick(View v) {	
	        	 EditText txtCaseTitle = (EditText)findViewById(R.id.txtCaseTitle) ;	        	 

				 Spinner spinPatrolType = (Spinner)findViewById(R.id.spinPatrolType );
				 Spinner spinPatrolOn = (Spinner)findViewById(R.id.spinPatrolOn );
				 EditText txtTotalTroops = (EditText)findViewById(R.id.txtTotalTroops );
	        	 //Create New Patrol
	        	 ClassPatrol patrol = new ClassPatrol();
	        	 patrol.CaseTitle = txtCaseTitle.getText().toString();
	        	 patrol.PatrolType = spinPatrolType.getSelectedItem().toString();
	        	 patrol.PatrolOn = spinPatrolOn.getSelectedItem().toString();
	        	 patrol.TotalNafri = txtTotalTroops.getText().toString();
	        	 
	        	 patrol.StartDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
	        	
	        	 if(patrol.Update())
	        	 {
	        		 	Toast.makeText(FastApp.getContext(), "Patrol Started", Toast.LENGTH_LONG).show();   
	        		   //# Set ID 
					 	FastConfig.appRunningPatrolID = patrol.GetRunningPatrolID();
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
	
}
