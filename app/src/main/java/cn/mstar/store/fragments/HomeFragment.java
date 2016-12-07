package cn.mstar.store.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.mstar.store.R;
import cn.mstar.store.activity.ClassifyActivity;
import cn.mstar.store.activity.CompanyProfile;
import cn.mstar.store.activity.LoginActivity;
import cn.mstar.store.activity.LogisticsActivity;
import cn.mstar.store.activity.MainActivity;
import cn.mstar.store.activity.MyCollectionActivity;
import cn.mstar.store.activity.MyFavoriteManagmentActivity;
import cn.mstar.store.activity.NearStoreActivity;
import cn.mstar.store.activity.ProductDetailsActivity;
import cn.mstar.store.activity.ProductListActivity;
import cn.mstar.store.activity.ScanCodeActivity;
import cn.mstar.store.activity.ScanHistoryActivity;
import cn.mstar.store.activity.SearchActivity;
import cn.mstar.store.app.Constants2;
import cn.mstar.store.app.MyAction;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.JazzyViewPager;
import cn.mstar.store.customviews.OutlineContainer;
import cn.mstar.store.entity.BannerItemData;
import cn.mstar.store.interfaces.HttpRequestCallBack;
import cn.mstar.store.parse.HomeJsonParse;
import cn.mstar.store.parse.HomeJsonParse.ParseCallBack;
import cn.mstar.store.utils.CustomToast;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.NetWorkUtil;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

public class HomeFragment extends Fragment implements OnClickListener {

