package com.sjy.presentdemo;

import android.app.AlertDialog;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyPresentation myPresentation1;
    private MyPresentation2 presentation2;
    private Display display;
    private MediaRouter mediaRouter;//方式1
    private DisplayManager displayManager;//方式2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        checkPermission();

        findViewById(R.id.btn_type1).setOnClickListener(this);
        findViewById(R.id.btn_type2).setOnClickListener(this);
        findViewById(R.id.btn_type3).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    //TODO 这种写法仍有问题，如果拒绝权限进行如下操作，会有bug
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //SYSTEM_ALERT_WINDOW权限申请
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));//不加会显示所有可能的app
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            } else {
                //TODO do something you need
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_type1:
                mediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
                MediaRouter.RouteInfo localRouteInfo = mediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_AUDIO);
                display = localRouteInfo != null ? localRouteInfo.getPresentationDisplay() : null;
                if (display != null) {
                    showPresentation(display);
                } else {
                    Toast.makeText(MainActivity.this, "不支持分屏", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_type2:
                displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
                Display[] arrayOfDisplay = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
                if (arrayOfDisplay.length > 0) {
                    showPresentation(arrayOfDisplay[0]);//取第一个分屏使用
                } else {
                    Toast.makeText(MainActivity.this, "不支持分屏", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_type3:
                displayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
                Display[] presentationDisplays = displayManager.getDisplays();
                if (presentationDisplays.length > 1) {
                    presentation2 = new MyPresentation2(this, presentationDisplays[1]);
                    presentation2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//TYPE_SYSTEM_ALERT / TYPE_PHONE
                    presentation2.show();
                } else {
                    Toast.makeText(MainActivity.this, "不支持分屏", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_back:
                if (myPresentation1 != null) {
                    myPresentation1.dismiss();
                    myPresentation1 = null;
                }
                if (presentation2 != null) {
                    presentation2.dismiss();
                    presentation2 = null;
                }

                break;
        }
    }

    /**
     * 主屏back键/home键隐藏后，副屏仍可使用。但是，再次打开主屏，副屏会失联，所以作如下设置
     *
     * @param display
     */
    private void showPresentation(Display display) {
        if (myPresentation1 == null) {
            myPresentation1 = new MyPresentation(this, display);
        }

        myPresentation1.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//TYPE_SYSTEM_ALERT / TYPE_PHONE
        myPresentation1.show();
    }

}
