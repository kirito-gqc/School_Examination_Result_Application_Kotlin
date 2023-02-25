package com.example.examresultenteringsystem


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun loginActivityTest() {
        val appCompatEditText = onView(
            allOf(
                withId(R.id.EditTextEmail),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.TextInputEmail),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("qian"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.EditTextEmail), withText("qian"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.TextInputEmail),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(click())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.EditTextEmail), withText("qian"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.TextInputEmail),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("qianchaogan@gmail.com"))

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.EditTextEmail), withText("qianchaogan@gmail.com"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.TextInputEmail),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.EditTextPassword),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.TextInputPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(replaceText("12345678"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.EditTextPassword), withText("12345678"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.TextInputPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.ButtonLogin), withText("Login"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
