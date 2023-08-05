package com.google.ads.consent;

public abstract class ConsentFormListener {
    public void onConsentFormLoaded() {
    }

    public void onConsentFormError(String reason) {
    }

    public void onConsentFormOpened() {
    }

    public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
    }
}
