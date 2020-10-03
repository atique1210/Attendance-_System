package com.staffapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SendTimeActivity extends AppCompatActivity {
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    // url to create new product
    private static String url_create_product =  ServerConfig.serverurl + "addSubjectDetail.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    String id;
    EditText entertime;

    public static String Time = "";

    Button submitbtn;
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_time);
        submitbtn = (Button) findViewById(R.id.submitbtn);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time = entertime.getText().toString().trim();

                if (Time.length() <= 0) {
                    Toast.makeText(SendTimeActivity.this, "Enter Time", Toast.LENGTH_SHORT).show();
                    entertime.requestFocus();
                } else {

                    new CallData().execute();
                    Intent intent = getIntent();
                    Bundle bundle = intent.getExtras();
                    id = bundle.getString("id");

                }
            }


        });
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    class CallData extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SendTimeActivity.this);
            pDialog.setMessage("Processing... Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("SreamName", Name));

            params.add(new BasicNameValuePair("id", id));
            params.add(new BasicNameValuePair("time", Time));

            params.add(new BasicNameValuePair("StreamDescription", Time));
            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "POST", params);
            try {
                int success = json.getInt("success");
                final String message = json.getString("message");
                if (success == 1) {
                    Handler handler = new Handler(SendTimeActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(SendTimeActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                    Intent i = new Intent(SendTimeActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Handler handler = new Handler(SendTimeActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(SendTimeActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
}



