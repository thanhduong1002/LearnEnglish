package com.example.learnenglish.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.TypedArrayUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.ui.activities.DetailPracticeActivity
import com.example.learnenglish.ui.activities.PracticeActivity
import com.example.learnenglish.ui.activities.VocabularyActivity

class OptionAdapter(private var listOptions: List<String>) :
    RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {
    class OptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewOption: TextView = view.findViewById(R.id.textViewOption)
        val cardItem: CardView = view.findViewById(R.id.optionItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.option_item, parent, false)

        return OptionViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return listOptions.size
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val item = listOptions[position]

        holder.textViewOption.text = item
        holder.cardItem.setOnClickListener {
            val intent: Intent

            when (item) {
                "Vocabulary" -> {
                    intent = Intent(holder.itemView.context, VocabularyActivity::class.java)

                    intent.putExtra(VocabularyActivity.Title, item)

                    holder.itemView.context.startActivity(intent)
                }

                "10 Questions" -> {
                    intent = Intent(holder.itemView.context, DetailPracticeActivity::class.java)

                    intent.putExtra(DetailPracticeActivity.Quantity, getNumberQuestions(item))

                    holder.itemView.context.startActivity(intent)
                }

                "20 Questions" -> {
                    intent = Intent(holder.itemView.context, DetailPracticeActivity::class.java)

                    intent.putExtra(DetailPracticeActivity.Quantity, getNumberQuestions(item))

                    holder.itemView.context.startActivity(intent)
                }

                "30 Questions" -> {
                    intent = Intent(holder.itemView.context, DetailPracticeActivity::class.java)

                    intent.putExtra(DetailPracticeActivity.Quantity, getNumberQuestions(item))

                    holder.itemView.context.startActivity(intent)
                }

                else -> {
                    intent = Intent(holder.itemView.context, PracticeActivity::class.java)

                    intent.putExtra(PracticeActivity.Title, item)

                    holder.itemView.context.startActivity(intent)
                }
            }
        }
    }

    private fun getNumberQuestions(packageQuestion: String): String {
        return packageQuestion.substring(0, 2)
    }
}