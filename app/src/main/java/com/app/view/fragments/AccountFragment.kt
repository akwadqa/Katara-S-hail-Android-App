package com.app.view.fragments

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.ActivityStatusBinding
import com.app.databinding.FragmentStatusBinding
import com.app.databinding.FragmentTicketBinding
import com.app.model.dataclasses.TicketListResponse
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.view.activities.*
import com.app.view.adaptors.TicketsAdapter
import com.app.viewmodel.AccountStatusViewModel
import com.app.viewmodel.OtherViewModel
import com.app.viewmodel.TicketViewModel
import java.util.*


class AccountFragment : BaseFragment() , View.OnClickListener  {
    private val viewModel by lazy { ViewModelProvider(this).get(AccountStatusViewModel::class.java) }

    private lateinit var statusBinding: FragmentStatusBinding
    private var isHold: Boolean? = true
    private var isPayment: Boolean? = false
    private var isForAction: Boolean? = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        statusBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_status, container, false)
        return statusBinding.root
    }


    override fun initialiseFragmentBaseViewModel() {
        statusBinding.lifecycleOwner = this
        setFragmentBaseViewModel(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        getData()
        updateHoldUI()
        statusBinding.ivLogout.setOnClickListener(this)
        statusBinding.tvPay.setOnClickListener(this)
    }

    private fun setObservers() {
        viewModel.getResponseObserver().observe(this@AccountFragment.requireActivity(), this)
    }

    private fun getData() {
        isHold = SharedPreferencesManager.getBoolean("isInHold")
        isPayment = SharedPreferencesManager.getBoolean("isPayed")
        isForAction = SharedPreferencesManager.getBoolean("isForAction")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            statusBinding.ivLogout.id -> {
                (activity as MainActivity).openLogoutDialog()
            }
            statusBinding.tvPay.id -> {
                val intent = Intent(this@AccountFragment.activity, PaymentActivity::class.java)
                resultLauncher.launch(intent)
            }
        }
    }

    private fun updateHoldUI()
    {
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) {
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

        if(isForAction == false){
            statusBinding.tvPay.visibility = View.GONE
            statusBinding.ivSend.setBackgroundResource(R.drawable.done)
            statusBinding.tvStatus.text = resources.getString(R.string.is_for_account)
        } else {
            when {
                isHold == true -> {
                    statusBinding.tvPay.visibility = View.GONE
                    statusBinding.ivSend.setBackgroundResource(R.drawable.pending)
                    statusBinding.tvStatus.text = resources.getString(R.string.account_in_progress)
                }
                isPayment == false -> {
                    statusBinding.ivSend.setBackgroundResource(R.drawable.pending)
                    statusBinding.tvStatus.text = resources.getString(R.string.pending_payment)
                    statusBinding.tvPay.visibility = View.VISIBLE
                }
                else -> {
                    statusBinding.tvPay.visibility = View.GONE
                    statusBinding.ivSend.setBackgroundResource(R.drawable.done)
                    statusBinding.tvStatus.text = resources.getString(R.string.account_done)
                }
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