package com.example.apflocation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import fastLibrary.AdapterSpinner;
import fastLibrary.ClassMetaData;
import fastLibrary.ClassPatrol;
import fastLibrary.FastApp;
import fastLibrary.FastConfig;
import fastLibrary.FastKeyValue;

public class PatrolCreate extends Activity  {


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patrol_create);
		
		Button btnStartPatrol =(Button) findViewById(R.id.btnStartPatrol );  //Create Patrol Button 	

		// Patrol Type Spinner
		List<FastKeyValue> lstPatrolType = new ClassMetaData().GetAllByTag("PatrolType");

		AdapterSpinner adapterPatrolType = new AdapterSpinner(FastApp.getContext(),
				R.layout.layout_spinner_item,
				lstPatrolType);

		adapterPatrolType.setDropDownViewResource(R.layout.layout_spinner_dropdown_item);

		Spinner spinPatrolType = (Spinner) findViewById(R.id.spinPatrolType);
		spinPatrolType.setAdapter(adapterPatrolType); // Set the custom adapter to the spinner


	    //## Patrol On Fill
		List<FastKeyValue> lstPatrolOn = new ClassMetaData().GetAllByTag("PatrolOn");

		AdapterSpinner adapterPatrolOn = new AdapterSpinner(FastApp.getContext(),
				R.layout.layout_spinner_item,
				lstPatrolOn);
		adapterPatrolOn.setDropDownViewResource(R.layout.layout_spinner_dropdown_item);

		Spinner spinPatrolOn = (Spinner) findViewById(R.id.spinPatrolOn);
		spinPatrolOn.setAdapter(adapterPatrolOn); // Set the custom adapter to the spinner

		/*
	  	adapterOn = new ArrayAdapter<String>(getApplicationContext(),R.layout.layout_spinner_item, listOn);
  		adapterOn.setDropDownViewResource(R.layout.layout_spinner_dropdown_item);
  	    spinPatrolOn.setAdapter(adapterOn); */
	    
	    
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
	        	 patrol.PatrolType = ((FastKeyValue)spinPatrolType.getSelectedItem()).key;
	        	 patrol.PatrolOn = ((FastKeyValue)spinPatrolOn.getSelectedItem()).key;
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
