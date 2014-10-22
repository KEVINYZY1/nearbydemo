package com.andieguo.loaction;

import com.andieguo.loaction.R;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.os.Bundle;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private TextView txtLocation;
	private LocationClient mLocClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		txtLocation = (TextView) findViewById(R.id.txtLocation);
		((ZApplication)getApplication()).mLocationResult = txtLocation;
		mLocClient = ((ZApplication) getApplication()).mLocationClient;

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ
		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		option.setIsNeedAddress(true);// ���صĶ�λ���������ַ��Ϣ
		option.setNeedDeviceDirect(true);// ���صĶ�λ��������ֻ���ͷ�ķ���
		mLocClient.setLocOption(option);
		
	}

	public void location(View view) {
		// ��ʼ��λ
		mLocClient.start();
		if (mLocClient != null && mLocClient.isStarted()) {
			mLocClient.requestLocation();
			Log.d(TAG, "locClient is started");
		} else {
			Log.d(TAG, "locClient is null or not started");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
