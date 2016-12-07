package cn.mstar.store.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.mstar.store.R;
import cn.mstar.store.activity.ProductDetailsActivity;
import cn.mstar.store.app.MyAction;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.BuyProductEntity;
import cn.mstar.store.entity.OrderDetailsEntity;
import cn.mstar.store.entity.OrderListItem;
import cn.mstar.store.functionutils.HttpUtils;
import cn.mstar.store.functionutils.LogUtils;
import cn.mstar.store.functionutils.LoginRequest;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by 1 on 2015/7/21.
 */
/**
 *
 *  @action: 所有订单的Itme
 *  @author:  YangShao
 *  @date: 2015/10/20 @time: 9:08
 */
public class GoodsManagement_InnerAdapter extends BaseAdapter {


    List<OrderListItem> data;
    Context mContext;
    LayoutInflater inf;
    private int SCREEN_WIDTH = -1;
    public enum ORDER_STATE { TRANSACTION_DONE_OK, WAITING_FOR_PAY, WAITING_FOR_RECEIVE, WAITING_FOR_RETRIEVE, WAITING_FOR_SENDING, CONFIRM_RECEIVE }


    public GoodsManagement_InnerAdapter (Context context, List<OrderListItem> OrderListItemList) {
        mContext = context;
        data = OrderListItemList;
        inf = LayoutInflater.from(mContext);
        SCREEN_WIDTH = Utils.getScreenWidth(mContext);
        L.d(mContext.getClass() + " ---- " + data.toString());
        keepViewMap = new HashMap<>();
    }

