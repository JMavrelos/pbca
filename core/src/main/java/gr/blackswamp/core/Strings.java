package gr.blackswamp.core;

public class Strings {

    public static boolean IsNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean IsNullOrWhitespace(String s) {
        return s == null || IsWhitespace(s);

    }
    private static boolean IsWhitespace(String s) {
        int length = s.length();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
