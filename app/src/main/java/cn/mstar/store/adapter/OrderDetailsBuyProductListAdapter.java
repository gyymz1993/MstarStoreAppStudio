package cn.mstar.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.activity.PostCommentActivity;
import cn.mstar.store.activity.ReturnGoodsProgressActivity;
import cn.mstar.store.entity.BuyProductEntity;
import cn.mstar.store.entity.Product;
import cn.mstar.store.utils.CustomToast;
import cn.mstar.store.utils.ImageLoadOptions;
/**
 *
 *  @action: 订单详情页面
 *  @author:  YangShao
 *  @date: 2015/10/19 @time: 17:10
 */
public class OrderDetailsBuyProductListAdapter extends BaseAdapter{

	private     int SCREEN_WIDTH = -1;
	private int orderStatus;
	private Context context;
	private List<BuyProductEntity> list;
	private LayoutInflater inflater;
	private OnPlusMinusListener listener;
	private String orderId = "";

	public OrderDetailsBuyProductListAdapter(Context context, String orderId, List<BuyProductEntity> list , int orderStatus, int sw){
		this.context=context;
		this.list=list;
		this.orderId = orderId;
		inflater=LayoutInflater.from(context);
		this.orderStatus = orderStatus;
		SCREEN_WIDTH = sw;
	}


	public void setOnPlusMinusListener(OnPlusMinusListener listener){
		this.listener=listener;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_confirm_indent_order_details, null);
			viewHolder.productIV=(ImageView) convertView.findViewById(R.id.product_img);
			viewHolder.productName=(TextView) convertView.findViewById(R.id.product_name);
			viewHolder.productNorms=(TextView) convertView.findViewById(R.id.product_norms);
			viewHolder.productPrice=(TextView) convertView.findViewById(R.id.product_price);
			viewHolder.productNums=(TextView) convertView.findViewById(R.id.product_number);
			viewHolder.rel_bottom = convertView.findViewById(R.id.rel_bottom);
			viewHolder.rel_bottom.setVisibility(View.GONE);
			viewHolder.btnMinus=(ImageView) convertView.findViewById(R.id.btn_commodity_minus);
			viewHolder.btnPlus=(ImageView) convertView.findViewById(R.id.btn_commodity_plus);
			viewHolder.TVnums=(TextView) convertView.findViewById(R.id.btn_commodity_number_display);
			viewHolder.tv_return_request_progress = (TextView) convertView.findViewById(R.id.return_request_progress);
			viewHolder.go_to_comment = (TextView) convertView.findViewById(R.id.go_to_comment);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		final BuyProductEntity entity=list.get(position);
//		L.e(entity.getProduct().getImageUrl());
		//	if(entity.getProduct().getImageUrl()!=null && !"".equals(entity.getProduct().getImageUrl()))
		ImageLoader.getInstance().displayImage(entity.getProduct().getImageUrl(), viewHolder.productIV, ImageLoadOptions.getOptions());

		viewHolder.productName.setText(entity.getProduct().getName());

		// the text should occupy maximum half of the screen.
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.productName.getLayoutParams();
		params.width = SCREEN_WIDTH / 2;
		viewHolder.productName.setLayoutParams(params);


		viewHolder.productNorms.setText(entity.getNorms());
		viewHolder.productNums.setText(""+entity.getBuyNum());
		viewHolder.TVnums.setText(""+entity.getBuyNum());
		viewHolder.productPrice.setText(context.getString(R.string.renminbi) + entity.getProduct().getPrice());
		viewHolder.btnMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(listener!=null)
					listener.minus(position);


			}
		});
		viewHolder.btnPlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(listener!=null)
					listener.plus(position);

			}
		});

		// 如果产品在退款中、显示退款中按钮、并设置监听
		if (entity.refund_state == 0) {
			viewHolder.tv_return_request_progress.setVisibility(View.GONE);
		} else {
			// 设置监listener让他直接调到退款进度界面去
			viewHolder.tv_return_request_progress.setText("退货中");
			if (Integer.valueOf(orderStatus) == 20){
				// 设置监listener让他直接调到退款进度界面去
				viewHolder.tv_return_request_progress.setText("退款中");
			}
			viewHolder.tv_return_request_progress.setVisibility(View.VISIBLE);
			viewHolder.go_to_comment.setEnabled(false);
			//不能评价
			viewHolder.go_to_comment.setVisibility(View.GONE);
			viewHolder.tv_return_request_progress.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CustomToast.makeToast(context, "查看进度", Toast.LENGTH_SHORT);
					// with the orderId and the item Id.
					jumpToReturnProductProgressActivity(entity.getProduct());
				}
			});
		}
		// 如果已收货
		if (Integer.valueOf(orderStatus) == 40) {
			viewHolder.go_to_comment.setVisibility(View.VISIBLE);
			if (entity.evaluation_state == 0) {
				//可以显示退货按钮
				// 未评价
				viewHolder.go_to_comment.setText("未评价");
				//viewHolder.tv_return_request_progress.setVisibility(View.VISIBLE);
				viewHolder.go_to_comment.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						getToCommentProductActivity(entity);
					}
				});

			} else {
				// 已评价
				viewHolder.tv_return_request_progress.setVisibility(View.GONE);
				viewHolder.go_to_comment.setText("已评价");
				viewHolder.go_to_comment.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CustomToast.makeToast(context, context.getString(R.string.alread_commented), Toast.LENGTH_SHORT);
					}
				});
			}
			if(entity.refund_state == 0){
			}else{
				viewHolder.go_to_comment.setVisibility(View.GONE);
			}
		} else {
			viewHolder.go_to_comment.setVisibility(View.GONE);
		}
		return convertView;
	}

	/**
	 *
	 *  @action:跳转评价管理
	 *  @author:  YangShao
	 *  @date: 2015/10/20 @time: 8:27
	 */
	private void getToCommentProductActivity(BuyProductEntity entity) {

		// create another activity to show comment the current product.
		Intent intent = new Intent(context, PostCommentActivity.class);
		intent.putExtra("data", entity);
		intent.putExtra("orderid", orderId);
		context.startActivity(intent);
	}

	/**
	 *
	 *  @action:  跳转物流页面
	 *  @author:  YangShao
	 *  @date: 2015/10/20 @time: 8:26
	 */
	private void jumpToReturnProductProgressActivity(Product product) {

		Intent intent = new Intent(context, ReturnGoodsProgressActivity.class);
		intent.putExtra(ReturnGoodsProgressActivity.ORDERID, orderId);
		intent.putExtra(ReturnGoodsProgressActivity.PROID, product.getProId());
		context.startActivity(intent);
	}

	public class ViewHolder{
		public ImageView  productIV;//产品图像
		public TextView productName,productNorms,productPrice,productNums,TVnums;//名称 规格 价格  数量,增加减少显示框的数量
		public View rel_bottom;
		public ImageView btnMinus,btnPlus;//减少 增加 显示
		public TextView tv_return_request_progress, go_to_comment;
	}

	public interface OnPlusMinusListener{
		public void plus(int position);
		public void minus(int position);
	}
}
