package com.example.admin.sgsauotomobile.ActivityClass;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.sgsauotomobile.Adapter.ItemReportAdapter;
import com.example.admin.sgsauotomobile.DataBase.DBController;
import com.example.admin.sgsauotomobile.Models.ItemReport;
import com.example.admin.sgsauotomobile.R;
import com.example.admin.sgsauotomobile.Utils.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Android-Team1 on 3/13/2017.
 */

public class ReportActivity extends AppCompatActivity {
    ListView list;
    FloatingActionButton print;
    DBController databaseHelper;
    ArrayList<ItemReport> itemshopAdapter;
    ItemReportAdapter adapter;
    String url,bill_number;
    Toolbar toolbar;
    public static TextView tvTitle;
    ImageView imageBack,imagBack;
    ProgressBar progres;
    EditText enter_bill_no;
    Button submit;
    String restoredIp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycler_for_report);
        list = (ListView)findViewById(R.id.report_list_listView);
//        print = (FloatingActionButton) findViewById(R.id.print_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title1);
        progres = (ProgressBar)findViewById(R.id.itemProgressBarReport);
        imageBack = (ImageView) findViewById(R.id.imgback);
        enter_bill_no = (EditText)findViewById(R.id.bill_number);
        submit = (Button)findViewById(R.id.submit_bill);
        imageBack.setImageDrawable(getResources().getDrawable(R.drawable.arrow_left));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tvTitle.setText("  Report ");


        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MYPREFERNCE",MODE_PRIVATE);
        restoredIp = prefs.getString("ip", null);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });
       /* print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bill_number = enter_bill_no.getText().toString();
                if(bill_number.equals("")){
                    Toast.makeText(ReportActivity.this,"Please Enter Bill Number",Toast.LENGTH_SHORT).show();
                }else {
                    new GetItemList().execute();
                }
            }
        });

    }
    // Download JSON file AsyncTask(list view items)
    private class GetItemList extends AsyncTask<Void, Void, Void> {
        ArrayList<ItemReport> listForSearchConcepts = new ArrayList<ItemReport>();
        @Override
        protected void onPreExecute() {
            progres.setVisibility(View.VISIBLE);
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // http://space7cloud.com/sgs_trader/sgs_datas.php?user_godown_id=g001&date=//for testing
            url = "http://"+restoredIp+"/android/automobiles/get_data.php?page=reports&bill_no=" + bill_number;
            System.out.println("The Report url :" +url);

            // Making a request to url and getting response
            String jsonStrr = sh.makeServiceCall(url);
            Log.e("Test url2", url);


            if (jsonStrr != null) {
                try {
                    JSONObject jsonObjj = new JSONObject(jsonStrr);

                    // Getting JSON Array node
                    JSONArray itemss = jsonObjj.getJSONArray("Itemlists");

                    // looping through All Contacts
                    for (int i = 0; i < itemss.length(); i++) {

                        JSONObject c = itemss.getJSONObject(i);

                        ItemReport item = new ItemReport();
                        item.setId(c.optString("Id"));
                        item.setName(c.optString("Item"));
                        item.setItemcode(c.optString("Code"));
                        item.setQty(c.optString("Qty"));
                        item.setRate(c.optString("Rate"));
                        item.setDiscount(c.optString("Idisc"));
                        listForSearchConcepts.add(item);
                        Log.i("myitemlist",listForSearchConcepts.toString());


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            progres.setVisibility(View.GONE);
            adapter = new ItemReportAdapter(ReportActivity.this, listForSearchConcepts);
            list.setAdapter(adapter);
            list.setScrollingCacheEnabled(true);


        }


    }
}
