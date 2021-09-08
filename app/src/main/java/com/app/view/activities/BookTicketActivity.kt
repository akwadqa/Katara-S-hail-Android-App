package com.app.view.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.R
import com.app.databinding.ActivityBookTicketBinding
import com.app.model.dataclasses.TicketDateTimeResponse
import com.app.model.dataclasses.TicketSlotsResponse
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.AppConstants.USER_FIRST_NAME
import com.app.utils.AppConstants.USER_FIRST_NAME_AR
import com.app.utils.AppUtils
import com.app.utils.SharedPreferencesManager
import com.app.utils.SharedPreferencesManager.getString
import com.app.view.adaptors.DateSlotAdapter
import com.app.view.adaptors.TimeSlotAdapter
import com.app.viewmodel.BookTicketViewModel
import kotlin.collections.ArrayList

class BookTicketActivity : BaseActivity(), View.OnClickListener,
    DateSlotAdapter.OnItemClickListener, TimeSlotAdapter.OnItemClickListener {
    private lateinit var binding: ActivityBookTicketBinding
    private val bookTicketViewModel by lazy { ViewModelProvider(this).get(BookTicketViewModel::class.java) }
    private var dateSlotPos : Int = 0
    private var timeSlotPos : Int = 0
    private var maxTicket : Int = 1
    private var isSlotAvailable : Boolean = false
    private lateinit var dateAdapter: DateSlotAdapter
    private lateinit var timeAdapter: TimeSlotAdapter
    private var cList : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_ticket)
        setBaseViewModel(bookTicketViewModel)
        setObservers()
        setListeners()
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
            binding.nameEt.setText(getString(USER_FIRST_NAME_AR))
        else
            binding.nameEt.setText(getString(USER_FIRST_NAME))
        bookTicketViewModel.getTicketConfigApi()
        bookTicketViewModel.getTicketDataApi(this)
    }

    private fun setObservers() {
        bookTicketViewModel.getResponseObserver().observe(this, this)
        bookTicketViewModel.getValidationLiveData().observe(this,
            {
                when (it.value) {
                    false -> {
                        showToastLong(it.message)
                    }
                }
            })
    }

    private fun setListeners() {
        binding.ivBack.setOnClickListener(this)
        binding.tvContinue.setOnClickListener(this)
        binding.tvCompanion.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.ivBack.id -> {
                finish()
            }
            binding.tvCompanion.id -> {
                val intent = Intent(this, CompanionTicketActivity::class.java)
                intent.putExtra("max_ticket",maxTicket)
                resultLauncher.launch(intent)
            }
            binding.tvContinue.id -> {
                if(isSlotAvailable) {
                    bookTicketViewModel.name = binding.nameEt.text.toString()
                    bookTicketViewModel.dateSlot =
                        bookTicketViewModel.dataSlotMLD.value!!.responce!![dateSlotPos].displayNameEn.toString()
                    bookTicketViewModel.timeSlot =
                        bookTicketViewModel.dataSlotMLD.value!!.responce!![dateSlotPos].timeSlots!![timeSlotPos].displayNameEn.toString()
                    bookTicketViewModel.timeSlotId =
                        bookTicketViewModel.dataSlotMLD.value!!.responce!![dateSlotPos].timeSlots!![timeSlotPos].timeSlotId.toString()
                    if (cList.size > 0)
                        bookTicketViewModel.companion = cList
                    bookTicketViewModel.validateCredentials(this)
                }
                else
                    showToastShort(resources.getString(R.string.slots))
            }
        }
    }

    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.ADD_TICKET -> {
                if(bookTicketViewModel.msg.isNotEmpty())
                    Toast.makeText(this, bookTicketViewModel.msg, Toast.LENGTH_SHORT).show()
                else
                   showTicketDialog()
            }
            ApiCodes.GET_CONFIG -> {
                if(bookTicketViewModel.configMsg.isEmpty())
                    maxTicket = bookTicketViewModel.dataConfigMLD.value!!.responce!!.maxTicketNumberForReservation!!
            }
            ApiCodes.GET_TICKET -> {
                if(bookTicketViewModel.msg.isNotEmpty()) {
                    isSlotAvailable = false
                    Toast.makeText(this, bookTicketViewModel.msg, Toast.LENGTH_SHORT).show()
                }
                else {
                    if(bookTicketViewModel.dataSlotMLD.value!!.responce!!.isNotEmpty()) {
                        isSlotAvailable = true
                        bookTicketViewModel.dataSlotMLD.value!!.responce!![0].order = -1
                        setDateAdaptor(bookTicketViewModel.dataSlotMLD.value!!.responce)
                        bookTicketViewModel.dataSlotMLD.value!!.responce!![0].timeSlots!![0].slotOrder = -1
                        setTimeSlotAdaptor(bookTicketViewModel.dataSlotMLD.value!!.responce!![0].timeSlots)
                    }
                    else
                        showToastShort(resources.getString(R.string.slots))
                }
            }
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            cList = data!!.getStringArrayListExtra("names") as ArrayList<String>
        }
    }

    private fun setDateAdaptor(value: List<TicketDateTimeResponse>?)
    {
        dateAdapter = DateSlotAdapter(this, value, this)
        binding.rvDate.layoutManager =
            GridLayoutManager(this, 3)
        binding.rvDate.adapter = dateAdapter
    }

    private fun setTimeSlotAdaptor(timeSlots: List<TicketSlotsResponse>?)
    {
        timeAdapter = TimeSlotAdapter(this, timeSlots, this)
        binding.rvTime.layoutManager =
            GridLayoutManager(this, 2)
        binding.rvTime.adapter = timeAdapter
    }

    override fun onDateItemClick(position: Int) {
        dateSlotPos = position
        for (item: TicketDateTimeResponse in bookTicketViewModel.dataSlotMLD.value!!.responce!!) {
            item.order = 0
        }
        bookTicketViewModel.dataSlotMLD.value!!.responce!![position].order = -1
        dateAdapter.notifyDataSetChanged()
        setTimeSlotAdaptor(bookTicketViewModel.dataSlotMLD.value!!.responce!![position].timeSlots)
    }

    override fun onTimeItemClick(position: Int) {
        timeSlotPos = position
        for (item: TicketSlotsResponse in bookTicketViewModel.dataSlotMLD.value!!.responce!![dateSlotPos].timeSlots!!) {
            item.slotOrder = 0
        }
        bookTicketViewModel.dataSlotMLD.value!!.responce!![dateSlotPos].timeSlots!![position].slotOrder = -1
        timeAdapter.notifyDataSetChanged()
    }

    private fun showTicketDialog() {
        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle(resources.getString(R.string.app_name))
        builder1.setMessage(bookTicketViewModel.msgAddTicket)
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            resources.getString(R.string.ok)
        ) { dialog , which ->
            dialog.dismiss()
            finish()
        }
        val alert11 = builder1.create()
        alert11.show()
    }
}