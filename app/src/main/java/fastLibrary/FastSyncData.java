package fastLibrary;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 3/11/2016.
 */
public class FastSyncData {

    public void DoSync()
    {
        ScheduledExecutorService scheduler =   Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    @Override
                    public void run() {

                        if (!FastTask.isNetworkAvailable()) {
                            Log.d("myApp", "Network not ready");
                            return;
                        }

                        UploadPatrol();

                        UploadTrack();

                        UploadActivity();

                    }
                } , 0, 5, TimeUnit.SECONDS ); //## Repeat In 5 Seconds

    }


    //## == Upload Patrol ==
    private void UploadPatrol()
    {
        //## UPLOAD PATROL
        ClassPatrol patrol = new ClassPatrol();
        FastKeyValue patrol_keyval = patrol.GetFson(5); // Get Fson of 5 Patrol

        if (patrol_keyval.value.length() < 5) return;

        String resText = CallServer("","patrol", patrol_keyval.value);

        Log.d("myApp", "Patrol-Upload Responce :" + resText);

        if(resText.length()<1 )  return ;

        String resChar = resText.substring(0, 1); // resText May contain other info from service page

        if(!resChar.equalsIgnoreCase("1"))  return;


        if (patrol.SetSynced(patrol_keyval.column))
            Log.d("myApp", "Patrol Synced & updated, ID : " + patrol_keyval.column);
        else
            Log.d("myApp", "Patrol Synced but not updated, ID : " + patrol_keyval.column);

    }

    //## == Upload Track ==
    private void UploadTrack()
    {
        //## UPLOAD Track
        ClassPatrolTrack patrol_track = new ClassPatrolTrack();
        FastKeyValue track_keyval = patrol_track.GetFson(5); // Get Fson of 5 Patrol

        if (track_keyval.value.length() < 5) return;

        String resText = CallServer("", "track", track_keyval.value);

        Log.d("myApp", "Track-Upload Responce :" + resText);

        if(resText.length()<1 )  return ;

        String resChar = resText.substring(0, 1); // resText May contain other info from service page

        if(!resChar.equalsIgnoreCase("1")) return;


        if (patrol_track.SetSynced(track_keyval.column))
            Log.d("myApp", "Track Synced & updated :" + track_keyval.column);
        else
            Log.d("myApp", "Track Synced but not updated, ID : " + track_keyval.column);

    }
    //## == Upload Activities ==
    private void UploadActivity()
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

}//## CLASS ENDS
