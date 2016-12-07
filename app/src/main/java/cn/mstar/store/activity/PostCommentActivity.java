package cn.mstar.store.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.mstar.store.R;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.GridViewForScrollView;
import cn.mstar.store.entity.BuyProductEntity;
import cn.mstar.store.entity.JsonResult;
import cn.mstar.store.functionutils.BimpUtils;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Until;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 *
 *  @action:商品评价管理
 *  @author:  YangShao
 *  @date: 2015/10/26 @time: 12:26
 */
public class PostCommentActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_CAMER = 2;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind({R.id.ck_good, R.id.ck_fair, R.id.ck_bad})
    List<RadioButton> rb;
    String[] group;
    @Bind(R.id.comment_product_img) ImageView iv_product_img;
    @Bind(R.id.product_name) TextView tv_product_name;
    @Bind(R.id.product_norms) TextView tv_product_norms;
    @Bind(R.id.btn_camera) ImageView iv_btn_camera;
    @Bind(R.id.editext) EditText edittext;
    @Bind(R.id.comment_gridView)
    GridViewForScrollView comment_gridView;

    // vars
    BuyProductEntity entity;
    Context mContext;
    String path = "";
    MyAdapter ImgAdapter;
    //图片
     public  static List<Bitmap> imgList = new ArrayList<Bitmap>();

    //文件路径
    public static List<String> filePaths=new ArrayList<String>();
    ImageView ivDelete;
    boolean isShowDelete=false;
    private MyApplication app;
    private int scores;
    private String content;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        ButterKnife.bind(this);
        app = (MyApplication) getApplication();
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        entity = (BuyProductEntity) getIntent().getSerializableExtra("data");
        orderId = getIntent().getStringExtra("orderid");
        inflateTopViews();
        mContext = this;
        ImgAdapter = new MyAdapter();
        ImgAdapter.setIsShowDelete(isShowDelete);
        comment_gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        comment_gridView.setAdapter(ImgAdapter);
        group = getResources().getStringArray(R.array.comment_group);
        comment_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //选择添加照片
                if(position==(imgList.size())){
                    selectimg();
                }else{
                    Intent intent = new Intent(PostCommentActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup grp, int checkedId) {
                int i = 0;
                switch (checkedId) {
                    case R.id.ck_bad:
                        i = 2;
                        rb.get(2).setTextColor(getResources().getColor(R.color.white));
                        rb.get(1).setTextColor(getResources().getColor(R.color.general_radiobtn));
                        rb.get(0).setTextColor(getResources().getColor(R.color.good_radiobtn));
                        break;
                    case R.id.ck_fair:
                        i = 1;
                        rb.get(1).setTextColor(getResources().getColor(R.color.white));
                        rb.get(2).setTextColor(getResources().getColor(R.color.bad_radiobtn));
                        rb.get(0).setTextColor(getResources().getColor(R.color.good_radiobtn));
                        break;
                    case R.id.ck_good:
                        i = 0;
                        rb.get(0).setTextColor(getResources().getColor(R.color.white));
                        rb.get(1).setTextColor(getResources().getColor(R.color.general_radiobtn));
                        rb.get(2).setTextColor(getResources().getColor(R.color.bad_radiobtn));
                        break;
                }
            }
        });
        radioGroup.check(R.id.ck_good);
        iv_btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             // 添加图片
              selectimg();
            }
        });
        setupTitle(getString(R.string.product_comment));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ImgAdapter!=null){
            ImgAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 照片选择器
     */
    private void selectimg() {
        final CharSequence[] items = { "拍照上传", "从相册选择" };
        new AlertDialog.Builder(this).setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == SELECT_PICTURE) {
                            toGetLocalImage();
                        } else {
                            toGetCameraImage();
                        }
                    }
                }).create().show();

    }




    /**
     * 更改状态
     * @param string
     */
    private void setupTitle(String string) {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(string);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm = null;
        String path=null;
        if (requestCode == 1 && data != null) {
            String[] all_path = data.getStringArrayExtra("all_path");
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //从相册选择
                case SELECT_PICTURE:
                    Uri vUri = data.getData();
                    // 将图片内容解析成字节数组
                    String[] proj = { MediaStore.Images.Media.DATA };
                    Cursor cursor = managedQuery(vUri, proj, null, null, null);
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    path = cursor.getString(column_index);
                    break;
                //拍照添加图片
                case SELECT_CAMER:
                    path=out.getAbsolutePath();
                    break;
            }
            //Toast.makeText(this, "图片路径"+path, Toast.LENGTH_SHORT).show();
            if (filePaths.size()>=5){
                Toast.makeText(this, "最多选择5张图片", Toast.LENGTH_SHORT).show();
                return;
            }
            if(filePaths.size()!=0){
                for(String file:filePaths){
                    if(path.equals(file)){
                        maketoast("照片已存在");
                        return;
                    }
                }
            }
            if (filePaths.size()>=5){
                maketoast("最多只能选择五张照片");
                return ;
            }
            bm = Until.getxtsldraw(mContext, path);
            Bitmap zipBp = BimpUtils.getInstace().compressImage(bm,1000);
            String bitFilePaht = BimpUtils.saveImg(zipBp, getPhotoNamebyDate());
            filePaths.add(bitFilePaht);
            //filePaths.add(path);
            imgList.add(bm);
            ImgAdapter.notifyDataSetChanged();

        }
    }

    @OnClick (R.id.bt_send_comment)
    public void sendComment() {
        content = edittext.getText().toString();
        if (content != null && content.length() < 3) {
            maketoast(getString(R.string.comment_not_less_than_3));
            return;
        }
        switch (scores) {
            case 1:
                scores = 3;
                break;
            case 3:
                scores = 1;
                break;
        }
        sendImgToServer(filePaths);

    }

    public void sendImgToServer(final List<String> filePaths){
        {
            HttpUtils http = new HttpUtils();
            HttpRequest.HttpMethod method = HttpRequest.HttpMethod.POST;
            RequestParams params=new RequestParams();
            maketoast("key" + Utils.getTokenKey(app));
            LogUtils.i("请求key" + Utils.getTokenKey(app));
            params.addBodyParameter("key",Utils.getTokenKey(app));
            params.addBodyParameter("OrderNum",orderId);
            params.addBodyParameter("goods_id",entity.getProduct().getProId()+"");
            params.addBodyParameter("scores",scores+"");
            params.addBodyParameter("content",content+"");
            int count=0;
            if(filePaths!=null&&filePaths.size()!=0){
                for(String filePath:filePaths){
                    //params.addBodyParameter("attachment"+count, new File(filePath), "multipart/form-data");
                    LogUtils.i("key"+"attachment"+count+"vaule"+filePath);
                    params.addBodyParameter("attachment"+count, new File(filePath), "multipart/form-data");
                   // Toast.makeText(this, "图片路径"+"数量"+count+"路径"+filePath, Toast.LENGTH_SHORT).show();
                   // LogUtils.i("上传中" + "图片路径" + "数量" + count + "路径" + filePath);
                    count++;
                }

            }
            http.send(HttpRequest.HttpMethod.POST, NewLink.SUBMIT_COMMENT, params, new RequestCallBack<String>() {
                @Override
                public void onStart() {

                }
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    String text="upload: " + current + "/"+ total;
                    // maketoast("上传中"+text);
                    LogUtils.i( "上传中"+text);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    LogUtils.i(responseInfo.result);
                    Gson gson = new Gson();
                    try {
                        JsonResult jsonResult = null;
                        jsonResult = gson.fromJson(responseInfo.result, JsonResult.class);
                        maketoast("功能吗"+jsonResult.getError());
                        if (jsonResult.getError().equals("0")) {
                            maketoast(getString(R.string.send_comment_success));
                            LogUtils.i("请求成功");
                            finish();
                        } else {
                            LogUtils.i("请求失败"+jsonResult.getMessage());
                            maketoast("请求失败" + jsonResult.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.i("返回数据问题");
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {

                }
            });
        }
    }


    public void maketoast(String string) {
        //Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }


    //actionbar
    @OnClick(R.id.title_back)
    public void back () {
        finish ();
    }
    @Bind(R.id.title_back)
    ImageView iv_back;
    @Bind(R.id.title_name)
    TextView tv_title;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 用于gridview显示多张照片
     *
     * @author wlc
     * @date 2015-4-16
     */
    public class MyAdapter extends BaseAdapter {

        private boolean isDelete;  //用于删除图标的显隐
        private LayoutInflater inflater = LayoutInflater.from(mContext);

        @Override
        public int getCount() {
            //需要额外多出一个用于添加图片
            return imgList.size() + 1;

        }

        @Override
        public Object getItem(int arg0) {
            return imgList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {
            //初始化页面和相关控件
            convertView = inflater.inflate(R.layout.item_commentgrid_imgview, null);
            ImageView img_pic = (ImageView) convertView
                    .findViewById(R.id.img_pic);
            LinearLayout ly = (LinearLayout) convertView
                    .findViewById(R.id.layout);
            LinearLayout ll_picparent = (LinearLayout) convertView
                    .findViewById(R.id.ll_picparent);
            ImageView delete = (ImageView) convertView
                    .findViewById(R.id.img_delete);

            //默认的添加图片的那个item是不需要显示删除图片的
            if (imgList.size() >= 1) {
                if (position <= imgList.size() - 1) {
                    ll_picparent.setVisibility(View.GONE);
                    img_pic.setVisibility(View.VISIBLE);
                    img_pic.setImageBitmap(imgList.get(position));
                    // 设置删除按钮是否显示
                    delete.setVisibility(isDelete ? View.VISIBLE : View.GONE);
                }
            }
            //当处于删除状态时，删除事件可用
            //注意：必须放到getView这个方法中，放到onitemClick中是不起作用的。
            if (isDelete) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgList.remove(position);
                        ImgAdapter.notifyDataSetChanged();

                    }
                });
            }

            return convertView;
        }

        /**
         * 设置是否显示删除图片
         *
         * @param isShowDelete
         */
        public void setIsShowDelete(boolean isShowDelete) {
            this.isDelete = isShowDelete;
            notifyDataSetChanged();
        }

    }


    /**
     * 选择本地图片
     */
    public void toGetLocalImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PICTURE);
    }

    /**
     * 拍照   并更具系统时间来命名照片
     */
    File out;
    public void toGetCameraImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        SimpleDateFormat formatter = new SimpleDateFormat    ("HH:mm:ss");
        Date curDate =  new   Date(System.currentTimeMillis());//获取当前时间
        String photoname  =  formatter.format(curDate)+".jpg";
        out = new File(getSDPath(), photoname);
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, SELECT_CAMER);
    }

    public String getPhotoNamebyDate(){
        SimpleDateFormat formatter = new SimpleDateFormat    ("HH:mm:ss");
        Date curDate =  new   Date(System.currentTimeMillis());//获取当前时间
        String photoname  =  formatter.format(curDate);
        return  photoname;
    }
    /**
     * 获取sd卡路径
     *
     * @return
     */
    private File getSDPath() {
        File sdDir = null;
        boolean sdCardExist =    Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            // 这里可以修改为你的路径
            sdDir = new File(Environment.getExternalStorageDirectory()
                    + "/DCIM/Camera");
        }
        return sdDir;
    }


    private void inflateTopViews() {
        L.d("XXX", entity.toString());
        tv_product_name.setText(entity.getProduct().getName());
        tv_product_norms.setText(entity.getNorms());
        ImageLoader.getInstance().displayImage(entity.getProduct().getImageUrl(), iv_product_img, ImageLoadOptions.getOptions());
    }

    public void submit(){
        String link = NewLink.SUBMIT_COMMENT+"&key="+ Utils.getTokenKey(app)+"&OrderNum="+orderId+
                "&goods_id="+entity.getProduct().getProId()+"&scores="+scores;
        try {
            String c = "&content="+ URLEncoder.encode(content, "UTF-8");
            link+=c;
        }catch (Exception e) {
            e.printStackTrace();
            String c = "&content="+content;
            L.d("link:::", "no!!! "+c);
            link+= c;
        }
        L.d("link:::", link);
        VolleyRequest.GetCookieRequest(this, link, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.d("result:::", result);
                maketoast(getString(R.string.send_comment_success));
                finish();
            }

            @Override
            public void onFail(String error) {
                maketoast(getString(R.string.send_comment_error));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgList.clear();
        filePaths.clear();
        BimpUtils.getInstace().clearAll();
    }
}
