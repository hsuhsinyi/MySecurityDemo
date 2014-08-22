package com.example.mysecuritydemo.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;

import javax.security.auth.PrivateCredentialPermission;

import com.example.mysecuritydemo.R;

import android.R.anim;
import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.example.mysecuritydemo.bean.UpdateAppInfo;
import com.example.mysecuritydemo.service.DownAppService;

/**
 * 应用程序更新检测部分
 * 
 * @author hhy CLASSNAME UpdateManager
 * @Version V1.0
 * @ModifyBy hhy
 * @ModifyTime 2014-8-21 14:15:09
 */
public class UpdateManager {

	private static final int CHECK_OK = 1;
	private static final int NOTIFYID = 100;
	private Context context;
	private String versionName = "";
	private int curVersionCode = 0;
	private String descriptionInfo;
	private String urlPath;
	private Thread checkThread;
	private UpdateAppInfo info;
	private NotificationManager notificationManager;
	private String saveFile;
	private String savePathDir;

	public UpdateManager(Context context) {
		this.context = context;
	}

	public void checkUpdate() {
		getCurrentAppInfo();
		getUpdateInfo();
	}

	private Handler mCheckHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHECK_OK:
				descriptionInfo = info.getDescription();
				versionName = info.getVersionName();
				urlPath = info.getApkUrl();
				System.out.println("descriptionInfo" + descriptionInfo);
				System.out.println("versionName" + versionName);
				System.out.println("urlPath" + urlPath);
				savePathDir = Environment.getExternalStorageDirectory()
						+ File.separator + "MySecurityDemo";
				saveFile = savePathDir + File.separator + versionName + "."
						+ "apk";
				if (info.getVersionCode() > curVersionCode) {
					showNotification();
				} else {
					return;
				}
				break;

			default:
				break;
			}
		}
	};

	public void showNotification() {
		NotificationCompat.Builder ncb = new NotificationCompat.Builder(context);
		ncb.setTicker("亲，有新的更新可用哟~");
		ncb.setAutoCancel(true);
		ncb.setDefaults(Notification.DEFAULT_ALL);
		ncb.setContentTitle(versionName + "." + "apk");
		ncb.setContentText("点击进行更新");
		ncb.setSmallIcon(R.drawable.icon_download);
		Intent intent = new Intent(context, DownAppService.class);
		intent.putExtra("urlPath", urlPath);
		intent.putExtra("saveFile", saveFile);
		PendingIntent contentIntent = PendingIntent.getService(context, 0,
				intent, 0);
		ncb.setContentIntent(contentIntent);
		notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFYID, ncb.build());
	}

	private Runnable mcheckThread = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String path = context.getResources().getString(R.string.serverUrl);
			System.out.println("path" + path);
			URL url;
			try {
				url = new URL(path);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url
						.openConnection();// 开启一个http链接
				httpURLConnection.setConnectTimeout(5000);// 设置链接的超时时间，现在为5秒
				httpURLConnection.setRequestMethod("GET");// 设置请求的方式
				httpURLConnection.connect();
				InputStream is = httpURLConnection.getInputStream();// 拿到一个输入流。里面包涵了update.xml的信息
				info = UpdateAppInfo.parse(is);// 解析xml
				mCheckHandler.sendEmptyMessage(CHECK_OK);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	/**
	 * 通过config.xml文件的服务器地址得到updateAppinfo实例。注意不能运行在主线程中，android更高版本的限制
	 * 
	 * @return UpdateAppInfo
	 * @throws IOException
	 */
	public void getUpdateInfo() {
		checkThread = new Thread(mcheckThread);
		checkThread.start();
	}

	public void getCurrentAppInfo() {
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			curVersionCode = info.versionCode;
			System.out.println("versionCode" + curVersionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
