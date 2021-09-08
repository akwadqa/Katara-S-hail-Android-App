package com.app.view.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.app.R
import com.app.databinding.ActivityRegisterOneBinding
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.viewmodel.RegisterOneViewModel
import java.text.SimpleDateFormat
import java.util.*


class RegistrationOneActivity : BaseActivity(), View.OnClickListener {
    private lateinit var registerOneBinding: ActivityRegisterOneBinding
    private val registerOneViewModel by lazy { ViewModelProvider(this).get(RegisterOneViewModel::class.java) }
    private val calendar: Calendar = Calendar.getInstance()
    private var dob : String = ""
    private var isAuction: Boolean ? = false
    private var isPassport: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerOneBinding = DataBindingUtil.setContentView(this, R.layout.activity_register_one)
        setBaseViewModel(registerOneViewModel)
        setListeners()
        setObservers()
        isAuction = intent.getBooleanExtra("isAuction",false)
        isPassport = intent.getBooleanExtra("isPassport",false)
    }

    private fun setListeners() {
        registerOneBinding.tvContinue.setOnClickListener(this)
        registerOneBinding.ivBack.setOnClickListener(this)
        registerOneBinding.dateEt.setOnClickListener(this)
    }

    private fun setObservers() {
        registerOneViewModel.getResponseObserver().observe(this, this)
        registerOneViewModel.getValidationLiveData().observe(this,
            {
                when (it.value) {
                    false -> {
                        showToastLong(it.message)
                    }
                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            registerOneBinding.tvContinue.id -> {
                registerOneViewModel.qID = registerOneBinding.numberEt.text.toString()
                registerOneViewModel.date = dob
                registerOneViewModel.validate(this)
            }
            registerOneBinding.ivBack.id -> {
                finish()
            }
            registerOneBinding.dateEt.id -> {
                getDatePicker().show()
            }
        }
    }

    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.CHECK_QID -> {
                if(registerOneViewModel.msg.isNotEmpty()) {
                    Toast.makeText(this, registerOneViewModel.msg, Toast.LENGTH_SHORT).show()
                }
            }
           ApiCodes.EXTRACT_QID -> {
               if(registerOneViewModel.msg.isNotEmpty()) {
                   Toast.makeText(this, registerOneViewModel.msg, Toast.LENGTH_SHORT).show()
               }
               else
               {
                   val intent = Intent(this, RegistrationTwoActivity::class.java)
                   intent.putExtra("data", registerOneViewModel.dataMLD.value?.qidDetails?.data)
                   intent.putExtra("moiError", registerOneViewModel.dataMLD.value?.moiError)
                   if(registerOneViewModel.dataMLD.value?.moiError.equals("true")) {
                       intent.putExtra("qid", registerOneBinding.numberEt.text.toString())
                   }
                   intent.putExtra("isPassport", isPassport)
                   intent.putExtra("isAuction",isAuction)
                   setLocaleTemp(SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE))
                   startActivity(intent)
               }
            }
        }
    }

    private fun getDatePicker(): DatePickerDialog {
        setLocaleTemp("EN")
        return DatePickerDialog(this, R.style.DatePickerStyle,
            { _, year, month, dayOfMonth ->
                dob =  year.toString() + "-" + (month+1).toString() + "-" + dayOfMonth.toString()
                registerOneBinding.dateEt.text = dob
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun onBackPressed() {
        setLocaleTemp(SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE))
        super.onBackPressed()
    }

    private fun setLocaleTemp(localeValue : String){
        val locale = Locale(localeValue)
        val resources: Resources = getResources()
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }



}