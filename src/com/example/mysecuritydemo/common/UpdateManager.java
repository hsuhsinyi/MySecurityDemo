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
 * Ӧ�ó�����¹���
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
				Toast.makeText(context, "������ɣ����ڽ��а�װ�����Ժ�", Toast.LENGTH_LONG).show();
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
		Dialog dialog = new AlertDialog.Builder(context).setTitle("�и�����Ӵ")
				.setMessage(descriptionInfo)
				.setPositiveButton("����", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
							DownLoadApk();
							System.out.println("DownLoadApk");
							loadDialog = downloadDialog();
							loadDialog.show();
						}else{
							Toast.makeText(context, "��ȷ��SD���Ƿ������", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton("ȡ��", new OnClickListener() {
					
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
		pbarDialog.setMessage("����������");
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
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();// ����һ��http����
				httpURLConnection.setConnectTimeout(5000);// �������ӵĳ�ʱʱ�䣬����Ϊ5��
				httpURLConnection.setRequestMethod("GET");// ��������ķ�ʽ
				httpURLConnection.connect();
				System.out.println("hererer");
				InputStream is = httpURLConnection.getInputStream();// �õ�һ�������������������update.xml����Ϣ
				System.out.println("yoy");
				info = UpdateAppInfo.parse(is);// ����xml
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
	 * ͨ��config.xml�ļ��ķ�������ַ�õ�updateAppinfoʵ����ע�ⲻ�����������߳��У�android���߰汾������
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
				//��ʾ�ļ���С��ʽ��2��С������ʾ
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	//������������ʾ�����ļ���С
		    	apkFileSize = df.format((float) length / 1024 / 1024) + "MB";
				//fos.write(buf,0,numread);
				while(downloadFlag){
		    		int numread = is.read(buf);
		    		count += numread;
		    		//������������ʾ�ĵ�ǰ�����ļ���С
		    		tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
		    		//��ǰ����ֵ
		    	    progress =(int)(((float)count / length) * 100);
		    	    //���½���
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
