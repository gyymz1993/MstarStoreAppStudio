package cn.mstar.store.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.mstar.store.R;
import cn.mstar.store.adapter.BuyProductListAdapter;
import cn.mstar.store.adapter.BuyProductListAdapter.OnPlusMinusListener;
import cn.mstar.store.adapter.BuyProductListAdapter.ViewHolder;
import cn.mstar.store.app.Constants2;
import cn.mstar.store.app.MyAction;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.customviews.PayModePopup;
import cn.mstar.store.customviews.SendModePopup;
import cn.mstar.store.entity.BuyProductEntity;
import cn.mstar.store.entity.OrderInfo;
import cn.mstar.store.entity.PayType;
import cn.mstar.store.entity.Product;
import cn.mstar.store.entity.ReceiverAddress;
import cn.mstar.store.entity.SelectCouponEntity;
import cn.mstar.store.entity.SendType;
import cn.mstar.store.entity.ShoppingCartItem;
import cn.mstar.store.functionutils.URLtoUTF8Utils;
import cn.mstar.store.utils.CustomToast;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * 确认订单页
 *
 * @author wenjundu
 */
public class ConfirmIndentActivity extends BaseActivity implements OnClickListener,
        PayModePopup.OnDialogListener, SendModePopup.OnDialogListener, OnPlusMinusListener {

    private ListView listview; // 购买产品列表
    private SendModePopup pop;// 选择在线支付 和 货到付款的 pop
    private PayModePopup ppop;
    private TextView payModeTV;//配送方式
    private TextView sendModeTV;// 支付方式
    private BuyProductListAdapter adapter;//购买商品列表adapter
    private List<BuyProductEntity> buylist;//购买产品list
    private List<SendType> sendTypeList;//配送方式
    private List<PayType> payTypelist;//支付方式
    private TextView totalTV;//显示总价
    private Button confirmBtn;//确定按钮
    private RelativeLayout recieverAddressLayout;//收货地址layout
    private LinearLayout addRecieverAddressLayout;//添加收货地址layout
    private RelativeLayout get_in_shop;//get by youself
    private TextView shopName;//returned shop namep
    private OrderInfo orderInfo;
    private ReceiverAddress receiverAddress;//收货地址类
    private TextView recieverTV, phoneTV, recieverAddressTV;//收货人，电话，收货地址
    private RelativeLayout select_coupon; //使用代金券
    private LinearLayout needInvoiceOrNot;
    private CheckBox writeInvoice;
    private TextView use_coupon_num;
    private TextView use_coupon_info;
    private ImageView titleBack;//返回
    private TextView titleName;//标题
    private String orderShowNowURL = "";
    private SendType sendType;//配送方式
    private PayType payType;//支付方式
    private String token;
    private List<BuyProductEntity> cartlist;//购物篮产品list
    private MyApplication app;
    private String orderId;
    private Double price = .0;
    private List<ShoppingCartItem> cartItemlist;
    private SelectCouponEntity entity;
    private int temp = 0;
    private String invoice_info_txt = "";
    private Dialog alertDialog;
    private TextView invoiceName;
    private TextView tips_txt;

    private String store_id;
    private int position = -1;

    private double totalAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_indent1);
        app = (MyApplication) getApplication();
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        initSendType();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initSendType() {
        sendTypeList = new ArrayList<>();
        String[] strs = getResources().getStringArray(R.array.sendType_arr);
        for (int i = 0; i < strs.length; i++) {
            SendType type = new SendType();
            type.setId(i + 1);
            type.setSendType(strs[i]);
            sendTypeList.add(type);
        }
        sendType = sendTypeList.get(0);
    }

    private void initData() {
        // TODO Auto-generated method stub
        token = app.tokenKey;
        buylist = new ArrayList<>();
        adapter = new BuyProductListAdapter(this, buylist);
        adapter.setOnPlusMinusListener(this);
        listview.setAdapter(adapter);
        payTypelist = new ArrayList<PayType>();
        //模拟数据

//		for(int i=0;i<20;i++){
//			BuyProductEntity entity=new BuyProductEntity();
//			Product product=new Product();
//			product.setName("千禧之星么么哒千禧之星么么哒千禧之星么么哒千禧之星么么哒");
//			product.setPrice(100.0);
//			entity.setProduct(product);
//			entity.setNorms("铂金 31g");
//			entity.setBuyNum(2);
//			buylist.add(entity);
//		}
        if (MyAction.selectCommodityActivityAction.equals(getIntent().getAction())) {//从选择商品传递过来的
            //list.addAll((List<BuyProductEntity>) getIntent().getSerializableExtra("list"));
            BuyProductEntity buyProductEntity = (BuyProductEntity) getIntent().getSerializableExtra("buyProduct");

            orderShowNowURL = Constants2.ORDER_SHOW_NOW + "&proId=" + buyProductEntity.getProduct().getProSpecialID() + "&number=" + buyProductEntity.getBuyNum();
        } else if (MyAction.productDetailsActivityAction.equals(getIntent().getAction())) {//从产品详情页传递过来

            BuyProductEntity buyProductEntity = (BuyProductEntity) getIntent().getSerializableExtra("buyProduct");
            if (buyProductEntity.getProduct().isHaveProSpecificationPrice()) {
                orderShowNowURL = Constants2.ORDER_SHOW_NOW + "&proId=" + buyProductEntity.getProduct().getProSpecialID() + "&number=" + buyProductEntity.getBuyNum();
            } else {
                orderShowNowURL = Constants2.ORDER_SHOW_NOW + "&proId=" + buyProductEntity.getProduct().getProId() + "&number=" + buyProductEntity.getBuyNum();
            }
        } else if (MyAction.goPayAction.equals(getIntent().getAction())) {//从购物篮传递过来的
            cartItemlist = (List<ShoppingCartItem>) getIntent().getSerializableExtra("outsideCheckedGoods");
            L.e("cartItemlistSize:" + cartItemlist.size());
            cartlist = new ArrayList<BuyProductEntity>();
            for (ShoppingCartItem cartItem : cartItemlist) {
                BuyProductEntity buyProductEntity = new BuyProductEntity();
                Product product = new Product();
                product.setClassName(cartItem.ClassName);
                product.setPrice(cartItem.price);
                product.setName(cartItem.name);
                product.setImageUrl(cartItem.pic);
                product.setParentClassName(cartItem.ParentClassName);
                product.setProId(cartItem.proId);
                product.setStock(cartItem.stock);
                product.setProSpecialID(cartItem.proSpecialId);
                buyProductEntity.setProduct(product);
                buyProductEntity.setBuyNum(cartItem.number);
                buyProductEntity.setCartId(cartItem.cartId);
                cartlist.add(buyProductEntity);
            }
            String numbers = "";//number集合
            String proIds = "";//proId集合
            L.e("cartlistSize:" + cartlist.size());
            for (int i = 0; i < cartlist.size(); i++) {
                BuyProductEntity entity = cartlist.get(i);
                if (i == 0) {
                    numbers = "" + entity.getBuyNum();
                    if (entity.getProduct().getProSpecialID() == 0)//没有规格id
                        proIds = "" + entity.getProduct().getProId();
                    else
                        proIds = "" + entity.getProduct().getProSpecialID();
                } else {
                    numbers += "|" + entity.getBuyNum();
                    if (entity.getProduct().getProSpecialID() == 0)
                        proIds += "|" + entity.getProduct().getProId();
                    else
                        proIds += "|" + entity.getProduct().getProSpecialID();
                }
            }
            orderShowNowURL = Constants2.CART_SHOW_NOW + "&proId=" + proIds + "&proId_number=" + numbers;
        }

//		//是否登录
        if (!token.equals("")) {
            orderShowNowURL = orderShowNowURL + "&key=" + token;
            getindentInfo(orderShowNowURL);
        } else {
            Intent intent = new Intent(ConfirmIndentActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1);
        }
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


    //获取订单信息
    private void getindentInfo(final String url) {
        L.e("获取订单URL：" + url);
        i_showProgressDialog();
        VolleyRequest.GetCookieRequest(this, url, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e("result:::" + result);
                try {
                /*
                * get order id and price
				* */
                    if (new JSONObject(result).optString("error").equals("0")) {
                        JSONObject data = (new JSONObject(result)).getJSONObject("data");
                        JSONObject jsonOrderInfo = data.optJSONObject("orderInfo");
                        if (jsonOrderInfo != null) {
                            orderInfo = new OrderInfo();
                            orderInfo.shopId = jsonOrderInfo.optString("shopId");
                            orderInfo.ifvoucher = jsonOrderInfo.optString("ifvoucher");
                            orderInfo.voucherNum = jsonOrderInfo.optString("voucherNum");
                            orderInfo.userAccount = jsonOrderInfo.optString("userAccount");
                        }
                        /*if (orderInfo != null) {
                            use_coupon_num.setText(orderInfo.optString("voucherNum") + "张可用");
                        }*/
                        JSONObject postInfo = data.optJSONObject("postInfo");
                        if (postInfo != null) {
                            receiverAddress = new ReceiverAddress();
                            receiverAddress.setName(postInfo.optString("postName"));
                            String mobile = postInfo.optString("mobile");
                            if (!"".equals(mobile) && mobile.length() > "18229998073".length())
                                mobile = mobile.substring(0, "18229998073".length());
                            receiverAddress.setPhone(mobile);
                            receiverAddress.setFullPostAddress(postInfo.optString("fullPostAddress"));
                            receiverAddress.setAddressId(postInfo.optInt("addressId"));
                        }
                        showReceiverAddress();
                        JSONArray buyPros = data.getJSONArray("buyPros");
                        for (int i = 0; i < buyPros.length(); i++) {

                            JSONObject buyPro = buyPros.getJSONObject(i);
                            BuyProductEntity buy = new BuyProductEntity();
                            Product product = new Product();
                            buy.setBuyNum(buyPro.getInt("number"));
                            buy.setNorms(buyPro.getString("specialTitle"));
                            //cartId 传过来为空
                            //buy.setCartId(buyPro.getInt("cartId"));
                            product.setName(buyPro.getString("name"));
                            product.setImageUrl(buyPro.getString("pic"));
                            product.setPrice(buyPro.getDouble("price"));
                            product.setmAccount(buyPro.getDouble("mAccount"));
                            product.setProId(buyPro.getInt("proId"));
                            product.setProSpecialID(buyPro.getInt("proSpecialId"));
                            product.setParentClassName(buyPro.getString("ParentClassName"));
                            product.setClassName(buyPro.getString("ClassName"));
                            product.setStock(buyPro.optInt("maxNumber"));
                            buy.setProduct(product);
                            buylist.add(buy);
                            //buylist.clear();
                        }

                        JSONArray payTypes = data.getJSONArray("payType");
                        for (int i = 0; i < payTypes.length(); i++) {
                            if (i == 3 || i == 4) {
                                JSONObject payType = payTypes.getJSONObject(i);
                                PayType pay = new PayType();
                                pay.setTypeId(payType.getInt("Typeid"));
                                pay.setTypeName(payType.getString("TypeName"));
                                payTypelist.add(pay);
                            }
                        }
                        confirmBtn.setClickable(true);

                        adapter.notifyDataSetChanged();
                        //默认顺丰快递
                        sendModeTV.setText(sendTypeList.get(0).getSendType());
                        //默认在线支付
                        payType = new PayType(7, "在线支付");
                        payModeTV.setText(payType.getTypeName());
                        //计算总价
                        totalTV.setText("" + Utils.formatedPrice(totalPrice()));
                        tips_txt.setText("" + Utils.formatedPrice(totalPrice()));
                    } else {
                        String message = new JSONObject(result).optString("message");
                        CustomToast.makeToast(ConfirmIndentActivity.this, message, Toast.LENGTH_SHORT);
                        if (message.equals(getString(R.string.outofstock_))) {
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    L.e(e.toString());
                }
                initParams();
                i_dismissProgressDialog();
            }

            @Override
            public void onFail(String error) {
                failure();
            }
        });
    }

    String numbers = "";//number集合
    String proIds = "";//proId集合

    private void initParams() {
        for (int i = 0; i < buylist.size(); i++) {
            BuyProductEntity entity = buylist.get(i);
            if (i == 0) {
                numbers = "" + entity.getBuyNum();
                proIds = "" + entity.getProduct().getProId();
            } else {
                numbers += "|" + entity.getBuyNum();
                proIds += "|" + entity.getProduct().getProId();
            }
        }
    }

    //提交订单
    private void postIndents() {


        String postUrl = "";
        if (MyAction.goPayAction.equals(getIntent().getAction())) {//购物篮传过来，提交订单
            postUrl = Constants2.POST_CART_INDENT_URL + "&proId=" + proIds + "&proId_number=" + numbers + "&addressId=" + receiverAddress.getAddressId() +
                    "&paymentId=" + payType.getTypeId() + "&transpway=" + sendType.getId() + "&store_id=" + store_id + "&key=" + token;
            if (cartItemlist != null) {
                postUrl += "&cart_id=" + "";
                int i = 0;
                for (ShoppingCartItem cart : cartItemlist) {
                    if (i != 0) postUrl += "|";
                    postUrl += cart.cartId;
                    i++;
                }
                app.frg_isFrg_shoppingcart_needUpdate = true;
            }
        } else {
            postUrl = Constants2.POST_INDENT_URL + "&proId=" + proIds + "&number=" + numbers + "&addressId=" + receiverAddress.getAddressId() +
                    "&paymentId=" + payType.getTypeId() + "&transpway=" + sendType.getId() + "&store_id=" + store_id + "&key=" + token;
        }
        if (writeInvoice.isChecked()) {
            postUrl = postUrl + "&invoice_info=" + URLtoUTF8Utils.toUtf8String(invoice_info_txt);
        }
        if (entity != null) {
            postUrl = postUrl + "&voucher_price=" + entity.getPrice() + "&voucher_code=" + entity.getCode();
        }
        L.e("postUrl:" + postUrl);
        i_showProgressDialog();
        VolleyRequest.GetCookieRequest(this, postUrl, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e("result:" + result);
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    if (jsonResult.optString("error").equals("0")) {
                        //cartlist.clear();
                        CustomToast.makeToast(ConfirmIndentActivity.this, jsonResult.optString("message"), Toast.LENGTH_SHORT);
                        if (payType.getTypeId() == 7) {
                            orderId = jsonResult.getJSONObject("data").optString("orderId");
                            price = jsonResult.getJSONObject("data").optDouble("total_fee");
                            String out_trade_no = jsonResult.getJSONObject("data").optString("out_trade_no");
                            Double totalPrices = Double.parseDouble(totalTV.getText().toString());
                            Intent intent = new Intent(ConfirmIndentActivity.this, PayActivity.class);
                            String proName = jsonResult.getJSONObject("data").optString("proName");
                            String proDesc = jsonResult.getJSONObject("data").optString("probody");
                            intent.setAction(MyAction.confirmIndentActivityAction);
                            intent.putExtra("paymentId", payType.getTypeId());
                            intent.putExtra("totalPrices", totalPrices);
                            intent.putExtra("orderid", orderId); // me
                            intent.putExtra("out_trade_no", out_trade_no); // me
                            intent.putExtra("pname", proName);
                            intent.putExtra("pdesc", proDesc);
                            // add product description and title.

                            startActivity(intent);
                            finish();
                        } else if (payType.getTypeId() == 6) {
                            finish();
                        }
                    } else {
                        //从购物车创建
                        /*if (MyAction.goPayAction.equals(getIntent().getAction())) {
                            cartlist.clear();
                        }*/
                        String message = new JSONObject(result).optString("message");
                        CustomToast.makeToast(ConfirmIndentActivity.this, message, Toast.LENGTH_SHORT);
                        /*if (message.equals(getString(R.string.outofstock_))) {
                            finish();
                        }*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    failure();
                }
                i_dismissProgressDialog();
            }

            @Override
            public void onFail(String error) {
                i_dismissProgressDialog();
                failure();
            }
        });
    }

    private void failure() {
        CustomToast.makeToast(ConfirmIndentActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT);
        finish();
    }


    private void initView() {
        // TODO Auto-generated method stub
        listview = (ListView) findViewById(R.id.listview);

        View headerView = LayoutInflater.from(this).inflate(R.layout.confirm_indent_headview, null, true);
        View footerView = LayoutInflater.from(this).inflate(R.layout.confirm_indent_footer, null, true);
        listview.addHeaderView(headerView);
        listview.addFooterView(footerView);
        //禁止底部出现分割线
        listview.setFooterDividersEnabled(false);

        payModeTV = (TextView) findViewById(R.id.select_pay_mode);
        sendModeTV = (TextView) findViewById(R.id.select_send_mode);
        totalTV = (TextView) findViewById(R.id.tv_total_money);
        confirmBtn = (Button) findViewById(R.id.btn_confirm_order);

        recieverTV = (TextView) findViewById(R.id.receiver_tv);
        phoneTV = (TextView) findViewById(R.id.receiver_phone_tv);
        recieverAddressTV = (TextView) findViewById(R.id.receives_an_address_tv);
        recieverAddressLayout = (RelativeLayout) findViewById(R.id.reciever_address_layout);
        get_in_shop = (RelativeLayout) findViewById(R.id.get_in_shop);
        shopName = (TextView) findViewById(R.id.shop_name);
        addRecieverAddressLayout = (LinearLayout) findViewById(R.id.add_address_layout);
        select_coupon = (RelativeLayout) findViewById(R.id.use_coupon_layout);
        use_coupon_info = (TextView) findViewById(R.id.remain_coupon_tv);
        needInvoiceOrNot = (LinearLayout) findViewById(R.id.need_invoice_or_not);
        writeInvoice = (CheckBox) findViewById(R.id.is_need_invoice_cb);
        invoiceName = (TextView) findViewById(R.id.invoice_name);
        tips_txt = (TextView) findViewById(R.id.tips_tv);
        addRecieverAddressLayout.setOnClickListener(this);
        get_in_shop.setOnClickListener(this);
        select_coupon.setOnClickListener(this);
        needInvoiceOrNot.setOnClickListener(this);
        titleBack = (ImageView) findViewById(R.id.title_back);
        titleBack.setVisibility(View.VISIBLE);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText(getString(R.string.confirm_indent));
        titleBack.setOnClickListener(this);
        recieverAddressLayout.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        confirmBtn.setClickable(false);
        sendModeTV.setOnClickListener(this);
        payModeTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (v.getId()) {
            case R.id.select_send_mode://选择配送方式
                showPopupWindow();
                break;
            case R.id.select_pay_mode:
                List<PayType> list = new ArrayList<>();
                if (sendType.getId() == 1) {
                    list.add(payTypelist.get(1));
                    showPopupWindow(list);
                } else if (sendType.getId() == 2) {
                    list.add(payTypelist.get(1));
                    list.add(payTypelist.get(0));
                    showPopupWindow(list);
                }
                break;
            case R.id.btn_confirm_order://确定
                L.e(receiverAddress + "");
                L.e(receiverAddress.isNull() + "");
                L.e(receiverAddress.toString());
                //if (sendType.getId() != 2)
                if (receiverAddress == null || receiverAddress.isNull()) {
                    CustomToast.makeToast(this, "请添加收货地址", Toast.LENGTH_SHORT);
                    return;
                }
               /* if (payType.isNull()) {
                    CustomToast.makeToast(this, "请选择配送方式", Toast.LENGTH_SHORT);
                    return;
                }*/
                //提交订单
                postIndents();
                break;
            case R.id.reciever_address_layout://收货地址
                intent = new Intent(this, ShippingAddressActivity.class);
                intent.setAction("fromConfirmIndents");
                startActivityForResult(intent, 0);
                break;
            case R.id.title_back://返回
                finish();
                break;
            case R.id.add_address_layout://添加收货地址
                /*intent = new Intent(this, CreateReceiverAddressActivity.class);
                intent.setAction(MyAction.confirmIndentActivityAction);
                startActivityForResult(intent, 2);*/
                intent = new Intent(this, ShippingAddressActivity.class);
                intent.setAction("fromConfirmIndents");
                startActivityForResult(intent, 0);
                break;
            case R.id.get_in_shop:
                intent = new Intent(this, GetInShopActivity.class);
                intent.putExtra("proId", proIds);
                intent.putExtra("proId_number", numbers);
                intent.putExtra("position", position);
                startActivityForResult(intent, 11);
                break;
            case R.id.use_coupon_layout:
                intent = new Intent(this, SelectCouponActivity.class);
                intent.putExtra("shopId", orderInfo.shopId);
                intent.putExtra("totalPrice", totalPrice() + "");
                intent.putExtra("userAccount", totalAccount + "");
                startActivityForResult(intent, 3);
                break;
            case R.id.need_invoice_or_not:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        alertDialog = new Dialog(ConfirmIndentActivity.this);
        //alertDialog.setCancelable(false);
        //alertDialog.setOnKeyListener(keyListener);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.invoice_dialog);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.findViewById(R.id.invoice_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String invoice_str = ((TextView) alertDialog.findViewById(R.id.invoice_info)).getText().toString().trim();
                if ("".equals(invoice_str)) {
                    Toast.makeText(ConfirmIndentActivity.this, "输入有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                invoice_info_txt = invoice_str;
                alertDialog.dismiss();
                invoiceName.setText(invoice_info_txt);
                writeInvoice.setChecked(true);
            }
        });
        alertDialog.findViewById(R.id.invoice_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                invoiceName.setText("");
                writeInvoice.setChecked(false);
            }
        });
        final EditText invoice_edit = (EditText) alertDialog.findViewById(R.id.invoice_info);
        invoice_edit.setText(invoiceName.getText().toString());
        invoice_edit.setSelectAllOnFocus(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(invoice_edit, 0);
            }
        }, 200);
    }

   /* private DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
        }
    };*/

    // 配送方式
    private void showPopupWindow() {

        pop = new SendModePopup(this, sendTypeList, this);
        L.e("payTypelist:" + payTypelist.size());
        // pop.showAtLocation(sendModeTV, Gravity.RIGHT, 0, 0);
        pop.showAsDropDown(sendModeTV);
    }

    //支付方式
    private void showPopupWindow(List<PayType> list) {
        ppop = new PayModePopup(this, list, this);
        ppop.showAsDropDown(payModeTV);
    }

    @Override
    public void onSendType(SendType sendType) {
        if (sendType.getId() == 1) {//顺丰快递
            this.sendType = sendType;
            sendModeTV.setText(sendType.getSendType());
            payType = payTypelist.get(1);//
            payModeTV.setText(payType.getTypeName());
            store_id = null;
        } else if (sendType.getId() == 2) {//到店自取
            Intent intent = new Intent(this, GetInShopActivity.class);
            intent.putExtra("proId", proIds);
            intent.putExtra("proId_number", numbers);
            intent.putExtra("position", position);
            startActivityForResult(intent, 11);
        }
    }


    @Override
    public void onPayType(PayType payType) {
        this.payType = payType;
        payModeTV.setText(payType.getTypeName());
    }

    //添加回调
    @Override
    public void plus(int position) {
        // TODO Auto-generated method stub
        int visiblestartPos = listview.getFirstVisiblePosition();
        int visibleendPos = listview.getLastVisiblePosition();
        if (position >= visiblestartPos - 1 && position <= visibleendPos - 1) {
            int offset = position - visiblestartPos + 1;
            View view = listview.getChildAt(offset);
            final BuyProductEntity entity = buylist.get(position);
            if (view.getTag() instanceof ViewHolder) {

                ViewHolder holder = (ViewHolder) view.getTag();
                /*if (entity.getBuyNum() < buylist.get(position).getProduct().getStock())*/
                entity.setBuyNum(entity.getBuyNum() + 1);
                /*else {
                    CustomToast.makeToast(this, "已达到最大库存", Toast.LENGTH_SHORT);
                }*/
                holder.productNums.setText("" + entity.getBuyNum());
                holder.TVnums.setText("" + entity.getBuyNum());
//				if(entity.getProduct().getImageUrl()!=null && !"".equals(entity.getProduct().getImageUrl()))
//					ImageLoader.getInstance().displayImage(entity.getProduct().getImageUrl(), holder.productIV, ImageLoadOptions.getOptions());
//				holder.productName.setText(entity.getProduct().getName());
//				holder.productNorms.setText(entity.getNorms());
//				holder.productPrice.setText(getString(R.string.renminbi)+entity.getProduct().getPrice());
                totalTV.setText("" + Utils.formatedPrice(totalPrice()));
                use_coupon_info.setText("未使用");
            }
        }
    }

    //减少回调
    @Override
    public void minus(int position) {
        // TODO Auto-generated method stub
        int visiblestartPos = listview.getFirstVisiblePosition();
        int visibleendPos = listview.getLastVisiblePosition();
        if (position >= visiblestartPos - 1 && position <= visibleendPos - 1) {
            int offset = position - visiblestartPos + 1;
            View view = listview.getChildAt(offset);
            final BuyProductEntity entity = buylist.get(position);
            if (view.getTag() instanceof ViewHolder) {

                ViewHolder holder = (ViewHolder) view.getTag();
                int num = 1;
                if (entity.getBuyNum() > 1)
                    num = entity.getBuyNum() - 1;
                entity.setBuyNum(num);
                holder.productNums.setText("" + num);
                holder.TVnums.setText("" + num);
//				if(entity.getProduct().getImageUrl()!=null && !"".equals(entity.getProduct().getImageUrl()))
//					ImageLoader.getInstance().displayImage(entity.getProduct().getImageUrl(), holder.productIV, ImageLoadOptions.getOptions());
//				else
//					holder.productIV.setBackgroundResource(R.drawable.picture1);
//				holder.productName.setText(entity.getProduct().getName());
//				holder.productNorms.setText(entity.getNorms());
//				holder.productPrice.setText(getString(R.string.renminbi)+entity.getProduct().getPrice());
                totalTV.setText("" + Utils.formatedPrice(totalPrice()));
                use_coupon_info.setText("未使用");
            }
        }
    }

    //计算总价
    private double totalPrice() {
        Double totalPrice = 0.0;
        totalAccount = 0;
        for (BuyProductEntity buyProductEntity : buylist) {
            totalPrice += buyProductEntity.getBuyNum() * buyProductEntity.getProduct().getPrice();
            totalAccount += buyProductEntity.getBuyNum() * buyProductEntity.getProduct().getmAccount();
        }
        return totalPrice;
    }


    //显示收货地址
    private void showReceiverAddress() {
        if (receiverAddress != null) {
            recieverAddressLayout.setVisibility(View.VISIBLE);
            recieverTV.setText(receiverAddress.getName());
            String mobile = receiverAddress.getPhone();
            if (!"".equals(mobile) && mobile.length() > "18229998073".length())
                mobile = mobile.substring(0, "18229998073".length());
            phoneTV.setText(mobile);
            recieverAddressTV.setText(receiverAddress.getFullPostAddress());
        } else {
            recieverAddressLayout.setVisibility(View.GONE);
            addRecieverAddressLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 0 && resultCode == RESULT_OK) {//返回收货地址
            if (data != null) {
                receiverAddress = (ReceiverAddress) data.getSerializableExtra("ReceiverAddress");
                showReceiverAddress();
                if (addRecieverAddressLayout.getVisibility() == View.VISIBLE)
                    addRecieverAddressLayout.setVisibility(View.GONE);
            }
        } else if (requestCode == 1 && resultCode == 2) {//登录成功
//			token=data.getExtras().getString("token");
            token = app.tokenKey;
            orderShowNowURL = orderShowNowURL + "&key=" + token;
            getindentInfo(orderShowNowURL);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {//新建地址传回来的数据

            if (data != null) {
                receiverAddress = (ReceiverAddress) data.getSerializableExtra("ReceiverAddress");
                showReceiverAddress();
                addRecieverAddressLayout.setVisibility(View.GONE);
            }
        } else if (requestCode == 3 && resultCode == 3) {
            entity = (SelectCouponEntity) data.getSerializableExtra("data");
            temp = entity.getPrice();
            use_coupon_info.setText(entity.getVoucherInfo());
            totalTV.setText("" + Utils.formatedPrice(totalPrice() - temp));
        } else if (requestCode == 11 && resultCode == 11) {
            if (data.getStringExtra("store_name") == null) {
                /*shopName.setText("无");
                payType = payTypelist.get(0);*/
            } else {
                shopName.setText(data.getStringExtra("store_name"));
                sendType = sendTypeList.get(1);
                sendModeTV.setText(sendType.getSendType());
            }
            store_id = data.getStringExtra("store_id");
            position = data.getIntExtra("position", -1);
            L.e(store_id + "--->" + position);
        }
    }

    @Override
    public void startActivity(Intent intent) {
    /*	Intent intent1 = new Intent(this, PayingTransactionSuccessActivity.class);
        intent1.putExtra("price", price);
		intent1.putExtra("orderid", orderId);*/
        super.startActivity(intent);
        overridePendingTransition(R.anim.custom_in_anim, R.anim.custom_out_anim);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        /*Intent intent1 = new Intent(this, PayingTransactionSuccessActivity.class);
        intent1.putExtra("price", price);
		intent1.putExtra("orderid", orderId);*/

        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.custom_in_anim, R.anim.custom_out_anim);
    }


}
