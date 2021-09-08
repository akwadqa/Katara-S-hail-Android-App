package com.app.view.activities

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.app.R
import com.app.databinding.ActivityOptionBinding
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.viewmodel.OtherViewModel
import java.util.*

class OptionActivity : BaseActivity(), View.OnClickListener {
    private lateinit var optionBinding: ActivityOptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        optionBinding = DataBindingUtil.setContentView(this, R.layout.activity_option)
        setListeners()
        setData()
    }

    private fun setListeners() {
        optionBinding.tvAuction.setOnClickListener(this)
        optionBinding.tvVisit.setOnClickListener(this)
        optionBinding.tvProceed.setOnClickListener(this)
        optionBinding.tvAr.setOnClickListener(this)
        optionBinding.tvEn.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            optionBinding.tvAuction.id -> {
                SharedPreferencesManager.put(AppConstants.isGuest, 1)
                startActivity(Intent(this, OnboardingOptionActivity::class.java).putExtra("isAuction",true))
            }
            optionBinding.tvVisit.id -> {
                startActivity(Intent(this, OnboardingOptionActivity::class.java).putExtra("isAuction",false))
            }
            optionBinding.tvProceed.id -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            optionBinding.tvEn.id -> {
                optionBinding.tvEn.setTextColor(resources.getColor(R.color.primary))
                optionBinding.tvEn.background = resources.getDrawable(R.drawable.white_rounded_more)
                optionBinding.tvAr.setTextColor(resources.getColor(R.color.white))
                optionBinding.tvAr.background = null
                setLanguage("en")
            }
            optionBinding.tvAr.id -> {
                optionBinding.tvAr.setTextColor(resources.getColor(R.color.primary))
                optionBinding.tvAr.background = resources.getDrawable(R.drawable.white_rounded_more)
                optionBinding.tvEn.setTextColor(resources.getColor(R.color.white))
                optionBinding.tvEn.background = null
                setLanguage("ar")
            }
        }
    }

    private fun setLanguage(value: String) {
        SharedPreferencesManager.put(AppConstants.LANGUAGE,value)
        recreate()
    }


    private fun setData()
    {
        optionBinding.tvAuction.text = resources.getString(R.string.auction)
        optionBinding.tvVisit.text = resources.getString(R.string.visit_the_exhibition)
        optionBinding.tvProceed.text = resources.getString(R.string.proceed_to_app)
        if(SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE) == "en") {
            optionBinding.tvEn.setTextColor(resources.getColor(R.color.primary))
            optionBinding.tvEn.background = resources.getDrawable(R.drawable.white_rounded_more)
            optionBinding.tvAr.setTextColor(resources.getColor(R.color.white))
            optionBinding.tvAr.background = null
        }
        else {
            optionBinding.tvAr.setTextColor(resources.getColor(R.color.primary))
            optionBinding.tvAr.background = resources.getDrawable(R.drawable.white_rounded_more)
            optionBinding.tvEn.setTextColor(resources.getColor(R.color.white))
            optionBinding.tvEn.background = null
        }
        optionBinding.tvAuction.setTypeface(optionBinding.tvAuction.typeface, Typeface.BOLD)
        optionBinding.tvVisit.setTypeface(optionBinding.tvVisit.typeface, Typeface.BOLD)
    }

}