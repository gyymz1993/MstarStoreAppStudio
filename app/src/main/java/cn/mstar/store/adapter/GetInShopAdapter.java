package cn.mstar.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.entity.ShopEntity;
import cn.mstar.store.utils.ImageLoadOptions;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class GetInShopAdapter extends BaseAdapter {
    private Context mContext;
    List<ShopEntity> data;
    private boolean isSinglePro;
    private int position_select = -1;

    public GetInShopAdapter(Context mContext, List data) {
        this.mContext = mContext;
        this.data = data;
    }

    public void notifyDataSetChanged(boolean flag,int position_select) {
        isSinglePro = flag;
        this.position_select = position_select;
        notifyDataSetChanged();
    }

    public void singleClick(int position_select){
        this.position_select = position_select;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ShopEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.get_in_shop_item, parent, false);
            vh.single = (RadioButton) convertView.findViewById(R.id.single_button);
            vh.img = (ImageView) convertView.findViewById(R.id.img);
            vh.store_name = (TextView) convertView.findViewById(R.id.store_name);
            vh.contact_num = (TextView) convertView.findViewById(R.id.contact_num);
            vh.business_hours = (TextView) convertView.findViewById(R.id.business_hours);
            vh.inventory = (TextView) convertView.findViewById(R.id.inventory);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.single.setChecked(position_select == position);
        ImageLoader.getInstance().displayImage(getItem(position).getStore_label(), vh.img, ImageLoadOptions.getOptions());
        vh.store_name.setText(getItem(position).getStore_name());
        vh.contact_num.setText(mContext.getResources().getString(R.string.get_in_shop_contact_num) + getItem(position).getStore_tel());
        vh.business_hours.setText(mContext.getResources().getString(R.string.get_in_shop_business_hours) + "8:00-22:00");
        //if (isSinglePro) {
            vh.inventory.setText(mContext.getResources().getString(R.string.get_in_shop_inventory) + "充足");
        /*} else {
            vh.inventory.setText(mContext.getResources().getString(R.string.get_in_shop_inventory) + getItem(position).getGoods_count().getProId_1());
        }*/
        return convertView;
    }

    class ViewHolder {
        RadioButton single;
        ImageView img;
        TextView store_name;
        TextView contact_num;
        TextView business_hours;
        TextView inventory;
    }
}
