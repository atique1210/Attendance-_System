package com.staffapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.staffapplication.JSONParser.json;

public class SubjectDetailActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    SqliteController controller;

    private static String url_list_details =  ServerConfig.serverurl +"addStaffSubInfo.php";
    public static String Time = "";
    public static String Subject = "";
    public static String SSID = "";
    public static String ClassRoom = "";
    public static String lsubdid;
    String username = "";
    Spinner subjectname;
    Spinner subjecttime;
    JSONObject json;
    Button btnstrat;
    Button btnstop;
    EditText classroom;
    String subname;
    String subtime;
    TextView wifiname;
    private String[] arraySpinnertime;
    public static String lsubject[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);


        wifiname=(TextView)findViewById(R.id.txtwifiname);
        wifiname.setText(getCurrentSsid(getApplicationContext()));

        controller = new SqliteController(this);
        username = controller.returnUsername();
        subjectname = (Spinner) findViewById(R.id.subjectname);

        classroom = (EditText) findViewById(R.id.classroom);
      //  subname = subjectname.getText().toString().trim();

        subjecttime =(Spinner) findViewById(R.id.subjecttime);
        this.arraySpinnertime = new String[] {
                "9:00am","10:00am","11:00am","12:30pm", "01:30pm","02:30pm","02:45pm","03:45pm","04:45pm"
        };
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinnertime);
        subjecttime.setAdapter(adapter);
       // subtime = subjecttime.getText().toString().trim();
        if(new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
            new ListCall().execute();
        }
        else{
            Toast.makeText(SubjectDetailActivity.this,"Please Connect To Working Internet", Toast.LENGTH_SHORT).show();
        }

        btnstop = (Button) findViewById(R.id.btnstop);
        btnstrat = (Button) findViewById(R.id.btnstrat);
        btnstrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time = subjecttime.getSelectedItem().toString().trim();

                Subject = subjectname.getSelectedItem().toString().trim();
                SSID = wifiname.getText().toString().trim();
                ClassRoom = classroom.getText().toString().trim();

                if (Time.length() == 0) {
                    Toast.makeText(SubjectDetailActivity.this, "Enter Time", Toast.LENGTH_SHORT).show();
                    subjecttime.requestFocus();
                }
                else if (ClassRoom.length() == 0) {
                    Toast.makeText(SubjectDetailActivity.this, "Enter Class Name", Toast.LENGTH_SHORT).show();
                    classroom.requestFocus();
                } else {
                    if(new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                        new AddDetail().execute();
                    }
                    else{
                        Toast.makeText(SubjectDetailActivity.this,"Please Connect To Working Internet", Toast.LENGTH_SHORT).show();
                    }
                    //new AddDetail().execute();

                }

            }
        });
        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                    new UpdateDetail().execute();
                }
                else{
                    Toast.makeText(SubjectDetailActivity.this,"Please Connect To Working Internet", Toast.LENGTH_SHORT).show();
                }
                //new UpdateDetail().execute();
            }
        });


    }
    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }
    class ListCall extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SubjectDetailActivity.this);
            pDialog.setMessage("Processing.. Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String...args){
            List<NameValuePair> params= new ArrayList<NameValuePair>();
            String url_list_details =   ServerConfig.serverurl +"getSubjectList.php?email="+username;

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequest(url_list_details, "POST", params);

            try {
                JSONArray usrDetails=json.getJSONArray("response");
                Log.d("sameen", usrDetails.toString());
                lsubject = new String[usrDetails.length()];
                //ltimeev = new String[usrDetails.length()];

                //lid= new String[usrDetails.length()];

                for(int i=0;i<usrDetails.length();i++){
                    JSONObject jsonObject=usrDetails.getJSONObject(i);
                    lsubject[i]=jsonObject.getString("Subject");
                    //ltimeev[i]=jsonObject.getString("timeev");

                    // lid[i]=jsonObject.getString("id");


                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String file_url){
            if(lsubject.length>0) {
                final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SubjectDetailActivity.this, android.R.layout.simple_spinner_item, lsubject);
                subjectname.setAdapter(adapter1);
            }
            pDialog.dismiss();
        }

    }

    class AddDetail extends AsyncTask<String, String, String> {
        @Override
       protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SubjectDetailActivity.this);
            pDialog.setMessage("Processing.. Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("SreamName", Name));

            params.add(new BasicNameValuePair("LecTime", Time));
            params.add(new BasicNameValuePair("SSID", SSID));

            params.add(new BasicNameValuePair("SubjectName", Subject));
            params.add(new BasicNameValuePair("ClassRoom", ClassRoom));
            params.add(new BasicNameValuePair("username", username));
           // params.add(new BasicNameValuePair("Status", "Start"));
                Log.d("sameen",""+params);

            //String url_list = "";

            JSONParser jParser = new JSONParser();
            json = jParser.makeHttpRequest(url_list_details, "POST", params);
            Log.d("sameen", ""+url_list_details);
            try {
                int success = json.getInt("success");
                final String message = json.getString("message");
                if (success == 1) {
                    Handler handler = new Handler(SubjectDetailActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(SubjectDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                                btnstrat.setVisibility(View.GONE);
                            btnstop.setVisibility(View.VISIBLE);
                        }
                    });
                   /* Intent i = new Intent(SubjectDetailActivity.this, SubjectDetailActivity.class);
                    startActivity(i);
                    finish();*/
                } else {
                    Handler handler = new Handler(SubjectDetailActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(SubjectDetailActivity.this, message, Toast.LENGTH_LONG).show();
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
    class UpdateDetail extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SubjectDetailActivity.this);
            pDialog.setMessage("Processing.. Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("SreamName", Name));

            params.add(new BasicNameValuePair("LecTime", Time));
            params.add(new BasicNameValuePair("SSID", SSID));

            params.add(new BasicNameValuePair("SubjectName", Subject));
            params.add(new BasicNameValuePair("ClassRoom", ClassRoom));
            params.add(new BasicNameValuePair("username", username));
           // params.add(new BasicNameValuePair("Status", "Stop"));
            // params.add(new BasicNameValuePair("Status", "Start"));
            Log.d("sameen",""+params);

            //String url_list = "";
            String url_list =  ServerConfig.serverurl +"updateStaffSubInfo.php";
            JSONParser jParser = new JSONParser();
            json = jParser.makeHttpRequest(url_list , "POST", params);
            Log.d("sameen", ""+url_list);
            try {
                int success = json.getInt("success");
                final String message = json.getString("message");
                if (success == 1) {
                    Handler handler = new Handler(SubjectDetailActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(SubjectDetailActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                    Intent i = new Intent(SubjectDetailActivity.this, MenuesActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Handler handler = new Handler(SubjectDetailActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(SubjectDetailActivity.this, message, Toast.LENGTH_LONG).show();
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