package com.flowz.introtooralanguage.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.flowz.introtooralanguage.R
import androidx.recyclerview.widget.ListAdapter
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel
import kotlinx.android.synthetic.main.ora_num.view.*


class OutdoorWordsAdapter  (val listener: RowClickListener)  :ListAdapter<OutdoorWordsModel, OutdoorWordsAdapter.OraNumViewHolder>(OutdoorWordsDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  OraNumViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.ora_num1, parent, false)
        return OraNumViewHolder(view, listener)

    }

    override fun onBindViewHolder(holder: OraNumViewHolder, position: Int) {

        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(getItem(position))
            listener.onPlayOraWordClickListener(getItem(position))
            listener.onEditOraWordClickListener(getItem(position))
        }

    }

    class OraNumViewHolder(view: View, val listener: RowClickListener): RecyclerView.ViewHolder(view){

        val play = itemView.findViewById<ImageView>(R.id.play_oraword)

        fun bind(outdoorWords: OutdoorWordsModel){

            itemView.eng_num.text = outdoorWords.engNum
            itemView.ora_num.text = outdoorWords.oraNum


            itemView.play_oraword.setOnClickListener {
                    listener.onPlayOraWordClickListener(outdoorWords)
            }

            itemView.edit_oraword.setOnClickListener {
                    listener.onEditOraWordClickListener(outdoorWords)

            }

        }

    }

    interface RowClickListener{
        fun onPlayOraWordClickListener(outdoorWords: OutdoorWordsModel)
        fun onEditOraWordClickListener(outdoorWords: OutdoorWordsModel)
        fun onItemClickListener(outdoorWords: OutdoorWordsModel)

    }

}