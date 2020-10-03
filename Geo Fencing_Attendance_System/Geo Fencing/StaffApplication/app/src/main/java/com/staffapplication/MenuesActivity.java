package com.staffapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MenuesActivity extends AppCompatActivity {
    SqliteController controller;
    String username;
    ListView lv;
    public static int[] icons = {R.drawable.ic_attendnc, R.drawable.ic_attendance_mark, R.drawable.ic_attendance_mark,R.drawable.ic_settings, R.drawable.ic_cencel};
    public static String[] mnuList = {"Attendance", "View Attendance","Defulter List","ChangePassword", "Logout"};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_menues);
        controller = new SqliteController(getApplicationContext());
        CustomAdapterMenu customAdapterMenu = new CustomAdapterMenu(MenuesActivity.this, mnuList, icons);
        lv = (ListView) findViewById(R.id.list_menueitem);
        lv.setAdapter(customAdapterMenu);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mnuList[position].equals("Attendance")) {
                    Toast.makeText(MenuesActivity.this, mnuList[position], Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MenuesActivity.this, SubjectDetailActivity.class);
                    startActivity(i);
                } else if (mnuList[position].equals("View Attendance")) {
                    Toast.makeText(MenuesActivity.this, mnuList[position], Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MenuesActivity.this, ViewAttendanceActivity.class);
                    startActivity(i);
                }  else if (mnuList[position].equals("Defulter List")) {
                    Toast.makeText(MenuesActivity.this, mnuList[position], Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MenuesActivity.this, DefulterActivity.class);
                    startActivity(i);
                }  else if (mnuList[position].equals("ChangePassword")) {
                    Toast.makeText(MenuesActivity.this, mnuList[position], Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MenuesActivity.this, ChangePasswordActivity.class);
                    startActivity(i);
                } else if (mnuList[position].equals("Logout")) {
                    Toast.makeText(MenuesActivity.this, mnuList[position], Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MenuesActivity.this, LoginActivity.class);
                    startActivity(i);
                    controller.funLogout();
                    finish();
                }

            }
        });
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

}


