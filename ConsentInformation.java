package com.google.ads.consent;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.VisibleForTesting;
import com.facebook.internal.ServerProtocol;
import com.facebook.share.internal.MessengerShareContentUtility;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ConsentInformation {
    private static final String CONSENT_DATA_KEY = "consent_string";
    private static final String MOBILE_ADS_SERVER_URL = "https://adservice.google.com/getconfig/pubvendors";
    private static final String PREFERENCES_FILE_KEY = "mobileads_consent";
    private static final String TAG = "ConsentInformation";
    private static ConsentInformation instance;
    private final Context context;
    private DebugGeography debugGeography = DebugGeography.DEBUG_GEOGRAPHY_DISABLED;
    private String hashedDeviceId = getHashedDeviceId();
    private List<String> testDevices = new ArrayList();

    private static class ConsentInfoUpdateResponse {
        String responseInfo;
        boolean success;

        ConsentInfoUpdateResponse(boolean success2, String responseInfo2) {
            this.success = success2;
            this.responseInfo = responseInfo2;
        }
    }

    private ConsentInformation(Context context2) {
        this.context = context2.getApplicationContext();
    }

    public static synchronized ConsentInformation getInstance(Context context2) {
        ConsentInformation consentInformation;
        synchronized (ConsentInformation.class) {
            if (instance == null) {
                instance = new ConsentInformation(context2);
            }
            consentInformation = instance;
        }
        return consentInformation;
    }

    /* access modifiers changed from: protected */
    public String getHashedDeviceId() {
        String androidId;
        ContentResolver contentResolver = this.context.getContentResolver();
        if (contentResolver == null) {
            androidId = null;
        } else {
            androidId = Settings.Secure.getString(contentResolver, "android_id");
        }
        return md5((androidId == null || isEmulator()) ? "emulator" : androidId);
    }

    private String md5(String string) {
        int i = 0;
        while (i < 3) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(string.getBytes());
                return String.format("%032X", new Object[]{new BigInteger(1, md5.digest())});
            } catch (NoSuchAlgorithmException e) {
                i++;
            } catch (ArithmeticException e2) {
                return null;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public void setHashedDeviceId(String hashedDeviceId2) {
        this.hashedDeviceId = hashedDeviceId2;
    }

    private boolean isEmulator() {
        return Build.FINGERPRINT.startsWith(MessengerShareContentUtility.TEMPLATE_GENERIC_TYPE) || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || (Build.BRAND.startsWith(MessengerShareContentUtility.TEMPLATE_GENERIC_TYPE) && Build.DEVICE.startsWith(MessengerShareContentUtility.TEMPLATE_GENERIC_TYPE)) || "google_sdk".equals(Build.PRODUCT);
    }

    public boolean isTestDevice() {
        return isEmulator() || this.testDevices.contains(this.hashedDeviceId);
    }

    public void addTestDevice(String hashedDeviceId2) {
        this.testDevices.add(hashedDeviceId2);
    }

    public DebugGeography getDebugGeography() {
        return this.debugGeography;
    }

    public void setDebugGeography(DebugGeography debugGeography2) {
        this.debugGeography = debugGeography2;
    }

    private static class AdNetworkLookupResponse {
        /* access modifiers changed from: private */
        @SerializedName("company_ids")
        public List<String> companyIds;
        /* access modifiers changed from: private */
        @SerializedName("ad_network_id")

        /* renamed from: id */
        public String f42id;
        /* access modifiers changed from: private */
        @SerializedName("is_npa")
        public boolean isNPA;
        /* access modifiers changed from: private */
        @SerializedName("lookup_failed")
        public boolean lookupFailed;
        /* access modifiers changed from: private */
        @SerializedName("not_found")
        public boolean notFound;

        private AdNetworkLookupResponse() {
        }
    }

    @VisibleForTesting
    protected static class ServerResponse {
        @SerializedName("ad_network_ids")
        List<AdNetworkLookupResponse> adNetworkLookupResponses;
        List<AdProvider> companies;
        @SerializedName("is_request_in_eea_or_unknown")
        Boolean isRequestLocationInEeaOrUnknown;

        protected ServerResponse() {
        }
    }

    private static class ConsentInfoUpdateTask extends AsyncTask<Void, Void, ConsentInfoUpdateResponse> {
        private static final String UPDATE_SUCCESS = "Consent update successful.";
        private final ConsentInformation consentInformation;
        private final ConsentInfoUpdateListener listener;
        private final List<String> publisherIds;
        private final String url;

        ConsentInfoUpdateTask(String url2, ConsentInformation consentInformation2, List<String> publisherIds2, ConsentInfoUpdateListener listener2) {
            this.url = url2;
            this.listener = listener2;
            this.publisherIds = publisherIds2;
            this.consentInformation = consentInformation2;
        }

        private String readStream(InputStream inputStream) {
            byte[] contents = new byte[1024];
            StringBuilder strFileContents = new StringBuilder();
            InputStream stream = new BufferedInputStream(inputStream);
            while (true) {
                try {
                    int read = stream.read(contents);
                    int bytesRead = read;
                    if (read != -1) {
                        strFileContents.append(new String(contents, 0, bytesRead));
                    } else {
                        try {
                            break;
                        } catch (IOException e) {
                            Log.e(ConsentInformation.TAG, e.getLocalizedMessage());
                        }
                    }
                } catch (IOException e2) {
                    Log.e(ConsentInformation.TAG, e2.getLocalizedMessage());
                    try {
                        stream.close();
                    } catch (IOException e3) {
                        Log.e(ConsentInformation.TAG, e3.getLocalizedMessage());
                    }
                    return null;
                } catch (Throwable th) {
                    try {
                        stream.close();
                    } catch (IOException e4) {
                        Log.e(ConsentInformation.TAG, e4.getLocalizedMessage());
                    }
                    throw th;
                }
            }
            stream.close();
            return strFileContents.toString();
        }

        private ConsentInfoUpdateResponse makeConsentLookupRequest(String urlString) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlString).openConnection();
                if (urlConnection.getResponseCode() != 200) {
                    return new ConsentInfoUpdateResponse(false, urlConnection.getResponseMessage());
                }
                String responseString = readStream(urlConnection.getInputStream());
                urlConnection.disconnect();
                this.consentInformation.updateConsentData(responseString, this.publisherIds);
                return new ConsentInfoUpdateResponse(true, UPDATE_SUCCESS);
            } catch (Exception e) {
                return new ConsentInfoUpdateResponse(false, e.getLocalizedMessage());
            }
        }

        public ConsentInfoUpdateResponse doInBackground(Void... unused) {
            String publisherIdsString = TextUtils.join(",", this.publisherIds);
            ConsentData consentData = this.consentInformation.loadConsentData();
            Uri.Builder uriBuilder = Uri.parse(this.url).buildUpon().appendQueryParameter("pubs", publisherIdsString).appendQueryParameter("es", "2").appendQueryParameter("plat", consentData.getSDKPlatformString()).appendQueryParameter("v", consentData.getSDKVersionString());
            if (this.consentInformation.isTestDevice() && this.consentInformation.getDebugGeography() != DebugGeography.DEBUG_GEOGRAPHY_DISABLED) {
                uriBuilder = uriBuilder.appendQueryParameter("debug_geo", this.consentInformation.getDebugGeography().getCode().toString());
            }
            return makeConsentLookupRequest(uriBuilder.build().toString());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(ConsentInfoUpdateResponse result) {
            if (result.success) {
                this.listener.onConsentInfoUpdated(this.consentInformation.getConsentStatus());
            } else {
                this.listener.onFailedToUpdateConsentInfo(result.responseInfo);
            }
        }
    }

    public synchronized void setTagForUnderAgeOfConsent(boolean underAgeOfConsent) {
        ConsentData consentData = loadConsentData();
        consentData.tagForUnderAgeOfConsent(underAgeOfConsent);
        saveConsentData(consentData);
    }

    public synchronized boolean isTaggedForUnderAgeOfConsent() {
        return loadConsentData().isTaggedForUnderAgeOfConsent();
    }

    public synchronized void reset() {
        SharedPreferences.Editor editor = this.context.getSharedPreferences(PREFERENCES_FILE_KEY, 0).edit();
        editor.clear();
        editor.apply();
        this.testDevices = new ArrayList();
    }

    public void requestConsentInfoUpdate(String[] publisherIds, ConsentInfoUpdateListener listener) {
        requestConsentInfoUpdate(publisherIds, MOBILE_ADS_SERVER_URL, listener);
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public void requestConsentInfoUpdate(String[] publisherIds, String url, ConsentInfoUpdateListener listener) {
        if (isTestDevice()) {
            Log.i(TAG, "This request is sent from a test device.");
        } else {
            String hashedDeviceId2 = getHashedDeviceId();
            StringBuilder sb = new StringBuilder(String.valueOf(hashedDeviceId2).length() + 93);
            sb.append("Use ConsentInformation.getInstance(context).addTestDevice(\"");
            sb.append(hashedDeviceId2);
            sb.append("\") to get test ads on this device.");
            Log.i(TAG, sb.toString());
        }
        new ConsentInfoUpdateTask(url, this, Arrays.asList(publisherIds), listener).execute(new Void[0]);
    }

    private void validatePublisherIds(ServerResponse response) throws Exception {
        if (response.isRequestLocationInEeaOrUnknown == null) {
            throw new Exception("Could not parse Event FE preflight response.");
        } else if (response.companies == null && response.isRequestLocationInEeaOrUnknown.booleanValue()) {
            throw new Exception("Could not parse Event FE preflight response.");
        } else if (response.isRequestLocationInEeaOrUnknown.booleanValue()) {
            HashSet<String> lookupFailedPublisherIds = new HashSet<>();
            HashSet<String> notFoundPublisherIds = new HashSet<>();
            for (AdNetworkLookupResponse adNetworkLookupResponse : response.adNetworkLookupResponses) {
                if (adNetworkLookupResponse.lookupFailed) {
                    lookupFailedPublisherIds.add(adNetworkLookupResponse.f42id);
                }
                if (adNetworkLookupResponse.notFound) {
                    notFoundPublisherIds.add(adNetworkLookupResponse.f42id);
                }
            }
            if (!lookupFailedPublisherIds.isEmpty() || !notFoundPublisherIds.isEmpty()) {
                StringBuilder errorString = new StringBuilder("Response error.");
                if (!lookupFailedPublisherIds.isEmpty()) {
                    errorString.append(String.format(" Lookup failure for: %s.", new Object[]{TextUtils.join(",", lookupFailedPublisherIds)}));
                }
                if (!notFoundPublisherIds.isEmpty()) {
                    errorString.append(String.format(" Publisher Ids not found: %s", new Object[]{TextUtils.join(",", notFoundPublisherIds)}));
                }
                throw new Exception(errorString.toString());
            }
        }
    }

    private HashSet<AdProvider> getNonPersonalizedAdProviders(List<AdProvider> adProviders, HashSet<String> nonPersonalizedAdProviderIds) {
        List<AdProvider> nonPersonalizedAdProviders = new ArrayList<>();
        for (AdProvider adProvider : adProviders) {
            if (nonPersonalizedAdProviderIds.contains(adProvider.getId())) {
                nonPersonalizedAdProviders.add(adProvider);
            }
        }
        return new HashSet<>(nonPersonalizedAdProviders);
    }

    /* access modifiers changed from: private */
    public synchronized void updateConsentData(String responseString, List<String> publisherIds) throws Exception {
        HashSet<AdProvider> newAdProviderSet;
        ServerResponse response = (ServerResponse) new Gson().fromJson(responseString, ServerResponse.class);
        validatePublisherIds(response);
        boolean hasNonPersonalizedPublisherId = false;
        HashSet<String> nonPersonalizedAdProvidersIds = new HashSet<>();
        if (response.adNetworkLookupResponses != null) {
            for (AdNetworkLookupResponse adNetworkLookupResponse : response.adNetworkLookupResponses) {
                if (adNetworkLookupResponse.isNPA) {
                    hasNonPersonalizedPublisherId = true;
                    List<String> companyIds = adNetworkLookupResponse.companyIds;
                    if (companyIds != null) {
                        nonPersonalizedAdProvidersIds.addAll(companyIds);
                    }
                }
            }
        }
        if (response.companies == null) {
            newAdProviderSet = new HashSet<>();
        } else if (hasNonPersonalizedPublisherId) {
            newAdProviderSet = getNonPersonalizedAdProviders(response.companies, nonPersonalizedAdProvidersIds);
        } else {
            newAdProviderSet = new HashSet<>(response.companies);
        }
        ConsentData consentData = loadConsentData();
        boolean hasNonPersonalizedPublisherIdChanged = consentData.hasNonPersonalizedPublisherId() != hasNonPersonalizedPublisherId;
        consentData.setHasNonPersonalizedPublisherId(hasNonPersonalizedPublisherId);
        consentData.setRawResponse(responseString);
        consentData.setPublisherIds(new HashSet(publisherIds));
        consentData.setAdProviders(newAdProviderSet);
        consentData.setRequestLocationInEeaOrUnknown(response.isRequestLocationInEeaOrUnknown.booleanValue());
        if (!response.isRequestLocationInEeaOrUnknown.booleanValue()) {
            saveConsentData(consentData);
            return;
        }
        if (!consentData.getAdProviders().equals(consentData.getConsentedAdProviders()) || hasNonPersonalizedPublisherIdChanged) {
            consentData.setConsentSource(ServerProtocol.DIALOG_PARAM_SDK_VERSION);
            consentData.setConsentStatus(ConsentStatus.UNKNOWN);
            consentData.setConsentedAdProviders(new HashSet());
        }
        saveConsentData(consentData);
    }

    public synchronized List<AdProvider> getAdProviders() {
        return new ArrayList(loadConsentData().getAdProviders());
    }

    /* access modifiers changed from: protected */
    public ConsentData loadConsentData() {
        String consentDataString = this.context.getSharedPreferences(PREFERENCES_FILE_KEY, 0).getString(CONSENT_DATA_KEY, "");
        if (TextUtils.isEmpty(consentDataString)) {
            return new ConsentData();
        }
        return (ConsentData) new Gson().fromJson(consentDataString, ConsentData.class);
    }

    private void saveConsentData(ConsentData consentData) {
        SharedPreferences.Editor editor = this.context.getSharedPreferences(PREFERENCES_FILE_KEY, 0).edit();
        editor.putString(CONSENT_DATA_KEY, new Gson().toJson((Object) consentData));
        editor.apply();
    }

    public boolean isRequestLocationInEeaOrUnknown() {
        return loadConsentData().isRequestLocationInEeaOrUnknown();
    }

    public void setConsentStatus(ConsentStatus consentStatus) {
        setConsentStatus(consentStatus, "programmatic");
    }

    /* access modifiers changed from: protected */
    public synchronized void setConsentStatus(ConsentStatus consentStatus, String source) {
        ConsentData consentData = loadConsentData();
        if (consentStatus == ConsentStatus.UNKNOWN) {
            consentData.setConsentedAdProviders(new HashSet());
        } else {
            consentData.setConsentedAdProviders(consentData.getAdProviders());
        }
        consentData.setConsentSource(source);
        consentData.setConsentStatus(consentStatus);
        saveConsentData(consentData);
    }

    public synchronized ConsentStatus getConsentStatus() {
        return loadConsentData().getConsentStatus();
    }
}
