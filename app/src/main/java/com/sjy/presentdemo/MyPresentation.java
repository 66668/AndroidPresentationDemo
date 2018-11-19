package com.sjy.presentdemo;

import android.app.AlertDialog;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

/**
 * act和Presentation之间可以设置监听，进行互相通讯
 */
public class MyPresentation extends Presentation {
    Context context;
    TextView textView;

    public MyPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        context = outerContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_display1);

        textView = (TextView) findViewById(R.id.text);
        textView.setText("副屏如果支持触摸，可以点击试试结果");
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Dialog")
                        .setMessage("Prsentation Click Test")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textView.setText("哎呦，不错哦");
//                                dismiss();

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
