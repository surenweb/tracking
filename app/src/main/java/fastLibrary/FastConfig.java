package fastLibrary;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;

public class FastConfig {


		//## Application Running Variable
		public static String appRunningPatrolID;

		public static Typeface fontMangal;

		//## Preference Setting Variable
		//User-Setting
		public static String appMobileID;
		public static String appStaffID;
		public static String appPhoneNo;
		public static String appEmail;
		public static String appVerified;

		//Important-Setting
		public static String appServerUrl ;
		public static String appRefreshTime ;
		public static String appLanguage ;

		//##Debug
		public static String appLogTag="myApp";

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
			appMobileID = settings.getString("appMobileID", "0");
			appStaffID = settings.getString("appStaffID", "0");
			appPhoneNo = settings.getString("appPhoneNo", "0");
			appEmail = settings.getString("appEmail", "0");
			appVerified = settings.getString("appVerified", "0");

			appServerUrl =  settings.getString("appServerUrl", "http://172.16.5.236/apfLocation/");
			appRefreshTime =  settings.getString("appRefreshTime", "10");
			appLanguage = settings.getString("appLanguage", "NP");

			fontMangal = Typeface.createFromAsset(FastApp.getContext().getAssets(),"font/Kalimati.ttf");

		}

		public static void SaveChanges () {
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(FastApp.getContext());
			Editor edit = settings.edit();

			//User Setting
			edit.putString("appMobileID", appMobileID );
			edit.putString("appStaffID", appStaffID );
			edit.putString("appPhoneNo", appPhoneNo );
			edit.putString("appEmail", appEmail );
			edit.putString("appVerified", appVerified );
			//IMP SETTING
			edit.putString("appServerUrl", appServerUrl );
			edit.putString("appRefreshTime",appRefreshTime);
			edit.putString("appLanguage",appLanguage);
			edit.commit();
		}

		public static void PrepareDB()
		{
			FastDb db = new FastDb("dbLocation",FastApp.getContext());
			//## Create Database-Table if Not Exists

			//## TABLE : patrol
			String sql = "CREATE TABLE IF NOT EXISTS patrol " +
					"(ID INTEGER PRIMARY KEY, MobileID TEXT,CaseTitle TEXT,PatrolType TEXT,PatrolOn TEXT,TotalNafri TEXT, " +
					" StartDate TEXT,EndDate TEXT,IsFinished INTEGER default 0,SyncStatus INTEGER default 0 );";

			if(!db.RunDml(sql))
				Log.d(FastConfig.appLogTag, "Could't Create Patrol Table");

			//## TABLE : patrol_track
			sql = "CREATE TABLE IF NOT EXISTS patrol_track " +
					"(ID INTEGER PRIMARY KEY, MobileID INTEGER,PatrolID INTEGER,Lat TEXT,Lon TEXT," +
					" GpsDate TEXT,Accuracy double,SyncStatus INTEGER default 0);";

			if(!db.RunDml(sql))
				Log.d(FastConfig.appLogTag,"Could't Create Track Table");


			//## TABLE : EVENT
			sql = "CREATE TABLE IF NOT EXISTS event " +
					"(ID INTEGER PRIMARY KEY, MobileID INTEGER, Event TEXT,EventDetail TEXT,EventImage TEXT,Lat TEXT,Lon TEXT," +
					" Accuracy TEXT,CreatedDate TEXT,SyncStatus INTEGER default 0);";

			if(!db.RunDml(sql))
				Log.d(FastConfig.appLogTag,"Could't Create event Table");

			//## TABLE : event_image MobileID,EventID,Title,FilePath,SyncStatus
			sql = "CREATE TABLE IF NOT EXISTS event_image " +
					"(ID INTEGER PRIMARY KEY, MobileID INTEGER, EventID INTEGER,Title TEXT,FilePath TEXT," +
					" SyncStatus INTEGER default 0);";

			if(!db.RunDml(sql))
				Log.d(FastConfig.appLogTag,"Could't Create event_image Table");


			sql = "CREATE TABLE IF NOT EXISTS app_meta_data (" +
					" ID INTEGER PRIMARY KEY, Tag TEXT, Value TEXT, Title TEXT,TitleNp TEXT, Serial INTEGER," +
					" IsActive INTEGER, UpdateDate TEXT )";

			if(!db.RunDml(sql))
				Log.d(FastConfig.appLogTag,"Could't create Meta Data Table");
			else
			{
				sql = "INSERT INTO app_meta_data (ID, Tag, Value, Title, TitleNp, Serial, IsActive, UpdateDate) VALUES " +
						"(1, 'PatrolType', 'regular', 'Regular', 'दैनिक पट्रोल', 1, 1, '2016-03-15 12:48:12'),  " +
						" (2, 'PatrolType', 'special', 'Special', 'बिशेष अपरेशन', 1, 1, '2016-03-15 12:48:14'),  " +
						" (3, 'PatrolType', 'night', 'Night Patrol', 'रात्रि पट्रोल', 1, 1, '2016-03-15 12:48:16'),  " +
						" (4, 'PatrolOn', 'foot', 'Foot', 'पैदल', 1, 1, '2016-03-15 12:48:23'),  " +
						" (5, 'PatrolOn', 'cycle', 'Cycle', 'साईकल', 1, 1, '2016-03-15 12:48:25'),  " +
						" (6, 'PatrolOn', 'motercycle', 'Moter Cycle', 'मोटर साईकल ', 1, 1, '2016-03-15 12:48:26'),  " +
						" (7, 'PatrolOn', 'vehicle', 'Vehicle', 'सवारि साधन', 1, 1, '2016-03-15 12:48:28'),  " +
						" (8, 'EventType', 'fire', 'Fire', 'आगलागि', 1, 1, '2016-03-15 15:57:32'),  " +
						" (9, 'EventType', 'strike', 'Strike', 'धर्ना', 1, 1, '2016-03-15 15:57:54'),  " +
						" (10, 'EventType', 'damage', 'Damage', 'तोडफोड', 1, 1, '2016-03-15 15:58:24'),  " +
						" (11, 'EventType', 'theft', 'Theft', 'चोरि', 1, 1, '2016-03-15 16:00:12'),  " +
						" (12, 'EventType', 'landslide', 'Landslide', 'पहिरो', 1, 1, '2016-03-15 16:01:32'),  " +
						" (13, 'EventType', 'road_block ', 'Road Block', 'सडक बन्द', 1, 1, '2016-03-15 16:02:56'),  " +
						" (14, 'EventType', 'agm', 'Aam-Sabha', 'आमसभा', 1, 1, '2016-03-15 16:04:39'),  " +
						" (15, 'EventType', 'explosion', 'Explosion', 'बिस्फोट', 1, 1, '2016-03-15 16:05:25'),  " +
						" (16, 'EventType', 'firing', 'Firing', 'गोलि प्रहार', 1, 1, '2016-03-15 16:06:00');";

				if(!db.RunDml(sql))
					Log.d(FastConfig.appLogTag,"Could't Insert Data in Meta Data Table " );
			}

			//Fill Running Info
			ClassPatrol patrol = new ClassPatrol() ;
			FastConfig.appRunningPatrolID = patrol.GetRunningPatrolID();

		}
}
