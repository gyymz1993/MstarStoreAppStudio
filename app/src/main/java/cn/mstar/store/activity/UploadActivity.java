package cn.mstar.store.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mstar.store.R;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.callback.OnSucceedListener;
import cn.mstar.store.entity.JsonResult;
import cn.mstar.store.functionutils.BimpUtils;
import cn.mstar.store.functionutils.HttpRequestUtil;
import cn.mstar.store.functionutils.LogUtils;
import cn.mstar.store.utils.AndroidMultiPartEntity;
import cn.mstar.store.utils.Config;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;


/**
 * 上传图片
 */
public class UploadActivity extends Activity {
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private ImageView imgPreview;
    private VideoView vidPreview;
    private Button btnUpload;
    long totalSize = 0;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);
        Intent i = getIntent();
        filePath = i.getStringExtra("filePath");
        boolean isImage = i.getBooleanExtra("isImage", true);
        if (filePath != null) {
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadImage();
                sendImgToServer(filePath);
            }
        });

    }

    private void previewMedia(boolean isImage) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            vidPreview.setVisibility(View.GONE);
            options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeFile(filePath, options);
            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            vidPreview.start();
            bitmap = BitmapFactory.decodeFile(filePath, options);
        }

    }


    public void sendImgToServer(final String filePath){
        {
            HttpUtils http = new HttpUtils();
            HttpRequest.HttpMethod method = HttpRequest.HttpMethod.POST;
            RequestParams params=new RequestParams();
            params.addBodyParameter("key",Utils.getTokenKey((MyApplication) getApplication()));
            params.addBodyParameter("attachment", new File(filePath));//, "multipart/form-data"
            http.send(HttpRequest.HttpMethod.POST, Config.FILE_UPLOAD_URL, params, new RequestCallBack<String>() {
                @Override
                public void onStart() {

                }
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    String text="upload: " + current + "/"+ total;
                    // maketoast("上传中"+text);
                    LogUtils.i("上传中" + text);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    com.lidroid.xutils.util.LogUtils.i(responseInfo.result);
                    Gson gson = new Gson();
                    try {
                        JsonResult jsonResult = null;
                        jsonResult = gson.fromJson(responseInfo.result, JsonResult.class);
                        //maketoast("功能吗"+jsonResult.getError());
                        if (jsonResult.getError().equals("0")) {
                           // maketoast(getString(R.string.send_comment_success));
                            BimpUtils.getInstace().savebitmap(bitmap);
                            com.lidroid.xutils.util.LogUtils.i("请求成功");
                            finish();
                        } else {
                            com.lidroid.xutils.util.LogUtils.i("请求失败" + jsonResult.getMessage());
                           // maketoast("请求失败" + jsonResult.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        com.lidroid.xutils.util.LogUtils.i("返回数据问题");
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {

                }
            });
        }
    }

//    /**
//     * Uploading the file to server
//     * */
//    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
//        @Override
//        protected void onPreExecute() {
//            // setting progress bar to zero
//            progressBar.setProgress(0);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            // Making progress bar visible
//            progressBar.setVisibility(View.VISIBLE);
//
//            // updating progress bar value
//            progressBar.setProgress(progress[0]);
//
//            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
//        }
//        Boolean succeed = false;
//
//        @Override
//        protected String doInBackground(Void... params) {
//            succeed = false;
//            return uploadFile();
//        }
//
//        @SuppressWarnings("deprecation")
//        private String uploadFile() {
//            String responseString = null;
//            HttpClient httpclient = new DefaultHttpClient();
//            LogUtils.e("上传路径"+ Config.FILE_UPLOAD_URL+"&key="+ Utils.getTokenKey((MyApplication) getApplication()));
//            HttpPost httppost = new HttpPost(cn.mstar.store.utils.Config.FILE_UPLOAD_URL+"&key="+ Utils.getTokenKey((MyApplication) getApplication()));
//            try {
//                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
//                        new AndroidMultiPartEntity.ProgressListener() {
//                            @Override
//                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
//                            }
//                        });
//
//                File sourceFile = new File(filePath);
//
//                //entity.addPart("attachment", new FileBody(sourceFile));
//                totalSize = entity.getContentLength();
//                httppost.setEntity(entity);
//                // Making server call
//                HttpResponse response = httpclient.execute(httppost);
//                HttpEntity r_entity = response.getEntity();
//                int statusCode = response.getStatusLine().getStatusCode();
//                if (statusCode == 200) {
//                    responseString = EntityUtils.toString(r_entity);
//                } else {
//                    responseString = "Error occurred! Http Status Code: "
//                            + statusCode;
//                }
//                succeed = true;
//            } catch (ClientProtocolException e) {
//                responseString = e.toString();
//            } catch (IOException e) {
//                responseString = e.toString();
//            }
//            return responseString;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Log.e(TAG, "Response from server: " + result);
//            if (succeed) {
//                makeToast(getString(R.string.upload_pic_success));
//                MyApplication app = (MyApplication) getApplication();
//                app.isFrg_me_needUpdate = true;
//                JsonElement elm1 = (new Gson()).fromJson(result, JsonElement.class).getAsJsonObject().get("data");
//                app.pic = elm1.getAsJsonObject().get("pic").getAsString();
//            } else
//                makeToast(getString(R.string.upload_pic_failure));
//            File file = new File(filePath);
//            if (file.exists())
//                file.deleteOnExit();
//            super.onPostExecute(result);
//            // remove the file.
//            finish();
//        }
//
//    }

//    public void uploadImage(){
//        //http://www.fanerjewelry.com/mobile/index.php?act=member_info&op=update_avatar&key=2ab3dea9c90bdb89a4824aed7c963c45
//        //String url="http://www.fanerjewelry.com/mobile/index.php?act=member_info&op=update_avatar&key="+Utils.getTokenKey((MyApplication) getApplication());
//        Map<String, Integer> mapcode=new HashMap<>();
//        mapcode.put("code",1);
//        HttpRequestUtil httputil=new HttpRequestUtil(Config.FILE_UPLOAD_URL,mapcode);
//        Map<String, Object> map=new HashMap<>();
//        LogUtils.e("文件路径"+filePath);
//        //String fileName = BimpUtils.getInstace().path + "head.jpg";// 图片名字
//        map.put("attachment",  new File(filePath));
//        map.put("key", Utils.getTokenKey((MyApplication) getApplication()));
//        httputil.suggest(map, new OnSucceedListener<JsonResult>() {
//            @Override
//            public void onResult(boolean isSecceed, JsonResult obj) {
//                if (isSecceed) {
//                    Toast.makeText(UploadActivity.this, "上传成功", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//    }

    private void makeToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}