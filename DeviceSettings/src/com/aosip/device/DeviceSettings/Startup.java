/*
* Copyright (C) 2013 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.aosip.device.DeviceSettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.PreferenceManager;

public class Startup extends BroadcastReceiver {

    private void restore(String file, boolean enabled) {
        if (file == null) {
            return;
        }
        if (enabled) {
            Utils.writeValue(file, "1");
        }
    }

    private void restore(String file, String value) {
        if (file == null) {
            return;
        }
        Utils.writeValue(file, value);
    }

    static boolean hasTouchscreenGestures () {
        return new File(Constants.TOUCHSCREEN_CAMERA_NODE).exists() &&
                new File(Constants.TOUCHSCREEN_DOUBLE_SWIPE_NODE).exists() &&
                new File(Constants.TOUCHSCREEN_FLASHLIGHT_NODE).exists();
    }

    private void disableComponent(Context context, String component) {
        ComponentName name = new ComponentName(context, component);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(name,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void enableComponent(Context context, String component) {
        ComponentName name = new ComponentName(context, component);
        PackageManager pm = context.getPackageManager();
        if (pm.getComponentEnabledSetting(name)
                == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            pm.setComponentEnabledSetting(name,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    @Override
    public void onReceive(final Context context, final Intent bootintent) {
        boolean enabled = false;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_SRGB_SWITCH, false);
        restore(SRGBModeSwitch.getFile(), enabled);
        enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_HBM_SWITCH, false);
        restore(HBMModeSwitch.getFile(), enabled);
        enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_DC_SWITCH, false);
        restore(DCModeSwitch.getFile(), enabled);
        enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_DCI_SWITCH, false);
        restore(DCIModeSwitch.getFile(), enabled);
        enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_NIGHT_SWITCH, false);
        restore(NightModeSwitch.getFile(), enabled);
        enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_WIDECOLOR_SWITCH, false);
        restore(WideColorModeSwitch.getFile(), enabled);
        enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_FPS_INFO, false);
        if (enabled) {
            context.startService(new Intent(context, FPSInfoService.class));
        }
        VibratorStrengthPreference.restore(context);

        if (!hasTouchscreenGestures()) {
            disableComponent(context, TouchscreenGestureSettings.class.getName());
        } else {
            enableComponent(context, TouchscreenGestureSettings.class.getName());
            // Restore nodes to saved preference values
            for (String pref : Constants.sGesturePrefKeys) {
                boolean value = Constants.isPreferenceEnabled(context, pref);
                String node = Constants.sBooleanNodePreferenceMap.get(pref);
                // If music gestures are toggled, update values of all music gesture proc files
                if (pref.equals(Constants.TOUCHSCREEN_MUSIC_GESTURE_KEY)) {
                    for (String music_nodes : Constants.TOUCHSCREEN_MUSIC_GESTURES_ARRAY) {
                        if (!FileUtils.writeLine(music_nodes, value ? "1" : "0")) {
                            Log.w(TAG, "Write to node " + music_nodes +
                                    " failed while restoring saved preference values");
                        }
                    }
                } else if (!FileUtils.writeLine(node, value ? "1" : "0")) {
                    Log.w(TAG, "Write to node " + node +
                            " failed while restoring saved preference values");
                }
            }
        }
    }
}
