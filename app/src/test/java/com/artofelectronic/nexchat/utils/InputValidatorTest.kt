package com.artofelectronic.nexchat.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test


class InputValidatorTest {

    @Test
    fun `validateEmail with empty input return error message`() {
        val expected = "Email is required"
        val actual = InputValidator.validateEmail("")

        assertEquals(expected, actual)
    }

    @Test
    fun `validateEmail with wrong email return error message`() {
        val expected = "Invalid email format"
        val actual = InputValidator.validateEmail(INVALID_EMAIL)

        assertEquals(expected, actual)
    }

    @Test
    fun `validateEmail with correct email return null`() {
        val expected = null
        val actual = InputValidator.validateEmail(VALID_EMAIL)

        assertEquals(expected, actual)
    }


    @Test
    fun `validatePassword with empty input return error message`() {
        val expected = "Password is required"
        val actual = InputValidator.validatePassword("")

        assertEquals(expected, actual)
    }

    @Test
    fun `validatePassword with wrong email return error message`() {
        val expected = "Weak password (min 8 chars, 1 upper, 1 digit, 1 special)"
        val actual = InputValidator.validatePassword(INVALID_PASSWORD)

        assertEquals(expected, actual)
    }

    @Test
    fun `validatePassword with correct email return null`() {
        val expected = null
        val actual = InputValidator.validatePassword(VALID_PASSWORD)

        assertEquals(expected, actual)
    }

}