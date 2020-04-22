# cordova-plugin-webviewActivity
通用android webview窗体.可复用现在已有的插件


# 安装
```javascript
cordova plugin add https://github.com/zhangjianying/cordova-plugin-webviewActivity.git
```

# 使用
```javascript
webView.show('http://XXXXX/cordovaDoc/test/index.html',true,'我是标题');

webView.close(); // 关闭当前窗口
```





# 注意

手工到cordova config.xml中添加

```xml
 <allow-navigation href="https://*/*" />
 <allow-navigation href="http://*/*" />
```

ios在未添加的情况下会默认打开浏览器