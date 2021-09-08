package com.app.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.ActivityNewsDetailBinding
import com.app.model.dataclasses.NewsMainResponse
import com.app.view.adaptors.AuctionAdapter
import com.app.view.adaptors.NewsDetailAdapter

class NewsDetailActivity : BaseActivity(), View.OnClickListener,
    AuctionAdapter.OnItemClickListener {
    private lateinit var binding: ActivityNewsDetailBinding
    private lateinit var dataModel : NewsMainResponse
    private lateinit var newsAdapter: NewsDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail)
        setListeners()
        getIntentData()
        setData()
    }

    private fun getIntentData() {
        dataModel = (intent.getSerializableExtra("data") as NewsMainResponse?)!!
    }

    private fun setData() {
        newsAdapter = NewsDetailAdapter(this, dataModel.responce,this)
        binding.rvAllNews.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAllNews.adapter = newsAdapter
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

    override fun onItemClick(position: Int) {
        startActivity(Intent(this, NewsViewActivity::class.java)
            .putExtra("data", dataModel.responce!![position]))
    }

}