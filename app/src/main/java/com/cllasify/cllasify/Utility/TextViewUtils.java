package com.cllasify.cllasify.Utility;

import android.text.TextPaint;
import android.text.style.URLSpan;

public class TextViewUtils extends URLSpan {
    public TextViewUtils(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}
