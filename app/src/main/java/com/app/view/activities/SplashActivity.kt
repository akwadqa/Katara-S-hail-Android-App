package com.app.view.activities

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.BuildConfig
import com.app.R
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.AppUtils
import com.app.utils.SharedPreferencesManager
import com.app.viewmodel.OtherViewModel
import java.util.*


class SplashActivity : BaseActivity() {
    private val optionViewModel by lazy { ViewModelProvider(this).get(OtherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setBaseViewModel(optionViewModel)
        setObservers()
        if(AppUtils.isInternetAvailable(this)) {
            optionViewModel.checkForceUpdate()
        }
        else {
            showToastLong(resources.getString(R.string.no_internet_connection))
            finish()
        }
    }

    private fun setObservers() {
        optionViewModel.getResponseObserver().observe(this, this)
    }


    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.LOGIN -> {
                if(optionViewModel.msg.isNotEmpty()) {
                    Toast.makeText(this, optionViewModel.msg, Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, OptionActivity::class.java))
                        finish()
                    }, 2000)
                }
            }
            ApiCodes.FORCE_UPDATE -> {
                getUpdateData()
                /*if(optionViewModel.dataMLD.value!!.error==true) {
                    navigateToNextScreen()
                }
                else {
                    getUpdateData()
                }*/
            }
        }
    }

    private fun navigateToNextScreen()
    {
        var userId : String = SharedPreferencesManager.getString(AppConstants.USER_ID)
        if(userId.isNullOrBlank()) {
            optionViewModel.hitLogin()
        }
        else
        {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 2000)
        }
    }

    private fun getUpdateData() {
        if (optionViewModel.dataMLD.value!!.androidVersion != null && versionCompare(optionViewModel.dataMLD.value!!.androidVersion.toString(),
                BuildConfig.VERSION_NAME) > 0) {
            showUpdateDialog()
        } else {
            navigateToNextScreen()
        }
    }

    private fun versionCompare(v1: String, v2: String): Int {
        // vnum stores each numeric part of version
        var vnum1 = 0
        var vnum2 = 0

        // loop untill both String are processed
        var i = 0
        var j = 0
        while (i < v1.length || j < v2.length) {

            // storing numeric part of version 1 in vnum1
            while (i < v1.length && v1[i] != '.') {
                vnum1 = vnum1 * 10 + (v1[i] - '0')
                i++
            }

            // storing numeric part of version 2 in vnum2
            while (j < v2.length && v2[j] != '.') {
                vnum2 = vnum2 * 10 + (v2[j] - '0')
                j++
            }
            if (vnum1 > vnum2) return 1
            if (vnum2 > vnum1) return -1

            // if equal, reset variables and go for next numeric
            // part
            vnum2 = 0
            vnum1 = vnum2
            i++
            j++
        }
        return 0
    }


    private fun showUpdateDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.app_name))
        var msg = ""
        if(SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE)=="ar")
            msg = optionViewModel.dataMLD.value!!.msgAr.toString()
        else
            msg = optionViewModel.dataMLD.value!!.msgEn.toString()
        builder.setMessage(msg)
        builder.setCancelable(false)
        builder.setPositiveButton(resources.getString(R.string.update)
        ) { dialog, which ->
            builder.show()
            try {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=shail.katara.android")))
            } catch (anfe: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=shail.katara.android")))
            }
        }
        if (optionViewModel.dataMLD.value!!.mandatory == false) builder.setNegativeButton(resources.getString(R.string.cancel)
        ) { dialog: DialogInterface, which: Int ->
            navigateToNextScreen()
            dialog.cancel()
        }
        builder.show()
    }

}