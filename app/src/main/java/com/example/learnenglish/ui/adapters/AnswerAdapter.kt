package com.example.learnenglish.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.databinding.OptionItemBinding
import com.example.learnenglish.interfaces.IChooseAnswer

class AnswerAdapter(
    private var listAnswers: List<String>
) :
    RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    private var selectedPosition = -1
    private val normalColorRes = R.color.main
    private val selectedColorRes = R.color.selected
    private var quantity: Int = 0
    private lateinit var listener: IChooseAnswer

    fun setAnswersList(listAnswers: List<String>, listener: IChooseAnswer) {
        this.listAnswers = listAnswers
        this.listener = listener
    }

    inner class AnswerViewHolder(val answerItemBinding: OptionItemBinding) :
        RecyclerView.ViewHolder(answerItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(
            OptionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listAnswers.size
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val item = listAnswers[position]

        holder.answerItemBinding.textViewOption.text = item
        holder.answerItemBinding.optionItem.setCardBackgroundColor(
            if (position == selectedPosition) {
                ContextCompat.getColor(holder.itemView.context, selectedColorRes)
            } else {
                ContextCompat.getColor(holder.itemView.context, normalColorRes)
            }
        )
        holder.answerItemBinding.optionItem.setOnClickListener {
            if (selectedPosition != position) {
                notifyItemChanged(selectedPosition)

                selectedPosition = position

                notifyItemChanged(position)
            }

            listener.onClickAnswer(listAnswers[selectedPosition])

            quantity = listener.getQuantity()

            listener.replaceOrAddAnswer(listAnswers[selectedPosition], quantity)
        }
    }
}