package com.example.learnenglish.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R

class AnswerAdapter(private var listAnswers: List<String>) :
    RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    fun setCartsList(listAnswers: List<String>) {
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

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val item = listAnswers[position]

        holder.textViewOption.text = item
    }
}