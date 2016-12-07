package cn.mstar.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.customviews.ScaleImageView;
import cn.mstar.store.entity.GoodsClassify;
import cn.mstar.store.entity.ProInfo;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.utils.Utils;


/**
 * Created by Administrator on 2015/9/23.
 */
public class ShopClassifyAdapter extends BaseAdapter{

    private Context context;
    private List<GoodsClassify> shopclassify;
    private LayoutInflater inflater;

    public ShopClassifyAdapter(Context context, List<GoodsClassify> shopclassify){
        this.context=context;
        this.shopclassify=shopclassify;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return shopclassify.size();
    }

    @Override
    public Object getItem(int position) {
        return shopclassify.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        String gcName=shopclassify.get(position).getGcName();
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_shopclassify_list, null);
            viewHolder.shopclassify_text=(TextView) convertView.findViewById(R.id.shopclassify_text);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.shopclassify_text.setText(/*"女装太便宜了"*/shopclassify.get(position).getGcName().trim());
        convertView.setTag(viewHolder);
        return convertView;
    }
    public class ViewHolder{
        public TextView shopclassify_text;

    }
}
