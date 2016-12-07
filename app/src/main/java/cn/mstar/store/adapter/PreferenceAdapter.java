package cn.mstar.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.activity.PreferenceContentActivity;
import cn.mstar.store.entity.PreferenceContentItem;
import cn.mstar.store.utils.ImageLoadOptions;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class PreferenceAdapter extends BaseAdapter {
    private Context mContext;
    private List<PreferenceContentItem> mData;

    public PreferenceAdapter(Context mContext,List<PreferenceContentItem> mData){
        this.mContext = mContext;
        this.mData = mData;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public PreferenceContentItem getItem(int position) {
        return (PreferenceContentItem)mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_preference_content,parent,false);
            vh.preference_time = (TextView) convertView.findViewById(R.id.preference_item_time);
            vh.preference_img = (ImageView) convertView.findViewById(R.id.preference_item_img);
            vh.preference_name = (TextView) convertView.findViewById(R.id.preference_item_name);
            vh.preference_desc = (TextView) convertView.findViewById(R.id.preference_item_desc);
            vh.preference_read = (TextView) convertView.findViewById(R.id.preference_item_read);
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.preference_time.setText(getItem(position).getTime());
        vh.preference_name.setText(getItem(position).getName());
        vh.preference_desc.setText(getItem(position).getDescript());
        ImageLoader.getInstance().displayImage(getItem(position).getPic(), vh.preference_img, ImageLoadOptions.getOptions());
        vh.preference_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PreferenceContentActivity.class);
                intent.putExtra("title",getItem(position).getName());
                intent.putExtra("activity_id",getItem(position).getActivity_id());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView preference_time;
        ImageView preference_img;
        TextView preference_name;
        TextView preference_desc;
        TextView preference_read;
    }
}
