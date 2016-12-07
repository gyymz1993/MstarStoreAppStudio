package cn.mstar.store.activity;
import cn.mstar.store.R;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.functionutils.LoginRequest;
import cn.mstar.store.functionutils.SpUtils;
import cn.mstar.store.functionutils.StatisticsUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

 /**
  * @category 欢迎页
  * @author wenjundu 2015-07-02
  *
  */
public class WelcomeActivity extends BaseActivity {

	private SharedPreferences preferences;
	//是否第一次进入
	private boolean isFrist;
	private final int GO_TO_FIRSTIN_ACTIVITY=0x0002;
	private final int GO_TO_MAIN_ACTIVITY=0x0003;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view=View.inflate(this, R.layout.activity_welcome, null);
		setContentView(view);
		MyApplication.getInstance().addActivity(this);
		preferences=getSharedPreferences("welcome", MODE_PRIVATE);
		isFrist=preferences.getBoolean("isFrist", true);
		
		// 渐变展示启动屏,这里通过动画来设置了开启应用程序的界面
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);
		// 给动画添加监听方法
		aa.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationEnd(Animation arg0)
			{
				if(isFrist){
					preferences.edit().putBoolean("isFrist", false).commit();
					myHandle.sendEmptyMessage(GO_TO_FIRSTIN_ACTIVITY);
				}
				else{
					myHandle.sendEmptyMessage(GO_TO_MAIN_ACTIVITY);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationStart(Animation animation)
			{
			}
		});


		StatisticsUtils.getInstance().init();
		//autoLogin();
	}

	 /**
	  * 自动登陆
	  */
	 public void autoLogin(){
		 String username = MyApplication.spUtils.getString(SpUtils.key_username);
		 String passwd = MyApplication.spUtils.getString(SpUtils.key_password);
		 SpUtils.LOGIN_USERNAME=username;
		 SpUtils.LOGIN_PASSWORD=passwd;
		 LoginRequest.startLogin(WelcomeActivity.this, username, passwd);

	 }


	 @SuppressLint("HandlerLeak")
	private Handler myHandle=new Handler(){ 
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Intent intent=null;
			switch (msg.what) {
			case GO_TO_FIRSTIN_ACTIVITY:
				//第一次就进入引导页
				intent=new Intent(WelcomeActivity.this,GuideActivity.class);
				break;

			case GO_TO_MAIN_ACTIVITY:
				intent=new Intent(WelcomeActivity.this,MainActivity.class);
				break;
			}
			if(intent!=null)
			{
				startActivity(intent);
				finish();
			}
			
		}
	 
 };

	 @Override
	 protected void onResume() {
		 super.onResume();
		 StatisticsUtils.getInstance().onResume(WelcomeActivity.this,"WelcomeActivity");
	 }

	 @Override
	 protected void onPause() {
		 super.onPause();
		 StatisticsUtils.getInstance().onPause(WelcomeActivity.this, "WelcomeActivity");
	 }
 }
