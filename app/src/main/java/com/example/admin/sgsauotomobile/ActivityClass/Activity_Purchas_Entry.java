package com.example.admin.sgsauotomobile.ActivityClass;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.sgsauotomobile.Adapter.InwardEntryAdapter;
import com.example.admin.sgsauotomobile.Adapter.PurchaseAdapter;
import com.example.admin.sgsauotomobile.DataBase.DBController;
import com.example.admin.sgsauotomobile.Fragment.Fragment_Items_One;
import com.example.admin.sgsauotomobile.Fragment.Fragment_Items_purchase;
import com.example.admin.sgsauotomobile.Models.Godown;
import com.example.admin.sgsauotomobile.Models.Gowdndetail;
import com.example.admin.sgsauotomobile.Models.ListItem;
import com.example.admin.sgsauotomobile.R;
import com.example.admin.sgsauotomobile.Utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 5/1/2017.
 */

public class Activity_Purchas_Entry extends AppCompatActivity implements
        View.OnClickListener {

    private static TextView party_name, bill_no, bill_date, godown_name, textTile;
    private EditText edt_search, edt_nameVendor, edt_billNo, edt_date, edt_discountNew, edt_rateNew, edt_qutyNew, edt_itemCodeNew, edt_nameNew;
    String purchaseItemName, purchQuty, purRate, purDisc, purstatus;
    Button btn_save, btn_saveAddNew, btn_update, btn_report;
    public static String pname, pbill, pdate, pgodown, searchItem;
    String fragementValue, text;
    public static String parmeter, purchaseCode;
    private Toolbar toolbar;
    public static TextView tvTitle;
    ImageView image_back, image_allitems, image_dialodVendor;
    Dialog dialog, dialogs;
    String val_frag;
    private ListItem item, item1;
    private static List<ListItem> listForSearchConcepts = new ArrayList<ListItem>();
    private static List<ListItem> listForSearchConcepts1 = new ArrayList<ListItem>();
    public int test2 = 0;
    int test;
    public static int outQutys;
    public ListView myList;
    SharedPreferences pref;
    String value, output1;
    ArrayList<HashMap<String, String>> output;
    public static int RowCount;
    public static PurchaseAdapter adapter, adapter1;
    public static TextView tex_rowCount, txt_totalPur;
    FloatingActionButton floatingActionButton_add;
    ArrayList<String> finallist = new ArrayList<String>();
    public static String purName, purcode, purQuty, purrate, purdisc, partyname, gname, gdate, pnobill;
    String restoredIp, logininfo, gcodee, gstatus_code;
    LinearLayout linear_purchase;
    RelativeLayout relative_list;
    String res, server_response, names, gowdon, bill, date;
    String customvalue;
    ImageView imag_calendr, cancelItem;
    static final int DATE_PICKER_ID = 1111;
    private int year, index;
    private int month;
    private int day;
    public static String currentDateandTime;
    public static String selected_date;
    public static String codeEdt;
    public static String final_date;
    ArrayList<Godown> godowns;
    public ArrayList<Godown> godownArrayList = new ArrayList<>();
    DBController controller = new DBController(this);
    public static String str_vendorInf;
    public List<ListItem> itemList;//arrayList.addAll(itemlist)
    TextView textdis, textrate;
    LinearLayout linearLayout, linearLayout1;
    ArrayList<HashMap<String, String>> values;
    String nameDB, codeDB, rateDB, gNameDB;
    Spinner spinnGwdName;
    ArrayList<String> godownName;
    ArrayList<Gowdndetail> godownNameLis;
    Boolean oAllow = false;
    EditText txtStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_puchse_screen);

        final Context context = this;


        myList = (ListView) findViewById(R.id.list_listView_purchase);
        image_back = (ImageView) findViewById(R.id.img);//
        image_back.setImageDrawable(getResources().getDrawable(R.drawable.arrow_left));//
        linear_purchase = (LinearLayout) findViewById(R.id.linear_purchase_purchase);
        relative_list = (RelativeLayout) findViewById(R.id.relative_list_purchase);
        image_allitems = (ImageView) findViewById(R.id.imgFragme);
        image_dialodVendor = (ImageView) findViewById(R.id.imgDialog);
        spinnGwdName = (Spinner) findViewById(R.id.spinner_godown_name_purchase);
        edt_search = (EditText) findViewById(R.id.searchitem_purchase);
        party_name = (TextView) findViewById(R.id.txtpartyname_purchase);
        bill_no = (TextView) findViewById(R.id.txtbillnumber_purchase);
        bill_date = (TextView) findViewById(R.id.txtdate_purchase);
        godown_name = (TextView) findViewById(R.id.txtGodownname_purchase);
        textTile = (TextView) findViewById(R.id.txt_title);//

        linearLayout = (LinearLayout) findViewById(R.id.linerheader_purchase);
        linearLayout1 = (LinearLayout) findViewById(R.id.txtoflinear_purchase);

        dialog = new Dialog(Activity_Purchas_Entry.this);
        dialog.setTitle("Vendor Information");
        dialog.setContentView(R.layout.layout_purchas_vendor_information);
        edt_nameVendor = (EditText) dialog.findViewById(R.id.pur_party_name_purchase);
        edt_billNo = (EditText) dialog.findViewById(R.id.pur_bill_number_purchase);
        edt_date = (EditText) dialog.findViewById(R.id.pur_date_purchase);
        imag_calendr = (ImageView) dialog.findViewById(R.id.imgCaled_purchase);
        btn_save = (Button) dialog.findViewById(R.id.save_btn_dialog_purchase);
        txt_totalPur = (TextView) findViewById(R.id.txt_quty_purchase);
