package com.google.ads.consent;

public interface ConsentInfoUpdateListener {
    void onConsentInfoUpdated(ConsentStatus consentStatus);

    void onFailedToUpdateConsentInfo(String str);
}
