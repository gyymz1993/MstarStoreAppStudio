package cn.mstar.store.functionutils;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import cn.mstar.store.R;

public class DialogUtils {
	// 左中右三个弹出窗口
	public static PopupWindow popLeft;

	/**
	 * @param listVieLayout  ListView的父布局
	 * @param rlTopBar  选择选项的  layout
	 */
		public  void setDialogList(View listVieLayout,View tvLeft,ViewGroup rlTopBar){
		// 创建弹出窗口
		// 窗口内容为layoutLeft，里面包含一个ListView
		// 窗口宽度跟tvLeft一样
		popLeft = new PopupWindow(listVieLayout, tvLeft.getWidth(),
				LayoutParams.WRAP_CONTENT);
	//	ColorDrawable cd = new ColorDrawable(-0000);
	//	popLeft.setBackgroundDrawable(cd);
		popLeft.setAnimationStyle(R.style.PopupAnimation);
		popLeft.update();
		popLeft.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popLeft.setTouchable(false); // 设置popupwindow可点击
		popLeft.setOutsideTouchable(true); // 设置popupwindow外部可点击
		popLeft.setFocusable(true); // 获取焦点
		// 设置popupwindow的位置（相对tvLeft的位置）
		int topBarHeight = rlTopBar.getBottom();
			popLeft.showAsDropDown(tvLeft, 0,
					(topBarHeight - tvLeft.getHeight()) / 2);
		popLeft.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 如果点击了popupwindow的外部，popupwindow也会消失
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					//popLeft.dismiss();
					return true;
				}
				return false;
			}
		});
	}

	public boolean isShow(){
		if(popLeft != null && popLeft.isShowing()){
			return true;
		}
		return false;
	}

	public void dismiss(){
		if(popLeft != null && popLeft.isShowing()){
			popLeft.dismiss();
		}

	}
}
