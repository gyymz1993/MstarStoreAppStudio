package cn.mstar.store.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.ShopClassifyAdapter;
import cn.mstar.store.adapter.StoreshopAdapter;
import cn.mstar.store.app.Constants2;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.entity.CommonJson;
import cn.mstar.store.entity.GoodsClassify;
import cn.mstar.store.entity.ProInfo;
import cn.mstar.store.entity.StroreDetailsEntity;
import cn.mstar.store.entity.TotalInfo;
import cn.mstar.store.interfaces.HttpRequestCallBack;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by Administrator on 2015/9/22.
 */
public class StoreDetailsActivity extends AppCompatActivity implements OnClickListener {

    private TextView store_titlename_text;
    private TextView store_detailsclick_text;
    private TextView store_allshopnum_text;
    private TextView store_newshopnum_text;
    private TextView store_promotionshopnum_text;
    private ImageView store_logo_img;
    private ImageView titleBack;
    private TextView titleName;
    private List<ProInfo> list;
    private ImageView store_title_baground;
    private StoreshopAdapter adapter;
    private GridView storeshopgridview;
    private LinearLayout store_detailslayout;
    private TextView shop_score_text;
    private TextView shop_service_text;
    private TextView shop_deliveryspeed_text;
    private TextView store_intro_text;
    private TextView campany_name_text;
    private TextView store_telphone_text;
    private TextView store_address_text;
    private TextView store_opentime_text;
    private ImageView title_message;
    private ListView classifylist;
    private SlidingMenu slidingMenu;
    private ShopClassifyAdapter shopClassifyAdapter;
    private List<GoodsClassify> classify_list;
    private GoodsClassify goodsclassify;
    private String shopid;
    private ImageView store_title_background;
    private LinearLayout shop_promotion_layout, shop_all_layout, shop_new_layout;
    private Context context;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        initView();
        initListener();
        showDialog();
        initData();
    }

    private void showDialog() {
        dialog = new LoadingDialog(this, "加载中...");
        dialog.show();
    }

    private void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.showContent();
            return;
        }
        super.onBackPressed();
    }

    private void initData() {
        //给适配器装数据
        list = new ArrayList<ProInfo>();
        adapter = new StoreshopAdapter(this, list, getScreenWidth());
        storeshopgridview.setAdapter(adapter);
        getStoreInfo("store", shopid);
        classify_list = new ArrayList<GoodsClassify>();
        shopClassifyAdapter = new ShopClassifyAdapter(this, classify_list);
        classifylist.setAdapter(shopClassifyAdapter);
        getshopclassify();
    }

    private void getStoreInfo(String act, String shopid) {
        String storeDetialsInfoUrl = Constants2.BASE_URL + "act=store&shopId=1";
        L.e("logisticsInfoUrl:" + storeDetialsInfoUrl);
        VolleyRequest.GetRequest(this, storeDetialsInfoUrl, new HttpRequestCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                L.e(jsonObject.toString());
                CommonJson<StroreDetailsEntity> cj = CommonJson.fromJson(jsonObject.toString(), StroreDetailsEntity.class);
                if (cj.getError().equals("0")) {
                    StroreDetailsEntity stroreDetailsEntity = cj.getData();
                    TotalInfo totalInfo = stroreDetailsEntity.totalInfo;
                    ProInfo[] proInfo = stroreDetailsEntity.proInfo;
                    store_titlename_text.setText(totalInfo.getStoreName());
                    store_allshopnum_text.setText(totalInfo.getAllgoods());
                    store_newshopnum_text.setText(totalInfo.getNewgoods());
                    store_promotionshopnum_text.setText(totalInfo.getCxgoods());
                    ImageLoader.getInstance().displayImage(totalInfo.getLogo(), store_logo_img, ImageLoadOptions.getOptions());
                    ImageLoader.getInstance().displayImage(totalInfo.getBanner(), store_title_baground, ImageLoadOptions.getOptions());
                    if (proInfo != null) {
                        list.addAll(Arrays.asList(proInfo));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(String fail) {
                Toast.makeText(StoreDetailsActivity.this, "网络连接错误，请检查网络！", Toast.LENGTH_SHORT).show();
                dismissDialog();
            }
        });

    }

    private int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private void initListener() {
        store_detailsclick_text.setOnClickListener(this);
        titleBack.setOnClickListener(this);
        title_message.setOnClickListener(this);
        shop_promotion_layout.setOnClickListener(this);
        shop_all_layout.setOnClickListener(this);
        shop_new_layout.setOnClickListener(this);
        store_title_background.setOnClickListener(this);
        classifylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StoreDetailsActivity.this, ClassifyStoreShopActivity.class);
                intent.putExtra("shopid", shopid);
                intent.putExtra("category", classify_list.get(position).getGcId());
                intent.putExtra("categoryName", classify_list.get(position).getGcName());
                intent.putExtra("classifylist", (Serializable) classify_list);
                startActivity(intent);
                slidingMenu.showContent();
            }
        });


    }

    private void initView() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        View view = LayoutInflater.from(this).inflate(R.layout.storedetails_menu_item, null);
        classifylist = (ListView) view.findViewById(R.id.storedetails_listView);
        slidingMenu.setMenu(view);

        store_titlename_text = (TextView) findViewById(R.id.store_title_name);
        store_detailsclick_text = (TextView) findViewById(R.id.store_details_click);
        store_allshopnum_text = (TextView) findViewById(R.id.store_allshop_numtext);
        store_newshopnum_text = (TextView) findViewById(R.id.shop_new_textnum);
        titleBack = (ImageView) findViewById(R.id.title_back);
        storeshopgridview = (GridView) findViewById(R.id.store_shop_gridview);
        titleName = (TextView) findViewById(R.id.title_name);
        store_promotionshopnum_text = (TextView) findViewById(R.id.shop_promotion_textnum);
        store_logo_img = (ImageView) findViewById(R.id.store_logo_img);
        store_title_baground = (ImageView) findViewById(R.id.store_title_background);

        store_detailslayout = (LinearLayout) findViewById(R.id.store_detailslayout);

        shop_score_text = (TextView) findViewById(R.id.shop_score_text);
        shop_service_text = (TextView) findViewById(R.id.shop_service_text);
        shop_deliveryspeed_text = (TextView) findViewById(R.id.shop_deliveryspeed_text);
        store_intro_text = (TextView) findViewById(R.id.store_intro_text);
        campany_name_text = (TextView) findViewById(R.id.company_name_text);
        store_telphone_text = (TextView) findViewById(R.id.store_telphone_text);
        store_address_text = (TextView) findViewById(R.id.store_address_text);
        store_opentime_text = (TextView) findViewById(R.id.store_opentime_text);
        title_message = (ImageView) findViewById(R.id.title_message);

        shop_promotion_layout = (LinearLayout) findViewById(R.id.shop_promotion_layout);
        ;
        shop_all_layout = (LinearLayout) findViewById(R.id.shop_all_layout);
        shop_new_layout = (LinearLayout) findViewById(R.id.shop_new_layout);
        store_title_background = (ImageView) findViewById(R.id.store_title_background);

        titleBack.setVisibility(View.VISIBLE);
        titleName.setText("本店详情");
        title_message.setVisibility(View.VISIBLE);
        title_message.setImageDrawable(getResources().getDrawable(R.drawable.shop_nav_icon_classification));
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        shopid = bundle.getString("shopid");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.store_details_click://显示详情
                   //转换界面
                storeshopgridview.setVisibility(View.GONE);
                store_detailslayout.setVisibility(View.VISIBLE);
                getStore();
                break;*/
            case R.id.title_message://侧滑
                slidingMenu.toggle();
                break;
            case R.id.shop_all_layout://所有商品
                //      getIntent().getExtras("shopid");
                Intent intent = new Intent(this, StoreShopActivity.class);
                String url = Constants2.BASE_URL + "act=shop_goods&shopId=" + shopid;
                intent.putExtra("url", url);
                intent.putExtra("shopId", shopid + "");
                intent.putExtra("classifylist", (Serializable) classify_list);
                startActivity(intent);
                break;


            case R.id.store_title_background://查看店铺详情

                Intent intent4 = new Intent(this, ShopDetailsActvity.class);
                intent4.putExtra("shopid", shopid);
                startActivity(intent4);

                break;
            case R.id.shop_new_layout://新产品

                Intent intent2 = new Intent(this, StoreShopActivity.class);
                String url2 = Constants2.BASE_URL + "act=shop_goods&op=promotion&type=new&shopId=" + shopid;
                intent2.putExtra("url", url2);
                intent2.putExtra("shopId", shopid + "");
                intent2.putExtra("classifylist", (Serializable) classify_list);
                startActivity(intent2);

                break;
            case R.id.shop_promotion_layout://促销
                Intent intent3 = new Intent(this, StoreShopActivity.class);
                String url3 = Constants2.BASE_URL + "act=shop_goods&op=promotion&type=prom&shopId=" + shopid;
                intent3.putExtra("url", url3);
                intent3.putExtra("shopId", shopid + "");
                intent3.putExtra("classifylist", (Serializable) classify_list);
                startActivity(intent3);
                break;

            case R.id.title_back:
                finish();
                break;
        }

    }

    private void getshopclassify() {
        String shopclassifyInfoUrl = Constants2.BASE_URL + "act=store&op=get_category&shopId=1";
        L.e("logisticsInfoUrl:" + shopclassifyInfoUrl);
        VolleyRequest.GetRequest(this, shopclassifyInfoUrl, new HttpRequestCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                L.e(jsonObject.toString());
                try {
                    String error = jsonObject.getString("error");
                    if (error.equals("0")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray classsifyarray = data.getJSONArray("gcInfo");
                        for (int i = 0; i < classsifyarray.length(); i++) {
                            goodsclassify = new GoodsClassify();
                            goodsclassify.setGcName(classsifyarray.getJSONObject(i).getString("gcName"));
                            goodsclassify.setGcId(classsifyarray.getJSONObject(i).getString("gcId"));
                            classify_list.add(goodsclassify);
                        }
                        shopClassifyAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissDialog();
            }

            @Override
            public void onFailure(String fail) {
                Toast.makeText(StoreDetailsActivity.this, "网络连接错误，请检查网络！", Toast.LENGTH_SHORT).show();
                dismissDialog();
            }
        });


    }

    private void getStore() {
        String storeInfoUrl = Constants2.BASE_URL + "act=store_detail&op=get_store_detail&shopId=1";
        L.e("logisticsInfoUrl:" + storeInfoUrl);
        VolleyRequest.GetRequest(this, storeInfoUrl, new HttpRequestCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                L.e(jsonObject.toString());
                try {
                    String error = jsonObject.getString("error");
                    if (error.equals("0")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject storeInfo = data.getJSONObject("storeInfo");
                        shop_score_text.setText(storeInfo.getString("descCredit"));
                        shop_service_text.setText(storeInfo.getString("serviceCredit"));
                        shop_deliveryspeed_text.setText(storeInfo.getString("deliveryCredit"));
                        store_intro_text.setText(storeInfo.getString("descriptions"));
                        campany_name_text.setText(storeInfo.getString("companyName"));
                        store_telphone_text.setText(storeInfo.getString("phoneNum"));
                        store_address_text.setText(storeInfo.getString("area"));
                        store_opentime_text.setText(storeInfo.getString("startTime"));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String fail) {
                Toast.makeText(StoreDetailsActivity.this, "网络连接错误，请检查网络！", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
