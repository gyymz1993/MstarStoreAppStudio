package cn.mstar.store.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.mstar.store.R;
import cn.mstar.store.app.Constants2;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.functionutils.LogUtils;
import cn.mstar.store.functionutils.WeiXinShare;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;
import cn.mstar.store.wxapi.Constants;

/**
 * Created by Administrator on 2015/10/28.
 */
public class ShareQcodeActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.id_lay_sharweixin)
    LinearLayout idLaySharweixin;
    @Bind(R.id.id_lay_sharweixinfriend)
    LinearLayout idLaySharweixinfriend;
    private View loading, noResult, networkErr, reload;
    private TextView profile_title;
    private ImageView profile_back;
    private WebView profile_content;
    private String link;
    private String url;
    private String html;
    private int flag;
    private MyApplication app;
    @Bind(R.id.shop_search_btn)
    TextView share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareqcode);
        ButterKnife.bind(this);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        app = (MyApplication)getApplication();
        flag = getIntent().getIntExtra("code", -1);
        shareQcode();
        idLaySharweixin.setOnClickListener(this);
        idLaySharweixinfriend.setOnClickListener(this);
    }


    /**
     * @action:推广链接
     * @author: YangShao
     * @date: 2015/10/28 @time: 11:57
     */
    public void shareQcode() {
        if (flag == 1) {
            //推广链接

            link = Constants2.BASE_URL0 + "m=QxWeb&a=extendLinkForApp&c=Drp&isapp=1" + "&key=" + Utils.getTokenKey(app);
            initView();
            setLoading();
            loadWebView();
        } else {
            url = NewLink.SHARE_URL + "&tokenKey=" + Utils.getTokenKey(app);

            VolleyRequest.GetCookieRequest(ShareQcodeActivity.this, url, new VolleyRequest.HttpStringRequsetCallBack() {
                @Override
                public void onSuccess(String result) {
                    try {
                        link = new Gson().fromJson(result, JsonObject.class).getAsJsonObject("data").get("ewCode").getAsString();
                        initView();
                        setLoading();
                        loadWebView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(String error) {

                }
            });
        }
    }


    private void wechatShare(int flag) {
        IWXAPI wxApi;
        //实例化
        wxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        wxApi.registerApp(Constants.APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = link;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "范儿网络";
        msg.description = "范儿珠宝 奢而不贵 品质保证 工厂直销 向暴利说不 售后无忧  是您购买珠宝的最佳选择";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.signin_logo);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);


    }

    private void initView() {
        profile_title = (TextView) findViewById(R.id.title_name);
        profile_back = (ImageView) findViewById(R.id.title_back);
        profile_content = (WebView) findViewById(R.id.profile_content);
        profile_title.setText(getResources().getString(R.string.share_qcode_title));
        profile_back.setVisibility(View.VISIBLE);
        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loading = findViewById(R.id.lny_loading_layout);
        noResult = findViewById(R.id.lny_no_result);
        networkErr = findViewById(R.id.lny_network_error_view);
        reload = networkErr.findViewById(R.id.wifi_retry);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading();
//                inflateDatas();
                loadWebView();
            }
        });

        share.setText("分享");
        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(this);

        WebSettings settings = profile_content.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
//        settings.setTextSize(WebSettings.TextSize.LARGEST);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        settings.setAppCacheEnabled(true);
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        profile_content.setScrollBarStyle(profile_content.SCROLLBARS_OUTSIDE_OVERLAY);
        settings.setJavaScriptEnabled(true);

    }

    private void loadWebView() {
        profile_content.loadUrl(link);
        setResult();
    }

    private void hideAllView() {
        loading.setVisibility(View.GONE);
        noResult.setVisibility(View.GONE);
        networkErr.setVisibility(View.GONE);
        profile_content.setVisibility(View.GONE);
    }

    private void setResult() {
        hideAllView();
        profile_content.setVisibility(View.VISIBLE);
    }

    private void setLoading() {
        hideAllView();
        loading.setVisibility(View.VISIBLE);
    }

    private void setNoResult() {
        hideAllView();
        noResult.setVisibility(View.VISIBLE);
    }

    private void setNetworkErr() {
        hideAllView();
        networkErr.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.requestQueue.cancelAll(this);

    }

    private PopupWindow popView;
    WindowManager.LayoutParams lp;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_lay_sharweixin:
                WeiXinShare.wechatShare(this, link, 0,null);//分享到微信好友
                if (popView != null)
                    popView.dismiss();
                break;
            case R.id.id_lay_sharweixinfriend:
                WeiXinShare.wechatShare(this, link, 1,null);//分享到微信好友
                if (popView != null)
                    popView.dismiss();
                break;
            case R.id.shop_search_btn:
                View convertView = getLayoutInflater().inflate(R.layout.share_layout, null, false);
                convertView.findViewById(R.id.id_lay_sharweixin).setOnClickListener(this);
                convertView.findViewById(R.id.id_lay_sharweixinfriend).setOnClickListener(this);
                popView = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popView.setTouchable(true);
                lp  = getWindow().getAttributes();
                lp.alpha = 0.3f;
                getWindow().setAttributes(lp);
                popView.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        lp.alpha = 1f;
                        getWindow().setAttributes(lp);
                        return false;
                    }
                });
                popView.setAnimationStyle(R.style.share_anim_style);
                popView.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                popView.showAtLocation(profile_content, Gravity.BOTTOM, 0, 0);
                break;
        }
    }
}