package adams.sheek.montazeranapp.ui.widgets

import android.view.View
import com.google.android.material.snackbar.Snackbar
import android.view.Gravity
import android.widget.FrameLayout
import android.os.Build
import android.widget.TextView


object Snacky {
    fun View?.snackError(msg: String){
        this?.let { customSnackBar(it, msg, Gravity.CENTER) }
    }
    private fun customSnackBar(parentView: View, str: String, gravity: Int){
        val snack: Snackbar = Snackbar.make(parentView, str, Snackbar.LENGTH_LONG)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = gravity
        if (gravity == Gravity.TOP){
            params.topMargin = 60
        }
        val mTextView = view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) mTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER else mTextView.gravity = Gravity.CENTER_HORIZONTAL
        view.layoutParams = params
        snack.show()
    }

}