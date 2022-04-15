package adams.sheek.montazeranapp.utils.animatedThemeManager.themes

import android.content.Context
import adams.sheek.montazeranapp.utils.animatedThemeManager.AppTheme

interface MyAppTheme : AppTheme {
    fun activityBackgroundColor(context: Context): Int
    fun activityImageRes(context: Context): Int
    fun activityIconColor(context: Context): Int
    fun activityTextColor(context: Context): Int
    fun activityThemeButtonColor(context: Context): Int
}