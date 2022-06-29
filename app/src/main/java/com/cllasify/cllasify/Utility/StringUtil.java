package com.cllasify.cllasify.Utility;

import android.text.Spannable;
import android.text.style.URLSpan;

public class StringUtil {
    public static void stripUnderlines(Spannable textView) {
        URLSpan[] spans = textView.getSpans(0, textView.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = textView.getSpanStart(span);
            int end = textView.getSpanEnd(span);
            textView.removeSpan(span);
            span = new TextViewUtils(span.getURL());
            textView.setSpan(span, start, end, 0);
        }
    }
}
