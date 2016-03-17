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

                Intent i;
                if(FastConfig.appMobileID.equalsIgnoreCase("9999"))
            	    i = new Intent(SplashScreen.this, Register.class);
                else
                    i =  new Intent(SplashScreen.this, MainActivity.class);
            	startActivity(i);
                finish();
                
            }
        }, SPLASH_TIME_OUT);
    }
    
   
}
