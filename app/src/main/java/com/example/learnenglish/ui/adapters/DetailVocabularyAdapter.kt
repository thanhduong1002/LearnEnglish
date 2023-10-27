package com.example.learnenglish.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.data.models.DetailVocabulary
import com.example.learnenglish.databinding.DetailVocabularyItemBinding

class DetailVocabularyAdapter(private var listDetailVocabularies: List<DetailVocabulary>) :
    RecyclerView.Adapter<DetailVocabularyAdapter.DetailVocabularyViewHolder>() {
    inner class DetailVocabularyViewHolder(val detailVocabularyItemBinding: DetailVocabularyItemBinding) :
        RecyclerView.ViewHolder(detailVocabularyItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailVocabularyViewHolder {
        return DetailVocabularyViewHolder(
            DetailVocabularyItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return listDetailVocabularies.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DetailVocabularyViewHolder, position: Int) {
        val item = listDetailVocabularies[position]

        holder.detailVocabularyItemBinding.textEnglish.text = "${item.english} - ${item.spelling}"
        holder.detailVocabularyItemBinding.textVietnamese.text = item.vietnamese
        holder.detailVocabularyItemBinding.textExample.text = item.example
        holder.detailVocabularyItemBinding.textExampleVietnamese.text = item.exampleVN
        holder.detailVocabularyItemBinding.flipCard.setOnClickListener {
            val frontVisible = holder.detailVocabularyItemBinding.frontLayout.visibility == View.VISIBLE

            holder.detailVocabularyItemBinding.frontLayout.visibility = if (frontVisible) View.GONE else View.VISIBLE
            holder.detailVocabularyItemBinding.backLayout.visibility = if (frontVisible) View.VISIBLE else View.GONE
        }
    }
}
