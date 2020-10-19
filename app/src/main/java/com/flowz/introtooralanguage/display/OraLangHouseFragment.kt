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
import kotlinx.android.synthetic.main.fragment_ora_lang_travel.*
import kotlinx.android.synthetic.main.ora_lang_numbers.*

/**
 * A simple [Fragment] subclass.
 */
class OraLangHouseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ora_lang_house, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var mediaPlayer: MediaPlayer? = null
        val houseList : ArrayList<OraLangNums> = ArrayList()

        houseList.add(0, OraLangNums("Welcome", "O bo khian", R.drawable.ic_launcher_background) )
        houseList.add(1, OraLangNums("Switch it on", "Ror ee ruu", R.drawable.ic_launcher_background) )
        houseList.add(2, OraLangNums("Put it down", "Muii khi vbo otoee", R.drawable.ic_launcher_background) )
        houseList.add(3, OraLangNums("Open the door", "Thu khu khe dee aah", R.drawable.ic_launcher_background) )
        houseList.add(4, OraLangNums("Close the door", "Ghu khu khe dee aah", R.drawable.ic_launcher_background) )
        houseList.add(5, OraLangNums("Go do the dishes", "Oho doh kpee ta saa", R.drawable.ic_launcher_background) )
        houseList.add(6, OraLangNums("Sit Down", "Dee gha vbo toie", R.drawable.ic_launcher_background) )
        houseList.add(7, OraLangNums("Stand up", "Kparhe muze", R.drawable.ic_launcher_background) )
        houseList.add(8, OraLangNums("Come and eat", " Vie  ebaee", R.drawable.ic_launcher_background) )
        houseList.add(9, OraLangNums("Be carefull", "Fuen gbee rhe", R.drawable.ic_launcher_background) )
        houseList.add(10, OraLangNums("Go buy a loaf of bread", "Olo odor doo okoh oibo", R.drawable.ic_launcher_background) )


        ora_house_recycler.layoutManager = LinearLayoutManager(this.context)

        ora_house_recycler.adapter = OraNumAdapter(this.requireContext(), houseList)

        ora_house_recycler.addOnItemTouchListener(
            RecyclerItemClickListener(this.requireContext(),  ora_house_recycler, object : RecyclerItemClickListener.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {

                    val soundPlayed = houseList.get(position)

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
