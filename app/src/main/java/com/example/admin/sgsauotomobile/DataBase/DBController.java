package com.example.admin.sgsauotomobile.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.admin.sgsauotomobile.ActivityClass.Activity_Inward_Entry_Screen;
import com.example.admin.sgsauotomobile.ActivityClass.Activity_Purchas_Entry;
import com.example.admin.sgsauotomobile.Fragment.Fragment_Items_One;
import com.example.admin.sgsauotomobile.Fragment.Fragment_Items_purchase;
import com.example.admin.sgsauotomobile.Models.Godown;
import com.example.admin.sgsauotomobile.Models.Item;
import com.example.admin.sgsauotomobile.Models.ListItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Android-Team1 on 2/4/2017.
 */

public class DBController extends SQLiteOpenHelper {

    private static final String tablename = "items";  // table name
    private static final String name = "name";  // column name
    private static final String id = "ID";  // auto generated ID column
    private static final String itemcode = "itemcode";
    private static final String quantity = "quantity";// column name
    private static final String databasename = "iteminfo"; // Dtabasename
    private static final int versioncode = 1; //versioncode of the database

    //VENDOR INFORMATION DB
    private static final String VendorTable = "tableofvendor";
    private static final String PartyName = "nameofparty";
    private static final String BillNo = "billNoofparty";
    private static final String Date = "dateofparty";
    private static final String GowdnCode ="vendorgodowncode";
    private static final String ActItemName="actitemname";
    private static final String ActItemCode="actitemcode";
    private static final String ActItemRate = "actitemrate";
    private static final String ActItemDiscount = "actitemdisc";
    private static final String ActItemQuty="actitemquty";
    private static final String ActItemTotal = "acttotalitem";


    //ALL ITEM DB
    private static final String ItemTable = "tableofitems";
    private static final String ItemCodes = "codeoditem";
    private static final String ItemName = "nameofitem";
    private static final String ItemRate = "rateofitem";
    private static final String ItemDiscount ="discofitem";
    private static final String ItemQuantity = "qtyofitem";
    private static final String ItemTotalRate ="toalrateofitem";




    public DBController(Context context) {
        super(context, databasename, null, versioncode);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query,vendorQuery,itemQuery;
        query = "CREATE TABLE IF NOT EXISTS " + tablename + "(" + id + " integer primary key, "
                                                                + name + " text, "
                                                                + quantity + " text, "
                                                                + itemcode + " text)";

        vendorQuery = "CREATE TABLE IF NOT EXISTS " + VendorTable + "(" + PartyName + " TEXT, "
                        + BillNo + " TEXT, " + Date + " INTEGER ," + GowdnCode + " TEXT, "+
                ActItemName + " TEXT, " + ActItemCode + " TEXT, " + ActItemQuty +" TEXT, " +
                ActItemRate + " TEXT, " +ActItemDiscount +" TEXT )";



        itemQuery = "CREATE TABLE IF NOT EXISTS " + ItemTable + "(" + ItemCodes + " TEXT, "
                    + ItemName + " TEXT, " + ItemQuantity + " TEXT, " + ItemRate + " TEXT, "
                    + ItemDiscount + " TEXT, " + ItemTotalRate + " TEXT)";

        database.execSQL(query);
        database.execSQL(vendorQuery);
        database.execSQL(itemQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old,
                          int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS " + tablename;
        database.execSQL(query);
        onCreate(database);

    }


    public  void delete(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" delete from " +tablename);
        db.execSQL(" delete from " + ItemTable);
    }
    public ArrayList<HashMap<String, String>> getNewVendor() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + VendorTable;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(PartyName, cursor.getString(0));
                map.put(BillNo, cursor.getString(1));
                map.put(Date, cursor.getString(2));
                map.put(GowdnCode,cursor.getString(3));
                wordList.add(map);
                System.out.println("The gowdown :" +String.valueOf(map));
                System.out.println("The gowdown1 :" +map);
                System.out.println("The gowdown :" +map.toString());

