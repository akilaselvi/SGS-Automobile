package com.example.admin.sgsauotomobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.admin.sgsauotomobile.Models.ItemReport;
import com.example.admin.sgsauotomobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-Team1 on 3/13/2017.
 */
 public class ItemReportAdapter extends ArrayAdapter<ItemReport> {
        private Context mContext;
        private static String LOG_TAG = "ItemReportAdapter";
        private ArrayList<ItemReport> mDataset;
        List<ItemReport> itemNameArrayList;
        LayoutInflater inflater;
        private static int added_value;
        private ArrayList<String> cartItem = new ArrayList<String>();
      public String[] Current;
        public ItemReportAdapter(Context mContext, List<ItemReport> itemNameArrayList) {
            super(mContext,0);

            this.mContext = mContext;
            this.itemNameArrayList = itemNameArrayList;
            inflater = LayoutInflater.from(mContext);
            this.mDataset = new ArrayList<>();
            this.mDataset.addAll(itemNameArrayList);

        }
    @Override
    public int getCount() {

        return itemNameArrayList.size();
    }

    @Override
    public ItemReport getItem(int position) {
        return itemNameArrayList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }




    private Context context;
    private ArrayList<String> data = new ArrayList<String>();
    // public static ViewHolder viewHolder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
//        final ItemReport item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_items_card_for_report, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.name_report = (TextView) convertView.findViewById(R.id.item_name_report_list);
            viewHolder.code_report = (TextView) convertView.findViewById(R.id.item_code_report_list);
            viewHolder.quantity_report = (TextView) convertView.findViewById(R.id.item_quantity_report_list);
            viewHolder.rate_report =(TextView) convertView.findViewById(R.id.item_rate_report_list);
            viewHolder.discount_report =(TextView) convertView.findViewById(R.id.discount_report_list);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.quantity_report.setText(mDataset.get(position).getQty());
        viewHolder.code_report.setText(mDataset.get(position).getItemcode());
        viewHolder.name_report.setText(mDataset.get(position).getName());
        viewHolder.discount_report.setText(mDataset.get(position).getDiscount());
        viewHolder.rate_report.setText(mDataset.get(position).getRate());




        return convertView;
    }
    public class ViewHolder {
        TextView name_report,code_report,discount_report,quantity_report,rate_report ;

    }
}
