package com.app.view.adaptors

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.model.dataclasses.FalconListResponse
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.view.fragments.HomeFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AuctionAdapter(
    private val context: Context,
    private val falconList: List<FalconListResponse?>?,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AuctionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_auction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(falconList?.get(position))
    }


    override fun getItemCount(): Int {
        return if (falconList != null && falconList.isNotEmpty()) {
            falconList.size
        } else {
            0
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        private val myItemView: CardView = itemView.findViewById(R.id.rl_detail)

        init{
            myItemView.setOnClickListener(this)
            }

        fun bindItems(falconListResponse: FalconListResponse?) {
            val tvName = itemView.findViewById(R.id.tv_name) as TextView
            val tvDate = itemView.findViewById(R.id.tv_date) as TextView
            val ivImage = itemView.findViewById(R.id.ivImage) as ImageView
            val tvPrice = itemView.findViewById(R.id.tv_price) as TextView
            if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
                tvName.text = falconListResponse!!.falconName
            else
                tvName.text = falconListResponse!!.falconNameEn
            tvDate.text = falconListResponse!!.limitAucctionTime!!.split("T")[0]
            tvPrice.text = falconListResponse!!.falconMaxPrice.toString()
            Glide.with(context).load(falconListResponse.falconImageMobiles!![0].docImageURL).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage)
//            ivImage.setImageBitmap(convertStringToBitmap(falconListResponse.falconImageMobiles!![0].docImageContent.toString()))
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