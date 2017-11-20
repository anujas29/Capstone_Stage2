package anuja.project.finalproject;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by USER on 08-11-2017.
 */

public class FindLocation {

    public static String Location(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.location),
                        context.getString(R.string.default_location)).toLowerCase();
    }
}
