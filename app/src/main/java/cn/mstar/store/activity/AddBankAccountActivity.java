package cn.mstar.store.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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
import cn.mstar.store.functionutils.BankUtils;
import cn.mstar.store.functionutils.URLtoUTF8Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class AddBankAccountActivity extends BaseActivity {
    private EditText cartNum;
    private EditText userName;
    private EditText bankName;
    private Button save;

    private String getBankName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank_account_layout);
        initView();
        initData();
    }

    private void initView() {
        initHeadView();
        cartNum = (EditText) findViewById(R.id.card_number);
        cartNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length()==6){
                    getBankName = BankUtils.getNameOfBank(editable.toString());
                    bankName.setText(getBankName);
                    bankName.setEnabled(false);
                }
                if (editable.toString().length()<6){
                    getBankName="";
                    bankName.setText(getBankName);
                }

            }
        });
        userName = (EditText) findViewById(R.id.use_name);
        bankName = (EditText) findViewById(R.id.spinner);
        save = (Button) findViewById(R.id.add_bank_save);
    } 

    private void initHeadView() {
        ImageView back = (ImageView) findViewById(R.id.title_back);
        TextView title = (TextView) findViewById(R.id.title_name);
        back.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText("添加银行账号");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bankNameArr);
//        bankName.setAdapter(adapter);
//        bankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                getBankName = parent.getItemAtPosition(position).toString();
//            }
//47


//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String banknum = cartNum.getText().toString();
                String regex = "^\\d{16}|\\d{19}$";
                if (!banknum.matches(regex)) {
                    Toast.makeText(AddBankAccountActivity.this, Html.fromHtml("<font color=\"red\">银行账号输入不正确！</font>"), Toast.LENGTH_SHORT).show();
                    return;
                }
                String user = userName.getText().toString();
                if ("".equals(user.trim())) {
                    Toast.makeText(AddBankAccountActivity.this, Html.fromHtml("<font color=\"red\">账户名输入不正确！</font>"), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (getBankName.equals("选择开户行")||getBankName.equals("")) {
                    Toast.makeText(AddBankAccountActivity.this, Html.fromHtml("<font color=\"red\">请选择开户行！</font>"), Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = Constants2.BASE_URL + "act=commission&op=addbank" + "&tokenKey=" + ((MyApplication) getApplication()).tokenKey + "&bankType=" + 2
                        + "&bankCart=" + URLtoUTF8Utils.toUtf8String(banknum) + "&accountName=" + user + "&bankName=" + URLtoUTF8Utils.toUtf8String(getBankName);
                showDialog();
                VolleyRequest.GetCookieRequest(AddBankAccountActivity.this, url, new VolleyRequest.HttpStringRequsetCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                        hideDialog();
                        if ("ok".equals(message)) {
                            Toast.makeText(AddBankAccountActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddBankAccountActivity.this, "添加失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(String error) {
                        hideDialog();
                        Toast.makeText(AddBankAccountActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
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
