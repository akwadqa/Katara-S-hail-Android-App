package com.app.view.activities

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.R.attr.maxLength
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.app.R
import com.app.databinding.ActivitySigninBinding
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.viewmodel.SignInViewModel


class SignInActivity : BaseActivity(),  View.OnClickListener {
    private lateinit var signInBinding: ActivitySigninBinding
    private val signInViewModel by lazy { ViewModelProvider(this).get(SignInViewModel::class.java) }
    private lateinit var dialog : Dialog
    private lateinit var  tvNumber : TextView
    private lateinit var  tvSubmit : TextView
    private lateinit var otpview : OtpTextView
    private var otp: String ? = ""
    private var isAuction: Boolean ? = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInBinding = DataBindingUtil.setContentView(this, R.layout.activity_signin)
        setBaseViewModel(signInViewModel)
        isAuction = intent.getBooleanExtra("isAuction",false)
        setListeners()
        setObservers()
        if(isAuction == true){
            setViews()
        }

    }

    private fun setViews() {
       signInBinding.qidEt.inputType = InputType.TYPE_CLASS_NUMBER
        signInBinding.qidEt.keyListener = DigitsKeyListener.getInstance(false, true)
        signInBinding.tvHeading.text = getString(R.string.qatar_id_number)
        val fArray = arrayOfNulls<InputFilter>(1)
        fArray[0] = LengthFilter(11)
        signInBinding.qidEt.setFilters(fArray)
    }

    private fun setListeners() {
        signInBinding.tvContinue.setOnClickListener(this)
        signInBinding.ivBack.setOnClickListener(this)
    }

    private fun setObservers() {
        signInViewModel.getResponseObserver().observe(this, this)
        signInViewModel.getValidationLiveData().observe(this,
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
            signInBinding.tvContinue.id -> {
                signInViewModel.login = signInBinding.qidEt.text.toString()
                signInViewModel.isAuction = isAuction!!
                signInViewModel.validateCredentials(this)
            }
            signInBinding.ivBack.id -> {
               finish()
            }
        }
    }

    override fun onBackPressed() {
        if(this::dialog.isInitialized  && dialog!=null && dialog.isShowing) {
            dialog.dismiss()
        } else
            super.onBackPressed()
    }



    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.SEND_OTP -> {
                if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) {
                    Toast.makeText(this, signInViewModel.dataMLD.value?.msgAr,Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, signInViewModel.dataMLD.value?.msgEn,Toast.LENGTH_SHORT).show()
                }
                if (signInViewModel.dataMLD.value?.error == false) {
                    showOTPDialog(signInViewModel.dataMLD.value!!.number)
//                    registerTwoViewModel.phoneNumber = registerTwoViewModel.dataMLD.value!!.number.toString()
                }
            }
            ApiCodes.VERIFY_OTP -> {
                if(signInViewModel.msg.isNotEmpty()) {
                    Toast.makeText(this, signInViewModel.msg, Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, resources.getString(R.string.verify_success),Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun showOTPDialog(number: String?) {
        dialog = Dialog(this,android.R.style.Theme_Light)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setDimAmount(0.5f)
        dialog.window!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.setContentView(R.layout.dialog_otp)
        otpview = dialog.findViewById(R.id.otp_view) as OtpTextView
        tvNumber = dialog.findViewById(R.id.tv_number) as TextView
        tvSubmit = dialog.findViewById(R.id.tv_submit) as TextView

        tvNumber.text = number

        tvSubmit.setOnClickListener(this)

        otpview?.requestFocusOTP()
        otpview?.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otpValue: String) {
                otp = otpValue
            }
        }

        tvSubmit.setOnClickListener {
            signInViewModel.otp = otp.toString()
            signInViewModel.validateOTP(this@SignInActivity)
        }
        tvNumber.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialogInterface, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action === KeyEvent.ACTION_UP) {
                onBackPressed()
                return@OnKeyListener true
            }
            false
        })
    }

}