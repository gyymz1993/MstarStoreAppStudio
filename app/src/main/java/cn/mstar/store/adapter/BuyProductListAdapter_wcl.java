package cn.mstar.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import cn.mstar.store.R;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class BuyProductListAdapter_wcl extends BaseAdapter{
    private Context mContext;
    private List mData;
    public BuyProductListAdapter_wcl(Context mContext,List mData){
        this.mContext = mContext;
        this.mData = mData;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null){
            vh = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_buy_product_wcl,parent,false);
            vh.list = (ListView) convertView.findViewById(R.id.buy_product_wcl_list);
//            View header = inflater.inflate(R.layout.)
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        ListView list;
    }
}
