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
import cn.mstar.store.entity.Product;
import cn.mstar.store.utils.ImageLoadOptions;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class ProductListAdapterWcl extends BaseAdapter{

    private Context mContext;
    private List<Product> mData;
    public ProductListAdapterWcl(Context mContext,List<Product> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Product getItem(int position) {
        return mData.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.product_layout_wcl,parent,false);
            vh.img = (ImageView) convertView.findViewById(R.id.product_layout_img);
            vh.desc = (TextView) convertView.findViewById(R.id.product_layout_desc);
            vh.price_r = (TextView) convertView.findViewById(R.id.product_layout_price_r);
            vh.price_b = (TextView) convertView.findViewById(R.id.product_layout_price_b);
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.desc.setText(getItem(position).getName());
        vh.price_b.setText(getItem(position).getMkprice() + "");
        vh.price_r.setText(getItem(position).getPrice() + "");
        ImageLoader.getInstance().displayImage(getItem(position).getImageUrl(), vh.img, ImageLoadOptions.getOptions());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra("proId", getItem(position).getProId());
                intent.setAction(MyAction.productListActivityAction);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView img;
        TextView desc;
        TextView price_r;
        TextView price_b;
    }
}
