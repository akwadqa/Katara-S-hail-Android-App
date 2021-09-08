package com.app.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.R
import com.app.databinding.ActivityShopBinding
import com.app.model.dataclasses.ProductList
import com.app.model.dataclasses.ShopDetailResponse
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.view.adaptors.ProductShopAdapter
import com.app.viewmodel.ProductViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ShopActivity : BaseActivity(), View.OnClickListener, ProductShopAdapter.OnItemClickListener
{
    private val productViewModel by lazy { ViewModelProvider(this).get(ProductViewModel::class.java) }
    private lateinit var binding: ActivityShopBinding
    private lateinit var shopId : String
    private lateinit var productAdapter: ProductShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop)
        setBaseViewModel(productViewModel)
        setObservers()
        setListeners()
        getIntentData()
        productViewModel.getShopDetails(shopId)
    }

    private fun setObservers() {
        productViewModel.getResponseObserver().observe(this, this)
    }

    private fun getIntentData() {
        shopId = intent.getStringExtra("shop_id").toString()
    }
    private fun setData(dataModel: ShopDetailResponse?) {
        var name : String = ""
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
        {
            binding.tvName.text = dataModel!!.shopNameAr
            binding.tvLocation.text = dataModel!!.shopLocationAr
            binding.tvDescription.text = dataModel.shopDescriptionAr
            name = dataModel!!.shopNameAr.toString()
        }
        else
        {
            binding.tvName.text = dataModel!!.shopName
            binding.tvLocation.text = dataModel!!.shopLocation
            binding.tvDescription.text = dataModel!!.shopDescription
            name = dataModel!!.shopName.toString()
        }
        Glide.with(this).load(dataModel.docImage).placeholder(resources.getDrawable(R.drawable.vendor_logo_background_circle)).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivImage)
        setProductAdaptor(dataModel.products,name)
    }

    private fun setProductAdaptor(value: List<ProductList>?, name: String)
    {
        productAdapter = ProductShopAdapter(this, value, name, this)
        binding.rvProducts.layoutManager = GridLayoutManager(this, 2)
        binding.rvProducts.adapter = productAdapter
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

    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.GET_SHOP_DETAILS -> {
                setData(productViewModel.dataShopDetailMLD.value)
            }
        }
    }

    override fun onItemClick(position: Int) {
        startActivity(Intent(this, ProductShopActivity::class.java)
            .putExtra("shop_phone",productViewModel.dataShopDetailMLD.value!!.shopPhone)
            .putExtra("shop_email",productViewModel.dataShopDetailMLD.value!!.shopEmail)
            .putExtra("data",productViewModel.dataShopDetailMLD.value!!.products!![position]))
    }

}