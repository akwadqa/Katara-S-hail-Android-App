package com.app.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.ActivityCompanionTicketBinding
import com.app.utils.ApiCodes
import com.app.utils.AppUtils
import com.app.view.adaptors.CompanionAdapter
import com.app.viewmodel.BookTicketViewModel

class CompanionTicketActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCompanionTicketBinding
    private var maxTicket : Int = 1
    private val myList : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_companion_ticket)
        setListeners()
        getIntentData()
    }

    private fun getIntentData() {
        maxTicket = intent.getIntExtra("max_ticket",1)
        binding.tvMax.text = resources.getString(R.string.maximum_of_companion_is_3_companion)+ " " + maxTicket
    }

    private fun setListeners() {
        binding.ivBack.setOnClickListener(this)
        binding.tvContinue.setOnClickListener(this)
        binding.rlSlot.setOnClickListener(this)
        binding.rlSlot2.setOnClickListener(this)
        binding.rlSlot3.setOnClickListener(this)
        binding.rlSlot4.setOnClickListener(this)
        binding.rlSlot5.setOnClickListener(this)
        binding.rlSlot6.setOnClickListener(this)
        binding.rlSlot7.setOnClickListener(this)
        binding.rlSlot8.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.ivBack.id -> {
                finish()
            }
            binding.rlSlot.id -> {
                if(maxTicket>1)
                binding.rlSlot2.visibility = View.VISIBLE
            }
            binding.rlSlot2.id -> {
                if(maxTicket>2)
                binding.rlSlot3.visibility = View.VISIBLE
            }
            binding.rlSlot3.id -> {
                if(maxTicket>3)
                binding.rlSlot4.visibility = View.VISIBLE
            }
            binding.rlSlot4.id -> {
                if(maxTicket>4)
                binding.rlSlot5.visibility = View.VISIBLE
            }
            binding.rlSlot5.id -> {
                if(maxTicket>5)
                binding.rlSlot6.visibility = View.VISIBLE
            }
            binding.rlSlot6.id -> {
                if(maxTicket>6)
                binding.rlSlot7.visibility = View.VISIBLE
            }
            binding.rlSlot7.id -> {
                if(maxTicket>7)
                binding.rlSlot8.visibility = View.VISIBLE
            }
            binding.tvContinue.id -> {
                if(binding.etSlot.text!!.isBlank())
                    showMessage()
                else if(binding.rlSlot2.visibility==View.VISIBLE && binding.etSlot2.text!!.trim()!!.isEmpty())
                    showMessage()
                else if(binding.rlSlot3.visibility==View.VISIBLE && binding.etSlot3.text!!.trim()!!.isEmpty())
                    showMessage()
                else if(binding.rlSlot4.visibility==View.VISIBLE && binding.etSlot4.text!!.trim()!!.isEmpty())
                    showMessage()
                else if(binding.rlSlot5.visibility==View.VISIBLE && binding.etSlot5.text!!.trim()!!.isEmpty())
                    showMessage()
                else if(binding.rlSlot6.visibility==View.VISIBLE && binding.etSlot6.text!!.trim()!!.isEmpty())
                    showMessage()
                else if(binding.rlSlot7.visibility==View.VISIBLE && binding.etSlot7.text!!.trim()!!.isEmpty())
                    showMessage()
                else if(binding.rlSlot8.visibility==View.VISIBLE && binding.etSlot8.text!!.trim()!!.isEmpty())
                    showMessage()
                else
                {
                    val intent = Intent()
                    setResult(RESULT_OK,intent)
                    myList.add(binding.etSlot.text.toString())
                    if(binding.rlSlot2.visibility==View.VISIBLE && binding.etSlot2.text!!.trim().isNotEmpty())
                        myList.add(binding.etSlot2.text.toString())
                    if(binding.rlSlot3.visibility==View.VISIBLE && binding.etSlot3.text!!.trim().isNotEmpty())
                        myList.add(binding.etSlot3.text.toString())
                    if(binding.rlSlot4.visibility==View.VISIBLE && binding.etSlot4.text!!.trim().isNotEmpty())
                        myList.add(binding.etSlot4.text.toString())
                    if(binding.rlSlot5.visibility==View.VISIBLE && binding.etSlot5.text!!.trim().isNotEmpty())
                        myList.add(binding.etSlot5.text.toString())
                    if(binding.rlSlot6.visibility==View.VISIBLE && binding.etSlot6.text!!.trim().isNotEmpty())
                        myList.add(binding.etSlot6.text.toString())
                    if(binding.rlSlot7.visibility==View.VISIBLE && binding.etSlot7.text!!.trim().isNotEmpty())
                        myList.add(binding.etSlot7.text.toString())
                    if(binding.rlSlot8.visibility==View.VISIBLE && binding.etSlot8.text!!.trim().isNotEmpty())
                        myList.add(binding.etSlot8.text.toString())

                    intent.putStringArrayListExtra("names",myList)
                    super.onBackPressed()
                }
            }
        }
    }

    private fun showMessage() {
        showToastShort(resources.getString(R.string.pecn))

    }



}