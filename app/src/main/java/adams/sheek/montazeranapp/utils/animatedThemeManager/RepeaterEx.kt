package adams.sheek.montazeranapp.utils.animatedThemeManager

import adams.sheek.montazeranapp.utils.animatedThemeManager.themes.MyAppTheme
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.forEachIndexed

object RepeaterEx {
    private fun detectViewAndChange(v: View, appTheme: AppTheme, context: Context){
        val myAppTheme = appTheme as MyAppTheme
        if (v is ViewGroup){
            when(true){
                v is CardView -> {
                    v.setCardBackgroundColor(myAppTheme.activityThemeButtonColor(context))
                }
                else -> {
                    v.setBackgroundColor(myAppTheme.activityThemeButtonColor(context))
                }
            }
        }else{
            when(true){
                v is Button -> {
                    v.setBackgroundColor(myAppTheme.activityIconColor(context))
                }
                v is ImageView -> {
                    v.setColorFilter(myAppTheme.activityIconColor(context))
                }
                v is TextView -> {
                    v.setTextColor(myAppTheme.activityTextColor(context))
                }
            }
        }
    }
    fun repeater(viewGroup: ViewGroup, appTheme: AppTheme, context: Context){
        viewGroup.forEachIndexed {i,v ->
            if (v is ViewGroup){
                if (v.tag != null){
                    if (!v.tag.toString().contains(CONST.SKIP_TO_CHANGE_ALL) &&
                        v.tag.toString().contains(CONST.SKIP_TO_CHANGE_THIS) ){
                        repeater(v, appTheme, context)
                    }else{
                        detectViewAndChange(v, appTheme, context)
                    }
                }else{
                    detectViewAndChange(v, appTheme, context)
                    repeater(v, appTheme, context)
                }
            }else{
                if (v.tag != null){
                    if (!v.tag.toString().contains(CONST.SKIP_TO_CHANGE_THIS) &&
                        !v.tag.toString().contains(CONST.SKIP_TO_CHANGE_THIS)){
                        detectViewAndChange(v, appTheme, context)
                    }
                }else{
                    detectViewAndChange(v, appTheme, context)
                }
            }
        }
    }
}