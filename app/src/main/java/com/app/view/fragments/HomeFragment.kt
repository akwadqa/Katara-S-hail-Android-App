
package com.app.view.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.FragmentHomeBinding
import com.app.model.dataclasses.*
import com.app.utils.*
import com.app.view.activities.*
import com.app.view.adaptors.AuctionAdapter
import com.app.view.adaptors.ImageAdapter
import com.app.view.adaptors.NewsAdapter
import com.app.view.dialogs.ProgressDialog
import com.app.viewmodel.HomeViewModel
import com.zhpan.indicator.enums.IndicatorOrientation


class HomeFragment : BaseFragment(), View.OnClickListener,
    NewsAdapter.OnNewsItemClickListener, AuctionAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by lazy { ViewModelProvider(requireActivity()).get(HomeViewModel::class.java) }
    private lateinit var newsAdapter:  NewsAdapter
    private lateinit var auctionAdapter: AuctionAdapter
    private var FALCONS: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun initialiseFragmentBaseViewModel() {
        binding.lifecycleOwner = this
        setFragmentBaseViewModel(homeViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        homeViewModel.getNewsList()
        homeViewModel.getBannerList()
        homeViewModel.getCategoryList()
    }

    private fun setObservers() {
        homeViewModel.getResponseObserver().observe(this@HomeFragment.requireActivity(), this)
    }

    private fun setListeners() {
        binding.tvGolden.setOnClickListener(this)
        binding.tvGeneral.setOnClickListener(this)
        binding.tvAll.setOnClickListener(this)
        binding.icPAuction.setOnClickListener(this)
        binding.ivTicket.setOnClickListener(this)
        binding.ivMenu.setOnClickListener(this)
        binding.tvProduct.setOnClickListener(this)
    }

    private fun setNewsAdaptor(value: List<NewsListResponse?>?) {
        if(activity!=null && isAdded) {
            newsAdapter = NewsAdapter(this@HomeFragment, value, this@HomeFragment)
            binding.rvNews.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvNews.adapter = newsAdapter
        }
    }

    private fun setBannerAdaptor(value: BannerListResponse?)
    {
        if(activity!=null && isAdded) {
            val images: ArrayList<String> = ArrayList()
            if (value != null) {
                if (value.mainBannerLink != null)
                    images.add(value!!.mainBannerLink.toString())
                if (value.banner01 != null)
                    images.add(value!!.banner01.toString())
                if (value.banner02 != null)
                    images.add(value!!.banner02.toString())
                if (value.banner03 != null)
                    images.add(value!!.banner03.toString())
                if (value.banner04 != null)
                    images.add(value!!.banner04.toString())
                if (value.banner05 != null)
                    images.add(value!!.banner05.toString())
                if (value.banner06 != null)
                    images.add(value!!.banner06.toString())
                if (value.banner07 != null)
                    images.add(value!!.banner07.toString())
                if (value.banner08 != null)
                    images.add(value!!.banner08.toString())
                if (value.banner09 != null)
                    images.add(value!!.banner09.toString())
                if (value.banner10 != null)
                    images.add(value!!.banner10.toString())
            }
            binding.viewPager.apply {
                adapter = ImageAdapter(this@HomeFragment.requireActivity(), images)
            }

            binding.indicatorView.apply {
                setSliderHeight(10F)
                setupWithViewPager(binding.viewPager)
                if(SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
                    setOrientation(IndicatorOrientation.INDICATOR_RTL)
            }
        }
    }

    private fun setFalconAdaptor(value: List<FalconListResponse?>?)
    {
        if(activity!=null && isAdded) {
            auctionAdapter = AuctionAdapter(this@HomeFragment.requireActivity(), value, this@HomeFragment)
            binding.rvBirds.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvBirds.adapter = auctionAdapter
        }
    }

    override fun onItemClick(position: Int) {
        if(SharedPreferencesManager.getString(AppConstants.USER_ROLE) == "USER") {
            startActivity(Intent(this@HomeFragment.requireActivity(), AuctionBetActivity::class.java)
                .putExtra("falconId",homeViewModel.dataFalconMLD.value!!.responce!![position].falconId)
                .putExtra("maxPrice",homeViewModel.dataFalconMLD.value!!.responce!![position].falconMaxPrice))
        }
        else
        {
            val intent = Intent(this@HomeFragment.requireActivity(), AuctionDetailViewActivity::class.java)
                .putExtra("falconId",homeViewModel.dataFalconMLD.value!!.responce!![position].falconId)
                .putExtra("maxPrice",homeViewModel.dataFalconMLD.value!!.responce!![position].falconMaxPrice)
            startActivity(intent)
        }
    }

    override fun onNewsItemClick(position: Int) {
        startActivity(Intent(this@HomeFragment.requireActivity(), NewsViewActivity::class.java)
            .putExtra("data", homeViewModel.dataNewsMLD.value!![position]))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.tvGolden.id -> {
                binding.ivDropGolden.visibility = View.VISIBLE
                binding.ivDropGeneral.visibility = View.INVISIBLE
                showProgressDialog(LoaderType.NORMAL, "")
                homeViewModel.getFalconList(homeViewModel.dataFalconCategoryMLD.value!!.responce!![0].falconCategoryId)
            }
            binding.tvGeneral.id -> {
                binding.ivDropGolden.visibility = View.INVISIBLE
                binding.ivDropGeneral.visibility = View.VISIBLE
                showProgressDialog(LoaderType.NORMAL, "")
                homeViewModel.getFalconList(homeViewModel.dataFalconCategoryMLD.value!!.responce!![1].falconCategoryId)
            }
            binding.tvAll.id -> {
                val list : List<NewsListResponse?>? = homeViewModel.dataNewsMLD.value
                val newsMainResponse = NewsMainResponse(
                    responce = list as List<NewsListResponse>?)
                startActivity(Intent(this@HomeFragment.requireActivity(), NewsDetailActivity::class.java).
                putExtra("data",newsMainResponse))
            }
            binding.icPAuction.id -> {
                startActivity(Intent(this@HomeFragment.requireActivity(), AuctionListActivity::class.java))
            }
            binding.ivTicket.id -> {
                (activity as MainActivity).hitTicketTab()
            }
            binding.ivMenu.id -> {
                (activity as MainActivity).openDrawer()
            }
            binding.tvProduct.id -> {
                (activity as MainActivity).openProductFragment()
            }
        }
    }

    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.GET_NEWS -> {
                setNewsAdaptor(homeViewModel.dataNewsMLD.value)
            }
            ApiCodes.GET_BANNER -> {
                setBannerAdaptor(homeViewModel.dataBannersMLD.value!!.responce)
            }
            ApiCodes.GET_FALCON_CATEGORY -> {
                hideProgressDialog(LoaderType.NORMAL, "")
                setCategoryText(homeViewModel.dataFalconCategoryMLD.value!!.responce)
                if(binding.ivDropGeneral.visibility == View.VISIBLE){
                    homeViewModel.getFalconList(homeViewModel.dataFalconCategoryMLD.value!!.responce!![1].falconCategoryId)
                } else {
                    homeViewModel.getFalconList(homeViewModel.dataFalconCategoryMLD.value!!.responce!![0].falconCategoryId)
                }
            }
            ApiCodes.GET_FALCON -> {
                hideProgressDialog(LoaderType.NORMAL, "")
                val list = homeViewModel.dataFalconMLD.value!!.responce
                if(list!!.size > 0) {
                    binding.rvBirds.visibility = View.VISIBLE
                    binding.cvNoData.visibility = View.GONE
                    setFalconAdaptor(list)
                } else {
                    binding.rvBirds.visibility = View.GONE
                    binding.cvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setCategoryText(response: List<FalconCategoryResponse>?) {
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) {
            binding.tvGoldenText.text = response!![0].falconCategoryAr
            binding.tvGeneralText.text = response[1].falconCategoryAr
        }
        else
        {
            binding.tvGoldenText.text = response!![0].falconCategoryEn
            binding.tvGeneralText.text = response[1].falconCategoryEn
        }
    }

    override fun onResume() {
        homeViewModel.getCategoryList()
        super.onResume()
    }

}