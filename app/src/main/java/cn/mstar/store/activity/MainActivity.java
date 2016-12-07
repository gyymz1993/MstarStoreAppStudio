package cn.mstar.store.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.mstar.store.R;
import cn.mstar.store.app.MyAction;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.callback.OnSucceedListener;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.entity.ShoppingCartItem;
import cn.mstar.store.entity.Version;
import cn.mstar.store.fragments.ClassifyFragment;
import cn.mstar.store.fragments.HomeFragment;
import cn.mstar.store.fragments.MySelfFragment;
import cn.mstar.store.fragments.ShoppingCartFragment;
import cn.mstar.store.functionutils.LogUtils;
import cn.mstar.store.functionutils.LoginRequest;
import cn.mstar.store.functionutils.SpUtils;
import cn.mstar.store.functionutils.UpdateUtil;
import cn.mstar.store.utils.Constants;
import cn.mstar.store.utils.CustomToast;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;
import cn.mstar.store.view.BadgeView;
import cn.mstar.store.view.CheckedLinearlayout;


public class MainActivity extends AppCompatActivity implements MySelfFragment.OnFragmentInteractionListener, View.OnClickListener {


    private static final String MYSELFFRAGMENT_TAG = "myselffragmenttag";
    @Bind(R.id.home)
    CheckedLinearlayout radioHome;
    @Bind(R.id.category)
    CheckedLinearlayout radioClassification;
    @Bind(R.id.shoppingcart)
    CheckedLinearlayout radioShoppingCart;
    @Bind(R.id.mycenter)
    CheckedLinearlayout radioMe;
    //public static MainActivity mainActivity;
    private FragmentManager fragmentManager;
    // Fragment资源
    private Fragment homeFragment, classficationFragment, myselfFragment, shoppingCartItem;
    // 主页按钮编号 有几个写几个
    public static final int TAB_HOME = 0;// 首页
    public static final int TAB_ME = 2;// 我
    public static final int TAB_SHOPPING_CART = 3;
    public static final int TAB_CLASSIFICATION = 4;// 更多
    //private RadioGroup mRadioGroup;
    private MyApplication app;
    private String token;
    private int previousId;
    private int nowId;
    private boolean justLogOf = false;
    private AlertDialog alertDialog;
    TextView shopping_tv;
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        ButterKnife.bind(this);
        Utils.LoginClean((MyApplication) getApplication(), false);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        // 设置为竖屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        MyApplication.getInstance().addActivity(this);
        init();
        previousId = R.id.radio_home;
        app = (MyApplication) getApplication();
        autoLogin();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  UpdateUtil.onDestroy();
    }

    private void updateAPK() {
        if (UpdateUtil.handler==null){
            UpdateUtil.setOnResultListener(new OnSucceedListener<Version>() {
                @Override
                public void onResult(boolean isSecceed, final Version obj) {
                    if (isSecceed && obj != null) {
                        Double serviceCode = Double.valueOf(obj.getVersion());
                        Double localCode = UpdateUtil.getVersionCode(MainActivity.this);
                        if (serviceCode > localCode) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("发现新版本" + serviceCode + "," + " 请升级后使用");
                            builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (UpdateUtil.isExis()) {
                                        UpdateUtil.getApkFile().delete();
                                    }
                                    UpdateUtil.downLoadNewApk(MainActivity.this, obj);
                                }
                            });
                            builder.setCancelable(false);
                            builder.create().show();
                        }
                    }
                }
            });
            UpdateUtil.getVersion(MainActivity.this);
        }



