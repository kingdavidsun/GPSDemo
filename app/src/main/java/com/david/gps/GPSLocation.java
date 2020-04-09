package com.david.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.List;

/**
 * Created by David on 2020/4/8
 */
public class GPSLocation {
    private static final String TAG = "GPSLocation";
    private LocationManager locationManager;
    private String locationProvider= LocationManager.GPS_PROVIDER;
    private OnLocationChangeListener mOnLocationChangeListener;

    public void setOnLocationChangeListener(OnLocationChangeListener onLocationChangeListener) {
        mOnLocationChangeListener = onLocationChangeListener;
    }

    public GPSLocation(Context context) {
        getLocation(context);
    }

    private void getLocation(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Log.i(TAG, "getLocation: ");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //没有权限
            } else {
                //监视地理位置变化
                locationManager.requestLocationUpdates(locationProvider, 3000, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(locationProvider);
                if (location != null) {
                    //不为空,显示地理位置经纬度
                }
            }
        } else {
            //监视地理位置变化
            locationManager.requestLocationUpdates(locationProvider, 3000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                //不为空,显示地理位置经纬度
            }
        }
    }
    public LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null&&mOnLocationChangeListener!=null) {
                //不为空,显示地理位置经纬度
                mOnLocationChangeListener.onLocationChange(location.getLongitude(),location.getLatitude());
            }
        }
    };
    private void stop(){
        locationManager.removeUpdates(locationListener);
    }
    public interface OnLocationChangeListener{
        void onLocationChange(double lo, double la);
    }
}
