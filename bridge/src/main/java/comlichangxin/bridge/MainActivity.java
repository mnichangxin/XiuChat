package comlichangxin.bridge;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

class JSInterface {
    private WebView webView;
    private Context context;

    public JSInterface(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    @JavascriptInterface
    public void showToast(String callback) {
        Toast.makeText(context, "JS调用Native", Toast.LENGTH_SHORT).show();
        webView.loadUrl("javascript:" + callback + "(" +  "123" + ")");
    }
}

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/test.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JSInterface(this, webView), "JSBridge");
    }
}
