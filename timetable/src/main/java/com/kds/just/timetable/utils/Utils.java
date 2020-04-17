package com.kds.just.timetable.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String TAG = "Utils";

    public static int dp2px(float dp) {
        Resources resources = Resources.getSystem();
        float px = dp * resources.getDisplayMetrics().density;
        return (int) Math.ceil(px);
    }

    public static int sp2px(Context ctx, float spValue) {
        final float scale = ctx.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public static int getRandomRGB() {
        Random rnd = new Random();
        return Color.parseColor("#" + String.format("%02X%02X%02X", rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255)));
    }
}
