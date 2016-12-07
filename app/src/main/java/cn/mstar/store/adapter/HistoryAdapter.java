package cn.mstar.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.activity.ProductDetailsActivity;
import cn.mstar.store.activity.ScanHistoryActivity;
import cn.mstar.store.app.MyAction;
import cn.mstar.store.entity.HistoryItem;
import cn.mstar.store.utils.ImageLoadOptions;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class HistoryAdapter extends BaseAdapter{
    private Context mContext;
    private List dataList;
    private int screenWidth;
    private LayoutInflater inflater;

    private OnDeleteListener mOnDeleteListener;

    public HistoryAdapter(Context mContext,List<HistoryItem> dataList,int scw){
        this.mContext = mContext;
        this.dataList = dataList;
        this.screenWidth = scw;
        inflater = LayoutInflater.from(mContext);
    }

    public void deleteItem(HistoryItem item){
        dataList.remove(item);
        notifyDataSetChanged();
    }

    public void clearList(){
        dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public HistoryItem getItem(int position) {
        return (HistoryItem)dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null){
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_scan_history,parent,false);
            vh.his_delete_img = (ImageView) convertView.findViewById(R.id.his_delete_img);
            vh.his_pre_img = (ImageView) convertView.findViewById(R.id.his_pre_img);
            vh.his_desc = (TextView) convertView.findViewById(R.id.his_desc_txt);
            vh.his_price = (TextView) convertView.findViewById(R.id.his_price_txt);
            vh.his_time = (TextView) convertView.findViewById(R.id.his_time_txt);
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder)convertView.getTag();
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vh.his_desc.getLayoutParams();
        params.width = screenWidth / 2;
        vh.his_desc.setLayoutParams(params);
        vh.his_desc.setText(getItem(position).getName());
        vh.his_price.setText(getItem(position).getPrice() + "");
        vh.his_time.setText(getItem(position).getBrowse_time());
        ImageLoader.getInstance().displayImage(getItem(position).pic, vh.his_pre_img, ImageLoadOptions.getOptions());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithId(getItem(position).proId);
            }
        });
        vh.his_delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDeleteListener.onDelete(getItem(position),getCount());
            }
        });
        return convertView;
    }

    protected void startActivityWithId(int proId) {
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        intent.putExtra("proId", proId);
        intent.setAction(MyAction.productListActivityAction);
        mContext.startActivity(intent);
    }

    class ViewHolder {
        ImageView his_pre_img;
        ImageView his_delete_img;
        TextView his_desc;
        TextView his_price;
        TextView his_time;
    }

    public interface OnDeleteListener{
        void onDelete(HistoryItem item,int size);
    }

    public void setOnDeleteListener(OnDeleteListener mOnDeleteListener){
        this.mOnDeleteListener = mOnDeleteListener;
    }
}
