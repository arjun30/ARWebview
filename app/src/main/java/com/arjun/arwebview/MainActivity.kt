package com.arjun.arwebview

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.arjun.arwebview.databinding.ActivityMainBinding
import com.arjun.arwebview.viewmodel.MainViewModel
import com.arjun.arwebview.webview.ARWebChromeClient
import com.arjun.arwebview.webview.IVideoOrientation
import org.json.JSONArray

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), IVideoOrientation {

    private lateinit var binding: ActivityMainBinding
    private lateinit var webChromeClient: ARWebChromeClient
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        prepareWebview()
        binding.webviewAr.loadUrl("file:///android_asset/videoplayer.html")
    }

    private fun prepareWebview() {

        webChromeClient = ARWebChromeClient()
        webChromeClient.setContainerView(binding.containerHalfscreen, binding.containerFullscreen)
        webChromeClient.setOrientationCallback(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        binding.webviewAr.webChromeClient = webChromeClient
        binding.webviewAr.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.evaluateJavascript(
                    "javascript:prepareVideoPlayer('${JSONArray(viewModel.getVideosList())}')",
                    null
                );
            }
        }

    }

    override fun onBackPressed() {
        if (!webChromeClient.handleBackPressEvent()) {
            if (binding.webviewAr.canGoBack()) {
                binding.webviewAr.goBack()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun handleOrientation(isFullScreen: Boolean) {

        // Handling the orientation and full screen mode
        if (isFullScreen) {
            val attrs = window.attributes
            attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            window.attributes = attrs
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
        } else {
            val attrs = window.attributes
            attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON.inv()
            window.attributes = attrs
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }

    }
}