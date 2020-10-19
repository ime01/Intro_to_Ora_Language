package com.flowz.introtooralanguage.display


import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.adapters.OraNumAdapter
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.recyclerviewlistener.RecyclerItemClickListener
import kotlinx.android.synthetic.main.fragment_ora_lang_house.*
import kotlinx.android.synthetic.main.fragment_ora_lang_outdoor.*
import kotlinx.android.synthetic.main.ora_lang_numbers.*

/**
 * A simple [Fragment] subclass.
 */
class OraLangOutdoorFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ora_lang_outdoor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var mediaPlayer: MediaPlayer? = null
        val OutdoorList : ArrayList<OraLangNums> = ArrayList()
//
        OutdoorList.add(0, OraLangNums("I am going to the market", "Ilo deh vbee kin", R.drawable.ic_launcher_background) )
        OutdoorList.add(1, OraLangNums("How much is this item", "Ekah ighor na", R.drawable.ic_launcher_background) )
        OutdoorList.add(2, OraLangNums("I will pay for this", "Mio hoosa ghor ona", R.drawable.ic_launcher_background) )
        OutdoorList.add(3, OraLangNums("I am going out", "Ilo deh vbooee", R.drawable.ic_launcher_background) )
        OutdoorList.add(4, OraLangNums("walk fast", "Tuah shaan", R.drawable.ic_launcher_background) )
        OutdoorList.add(5, OraLangNums("put the item down", "Meo onee mi eeh wo otoi", R.drawable.ic_launcher_background) )
        OutdoorList.add(6, OraLangNums("Put on your shoes", "Whei ebata weh", R.drawable.ic_launcher_background) )
        OutdoorList.add(7, OraLangNums("You can play here", "Khi een maan", R.drawable.ic_launcher_background) )
        OutdoorList.add(8, OraLangNums("I'll wait for you here", " Meo muze khe maan", R.drawable.ic_launcher_background) )
        OutdoorList.add(9, OraLangNums("How are you", "Or nhe gbe", R.drawable.ic_launcher_background) )
        OutdoorList.add(10, OraLangNums("I am fine", "Orfure", R.drawable.ic_launcher_background) )

        ora_outdoor_recycler.layoutManager = LinearLayoutManager(this.context)

        ora_outdoor_recycler.adapter = OraNumAdapter(this.requireContext(), OutdoorList)

        ora_outdoor_recycler.addOnItemTouchListener(
            RecyclerItemClickListener(this.requireContext(),ora_outdoor_recycler, object : RecyclerItemClickListener.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {

                    val soundPlayed = OutdoorList.get(position)

                    if (mediaPlayer == null) {

                        mediaPlayer = MediaPlayer.create(context, soundPlayed.numIcon!!)
                        mediaPlayer?.start()
                    } else {
                        mediaPlayer?.stop()
                        mediaPlayer = MediaPlayer.create(context, soundPlayed.numIcon!!)
                        mediaPlayer?.start()
                    }
                }

                override fun onItemLongClick(view: View?, position: Int) {

                }

            })
        )


    }
}
