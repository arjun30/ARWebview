package com.arjun.arwebview.webview

import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.FrameLayout

/**
 *
 *  This class will acts as WebViewChromeClient for Webview.
 *  It will handle videos orientation along with custom layout to handle fullscreen mode.
 *
 *  @author Arjun Singh
 */

class ARWebChromeClient() : WebChromeClient() {

    private lateinit var halfScreenContainer: ViewGroup
    private lateinit var fullScreenContainer: ViewGroup
    private var videoViewContainer: FrameLayout? = null
    private var viewCallback: CustomViewCallback? = null
    private var isFullScreen: Boolean = false
    private var orientationCallback: IVideoOrientation? = null


    fun setContainerView(halfScreenContainer: ViewGroup, fullScreenContainer: ViewGroup) {
        this.halfScreenContainer = halfScreenContainer
        this.fullScreenContainer = fullScreenContainer
    }

    fun setOrientationCallback(callback: IVideoOrientation) {
        orientationCallback = callback
    }

    /**
     *  This mehod will be used to handle the back event in Fullscreen mode.
     */
    fun handleBackPressEvent(): Boolean {
        return if (isFullScreen) {
            onHideCustomView()
            true
        } else {
            false
        }
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        super.onShowCustomView(view, callback)

        if (view is FrameLayout) {

            isFullScreen = true
            viewCallback = callback

            this.videoViewContainer = view

            // Hide halfscreen container and display video in fullscree container
            halfScreenContainer.visibility = View.INVISIBLE
            fullScreenContainer.addView(
                videoViewContainer,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            fullScreenContainer.visibility = View.VISIBLE

            // Handling orientation
            orientationCallback?.let {
                it.handleOrientation(true)
            }
        }
    }

    override fun onHideCustomView() {
        super.onHideCustomView()

        if (isFullScreen) {
            halfScreenContainer.visibility = View.VISIBLE
            fullScreenContainer.visibility = View.INVISIBLE
            fullScreenContainer.removeView(videoViewContainer)
            videoViewContainer = null

            // Handling close event in cases like back button
            viewCallback?.let {
                it.onCustomViewHidden()
            }

            // Handling orientation
            orientationCallback?.let {
                it.handleOrientation(false)
            }

            isFullScreen = false
        }
    }

}