package cn.mstar.store.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class TextViewWithUnderLine extends TextView {
    private float underlineWidth;
    public TextViewWithUnderLine(Context context) {
        super(context);
    }

    public TextViewWithUnderLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        underlineWidth = attrs.getAttributeResourceValue(null,"underlineWidth",5);
    }

    public TextViewWithUnderLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        underlineWidth = attrs.getAttributeResourceValue(null,"underlineWidth",5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        Paint paint = getPaint();
        paint.setStrokeWidth(underlineWidth);
        canvas.drawLine(0,getMeasuredHeight(),getMeasuredWidth(),getMeasuredHeight(),paint);
        canvas.restore();
    }
}
