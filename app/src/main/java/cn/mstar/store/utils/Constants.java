package cn.mstar.store.utils;

/**
 * Created by Ultima on 7/11/2015.
 */
public class Constants {




	public static final String START_ACTIVITY_FOR_RESULT_KEY = "MESSAGE";

	public static final int
	ALL = 1,
	WAITINGPAY = 2,
	WAITINGSENDING = 3,
	WAITINGRECEIVING = 4,
	WAITINGRETRIEVING = 5;


	public static  final int[] GOODS_MENU_TAB_POSITION = {ALL, WAITINGPAY, WAITINGSENDING, WAITINGRECEIVING, WAITINGRETRIEVING};

	public static final int ALREADY_USED = 3;

	public static final int EXPIRED = 4;

	public static final int NOT_USED = 2;

	public static  final int[] REDUCTION_TICKETS_TAB_POSITION = {ALL, NOT_USED, ALREADY_USED, EXPIRED};

//
	public static final String IPADRESS = "192.168.1.47:8082";

//	192.168.1.47:8082//App/UserOrder/WaitPay?page=1



	// 获取注册验证码链接


	public static final String REGISTERING_ENTITY_LINK = "http://"+IPADRESS+"/App/UserRegister/post";


	//


	// 通过mainactivity登录的intent action
	public static final String SHARED_PREFS_NAME = "GO_SEE_SOMEWHERE_ELSE";

	/*
    退出登录:http://192.168.1.240:8083/App/UserAction/out
是否已经登录:http://192.168.1.240:8083/App/UserAction/islogin
提交用户登录模型:http://192.168.1.240:8083/App/UserAction/entity
	 */

	public static final String UNLOG_LINK = "http://"+IPADRESS+"/App/UserAction/out";
	public static final String IS_LOGGED_LINK = "http://"+IPADRESS+"/App/UserAction/islogin";
	public static final String LOGIN_CLEAN_ENTITY = "http://"+IPADRESS+"/App/UserAction/entity";



	public static final String LOGIN_SUCCESS_USERNAME = "USERNAME";
	public static final String MENU_POSITION = "MESSAGE";

	public static final String SP_USERNAME = "SPUSERNAME";

	public static final String SP_PASSWORD = "SPUSERPASSWORD";
	public static final String SP_TOKENKEY = "SPTOKENKEY";

	public static final String LOGOUT_LINK = "http://"+IPADRESS+"/App/UserAction/out";

	public static final String UNLOGGED = "UNLOGGED";
	public static final String POINTS = "POINTS";
	public static String PIC = "PIC";


	public enum Day {
		WAITING_PAYMENT, WAITING_RECEIVING, WAIT_RETRIEVING, WAIT_SENDING;
	}



}
