package com.google.ads.consent;

public enum DebugGeography {
    DEBUG_GEOGRAPHY_DISABLED(0),
    DEBUG_GEOGRAPHY_EEA(1),
    DEBUG_GEOGRAPHY_NOT_EEA(2);
    
    private final int value;

    private DebugGeography(int value2) {
        this.value = value2;
    }

    public Integer getCode() {
        return Integer.valueOf(this.value);
    }
}
