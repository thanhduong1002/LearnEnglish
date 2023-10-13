package com.example.learnenglish.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.ui.activities.DetailPracticeActivity

class AnswerAdapter(private var listAnswers: List<String>, private val activity: DetailPracticeActivity) :
    RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    private var selectedPosition = -1
    private val normalColorRes = R.color.main
    private val selectedColorRes = R.color.selected
    fun setAnswersList(listAnswers: List<String>) {
        this.listAnswers = listAnswers
    }
    class AnswerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewOption: TextView = view.findViewById(R.id.textViewOption)
        val cardItem: CardView = view.findViewById(R.id.optionItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.option_item, parent, false)
        return AnswerViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return listAnswers.size
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val item = listAnswers[position]

        holder.textViewOption.text = item

        if (position == selectedPosition) {
            holder.cardItem.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, selectedColorRes))
        } else {
            holder.cardItem.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, normalColorRes))
        }

        holder.cardItem.setOnClickListener {
            if (selectedPosition != position) {
                notifyItemChanged(selectedPosition)
                selectedPosition = position
                notifyItemChanged(position)
            }

            activity.setAnswer(listAnswers[selectedPosition])
        }
    }
}