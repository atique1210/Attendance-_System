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

public class LoginActivity extends AppCompatActivity {
    SqliteController controller;
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    // url to create new product
    private static String url_create_product =  ServerConfig.serverurl +"checkLogin.php";

    private static final String TAG_SUCCESS = "success";

    EditText editUsername;
    EditText editPassword;
    Button butnLogin;

   // AsyncTask<Void, Void, Void> mRegisterTask;

    String Username;
    String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        butnLogin = (Button) findViewById(R.id.butnLogin);
        butnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Username = editUsername.getText().toString().trim();
                Password = editPassword.getText().toString().trim();

                if (Username.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    editUsername.requestFocus();
                } else if (Password.length() <= 0) {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    editPassword.requestFocus();
                }
                else {
                    new CheckUser().execute();
                }

            }
        });
        controller = new SqliteController(getApplicationContext());

        if (controller.directLogin() > 0) {
            Intent i = new Intent(LoginActivity.this, MenuesActivity.class);
            startActivity(i);
            finish();
        }

    }

    class CheckUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Validating User.. Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Username", Username));
            params.add(new BasicNameValuePair("Password", Password));
           Log.d("nikhil",Username);
            Log.d("nikhil",Password);

            // getting JSON Object
            // Note that create product url accepts POST method

            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "POST", params);

            // check log cat fro response

            Log.d("nikhil", json.toString());
            //Log.d("nikhil", ""+url_create_product);
            try {
                int success = json.getInt(TAG_SUCCESS);
                final String message = json.getString("message");
                if (success == 1) {

                    controller.checkLogin(Username);
                    Handler handler = new Handler(LoginActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });

                    Intent i = new Intent(LoginActivity.this, MenuesActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Handler handler = new Handler(LoginActivity.this.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
            catch (JSONException e) {
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