//        VolleyRequest.GetCookieRequest(app.getApplicationContext(), NewLink.CHECK_UPGRADE, new VolleyRequest.HttpStringRequsetCallBack() {
//            @Override
//            public void onSuccess(String result) {
//                L.d("update:::", result);
//                try {
//                    Gson gson=new Gson();
//                    JsonObject elm = gson.fromJson(result, JsonElement.class).getAsJsonObject().get("data").getAsJsonObject();
//                    boolean ifUpdate = elm.get("ifupdate").getAsBoolean();
//                    UpgradeItem it = gson.fromJson(elm, UpgradeItem.class);
//                    //  if (Double.valueOf(it.version) > UpdateUtil.getVersionCode(app.getApplicationContext())){
//                    LogUtils.i("服务器版本号" + ""+(Double.valueOf(it.version)>UpdateUtil.getVersionCode(app.getApplicationContext())));
//                    if (Double.valueOf(it.version)>UpdateUtil.getVersionCode(app.getApplicationContext())) {
//                            updatesAPK();
//                    }
//
//                    //   }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//            }
//        });
    }


    /**
     * 自动登陆
     */
    public void autoLogin() {
        try {
            String username = MyApplication.spUtils.getString(SpUtils.key_username);
            String passwd = MyApplication.spUtils.getString(SpUtils.key_password);
            SpUtils.LOGIN_USERNAME = username;
            SpUtils.LOGIN_PASSWORD = passwd;
            LoginRequest.startLogin(MainActivity.this, username, passwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载购物车数据
     */
    public static int cartCount = 0;

    public void inflateDatas() {
        if ("".equals(Utils.getTokenKey(app))) {

        } else {
            //i_showProgressDialog();
            String link = NewLink.LIST_SHOPPING_CART + "&key=" + Utils.getTokenKey(app);
            link += "&size=240&page=10000";
            VolleyRequest.GetCookieRequest(this, link, new VolleyRequest.HttpStringRequsetCallBack() {
                @Override
                public void onSuccess(String result) {
                    // get the result as a JsonObject and
                    // turn the result into the ShoppingCardObject.
                    try {
                        L.d("XXX:::", result);
                        Gson gson = new Gson();
                        cartCount = gson.fromJson(result, JsonObject.class).getAsJsonObject("data").get("list_count").getAsInt();
                        LogUtils.e(cartCount + "setMycartCount");
                        setMycartCount();
                    } catch (Exception e) {
                        e.printStackTrace();
                        setMycartCount();
                    } finally {
                        // i_dismissProgressDialog();
                    }
                }

                @Override
                public void onFail(String error) {
                    i_dismissProgressDialog();
                }
            });
        }

    }


    public void setMycartCount() {
        if (shopping_tv == null || cartCount == 0) {
            shopping_tv.setVisibility(View.GONE);
            remind(shopping_tv, false);
            return;
        } else {
            shopping_tv.setVisibility(View.VISIBLE);
            remind(shopping_tv, true);
        }

    }

    BadgeView badge1;

    private void remind(View view, boolean isVisible) {
        //BadgeView的具体使用
        badge1.setText(cartCount + ""); // 需要显示的提醒类容
        badge1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        badge1.setTextColor(Color.WHITE); // 文本颜色
        int hint = Color.rgb(200, 39, 73);
        badge1.setBadgeBackgroundColor(hint); // 提醒信息的背景颜色，自己设置
        badge1.setTextSize(10); // 文本大小
        badge1.setBadgeMargin(3, 3); // 水平和竖直方向的间距
        badge1.setBadgeMargin(5); //各边间隔
        if (isVisible) {
            badge1.show();// 只有显示
        } else {
            badge1.hide();//影藏显示
        }
        // badge1.toggle(); //显示效果，如果已经显示，则影藏，如果影藏，则显示
        // badge1.hide();//影藏显示
    }

    // each time we onresume... check if there is an update...


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //String orderId = intent.getStringExtra("data");
        // jump to intent
        if (!intent.getAction().equals(MyAction.paySuccessGotodetails))
            return;
        curu_Tab = TAB_HOME;
        selectItem(TAB_HOME);
    }

    private void init() {

        token = MyApplication.getInstance().tokenKey;
        fragmentManager = getSupportFragmentManager();
        //默认选中HomeFragment
        previousId = radioHome.getId();
        nowId = TAB_HOME;
        selectItem(TAB_HOME);
        shopping_tv = (TextView) findViewById(R.id.shopping_tv);
        badge1 = new BadgeView(MainActivity.this, shopping_tv);// 创建一个BadgeView对象，view为你需要显示提醒的控件
        radioHome.setOnClickListener(this);
        radioClassification.setOnClickListener(this);
        radioShoppingCart.setOnClickListener(this);
        radioMe.setOnClickListener(this);

    }


    // 显示选定片段视图导航抽屉列表项
    public void selectItem(int position) {
        ressetAll();
        justLogOf = false;
        if (position == TAB_HOME) {
            // 替换首页
            changeHome();
            makeToast("Home");
        } else if (position == TAB_CLASSIFICATION) {
            changeClassification();
            makeToast("Classification");
        } else {
            // 先判断登录了没、 再决定替换
            if ("".equals(Utils.getTokenKey(app))) {
                // 选择目前那个
//                checkPrevious();
                ressetAll();
                // 判断是哪一个fragment再替换
                loginToExchange(position);
                makeToast("没登录");
                shopping_tv.setVisibility(View.GONE);
                remind(shopping_tv, false);
            } else {
                inflateDatas();
                // 调到登录界面，并监听回来
                makeToast("已登录、jump to " + position);
                switch (position) {
                    case TAB_SHOPPING_CART:
                        changeShoppingCart();
                        break;
                    case TAB_ME:
                        changeMyself();
                        break;
                }
            }
        }
    }


    private void checkPrevious() {

    }

    private void loginToExchange(int position) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setAction(MyAction.logForExchange);
        loginIntent.putExtra("fragmentid", position);
        startActivityForResult(loginIntent, 94);
    }

    private void changeHome() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//		transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            transaction.add(R.id.content_frame, homeFragment);
        } else
            transaction.show(homeFragment);
        transaction.commit();
        radioHome.setChecked(true);
    }

    private void changeClassification() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//		transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        if (classficationFragment == null) {
            classficationFragment = new ClassifyFragment();
            transaction.add(R.id.content_frame, classficationFragment);
        } else
            transaction.show(classficationFragment);
        transaction.commit();
        radioClassification.setChecked(true);
    }


    private void changeShoppingCart() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//		transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        if (shoppingCartItem == null) {
            shoppingCartItem = new ShoppingCartFragment();
            transaction.add(R.id.content_frame, shoppingCartItem);
        } else {
            if (app.frg_isFrg_shoppingcart_needUpdate == true) {
                ((ShoppingCartFragment) shoppingCartItem).inflateDatas();
                app.frg_isFrg_shoppingcart_needUpdate = false;
            }
            transaction.show(shoppingCartItem);
        }
        transaction.commit();
        radioShoppingCart.setChecked(true);
    }

    private void changeMyself() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//		transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        if (myselfFragment == null) {
            myselfFragment = new MySelfFragment();
            transaction.add(R.id.content_frame, myselfFragment);
        } else if (app.isFrg_me_needUpdate == true) {
            ((MySelfFragment) myselfFragment).updateNumbers();
            app.isFrg_me_needUpdate = false;
        }
        transaction.show(myselfFragment);
        transaction.commit();
        radioMe.setChecked(true);
    }


    public void loginToServer() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setAction(MyAction.logForShoppingCart);
        startActivityForResult(loginIntent, 11);
    }

    private void invokeShoppingCart() {

        i_showProgressDialog();
        VolleyRequest.checkLogStatus((MyApplication) getApplication(), new VolleyRequest.LogonStatusLinstener() {
            @Override
            public void OK(String reason) {

                // 登录完成后
                afterLogin();
                i_dismissProgressDialog();
            }

            @Override
            public void NO() {
                // 未登录
                beforeLogin();
                i_dismissProgressDialog();
            }
        });
    }

    // we create the fragment only once.
    private void afterLogin() {
//		inflateData(link);
        shoppingCartItem = new ShoppingCartFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//		transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        transaction.add(R.id.content_frame, shoppingCartItem);
        transaction.show(shoppingCartItem);
        transaction.commit();
        CustomToast.mSystemToast(this, "creating shoppingcartitemfragmnet");
    }

    private void beforeLogin() {

        // 让用户登录
        Intent intent = new Intent(this, LoginActivity.class);
        // give him an action
        intent.setAction(MyAction.logForShoppingCart);
        startActivityForResult(intent, 11);
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

    public void i_showProgressDialog(String message) {
        dialog = new LoadingDialog(this, message);
        dialog.show();
    }


    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null)
            transaction.hide(homeFragment);
        if (classficationFragment != null)
            transaction.hide(classficationFragment);
        if (myselfFragment != null)
            transaction.hide(myselfFragment);
        if (shoppingCartItem != null)
            transaction.hide(shoppingCartItem);
    }


    private long exitTime = 0;
    private boolean myselfFragJustChanged = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                CustomToast.makeToast(this, getString(R.string.exit_app), Toast.LENGTH_SHORT);
                exitTime = System.currentTimeMillis();
            } else {
//				MyApplication.getInstance().exit();
                moveTaskToBack(true);
            }
        }
        return true;
    }


    protected void replaceFragment(int viewId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment)
                .commitAllowingStateLoss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if you are not logged then, back to the home
        L.d("request:::", "" + requestCode + " and data is " + data + " and previous is " + nowId);
        if (requestCode == 94) {
            int fragment_id = -1;
            if (data != null)
                fragment_id = data.getIntExtra("fragmentid", -1);
            else {
                fragment_id = nowId;
                justLogOf = true;
            }
            curu_Tab = fragment_id;
            selectItem(fragment_id);
        } else if (requestCode != 94 && data != null) {
// if the result is 11... move back to the previous button.
            if (requestCode == 11 && data != null) {
                // tell the main activity to proceed the change.
                // choose back the previous button
//			selectPrevious();
//			selectItem(TAB_SHOPPING_CART);
            } else if (requestCode == 2 && data != null) {
                app.username = data.getStringExtra(Constants.START_ACTIVITY_FOR_RESULT_KEY);
                app.points = data.getStringExtra(Constants.POINTS);
                app.pic = data.getStringExtra(Constants.PIC);
                makeToast("MAIN points " + app.points);
            }

            // if the code is 94 ... then it doesn't concern the other viewz.

            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (fragment != null)
                        fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    private void makeToast(String s) {
//		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction(int i) {
        if (i == 0) {
            // update fragment .
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment df; // = getSupportFragmentManager().findFragmentByTag(MYSELFFRAGMENT_TAG);
            df = MySelfFragment.newInstance(false);
            fragmentTransaction.replace(R.id.content_frame, df, MYSELFFRAGMENT_TAG).commit();
        }
    }

    public void updateBottomTotal(List<ShoppingCartItem> checkedGoods) {
        // call the same function inside the fragment.
        if (shoppingCartItem != null)
            ((ShoppingCartFragment) shoppingCartItem).updateBottom(checkedGoods);
    }

/*	public void updateMyselfFrag(boolean isLogged, boolean doswitch) {
//		CustomToast.mToast(this, "updating myselffragment");
		myselfFragJustChanged  = true;
		// the current showing function is me ~ then update it automatically...
		if (myselfFragment != null  && myselfFragment.isVisible() *//*&&
                (myselfFragment == null ? true : !(((MySelfFragment)myselfFragment).getLoggedState() == isLogged))*//*) {

			// update the fragment and show it ~
			myselfFragJustChanged  = true;
			if (doswitch)
				fakeSelectItem(isLogged);
			// i need to know what was the previous state.
		}
	}*/

    private void fakeSelectItem(boolean isLogged) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (myselfFragment != null) {
            transaction.hide(myselfFragment);
        }
        if (myselfFragJustChanged) {
            if (myselfFragment != null)
                transaction.remove(myselfFragment);
            myselfFragment = MySelfFragment.newInstance(isLogged);
            transaction.add(R.id.content_frame, myselfFragment, /*my own adding*/ MYSELFFRAGMENT_TAG);
            myselfFragJustChanged = false;
        } else {
            if (myselfFragment == null) {
                myselfFragment = MySelfFragment.newInstance(false);
                transaction.add(R.id.content_frame, myselfFragment, /*my own adding*/ MYSELFFRAGMENT_TAG);
            } else
                transaction.show(myselfFragment);
        }
        transaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        inflateDatas();
        updateAPK();
        ressetOne();
        if (app == null || "".equals(app.tokenKey)) {
            myselfFragment = null;
            return;
        }
        if (app != null)
            app.isFrg_me_needUpdate = true;

        // check in another thread if the fragment myself needs to be updated.

	/*	if (myselfFragment != null && !((MySelfFragment) myselfFragment).getLoggedState ()) {
            // check if the fragment needs to be updated.

			VolleyRequest.checkLogStatus(this, new LogonStatusLinstener() {
				@Override
				public void OK(String reason) {
//					CustomToast.mSystemToast(MainActivity.this, "main logged");
					updateMyselfFrag(true, true);
				}

				@Override
				public void NO() {
					CustomToast.mSystemToast(MainActivity.this, "main not logged");
					updateMyselfFrag(false, true);
				}
			});
		}*/
    }

    //用户更新APK
    /*private void updatesAPK() {
            alertDialog = new AlertDialog.Builder(this).setMessage(getString(R.string.needtoupdate_toversion)*//*+ app.upgradeEntity.version*//*)
                    .setPositiveButton(getString(R.string.upgrade), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            jumpToBrowser(app.upgradeEntity.download);
                            alertDialog.dismiss();
                        }
                    }).create();
            alertDialog.setCancelable(false);
            alertDialog.show();
    }*/

    private void jumpToBrowser(String download) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(download));
        startActivity(i);
        app.needUpgrade = false;
    }


    public int getScreenWidth() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
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


    public void mySelfFragmentNeedUpdate(boolean b) {
        myselfFragJustChanged = true;
        i_dismissProgressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                if (!"".equals(Utils.getTokenKey(app)) || v.getId() == R.id.radio_home || v.getId() == R.id.radio_classification) {
//					if (nowId != 0)
                    previousId = nowId;
                    nowId = TAB_HOME;
                    curu_Tab = TAB_HOME;
                }
                selectItem(TAB_HOME);
                break;
            case R.id.mycenter:
                curu_Tab = TAB_ME;
                selectItem(TAB_ME);
                break;
            case R.id.shoppingcart:
                curu_Tab = TAB_SHOPPING_CART;
                selectItem(TAB_SHOPPING_CART);
                break;
            case R.id.category:
                curu_Tab = TAB_CLASSIFICATION;
                if (!"".equals(Utils.getTokenKey(app)) || v.getId() == R.id.radio_home || v.getId() == R.id.radio_classification) {
//					if (nowId != 0)
                    previousId = nowId;
                    nowId = TAB_CLASSIFICATION;
                }
                selectItem(TAB_CLASSIFICATION);
                break;
        }
    }

    /**
     * @action: 重置所有按钮状态
     * @author: YangShao
     * @date: 2015/10/19 @time: 10:52
     */
    public void ressetAll() {
        radioHome.setChecked(false);
        radioClassification.setChecked(false);
        radioShoppingCart.setChecked(false);
        radioMe.setChecked(false);
    }

    /**
     * @action: 记录当前页面的值   跳转返回重置一次
     * @author: YangShao
     * @date: 2015/10/19 @time: 10:59
     */
    public static int curu_Tab = 0;

    public void ressetOne() {
        ressetAll();
        if (curu_Tab == TAB_HOME) {
            radioHome.setChecked(true);
        }
        if (curu_Tab == TAB_ME) {
            radioMe.setChecked(true);
        }
        if (curu_Tab == TAB_SHOPPING_CART) {
            radioShoppingCart.setChecked(true);
        }
        if (curu_Tab == TAB_CLASSIFICATION) {
            radioClassification.setChecked(true);
        }
    }

}


