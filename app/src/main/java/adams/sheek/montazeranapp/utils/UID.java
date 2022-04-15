package adams.sheek.montazeranapp.utils;
import android.os.Build;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class UID {

    public  static String getDeviceLanguage()
    {
        Locale locale=Locale.getDefault();
        return locale.getDisplayLanguage();
    }

    public  static String getDeviceCountry()
    {
        Locale locale=Locale.getDefault();
        return locale.getDisplayCountry();
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String getAndroidVersion() {

        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion + " (" + release +")";

    }

    public static int getAndroidAPILevel() {

        int sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion;

    }




    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    public static String get() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = null;
        try {
            serial = Objects.requireNonNull(Build.class.getField("SERIAL").get(null)).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial";
        }

        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

}

