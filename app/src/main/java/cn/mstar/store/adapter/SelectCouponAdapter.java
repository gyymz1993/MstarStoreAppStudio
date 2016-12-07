package cn.mstar.store.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.entity.SelectCouponEntity;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class SelectCouponAdapter extends BaseAdapter {
    private Context mContext;
    private List<SelectCouponEntity> mData;
    private boolean isUseable = true;
    private OnItemClickCallBackListener mOnItemClickCallBackListener;

    public SelectCouponAdapter(Context mContext, List<SelectCouponEntity> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void changeData(List<SelectCouponEntity> mData, boolean isUseable) {
        this.mData = mData;
        this.isUseable = isUseable;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public SelectCouponEntity getItem(int position) {
        return (SelectCouponEntity) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_wcl, parent, false);
            vh.coupon_bg = (ImageView) convertView.findViewById(R.id.coupon_item_bg);
            vh.coupon_num = (TextView) convertView.findViewById(R.id.coupon_num);
            vh.coupon_com = (TextView) convertView.findViewById(R.id.coupon_com_name);
            vh.coupon_condition = (TextView) convertView.findViewById(R.id.coupon_condition);
            vh.coupon_use_range = (TextView) convertView.findViewById(R.id.use_range);
            vh.coupon_use_range_valve = (TextView) convertView.findViewById(R.id.coupon_use_range);
            vh.coupon_use_flag = (TextView) convertView.findViewById(R.id.coupon_use_flag);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.coupon_bg.setImageResource(R.drawable.coupons_yellow);
        vh.coupon_use_range.setTextColor(mContext.getResources().getColor(R.color.color_black_st));
        vh.coupon_use_range_valve.setTextColor(mContext.getResources().getColor(R.color.color_black_st));
        vh.coupon_use_flag.setTextColor(mContext.getResources().getColor(R.color.color_black_st));
        vh.coupon_use_flag.setText(mContext.getResources().getString(R.string.coupon_no_use));
        vh.coupon_num.setText(getItem(position).getPrice() + "");
        vh.coupon_com.setText(getItem(position).getStoreName());
        vh.coupon_condition.setText(getItem(position).getVoucherInfo());
        vh.coupon_use_range_valve.setText(getItem(position).getEndtime());

        vh.coupon_condition.setTextSize(atuoChangeTextViewSize(vh.coupon_condition, 140, 16));
        vh.coupon_com.setTextSize(atuoChangeTextViewSize(vh.coupon_com, 140, 16));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUseable){
                    mOnItemClickCallBackListener.onClickCallBack(getItem(position));
                }
            }
        });
        return convertView;
    }

    /**
     * 自动改变字体大小
     *
     * @param textView     要改变的TextView
     * @param limit        限制长度 单位dp
     * @param initTextSize 初始字体大小 单位sp
     * @return 返回字体大小
     */
    private float atuoChangeTextViewSize(TextView textView, int limit, float initTextSize) {
        Paint paint = new Paint();
        float length = 0;
        float textSize = initTextSize + 1;
        String content = textView.getText().toString();
        do {
            textSize = textSize - 1;
            paint.setTextSize(textSize);
            length = paint.measureText(content);
        } while (length > limit);
        return textSize;
    }

    class ViewHolder {
        ImageView coupon_bg;
        TextView coupon_num;
        TextView coupon_com;
        TextView coupon_condition;
        TextView coupon_use_range;
        TextView coupon_use_range_valve;
        TextView coupon_use_flag;
    }

    public interface OnItemClickCallBackListener {
        void onClickCallBack(SelectCouponEntity entity);
    }

    public void setOnItemClickCallBackListener(OnItemClickCallBackListener mOnItemClickCallBackListener) {
        this.mOnItemClickCallBackListener = mOnItemClickCallBackListener;
    }
}
