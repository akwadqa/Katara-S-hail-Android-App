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
import com.app.model.dataclasses.ProductList
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProductShopAdapter(
    private val context: Context,
    private val productList: List<ProductList>?,
    private val name: String,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ProductShopAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_products, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(productList?.get(position))
    }


    override fun getItemCount(): Int {
        return if (productList != null && productList.isNotEmpty()) {
            productList.size
        } else {
            0
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        private val myItemView: CardView = itemView.findViewById(R.id.rl_detail)

        init{
            myItemView.setOnClickListener(this)
            }

        fun bindItems(productListResponse: ProductList?) {
            val tvName = itemView.findViewById(R.id.tv_name) as TextView
            val tvShopName= itemView.findViewById(R.id.tv_shop_name) as TextView
            val ivImage = itemView.findViewById(R.id.ivImage) as ImageView
            val tvPrice = itemView.findViewById(R.id.tv_price) as TextView
            if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) {
                tvName.text = productListResponse!!.productNameAr
                tvShopName.text = name
            }
            else {
                tvName.text = productListResponse!!.productName
                tvShopName.text = name
            }

            tvPrice.text = productListResponse!!.productPrice.toString()
            Glide.with(context).load(productListResponse.docImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage)
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