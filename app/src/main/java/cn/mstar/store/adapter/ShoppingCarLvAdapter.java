package cn.mstar.store.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.mstar.store.R;
import cn.mstar.store.activity.MainActivity;
import cn.mstar.store.activity.MyShoppingCarActivity;
import cn.mstar.store.activity.ProductDetailsActivity;
import cn.mstar.store.app.MyAction;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.CustomListeningTextview;
import cn.mstar.store.entity.ShoppingCartItem;
import cn.mstar.store.interfaces.OnResultStatusListener;
import cn.mstar.store.utils.CustomToast;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.VolleyRequest;

public class ShoppingCarLvAdapter extends BaseAdapter {


    private static final String POSITION = "position";


    Context mContext;
    List<ShoppingCartItem> data;
    LayoutInflater inf;
    private int SCREEN_WIDTH;
    private MyApplication app;


    // need to build a shopping car items
    public ShoppingCarLvAdapter(MainActivity activity, List<ShoppingCartItem> dt, int scw) {


        this.mContext = activity;
        this.app = (MyApplication) activity.getApplication();
        this.data = new ArrayList<>(dt);
        inf = LayoutInflater.from(mContext);
        SCREEN_WIDTH = scw;
        checkedState = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            checkedState.put(POSITION + i, false);
        }
    }

    // at the start, any of the views is selected.
    // create a list that will key the states.

    void removeAt(int position) {

        if (data != null) {
            data.remove(position);
            data = new ArrayList<>(data);
            checkedState.remove(POSITION + position);
            updateBottomTotal();
            if (data != null && data.size() == 0)
                ((MyShoppingCarActivity) mContext).setNoResult(true);
            notifyDataSetChanged();
        }
    }

    private void updateBottomTotal() {

        // 通过context去改变fragment里的数据
        // 计算总计

        if (checkedState != null) {

            List<ShoppingCartItem> checkedGoods = new ArrayList<>();
            for (String key : checkedState.keySet()) {

                if (checkedState.get(key) != null && checkedState.get(key) == true) {
                    int i = Integer.valueOf(key.substring(POSITION.length()));
                    ShoppingCartItem cartItem = data.get(i);
                    checkedGoods.add(cartItem);
                    L.d("III " + i, cartItem.toString());
                }
            }
            ((MainActivity) mContext).updateBottomTotal(checkedGoods);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        final ViewHolder vh;
        if (convertView == null) {
            view = inf.inflate(R.layout.shopping_car_item, null);
            vh = new ViewHolder(view);
        } else {
            view = convertView;
            vh = (ViewHolder) convertView.getTag();
        }
        boolean isChecked = false;
        if (checkedState != null && checkedState.containsKey(POSITION + position)) {
            isChecked = checkedState.get(POSITION + position);
        }

        final ShoppingCartItem item = (ShoppingCartItem) getItem(position);

        LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) vh.tv_title.getLayoutParams();
        params.width = SCREEN_WIDTH / 2;
        vh.tv_title.setLayoutParams(params);

        vh.ck_category_name.setChecked(isChecked);

        vh.tv_title.setText(item.name);
//        vh.tv_shopName.setText(item.);
        vh.tv_product_commodities.setText(item.specialTitle);
        vh.tv_title.setText(item.name);
        vh.tv_unit_price.setText(Double.toString(item.price));
        // there will be another count value comming from the local.

        if (item.number != 0) {
            L.d("GGG", "position = " + position + "  --- itemNumbers = " + item.number);
            vh.tv_item_count_static.setText("X" + item.number);
            vh.tv_item_count_dynamic.setText("" + item.number);
        }
        ImageLoader.getInstance().displayImage(item.pic, vh.iv_item_pic, ImageLoadOptions.getOptions());

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithId(item.proId);
            }
        });

        vh.ck_category_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedState.put(POSITION + position, vh.ck_category_name.isChecked());
                updateBottomTotal();
            }
        });

        vh.iv_delete_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                delete(position);
            }
        });
        // left and right buttons.
        vh.iv_sous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int val = Integer.valueOf(vh.tv_item_count_dynamic.getText().toString());
                if (val > 1) {
                    val--;
                    vh.tv_item_count_dynamic.setText(val + "");
                    updateText(val + "", position, vh.tv_item_count_dynamic, vh.tv_item_count_static);
//					L.d("GGG", "pressed " + position + " ivsous " + val+" --- "+data.get(position).number);
                    updateBottomTotal();
                }
            }
        }/*(MyShoppingCarActivity)mContext*/);
        vh.iv_plus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int val = Integer.valueOf(vh.tv_item_count_dynamic.getText().toString());
                if (val < item.stock) {
                    val++;
//					L.d("GGG", "pressed" + position + " ivplus " + val + " --- " + data.get(position).number);
                    vh.tv_item_count_dynamic.setText(val + "");
                    updateText(val + "", position, vh.tv_item_count_dynamic, vh.tv_item_count_static);
                    updateBottomTotal();
                } else {
                    CustomToast.makeToast(mContext, mContext.getString(R.string.outofstock), Toast.LENGTH_SHORT);
                }
            }
        });
//		vh.tv_item_count_dynamic.setOnTextChangedListener(new OnTextChangedListener() {
//			@Override

