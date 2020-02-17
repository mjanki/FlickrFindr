package org.br.util.extensions

import android.view.View

fun View.setVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}