package com.app.view.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.app.R
import com.app.databinding.ActivityAuctionViewBinding
import com.app.model.dataclasses.*
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.view.adaptors.AuctionImageAdapter
import com.app.view.adaptors.AuctionPreviousAdapter
import com.app.viewmodel.AuctionViewModel
import com.zhpan.indicator.enums.IndicatorOrientation

class AuctionPreviousDetailViewActivity : BaseActivity(), View.OnClickListener,
    AuctionPreviousAdapter.OnItemClickListener {
    private val auctionViewModel by lazy { ViewModelProvider(this).get(AuctionViewModel::class.java) }
    private lateinit var auctionBinding: ActivityAuctionViewBinding
    private var falconId : String= ""
    private lateinit var auctionAdapter: AuctionPreviousAdapter
    private lateinit var imageAdapter: AuctionImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auctionBinding = DataBindingUtil.setContentView(this, R.layout.activity_auction_view)
        setBaseViewModel(auctionViewModel)
        setObservers()
        setListeners()
        getIntentData()
        hitAllApis()
    }

    private fun hitAllApis() {
        auctionViewModel.getAuctionPreviousDetailsList(falconId)
        auctionViewModel.getAuction10List(falconId)
        auctionViewModel.getAuctionMaxPrice(falconId)
    }

    private fun getIntentData() {
        falconId = intent.getStringExtra("falconId").toString()
    }

    private fun setListeners() {
        auctionBinding.ivBack.setOnClickListener(this)
        auctionBinding.ivNext.setOnClickListener(this)
        auctionBinding.ivPrevious.setOnClickListener(this)
    }

    private fun setObservers() {
        auctionViewModel.getResponseObserver().observe(this, this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            auctionBinding.ivBack.id -> {
                finish()
            }
            auctionBinding.ivNext.id -> {
                val pos = auctionBinding.viewPager.currentItem
                if(pos < (auctionViewModel.dataAuctionMLD.value!!.falconImageMobiles!!.size - 1) )
                    auctionBinding.viewPager.currentItem = pos + 1
            }
            auctionBinding.ivPrevious.id -> {
                val pos = auctionBinding.viewPager.currentItem
                if(pos > 0 )
                    auctionBinding.viewPager.currentItem = pos - 1
            }
        }
    }

    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.GET_AUCTION -> {
                setAuctionDetails(auctionViewModel.dataAuctionMLD)
            }
            ApiCodes.GET_MAX_PRICE -> {
                if(auctionViewModel.msg.isNotEmpty()) {
                    Toast.makeText(this, auctionViewModel.msg, Toast.LENGTH_SHORT).show()
                }
                else {
                    setMaxPriceData(auctionViewModel.dataMaxPriceMLD.value)
                }
            }
            ApiCodes.GET_LAST_10 -> {
                if(auctionViewModel.msg.isNotEmpty()) {
                    Toast.makeText(this, auctionViewModel.msg, Toast.LENGTH_SHORT).show()
                }
                else {
                    setFalconAdaptor(auctionViewModel.dataAuction10MLD.value)
                }
            }
        }
    }

    private fun setAuctionDetails(dataAuctionMLD: MutableLiveData<FalconListResponse?>) {
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
            auctionBinding.tvName.text = dataAuctionMLD.value!!.falconName
        else
            auctionBinding.tvName.text = dataAuctionMLD.value!!.falconNameEn
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
            auctionBinding.tvDescription.text = dataAuctionMLD.value!!.falconDescription
        else
            auctionBinding.tvDescription.text = dataAuctionMLD.value!!.falconDescriptionEn
        auctionBinding.tvDate.text = dataAuctionMLD.value!!.falconBirth!!.split("T")[0]
        setBannerAdaptor(dataAuctionMLD.value!!.falconImageMobiles)
    }

    private fun setBannerAdaptor(value: List<FalconImageResponse>?)
    {
        var sortedList = value!!.sortedWith(compareBy { it.docOrder })
        auctionBinding.viewPager.apply {
            imageAdapter = AuctionImageAdapter(this@AuctionPreviousDetailViewActivity, sortedList)
            adapter = imageAdapter
            offscreenPageLimit = 1
        }
        var myPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (imageAdapter != null) {
                    imageAdapter.pauseVideo()
                }
            }
        }
        auctionBinding.viewPager.registerOnPageChangeCallback(myPageChangeCallback)
        auctionBinding.indicatorView.apply {
            setSliderHeight(10F)
            setupWithViewPager(auctionBinding.viewPager)
            if(SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
                setOrientation(IndicatorOrientation.INDICATOR_RTL)
        }
        if(value!!.size>1)
        {
            auctionBinding.ivNext.visibility = View.VISIBLE
            auctionBinding.ivPrevious.visibility = View.VISIBLE
        }
        else
        {
            auctionBinding.ivNext.visibility = View.GONE
            auctionBinding.ivPrevious.visibility = View.GONE
        }
    }

    private fun setMaxPriceData(value: AuctionMaxPriceMainResponse?) {
        auctionBinding.tvPrice.text = value!!.responce!!.maxPrice.toString()
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
            auctionBinding.tvUserName.text = value.responce!!.memberNameAr
        else
            auctionBinding.tvUserName.text = value.responce!!.memberNameEn
    }

    private fun setFalconAdaptor(value: FalconPreviousResponse?)
    {
        auctionBinding.tvNoData.visibility = View.VISIBLE
        if(value!!.responce!!.isNotEmpty()) {
            auctionBinding.tvNoData.visibility = View.GONE
            auctionAdapter = AuctionPreviousAdapter(this, value!!.responce, this)
            auctionBinding.rvAuction.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            auctionBinding.rvAuction.adapter = auctionAdapter
            auctionBinding.rvAuction.isNestedScrollingEnabled = false
        }

    }

    override fun onItemClick(position: Int) {

    }


    override fun onPause() {
        if (imageAdapter != null) {
            imageAdapter.pauseVideo()
        }
        super.onPause()
    }


}

