package cn.mstar.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.UnionListAdapter;
import cn.mstar.store.adapter.ZhifubaoListAdapter;
import cn.mstar.store.app.Constants2;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.entity.Union;
import cn.mstar.store.entity.Zhifubao;
import cn.mstar.store.functionutils.DialogUtils;
import cn.mstar.store.interfaces.HttpRequestCallBack;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

public class ApplyForWithdrawActivity extends BaseActivity implements View.OnClickListener {

    private TextView applyformoney_text;
    private CheckBox ck_applyforzhifubao, ck_applyforunion;
    private ListView listView_zhifubao, listView_union;
    private Button applyforcommission_btn;
    private LinearLayout zhifubao_layout, union_layout;
    private List<Zhifubao> zhifubaolist;
    private List<Union> unionlist;
    private ZhifubaoListAdapter zhifubaoListAdapter;
    private UnionListAdapter unionListAdapter;
    private ImageView titleBack;
    private EditText ed_applyformoney;
    private String applybankid, applybankid_union, recordbankid_zhifubao, recordbankid_union;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        setContentView(R.layout.activity_applyforcommission);
        initView();
        initLinstener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zhifubaolist.clear();
        unionlist.clear();
        getData();
    }

    private void initData() {
        zhifubaolist = new ArrayList<>();
        unionlist = new ArrayList<>();
        zhifubaoListAdapter = new ZhifubaoListAdapter(this, zhifubaolist);
        listView_zhifubao.setAdapter(zhifubaoListAdapter);
        unionListAdapter = new UnionListAdapter(this, unionlist);
        listView_union.setAdapter(unionListAdapter);
    }

    private void getData() {
        String url = Constants2.BASE_URL + "act=commission&op=applyStepOne&tokenKey=" + ((MyApplication) getApplication()).tokenKey;
        showDialog();
        VolleyRequest.GetRequest(ApplyForWithdrawActivity.this, url, new HttpRequestCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONObject data = jsonObject.getJSONObject("data");
                    applyformoney_text.setText(data.getString("validcomm"));
                    JSONArray zhifubaoArray = data.getJSONArray("zfb");
                    if (zhifubaoArray.length() > 0) {
                        for (int i = 0; i < zhifubaoArray.length(); i++) {
                            Zhifubao zhifubao = new Zhifubao();
                            zhifubao.setBank_id(zhifubaoArray.getJSONObject(i).getString("bank_id"));
                            zhifubao.setAccount_name(zhifubaoArray.getJSONObject(i).getString("account_name"));
                            zhifubao.setBank_cart(zhifubaoArray.getJSONObject(i).getString("bank_cart"));
                            zhifubaolist.add(zhifubao);
                        }
                    }
                    JSONArray unionArray = data.getJSONArray("yhk");
                    if (unionArray.length() > 0) {
                        for (int i = 0; i < unionArray.length(); i++) {
                            Union union = new Union();
                            union.setBank_id(unionArray.getJSONObject(i).getString("bank_id"));
                            union.setAccount_name(unionArray.getJSONObject(i).getString("account_name"));
                            union.setBank_cart(unionArray.getJSONObject(i).getString("bank_cart"));
                            union.setBank_name(unionArray.getJSONObject(i).getString("bank_name"));
                            unionlist.add(union);
                        }
                    }
                    zhifubaoListAdapter.notifyDataSetInvalidated();
                    unionListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    hideDialog();
                }

            }

            @Override
            public void onFailure(String fail) {
                hideDialog();
                Toast.makeText(ApplyForWithdrawActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initLinstener() {
        ck_applyforzhifubao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listView_zhifubao.setVisibility(View.VISIBLE);
                    listView_union.setVisibility(View.GONE);
                    ck_applyforunion.setChecked(false);
                    ck_applyforzhifubao.setChecked(true);
                    applybankid = recordbankid_zhifubao;
                } else {
                    listView_zhifubao.setVisibility(View.GONE);
                    applybankid = null;
                }
            }
        });


        ck_applyforunion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listView_zhifubao.setVisibility(View.GONE);
                    listView_union.setVisibility(View.VISIBLE);
                    ck_applyforzhifubao.setChecked(false);
                    ck_applyforunion.setChecked(true);
                    applybankid = recordbankid_union;

                } else {
                    listView_union.setVisibility(View.GONE);
                    applybankid = null;
                }
            }
        });


        ck_applyforunion.setOnClickListener(this);
        applyforcommission_btn.setOnClickListener(this);
        zhifubao_layout.setOnClickListener(this);
        union_layout.setOnClickListener(this);
        titleBack.setOnClickListener(this);
        listView_zhifubao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > zhifubaolist.size() - 1) {
                    view.findViewById(R.id.alipay_select_indicator).setVisibility(View.INVISIBLE);
                    // 添加账号
                    Intent intent = new Intent(ApplyForWithdrawActivity.this, AddAlipayAccountActivity.class);
                    startActivity(intent);
                } else {
                    applybankid = zhifubaolist.get(position).getBank_id();
                    recordbankid_zhifubao = applybankid;
                    view.findViewById(R.id.alipay_select_indicator).setVisibility(View.VISIBLE);
                }
            }
        });
        listView_union.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > unionlist.size() - 1) {
                    view.findViewById(R.id.bank_select_indicator).setVisibility(View.INVISIBLE);
                    // 添加账号
                    Intent intent = new Intent(ApplyForWithdrawActivity.this, AddBankAccountActivity.class);
                    startActivity(intent);
                } else {
                    applybankid = unionlist.get(position).getBank_id();
                    recordbankid_union = applybankid;
                    view.findViewById(R.id.bank_select_indicator).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initView() {
        applyformoney_text = (TextView) findViewById(R.id.applyformoney_text);
        ck_applyforzhifubao = (CheckBox) findViewById(R.id.ck_applyforzhifubao);
        ck_applyforunion = (CheckBox) findViewById(R.id.ck_applyforunion);
        listView_zhifubao = (ListView) findViewById(R.id.listView_zhifubao);
        listView_union = (ListView) findViewById(R.id.listView_union);
        applyforcommission_btn = (Button) findViewById(R.id.applyforcommission_btn);
        zhifubao_layout = (LinearLayout) findViewById(R.id.zhifubao_layout);
        union_layout = (LinearLayout) findViewById(R.id.union_layout);
        titleBack = (ImageView) findViewById(R.id.title_back);
        TextView titleName = (TextView) findViewById(R.id.title_name);
        ed_applyformoney = (EditText) findViewById(R.id.ed_applyformoney);
        listView_zhifubao.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//
        listView_union.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//


        titleBack.setVisibility(View.VISIBLE);
        titleName.setText("申请提现");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:// 返回
                finish();
                break;

            case R.id.applyforcommission_btn:// tixian


                if (ed_applyformoney.getText().toString().equals("") || Integer.valueOf(ed_applyformoney.getText().toString()) <= 0
                        || Integer.valueOf(ed_applyformoney.getText().toString()) > Integer.valueOf(applyformoney_text.getText().toString())) {
                    Toast.makeText(this, "提现金额有误 请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    applyforcommission();
                }


                break;
            case R.id.zhifubao_layout:// 支付宝layout
                listView_zhifubao.setVisibility(View.VISIBLE);
                listView_union.setVisibility(View.GONE);
                ck_applyforunion.setChecked(false);
                ck_applyforzhifubao.setChecked(true);

                break;
            case R.id.union_layout:// yinlianlayout
                listView_zhifubao.setVisibility(View.GONE);
                listView_union.setVisibility(View.VISIBLE);
                ck_applyforunion.setChecked(true);
                ck_applyforzhifubao.setChecked(false);
                break;

        }


    }

    private void applyforcommission() {
        showDialog();

        String str = Constants2.BASE_URL + "act=commission&op=applytx&tokenKey=" + Utils.getTokenKey((MyApplication) this.getApplication()) + "&bankId=" + applybankid + "&amount=" + ed_applyformoney.getText().toString();

        VolleyRequest.GetRequest(this, str, new HttpRequestCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {

                try {
                    int error = jsonObject.getInt("error");
                    if (error == 0) {
                        if (jsonObject.getString("message").equals("提交成功")) {
                            Toast.makeText(ApplyForWithdrawActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                            zhifubaolist.clear();
                            unionlist.clear();
                            getData();
                            applyformoney_text.setText("");
                        } else {
                            Toast.makeText(ApplyForWithdrawActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ApplyForWithdrawActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    /*JSONObject data = jsonObject.getJSONObject("data");
                    message = data.getString("message");
                    if (message.trim().equals("提交成功")) {
                        Toast.makeText(ApplyForWithdrawActivity.this, "提交成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ApplyForWithdrawActivity.this, "提交失败", Toast.LENGTH_LONG).show();
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideDialog();
            }

            @Override
            public void onFailure(String fail) {
                Toast.makeText(ApplyForWithdrawActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });

    }

    private LoadingDialog dialog;

    private void showDialog() {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new LoadingDialog(this);
            dialog.show();
        }
    }

    private void hideDialog() {
        dialog.dismiss();
    }

}
