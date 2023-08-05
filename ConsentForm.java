package com.google.ads.consent;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.facebook.internal.NativeProtocol;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;

public class ConsentForm {
    private final boolean adFreeOption;
    private final URL appPrivacyPolicyURL;
    private final Context context;
    private final Dialog dialog;
    /* access modifiers changed from: private */
    public final ConsentFormListener listener;
    /* access modifiers changed from: private */
    public LoadState loadState;
    private final boolean nonPersonalizedAdsOption;
    private final boolean personalizedAdsOption;
    private final WebView webView;

    private enum LoadState {
        NOT_READY,
        LOADING,
        LOADED
    }

    private ConsentForm(Builder builder) {
        this.context = builder.context;
        if (builder.listener == null) {
            this.listener = new ConsentFormListener(this) {
            };
        } else {
            this.listener = builder.listener;
        }
        this.personalizedAdsOption = builder.personalizedAdsOption;
        this.nonPersonalizedAdsOption = builder.nonPersonalizedAdsOption;
        this.adFreeOption = builder.adFreeOption;
        this.appPrivacyPolicyURL = builder.appPrivacyPolicyURL;
        this.dialog = new Dialog(this.context, 16973840);
        this.loadState = LoadState.NOT_READY;
        this.webView = new WebView(this.context);
        this.webView.setBackgroundColor(0);
        this.dialog.setContentView(this.webView);
        this.dialog.setCancelable(false);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new WebViewClient() {
            boolean isInternalRedirect;

            private boolean isConsentFormUrl(String url) {
                return !TextUtils.isEmpty(url) && url.startsWith("consent://");
            }

            /* JADX WARNING: Code restructure failed: missing block: B:10:0x003c, code lost:
                if (r2.equals("dismiss") == false) goto L_0x0053;
             */
            /* JADX WARNING: Removed duplicated region for block: B:19:0x0058  */
            /* JADX WARNING: Removed duplicated region for block: B:20:0x005e  */
            /* JADX WARNING: Removed duplicated region for block: B:21:0x0066  */
            /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            private void handleUrl(java.lang.String r10) {
                /*
                    r9 = this;
                    boolean r0 = r9.isConsentFormUrl(r10)
                    if (r0 != 0) goto L_0x0007
                    return
                L_0x0007:
                    r0 = 1
                    r9.isInternalRedirect = r0
                    android.net.Uri r1 = android.net.Uri.parse(r10)
                    java.lang.String r2 = "action"
                    java.lang.String r2 = r1.getQueryParameter(r2)
                    java.lang.String r3 = "status"
                    java.lang.String r3 = r1.getQueryParameter(r3)
                    java.lang.String r4 = "url"
                    java.lang.String r4 = r1.getQueryParameter(r4)
                    r5 = -1
                    int r6 = r2.hashCode()
                    r7 = -1370505102(0xffffffffae4fc072, float:-4.7237277E-11)
                    r8 = 0
                    if (r6 == r7) goto L_0x0049
                    r7 = 150940456(0x8ff2b28, float:1.53574E-33)
                    if (r6 == r7) goto L_0x003f
                    r7 = 1671672458(0x63a3b28a, float:6.039369E21)
                    if (r6 == r7) goto L_0x0036
                    goto L_0x0053
                L_0x0036:
                    java.lang.String r6 = "dismiss"
                    boolean r6 = r2.equals(r6)
                    if (r6 == 0) goto L_0x0053
                    goto L_0x0054
                L_0x003f:
                    java.lang.String r0 = "browser"
                    boolean r0 = r2.equals(r0)
                    if (r0 == 0) goto L_0x0053
                    r0 = 2
                    goto L_0x0054
                L_0x0049:
                    java.lang.String r0 = "load_complete"
                    boolean r0 = r2.equals(r0)
                    if (r0 == 0) goto L_0x0053
                    r0 = 0
                    goto L_0x0054
                L_0x0053:
                    r0 = -1
                L_0x0054:
                    switch(r0) {
                        case 0: goto L_0x0066;
                        case 1: goto L_0x005e;
                        case 2: goto L_0x0058;
                        default: goto L_0x0057;
                    }
                L_0x0057:
                    goto L_0x006c
                L_0x0058:
                    com.google.ads.consent.ConsentForm r0 = com.google.ads.consent.ConsentForm.this
                    r0.handleOpenBrowser(r4)
                    goto L_0x006c
                L_0x005e:
                    r9.isInternalRedirect = r8
                    com.google.ads.consent.ConsentForm r0 = com.google.ads.consent.ConsentForm.this
                    r0.handleDismiss(r3)
                    goto L_0x006c
                L_0x0066:
                    com.google.ads.consent.ConsentForm r0 = com.google.ads.consent.ConsentForm.this
                    r0.handleLoadComplete(r3)
                L_0x006c:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.ads.consent.ConsentForm.C07452.handleUrl(java.lang.String):void");
            }

            public void onLoadResource(WebView view, String url) {
                handleUrl(url);
            }

            @TargetApi(24)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (!isConsentFormUrl(url)) {
                    return false;
                }
                handleUrl(url);
                return true;
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!isConsentFormUrl(url)) {
                    return false;
                }
                handleUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (!this.isInternalRedirect) {
                    ConsentForm.this.updateDialogContent(view);
                }
                super.onPageFinished(view, url);
            }

            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LoadState unused = ConsentForm.this.loadState = LoadState.NOT_READY;
                ConsentForm.this.listener.onConsentFormError(error.toString());
            }
        });
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public boolean adFreeOption = false;
        /* access modifiers changed from: private */
        public final URL appPrivacyPolicyURL;
        /* access modifiers changed from: private */
        public final Context context;
        /* access modifiers changed from: private */
        public ConsentFormListener listener;
        /* access modifiers changed from: private */
        public boolean nonPersonalizedAdsOption = false;
        /* access modifiers changed from: private */
        public boolean personalizedAdsOption = false;

        public Builder(Context context2, URL appPrivacyPolicyURL2) {
            this.context = context2;
            this.appPrivacyPolicyURL = appPrivacyPolicyURL2;
            if (this.appPrivacyPolicyURL == null) {
                throw new IllegalArgumentException("Must provide valid app privacy policy url to create a ConsentForm");
            }
        }

        public Builder withListener(ConsentFormListener listener2) {
            this.listener = listener2;
            return this;
        }

        public Builder withPersonalizedAdsOption() {
            this.personalizedAdsOption = true;
            return this;
        }

        public Builder withNonPersonalizedAdsOption() {
            this.nonPersonalizedAdsOption = true;
            return this;
        }

        public Builder withAdFreeOption() {
            this.adFreeOption = true;
            return this;
        }

        public ConsentForm build() {
            return new ConsentForm(this);
        }
    }

    private static String getApplicationName(Context context2) {
        return context2.getApplicationInfo().loadLabel(context2.getPackageManager()).toString();
    }

    private static String getAppIconURIString(Context context2) {
        Drawable iconDrawable = context2.getPackageManager().getApplicationIcon(context2.getApplicationInfo());
        Bitmap bitmap = Bitmap.createBitmap(iconDrawable.getIntrinsicWidth(), iconDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        iconDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        iconDrawable.draw(canvas);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String valueOf = String.valueOf(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
        return valueOf.length() != 0 ? "data:image/png;base64,".concat(valueOf) : new String("data:image/png;base64,");
    }

    private static String createJavascriptCommand(String command, String argumentsJSON) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("info", argumentsJSON);
        HashMap<String, Object> wrappedArgs = new HashMap<>();
        wrappedArgs.put("args", args);
        return String.format("javascript:%s(%s)", new Object[]{command, new Gson().toJson((Object) wrappedArgs)});
    }

    /* access modifiers changed from: private */
    public void updateDialogContent(WebView webView2) {
        HashMap<String, Object> formInfo = new HashMap<>();
        formInfo.put(NativeProtocol.BRIDGE_ARG_APP_NAME_STRING, getApplicationName(this.context));
        formInfo.put("app_icon", getAppIconURIString(this.context));
        formInfo.put("offer_personalized", Boolean.valueOf(this.personalizedAdsOption));
        formInfo.put("offer_non_personalized", Boolean.valueOf(this.nonPersonalizedAdsOption));
        formInfo.put("offer_ad_free", Boolean.valueOf(this.adFreeOption));
        formInfo.put("is_request_in_eea_or_unknown", Boolean.valueOf(ConsentInformation.getInstance(this.context).isRequestLocationInEeaOrUnknown()));
        formInfo.put("app_privacy_url", this.appPrivacyPolicyURL);
        ConsentData consentData = ConsentInformation.getInstance(this.context).loadConsentData();
        formInfo.put("plat", consentData.getSDKPlatformString());
        formInfo.put("consent_info", consentData);
        webView2.loadUrl(createJavascriptCommand("setUpConsentDialog", new Gson().toJson((Object) formInfo)));
    }

    public void load() {
        if (this.loadState == LoadState.LOADING) {
            this.listener.onConsentFormError("Cannot simultaneously load multiple consent forms.");
        } else if (this.loadState == LoadState.LOADED) {
            this.listener.onConsentFormLoaded();
        } else {
            this.loadState = LoadState.LOADING;
            this.webView.loadUrl("file:///android_asset/consentform.html");
        }
    }

    /* access modifiers changed from: private */
    public void handleLoadComplete(String status) {
        if (TextUtils.isEmpty(status)) {
            this.loadState = LoadState.NOT_READY;
            this.listener.onConsentFormError("No information");
        } else if (status.contains("Error")) {
            this.loadState = LoadState.NOT_READY;
            this.listener.onConsentFormError(status);
        } else {
            this.loadState = LoadState.LOADED;
            this.listener.onConsentFormLoaded();
        }
    }

    /* access modifiers changed from: private */
    public void handleOpenBrowser(String urlString) {
        if (TextUtils.isEmpty(urlString)) {
            this.listener.onConsentFormError("No valid URL for browser navigation.");
            return;
        }
        try {
            this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(urlString)));
        } catch (ActivityNotFoundException e) {
            this.listener.onConsentFormError("No Activity found to handle browser intent.");
        }
    }

    /* access modifiers changed from: private */
    public void handleDismiss(String status) {
        ConsentStatus consentStatus;
        this.loadState = LoadState.NOT_READY;
        this.dialog.dismiss();
        if (TextUtils.isEmpty(status)) {
            this.listener.onConsentFormError("No information provided.");
        } else if (status.contains("Error")) {
            this.listener.onConsentFormError(status);
        } else {
            boolean userPrefersAdFree = false;
            char c = 65535;
            int hashCode = status.hashCode();
            if (hashCode != -1152655096) {
                if (hashCode != -258041904) {
                    if (hashCode == 1666911234 && status.equals("non_personalized")) {
                        c = 1;
                    }
                } else if (status.equals("personalized")) {
                    c = 0;
                }
            } else if (status.equals("ad_free")) {
                c = 2;
            }
            switch (c) {
                case 0:
                    consentStatus = ConsentStatus.PERSONALIZED;
                    break;
                case 1:
                    consentStatus = ConsentStatus.NON_PERSONALIZED;
                    break;
                case 2:
                    userPrefersAdFree = true;
                    consentStatus = ConsentStatus.UNKNOWN;
                    break;
                default:
                    consentStatus = ConsentStatus.UNKNOWN;
                    break;
            }
            ConsentInformation.getInstance(this.context).setConsentStatus(consentStatus, "form");
            this.listener.onConsentFormClosed(consentStatus, Boolean.valueOf(userPrefersAdFree));
        }
    }

    public void show() {
        if (this.loadState != LoadState.LOADED) {
            this.listener.onConsentFormError("Consent form is not ready to be displayed.");
        } else if (ConsentInformation.getInstance(this.context).isTaggedForUnderAgeOfConsent()) {
            this.listener.onConsentFormError("Error: tagged for under age of consent");
        } else {
            this.dialog.getWindow().setLayout(-1, -1);
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                public void onShow(DialogInterface dialog) {
                    ConsentForm.this.listener.onConsentFormOpened();
                }
            });
            this.dialog.show();
            if (!this.dialog.isShowing()) {
                this.listener.onConsentFormError("Consent form could not be displayed.");
            }
        }
    }

    public boolean isShowing() {
        return this.dialog.isShowing();
    }
}
