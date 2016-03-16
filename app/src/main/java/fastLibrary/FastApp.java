package fastLibrary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class FastApp extends Application {

	//## private
	private static FastLocation locListener;
	private static Context mContext;
	private Timer timer = new Timer();
	private FastSyncData syncData = new FastSyncData();

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
		FastConfig.Prepare();
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

		syncData.DoSync();
	}


		
}
