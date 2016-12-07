package cn.mstar.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.activity.ProductDetailsActivity;
import cn.mstar.store.app.MyAction;
import cn.mstar.store.customviews.ScaleImageView;
import cn.mstar.store.entity.ProInfo;
import cn.mstar.store.entity.Product;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.utils.Utils;


/**
 * Created by Administrator on 2015/9/23.
 */
public class StoreshopAdapter extends BaseAdapter{

    private int SCREENWIDTH = -1;
    private Context context;
    private List<ProInfo> productList;
    private LayoutInflater inflater;

    public StoreshopAdapter(Context context, List<ProInfo> productList, int sw){
        this.context=context;
        this.productList=productList;
        inflater=LayoutInflater.from(context);
        SCREENWIDTH = sw;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        final ProInfo product=productList.get(position);
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_product_list2, null);
            viewHolder.productIV=(ScaleImageView) convertView.findViewById(R.id.product_img);
            viewHolder.productName=(TextView) convertView.findViewById(R.id.product_name);
            viewHolder.productPrice=(TextView) convertView.findViewById(R.id.product_price_r);
            viewHolder.mprice=(TextView) convertView.findViewById(R.id.product_price_b);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.productIV.setImageBitmap(null);
        // we get the layout paramz, we anlayze it, and set up to the next view.
        // I CAN DETERMINE THE WIDTH OF EACH IMAGE BY CALCUL HERE.
        // screenwidth - margin / 2 ... et on enleve encore les paddings et le tour est jouer.
        int itemwidth = (SCREENWIDTH - Utils.convertDiptoPx(3, context))/2;
        ViewGroup.LayoutParams paramz = viewHolder.productIV.getLayoutParams();
        paramz.height = itemwidth;
        paramz.width = itemwidth;
        viewHolder.productIV.setLayoutParams(paramz);


        ImageLoader.getInstance().displayImage(product.getPic(), viewHolder.productIV, ImageLoadOptions.getOptions());
        viewHolder.productName.setText(/*"女装太便宜了"*/product.getName().trim());
        viewHolder.productPrice.setText(product.getPrice() + "");
        viewHolder.mprice.setText(product.getMkprice() + "");
        convertView.setTag(viewHolder);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("proId", Integer.parseInt(product.getProId()));
                intent.setAction(MyAction.productListActivityAction);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    public class ViewHolder{
        public ScaleImageView productIV;//产品图片
        public TextView productName;
        public TextView productPrice;//产品名称,价格
        public TextView mprice;

    }
}
