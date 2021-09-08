package com.app.view.activities

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.app.R
import com.app.databinding.ActivityNewsViewBinding
import com.app.model.dataclasses.NewsListResponse
import com.app.model.dataclasses.NewsMainResponse
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class NewsViewActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityNewsViewBinding
    private lateinit var dataModel : NewsListResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_view)
        setListeners()
        getIntentData()
        setData()
    }

    private fun getIntentData() {
        dataModel = (intent.getSerializableExtra("data") as NewsListResponse?)!!
    }

    private fun setData() {
        binding.tvName.text = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) dataModel!!.newsTitleAr else dataModel!!.newsTitleEn
        binding.tvDate.text = dataModel.newsDate!!.split("T")[0]
        binding.tvDescription.text = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) dataModel.newsDetailAr else dataModel.newsDetailEn
        Glide.with(this).load(dataModel.newsImageLink).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivImage)
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