                System.out.println("DBHelper: 0" + PartyName);
                System.out.println("DBHelper: 1" + BillNo);
                System.out.println("DBHelper 2" + Date);
                System.out.println("DBHelper 2" + GowdnCode);

                System.out.println("The gowdown :" +String.valueOf(wordList));
                System.out.println("The gowdown1 :" +wordList);
                System.out.println("The gowdown :" +wordList.toString());
            } while (cursor.moveToNext());
        }
        return wordList;
    }

    public ArrayList<Item> getItems(){
        String selectqurey = "SELECT * FROM " + ItemTable;
        ArrayList<Item> ItemInfo = new ArrayList<Item>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(selectqurey, null);
        if (c != null) {
            while (c.moveToNext()) {
                String code = c.getString(c.getColumnIndex(ItemCodes));
                String name = c.getString(c.getColumnIndex(ItemName));
                String disc = c.getString(c.getColumnIndex(ItemDiscount));
                String quty = c.getString(c.getColumnIndex(ItemQuantity));
                String rates = c.getString(c.getColumnIndex(ItemRate));

                System.out.println("The item is : " +code);
                System.out.println("The item is : " +name);
                System.out.println("The item is : " +quty);
                System.out.println("The item is : " +rates);

                Item listItem = new Item();
                listItem.setItemcode(code);
                listItem.setName(name);
                listItem.setMrb(rates);
                listItem.setQty(quty);
                ItemInfo.add(listItem);

            }
        }
        database.close();
        return ItemInfo;

    }

    public ArrayList<HashMap<String, String>> getItemDetails() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String codes = Activity_Inward_Entry_Screen.codeEdt;
        String selectQuery = "SELECT * FROM " + ItemTable +" WHERE " + ItemCodes +" = '" +codes +"'";
        System.out.println("selection query : " + selectQuery);


        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String name1 = cursor.toString();
        System.out.println("selection query result : " + name1);


            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(ItemRate));
                    System.out.println("The name is :" + name);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("codeoditem", cursor.getString(cursor.getColumnIndex(ItemCodes)));
                    map.put("nameofitem", cursor.getString(cursor.getColumnIndex(ItemName)));
                    map.put("rateofitem", cursor.getString(cursor.getColumnIndex(ItemRate)));


                    wordList.add(map);

                } while (cursor.moveToNext());
            }
        // return contact list
        return wordList;

    }

    public ArrayList<HashMap<String, String>> getItemDetailspur() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String codes = Activity_Purchas_Entry.codeEdt;
        String selectQuery = "SELECT * FROM " + ItemTable +" WHERE " + ItemCodes +" = '" +codes +"'";
        System.out.println("selection query : " + selectQuery);


        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String name1 = cursor.toString();
        System.out.println("selection query result : " + name1);


        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(ItemRate));
                System.out.println("The name is :" + name);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("codeoditem", cursor.getString(cursor.getColumnIndex(ItemCodes)));
                map.put("nameofitem", cursor.getString(cursor.getColumnIndex(ItemName)));
                map.put("rateofitem", cursor.getString(cursor.getColumnIndex(ItemRate)));


                wordList.add(map);

            } while (cursor.moveToNext());
        }
        // return contact list
        return wordList;

    }

    public ArrayList<Item> searchItemCodes() {
        ArrayList<Item> itemList;
        itemList = new ArrayList<Item>();
        String searchKey = Fragment_Items_purchase.editTxtSearch;
        System.out.println("The query is :" +searchKey);
        //SELECT * FROM `item` WHERE `item` like '%1%' and `code` like '%1%'

        String selectQuery = "SELECT * FROM " + ItemTable + " WHERE " + ItemName +" like " +"'%" + searchKey + "%'"
                +" or " + ItemCodes + " like " +"'%" + searchKey + "%'" ;

        System.out.println("The query is :" +selectQuery);

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String name =  cursor.getString(cursor.getColumnIndex(ItemName));
                String code =  cursor.getString(cursor.getColumnIndex(ItemCodes));
                String mrp  = cursor.getString(cursor.getColumnIndex(ItemRate));
                String quty = cursor.getString(cursor.getColumnIndex(ItemQuantity));

                System.out.println("The selected item is :" +name);
                System.out.println("The selected item is :" +code);
                System.out.println("The selected item is :" +mrp);


                Item ItemList = new Item();
                ItemList.setName(name);
                ItemList.setItemcode(code);
                ItemList.setMrb(mrp);
                ItemList.setQty(quty);


                itemList.add(ItemList);


            } while (cursor.moveToNext());
        }
        // return contact list
        return itemList;
    }


    public ArrayList<Item> searchItemCode() {
        ArrayList<Item> itemList;
        itemList = new ArrayList<Item>();
        String searchKey = Fragment_Items_One.editTxtSearch;
        System.out.println("The query is :" +searchKey);
        //SELECT * FROM `item` WHERE `item` like '%1%' and `code` like '%1%'

        String selectQuery = "SELECT * FROM " + ItemTable + " WHERE " + ItemName +" like " +"'%" + searchKey + "%'"
                +" or " + ItemCodes + " like " +"'%" + searchKey + "%'" ;

        System.out.println("The query is :" +selectQuery);

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String name =  cursor.getString(cursor.getColumnIndex(ItemName));
                String code =  cursor.getString(cursor.getColumnIndex(ItemCodes));
                String mrp  = cursor.getString(cursor.getColumnIndex(ItemRate));
                String quty = cursor.getString(cursor.getColumnIndex(ItemQuantity));

                System.out.println("The selected item is :" +name);
                System.out.println("The selected item is :" +code);
                System.out.println("The selected item is :" +mrp);


                Item ItemList = new Item();
                ItemList.setName(name);
                ItemList.setItemcode(code);
                ItemList.setMrb(mrp);
                ItemList.setQty(quty);


                itemList.add(ItemList);


            } while (cursor.moveToNext());
        }
        // return contact list
        return itemList;
    }
    //SELECT * FROM `item` WHERE `item` like '%1%' and `code` like '%1%'

