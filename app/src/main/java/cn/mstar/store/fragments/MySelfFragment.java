package cn.mstar.store.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.mstar.store.R;
import cn.mstar.store.activity.CouponActivity;
import cn.mstar.store.activity.CreateReceiverAddressActivity;
import cn.mstar.store.activity.GoodsManagementActivity;
import cn.mstar.store.activity.LoginActivity;
import cn.mstar.store.activity.MainActivity;
import cn.mstar.store.activity.MessageActivity;
import cn.mstar.store.activity.MockActivity;
import cn.mstar.store.activity.MyCollectionActivity;
import cn.mstar.store.activity.MyCommissionActivity;
import cn.mstar.store.activity.PopularizeActivity;
import cn.mstar.store.activity.RegisterActivity;
import cn.mstar.store.activity.SelfInformationActivity;
import cn.mstar.store.activity.ShareQcodeActivity;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.CircleImageView;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.customviews.OverscrollView2;
import cn.mstar.store.entity.UserSelfInfoEntity;
import cn.mstar.store.functionutils.LogUtils;
import cn.mstar.store.utils.Constants;
import cn.mstar.store.utils.CustomToast;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;
import cn.mstar.store.view.BadgeView;

/**
 * 我的订单页面
 */
public class MySelfFragment extends Fragment {

    protected static final int BG_LOGIN_START = 91;
    protected static final int BG_LOGIN_END = 90;
    LayoutInflater inflater;
    Context mContext;
    private LinearLayout lny_good_menus;
    private LinearLayout lny_good_below_menus;
    private LinearLayout lny_afterLog_content;
    private LinearLayout lny_login_layout;
    private RelativeLayout lny_login_succed_layout;
    private int LIGHT_LINE, HEAVY_LINE;
    private View i_login, i_register;
    private int[] goods_management = new int[]{R.drawable.icon_to_be_paid, R.drawable.icon_deliver_goods,
            R.drawable.icon_goods_receipt, R.drawable.icon_pick_up};
//    private int[] goods_below_icons = new int[]{R.drawable.icon_order_form, R.drawable.icon_collection,
//            R.drawable.icon_coupons, R.drawable.icon_register, R.drawable.icon_shopping_cart
//    ,R.drawable.iconfont_erweima,R.drawable.icon_document,R.drawable.iconfont_lianjie,R.drawable.iconfont_yongjin};
private int[] goods_below_icons = new int[]{R.drawable.icon_order_form, R.drawable.icon_collection,
        R.drawable.icon_coupons,R.drawable.icon_document,R.drawable.iconfont_yongjin,R.drawable.iconfont_lianjie,R.drawable.iconfont_erweima};
    private boolean loggedState = true;
    private String[] ICON_BOTTOM_TEXT;
    private String username;
    private TextView tv_logged_username;
    private TextView change_info;
    private View view;
    private String password;
    @Bind(R.id.tv_Integral)
    TextView tv_Integral;
    @Bind(R.id.scrollview)
    OverscrollView2 scrollView;
    private SystemBarTintManager mStatusBarManager;
    private View mHeader;
    @Bind(R.id.title_name)
    TextView titlebar_title;
    @Bind(R.id.title_message)
    ImageView titlebar_message;
    private String points;
    private String pic;
    private MyApplication app;
    @Bind(R.id.iv_head)
    CircleImageView iv_head;
    private LoadingDialog dialog;
    private TextView[] tv_side_numer;
    private View[] iv_goods_tocheck_number;

