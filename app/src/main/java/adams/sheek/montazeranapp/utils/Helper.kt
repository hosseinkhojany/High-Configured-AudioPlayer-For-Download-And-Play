package adams.sheek.montazeranapp.utils

import adams.sheek.montazeranapp.data.datasource.ram.InRamDs
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


object Helper {


    fun ImageView.loadImage(uri: String?) {

        this.post {

            val myOptions = RequestOptions()
                .override(this.width, this.height)
                .centerCrop()

            Glide
                .with(this.context)
                .load(uri)
                .apply(myOptions)
                .into(this)

        }

    }

    fun showForceUpdateDialog(context: Context, url: String){
        AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle("")
            .setMessage("برنامه نیاز به آپدیت دارد")
            .setPositiveButton("دانلود"
            ) { dialog, which ->
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }.show()
    }

    fun showNeedUpdateDialog(context: Context, url: String){
        AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle("")
            .setMessage("آپدیت جدید در دسرس است")
            .setPositiveButton("دانلود"
            ) { dialog, which ->
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }.setNegativeButton("فعلا نه", ){ dialog, which ->
                dialog.dismiss()
            }.show()
    }

    fun showLogoutDialog(context: Context, onYesClick: () -> Unit){
        AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle("")
            .setMessage("خروج از حساب کاربری")
            .setPositiveButton("بله"
            ) { dialog, which ->
                onYesClick()
            }.setNegativeButton("خیر", ){ dialog, which ->
                dialog.dismiss()
            }.show()
    }

    fun getUid(): String {
        return UID.get()
    }

    fun updateStringWithOneSecond(elapsedTime: String): String{
        var hour = elapsedTime.split(":")[0].toInt()
        var minute = elapsedTime.split(":")[1].toInt()
        var second = elapsedTime.split(":")[2].toInt()

        second += 1
        if (second == 60){
            minute += 1
            second = 0
        }
        if (minute == 60){
            hour += 1
            minute = 0
        }

        val secondTxt = if (second.toString().length == 1){
            "0$second"
        }else{
            second.toString()
        }

        val minuteTxt = if (minute.toString().length == 1){
            "0$minute"
        }else{
            minute.toString()
        }

        val hourTxt = if (hour.toString().length == 1){
            "0$hour"
        }else{
            hour.toString()
        }
        return "$hourTxt:$minuteTxt:$secondTxt"
    }

    fun calculatePositions(): String{
        var res = ""
        res += when(InRamDs.selectedTopicPositionInList+1){
            1 -> "جلسه اول"
            2 -> "جلسه دوم"
            3 -> "جلسه سوم"
            4 -> "جلسه چهارم"
            5 -> "جلسه پنجم"
            6 -> "جلسه ششم"
            7 -> "جلسه هفتم"
            8 -> "جلسه هشتم"
            9 -> "جلسه نهم"
            10 -> "جلسه دهم"
            11 -> "جلسه یازدهم"
            12 -> "جلسه دوازدهم"
            13 -> "جلسه سیزدهم"
            14 -> "جلسه چهارم"
            15 -> "جلسه پانزدهم"
            16 -> "جلسه شانزدهم"
            17 -> "جلسه هفدهم"
            18 -> "جلسه هجدهم"
            19 -> "جلسه نوزدهم"
            20 -> "جلسه بیستم"
            else -> ""
        }
        res += " از "
        res += when(InRamDs.selectedTopicFilePositionInList+1){
            1 -> "بخش اول"
            2 -> "بخش دوم"
            3 -> "بخش سوم"
            4 -> "بخش چهارم"
            5 -> "بخش پنجم"
            6 -> "بخش ششم"
            7 -> "بخش هفتم"
            8 -> "بخش هشتم"
            9 -> "بخش نهم"
            10 -> "بخش دهم"
            11 -> "بخش یازدهم"
            12 -> "بخش دوازدهم"
            13 -> "بخش سیزدهم"
            14 -> "بخش چهارم"
            15 -> "بخش پانزدهم"
            16 -> "بخش شانزدهم"
            17 -> "بخش هفدهم"
            18 -> "بخش هجدهم"
            19 -> "بخش نوزدهم"
            20 -> "بخش بیستم"
            else -> ""
        }
        return res
    }

}