package bolts;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import bolts.AppLink;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.applinks.AppLinkData;
import com.facebook.internal.AnalyticsEvents;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppLinkNavigation {
    private static final String KEY_NAME_REFERER_APP_LINK = "referer_app_link";
    private static final String KEY_NAME_REFERER_APP_LINK_APP_NAME = "app_name";
    private static final String KEY_NAME_REFERER_APP_LINK_PACKAGE = "package";
    private static final String KEY_NAME_USER_AGENT = "user_agent";
    private static final String KEY_NAME_VERSION = "version";
    private static final String VERSION = "1.0";
    private static AppLinkResolver defaultResolver;
    private final AppLink appLink;
    private final Bundle appLinkData;
    private final Bundle extras;

    public enum NavigationResult {
        FAILED("failed", false),
        WEB(AnalyticsEvents.PARAMETER_SHARE_DIALOG_SHOW_WEB, true),
        APP("app", true);
        
        private String code;
        private boolean succeeded;

        public String getCode() {
            return this.code;
        }

        public boolean isSucceeded() {
            return this.succeeded;
        }

        private NavigationResult(String code2, boolean success) {
            this.code = code2;
            this.succeeded = success;
        }
    }

    public AppLinkNavigation(AppLink appLink2, Bundle extras2, Bundle appLinkData2) {
        if (appLink2 != null) {
            extras2 = extras2 == null ? new Bundle() : extras2;
            appLinkData2 = appLinkData2 == null ? new Bundle() : appLinkData2;
            this.appLink = appLink2;
            this.extras = extras2;
            this.appLinkData = appLinkData2;
            return;
        }
        throw new IllegalArgumentException("appLink must not be null.");
    }

    public AppLink getAppLink() {
        return this.appLink;
    }

    public Bundle getAppLinkData() {
        return this.appLinkData;
    }

    public Bundle getExtras() {
        return this.extras;
    }

    private Bundle buildAppLinkDataForNavigation(Context context) {
        String refererAppName;
        Bundle data = new Bundle();
        Bundle refererAppLinkData = new Bundle();
        if (context != null) {
            String refererAppPackage = context.getPackageName();
            if (refererAppPackage != null) {
                refererAppLinkData.putString(KEY_NAME_REFERER_APP_LINK_PACKAGE, refererAppPackage);
            }
            ApplicationInfo appInfo = context.getApplicationInfo();
            if (!(appInfo == null || (refererAppName = context.getString(appInfo.labelRes)) == null)) {
                refererAppLinkData.putString("app_name", refererAppName);
            }
        }
        data.putAll(getAppLinkData());
        data.putString("target_url", getAppLink().getSourceUrl().toString());
        data.putString("version", "1.0");
        data.putString(KEY_NAME_USER_AGENT, "Bolts Android 1.4.0");
        data.putBundle(KEY_NAME_REFERER_APP_LINK, refererAppLinkData);
        data.putBundle(AppLinkData.ARGUMENTS_EXTRAS_KEY, getExtras());
        return data;
    }

    private Object getJSONValue(Object value) throws JSONException {
        if (value instanceof Bundle) {
            return getJSONForBundle((Bundle) value);
        }
        if (value instanceof CharSequence) {
            return value.toString();
        }
        if (value instanceof List) {
            JSONArray array = new JSONArray();
            for (Object listValue : (List) value) {
                array.put(getJSONValue(listValue));
            }
            return array;
        }
        int i = 0;
        if (value instanceof SparseArray) {
            JSONArray array2 = new JSONArray();
            SparseArray<?> sparseValue = (SparseArray) value;
            while (i < sparseValue.size()) {
                array2.put(sparseValue.keyAt(i), getJSONValue(sparseValue.valueAt(i)));
                i++;
            }
            return array2;
        } else if (value instanceof Character) {
            return value.toString();
        } else {
            if (value instanceof Boolean) {
                return value;
            }
            if (value instanceof Number) {
                if ((value instanceof Double) || (value instanceof Float)) {
                    return Double.valueOf(((Number) value).doubleValue());
                }
                return Long.valueOf(((Number) value).longValue());
            } else if (value instanceof boolean[]) {
                JSONArray array3 = new JSONArray();
                boolean[] arr$ = (boolean[]) value;
                int len$ = arr$.length;
                while (i < len$) {
                    array3.put(getJSONValue(Boolean.valueOf(arr$[i])));
                    i++;
                }
                return array3;
            } else if (value instanceof char[]) {
                JSONArray array4 = new JSONArray();
                char[] arr$2 = (char[]) value;
                int len$2 = arr$2.length;
                while (i < len$2) {
                    array4.put(getJSONValue(Character.valueOf(arr$2[i])));
                    i++;
                }
                return array4;
            } else if (value instanceof CharSequence[]) {
                JSONArray array5 = new JSONArray();
                CharSequence[] arr$3 = (CharSequence[]) value;
                int len$3 = arr$3.length;
                while (i < len$3) {
                    array5.put(getJSONValue(arr$3[i]));
                    i++;
                }
                return array5;
            } else if (value instanceof double[]) {
                JSONArray array6 = new JSONArray();
                double[] arr$4 = (double[]) value;
                int len$4 = arr$4.length;
                while (i < len$4) {
                    array6.put(getJSONValue(Double.valueOf(arr$4[i])));
                    i++;
                }
                return array6;
            } else if (value instanceof float[]) {
                JSONArray array7 = new JSONArray();
                float[] arr$5 = (float[]) value;
                int len$5 = arr$5.length;
                while (i < len$5) {
                    array7.put(getJSONValue(Float.valueOf(arr$5[i])));
                    i++;
                }
                return array7;
            } else if (value instanceof int[]) {
                JSONArray array8 = new JSONArray();
                int[] arr$6 = (int[]) value;
                int len$6 = arr$6.length;
                while (i < len$6) {
                    array8.put(getJSONValue(Integer.valueOf(arr$6[i])));
                    i++;
                }
                return array8;
            } else if (value instanceof long[]) {
                JSONArray array9 = new JSONArray();
                long[] arr$7 = (long[]) value;
                int len$7 = arr$7.length;
                while (i < len$7) {
                    array9.put(getJSONValue(Long.valueOf(arr$7[i])));
                    i++;
                }
                return array9;
            } else if (value instanceof short[]) {
                JSONArray array10 = new JSONArray();
                short[] arr$8 = (short[]) value;
                int len$8 = arr$8.length;
                while (i < len$8) {
                    array10.put(getJSONValue(Short.valueOf(arr$8[i])));
                    i++;
                }
                return array10;
            } else if (!(value instanceof String[])) {
                return null;
            } else {
                JSONArray array11 = new JSONArray();
                String[] arr$9 = (String[]) value;
                int len$9 = arr$9.length;
                while (i < len$9) {
                    array11.put(getJSONValue(arr$9[i]));
                    i++;
                }
                return array11;
            }
        }
    }

    private JSONObject getJSONForBundle(Bundle bundle) throws JSONException {
        JSONObject root = new JSONObject();
        for (String key : bundle.keySet()) {
            root.put(key, getJSONValue(bundle.get(key)));
        }
        return root;
    }

    public NavigationResult navigate(Context context) {
        PackageManager pm = context.getPackageManager();
        Bundle finalAppLinkData = buildAppLinkDataForNavigation(context);
        Intent eligibleTargetIntent = null;
        Iterator i$ = getAppLink().getTargets().iterator();
        while (true) {
            if (!i$.hasNext()) {
                break;
            }
            AppLink.Target target = i$.next();
            Intent targetIntent = new Intent("android.intent.action.VIEW");
            if (target.getUrl() != null) {
                targetIntent.setData(target.getUrl());
            } else {
                targetIntent.setData(this.appLink.getSourceUrl());
            }
            targetIntent.setPackage(target.getPackageName());
            if (target.getClassName() != null) {
                targetIntent.setClassName(target.getPackageName(), target.getClassName());
            }
            targetIntent.putExtra("al_applink_data", finalAppLinkData);
            ResolveInfo resolved = pm.resolveActivity(targetIntent, 65536);
            if (resolved != null) {
                eligibleTargetIntent = targetIntent;
                ResolveInfo resolveInfo = resolved;
                break;
            }
            ResolveInfo resolveInfo2 = resolved;
        }
        Intent outIntent = null;
        NavigationResult result = NavigationResult.FAILED;
        if (eligibleTargetIntent != null) {
            outIntent = eligibleTargetIntent;
            result = NavigationResult.APP;
        } else {
            Uri webUrl = getAppLink().getWebUrl();
            if (webUrl != null) {
                try {
                    outIntent = new Intent("android.intent.action.VIEW", webUrl.buildUpon().appendQueryParameter("al_applink_data", getJSONForBundle(finalAppLinkData).toString()).build());
                    result = NavigationResult.WEB;
                } catch (JSONException e) {
                    sendAppLinkNavigateEventBroadcast(context, eligibleTargetIntent, NavigationResult.FAILED, e);
                    throw new RuntimeException(e);
                }
            }
        }
        sendAppLinkNavigateEventBroadcast(context, outIntent, result, (JSONException) null);
        if (outIntent != null) {
            context.startActivity(outIntent);
        }
        return result;
    }

    private void sendAppLinkNavigateEventBroadcast(Context context, Intent intent, NavigationResult type, JSONException e) {
        Map<String, String> extraLoggingData = new HashMap<>();
        if (e != null) {
            extraLoggingData.put("error", e.getLocalizedMessage());
        }
        extraLoggingData.put(GraphResponse.SUCCESS_KEY, type.isSucceeded() ? AppEventsConstants.EVENT_PARAM_VALUE_YES : AppEventsConstants.EVENT_PARAM_VALUE_NO);
        extraLoggingData.put("type", type.getCode());
        MeasurementEvent.sendBroadcastEvent(context, MeasurementEvent.APP_LINK_NAVIGATE_OUT_EVENT_NAME, intent, extraLoggingData);
    }

    public static void setDefaultResolver(AppLinkResolver resolver) {
        defaultResolver = resolver;
    }

    public static AppLinkResolver getDefaultResolver() {
        return defaultResolver;
    }

    private static AppLinkResolver getResolver(Context context) {
        if (getDefaultResolver() != null) {
            return getDefaultResolver();
        }
        return new WebViewAppLinkResolver(context);
    }

    public static NavigationResult navigate(Context context, AppLink appLink2) {
        return new AppLinkNavigation(appLink2, (Bundle) null, (Bundle) null).navigate(context);
    }

    public static Task<NavigationResult> navigateInBackground(final Context context, Uri destination, AppLinkResolver resolver) {
        return resolver.getAppLinkFromUrlInBackground(destination).onSuccess(new Continuation<AppLink, NavigationResult>() {
            public NavigationResult then(Task<AppLink> task) throws Exception {
                return AppLinkNavigation.navigate(context, task.getResult());
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    public static Task<NavigationResult> navigateInBackground(Context context, URL destination, AppLinkResolver resolver) {
        return navigateInBackground(context, Uri.parse(destination.toString()), resolver);
    }

    public static Task<NavigationResult> navigateInBackground(Context context, String destinationUrl, AppLinkResolver resolver) {
        return navigateInBackground(context, Uri.parse(destinationUrl), resolver);
    }

    public static Task<NavigationResult> navigateInBackground(Context context, Uri destination) {
        return navigateInBackground(context, destination, getResolver(context));
    }

    public static Task<NavigationResult> navigateInBackground(Context context, URL destination) {
        return navigateInBackground(context, destination, getResolver(context));
    }

    public static Task<NavigationResult> navigateInBackground(Context context, String destinationUrl) {
        return navigateInBackground(context, destinationUrl, getResolver(context));
    }
}
