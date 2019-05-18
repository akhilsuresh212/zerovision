package org.tensorflow.tensorflowdemo;

import android.content.Context;


/**
 * Created by Noushad on 16/5/17.
 */

public class SharedPreferenceUtils {

    public static final String SHARED_PREFERENCES_ID = "shared_preference_id";
    public static String getString(Context context, String key) {
        if (context != null) {
            return context.getSharedPreferences(SHARED_PREFERENCES_ID, Context.MODE_PRIVATE)
                    .getString(key, "");
        } else {
            return null;
        }
    }

    public static void setString(Context context, String key, String value) {
        if (context != null) {
            context.getSharedPreferences(SHARED_PREFERENCES_ID, Context.MODE_PRIVATE)
                    .edit()
                    .putString(key, value)
                    .apply();
        }
    }

    public static boolean getBoolean(Context context, String key) {
        return context.getSharedPreferences(SHARED_PREFERENCES_ID, Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }


    public static void setBoolean(Context context, String key, boolean b) {
        context.getSharedPreferences(SHARED_PREFERENCES_ID, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, b)
                .apply();
    }


    public static void clearPreference(Context context, String key) {
        context.getSharedPreferences(SHARED_PREFERENCES_ID, Context.MODE_PRIVATE).edit().remove(key).apply();
    }


    public static void setInt(Context context, String key, int value) {


        if (context != null) {
            context.getSharedPreferences(SHARED_PREFERENCES_ID, Context.MODE_PRIVATE)
                    .edit()
                    .putInt(key, value)
                    .apply();
        }
    }

    public static int getInt(Context context, String key) {

        if (context != null) {
            return context.getSharedPreferences(SHARED_PREFERENCES_ID, Context.MODE_PRIVATE)
                    .getInt(key, 0);
        } else {
            return 0;
        }

    }


}