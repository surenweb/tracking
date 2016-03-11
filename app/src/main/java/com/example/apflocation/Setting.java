package com.example.apflocation;

import fastLibrary.FastApp;
import fastLibrary.FastConfig;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

public class Setting extends Activity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		//## Load Default Setting
    	String strMobileID = FastConfig.appMobileID ;
    	String strUrl = FastConfig.appServerUrl ;
    	String strRefreshRate = FastConfig.appRefreshTime ;
    	
		((EditText)findViewById(R.id.txtSettingMobileID)).setText(strMobileID);
    	((EditText)findViewById(R.id.txtSettingUrl)).setText(strUrl);
    	((EditText)findViewById(R.id.txtSettingRefreshRate)).setText(strRefreshRate);	
    	
    	//Hide Form 
    	TableLayout tblSettingForm = (TableLayout)findViewById(R.id.tblSettingForm );
		tblSettingForm.setVisibility(View.INVISIBLE);

		//## Check Pass Code 
		Button btnCheckPasscode =(Button) findViewById(R.id.btnCheckPasscode );  //Check Passcode 	
		btnCheckPasscode.setOnClickListener(new OnClickListener() {	      
	        @Override
	       	public void onClick(View v) {		        	       
	        
	        	EditText txtSettingPassCode =  (EditText)findViewById(R.id.txtSettingPassCode ); 
	        	String strPass = txtSettingPassCode.getText().toString() ;
	        	if(strPass.equalsIgnoreCase("apf@9753"))
	        	{
	        		TableLayout tblSettingForm = (TableLayout)findViewById(R.id.tblSettingForm );
	        		tblSettingForm.setVisibility(View.VISIBLE);
	        	}
	        	else 
	        	{
	        		  Toast.makeText(FastApp.getContext(), "Incorrect Password ", Toast.LENGTH_LONG).show();
	        	}
	        }
		});	
		
		//## Save Setting 
		Button btnSaveSetting =(Button) findViewById(R.id.btnSaveSetting );  //End Patrol Button 	
		btnSaveSetting.setOnClickListener(new OnClickListener() {	      
	        @Override
	       	public void onClick(View v) {	       	       
	        	String strMobileID = ((EditText)findViewById(R.id.txtSettingMobileID)).getText().toString();
	        	String strUrl = ((EditText)findViewById(R.id.txtSettingUrl)).getText().toString();
	        	String strRefreshRate = ((EditText)findViewById(R.id.txtSettingRefreshRate)).getText().toString();

				FastConfig.appMobileID = strMobileID ;
				FastConfig.appServerUrl = strUrl ;
				FastConfig.appRefreshTime = strRefreshRate ;
				FastConfig.SaveChanges();

	        	Toast.makeText(FastApp.getContext(), "Setting Saved Successfully", Toast.LENGTH_LONG).show();
	        	
	        	//## Go To DashBoard	         		        		
                finish(); 
                return;  // Goes To Main Activity 
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
