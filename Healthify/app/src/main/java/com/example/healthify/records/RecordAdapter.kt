package com.example.healthify.records

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthify.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordAdapter(private val records: List<RecordData>) :
    RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timestampTextView: TextView = itemView.findViewById(R.id.textViewTimestamp)
        val calorieTextView: TextView = itemView.findViewById(R.id.textViewCalorie)
        val weightTextView: TextView = itemView.findViewById(R.id.textViewWeight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.record_card, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = records[position]
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(record.timestamp)
        holder.timestampTextView.text = sdf.format(date)
        holder.calorieTextView.text = String.format("Calorie Consumed: %.2f kCal",record.calorie)
        holder.weightTextView.text = String.format("Weight: %.1f Kg",record.weight)
    }

    override fun getItemCount() = records.size
}
