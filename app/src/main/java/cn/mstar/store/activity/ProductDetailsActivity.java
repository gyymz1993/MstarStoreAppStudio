package cn.mstar.store.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.mstar.store.R;
import cn.mstar.store.adapter.DetailsListAdapter;
import cn.mstar.store.app.Constants2;
import cn.mstar.store.app.MyAction;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.CompatibilityScrollListView;
import cn.mstar.store.customviews.CustomWebview;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.customviews.SharePopup;
import cn.mstar.store.customviews.SharePopup.OnDialogListener;
import cn.mstar.store.db.entityToSave.ProAndSpecialIdz;
import cn.mstar.store.entity.BuyProductEntity;
import cn.mstar.store.entity.Product;
import cn.mstar.store.fragments.ProductDetailsBottomFragment;
import cn.mstar.store.functionutils.BimpUtils;
import cn.mstar.store.functionutils.HtmlStyle;
import cn.mstar.store.functionutils.LogUtils;
import cn.mstar.store.functionutils.WeiXinShare;
import cn.mstar.store.interfaces.OnResultStatusListener;
import cn.mstar.store.utils.CustomToast;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.NetWorkUtil;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;
import cn.mstar.store.utils.VolleyRequest.HttpStringRequsetCallBack;
import cn.mstar.store.utils.VolleyRequest.LogonStatusLinstener;
import cn.mstar.store.view.ObservableScrollView;

/**
 * 产品详情界面
 *
 * @author wenjundu 2015-7-17
 */
