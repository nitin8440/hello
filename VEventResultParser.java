package com.google.zxing.client.result;

import com.facebook.share.internal.ShareConstants;
import com.google.zxing.Result;
import java.util.List;

public final class VEventResultParser extends ResultParser {
    public CalendarParsedResult parse(Result result) {
        double longitude;
        double latitude;
        String massagedText = getMassagedText(result);
        String rawText = massagedText;
        if (massagedText.indexOf("BEGIN:VEVENT") < 0) {
            return null;
        }
        String summary = matchSingleVCardPrefixedField("SUMMARY", rawText, true);
        String matchSingleVCardPrefixedField = matchSingleVCardPrefixedField("DTSTART", rawText, true);
        String start = matchSingleVCardPrefixedField;
        if (matchSingleVCardPrefixedField == null) {
            return null;
        }
        String end = matchSingleVCardPrefixedField("DTEND", rawText, true);
        String duration = matchSingleVCardPrefixedField("DURATION", rawText, true);
        String location = matchSingleVCardPrefixedField("LOCATION", rawText, true);
        String organizer = stripMailto(matchSingleVCardPrefixedField("ORGANIZER", rawText, true));
        String[] matchVCardPrefixedField = matchVCardPrefixedField("ATTENDEE", rawText, true);
        String[] attendees = matchVCardPrefixedField;
        if (matchVCardPrefixedField != null) {
            for (int i = 0; i < attendees.length; i++) {
                attendees[i] = stripMailto(attendees[i]);
            }
        }
        String description = matchSingleVCardPrefixedField(ShareConstants.DESCRIPTION, rawText, true);
        String matchSingleVCardPrefixedField2 = matchSingleVCardPrefixedField("GEO", rawText, true);
        String geoString = matchSingleVCardPrefixedField2;
        if (matchSingleVCardPrefixedField2 == null) {
            latitude = Double.NaN;
            longitude = Double.NaN;
        } else {
            int indexOf = geoString.indexOf(59);
            int semicolon = indexOf;
            if (indexOf < 0) {
                return null;
            }
            try {
                latitude = Double.parseDouble(geoString.substring(0, semicolon));
                longitude = Double.parseDouble(geoString.substring(semicolon + 1));
            } catch (NumberFormatException e) {
                String[] strArr = attendees;
                return null;
            }
        }
        try {
            String[] strArr2 = attendees;
            try {
                CalendarParsedResult calendarParsedResult = new CalendarParsedResult(summary, start, end, duration, location, organizer, attendees, description, latitude, longitude);
                return calendarParsedResult;
            } catch (IllegalArgumentException e2) {
                return null;
            }
        } catch (IllegalArgumentException e3) {
            String[] strArr3 = attendees;
            return null;
        }
    }

    private static String matchSingleVCardPrefixedField(CharSequence prefix, String rawText, boolean trim) {
        List<String> matchSingleVCardPrefixedField = VCardResultParser.matchSingleVCardPrefixedField(prefix, rawText, trim, false);
        List<String> values = matchSingleVCardPrefixedField;
        if (matchSingleVCardPrefixedField == null || values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }

    private static String[] matchVCardPrefixedField(CharSequence prefix, String rawText, boolean trim) {
        List<List<String>> matchVCardPrefixedField = VCardResultParser.matchVCardPrefixedField(prefix, rawText, trim, false);
        List<List<String>> values = matchVCardPrefixedField;
        if (matchVCardPrefixedField == null || values.isEmpty()) {
            return null;
        }
        int size = values.size();
        int size2 = size;
        String[] result = new String[size];
        for (int i = 0; i < size2; i++) {
            result[i] = (String) values.get(i).get(0);
        }
        return result;
    }

    private static String stripMailto(String s) {
        if (s == null) {
            return s;
        }
        if (s.startsWith("mailto:") || s.startsWith("MAILTO:")) {
            return s.substring(7);
        }
        return s;
    }
}
