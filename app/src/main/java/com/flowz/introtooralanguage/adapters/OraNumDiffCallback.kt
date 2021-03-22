package com.flowz.introtooralanguage.adapters

import androidx.recyclerview.widget.DiffUtil
import com.flowz.introtooralanguage.data.models.HouseWordsModel
import com.flowz.introtooralanguage.data.models.NumbersModel
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel
import com.flowz.introtooralanguage.data.models.TravelWordsModel

class OraNumDiffCallback : DiffUtil.ItemCallback<NumbersModel>(){
    override fun areItemsTheSame(oldItem: NumbersModel, newItem: NumbersModel): Boolean {
        return oldItem.oraid == newItem.oraid
    }
    override fun areContentsTheSame(oldItem: NumbersModel, newItem: NumbersModel): Boolean {
        return oldItem == newItem
    }

}

class OutdoorWordsDiffCallback : DiffUtil.ItemCallback<OutdoorWordsModel>(){
    override fun areItemsTheSame(oldItem: OutdoorWordsModel, newItem: OutdoorWordsModel): Boolean {
        return oldItem.oraid == newItem.oraid
    }
    override fun areContentsTheSame(oldItem: OutdoorWordsModel, newItem: OutdoorWordsModel): Boolean {
        return oldItem == newItem
    }

}

class TravelWordsDiffCallback : DiffUtil.ItemCallback<TravelWordsModel>(){
    override fun areItemsTheSame(oldItem: TravelWordsModel, newItem: TravelWordsModel): Boolean {
        return oldItem.oraid == newItem.oraid
    }
    override fun areContentsTheSame(oldItem: TravelWordsModel, newItem: TravelWordsModel): Boolean {
        return oldItem == newItem
    }

}


class HouseWordsDiffCallback : DiffUtil.ItemCallback<HouseWordsModel>(){
    override fun areItemsTheSame(oldItem: HouseWordsModel, newItem: HouseWordsModel): Boolean {
        return oldItem.oraid == newItem.oraid
    }
    override fun areContentsTheSame(oldItem: HouseWordsModel, newItem: HouseWordsModel): Boolean {
        return oldItem == newItem
    }

}