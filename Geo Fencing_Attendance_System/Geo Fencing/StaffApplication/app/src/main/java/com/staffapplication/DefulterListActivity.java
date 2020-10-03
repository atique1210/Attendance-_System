package com.staffapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DefulterListActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private static String url_create_product = ServerConfig.serverurl +"getDefulterList.php";

    JSONObject json;
    String Sdate="";
    String Edate="";
    String Sub="";
    public static String student[];
    public static String per[];
    ListView StudentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defulter_list);
        StudentList = (ListView) findViewById(R.id.StudentList) ;
        StudentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                /*String text = ldate[position];
                Intent i = new Intent(BookAppointmentDateActivity.this, MainApTimeActivity.class);
                i.putExtra("date", ldate[position]);
                i.putExtra("date", text);
                startActivity(i);*/

            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Sdate = bundle.getString("sdate");
        Edate = bundle.getString("edate");
        Sub = bundle.getString("subject");
        new ListCall().execute();
        Log.d("sameen",""+Sdate);
        Log.d("sameen",""+Edate);
        Log.d("sameen",""+Sub);

    }
    class ListCall extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DefulterListActivity.this);
            pDialog.setMessage("Processing.. Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String...args){
            List<NameValuePair> params= new ArrayList<NameValuePair>();
            String url_list_details = "http://www.proitce.com/umesh/wifiattend/Staffapp/studentnames.php?Sub="+Sub+"&sdate="+Sdate+"&edate="+Edate;

            Log.d("sameen",""+url_list_details);
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequest(url_list_details, "POST", params);

            try {
                JSONArray usrDetails=json.getJSONArray("response");
                Log.d("sameen", usrDetails.toString());
                student = new String[usrDetails.length()];
                per = new String[usrDetails.length()];



                for(int i=0;i<usrDetails.length();i++){
                    JSONObject jsonObject=usrDetails.getJSONObject(i);
                    student[i]=jsonObject.getString("StudentName");
                   per[i]=jsonObject.getString("Percen");



                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String file_url){
            if(student.length>0) {
                CustomAdapterDefulterlist customAdapterDefulterlist = new CustomAdapterDefulterlist(DefulterListActivity.this, student, per);

                StudentList.setAdapter(customAdapterDefulterlist);
            }
            pDialog.dismiss();
        }

    }
}
