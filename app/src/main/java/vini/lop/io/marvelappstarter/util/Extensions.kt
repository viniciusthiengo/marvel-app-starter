package vini.lop.io.marvelappstarter.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(
    message: String,
    duration: Int = Toast.LENGTH_LONG
) {
    Toast.makeText(
        requireContext(),
        message,
        duration
    ).show()
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}