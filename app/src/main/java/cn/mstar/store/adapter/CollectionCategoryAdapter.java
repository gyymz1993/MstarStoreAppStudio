package cn.mstar.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.entity.FavoriteManagerItem;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class CollectionCategoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<FavoriteManagerItem.InnerData> mData;
    private List<FavoriteManagerItem.InnerData.Favorite> favorites;
    private OnCategoryItemClickListener cateInterface;

    public CollectionCategoryAdapter(Context mContext, List<FavoriteManagerItem.InnerData> mData) {
        this.mContext = mContext;
        this.mData = mData;
        init();
    }

    private void init() {
        favorites = new ArrayList<FavoriteManagerItem.InnerData.Favorite>();
        for (FavoriteManagerItem.InnerData data : mData) {
            for (FavoriteManagerItem.InnerData.Favorite favorite : data.favorites) {
                favorites.add(favorite);
            }
        }
    }

    public void changeData(FavoriteManagerItem.InnerData.Favorite favorite){
        for (int i = 0; i < mData.size(); i++){
            /*for (int j = 0; j < mData.get(i).favorites.size(); j++){

            }*/
            if (mData.get(i).favorites.contains(favorite)){
                if (mData.get(i).favorites.size() == 1){
                    mData.remove(i);
                }else if (mData.get(i).favorites.size() > 1){
                    mData.get(i).favorites.remove(favorite);
                }
            }
        }
        init();
        notifyDataSetChanged();
    }

    public List<FavoriteManagerItem.InnerData.Favorite> getFavorites() {
        return favorites;
    }

    @Override
    public int getCount() {
        return mData.size() + 1;
    }

    @Override
    public List<FavoriteManagerItem.InnerData.Favorite> getItem(int position) {
        if (position == 0) {
            return favorites;
        } else {
            return mData.get(position - 1).favorites;
        }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_collection_wcl, parent, false);
            vh.cate_name = (TextView) convertView.findViewById(R.id.collection_item_name);
            vh.cate_num = (TextView) convertView.findViewById(R.id.collection_item_num);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final String categoryName;
        if (position == 0) {
            categoryName = mContext.getResources().getString(R.string.collection_all_category);
        } else {
            categoryName = mData.get(position - 1).categoryName;
        }
        vh.cate_name.setText(categoryName);
        vh.cate_num.setText(getItem(position).size() + "");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateInterface.onCategoryItemClick(categoryName, getItem(position).size(), getItem(position));
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView cate_name;
        TextView cate_num;
    }

    public interface OnCategoryItemClickListener {
        void onCategoryItemClick(String categoryName, int categoryNum, List<FavoriteManagerItem.InnerData.Favorite> data);
    }

    public void setOnCategoryItemClickListener(OnCategoryItemClickListener cateInterface) {
        this.cateInterface = cateInterface;
    }
}
