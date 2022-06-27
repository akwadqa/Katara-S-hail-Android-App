package com.app.view.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.app.R
import com.app.databinding.ActivityNewsViewBinding
import com.app.databinding.ActivityProductBinding
import com.app.model.dataclasses.NewsListResponse
import com.app.model.dataclasses.NewsMainResponse
import com.app.model.dataclasses.ProductList
import com.app.model.dataclasses.ProductListResponse
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProductShopActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProductBinding
    private lateinit var dataModel : ProductList
    private lateinit var shopPhone : String
    private lateinit var shopEmail : String
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        setListeners()
        getIntentData()
        setData()
    }

    private fun getIntentData() {
        dataModel = (intent.getSerializableExtra("data") as ProductList?)!!
        shopPhone = intent.getStringExtra("shop_phone").toString()
        shopEmail = intent.getStringExtra("shop_email").toString()
    }

    private fun setData() {
        binding.tvName.text = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) dataModel.productNameAr else dataModel.productName
        binding.tvPrice.text = dataModel.productPrice.toString()
        binding.tvDescription.text = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) dataModel.productDescriptionAr else dataModel.productDescription
        Glide.with(this).load(dataModel.docImage).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivImage)
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

    override fun onBackPressed() {
        if(this::dialog.isInitialized && dialog!=null && dialog.isShowing) {
            dialog.dismiss()
        }
        else {
            super.onBackPressed()
        }
    }

}