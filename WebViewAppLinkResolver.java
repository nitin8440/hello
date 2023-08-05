package bolts;

import android.content.Context;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebViewAppLinkResolver implements AppLinkResolver {
    private static final String KEY_AL_VALUE = "value";
    private static final String KEY_ANDROID = "android";
    private static final String KEY_APP_NAME = "app_name";
    private static final String KEY_CLASS = "class";
    private static final String KEY_PACKAGE = "package";
    private static final String KEY_SHOULD_FALLBACK = "should_fallback";
    private static final String KEY_URL = "url";
    private static final String KEY_WEB = "web";
    private static final String KEY_WEB_URL = "url";
    private static final String META_TAG_PREFIX = "al";
    private static final String PREFER_HEADER = "Prefer-Html-Meta-Tags";
    private static final String TAG_EXTRACTION_JAVASCRIPT = "javascript:boltsWebViewAppLinkResolverResult.setValue((function() {  var metaTags = document.getElementsByTagName('meta');  var results = [];  for (var i = 0; i < metaTags.length; i++) {    var property = metaTags[i].getAttribute('property');    if (property && property.substring(0, 'al:'.length) === 'al:') {      var tag = { \"property\": metaTags[i].getAttribute('property') };      if (metaTags[i].hasAttribute('content')) {        tag['content'] = metaTags[i].getAttribute('content');      }      results.push(tag);    }  }  return JSON.stringify(results);})())";
    /* access modifiers changed from: private */
    public final Context context;

    public WebViewAppLinkResolver(Context context2) {
        this.context = context2;
    }

    public Task<AppLink> getAppLinkFromUrlInBackground(final Uri url) {
        final Capture<String> content = new Capture<>();
        final Capture<String> contentType = new Capture<>();
        return Task.callInBackground(new Callable<Void>() {
            public Void call() throws Exception {
                URL currentURL = new URL(url.toString());
                URLConnection connection = null;
                while (currentURL != null) {
                    connection = currentURL.openConnection();
                    if (connection instanceof HttpURLConnection) {
                        ((HttpURLConnection) connection).setInstanceFollowRedirects(true);
                    }
                    connection.setRequestProperty(WebViewAppLinkResolver.PREFER_HEADER, WebViewAppLinkResolver.META_TAG_PREFIX);
                    connection.connect();
                    if (connection instanceof HttpURLConnection) {
                        HttpURLConnection httpConnection = (HttpURLConnection) connection;
                        if (httpConnection.getResponseCode() < 300 || httpConnection.getResponseCode() >= 400) {
                            currentURL = null;
                        } else {
                            currentURL = new URL(httpConnection.getHeaderField("Location"));
                            httpConnection.disconnect();
                        }
                    } else {
                        currentURL = null;
                    }
                }
                try {
                    content.set(WebViewAppLinkResolver.readFromConnection(connection));
                    contentType.set(connection.getContentType());
                    return null;
                } finally {
                    if (connection instanceof HttpURLConnection) {
                        ((HttpURLConnection) connection).disconnect();
                    }
                }
            }
        }).onSuccessTask(new Continuation<Void, Task<JSONArray>>() {
            public Task<JSONArray> then(Task<Void> task) throws Exception {
                String inferredContentType;
                final TaskCompletionSource<JSONArray> tcs = new TaskCompletionSource<>();
                WebView webView = new WebView(WebViewAppLinkResolver.this.context);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setNetworkAvailable(false);
                webView.setWebViewClient(new WebViewClient() {
                    private boolean loaded = false;

                    private void runJavaScript(WebView view) {
                        if (!this.loaded) {
                            this.loaded = true;
                            view.loadUrl(WebViewAppLinkResolver.TAG_EXTRACTION_JAVASCRIPT);
                        }
                    }

                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        runJavaScript(view);
                    }

                    public void onLoadResource(WebView view, String url) {
                        super.onLoadResource(view, url);
                        runJavaScript(view);
                    }
                });
                webView.addJavascriptInterface(new Object() {
                    @JavascriptInterface
                    public void setValue(String value) {
                        try {
                            tcs.trySetResult(new JSONArray(value));
                        } catch (JSONException e) {
                            tcs.trySetError(e);
                        }
                    }
                }, "boltsWebViewAppLinkResolverResult");
                if (contentType.get() != null) {
                    inferredContentType = ((String) contentType.get()).split(";")[0];
                } else {
                    inferredContentType = null;
                }
                webView.loadDataWithBaseURL(url.toString(), (String) content.get(), inferredContentType, (String) null, (String) null);
                return tcs.getTask();
            }
        }, Task.UI_THREAD_EXECUTOR).onSuccess(new Continuation<JSONArray, AppLink>() {
            public AppLink then(Task<JSONArray> task) throws Exception {
                return WebViewAppLinkResolver.makeAppLinkFromAlData(WebViewAppLinkResolver.parseAlData(task.getResult()), url);
            }
        });
    }

    /* access modifiers changed from: private */
    public static Map<String, Object> parseAlData(JSONArray dataArray) throws JSONException {
        Map<String, Object> al = new HashMap<>();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject tag = dataArray.getJSONObject(i);
            String[] nameComponents = tag.getString("property").split(":");
            if (nameComponents[0].equals(META_TAG_PREFIX)) {
                Map<String, Object> root = al;
                int j = 1;
                while (true) {
                    Map<String, Object> child = null;
                    if (j >= nameComponents.length) {
                        break;
                    }
                    List<Map<String, Object>> children = (List) root.get(nameComponents[j]);
                    if (children == null) {
                        children = new ArrayList<>();
                        root.put(nameComponents[j], children);
                    }
                    if (children.size() > 0) {
                        child = children.get(children.size() - 1);
                    }
                    if (child == null || j == nameComponents.length - 1) {
                        child = new HashMap<>();
                        children.add(child);
                    }
                    root = child;
                    j++;
                }
                if (tag.has("content")) {
                    if (tag.isNull("content")) {
                        root.put(KEY_AL_VALUE, (Object) null);
                    } else {
                        root.put(KEY_AL_VALUE, tag.getString("content"));
                    }
                }
            }
        }
        return al;
    }

    private static List<Map<String, Object>> getAlList(Map<String, Object> map, String key) {
        List<Map<String, Object>> result = (List) map.get(key);
        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r16v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r16v1, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v15, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r16v2, resolved type: java.lang.String} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static bolts.AppLink makeAppLinkFromAlData(java.util.Map<java.lang.String, java.lang.Object> r18, android.net.Uri r19) {
        /*
            r0 = r18
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.lang.String r2 = "android"
            java.lang.Object r2 = r0.get(r2)
            java.util.List r2 = (java.util.List) r2
            if (r2 != 0) goto L_0x0015
            java.util.List r2 = java.util.Collections.emptyList()
        L_0x0015:
            java.util.Iterator r3 = r2.iterator()
        L_0x0019:
            boolean r4 = r3.hasNext()
            r5 = 0
            if (r4 == 0) goto L_0x00d3
            java.lang.Object r4 = r3.next()
            java.util.Map r4 = (java.util.Map) r4
            java.lang.String r6 = "url"
            java.util.List r6 = getAlList(r4, r6)
            java.lang.String r7 = "package"
            java.util.List r7 = getAlList(r4, r7)
            java.lang.String r8 = "class"
            java.util.List r8 = getAlList(r4, r8)
            java.lang.String r9 = "app_name"
            java.util.List r9 = getAlList(r4, r9)
            int r10 = r6.size()
            int r11 = r7.size()
            int r12 = r8.size()
            int r13 = r9.size()
            int r12 = java.lang.Math.max(r12, r13)
            int r11 = java.lang.Math.max(r11, r12)
            int r10 = java.lang.Math.max(r10, r11)
        L_0x005b:
            if (r5 >= r10) goto L_0x00cf
            int r11 = r6.size()
            if (r11 <= r5) goto L_0x0070
            java.lang.Object r11 = r6.get(r5)
            java.util.Map r11 = (java.util.Map) r11
            java.lang.String r13 = "value"
            java.lang.Object r11 = r11.get(r13)
            goto L_0x0071
        L_0x0070:
            r11 = 0
        L_0x0071:
            java.lang.String r11 = (java.lang.String) r11
            android.net.Uri r13 = tryCreateUrl(r11)
            int r14 = r7.size()
            if (r14 <= r5) goto L_0x008a
            java.lang.Object r14 = r7.get(r5)
            java.util.Map r14 = (java.util.Map) r14
            java.lang.String r15 = "value"
            java.lang.Object r14 = r14.get(r15)
            goto L_0x008b
        L_0x008a:
            r14 = 0
        L_0x008b:
            java.lang.String r14 = (java.lang.String) r14
            int r15 = r8.size()
            if (r15 <= r5) goto L_0x00a0
            java.lang.Object r15 = r8.get(r5)
            java.util.Map r15 = (java.util.Map) r15
            java.lang.String r12 = "value"
            java.lang.Object r12 = r15.get(r12)
            goto L_0x00a1
        L_0x00a0:
            r12 = 0
        L_0x00a1:
            java.lang.String r12 = (java.lang.String) r12
            int r15 = r9.size()
            if (r15 <= r5) goto L_0x00ba
            java.lang.Object r15 = r9.get(r5)
            java.util.Map r15 = (java.util.Map) r15
            r17 = r2
            java.lang.String r2 = "value"
            java.lang.Object r2 = r15.get(r2)
            r16 = r2
            goto L_0x00be
        L_0x00ba:
            r17 = r2
            r16 = 0
        L_0x00be:
            r2 = r16
            java.lang.String r2 = (java.lang.String) r2
            bolts.AppLink$Target r15 = new bolts.AppLink$Target
            r15.<init>(r14, r12, r13, r2)
            r1.add(r15)
            int r5 = r5 + 1
            r2 = r17
            goto L_0x005b
        L_0x00cf:
            r17 = r2
            goto L_0x0019
        L_0x00d3:
            r17 = r2
            r2 = r19
            java.lang.String r3 = "web"
            java.lang.Object r3 = r0.get(r3)
            java.util.List r3 = (java.util.List) r3
            if (r3 == 0) goto L_0x014f
            int r4 = r3.size()
            if (r4 <= 0) goto L_0x014f
            java.lang.Object r4 = r3.get(r5)
            java.util.Map r4 = (java.util.Map) r4
            java.lang.String r6 = "url"
            java.lang.Object r6 = r4.get(r6)
            java.util.List r6 = (java.util.List) r6
            java.lang.String r7 = "should_fallback"
            java.lang.Object r7 = r4.get(r7)
            java.util.List r7 = (java.util.List) r7
            if (r7 == 0) goto L_0x0133
            int r8 = r7.size()
            if (r8 <= 0) goto L_0x0133
            java.lang.Object r8 = r7.get(r5)
            java.util.Map r8 = (java.util.Map) r8
            java.lang.String r9 = "value"
            java.lang.Object r8 = r8.get(r9)
            java.lang.String r8 = (java.lang.String) r8
            r9 = 3
            java.lang.String[] r9 = new java.lang.String[r9]
            java.lang.String r10 = "no"
            r9[r5] = r10
            java.lang.String r10 = "false"
            r11 = 1
            r9[r11] = r10
            r10 = 2
            java.lang.String r11 = "0"
            r9[r10] = r11
            java.util.List r9 = java.util.Arrays.asList(r9)
            java.lang.String r10 = r8.toLowerCase()
            boolean r9 = r9.contains(r10)
            if (r9 == 0) goto L_0x0133
            r2 = 0
        L_0x0133:
            if (r2 == 0) goto L_0x014f
            if (r6 == 0) goto L_0x014f
            int r8 = r6.size()
            if (r8 <= 0) goto L_0x014f
            java.lang.Object r5 = r6.get(r5)
            java.util.Map r5 = (java.util.Map) r5
            java.lang.String r8 = "value"
            java.lang.Object r5 = r5.get(r8)
            java.lang.String r5 = (java.lang.String) r5
            android.net.Uri r2 = tryCreateUrl(r5)
        L_0x014f:
            bolts.AppLink r4 = new bolts.AppLink
            r5 = r19
            r4.<init>(r5, r1, r2)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: bolts.WebViewAppLinkResolver.makeAppLinkFromAlData(java.util.Map, android.net.Uri):bolts.AppLink");
    }

    private static Uri tryCreateUrl(String urlString) {
        if (urlString == null) {
            return null;
        }
        return Uri.parse(urlString);
    }

    /* access modifiers changed from: private */
    public static String readFromConnection(URLConnection connection) throws IOException {
        InputStream stream;
        if (connection instanceof HttpURLConnection) {
            try {
                stream = connection.getInputStream();
            } catch (Exception e) {
                stream = ((HttpURLConnection) connection).getErrorStream();
            }
        } else {
            stream = connection.getInputStream();
        }
        InputStream stream2 = stream;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int i$ = 0;
            while (true) {
                int read = stream2.read(buffer);
                int read2 = read;
                if (read == -1) {
                    break;
                }
                output.write(buffer, 0, read2);
            }
            String charset = connection.getContentEncoding();
            if (charset == null) {
                String[] arr$ = connection.getContentType().split(";");
                int len$ = arr$.length;
                while (true) {
                    if (i$ >= len$) {
                        break;
                    }
                    String part = arr$[i$].trim();
                    if (part.startsWith("charset=")) {
                        charset = part.substring("charset=".length());
                        break;
                    }
                    i$++;
                }
                if (charset == null) {
                    charset = "UTF-8";
                }
            }
            return new String(output.toByteArray(), charset);
        } finally {
            stream2.close();
        }
    }
}
