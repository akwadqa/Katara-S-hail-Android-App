package com.app.view.activities

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.app.R
import com.app.databinding.ActivityTicketDetailBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class TicketDetailActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityTicketDetailBinding
    private var name: String= ""
    private var date: String= ""
    private var time: String= ""
    private var link: String= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ticket_detail)
        setListeners()
        getIntentData()
        setData()
    }

    private fun getIntentData() {
        name = intent.getStringExtra("name").toString()
        date = intent.getStringExtra("date").toString()
        time = intent.getStringExtra("time").toString()
        link = intent.getStringExtra("link").toString()
    }

    private fun setData() {
        binding.tvName.text = name
        binding.tvDate.text = date
        binding.tvTime.text = time
        Glide.with(this).load(link).into(binding.ivScan)
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