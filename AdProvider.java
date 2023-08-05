package com.google.ads.consent;

import com.google.gson.annotations.SerializedName;

public class AdProvider {
    @SerializedName("company_id")

    /* renamed from: id */
    private String f41id;
    @SerializedName("company_name")
    private String name;
    @SerializedName("policy_url")
    private String privacyPolicyUrlString;

    public String getId() {
        return this.f41id;
    }

    public void setId(String id) {
        this.f41id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPrivacyPolicyUrlString() {
        return this.privacyPolicyUrlString;
    }

    public void setPrivacyPolicyUrlString(String privacyPolicyUrlString2) {
        this.privacyPolicyUrlString = privacyPolicyUrlString2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdProvider that = (AdProvider) o;
        if (!this.f41id.equals(that.f41id) || !this.name.equals(that.name) || !this.privacyPolicyUrlString.equals(that.privacyPolicyUrlString)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((this.f41id.hashCode() * 31) + this.name.hashCode()) * 31) + this.privacyPolicyUrlString.hashCode();
    }
}
