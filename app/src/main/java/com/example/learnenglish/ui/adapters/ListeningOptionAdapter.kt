package com.example.learnenglish.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.databinding.OptionItemBinding
import com.example.learnenglish.ui.activities.DetailListeningActivity

class ListeningOptionAdapter(private var listListeningOptions: List<String>) :
    RecyclerView.Adapter<ListeningOptionAdapter.ListeningOptionViewHolder>() {
    inner class ListeningOptionViewHolder(val listeningOptionItemBinding: OptionItemBinding) :
        RecyclerView.ViewHolder(listeningOptionItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeningOptionViewHolder {
        return ListeningOptionViewHolder(
            OptionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listListeningOptions.size
    }

    override fun onBindViewHolder(holder: ListeningOptionViewHolder, position: Int) {
        val item = listListeningOptions[position]

        holder.listeningOptionItemBinding.textViewOption.text = item
        holder.listeningOptionItemBinding.optionItem.setOnClickListener {
            val intent: Intent

            when (item) {
                "10 Questions" -> {
                    intent = Intent(holder.itemView.context, DetailListeningActivity::class.java)

                    intent.putExtra(DetailListeningActivity.Quantity, getNumberQuestions(item))

                    holder.itemView.context.startActivity(intent)
                }

                "20 Questions" -> {
                    intent = Intent(holder.itemView.context, DetailListeningActivity::class.java)

                    intent.putExtra(DetailListeningActivity.Quantity, getNumberQuestions(item))

                    holder.itemView.context.startActivity(intent)
                }

                "30 Questions" -> {
                    intent = Intent(holder.itemView.context, DetailListeningActivity::class.java)

                    intent.putExtra(DetailListeningActivity.Quantity, getNumberQuestions(item))

                    holder.itemView.context.startActivity(intent)
                }
            }
        }
    }

    private fun getNumberQuestions(packageQuestion: String): String {
        return packageQuestion.substring(0, 2)
    }
}