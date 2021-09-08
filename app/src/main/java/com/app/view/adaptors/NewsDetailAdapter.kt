package com.app.view.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.model.dataclasses.NewsListResponse
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.view.activities.NewsDetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class NewsDetailAdapter(
    private val context: Context,
    private val newsList: List<NewsListResponse?>?,
    private val listener: AuctionAdapter.OnItemClickListener
) : RecyclerView.Adapter<NewsDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_all_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(newsList?.get(position))
    }


    override fun getItemCount(): Int {
        return if (newsList != null && newsList.isNotEmpty()) {
            newsList.size
        } else {
            0
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        private val myItemView: CardView = itemView.findViewById(R.id.rl_detail)

        init{
            myItemView.setOnClickListener(this)
        }

        fun bindItems(newsList: NewsListResponse?) {
            val tvName = itemView.findViewById(R.id.tv_name) as TextView
            val tvDate = itemView.findViewById(R.id.tv_date) as TextView
            val tvDesc = itemView.findViewById(R.id.tv_desc) as TextView
            val ivImage = itemView.findViewById(R.id.ivImage) as ImageView
            tvDate.text = newsList!!.newsDate!!.split("T")[0]
            if(SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)){
                tvName.text = newsList.newsTitleAr
                tvDesc.text = newsList.newsDetailAr
            } else {
                tvName.text =newsList.newsTitleEn
                tvDesc.text = newsList.newsDetailEn
            }
            Glide.with(context).load(newsList.newsImageLink).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivImage)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}