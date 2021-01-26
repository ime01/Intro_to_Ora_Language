package com.flowz.introtooralanguage.adapters

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.data.room.OraWordsDatabase
import com.flowz.introtooralanguage.utils.OraListDiffCallback
import com.flowz.introtooralanguage.display.numbers.OraLangNumbersFragmentDirections
import com.flowz.introtooralanguage.extensions.showSnackbar
import androidx.recyclerview.widget.ListAdapter
import com.flowz.introtooralanguage.extensions.playContentUri
import jp.wasabeef.recyclerview.internal.ViewHelper
import kotlinx.android.synthetic.main.ora_num.view.*
import kotlinx.android.synthetic.main.ora_num.view.eng_num
import kotlinx.android.synthetic.main.ora_num.view.ora_num
import kotlinx.android.synthetic.main.ora_num3.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


class OraNumbersAdapter1  (val listener: RowClickListener)  :ListAdapter<OraLangNums, OraNumbersAdapter1.OraNumViewHolder>(OraNumDiffCallback()) {

    var mediaPlayer: MediaPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  OraNumViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.ora_num3, parent, false)
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

        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(getItem(position))
            listener.onPlayOraWordClickListener(getItem(position))
            listener.onEditOraWordClickListener(getItem(position))
            listener.onDeleteOraWordClickListener(getItem(position))
        }

//        holder.oraMenu?.setOnClickListener {
//
//            val popMenu = PopupMenu(context, holder.oraMenu)
//            popMenu.menuInflater.inflate(R.menu.ora_words_options_menu, popMenu.menu)
//            popMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
//                override fun onMenuItemClick(item: MenuItem?): Boolean {
//                    when(item!!.itemId){
//                        R.id.edit->{
//                            showSnackbar(holder.oraMenu,"Edit Clicked")
//                            if(position <= 27){
//                                showSnackbar(holder.oraMenu, "You can't edit preinstalled OraWords")
//                            }
//                            else{
////                                val bundle = Bundle()
////                                bundle.putString("engWord", oraLangNumList[position].engNum)
////                                bundle.putString("oraWord", oraLangNumList[position].oraNum)
////                                bundle.putInt("position", oraLangNumList[position].oraid)
////                                val navController : NavController = Navigation.findNavController(holder.itemView)
////                                navController.navigate(R.id.editOraWordFragment, bundle)
//
//                                val action = OraLangNumbersFragmentDirections.actionOraLangNumbersFragmentToEditOraWordFragment()
//                                action.oraLangNums = oraLangNumList[position]
//                                Navigation.findNavController(holder.itemView).navigate(action)
//
//                            }
//
//                        }
//                        R.id.delete->{
//
//                            if(position <= 27){
//                                showSnackbar(holder.oraMenu, "You can't delete preinstalled OraWords")
//                            }
//                            else{
//
//                                GlobalScope.launch {
//
////                                    oraDb.oraWordsDao().delete(oraLangNumList[position])
//                                    deleteOraNumber(oraLangNumList[position])
//
//                                }
//
//                            }
//                        }
//                    }
//                    return true
//                }
//
//            })
//            popMenu.show()
//
//        }
//
//        holder.play?.setOnClickListener {
//
//            val animation = AnimationUtils.loadAnimation(context, R.anim.play_icon_blink)
//            holder.play.startAnimation(animation)
//
//            if(position<=27){
//
//                val  sound = oraLangNumList[position].numIcon.toString()
//                val  played = Uri.parse(sound)
//
//                playContentInt(oraLangNumList[position].numIcon!!)
//
//            }else{
//
//                val played1 = oraLangNumList[position].recordedAudio
//                playContentUri(played1!!)
//            }
//
//            showSnackbar(holder.play, "Ora Word Clicked")
//        }

    }



    class OraNumViewHolder(view: View, val listener: RowClickListener): RecyclerView.ViewHolder(view){

        val oraMenu = itemView.findViewById<TextView?>(R.id.ora_options_menu)
        val play = itemView.findViewById<ImageView>(R.id.play)

        fun bind(oraWords: OraLangNums){

            itemView.eng_num.text = oraWords.engNum
            itemView.ora_num.text = oraWords.oraNum



//            Picasso.get().load(oraWords.numIcon!!).into(itemView.ora_numIcon)
            itemView.play_oraword.setOnClickListener {
                listener.onPlayOraWordClickListener(oraWords)

            }

            itemView.edit_oraword.setOnClickListener {
                listener.onEditOraWordClickListener(oraWords)

            }

            itemView.delete_oraword.setOnClickListener {
                listener.onDeleteOraWordClickListener(oraWords)

            }
        }

    }

    interface RowClickListener{
        fun onPlayOraWordClickListener(oraWords: OraLangNums)
        fun onEditOraWordClickListener(oraWords: OraLangNums)
        fun onDeleteOraWordClickListener(oraWords: OraLangNums)
        fun onItemClickListener(oraWords: OraLangNums)

    }

