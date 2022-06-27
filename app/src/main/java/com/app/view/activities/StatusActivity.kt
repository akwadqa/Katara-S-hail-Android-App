package com.app.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.app.R
import com.app.databinding.ActivityStatusBinding
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.AppUtils
import com.app.utils.SharedPreferencesManager
import com.app.viewmodel.AccountStatusViewModel

class StatusActivity : BaseActivity(), View.OnClickListener {
    private val viewModel by lazy { ViewModelProvider(this).get(AccountStatusViewModel::class.java) }
    private lateinit var statusBinding : ActivityStatusBinding
    private var isHold: Boolean? = true
    private var isPayment: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBinding = DataBindingUtil.setContentView(this, R.layout.activity_status)
        setBaseViewModel(viewModel)
        setObservers()
        setListeners()
        getData()
        updateHoldUI()
    }

    private fun setObservers() {
        viewModel.getResponseObserver().observe(this, this)
    }

    private fun getData() {
        isHold = SharedPreferencesManager.getBoolean(AppConstants.isInHold)
        isPayment = SharedPreferencesManager.getBoolean(AppConstants.isPayed)
    }

    private fun setListeners() {
        statusBinding.tvContinue.setOnClickListener(this)
        statusBinding.tvPay.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            statusBinding.tvContinue.id -> {
                startActivity(Intent(this, MainActivity::class.java).putExtra("fromAccount",true))
                finish()
            }
            statusBinding.tvPay.id -> {
                val link = SharedPreferencesManager.getString(AppConstants.PAY_LINK);
                AppUtils.logout(this)
                SharedPreferencesManager.put(AppConstants.PAY_LINK, link)
                val intent = Intent(this, PaymentActivity::class.java)
                resultLauncher.launch(intent)
            }
        }
    }

    private fun updateHoldUI()
    {
      if (SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE)=="ar")
        {
            if(SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME_AR).isEmpty())
                statusBinding.tvName.text =
                    SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME)
            else
                statusBinding.tvName.text =
                    SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME_AR)
        }
        else
            statusBinding.tvName.text = SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME)
        statusBinding.tvNumber.text = SharedPreferencesManager.getString(AppConstants.PHONECODE)+ " - " + SharedPreferencesManager.getString(AppConstants.USER_PHONE)

        when {
            isHold==true -> {
                statusBinding.tvPay.visibility = View.GONE
                statusBinding.tvContinue.setTextColor(resources.getColor(R.color.primary))
                statusBinding.tvContinue.background = resources.getDrawable(R.drawable.primary_stroke)
                statusBinding.ivSend.setBackgroundResource(R.drawable.pending)
                statusBinding.tvStatus.text = resources.getString(R.string.account_in_progress)
            }
            isPayment == false -> {
                statusBinding.tvContinue.setTextColor(resources.getColor(R.color.primary))
                statusBinding.tvContinue.background = resources.getDrawable(R.drawable.primary_stroke)
                statusBinding.ivSend.setBackgroundResource(R.drawable.pending)
                statusBinding.tvStatus.text = resources.getString(R.string.pending_payment)
                statusBinding.tvPay.visibility = View.VISIBLE
            }
            else -> {
                statusBinding.tvPay.visibility = View.GONE
                statusBinding.tvContinue.setTextColor(resources.getColor(R.color.white))
                statusBinding.tvContinue.background = resources.getDrawable(R.drawable.button_rounded)
                statusBinding.ivSend.setBackgroundResource(R.drawable.done)
                statusBinding.tvStatus.text = resources.getString(R.string.account_done)
            }
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.hitRefresh()
        }
    }

    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.LOGIN -> {
                getData()
                updateHoldUI()
            }
        }
    }

}