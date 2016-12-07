package cn.mstar.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.entity.PopEntity;
import cn.mstar.store.utils.ImageLoadOptions;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class PopularizeAdapter extends BaseAdapter {

    private Context mContext;
    private List<PopEntity> mData;

    public PopularizeAdapter(Context mContext, List mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public PopEntity getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pop_layout, parent, false);
            vh.pop_img = (ImageView) convertView.findViewById(R.id.pop_img);
            vh.pop_nickname = (TextView) convertView.findViewById(R.id.pop_nick_name);
            vh.pop_num = (TextView) convertView.findViewById(R.id.pop_num);
            vh.pop_addtime = (TextView) convertView.findViewById(R.id.pop_addtime);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(getItem(position).getLogo(), vh.pop_img, ImageLoadOptions.getOptions());
        vh.pop_nickname.setText("昵称：" + getItem(position).getUsername());
        vh.pop_num.setText("编号：" + getItem(position).getUserNo());
        vh.pop_addtime.setText(getItem(position).getAddtime());
        return convertView;
    }
    class ViewHolder {
        ImageView pop_img;
        TextView pop_nickname;
        TextView pop_num;
        TextView pop_addtime;
    }
}
