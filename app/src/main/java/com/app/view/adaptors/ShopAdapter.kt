package com.app.view.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.model.dataclasses.ShopListResponse
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.util.*

class ShopAdapter(
    private val context: Context,
    private val shopList: List<ShopListResponse?>?,
    private val listener: OnShopItemClickListener
) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_shop, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(shopList?.get(position))
    }


    override fun getItemCount(): Int {
        return if (shopList != null && shopList.isNotEmpty()) {
            shopList.size
        } else {
            0
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        private val myItemView: RelativeLayout = itemView.findViewById(R.id.rl_detail)

        init{
            myItemView.setOnClickListener(this)
            }

        fun bindItems(shopList: ShopListResponse?) {
            val tvName = itemView.findViewById(R.id.tv_name) as TextView
            val ivImage = itemView.findViewById(R.id.ivImage) as ImageView
            if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) {
                tvName.text = shopList!!.shopNameAr
            }
            else {
                tvName.text = shopList!!.shopName
            }
            Glide.with(context).load(shopList.docImage).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(ivImage)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onShopItemClick(position)
            }
        }
    }

    interface OnShopItemClickListener {
        fun onShopItemClick(position: Int)
    }


}