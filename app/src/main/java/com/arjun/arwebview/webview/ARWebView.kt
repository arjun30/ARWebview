package com.arjun.arwebview.webview

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebView

class ARWebView : WebView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    init {
        settings.javaScriptEnabled = true
    }

    override fun setWebChromeClient(client: WebChromeClient?) {
        super.setWebChromeClient(client)
    }

    override fun loadUrl(url: String) {
        addJavaScriptInterface()
        super.loadUrl(url)
    }

    private fun addJavaScriptInterface() {
        val jsInterface = ARJavascriptInterface()
        addJavascriptInterface(jsInterface, ARJavascriptInterface.name)
    }
}