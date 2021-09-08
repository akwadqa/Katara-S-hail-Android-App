package com.app.view.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

class ImageAdapter(
    private val context: Context,
    images: ArrayList<String>
) :  RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val image : ArrayList<String> = images

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(image?.get(position))
    }



    override fun getItemCount(): Int {
        return if (image != null && image.isNotEmpty()) {
            image.size
        } else {
            0
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(list: String) {
            val ivImage = itemView.findViewById(R.id.ivImage) as RoundedImageView
            Glide.with(context).load(list).into(ivImage)
        }
    }



}