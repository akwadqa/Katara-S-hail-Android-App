package com.app.view.activities

import `in`.aabhasjindal.otptextview.OTPListener
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.app.R
import com.app.databinding.ActivityOnboardingOptionBinding
import com.app.utils.SharedPreferencesManager
import com.app.viewmodel.OtherViewModel

class OnboardingOptionActivity : BaseActivity(), View.OnClickListener {
    private lateinit var onboardingOptionBinding: ActivityOnboardingOptionBinding
    private lateinit var otherViewModel: OtherViewModel
    private var isAuction: Boolean ? = false
    private lateinit var dialog : Dialog
    private lateinit var tvCancel:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onboardingOptionBinding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding_option)
        setListeners()
        isAuction = intent.getBooleanExtra("isAuction",false)
        if(!isAuction!!)
        {
            onboardingOptionBinding.tvRegister.text = resources.getString(R.string.registration)
        }else{
            otherViewModel = OtherViewModel()
            otherViewModel.getAuctionIsForQatari()
        }
    }

    private fun setListeners() {
        onboardingOptionBinding.tvSignin.setOnClickListener(this)
        onboardingOptionBinding.tvRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            onboardingOptionBinding.tvSignin.id -> {
                startActivity(Intent(this, SignInActivity::class.java).putExtra("isAuction",isAuction))
            }
            onboardingOptionBinding.tvRegister.id -> {
                if(isAuction == true) {
                    if(otherViewModel.auctionIsForQatariData.value?.response == true){
                        startActivity(Intent(this, RegistrationOneActivity::class.java).putExtra("isAuction",isAuction))
                    }
                    else{
                        startActivity(Intent(this, NewBidderOptionsActivity::class.java))
                    }
                } else {
                    showRegistrationPicker()
                }
            }
        }
    }

    override fun onBackPressed() {
        if(this::dialog.isInitialized && dialog!=null && dialog.isShowing)
            dialog.dismiss()
        else
            super.onBackPressed()
    }

    private fun getDialog() : Dialog {
        dialog = Dialog(this,android.R.style.Theme_Light)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setDimAmount(0.5f)
        dialog.window!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.setContentView(R.layout.ticket_registration_popup)
        return dialog
    }

    private fun showRegistrationPicker(){
        dialog = getDialog()
        val tvPassport:TextView = dialog.findViewById(R.id.btn_passport) as TextView
        val tvQid:TextView = dialog.findViewById(R.id.btn_qid) as TextView
        tvCancel = dialog.findViewById(R.id.btnCancelDialog) as TextView
        tvPassport.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, RegistrationTwoActivity::class.java).putExtra("isAuction",isAuction)
                .putExtra("registrationOptions", RegistrationOptions.PASSPORT))
        }
        tvQid.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, RegistrationOneActivity::class.java).putExtra("isAuction",isAuction)
                .putExtra("registrationOptions", RegistrationOptions.QID))
        }
        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}