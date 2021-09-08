package com.app.view.adaptors

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
import com.app.view.fragments.HomeFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(
    private val context: HomeFragment,
    private val newsList: List<NewsListResponse?>?,
    private val listener: OnNewsItemClickListener
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_news, parent, false)
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
            val ivImage = itemView.findViewById(R.id.ivImage) as ImageView
            tvName.text = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) newsList!!.newsTitleAr else newsList!!.newsTitleEn
            tvDate.text = newsList.newsDate!!.split("T")[0]
            Glide.with(context).load(newsList.newsImageLink).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivImage)

        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onNewsItemClick(position)
            }
        }
    }

    interface OnNewsItemClickListener {
        fun onNewsItemClick(position: Int)
    }
}