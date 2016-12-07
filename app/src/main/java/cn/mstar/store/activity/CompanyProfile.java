package cn.mstar.store.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import cn.mstar.store.R;
import cn.mstar.store.app.Constants2;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.interfaces.HttpRequestCallBack;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class CompanyProfile extends BaseActivity {
    private View loading, noResult, networkErr, reload;
    private TextView profile_title;
    private ImageView profile_back;
    private WebView profile_content;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_profile);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        link = Constants2.BASE_URL0 + "m=QxWeb&a=companyInfo&c=Page";
        initView();
        setLoading();
        loadWebView();
    }

    private void initView() {
        profile_title = (TextView) findViewById(R.id.title_name);
        profile_back = (ImageView) findViewById(R.id.title_back);
        profile_content = (WebView) findViewById(R.id.profile_content);
        profile_title.setText(getResources().getString(R.string.company_profile_title));
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
                loadWebView();
            }
        });

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


    private void
    hideAllView() {
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

}
