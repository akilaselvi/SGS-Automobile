package com.example.admin.sgsauotomobile.Fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.sgsauotomobile.ActivityClass.Activity_Inward_Entry_Screen;
import com.example.admin.sgsauotomobile.ActivityClass.Activity_Purchas_Entry;
import com.example.admin.sgsauotomobile.Adapter.ItemCartAdapter;
import com.example.admin.sgsauotomobile.DataBase.DBController;
import com.example.admin.sgsauotomobile.Models.Item;
import com.example.admin.sgsauotomobile.R;
import com.example.admin.sgsauotomobile.Utils.HttpHandler;
import com.example.admin.sgsauotomobile.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Android-Team1 on 2/14/2017.
 */

public class Fragment_Items_purchase extends Fragment implements Serializable{
    private Context mContext;
    private CoordinatorLayout layout_main;
    private FloatingActionButton floatingActionButton;
    private ItemCartAdapter itemshopAdapter;
    public ArrayList<Item> itemNameArrayList;
    private RecyclerView shopRcyclerView;
    private ProgressBar itemProgressBar;
    Activity_Inward_Entry_Screen activie;
    Bundle mBundle = new Bundle();
    DBController controller;
    public static TextView tvTitle;
    ImageView image_back;

    public static String search_text;
  //  String code,name,qty,id;
    ArrayList<Item> employeeList;
    private static Dialog dialogs;
    String updateinfo,restoredIp,logininfo,gcodee;
    ImageView searchs,cross, imgBack ;
    String fromParamactivity;
    EditText edtsearch;
    ItemCartAdapter itemadpter = new ItemCartAdapter(mContext);
    List<Item> filterezdList;
    public static String  editTxtSearch;
    List<Item> listForSearchConcepts = new ArrayList<Item>();
    ArrayList<Item> item ;

    public Fragment_Items_purchase() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getContext();
        itemNameArrayList =  new ArrayList<Item>();
        item =  new ArrayList<Item>();
        controller = new DBController(mContext);

        View rootView = inflater.inflate(R.layout.layout_all_items_recycler, container, false);
        layout_main = (CoordinatorLayout) rootView.findViewById(R.id.layout_main);
        imgBack = (ImageView) rootView.findViewById(R.id.img_BACK);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        shopRcyclerView = (RecyclerView) rootView.findViewById(R.id.itemRecyclerView);
        shopRcyclerView.setHasFixedSize(true);

        SharedPreferences prefss = getContext().getSharedPreferences("MYPREFF", MODE_PRIVATE);
        logininfo = prefss.getString("loginInfo", null);

