package cn.mstar.store.functionutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lidroid.xutils.BitmapUtils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.mstar.store.R;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.wxapi.Constants;
import cn.mstar.store.wxapi.Util;

/**
 * Created by Administrator on 2015/11/11.
 */
public class WeiXinShare {

    public static  void wechatShare(Context c,String link,int flag,Bitmap thumb) {

        IWXAPI wxApi;
        //实例化
        wxApi = WXAPIFactory.createWXAPI(c, Constants.APP_ID);
        wxApi.registerApp(Constants.APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = link == null ?"":link;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "范儿网络";
        msg.description = "范儿珠宝 奢而不贵 品质保证 工厂直销 向暴利说不 售后无忧  是您购买珠宝的最佳选择";
        if (thumb == null){
            thumb = BitmapFactory.decodeResource(c.getResources(), R.drawable.signin_logo);
        }
        Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 120, 120, true);
        msg.setThumbImage(thumbBmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }
}
