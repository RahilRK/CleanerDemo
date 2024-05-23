package com.appname.structure.user.utils

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


/**
 * Custom Espresso Matcher which checks if a RecyclerView has an specific size
 *
 */
fun recyclerViewSizeMatcher(matcherSize: Int): Matcher<View?>? {
    return object : BoundedMatcher<View?, RecyclerView?>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("with list size: $matcherSize")
        }

        override fun matchesSafely(item: RecyclerView?): Boolean {
            return matcherSize == item?.adapter!!.itemCount
        }
    }
}



/**
 * Custom matcher for recyclerview testing.
 */
fun recyclerItemAtPosition(position: Int, @NonNull itemMatcher: Matcher<View>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }
        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position)
                ?:
                return false
            return itemMatcher.matches(viewHolder.itemView)
        }
    }
}
/**
 * Goes to an specific position in a RecyclerView, then checks if that item has specific information (i.e an specific text in the holder)
 *
 */
fun recyclerViewAtPositionOnView(
    position: Int,
    itemMatcher: Matcher<View?>,
    @NonNull targetViewId: Int
): Matcher<View?> {
    return object : BoundedMatcher<View?, RecyclerView>(
        RecyclerView::class.java
    ) {
        override fun describeTo(description: Description) {
            description.appendText("has view id $itemMatcher at position $position")
        }

        override fun matchesSafely(recyclerView: RecyclerView): Boolean {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
            val targetView = viewHolder!!.itemView.findViewById<View>(targetViewId)
            return itemMatcher.matches(targetView)
        }
    }
}

/**
 * Verifies if a text matches the text of a TextInputLayout hint
 *
 */
fun textInputLayoutWithItemHint(matcherText: String): Matcher<View?>? {
    return object : BoundedMatcher<View?, TextInputLayout>(
        TextInputLayout::class.java
    ) {
        override fun describeTo(description: Description) {
            description.appendText("with item hint: $matcherText")
        }

        override fun matchesSafely(editTextField: TextInputLayout): Boolean {
            return matcherText.equals(editTextField.hint.toString(), ignoreCase = true)
        }
    }
}

/**
 * A custom matcher that checks the hint property of an {@link EditText}. It
 * accepts either a {@link String} or a {@link Matcher}.
 */
fun editTextviewItemHint(matcherText: String): Matcher<View?>? {
    return object : BoundedMatcher<View?, EditText>(
        EditText::class.java
    ) {
        override fun describeTo(description: Description) {
            description.appendText("with item hint: $matcherText")
        }

        override fun matchesSafely(editTextField: EditText): Boolean {
            return matcherText.equals(editTextField.hint.toString(), ignoreCase = true)
        }
    }
}

/**
 * A custom matcher that checks the button has text as matcherText
 *
 */
fun buttonItemText(matcherText: String): Matcher<View?> {
    return object : BoundedMatcher<View?, Button>(
        Button::class.java
    ) {
        override fun describeTo(description: Description) {
            description.appendText("with item hint: $matcherText")
        }

        override fun matchesSafely(button: Button): Boolean {
            return matcherText.equals(button.text.toString(), ignoreCase = true)
        }
    }
}


class DrawableMatcher(@param:DrawableRes private val expectedId: Int) :
    TypeSafeMatcher<View>(View::class.java) {
    private var resourceName: String? = null
    override fun matchesSafely(target: View): Boolean {
        if (target !is ImageView) {
            return false
        }
        val imageView: ImageView = target as ImageView
        if (expectedId < 0) {
            return imageView.getDrawable() == null
        }
        val resources: Resources = target.context.resources
        val expectedDrawable: Drawable = resources.getDrawable(expectedId)
        resourceName = resources.getResourceEntryName(expectedId)
        return if (expectedDrawable != null && expectedDrawable.constantState != null) {
            expectedDrawable.constantState ==
                    imageView.getDrawable().getConstantState()
        } else {
            false
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: ")
        description.appendValue(expectedId)
        if (resourceName != null) {
            description.appendText("[")
            description.appendText(resourceName)
            description.appendText("]")
        }
    }
}
