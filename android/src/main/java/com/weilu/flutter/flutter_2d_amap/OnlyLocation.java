package com.weilu.flutter.flutter_2d_amap;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;

import org.json.JSONArray;
import org.json.JSONObject;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.JSONUtil;
import io.flutter.plugin.common.MethodChannel;

public class OnlyLocation {

    //    private AMap aMap;
    private AMapLocationListener mListener;
    private AMapLocationClient mLocationClient;
    private final MethodChannel methodChannel;
    private final Handler platformThreadHandler;
    private Runnable postMessageRunnable;
    private final Context mContext;

    OnlyLocation(Context context, BinaryMessenger messenger) {
        this.mContext = context;
        platformThreadHandler = new Handler(context.getMainLooper());
        methodChannel = new MethodChannel(messenger, "plugins.weilu/flutter_2d_amap_");
        Log.d("location", "OnlyLocation 初始化");
        mListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(final AMapLocation aMapLocation) {
                if (mListener != null && aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        // 显示系统小蓝点
//                mListener.onLocationChanged(aMapLocation);
                        Log.d("location", "定位成功" + aMapLocation.getAddress() + " str "
                                + aMapLocation.toStr() + aMapLocation.getLocationDetail());
//                        Toast.makeText(mContext, "定位成功！" + aMapLocation.getAddress(), Toast.LENGTH_LONG).show();
                        String location = aMapLocation.getLocationDetail();
                        postMessageRunnable = new Runnable() {
                            @Override
                            public void run() {
//                        Map<String, String> map = new HashMap<>(2);
//                        map.put("location", builder.toString());
                                methodChannel.invokeMethod("locationResult", aMapLocation.toStr());
                            }
                        };
                        if (platformThreadHandler.getLooper() == Looper.myLooper()) {
                            postMessageRunnable.run();
                        } else {
                            platformThreadHandler.post(postMessageRunnable);
                        }
                    } else {
                        Log.d("location", "定位失败" + aMapLocation.getErrorCode());
                        Toast.makeText(mContext, "定位失败，请检查GPS是否开启！", Toast.LENGTH_SHORT).show();
                    }
                    if (mLocationClient != null) {
                        mLocationClient.stopLocation();
                    }
                }
            }
        };
        getLocation();
    }


    void setAMap2DDelegate(AMap2DDelegate delegate) {
        if (delegate != null) {
            delegate.requestPermissions(new AMap2DDelegate.RequestPermission() {
                @Override
                public void onRequestPermissionSuccess() {

                    if (mLocationClient != null) {
                        mLocationClient.startLocation();
                    }
//                    setUpMap();
                }

                @Override
                public void onRequestPermissionFailure() {
//                    Toast.makeText(mContext.getApplicationContext(), "定位失败，请检查定位权限是否开启！", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

//    @Override
//    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
//        mListener = onLocationChangedListener;
//        if (mLocationClient == null) {
//            try {
//                mLocationClient = new AMapLocationClient(context);
//                AMapLocationClientOption locationOption = new AMapLocationClientOption();
//                mLocationClient.setLocationListener(this);
//                //设置为高精度定位模式
//                locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//                //设置定位参数
//                mLocationClient.setLocationOption(locationOption);
//                mLocationClient.startLocation();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    @Override
//    public void deactivate() {
//        mListener = null;
//        if (mLocationClient != null) {
//            mLocationClient.stopLocation();
//            mLocationClient.onDestroy();
//        }
//        mLocationClient = null;
//    }


//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (mListener != null && aMapLocation != null) {
//            if (aMapLocation.getErrorCode() == 0) {
//                // 显示系统小蓝点
////                mListener.onLocationChanged(aMapLocation);
//                Log.d("android","定位成功"+aMapLocation.getAddress());
//                Toast.makeText(mContext,"定位成功！"+aMapLocation.getAddress(), Toast.LENGTH_LONG).show();
////                postMessageRunnable = new Runnable() {
////                    @Override
////                    public void run() {
////                        Map<String, String> map = new HashMap<>(2);
////                        map.put("location", builder.toString());
////                        methodChannel.invokeMethod("poiSearchResult", map);
////                    }
////                };
////                if (platformThreadHandler.getLooper() == Looper.myLooper()) {
////                    postMessageRunnable.run();
////                } else {
////                    platformThreadHandler.post(postMessageRunnable);
////                }
//            } else {
//                Log.d("android","定位失败"+aMapLocation.getErrorCode());
//                Toast.makeText(mContext,"定位失败，请检查GPS是否开启！", Toast.LENGTH_SHORT).show();
//            }
//            if (mLocationClient != null) {
//                mLocationClient.stopLocation();
//            }
//        }
//    }

    public void getLocation() {
        //初始化定位
        try {
            Log.d("location", "init");
            mLocationClient = new AMapLocationClient(this.mContext);
            //设置定位回调监听
            mLocationClient.setLocationListener(mListener);
            //初始化AMapLocationClientOption对象
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            /**
             * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
             */
            //不允许模拟定位
            mLocationOption.setMockEnable(false);
            mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
            if (null != mLocationClient) {
                mLocationClient.setLocationOption(mLocationOption);
                //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                mLocationClient.stopLocation();
                mLocationClient.startLocation();
            }
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
            Log.d("android", "init end");
//        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
//        mLocationClient.onDestroy();
        } catch (Exception e) {

        }

    }
//    private void setUpMap() {
//        Log.d("android","setUpMap");
////        CameraUpdateFactory.zoomTo(32);
//        // 设置定位监听
////        aMap.setLocationSource(this);
//        // 设置默认定位按钮是否显示
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.strokeWidth(1f);
//        myLocationStyle.strokeColor(Color.parseColor("#8052A3FF"));
//        myLocationStyle.radiusFillColor(Color.parseColor("#3052A3FF"));
//        myLocationStyle.showMyLocation(true);
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.yd));
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
////        aMap.setMyLocationStyle(myLocationStyle);
//        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
////        aMap.setMyLocationEnabled(true);
//    }
}
