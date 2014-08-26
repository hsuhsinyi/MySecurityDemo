package com.example.mysecuritydemo.ui;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.common.AssetsDatabaseManager;
import com.example.mysecuritydemo.common.DatabaseDAO;

import android.app.ActionBar;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class Attribution extends FragmentActivity {

	private static final String TAB_QUERY = "tabquery";
	private static final String TAB_COMMON = "tabcommon";
	private TabHost mTabHost;
	private ActionBar actionBar;
	private SQLiteDatabase sqliteDB;
	private Button queryBtn;
	private EditText queryEdit;
	private TextView result_query;
	private DatabaseDAO dao;
	private String phoneNumber;
	private RadioButton queryRadioButton;
	private RadioButton commonRadioButton;
	private RadioGroup footerRadioGroup;
	private Fragment[] mFragments;
	private RadioGroup bottomRg;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private RadioButton rbOne, rbTwo, rbThree, rbFour;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attribution_main);
		initFragment();
		initView();
	}

	public void initFragment() {
		mFragments = new Fragment[2];
		fragmentManager = getSupportFragmentManager();
		mFragments[0] = fragmentManager.findFragmentById(R.id.fragement_search);
		mFragments[1] = fragmentManager.findFragmentById(R.id.fragement_common);
		fragmentTransaction = fragmentManager.beginTransaction()
				.hide(mFragments[0]).hide(mFragments[1]);
		fragmentTransaction.show(mFragments[0]).commit();
	}

	public void initView() {
		queryRadioButton = (RadioButton) findViewById(R.id.query_radiobtn);
		commonRadioButton = (RadioButton) findViewById(R.id.common_radiobtn);
		footerRadioGroup = (RadioGroup) findViewById(R.id.main_radio);
		queryRadioButton.setChecked(true);
		footerRadioGroup.setOnCheckedChangeListener(checkedChangeListener);
	}

	android.widget.RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			fragmentTransaction = fragmentManager.beginTransaction()
					.hide(mFragments[0]).hide(mFragments[1]);
			switch (checkedId) {
			case R.id.query_radiobtn:
				System.out.println("query_radiobtn");
				// fragmentTransaction.hide(mFragments[1]);
				fragmentTransaction.show(mFragments[0]).commit();
				break;
			case R.id.common_radiobtn:
				System.out.println("common_radiobtn");
				// fragmentTransaction.hide(mFragments[0]);
				fragmentTransaction.show(mFragments[1]).commit();
				break;

			default:
				break;
			}
		}
	};
}