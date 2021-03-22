package com.flowz.introtooralanguage.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.flowz.introtooralanguage.R
import androidx.recyclerview.widget.ListAdapter
import com.flowz.introtooralanguage.data.models.HouseWordsModel
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel
import kotlinx.android.synthetic.main.ora_num.view.*


class HouseWordsAdapter  (val listener: RowClickListener)  :ListAdapter<HouseWordsModel, HouseWordsAdapter.OraNumViewHolder>(HouseWordsDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  OraNumViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.ora_num1, parent, false)
        return OraNumViewHolder(view, listener)

    }

    override fun onBindViewHolder(holder: OraNumViewHolder, position: Int) {

//        if ( holder.position < 28){
//
//            holder.itemView.edit_oraword.visibility = View.GONE
//            holder.itemView.delete_oraword.visibility = View.GONE
//        }else{
//            holder.itemView.edit_oraword.visibility = View.VISIBLE
//            holder.itemView.delete_oraword.visibility = View.VISIBLE
//        }

//        oraWords.numIcon?.let { Picasso.get().load(it).into(itemView.ora_numIcon) }

        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(getItem(position))
            listener.onPlayOraWordClickListener(getItem(position))
            listener.onEditOraWordClickListener(getItem(position))
            listener.onDeleteOraWordClickListener(getItem(position))
        }

    }

    class OraNumViewHolder(view: View, val listener: RowClickListener): RecyclerView.ViewHolder(view){

        val play = itemView.findViewById<ImageView>(R.id.play_oraword)

        fun bind(houseWords: HouseWordsModel){

            itemView.eng_num.text = houseWords.engNum
            itemView.ora_num.text = houseWords.oraNum


            itemView.play_oraword.setOnClickListener {
                    listener.onPlayOraWordClickListener(houseWords)
            }

            itemView.edit_oraword.setOnClickListener {
//                if(position> 27){
                    listener.onEditOraWordClickListener(houseWords)
//                }
//                showSnackbar(itemView.eng_num, "You can't edit pre-installed OraWords")


            }

            itemView.delete_oraword.setOnClickListener {
//                if(position> 27){
                    listener.onDeleteOraWordClickListener(houseWords)
//                }
//                showSnackbar(itemView.eng_num, "You can't delete pre-installed OraWords")

            }
        }

    }

    interface RowClickListener{
        fun onPlayOraWordClickListener(houseWords: HouseWordsModel)
        fun onEditOraWordClickListener(houseWords: HouseWordsModel)
        fun onDeleteOraWordClickListener(houseWords: HouseWordsModel)
        fun onItemClickListener(houseWords: HouseWordsModel)

    }

}