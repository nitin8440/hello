package com.google.protobuf;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;

final class zze {
    private static final Logger logger = Logger.getLogger(zze.class.getName());
    private static final boolean zzcrM = zzLw();
    private static final long zzcrN = ((long) (zzcrM ? zzcrY.zzcsd.arrayBaseOffset(byte[].class) : -1));
    private static final Unsafe zzcrT = zzLv();
    private static final Class<?> zzcrU = zzhP("libcore.io.Memory");
    private static final boolean zzcrV = (zzhP("org.robolectric.Robolectric") != null);
    private static final boolean zzcrW = zzg(Long.TYPE);
    private static final boolean zzcrX = zzg(Integer.TYPE);
    private static final zzd zzcrY;
    private static final boolean zzcrZ = zzLy();
    private static final boolean zzcsa = zzLx();
    private static final long zzcsb;
    private static final boolean zzcsc;

    static final class zza extends zzd {
        zza(Unsafe unsafe) {
            super(unsafe);
        }
    }

    static final class zzb extends zzd {
        zzb(Unsafe unsafe) {
            super(unsafe);
        }
    }

    static final class zzc extends zzd {
        zzc(Unsafe unsafe) {
            super(unsafe);
        }
    }

    static abstract class zzd {
        Unsafe zzcsd;

        zzd(Unsafe unsafe) {
            this.zzcsd = unsafe;
        }
    }

    static {
        Field field;
        boolean z = false;
        zzd zzd2 = null;
        if (zzcrT != null) {
            if (!zzLz()) {
                zzd2 = new zzc(zzcrT);
            } else if (zzcrW) {
                zzd2 = new zzb(zzcrT);
            } else if (zzcrX) {
                zzd2 = new zza(zzcrT);
            }
        }
        zzcrY = zzd2;
        if (!zzLz() || (field = zza(Buffer.class, "effectiveDirectAddress")) == null) {
            field = zza(Buffer.class, "address");
        }
        zzcsb = (field == null || zzcrY == null) ? -1 : zzcrY.zzcsd.objectFieldOffset(field);
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            z = true;
        }
        zzcsc = z;
    }

    private zze() {
    }

    static boolean zzLt() {
        return zzcrM;
    }

    static long zzLu() {
        return zzcrN;
    }

    private static Unsafe zzLv() {
        try {
            return (Unsafe) AccessController.doPrivileged(new zzf());
        } catch (Throwable th) {
            return null;
        }
    }

    private static boolean zzLw() {
        if (zzcrT == null) {
            return false;
        }
        try {
            Class<?> cls = zzcrT.getClass();
            cls.getMethod("objectFieldOffset", new Class[]{Field.class});
            cls.getMethod("arrayBaseOffset", new Class[]{Class.class});
            cls.getMethod("getInt", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putInt", new Class[]{Object.class, Long.TYPE, Integer.TYPE});
            cls.getMethod("getLong", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putLong", new Class[]{Object.class, Long.TYPE, Long.TYPE});
            cls.getMethod("getObject", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putObject", new Class[]{Object.class, Long.TYPE, Object.class});
            if (zzLz()) {
                return true;
            }
            cls.getMethod("getByte", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putByte", new Class[]{Object.class, Long.TYPE, Byte.TYPE});
            cls.getMethod("getBoolean", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putBoolean", new Class[]{Object.class, Long.TYPE, Boolean.TYPE});
            cls.getMethod("getFloat", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putFloat", new Class[]{Object.class, Long.TYPE, Float.TYPE});
            cls.getMethod("getDouble", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putDouble", new Class[]{Object.class, Long.TYPE, Double.TYPE});
            return true;
        } catch (Throwable th) {
            Logger logger2 = logger;
            Level level = Level.WARNING;
            String valueOf = String.valueOf(th);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 71);
            sb.append("platform method missing - proto runtime falling back to safer methods: ");
            sb.append(valueOf);
            logger2.logp(level, "com.google.protobuf.UnsafeUtil", "supportsUnsafeArrayOperations", sb.toString());
            return false;
        }
    }

    private static boolean zzLx() {
        if (zzcrT == null) {
            return false;
        }
        try {
            zzcrT.getClass().getMethod("copyMemory", new Class[]{Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE});
            return true;
        } catch (Throwable th) {
            logger.logp(Level.WARNING, "com.google.protobuf.UnsafeUtil", "supportsUnsafeCopyMemory", "copyMemory is missing from platform - proto runtime falling back to safer methods.");
            return false;
        }
    }

    private static boolean zzLy() {
        if (zzcrT == null) {
            return false;
        }
        try {
            Class<?> cls = zzcrT.getClass();
            cls.getMethod("objectFieldOffset", new Class[]{Field.class});
            cls.getMethod("getLong", new Class[]{Object.class, Long.TYPE});
            if (zzLz()) {
                return true;
            }
            cls.getMethod("getByte", new Class[]{Long.TYPE});
            cls.getMethod("putByte", new Class[]{Long.TYPE, Byte.TYPE});
            cls.getMethod("getInt", new Class[]{Long.TYPE});
            cls.getMethod("putInt", new Class[]{Long.TYPE, Integer.TYPE});
            cls.getMethod("getLong", new Class[]{Long.TYPE});
            cls.getMethod("putLong", new Class[]{Long.TYPE, Long.TYPE});
            cls.getMethod("copyMemory", new Class[]{Long.TYPE, Long.TYPE, Long.TYPE});
            return true;
        } catch (Throwable th) {
            Logger logger2 = logger;
            Level level = Level.WARNING;
            String valueOf = String.valueOf(th);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 71);
            sb.append("platform method missing - proto runtime falling back to safer methods: ");
            sb.append(valueOf);
            logger2.logp(level, "com.google.protobuf.UnsafeUtil", "supportsUnsafeByteBufferOperations", sb.toString());
            return false;
        }
    }

    private static boolean zzLz() {
        return zzcrU != null && !zzcrV;
    }

    private static Field zza(Class<?> cls, String str) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable th) {
            return null;
        }
    }

    private static boolean zzg(Class<?> cls) {
        if (!zzLz()) {
            return false;
        }
        try {
            Class<?> cls2 = zzcrU;
            cls2.getMethod("peekLong", new Class[]{cls, Boolean.TYPE});
            cls2.getMethod("pokeLong", new Class[]{cls, Long.TYPE, Boolean.TYPE});
            cls2.getMethod("pokeInt", new Class[]{cls, Integer.TYPE, Boolean.TYPE});
            cls2.getMethod("peekInt", new Class[]{cls, Boolean.TYPE});
            cls2.getMethod("pokeByte", new Class[]{cls, Byte.TYPE});
            cls2.getMethod("peekByte", new Class[]{cls});
            cls2.getMethod("pokeByteArray", new Class[]{cls, byte[].class, Integer.TYPE, Integer.TYPE});
            cls2.getMethod("peekByteArray", new Class[]{cls, byte[].class, Integer.TYPE, Integer.TYPE});
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private static <T> Class<T> zzhP(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable th) {
            return null;
        }
    }
}
