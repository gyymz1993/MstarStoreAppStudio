package cn.mstar.store.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

/**
 * 工具类
 * @date 2015-5-5
 */
public class Until {
	public static final String DATETIME = "yyyyMMddHHmmss";

	public static boolean StrIsNull(String str) {
		if (str == null || str.equals("")) {
			return true;
		}
		return false;
	}
	
	public static String getVersion(Context mContext){
		PackageManager packageManager = mContext.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(mContext.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		if(!StrIsNull(version))
			return version ;
		else
			return "1.0";
	}

	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED), MeasureSpec
				.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}
	
	

	public static byte[] BitmaptoBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
		return baos.toByteArray();
	}

	

	public static String getHunyin(String hunyin) {
		if (hunyin.equals("0")) {
			return "未婚";
		} else if (hunyin.equals("1")) {
			return "已婚";
		} else {
			return hunyin;
		}
	}

	public static String getDate(String date) {
		long time1 = Long.valueOf(date);
		long time2 = Calendar.getInstance().getTimeInMillis() / 1000;
		long time = time2 - time1;
		int i = (int) (time / (60 * 60 * 24));
		Date vDate = new Date(time1 * 1000);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // 打印年份
		String t = timeFormat.format(vDate);
		if (i == 0) {
			return "今天 " + t;
		}
		if (i == 1) {
			return "昨天 " + t;
		}
		if (i == 2) {
			return "前天 " + t;
		}
		if (i > 2) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm"); // 打印年份
			return dateFormat.format(vDate);
		}
		return "-";
	}

	public static void Log(Object pLog) {
		 Log.i("sssss", pLog + "");
	}

	public static boolean isGpsEnable(Context pContext) {
		LocationManager locationManager = ((LocationManager) pContext
				.getSystemService(Context.LOCATION_SERVICE));
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static void OpenGpsSetting(Context pContext) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			pContext.startActivity(intent);
		} catch (ActivityNotFoundException ex) {
			intent.setAction(Settings.ACTION_SETTINGS);
			try {
				pContext.startActivity(intent);
			} catch (Exception e) {
			}
		}
	}

	
	

	public static void writeFileSdcard(Context pContext, String fileName,
			String message) {
		try {
			File vFile = new File(getSDCardPath(pContext));
			if (!vFile.exists()) {
				vFile.mkdir();
			}
			FileOutputStream fout = new FileOutputStream(
					getSDCardPath(pContext) + "/" + fileName);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readFileSdcard(Context pContext, String fileName) {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(
					getSDCardPath(pContext) + "/" + fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return res;

	}

	public static String Getdistance(String pLat, String pLng, String pMap) {
		if (StrIsNull(pMap) || StrIsNull(pLat) || StrIsNull(pLng)) {
			return "未知";
		}

		String[] distance = pMap.split(",");
		double lng = Double.valueOf(distance[0]);
		double lat = Double.valueOf(distance[1]);
		double dis = GetDistance(lat, lng, Double.valueOf(pLat),
				Double.valueOf(pLng));
		return String.format("%.2f", dis) + "km";
	}

	private static double GetDistance(double lat1, double lon1, double lat2,
			double lon2) {
		double R = 6371;
		double distance = 0.0;
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLon = (lon2 - lon1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(lat1 * Math.PI / 180)
				* Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R;
		return distance;
	}

	/**
	 * 根据url获取Bitmap对象
	 * 
	 * @param uri
	 * @return
	 */
	public static Bitmap getBitmapByUrl(Context pContext, String uri) {

		URLConnection url;
		Bitmap bitmap = null;
		String newuri;
		String iconFile = downloadFile(pContext, uri);

		if (iconFile == null)
			return null;

		if (iconFile.length() > 0) {
			newuri = Uri.fromFile(
					new File(getSDCardPath(pContext) + "/" + iconFile))
					.toString();
		} else {
			newuri = uri;
		}
		try {
			url = new URL(newuri).openConnection();
			InputStream picStream = url.getInputStream();
			bitmap = BitmapFactory.decodeStream(picStream);
		} catch (MalformedURLException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 * @return
	 */
	public static String downloadFile(Context pContext, String url) {

		if (url == null || url == "")
			return null;

		String filename = url.substring(url.lastIndexOf("/") + 1);

		try {
			File downDir = new File(getSDCardPath(pContext));
			downDir.mkdirs();

			File tryFile = new File(downDir, filename);
			if (tryFile.exists()) {
				if (tryFile.length() != 0) {
					return filename;
				} else {
					tryFile.delete();
				}
			}

			URL myURL = new URL(url);
			URLConnection conn = myURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			int fileSize = conn.getContentLength();

			if (fileSize <= 0)
				throw new RuntimeException("");

			File outputFile = new File(downDir, filename);
			@SuppressWarnings("resource")
			FileOutputStream fos = new FileOutputStream(outputFile);

			if (is == null)
				throw new RuntimeException("stream is null");

			byte buf[] = new byte[1024];

			do {
				int numread = is.read(buf);
				if (numread == -1) {
					break;
				}
				fos.write(buf, 0, numread);

			} while (true);

		} catch (Exception ex) {
			// ex.printStackTrace();
			return "";

		}
		return filename;
	}

	@SuppressLint({ "NewApi", "InlinedApi" })
	public static Bitmap getxtsldraw(Context c, String file) {
		File f = new File(file);
		Bitmap drawable = null;
		if (f.length() / 1024 < 100) {
			drawable = BitmapFactory.decodeFile(file);
		} else {
			Cursor cursor = c.getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					new String[] { MediaStore.Images.Media._ID },
					MediaStore.Images.Media.DATA + " like ?",
					new String[] { "%" + file }, null);
			if (cursor == null || cursor.getCount() == 0) {
				drawable = getbitmap(file, 720 * 1280);
			} else {
				if (cursor.moveToFirst()) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPurgeable = true;
					options.inInputShareable = true;
					options.inPreferredConfig = Config.RGB_565;
					String videoId = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media._ID));
					long videoIdLong = Long.parseLong(videoId);
					Bitmap bitmap = Thumbnails.getThumbnail(
							c.getContentResolver(), videoIdLong,
							Thumbnails.MINI_KIND, options);
					return bitmap;
				} else {
					// drawable = BitmapFactory.decodeResource(c.getResources(),
					// R.drawable.ic_doctor);
				}
			}
		}
		int degree = 0;
		ExifInterface exifInterface;
		try {
			exifInterface = new ExifInterface(file);

			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
			if (degree != 0 && drawable != null) {
				Matrix m = new Matrix();
				m.setRotate(degree, (float) drawable.getWidth() / 2,
						(float) drawable.getHeight() / 2);
				drawable = Bitmap.createBitmap(drawable, 0, 0,
						drawable.getWidth(), drawable.getHeight(), m, true);
			}
		} catch (OutOfMemoryError e) {
			// Toast.makeText(c, "牌照出错，请重新牌照", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return drawable;
	}

	public static Bitmap getbitmap(String imageFile, int length) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Config.RGB_565;
		opts.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(imageFile, opts);
		int ins = computeSampleSize(opts, -1, length);
		opts.inSampleSize = ins;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inJustDecodeBounds = false;
		try {
			Bitmap bmp = BitmapFactory.decodeFile(imageFile, opts);
			return bmp;
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return null;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}


	public static Bitmap getViewBitmap(View view) {
		int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		view.measure(spec, spec);
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Config.ARGB_8888);
		Canvas c = new Canvas(b);
		c.translate(-view.getScrollX(), -view.getScrollY());
		view.draw(c);
		view.setDrawingCacheEnabled(true);
		Bitmap cacheBmp = view.getDrawingCache();
		Bitmap viewBmp = cacheBmp.copy(Config.ARGB_8888, true);
		view.destroyDrawingCache();

		return viewBmp;
	}

	public static String getSDCardPath(Context pContext) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/.jiuyi160_c" ;
			File PathDir = new File(path);
			if(!PathDir.exists()){
				PathDir.mkdirs();
			}
			return path;
		} else {
			return pContext.getCacheDir().getAbsolutePath();
		}
	}


	
}

