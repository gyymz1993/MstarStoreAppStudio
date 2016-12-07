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
import cn.mstar.store.app.MyAction;
import cn.mstar.store.entity.FavoriteManagerItem;
import cn.mstar.store.utils.ImageLoadOptions;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class CollectionContentAdapter extends BaseAdapter{
    private Context mContext;
    private List<FavoriteManagerItem.InnerData.Favorite> mData;
    private int mScreenWidth;

    private OnDeleteItemListener mDeleteInterface;

    public CollectionContentAdapter(Context mContext, List<FavoriteManagerItem.InnerData.Favorite> mData, int mScreenWidth){
        this.mContext = mContext;
        this.mData = mData;
        this.mScreenWidth = mScreenWidth;
    }

    public void changeData(List<FavoriteManagerItem.InnerData.Favorite> mData){
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void deleteData(FavoriteManagerItem.InnerData.Favorite item){
        mData.remove(item);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public FavoriteManagerItem.InnerData.Favorite getItem(int position) {
        return (FavoriteManagerItem.InnerData.Favorite)mData.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_scan_history, parent, false);
            vh.collection_delete_img = (ImageView) convertView.findViewById(R.id.his_delete_img);
            vh.collection_pre_img = (ImageView) convertView.findViewById(R.id.his_pre_img);
            vh.collection_desc = (TextView) convertView.findViewById(R.id.his_desc_txt);
            vh.collection_price = (TextView) convertView.findViewById(R.id.his_price_txt);
            vh.collection_time = (TextView) convertView.findViewById(R.id.his_time_txt);
            vh.collection_time.setVisibility(View.GONE);
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder)convertView.getTag();
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vh.collection_desc.getLayoutParams();
        params.width = mScreenWidth / 2;
        vh.collection_desc.setLayoutParams(params);
        vh.collection_desc.setText(getItem(position).title);
        vh.collection_price.setText(getItem(position).price + "");
        ImageLoader.getInstance().displayImage(getItem(position).pic, vh.collection_pre_img, ImageLoadOptions.getOptions());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithId(getItem(position).proId);
            }
        });
        vh.collection_delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteInterface.onDelete(getItem(position),getCount());
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
        ImageView collection_pre_img;
        ImageView collection_delete_img;
        TextView collection_desc;
        TextView collection_price;
        TextView collection_time;
    }

    public interface OnDeleteItemListener{
        void onDelete(FavoriteManagerItem.InnerData.Favorite item,int size);
    }

    public void setOnDeleteItemListener(OnDeleteItemListener mDeleteInterface){
        this.mDeleteInterface = mDeleteInterface;
    }
}
