package com.example.mysecuritydemo.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
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

	private Context context;
	private String versionName = "";
	private int curVersionCode = 0;
	private String descriptionInfo;
	private String urlPath;
	private Thread downApkThread;
	//private String savePath;
	private String tmpFileSize;
	private String apkFileSize;
	private boolean downloadFlag = false;
	private int progress;
	private ProgressDialog loadDialog = null;
	//private Handler mHandler;

	public UpdateManager(Context context) {
		this.context = context;
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case DOWN_UPDATE:
				loadDialog.setProgress(progress);
				break;
			case DOWN_OVER:
				loadDialog.dismiss();
				installApk();

			default:
				break;
			}
		}
	};

	public void checkUpdate() {
		getCurrentAppInfo();
		try {
			UpdateAppInfo info = getUpdateInfo();
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Dialog UpdateDialog() {
		Dialog dialog = new AlertDialog.Builder(context).setTitle("�и�����Ӵ")
				.setMessage(descriptionInfo)
				.setPositiveButton("����", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
							loadDialog = downloadDialog();
							loadDialog.show();
							DownLoadApk();
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

	/**
	 * ͨ��config.xml�ļ��ķ�������ַ�õ�updateAppinfoʵ��
	 * 
	 * @return UpdateAppInfo
	 * @throws IOException
	 */
	public UpdateAppInfo getUpdateInfo() throws IOException {
		String path = context.getResources().getString(R.string.serverUrl);
		System.out.println("path" + path);
		URL url = new URL(path);
		
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();// ����һ��http����
		httpURLConnection.setConnectTimeout(5000);// �������ӵĳ�ʱʱ�䣬����Ϊ5��
		httpURLConnection.setRequestMethod("GET");// ��������ķ�ʽ
		System.out.println("hererer");
		InputStream is = httpURLConnection.getInputStream();// �õ�һ�������������������update.xml����Ϣ
		System.out.println("yoy");
		return UpdateAppInfo.parse(is);// ����xml
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
			String savePath = Environment.getExternalStorageDirectory() + File.pathSeparator + "MySecurityDemo" ;
			File file = new File(savePath);
			if(!file.exists()){
				file.mkdirs();
			}
			String saveFile = savePath + File.separator + versionName + "." + "apk";
			File apkFile = new File(saveFile);
			if(apkFile.exists()){
				apkFile.delete();
			}
			try {
				FileOutputStream fos = new FileOutputStream(apkFile);
				URL downRul = new URL(urlPath);
				HttpURLConnection conn = null;
				conn.connect();
				conn = (HttpURLConnection)downRul.openConnection();
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
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){	
		    			mHandler.sendEmptyMessage(DOWN_OVER);
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

	}
	

}
