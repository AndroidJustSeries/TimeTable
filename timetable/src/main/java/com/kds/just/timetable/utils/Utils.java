package com.kds.just.timetable.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String TAG = "Utils";

    public static String getMoneyStr(int money) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(money);
    }

    public static int dp2px(float dp) {
        Resources resources = Resources.getSystem();
        float px = dp * resources.getDisplayMetrics().density;
        return (int) Math.ceil(px);
    }

    public static int sp2px(Context ctx, float spValue) {
        final float scale = ctx.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static String getCountryCode(Context ctx) {
        Locale systemLocale = ctx.getApplicationContext().getResources().getConfiguration().locale;
        String strCountry = systemLocale.getCountry(); // KR
        return strCountry;
    }

    public static String GetCountryCode(Context ctx) {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
//        String[] rl = getResources().getStringArray(R.array.CountryCodes);
//        for (int i = 0; i < rl.length; i++) {
//            String[] g = rl[i].split(",");
//            if (g[1].trim().equals(CountryID.trim())) {
//                CountryZipCode = g[0];
//                break;
//            }
//        }
        return CountryID;
    }

    public static final Pattern VALID_EMAIL_REGEX = Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$");
    public static boolean isEmail(String email) {
        Matcher m = VALID_EMAIL_REGEX.matcher(email);
        return m.matches();
    }

    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$");
    // 8자리 ~ 16자리까지 가능 // 비밀번호 검사
    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.matches();
    }

    private static final String VERSIONCHECK_REGULAR_EXPRESSION = "[0-9]+(.([0-9])+)*";

    public static Integer versionCompare(String str1, String str2) {

        String[] vals1 = isValidVersion(str1).split("\\.");
        String[] vals2 = isValidVersion(str2).split("\\.");
        int i = 0;
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }

    private static String isValidVersion(String s) {
        String result = s;
        if (!s.matches(VERSIONCHECK_REGULAR_EXPRESSION)) {
            result = "0";
        }
        return result;
    }


}