//		});


        // get the data ~ make up the data and send back the view.
        view.setTag(vh);
        return view;
    }


    protected void startActivityWithId(int proId) {

        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        intent.putExtra("proId", proId);
        intent.setAction(MyAction.productListActivityAction);
        mContext.startActivity(intent);
    }

    public void updateText(String string, int position, TextView tv_item_count_dynamic, TextView tv_item_count_static) {
        String s = tv_item_count_dynamic.getText().toString();
        tv_item_count_static.setText("X " + s);
//				item.number = Integer.valueOf(s);
        // update it inside the database.
        updateDataSet(position, Integer.valueOf(s));
        updateBottomTotal();
    }

    private void updateDataSet(int position, int newCount) {

        // only the number count must be diferent.
        ShoppingCartItem cartItem = data.get(position);
        if (newCount != cartItem.number) {
            cartItem.number = newCount;
            if (position < data.size()) {
//				L.d("VVV", "updating "+item.toString()+" \nto "+cartItem.toString());
                data.set(position, cartItem);
                notifyDataSetChanged();
            }
        }
    }

    Dialog alertDialog = null;


    public void delete(List<ShoppingCartItem> itemz) {


        // retrieve their id buy parcouring the itemz.
        for (ShoppingCartItem item : itemz
                ) {

            // get the id inside the data.

        }

    }

    public void delete(final int position) {
        final ShoppingCartItem item = (ShoppingCartItem) getItem(position);
        final AlertDialog mAlertDialog = new AlertDialog.Builder(mContext).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.scan_history_dialog, null, false);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        dialog_title.setText("你确定要删除这个记录吗？");
        mAlertDialog.setView(view);
        mAlertDialog.show();
        view.findViewById(R.id.scan_history_cancel_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        view.findViewById(R.id.scan_history_confirm_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyRequest.checkLogStatus(app, new VolleyRequest.LogonStatusLinstener() {
                    @Override
                    public void OK(String reason) {
                        // 从网络上删掉
                        VolleyRequest.deleteShoppingCartItem(app, item, new OnResultStatusListener() {

                            public void success(String result) {
                                // 添加成功
                                dismissDialog();
                                CustomToast.makeToast(mContext, mContext.getString(R.string.del_shopping_cart_success), Toast.LENGTH_SHORT);
                                ((MainActivity)(mContext)).inflateDatas();
                                removeAt(position);
                                mAlertDialog.dismiss();
                            }

                            @Override
                            public void failure(String error) {
                                switch (error) {
                                    case "0":
                                        // 添加失败
                                        CustomToast.makeToast(mContext, mContext.getString(R.string.del_shopping_cart_failure), Toast.LENGTH_SHORT);
                                        break;
                                    case "1":
                                        // 网络失败
                                        CustomToast.makeToast(mContext, mContext.getString(R.string.network_error), Toast.LENGTH_SHORT);
                                        break;
                                }
                                mAlertDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void NO() {
                        // 本地删除
                        CustomToast.makeToast(mContext, mContext.getString(R.string.delete_error), Toast.LENGTH_SHORT);
                    }
                });
            }
        });
        /*alertDialog = new Dialog(mContext);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.confirmation_dialog_box);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.findViewById(R.id.tv_bt_cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.findViewById(R.id.tv_bt_confirm).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // delete it from the local or from the net depending on the connection state.
                VolleyRequest.checkLogStatus(app, new VolleyRequest.LogonStatusLinstener() {
                    @Override
                    public void OK(String reason) {
                        // 从网络上删掉
                        VolleyRequest.deleteShoppingCartItem(app, item, new OnResultStatusListener() {

                            public void success(String result) {
                                // 添加成功
                                dismissDialog();
                                CustomToast.makeToast(mContext, mContext.getString(R.string.del_shopping_cart_success), Toast.LENGTH_SHORT);
                                removeAt(position);
                            }

                            @Override
                            public void failure(String error) {
                                switch (error) {
                                    case "0":
                                        // 添加失败
                                        CustomToast.makeToast(mContext, mContext.getString(R.string.del_shopping_cart_failure), Toast.LENGTH_SHORT);
                                        break;
                                    case "1":
                                        // 网络失败
                                        CustomToast.makeToast(mContext, mContext.getString(R.string.network_error), Toast.LENGTH_SHORT);
                                        break;
                                }
                            }
                        });
                    }

                    @Override
                    public void NO() {
                        // 本地删除
                        CustomToast.makeToast(mContext, mContext.getString(R.string.delete_error), Toast.LENGTH_SHORT);
                    }
                });
                alertDialog.dismiss();
            }
        });
        // and send the request to the db*/
    }

    private void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog.cancel();
            alertDialog = null;
        }
    }

    private Map<String, Boolean> checkedState;

    public void checkAll(boolean isChecked) {

        if (checkedState == null) {
            checkedState = new HashMap<>();
        }
        for (int i = 0; i < getCount(); i++) {
            checkedState.put(POSITION + i, isChecked);
        }
        updateBottomTotal();
    }


    public void addAll(List<ShoppingCartItem> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        for (ShoppingCartItem shoppingCartItem : data) {
            data.add(shoppingCartItem);
            notifyDataSetChanged();
        }
    }


    class ViewHolder {

        @Bind(R.id.ck_radiobutton_category_name)
        CheckBox ck_category_name;
        @Bind(R.id.shopping_car_shopname) TextView tv_shopName;
        @Bind(R.id.iv_image)
        public ImageView iv_item_pic;
        @Bind(R.id.iv_icon_del)
        public ImageView iv_delete_icon;
        @Bind(R.id.btn_commodity_plus)
        public ImageView iv_plus;
        @Bind(R.id.btn_commodity_minus)
        public ImageView iv_sous;
        @Bind(R.id.tv_item_name)
        public TextView tv_title;
        @Bind(R.id.tv_item_price)
        public TextView tv_unit_price;
        @Bind(R.id.tv_item_count_static)
        public TextView tv_item_count_static;
        @Bind(R.id.btn_commodity_number_display)
        public CustomListeningTextview tv_item_count_dynamic;
        @Bind(R.id.tv_product_commodities)
        public TextView tv_product_commodities;


        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

    }

}
