package com.passitwiki.passit.dialogfragments

import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import com.passitwiki.passit.R
import kotlinx.android.synthetic.main.fragment_web_view_dialog.view.*

class WebViewDialogFragment : DialogFragment() {

    companion object {
        const val KEY = "WebViewDialogFragment"
        const val URL = "AttachmentUrl"
        fun newInstance(key: String, url: String): DialogFragment {
            val webViewDialogFragment =
                WebViewDialogFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            argument.putString(URL, url)
            webViewDialogFragment.arguments = argument
            return webViewDialogFragment
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_web_view_dialog, container, false)
        val webView = rootView.webView
        initWebView(webView)
        webView.loadUrl(arguments!!.getString(URL))

        rootView.buttonArrowBack.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    private fun initWebView(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }
        }

    }
}
