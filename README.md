# 双屏异显使用教程
为开发酒店柜台刷身份证+识别VIP用户而定制的双屏异显设备。测试时请用双屏设备测试。

##使用具体步骤

### 1.申请权限:SYSTEM_ALERT_WINDOW
需要在AndroidManifest.xml中添加权限：

    <uses-permission  android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    
说明：SYSTEM_ALERT_WINDOW权限申请和危险权限申请步骤不同，而且不需要startActivityForResult/onActivityResult的回调:

            //大于android 6.0才可以申请权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    //隐式Intent
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));//不加会显示所有可能的app
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //TODO 非常不建议使用startActivityForResult，已测试。
                } else {
                    Toast.makeText(SpecialAct.this, "双屏异显-授权通过，可以使用了", Toast.LENGTH_SHORT).show();
                    //TODO do something you need
                }
            }

 ### 2.三种方式获取Display类:
 
  ####方式1：MediaRouter
  
    mediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
                  MediaRouter.RouteInfo localRouteInfo = mediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_AUDIO);
                  display = localRouteInfo != null ? localRouteInfo.getPresentationDisplay() : null;
                  if (display != null) {
                      showPresentation(display);
                  } else {
                      Toast.makeText(MainActivity.this, "不支持分屏", Toast.LENGTH_SHORT).show();
                  }

  
   ####方式2：获取可支持的displahy,displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION)
   
        displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
                   Display[] arrayOfDisplay = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
                   if (arrayOfDisplay.length > 0) {
                       showPresentation(arrayOfDisplay[0]);//取第一个分屏使用
                   } else {
                       Toast.makeText(MainActivity.this, "不支持分屏", Toast.LENGTH_SHORT).show();
                   }
                   
 ####方式3：获取所有的display,包括主屏幕displayManager.getDisplays()
 
    displayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
                 Display[] presentationDisplays = displayManager.getDisplays();
                 if (presentationDisplays.length > 1) {
                     presentation2 = new MyPresentation2(this, presentationDisplays[1]);
                     presentation2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//TYPE_SYSTEM_ALERT / TYPE_PHONE
                     presentation2.show();
                 } else {
                     Toast.makeText(MainActivity.this, "不支持分屏", Toast.LENGTH_SHORT).show();
                 }
 
 ##           