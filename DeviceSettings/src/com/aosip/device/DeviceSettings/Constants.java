/*
 * Copyright (C) 2015 The CyanogenMod Project
 * Copyright (C) 2017 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aosip.device.DeviceSettings;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.SwitchPreference;
import androidx.preference.PreferenceManager;

import android.media.AudioManager;

public class Constants {

    // Gestures
    public static final String TOUCHSCREEN_CAMERA_GESTURE_KEY = "touchscreen_gesture_camera";
    public static final String TOUCHSCREEN_MUSIC_GESTURE_KEY = "touchscreen_gesture_music";
    public static final String TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY = "touchscreen_gesture_flashlight";

    // Gestures nodes
    public static final String TOUCHSCREEN_CAMERA_NODE = "/proc/touchpanel/letter_o_enable";
    public static final String TOUCHSCREEN_DOUBLE_SWIPE_NODE = "/proc/touchpanel/double_swipe_enable";
    public static final String TOUCHSCREEN_LEFT_ARROW = "/proc/touchpanel/left_arrow_enable";
    public static final String TOUCHSCREEN_RIGHT_ARROW = "/proc/touchpanel/right_arrow_enable";
    public static final String TOUCHSCREEN_FLASHLIGHT_NODE = "/proc/touchpanel/down_arrow_enable";

    // Gestures nodes default values
    public static final boolean TOUCHSCREEN_CAMERA_DEFAULT = true;
    public static final boolean TOUCHSCREEN_MUSIC_DEFAULT = true;
    public static final boolean TOUCHSCREEN_FLASHLIGHT_DEFAULT = true;

    // Array of music-related gestures
    public static final String[] TOUCHSCREEN_MUSIC_GESTURES_ARRAY = {TOUCHSCREEN_DOUBLE_SWIPE_NODE, TOUCHSCREEN_LEFT_ARROW, TOUCHSCREEN_RIGHT_ARROW};

    // Preference keys
    public static final String NOTIF_SLIDER_TOP_KEY = "keycode_top_position";
    public static final String NOTIF_SLIDER_MIDDLE_KEY = "keycode_middle_position";
    public static final String NOTIF_SLIDER_BOTTOM_KEY = "keycode_bottom_position";

    // Button prefs
    public static final String NOTIF_SLIDER_TOP_PREF = "pref_keycode_top_position";
    public static final String NOTIF_SLIDER_MIDDLE_PREF = "pref_keycode_middle_position";
    public static final String NOTIF_SLIDER_BOTTOM_PREF = "pref_keycode_bottom_position";

    // Default values
    public static final int KEY_VALUE_TOTAL_SILENCE = 0;
    public static final int KEY_VALUE_SILENT = 1;
    public static final int KEY_VALUE_PRIORTY_ONLY = 2;
    public static final int KEY_VALUE_VIBRATE = 3;
    public static final int KEY_VALUE_NORMAL = 4;

    public static final Map<String, String> sBooleanNodePreferenceMap = new HashMap<>();
    public static final Map<String, Object> sNodeDefaultMap = new HashMap<>();
    public static final Map<String, String> sStringKeyPreferenceMap = new HashMap<>();
    public static final Map<String, String> sStringNodePreferenceMap = new HashMap<>();
    public static final Map<Integer, String> sKeyMap = new HashMap<>();
    public static final Map<String, Integer> sKeyDefaultMap = new HashMap<>();

    public static final String[] sGesturePrefKeys = {
        TOUCHSCREEN_CAMERA_GESTURE_KEY,
        TOUCHSCREEN_MUSIC_GESTURE_KEY,
        TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY
    };

    static {
        sBooleanNodePreferenceMap.put(TOUCHSCREEN_CAMERA_GESTURE_KEY, TOUCHSCREEN_CAMERA_NODE);
        sBooleanNodePreferenceMap.put(TOUCHSCREEN_MUSIC_GESTURE_KEY, TOUCHSCREEN_DOUBLE_SWIPE_NODE);
        sBooleanNodePreferenceMap.put(TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY,
                TOUCHSCREEN_FLASHLIGHT_NODE);

        sStringKeyPreferenceMap.put(NOTIF_SLIDER_TOP_KEY, NOTIF_SLIDER_TOP_PREF);
        sStringKeyPreferenceMap.put(NOTIF_SLIDER_MIDDLE_KEY, NOTIF_SLIDER_MIDDLE_PREF);
        sStringKeyPreferenceMap.put(NOTIF_SLIDER_BOTTOM_KEY, NOTIF_SLIDER_BOTTOM_PREF);

        sKeyMap.put(603, NOTIF_SLIDER_TOP_KEY);
        sKeyMap.put(602, NOTIF_SLIDER_MIDDLE_KEY);
        sKeyMap.put(601, NOTIF_SLIDER_BOTTOM_KEY);

        sNodeDefaultMap.put(TOUCHSCREEN_CAMERA_GESTURE_KEY, TOUCHSCREEN_CAMERA_DEFAULT);
        sNodeDefaultMap.put(TOUCHSCREEN_MUSIC_GESTURE_KEY, TOUCHSCREEN_MUSIC_DEFAULT);
        sNodeDefaultMap.put(TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY, TOUCHSCREEN_FLASHLIGHT_DEFAULT);

        sKeyDefaultMap.put(NOTIF_SLIDER_TOP_KEY, KEY_VALUE_TOTAL_SILENCE);
        sKeyDefaultMap.put(NOTIF_SLIDER_MIDDLE_KEY, KEY_VALUE_VIBRATE);
        sKeyDefaultMap.put(NOTIF_SLIDER_BOTTOM_KEY, KEY_VALUE_NORMAL);
    }

    public static int getPreferenceInt(Context context, String key) {
        return Settings.System.getIntForUser(context.getContentResolver(),
                sStringKeyPreferenceMap.get(key), sKeyDefaultMap.get(key), UserHandle.USER_CURRENT);
    }

    public static void setPreferenceInt(Context context, String key, int value) {
        Settings.System.putIntForUser(context.getContentResolver(),
                sStringKeyPreferenceMap.get(key), value, UserHandle.USER_CURRENT);
    }

    public static boolean isPreferenceEnabled(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, (Boolean) sNodeDefaultMap.get(key));
    }

    public static String getPreferenceString(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, (String) sNodeDefaultMap.get(key));
    }
}
