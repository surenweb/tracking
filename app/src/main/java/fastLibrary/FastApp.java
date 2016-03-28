package fastLibrary;

import java.util.Timer;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

public class FastApp extends Application {

	//## API KEY - GOOGLE MAP - AIzaSyDJq_o19z9ao6D1B4kxqT0Je1V0-NDYG-Q

	//## private
	private static FastLocation locListener;
	private static Context mContext;
	private Timer timer = new Timer();
	private FastServer server = new FastServer();

	@Override
	public void onCreate() {
		super.onCreate();
		if (mContext == null) {
			mContext = getApplicationContext();
			Log.d(FastConfig.appLogTag, "context Added");
		}

		if (locListener == null) {
			LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locListener = new FastLocation();

			try {
					locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 20, locListener); //## 3 sec & 20 Meter
					Log.d(FastConfig.appLogTag, "Location update requested from context");
				} catch (Exception e) {
					Toast.makeText(mContext, "Location Permission Not Found", Toast.LENGTH_SHORT).show();
				}
			}

		FastConfig.LoadConfig();
		FastConfig.PrepareDB();

		SyncData();

	}
	public static Context getContext() {

		return mContext;
	}
	public static FastLocation getLocation()
	{
		return locListener;
	}


	//## =========== CUSTOM METHODS =============== //

	private void SyncData() {

		server.DoSync();
	}


		
}
