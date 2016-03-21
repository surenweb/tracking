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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fastLibrary.FastApp;
import fastLibrary.FastConfig;
import fastLibrary.FastKeyValue;
import fastLibrary.FastServer;
import fastLibrary.FastTask;

public class Register extends Activity {

    @Bind(R.id.input_staff_id)  EditText _staffID;
    @Bind(R.id.input_phone_number)  EditText _phoneNo;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.btn_signup)  Button _signupButton;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        FastConfig.LoadConfig();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    onSignupFailed();
                    return;
                }

                _signupButton.setEnabled(false);

                String staffID = _staffID.getText().toString();
                String email = _emailText.getText().toString();
                String phoneNo = _phoneNo.getText().toString();

                // TODO: Implement your own signup logic here.
                new RegisterMobile()
                        .execute(new String[]{staffID, phoneNo, email});
            }
        });

    }

    public void onSignupSuccess(int argMobileID) {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Toast.makeText(FastApp.getContext(), "Registered Successfully", Toast.LENGTH_LONG).show();

        FastConfig.appStaffID = _staffID.getText().toString();
        FastConfig.appEmail = _emailText.getText().toString();
        FastConfig.appPhoneNo = _phoneNo.getText().toString();
        FastConfig.appMobileID = argMobileID+"" ;

        FastConfig.SaveChanges();

        // Log.d(FastConfig.appLogTag, "Default MobileId Given :" + FastConfig.appMobileID);

        Intent i = new Intent(Register.this, RegisterVerify.class);
        startActivity(i);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(FastApp.getContext(), "Failed to register", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String staffID = _staffID.getText().toString();
        String email = _emailText.getText().toString();
        String phoneNo = _phoneNo.getText().toString();


        if (staffID.isEmpty() || staffID.length() < 3) {
            _staffID.setError("at least 3 numbers");
            valid = false;
        } else {
            _staffID.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (phoneNo.isEmpty() || phoneNo.length() < 5 || phoneNo.length() > 10) {
            _phoneNo.setError("enter valid phone no.");
            valid = false;
        } else {
            _phoneNo.setError(null);
        }

        return valid;
    }


    //## ASYNC SERVER REGISTER METHOD

    private class RegisterMobile extends AsyncTask<String, Integer, String> {



        @Override
        protected void onPreExecute() {

           // ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarRegister);
           //  progressBar.setVisibility(View.VISIBLE);

            progressDialog = new ProgressDialog(Register.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String staffID = params[0];
            String phoneNo = params[1];
            String eMail = params[2];

            publishProgress(10); // Calls onProgressUpdate .

            List<FastKeyValue> lstPostParams = new ArrayList<FastKeyValue>();
            lstPostParams.add(new FastKeyValue("task","register"));
            lstPostParams.add(new FastKeyValue("data","hello"));
            lstPostParams.add(new FastKeyValue("staffID",staffID));
            lstPostParams.add(new FastKeyValue("phoneNo", phoneNo));
            lstPostParams.add(new FastKeyValue("eMail", eMail));


            String resText = new FastServer().CallServer("", lstPostParams);
            return resText ;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.d(FastConfig.appLogTag, "Register Mobile response :" + result);

            if(result.length()>0  )
            {
                String resChar = result.substring(0, 1); // resText has id as first charecter
                int mobID = FastTask.IsInt(resChar)? Integer.parseInt(resChar):0 ;

                if(mobID > 0 ){
                    Log.d(FastConfig.appLogTag, "Registered");
                    onSignupSuccess(mobID);
                }
                else{
                    Log.d(FastConfig.appLogTag, "Failed to register");
                    onSignupFailed();
                }
            }
            else
            {
                Toast.makeText(FastApp.getContext(), "No response from server ", Toast.LENGTH_LONG).show();
                onSignupFailed();
            }

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
