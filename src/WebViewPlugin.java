package com.zsoftware;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.os.Build;

import static android.content.Context.POWER_SERVICE;

public class WebViewPlugin extends CordovaPlugin {
   private static final String LOG_TAG = "WebViewPlugin";
   private static String SHOW_ACTION = "show";

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    /**
     * Constructor.
     */
    public WebViewPlugin() {
    }


    /**
     * @param action          The action to execute.
     * @param args            JSONArray of arguments for the plugin.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return True when the action was valid, false otherwise.
     */
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (this.cordova.getActivity().isFinishing()) return true;

        if (action.equalsIgnoreCase(SHOW_ACTION)  && args.length() > 0) { //跳转界面
            final String url = args.getString(0);
            Boolean shouldShowLoading = false;
            try{
                shouldShowLoading = args.getBoolean(1);
            }
            catch(Exception e){
                   e.printStackTrace();
            }
            String title ="";
            try{
                title = args.getString(2);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            final String paramTitle = title;
            final Boolean paramShowLoading = shouldShowLoading;
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!"".equals(url)) {
                        showWebViewActivity(url,paramShowLoading,paramTitle);
                        callbackContext.success();
                    }

                }
            });
            return true;
        }

        callbackContext.success();
        return true;
    }

     /**
     * 跳转到新页面窗体
     */
    private void showWebViewActivity( String url, Boolean shouldShowLoading,String title) {
        final Context context = this.cordova.getActivity();
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("shouldShowLoading", shouldShowLoading);
        intent.putExtra("title", title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public Boolean shouldAllowBridgeAccess(String url) {
        return true;
    }
}