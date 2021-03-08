package com.flowz.introtooralanguage.firebase

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.flowz.introtooralanguage.R
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(AndroidJUnit4::class)
class SignupActivityTest{


@get:Rule
val activityRule = ActivityScenarioRule(SignupActivity::class.java)

    @Test
    fun testViews() {
        onView(withId(R.id.rg_user_email)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.rg_user_name)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.rg_user_password)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.user_phone_number)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.rg_user_confirm_password)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.register_user)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.account_holder)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.textView2)).check(ViewAssertions.matches(isDisplayed()))

    }
}