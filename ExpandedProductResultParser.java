package com.google.zxing.client.result;

public final class ExpandedProductResultParser extends ResultParser {
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x01e0, code lost:
        if (r3.equals("10") != false) goto L_0x0216;
     */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0219  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x021f  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0236  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x023f  */
    /* JADX WARNING: Removed duplicated region for block: B:134:0x024a  */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0255  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x025a  */
    /* JADX WARNING: Removed duplicated region for block: B:137:0x025f  */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x0264  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0269  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x026e  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x0273  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.zxing.client.result.ExpandedProductParsedResult parse(com.google.zxing.Result r34) {
        /*
            r33 = this;
            com.google.zxing.BarcodeFormat r0 = r34.getBarcodeFormat()
            com.google.zxing.BarcodeFormat r1 = com.google.zxing.BarcodeFormat.RSS_EXPANDED
            r2 = 0
            if (r0 == r1) goto L_0x000a
            return r2
        L_0x000a:
            java.lang.String r0 = getMassagedText(r34)
            r1 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r11 = 0
            r12 = 0
            r13 = 0
            r14 = 0
            java.util.HashMap r15 = new java.util.HashMap
            r15.<init>()
            r2 = 0
            r20 = r1
            r21 = r3
            r22 = r4
            r23 = r5
            r24 = r6
            r25 = r7
            r26 = r8
            r27 = r9
            r28 = r10
            r29 = r11
            r30 = r12
            r31 = r13
            r32 = r14
            r1 = 0
            r3 = 0
        L_0x003d:
            int r4 = r0.length()
            if (r1 >= r4) goto L_0x0278
            java.lang.String r4 = findAIvalue(r1, r0)
            r3 = r4
            if (r4 != 0) goto L_0x004c
            r2 = 0
            return r2
        L_0x004c:
            int r4 = r3.length()
            r5 = 2
            int r4 = r4 + r5
            int r4 = r4 + r1
            r1 = r4
            java.lang.String r4 = findValue(r4, r0)
            int r6 = r4.length()
            int r1 = r1 + r6
            r6 = -1
            int r7 = r3.hashCode()
            r8 = 1570(0x622, float:2.2E-42)
            r9 = 4
            r10 = 3
            if (r7 == r8) goto L_0x020b
            r8 = 1572(0x624, float:2.203E-42)
            if (r7 == r8) goto L_0x0201
            r8 = 1574(0x626, float:2.206E-42)
            if (r7 == r8) goto L_0x01f7
            switch(r7) {
                case 1536: goto L_0x01ed;
                case 1537: goto L_0x01e3;
                default: goto L_0x0073;
            }
        L_0x0073:
            switch(r7) {
                case 1567: goto L_0x01da;
                case 1568: goto L_0x01d0;
                default: goto L_0x0076;
            }
        L_0x0076:
            switch(r7) {
                case 1567966: goto L_0x01c6;
                case 1567967: goto L_0x01bb;
                case 1567968: goto L_0x01b0;
                case 1567969: goto L_0x01a4;
                case 1567970: goto L_0x0198;
                case 1567971: goto L_0x018c;
                case 1567972: goto L_0x0180;
                case 1567973: goto L_0x0174;
                case 1567974: goto L_0x0168;
                case 1567975: goto L_0x015c;
                default: goto L_0x0079;
            }
        L_0x0079:
            switch(r7) {
                case 1568927: goto L_0x0150;
                case 1568928: goto L_0x0144;
                case 1568929: goto L_0x0138;
                case 1568930: goto L_0x012c;
                case 1568931: goto L_0x0120;
                case 1568932: goto L_0x0114;
                case 1568933: goto L_0x0108;
                case 1568934: goto L_0x00fc;
                case 1568935: goto L_0x00f0;
                case 1568936: goto L_0x00e4;
                default: goto L_0x007c;
            }
        L_0x007c:
            switch(r7) {
                case 1575716: goto L_0x00d8;
                case 1575717: goto L_0x00cc;
                case 1575718: goto L_0x00c0;
                case 1575719: goto L_0x00b4;
                default: goto L_0x007f;
            }
        L_0x007f:
            switch(r7) {
                case 1575747: goto L_0x00a8;
                case 1575748: goto L_0x009c;
                case 1575749: goto L_0x0090;
                case 1575750: goto L_0x0084;
                default: goto L_0x0082;
            }
        L_0x0082:
            goto L_0x0215
        L_0x0084:
            java.lang.String r5 = "3933"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 34
            goto L_0x0216
        L_0x0090:
            java.lang.String r5 = "3932"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 33
            goto L_0x0216
        L_0x009c:
            java.lang.String r5 = "3931"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 32
            goto L_0x0216
        L_0x00a8:
            java.lang.String r5 = "3930"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 31
            goto L_0x0216
        L_0x00b4:
            java.lang.String r5 = "3923"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 30
            goto L_0x0216
        L_0x00c0:
            java.lang.String r5 = "3922"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 29
            goto L_0x0216
        L_0x00cc:
            java.lang.String r5 = "3921"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 28
            goto L_0x0216
        L_0x00d8:
            java.lang.String r5 = "3920"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 27
            goto L_0x0216
        L_0x00e4:
            java.lang.String r5 = "3209"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 26
            goto L_0x0216
        L_0x00f0:
            java.lang.String r5 = "3208"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 25
            goto L_0x0216
        L_0x00fc:
            java.lang.String r5 = "3207"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 24
            goto L_0x0216
        L_0x0108:
            java.lang.String r5 = "3206"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 23
            goto L_0x0216
        L_0x0114:
            java.lang.String r5 = "3205"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 22
            goto L_0x0216
        L_0x0120:
            java.lang.String r5 = "3204"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 21
            goto L_0x0216
        L_0x012c:
            java.lang.String r5 = "3203"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 20
            goto L_0x0216
        L_0x0138:
            java.lang.String r5 = "3202"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 19
            goto L_0x0216
        L_0x0144:
            java.lang.String r5 = "3201"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 18
            goto L_0x0216
        L_0x0150:
            java.lang.String r5 = "3200"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 17
            goto L_0x0216
        L_0x015c:
            java.lang.String r5 = "3109"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 16
            goto L_0x0216
        L_0x0168:
            java.lang.String r5 = "3108"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 15
            goto L_0x0216
        L_0x0174:
            java.lang.String r5 = "3107"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 14
            goto L_0x0216
        L_0x0180:
            java.lang.String r5 = "3106"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 13
            goto L_0x0216
        L_0x018c:
            java.lang.String r5 = "3105"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 12
            goto L_0x0216
        L_0x0198:
            java.lang.String r5 = "3104"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 11
            goto L_0x0216
        L_0x01a4:
            java.lang.String r5 = "3103"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 10
            goto L_0x0216
        L_0x01b0:
            java.lang.String r5 = "3102"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 9
            goto L_0x0216
        L_0x01bb:
            java.lang.String r5 = "3101"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 8
            goto L_0x0216
        L_0x01c6:
            java.lang.String r5 = "3100"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 7
            goto L_0x0216
        L_0x01d0:
            java.lang.String r5 = "11"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 3
            goto L_0x0216
        L_0x01da:
            java.lang.String r7 = "10"
            boolean r7 = r3.equals(r7)
            if (r7 == 0) goto L_0x0215
            goto L_0x0216
        L_0x01e3:
            java.lang.String r5 = "01"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 1
            goto L_0x0216
        L_0x01ed:
            java.lang.String r5 = "00"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 0
            goto L_0x0216
        L_0x01f7:
            java.lang.String r5 = "17"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 6
            goto L_0x0216
        L_0x0201:
            java.lang.String r5 = "15"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 5
            goto L_0x0216
        L_0x020b:
            java.lang.String r5 = "13"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L_0x0215
            r5 = 4
            goto L_0x0216
        L_0x0215:
            r5 = -1
        L_0x0216:
            switch(r5) {
                case 0: goto L_0x0273;
                case 1: goto L_0x026e;
                case 2: goto L_0x0269;
                case 3: goto L_0x0264;
                case 4: goto L_0x025f;
                case 5: goto L_0x025a;
                case 6: goto L_0x0255;
                case 7: goto L_0x024a;
                case 8: goto L_0x024a;
                case 9: goto L_0x024a;
                case 10: goto L_0x024a;
                case 11: goto L_0x024a;
                case 12: goto L_0x024a;
                case 13: goto L_0x024a;
                case 14: goto L_0x024a;
                case 15: goto L_0x024a;
                case 16: goto L_0x024a;
                case 17: goto L_0x023f;
                case 18: goto L_0x023f;
                case 19: goto L_0x023f;
                case 20: goto L_0x023f;
                case 21: goto L_0x023f;
                case 22: goto L_0x023f;
                case 23: goto L_0x023f;
                case 24: goto L_0x023f;
                case 25: goto L_0x023f;
                case 26: goto L_0x023f;
                case 27: goto L_0x0236;
                case 28: goto L_0x0236;
                case 29: goto L_0x0236;
                case 30: goto L_0x0236;
                case 31: goto L_0x021f;
                case 32: goto L_0x021f;
                case 33: goto L_0x021f;
                case 34: goto L_0x021f;
                default: goto L_0x0219;
            }
        L_0x0219:
            r5 = 0
            r15.put(r3, r4)
            goto L_0x003d
        L_0x021f:
            int r5 = r4.length()
            if (r5 >= r9) goto L_0x0227
            r5 = 0
            return r5
        L_0x0227:
            r5 = 0
            java.lang.String r30 = r4.substring(r10)
            java.lang.String r32 = r4.substring(r2, r10)
            java.lang.String r31 = r3.substring(r10)
            goto L_0x003d
        L_0x0236:
            r5 = 0
            r30 = r4
            java.lang.String r31 = r3.substring(r10)
            goto L_0x003d
        L_0x023f:
            r5 = 0
            r27 = r4
            java.lang.String r28 = "LB"
            java.lang.String r29 = r3.substring(r10)
            goto L_0x003d
        L_0x024a:
            r5 = 0
            r27 = r4
            java.lang.String r28 = "KG"
            java.lang.String r29 = r3.substring(r10)
            goto L_0x003d
        L_0x0255:
            r5 = 0
            r26 = r4
            goto L_0x003d
        L_0x025a:
            r5 = 0
            r25 = r4
            goto L_0x003d
        L_0x025f:
            r5 = 0
            r24 = r4
            goto L_0x003d
        L_0x0264:
            r5 = 0
            r23 = r4
            goto L_0x003d
        L_0x0269:
            r5 = 0
            r22 = r4
            goto L_0x003d
        L_0x026e:
            r5 = 0
            r20 = r4
            goto L_0x003d
        L_0x0273:
            r5 = 0
            r21 = r4
            goto L_0x003d
        L_0x0278:
            com.google.zxing.client.result.ExpandedProductParsedResult r2 = new com.google.zxing.client.result.ExpandedProductParsedResult
            r3 = r2
            r4 = r0
            r5 = r20
            r6 = r21
            r7 = r22
            r8 = r23
            r9 = r24
            r10 = r25
            r11 = r26
            r12 = r27
            r13 = r28
            r14 = r29
            r19 = r15
            r15 = r30
            r16 = r31
            r17 = r32
            r18 = r19
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.client.result.ExpandedProductResultParser.parse(com.google.zxing.Result):com.google.zxing.client.result.ExpandedProductParsedResult");
    }

    private static String findAIvalue(int i, String rawText) {
        if (rawText.charAt(i) != '(') {
            return null;
        }
        CharSequence rawTextAux = rawText.substring(i + 1);
        StringBuilder buf = new StringBuilder();
        for (int index = 0; index < rawTextAux.length(); index++) {
            char charAt = rawTextAux.charAt(index);
            char currentChar = charAt;
            if (charAt == ')') {
                return buf.toString();
            }
            if (currentChar < '0' || currentChar > '9') {
                return null;
            }
            buf.append(currentChar);
        }
        return buf.toString();
    }

    private static String findValue(int i, String rawText) {
        StringBuilder buf = new StringBuilder();
        String rawTextAux = rawText.substring(i);
        for (int index = 0; index < rawTextAux.length(); index++) {
            char charAt = rawTextAux.charAt(index);
            char c = charAt;
            if (charAt == '(') {
                if (findAIvalue(index, rawTextAux) != null) {
                    break;
                }
                buf.append('(');
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
}
