package com.staffapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldpass;
    EditText newpass;
    EditText repass;
    Button btnReset;
    String oldPassword="";
    String newPassword="";
    String rePassword,username;

   // SqliteController controller;
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    // url to create new product
    private static String url_create_product =  ServerConfig.serverurl + "changePassword.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    SqliteController sqliteController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldpass=(EditText)findViewById(R.id.old_password);
        newpass=(EditText)findViewById(R.id.new_password);
        repass=(EditText)findViewById(R.id.repassword);
        btnReset=(Button)findViewById(R.id.btn_reset);
        sqliteController=new SqliteController(ChangePasswordActivity.this);
        username=sqliteController.returnUsername();
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword=oldpass.getText().toString().trim();
                newPassword=newpass.getText().toString().trim();
                rePassword=repass.getText().toString().trim();
                Log.d("Username", username);
                if(oldPassword.equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Old Password", Toast.LENGTH_SHORT).show();
                    oldpass.requestFocus();
                }
                else if(newPassword.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter New Password",Toast.LENGTH_SHORT).show();
                    newpass.requestFocus();
                }
                else if(rePassword.equals("")){
                    Toast.makeText(getApplicationContext(),"Retype Password",Toast.LENGTH_SHORT).show();
                    repass.requestFocus();
                }
                else if(rePassword.equals(newPassword)==false){
                    Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
                    repass.requestFocus();
                    repass.setText("");
                }
                else{
                    new ChangePassword().execute();
                }
            }
        });
    }
    class ChangePassword extends AsyncTask<String,String ,String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChangePasswordActivity.this);
            pDialog.setMessage("Processing.. Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Creating User
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username",username));
            params.add(new BasicNameValuePair("oldPassword", oldPassword));
            params.add(new BasicNameValuePair("newPassword", newPassword));
            Log.d("sameen",""+params);

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                final String message = json.getString("message");
                if (success == 1) {
                    // successfully created product
                    //Toast.makeText(getApplicationContext(),"Account Created Successfully", Toast.LENGTH_SHORT).show();



                    Handler handler = new Handler(ChangePasswordActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            sqliteController.funLogout();
                            Intent i = new Intent(ChangePasswordActivity.this,LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    });



                } else {
                    Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    if (success==2)
                    {

                        /*oldpass_et.post(new Runnable() {
                            @Override
                            public void run() {
                                oldpass_et.requestFocus();
                            }
                        });*/
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}