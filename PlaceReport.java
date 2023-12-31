package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.appevents.internal.ViewHierarchyConstants;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzd;
import com.google.android.gms.common.internal.zzbe;
import com.google.android.gms.common.internal.zzbg;
import java.util.Arrays;

public class PlaceReport extends zza implements ReflectedParcelable {
    public static final Parcelable.Creator<PlaceReport> CREATOR = new zzl();
    private final String mTag;
    private final String zzaeK;
    private int zzaku;
    private final String zzbjI;

    PlaceReport(int i, String str, String str2, String str3) {
        this.zzaku = i;
        this.zzbjI = str;
        this.mTag = str2;
        this.zzaeK = str3;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.android.gms.location.places.PlaceReport create(java.lang.String r4, java.lang.String r5) {
        /*
            java.lang.String r0 = "unknown"
            com.google.android.gms.common.internal.zzbo.zzu(r4)
            com.google.android.gms.common.internal.zzbo.zzcF(r5)
            com.google.android.gms.common.internal.zzbo.zzcF(r0)
            int r1 = r0.hashCode()
            r2 = 0
            r3 = 1
            switch(r1) {
                case -1436706272: goto L_0x0047;
                case -1194968642: goto L_0x003d;
                case -284840886: goto L_0x0033;
                case -262743844: goto L_0x0029;
                case 1164924125: goto L_0x001f;
                case 1287171955: goto L_0x0015;
                default: goto L_0x0014;
            }
        L_0x0014:
            goto L_0x0051
        L_0x0015:
            java.lang.String r1 = "inferredRadioSignals"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = 3
            goto L_0x0052
        L_0x001f:
            java.lang.String r1 = "inferredSnappedToRoad"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = 5
            goto L_0x0052
        L_0x0029:
            java.lang.String r1 = "inferredReverseGeocoding"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = 4
            goto L_0x0052
        L_0x0033:
            java.lang.String r1 = "unknown"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = 0
            goto L_0x0052
        L_0x003d:
            java.lang.String r1 = "userReported"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = 1
            goto L_0x0052
        L_0x0047:
            java.lang.String r1 = "inferredGeofencing"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = 2
            goto L_0x0052
        L_0x0051:
            r1 = -1
        L_0x0052:
            switch(r1) {
                case 0: goto L_0x0056;
                case 1: goto L_0x0056;
                case 2: goto L_0x0056;
                case 3: goto L_0x0056;
                case 4: goto L_0x0056;
                case 5: goto L_0x0056;
                default: goto L_0x0055;
            }
        L_0x0055:
            goto L_0x0057
        L_0x0056:
            r2 = 1
        L_0x0057:
            java.lang.String r1 = "Invalid source"
            com.google.android.gms.common.internal.zzbo.zzb((boolean) r2, (java.lang.Object) r1)
            com.google.android.gms.location.places.PlaceReport r1 = new com.google.android.gms.location.places.PlaceReport
            r1.<init>(r3, r4, r5, r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.location.places.PlaceReport.create(java.lang.String, java.lang.String):com.google.android.gms.location.places.PlaceReport");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PlaceReport)) {
            return false;
        }
        PlaceReport placeReport = (PlaceReport) obj;
        return zzbe.equal(this.zzbjI, placeReport.zzbjI) && zzbe.equal(this.mTag, placeReport.mTag) && zzbe.equal(this.zzaeK, placeReport.zzaeK);
    }

    public String getPlaceId() {
        return this.zzbjI;
    }

    public String getTag() {
        return this.mTag;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzbjI, this.mTag, this.zzaeK});
    }

    public String toString() {
        zzbg zzt = zzbe.zzt(this);
        zzt.zzg("placeId", this.zzbjI);
        zzt.zzg(ViewHierarchyConstants.TAG_KEY, this.mTag);
        if (!"unknown".equals(this.zzaeK)) {
            zzt.zzg(ShareConstants.FEED_SOURCE_PARAM, this.zzaeK);
        }
        return zzt.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        int zze = zzd.zze(parcel);
        zzd.zzc(parcel, 1, this.zzaku);
        zzd.zza(parcel, 2, getPlaceId(), false);
        zzd.zza(parcel, 3, getTag(), false);
        zzd.zza(parcel, 4, this.zzaeK, false);
        zzd.zzI(parcel, zze);
    }
}