//    fun playContentUri(uri: Uri) {
////        val mMediaPlayer = MediaPlayer().apply {
//
//        if (mediaPlayer != null) {
//
//            mediaPlayer?.stop()
//
//            try {
//                mediaPlayer = MediaPlayer().apply {
//                    setDataSource(context!!, uri)
//                    setAudioAttributes(
//                        AudioAttributes.Builder()
//                            .setUsage(AudioAttributes.USAGE_MEDIA)
//                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                            .build()
//                    )
//                    prepare()
//                }
//                mediaPlayer?.start()
//            } catch (e: IOException) {
//
//                mediaPlayer = null
//                mediaPlayer?.release()
//            }
//        } else {
//
//            try {
//                mediaPlayer = MediaPlayer().apply {
//
//                    setDataSource(context!!, uri)
//
//                    setAudioAttributes(
//                        AudioAttributes.Builder()
//                            .setUsage(AudioAttributes.USAGE_MEDIA)
//                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                            .build()
//                    )
//                    prepare()
//                }
//                mediaPlayer?.start()
//            } catch (e: IOException) {
//
//                mediaPlayer = null
//                mediaPlayer?.release()
//            }
//
//        }
//    }
//
//
//    fun playContentInt(int: Int) {
//
//        if (mediaPlayer == null) {
//                                mediaPlayer = MediaPlayer.create(context, int)
//                                mediaPlayer?.start()
//
//                            } else {
//                                mediaPlayer?.stop()
//                                mediaPlayer = MediaPlayer.create(context, int)
//                                mediaPlayer?.start()
//                            }
//
//    }
//    fun addOraNumber(oraWord: OraLangNums){
////        oraLangNumList.add(oraWord)
////        oraDb.oraWordsDao().insert(oraWord)
////        notifyItemInserted(oraLangNumList.size-1)
//        if (!oraLangNumList.contains(oraWord)) {
//            oraLangNumList.add(oraWord)
////            notifyItemInserted(oraLangNumList.size)
//        }
//
//    }

//    fun updateEditedOraItem(oraWord: OraLangNums){
////        deleteOraNumber(oraLangNumList[position])
//        oraDb.oraWordsDao().update(oraWord)
//        notifyDataSetChanged()
//    }

//    fun addOraNumberList(oraWords: List<OraLangNums>){
////        oraLangNumList.clear()
////        oraLangNumList.addAll(oraWords)
//////        oraLangNumList += oraWords
////        notifyDataSetChanged()
//        if(!oraLangNumList.containsAll(oraWords)){
//            oraLangNumList.addAll(oraWords)
////            notifyItemInserted(oraLangNumList.size)
//
//        }
//    }

//    fun upDateData(newList: List<OraLangNums>){
//
//        val diffCallback =
//            OraListDiffCallback(
//                oraLangNumList,
//                newList
//            )
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
////        oraLangNumList.clear()
//        oraLangNumList.addAll(newList)
//        diffResult.dispatchUpdatesTo(this)
//
//    }


//    fun deleteOraNumber( oraWord: OraLangNums){
//        oraDb.oraWordsDao().delete(oraWord)
//
//    }



}