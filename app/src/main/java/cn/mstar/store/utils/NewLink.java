package cn.mstar.store.utils;

import java.util.HashMap;
import java.util.Map;

import cn.mstar.store.entity.RegisterOb;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class NewLink {

    public static final String BASE_URL = "http://www.fanershop.com/";


    public static final class RequestType {


        public static Map<String, Class<RegisterOb>> typeDb;

        static {

            typeDb = new HashMap<>();
            typeDb.put(NewLink.REGISTERING_ACT, RegisterOb.class);
//            typeDb.put()
        }

      /*  public K getType (String baseUrl) {

            if (typeDb != null) {
                return
            }
        }
*/
    }

    // links
    public static final String REGISTERING_AUTH_CODE = BASE_URL + "mobile/index.php?act=getcode&phone=";
    public static final String REGISTERING_ACT = BASE_URL + "mobile/index.php?act=login&op=register";
    public static final String LOGIN_ACT = BASE_URL + "mobile/index.php?act=login";
    public static final String LOGOUT_ACT = BASE_URL + "mobile/index.php?act=logout";
    public static final String CHECK_LOG_STATUS_ACT = BASE_URL + "mobile/index.php?act=login&op=login_pd";
    public static final String ADD_FAVORITE_ACT = BASE_URL + "mobile/index.php?act=member_favorites&op=favorites_add";
    public static final String DEL_FAVORITE_ACT = BASE_URL + "mobile/index.php?act=member_favorites&op=favorites_del";
    public static final String GET_FAVORITE_LIST = BASE_URL + "mobile/index.php?act=member_favorites&op=favorites_list";
    public static final String DELETE_FAVORITE_ITEM = BASE_URL + "mobile/index.php?act=member_favorites&op=favorites_del";
    public static final String GET_ORDER_LIST_ALL = BASE_URL + "mobile/index.php?act=member_order&op=order_list";
    public static final String GET_ORDER_LIST_WAITING_FOR_PAY = BASE_URL + "mobile/index.php?act=member_order&op=order_list&state=10";
    public static final String GET_ORDER_LIST_WAITING_FOR_SEND = BASE_URL + "mobile/index.php?act=member_order&op=order_list&state=20";
    public static final String GET_ORDER_LIST_WAITING_FOR_RECEIVE = BASE_URL + "mobile/index.php?act=member_order&op=order_list&state=30";
    public static final String ADD_TO_SHOPPING_CART = BASE_URL + "mobile/index.php?act=member_cart&op=cart_add";
    public static final String DEL_FROM_SHOPPING_CART = BASE_URL + "mobile/index.php?act=member_cart&op=cart_del";
    public static final String LIST_SHOPPING_CART = BASE_URL + "mobile/index.php?act=member_cart&op=cart_list&page=10000";
    public static final String LOCAL_SHOPPING_CART = BASE_URL + "mobile/index.php?act=cart_view&page=10000";
    public static final String GOPAY_FOR_ORDER = BASE_URL + "mobile/index.php?act=member_order&op=order_detail";
    public static final String REQUEST_GOOD_RETURN_GETINFO = BASE_URL + "mobile/index.php?act=member_return&op=return_application";
    public static final String REQUEST_GOOD_RETURN_SEND = BASE_URL + "mobile/index.php?act=member_return&op=return_application_submit";
    public static final String REQUEST_USER_INFO = BASE_URL + "mobile/index.php?act=member_info&op=edit_member_info";
    public static final String MODIFY_USER_INFO = BASE_URL + "mobile/index.php?act=member_info&op=edit_member_info";
    public static final String UPDATE_USER_INFO = BASE_URL + "mobile/index.php?act=member_info&op=update_member_info";
    public static final String UPLOAD_PIC = BASE_URL + "mobile/index.php?act=member_info&op=update_avatar";
    public static final String RETURN_PRODUCT_PROGRESS = BASE_URL + "mobile/index.php?act=member_return&op=return_application_list";
    public static final String CONFIRM_RECEIVING_PRODUCT = BASE_URL + "mobile/index.php?act=member_order&op=order_receive";
    public static final String HISTORY_BROWSE = BASE_URL + "mobile/index.php?act=history_browse";
    public static final String LOGISTICS_INFO = BASE_URL + "mobile/index.php?act=shipping_info&op=expressList";
    public static final String PREFERENCE_INFO = BASE_URL + "mobile/index.php?act=activity";
    public static final String PREFERENCE_CONTENT = BASE_URL + "mobile/index.php?act=activity&op=list";
    public static final String PREFERENCE_CONTENT_LIST = BASE_URL + "mobile/index.php?act=activity&op=goods_list&curpage=1&page=5&order=1";
    public static final String COMPANY_PROFILE = BASE_URL + "mobile/index.php?act=company_introduction&op=get_company_info";
    public static final String MY_COUPON = BASE_URL + "mobile/index.php?act=member_voucher&op=get_voucher_info";
    public static final String HISTORY_DELETE_SINGLE = BASE_URL + "mobile/index.php?act=history_browse&op=delOne";
    public static final String HISTORY_DELETE_ALL = BASE_URL + "mobile/index.php?act=history_browse&op=delAll";
    public static final String SELECT_COUPON = BASE_URL + "mobile/index.php?act=voucher";
    public static final String COMMENT_LSIT = BASE_URL + "mobile/index.php?act=member_evaluate&op=list";
    // cancel order
    public static final String CANCEL_ORDER_LINK = BASE_URL + "mobile/index.php?act=member_order&op=order_cancel";
    public static final String SUBMIT_COMMENT = BASE_URL + "mobile/index.php?act=member_evaluate&op=index";
    public static final String CHECK_UPGRADE = BASE_URL + "mobile/index.php?act=uversion&client=android";
    public static final String PAY_FOR_WEIXIN = BASE_URL + "mobile/api/payment/wxpay/redirect_uri.php?";
    public static final String NEAR_STORE = BASE_URL + "mobile/index.php?act=nearby_shop";

    //我的分销
    public static final String MYDISTRIBUTION = BASE_URL + "mobile/index.php?act=member_info&op=edit_member_info";

    //申请分销
    public static final String APPDISTRIBUTION = BASE_URL + "mobile/index.php?act=mregister&op=mreg";

    //二维码退推广
    public static final String SHARE_QRCODE = BASE_URL + "mobile/index.php?act=spread&op=spreadurl";


    //二维码退推广
    public static final String SHARE_URL = BASE_URL + "mobile/index.php?act=spread&op=spreadewm";


    //我的佣金
    public static final String MYCOMMISSION = BASE_URL + "mobile/index.php?act=commission&op=index";

    //版本更新
    public static final String UPDATE_APK = BASE_URL + "mobile/index.php?act=uversion&client=android&ifupdate=1";

    //到店自取
    public static final String GET_IN_SHOP = BASE_URL + "mobile/index.php?act=member_buytoshop&op=show_stocklist";
}

