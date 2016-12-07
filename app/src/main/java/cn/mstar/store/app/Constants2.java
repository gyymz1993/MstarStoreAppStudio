package cn.mstar.store.app;

import android.os.Environment;

public class Constants2 {

	public final static String BASE_URL0 = "http://m.fanershop.com/index.php?";

	public final static String BASE_URL="http://www.fanershop.com/mobile/index.php?";

	// x首页路径
	public static final String IMAGE_URL = BASE_URL+"act=home";

	// 搜索路径,产品列表路径
	public static final String SEARCH_URL =  BASE_URL+"act=goodsb&op=goods_list";

	// x产品详情路径
	public static final String PRODUCTDETAIL_URL =  BASE_URL+"act=goodsb&op=goods_detail";

	// 筛选路径
	public static final String SEARCH_FILTERCLASS_URL =  BASE_URL+"/App/FilterClassType";

	//x 更多分类路径
	public static final String MORECLASSIFY_URL =  BASE_URL+"act=goods_list";

	// x商品规格选择路径
	public static final String CUSTOM_MADE_URL =  BASE_URL+"act=goods_spc";

	// 未登陆购物篮路径
	public static final String SHOP_CART_URL =  BASE_URL+"/App/CartView/Temp";

	//商品筛选接口
	public static final String PRODUCT_FILTER=BASE_URL+"/App/ProductList/FilterClassList?";

	//x选择区域
	public static final String SELECT_PROVINCE_URL=BASE_URL+"act=area_list";
	//选择城市接口
	public static final String SELECT_CITY_URL=BASE_URL+"/App/AreaAddress/city?";
	//选择区县接口
	public static final String SELECT_AREA_URL=BASE_URL+"/App/AreaAddress/county?";
	//获取用户地址管理模板
	public static final String GET_USER_MANAGE_MOUDLE=BASE_URL+"/App/UserAddress/entity";
	//x获取用户地址list
	public static final String GET_USER_ADDRESS_LIST=BASE_URL+"act=member_address&op=address_list";
	//x添加收货地址
	public static final String ADD_RECEIVER_ADDRESS=BASE_URL+"act=member_address&op=address_add";
	//x修改收货地址
	public static final String EDIT_RECEIVER_ADDRESS=BASE_URL+"act=member_address&op=address_edit";
	//x删除收货地址
	public static final String DELETE_RECEIVER_ADDRESS=BASE_URL+"act=member_address&op=address_del";
	//x设置默认地址
	public static final String SET_DEFAULT_RECEIVER_ADDRESS=BASE_URL+"act=member_address&op=address_default";
	//管理用户地址
	public static final String POST_USER_MANAGE_ADDRESS=BASE_URL+"/App/UserAddress/post";
	//获取设置默认地址模型
	public static final String GET_SET_DEFAULT_ADDRESS_MODEL=BASE_URL+"/App/UserAddress/setdefaultentity";
	//提交用户默认地址
	public static final String POST_USER_DEFAULT_ADDRESS=BASE_URL+"/App/UserAddress/setdefaultpost";

	//是否登录
	public static final String IS_LOGGED_LINK = BASE_URL+"/App/UserAction/islogin";

	//x登录接口
	public static final String LOGIN_URL=BASE_URL+"act=login";

	//x立即订购页,获取产品信息和默认地址
	public static final String ORDER_SHOW_NOW=BASE_URL+"act=member_buy_now&op=buy_now";
	//x立即订购也，获取产品信息和默认地址 从购物篮传过来时
	public static final String CART_SHOW_NOW=BASE_URL+"act=shop_basket&op=Commit";
	//x提交立即购买订单
	public static final String POST_INDENT_URL=BASE_URL+"act=member_buy_now&op=create_order";
	//x立即订购页，提交购物篮传过来的，提交订单
	public static final String POST_CART_INDENT_URL=BASE_URL+"act=shop_basket&op=mobileCreateOrder";
	// 订单以生产、立即购买
	public static final String POST_BUY_NOW_INDENT_URL = BASE_URL+"act=member_buy_now&op=buy_now";
	//x修改密码
	public static final String CHANGE_PASSWORD_URL=BASE_URL+"act=member_info&op=update_member_password";
	//x获取验证码
	public static final String GET_AUTH_CODE_URL=BASE_URL+"act=getcode";
	//x比对验证码
	public static final String EQUELSE_AUTH_CODE_URL=BASE_URL+"act=member_password&op=password_find";
	//x重置密码
	public static final String RESET_PASSWORD_URL=BASE_URL+"act=member_password&op=password_reset";
	//x获取物流信息
	public static final String GET_LOGISTICS_INFO_URL=BASE_URL+"act=shipping_info&op=return_application";

	//获取订单模型
	public static final String GET_INDENT_MODEL=BASE_URL+"/App/GoToPay/entity";
	//提交立即购买订单
	public static final String COMMIT_INDENT=BASE_URL+"/App/GoToPay/nowpost";

	// 保存参数文件夹名称
	public static final String SHARED_PREFERENCE_NAME = "mstar_store_prefs";


	// SDCard路径

	public static final String SD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	// 图片存储路径
	public static final String BASE_PATH = SD_PATH + "/mstar/store/";

	// 缓存图片路径
	public static final String BASE_IMAGE_CACHE = BASE_PATH + "cache/images/";

	// 需要分享的图片
	public static final String SHARE_FILE = BASE_PATH + "QrShareImage.png";


	//附近门店的信息
	public static final String NEARSTORE_URL=BASE_URL+"act=nearby_shop";
}
