package com.base.hilt.extensions

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.AdapterView
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.doOnLayout

import com.google.android.material.textfield.TextInputLayout


fun View.enable() {
    isEnabled = true
    isClickable = true
}

fun View.disable() {
    isEnabled = false
    isClickable = false
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Group.showGroup() {
    visibility = View.VISIBLE
    requestLayout()
}

fun Group.hideGroup() {
    visibility = View.GONE
    requestLayout()
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Group.addOnClickListener(listener: (view: View) -> Unit) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}

fun onViews(views: List<View>, func: View.() -> Unit) {
    views.map { it.func() }
}

fun hideViews(views: List<View>) {
    onViews(views) { hide() }
}

fun showViews(views: List<View>) {
    onViews(views) { show() }
}

fun conditionalShowViews(views: List<View>, predicate: () -> Boolean) {
    if (predicate()) showViews(views) else hideViews(views)
}

fun TextInputLayout.setHintStyle(id: Int) {
    doOnLayout {
        setHintTextAppearance(id)
    }
}

fun NumberPicker.setNumberPickerWithTypeString(
    range: Array<String>,
    minValue: Int,
    maxValue: Int,
    listener: (view: NumberPicker, Any?, newVal: Any?) -> Unit,
    selectedValue: String? = null
) {
    this.minValue = minValue
    this.maxValue = maxValue

    this.displayedValues = range

    /* if (selectedValue.isNullOrEmpty().not()) {
         this.value = selectedValue
     }*/
    this.setFormatter({ i -> String.format("%02d", i) })
    // Set  picker value change
    this.setOnValueChangedListener(listener)
}

fun NumberPicker.setNumberPickerWithTypeInt(
    minValue: Int,
    maxValue: Int,
    listener: (view: NumberPicker, Any?, Any?) -> Unit,
    selectedValue: Int? = null
) {
    this.minValue = minValue
    this.maxValue = maxValue


    /* if (selectedValue.isNullOrEmpty().not()) {
         this.value = selectedValue
     }*/
    this.setFormatter({ i -> String.format("%02d", i) })
    // Set  picker value change
    this.setOnValueChangedListener(listener)
}
// Spinner
fun Spinner.onItemSelected(cb: (index: Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) = cb(position)
    }
}

fun setTextViewDrawableColor(textView: TextView, color: Int) {
    for (drawable in textView.compoundDrawables) {
        if (drawable != null) {
            drawable.colorFilter =
                PorterDuffColorFilter(
                    color,
                    PorterDuff.Mode.SRC_IN
                )
        }
    }
}

fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}

/*fun LottieAnimationView.setupAnimation() {
//        this.speed = 2.0F // How fast does the animation play
//        animation.progress = 1F // Starts the animation from 50% of the beginning
    this.addAnimatorUpdateListener {
// Called every time the frame of the animation changes
    }
    this.repeatMode =
        LottieDrawable.RESTART // Restarts the animation (you can choose to reverse it as well)
    if (this.isAnimating) {
        this.cancelAnimation() // Cancels the animation
    } else {
        this.playAnimation()
        }
    }*/

