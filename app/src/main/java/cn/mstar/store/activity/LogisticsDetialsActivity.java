package cn.mstar.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.LogisticsAdapter;
import cn.mstar.store.app.Constants2;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.CommonJson;
import cn.mstar.store.entity.ExpressInfo;
import cn.mstar.store.entity.LogisticsEntity;
import cn.mstar.store.entity.ShipInfo;
import cn.mstar.store.interfaces.HttpRequestCallBack;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * 物流详情页
 *
 * @author wenjundu
 */
public class LogisticsDetialsActivity extends BaseActivity {
    private ImageView logisticsDetails_back_btn;
    private TextView logisticsDetails_title_txt;
    private TextView companyTV, waybillTV, orderTimeTV;//公司名称，运单单号,下单时间
    private String token;
    private ListView listView;
    private List<ShipInfo> list;
    private LogisticsAdapter adapter;
    private String shippingCode;
    private String eCode;
    MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_details);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        app = (MyApplication) getApplication();
        init();
    }

    private void init() {
        logisticsDetails_back_btn = (ImageView) findViewById(R.id.title_back);
        logisticsDetails_title_txt = (TextView) findViewById(R.id.title_name);
        companyTV = (TextView) findViewById(R.id.logistics_company_tv);
        waybillTV = (TextView) findViewById(R.id.waybill_number_tv);
        orderTimeTV = (TextView) findViewById(R.id.Order_time_tv);
        listView = (ListView) findViewById(R.id.logistics_info_list);
        logisticsDetails_title_txt.setText(getResources().getString(R.string.logistics_detail_title_name));
        logisticsDetails_back_btn.setVisibility(View.VISIBLE);
        logisticsDetails_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        token = app.tokenKey;
        Intent mIntent = getIntent();
        shippingCode = mIntent.getStringExtra("shippingCode");
        eCode = mIntent.getStringExtra("eCode");
        list = new ArrayList<ShipInfo>();
        adapter = new LogisticsAdapter(this, list);
        listView.setAdapter(adapter);
        if (token.equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 1);
        } else {
            getLogisticsInfo();
        }
    }

    //获取物流信息
    private void getLogisticsInfo() {
//		String logisticsInfoUrl= Constants2.GET_LOGISTICS_INFO_URL+"&key="+token+"&shippingCode="+"560353808491"+"&eCode="+"tiantian";
        String logisticsInfoUrl = Constants2.GET_LOGISTICS_INFO_URL + "&key=" + token + "&shippingCode=" + shippingCode + "&eCode=" + eCode;
        L.e("logisticsInfoUrl:" + logisticsInfoUrl);
        VolleyRequest.GetRequest(this, logisticsInfoUrl, new HttpRequestCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                L.e(jsonObject.toString());
                try {
                    CommonJson<LogisticsEntity> cj = CommonJson.fromJson(jsonObject.toString(), LogisticsEntity.class);
                    if (cj.getError().equals("0")) {
                        LogisticsEntity logisticsEntity = cj.getData();
                        ExpressInfo expressInfo = logisticsEntity.expressInfo;
                        ShipInfo[] shipInfos = logisticsEntity.shipInfo;
                        companyTV.setText(expressInfo.geteName());
                        waybillTV.setText(expressInfo.getShippingCode());
                        orderTimeTV.setText(expressInfo.getAddTime());
                        if (shipInfos != null) {
                            list.addAll(Arrays.asList(shipInfos));
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(LogisticsDetialsActivity.this, "没有物流信息", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String fail) {
                Toast.makeText(LogisticsDetialsActivity.this, "网络连接错误，请检查网络！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        MyApplication.requestQueue.cancelAll(this);
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            if (data != null) {
                token = data.getExtras().getString("token");
                getLogisticsInfo();
            }
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


}
