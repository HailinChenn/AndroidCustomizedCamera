package com.joyhen.android;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Display;
import android.view.Surface;

/**
 * Created by chenhailin on 16/1/29.
 *
 * Helper class for android.
 */
public final class AndroidUtil {
    public static final String TAG = "AndroidUtil";
    private AndroidUtil(){}

    /**
     *  Get the natural orientation of the device.
     * @param activity
     * @return Configuration.ORIENTATION_LANDSCAPE or Configuration.ORIENTATION_PORTRAIT.
     */
    public static int getNaturalOrientation(Activity activity) {
        int naturalOrientation = Configuration.ORIENTATION_UNDEFINED;

        Display display = activity.getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();

        Log.d(TAG, "" + rotation);

        Configuration configuration = activity.getResources().getConfiguration();
        if (Configuration.ORIENTATION_LANDSCAPE == configuration.orientation) {
            if (Surface.ROTATION_0 == rotation || Surface.ROTATION_180 == rotation) {
                naturalOrientation = Configuration.ORIENTATION_LANDSCAPE;
            }
            else
            {
                naturalOrientation = Configuration.ORIENTATION_PORTRAIT;
            }
        }
        else
        {
            if (Surface.ROTATION_0 == rotation || Surface.ROTATION_180 == rotation) {
                naturalOrientation = Configuration.ORIENTATION_PORTRAIT;
            }
            else
            {
                naturalOrientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return naturalOrientation;
    }
}
