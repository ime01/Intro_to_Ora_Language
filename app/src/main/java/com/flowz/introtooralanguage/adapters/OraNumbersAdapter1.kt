package com.flowz.introtooralanguage.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.data.OraLangNums
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.ora_num.view.*


class OraNumbersAdapter1  (val listener: RowClickListener)  :ListAdapter<OraLangNums, OraNumbersAdapter1.OraNumViewHolder>(OraNumDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  OraNumViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.ora_num, parent, false)
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
        getItem(position).numIcon?.let { Picasso.get().load(it).placeholder(R.drawable.itwo).error(R.drawable.itwo) .into(holder.itemView.ora_numIcon) }

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

        fun bind(oraWords: OraLangNums){

            itemView.eng_num.text = oraWords.engNum
            itemView.ora_num.text = oraWords.oraNum


            itemView.play_oraword.setOnClickListener {
                    listener.onPlayOraWordClickListener(oraWords)
            }

            itemView.edit_oraword.setOnClickListener {
//                if(position> 27){
                    listener.onEditOraWordClickListener(oraWords)
//                }
//                showSnackbar(itemView.eng_num, "You can't edit pre-installed OraWords")


            }

            itemView.delete_oraword.setOnClickListener {
//                if(position> 27){
                    listener.onDeleteOraWordClickListener(oraWords)
//                }
//                showSnackbar(itemView.eng_num, "You can't delete pre-installed OraWords")


            }
        }

    }

    interface RowClickListener{
        fun onPlayOraWordClickListener(oraWords: OraLangNums)
        fun onEditOraWordClickListener(oraWords: OraLangNums)
        fun onDeleteOraWordClickListener(oraWords: OraLangNums)
        fun onItemClickListener(oraWords: OraLangNums)

    }

}