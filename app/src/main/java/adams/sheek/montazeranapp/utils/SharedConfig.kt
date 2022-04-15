package adams.sheek.montazeranapp.utils

import adams.sheek.montazeranapp.App
import adams.sheek.montazeranapp.R
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.lang.StringBuilder

object SharedConfig {

    //region config
    val pref by lazy { App.context().getSharedPreferences("cfg" , Context.MODE_PRIVATE) }

    fun setPreference(key: String?, value: Any?) {
        key?.let {
            value?.let {
                Log.i("SHARED_PREF","$key => $value")
                val editor: SharedPreferences.Editor =
                    pref.edit()
                when (value) {
                    is Int -> editor.putInt(
                        key,
                        (value as Int?)!!
                    )
                    is String -> editor.putString(
                        key,
                        value as String?
                    )
                    is Boolean -> editor.putBoolean(
                        key,
                        (value as Boolean?)!!
                    )
                    is Long -> editor.putLong(
                        key,
                        (value as Long?)!!
                    )
                    is Set<*> -> editor.putStringSet(key, value as Set<String?>?)
                }
                editor.apply()
            }
        }
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return pref.getInt(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String): String {
        return pref.getString(key, defaultValue) ?: defaultValue
    }
    
    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return pref.getBoolean(key, defaultValue)
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return pref.getLong(key, defaultValue)
    }

    fun getStringSet(key: String?, defaultValue: Set<String?>?): Set<String?>? {
        return pref.getStringSet(key, defaultValue)
    }

    fun clearTag(keyName: String?) {
        val editor: SharedPreferences.Editor = pref.edit()
        editor.remove(keyName)
        editor.apply()
    }

    fun contain(key: String?): Boolean {
        return pref.contains(key)
    }

    fun RemovingSinglePreference(key: String?) {
        pref.edit().remove(key).apply()
    }
    //endregion


    //region variables

    private const val MOBILE = "MOBILE"
    private const val TOKEN = "TOKEN"
    private const val APP_THEME = "APP_THEME"

    fun setMobile(value: String){
        setPreference(this.MOBILE, value)
    }
    fun getMobile(): String{
        return getString(this.MOBILE, "")
    }
    fun setUserToken(value: String){
        setPreference(this.TOKEN, value)
    }
    fun getUserToken(): String{
        return getString(this.TOKEN, "")
    }
    fun setAppTheme(value: Int){
        setPreference(this.APP_THEME, value)
    }
    fun getAppTheme(): Int{
        return getInt(this.APP_THEME, R.style.Default)
    }

    fun clearAll(){
        pref.edit().clear().apply()
    }

    fun logAllCfg(): StringBuilder {
        var all = StringBuilder()
        val keys: Map<String, *> = pref.all
        Log.i("SHARED_PREF", "------------------------------START PREFS------------------------------")
        for ((key, value) in keys) {
            all.append("{$key => $value}")
            Log.i("SHARED_PREF", "{$key => $value}")
        }
        Log.i("SHARED_PREF", "-----------------------------END PREFS-------------------------------")
        return all
    }

    //endregion

}
