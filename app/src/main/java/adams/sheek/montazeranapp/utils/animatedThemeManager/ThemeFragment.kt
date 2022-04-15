package adams.sheek.montazeranapp.utils.animatedThemeManager

import adams.sheek.montazeranapp.utils.animatedThemeManager.RepeaterEx.repeater
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class ThemeFragment : Fragment() {

    override fun onResume() {
        getThemeManager()?.getCurrentLiveTheme()?.observe(this) {
            repeater(requireView() as ViewGroup, it, requireContext())
        }

        super.onResume()
    }

    protected fun getThemeManager() : ThemeManager? {
        return ThemeManager.instance
    }

    // to sync ui with selected theme
//    abstract fun syncTheme(appTheme: AppTheme)
}