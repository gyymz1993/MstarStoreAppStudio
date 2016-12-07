package cn.mstar.store.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import cn.mstar.store.interfaces.ScrollInterface;

/**
 * Created by jimubox on 5/26/2015.
 * viewpager兼容
 */
public class CompatibilityViewPagerScrollView extends ScrollView {
    private boolean canScroll;
    private GestureDetector mGestureDetector;
    private ScrollInterface scrollInterface;
    public CompatibilityViewPagerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);
        canScroll = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        // TODO Auto-generated method stub
//        if (ev.getAction() == MotionEvent.ACTION_UP)
//            canScroll = true;
//        return super.onInterceptTouchEvent(ev)
//                && mGestureDetector.onTouchEvent(ev);
        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:
                startX = (int) getX();
                startY = (int) getY();

            case MotionEvent.ACTION_UP:
                endX = (int) getX();
                endY = (int) getY();

        }
        if ((Math.abs(endY - startY)- Math.abs(endX - startX))>0){
            return true;
        }
        return false;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    int startX;
    int startY;
    int endX;
    int endY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()){
//
//            case MotionEvent.ACTION_DOWN:
//                startX = (int) getX();
//                startY = (int) getY();
//
//            case MotionEvent.ACTION_UP:
//                endX = (int) getX();
//                endY = (int) getY();
//
//        }
//        if ((Math.abs(endY-startY)-Math.abs(endX-startX))>0){
//            return true;
//        }
//        return false;
        return true;
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (canScroll)
                if (Math.abs(distanceY) >= Math.abs(distanceX))
                    canScroll = true;
                else
                    canScroll = false;
            return canScroll;
        }

    }
    public void setOnCustomScroolChangeListener(ScrollInterface scrollInterface){
        this.scrollInterface =scrollInterface;
    }
}
