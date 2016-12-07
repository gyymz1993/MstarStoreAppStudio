package cn.mstar.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mstar.store.R;
import cn.mstar.store.alipay.Alipay;
import cn.mstar.store.alipay.Keys;
import cn.mstar.store.alipay.PayResult;
import cn.mstar.store.app.MyAction;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.functionutils.LogUtils;
import cn.mstar.store.interfaces.HttpRequestCallBack;
import cn.mstar.store.utils.CustomToast;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;
import cn.mstar.store.wxapi.Constants;


/**
 * 支付界面
 *
 * @author wenjundu
 */


public class PayActivity extends BaseActivity implements OnClickListener {

    private static final String ALIPAY = "支付宝支付";
    private static final String WEIXINPAY = "微信支付";
    private static final String INSHOPPAY = "到店支付";

    private ListView listView;
    private int[] payDrawables;//支付方式图标
    private String[] payNames;//支付方式 名称
    private Button btnConfirm;//确定按钮
    private ImageView titleBack;//返回按钮
    private TextView titleName;//标题
    //private ImageView productIV;//产品头像
    //private TextView productNameTV,productNormTV,productPriceTV,productNumTV;//产品名称 参数 价格 数量
    //private Product product;
    //private String norm="";
    //private int productNum;//数量
    private TextView priceTV;
    private Double totalPrice;
    private int paymentId;
    private Double price;
    private String orderId;
    private String proDesc;
    private String proName;
    private String out_trade_no;
    private String url;
    private String prepay_id;

    private JSONObject wechatPayJSONObject;
    IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        msgApi.registerApp(Constants.APP_ID);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        initView();
        paymentId = getIntent().getIntExtra("paymentId", 2);
        price = getIntent().getDoubleExtra("totalPrices", 0);
        orderId = getIntent().getStringExtra("orderid");
        out_trade_no = getIntent().getStringExtra("out_trade_no");
        proName = getIntent().getStringExtra("pname");
        proDesc = getIntent().getStringExtra("pdesc");
        if (proName == null)
            proName = "";
        if (proDesc == null)
            proDesc = "";
        // i need the product information and description
        L.d("pay:::", proDesc + "  " + proName);

