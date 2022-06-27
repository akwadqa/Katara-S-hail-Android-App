package com.app.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.app.R
import com.app.databinding.ActivityNewBidderOptionsBinding

class NewBidderOptionsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var activityNewBidderOptionsBinding: ActivityNewBidderOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNewBidderOptionsBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_bidder_options)
        setListeners()
    }

    private fun setListeners() {
        activityNewBidderOptionsBinding.tvQatari.setOnClickListener(this)
        activityNewBidderOptionsBinding.tvNoneQatari.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            activityNewBidderOptionsBinding.tvQatari.id -> {
                startActivity(Intent(this, RegistrationOneActivity::class.java).putExtra("isAuction",true))
            }
            activityNewBidderOptionsBinding.tvNoneQatari.id -> {
                startActivity(Intent(this, RegistrationTwoActivity::class.java).putExtra("isAuction",true)
                    .putExtra("registrationOptions", RegistrationOptions.NONE_QATARI))
            }
        }
    }

}