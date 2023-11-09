package com.example.learnenglish.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.databinding.OptionItemBinding
import com.example.learnenglish.ui.activities.DetailPracticeActivity
import com.example.learnenglish.ui.activities.ListeningActivity
import com.example.learnenglish.ui.activities.PracticeActivity
import com.example.learnenglish.ui.activities.TranslationActivity
import com.example.learnenglish.ui.activities.VocabularyActivity

class OptionAdapter(private var listOptions: List<String>) :
    RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {
    inner class OptionViewHolder(val optionItemBinding: OptionItemBinding) :
        RecyclerView.ViewHolder(optionItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        return OptionViewHolder(
            OptionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOptions.size
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val item = listOptions[position]

        holder.optionItemBinding.textViewOption.text = item
        holder.optionItemBinding.optionItem.setOnClickListener {
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

                "Listening" -> {
                    intent = Intent(holder.itemView.context, ListeningActivity::class.java)

                    intent.putExtra(ListeningActivity.Title, item)

                    holder.itemView.context.startActivity(intent)
                }

                "Translation" -> {
                    intent = Intent(holder.itemView.context, TranslationActivity::class.java)

                    intent.putExtra(TranslationActivity.Title, item)

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