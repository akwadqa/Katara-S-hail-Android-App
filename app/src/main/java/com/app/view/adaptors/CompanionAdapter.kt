package com.app.view.adaptors

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.google.android.material.textfield.TextInputEditText


class CompanionAdapter(
    private val context: Context,
    private val list: ArrayList<String>,
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<CompanionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_companion, parent, false)
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

        private val tvAdd: TextView = itemView.findViewById(R.id.tv_Add)

        init{
            tvAdd.setOnClickListener(this)
            }

        fun bindItems(list1: String?) {
            val stSlot = itemView.findViewById(R.id.et_slot) as TextInputEditText
//            stSlot.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence,
//                    start: Int,
//                    count: Int,
//                    after: Int,
//                ) {
//                }
//
//                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                    list!![adapterPosition] = s.toString()
//                }
//
//                override fun afterTextChanged(s: Editable) {}
//            })
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