        try {
            JSONObject jsonObj = new JSONObject(logininfo);
            JSONArray godown_name = jsonObj.getJSONArray("Gcode");

            for (int i = 0; i < godown_name.length(); i++) {
                JSONObject cc = godown_name.getJSONObject(i);
                gcodee = cc.optString("Code");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        int dbCount = controller.getItemCount();

        if(dbCount == 0){
            if(!Utils.isNetworkAvailable(mContext)){
                Toast.makeText(mContext, "Network Is Not Avialable", Toast.LENGTH_SHORT).show();
            }
            else {

                itemshopAdapter = new ItemCartAdapter(mContext, itemNameArrayList);
                itemProgressBar = (ProgressBar) rootView.findViewById(R.id.itemProgressBar);

                ItemListFragm();

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                shopRcyclerView.setLayoutManager(mLayoutManager);
                shopRcyclerView.setItemAnimator(new DefaultItemAnimator());
                shopRcyclerView.setAdapter(itemshopAdapter);
            }
        }
        else{

//            itemProgressBar.setVisibility(View.VISIBLE);
            itemNameArrayList = controller.getItems();
            itemshopAdapter = new ItemCartAdapter(mContext, itemNameArrayList);
            itemProgressBar = (ProgressBar) rootView.findViewById(R.id.itemProgressBar);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            shopRcyclerView.setLayoutManager(mLayoutManager);
            shopRcyclerView.setItemAnimator(new DefaultItemAnimator());
            shopRcyclerView.setAdapter(itemshopAdapter);
        }


        shopRcyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, shopRcyclerView, new Fragment_Items_purchase.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        searchs = (ImageView) rootView.findViewById(R.id.img_SEARCH);
        cross =(ImageView) rootView.findViewById(R.id.img_cross);
        edtsearch = (EditText)rootView.findViewById(R.id.searchitemFragment);

        searchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(mContext, "Search Click", Toast.LENGTH_SHORT).show();
                cross.setVisibility(View.VISIBLE);
                edtsearch.setVisibility(View.VISIBLE);
                searchListener();

                searchs.setVisibility(View.GONE);

            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(mContext, "Cross click", Toast.LENGTH_SHORT).show();
                searchs.setVisibility(View.VISIBLE);
                cross.setVisibility(View.GONE);
                edtsearch.setVisibility(View.GONE);
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String shippingorder = prefs.getString("Orderinfo", "");


              //  Toast.makeText(mContext, "Fragment fab is clicked", Toast.LENGTH_SHORT).show();
                Intent in  = new Intent(getActivity(), Activity_Purchas_Entry.class);
                in.putExtra("Items_List",shippingorder);


                startActivity(in);
                getActivity().finish();


            }
        });


        return rootView;
    }

    private void searchListener() {
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTxtSearch = edtsearch.getText().toString();
                System.out.println("The search key is :" +editTxtSearch);

                item = controller.searchItemCodes();
                itemshopAdapter = new ItemCartAdapter(mContext, item);
                System.out.println("The list is  :" +item);
                System.out.println("The list is  :" +itemshopAdapter);


                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                shopRcyclerView.setLayoutManager(mLayoutManager);
                shopRcyclerView.setItemAnimator(new DefaultItemAnimator());
                shopRcyclerView.setAdapter(itemshopAdapter);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onBackPressed() {
        // super.onBackPressed();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    private void ItemListFragm() {
        if(!Utils.isNetworkAvailable(mContext)){
            Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
        }
        new GetItemList().execute();
    }

    private class GetItemList extends AsyncTask<Void, Void, Void> {
        List<Item> listForSearchConcepts = new ArrayList<Item>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //itemProgressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler httpHandler = new HttpHandler();

            SharedPreferences prefs = getContext().getSharedPreferences("MYPREFERNCE",Context.MODE_PRIVATE);;
            restoredIp = prefs.getString("ip", null);
            System.out.println("test update info ipAddress "+restoredIp);

            String Url = "http://"+ restoredIp +"/android/automobiles/get_data.php?page=item_list&Gcode=0M50PB1QA";
            System.out.println("The fragement url :" +Url);
            String jsonStrr = httpHandler.makeServiceCall(Url);

            Log.e("Test url2", jsonStrr);
            //Log.e("Response from url:", jsonStrr);

            if (jsonStrr != null) {
                try {
                    JSONObject jsonObjj = new JSONObject(jsonStrr);

                    // Getting JSON Array node
                    JSONArray itemss = jsonObjj.getJSONArray("items");

                    // looping through All Contacts
                    for (int i = 0; i < itemss.length(); i++) {

                        JSONObject c = itemss.getJSONObject(i);

                        Item item = new Item();
                        item.setQty(c.optString("qty"));
                        item.setName(c.optString("name"));
                        item.setItemcode(c.optString("itemcode"));
                        item.setMrb(c.optString("rate"));


                        String itemcode = c.optString("itemcode");
                        String itemname = c.optString("name");
                        String itemfont = c.optString("font");
                        String itemid = c.optString("id");
                        String itemqty = c.optString("qty");
                        String itemrates = c.optString("rate");

                        System.out.println("The rate is :"+ itemrates);


                        controller = new DBController(mContext);
                        SQLiteDatabase db = controller.getWritableDatabase();
                        ContentValues cv2 = new ContentValues();

                        cv2.put("nameofitem",itemname );
                        cv2.put("codeoditem",itemcode);
                        cv2.put("qtyofitem", itemqty);
                        cv2.put("rateofitem",itemrates);

                        db.insert("tableofitems", null, cv2);
                        db.close();

                        System.out.println("test_cv2" + cv2);


                        listForSearchConcepts.add(item);
                        Log.i("myitemlist",listForSearchConcepts.toString());
                        System.out.println("Hi"+listForSearchConcepts.toString());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(mContext, "Network Is Not Avialable", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            itemProgressBar.setVisibility(View.GONE);

            itemshopAdapter = new ItemCartAdapter(mContext, (ArrayList<Item>)listForSearchConcepts);
            shopRcyclerView.setAdapter(itemshopAdapter);

            //search();


        }
    }

    private void search() {

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*text = charSequence.toString().toLowerCase(Locale.getDefault());
                System.out.println("The string value " +text);
                itemadpter.filter(text);*/
                System.out.println("the character :" +charSequence.toString());
                itemshopAdapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}