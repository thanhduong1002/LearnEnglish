package com.example.learnenglish.ui.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.data.models.DetailResult
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailResultAdapter(private var listDetailResults: MutableList<DetailResult>, private val detailVocabularyViewModel: DetailVocabularyViewModel) :
    RecyclerView.Adapter<DetailResultAdapter.DetailResultViewHolder>() {
    class DetailResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textResult: TextView = view.findViewById(R.id.textResult)
        val textAnswer: TextView = view.findViewById(R.id.textAnswer)
        val cardDetail: CardView = view.findViewById(R.id.cardDetail)
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
                if (position % 2 == 0) holder.textResult.text = vietnameseQuestion
                else holder.textResult.text = item.question
                holder.textAnswer.text = item.answer

                val iconResource = if (vietnameseQuestion == item.answer || item.question == item.answer) R.drawable.correct_icon
                else R.drawable.incorrect_icon

                val colorId = if (vietnameseQuestion == item.answer || item.question == item.answer) {
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