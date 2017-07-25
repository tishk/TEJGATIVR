//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.util;

public class ClientUtil {
    public ClientUtil() {
    }

    public static String getFormattedDateyyyymmdd(String dateStr_yyyymmdd) {
        StringBuffer buffer = new StringBuffer(dateStr_yyyymmdd);
        buffer.insert(6, "/");
        buffer.insert(4, "/");
        return buffer.toString();
    }

    public static String getFormattedDateyymmdd(String dateStr_yymmdd) {
        StringBuffer buffer = new StringBuffer(dateStr_yymmdd);
        buffer.insert(4, "/");
        buffer.insert(2, "/");
        return buffer.toString();
    }

    public static String zeropad(String str, int length) {
        if(str != null && str.length() < length) {
            int index = length - str.length();
            String s = "";

            for(int i = 0; i < index; ++i) {
                s = s + "0";
            }

            return s + str;
        } else {
            return str;
        }
    }
}
