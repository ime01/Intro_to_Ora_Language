package com.flowz.introtooralanguage.display


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.flowz.introtooralanguage.R
import kotlinx.android.synthetic.main.fragment_add_words.*

/**
 * A simple [Fragment] subclass.
 */
class AddWordsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_words, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val engWordEntered: String
        val oraWordEntered: String

        if (eng_entered_words!= null && ora_entered_words != null){

            engWordEntered = eng_entered_words.text.toString()
            oraWordEntered = ora_entered_words.text.toString()
        }else{
            Toast.makeText(this.context, "Ensure you have entered the English and Ora Words", Toast.LENGTH_LONG).show()
        }











    }
}
