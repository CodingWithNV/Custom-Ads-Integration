package com.secrethq.utils;

import android.util.Log;

import org.cocos2dx.lib.Cocos2dxActivity;
import java.util.List;

public class PTJniHelper {
	public static String password() {
		return "TCupJpuusAseK/F3yK6zDxkq+SGU+rYKGXuqIJj84QpIfPEhm6ixXhwq+iHOrrEPSy/+cJ2msFtNfPx0nq23XQ==";
	}

    public static native boolean isAdNetworkActive(String name);

    public static native String jsSettingsString();

    public static void setSettingsValue(String path, String value, Cocos2dxActivity act) {
        if (!Thread.currentThread().getName().contains("GLThread")) {
            act.runOnGLThread(() -> setSettingsValueNative(path, value));
        } else {
            setSettingsValueNative(path, value);
        }
    }

    public static String getSettingsValue(String path) {
        if (!Thread.currentThread().getName().contains("GLThread")) {
            Log.e("PTJniHelper", "getSettingsValue must be run inside runOnGLThread(new Runnable() {...}");
            throw new RuntimeException("getSettingsValue must be run inside runOnGLThread(new Runnable() {...}");
        } else {
            return getSettingsValueNative(path);
        }
    }

    private static native void setSettingsValueNative(String path, String value);
    private static native String getSettingsValueNative(String path);

    public static native boolean targetsChildren();

    public static native List<String> adNetworkSdkIds();
    public static native List<String> analyticsSdkIds();

    public static native String classPath(String groupId, String sdkId);
    public static native String displayName(String groupId, String sdkId);
    public static native String privacyPolicyUrl(String groupId, String sdkId);
}
