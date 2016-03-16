package fastLibrary;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class FastLocation implements LocationListener,FastGpsListener{
	 public static String Lat="";
	 public static String Lon="";
	 public static String Accuracy="" ;
	 public static String UpdateDate="" ;
	 public static String ListnerStatus="" ;

 	//EVENT TO Over-ride ON CLIENT 
	 private static FastGpsListener mOnEventListener;

     public void setOnEventListener(FastGpsListener listener) {
    	 if (mOnEventListener == null) //## SingleTone 
    	 {
    		 mOnEventListener = listener;
			 Log.d(FastConfig.appLogTag,"Listner Added to FastLocation");
    	 }
     }

     public void doLocationChangedEvent() {
    	 
         if (mOnEventListener != null)
             	mOnEventListener.onLocationChangedEvent(); // Location event object :)
     }
     
     public void doTrackAddedEvent() {
    	 
         if (mOnEventListener != null)
             	mOnEventListener.onTrackAddedEvent(); // Location event object :)
     }
 	//##
 	
	    @SuppressLint("SimpleDateFormat")
		@Override	   
	    public void onLocationChanged(Location Location) {
		
		        if(Location == null) return ;
		        
	            double plong = Location.getLongitude();
	            double plat = Location.getLatitude();
	            double acc = Location.getAccuracy();
	            
	            Lat = (Double.toString(plat));
	            Lon = (Double.toString(plong));
				Accuracy = (Double.toString(acc));
	            UpdateDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()); 
	            
	            //Fire Location Changed
	            doLocationChangedEvent();
	            
	            
	          
	            if(FastConfig.appRunningPatrolID == null || FastConfig.appRunningPatrolID.equalsIgnoreCase(""))  //## IF Patrol Finished, Return
	            {
					Log.d(FastConfig.appLogTag,"gps location has no running patrol");
	            	return ; 
	            }
	            	
	            //## INSERT TO Database (Patrol Is On)	
	            
         	    ClassPatrolTrack track = new ClassPatrolTrack();
         	    track.Lat = FastLocation.Lat ;
         	    track.Lon = FastLocation.Lon ;
         	    track.GpsDate = FastLocation.UpdateDate ;
         	    track.Accuracy = acc;
         	    
         	    track.PatrolID = Integer.parseInt( FastConfig.appRunningPatrolID);
         	   
         	    if(track.Update())
         	    {
					Log.d(FastConfig.appLogTag,"Track Added" );
         	    	Toast.makeText(FastApp.getContext(), "Lat-"+ Lat + " Lon-"+ Lon, Toast.LENGTH_SHORT).show();
         	    	
         	    	//Fire Track Added 
         	    	doTrackAddedEvent();
         	    }
         	    
	    }

	    @Override
	    public void onProviderDisabled(String provider) {
	        // TODO Auto-generated method stub
	    	ListnerStatus="GpsIsOff";

	    }

	    @Override
	    public void onProviderEnabled(String provider) {
	        // TODO Auto-generated method stub
	    	ListnerStatus="GpsIsOn";
	    }

	    @Override
	    public void onStatusChanged(String provider, int status, 
	            Bundle extras) {
	        // TODO Auto-generated method stub

	    }

		@Override
		public void onLocationChangedEvent() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTrackAddedEvent() {
			// TODO Auto-generated method stub

		}

	}
