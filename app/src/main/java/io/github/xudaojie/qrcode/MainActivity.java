package io.github.xudaojie.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_QR_CODE = 1;
    private WebView webview;
    //加载的目标url
    private static final String WEB_URL = "file:///android_asset/index.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        webview = (WebView) findViewById(R.id.webview);

        WebSettings settings = webview.getSettings();
        //打开js和安卓通信
        settings.setJavaScriptEnabled(true);
        //打开h5定位
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);

        //加载本地assets目录下的网页
        webview.loadUrl(WEB_URL);
        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new MyWebChromeClient());


        //核心方法, 用于处理js被执行后的回调;//参1是回调接口的实现类;参2是js回调对象的名称
        //JS调用方式:window.iosAndroid.xxx,xxx为JsInterface的方法名称
        webview.addJavascriptInterface(new JsInterface(), "iosAndroid");
    }

    /**
     * WebViewClient客户端
     */
    class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    /**
     * WebChromeClient客户端
     */
    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        //开启才能使用H5定位
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, true);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }


    /**
     * 给js调用的接口
     */
    class JsInterface {

        /**
         * 注意:此处一定要加该注解,否则在4.1+系统上运行失败
         * 打开摄像头扫描
         */
        @JavascriptInterface
        public void openQrScaner() {
            System.out.println("js调用Android啦");
            Intent i = new Intent(MainActivity.this, SimpleCaptureActivity.class);
            MainActivity.this.startActivityForResult(i, REQUEST_QR_CODE);
        }
    }

    /**
     * 获取扫描结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webview.loadUrl("javascript:openURL(" + 123456789 + ")");
        if (resultCode == RESULT_OK && requestCode == REQUEST_QR_CODE && data != null) {
            String result = data.getStringExtra("result");
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

}
