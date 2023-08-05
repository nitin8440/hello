package xnspy.app.xnspy;

import android.app.Application;
import com.facebook.ads.AdError;
import com.facebook.ads.C0443Ad;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class MyApplication extends Application {
    public static MyApplication intance = null;
    private InterstitialAd interstitialAd;

    public static MyApplication getInstance() {
        return intance;
    }

    public void onCreate() {
        super.onCreate();
        intance = this;
        loadInterstitial();
    }

    private void loadInterstitial() {
        this.interstitialAd = new InterstitialAd(this, getResources().getString(C0893R.string.fb_interstitial_home));
        loadInit();
    }

    /* access modifiers changed from: package-private */
    public void loadInit() {
        this.interstitialAd = new InterstitialAd(this, getResources().getString(C0893R.string.fb_interstitial_home));
        this.interstitialAd.loadAd(this.interstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
            public void onInterstitialDisplayed(C0443Ad ad) {
            }

            public void onInterstitialDismissed(C0443Ad ad) {
                MyApplication.this.loadInit();
            }

            public void onError(C0443Ad ad, AdError adError) {
            }

            public void onAdLoaded(C0443Ad ad) {
            }

            public void onAdClicked(C0443Ad ad) {
            }

            public void onLoggingImpression(C0443Ad ad) {
            }
        }).build());
    }

    public void showInterstitial() {
        if (this.interstitialAd.isAdLoaded()) {
            this.interstitialAd.show();
        }
    }
}
