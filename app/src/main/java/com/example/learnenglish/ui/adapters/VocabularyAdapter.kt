package com.example.learnenglish.ui.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.data.models.Vocabulary
import com.example.learnenglish.databinding.VocabularyItemBinding
import com.example.learnenglish.ui.activities.DetailVocabularyActivity

class VocabularyAdapter(private var listVocabularies: List<Vocabulary>) :
    RecyclerView.Adapter<VocabularyAdapter.VocabularyViewHolder>() {
    inner class VocabularyViewHolder(val vocabularyItemBinding: VocabularyItemBinding) :
        RecyclerView.ViewHolder(vocabularyItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabularyViewHolder {
        return VocabularyViewHolder(
            VocabularyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listVocabularies.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VocabularyViewHolder, position: Int) {
        val item = listVocabularies[position]

        item.image?.let { holder.vocabularyItemBinding.imageItem.setImageResource(it) }
        holder.vocabularyItemBinding.textItem.text = "${position + 1}. ${item.title}"
        holder.vocabularyItemBinding.vocabularyItem.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailVocabularyActivity::class.java)

            intent.putExtra(DetailVocabularyActivity.Title, item.title)

            holder.itemView.context.startActivity(intent)
        }
    }
}