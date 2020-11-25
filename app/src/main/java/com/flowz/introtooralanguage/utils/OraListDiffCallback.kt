package com.flowz.introtooralanguage.utils

import androidx.recyclerview.widget.DiffUtil
import com.flowz.introtooralanguage.data.OraLangNums

class OraListDiffCallback(private val oldList: List<OraLangNums>, private val newList: List<OraLangNums> ) : DiffUtil.Callback() {


    override fun getOldListSize(): Int  = oldList.size


    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return oldList[oldItemPosition].oraid == newList.get(newItemPosition).oraid
    }


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        val oldEnglishWord = oldList[oldItemPosition].engNum
        val newEnglishWord = newList[newItemPosition].engNum

        val oldOraWord = oldList[oldItemPosition].oraNum
        val newOraWord = newList[newItemPosition].oraNum

        return oldEnglishWord == newEnglishWord && oldOraWord==newOraWord
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}