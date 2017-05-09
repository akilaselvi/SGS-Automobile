package com.example.admin.sgsauotomobile.ActivityClass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.sgsauotomobile.R;
import com.example.admin.sgsauotomobile.Utils.EndPoints;
import com.example.admin.sgsauotomobile.Utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by Android-Team1 on 2/17/2017.
 */

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView date,day,weather;
    private ImageView weather_cloud;
    private RelativeLayout img1,img2,img3,img4,img5,img6;
    static InputStream is = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home_screen2);
        date = (TextView)findViewById(R.id.date);
        day = (TextView)findViewById(R.id.day);
        weather = (TextView)findViewById(R.id.cloud_type);
        weather_cloud =(ImageView)findViewById(R.id.img_cloud);

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH : mm : ss");
        SimpleDateFormat sdf = new SimpleDateFormat("HH : mm : ss");
        String currentDateandTime = sdf.format(new Date());
        date.setText(currentDateandTime);

        SimpleDateFormat sdfq = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdfq.format(d);
        System.out.println("Test Date" +dayOfTheWeek);
        day.setText(dayOfTheWeek);

        new LoadWeatherDetails().execute();

        img1 = (RelativeLayout) findViewById(R.id.relative1);
        img2 = (RelativeLayout) findViewById(R.id.relative2);
        img3 = (RelativeLayout) findViewById(R.id.relative3);
        img4 = (RelativeLayout) findViewById(R.id.relative4);
        img5 = (RelativeLayout) findViewById(R.id.relative5);
        img6 = (RelativeLayout) findViewById(R.id.relative6);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        {
            switch(view.getId())
            {
                case R.id.relative1:
                    Intent in=new Intent(HomeScreenActivity.this,Activity_Inward_Entry_Screen.class);
                    //in.putExtra("Inward","InwardEntry");
                    startActivity(in);
                    finish();
                    break;

                case R.id.relative2:
                    Intent intent=new Intent(HomeScreenActivity.this,Activity_Purchas_Entry.class);
                   // intent.putExtra("Inward"," ");
                    startActivity(intent);
                    finish();
                    Toast.makeText(HomeScreenActivity.this,"Under Construction",Toast.LENGTH_SHORT).show();
                    break;

                case R.id.relative3:
                    /*Intent intentSave=new Intent(HomeScreenActivity.this,Activity_Inward_Entry_Offline.class);
                    intentSave.putExtra("Inward"," ");
                    startActivity(intentSave);
                    finish();*/

                    break;

                case R.id.relative4:
                    Toast.makeText(HomeScreenActivity.this,"Under Construction",Toast.LENGTH_SHORT).show();
                    break;

                case R.id.relative5:
                     Toast.makeText(HomeScreenActivity.this,"Under Construction",Toast.LENGTH_SHORT).show();
                     break;

                case R.id.relative6:
                 //   Toast.makeText(HomeScreenActivity.this,"Under Construction",Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor edit_pref = getSharedPreferences("MYPREF", MODE_PRIVATE).edit();
                    edit_pref.clear();
                    edit_pref.commit();

                    restartActivity();
                    break;
            }
        }
    }

    private void restartActivity() {
        finish();
        Intent in = new Intent(HomeScreenActivity.this,LoginActivity.class);
        startActivity(in);
    }

    // -- Async class for loading weather --//
    public class LoadWeatherDetails extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
           try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(EndPoints.weather);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "n");
                }
                is.close();
                return sb.toString();
            } catch (Exception e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jObj = null;
            if (Utils.isNetworkAvailable(HomeScreenActivity.this)) {
                try {
                    jObj = new JSONObject(result);
                    String current_weather = jObj.getString("current");

                    JSONObject object = new JSONObject(current_weather);
                    String temp = object.getString("temp_c");

                    JSONObject condition = object.getJSONObject("condition");
                    String cloud_name = condition.getString("text");
                    String img1 = condition.getString("icon");
                    String image = "http:" + img1 ;
                    Log.i("grievances_list",image);

                    Picasso.with(HomeScreenActivity.this).load(image).into(weather_cloud);
                    weather.setText(cloud_name + "  " +temp + " " + (char) 0x00B0 + "C");

                    Log.i("temp", temp + cloud_name);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }


            }
        }
    }
}