//    public ArrayList<NewItemList> getItemDetails() {
//        ArrayList<NewItemList> wordList;
//        wordList = new ArrayList<NewItemList>();
//        String codes = Activity_Inward_Entry_Screen.code;
//        String selectQuery = "SELECT * FROM " + ItemTable +" WHERE " + ItemCodes +" = '" +codes +"'";
//        SQLiteDatabase database = this.getWritableDatabase();
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                String name =  cursor.getString(cursor.getColumnIndex(ItemName));
//                String code =  cursor.getString(cursor.getColumnIndex(ItemCodes));
//                String mrp  = cursor.getString(cursor.getColumnIndex(ItemRate));
//
//                System.out.println("The selected item is :" +name);
//                System.out.println("The selected item is :" +code);
//                System.out.println("The selected item is :" +mrp);
//
//
//                NewItemList newItemList = new NewItemList();
//                newItemList.setItemName(name);
//                newItemList.setItemCode(code);
//                newItemList.setRate(mrp);
//
//                wordList.add(newItemList);
//
//
//            } while (cursor.moveToNext());
//        }
//        // return contact list
//        return wordList;
//    }

 /*   public String getItemDetails(){
        String codes = Activity_Inward_Entry_Screen.code;

        String selectcode = "SELECT * FROM " + ItemTable +" WHERE " + ItemCodes +" = '" +codes +"'";

        System.out.println("The selected item is :" +selectcode);

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectcode, null);

        String name = "";

        if (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ItemName));
            name = cursor.getString(cursor.getColumnIndex(ItemRate));
            System.out.println("The selected item  :" +name);
        }

        database.close();
        return str;

    }
*/
    public int getItemCount() {
        String countQuery = "SELECT  * FROM " + ItemTable;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        System.out.println("The count db from dadabase:" +cnt);
        cursor.close();
        return cnt;
    }



    public ArrayList<Godown> getVendor() {
        String selectqurey = "SELECT * FROM " + VendorTable;
        ArrayList<Godown> vendorInfo = new ArrayList<Godown>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(selectqurey, null);
        if (c != null) {
            while (c.moveToNext()) {
                String name = c.getString(c.getColumnIndex(PartyName));
                String code = c.getString(c.getColumnIndex(GowdnCode));
                String billno = c.getString(c.getColumnIndex(BillNo));
                String date = c.getString(c.getColumnIndex(Date));
                String itemname = c.getString(c.getColumnIndex(ActItemName));
                String itemcode = c.getString(c.getColumnIndex(ActItemCode));
                String itemquty = c.getString(c.getColumnIndex(ActItemQuty));
                String itemrate = c.getString(c.getColumnIndex(ActItemRate));
                String itemdisc = c.getString(c.getColumnIndex(ActItemDiscount));

                Godown godown = new Godown();
                godown.setPartyName(name);
                godown.setGcode(code);
                godown.setPartyBillNo(billno);
                godown.setPartyDate(date);
                godown.setItemName(itemname);
                godown.setItemCode(itemcode);
                godown.setItemQuty(itemquty);
                godown.setItemDisc(itemdisc);
                godown.setItemRate(itemrate);
                vendorInfo.add(godown);
            }
        }
        database.close();
        return vendorInfo;
    }

    /* Method for fetching record from Database (For all items into cardview or grid view using pojo class)*/
    public ArrayList<Item> getAllEmployee() {
        String selectQuery = "SELECT  * FROM " + tablename;
        ArrayList<Item> employees = new ArrayList<Item>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        if (c != null) {
            while (c.moveToNext()) {
                String code1 = c.getString(c.getColumnIndex(id));
                String name1 = c.getString(c.getColumnIndex(name));
                String quantity1 = c.getString(c.getColumnIndex(quantity));
                String itemcode1 = c.getString(c.getColumnIndex(itemcode));

                Item emp = new Item();
                emp.setName(name1);
                emp.setQty(quantity1);
                emp.setItemcode(itemcode1);

                Log.v("DBHelper: ", "Name: " + name1);
                Log.v("DBHelper: ", "Code: " + code1);
                Log.v("DBHelper: ", "Email: " + quantity1);

                employees.add(emp);
            }
        }
        database.close();
        return employees;

    }


    /* Method for fetching record from Database (For all items into cardview or grid view using pojo class)*/
    public ArrayList<ListItem> getAllListItem() {
        String selectQuery = "SELECT  * FROM " + tablename;
        ArrayList<ListItem> employees = new ArrayList<ListItem>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        if (c != null) {
            while (c.moveToNext()) {
                String code1 = c.getString(c.getColumnIndex(id));
                String name1 = c.getString(c.getColumnIndex(name));
                String quantity1 = c.getString(c.getColumnIndex(quantity));
                String itemcode1 = c.getString(c.getColumnIndex(itemcode));

                ListItem emp = new ListItem();
                emp.setItemName(name1);
                emp.setIteQuty(quantity1);
                emp.setItemCode(itemcode1);

                Log.v("DBHelper: ", "Name: " + name1);
                Log.v("DBHelper: ", "Code: " + code1);
                Log.v("DBHelper: ", "Email: " + quantity1);

                employees.add(emp);
            }
        }
        database.close();
        return employees;

    }


//(get all items into listview(like single row)--listview)
    public ArrayList<HashMap<String, String>> getAllPlace() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + tablename;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("name", cursor.getString(1));
                map.put("quantity", cursor.getString(2));
                map.put("itemcode", cursor.getString(3));

                wordList.add(map);
            } while (cursor.moveToNext());
        }
        // return contact list
        return wordList;
    }
/*
    public ArrayList<String> getAllPlace() {
        ArrayList<String> wordList;
        wordList = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + VendorTable;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("name", cursor.getString(1));
                map.put("quantity", cursor.getString(2));
                map.put("itemcode", cursor.getString(3));

                wordList.add(String.valueOf(map));
            } while (cursor.moveToNext());
        }
        // return contact list
        return wordList;
    }
*/



}