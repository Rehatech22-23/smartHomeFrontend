package de.rehatech2223.lgg_frontend.services

import android.view.View
import com.google.android.material.snackbar.Snackbar

@Deprecated("unused in 1.0")
class PopUpService {

    companion object {
        // Should be used on Coordinator Layout
        fun showUniversalPopUp(text: String, view: View) {
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
        }
    }
}