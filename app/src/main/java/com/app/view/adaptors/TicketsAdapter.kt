package com.app.view.adaptors

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.model.dataclasses.TicketListResponse
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.view.activities.BookTicketActivity
import java.text.SimpleDateFormat
import java.util.*

class TicketsAdapter(
    private val ticketList: List<TicketListResponse?>?,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_ticket, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(ticketList?.get(position))
    }


    override fun getItemCount(): Int {
        return if (ticketList != null && ticketList.isNotEmpty()) {
            ticketList.size
        } else {
            0
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        private val myItemView: RelativeLayout = itemView.findViewById(R.id.rl_detail)
        private val ivScan: ImageView = itemView.findViewById(R.id.iv_share)

        init{
            myItemView.setOnClickListener(this)
            ivScan.setOnClickListener(this)
            }

        fun bindItems(ticketListResponse: TicketListResponse?) {
            val tvName = itemView.findViewById(R.id.tv_name) as TextView
            val tvDate = itemView.findViewById(R.id.tv_date) as TextView
            val tvTime = itemView.findViewById(R.id.tv_time) as TextView
             tvName.text = ticketListResponse!!.holderName
            tvDate.text = ticketListResponse.ticketDate!!.split("T")[0]
            tvTime.text = ticketListResponse.timeSlot!!.displayNameEn
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            when (v?.id) {
                R.id.iv_share -> {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onShareClick(position)
                    }
                }
                R.id.rl_detail -> {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position)
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onShareClick(position: Int)
    }
}