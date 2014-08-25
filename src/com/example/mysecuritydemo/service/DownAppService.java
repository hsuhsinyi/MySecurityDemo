package com.example.mysecuritydemo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import com.example.mysecuritydemo.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

/**
 * 更新APP下载实现，服务开始创建线程下载文件，同步更新notification
 * 
 * @author hhy 
 * @Version V1.0
 * @ModifyTime 2014-8-22 14:45:42
 */
public class DownAppService extends Service {

	private static final int DOWN_OVER = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_FAIL = -1;

	private String saveFilePath;
	private String urlPath;
	private boolean downloadFlag = false;
	private String tmpFileSize;
	private String apkFileSize;
	private int progress;
	private NotificationManager updateNotificationManager;
	private Notification updateNotification;
	private File apkFile;
	private static final int NOTIFYID = 101;
	private NotificationCompat.Builder ncb;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		System.out.println("DownAppService start");
		urlPath = intent.getStringExtra("urlPath");
		saveFilePath = intent.getStringExtra("saveFile");
		apkFile = new File(saveFilePath);
		ncb = new NotificationCompat.Builder(getApplication());
		ncb.setTicker("正在进行下载");
		ncb.setContentTitle(apkFile.getName());
		ncb.setContentText("正在下载");
		ncb.setSmallIcon(R.drawable.icon_download_small);
		ncb.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon_download));
		Intent nullIntent = new Intent();
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, nullIntent, 0);
		PendingIntent contentIntent = pendingIntent;
		ncb.setContentIntent(contentIntent);
		updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		updateNotificationManager.notify(NOTIFYID, ncb.build());

		new Thread(new updateRunnable()).start();
		return super.onStartCommand(intent, flags, startId);
	}

	private Handler mDownloadHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				System.out.println("progress" + progress);
				ncb.setContentTitle(apkFile.getName());
				ncb.setContentText(progress + "%");
				updateNotificationManager.notify(NOTIFYID, ncb.build());
				break;
			case DOWN_OVER:
				installApk();
				stopSelf();
				break;
			case DOWN_FAIL:
				ncb.setContentTitle(apkFile.getName() + "." + "APK");
				ncb.setContentText("下载失败，请检查网络");
				updateNotificationManager.notify(NOTIFYID, ncb.build());
				break;
			default:
				break;
			}
		}
	};

	private class updateRunnable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String savePath = apkFile.getParentFile().getAbsolutePath();
			File file = new File(savePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			if (apkFile.exists()) {
				apkFile.delete();
			}
			System.out.println("i am run");
			try {
				FileOutputStream fos = new FileOutputStream(apkFile);
				URL downRul = new URL(urlPath);
				System.out.println("urlPath" + urlPath);
				HttpURLConnection conn = null;
				conn = (HttpURLConnection) downRul.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				byte buf[] = new byte[1024];
				int count = 0;
				int downloadCount = 0;
				downloadFlag = true;
				// 显示文件大小格式：2个小数点显示
				DecimalFormat df = new DecimalFormat("0.00");
				// 进度条下面显示的总文件大小
				apkFileSize = df.format((float) length / 1024 / 1024) + "MB";
				// fos.write(buf,0,numread);
				while (downloadFlag) {
					int numread = is.read(buf);
					count += numread;
					// 进度条下面显示的当前下载文件大小
					tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
					// 当前进度值
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					Intent nullIntent = new Intent();
					// 每隔10%更新notification，防止过于频繁
					if ((numread > 0) && (progress - 10 >= downloadCount)) {
						downloadCount += 10;
						mDownloadHandler.sendEmptyMessage(DOWN_UPDATE);
					}
					if (numread <= 0) {
						mDownloadHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mDownloadHandler.sendEmptyMessage(DOWN_FAIL);
			}
		}

	}

	public void installApk() {
		ncb = new NotificationCompat.Builder(getApplication());
		ncb.setContentTitle(apkFile.getName());
		ncb.setTicker("下载完成，点击进行安装");
		ncb.setDefaults(Notification.DEFAULT_SOUND);
		ncb.setContentText("下载完成，点击进行安装");
		ncb.setSmallIcon(R.drawable.icon_download_small);
		ncb.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon_download));
		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.setDataAndType(Uri.fromFile(new File(saveFilePath)),
				"application/vnd.android.package-archive");
		PendingIntent pendingIntent = PendingIntent.getActivity(
				DownAppService.this, 0, installIntent, 0);
		ncb.setDefaults(Notification.DEFAULT_ALL);
		ncb.setAutoCancel(true);
		PendingIntent contentIntent = pendingIntent;
		ncb.setContentIntent(contentIntent);
		updateNotificationManager.notify(NOTIFYID, ncb.build());
	}
}
