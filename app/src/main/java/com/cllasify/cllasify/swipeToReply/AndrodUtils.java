package com.cllasify.cllasify.swipeToReply;

import android.content.Context;
import android.content.res.Resources;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

final class AndroidUtils {
    private static float density;
    @NotNull
    public static final AndroidUtils INSTANCE;

    public final int dp(float value, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        if (density == 1.0F) {
            this.checkDisplaySize(context);
        }

        return value == 0.0F ? 0 : (int)Math.ceil((double)(density * value));
    }

    private final void checkDisplaySize(Context context) {
        try {
            Resources var10000 = context.getResources();
            Intrinsics.checkNotNullExpressionValue(var10000, "context.resources");
            density = var10000.getDisplayMetrics().density;
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private AndroidUtils() {
    }

    static {
        AndroidUtils var0 = new AndroidUtils();
        INSTANCE = var0;
        density = 1.0F;
    }
}
