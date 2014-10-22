package com.andieguoe.locationdemo;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class NetWorkActivity extends Activity {
	private static final String TAG = "NetWorkActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network);
		// ��ȡ��LocationManager����
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// ����һ��Criteria����
		Criteria criteria = new Criteria();
		// ���ô��Ծ�ȷ��
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// �����Ƿ���Ҫ���غ�����Ϣ
		criteria.setAltitudeRequired(false);
		// �����Ƿ���Ҫ���ط�λ��Ϣ
		criteria.setBearingRequired(false);
		// �����Ƿ������ѷ���
		criteria.setCostAllowed(true);
		// ���õ������ĵȼ�
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		// �����Ƿ���Ҫ�����ٶ���Ϣ
		criteria.setSpeedRequired(false);

		// �������õ�Criteria���󣬻�ȡ����ϴ˱�׼��provider����
		String currentProvider = locationManager.getBestProvider(criteria, true);
		Log.d(TAG, "currentProvider: " + currentProvider);
		// ���ݵ�ǰprovider�����ȡ���һ��λ����Ϣ
		Location currentLocation = locationManager.getLastKnownLocation(currentProvider);
		// ���λ����ϢΪnull�����������λ����Ϣ
		if (currentLocation == null) {
			locationManager.requestLocationUpdates(currentProvider, 0, 0, locationListener);
		}
		// ֱ��������һ��λ����ϢΪֹ�����δ������һ��λ����Ϣ������ʾĬ�Ͼ�γ��
		// ÿ��10���ȡһ��λ����Ϣ
		while (true) {
			currentLocation = locationManager.getLastKnownLocation(currentProvider);
			if (currentLocation != null) {
				Log.d(TAG, "Latitude: " + currentLocation.getLatitude());
				Log.d(TAG, "location: " + currentLocation.getLongitude());
				break;
			} else {
				Log.d(TAG, "Latitude: " + 0);
				Log.d(TAG, "location: " + 0);
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}

		// ������ַ����ʾ
		Geocoder geoCoder = new Geocoder(this);
		try {
			int latitude = (int) currentLocation.getLatitude();
			int longitude = (int) currentLocation.getLongitude();
			List<Address> list = geoCoder.getFromLocation(latitude, longitude, 2);
			for (int i = 0; i < list.size(); i++) {
				Address address = list.get(i);
				Toast.makeText(NetWorkActivity.this, address.getCountryName() + address.getAdminArea()+address.getSubLocality()+ address.getFeatureName(), Toast.LENGTH_LONG).show();
			}
		} catch (IOException e) {
			Toast.makeText(NetWorkActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	// ����λ�ü�����
		private LocationListener locationListener = new LocationListener() {
			// λ�÷����ı�ʱ����
			@Override
			public void onLocationChanged(Location location) {
				Log.d(TAG, "onLocationChanged");
				Log.d(TAG, "onLocationChanged Latitude" + location.getLatitude());
				Log.d(TAG, "onLocationChanged location" + location.getLongitude());
			}

			// providerʧЧʱ����
			@Override
			public void onProviderDisabled(String provider) {
				Log.d(TAG, "onProviderDisabled");
			}

			// provider����ʱ����
			@Override
			public void onProviderEnabled(String provider) {
				Log.d(TAG, "onProviderEnabled");
			}

			// ״̬�ı�ʱ����
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				Log.d(TAG, "onStatusChanged");
			}
		};

	
}
