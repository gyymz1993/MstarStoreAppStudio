package cn.mstar.store.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.activity.ProductDetailsActivity;
import cn.mstar.store.adapter.DetailsListAdapter;
import cn.mstar.store.customviews.CustomWebview;

public class DetailPhotoDetailFragment extends Fragment {
    private View view;
    public static Fragment getInstance(String descURL) {
        Fragment fragment = new DetailPhotoDetailFragment();
        Bundle args = new Bundle();
        args.putString("desc", descURL);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_details_layout, container, false);
        view = View.inflate(getActivity(), R.layout.fragment_webview_layout, null);
        String descUrl = getArguments().getString("desc");
//            L.d("desc:::", descUrl);
        CustomWebview webView = (CustomWebview) view.findViewById(R.id.webview);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
//            webView.getSettings().setAppCachePath("/data/data/com.your.package.appname/cache"‌​);
        StorageUtils.getOwnCacheDirectory(getActivity(),
                "MstarStore/Cache").getAbsolutePath();
        webView.getSettings().setAppCacheEnabled(true);

        // remove a weird white line on the right size
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        String mime = "text/html";
        String encoding = "utf-8";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, descUrl, mime, encoding, null);
        return view;
    }
}
