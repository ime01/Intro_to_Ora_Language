package com.flowz.introtooralanguage.adapters

import androidx.recyclerview.widget.DiffUtil
import com.flowz.introtooralanguage.data.OraLangNums

class OraNumDiffCallback : DiffUtil.ItemCallback<OraLangNums>(){


    override fun areItemsTheSame(oldItem: OraLangNums, newItem: OraLangNums): Boolean {
        return oldItem.oraid == newItem.oraid
    }

    override fun areContentsTheSame(oldItem: OraLangNums, newItem: OraLangNums): Boolean {
        return oldItem == newItem
    }

}