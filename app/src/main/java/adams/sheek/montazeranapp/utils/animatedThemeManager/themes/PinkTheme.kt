package adams.sheek.montazeranapp.utils.animatedThemeManager.themes

import android.content.Context
import androidx.core.content.ContextCompat
import adams.sheek.montazeranapp.R

class PinkTheme : MyAppTheme {

    companion object {
        val ThemeId = 2
    }
    override fun id(): Int {
        return ThemeId
    }

    override fun activityBackgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.background_pink)
    }

    override fun activityImageRes(context: Context): Int {
        return R.drawable.image_pink
    }

    override fun activityIconColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.icon_pink)
    }

    override fun activityTextColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.text_pink)
    }

    override fun activityThemeButtonColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.button_pink)
    }
}