package com.example.learnenglish.ui.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.data.models.DetailResult
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailResultAdapter(
    private var listDetailResults: MutableList<DetailResult>,
    private val detailVocabularyViewModel: DetailVocabularyViewModel
) :
    RecyclerView.Adapter<DetailResultAdapter.DetailResultViewHolder>() {
    private lateinit var isListening: String
    fun setIsListening(newIsListening: String) {
        isListening = newIsListening
    }

    private fun compareStringsIgnoreCase(str1: String, str2: String): Boolean {
        return str1.equals(str2, ignoreCase = true)
    }

    class DetailResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textResult: TextView = view.findViewById(R.id.textResult)
        val textAnswer: TextView = view.findViewById(R.id.textAnswer)
        val imageResult: ImageView = view.findViewById(R.id.imageResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailResultViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_result_item, parent, false)

        return DetailResultViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return listDetailResults.size
    }

    @DelicateCoroutinesApi
    override fun onBindViewHolder(holder: DetailResultViewHolder, position: Int) {
        val item = listDetailResults[position]
        var vietnameseQuestion: String

        GlobalScope.launch(Dispatchers.IO) {
            vietnameseQuestion = detailVocabularyViewModel.getVietnameseByEnglish(item.question)

            holder.itemView.post {
                holder.textResult.text = if (isListening == "true" || position % 2 != 0) {
                    item.question
                } else {
                    vietnameseQuestion
                }

                holder.textAnswer.text = item.answer

                val iconResource =
                    if (vietnameseQuestion == item.answer || compareStringsIgnoreCase(
                            item.question,
                            item.answer
                        )
                    ) R.drawable.correct_icon
                    else R.drawable.incorrect_icon

                val colorId = if (vietnameseQuestion == item.answer || compareStringsIgnoreCase(
                        item.question,
                        item.answer
                    )
                ) {
                    R.color.green
                } else {
                    R.color.red
                }

                val color = ContextCompat.getColor(holder.itemView.context, colorId)

                holder.imageResult.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                holder.imageResult.setImageResource(iconResource)
            }
        }
    }
}