package com.sjy.presentdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        setContentView(R.layout.activity_main);
        checkPermission();

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);


        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.d("chenhuan", "isWifiEnabled = " + wifiManager.isWifiEnabled());

    }

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

    int index = 0;
    int[] texts = {R.string.first, R.string.second, R.string.third, R.string.forth};

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                mediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
                MediaRouter.RouteInfo localRouteInfo = mediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_AUDIO);
//                MediaRouter.RouteInfo localRouteInfo = mediaRouter.getSelectedRoute(2);
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
                if (arrayOfDisplay.length != 0) {
                    showPresentation(arrayOfDisplay[0]);
                } else {
                    Toast.makeText(MainActivity.this, "不支持分屏", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn3:
                if (myPresentation != null) {
                    if (index > 3)
                        index = 0;
                    textView.setText(texts[index]);
                    Log.d("chenhuan", "setBackgroundColor with index " + index);
                    index++;
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

    private class MyPresentation extends Presentation {

        public MyPresentation(Context outerContext, Display display) {
            super(outerContext, display);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.presentation_layout);

            textView = (TextView) findViewById(R.id.text);
            findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Dialog")
                            .setMessage("Prsentation Click Test")
                            .setCancelable(false)
                            .setPositiveButton("OK", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    dismiss();

                                }
                            }).create().show();
                }
            });

            findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
}
