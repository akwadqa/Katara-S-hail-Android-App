package com.app.view.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.model.dataclasses.FalconPreviousListResponse
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import java.text.SimpleDateFormat
import java.util.*

class AuctionPreviousAdapter(
    private val context: Context,
    private val falconList: List<FalconPreviousListResponse?>?,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AuctionPreviousAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_previous_auction, parent, false)
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

        private val myItemView: RelativeLayout = itemView.findViewById(R.id.rl_detail)

        init{
            myItemView.setOnClickListener(this)
        }

        fun bindItems(falconListResponse: FalconPreviousListResponse?) {
            val tvName = itemView.findViewById(R.id.tv_name) as TextView
            val tvDate = itemView.findViewById(R.id.tv_date) as TextView
            val tvTime = itemView.findViewById(R.id.tv_time) as TextView
            val tvPrice = itemView.findViewById(R.id.tv_price) as TextView
            if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
                tvName.text = falconListResponse!!.user!!.userNameAr
            else
                tvName.text = falconListResponse!!.user!!.userName
            tvDate.text = falconListResponse!!.entryDate!!.split("T")[0]
            tvPrice.text = falconListResponse!!.amount.toString() + " QAR"

            var spf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH)
            val date1: Date = spf.parse(falconListResponse.entryDate)
            val calendar = Calendar.getInstance()
            calendar.time = date1
            val currentTime = SimpleDateFormat("hh:mm:ss aa", Locale.ENGLISH).format(calendar.time)
            tvTime.text = currentTime
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