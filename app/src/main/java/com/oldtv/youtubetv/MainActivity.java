package com.oldtv.youtubetv;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

    private WebView webView;
    private ProgressBar progressBar;

    // TV-optimised desktop user-agent so YouTube serves the full site
    private static final String USER_AGENT =
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 " +
        "(KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36";

    private static final String YOUTUBE_TV_URL = "https://www.youtube.com/tv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full-screen, no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        webView     = findViewById(R.id.webView);

        setupWebView();

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            webView.loadUrl(YOUTUBE_TV_URL);
        }
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();

        // Core settings
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        // Media / hardware
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        // Layout
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        // Cache — keep working offline after first load
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Desktop UA so YouTube TV interface loads properly
        settings.setUserAgentString(USER_AGENT);

        // Allow mixed content (needed on older Android)
        try {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        } catch (NoSuchMethodError ignored) { /* API < 21 */ }

        // Hardware acceleration for smooth video
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // Keep screen on while the app is open
        webView.setKeepScreenOn(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Stay inside the WebView — don't open the browser
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    // D-pad / remote navigation
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                webView.scrollBy(0, -150);
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                webView.scrollBy(0, 150);
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                webView.scrollBy(-150, 0);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                webView.scrollBy(150, 0);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
