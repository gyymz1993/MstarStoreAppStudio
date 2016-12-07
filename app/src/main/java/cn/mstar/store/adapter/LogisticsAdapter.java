package cn.mstar.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.activity.BaseActivity;
import cn.mstar.store.entity.ShipInfo;
import cn.mstar.store.utils.L;

/**
 * Created by Administrator on 2015/8/25.
 */
public class LogisticsAdapter extends BaseAdapter{
    private Context context;
    private List<ShipInfo> list;
    private LayoutInflater inflater;
    public LogisticsAdapter(Context context,List<ShipInfo> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        if (list == null || list.size() == 0){
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_logistics_details_content,null);
            viewHolder.catalogTV= (TextView) convertView.findViewById(R.id.contactitem_catalog);
            viewHolder.contentTV= (TextView) convertView.findViewById(R.id.item_content);
            viewHolder.topLintView=(TextView) convertView.findViewById(R.id.top_line_view);
            viewHolder.logistics_img=(ImageView)convertView.findViewById(R.id.logistics_imageView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ShipInfo shipInfo=list.get(position);
        if(position==0){
            viewHolder.catalogTV.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String date=sdf.format(new java.util.Date());
            if (shipInfo.getTime().split(" ")[0].equals(date)) {
                viewHolder.catalogTV.setText("今天");
            }else{
                viewHolder.catalogTV.setText(shipInfo.getTime().split(" ")[0].substring(2));
            }
            viewHolder.catalogTV.setVisibility(View.VISIBLE);
            viewHolder.topLintView.setVisibility(View.GONE);
            viewHolder.contentTV.setText(shipInfo.getTime().substring(11,16) + " " + shipInfo.getContext());
            viewHolder.contentTV.setBackgroundResource(R.drawable.logistics_text_color);
            viewHolder.logistics_img.setImageResource(R.drawable.logistics_icon_point);

        }else{

            if(shipInfo.getTime().split(" ")[0].equals(list.get(position-1).getTime().split(" ")[0])){
                viewHolder.catalogTV.setVisibility(View.GONE);
                viewHolder.topLintView.setVisibility(View.VISIBLE);
                viewHolder.contentTV.setText(shipInfo.getTime().substring(11,16) + " " + shipInfo.getContext());
            }else{
                viewHolder.catalogTV.setVisibility(View.VISIBLE);
                viewHolder.topLintView.setVisibility(View.GONE);
                viewHolder.catalogTV.setText(shipInfo.getTime().split(" ")[0].substring(2));
                viewHolder.contentTV.setText(shipInfo.getTime().substring(11,16) + " " + shipInfo.getContext());
            }
            viewHolder.contentTV.setBackgroundResource(R.drawable.logistics_text_gray);
            viewHolder.logistics_img.setImageResource(R.drawable.logistics_icon_point_grey);
        }

        return convertView;
    }

    class ViewHolder{
        public TextView catalogTV,contentTV;
        public ImageView logistics_img;
        public TextView topLintView;
        public TextView bottomLintView;
    }
}
