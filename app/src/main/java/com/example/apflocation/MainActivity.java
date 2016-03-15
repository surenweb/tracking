package com.example.apflocation;
import fastLibrary.FastApp;
import fastLibrary.FastConfig;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnPatrol =(Button) findViewById(R.id.btnPatrol ); 		
		Button btnSetting =(Button) findViewById(R.id.btnSetting ); 
		Button btnExit =(Button) findViewById(R.id.btnExit ); 
		Button btnEvent = (Button) findViewById(R.id.btnEvent);

		TextView lblMainMobileID = (TextView)findViewById(R.id.lblMainMobileID ) ;	
		lblMainMobileID.setText("Mobile ID : "+ FastConfig.appMobileID );
		
		TextView lblMainServer = (TextView)findViewById(R.id.lblMainServer ) ;
		lblMainServer.setText("Server : "+ FastConfig.appServerUrl );
		
				
		if(!FastConfig.appRunningPatrolID.equalsIgnoreCase(""))
		{    		
    		btnPatrol.setText("Running Patrol");
		}
		
		//## Button Click Listner 
		btnPatrol.setOnClickListener(new OnClickListener() {	      
		        @Override
		       	public void onClick(View v) {	
			       	Intent intent;
			       	if(!FastConfig.appRunningPatrolID.equalsIgnoreCase(""))
					{  
			       		intent = new Intent(MainActivity.this,Patrol.class);
					}
			       	else
			       	{ 
			       		intent = new Intent(MainActivity.this,PatrolCreate.class);
			       	}
			       	
			       	intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			       	startActivity(intent);
		       }
		});

		btnEvent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,EventCreate.class);
				startActivity(intent);
			}
		});

		btnSetting.setOnClickListener(new OnClickListener() {
	        @Override
	       	public void onClick(View v) {
		       	Intent intent = new Intent(MainActivity.this,Setting.class);
		       	startActivity(intent);
		       }
		});


		btnExit.setOnClickListener(new OnClickListener() {
	        @Override
	       	public void onClick(View v) {	
	        	MainActivity.this.finish();
	       }
		});
		
		//## Button Click Listner Ends Here 
		
		
    	    	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResume(){
	    super.onResume();
	    // put your code here...
	    
	    Button btnPatrol =(Button) findViewById(R.id.btnPatrol ); 	
		//Fill Running Info	
	    
		if(!FastConfig.appRunningPatrolID.equalsIgnoreCase(""))
		{    		
    		btnPatrol.setText("Running Patrol");
		}
		else {
			btnPatrol.setText("New Patrol");
		}
		
		FastConfig share = new FastConfig();
		TextView lblMainMobileID = (TextView)findViewById(R.id.lblMainMobileID ) ;	
		lblMainMobileID.setText("Mobile ID : "+ FastConfig.appMobileID );
		
		TextView lblMainServer = (TextView)findViewById(R.id.lblMainServer ) ;
		lblMainServer.setText("Server : "+ FastConfig.appServerUrl );
		
	}
}
