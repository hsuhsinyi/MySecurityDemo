package com.example.mysecuritydemo.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.adapter.ScanPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.Build;

public class FragmentCommon extends Fragment {
	private static final String TAG = "MainActivity";
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentsList;
	private ImageView ivBottomLine;
	private TextView TabCommonNumber, TabCommonPost, TabCommonPlate;

	private int currIndex = 0;
	private int bottomLineWidth;
	private int offset = 0;
	private int position_one;
	private int position_two;
	private int position_three;
	private int position_four;
	private int screenW;
	private Fragment[] mFragments;
	private View mConfirmOperationBar;
	private FragmentManager fragmentManager;
	private LinearLayout linearLayout;
	private ScanPagerAdapter adapter;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.attribution_common, container, false);  
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		InitWidth();
		InitTextView();
		InitViewPager();
	}

	private void InitTextView() {
		// 动态改变viewpager的布局
		LinearLayout viewPagerHead = (LinearLayout) getActivity().findViewById(R.id.layout_viewpagerhead);
		LayoutParams params = viewPagerHead.getLayoutParams();
		params.width = getScreenWidth();
		viewPagerHead.setLayoutParams(params);

		TabCommonNumber = (TextView) getActivity().findViewById(R.id.common_number);
		TabCommonPost = (TextView) getActivity().findViewById(R.id.common_post);
		TabCommonPlate = (TextView) getActivity().findViewById(R.id.common_plate);
		// TabMusicFile = (TextView) findViewById(R.id.category_music_file);
		// TabZipFile = (TextView) findViewById(R.id.category_apk_file);

		TabCommonNumber.setOnClickListener(new MyOnClickListener(0));
		TabCommonPost.setOnClickListener(new MyOnClickListener(1));
		TabCommonPlate.setOnClickListener(new MyOnClickListener(2));
		// TabMusicFile.setOnClickListener(new MyOnClickListener(3));
		// TabZipFile.setOnClickListener(new MyOnClickListener(4));
	}

	/*
	 * 初始化viewpager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) getActivity().findViewById(R.id.vPager);
		fragmentsList = new ArrayList<Fragment>();
		mFragments = new Fragment[3];
		fragmentManager =getActivity().getSupportFragmentManager();
		mFragments[0] = new FragmentCommonNumber();
		mFragments[1] = new FragmentCommonPost();
		mFragments[2] = new FragmentCommonPlate();

		fragmentsList.add(mFragments[0]);
		fragmentsList.add(mFragments[1]);
		fragmentsList.add(mFragments[2]);

		adapter = new ScanPagerAdapter(getActivity().getSupportFragmentManager(),
				fragmentsList);
		// mPager.setAdapter(new
		// MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
		mPager.setAdapter(adapter);
		// mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setOffscreenPageLimit(2);
	}

	private void InitWidth() {
		ivBottomLine = (ImageView) getActivity().findViewById(R.id.iv_bottom_line);
		bottomLineWidth = ivBottomLine.getLayoutParams().width;
		screenW = getScreenWidth();
		int eachWith = (int) (screenW / 3.0);
		ivBottomLine
				.setLayoutParams(new LinearLayout.LayoutParams(eachWith, 5));
		// offset = (int) (screenW /10.0) - (int) (bottomLineWidth/2);
		// ivBottomLine.setX((float)offset);
		offset = (int) ((screenW / 3.0) / 2);

		position_one = (int) (screenW / 3.0);
		position_two = (int) (screenW / 3.0) * 2;
		position_three = (int) (screenW / 3.0) * 3;
		position_four = (int) (screenW / 3.0) * 4;
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, position_one, 0,
							0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two,
							position_one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, position_two, 0,
							0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(position_one,
							position_two, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	public int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}

}
