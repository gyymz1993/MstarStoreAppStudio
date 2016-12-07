package cn.mstar.store.functionutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mstar.store.R;
import cn.mstar.store.activity.BaseActivity;
import cn.mstar.store.activity.MainActivity;
import cn.mstar.store.activity.MockActivity;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.entity.BuyProductEntity;
import cn.mstar.store.entity.OrderDetailsEntity;
import cn.mstar.store.entity.OrderListItem;
import cn.mstar.store.utils.Constants;
import cn.mstar.store.utils.CustomToast;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by Administrator on 2015/10/17.
 */
public class LoginRequest {

    public static  void startLogin(Activity activity, String username,  String password) {

        final String usernames=username;
        final String passwords=password;
        final Activity activitys=activity;

        LogUtils.e("自动登陆"+NewLink.LOGIN_ACT + "&username=" + username + "&password=" + password + "&client=android");
        // 进行登录请求
        VolleyRequest.GetCookieRequest(activity, NewLink.LOGIN_ACT + "&username=" + username + "&password=" + password + "&client=android", new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.d("XXX", result);
                try {
                    Gson gson = new Gson();
                    JsonObject elm = gson.fromJson(result, JsonElement.class).getAsJsonObject().get("data").getAsJsonObject();
                    final String un = elm.get("userName").getAsString();
                    final String pw = elm.get("password").getAsString();
                    final String tokenKey = elm.get("tokenkey").getAsString();
                    final  String pic = elm.get("pic").getAsString();
                    final int points = elm.get("points").getAsInt();
					L.d("XXX", "username --- "+un+" and "+ un+"--- comp "+un.trim().equals(un));
					L.d("XXX", "password --- "+pw+" and "+ pw+"--- comp "+pw.trim().equals(pw));
                    //String error = elm.get("error").getAsString();
                    if (passwords.trim().equals(pw)) {
                        //登录成功
                        loginSuccess(activitys,usernames, un, pw, tokenKey, pic, points);
                    } else {
                        LogUtils.e("登陆错误"+elm.get(""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFail(String error) {
                // 网络异常

            }
        });

    }


    //	loginSuccess(username, password, tokenKey, pic, points);
    public static void loginSuccess(Activity activity,String loginName, String username, String password, String tokenKey, String pic, int points) {
        // get the action of the login... if it is for exchange, then send back the 94 result.
        Utils.LoginSuccess((MyApplication) activity.getApplication(), loginName, username, password, tokenKey, pic, points);

    }




//    // logOut
//    public void logOut (View view) {
//        // clean the rep
//        Utils.cleanSharedPref(SelfInformationActivity.this);
//        BaseActivity.spUtils.cleanSharedPref();
//        if (MainActivity.mainActivity!=null){
//            MainActivity.mainActivity.cartCount=0;
//            MainActivity.mainActivity.setMycartCount();//cartCount
//        }
//
//        // log out.
//        VolleyRequest.Logout((MyApplication) getApplication(), new VolleyRequest.HttpStringRequsetCallBack() {
//            @Override
//            public void onSuccess(String result) {
//
//            }
//
//            @Override
//            public void onFail(String error) {
//
//            }
//        });
//        // finish ()
//        // hide the dialog.
//        Intent data = new Intent ();
//        data.putExtra(Constants.UNLOGGED, true);
//        setResult(3, data);
//    }

}
