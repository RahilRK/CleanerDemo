package com.appname.structure.user.validation

import com.base.hilt.utils.Validation
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
/**
 * Unit tests for the [Validation].
 */
@RunWith(JUnit4::class)
class ValidationUtilTest {

    @get:Rule
    var globalTimeout: Timeout = Timeout.seconds(50) // n seconds max per method tested

    @Test
    @Ignore("This can not be tested as it contains view inside validation method")
    fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(Validation.isEmailValid("name@email.com"))
    }

    @Test
    fun validator_isNotNull_ReturnsTrue() {
        assertTrue(Validation.isNotNull("Abc"))
    }

    @Test
    fun passWordValidator_isNotNull_ReturnsTrue() {
        assertTrue(Validation.isNotNull("Abc@123"))
    }

    @Test
    fun passWordValidator_isValid_ReturnsTrue() {
        Assert.assertTrue(Validation.isValidPassword("Abc@123"))
    }

    @Test
    fun emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        Assert.assertFalse(Validation.isEmailValid("name@email"))
    }

    @Test
    fun emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        Assert.assertFalse(Validation.isEmailValid("name@email..com"))
    }

    @Test
    fun emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        Assert.assertFalse(Validation.isEmailValid("@email.com"))
    }

    @Test
    fun emailValidator_EmptyString_ReturnsFalse() {
        Assert.assertFalse(Validation.isEmailValid(""))
    }

    @Test
    fun emailValidator_NullEmail_ReturnsFalse() {
        Assert.assertFalse(Validation.isEmailValid("null"))
    }
}