    private Context mContext;
    private WebView webView;
    private JazzyViewPager mViewPager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyAdapter BannerAdapter = new MyAdapter();
    private Handler mHandler;
    private static final int MSG_CHANGE_PHOTO = 1;
    private boolean isFirstSetAdapter = true;
    @Bind(R.id.webview_progressbar)
    ProgressBar webview_progressbar;
    /**
     * 图片自动切换时间
     */
    private static final int PHOTO_CHANGE_TIME = 3000;
    private LinearLayout mIndicator;
    /**
     * 装指引的ImageView数组
     */
    private ImageView[] mIndicators;
    /**
     * 装ViewPager中ImageView的数组
     */
    private ImageView[] mImageViews;
    private List<String> mImageUrls = new ArrayList<String>();
    private ArrayList<BannerItemData> homeBannerList = new ArrayList<BannerItemData>();
    //搜索按钮
    private TextView searchBtn;
    //定位 扫一扫按钮
    private ImageButton locationBtn, camerBtn;
    View rootview = null;
    protected LinearLayout lny_network_error_layout;
    MyApplication app;
    private View favorite, logistics, history, profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        rootview = inflater.inflate(
                R.layout.fragment_home1, null);
        ButterKnife.bind(this, rootview);
        mContext = rootview.getContext();
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initView();
        webView.setWebViewClient(new MyWebviewClient());
        initData();
    }


    // 通过网络加载数据
    @OnClick(R.id.wifi_retry)
    public void initData() {
        hide_all(rootview);
        webview_progressbar.setVisibility(View.VISIBLE);
        // TODO Auto-generated method stub
        if (NetWorkUtil.isNetworkConnected(mContext)) {
            VolleyRequest.GetRequest(mContext, Constants2.IMAGE_URL,
                    new HttpRequestCallBack() {

                        @Override
                        public void onSuccess(JSONObject jsonObj) {
                            // TODO Auto-generated method stub
                            setBannerValueToView(jsonObj);
                            L.d(" --- " + jsonObj);
                        }

                        @Override
                        public void onFailure(String failresult) {
                            // TODO Auto-generated method stub
                            networkExceptionError();
                        }
                    });
        } else {
            networkExceptionError();
        }
    }

    // 解析JSON数据，Banner
    private void setBannerValueToView(JSONObject myJSONObj) {
        HomeJsonParse.parseJson(myJSONObj, new ParseCallBack() {

            @Override
            public void onSuccess(ArrayList<BannerItemData> list, String url) {
                MyApplication.getInstance();
                // TODO Auto-generated method stub
                mImageUrls.clear();
                for (int i = 0; i < list.size(); i++) {
                    mImageUrls.add(list.get(i).getImageUrl());
                }
                setBannerAdapter(list);
                if (mImageUrls != null && mImageUrls.size() > 0) {
                    reflushView();
                }
                setWebViewValue(url);
            }


            @Override
            public void onFailure(String failresult) {

            }
        });
    }

    // 刷新页面
    private void reflushView() {
        mIndicators = new ImageView[mImageUrls.size()];
        mIndicator.removeAllViews();
        if (mImageUrls.size() <= 1) {
            mIndicator.setVisibility(View.GONE);
        } else {
            mIndicator.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < mIndicators.length; i++) {
            ImageView imageView = new ImageView(mContext);
            LayoutParams params = new LayoutParams(0,
                    LayoutParams.WRAP_CONTENT, 1.0f);
            if (i != 0) {
                params.leftMargin = 5;
            }
            imageView.setLayoutParams(params);
            mIndicators[i] = imageView;
            if (i == 0) {
                mIndicators[i]
                        .setBackgroundResource(R.drawable.homepage_dot_gold);
            } else {
                mIndicators[i].setBackgroundResource(R.drawable.homepage_dot_grey);
            }

            mIndicator.addView(imageView);
        }

        mImageViews = new ImageView[mImageUrls.size()];

        for (int i = 0; i < mImageViews.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ScaleType.FIT_XY);
            mImageViews[i] = imageView;
        }
        if (isFirstSetAdapter) {
            mViewPager.setAdapter(BannerAdapter);
            isFirstSetAdapter = false;
        } else {
            BannerAdapter.notifyDataSetChanged();
        }
        mViewPager.setCurrentItem(0);

    }

    // 把数据适配到Banner
    private void setBannerAdapter(final ArrayList<BannerItemData> myBannerList) {
        if (mContext == null)
            return;
        if (myBannerList != null) {

            // ======= 初始化ViewPager ========
            mIndicators = new ImageView[myBannerList.size()];
            if (myBannerList.size() <= 1) {
                mIndicator.setVisibility(View.GONE);
            }

            for (int i = 0; i < mIndicators.length; i++) {
                ImageView imageView = new ImageView(mContext);
                LayoutParams params = new LayoutParams(0,
                        LayoutParams.WRAP_CONTENT, 1.0f);
                if (i != 0) {
                    params.leftMargin = 10;
                }
                imageView.setLayoutParams(params);
                mIndicators[i] = imageView;
                if (i == 0) {
                    mIndicators[i]
                            .setBackgroundResource(R.drawable.homepage_dot_gold);
                } else {
                    mIndicators[i]
                            .setBackgroundResource(R.drawable.homepage_dot_grey);
                }
                mIndicator.addView(imageView);
            }

            mImageViews = new ImageView[myBannerList.size()];

            for (int i = 0; i < mImageViews.length; i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ScaleType.FIT_XY);
                mImageViews[i] = imageView;
            }
            mViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);
            mViewPager.setCurrentItem(0);
            homeBannerList = myBannerList;
            mHandler.removeMessages(MSG_CHANGE_PHOTO);
            mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
                    PHOTO_CHANGE_TIME);

            mViewPager.setAdapter(BannerAdapter);
            mViewPager.setOnPageChangeListener(new MyPageChangeListener());
            mViewPager.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        swipeRefreshLayout.setEnabled(false);
                    }
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        swipeRefreshLayout.setEnabled(false);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        swipeRefreshLayout.setEnabled(true);
                    }
                    if (myBannerList.size() == 0 || myBannerList.size() == 1)
                        return true;
                    else
                        return false;

                }
            });

            BannerAdapter.bindData(myBannerList);
            BannerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author Administrator
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            setImageBackground(position);

        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItemsIndex
     */
    private void setImageBackground(int selectItemsIndex) {
        for (int i = 0; i < mIndicators.length; i++) {
            if (i == selectItemsIndex) {
                mIndicators[i]
                        .setBackgroundResource(R.drawable.homepage_dot_gold);
            } else {
                mIndicators[i].setBackgroundResource(R.drawable.homepage_dot_grey);
            }
        }
    }

    // 提供给banner的viewpager适配器
    public class MyAdapter extends PagerAdapter {

        ArrayList<BannerItemData> myBannerList = new ArrayList<BannerItemData>();

        @Override
        public int getCount() {
            return myBannerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mViewPager
                    .findViewFromObject(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ImageLoader.getInstance().displayImage(
                    myBannerList.get(position).getImageUrl(),
                    mImageViews[position], ImageLoadOptions.getOptions());
            ((ViewPager) container).addView(mImageViews[position], 0);
            mViewPager.setObjectForPosition(mImageViews[position], position);

            mImageViews[position].setOnClickListener(viewPagerOnClick);
            mImageViews[position].setTag(position);
            return mImageViews[position];

        }

        public void bindData(ArrayList<BannerItemData> myBannerList) {
            this.myBannerList = myBannerList;
        }

        OnClickListener viewPagerOnClick = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.e("GZB", "viewPagerOnClickm,getTag:" + v.getTag());

                // 0:没有任何动作；1:加载url网页2：进入分类列表3：进入搜索关键字列表4：进入详情页
                int vID = (Integer) v.getTag();
                /*switch (myBannerList.get(vID).getActionKey()) {
                    case 0:
                        break;
                    case 1:
                        gotoShowWebView(myBannerList.get(vID).getUrl());
                        break;
                    case 2:
                        //gotomyProductList(myBannerList.get(vID).getCategoryId());
                        break;
                    case 3:
                        gotomyProductListByKeyword(myBannerList.get(vID)
                                .getKeyword());
                        break;
                    case 4:
                        gotomyProductDetail(myBannerList.get(vID).getProId());
                        break;
                }*/
                Intent intent = new Intent(getActivity(), ProductListActivity.class);
                intent.setAction(MyAction.searchActivitryAction);
                intent.putExtra("key", myBannerList.get(vID).getKeyword());
                startActivity(intent);
                /*Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("keyword",myBannerList.get(vID).getKeyword());
                intent.setAction(MyAction.productListActivityAction);
                startActivity(intent);*/
            }
        };

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {

        webView = (WebView) getView().findViewById(R.id.home_webview);
        mViewPager = (JazzyViewPager) getView().findViewById(
                R.id.index_product_images_container);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(
                R.id.swipeRefreshLayout);
        mIndicator = (LinearLayout) getView().findViewById(
                R.id.index_product_images_indicator);

        searchBtn = (TextView) getView().findViewById(R.id.index_search_button);
        locationBtn = (ImageButton) getView().findViewById(R.id.index_location_button);
        camerBtn = (ImageButton) getView().findViewById(R.id.index_camer_button);

        searchBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        camerBtn.setOnClickListener(this);
        favorite = getView().findViewById(R.id.home_collection);
        logistics = getView().findViewById(R.id.home_logistics_information);
        history = getView().findViewById(R.id.home_history);
        profile = getView().findViewById(R.id.home_company_profile);
        favorite.setOnClickListener(this);
        logistics.setOnClickListener(this);
        history.setOnClickListener(this);
        profile.setOnClickListener(this);
        // 允许调用JS
        webView.getSettings().setJavaScriptEnabled(true);
        // 添加接口，提供本地方法给网页调用
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void gotoProductClass(int categoryId) {
                L.e("...........productId.......");
                gotomyProductClass(categoryId);
            }

            public void gotoProductItem(int categoryId) {

            }
        }, "mstar");
        // 设置WebViewClient，使之可以自动展开所有内容
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        // 设置下拉刷新监听
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        // 设置卷内的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.color_gold_nor,
                R.color.color_gold_nor, R.color.color_gold_nor,
                R.color.color_gold_nor);

        // 使用Handler，使viewpage自动播放
        mHandler = new Handler(mContext.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_CHANGE_PHOTO:
                        int index = mViewPager.getCurrentItem();
                        if (index == homeBannerList.size() - 1) {
                            index = -1;
                        }
                        mViewPager.setCurrentItem(index + 1);
                        mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
                                PHOTO_CHANGE_TIME);
                }
            }

        };
    }

    // 下拉刷新监听器
    OnRefreshListener onRefreshListener = new OnRefreshListener() {

        @Override
        public void onRefresh() {
            // TODO Auto-generated method stub
            // .......操作
            initData();
            // 停止刷新动画
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    public void setWebViewValue(String url) {
        webView.loadUrl(url);
    }

    public void gotoShowWebView(String url) {
        // Intent intent = new Intent(mContext,
        // ShowActivityDetailsActivity.class);
        // intent.putExtra("webUrl", url);
        // startActivity(intent);
    }

    public void gotomyProductClass(int categoryId) {

//		L.d("cat::: 1064", ""+categoryId);
//		if (categoryId != 1064) {
        Intent intent = new Intent(mContext, ClassifyActivity.class);
        intent.putExtra("categoryId", categoryId + "");
        intent.setAction(MyAction.mainActivityAction);
        startActivity(intent);
        /*} else {
            ((MainActivity) mContext).selectItem(MainActivity.TAB_CLASSIFICATION);
		}*/
    }

    public void gotomyProductListByKeyword(String keyword) {
        // Intent intent = new Intent(mContext, SearchActivity.class);
        // intent.putExtra("classify", keyword);
        // startActivity(intent);
    }

    public void gotomyProductDetail(int proId) {
        // Intent intent = new Intent(mContext,
        // ProductDetailActivity.class);
        // intent.putExtra("productId", proId);
        // startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (v.getId()) {
            case R.id.index_search_button://搜索
                intent = new Intent(mContext, SearchActivity.class);
                break;

//			case R.id.index_location_button://定位
//				intent=new Intent(mContext,LogisticsDetialsActivity.class);
//
//				break;
            case R.id.index_location_button://定位
                intent = new Intent(mContext, NearStoreActivity.class);
                break;
            case R.id.index_camer_button://扫描二维码
                intent = new Intent(mContext, ScanCodeActivity.class);
                break;
            case R.id.home_collection:
                if ("".equals(Utils.getTokenKey(app))) {
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    loginIntent.putExtra(LoginActivity.JUMP_TO_ACTIVITY, MyCollectionActivity.class);
                    startActivity(loginIntent);
//                    startActivityForResult(loginIntent, 94);
                } else {
                    Intent collecionIntent = new Intent(getActivity(), MyCollectionActivity.class);
                    startActivity(collecionIntent);
                }
                break;
            case R.id.home_logistics_information:
                if ("".equals(Utils.getTokenKey(app))) {
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    loginIntent.putExtra(LoginActivity.JUMP_TO_ACTIVITY, LogisticsActivity.class);
                    startActivityForResult(loginIntent, 94);
                } else {
                    Intent logisticsIntent = new Intent(getActivity(), LogisticsActivity.class);
                    startActivity(logisticsIntent);
                }
                break;
            case R.id.home_history:
                if ("".equals(Utils.getTokenKey(app))) {
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    loginIntent.putExtra(LoginActivity.JUMP_TO_ACTIVITY, ScanHistoryActivity.class);
                    startActivityForResult(loginIntent, 94);
                } else {
                    Intent historyIntent = new Intent(getActivity(), ScanHistoryActivity.class);
                    startActivity(historyIntent);
                }
                break;
            case R.id.home_company_profile:
                Intent profileIntent = new Intent(getActivity(), CompanyProfile.class);
                startActivity(profileIntent);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        //		String result = data.getExtras().getString("result");
        if (requestCode == 11 && data != null) {
            // tell the main activity to proceed the change.
            ((MainActivity) mContext).selectItem(MainActivity.TAB_SHOPPING_CART);
            ((MainActivity) mContext).mySelfFragmentNeedUpdate(true);
        }
    }


    class MyWebviewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (webView != null) {
                webView.setVisibility(View.INVISIBLE);
                webview_progressbar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (webView != null) {
                webView.setVisibility(View.VISIBLE);
                webview_progressbar.setVisibility(View.GONE);
            }
        }

    }


    protected LinearLayout getLny_network_error_layout(View v) {
        if (lny_network_error_layout == null)
            lny_network_error_layout = (LinearLayout) v.findViewById(R.id.lny_network_error_view);
        return lny_network_error_layout;
    }


    protected void hide_all(View view) {
        if (webview_progressbar != null)
            webview_progressbar.setVisibility(View.GONE);
        getLny_network_error_layout(view).setVisibility(View.GONE);
    }


    protected void networkExceptionError() {
        CustomToast.makeToast(mContext, getString(R.string.network_error), Toast.LENGTH_SHORT);
        hide_all(rootview);
        getLny_network_error_layout(rootview).setVisibility(View.VISIBLE);
    }


}
