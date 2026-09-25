package com.hdstatus.module;

import android.media.MediaFormat;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        // Only run if the app being loaded is WhatsApp
        if (!lpparam.packageName.equals("com.whatsapp")) {
            return;
        }

        // Hook the Android MediaFormat system to intercept WhatsApp's compression limits
        XposedHelpers.findAndHookMethod(
            "android.media.MediaFormat", 
            lpparam.classLoader, 
            "setInteger", 
            String.class, int.class, 
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String key = (String) param.args[0];
                    int currentValue = (Integer) param.args[1];

                    // 1. Boost bitrate to 5 Mbps
                    if (MediaFormat.KEY_BITRATE.equals(key)) {
                        param.args[1] = 5000000; 
                    }
                    // 2. Force 1080p resolution width
                    else if (MediaFormat.KEY_WIDTH.equals(key) && currentValue < 1080) {
                        param.args[1] = 1080;
                    }
                    // 3. Force 1920p resolution height
                    else if (MediaFormat.KEY_HEIGHT.equals(key) && currentValue < 1920) {
                        param.args[1] = 1920;
                    }
                    // 4. Force 30 FPS minimum
                    else if (MediaFormat.KEY_FRAME_RATE.equals(key) && currentValue < 30) {
                        param.args[1] = 30;
                    }
                }
            }
        );
    }
}
