package com.appname.structure.user.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import com.appname.structure.user.utils.MainActivityTestRule
import com.base.hilt.R
import com.base.hilt.ui.dashboard.DashboardFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the [DashboardFragment].
 */
@LargeTest
@HiltAndroidTest
class LoginTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

     @get:Rule(order = 2)
     var activityRule = MainActivityTestRule(R.id.loginFragment)

    //Old way
  /*  @get:Rule(order = 2)
    var mActivityScenarioRule = ActivityTestRule(MainActivity::class.java)
*/
    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test(timeout = 500000)
    fun checkToolBarTitle() {

        onView(ViewMatchers.withId(R.id.toolbar)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(R.string.login)
                )
            )
        );
    }

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html

    @Test
    fun emptyEmail() {
        Thread.sleep(3000)
        val error = R.string.alert_enter_email
        onView(withId(R.id.email_text)).perform(
            ViewActions.typeText(""),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.login_btn)).perform(ViewActions.click())
        onView(withText(error)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun invalidEmail() {
        Thread.sleep(3000)
        val error = R.string.alert_enter_valid_email
        onView(withId(R.id.email_text)).perform(
            ViewActions.typeText("Hello.12345678"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.login_btn)).perform(ViewActions.click())
        onView(withText(error)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun emptyPassword() {
        Thread.sleep(3000)
        val error = R.string.alert_enter_password
        onView(withId(R.id.email_text)).perform(
            ViewActions.typeText("abc@abc.xyz"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.login_btn)).perform(ViewActions.click())
        onView(withText(error)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test(timeout = 10000)
    fun invalidPassword() {
        Thread.sleep(3000)
        val error = R.string.alert_enter_valid_password
        onView(withId(R.id.email_text)).perform(ViewActions.typeText("abc@abc.xyz"))
        onView(withId(R.id.password_text)).perform(ViewActions.typeText("asd"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.login_btn)).perform(scrollTo()).perform(ViewActions.click())
        onView(withText(error)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    fun loginSuccess() {
        Thread.sleep(3000)
        onView(withId(R.id.email_text)).perform(
            ViewActions.replaceText("vishal.joshi@brainvire.com"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.password_text)).perform(
            ViewActions.replaceText("Brain@2021"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.login_btn)).perform(ViewActions.click())

        onView(withText(R.string.label_login_successfully))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

    }
}