public class ProductDetailsActivity extends AppCompatActivity implements
        OnDialogListener, OnClickListener ,ObservableScrollView.Callbacks{
    public static final int SET_TO_FAVORITE_SUCCESS = 221;
    public static final int SET_TO_FAVORITE_FAILURE = 222;
    // 分享按钮
    private ImageView shareIV;
    // 分享窗口
    private SharePopup popup;
    private RelativeLayout layout_root;
    private Product product;
    private String productinfoURL;
    private ViewPager viewPager;
    private LayoutInflater inflater;
    private View item;
    private ImageView image;
    private MyAdapter adapter;// viewpager适配器
    private ImageView titleBack;
    private TextView titleName;
    private TextView productName, productPrice;// 产品名称,价格
    private LinearLayout normsLayout;// 规格行
    private TextView normsTV;// 规格文字
    private LinearLayout btnAddcart, btnBuyNow;// 加入购物车 立即购买
    private String norm = "";// 规格字符串
    private int buyNum = 1;// 购买数量默认1
    private TextView indicatorTV;//展示图片指示器
    private int[] checkedIds;
    private ImageView iv_collect;

    private boolean hasContentChanged = false;
    private BuyProductEntity buyProductEntity;
    private MyApplication app;

    private CustomWebview webview;
    private CompatibilityScrollListView listView;
    private RadioGroup radioGroup;

    private Dialog mAlertDialog;

    private String shopid;
    private int proId;

    @Bind(R.id.share_btn)
    ImageView share;

    //进入商店和联系客服
    private LinearLayout go_comment_list;
    private LinearLayout go_store_button;
    private LinearLayout entrance_store_button;
    private LinearLayout contacet_service_button;
//	@Bind(R.id.frame_product_details_viewpager) FrameLayout frame_product_details_viewpager;
    //新商品个数
    public int storeNewPropectCount=10;
    public int storeGoodsPrCount=10;
    private TextView tv_new_product_count,tv_cx_product_count;


    public void initScrollView(){
        stopView = (View) findViewById(R.id.stopView);
        scrollView = (ObservableScrollView)findViewById(R.id.sv_bottom);
        scrollView.setCallbacks(this);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        onScrollChanged(scrollView.getScrollY());
                    }
                });
        // 滚动范围
        scrollView.scrollTo(0, 0);
        scrollView.smoothScrollTo(0, 0);//设置scrollView默认滚动到顶部

    }
    private View stopView;
    private ObservableScrollView scrollView;
    @SuppressLint("NewApi")
    @Override
    public void onScrollChanged(int scrollY) {
        ((LinearLayout)findViewById(R.id.stickyView))
                .setTranslationY(Math.max(stopView.getTop(), scrollY));

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent() {

    }


    public void loginToServer() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        app = (MyApplication) getApplication();
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        initView();
        initData();
        initScrollView();
        // we will init it with the gotten data.
//		initBottomFragment ();
    }


    @Override
    protected void onResume() {
        super.onResume();
        i_dismissProgressDialog();
        String tempUrl;
        if ("".equals(((MyApplication) getApplication()).tokenKey)) {
            tempUrl = productinfoURL;
        } else {
            tempUrl = productinfoURL + "&key=" + ((MyApplication) getApplication()).tokenKey;
        }
        getproductInfo(tempUrl);
    }

    private void initData() {
        Intent intent = getIntent();
        if (MyAction.productListActivityAction.equals(intent.getAction())) {
            proId = intent.getExtras().getInt("proId");
            productinfoURL = Constants2.PRODUCTDETAIL_URL + "&proid=" + proId;
        }
        L.e("productinfoURL", productinfoURL);
        //  标题
        titleName.setText(getString(R.string.product_details));
    }

    // 获得产品信息
    private void getproductInfo(String productinfoURL) {

        // size
        productinfoURL += "&size=" + (getScreenWidth() < 700 ? "500" : "800");
       // productinfoURL += "&size=" + 800;
        LogUtils.e("获取商品详情URL"+productinfoURL);
        if (NetWorkUtil.isNetworkConnected(this)) {
            i_showProgressDialog();
            VolleyRequest.GetCookieRequestPurePage(ProductDetailsActivity.this, productinfoURL, new HttpStringRequsetCallBack() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject dataJson = jsonObject
                                .getJSONObject("data");
                        product = new Product();
                        product.setProId(dataJson
                                .getInt("proId"));
                        product.setName(dataJson.getString("name"));
                        product.setDescURL(dataJson
                                .getString("desc"));
                        product.setImageUrl(dataJson
                                .getString("pic"));
                        product.setAttributes(dataJson
                                .getString("attribute"));
                        product.setPrice(Double
                                .parseDouble(dataJson
                                        .getString("price")));
                        product.setKindName(dataJson
                                .getString("kindName"));
                        product.setWeight(dataJson
                                .getString("weight"));
                        product.setBrandName(dataJson
                                .getString("brandName"));
                        product.setArea(dataJson.getString("area"));
                        product.setHit(dataJson.getString("hit"));
                        product.setProNum(dataJson
                                .getString("proNum"));
                        product.setHaveProSpecificationPrice(Boolean.parseBoolean(dataJson
                                .getString("isHaveProSpecificationPrice")));
                        product.setStock(dataJson.optInt("stock"));

                        product.setStock(dataJson.optInt("stock"));
                        storeNewPropectCount = dataJson.optInt("storeGoodsNew");
                        storeGoodsPrCount = dataJson.optInt("storeGoodsPr");
                        product.setHaveProFavorite(Boolean.parseBoolean(dataJson.getString("isHaveproFavorite")));
                        shopid = dataJson.getString("storeId");
                        Product temp = product;
                        JSONArray picsJson = dataJson
                                .getJSONArray("pics");
                        String[] pics = new String[picsJson
                                .length()];
                        for (int i = 0; i < picsJson.length(); i++) {
                            pics[i] = picsJson.optString(i);
                        }
                        product.setPics(pics);
                        // 往页面填充数据
                        L.d(" --- " + product.toString());
                        LoadingData(product);
                    } catch (Exception e) {
                        CustomToast.makeToast(ProductDetailsActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT);
                        finish();
                    }
                    i_dismissProgressDialog();
                }

                @Override
                public void onFail(String error) {
                    CustomToast.makeToast(ProductDetailsActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT);
                    i_dismissProgressDialog();
                    finish();
                }
            });
        }
    }


    @SuppressLint("InflateParams")
    private void LoadingData(Product product) {

        tv_new_product_count.setText(storeNewPropectCount+" 个");
        tv_cx_product_count.setText(storeGoodsPrCount+" 个");
        final List<View> list = new ArrayList<View>();
        inflater = LayoutInflater.from(this);
        /**
         * 创建多个item （每一条viewPager都是一个item） 从服务器获取完数据（图片url地址） 后，再设置适配器
         */
        for (int i = 0; i < product.getPics().length; i++) {
            item = inflater.inflate(R.layout.item_product_viewpager, null);
            list.add(item);
        }
        // 创建适配器， 把组装完的组件传递进去
        adapter = new MyAdapter(list);
        L.e(viewPager.toString());
        viewPager.setAdapter(adapter);
        indicatorTV.setVisibility(View.VISIBLE);
        indicatorTV.setText("1" + "/" + list.size());
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                indicatorTV.setText(position + 1 + "/" + list.size());
                L.e("" + position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        if (product != null)
            L.d("pro::", product.toString());
        // 产品名称
        productName.setText(product.getName());
        // 产品价格
        productPrice.setText(Utils.formatedPrice(product.getPrice()));
        // 默认规格字
        if (product.isHaveProSpecificationPrice()) {
            normsTV.setText(getString(R.string.select_norms));
            normsLayout.setClickable(true);
//			iv_collect.setBackgroundResource(R.drawable.commoditydetails_icon_collection_down);
        } else {
            normsTV.setText(getString(R.string.no_norms));
            normsLayout.setClickable(false);
//			iv_collect.setBackgroundResource(R.drawable.commoditydetails_icon_collection_nor);
        }

        if (product.isHaveProFavorite()) {
            iv_collect.setImageResource(R.drawable.commoditydetails_icon_collection_down);
//            iv_collect.setBackgroundResource(R.drawable.commoditydetails_icon_collection_down);
        } else
            iv_collect.setImageResource(R.drawable.commoditydetails_icon_collection_nor);
//            iv_collect.setBackgroundResource(R.drawable.commoditydetails_icon_collection_nor);

        // loading the webview.
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
        webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        StorageUtils.getOwnCacheDirectory(ProductDetailsActivity.this,
                "MstarStore/Cache").getAbsolutePath();
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // remove a weird white line on the right size
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        String mime = "text/html";
        String encoding = "utf-8";
        webview.getSettings().setJavaScriptEnabled(true);
        product.setDescURL(HtmlStyle.setHtmlStyle(this, product.getDescURL()));
        webview.loadDataWithBaseURL(null, product.getDescURL(), mime, encoding, null);
        webview.setVerticalScrollBarEnabled(false);

        Gson gson = new Gson();
        String[] data = gson.fromJson(product.getAttributes(), String[].class);
        listView.setAdapter(new DetailsListAdapter(ProductDetailsActivity.this, data));
        listView.setVerticalScrollBarEnabled(false);
//		if (product != null)
//			initBottomFragment (product.getDescURL(), product.getAttributes());
    }



    public void setImage(){
//        Elements elementImgs = detail.getElementsByTag("img");//获取所有img标
//        for (Element img : elementImgs) {
//            img.attr("width", (int)(deviceInfo.width/deviceInfo.density) + "px");//设置width属性
//        }
    }


    private void initBottomFragment(String descURL, String attributes) {


        L.d("desc:::", descURL);
        //
        ProductDetailsBottomFragment frg = ProductDetailsBottomFragment.getInstance(descURL, attributes, getScreenHeight());
////		frame_product_details_viewpager
//		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////		transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
//		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
//		transaction.add(R.id.frame_product_details_viewpager, frg);
//		transaction.commit();
    }

    private int getScreenHeight() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }


    private int getScreenWidth() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }


    LoadingDialog dialog;

    public void i_showProgressDialog() {
        dialog = new LoadingDialog(this);
        dialog.show();
    }

    public void i_dismissProgressDialog() {
        if (dialog != null) {
            dialog.cancel();
            dialog.dismiss();
            dialog = null;
        }
    }

    private void initView() {

        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(this);

        tv_cx_product_count= (TextView) findViewById(R.id.id_tv_cx_product_count);
        tv_new_product_count= (TextView) findViewById(R.id.id_tv_new_product_count);
        webview = (CustomWebview) findViewById(R.id.webview);
        listView = (CompatibilityScrollListView) findViewById(R.id.listview);
        radioGroup = (RadioGroup) findViewById(R.id.bottom_view_radiogroup);
        shareIV = (ImageView) findViewById(R.id.iv_share);
        layout_root = (RelativeLayout) findViewById(R.id.layout_root);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        titleBack = (ImageView) findViewById(R.id.title_back);
        titleName = (TextView) findViewById(R.id.title_name);

        shareIV = (ImageView) findViewById(R.id.iv_share);
        layout_root = (RelativeLayout) findViewById(R.id.layout_root);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        titleBack.setVisibility(View.VISIBLE);
        titleBack.setOnClickListener(this);
        productName = (TextView) findViewById(R.id.tv_product_name);
        productPrice = (TextView) findViewById(R.id.tv_price);

        btnAddcart = (LinearLayout) findViewById(R.id.btn_add_shopping_cart);
        btnBuyNow = (LinearLayout) findViewById(R.id.btn_buy_now_cart);

        indicatorTV = (TextView) findViewById(R.id.indicator_tv);

        btnAddcart.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
        normsLayout = (LinearLayout) findViewById(R.id.norms_layout);
        normsTV = (TextView) findViewById(R.id.tv_norms);
        popup = new SharePopup(this, this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.detail_radiobutton) {
                    webview.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    webview.setVisibility(View.GONE);
                }
            }
        });
        radioGroup.check(R.id.detail_radiobutton);
        webview.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        go_comment_list = (LinearLayout) findViewById(R.id.comment_layout);
        go_comment_list.setOnClickListener(this);
        go_store_button = (LinearLayout) findViewById(R.id.product_go_store);
        contacet_service_button = (LinearLayout) findViewById(R.id.contacet_service_button);
        entrance_store_button = (LinearLayout) findViewById(R.id.entrance_store_button);
        contacet_service_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog = new Dialog(ProductDetailsActivity.this);
                mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mAlertDialog.setContentView(R.layout.product_contact_service);
                mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mAlertDialog.show();
                mAlertDialog.findViewById(R.id.product_detail_refuse).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAlertDialog.dismiss();
                    }
                });
                mAlertDialog.findViewById(R.id.product_detail_accept).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.service_phone_number)));
                        startActivity(intent);
                        mAlertDialog.dismiss();
                    }
                });
            }
        });
        entrance_store_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, StoreDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("shopid", shopid);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        go_store_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, StoreDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("shopid", shopid);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        
        normsLayout.setOnClickListener(this);
        iv_collect = (ImageView) findViewById(R.id.iv_collect);
        iv_collect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                L.d(" --- got object " + product.toString());
                VolleyRequest.checkLogStatus((MyApplication) ProductDetailsActivity.this.getApplication(), new LogonStatusLinstener() {

                    @Override
                    public void OK(String reason) {
                        // TODO Auto-generated method stub

                        // a method to delete or add the items.
                        if (product.isHaveProFavorite())
                            VolleyRequest.deleteItemsFromFavorite(ProductDetailsActivity.this, String.valueOf(product.getProId()), Utils.getTokenKey((MyApplication) ProductDetailsActivity.this.getApplication()), new OnResultStatusListener() {

                                @Override
                                public void success(String result) {

                                    product.setHaveProFavorite(!product.isHaveProFavorite());
                                    hasContentChanged = !hasContentChanged;
                                    if (product.isHaveProFavorite()) {
//                                        iv_collect.setBackgroundResource(R.drawable.commoditydetails_icon_collection_down);
                                        iv_collect.setImageResource(R.drawable.commoditydetails_icon_collection_down);
                                        //										CustomToast.mToast(ProductDetailsActivity.this, "adding ok");
                                        CustomToast.mSystemToast(ProductDetailsActivity.this, getString(R.string.delete_favorite_error));
                                    } else {
//                                        iv_collect.setBackgroundResource(R.drawable.commoditydetails_icon_collection_nor);
                                        iv_collect.setImageResource(R.drawable.commoditydetails_icon_collection_nor);
                                        //										CustomToast.mToast(ProductDetailsActivity.this, "deleting ok");
                                        CustomToast.mSystemToast(ProductDetailsActivity.this, getString(R.string.delete_favorite_ok));
                                    }
                                }

                                @Override
                                public void failure(String error) {
                                    CustomToast.mSystemToast(ProductDetailsActivity.this, error);
                                }
                            });
                        else {
                            VolleyRequest.addItemsFromFavorite(ProductDetailsActivity.this, String.valueOf(product.getProId()), Utils.getTokenKey((MyApplication) ProductDetailsActivity.this.getApplication()), new OnResultStatusListener() {

                                @Override
                                public void success(String result) {
                                    product.setHaveProFavorite(!product.isHaveProFavorite());
                                    hasContentChanged = !hasContentChanged;
                                    if (product.isHaveProFavorite()) {
//                                        iv_collect.setBackgroundResource(R.drawable.commoditydetails_icon_collection_down);
                                        iv_collect.setImageResource(R.drawable.commoditydetails_icon_collection_down);
                                        CustomToast.mSystemToast(ProductDetailsActivity.this, getString(R.string.adding_favorite_ok));
                                    } else {
//                                        iv_collect.setBackgroundResource(R.drawable.commoditydetails_icon_collection_nor);
                                        iv_collect.setImageResource(R.drawable.commoditydetails_icon_collection_nor);
                                        CustomToast.mSystemToast(ProductDetailsActivity.this, getString(R.string.adding_favorite_error));
                                    }
                                }

                                @Override
                                public void failure(String error) {
                                    CustomToast.mSystemToast(ProductDetailsActivity.this, error);
                                }
                            });
                        }
                    }

                    @Override
                    public void NO() {

                        // 需要首先登陆
                        loginToServer();
                    }
                });


            }
        });
    }

    @Override
    public void onShareWechat() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onShareFriends() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onShareSina() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onShareQQ() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onShareQQZone() {
        // TODO Auto-generated method stub

    }

    /**
     * 适配器，负责装配 、销毁 数据 和 组件 。
     */
    private class MyAdapter extends PagerAdapter {
        private List<View> mList;

        public MyAdapter(List<View> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        /**
         * Remove a page for the given position. 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
         * instantiateItem(View container, int position) This method was
         * deprecated in API level . Use instantiateItem(ViewGroup, int)
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // TODO Auto-generated method stub

            View view = mList.get(position);
            image = ((ImageView) view.findViewById(R.id.image));
            ImageLoader.getInstance().displayImage(product.getPics()[position],
                    image, ImageLoadOptions.getOptions());
            image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProductDetailsActivity.this,
                            ImageBrowserActivity.class);
                    intent.putExtra("photos", product.getPics());
                    L.e("size:" + product.getPics().length);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
            container.removeView(mList.get(position));
            container.addView(mList.get(position));
            return mList.get(position);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (resultCode) {
            case 1:
                buyProductEntity = (BuyProductEntity) data.getSerializableExtra("buyProduct");
                if (buyProductEntity != null)
                    L.d("buy:::", buyProductEntity.toString());
                product.setPrice(buyProductEntity.getProduct().getPrice());
                product.setStock(buyProductEntity.getProduct().getStock());
                product.setProSpecialID(buyProductEntity.getProduct().getProSpecialID());
                buyProductEntity.setProduct(product);
                productPrice.setText(Utils.formatedPrice(product.getPrice()));
                // 不让改变选择规格那部分二
//				normsTV.setText(buyProductEntity.getNorms());
                checkedIds = data.getIntArrayExtra("checkedIds");
                buyNum = buyProductEntity.getBuyNum();
                break;
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        //                                          i.putExtra(Constants.START_ACTIVITY_FOR_RESULT_KEY, true);
        i.putExtra(cn.mstar.store.utils.Constants.START_ACTIVITY_FOR_RESULT_KEY, hasContentChanged);
        this.setResult(2, i);
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private PopupWindow popView;
    WindowManager.LayoutParams lp;
    private String shareUrl;
    Bitmap bit;
    @Override
    public void onClick(View v) {
        shareUrl="http://m.fanershop.com/index.php?m=QxWeb&a=goods&id="+proId+"&code=001464f690bb83265732312739ce8b1y&state=STATE";
        bit =ImageLoader.getInstance().loadImageSync(product.getPics()[0]);
        switch (v.getId()) {
            case R.id.title_back:// 返回
                finish();
                break;
            case R.id.norms_layout:// 跳转到产品规格页
                addElementToShoppingCart();
                break;
            case R.id.btn_add_shopping_cart:// 加入购物车
                // add elements to shopping cart.
                addElementToShoppingCart();
                break;
            case R.id.btn_buy_now_cart:// 立即购买
                if (product.getStock() == 0) {
                    CustomToast.makeToast(this, "库存不足", Toast.LENGTH_SHORT);
                    return;
                }

                if ("".equals(app.tokenKey)) {
                    loginToServer();
                    return;
                }

                if (!product.isHaveProSpecificationPrice()) {
                    BuyProductEntity buyProductEntity = new BuyProductEntity();
                    buyProductEntity.setProduct(product);
                    buyProductEntity.setBuyNum(1);
                    Intent intent = new Intent(this, ConfirmIndentActivity.class);
                    intent.setAction(MyAction.productDetailsActivityAction);
                    intent.putExtra("buyProduct", buyProductEntity);
                    startActivity(intent);
                    return;
                }

                // 直接调到选择规格见面，然后
                Intent intent = new Intent(this, SelectCommodityActivity.class);
                intent.putExtra("proId", product.getProId());
                intent.setAction(MyAction.directlyGetToPayAction);
                startActivity(intent);
                break;
            case R.id.comment_layout:
                Intent intent2 = new Intent(this, CommentListActivity.class);
                intent2.putExtra("proId", proId + "");
                startActivity(intent2);
                break;
            case R.id.id_lay_sharweixin:

                WeiXinShare.wechatShare(this, shareUrl, 0, bit);//分享到微信好友
                if (popView != null)
                    popView.dismiss();
                break;
            case R.id.id_lay_sharweixinfriend:
                WeiXinShare.wechatShare(this, shareUrl, 1,bit);//分享到微信好友
                if (popView != null)
                    popView.dismiss();
                break;
            case R.id.share_btn:
                LogUtils.e("showDialog");
                View convertView = getLayoutInflater().inflate(R.layout.share_layout, null, false);
                convertView.findViewById(R.id.id_lay_sharweixin).setOnClickListener(this);
                convertView.findViewById(R.id.id_lay_sharweixinfriend).setOnClickListener(this);
                popView = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popView.setTouchable(true);
                // 设置背景颜色变暗
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
                popView.showAtLocation(btnBuyNow, Gravity.BOTTOM, 0, 0);
                break;
        }
    }




    private void addElementToShoppingCart() {

        if (app == null || "".equals(app.tokenKey)) {
            loginToServer();
            return;
        }
        if (product != null) {

            if (!product.isHaveProSpecificationPrice()) {
                if (product.getStock() == 0) {
                    CustomToast.makeToast(this, "库存不足", Toast.LENGTH_SHORT);
                    return;
                }
                i_showProgressDialog();
                ProAndSpecialIdz item = new ProAndSpecialIdz(product.getProId(), product.getProSpecialID(), 1);
                VolleyRequest.addInShoppingCart((MyApplication) ProductDetailsActivity.this.getApplication(), new ProAndSpecialIdz[]{item},
                        Utils.getTokenKey((MyApplication) ProductDetailsActivity.this.getApplication()), new OnResultStatusListener() {

                            @Override
                            public void success(String result) {
                                // 添加成功
                                CustomToast.makeToast(ProductDetailsActivity.this, getString(R.string.add_shopping_cart_success), Toast.LENGTH_SHORT);
                                i_dismissProgressDialog();
                            }

                            @Override
                            public void failure(String error) {
                                switch (error) {
                                    case "0":
                                        // 添加失败
                                        CustomToast.makeToast(ProductDetailsActivity.this, getString(R.string.add_shopping_cart_failure), Toast.LENGTH_SHORT);
                                        break;
                                    case "-1":
                                        // 网络失败
                                        CustomToast.makeToast(ProductDetailsActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT);
                                        break;
                                    default:
                                        CustomToast.makeToast(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT);
                                        break;
                                }
                                i_dismissProgressDialog();
                            }
                        });

                return;
            }
            Intent intent = new Intent(this,
                    SelectCommodityActivity.class);
            if (checkedIds != null)
                intent.putExtra("checkedIds", checkedIds);
            if (buyProductEntity != null)
                intent.putExtra("buyNums", buyProductEntity.getBuyNum());
            else
                intent.putExtra("buyNums", 1);
            intent.putExtra("proId", product.getProId());
            intent.setAction(MyAction.productDetailsActivityAction);
            // startActivity(intent);
            startActivityForResult(intent, 1);
        }
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.custom_in_anim, R.anim.custom_out_anim);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.custom_in_anim, R.anim.custom_out_anim);
    }

    @Override
    protected void onDestroy() {
        MyApplication.requestQueue.cancelAll(this);
        super.onDestroy();
    }
}
