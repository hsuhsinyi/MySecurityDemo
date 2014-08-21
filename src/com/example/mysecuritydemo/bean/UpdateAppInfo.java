package com.example.mysecuritydemo.bean;

import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * 应用程序更新实体类
 * 
 * @author hhy CLASSNAME UpdateAppInfo
 * @Version V1.0
 * @ModifyBy hhy
 * @ModifyTime 2014-8-21 14:15:09
 */
public class UpdateAppInfo {

	public final static String UTF8 = "UTF-8";
	public final static String NODE_ROOT = "hhy";

	private int versionCode; // 应用版本号信息
	private String versionName;	//应用版本信息
	private String description; // 应用描述信息
	private String apkurl; // 应用URL
	
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

	/**解析服务器传来的XML，得到APK信息
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static UpdateAppInfo parse(InputStream inputStream)
			throws IOException {
		UpdateAppInfo updateAppInfo = new UpdateAppInfo();
		// 获得XmlPullParser解析器
		XmlPullParser xmlParser = Xml.newPullParser();
		try {
			xmlParser.setInput(inputStream, UTF8);
			// 获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
			int evtType = xmlParser.getEventType();
			// 一直循环，直到文档结束
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
				// 如果xml没有结束，则导航到下一个节点
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
