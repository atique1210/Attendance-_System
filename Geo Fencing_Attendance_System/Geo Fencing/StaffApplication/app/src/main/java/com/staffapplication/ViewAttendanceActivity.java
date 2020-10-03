package com.staffapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.List;

import static com.staffapplication.SubjectDetailActivity.Subject;

public class ViewAttendanceActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    TextView displayResult;
    Button btnsubmit;
    Spinner subjectlist;

    private static final String TAG_SUCCESS = "success";
    public static String lsubject[];
    SqliteController controller;
    JSONObject json;
    String username = "";
    EditText dateend;
    EditText datestart;
    EditText studentname;
    public static String StartDate = "";
    public static String EndDate = "";
    public static String Result = "";
    public static String StudentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        controller = new SqliteController(this);
        username = controller.returnUsername();

        datestart = (EditText) findViewById(R.id.datestart);
        datestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                getCurrentdate();
            }
        });

        studentname = (EditText) findViewById(R.id.studentname);
        dateend = (EditText) findViewById(R.id.dateend);
        dateend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                getEnaddate();
            }
        });
        subjectlist = (Spinner) findViewById(R.id.subjectname);
        new ListCall().execute();
        displayResult = (TextView) findViewById(R.id.displayResult);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Time = timelist.getSelectedItem().toString().trim();
                Subject = subjectlist.getSelectedItem().toString().trim();
                StartDate = datestart.getText().toString().trim();
                EndDate = dateend.getText().toString().trim();
                Result = displayResult.getText().toString().trim();
                StudentName = studentname.getText().toString().trim();
                if (StartDate.length() == 0) {
                    Toast.makeText(ViewAttendanceActivity.this, "Enter Start Date", Toast.LENGTH_SHORT).show();
                    datestart.requestFocus();
                } else if (EndDate.length() <= 0) {
                    Toast.makeText(ViewAttendanceActivity.this, "Enter End Date", Toast.LENGTH_SHORT).show();
                    dateend.requestFocus();
                }else if (StudentName.length() <= 0) {
                    Toast.makeText(ViewAttendanceActivity.this, "Enter Student Name", Toast.LENGTH_SHORT).show();
                    dateend.requestFocus();
                }
                else {
                    if(new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                        new GetPercen().execute();
                    }
                    else{
                        Toast.makeText(ViewAttendanceActivity.this,"Please Connect To Working Internet", Toast.LENGTH_SHORT).show();
                    }
                    //new GetPercen().execute();
                }
            }
        });
    }
    public void getCurrentdate() {
        int mYear, mMonth, mDay;

        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);



        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        datestart.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void getEnaddate() {
        int mYear, mMonth, mDay;

        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);



        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        dateend.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    class ListCall extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewAttendanceActivity.this);
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
                final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ViewAttendanceActivity.this, android.R.layout.simple_spinner_item, lsubject);
                subjectlist.setAdapter(adapter1);
            }
            pDialog.dismiss();
        }

    }
    class GetPercen extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewAttendanceActivity.this);
            pDialog.setMessage("Processing.. Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String...args){
            List<NameValuePair> params= new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("Subject", Subject));

            params.add(new BasicNameValuePair("startdate", StartDate));
            params.add(new BasicNameValuePair("enddate", EndDate));
             params.add(new BasicNameValuePair("studentname", StudentName));
            Log.d("sameen",""+params);
            String url_list_details =   ServerConfig.serverurl +"getAttendance.php";
            Log.d("sameen",""+url_list_details);
            JSONParser jParser = new JSONParser();
            json = jParser.makeHttpRequest(url_list_details, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                final String message = json.getString("message");
                if (success == 1) {

                    //controller.checkLogin(Username);
                    Handler handler = new Handler(ViewAttendanceActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            displayResult.setText(message);

                            //Toast.makeText(DisplayResultActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Intent i = new Intent(DisplayResultActivity.this, ScanQRActivity.class);
                    //  startActivity(i);
                    // finish();
                }
                else {
                    Handler handler = new Handler(ViewAttendanceActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(ViewAttendanceActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String file_url){

            pDialog.dismiss();
        }

    }
}
