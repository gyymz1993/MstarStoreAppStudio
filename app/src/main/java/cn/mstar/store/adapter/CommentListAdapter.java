package cn.mstar.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.activity.ImageBrowserActivity;
import cn.mstar.store.entity.CommentListEntity;
import cn.mstar.store.utils.ImageLoadOptions;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class CommentListAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommentListEntity.CommentContent> mData;

    public CommentListAdapter(Context mContext, List<CommentListEntity.CommentContent> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CommentListEntity.CommentContent getItem(int position) {
        return mData.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment_list, parent, false);
            vh.comList_img = (ImageView) convertView.findViewById(R.id.comment_list_img);
            vh.comList_name = (TextView) convertView.findViewById(R.id.comment_list_name);
            vh.comList_comTime = (TextView) convertView.findViewById(R.id.comment_list_commenttime);
            vh.comList_desc = (TextView) convertView.findViewById(R.id.comment_list_desc);
            vh.comList_grid = (GridView) convertView.findViewById(R.id.comment_list_comimg);
            vh.comList_gg = (TextView) convertView.findViewById(R.id.comment_list_gg);
            vh.comList_xh = (TextView) convertView.findViewById(R.id.comment_list_xh);
            vh.comList_buyTime = (TextView) convertView.findViewById(R.id.comment_list_buytime);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(getItem(position).getUserpic(), vh.comList_img, ImageLoadOptions.getOptions());
        vh.comList_name.setText(getItem(position).getName());
        vh.comList_comTime.setText(getItem(position).getAddtime());
        vh.comList_desc.setText(getItem(position).getContent());
        vh.comList_gg.setText(getItem(position).getSpecialTitle());
        vh.comList_xh.setText(getItem(position).getProNo());
        vh.comList_buyTime.setText(getItem(position).getOrdertime());
        if (getItem(position).getPic() != null && getItem(position).getPic().size() > 0) {
            vh.comList_grid.setAdapter(new CommentImgAdapter(getItem(position).getPic()));
        } else {
            vh.comList_grid.setAdapter(null);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView comList_img;
        TextView comList_name;
        TextView comList_comTime;
        TextView comList_desc;
        GridView comList_grid;
        TextView comList_gg;
        TextView comList_xh;
        TextView comList_buyTime;
    }

    private class CommentImgAdapter extends BaseAdapter {

        List<String> pics;
        String[] picArr;

        public CommentImgAdapter(List<String> pics) {
            this.pics = pics;
            picArr = new String[pics.size()];
            int i = 0;
            for (String s : pics){
                picArr[i++] = s;
            }
        }

        @Override
        public int getCount() {
            return pics.size();
        }

        @Override
        public String getItem(int position) {
            return pics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.comment_list_pics, parent, false);
            ImageView mImageView = (ImageView) view.findViewById(R.id.comment_pics);
            ImageLoader.getInstance().displayImage(getItem(position), mImageView, ImageLoadOptions.getOptions());
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ImageBrowserActivity.class);
                    intent.putExtra("photos", picArr);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }
            });
            return mImageView;
        }
    }
}
