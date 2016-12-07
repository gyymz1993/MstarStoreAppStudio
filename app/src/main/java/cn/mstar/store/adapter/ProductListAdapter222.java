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
import cn.mstar.store.customviews.SquareImageView;
import cn.mstar.store.entity.Product;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.customviews.ScaleImageView;

/**产品列表适配器
 * @author wenjundu 2015-7-13
 *
 */
public class ProductListAdapter222 extends BaseAdapter {

	private int SCREENWIDTH = -1;
	private Context context;
	private List<Product> productList;
	private LayoutInflater inflater;

	private static ViewGroup.LayoutParams parentParams;

	public ProductListAdapter222(Context context, List<Product> productList, int sw){
		this.context=context;
		this.productList=productList;
		inflater=LayoutInflater.from(context);
		SCREENWIDTH = sw;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return productList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return productList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	ViewGroup.LayoutParams layoutParams;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		Product product=productList.get(position);
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_product_list2, null);
			viewHolder.productIV=(SquareImageView) convertView.findViewById(R.id.product_img);
			viewHolder.productName=(TextView) convertView.findViewById(R.id.product_name);
			viewHolder.price_r = (TextView) convertView.findViewById(R.id.product_price_r);
			viewHolder.price_b = (TextView) convertView.findViewById(R.id.product_price_b);
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


		ImageLoader.getInstance().displayImage(product.getImageUrl(), viewHolder.productIV, ImageLoadOptions.getOptions());
		viewHolder.productName.setText(/*"女装太便宜了"*/product.getName().trim());
		viewHolder.price_r.setText(product.getPrice() + "");
		viewHolder.price_b.setText(product.getMkprice() + "");
		convertView.setTag(viewHolder);
		return convertView;
	}



	public class ViewHolder{
		public SquareImageView productIV;//产品图片
		public TextView productName;
		public TextView price_r;
		public TextView price_b;
	}
}
