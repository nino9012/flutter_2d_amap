package com.weilu.flutter.flutter_2d_amap;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.services.core.ServiceSettings;

import java.util.logging.Logger;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * Flutter2dAmapPlugin
 * @author weilu
 * */
public class Flutter2dAmapPlugin implements FlutterPlugin, ActivityAware{

  private AMap2DDelegate delegate;
  private FlutterPluginBinding pluginBinding;
  private ActivityPluginBinding activityBinding;
  private MethodChannel methodChannel;
  
  public Flutter2dAmapPlugin() {}
  
//  /** Plugin registration. */
//  public static void registerWith(Registrar registrar) {
//    if (registrar.activity() == null) {
//      return;
//    }
//    // 添加权限回调监听
//    final AMap2DDelegate delegate = new AMap2DDelegate(registrar.activity());
//    registrar.addRequestPermissionsResultListener(delegate);
//    registrar.platformViewRegistry().registerViewFactory("plugins.weilu/flutter_2d_amap", new AMap2DFactory(registrar.messenger(), delegate));
//  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    pluginBinding = binding;
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    pluginBinding = null;
  }

  @Override
  public void onAttachedToActivity(@NonNull final ActivityPluginBinding binding) {
    activityBinding = binding;
    
    final BinaryMessenger messenger = pluginBinding.getBinaryMessenger();


    methodChannel = new MethodChannel(messenger, "plugins.weilu/flutter_2d_amap_");
    methodChannel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {
      @Override
      public void onMethodCall(@NonNull MethodCall methodCall, @NonNull MethodChannel.Result result) {
        String method = methodCall.method;
        switch(method) {
          case "updatePrivacy":
            boolean isAgree = "true".equals(methodCall.arguments);
            ServiceSettings.updatePrivacyShow(binding.getActivity(), isAgree, isAgree);
            ServiceSettings.updatePrivacyAgree(binding.getActivity(), isAgree);
            AMapLocationClient.updatePrivacyShow(binding.getActivity(), isAgree, isAgree);
            AMapLocationClient.updatePrivacyAgree(binding.getActivity(), isAgree);
            break;
          case "setOnlyLocation":
            boolean isOnlyLocation = "true".equals(methodCall.arguments);
            if (isOnlyLocation){
              Log.d("location","setOnlyLocation true");
              OnlyLocation onlyLocation = new OnlyLocation(pluginBinding.getApplicationContext(),messenger);
              delegate = new AMap2DDelegate(binding.getActivity());
              binding.addRequestPermissionsResultListener(delegate);
              onlyLocation.setAMap2DDelegate(delegate);
            }else {
              Log.d("location","setOnlyLocation flase");
              AMap2DFactory mFactory = new AMap2DFactory(pluginBinding.getBinaryMessenger(), null);
              pluginBinding.getPlatformViewRegistry().registerViewFactory("plugins.weilu/flutter_2d_amap", mFactory);

              delegate = new AMap2DDelegate(binding.getActivity());
              binding.addRequestPermissionsResultListener(delegate);
              mFactory.setDelegate(delegate);
            }
            break;
          default:
            break;
        }
      }
    });

  }



  @Override
  public void onDetachedFromActivity() {
    tearDown();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity();
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    onAttachedToActivity(binding);
  }

  private void tearDown() {
    activityBinding.removeRequestPermissionsResultListener(delegate);
    activityBinding = null;
    delegate = null;
    methodChannel.setMethodCallHandler(null);
  }
}
