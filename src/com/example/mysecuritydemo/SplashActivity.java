package com.example.mysecuritydemo;

import com.example.mysecuritydemo.ui.SecurityMain;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.os.Build;

/**
 * Ӧ�û�ӭ�������棬��ת��������
 * 
 * @author hhy CLASSNAME SplashActivity
 * @Version V1.0
 * @ModifyBy hhy
 * @ModifyTime 2014-8-21 12:14:51
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);
		startAnimaition(view);
	}

	public void startAnimaition(View view) {
		// ����չʾ������
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				startActivityTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}

	public void startActivityTo() {
		Intent intent = new Intent(this, SecurityMain.class);
		startActivity(intent);
		finish();
	}

}
