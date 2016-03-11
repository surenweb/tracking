package fastLibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class FastConfig {


		//## Application Running Variable
		public static String appRunningPatrolID;

		//## Preference Setting Variable
		public static String appMobileID;
		public static String appServerUrl;
		public static String appRefreshTime;

		//## Constructor
		/*
		private SharedPreferences settings;
		public FastConfig()
		{
			settings = PreferenceManager.getDefaultSharedPreferences(FastApp.getContext());
			LoadConfig ();
		}
		*/
		public static void LoadConfig ()
		{
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(FastApp.getContext());
			appMobileID = settings.getString("appMobileID", "-1");
			appServerUrl =  settings.getString("appServerUrl", "-1");
			appRefreshTime =  settings.getString("appRefreshTime", "-1");
		}

		public static void SaveChanges () {
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(FastApp.getContext());
			Editor edit = settings.edit();
			edit.putString("appMobileID", appMobileID );
			edit.putString("appServerUrl", appServerUrl );
			edit.putString("appRefreshTime",appRefreshTime);
			edit.commit();
		}
}
