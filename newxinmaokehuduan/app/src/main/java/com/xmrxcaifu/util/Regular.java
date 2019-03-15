package com.xmrxcaifu.util;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class Regular {
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
//		String telRegex = "[1][34587]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "1(3[0-9]|4[57]|5[0-35-9]|66|7[0135678]|8[0-9]|9[89])\\d{8}";


        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    @SuppressWarnings("unused")


    public static boolean getPass(String regex) {

//        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
/*     [0-9A-Za-z]{8,16}
        }*/
//        boolean is_matcher = regex != null && regex.matches("[0-9A-Za-z_]{6,16}");
        boolean is_matcher = regex != null && regex.matches("[0-9A-Za-z]{6,16}");

        return true;

    }
    /**
     * 检查字符串是否为纯数字
     *
     * @param str 字符串内容
     * @return true是纯数字，false不是。
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 检查字符串是否为纯字母
     *
     * @param s 字符串内容
     * @return true是纯字母，false不是。
     */
    public static boolean isLetter(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!(s.charAt(i) >= 'A' && s.charAt(i) <= 'Z')
                    && !(s.charAt(i) >= 'a' && s.charAt(i) <= 'z')) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断是否包含特殊字符，
     *
     * @param conten 需要判断的内容
     * @return 包含了为true，没有包含为false
     */
    public static boolean containSpecialCharacter(String conten) {
        if (null != conten) {
            if (!"".equals(conten)) {
                if (conten.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() > 0) {
                    return true;
                }
            }
        }
        return false;
    }





    public static String getDecimalFormatZero(Double dou) {
        DecimalFormat df = new DecimalFormat("###0");
        if (dou.equals("")) {
            return "";
        }
        return df.format(dou);
    }

    public static SpannableString setSpannableString(String text, int index, int last) {
        SpannableString titleString = new SpannableString(text);
        titleString.setSpan(new AbsoluteSizeSpan(10, true), index, last, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return titleString;

    }

    public static SpannableString setSpannablecolor(String text, String color, int index, int last) {
        SpannableString titleString = new SpannableString(text);
        titleString.setSpan(new ForegroundColorSpan(Color.parseColor(color)), index, last, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return titleString;

    }

    public static String getDecimalFormatOne(Double dou) {
        DecimalFormat df = new DecimalFormat("###0.0");
        if (dou.equals("")) {
            return "";
        }
        return df.format(dou);
    }

    public static String getDecimalFormatTwo(Double dou) {
        DecimalFormat df = new DecimalFormat("###0.00");
        if (dou.equals("")) {
            return "";
        }
        return df.format(dou);
    }

    public static String getMilliToDate(String time) {
        if (time == null) {
            return " ";
        }
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static boolean hasSpecialCharacter(String str) {
        String regEx = "[~!@#$%^&*<>]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return false;
        }
        return true;
    }

    public static String getMilliToDateZhe(String time) {
        if (time == null) {
            return " ";
        }
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat("dd日");
        return formatter.format(date);
    }

    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String getMilliToDate2(String time) {
        if (time == null) {
            return " ";
        }
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        return formatter.format(date);
    }

    public static String getMilliToDate3(String time) {
        if (time == null) {
            return " ";
        }
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        return formatter.format(date);
    }

    public static String getMilliToDate4(String time) {
        if (time == null) {
            return " ";
        }
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String getMilliTotime(String time) {
        if (time == null) {
            return " ";
        }
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(date);
    }

    public static String getMilliTotime2(String time) {
        if (time == null) {
            return " ";
        }
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日HH时mm分ss秒");
        return formatter.format(date);
    }

    public static String getMilliToHour_Min(String time) {
        if (time == null) {
            return " ";
        }
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat(
                "HH:mm");
        return formatter.format(date);
    }

    public static String getMilliTotime3(String time) {
        if (time == null) {
            return " ";
        }
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    public static String getMilliTotime4(String time) {
        if (time == null) {
            return " ";
        }
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    public static String data(String time) {
        if (time == null) {
            return " ";
        }
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String ondata() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

}
