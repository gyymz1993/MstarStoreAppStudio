package cn.mstar.store.functionutils;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.BuyProductEntity;
import cn.mstar.store.entity.OrderDetailsEntity;
import cn.mstar.store.entity.OrderListItem;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/*
 *
 *  @action:  主要提取一些公共网络数据方法
 *  @author:  YangShao
 *  @date: 2015/10/21 @time: 8:33
 */
public class HttpUtils {

    /**
     *
     *  @action: 获取物流状态  判断是否评价是否处于退货状态
     *  @author:  YangShao
     *  @date: 2015/10/21 @time: 8:34
     */
    public static String orderId;
    public static List<BuyProductEntity> buydata=new ArrayList<>();
    public static Map<String,List<BuyProductEntity>> buydatas=new HashMap<>();
    public static Map<String,OrderDetailsEntity> orderDetailsEntitys=new HashMap<>();
    public static void getWuLiuState(Context mContext,final OrderListItem.OrderInfo d){
        Activity ac = (Activity) mContext;
        final String link = NewLink.GOPAY_FOR_ORDER + "&key=" + Utils.getTokenKey((MyApplication) ac.getApplication()) + "&OrderNum=" + d.orderId;
       // L.d("link:::", link);
        VolleyRequest.GetCookieRequest(mContext, link, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                if (!"".equals(result)) {
                    try {
                        Gson gson = new Gson();
                        JsonElement elm = gson.fromJson(result, JsonElement.class).getAsJsonObject().get("data");
                        OrderDetailsEntity orderDetailsEntity = gson.fromJson(elm, OrderDetailsEntity.class);
                        orderId = orderDetailsEntity.order.orderId;
                        buydata = Utils.orderItemzToProductEntity(orderDetailsEntity.orderItems);
                        buydatas.put(orderId, buydata);
                        orderDetailsEntitys.put(orderId,orderDetailsEntity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    public void cancelAll(){
        MyApplication.requestQueue.cancelAll(this);
    }
}
