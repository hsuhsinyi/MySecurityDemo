package com.example.mysecuritydemo.bean;

import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * Ӧ�ó������ʵ����
 * 
 * @author hhy CLASSNAME UpdateAppInfo
 * @Version V1.0
 * @ModifyBy hhy
 * @ModifyTime 2014-8-21 14:15:09
 */
public class UpdateAppInfo {

	public final static String UTF8 = "UTF-8";
	public final static String NODE_ROOT = "hhy";

	private int versionCode; // Ӧ�ð汾����Ϣ
	private String versionName;	//Ӧ�ð汾��Ϣ
	private String description; // Ӧ��������Ϣ
	private String apkurl; // Ӧ��URL
	
	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApkUrl() {
		return apkurl;
	}

	public void setApkUrl(String apkurl) {
		this.apkurl = apkurl;
	}

	/**����������������XML���õ�APK��Ϣ
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static UpdateAppInfo parse(InputStream inputStream)
			throws IOException {
		UpdateAppInfo updateAppInfo = new UpdateAppInfo();
		// ���XmlPullParser������
		XmlPullParser xmlParser = Xml.newPullParser();
		try {
			xmlParser.setInput(inputStream, UTF8);
			// ��ý��������¼���������п�ʼ�ĵ��������ĵ�����ʼ��ǩ��������ǩ���ı��ȵ��¼���
			int evtType = xmlParser.getEventType();
			// һֱѭ����ֱ���ĵ�����
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
				case XmlPullParser.START_TAG:
					if (xmlParser.getName().equals("versioncode")) {
						updateAppInfo.setVersionCode(Integer.parseInt(xmlParser.nextText()));
					} else if (xmlParser.getName().equals("versionname")) {
						updateAppInfo.setVersionName(xmlParser.nextText());
					} else if (xmlParser.getName().equals("description")) {
						updateAppInfo.setDescription(xmlParser.nextText());
					} else if (xmlParser.getName().equals("apkurl")) {
						updateAppInfo.setApkUrl(xmlParser.nextText());
					}
					break;

				default:
					break;
				}
				// ���xmlû�н������򵼺�����һ���ڵ�
				evtType = xmlParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inputStream.close();
		}
		return updateAppInfo;
	}
}
