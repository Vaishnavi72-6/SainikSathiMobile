package com.example.sainiksathimobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sainiksathimobile.R
import com.example.sainiksathimobile.data.model.PensionRecord

class PensionAdapter(
    private var pensionList: List<PensionRecord> = listOf()
) : RecyclerView.Adapter<PensionAdapter.PensionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PensionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pension_record, parent, false)
        return PensionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PensionViewHolder, position: Int) {
        val record = pensionList[position]
        holder.tvDate.text = record.lastPaidDate
        holder.tvAmount.text = "₹${record.amount}"
        holder.tvRemarks.text = record.pensionType
    }

    override fun getItemCount(): Int = pensionList.size

    fun updateData(newList: List<PensionRecord>) {
        pensionList = newList
        notifyDataSetChanged()
    }

    class PensionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvRemarks: TextView = itemView.findViewById(R.id.tvRemarks)
    }
}
