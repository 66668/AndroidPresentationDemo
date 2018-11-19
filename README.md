# 双屏异显学习
## 申请权限:SYSTEM_ALERT_WINDOW
需要在AndroidManifest.xml中添加权限：

    <uses-permission  android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    
说明：SYSTEM_ALERT_WINDOW权限申请和危险权限申请步骤不同，而且不需要startActivityForResult/onActivityResult的回调：

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