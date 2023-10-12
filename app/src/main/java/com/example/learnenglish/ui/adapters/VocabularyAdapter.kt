package com.example.learnenglish.ui.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.data.models.Vocabulary
import com.example.learnenglish.ui.activities.DetailVocabularyActivity

class VocabularyAdapter(private var listVocabularies: List<Vocabulary>) : RecyclerView.Adapter<VocabularyAdapter.VocabularyViewHolder>() {
    class VocabularyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageItem: ImageView = view.findViewById(R.id.imageItem)
        val textItem: TextView = view.findViewById(R.id.textItem)
        val vocabularyItem: CardView = view.findViewById(R.id.vocabularyItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabularyViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.vocabulary_item, parent, false)
        return VocabularyViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return listVocabularies.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VocabularyViewHolder, position: Int) {
        val item = listVocabularies[position]

        item.image?.let { holder.imageItem.setImageResource(it) }
        holder.textItem.text = "${position + 1}. ${ item.title }"
        holder.vocabularyItem.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailVocabularyActivity::class.java)

            intent.putExtra(DetailVocabularyActivity.Title, item.title)
            holder.itemView.context.startActivity(intent)
        }
    }
}