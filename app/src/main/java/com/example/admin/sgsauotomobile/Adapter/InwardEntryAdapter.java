package com.example.admin.sgsauotomobile.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.admin.sgsauotomobile.Models.ListItem;
import com.example.admin.sgsauotomobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.example.admin.sgsauotomobile.ActivityClass.Activity_Inward_Entry_Screen.parmeter;
import static com.example.admin.sgsauotomobile.ActivityClass.Activity_Inward_Entry_Screen.tex_rowCount;
import static com.example.admin.sgsauotomobile.ActivityClass.Activity_Inward_Entry_Screen.txt_total;

/**
 * Created by AndroidTeam2 on 2/13/2017.
 */

public class InwardEntryAdapter extends ArrayAdapter<ListItem> {
    private LayoutInflater mLayoutInflater;
    private ArrayList<ListItem> arraylist;
    List<ListItem> itemList;//arrayList.addAll(itemlist)
    private Context mContext;
    int rowBeforeDel;
    public ArrayList<Integer> Quantity;

    ArrayList<String> testItem = new ArrayList<>();
    private ArrayList<ListItem> list;

    private String all_details;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public InwardEntryAdapter(Context context, int simple_list_item_1, List<ListItem> itemList){

        super(context ,0,itemList);
        this.itemList = itemList;
        mContext = context;
        this.arraylist = new ArrayList<ListItem>();
        this.arraylist.addAll(itemList);
     }

    @Override
    public int getCount() {
        rowBeforeDel = itemList.size();
        System.out.println(" list size is" + rowBeforeDel);
        tex_rowCount.setText("Total Count :" +rowBeforeDel);
        return itemList.size();
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        int test2 = 0;
        ViewHolder holder;
        String code,name,quentity,rate,dist;
        String purName,purcode,purQuty,purrate,purdist;
        Quantity = new ArrayList<Integer>();



          if (view == null) {
              view = LayoutInflater.from(mContext).inflate(R.layout.activity_item, viewGroup, false);
              holder = new ViewHolder();

              holder.itemname = (TextView) view.findViewById(R.id.txtnames);
              holder.itemcode = (TextView) view.findViewById(R.id.txtCodes);
              holder.itemqut = (TextView) view.findViewById(R.id.txtQuty);
              holder.itemdis = (TextView) view.findViewById(R.id.txtDisc);
              holder.itemrate = (TextView) view.findViewById(R.id.txtRate);
              holder.img_delete = (ImageView)view.findViewById(R.id.delete);

              code = itemList.get(position).itemCode;
              name = itemList.get(position).itemName;
              quentity  = itemList.get(position).iteQuty;
              rate = itemList.get(position).itemRate;
              dist = itemList.get(position).itemDisc;

              purName =  "pro_name =" + name + ";";
              purcode =  "pro_code =" + code +  ";";
              purQuty =  "pro_qty ="  + quentity +";";
              purrate =  "pro_rate =" + rate +";";
              purdist =  "pro_disc =" + dist ;

              String all_details = "{"+purName + purcode + purQuty+ purrate+ purdist+"}";
              testItem.add(all_details);
              // notifyDataSetChanged();

              SharedPreferences pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
              SharedPreferences.Editor editor = pref.edit();
              editor.putString("ItemListFinalValue", String.valueOf(testItem));
              editor.clear();
              editor.commit();

              view.setTag(holder);
          }
          else
          {
              holder = (ViewHolder)view.getTag();

          }

          rowBeforeDel = itemList.size();
          System.out.println(" rowBeforeDel " + rowBeforeDel);

          for(int i=0;i<rowBeforeDel;i++){
              int val = Integer.parseInt(itemList.get(i).getIteQuty());
              Quantity.add(val);
              System.out.println("The total quantity of array: " + Quantity +"  hi");

          }

          for(int j = 0; j< Quantity.size(); j++){
              int id = Quantity.get(j);
              test2 = test2 + id;
              System.out.println("The total quantity of array tetst: " + test2);
              txt_total.setText("Total Quantity:"+test2+"");
          }


          holder.itemcode.setText(itemList.get(position).getItemCode());
          holder.itemname.setText(itemList.get(position).getItemName());
          holder.itemqut.setText(itemList.get(position).getIteQuty());
          holder.itemrate.setText(itemList.get(position).getItemRate());
          holder.itemdis.setText(itemList.get(position).getItemDisc());


          holder.img_delete.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                          mContext);

                  alertDialogBuilder.setTitle("Delete");

                  alertDialogBuilder
                          .setCancelable(false)
                          .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog,int id) {
                                  int tests =0 ;
                                  int test = Integer.parseInt(itemList.get(position).getIteQuty());
                                  itemList.remove(position);
                                  notifyDataSetChanged();

                                  txt_total.setText("Total Quantity:");

                              }
                          })
                          .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog,int id) {

                                  dialog.cancel();
                              }
                          });

                  AlertDialog alertDialog = alertDialogBuilder.create();

                  alertDialog.show();
              }

          });


          holder.img_delete.setTag(position);

          return view;


     //   return view;


    }

    class ViewHolder {

        TextView itemname, itemcode,itemqut,itemdis,itemrate;
        ImageView img_delete;

    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        /// System.out.print("response arraylist"+arraylist);
        itemList.clear();
        if (charText.length() == 0) {
            itemList.addAll(arraylist);
            // System.out.print("response search nothing"+itemList);

        } else {
            for (ListItem postDetail : arraylist) {
                //System.out.print("response search nothing"+arraylist);
                if (charText.length() != 0 && postDetail.getItemCode().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getItemName().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getIteQuty().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getItemRate().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getItemDisc().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    itemList.add(postDetail);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filter2(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        itemList.clear();
        if (charText.length() == 0) {
            itemList.addAll(arraylist);


        }
        else
        {
            for (ListItem postDetail : arraylist) {

                if (charText.length() != 0 && postDetail.getItemCode().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getItemName().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getIteQuty().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getItemRate().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getItemDisc().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    itemList.add(postDetail);
                }
            }
        }
        notifyDataSetChanged();
    }



}
