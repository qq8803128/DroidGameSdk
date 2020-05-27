package droid.game.common.preferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import droid.game.annotation.NonNull;

import java.util.LinkedHashSet;
import java.util.Set;

public class Prefs {
    private static final String LENGTH = "_length";
    private static final String DEFAULT_STRING_VALUE = "";
    private static final int DEFAULT_INT_VALUE = -1;
    private static final double DEFAULT_DOUBLE_VALUE = -1d;
    private static final float DEFAULT_FLOAT_VALUE = -1f;
    private static final long DEFAULT_LONG_VALUE = -1L;
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;

    private static SharedPreferences sharedPreferences;
    private static Prefs prefsInstance;

    private Prefs(@NonNull Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(
                context.getPackageName() + ".single.preferences",
                Context.MODE_PRIVATE
        );
    }

    private Prefs(@NonNull Context context, @NonNull String preferencesName) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(
                preferencesName,
                Context.MODE_PRIVATE
        );
    }

    /**
     * @param context
     * @return Returns a 'Prefs' instance
     */
    public static Prefs with(@NonNull Context context) {
        if (prefsInstance == null) {
            prefsInstance = new Prefs(context);
        }
        return prefsInstance;
    }

    /**
     * @param context
     * @param forceInstantiation
     * @return Returns a 'Prefs' instance
     */
    public static Prefs with(@NonNull Context context, boolean forceInstantiation) {
        if (forceInstantiation) {
            prefsInstance = new Prefs(context);
        }
        return prefsInstance;
    }

    /**
     * @param context
     * @param preferencesName
     * @return Returns a 'Prefs' instance
     */
    public static Prefs with(@NonNull Context context, @NonNull String preferencesName) {
        if (prefsInstance == null) {
            prefsInstance = new Prefs(context, preferencesName);
        }
        return prefsInstance;
    }

    /**
     * @param context
     * @param preferencesName
     * @param forceInstantiation
     * @return Returns a 'Prefs' instance
     */
    public static Prefs with(@NonNull Context context, @NonNull String preferencesName,
                             boolean forceInstantiation) {
        if (forceInstantiation) {
            prefsInstance = new Prefs(context, preferencesName);
        }
        return prefsInstance;
    }

    // String related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public String read(String what) {
        return sharedPreferences.getString(what, DEFAULT_STRING_VALUE);
    }

    /**
     * @param what
     * @param defaultString
     * @return Returns the stored value of 'what'
     */
    public String read(String what, String defaultString) {
        return sharedPreferences.getString(what, defaultString);
    }

    /**
     * @param where
     * @param what
     */
    public Prefs write(String where, String what) {
        sharedPreferences.edit().putString(where, what).apply();
        return this;
    }

    // int related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public int readInt(String what) {
        return sharedPreferences.getInt(what, DEFAULT_INT_VALUE);
    }

    /**
     * @param what
     * @param defaultInt
     * @return Returns the stored value of 'what'
     */
    public int readInt(String what, int defaultInt) {
        return sharedPreferences.getInt(what, defaultInt);
    }

    /**
     * @param where
     * @param what
     */
    public Prefs writeInt(String where, int what) {
        sharedPreferences.edit().putInt(where, what).apply();
        return this;
    }

    // double related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public double readDouble(String what) {
        if (!contains(what))
            return DEFAULT_DOUBLE_VALUE;
        return Double.longBitsToDouble(readLong(what));
    }

    /**
     * @param what
     * @param defaultDouble
     * @return Returns the stored value of 'what'
     */
    public double readDouble(String what, double defaultDouble) {
        if (!contains(what))
            return defaultDouble;
        return Double.longBitsToDouble(readLong(what));
    }

    /**
     * @param where
     * @param what
     */
    public Prefs writeDouble(String where, double what) {
        writeLong(where, Double.doubleToRawLongBits(what));
        return this;
    }

    // float related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public float readFloat(String what) {
        return sharedPreferences.getFloat(what, DEFAULT_FLOAT_VALUE);
    }

    /**
     * @param what
     * @param defaultFloat
     * @return Returns the stored value of 'what'
     */
    public float readFloat(String what, float defaultFloat) {
        return sharedPreferences.getFloat(what, defaultFloat);
    }

    /**
     * @param where
     * @param what
     */
    public Prefs writeFloat(String where, float what) {
        sharedPreferences.edit().putFloat(where, what).apply();
        return this;
    }

    // long related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public long readLong(String what) {
        return sharedPreferences.getLong(what, DEFAULT_LONG_VALUE);
    }

    /**
     * @param what
     * @param defaultLong
     * @return Returns the stored value of 'what'
     */
    public long readLong(String what, long defaultLong) {
        return sharedPreferences.getLong(what, defaultLong);
    }

    /**
     * @param where
     * @param what
     */
    public Prefs writeLong(String where, long what) {
        sharedPreferences.edit().putLong(where, what).apply();
        return this;
    }

    // boolean related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public boolean readBoolean(String what) {
        return sharedPreferences.getBoolean(what, DEFAULT_BOOLEAN_VALUE);
    }

    /**
     * @param what
     * @param defaultBoolean
     * @return Returns the stored value of 'what'
     */
    public boolean readBoolean(String what, boolean defaultBoolean) {
        return sharedPreferences.getBoolean(what, defaultBoolean);
    }

    /**
     * @param where
     * @param what
     */
    public Prefs writeBoolean(String where, boolean what) {
        sharedPreferences.edit().putBoolean(where, what).apply();
        return this;
    }

    // String set methods

    /**
     * @param key
     * @param value
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Prefs putStringSet(final String key, final Set<String> value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            sharedPreferences.edit().putStringSet(key, value).apply();
        } else {
            // Workaround for pre-HC's lack of StringSets
            putOrderedStringSet(key, value);
        }
        return this;
    }

    /**
     * @param key
     * @param value
     */
    public Prefs putOrderedStringSet(String key, Set<String> value) {
        int stringSetLength = 0;
        if (sharedPreferences.contains(key + LENGTH)) {
            // First read what the value was
            stringSetLength = readInt(key + LENGTH);
        }
        writeInt(key + LENGTH, value.size());
        int i = 0;
        for (String aValue : value) {
            write(key + "[" + i + "]", aValue);
            i++;
        }
        for (; i < stringSetLength; i++) {
            // Remove any remaining values
            remove(key + "[" + i + "]");
        }
        return this;
    }

    /**
     * @param key
     * @param defValue
     * @return Returns the String Set with HoneyComb compatibility
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(final String key, final Set<String> defValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return sharedPreferences.getStringSet(key, defValue);
        } else {
            // Workaround for pre-HC's missing getStringSet
            return getOrderedStringSet(key, defValue);
        }
    }

    /**
     * @param key
     * @param defValue
     * @return Returns the ordered String Set
     */
    public Set<String> getOrderedStringSet(String key, final Set<String> defValue) {
        if (contains(key + LENGTH)) {
            LinkedHashSet<String> set = new LinkedHashSet<>();
            int stringSetLength = readInt(key + LENGTH);
            if (stringSetLength >= 0) {
                for (int i = 0; i < stringSetLength; i++) {
                    set.add(read(key + "[" + i + "]"));
                }
            }
            return set;
        }
        return defValue;
    }

    // end related methods

    /**
     * @param key
     */
    public void remove(final String key) {
        if (contains(key + LENGTH)) {
            // Workaround for pre-HC's lack of StringSets
            int stringSetLength = readInt(key + LENGTH);
            if (stringSetLength >= 0) {
                sharedPreferences.edit().remove(key + LENGTH).apply();
                for (int i = 0; i < stringSetLength; i++) {
                    sharedPreferences.edit().remove(key + "[" + i + "]").apply();
                }
            }
        }
        sharedPreferences.edit().remove(key).apply();
    }

    /**
     * @param key
     * @return Returns if that key exists
     */
    public boolean contains(final String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * Clear all the preferences
     */
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
