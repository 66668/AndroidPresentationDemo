package com.sjy.presentdemo;

import android.app.AlertDialog;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
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

    private MyPresentation myPresentation;
    private Display display;
    private MediaRouter mediaRouter;//方式1
    private DisplayManager displayManager;//方式2
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        checkPermission();

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
    }

    //这种写法仍有问题，如果拒绝权限进行如下操作，会有问题，可以大胆测试下
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //SYSTEM_ALERT_WINDOW权限申请
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
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
            case R.id.btn1:
                mediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
                MediaRouter.RouteInfo localRouteInfo = mediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_AUDIO);
                display = localRouteInfo != null ? localRouteInfo.getPresentationDisplay() : null;
                if (display != null) {
                    showPresentation(display);
                } else {
                    Toast.makeText(MainActivity.this, "不支持分屏", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn2:
                displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
                Display[] arrayOfDisplay = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
                if (arrayOfDisplay.length > 0) {
                    showPresentation(arrayOfDisplay[0]);//取第一个分屏使用
                } else {
                    Toast.makeText(MainActivity.this, "不支持分屏", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn3:
                displayManager = (DisplayManager) this.getSystemService(Context.DISPLAY_SERVICE);
                Display[] presentationDisplays = displayManager.getDisplays();
                if (presentationDisplays.length > 1) {
                    MyPresentation2 presentation = new MyPresentation2(this, presentationDisplays[1]);
                    presentation.show();
                } else {
                    Toast.makeText(MainActivity.this, "不支持分屏", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn4:
                if (myPresentation != null) {
                    myPresentation.dismiss();
                    myPresentation = null;
                }
                break;
        }
    }


    private void showPresentation(Display display) {
        if (myPresentation == null) {
            myPresentation = new MyPresentation(this, display);
        }

        myPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//TYPE_SYSTEM_ALERT / TYPE_PHONE
        myPresentation.show();
    }

}
