package com.example.apflocation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fastLibrary.FastApp;
import fastLibrary.FastConfig;
import fastLibrary.FastKeyValue;
import fastLibrary.FastServer;

public class RegisterVerify extends Activity {

    @Bind(R.id.verify_mobile_id)  EditText _mobileID;
    @Bind(R.id.verify_staff_id)  EditText _staffID;
    @Bind(R.id.verify_phone_number)  EditText _phoneNo;
    @Bind(R.id.verify_email) EditText _emailText;
    @Bind(R.id.btn_reVerify)  Button _reVerify;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verify);
        ButterKnife.bind(this);

        FastConfig.LoadConfig();
        _mobileID.setText(FastConfig.appMobileID);
        _staffID.setText(FastConfig.appStaffID);
        _phoneNo.setText(FastConfig.appPhoneNo);
        _emailText.setText(FastConfig.appEmail);

        _mobileID.setFocusable(false);
        _staffID.setFocusable(false);
        _phoneNo.setFocusable(false);
        _emailText.setFocusable(false);


        _reVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _reVerify.setEnabled(false);

                String staffID = _staffID.getText().toString();
                String mobileID = _mobileID.getText().toString();

                // TODO: Implement your own signup logic here.
                new VerifyMobile()
                        .execute(new String[]{staffID, mobileID});
            }
        });

    }

    public void onVerifySuccess() {

        setResult(RESULT_OK, null);

        Toast.makeText(FastApp.getContext(), "Verified Successfully", Toast.LENGTH_LONG).show();

        FastConfig.appVerified = "1";
        FastConfig.SaveChanges();

        Intent i = new Intent(RegisterVerify.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    //## ASYNC SERVER Verify METHOD
    private class VerifyMobile extends AsyncTask<String, Integer, String> {



        @Override
        protected void onPreExecute() {

           // ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarRegister);
           //  progressBar.setVisibility(View.VISIBLE);

            progressDialog = new ProgressDialog(RegisterVerify.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Checking account ...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String staffID = params[0];
            String mobileID = params[1];

            publishProgress(10); // Calls onProgressUpdate .

            List<FastKeyValue> lstPostParams = new ArrayList<FastKeyValue>();
            lstPostParams.add(new FastKeyValue("task","register_verify"));
            lstPostParams.add(new FastKeyValue("data","hello"));
            lstPostParams.add(new FastKeyValue("staffID", staffID));
            lstPostParams.add(new FastKeyValue("mobileID", mobileID));

            String resText = new FastServer().CallServer("", lstPostParams);
            return resText ;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.d(FastConfig.appLogTag, "Register Mobile response :" + result);

            if(result.length()>0  ) {

                String resChar = result.substring(0, 1); // resText May contain other info from service page

                if(resChar.equalsIgnoreCase("1")){
                    Log.d(FastConfig.appLogTag, "mobile verified ");
                    onVerifySuccess();
                }
                else{
                    Log.d(FastConfig.appLogTag, "mobile not verified ");
                    Toast.makeText(FastApp.getContext(), "mobile not verified yet.", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(FastApp.getContext(), "No responce from server ", Toast.LENGTH_LONG).show();
            }

            _reVerify.setEnabled(true);
           // progressBar.setVisibility(View.INVISIBLE);
           // progressDialog.hide();
            progressDialog.dismiss();
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }


        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

}
