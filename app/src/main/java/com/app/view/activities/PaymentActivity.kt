package com.app.view.activities

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.app.R
import com.app.databinding.ActivityMainBinding
import com.app.databinding.ActivityPaymentBinding
import com.app.databinding.ActivityStatusBinding
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.viewmodel.OtherViewModel
import java.util.*

class PaymentActivity : BaseActivity() , View.OnClickListener {
    private lateinit var binding : ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)
        binding.ivBack.setOnClickListener(this)
        val webSettings = binding.wbPay.settings
        webSettings.javaScriptEnabled = true
        binding.wbPay.loadUrl(SharedPreferencesManager.getString(AppConstants.PAY_LINK))
        WebView.setWebContentsDebuggingEnabled(false)

        binding.wbPay.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }


            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                val errorMessage = "Got Error! $error"
                showToastShort(errorMessage)
                super.onReceivedError(view, request, error)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.pb.visibility = View.GONE
                if(url.toString().contains("payment-success")) {
                    val intent = Intent()
                    setResult(RESULT_OK,intent)
                    onBackPressed()
                }
                super.onPageFinished(view, url)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.ivBack.id -> {
               finish()
            }
        }
    }


}