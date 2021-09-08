package com.app.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.ActivityAuctionListBinding
import com.app.model.dataclasses.FalconMainResponse
import com.app.utils.ApiCodes
import com.app.view.adaptors.AuctionAdapter
import com.app.view.adaptors.AuctionPreviousAdapter
import com.app.viewmodel.AuctionPreviousViewModel

class AuctionListActivity : BaseActivity(), View.OnClickListener,
    AuctionAdapter.OnItemClickListener {
    private val auctionViewModel by lazy { ViewModelProvider(this).get(AuctionPreviousViewModel::class.java) }
    private lateinit var auctionBinding: ActivityAuctionListBinding
    private lateinit var auctionAdapter: AuctionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auctionBinding = DataBindingUtil.setContentView(this, R.layout.activity_auction_list)
        setBaseViewModel(auctionViewModel)
        setObservers()
        setListeners()
        auctionViewModel.getPreviousAuctionList()
    }

    private fun setListeners() {
        auctionBinding.ivBack.setOnClickListener(this)
    }

    private fun setObservers() {
        auctionViewModel.getResponseObserver().observe(this, this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            auctionBinding.ivBack.id -> {
                finish()
            }
        }
    }

    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.GET_FALCON -> {
                if(auctionViewModel.msg.isNotEmpty()) {
                    Toast.makeText(this, auctionViewModel.msg, Toast.LENGTH_SHORT).show()
                }
                else {
                    setFalconAdaptor(auctionViewModel.dataFalconMLD.value)
                }
            }
        }
    }

    private fun setFalconAdaptor(value: FalconMainResponse?)
    {
        auctionBinding.tvNoData.visibility = View.VISIBLE
        if(value!!.responce!!.isNotEmpty()) {
            auctionBinding.tvNoData.visibility = View.GONE
            auctionAdapter = AuctionAdapter(this, value!!.responce, this)
            auctionBinding.rvAuction.layoutManager =
                GridLayoutManager(this, 2)
            auctionBinding.rvAuction.adapter = auctionAdapter
        }
    }

    override fun onItemClick(position: Int) {
        startActivity(Intent(this, AuctionPreviousDetailViewActivity::class.java)
            .putExtra("falconId",auctionViewModel.dataFalconMLD.value!!.responce!![position].falconId))
    }


}