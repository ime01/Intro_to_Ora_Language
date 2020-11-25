package com.flowz.introtooralanguage

import com.flowz.introtooralanguage.utils.RegistrationUtil
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty username returns false` (){

        val result = RegistrationUtil.validateRegistrationInput("", "123", "123")

        assertThat(result).isFalse()

    }

    @Test
    fun `valid username and correctly repeated password returns true` (){

        val result = RegistrationUtil.validateRegistrationInput("Tony", "123", "123")

        assertThat(result).isTrue()

    }

    @Test
    fun `username already exists returns false` (){

        val result = RegistrationUtil.validateRegistrationInput("Mark", "123", "123")

        assertThat(result).isFalse()

    }

    @Test
    fun `empty password returns false` (){

        val result = RegistrationUtil.validateRegistrationInput("John", "", "")

        assertThat(result).isFalse()

    }

    @Test
    fun `password and confirmPassword do not match returns false` (){

        val result = RegistrationUtil.validateRegistrationInput("jake", "123", "120")


        assertThat(result).isFalse()

    }

    @Test
    fun `password with less than 2 digits returns false` (){

        val result = RegistrationUtil.validateRegistrationInput("hank", "how6", "house3")

        assertThat(result).isFalse()

    }
}