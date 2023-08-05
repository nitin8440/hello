package com.google.zxing.client.result;

import com.facebook.share.internal.ShareConstants;
import com.google.zxing.Result;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VCardResultParser extends ResultParser {
    private static final Pattern BEGIN_VCARD = Pattern.compile("BEGIN:VCARD", 2);
    private static final Pattern COMMA = Pattern.compile(",");
    private static final Pattern CR_LF_SPACE_TAB = Pattern.compile("\r\n[ \t]");
    private static final Pattern EQUALS = Pattern.compile("=");
    private static final Pattern NEWLINE_ESCAPE = Pattern.compile("\\\\[nN]");
    private static final Pattern SEMICOLON = Pattern.compile(";");
    private static final Pattern SEMICOLON_OR_COMMA = Pattern.compile("[;,]");
    private static final Pattern UNESCAPED_SEMICOLONS = Pattern.compile("(?<!\\\\);+");
    private static final Pattern VCARD_ESCAPES = Pattern.compile("\\\\([,;\\\\])");
    private static final Pattern VCARD_LIKE_DATE = Pattern.compile("\\d{4}-?\\d{2}-?\\d{2}");

    public AddressBookParsedResult parse(Result result) {
        String rawText = getMassagedText(result);
        Matcher matcher = BEGIN_VCARD.matcher(rawText);
        Matcher m = matcher;
        if (!matcher.find() || m.start() != 0) {
            return null;
        }
        List<List<String>> matchVCardPrefixedField = matchVCardPrefixedField("FN", rawText, true, false);
        List<List<String>> names = matchVCardPrefixedField;
        if (matchVCardPrefixedField == null) {
            List<List<String>> matchVCardPrefixedField2 = matchVCardPrefixedField("N", rawText, true, false);
            names = matchVCardPrefixedField2;
            formatNames(matchVCardPrefixedField2);
        }
        List<String> nicknameString = matchSingleVCardPrefixedField("NICKNAME", rawText, true, false);
        String[] nicknames = nicknameString == null ? null : COMMA.split(nicknameString.get(0));
        List<List<String>> phoneNumbers = matchVCardPrefixedField("TEL", rawText, true, false);
        List<List<String>> emails = matchVCardPrefixedField("EMAIL", rawText, true, false);
        List<String> note = matchSingleVCardPrefixedField("NOTE", rawText, false, false);
        List<List<String>> addresses = matchVCardPrefixedField("ADR", rawText, true, true);
        List<String> org = matchSingleVCardPrefixedField("ORG", rawText, true, true);
        List<String> matchSingleVCardPrefixedField = matchSingleVCardPrefixedField("BDAY", rawText, true, false);
        List<String> birthday = matchSingleVCardPrefixedField;
        if (matchSingleVCardPrefixedField != null && !isLikeVCardDate(birthday.get(0))) {
            birthday = null;
        }
        List<String> birthday2 = birthday;
        List<String> title = matchSingleVCardPrefixedField(ShareConstants.TITLE, rawText, true, false);
        List<List<String>> urls = matchVCardPrefixedField("URL", rawText, true, false);
        List<String> instantMessenger = matchSingleVCardPrefixedField("IMPP", rawText, true, false);
        List<String> matchSingleVCardPrefixedField2 = matchSingleVCardPrefixedField("GEO", rawText, true, false);
        List<String> geoString = matchSingleVCardPrefixedField2;
        String[] split = matchSingleVCardPrefixedField2 == null ? null : SEMICOLON_OR_COMMA.split(geoString.get(0));
        String[] geo = split;
        if (!(split == null || geo.length == 2)) {
            geo = null;
        }
        List<String> list = geoString;
        return new AddressBookParsedResult(toPrimaryValues(names), nicknames, (String) null, toPrimaryValues(phoneNumbers), toTypes(phoneNumbers), toPrimaryValues(emails), toTypes(emails), toPrimaryValue(instantMessenger), toPrimaryValue(note), toPrimaryValues(addresses), toTypes(addresses), toPrimaryValue(org), toPrimaryValue(birthday2), toPrimaryValue(title), toPrimaryValues(urls), geo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:78:0x019f A[SYNTHETIC, Splitter:B:78:0x019f] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01af  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x01c0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.util.List<java.util.List<java.lang.String>> matchVCardPrefixedField(java.lang.CharSequence r24, java.lang.String r25, boolean r26, boolean r27) {
        /*
            r1 = r25
            r0 = 0
            r2 = 0
            int r3 = r25.length()
            r4 = 0
            r5 = r4
        L_0x000a:
            if (r2 >= r3) goto L_0x01d4
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "(?:^|\n)"
            r6.<init>(r7)
            r7 = r24
            r6.append(r7)
            java.lang.String r8 = "(?:;([^:]*))?:"
            r6.append(r8)
            java.lang.String r6 = r6.toString()
            r8 = 2
            java.util.regex.Pattern r6 = java.util.regex.Pattern.compile(r6, r8)
            java.util.regex.Matcher r6 = r6.matcher(r1)
            if (r2 <= 0) goto L_0x002e
            int r2 = r2 + -1
        L_0x002e:
            boolean r9 = r6.find(r2)
            if (r9 == 0) goto L_0x01d4
            r9 = 0
            int r2 = r6.end(r9)
            r10 = 1
            java.lang.String r11 = r6.group(r10)
            r12 = 0
            r13 = 0
            r14 = 0
            r15 = 0
            if (r11 == 0) goto L_0x00b8
            java.util.regex.Pattern r9 = SEMICOLON
            java.lang.String[] r9 = r9.split(r11)
            int r8 = r9.length
            r16 = r15
            r15 = r14
            r14 = r13
            r13 = r4
            r4 = 0
        L_0x0051:
            if (r4 >= r8) goto L_0x00ae
            r10 = r9[r4]
            if (r12 != 0) goto L_0x0063
            r17 = r2
            java.util.ArrayList r2 = new java.util.ArrayList
            r18 = r6
            r6 = 1
            r2.<init>(r6)
            r12 = r2
            goto L_0x0067
        L_0x0063:
            r17 = r2
            r18 = r6
        L_0x0067:
            r12.add(r10)
            java.util.regex.Pattern r2 = EQUALS
            r6 = 2
            java.lang.String[] r2 = r2.split(r10, r6)
            r6 = r13
            r13 = r2
            int r2 = r2.length
            r6 = 1
            if (r2 <= r6) goto L_0x00a4
            r2 = 0
            r7 = r13[r2]
            r2 = r13[r6]
            java.lang.String r6 = "ENCODING"
            boolean r6 = r6.equalsIgnoreCase(r7)
            if (r6 == 0) goto L_0x008f
            java.lang.String r6 = "QUOTED-PRINTABLE"
            boolean r6 = r6.equalsIgnoreCase(r2)
            if (r6 == 0) goto L_0x008f
            r6 = 1
            r14 = r6
            goto L_0x00a4
        L_0x008f:
            java.lang.String r6 = "CHARSET"
            boolean r6 = r6.equalsIgnoreCase(r7)
            if (r6 == 0) goto L_0x009a
            r6 = r2
            r15 = r6
            goto L_0x00a4
        L_0x009a:
            java.lang.String r6 = "VALUE"
            boolean r6 = r6.equalsIgnoreCase(r7)
            if (r6 == 0) goto L_0x00a4
            r16 = r2
        L_0x00a4:
            int r4 = r4 + 1
            r2 = r17
            r6 = r18
            r7 = r24
            r10 = 1
            goto L_0x0051
        L_0x00ae:
            r17 = r2
            r18 = r6
            r4 = r13
            r13 = r14
            r14 = r15
            r2 = r16
            goto L_0x00bd
        L_0x00b8:
            r17 = r2
            r18 = r6
            r2 = r15
        L_0x00bd:
            r6 = r17
        L_0x00bf:
            r7 = r17
            r8 = 10
            int r8 = r1.indexOf(r8, r6)
            r6 = r8
            if (r8 < 0) goto L_0x010a
            int r8 = r25.length()
            r9 = 1
            int r8 = r8 - r9
            if (r6 >= r8) goto L_0x00eb
            int r8 = r6 + 1
            char r8 = r1.charAt(r8)
            r9 = 32
            if (r8 == r9) goto L_0x00e6
            int r8 = r6 + 1
            char r8 = r1.charAt(r8)
            r9 = 9
            if (r8 != r9) goto L_0x00eb
        L_0x00e6:
            int r6 = r6 + 2
        L_0x00e8:
            r17 = r7
            goto L_0x00bf
        L_0x00eb:
            if (r13 == 0) goto L_0x010a
            r8 = 61
            if (r6 <= 0) goto L_0x00fc
            int r9 = r6 + -1
            char r9 = r1.charAt(r9)
            if (r9 == r8) goto L_0x00fa
            goto L_0x00fc
        L_0x00fa:
            r9 = 2
            goto L_0x0107
        L_0x00fc:
            r9 = 2
            if (r6 < r9) goto L_0x010a
            int r10 = r6 + -2
            char r10 = r1.charAt(r10)
            if (r10 != r8) goto L_0x010a
        L_0x0107:
            int r6 = r6 + 1
            goto L_0x00e8
        L_0x010a:
            if (r6 >= 0) goto L_0x0110
            r6 = r3
            r2 = r6
            goto L_0x000a
        L_0x0110:
            if (r6 <= r7) goto L_0x01cd
            if (r0 != 0) goto L_0x011c
            java.util.ArrayList r8 = new java.util.ArrayList
            r9 = 1
            r8.<init>(r9)
            r0 = r8
            goto L_0x011d
        L_0x011c:
            r8 = r0
        L_0x011d:
            if (r6 <= 0) goto L_0x012b
            int r0 = r6 + -1
            char r0 = r1.charAt(r0)
            r9 = 13
            if (r0 != r9) goto L_0x012b
            int r6 = r6 + -1
        L_0x012b:
            java.lang.String r0 = r1.substring(r7, r6)
            if (r26 == 0) goto L_0x0135
            java.lang.String r0 = r0.trim()
        L_0x0135:
            if (r13 == 0) goto L_0x0155
            java.lang.String r0 = decodeQuotedPrintable(r0, r14)
            if (r27 == 0) goto L_0x0152
            java.util.regex.Pattern r10 = UNESCAPED_SEMICOLONS
            java.util.regex.Matcher r10 = r10.matcher(r0)
            r19 = r0
            java.lang.String r0 = "\n"
            java.lang.String r0 = r10.replaceAll(r0)
            java.lang.String r0 = r0.trim()
        L_0x014f:
            r19 = r0
            goto L_0x0197
        L_0x0152:
            r19 = r0
            goto L_0x0197
        L_0x0155:
            if (r27 == 0) goto L_0x016a
            java.util.regex.Pattern r10 = UNESCAPED_SEMICOLONS
            java.util.regex.Matcher r10 = r10.matcher(r0)
            r20 = r0
            java.lang.String r0 = "\n"
            java.lang.String r0 = r10.replaceAll(r0)
            java.lang.String r0 = r0.trim()
            goto L_0x016c
        L_0x016a:
            r20 = r0
        L_0x016c:
            java.util.regex.Pattern r10 = CR_LF_SPACE_TAB
            java.util.regex.Matcher r10 = r10.matcher(r0)
            r21 = r0
            java.lang.String r0 = ""
            java.lang.String r0 = r10.replaceAll(r0)
            java.util.regex.Pattern r10 = NEWLINE_ESCAPE
            java.util.regex.Matcher r10 = r10.matcher(r0)
            r22 = r0
            java.lang.String r0 = "\n"
            java.lang.String r0 = r10.replaceAll(r0)
            java.util.regex.Pattern r10 = VCARD_ESCAPES
            java.util.regex.Matcher r10 = r10.matcher(r0)
            r23 = r0
            java.lang.String r0 = "$1"
            java.lang.String r0 = r10.replaceAll(r0)
            goto L_0x014f
        L_0x0197:
            java.lang.String r0 = "uri"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x01ab
            java.net.URI r0 = java.net.URI.create(r19)     // Catch:{ IllegalArgumentException -> 0x01aa }
            java.lang.String r0 = r0.getSchemeSpecificPart()     // Catch:{ IllegalArgumentException -> 0x01aa }
            r19 = r0
            goto L_0x01ad
        L_0x01aa:
            r0 = move-exception
        L_0x01ab:
            r0 = r19
        L_0x01ad:
            if (r12 != 0) goto L_0x01c0
            java.util.ArrayList r10 = new java.util.ArrayList
            r1 = 1
            r10.<init>(r1)
            r1 = r5
            r1 = r10
            r10.add(r0)
            r8.add(r1)
            r5 = r1
            goto L_0x01c7
        L_0x01c0:
            r1 = 0
            r12.add(r1, r0)
            r8.add(r12)
        L_0x01c7:
            int r0 = r6 + 1
            r2 = r0
            r0 = r8
            goto L_0x01d0
        L_0x01cd:
            int r2 = r6 + 1
        L_0x01d0:
            r1 = r25
            goto L_0x000a
        L_0x01d4:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.client.result.VCardResultParser.matchVCardPrefixedField(java.lang.CharSequence, java.lang.String, boolean, boolean):java.util.List");
    }

    private static String decodeQuotedPrintable(CharSequence value, String charset) {
        int length = value.length();
        StringBuilder result = new StringBuilder(length);
        ByteArrayOutputStream fragmentBuffer = new ByteArrayOutputStream();
        int i = 0;
        while (i < length) {
            char charAt = value.charAt(i);
            char c = charAt;
            if (!(charAt == 10 || charAt == 13)) {
                if (charAt != '=') {
                    maybeAppendFragment(fragmentBuffer, charset, result);
                    result.append(c);
                } else if (i < length - 2) {
                    char charAt2 = value.charAt(i + 1);
                    char nextChar = charAt2;
                    if (!(charAt2 == 13 || nextChar == 10)) {
                        char nextNextChar = value.charAt(i + 2);
                        int firstDigit = parseHexDigit(nextChar);
                        int secondDigit = parseHexDigit(nextNextChar);
                        if (firstDigit >= 0 && secondDigit >= 0) {
                            fragmentBuffer.write((firstDigit << 4) + secondDigit);
                        }
                        i += 2;
                    }
                }
            }
            i++;
        }
        maybeAppendFragment(fragmentBuffer, charset, result);
        return result.toString();
    }

    private static void maybeAppendFragment(ByteArrayOutputStream fragmentBuffer, String charset, StringBuilder result) {
        String fragment;
        if (fragmentBuffer.size() > 0) {
            byte[] fragmentBytes = fragmentBuffer.toByteArray();
            if (charset == null) {
                fragment = new String(fragmentBytes, StandardCharsets.UTF_8);
            } else {
                try {
                    fragment = new String(fragmentBytes, charset);
                } catch (UnsupportedEncodingException e) {
                    fragment = new String(fragmentBytes, StandardCharsets.UTF_8);
                }
            }
            fragmentBuffer.reset();
            result.append(fragment);
        }
    }

    static List<String> matchSingleVCardPrefixedField(CharSequence prefix, String rawText, boolean trim, boolean parseFieldDivider) {
        List<List<String>> matchVCardPrefixedField = matchVCardPrefixedField(prefix, rawText, trim, parseFieldDivider);
        List<List<String>> values = matchVCardPrefixedField;
        if (matchVCardPrefixedField == null || values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }

    private static String toPrimaryValue(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    private static String[] toPrimaryValues(Collection<List<String>> lists) {
        if (lists == null || lists.isEmpty()) {
            return null;
        }
        List<String> result = new ArrayList<>(lists.size());
        for (List<String> list : lists) {
            String str = (String) list.get(0);
            String value = str;
            if (str != null && !value.isEmpty()) {
                result.add(value);
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }

    private static String[] toTypes(Collection<List<String>> lists) {
        if (lists == null || lists.isEmpty()) {
            return null;
        }
        List<String> result = new ArrayList<>(lists.size());
        for (List next : lists) {
            List list = next;
            String str = (String) next.get(0);
            String value = str;
            if (str != null && !value.isEmpty()) {
                String type = null;
                int i = 1;
                while (true) {
                    if (i >= list.size()) {
                        break;
                    }
                    String str2 = (String) list.get(i);
                    String metadatum = str2;
                    int indexOf = str2.indexOf(61);
                    int equals = indexOf;
                    if (indexOf < 0) {
                        type = metadatum;
                        break;
                    } else if ("TYPE".equalsIgnoreCase(metadatum.substring(0, equals))) {
                        type = metadatum.substring(equals + 1);
                        break;
                    } else {
                        i++;
                    }
                }
                result.add(type);
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }

    private static boolean isLikeVCardDate(CharSequence value) {
        return value == null || VCARD_LIKE_DATE.matcher(value).matches();
    }

    private static void formatNames(Iterable<List<String>> names) {
        if (names != null) {
            for (List next : names) {
                List list = next;
                String name = (String) next.get(0);
                String[] components = new String[5];
                int start = 0;
                int componentIndex = 0;
                while (componentIndex < 4) {
                    int indexOf = name.indexOf(59, start);
                    int end = indexOf;
                    if (indexOf < 0) {
                        break;
                    }
                    components[componentIndex] = name.substring(start, end);
                    componentIndex++;
                    start = end + 1;
                }
                components[componentIndex] = name.substring(start);
                StringBuilder newName = new StringBuilder(100);
                maybeAppendComponent(components, 3, newName);
                maybeAppendComponent(components, 1, newName);
                maybeAppendComponent(components, 2, newName);
                maybeAppendComponent(components, 0, newName);
                maybeAppendComponent(components, 4, newName);
                list.set(0, newName.toString().trim());
            }
        }
    }

    private static void maybeAppendComponent(String[] components, int i, StringBuilder newName) {
        if (components[i] != null && !components[i].isEmpty()) {
            if (newName.length() > 0) {
                newName.append(' ');
            }
            newName.append(components[i]);
        }
    }
}
