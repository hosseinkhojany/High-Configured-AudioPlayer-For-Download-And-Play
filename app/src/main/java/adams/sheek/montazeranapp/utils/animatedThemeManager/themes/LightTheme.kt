package adams.sheek.montazeranapp.utils.animatedThemeManager.themes

import android.content.Context
import androidx.core.content.ContextCompat
import adams.sheek.montazeranapp.R

class LightTheme : MyAppTheme {

    companion object {
        val ThemeId = 0
    }

    override fun id(): Int {
        return ThemeId
    }

    override fun activityBackgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.background_light)
    }

    override fun activityImageRes(context: Context): Int {
        return R.drawable.image_light
    }

    override fun activityIconColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.icon_light)
    }

    override fun activityTextColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.text_light)
    }

    override fun activityThemeButtonColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.button_light)
    }
}