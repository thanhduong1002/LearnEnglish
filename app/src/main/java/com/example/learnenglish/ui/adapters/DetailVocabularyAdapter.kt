package com.example.learnenglish.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.data.models.DetailVocabulary

class DetailVocabularyAdapter(private var listDetailVocabularies: List<DetailVocabulary>) : RecyclerView.Adapter<DetailVocabularyAdapter.DetailVocabularyViewHolder>() {
    class DetailVocabularyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flipCard: CardView = view.findViewById(R.id.flipCard)
        val frontLayout: LinearLayout = view.findViewById(R.id.frontLayout)
        val backLayout: LinearLayout = view.findViewById(R.id.backLayout)
        val textEnglish: TextView = view.findViewById(R.id.textEnglish)
        val textVietnamese: TextView = view.findViewById(R.id.textVietnamese)
        val textExample: TextView = view.findViewById(R.id.textExample)
        val textExampleVietnamese: TextView = view.findViewById(R.id.textExampleVietnamese)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailVocabularyViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_vocabulary_item, parent, false)
        return DetailVocabularyViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return listDetailVocabularies.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DetailVocabularyViewHolder, position: Int) {
        val item = listDetailVocabularies[position]

        holder.textEnglish.text = "${ item.english } - ${item.spelling}"
        holder.textVietnamese.text = item.vietnamese
        holder.textExample.text = item.example
        holder.textExampleVietnamese.text = item.exampleVN

        holder.flipCard.setOnClickListener {
            if (holder.backLayout.visibility == View.GONE) {
                holder.backLayout.visibility = View.VISIBLE
                holder.frontLayout.visibility = View.GONE
            } else {
                holder.backLayout.visibility = View.GONE
                holder.frontLayout.visibility = View.VISIBLE
            }
        }
    }
}