    public static MySelfFragment newInstance(boolean isLogged) {
        MySelfFragment frg = new MySelfFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_LOGGED_LINK, isLogged);
        frg.setArguments(args);
        return frg;
    }



    /**
 *
 *  @action: 判断状态
 *  @author:  YangShao
 *  @date: 2015/10/27 @time: 17:54
 */
    private void  isDistributions() {
        String link = NewLink.MYDISTRIBUTION+"&key="+Utils.getTokenKey(app)/*+"&wxName="+""*/;
        LogUtils.e("申请分销路径"+link);
      //  link += "&tName=" + tName + "&email=" + info.email + "&sex=" + info.sex;
            VolleyRequest.GetCookieRequest(getActivity(), link,
                    new VolleyRequest.HttpStringRequsetCallBack() {
                        @Override
                        public void onSuccess(String result) {
                            if (result != null) {
                                try {
                                    Gson gson = new Gson();
                                    int symbol = gson.fromJson(result, JsonObject.class).getAsJsonObject("data").get("ifmshop").getAsInt();
                                    addDistributionView(symbol);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFail(String error) {

                        }
                    });//18126216085
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getActivity().getApplication();
        MainActivity.curu_Tab= 2;
        Bundle args = getArguments();
        if (args != null) {
            loggedState = args.getBoolean(Constants.IS_LOGGED_LINK, false);
        }
        isDistributions();
    }






    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.fragment_me, null);
        ButterKnife.bind(this, view);
        iv_goods_tocheck_number = new View[3];
        tv_side_numer = new TextView[3];
        titlebar_title.setText(getString(R.string.me));
        titlebar_message.setVisibility(View.VISIBLE);
        titlebar_message.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
            }
        });
        scrollView = (OverscrollView2) view.findViewById(R.id.scrollview);
        updateLoggedState();
        return view;
    }


    private void initListeners() {
        change_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelfInformationActivity.class);
                startActivityForResult(intent, 3);
            }
        });
    }

    public android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BG_LOGIN_START:
                    dialog = new LoadingDialog(getActivity(), getString(R.string.pull_to_refresh_footer_refreshing_label));
                    dialog.show();
                    break;
                case BG_LOGIN_END:
                    initValues();
                    initOutViews(view);
                    initTopView(loggedState);
                    initGoodsManagement(loggedState);
                    initGoodsBelowMenus(loggedState);
                    initListeners();
                    dialog.cancel();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private boolean firstTime = true;


    private void updateLoggedState() {

        username = app.username;
        password = app.password;
        pic = app.pic;
    //http://www.fanerjewelry.com/data/upload/shop/avatar/avatar_1416.jpg
        //LogUtils.e("缓存头像数据"+app.pic);
        points = app.points;

        // check from the internet if the data are up..
        if (firstTime) {
            // then update the view...
            firstTime = false;
            initValues();
            initOutViews(view);
            initTopView(loggedState);
            initGoodsManagement(loggedState);
            initGoodsBelowMenus(loggedState);
            initListeners();
        } else {
        }

    }


    private void initTopView(boolean loggedState) {
        if (loggedState) {
            lny_login_layout.setVisibility(View.GONE);
            lny_login_succed_layout.setVisibility(View.VISIBLE);
            tv_logged_username.setText(app.username + " , " + getString(R.string.welcome));
            tv_Integral.setText(getString(R.string.points) + app.points);
//			iv_head
    //        L.d("pic:::", app.pic);
            ImageLoader.getInstance().displayImage(app.pic, iv_head, ImageLoadOptions.getOptions());
        } else {
            lny_login_succed_layout.setVisibility(View.GONE);
            lny_login_layout.setVisibility(View.VISIBLE);
            // listen to login and succed buttons.
            i_register.setOnClickListener(new Login_RegisterOnclickListener());
            i_login.setOnClickListener(new Login_RegisterOnclickListener());
        }
        mStatusBarManager = new SystemBarTintManager(getActivity());
        mStatusBarManager.setStatusBarTintEnabled(true);
        mHeader = view.findViewById(R.id.header);
    }


    public boolean getLoggedState() {
        return loggedState;
    }


    private void setLoggedState(boolean booleanExtra, String username) {
        loggedState = booleanExtra;
        // tell the mainactivity to update me
//		((MainActivity) getActivity()).updateMyselfFrag(booleanExtra, true);
    }

    private void initValues() {

        LIGHT_LINE = Utils.convertPxtoDip(1, mContext);
        HEAVY_LINE = Utils.convertPxtoDip(15, mContext);
        ICON_BOTTOM_TEXT = getResources().getStringArray(R.array.goods_managment_1);
    }
    /*
	private final int[] ic_id = {R.drawable.icon_order_form, R.drawable.icon_register, R.drawable.icon_collection,
	R.drawable.ic};*/
    List count=new ArrayList<>();;
    private void initGoodsBelowMenus(boolean loggedState) {
        Class<?> jumpToActivity = null;
        count.add("所有订单");
        count.add("我的收藏");
        count.add("代金劵");
        for (int i = 0; i < count.size(); i++) {
            View inf_rel = getView();
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = LIGHT_LINE;
            switch (i) {
                case 0:
                    viewHoder. tv_title.setText(getResources().getString(R.string.allthegoods));
                    viewHoder. tv_openning_hint.setText(getResources().getString(R.string.allthegoods_hint));
                    viewHoder. iv_menu_ic.setImageResource(goods_below_icons[i]);
                    viewHoder.inf_rel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), cn.mstar.store.activity.GoodsManagementActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    viewHoder. tv_title.setText(getResources().getString(R.string.favorites));
                    viewHoder. tv_openning_hint.setText(getString(R.string.my_store_hint));
                    viewHoder. iv_menu_ic.setImageResource(goods_below_icons[i]);
                    viewHoder. inf_rel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), MyCollectionActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    viewHoder. tv_title.setText(getResources().getString(R.string.coupon));
                    viewHoder.tv_openning_hint.setText(getString(R.string.my_coupon_hint));
                    viewHoder. iv_menu_ic.setImageResource(goods_below_icons[i]);
                    viewHoder.inf_rel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(),CouponActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
            }
            lny_good_below_menus.addView(inf_rel, params);
        }

    }



    /**
     *
     *  @action:添加分销
     *  @author:  YangShao
     *  @date: 2015/10/27 @time: 17:32
     */
    public void addDistributionView(int symbol){
        count.clear();
        lny_good_below_menus.removeAllViews();
        initGoodsBelowMenus(loggedState);
        if (symbol==1){
            count.add("我的推广");
            count.add("我的佣金");
            count.add("推广链接");
            count.add("推广二维码");
            for (int i=3;i<count.size();i++){
                {
                    // get the views.
                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    View inf_rel = getView();
                    params.topMargin = LIGHT_LINE;
                    switch (i) {
                        case 3:
                            viewHoder. tv_title.setText("我的推广");
                            viewHoder.tv_openning_hint.setText(getString(R.string.see_extension_workers));
                            viewHoder.iv_menu_ic.setImageResource(goods_below_icons[i]);
                            viewHoder.inf_rel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(),PopularizeActivity.class);
                                    startActivity(intent);
                                }
                            });
                            break;
                        case 4:
                            viewHoder. tv_title.setText("我的佣金");
                            viewHoder. tv_openning_hint.setText(getString(R.string.see_and_get_commission));
                            viewHoder.iv_menu_ic.setImageResource(goods_below_icons[i]);
                            inf_rel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //My commission  我的佣金页面
                                    Intent intent = new Intent(getActivity(),MyCommissionActivity.class);
                                    startActivity(intent);
                                }
                            });
                            break;
                        case 5:
                            viewHoder.tv_title.setText("推广链接");
                            viewHoder.tv_openning_hint.setText(getString(R.string.see_extension_link));
                            viewHoder.iv_menu_ic.setImageResource(goods_below_icons[i]);
                            viewHoder. inf_rel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(),ShareQcodeActivity.class);
                                    intent.putExtra("code",1);
                                    startActivity(intent);
                                }
                            });
                            break;
                        case 6:
                            viewHoder. tv_title.setText("推广二维码");
                            viewHoder.tv_openning_hint.setText(getString(R.string.see_two_dimension_code));
                            viewHoder. iv_menu_ic.setImageResource(goods_below_icons[i]);
                            viewHoder.inf_rel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(),ShareQcodeActivity.class);
                                    intent.putExtra("code",2);
                                    startActivity(intent);
                                }
                            });
                            break;
                    }
                    lny_good_below_menus.addView(inf_rel, params);
                }
            }
        }else{
            count.add("申请分销");
            for (int i=3;i<count.size();i++){
                // get the views.
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                View inf_rel = getView();
                switch (i) {
                    case 3:
                        params.topMargin = LIGHT_LINE;
                        viewHoder. tv_openning_hint.setText("");
                        viewHoder.iv_menu_ic.setImageResource(goods_below_icons[i]);
                        if (symbol==0){
                            viewHoder. tv_title.setText("申请分销");
                            viewHoder.inf_rel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), CreateReceiverAddressActivity.class);
                                    intent.putExtra("code", 1);
                                    startActivity(intent);
                                }
                            });
                        }
                        if (symbol==2){
                            viewHoder. tv_title.setText("审核不通过");
                        }
                        if (symbol==3){
                            viewHoder. tv_title.setText("审核中");
                        }
                        break;
                }
                lny_good_below_menus.addView(inf_rel, params);
            }
        }
    }



    public View getView(){
        viewHoder=new ViewHoder();
        if (inflater == null)
            inflater = LayoutInflater.from(mContext);
        View inf_rel = inflater.inflate(R.layout.me_goods_below_menus, null);
        viewHoder. tv_title = (TextView) inf_rel.findViewById(R.id.tv_menu_title);
        viewHoder. tv_openning_hint = (TextView) inf_rel.findViewById(R.id.tv_expand_hint);
        viewHoder. iv_menu_ic = (ImageView) inf_rel.findViewById(R.id.iv_below_menu_ic);

        if (!loggedState) {
            viewHoder. tv_openning_hint.setVisibility(View.INVISIBLE);
        } else {
            viewHoder.tv_openning_hint.setVisibility(View.VISIBLE);
        }
        viewHoder.inf_rel=inf_rel;
        return inf_rel;
    }
    ViewHoder viewHoder;
    public class ViewHoder{
        View inf_rel;
        TextView tv_title;
        TextView tv_openning_hint;
        ImageView iv_menu_ic;
    }

    private void initOutViews(View view) {
        i_login = view.findViewById(R.id.btn_login);
        i_register = view.findViewById(R.id.btn_register);
        lny_good_menus = (LinearLayout) view.findViewById(R.id.lny_manage_goods);
        lny_good_below_menus = (LinearLayout) view.findViewById(R.id.lny_goods_below_menus);
        lny_afterLog_content = (LinearLayout) view.findViewById(R.id.lny_on_login_succeeded);
        lny_login_layout = (LinearLayout) view.findViewById(R.id.login_layout);
        lny_login_succed_layout = (RelativeLayout) view.findViewById(R.id.login_successed_layout);
        tv_logged_username = (TextView) view.findViewById(R.id.tv_logged_username);
        change_info = (TextView) view.findViewById(R.id.change_info);
    }

    private void initGoodsManagement(boolean loggedState) {
        if (app == null)
            return;
        loggedState = !"".equals(app.tokenKey);
        String tokenKey = app.tokenKey;
        String[] links = new String[]{
                NewLink.GET_ORDER_LIST_WAITING_FOR_PAY + "&key=" + tokenKey,
                NewLink.GET_ORDER_LIST_WAITING_FOR_SEND + "&key=" + tokenKey,
                NewLink.GET_ORDER_LIST_WAITING_FOR_RECEIVE + "&key=" + tokenKey
        };
        for (int i = 0; i < 3; i++) {
            // inflate it first
            if (inflater == null)
                inflater = LayoutInflater.from(mContext);
            View inf_rel = inflater.inflate(R.layout.me_goods_management_items, null);
            inf_rel.setTag((i + 1));
            // get the views.
			/*final View[]*/
            if (iv_goods_tocheck_number[i] == null)
                iv_goods_tocheck_number[i] = inf_rel.findViewById(R.id.iv_goods_tocheck_number);
			/*final TextView[]*/
            if (tv_side_numer[i] == null)
                tv_side_numer[i] = (TextView) inf_rel.findViewById(R.id.tv_number_tocheck);

            ImageView iv_top_img = (ImageView) inf_rel.findViewById(R.id.iv_menu_goods_top_icon);
            TextView tv_bottom = (TextView) inf_rel.findViewById(R.id.tv_goods_menu_option);
            if (!loggedState) {
                iv_goods_tocheck_number[i].setVisibility(View.INVISIBLE);
            } else {
                // 获取每个列表一共有多少个订单 & 然后在旁边显示
                final int finalI = i;
                VolleyRequest.GetCookieRequest(getActivity(), links[i], new VolleyRequest.HttpStringRequsetCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            L.d("result:::", result);
                            Gson gson = new Gson();
                            JsonElement elm = gson.fromJson(result, JsonElement.class).getAsJsonObject().get("data");
                            int totalItem = elm.getAsJsonObject().get("list_count").getAsInt();
                            // make a list of what we need from here.
                            if (totalItem != 0) {
                                tv_side_numer[finalI].setText(totalItem + "");
                                iv_goods_tocheck_number[finalI].setVisibility(View.VISIBLE);
                            } else {
                                iv_goods_tocheck_number[finalI].setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
//                            VolleyRequest.manifestNetworkError(getActivity(), "exception");
                        }
                    }
                    @Override
                    public void onFail(String error) {
//                        tv_side_numer[finalI].setVisibility(View.INVISIBLE);
                    }
                });
            }
            iv_top_img.setImageResource(goods_management[i]);
            tv_bottom.setText(ICON_BOTTOM_TEXT[i]);
            // i want to put them inside a linearlayout.
            LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout_params.weight = 25;
            if (loggedState == true)
                inf_rel.setOnClickListener(new GoodManagmentTopOnclickListener(i + 1));
            else
                inf_rel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        loginToServer(GoodsManagementActivity.class);
                    }
                });
            lny_good_menus.addView(inf_rel, layout_params);
        }
        // how to do with the dividers.
    }

    public void updateNumbers() {
        if (app != null && !"".equals(app.tokenKey))
            loggedState = true;
        else
            return;
        String tokenKey = app.tokenKey;
        String[] links = new String[]{
                NewLink.GET_ORDER_LIST_WAITING_FOR_PAY + "&key=" + tokenKey,
                NewLink.GET_ORDER_LIST_WAITING_FOR_SEND + "&key=" + tokenKey,
                NewLink.GET_ORDER_LIST_WAITING_FOR_RECEIVE + "&key=" + tokenKey
        };

        for (int i = 0; i < 3; i++) {
            if (iv_goods_tocheck_number == null || tv_side_numer == null)
                return;
//            tv_side_numer[i].setVisibility(View.INVISIBLE);
            iv_goods_tocheck_number[i].setVisibility(View.INVISIBLE);

            if (!loggedState) {
                iv_goods_tocheck_number[i].setVisibility(View.INVISIBLE);
            } else {
                // 获取每个列表一共有多少个订单 & 然后在旁边显示
                final int finalI = i;
                VolleyRequest.GetCookieRequest(getActivity(), links[i], new VolleyRequest.HttpStringRequsetCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            Gson gson = new Gson();
                            JsonElement elm = gson.fromJson(result, JsonElement.class).getAsJsonObject().get("data");
                            int totalItem = elm.getAsJsonObject().get("list_count").getAsInt();
                            // make a list of what we need from here.
                            if (totalItem != 0) {
                                tv_side_numer[finalI].setText(totalItem + "");
                               // remind(getActivity(),tv_side_numer[finalI],true,totalItem);
                                iv_goods_tocheck_number[finalI].setVisibility(View.VISIBLE);
                            } else {
                                tv_side_numer[finalI].setVisibility(View.INVISIBLE);
                                //remind(getActivity(),tv_side_numer[finalI],false,totalItem);
                                iv_goods_tocheck_number[finalI].setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                           // e.printStackTrace();
                           //  VolleyRequest.manifestNetworkError(getActivity(), "exception");
                        }
                    }

                    @Override
                    public void onFail(String error) {
//                        tv_side_numer[finalI].setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }




    private void remind(Context context,View view,boolean isVisible,int count) {
        BadgeView  badge1  = new BadgeView(context, view);// 创建一个BadgeView对象，view为你需要显示提醒的控件
        //BadgeView的具体使用
        badge1.setText(count + ""); // 需要显示的提醒类容
        badge1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        badge1.setTextColor(Color.WHITE); // 文本颜色
        int hint=Color.rgb(200,39,73);
        badge1.setBadgeBackgroundColor(hint); // 提醒信息的背景颜色，自己设置
        badge1.setTextSize(10); // 文本大小
        badge1.setBadgeMargin(3, 3); // 水平和竖直方向的间距
        badge1.setBadgeMargin(5); //各边间隔
        if(isVisible){
            badge1.show();// 只有显示
        }else{
            badge1.hide();//影藏显示
        }
        // badge1.toggle(); //显示效果，如果已经显示，则影藏，如果影藏，则显示
        // badge1.hide();//影藏显示
    }



    private class Login_RegisterOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == i_login) {
                // jump to my login activity
                loginToServer(null);
            } else if (v == i_register) {
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                startActivity(i);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (app.isFrg_me_needUpdate) {
            initTopView("".equals(app.tokenKey) ? false : true);
        }
        if (loggedState){
            updateNumbers();
            isDistributions();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && data != null && !"".equals(Utils.getTokenKey(app))) {
            String infoS = data.getStringExtra("message");
            UserSelfInfoEntity info = (new Gson()).fromJson(infoS, UserSelfInfoEntity.class);
            if (info == null) {
                setLoggedState(false, "");
            } else {
                updateUserInformations(info);
            }
        }  else {
            if ("".equals(Utils.getTokenKey(app)))
                ((MainActivity) getActivity()).selectItem(MainActivity.TAB_HOME);
        }

    }

    private void updateUserInformations(final UserSelfInfoEntity info) {
       // String a= (new Gson()).toJson(info);
       // UserSelfInfoEntity info = (new Gson()).fromJson(a, UserSelfInfoEntity.class);
        /*key=》登陆令牌
            uName=》用户名
            tName=>真实姓名
            email=>邮箱
            sex=》性别*/
        String link = NewLink.UPDATE_USER_INFO + "&key=" + Utils.getTokenKey((MyApplication) getActivity().getApplication());
        String tName = Utils.encodeChinese(info.tName);
        try {
            tName = URLEncoder.encode(info.tName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        link += "&tName=" + tName + "&email=" + info.email + "&sex=" + info.sex;
        VolleyRequest.GetCookieRequest(getActivity(), link, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
				/* success */
                try {
                    JsonElement elm = (new Gson()).fromJson(result, JsonElement.class).getAsJsonObject().get("error");
                    String error = elm.getAsString();
                    //String error = elm.getAsString();
                    if ("0".equals(error)) {
                        // no error
                        // update the myself view
//						app.isFrg_me_needUpdate = true;
                        ((MainActivity) getActivity()).selectItem(MainActivity.TAB_ME);
                        // set up the changes inside the app.
                        app.username = info.tName;
                        app.points = info.points;
//						app.pic = info.pic;
                        initTopView(true);
//						CustomToast.makeToast(getActivity(), getString(R.string.information_change_success), Toast.LENGTH_SHORT);
                    } else {
//						CustomToast.makeToast(getActivity(), getString(R.string.information_change_failure), Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//					CustomToast.makeToast(getActivity(), getString(R.string.information_change_failure), Toast.LENGTH_SHORT);
                } finally {
                }
            }

            @Override
            public void onFail(String error) {
				/*failure 可能是网络的问题 */
                CustomToast.makeToast(getActivity(), getString(R.string.information_change_failure), Toast.LENGTH_SHORT);
            }
        });
    }

    private void dismissDialog_() {
        if (dialog != null) {
            dialog.dismiss();
            dialog.cancel();
            dialog = null;
        }
    }

    private void showDialog() {
        dialog = new LoadingDialog(getActivity());
        dialog.show();
    }

    // jumpTo 是跳过去那个activity的class命令~
    // 他不为空的情况下，登录成功他就会调到用户徐需要的那个activity里面去
    public void loginToServer(Class<?> jumpTo, String key) {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        if (jumpTo == null)
            getActivity().startActivityForResult(loginIntent, 2);
        else {
            loginIntent.putExtra(LoginActivity.JUMP_TO_ACTIVITY, jumpTo);
            loginIntent.putExtra(MockActivity.GET_TO, MockActivity.EXCHANGE_POINTS_ACTIVITY);
            getActivity().startActivity(loginIntent);
        }
    }

    public void loginToServer(Class<?> jumpTo) {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        makeToast("Jumpto ==  " + jumpTo);
        if (jumpTo == null)
            getActivity().startActivityForResult(loginIntent, 2);
        else {
            loginIntent.putExtra(LoginActivity.JUMP_TO_ACTIVITY, jumpTo);
            getActivity().startActivity(loginIntent);
        }
    }

    private void makeToast(String string) {
//		CustomToast.makeToast(getActivity(), string, Toast.LENGTH_SHORT);
    }

    public static final int LOGIN_REQUEST = 1;  // The request code


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int i);
    }

    private class GoodManagmentTopOnclickListener implements View.OnClickListener {

        private int MENU_ID = -1;

        public GoodManagmentTopOnclickListener(int i) {
            MENU_ID = i;
        }

        @Override
        public void onClick(View v) {
            //
            Intent i = new Intent(getActivity(), GoodsManagementActivity.class);
            i.putExtra(Constants.MENU_POSITION, MENU_ID);
            startActivity(i);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.custom_in_anim, R.anim.custom_out_anim);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.custom_in_anim, R.anim.custom_out_anim);
    }
}