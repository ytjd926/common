package com.tangjd.common.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by tangjd on 2016/1/8.
 */
public class SPManager {
    private static SPManager sSPManager;
    private Context mContext;

    public static SPManager getInstance() {
        if (sSPManager == null) {
            synchronized (SPManager.class) {
                if (sSPManager == null) {
                    sSPManager = new SPManager();
                }
            }
        }
        return sSPManager;
    }

    public void init(Context context) {
        mContext = context;
    }

    /**
     * Set an int value in the preferences editor, to be written back once method commit is called.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns a reference to the same Editor object, so you can chain put calls together.
     */
    public void putInt(String key, int value) {
        getPref().edit().putInt(key, value).commit();
    }

    /**
     * Retrieve an int value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not an int.
     * @throws ClassCastException
     */
    public int getInt(String key, int defValue) {
        return getPref().getInt(key, defValue);
    }

    /**
     * Set a String value in the preferences editor, to be written back once method commit is called.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns a reference to the same Editor object, so you can chain put calls together.
     */
    public void putString(String key, String value) {
        getPref().edit().putString(key, value).commit();
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a String.
     * @throws ClassCastException
     */
    public String getString(String key, String defValue) {
        return getPref().getString(key, defValue);
    }

    /**
     * Set a long value in the preferences editor, to be written back once method commit is called.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns a reference to the same Editor object, so you can chain put calls together.
     */
    public void putLong(String key, Long value) {
        getPref().edit().putLong(key, value).commit();
    }

    /**
     * Retrieve a long value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a long.
     * @throws ClassCastException
     */
    public long getLong(String key, long defValue) {
        return getPref().getLong(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        getPref().edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getPref().getBoolean(key, defValue);
    }

    private SharedPreferences mPref;

    private SharedPreferences getPref() {
        if (mPref == null) {
            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        return mPref;
    }
}
