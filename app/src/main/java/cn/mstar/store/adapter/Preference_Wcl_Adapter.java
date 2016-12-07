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
import cn.mstar.store.activity.ProductDetailsActivity;
import cn.mstar.store.app.MyAction;
import cn.mstar.store.entity.PreferenceListItem;
import cn.mstar.store.utils.ImageLoadOptions;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class Preference_Wcl_Adapter extends BaseAdapter{
    private Context mContext;
    private List<PreferenceListItem> mData;

    public Preference_Wcl_Adapter(Context mContext, List<PreferenceListItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public PreferenceListItem getItem(int position) {
        return (PreferenceListItem)mData.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_preference_wcl,parent,false);
            vh.p_img = (ImageView) convertView.findViewById(R.id.preference_wcl_img);
            vh.p_name = (TextView) convertView.findViewById(R.id.preference_wcl_name);
            vh.p_fr = (TextView) convertView.findViewById(R.id.preference_wcl_price_fr);
            vh.p_sc = (TextView) convertView.findViewById(R.id.preference_wcl_price_sc);
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.p_name.setText(getItem(position).getName());
        vh.p_fr.setText(getItem(position).getPrice() + "");
        vh.p_sc.setText(getItem(position).getMkprice() + "");
        ImageLoader.getInstance().displayImage(getItem(position).getPic(), vh.p_img, ImageLoadOptions.getOptions());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithId(getItem(position).getProId());
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

    class ViewHolder{
        ImageView p_img;
        TextView p_name;
        TextView p_fr;
        TextView p_sc;
    }
}
