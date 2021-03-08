package com.flowz.introtooralanguage.display.numbers

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flowz.introtooralanguage.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OraLangNumbersFragmentTest {

    @Before
    fun setUp() {

//        launchFragmentInContainer<OraLangNumbersFragment>(fragmentArgs = null, factory = null)
    }


    //    val scenario =
    @Test
    fun confirmViews() {

        Espresso.onView(withId(R.id.ora_num_recycler))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }
}