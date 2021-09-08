package com.app.view.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.model.dataclasses.TicketSlotsResponse
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager


class TimeSlotAdapter(
    private val context: Context,
    private val list: List<TicketSlotsResponse>?,
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<TimeSlotAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_slot, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list?.get(position))
    }


    override fun getItemCount(): Int {
        return if (list != null && list.isNotEmpty()) {
            list.size
        } else {
            0
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        private val rlItem: RelativeLayout = itemView.findViewById(R.id.rl_detail)

        init{
            rlItem.setOnClickListener(this)
            }

        fun bindItems(list1: TicketSlotsResponse?) {
            val tvSlot = itemView.findViewById(R.id.tv_slot) as TextView
            if(SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
                tvSlot.text = list1!!.displayNameAr
            else
                tvSlot.text = list1!!.displayNameEn
            if(list1.slotOrder==-1) {
                tvSlot.setTextColor(context.resources.getColor(R.color.white))
                tvSlot.background = context.resources.getDrawable(R.drawable.button_rounded_more)
            }
            else
            {
                tvSlot.setTextColor(context.resources.getColor(R.color.black))
                tvSlot.background = context.resources.getDrawable(R.drawable.grey_rounded_more)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onTimeItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onTimeItemClick(position: Int)
    }

}