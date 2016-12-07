package cn.mstar.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.activity.LogisticsDetialsActivity;
import cn.mstar.store.entity.LogisticsItem;
import cn.mstar.store.functionutils.LogUtils;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class LogisticsInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<LogisticsItem> mData;
    private int mScreenWidth;

    public LogisticsInfoAdapter(Context mContext, List<LogisticsItem> mData, int mScreenWidth) {
        this.mContext = mContext;
        this.mData = mData;
        this.mScreenWidth = mScreenWidth;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public LogisticsItem getItem(int position) {
        return (LogisticsItem) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_logistics_info_list, parent, false);
            vh.logistics_info_context = (TextView) convertView.findViewById(R.id.logistics_time);
            vh.logistics_info_context = (TextView) convertView.findViewById(R.id.logistics_info_context);
            vh.logistics_info_time = (TextView) convertView.findViewById(R.id.logistics_info_time);
            vh.logistics_info_action = (TextView) convertView.findViewById(R.id.logistics_info_action);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vh.logistics_info_context.getLayoutParams();
        params.width = mScreenWidth * 3 / 4;
        vh.logistics_info_context.setLayoutParams(params);
        vh.logistics_info_context.setText(getContext(position));
        vh.logistics_info_time.setText(getInfoTime(position));
        vh.logistics_info_action.setText(mContext.getResources().getString(R.string.logistics_go_to_detail));
        vh.logistics_info_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LogisticsDetialsActivity.class);
                intent.putExtra("shippingCode",getItem(position).getShpping_code() + "");
                intent.putExtra("eCode",getItem(position).getE_code());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private String getContext(int position) {
        if (getItem(position).getShipInfo() == null || getItem(position).getShipInfo().size() == 0){
            return "暂无信息";
        }
        return getItem(position).getShipInfo().get(0).getContext();
    }

    private String getInfoTime(int position) {
        if (getItem(position).getShipInfo() == null || getItem(position).getShipInfo().size() == 0){
            return "";
        }
        return getItem(position).getShipInfo().get(0).getFtime();
    }

    class ViewHolder {
        TextView logistics_time;
        TextView logistics_info_context;
        TextView logistics_info_time;
        TextView logistics_info_action;
    }
}
