package xnspy.app.xnspy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.C0443Ad;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.hsalf.smilerating.SmileRating;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    public static final String ACTION_WIFI_DISPLAY_SETTINGS = "android.settings.WIFI_DISPLAY_SETTINGS";
    private static final int WAIT_TIME = 5000;
    private LinearLayout adContainer;
    private LinearLayout adContainerOne;
    private LinearLayout adContainerTwo;
    private LinearLayout adView;
    private AdView adViewBanner;
    private AdView adViewBannerOne;
    private AdView adViewBannerTwo;
    SharedPreferences app_preferences;
    Button btn;
    int count = 0;
    Dialog dialog;
    boolean doubleBackToExitPressedOnce = false;
    private InterstitialAd interstitialAd;
    private boolean interstitialCanceled = false;
    TextView modelTextView;
    /* access modifiers changed from: private */
    public NativeAd nativeAd;
    private NativeAdLayout nativeAdLayout;
    TextView osTextView;
    TextView productTextView;
    TextView sdkTextView;
    private Timer waitTimer;
    WifiManager wifi;

    class C08681 implements View.OnClickListener {
        C08681() {
        }

        public void onClick(View v) {
            MainActivity.this.interstitialAd();
        }
    }

    class C08746 implements DialogInterface.OnClickListener {
        C08746() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
        }
    }

    class C08757 implements DialogInterface.OnClickListener {
        C08757() {
        }

        public void onClick(DialogInterface dialog, int which) {
            String appName = MainActivity.this.getApplicationContext().getPackageName();
            try {
                MainActivity mainActivity = MainActivity.this;
                mainActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appName)));
            } catch (ActivityNotFoundException e) {
                MainActivity mainActivity2 = MainActivity.this;
                mainActivity2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0893R.layout.activity_main);
        this.count = 0;
        this.app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        rateMyApp();
        loadInit();
        this.adViewBanner = new AdView((Context) this, getString(C0893R.string.fb_banner_home), AdSize.BANNER_HEIGHT_50);
        this.adContainer = (LinearLayout) findViewById(C0893R.C0895id.banner_container);
        this.adContainer.addView(this.adViewBanner);
        this.adViewBanner.loadAd();
        this.adViewBannerOne = new AdView((Context) this, getString(C0893R.string.fb_banner_home), AdSize.BANNER_HEIGHT_50);
        this.adContainerOne = (LinearLayout) findViewById(C0893R.C0895id.banner_container1);
        this.adContainerOne.addView(this.adViewBannerOne);
        this.adViewBannerOne.loadAd();
        this.modelTextView = (TextView) findViewById(C0893R.C0895id.modelName);
        this.productTextView = (TextView) findViewById(C0893R.C0895id.productName);
        this.osTextView = (TextView) findViewById(C0893R.C0895id.osVersionName);
        this.sdkTextView = (TextView) findViewById(C0893R.C0895id.versionSDKVersion);
        this.btn = (Button) findViewById(C0893R.C0895id.widiBtn);
        this.modelTextView.setText("");
        this.productTextView.setText("");
        this.osTextView.setText("");
        this.sdkTextView.setText("");
        this.wifi = (WifiManager) getApplicationContext().getSystemService("wifi");
        loadNativeAd(false);
        this.btn.setOnClickListener(new C08681());
        findViewById(C0893R.C0895id.howBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MyApplication.getInstance().showInterstitial();
                MainActivity.this.showHowTouseDialog();
            }
        });
    }

    /* access modifiers changed from: private */
    public void loadInit() {
        this.interstitialAd = new InterstitialAd(this, getResources().getString(C0893R.string.fb_interstitial_home));
        this.interstitialAd.loadAd(this.interstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
            public void onInterstitialDisplayed(C0443Ad ad) {
            }

            public void onInterstitialDismissed(C0443Ad ad) {
                MainActivity.this.loadInit();
                MainActivity.this.enablingWiFiDisplay();
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

    public void interstitialAd() {
        if (this.interstitialAd.isAdLoaded()) {
            this.interstitialAd.show();
        } else {
            enablingWiFiDisplay();
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void enablingWiFiDisplay() {
        if (this.wifi.isWifiEnabled()) {
            wifidisplay();
            return;
        }
        this.wifi.setWifiEnabled(true);
        wifidisplay();
    }

    public void wifidisplay() {
        try {
            startActivity(new Intent("android.settings.WIFI_DISPLAY_SETTINGS"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            try {
                startActivity(getPackageManager().getLaunchIntentForPackage("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG"));
            } catch (Exception e2) {
                try {
                    startActivity(new Intent("android.settings.CAST_SETTINGS"));
                } catch (Exception e3) {
                    Toast.makeText(getApplicationContext(), "Device not supported", 1).show();
                }
            }
        }
    }

    public void onBackPressed() {
        this.dialog = new Dialog(this, C0893R.style.MyDialog);
        this.dialog.setCanceledOnTouchOutside(true);
        this.dialog.setContentView(C0893R.layout.backdlg);
        this.dialog.getWindow().getAttributes().windowAnimations = C0893R.style.DialogAnimation;
        loadNativeAd(true);
        ((SmileRating) this.dialog.findViewById(C0893R.C0895id.smile_rating)).setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            public void onRatingSelected(int i, boolean z) {
                if (i == 4 || i == 5) {
                    try {
                        MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + MainActivity.this.getPackageName())));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "You don't have Google Play installed", 1).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Thank you for given Review", 0).show();
                }
            }
        });
        ((Button) this.dialog.findViewById(C0893R.C0895id.btn_no)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.dialog.cancel();
            }
        });
        ((Button) this.dialog.findViewById(C0893R.C0895id.btn_yes)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.dialog.dismiss();
                MainActivity.this.finish();
            }
        });
        this.dialog.show();
    }

    public void rateMyApp() {
        try {
            int counter = this.app_preferences.getInt("counter", 0);
            if (counter != 0 && counter % 5 == 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Please rate");
                alert.setMessage("Thanks for using this app. Please take a moment to rate it.");
                alert.setPositiveButton("Cancel", new C08746());
                alert.setNegativeButton("Rate it", new C08757());
                alert.show();
            }
            SharedPreferences.Editor editor = this.app_preferences.edit();
            editor.putInt("counter", counter + 1);
            editor.commit();
        } catch (Exception e) {
        }
    }

    private void loadNativeAd(final boolean check) {
        this.nativeAd = new NativeAd((Context) this, getResources().getString(C0893R.string.facebook_native));
        this.nativeAd.loadAd(this.nativeAd.buildLoadAdConfig().withAdListener(new NativeAdListener() {
            public void onMediaDownloaded(C0443Ad ad) {
            }

            public void onError(C0443Ad ad, AdError adError) {
            }

            public void onAdLoaded(C0443Ad ad) {
                if (MainActivity.this.nativeAd != null && MainActivity.this.nativeAd == ad) {
                    MainActivity.this.inflateAd(MainActivity.this.nativeAd, check);
                }
            }

            public void onAdClicked(C0443Ad ad) {
            }

            public void onLoggingImpression(C0443Ad ad) {
            }
        }).build());
    }

    /* access modifiers changed from: private */
    public void inflateAd(NativeAd nativeAd2, boolean check) {
        nativeAd2.unregisterView();
        if (check) {
            this.nativeAdLayout = (NativeAdLayout) this.dialog.findViewById(C0893R.C0895id.native_ad_container);
        } else {
            this.nativeAdLayout = (NativeAdLayout) findViewById(C0893R.C0895id.native_ad_container);
        }
        nativeAd2.unregisterView();
        this.nativeAdLayout = (NativeAdLayout) findViewById(C0893R.C0895id.native_ad_container);
        int i = 0;
        this.adView = (LinearLayout) LayoutInflater.from(this).inflate(C0893R.layout.native_layout, this.nativeAdLayout, false);
        this.nativeAdLayout.addView(this.adView);
        LinearLayout adChoicesContainer = (LinearLayout) findViewById(C0893R.C0895id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(this, nativeAd2, this.nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);
        MediaView nativeAdIcon = (MediaView) this.adView.findViewById(C0893R.C0895id.native_ad_icon);
        TextView nativeAdTitle = (TextView) this.adView.findViewById(C0893R.C0895id.native_ad_title);
        MediaView nativeAdMedia = (MediaView) this.adView.findViewById(C0893R.C0895id.native_ad_media);
        TextView sponsoredLabel = (TextView) this.adView.findViewById(C0893R.C0895id.native_ad_sponsored_label);
        Button nativeAdCallToAction = (Button) this.adView.findViewById(C0893R.C0895id.native_ad_call_to_action);
        nativeAdTitle.setText(nativeAd2.getAdvertiserName());
        ((TextView) this.adView.findViewById(C0893R.C0895id.native_ad_body)).setText(nativeAd2.getAdBodyText());
        ((TextView) this.adView.findViewById(C0893R.C0895id.native_ad_social_context)).setText(nativeAd2.getAdSocialContext());
        if (!nativeAd2.hasCallToAction()) {
            i = 4;
        }
        nativeAdCallToAction.setVisibility(i);
        nativeAdCallToAction.setText(nativeAd2.getAdCallToAction());
        sponsoredLabel.setText(nativeAd2.getSponsoredTranslation());
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeAd2.registerViewForInteraction((View) this.adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }

    public void showHowTouseDialog() {
        final Dialog dialog2 = new Dialog(this);
        dialog2.setContentView(C0893R.layout.how_to_use_dialog);
        dialog2.setCancelable(false);
        dialog2.getWindow().setBackgroundDrawableResource(17170445);
        this.adViewBannerTwo = new AdView((Context) this, getString(C0893R.string.fb_banner_home), AdSize.BANNER_HEIGHT_50);
        this.adContainerTwo = (LinearLayout) dialog2.findViewById(C0893R.C0895id.banner_container);
        this.adContainerTwo.addView(this.adViewBannerTwo);
        this.adViewBannerTwo.loadAd();
        dialog2.findViewById(C0893R.C0895id.btnOk).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MyApplication.getInstance().showInterstitial();
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }
}
