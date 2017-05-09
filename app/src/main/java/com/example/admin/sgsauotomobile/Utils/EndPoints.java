package com.example.admin.sgsauotomobile.Utils;


import com.example.admin.sgsauotomobile.ActivityClass.LoginActivity;

/**
 * Created by Android-Team1 on 2/10/2017.
 */

public class EndPoints {
    public static String weather = "http://api.apixu.com/v1/current.json?key=f6d6dafd24ea4b4ead761745163108&q=Coimbatore";
    public static String allitems = "http://192.168.0.3/sgs_traders/sgs_datas.php?data=all";
    public static String allitemss = "http://"+ LoginActivity.ip+"/sgs_traders/sgs_datas.php?data=all";
    public static String item_search = "http://192.168.1.4/sgs_traders/sgs_datas.php?page=search&godown_id=_0M50PB1QA&search_date=19-1-2017&Search_value=";
            //"http://192.168.1.4/sgs_traders/sgs_datas.php?user_godown_id=_0M50PB1QA&date=15-1-2017";
}
