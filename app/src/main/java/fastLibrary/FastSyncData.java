package fastLibrary;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by root on 3/11/2016.
 */
public class FastSyncData {

    Timer timer = new Timer();

    public void DoSync()
    {

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // Do your task

                if (!FastTask.isNetworkAvailable()) {
                    Log.d("myApp", "Network not ready");
                    return;
                }

                if (FastConfig.appMobileID == null) {
                    Log.d("myApp", "Config file not loaded ");
                    return;
                }

                UploadPatrol();

                UploadTrack();

                UploadActivity();

                UpdateMetaData();

            }

        }, 0, 5000); // 5000 for 5 seconds

    }


    //## == Upload Patrol ==
    private void UploadPatrol()
    {
        //## UPLOAD PATROL
        ClassPatrol patrol = new ClassPatrol();
        FastKeyValue patrol_keyval = patrol.GetFson(5); // Get Fson of 5 Patrol

        if (patrol_keyval.value.length() < 5) return;

        //String resText = CallServer("","patrol", patrol_keyval.value);
        String resText = Call_One("", "patrol", patrol_keyval.value);

        Log.d("myApp", "Patrol-Upload Responce :" + resText);

        if(resText.length()<1 )  return ;

        String resChar = resText.substring(0, 1); // resText May contain other info from service page

        if(!resChar.equalsIgnoreCase("1"))  return;


        if (patrol.SetSynced(patrol_keyval.key))
            Log.d("myApp", "Patrol Synced & updated, ID : " + patrol_keyval.key);
        else
            Log.d("myApp", "Patrol Synced but not updated, ID : " + patrol_keyval.key);

    }

    //## == Upload Track ==
    private void UploadTrack()
    {
        //## UPLOAD Track
        ClassPatrolTrack patrol_track = new ClassPatrolTrack();
        FastKeyValue track_keyval = patrol_track.GetFson(5); // Get Fson of 5 Patrol

        if (track_keyval.value.length() < 5) return;

        // String resText = CallServer("", "track", track_keyval.value);
        String resText = Call_One("", "track", track_keyval.value);


        Log.d("myApp", "Track-Upload Responce :" + resText);

        if(resText.length()<1 )  return ;

        String resChar = resText.substring(0, 1); // resText May contain other info from service page

        if(!resChar.equalsIgnoreCase("1")) return;


        if (patrol_track.SetSynced(track_keyval.key))
            Log.d("myApp", "Track Synced & updated ID :" + track_keyval.key);
        else
            Log.d("myApp", "Track Synced but not updated, ID : " + track_keyval.key);

    }
    //## == Upload Activities ==
    private void UploadActivity()
    {

    }

    //## == Update Settings And Metadata for app if any changes in server ==
    private void UpdateMetaData()
    {

    }


    //## MASTER SERVER HTTP CALLER
    private String CallServer(String page,String task,String data ) {
        String resText ="";

        if (!FastTask.isNetworkAvailable()) {
            Log.d("myApp", "Network not ready");
            return resText ;
        }

        try {
            URL url = new URL(FastConfig.appServerUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("task", task )
                    .appendQueryParameter("data", data );

            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    resText += line;
                }
            } else {
                resText = "";
            }
        }
        catch (Exception e )  {
            Log.d("myApp", "HTTP Client ERROR :" + e.getMessage().toString());
        }

        return resText;
    } //## MASTER ENDS



    private String Call_One (String page,String task,String data) {

        HttpURLConnection urlConnection = null;
        URL url = null;
        String response = "";
        InputStream inStream = null;

        try {
            url = new URL(FastConfig.appServerUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("task", task)
                    .appendQueryParameter("data", data);

            String query = builder.build().getEncodedQuery();

            byte[] outputBytes = query.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputBytes);

            int responseCode = urlConnection.getResponseCode();


            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp ;
                while ((temp = bReader.readLine()) != null) {
                    response += temp;
                }
            } else {
                Log.e("myApp", "14 - False - HTTP_OK");
                response = "";
            }

        } catch (Exception e) {
            if(e.getMessage()!=null )
                Log.d("myApp", "HTTP HttpURLConnection ERROR :" + e.getMessage().toString());
            else
                Log.d("myApp", "HTTP HttpURLConnection ERROR :" + e.toString());

        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                    Log.d("myApp", "IOException ERROR :" + ignored.getMessage().toString());
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return response ;
    } //## CAll_ONE ENDS

}//## CLASS ENDS
