package vini.lop.io.marvelappstarter.util

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

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

fun String.preciseSubstring(length: Int): String =
    if (length < this.length) {
        "${this.substring(0, length)}..."
    } else {
        this
    }

fun ImageView.loadRemoteImg(path: String) {
    Glide.with(this.context)
        .load(path)
        .into(this)
}