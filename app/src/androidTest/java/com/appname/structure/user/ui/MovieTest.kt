package com.appname.structure.user.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.appname.structure.user.utils.MainActivityTestRule
import com.appname.structure.user.utils.recyclerItemAtPosition
import com.appname.structure.user.utils.recyclerViewAtPositionOnView
import com.appname.structure.user.utils.recyclerViewSizeMatcher
import com.base.hilt.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters



/**
 * Unit tests for the [HomeFragment].
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MovieTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mActivityScenarioRule = MainActivityTestRule(initialNavId = R.id.navigation_home, graphId = R.navigation.mobile_navigation)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    // Note: this name might get change from API end
    private val nameTestOne = "Coco"

    @Ignore("flow has been changed no requirement to open login fragment to go to movie ")
    @Test(timeout = 50000)
    fun clickingLoginWithData_navigateToMovieUi_checkRecyclerviewItem() {
        Thread.sleep(5000)
        onView(withId(R.id.email_text)).perform(
            ViewActions.replaceText("abc.xyz@brainvire.com"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.password_text))
            .perform(ViewActions.replaceText("Brain@2021"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.login_btn)).perform(ViewActions.click())

        //Check if item at 0th position is having 0th element in json
        onView(withId(R.id.rvMovieList))
            .check(
                ViewAssertions.matches(
                    recyclerItemAtPosition(
                        0,
                        ViewMatchers.hasDescendant(ViewMatchers.withText(nameTestOne))
                    )
                )
            )


    }


    @Test(timeout = 50000)
    fun testRecyclerview_Visible() {
        Thread.sleep(8000)
        Espresso.onView(withId(R.id.rvMovieList))
            .inRoot(
                RootMatchers.withDecorView(
                    Matchers.`is`(mActivityScenarioRule.activity.window.decorView)
                )
            )
            .check(matches(isDisplayed()))
    }

    @Test(timeout = 50000)
    fun testRecyclerview_Scroll() {
        Thread.sleep(3000)
        // Get total item of RecyclerView
        val recyclerView: RecyclerView =
            mActivityScenarioRule.activity.findViewById(R.id.rvMovieList)
        val itemCount = recyclerView.adapter!!.itemCount

        // Scroll to end of page with position
        Espresso.onView(withId(R.id.rvMovieList))
            .inRoot(
                RootMatchers.withDecorView(
                    Matchers.`is`(mActivityScenarioRule.activity.window.decorView)
                )
            )
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemCount - 1))
    }


    //  Custom Espresso Matcher to check the size of a RecyclerView
    @Test(timeout = 500000)
    fun testRecyclerview_HasSpecificSize() {
        Thread.sleep(10000)
        // Get total item of RecyclerView
        val recyclerView: RecyclerView =
            mActivityScenarioRule.activity.findViewById(R.id.rvMovieList)
        val itemCount = recyclerView.adapter!!.itemCount

        onView(withId(R.id.rvMovieList)).check(matches(recyclerViewSizeMatcher(itemCount)))

    }

    //  Custom Espresso Matcher to check item at specific position has specific information
    @Test(timeout = 500000)
    fun testRecyclerview_ItemAtSpecificPosition_HasSpecificInfo() {
        Thread.sleep(4000)
        // Get total item of RecyclerView
        val recyclerView: RecyclerView =
            mActivityScenarioRule.activity.findViewById(R.id.rvMovieList)
        val itemCount = recyclerView.adapter!!.itemCount

        onView(withId(R.id.rvMovieList)).check(
            matches(recyclerViewAtPositionOnView(0, withText(nameTestOne), R.id.name))
        )

    }
}