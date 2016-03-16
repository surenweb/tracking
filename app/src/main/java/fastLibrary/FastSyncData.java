package fastLibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.example.apflocation.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
                    Log.d(FastConfig.appLogTag, "Network not ready");
                    return;
                }

                if (FastConfig.appMobileID == null) {
                    Log.d(FastConfig.appLogTag, "Config file not loaded ");
                    return;
                }

                UploadPatrol();

                UploadTrack();

                UploadEvent();

                UploadImage();
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


        List<FastKeyValue> lstPostParams = new ArrayList<FastKeyValue>();
        lstPostParams.add(new FastKeyValue("task","patrol"));
        lstPostParams.add(new FastKeyValue("data",patrol_keyval.value));

        String resText = Call_One("", lstPostParams );

        Log.d(FastConfig.appLogTag, "Patrol-Upload Responce :" + resText);

        if(resText.length()<1 )  return ;

        String resChar = resText.substring(0, 1); // resText May contain other info from service page

        if(!resChar.equalsIgnoreCase("1"))  return;


        if (patrol.SetSynced(patrol_keyval.key))
            Log.d(FastConfig.appLogTag, "Patrol Synced & updated, ID : " + patrol_keyval.key);
        else
            Log.d(FastConfig.appLogTag, "Patrol Synced but not updated, ID : " + patrol_keyval.key);

    }

    //## == Upload Track ==
    private void UploadTrack()
    {
        //## UPLOAD Track
        ClassPatrolTrack patrol_track = new ClassPatrolTrack();
        FastKeyValue track_keyval = patrol_track.GetFson(5); // Get Fson of 5 Patrol

        if (track_keyval.value.length() < 5) return;

        List<FastKeyValue> lstPostParams = new ArrayList<FastKeyValue>();
        lstPostParams.add(new FastKeyValue("task","track"));
        lstPostParams.add(new FastKeyValue("data",track_keyval.value));

        String resText = Call_One("", lstPostParams );


        Log.d(FastConfig.appLogTag, "Track-Upload Responce :" + resText);

        if(resText.length()<1 )  return ;

        String resChar = resText.substring(0, 1); // resText May contain other info from service page

        if(!resChar.equalsIgnoreCase("1")) return;


        if (patrol_track.SetSynced(track_keyval.key))
            Log.d(FastConfig.appLogTag, "Track Synced & updated ID :" + track_keyval.key);
        else
            Log.d(FastConfig.appLogTag, "Track Synced but not updated, ID : " + track_keyval.key);

    }
    //## == Upload Events  ==
    private void UploadEvent()
    {
        //## UPLOAD Track
        ClassEvent event = new ClassEvent();
        FastKeyValue event_hash = event.GetFson(5); // Get Fson of 5 Patrol

        if (event_hash.value.length() < 5) return;

        List<FastKeyValue> lstPostParams = new ArrayList<FastKeyValue>();
        lstPostParams.add(new FastKeyValue("task","event"));
        lstPostParams.add(new FastKeyValue("data",event_hash.value));

        String resText = Call_One("", lstPostParams );
        Log.d(FastConfig.appLogTag, "Event-Upload Responce :" + resText);

        if(resText.length()<1 )  return ;

        String resChar = resText.substring(0, 1); // resText May contain other info from service page

        if(!resChar.equalsIgnoreCase("1")) return;


        if (event.SetSynced(event_hash.key))
            Log.d(FastConfig.appLogTag, "Event Synced & updated ID :" + event_hash.key);
        else
            Log.d(FastConfig.appLogTag, "Event Synced but not updated, ID : " + event_hash.key);

    }

    //## == Update Settings And Metadata for app if any changes in server ==
    private void UploadImage()
    {
        ClassImage image = new ClassImage(0);
        image.LoadTopImage();

        if(image.ID <1 ) return ;

        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile(image.FilePath ,options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();

        // Encode Image to String
        String encodedString = Base64.encodeToString(byte_arr, 0);

        List<FastKeyValue> lstPostParams = new ArrayList<FastKeyValue>();
        lstPostParams.add(new FastKeyValue("task","image"));
        lstPostParams.add(new FastKeyValue("file_name",image.MobileID+"_"+image.EventID+"_"+image.Title));
        lstPostParams.add(new FastKeyValue("data",encodedString));

        String resText = Call_One("", lstPostParams );


        Log.d(FastConfig.appLogTag, "Image-Upload Responce :" + resText);

        if(resText.length()<1 )  return ;

        String resChar = resText.substring(0, 1); // resText May contain other info from service page

        if(!resChar.equalsIgnoreCase("1")) return;


        if (image.SetSynced())
            Log.d(FastConfig.appLogTag, "Image uploaded & updated ID :" + image.ID);
        else
            Log.d(FastConfig.appLogTag, "Image uploaded but not updated, ID : " + image.ID);

    }

    //## == Update app meta data  ==
    private void UpdateMetaData()
    {

    }

    //Master Page Caller
    private String Call_One (String page,List<FastKeyValue> lstParamValue ) {

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

            Uri.Builder builder = new Uri.Builder();
            //Add All Post Params
            for (FastKeyValue paramValue : lstParamValue ) {
                builder.appendQueryParameter(paramValue.key, paramValue.value);
            }

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
                Log.d(FastConfig.appLogTag, "14 - False - HTTP_OK");
                response = "";
            }

        } catch (Exception e) {
            if(e.getMessage()!=null )
                Log.d(FastConfig.appLogTag, "HTTP HttpURLConnection ERROR :" + e.getMessage().toString());
            else
                Log.d(FastConfig.appLogTag, "HTTP HttpURLConnection ERROR :" + e.toString());

        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                    Log.d(FastConfig.appLogTag, "IOException ERROR :" + ignored.getMessage().toString());
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return response ;
    } //## CAll_ONE ENDS

}//## CLASS ENDS
