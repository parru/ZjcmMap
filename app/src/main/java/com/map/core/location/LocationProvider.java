
package com.map.core.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.map.core.animation.MyPlaceIconAnim;

// 接下来就是主要代码了，请大家鉴定，有错误的地方可以提出，一起改进。
@SuppressLint("NewApi")
public class LocationProvider implements Runnable {
	private Context context;
	private Location location = null;
	private LocationManager locationManager;
	private MyPlaceIconAnim animation;
	
	public LocationProvider(Context context, MyPlaceIconAnim animation){
		this.context = context;
		this.animation = animation;
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(getCriteria(), true);
        
        try{
        	if(provider != null){
        		location = locationManager.getLastKnownLocation(provider);
        		locationManager.requestLocationUpdates(provider,500,8,locationListener);
        	}else{
        		return;
        	}
        	
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
	}
	
	private LocationListener locationListener = new LocationListener() {	
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			updateLocation(location);
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			updateLocation(null);
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Location location = locationManager.getLastKnownLocation(provider);
			updateLocation(location);
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			updateLocation(location);
		}
	};
	
	public Location getMyLocation(){
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(getCriteria(), true);
        location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 500, 8, locationListener);
        return this.location;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void updateLocation(Location location){
		Log.i("myLocation-->", Double.toString(location.getLongitude()));
		this.location = location;	
		animation.updateMyLocation(location);
	}
	
	public Criteria getCriteria(){
    	Criteria criteria = new Criteria();
    	criteria.setAccuracy(Criteria.ACCURACY_COARSE);
    	criteria.setSpeedRequired(false);
    	criteria.setCostAllowed(false);
    	criteria.setBearingRequired(false);
    	criteria.setAltitudeRequired(false);
    	criteria.setPowerRequirement(Criteria.POWER_LOW);
    	return criteria;
    }
}