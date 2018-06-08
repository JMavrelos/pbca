package gr.blackswamp.core;

import java.nio.ByteBuffer;
import java.sql.Date;
import java.util.Calendar;
import java.util.UUID;

public class Util {
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

    public static UUID FromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    public static byte[] ToBytes(UUID uuid) {
        return ByteBuffer.wrap(new byte[16])
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .array();
    }
    public static Calendar FromTimeInMillis(long timeInMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        return c;
    }
    public static Calendar FromDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }
}
