package com.hdstatus.module;

import android.media.MediaFormat;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.whatsapp")) {
            return;
        }

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

                    if (MediaFormat.KEY_BIT_RATE.equals(key)) {
                        param.args[1] = 5000000; 
                    }
                    else if (MediaFormat.KEY_WIDTH.equals(key) && currentValue < 1080) {
                        param.args[1] = 1080;
                    }
                    else if (MediaFormat.KEY_HEIGHT.equals(key) && currentValue < 1920) {
                        param.args[1] = 1920;
                    }
                    else if (MediaFormat.KEY_FRAME_RATE.equals(key) && currentValue < 30) {
                        param.args[1] = 30;
                    }
                }
            }
        );
    }
}