    public void add(OrderListItem cheese) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.add(cheese);
    }

    public void addAll(List<OrderListItem> order_items) {
        for (OrderListItem orderItem: order_items) {
            if (data == null) {
                data = new ArrayList<>();
            }
            data.add(orderItem);
            notifyDataSetChanged();
        }
    }

    public class OrderItemViewHolder {

        @Bind(R.id.tv_total_count) public TextView total_count_pres;
        @Bind(R.id.tv_confirm_receiving) public TextView tv_confirm_receiving;
        @Bind(R.id.tv_evaluate) public TextView tv_evaluate;
        @Bind(R.id.iv_del_order) public ImageView iv_delete;
        @Bind(R.id.tv_check_shipment) public TextView tv_check_shipment;
        @Bind(R.id.tv_pay_now) public TextView tv_bottom_action_pay;
        @Bind(R.id.tv_situation) public TextView tv_situation;
        @Bind(R.id.tv_total_amount) public TextView tv_total_amount;
        @Bind(R.id.inner_lny_container) public LinearLayout linear_inner_lny_containervh;
        public ORDER_STATE state;


        public  OrderItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }



    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        try {
            return data.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        OrderItemViewHolder vh;
        if (convertView == null) {
            view = inf.inflate(R.layout.myorderitem_with_innerlist, null);
            vh = new OrderItemViewHolder(view);
        } else {
            view = convertView;
            vh = (OrderItemViewHolder) view.getTag();
        }

        final OrderListItem itemz = (OrderListItem) getItem(position);
        int state = 1;
        if(itemz==null||itemz.proInfo==null){
            return convertView;
        }

        for (int i = 0; i < itemz.proInfo.length; i++) {
            if (itemz.proInfo[i].evaluation_state == 0) {
                state = 0;
            }
        }

        vh.state = convertStatus(Integer.valueOf(itemz.orderInfo.status));
        itemz.orderInfo.evaluation_state = state;
        shapeOutView(vh, itemz);

        vh.linear_inner_lny_containervh.removeAllViews();
        for (int i = 0; i < itemz.proInfo.length; i++) {
            if (i > 0 && i <= itemz.proInfo.length-1) {
                // inflate the bar
                View v = inflateBar();
                if (v != null) {
                    vh.linear_inner_lny_containervh.addView(v);
                    L.d("line:::", "added line i = "+i);
                }
            }
            View inflated = inflateItem(itemz.proInfo[i]);
            vh.linear_inner_lny_containervh.addView(inflated);
        }
        view.setTag(vh);
        return view;
    }

    private View inflateBar() {
        return inf.inflate(R.layout.line_separation, null);
    }

    // for things to be faster... ill just keep created views once and save them into
    // an array thing.
    Map<String, View> keepViewMap;
    private View inflateItem(final OrderListItem.ProInfo item) {
        View  view = inf.inflate(R.layout.item_confirm_indent_order_details, null);
        LinearItemViewHolder viewHolder = new  LinearItemViewHolder(view);
        OrderListItem.ProInfo entity = item;
        ImageLoader.getInstance().displayImage(entity.pic, viewHolder.productIV, ImageLoadOptions.getOptions());
        viewHolder.productName.setText(entity.name);
        viewHolder.productNorms.setText(entity.specialTitle);
        viewHolder.productNums.setText(""+entity.num);
        viewHolder.productPrice.setText(mContext.getString(R.string.renminbi) + entity.Price);
        viewHolder.go_to_comment.setVisibility (View.GONE);
        viewHolder.productIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithId(Integer.valueOf(item.proId));
            }
        });
        return view;
    }


    public   class LinearItemViewHolder{
        @Bind(R.id.product_img) public   ImageView productIV;//产品图像
        @Bind(R.id.product_name) public   TextView productName;
        @Bind(R.id.product_norms)  public   TextView productNorms;
        @Bind(R.id.product_price)  public   TextView productPrice;
        @Bind(R.id.product_number)  public   TextView productNums;
        //     @Bind(R.id.product_name)  public TextView TVnums;//名称 规格 价格  数量,增加减少显示框的数量
        //    @Bind(R.id.product_name)  public ImageView btnMinus,btnPlus;//减少 增加 显示
        @Bind(R.id.return_request_progress)  public   TextView tv_return_request_progress;
        @Bind(R.id.rel_bottom) public   View rel_bottom;
        @Bind(R.id.go_to_comment) public TextView go_to_comment;

        public LinearItemViewHolder(View view) {
            ButterKnife.bind(this, view);
            tv_return_request_progress.setVisibility(View.GONE);
            rel_bottom.setVisibility(View.GONE);
        }
    }


    private ORDER_STATE convertStatus(int status) {

       /* if (status == 0) {
            return ORDER_STATE.WAITING_FOR_PAY;
        } else*/ if (status == 10) {
            return ORDER_STATE.WAITING_FOR_PAY;
        } else if (status == 20) {
            return ORDER_STATE.WAITING_FOR_SENDING;
        } else if (status == 30) {
            return ORDER_STATE.WAITING_FOR_RECEIVE;
        } else if (status == 40){
            return ORDER_STATE.CONFIRM_RECEIVE;
        }
        return null;
    }

    protected void startActivityWithId(int proId) {

        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        intent.putExtra("proId", proId);
        intent.setAction(MyAction.productListActivityAction);
        mContext.startActivity(intent);
    }
   // BuyProductEntity entity=new BuyProductEntity();
    private void shapeOutView(OrderItemViewHolder vh, final OrderListItem orderItem) {
       final OrderListItem.OrderInfo d = orderItem.orderInfo;

        L.d("XXX UUU ", d.toString());
        //可以显示退款
        String orderState="";
//        if (HttpUtils.buydatas.size()!=0&&HttpUtils.buydatas!=null){
//            List<BuyProductEntity> buydata = HttpUtils.buydatas.get(d.orderId);
//            if (buydata!=null&&buydata.size()!=0){
//                for (BuyProductEntity entity:buydata){
//                    LogUtils.e(entity.toString());
//                    if(entity.refund_state!=0){
//                        if((vh.state == ORDER_STATE.CONFIRM_RECEIVE)){
//                            orderState="退货中";
//                        }else{
//                            orderState="退款中";
//                        }
//                    }
//                }
//            }
//        }
        if(d.refundState!=0){
            if((vh.state == ORDER_STATE.CONFIRM_RECEIVE)){
                orderState="退货中";
            }else{
                orderState="退款中";
            }
        }
        // show all
        vh.iv_delete.setVisibility(View.GONE);
        vh.tv_bottom_action_pay.setVisibility(View.GONE);
        vh.tv_check_shipment.setVisibility(View.GONE);
        vh.tv_confirm_receiving.setVisibility(View.GONE);

        // often needed
        vh.tv_situation.setVisibility(View.VISIBLE);

        if (vh.state == ORDER_STATE.WAITING_FOR_PAY) {

            vh.tv_situation.setText(R.string.waitingforpay);
            vh.tv_bottom_action_pay.setVisibility(View.VISIBLE);
        } else  if (vh.state == ORDER_STATE.WAITING_FOR_RECEIVE) {
            vh.tv_situation.setText(R.string.waitingforreceiving);
            vh.tv_check_shipment.setVisibility(View.VISIBLE);
        } else  if (vh.state == ORDER_STATE.WAITING_FOR_RETRIEVE) {
            vh.tv_situation.setText(R.string.waitingforretrieve);
        } else  if (vh.state == ORDER_STATE.WAITING_FOR_SENDING) {
            vh.tv_situation.setText(R.string.waitingforsending);
            vh.tv_confirm_receiving.setText("查看详情");
            vh.tv_confirm_receiving.setVisibility(View.VISIBLE);
            //代发货状态
            if (!orderState.equals("")){
                vh.tv_confirm_receiving.setText(orderState);
            }

        } else if (vh.state == ORDER_STATE.CONFIRM_RECEIVE) {
            vh.iv_delete.setVisibility(View.VISIBLE);
            vh.tv_situation.setText(R.string.transaction_done_ok);
            vh.tv_confirm_receiving.setVisibility(View.VISIBLE);

            // if all the items are just commented, then 已评价
            // else 未评价
            if (Integer.valueOf(d.evaluation_state) == 0) {
                // 未评价
                vh.tv_confirm_receiving.setText(mContext.getString(R.string.waitfor_evaluate));
                if (!orderState.equals("")){
                    vh.tv_confirm_receiving.setText(orderState);
                }
            } else   if (Integer.valueOf(d.evaluation_state) == 1) {
                // 已经评价
                vh.tv_confirm_receiving.setText(mContext.getString(R.string.evaluated));
                // make it clickable.
            }
        }

        vh.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HttpUtils.orderDetailsEntitys.size()!=0&&HttpUtils.orderDetailsEntitys!=null){
                    OrderDetailsEntity  orderDetailsEntity = HttpUtils.orderDetailsEntitys.get(d.orderId);
                    if (orderDetailsEntity!=null){
                        cancelOrder(orderItem,orderDetailsEntity);
                    }
                }
            }
        });
        vh.tv_total_amount.setText("总计：" + mContext.getString(R.string.yuan_char) + String.valueOf(d.totalPrice));
