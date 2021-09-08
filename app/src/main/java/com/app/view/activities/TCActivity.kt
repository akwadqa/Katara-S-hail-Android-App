package com.app.view.activities

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.app.R
import com.app.databinding.ActivityTcBinding

class TCActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityTcBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tc)
        setListeners()
    }

    private fun setListeners() {
        binding.ivBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.ivBack.id -> {
                finish()
            }
        }
    }

}