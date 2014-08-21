package com.example.mysecuritydemo.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;

import javax.security.auth.PrivateCredentialPermission;

import com.example.mysecuritydemo.R;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
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
import android.widget.Toast;

import com.example.mysecuritydemo.bean.UpdateAppInfo;

/**
 * 应用程序更新管理
 * 
 * @author hhy CLASSNAME UpdateManager
 * @Version V1.0
 * @ModifyBy hhy
 * @ModifyTime 2014-8-21 14:15:09
 */
public class UpdateManager {
	
	private static final int DOWN_OVER = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int CHECK_OK = 1;

	private Context context;
	private String versionName = "";
	private int curVersionCode = 0;
	private String descriptionInfo;
	private String urlPath;
	private Thread downApkThread;
	private Thread checkThread;
	//private String savePath;
	private String tmpFileSize;
	private String apkFileSize;
	private String saveFile;
	private boolean downloadFlag = false;
	private int progress;
	private ProgressDialog loadDialog = null;
	private UpdateAppInfo info;
	private boolean isCheckOk = false;
	//private Handler mHandler;

	public UpdateManager(Context context) {
		this.context = context;
	}
	
	private Handler mDownloadHandler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case DOWN_UPDATE:
				loadDialog.setProgress(progress);
				loadDialog.setProgressNumberFormat(tmpFileSize);
				break;
			case DOWN_OVER:
				loadDialog.dismiss();
				Toast.makeText(context, "下载完成，正在进行安装，请稍后", Toast.LENGTH_LONG).show();
				installApk();

			default:
				break;
			}
		}
	};
	
	private Handler mCheckHandler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case CHECK_OK:
				isCheckOk = true;
				if(isCheckOk){
					descriptionInfo = info.getDescription();
					versionName = info.getVersionName();
					urlPath = info.getApkUrl();
					System.out.println("descriptionInfo" +descriptionInfo);
					System.out.println("versionName" +versionName);
					System.out.println("urlPath" +urlPath);
					if (info.getVersionCode() > curVersionCode) {
						 UpdateDialog().show();
					} else {

					}
				}
				break;

			default:
				break;
			}
		}
	};

	public void checkUpdate() {
		getCurrentAppInfo();
		getUpdateInfo();
	}

	private Dialog UpdateDialog() {
		Dialog dialog = new AlertDialog.Builder(context).setTitle("有更新了哟")
				.setMessage(descriptionInfo)
				.setPositiveButton("更新", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
							DownLoadApk();
							System.out.println("DownLoadApk");
							loadDialog = downloadDialog();
							loadDialog.show();
						}else{
							Toast.makeText(context, "请确认SD卡是否挂载上", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				})
				.create();
		return dialog;
	}
	
	
	public ProgressDialog downloadDialog(){
		ProgressDialog pbarDialog = null; 
		pbarDialog = new ProgressDialog(context); 
		pbarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
		pbarDialog.setMessage("正在下载中");
		return pbarDialog; 
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
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();// 开启一个http链接
				httpURLConnection.setConnectTimeout(5000);// 设置链接的超时时间，现在为5秒
				httpURLConnection.setRequestMethod("GET");// 设置请求的方式
				httpURLConnection.connect();
				System.out.println("hererer");
				InputStream is = httpURLConnection.getInputStream();// 拿到一个输入流。里面包涵了update.xml的信息
				System.out.println("yoy");
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
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			curVersionCode = info.versionCode;
			System.out.println("versionCode" + curVersionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void DownLoadApk() {
		downApkThread = new Thread(mdownApkRunnable);
		downApkThread.start();
	}
	

	
	private Runnable mdownApkRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String savePath = Environment.getExternalStorageDirectory() + File.separator + "MySecurityDemo" ;
			File file = new File(savePath);
			if(!file.exists()){
				file.mkdirs();
			}
			saveFile = savePath + File.separator + versionName + "." + "apk";
			File apkFile = new File(saveFile);
			if(apkFile.exists()){
				apkFile.delete();
			}
			System.out.println("i am run");
			try {
				FileOutputStream fos = new FileOutputStream(apkFile);
				URL downRul = new URL(urlPath);
				System.out.println("urlPath" + urlPath);
				HttpURLConnection conn = null;
				conn = (HttpURLConnection)downRul.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				byte buf[] = new byte[1024];
				int count = 0;
				downloadFlag = true;
				//显示文件大小格式：2个小数点显示
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	//进度条下面显示的总文件大小
		    	apkFileSize = df.format((float) length / 1024 / 1024) + "MB";
				//fos.write(buf,0,numread);
				while(downloadFlag){
		    		int numread = is.read(buf);
		    		count += numread;
		    		//进度条下面显示的当前下载文件大小
		    		tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
		    		//当前进度值
		    	    progress =(int)(((float)count / length) * 100);
		    	    //更新进度
		    	    mDownloadHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){	
		    			mDownloadHandler.sendEmptyMessage(DOWN_OVER);
		    			break;
		    		}
		    		fos.write(buf,0,numread);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	public void installApk() {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(saveFile)),"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

}
