package com.andieguoe.locationdemo;

import java.util.Iterator;

import android.app.Activity;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GpsActivity extends Activity {
	private LocationManager locationManager;
	private GpsStatus gpsstatus;
	private static final String TAG = "GpsActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps);

		// ��ȡ��LocationManager����
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// �������õ�Criteria���󣬻�ȡ����ϴ˱�׼��provider����
		String currentProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER).getName();

		// ���ݵ�ǰprovider�����ȡ���һ��λ����Ϣ
		Location currentLocation = locationManager.getLastKnownLocation(currentProvider);
		// ���λ����ϢΪnull�����������λ����Ϣ
		if (currentLocation == null) {
			locationManager.requestLocationUpdates(currentProvider, 0, 0, locationListener);
		}
		// ����GPS״̬������
		locationManager.addGpsStatusListener(gpsListener);

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
	}

	private GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
		// GPS״̬�����仯ʱ����
		@Override
		public void onGpsStatusChanged(int event) {
			// ��ȡ��ǰ״̬
			gpsstatus = locationManager.getGpsStatus(null);
			switch (event) {
			// ��һ�ζ�λʱ���¼�
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				break;
			// ��ʼ��λ���¼�
			case GpsStatus.GPS_EVENT_STARTED:
				break;
			// ����GPS����״̬�¼�
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Toast.makeText(GpsActivity.this, "GPS_EVENT_SATELLITE_STATUS", Toast.LENGTH_SHORT).show();
				Iterable<GpsSatellite> allSatellites = gpsstatus.getSatellites();
				Iterator<GpsSatellite> it = allSatellites.iterator();
				int count = 0;
				while (it.hasNext()) {
					count++;
				}
				Toast.makeText(GpsActivity.this, "Satellite Count:" + count, Toast.LENGTH_SHORT).show();
				break;
			// ֹͣ��λ�¼�
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.d(TAG, "GPS_EVENT_STOPPED");
				break;
			}
		}
	};

	// ����λ�ü�����
	private LocationListener locationListener = new LocationListener() {
		// λ�÷����ı�ʱ����
		@Override
		public void onLocationChanged(Location location) {
			Log.d(TAG, "onLocationChanged");
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
