package com.flowz.introtooralanguage.data.room

import android.content.Context
import android.net.Uri
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.data.models.HouseWordsModel
import com.flowz.introtooralanguage.data.models.NumbersModel
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel
import com.flowz.introtooralanguage.data.models.TravelWordsModel

class DefaultDataSource {

    fun ListOfNumbers(context: Context): List<NumbersModel> {
        val numList: ArrayList<NumbersModel> = ArrayList()

        numList.add(
            0,
            NumbersModel(
                "One",
                "Okpa",
                R.drawable.ione,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        numList.add(
            1,
            NumbersModel(
                "Two",
                "Evah",
                R.drawable.itwo,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/two")
            )
        )
        numList.add(
            2,
            NumbersModel(
                "Three",
                "Eha",
                R.drawable.ithree,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/three")
            )
        )
        numList.add(
            3,
            NumbersModel(
                "Four",
                "Enee",
                R.drawable.ifour,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/four")
            )
        )
        numList.add(
            4,
            NumbersModel(
                "Five",
                "Iheen",
                R.drawable.ifive,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/five")
            )
        )
        numList.add(
            5,
            NumbersModel(
                "Six",
                "Ekhan",
                R.drawable.i6,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/six")
            )
        )
        numList.add(
            6,
            NumbersModel(
                "Seven",
                "Ikhion",
                R.drawable.i7,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/seven")
            )
        )
        numList.add(
            7,
            NumbersModel(
                "Eight",
                "Een",
                R.drawable.ieight,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/eight")
            )
        )
        numList.add(
            8,
            NumbersModel(
                "Nine",
                "Isiin",
                R.drawable.inine,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/nine")
            )
        )
        numList.add(
            9,
            NumbersModel(
                "Ten",
                "Igbee",
                R.drawable.i10,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/ten")
            )
        )
        numList.add(
            10,
            NumbersModel(
                "Eleven",
                "Ugbour",
                R.drawable.i11,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/eleven")
            )
        )
        numList.add(
            11,
            NumbersModel(
                "Twelve",
                "Igbe-vah",
                R.drawable.i12,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/twelve")
            )
        )
        numList.add(
            12,
            NumbersModel(
                "Thirteen",
                "Igbe-eha",
                R.drawable.i13a,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/thirteen")
            )
        )
        numList.add(
            13,
            NumbersModel(
                "Fourteen",
                "Igbe-Enee",
                R.drawable.i14,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/fourteen")
            )
        )
        numList.add(
            14,
            NumbersModel(
                "Fifteen",
                "Igbe-Iheen",
                R.drawable.i15,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/fifteen")
            )
        )
        numList.add(
            15,
            NumbersModel(
                "Sixteen",
                "Ke-enee-Suuee",
                R.drawable.i16,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/sixteen")
            )
        )
        numList.add(
            16,
            NumbersModel(
                "Seventeen",
                "Ke-eha-Suuee",
                R.drawable.i17,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/seventeen")
            )
        )
        numList.add(
            17,
            NumbersModel(
                "Eighteen",
                "Ke-evah-Suuee",
                R.drawable.i18,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/eighteen")
            )
        )
        numList.add(
            18,
            NumbersModel(
                "Nineteen",
                "Ke-okpa-Suuee",
                R.drawable.i19,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/nineteen")
            )
        )
        numList.add(
            19,
            NumbersModel(
                "Twenty",
                "Uuee",
                R.drawable.i20,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/twenty")
            )
        )
        numList.add(
            20,
            NumbersModel(
                "Thirty",
                "Ogban",
                R.drawable.i30,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/thirty")
            )
        )
        numList.add(
            21,
            NumbersModel(
                "Fourty",
                "Egbo-evah",
                R.drawable.i40,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/fourty")
            )
        )
        numList.add(
            22,
            NumbersModel(
                "Fifty",
                "Egbo-evah-bi-igbe",
                R.drawable.i50,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/fifty")
            )
        )
        numList.add(
            23,
            NumbersModel(
                "Sixty",
                "Egbo-eha",
                R.drawable.i60,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/sixty")
            )
        )
        numList.add(
            24,
            NumbersModel(
                "Seventy",
                "Egbo-eha-bi-igbe",
                R.drawable.i70,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/seventy")
            )
        )
        numList.add(
            25,
            NumbersModel(
                "Eighty",
                "Egbo-enee",
                R.drawable.i80,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/eighty")
            )
        )
        numList.add(
            26,
            NumbersModel(
                "Ninety",
                "Egbo-enee-bi-igbe",
                R.drawable.i90,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/ninety")
            )
        )
        numList.add(
            27,
            NumbersModel(
                "One Hundred",
                "Egbo-eheen",
                R.drawable.i100,
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/hundred")
            )
        )

        return numList

    }


    fun ListOfTravelWords(context: Context): List<TravelWordsModel> {


        val travelList: ArrayList<TravelWordsModel> = ArrayList()

        travelList.add(
            0,
            TravelWordsModel(
                "We'll stop for a break here",
                "Mi ma muze fietian waun",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        travelList.add(
            1,
            TravelWordsModel(
                " Shortly we'll be back on our journey",
                "Mi ma gbe gbe bee vbi o shaan",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        travelList.add(
            2,
            TravelWordsModel(
                "The light says stop ",
                "Uru okpa owee nu muze",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        travelList.add(
            3,
            TravelWordsModel(
                "Here are my papers ",
                "Ka ough kpebe men",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        travelList.add(
            4,
            TravelWordsModel(
                "You can go",
                "Kha Shaan",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        travelList.add(
            5,
            TravelWordsModel(
                "We've on the road for some time",
                "Or khuiee nii mai da rii vbi ukpodee",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        travelList.add(
            6,
            TravelWordsModel(
                "Sit in the front seat",
                "Dey gha vbi odaoo",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        travelList.add(
            7,
            TravelWordsModel(
                "Sit in the back seat",
                "Dey gha vbi Ehimin",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        travelList.add(
            8,
            TravelWordsModel(
                "Stop here",
                "Muze ma ann",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        travelList.add(
            9,
            TravelWordsModel(
                "We will get down here",
                "Mi ma do otoi maan",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        travelList.add(
            10,
            TravelWordsModel(
                "Thanks for taking me",
                "Uzor kah nu dah mu mee",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )


        return travelList

    }


    fun ListOfHouseWords(app: Context): List<HouseWordsModel>{

        val houseList: ArrayList<HouseWordsModel> = ArrayList()

        houseList.add(0, HouseWordsModel("Welcome", "O bo khian", 0, Uri.parse("android.resource://"+app?.packageName  +"/raw/one")))
        houseList.add(1, HouseWordsModel("Switch it on", "Ror ee ruu", 0, Uri.parse("android.resource://"+app?.packageName  +"/raw/one")))
        houseList.add(2, HouseWordsModel("Put it down", "Muii khi vbo otoee", 0, Uri.parse("android.resource://"+app?.packageName  +"/raw/one")))
        houseList.add(3, HouseWordsModel("Open the door", "Thu khu khe dee aah", 0, Uri.parse("android.resource://"+app?.packageName  +"/raw/one")))
        houseList.add(4, HouseWordsModel("Close the door", "Ghu khu khe dee aah", 0, Uri.parse("android.resource://"+app?.packageName  +"/raw/one")))
        houseList.add(5, HouseWordsModel("Go do the dishes", "Oho doh kpee ta saa", 0, Uri.parse("android.resource://"+app?.packageName  +"/raw/one")))
        houseList.add(6, HouseWordsModel("Sit Down", "Dee gha vbo toie", 0, Uri.parse("android.resource://"+app?.packageName  +"/raw/one")))
        houseList.add(7, HouseWordsModel("Stand up", "Kparhe muze",0, Uri.parse("android.resource://"+app?.packageName  +"/raw/one")))
        houseList.add(8, HouseWordsModel("Come and eat", " Vie  ebaee", 0, Uri.parse("android.resource://"+app?.packageName  +"/raw/one")))
        houseList.add(9, HouseWordsModel("Be carefull", "Fuen gbee rhe", 0, Uri.parse("android.resource://"+app?.packageName  +"/raw/one")))
        houseList.add(10, HouseWordsModel("Go buy a loaf of bread", "Olo odor doo okoh oibo", 0, Uri.parse("android.resource://"+app?.packageName  +"/raw/one")))

        return houseList
    }

    fun ListOfOudoorWords(context: Context): List<OutdoorWordsModel>{

        val outdoorList: ArrayList<OutdoorWordsModel> = ArrayList()
//
        outdoorList.add(
            0,
            OutdoorWordsModel(
                "I am going to the market",
                "Ilo deh vbee kin",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        outdoorList.add(
            1,
            OutdoorWordsModel(
                "How much is this item",
                "Ekah ighor na",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        outdoorList.add(
            2,
            OutdoorWordsModel(
                "I will pay for this",
                "Mio hoosa ghor ona",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        outdoorList.add(
            3,
            OutdoorWordsModel(
                "I am going out",
                "Ilo deh vbooee",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        outdoorList.add(
            4,
            OutdoorWordsModel(
                "walk fast",
                "Tuah shaan",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        outdoorList.add(
            5,
            OutdoorWordsModel(
                "put the item down",
                "Meo onee mi eeh wo otoi",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        outdoorList.add(
            6,
            OutdoorWordsModel(
                "Put on your shoes",
                "Whei ebata weh",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        outdoorList.add(
            7,
            OutdoorWordsModel(
                "You can play here",
                "Khi een maan",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        outdoorList.add(
            8,
            OutdoorWordsModel(
                "I'll wait for you here",
                " Meo muze khe maan",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        outdoorList.add(
            9,
            OutdoorWordsModel(
                "How are you",
                "Or nhe gbe",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        outdoorList.add(
            10,
            OutdoorWordsModel(
                "I am fine",
                "Orfure",
                0,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )

        return outdoorList

    }

}