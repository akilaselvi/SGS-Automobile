package com.example.admin.sgsauotomobile.ActivityClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.sgsauotomobile.Models.Item;
import com.example.admin.sgsauotomobile.R;
import com.example.admin.sgsauotomobile.Utils.Utils;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-Team1 on 2/17/2017.
 */

public class
LoginActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView image;
    TextView suc_entry;
    TextInputLayout userlayout, passwordlayout;
    EditText username,password,insert_ip;
    Button login,submit;
    public static String  ip,uname, upass;
    String server_response,parameters;
    ProgressDialog progress;
    DotProgressBar prg_bar;
    public static TextView tvTitle;
    String status , userid , usercode ,Entchkflag ,af ,isel ,gcode , godown ,isnof ,isnot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title1);
        image = (ImageView) findViewById(R.id.imgback);
        suc_entry = (TextView) findViewById(R.id.suc_entry);
        prg_bar = (DotProgressBar)findViewById(R.id.dotpbar);

        userlayout = (TextInputLayout) findViewById(R.id.emailloginLayout);
        passwordlayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        username = (EditText) findViewById(R.id.loginEmailET);
        password = (EditText) findViewById(R.id.loginPasswordET);
        login = (Button) findViewById(R.id.loginBtn);
        insert_ip = (EditText)findViewById(R.id.insert_ip);
        submit = (Button)findViewById(R.id.submit);

        image.setImageDrawable(getResources().getDrawable(R.drawable.home));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tvTitle.setText("   User Login");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ip = insert_ip.getText().toString();
                System.out.println("ip : " + ip);

            /*    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("ip", ip);
                editor.clear();
                editor.commit();*/
                SharedPreferences.Editor mypreff = getSharedPreferences("MYPREFERNCE", MODE_PRIVATE).edit();
                mypreff.putString("ip", ip);
                mypreff.commit();
                mypreff.clear();
                Toast.makeText(LoginActivity.this,"Ip Added",Toast.LENGTH_SHORT).show();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = username.getText().toString();
                upass = password.getText().toString();
                Log.i("uname", uname + "" + upass);


                if(!Utils.isNetworkAvailable(LoginActivity.this)){
                    Toast.makeText(LoginActivity.this, "No Network Connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    GetLoginDetails();
                    Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });

    }
    private void GetLoginDetails() {

        new getLogin().execute();
    }

    //Sending data to server(all login details)
    private class getLogin extends AsyncTask<Void, Void, Void> {

        List<Item> listForSearchConcepts = new ArrayList<Item>();
        @Override
        protected void onPreExecute() {
            prg_bar.setVisibility(View.VISIBLE);
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {

            //http://localhost/android/automobiles/get_data.php?page=login&username=admin&password=admin
            parameters = "http://"+ip+ "/android/automobiles/get_data.php?page=login&username=" + uname + "&password=" + upass ;
            System.out.println("login api:" + parameters);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(parameters);
            HttpResponse response;
            try {
                response = httpclient.execute(httpget);
                System.out.println("response :" + response);
                if (response.getStatusLine().getStatusCode() == 200) {
                     server_response = EntityUtils.toString(response.getEntity());
                     System.out.println("login api response :" + server_response);
                     addValuesToTables(server_response);
                } else {
                    Log.i("Server response", "Failed to get server response");
                    System.out.println("login api response :" + server_response);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
           prg_bar.setVisibility(View.GONE);
            if (server_response != null) {
                try {
                    JSONObject jsonObjj = new JSONObject(server_response);
                    status = jsonObjj.getString("status");
                    String check_status = status;
                    if (check_status.equals("true")) {
                         System.out.println("successful login");
                         Intent in = new Intent(LoginActivity.this, HomeScreenActivity.class);
                         startActivity(in);
                         finish();

                    } else {
                        suc_entry.setText("Wrong User Name or Password");
                        suc_entry.setVisibility(View.VISIBLE);
                        suc_entry.postDelayed(new Runnable() {
                            public void run() {
                                suc_entry.setVisibility(View.GONE);
                            }
                        }, 3000);
                        username.setText("");
                        password.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void addValuesToTables(String server_response) {
        System.out.println("login api server response : "+ server_response);
        List<Item> listForSearchConcepts = new ArrayList<Item>();

        try {
            JSONObject jsonObjj = new JSONObject(server_response);

            SharedPreferences.Editor edit_pref = getSharedPreferences("MYPREF", MODE_PRIVATE).edit();
            edit_pref.putString("UserName", uname);
            edit_pref.putString("Password", upass);
            edit_pref.commit();
            edit_pref.clear();

            SharedPreferences.Editor prefs = getSharedPreferences("MYPREFF", MODE_PRIVATE).edit();
            prefs.putString("loginInfo", server_response);
            prefs.commit();
            prefs.clear();


            userid = jsonObjj.getString("Userid");
            usercode = jsonObjj.getString("Usercode");
            Entchkflag = jsonObjj.getString("Entchkflag");
            af = jsonObjj.getString("af");
            isel = jsonObjj.getString("Isel");
            status = jsonObjj.getString("Status");

            System.out.println("userid : " +userid);

            // Getting JSON Array node for godown
            JSONArray godown_name = jsonObjj.getJSONArray("Gcode");
            System.out.println("godown_name : " +godown_name);

            // looping through All Contacts
            for (int i = 0; i < godown_name.length(); i++) {
                JSONObject c = godown_name.getJSONObject(i);
                gcode = c.optString("Code");
                godown = c.optString("Godown");


            }
            System.out.println("login godown :" +gcode);




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}