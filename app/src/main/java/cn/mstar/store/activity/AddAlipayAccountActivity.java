package cn.mstar.store.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cn.mstar.store.R;
import cn.mstar.store.app.Constants2;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.functionutils.URLtoUTF8Utils;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class AddAlipayAccountActivity extends BaseActivity {

    private EditText account;
    private EditText user;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarColor(this,getResources().getColor(R.color.status_bar_color));
        setContentView(R.layout.addalipayaccount_layout);
        initView();
        initData();
    }

    private void initView() {
        initHeadView();
        account = (EditText) findViewById(R.id.alipay_account);
        user = (EditText) findViewById(R.id.alipay_name);
        save = (Button) findViewById(R.id.alipay_save);
    }

    private void initHeadView() {
        ImageView back = (ImageView) findViewById(R.id.title_back);
        TextView title = (TextView) findViewById(R.id.title_name);
        back.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText("添加支付宝账号");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account_s = account.getText().toString();
                if ("".equals(account_s)) {
                    Toast.makeText(AddAlipayAccountActivity.this, Html.fromHtml("<font color=\"red\">账号为空！</font>"), Toast.LENGTH_SHORT).show();
                    return;
                }
                String user_s = user.getText().toString();
                if ("".equals(user_s)) {
                    Toast.makeText(AddAlipayAccountActivity.this, Html.fromHtml("<font color=\"red\">用户名为空！</font>"), Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = Constants2.BASE_URL + "act=commission&op=addbank" + "&tokenKey=" + ((MyApplication) getApplication()).tokenKey + "&bankType=" + 1
                        + "&bankCart=" + URLtoUTF8Utils.toUtf8String(account_s) + "&accountName=" + URLtoUTF8Utils.toUtf8String(user_s);

                showDialog();
                VolleyRequest.GetCookieRequest(AddAlipayAccountActivity.this, url, new VolleyRequest.HttpStringRequsetCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                        dialog.dismiss();
                        if ("ok".equals(message)) {
                            Toast.makeText(AddAlipayAccountActivity.this, Html.fromHtml("<font color=\"red\">保存成功！</font>"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(AddAlipayAccountActivity.this, Html.fromHtml("<font color=\"red\">保存失败！</font>"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(String error) {
                        hideDialog();
                        Toast.makeText(AddAlipayAccountActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private LoadingDialog dialog;

    private void showDialog() {
        dialog = new LoadingDialog(this);
        dialog.show();
    }

    private void hideDialog() {
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.requestQueue.cancelAll(this);
    }
}
