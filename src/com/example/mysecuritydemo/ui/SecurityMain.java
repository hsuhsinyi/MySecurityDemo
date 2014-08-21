package com.example.mysecuritydemo.ui;

import java.io.IOException;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.common.UpdateManager;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SecurityMain extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Toast.makeText(this, "asd", Toast.LENGTH_SHORT).show();
		setContentView(R.layout.main_ui);
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);  
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();  
        if(networkInfo == null || !networkInfo.isAvailable())  
        {  
            Toast.makeText(this, "网络连接不可用, 请检查网络连接",Toast.LENGTH_LONG).show();
        }  
        else   
        {  System.out.println("i am here");
            UpdateManager updateManager = new UpdateManager(this);
            updateManager.checkUpdate();
        }  
	}
}
