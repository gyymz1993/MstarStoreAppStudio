package cn.mstar.store.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.mstar.store.R;
import cn.mstar.store.adapter.GetInShopAdapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.entity.ShopEntity;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class GetInShopActivity extends BaseActivity {

    private static final String DEFAULT_AREA_ID = "0";

    @Bind(R.id.title_back)
    ImageView back;
    @Bind(R.id.title_name)
    TextView title;
    @Bind(R.id.get_in_shop_address)
    TextView address;
    @Bind(R.id.shop_list)
    ListView shopList;

    private GetInShopAdapter adapter;
    private List<ShopEntity> data;
    private boolean isSinglePro;
    private int selected_position;

    private String area_id = DEFAULT_AREA_ID;
    private String latitude = "";
    private String longtitude = "";
    private LocationClient mLocationClient;
    private MyBDLocationListener bdLocationListener = new MyBDLocationListener();
    private GeoCoder geoCoder;
    private MyOnGetGeoCoderResultListener getGeoCoderResultListener = new MyOnGetGeoCoderResultListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_in_shop_layout);
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        L.e(((MyApplication) getApplication()).tokenKey);
        ButterKnife.bind(this);
        initView();
        initLocation();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {

        //init titlelayout
        back.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.get_in_shop_title));

        //init listview
        shopList.setHeaderDividersEnabled(false);
        shopList.setOnItemClickListener(itemClickListener);
        data = new ArrayList<>();
        adapter = new GetInShopAdapter(this, data);
        shopList.setAdapter(adapter);
    }

    private void initLocation() {
        //init GeoCoder
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(getGeoCoderResultListener);
        //init Location Client
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(bdLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void inflateData() {

        String proIds = getIntent().getStringExtra("proId");
        isSinglePro = proIds.contains("|");
        String numbers = getIntent().getStringExtra("proId_number");
        selected_position = getIntent().getIntExtra("position", -1);
        String url = NewLink.GET_IN_SHOP + "&proId=" + proIds
                + "&proId_number=" + numbers
                + "&area_id=" + area_id
                + "&company_longitude=" + latitude
                + "&company_latitude=" + longtitude
                + "&key=" + ((MyApplication) getApplication()).tokenKey;
        //url = "http://www.fanershop.com/mobile/index.php?act=member_buytoshop&op=show_stocklist&proId=2|1&proId_number=1|1&area_id=0&company_longitude=&company_latitude=&key=%20a8512f0dc4239931d29ea20848830e5a";
        L.e(url);
        i_showProgressDialog();
        VolleyRequest.GetCookieRequest(this, url, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e(result);
                if (result == null || "".equals(result)) {
                    i_dismissProgressDialog();
                    return;
                }
                data.clear();
                Gson gson = new Gson();
                String message = gson.fromJson(result, JsonObject.class).get("message").getAsString();
                L.e("wcl-->" + message);
                JsonElement element = gson.fromJson(result, JsonObject.class).get("data");
                L.e(element + "");
                if ("ok".equals(message) && element != null) {
                    JsonArray jsonArr = gson.fromJson(result, JsonObject.class).get("data").getAsJsonArray();
                    ShopEntity[] arr = gson.fromJson(jsonArr, ShopEntity[].class);
                    Collections.addAll(data, arr);
                    adapter.notifyDataSetChanged(isSinglePro, selected_position);
                } else if (element == null) {
                    Toast.makeText(GetInShopActivity.this, "没有合适店铺", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GetInShopActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                i_dismissProgressDialog();
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(GetInShopActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                i_dismissProgressDialog();
            }
        });
    }

    @OnClick(R.id.title_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.get_in_shop_address)
    public void selectAddress(View view) {
        Intent intent = new Intent();
        intent.setClass(this, SelectProvinceActivity.class);
        startActivityForResult(intent, 11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11 && resultCode == RESULT_OK) {
            L.e("countyId:" + data.getStringExtra("countyId"));
            L.e("cityId:" + data.getStringExtra("cityId"));
            L.e("provinceId:" + data.getStringExtra("provinceId"));
            area_id = data.getStringExtra("countyId");
            address.setText(getResources().getString(R.string.get_in_shop_your_address) + data.getStringExtra("address"));
            latitude = "";
            longtitude = "";
            inflateData();
        }
    }

    private class MyBDLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        address.setText(getResources().getString(R.string.get_in_shop_current_address) + "定位失败！");
                    }
                });
                return;
            }

            latitude = bdLocation.getLatitude() + "";
            longtitude = bdLocation.getLongitude() + "";
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
        }
    }

    private class MyOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        address.setText(getResources().getString(R.string.get_in_shop_current_address) + "未找到结果！");
                    }
                });
                return;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    address.setText(getResources().getString(R.string.get_in_shop_current_address) + reverseGeoCodeResult.getAddress());
                    inflateData();
                }
            });
        }
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            L.e(position + "");
            Intent intent = new Intent();
            adapter.singleClick(position);
            intent.putExtra("store_name", data.get(position).getStore_name());
            intent.putExtra("store_id", data.get(position).getStore_id());
            intent.putExtra("position", position);
            setResult(11, intent);
            finish();
        }
    };

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

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        MyApplication.requestQueue.cancelAll(this);
        super.onDestroy();
    }
}