//        vh.tv_unit_count.setText(String.valueOf(d.count));
        vh.total_count_pres.setText("共 " + d.count + " 件商品");
    }

    /**
     *
     *  @action:  我的订单页面执行删除订单
     *  @author:  YangShao
     *  @date: 2015/10/20 @time: 9:41
     */
    private void cancelOrder(final OrderListItem orderInfo,final OrderDetailsEntity orderDetailsEntity) {
        Activity activity=(Activity)mContext;
        final MyApplication app = (MyApplication)activity.getApplication();
        String tokenKey = "";
        if (tokenKey == null)
            tokenKey = Utils.getTokenKey((MyApplication)activity.getApplication());
        final AlertDialog mAlertDialog = new AlertDialog.Builder(mContext).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.scan_history_dialog, null, false);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        dialog_title.setText("你确定要删除该订单吗？");
        view.findViewById(R.id.scan_history_confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cancelLink = NewLink.CANCEL_ORDER_LINK + "&orderNo=" + orderInfo.orderInfo.orderId + "&key=" + Utils.getTokenKey(app) + "&voucher_code="+ orderDetailsEntity.order.voucher_code;
                L.d("cancel:::", cancelLink);
                VolleyRequest.GetCookieRequest(mContext, cancelLink, new VolleyRequest.HttpStringRequsetCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        // 取消成功
                        try {
                            if (new JSONObject(result).optString("error").equals("0")) {
                              //  LogUtils.e("删除成功");
                                data.remove(orderInfo);
                                notifyDataSetChanged();
                            } else {
                                LogUtils.e("删除失败");
                            }
                            mAlertDialog.dismiss();
                        } catch (Exception e) {

                        }
                        L.d("cancel:::", "res " + result);
                    }
                    @Override
                    public void onFail(String error) {
                        mAlertDialog.dismiss();
                    }
                });
            }
        });
        view.findViewById(R.id.scan_history_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog.setView(view);
        mAlertDialog.show();
    }


    OnDeleteOrderItem onDeleteOrderItem;
    public void setOnDeleteOrderItem(OnDeleteOrderItem onDeleteOrderItem) {
        this.onDeleteOrderItem = onDeleteOrderItem;
    }

    public interface OnDeleteOrderItem{
        void onDelete(boolean issucess);
    }
    public static  class ItemListAdapter extends BaseAdapter {

        public List<OrderListItem.ProInfo> data;
        public ItemListAdapter(OrderListItem.ProInfo[] proInfo) {
            // the ctx nd the inf are common.
            data = new ArrayList<>();
            for (OrderListItem.ProInfo info: proInfo
                    ) {
                data.add(info);
            }
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            return null;
        }
    }
}