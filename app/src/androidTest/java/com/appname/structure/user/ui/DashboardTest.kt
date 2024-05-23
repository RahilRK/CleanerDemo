package com.appname.structure.user.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.filters.LargeTest
import com.appname.structure.user.utils.MainActivityTestRule
import com.appname.structure.user.utils.launchFragmentInHiltContainer
import com.base.hilt.R
import com.base.hilt.ui.dashboard.DashboardFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

/**
 * Unit tests for the [DashboardFragment].
 */
@LargeTest
@HiltAndroidTest
class DashboardTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

     @get:Rule(order = 2)
     var activityRule = MainActivityTestRule(R.id.navigation_dashboard)

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
                    ViewMatchers.withText(R.string.title_dashboard)
                )
            )
        );
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(timeout = 500000)
    fun goToDashboard_clickEditImageButton_navigateToAddImageFragment() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<DashboardFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        Thread.sleep(8000)
        Espresso.onView(ViewMatchers.withId(R.id.imageViewEditProfile)).perform(ViewActions.click())

        onView(isRoot()).inRoot(isDialog()).noActivity()
    }
}