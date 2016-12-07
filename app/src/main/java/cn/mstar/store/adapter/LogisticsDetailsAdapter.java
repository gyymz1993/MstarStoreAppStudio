package cn.mstar.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mstar.store.entity.LogisticsItem;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class LogisticsDetailsAdapter extends BaseAdapter {
    private Context mContext;
    private LogisticsItem mData;
    private int mScreenWidth;
    private Map<String,String> temp;
    List<Map<String,String>> tempList;
    private String flag;

    public LogisticsDetailsAdapter(Context mContext,LogisticsItem mData, int mScreenWidth){
        this.mContext = mContext;
        this.mData = mData;
        this.mScreenWidth = mScreenWidth;
        tempList = new ArrayList<Map<String,String>>();
        handleData();
    }

    private void handleData(){
        String split;
        for (int i = 0; i < mData.getShipInfo().size(); i++){
            temp = new HashMap<String,String>();
            split = convertTime(mData.getShipInfo().get(i).getTime());
            if (i == 0) {
                if (split.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
                    temp.put("date", "今天");
                }else{
                    temp.put("date",split);
                }
                flag = split;
            }
            else{
                if (flag.equals(split)){
                    temp.put("date",null);
                }else{
                    temp.put("date",split);
                    flag = split;
                }
            }
            temp.put("time",mData.getShipInfo().get(i).getTime());
            temp.put("context",mData.getShipInfo().get(i).getContext());
            temp.put("ftime", mData.getShipInfo().get(i).getFtime());
        }
    }
    
    private String convertTime(String time){
        return time.split(" ")[0];
    }

    @Override
    public int getCount() {
        return tempList.size();
    }

    @Override
    public Map<String,String> getItem(int position) {
        return tempList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }

    class ViewHolder {

    }
}
