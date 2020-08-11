package com.flowz.introtooralanguage.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.data.OraLangNums
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.ora_num.view.*
import javax.xml.transform.Templates

class OraNumAdapter  (private val oraLangNumList: ArrayList<OraLangNums>)  : RecyclerView.Adapter<OraNumAdapter.OraNumViewHolder> () {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OraNumViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.ora_num, parent, false)
        return OraNumViewHolder(view)

    }

//    override fun getItemCount(): Int {
//        return oraLangNumList.size
//    }

    override fun getItemCount(): Int = oraLangNumList.size


    override fun onBindViewHolder(holder: OraNumViewHolder, position: Int) {

        holder.bind(oraLangNumList[position])
    }


    class OraNumViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bind(oraWords: OraLangNums){

            itemView.eng_num.text = oraWords.engNum
            itemView.ora_num.text = oraWords.oraNum

//            Picasso.get().load(oraWords.numIcon!!).into(itemView.ora_numIcon)

        }

    }

    fun addOraNumber(oraWord: OraLangNums){
        oraLangNumList.add(oraWord)
        notifyItemInserted(oraLangNumList.size-1)
    }



}