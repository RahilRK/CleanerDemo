package com.appname.structure.user.ui

import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import com.appname.structure.user.utils.MainActivityTestRule
import com.base.hilt.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the [SplashFragment].
 */
@LargeTest
@HiltAndroidTest
class SplashTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mActivityScenarioRule = MainActivityTestRule(R.id.splashFragment)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test(timeout = 10000)
    fun checkToolBarTitle() {
        Thread.sleep(3000)
        ViewAssertions.matches(ViewMatchers.isDisplayed())
    }
}
