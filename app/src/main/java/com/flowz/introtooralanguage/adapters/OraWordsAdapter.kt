package com.flowz.introtooralanguage.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.data.OraLangNums
import androidx.recyclerview.widget.ListAdapter
import com.flowz.introtooralanguage.extensions.showSnackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.ora_num.view.delete_oraword
import kotlinx.android.synthetic.main.ora_num.view.edit_oraword
import kotlinx.android.synthetic.main.ora_num.view.eng_num
import kotlinx.android.synthetic.main.ora_num.view.ora_num
import kotlinx.android.synthetic.main.ora_num.view.play_oraword
import kotlinx.android.synthetic.main.ora_num1.view.*


class OraWordsAdapter  (val listener: RowClickListener)  :ListAdapter<OraLangNums, OraWordsAdapter.OraNumViewHolder>(OraNumDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  OraNumViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.ora_num1, parent, false)
        return OraNumViewHolder(view, listener)

    }

    override fun onBindViewHolder(holder: OraNumViewHolder, position: Int) {

        val oraPosition = getItemId(position)

//        if (oraPosition.toInt() <11 ){
            holder.itemView.edit_oraword.isClickable = false
            holder.itemView.edit_oraword.isEnabled = false
            holder.itemView.delete_oraword.isClickable= false
            holder.itemView.delete_oraword.isEnabled = false

//        }else{
////            holder.itemView.edit_oraword.visibility = View.VISIBLE
////            holder.itemView.delete_oraword.visibility = View.VISIBLE
//            holder.itemView.edit_oraword.isClickable= false
//            holder.itemView.delete_oraword.isClickable = false

//                showSnackbar(holder.itemView.eng_num, "You can't edit pre-installed OraWords")
//        }

//        if (oraPosition.toInt() > 11 ){
//
//        }else{
//            showSnackbar(holder.itemView.eng_num, "You can't delete pre-installed OraWords")
//        }


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

            val position = adapterPosition

            itemView.edit_oraword.setOnClickListener {
//                if(position>11 ){
                    listener.onEditOraWordClickListener(oraWords)
//                }else{
//                    showSnackbar(itemView.eng_num, "You can't edit pre-installed OraWords")
//                }

            }

            itemView.delete_oraword.setOnClickListener {
//                if(position> 11){
                    listener.onDeleteOraWordClickListener(oraWords)
//                }
//                else{
//                    showSnackbar(itemView.eng_num, "You can't delete pre-installed OraWords")
//                }



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