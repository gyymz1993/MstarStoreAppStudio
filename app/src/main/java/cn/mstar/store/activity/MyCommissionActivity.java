package cn.mstar.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.mstar.store.R;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.interfaces.HttpRequestCallBack;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by Administrator on 2015/10/29.
 */
public class MyCommissionActivity extends BaseActivity implements View.OnClickListener {

    private TextView commission_text;
    private TextView allcommission_text;
    private TextView recentlycommission_text, titleName;
    private Button applyforcommission_btn;
    private ImageView titleBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycommission);
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        initView();
        initListener();
        initData();


    }

    private void initData() {

        getInfo();

    }

    private void getInfo() {

        String str = NewLink.MYCOMMISSION + "&tokenKey=" + Utils.getTokenKey((MyApplication) this.getApplication());
        VolleyRequest.GetRequest(MyCommissionActivity.this, str, new HttpRequestCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONObject data = jsonObject.getJSONObject("data");
                    commission_text.setText(data.getString("restcomm"));
                    allcommission_text.setText(data.getString("hiscomm"));
                    recentlycommission_text.setText(data.getString("newcomm"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String fail) {

            }
        });

    }

    private void initListener() {
        applyforcommission_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCommissionActivity.this, ApplyForWithdrawActivity.class);
                startActivity(intent);
            }
        });
        titleBack.setOnClickListener(this);

    }

    private void initView() {
        commission_text = (TextView) findViewById(R.id.commission_text);
        titleBack = (ImageView) findViewById(R.id.title_back);
        titleName = (TextView) findViewById(R.id.title_name);
        allcommission_text = (TextView) findViewById(R.id.allcommission_text);
        recentlycommission_text = (TextView) findViewById(R.id.recentlycommission_text);
        applyforcommission_btn = (Button) findViewById(R.id.applyforcommission_btn);

        titleBack.setVisibility(View.VISIBLE);
        titleName.setText("我的佣金");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;

        }
    }
}
