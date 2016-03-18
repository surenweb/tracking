package fastLibrary;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class FastTask {

	public static String sq(String str)
	{
		if(str==null)
			return "";
		
		return "'"+ str.replace("'", "''") + "'" ;
	}
	
	public static boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) FastApp.getContext().getSystemService(FastApp.getContext().CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public boolean IsInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
