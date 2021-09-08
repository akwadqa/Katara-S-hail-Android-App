package com.app.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.FragmentTicketBinding
import com.app.model.dataclasses.TicketListResponse
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.view.activities.BookTicketActivity
import com.app.view.activities.TicketDetailActivity
import com.app.view.adaptors.TicketsAdapter
import com.app.viewmodel.TicketViewModel


class TicketFragment : BaseFragment(), TicketsAdapter.OnItemClickListener, View.OnClickListener {
    private lateinit var binding: FragmentTicketBinding
    private val ticketViewModel by lazy { ViewModelProvider(requireActivity()).get(TicketViewModel::class.java) }
    private lateinit var ticketAdapter: TicketsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ticket, container, false)
        return binding.root
    }


    override fun initialiseFragmentBaseViewModel() {
        binding.lifecycleOwner = this
        setFragmentBaseViewModel(ticketViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        ticketViewModel.getTicketList()
    }

    private fun setObservers() {
        ticketViewModel.getResponseObserver().observe(this@TicketFragment.requireActivity(), this)
    }

    private fun setListeners() {
        binding.ivAdd.setOnClickListener(this)
        binding.tvAddTicket.setOnClickListener(this)
    }

    private fun setAdaptor(value: List<TicketListResponse?>?) {
        if(activity!=null && isAdded) {
            binding.linNoData.visibility = View.VISIBLE
            if(value!!.isNotEmpty()) {
                binding.linNoData.visibility = View.GONE
                ticketAdapter = TicketsAdapter(value, this@TicketFragment)
                binding.rvTicket.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.rvTicket.adapter = ticketAdapter
            }
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this@TicketFragment.requireActivity(), TicketDetailActivity::class.java)
        intent.putExtra("date", ticketViewModel.dataMLD.value!!.responce!![position]!!.ticketDate!!.split("T")[0])
        intent.putExtra("time", ticketViewModel.dataMLD.value!!.responce!![position]!!.timeSlot!!.displayNameEn)
        intent.putExtra("name", ticketViewModel.dataMLD.value!!.responce!![position]!!.holderName)
        intent.putExtra("link", ticketViewModel.dataMLD.value!!.responce!![position]!!.qrCodeLink)
        startActivity(intent)
    }

    override fun onShareClick(position: Int) {
        shareViaApp(ticketViewModel.dataMLD.value!!.responce!![position].downloadLink)
    }

    private fun shareViaApp(qrCodeLink: String?) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_SUBJECT, "Ticket QR Code")
        share.putExtra(Intent.EXTRA_TEXT, qrCodeLink)
        startActivity(Intent.createChooser(share, "Share QR Code!"))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.ivAdd.id -> {
                startActivityForResult(Intent(this@TicketFragment.requireActivity(), BookTicketActivity::class.java),1)
            }
            binding.tvAddTicket.id -> {
                startActivityForResult(Intent(this@TicketFragment.requireActivity(), BookTicketActivity::class.java),1)
            }
        }
    }

    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.GET_TICKET -> {
                setAdaptor(ticketViewModel.dataMLD.value!!.responce)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ticketViewModel.getTicketList()
    }


}