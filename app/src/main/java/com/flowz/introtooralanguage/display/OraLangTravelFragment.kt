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
class OraLangTravelFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ora_lang_travel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var mediaPlayer: MediaPlayer? = null
        val travelList : ArrayList<OraLangNums> = ArrayList()

        travelList.add(0, OraLangNums("We'll stop for a break here", "Mi ma muze fietian waun", R.drawable.ic_launcher_background) )
        travelList.add(1, OraLangNums(" Shortly we'll be back on our journey", "Mi ma gbe gbe bee vbi o shaan", R.drawable.ic_launcher_background) )
        travelList.add(2, OraLangNums("The light says stop ", "Uru okpa owee nu muze", R.drawable.ic_launcher_background) )
        travelList.add(3, OraLangNums("Here are my papers ", "Ka ough kpebe men", R.drawable.ic_launcher_background) )
        travelList.add(4, OraLangNums("You can go", "Kha Shaan", R.drawable.ic_launcher_background) )
        travelList.add(5, OraLangNums("We've on the road for some time", "Or khuiee nii mai da rii vbi ukpodee", R.drawable.ic_launcher_background) )
        travelList.add(6, OraLangNums("Sit in the front seat", "Dey gha vbi odaoo", R.drawable.ic_launcher_background) )
        travelList.add(7, OraLangNums("Sit in the back seat", "Dey gha vbi Ehimin", R.drawable.ic_launcher_background) )
        travelList.add(8, OraLangNums("Stop here", "Muze ma ann", R.drawable.ic_launcher_background) )
        travelList.add(9, OraLangNums("We will get down here", "Mi ma do otoi maan", R.drawable.ic_launcher_background) )
        travelList.add(10, OraLangNums("Thanks for taking me", "Uzor kah nu dah mu mee", R.drawable.ic_launcher_background) )

        ora_travel_recycler.layoutManager = LinearLayoutManager(this.context)

        ora_travel_recycler.adapter = OraNumAdapter(this.requireContext(), travelList)

        ora_travel_recycler.addOnItemTouchListener(
            RecyclerItemClickListener(this.requireContext(), ora_travel_recycler, object : RecyclerItemClickListener.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {

                    val soundPlayed = travelList.get(position)

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
