package com.example.learnenglish.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.ui.activities.DetailListeningActivity

class ListeningOptionAdapter(private var listListeningOptions: List<String>) :
    RecyclerView.Adapter<ListeningOptionAdapter.ListeningOptionViewHolder>() {
    class ListeningOptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewOption: TextView = view.findViewById(R.id.textViewOption)
        val cardItem: CardView = view.findViewById(R.id.optionItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeningOptionViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.option_item, parent, false)

        return ListeningOptionViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return listListeningOptions.size
    }

    override fun onBindViewHolder(holder: ListeningOptionViewHolder, position: Int) {
        val item = listListeningOptions[position]

        holder.textViewOption.text = item
        holder.cardItem.setOnClickListener {
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