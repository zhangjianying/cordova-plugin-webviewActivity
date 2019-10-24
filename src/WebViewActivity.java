package com.zsoftware;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import org.apache.cordova.CordovaActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewActivity extends CordovaActivity
{
    static Dialog dialog;
    static WebViewActivity activity2;

    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,


    };
    /**
     * 开始提交请求权限
     */
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 3721);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity2 = this;
        Bundle b = getIntent().getExtras();
        String url = b.getString("url");
        String title = b.getString("title");
        Boolean isBackBtn=true;
        Boolean shouldShowLoading = false;
        try{
            shouldShowLoading = b.getBoolean("shouldShowLoading");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        url=  getDefaultParams(url);
        Log.d("WebViewActivity","url="+url);


        //动态申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]);
            int l = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]);
            int m = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[2]);
            int n = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[3]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED || l != PackageManager.PERMISSION_GRANTED || m != PackageManager.PERMISSION_GRANTED ||
                    n != PackageManager.PERMISSION_GRANTED) {
                startRequestPermission();
            }else{
                loadUrl(url);
                if(shouldShowLoading){
                    showLoading();
                }
                initTitle(title, isBackBtn);
            }
        }else{// 6.0以下处理流程
            loadUrl(url);
            if(shouldShowLoading){
                showLoading();
            }
            initTitle(title, isBackBtn);
        }


    }


    private String getDefaultParams(String url){
        ArrayList<String> params = new ArrayList<String>();
        params.add("v="+getAppVersionName());
        params.add("p=android");
        params.add("rt="+new Date().getTime());

        String extendParams = TextUtils.join("&", params);
        if(url.indexOf("?")>-1)
            url=url+"&"+extendParams;
        else
            url=url+"?"+extendParams;
        return url;
    }

    //获取版本号
    private String getAppVersionName(){
        String retVal ="";
        PackageManager packageManager = activity2.getPackageManager();
        try {
            retVal= packageManager.getPackageInfo(activity2.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    /**
     * 用户权限 申请 的回调方法
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3721) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //如果没有获取权限，那么可以提示用户去设置界面--->应用权限开启权限
                    Toast.makeText(activity2, "由于您拒绝授权应用权限,无法使用相关功能", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle b = getIntent().getExtras();
                    String url = b.getString("url");
                    url=  getDefaultParams(url);
                    String title = b.getString("title");
                    Boolean isBackBtn=true;
                    Boolean shouldShowLoading = false;
                    try{
                        shouldShowLoading = b.getBoolean("shouldShowLoading");
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    loadUrl(url);
                    if(shouldShowLoading){
                        showLoading();
                    }
                    initTitle(title, isBackBtn);
                }
            }
        }
    }


    public static boolean showLoading() {
        // Loading spinner
        activity2.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = new Dialog(activity2,android.R.style.Theme_Translucent_NoTitleBar);
                ProgressBar progressBar = new ProgressBar(activity2,null,android.R.attr.progressBarStyle);

                LinearLayout linearLayout = new LinearLayout(activity2);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                RelativeLayout layoutPrincipal = new RelativeLayout(activity2);
                layoutPrincipal.setBackgroundColor(Color.parseColor("#00b5b2b2"));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                linearLayout.addView(progressBar);
                linearLayout.setLayoutParams(params);
                layoutPrincipal.addView(linearLayout);
                dialog.setContentView(layoutPrincipal);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                    }
                });
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK){
                            dialog.hide();
                    }
                        return false;
                    }
                });

                dialog.show();
                new Handler().postDelayed(new Runnable(){

                    public void run() {
                        dialog.hide();
                    }

                }, 2000);
            }
        });

        return true;
    }
    private void initTitle(String title, boolean isBack) {
        boolean isTitle = false;

        //标题高度
        int h = dip2px(50);

        //标题整体布局
        RelativeLayout titleBoxView = new RelativeLayout(this);
        RelativeLayout.LayoutParams boxLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        titleBoxView.setBackgroundColor(Color.BLACK);

        //添加标题
        if (!(title == null || title.trim().equals(""))) {
            setMyTitle(title, titleBoxView);
            isTitle = true;
        }
        //添加返回
        if (isBack) {
            setBackBtn(titleBoxView);
            isTitle = true;
        }
        //有标题或返回,才添加标题
        if (isTitle) {
            setAppViewMargins(h);//整体布局下移标题高度
            addContentView(titleBoxView, boxLp);//添加标题布局进入页面
        }


    }


    //添加关闭按钮
    private void setBackBtn(RelativeLayout box) {
        TextView text = new TextView(this);
        text.setText("×");
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 33);
        text.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        lp.setMargins(dip2px(10), 0, 0, 0);
        text.setLayoutParams(lp);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        box.addView(text);
    }

    //设置标题
    private void setMyTitle(String str, RelativeLayout box) {
        if (str == null || str.trim().equals("")) {
            return;
        }
        int margin = 60;
        TextView text = new TextView(this);
        text.setText(str);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        text.setTextColor(Color.WHITE);
        //text.setBackgroundColor(Color.GREEN);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        lp.setMargins(dip2px(margin), dip2px(2), dip2px(margin), 0);
        text.setLayoutParams(lp);
        text.setMaxLines(1);
        text.setEllipsize(TextUtils.TruncateAt.END);
        box.addView(text);
    }

    //设置页面的margins
    private void setAppViewMargins(int h) {
        View view = appView.getView();
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        lp.setMargins(0, h, 0, 0);
        view.setLayoutParams(lp);
    }

    //ps转px
    private int dip2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}