//         cancelItem = (ImageView) findViewById(R.id.cancel);

        dialogs = new Dialog(Activity_Purchas_Entry.this);
        dialogs.setTitle("New Item");
        dialogs.setContentView(R.layout.prchase_entity_dialogbx_purchase);
        edt_nameNew = (EditText) dialogs.findViewById(R.id.pur_dialog_itemName_purchase);
        edt_itemCodeNew = (EditText) dialogs.findViewById(R.id.pur_dialog_itemCode_purchase);
        edt_qutyNew = (EditText) dialogs.findViewById(R.id.pur_dialog_quty_purchase);
        edt_rateNew = (EditText) dialogs.findViewById(R.id.pur_dialog_rate_purchase);
        edt_discountNew = (EditText) dialogs.findViewById(R.id.purcDialog_discs_purchase);
        txtStatus = (EditText) dialogs.findViewById(R.id.dialogstatus_purchase);

        floatingActionButton_add = (FloatingActionButton) findViewById(R.id.fabAdd_purchase);
        btn_saveAddNew = (Button) dialogs.findViewById(R.id.save_btn_dialogPur_purchase);

        btn_update = (Button) findViewById(R.id.update_purchase);
        btn_report = (Button) findViewById(R.id.btn_report_purchase);

        tex_rowCount = (TextView) findViewById(R.id.txt_rowCount_purchase);

        textTile.setText(R.string.TilePurchase);

        party_name.setFreezesText(true);
        godown_name.setFreezesText(true);


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        currentDateandTime = sdf.format(new Date());


        getGowdnNameCode();


        Intent in = getIntent();


        fragementValue = in.getStringExtra("Items_List");
        val_frag = fragementValue;
        test(val_frag);


        clicklistener();
        searchtext();

        edt_itemCodeNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                codeEdt = edt_itemCodeNew.getText().toString();

                if (!codeEdt.equals("")) {
                    values = controller.getItemDetailspur();
                    System.out.println("codeEdt: " + codeEdt);

                    if (values.size() == 0) {
                        edt_nameNew.setText("");
                        edt_rateNew.setText("");
                        txtStatus.setText("0");
                        System.out.println("The status if : " + txtStatus.getText());
                    } else {
                        nameDB = values.get(0).get("nameofitem");
                        codeDB = values.get(0).get("codeoditem");
                        rateDB = values.get(0).get("rateofitem");
                        System.out.println("code: " + codeDB);
                        edt_nameNew.setText(nameDB);
                        edt_rateNew.setText(rateDB);
                        txtStatus.setText("1");
                        System.out.println("The status else : " + txtStatus.getText());
                    }
                } else {
                    edt_nameNew.setText("");
                    edt_rateNew.setText("");
                    System.out.println("the arraylist is111 : " + nameDB);
                }
            }
        });

    }

    private void getGowdnNameCode() {
        new GowdnDetails().execute();
    }



    private void clicklistener() {
        image_back.setOnClickListener(this);
        imag_calendr.setOnClickListener(this);
        image_allitems.setOnClickListener(this);
        image_dialodVendor.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        floatingActionButton_add.setOnClickListener(this);
        btn_saveAddNew.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_report.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img:

                onBackPressed();

                break;
            case R.id.imgCaled_purchase:
                Calendar mcurrentDate = Calendar.getInstance();
                year = mcurrentDate.get(Calendar.YEAR);
                month = mcurrentDate.get(Calendar.MONTH);
                day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(Activity_Purchas_Entry.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        year = selectedyear;
                        month = selectedmonth;
                        day = selectedday;

                        edt_date.setText(new StringBuilder().append(day)
                                .append("-").append(month + 1).append("-").append(year)
                                .append(""));

                        selected_date = edt_date.getText().toString();
                        final_date = selected_date;

                    }
                }, year, month, day);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();

                break;
            case R.id.imgFragme:

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment_Items_purchase fragment = new Fragment_Items_purchase();
                fragmentTransaction.add(android.R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;
            case R.id.imgDialog:

                dialog.show();

                edt_nameVendor.setText("");
                edt_billNo.setText("");
                edt_date.setText("");

                break;
            case R.id.save_btn_dialog_purchase:

                pname = edt_nameVendor.getText().toString();
                pbill = edt_billNo.getText().toString();
                pdate = edt_date.getText().toString();

                partyname = "Pname :" + pname + ",";
                gname = "Gname :" + gcodee + ",";
                gdate = "Date :" + pdate + ",";
                pnobill = "Pbill :" + pbill;

                party_name.setText("Party : " + pname);
                godown_name.setText("Godown : " + gcodee);
                bill_no.setText("BillNo : " + pbill);
                bill_date.setText("Date : " + pdate);

                str_vendorInf = partyname + gname + gdate + pnobill;
                addVendorInformation();

                break;
            case R.id.fabAdd_purchase:
                dialogs.show();

            break;

            case R.id.save_btn_dialogPur_purchase:

                    Test();

                break;

            case R.id.update_purchase:

                pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                value = pref.getString("ItemListFinalValue", "");
                //  updateCode();


                if (!Utils.isNetworkAvailable(Activity_Purchas_Entry.this)) {
                    //partyname + gname + gdate + pnobill

                    DBController controller = new DBController(Activity_Purchas_Entry.this);
                    SQLiteDatabase db = controller.getWritableDatabase();
                    ContentValues cvVendor = new ContentValues();

                    cvVendor.put("nameofparty", pname);
                    cvVendor.put("billNoofparty", pbill);
                    cvVendor.put("dateofparty", pdate);
                    cvVendor.put("vendorgodowncode", gname);
                  /*  cvVendor.put("actitemname",name);
                    cvVendor.put("actitemcode",code);
                    cvVendor.put("actitemrate",rate);
                    cvVendor.put("actitemdisc",dist);
                    cvVendor.put("actitemquty",quentity);*/

                    db.insert("tableofvendor", null, cvVendor);
                    db.close();

                    System.out.println("test_cv2" + cvVendor);

                    Toast.makeText(Activity_Purchas_Entry.this, "Network Is Not Avialable", Toast.LENGTH_SHORT).show();
                    System.out.println("The string is : " + value + " Vendor info" + str_vendorInf);
                } else {
                    new addNewTask().execute();

                }
                break;

            case R.id.btn_report_purchase:

                Intent intent = new Intent(Activity_Purchas_Entry.this, ReportActivity.class);
                startActivity(intent);

                break;

        }

    }


    private void searchtext() {


        // Capture Text in EditText
        edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                text = edt_search.getText().toString().toLowerCase(Locale.getDefault());
                System.out.println("The string value " + text);

                    adapter.filter(text);

            }
        });





      /*  //edt_search.setCursorVisible(true);
        if (!edt_search.equals("")) {

            edt_search.addTextChangedListener(this);
        } else {
            System.out.println(" The EditText Not TextChange ");

        }*/
    }



    private void test(String val_frag) {

            try {

                JSONArray itemss = new JSONArray(val_frag);
                for (int i = 0; i < itemss.length(); i++) {
                    JSONObject c = itemss.getJSONObject(i);

                    item1 = new ListItem();
                    item1.setItemName(c.optString("pro_name"));
                    item1.setItemCode(c.optString("pro_code"));
                    item1.setIteQuty(c.optString("pro_qty"));
                    item1.setItemRate(c.optString("pro_rate"));
                    item1.setItemDisc("0");

                    listForSearchConcepts.add(0, item1);

                }
                adapter = new PurchaseAdapter(Activity_Purchas_Entry.this, android.R.layout.simple_list_item_1, listForSearchConcepts);
                myList.setAdapter(adapter);

                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }



    }


    private void addVendorInformation() {
        if (!pname.equals("") && !pbill.equals("") && !pdate.equals("")) {
            dialog.dismiss();

        } else {
            dialog.show();
            Toast.makeText(this, "Please Fill All", Toast.LENGTH_SHORT).show();
        }
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getOrientation();
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                if (!oAllow) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                if (!oAllow) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
        }
    }


    private void Test() {
        System.out.println("The Activity Param1:" + parmeter);
        purchaseItemName = edt_nameNew.getText().toString();
        purchaseCode = edt_itemCodeNew.getText().toString();
        purchQuty = edt_qutyNew.getText().toString();
        purRate = edt_rateNew.getText().toString();
        purDisc = edt_discountNew.getText().toString();
        purstatus = txtStatus.getText().toString();
        System.out.println("The Status is :" + purstatus);

        if (!purchaseItemName.equals("") && !purchaseCode.equals("") && !purchQuty.equals("") && !purRate.equals("") && !purDisc.equals("")) {

            item = new ListItem();
            item.setItemName(purchaseItemName);
            item.setItemCode(purchaseCode);
            item.setIteQuty(purchQuty);
            item.setItemRate(purRate);
            item.setItemDisc(purDisc);
            item.setItemStaus(purstatus);
            listForSearchConcepts.add(0, item);
            adapter = new PurchaseAdapter(Activity_Purchas_Entry.this, android.R.layout.simple_list_item_1, listForSearchConcepts);

            for (int i = 0; i < listForSearchConcepts.size(); i++) {

                System.out.println("Count_loop :" + listForSearchConcepts.size());
                myList.setAdapter(adapter);
                myList.setScrollingCacheEnabled(true);
                adapter.notifyDataSetChanged();
            }
            dialogs.dismiss();
            edt_nameNew.setText("");
            edt_itemCodeNew.setText("");
            edt_qutyNew.setText("");
            edt_rateNew.setText("");
            edt_discountNew.setText("");

        } else {

            Toast.makeText(Activity_Purchas_Entry.this, R.string.dialogAdd_new, Toast.LENGTH_SHORT).show();
            dialogs.show();
        }


    }



    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        int count = listForSearchConcepts.size();
        int counts = listForSearchConcepts1.size();
        if (count == 0 && counts == 0) {

            Intent in = new Intent(Activity_Purchas_Entry.this, HomeScreenActivity.class);
            startActivity(in);
            finish();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    Activity_Purchas_Entry.this);

            alertDialogBuilder
                    .setMessage("If go back data will be Lost")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            listForSearchConcepts1.clear();
                            listForSearchConcepts.clear();
                            Intent in = new Intent(Activity_Purchas_Entry.this, HomeScreenActivity.class);
                            in.addCategory(Intent.CATEGORY_HOME);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    class GowdnDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences prefss = getSharedPreferences("MYPREFF", MODE_PRIVATE);
            logininfo = prefss.getString("loginInfo", null);
            godownNameLis = new ArrayList<Gowdndetail>();
            godownName = new ArrayList<String>();
            try {
                JSONObject jsonObj = new JSONObject(logininfo);
                JSONArray godown_name = jsonObj.getJSONArray("Gcode");

                for (int i = 0; i < godown_name.length(); i++) {
                    JSONObject cc = godown_name.getJSONObject(i);
                    Gowdndetail gdn = new Gowdndetail();
                    gdn.setgName(cc.getString("Godown"));

                    gcodee = cc.optString("Code");
                    gNameDB = cc.getString("Godown");

                    System.out.println("godown name :" + gNameDB);

                    godownNameLis.add(gdn);


                    godownName.add(cc.getString("Godown"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            spinnGwdName
                    .setAdapter(new ArrayAdapter<String>(Activity_Purchas_Entry.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            godownName));

            System.out.println("godown name :" + godownName);

            spinnGwdName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    // Locate the textviews in activity_main.xml
                    // godown_id = godnn.get(position).getGcode();
                    //String  Godown_name = godownNameLis.get(position).getgName();
                    // tvTitle.setText("   "+Godown_name);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
            //  Toast.makeText(Activity_Purchas_Entry.this," Updated ",Toast.LENGTH_SHORT).show();


        }

    }


    private class addNewTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            SharedPreferences prefs = getApplicationContext().getSharedPreferences("MYPREFERNCE", MODE_PRIVATE);
            restoredIp = prefs.getString("ip", null);
            System.out.println("wedew" + restoredIp);

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(restoredIp)//space7cloud.com//2//92.168.1.100//192.168.1.7
                    .appendPath("android")
                    .appendPath("automobiles")//sgs_traders
                    .appendPath("get_data.php")
                    .appendQueryParameter("page", "item_save")
                    .appendQueryParameter("party_details", str_vendorInf)
                    .appendQueryParameter("item_list", value);
            System.out.println("test url : " + value);
            System.out.println("test url : " + str_vendorInf);
            String myUrl = builder.build().toString();
            System.out.println("Value Submit Url  :" + myUrl);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpget = new HttpPost(myUrl);
            System.out.println("The Http " + httpget);
            HttpResponse response = null;
            try {
                response = httpclient.execute(httpget);
                System.out.println("The Http rsponse " + response);
                if (response.getStatusLine().getStatusCode() == 200) {

                    server_response = EntityUtils.toString(response.getEntity());

                    try {
                        JSONObject jsonObjj = new JSONObject(server_response);
                        res = jsonObjj.getString("status");
                        gstatus_code = jsonObjj.getString(gcodee);
                        System.out.println("hfdfdfh" + gstatus_code);
                        System.out.println("hdfh" + res);

                        //Getting JSON ARRAY for Items
                        JSONObject Godown_jsonobjj = jsonObjj.getJSONObject(gcodee);
                        System.out.println("hdfhsdsd" + Godown_jsonobjj);

                        JSONArray Godown_list_items_jsonobjj = Godown_jsonobjj.getJSONArray("items");
                        for (int j = 0; j < Godown_list_items_jsonobjj.length(); j++) {
                            JSONObject c = Godown_list_items_jsonobjj.getJSONObject(j);

                            ListItem itemc = new ListItem();
                            itemc.setIteQuty(c.optString("qty"));
                            itemc.setItemName(c.optString("name"));
                            itemc.setItemCode(c.optString("itemcode"));


                            String itemcode = c.optString("itemcode");
                            String itemname = c.optString("name");
                            String itemfont = c.optString("font");
                            String itemid = c.optString("id");
                            String itemqty = c.optString("qty");

                            System.out.println("The string id :" + itemname + itemqty);

                            DBController controller = new DBController(Activity_Purchas_Entry.this);
                            SQLiteDatabase db = controller.getWritableDatabase();
                            ContentValues cv2 = new ContentValues();

                            cv2.put("name", itemname);
                            cv2.put("itemcode", itemcode);
                            cv2.put("quantity", itemqty);
                            db.insert("items", null, cv2);
                            db.close();

                            System.out.println("test_cv2" + cv2);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("Server response", server_response);
                    Log.i("Server response", myUrl);
                    System.out.println("Value server resonse" + server_response);

                } else {
                    Log.i("Server response", "Failed to get server response");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            Toast.makeText(Activity_Purchas_Entry.this, " Updated ", Toast.LENGTH_SHORT).show();
            listForSearchConcepts.clear();
            listForSearchConcepts1.clear();

        }
    }
}