        initData();


    }


    private void initData() {
        // TODO Auto-generated method stub
        payDrawables = new int[]{R.drawable.alipay, R.drawable.wechat_pay};
        payNames = new String[]{getResources().getString(R.string.zhifubao_pay),getResources().getString(R.string.wechat_pay)};
        SimpleAdapter adapter = new SimpleAdapter(this, getListViewData(),
                R.layout.item_pay_mode, new String[]{"img", "info"},
                new int[]{R.id.image, R.id.info});
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//开启单选模式
        //默认选择第一行
        listView.setItemChecked(0, true);
        Intent intent = getIntent();
        if (MyAction.confirmIndentActivityAction.equals(intent.getAction())) {
            totalPrice = intent.getExtras().getDouble("totalPrices");
        }
        priceTV.setText((Html.fromHtml(getString((R.string.pay_tips)) + "<font color='#ff0000'>" + getString(R.string.renminbi) + totalPrice + "</font>")));

        int total_fee;
        total_fee = (int) (price * 100);
        //http://www.fanershop.com/mobile/api/payment/wxpay/redirect_uri.php?out_trade_no=580498406208790014&total_fee=251000
        //url="http://www.fanerjewelry.com/mobile/api/payment/wxpay/redirect_uri.php?out_trade_no="+out_trade_no+"&total_fee="+total_fee;
        url = NewLink.PAY_FOR_WEIXIN + "out_trade_no=" + out_trade_no + "&total_fee=" + total_fee;
        LogUtils.e("请求支付路径" + url);
        VolleyRequest.GetRequest(this, url, new HttpRequestCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    wechatPayJSONObject = jsonObject.getJSONObject("data");
                    prepay_id = wechatPayJSONObject.getString("prepay_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String fail) {

            }
        });


    }

    private void initView() {
        listView = (ListView) findViewById(R.id.pay_mode_list);
        btnConfirm = (Button) findViewById(R.id.btn_confirm_pay);
        titleBack = (ImageView) findViewById(R.id.title_back);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText(getString(R.string.pay));
        priceTV = (TextView) findViewById(R.id.price_tv);

        titleBack.setVisibility(View.VISIBLE);
        titleBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    private List<Map<String, Object>> getListViewData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < payDrawables.length; i++) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", payDrawables[i]);
            map.put("info", payNames[i]);
            list.add(map);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_confirm_pay:             //确认支付
                // INVALID_POSITION 代表无效的位置。有效值的范围是 0 到当前适配器项目数减 1 。
                int select = listView.getCheckedItemPosition();
                if (ListView.INVALID_POSITION != select) {
                    String type = payNames[select];
                    switch (type) {
                        case ALIPAY: //支付宝支付
                            alipay();
                            break;
                        case WEIXINPAY: //微信支付
                            weiXinPay();
                            break;
                        case INSHOPPAY: //到店支付
                            inShopPay();
                            break;
                    }
                }
                break;

            case R.id.title_back:
                finish();
                break;
        }
    }

    /**
     * alipay
     */
    private void alipay() {
        new Alipay(PayActivity.this, mHandler, totalPrice, proName, proDesc, out_trade_no).pay();
    }

    /**
     * weinxinpay
     */
    private void weiXinPay() {
        PayReq req = new PayReq();
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = wechatPayJSONObject.optString("prepayid");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = wechatPayJSONObject.optString("noncestr");
        req.timeStamp = wechatPayJSONObject.optString("timestamp");
        req.sign = wechatPayJSONObject.optString("sign");
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }

    /**
     * go to shop
     */
    private void inShopPay() {
        Toast.makeText(PayActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        MyApplication.requestQueue.cancelAll(this);
        super.onDestroy();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Keys.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {

                        startActivity(new Intent(PayActivity.this, PayingTransactionSuccessActivity.class));
                        CustomToast.makeToast(PayActivity.this, "支付成功", Toast.LENGTH_SHORT);

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            CustomToast.makeToast(PayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT);

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            CustomToast.makeToast(PayActivity.this, "支付未完成", Toast.LENGTH_SHORT);

                        }
                    }
                    break;
                }
                case Keys.SDK_CHECK_FLAG: {

                    CustomToast.makeToast(PayActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT);
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void startActivity(Intent intent) {
    /*	Intent intent1 = new Intent(this, PayingTransactionSuccessActivity.class);
        intent1.putExtra("price", price);
		intent1.putExtra("orderid", orderId);*/
        intent.putExtra("price", price);
        intent.putExtra("orderid", orderId);
        super.startActivity(intent);
        finish();
        overridePendingTransition(R.anim.custom_in_anim, R.anim.custom_out_anim);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        /*Intent intent1 = new Intent(this, PayingTransactionSuccessActivity.class);
        intent1.putExtra("price", price);
		intent1.putExtra("orderid", orderId);*/
        intent.putExtra("price", price);
        intent.putExtra("orderid", orderId);
        super.startActivityForResult(intent, requestCode);
        finish();
        overridePendingTransition(R.anim.custom_in_anim, R.anim.custom_out_anim);
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

//	/**
//	 * call alipay sdk pay. 调用SDK支付
//	 *
//	 */
//	public void pay() {
//		// 订单
//		String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
//
//		// 对订单做RSA 签名
//		String sign = sign(orderInfo);
//
//		try {
//			// 仅需对sign 做URL编码
//			sign = URLEncoder.encode(sign, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//
//		// 完整的符合支付宝参数规范的订单信息
//		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
//				+ getSignType();
//
//		Runnable payRunnable = new Runnable() {
//
//			@Override
//			public void run() {
//				// 构造PayTask 对象
//				PayTask alipay = new PayTask(PayActivity.this);
//				// 调用支付接口，获取支付结果
//				String result = alipay.pay(payInfo);
//
//				Message msg = new Message();
//				msg.what = Keys.SDK_PAY_FLAG;
//				msg.obj = result;
//				mHandler.sendMessage(msg);
//			}
//		};
//
//		// 必须异步调用
//		Thread payThread = new Thread(payRunnable);
//		payThread.start();
//	}
//	/**
//	 * create the order info. 创建订单信息
//	 *
//	 */
//	public String getOrderInfo(String subject, String body, String price) {
//		// 签约合作者身份ID
//		String orderInfo = "partner=" + "\"" + Keys.PARTNER + "\"";
//
//		// 签约卖家支付宝账号
//		orderInfo += "&seller_id=" + "\"" + Keys.SELLER + "\"";
//
//		// 商户网站唯一订单号
//		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
//
//		// 商品名称
//		orderInfo += "&subject=" + "\"" + subject + "\"";
//
//		// 商品详情
//		orderInfo += "&body=" + "\"" + body + "\"";
//
//		// 商品金额
//		orderInfo += "&total_fee=" + "\"" + price + "\"";
//
//		// 服务器异步通知页面路径
//		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
//				+ "\"";
//
//		// 服务接口名称， 固定值
//		orderInfo += "&service=\"mobile.securitypay.pay\"";
//
//		// 支付类型， 固定值
//		orderInfo += "&payment_type=\"1\"";
//
//		// 参数编码， 固定值
//		orderInfo += "&_input_charset=\"utf-8\"";
//
//		// 设置未付款交易的超时时间
//		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
//		// 取值范围：1m～15d。
//		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
//		// 该参数数值不接受小数点，如1.5h，可转换为90m。
//		orderInfo += "&it_b_pay=\"30m\"";
//
//		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
//
//		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//		orderInfo += "&return_url=\"m.alipay.com\"";
//
//		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
//		// orderInfo += "&paymethod=\"expressGateway\"";
//
//		return orderInfo;
//	}
//	/**
//	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
//	 *
//	 */
//	public String getOutTradeNo() {
//		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
//				Locale.getDefault());
//		Date date = new Date();
//		String key = format.format(date);
//
//		Random r = new Random();
//		key = key + r.nextInt();
//		key = key.substring(0, 15);
//		return key;
//	}
//	/**
//	 * sign the order info. 对订单信息进行签名
//	 *
//	 * @param content
//	 *            待签名订单信息
//	 */
//	public String sign(String content) {
//		return SignUtils.sign(content, Keys.RSA_PRIVATE);
//	}
//
//	/**
//	 * get the sign type we use. 获取签名方式
//	 *
//	 */
//	public String getSignType() {
//		return "sign_type=\"RSA\"";
//	}


}
