package lsieun.utils;

public class BitUtils {
    public static boolean hasBit(byte b, int index) {
        int shift = index - 1;
        return (byte) (b << shift) < 0;
    }

    public static byte setBit(byte b, int index) {
        int shift = 8 - index;
        int bitmask = 1 << shift;
        return (byte) ((b & 0xFF) | bitmask);
    }

    public static byte clearBit(byte b, int index) {
        int shift = 8 - index;
        int bitmask = (~(1 << shift)) & 0xFF;
        return (byte) ((b & 0xFF) & bitmask);
    }

    public static byte swapBit(byte b, int index1, int index2) {
        boolean isSet1 = hasBit(b, index1);
        boolean isSet2 = hasBit(b, index2);
        if (isSet1 ^ isSet2) {
            int set_shift;
            int clear_shift;
            if (isSet1) {
                set_shift = 8 - index2;
                clear_shift = 8 - index1;
            }
            else {
                set_shift = 8 - index1;
                clear_shift = 8 - index2;
            }

            int set_bitmask = 1 << set_shift;
            int clear_bitmask = (~(1 << clear_shift)) & 0xFF;
            return (byte) (((b & 0xFF) | set_bitmask) & clear_bitmask);
        }
        else {
            return b;
        }
    }

    public static byte swap(byte b) {
        byte b1 = swapBit(b, 1, 8);
        byte b2 = swapBit(b1, 2, 7);
        byte b3 = swapBit(b2, 3, 6);
        return swapBit(b3, 4, 5);
    }

    public static int getBit(byte[] bytes, int pos) {
        // 1. index
        int byteIndex = pos / 8;
        int bitIndex = pos % 8;

        // 2. mask
        int mask = 0x80;
        mask = mask >> bitIndex;

        // 3. value
        int val = bytes[byteIndex] & 0xFF;

        // 4. result
        return (val & mask) == mask ? 1 : 0;
    }

    public static void setBit(byte[] bytes, int pos) {
        int val = (bytes[pos / 8] & 0xFF) | (0x80 >> (pos % 8));
        bytes[pos / 8] = (byte) val;
    }

    public static void clearBit(byte[] bytes, int pos) {
        int val = (bytes[pos / 8] & 0xFF) & ~(0x80 >> (pos % 8));
        bytes[pos / 8] = (byte) val;
    }

    public static void xorBit(byte[] bytes1, byte[] bytes2, int size) {
        int quotient = size / 8;
        int remainder = size % 8;
        if (remainder != 0) {
            quotient++;
        }
        byte[] result_bytes = new byte[quotient];
        for (int i = 0; i < size; i++) {
            if (getBit(bytes1, i) != getBit(bytes2, i)) {
                setBit(result_bytes, i);
            }
            else {
                clearBit(result_bytes, i);
            }
        }
    }

    public static void rotLeftBit(byte[] bytes, int size, int count) {
//        if (size <= 0) return;
//        for (int j=0;j<count;j++) {
//            for (int i=0;i<)
//        }
    }

    public static String fromByte(byte byteValue) {
        int length = Byte.SIZE;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int val = ((byte) (byteValue << i) < 0) ? 0x31 : 0x30;
            bytes[i] = (byte) val;
        }
        return new String(bytes);
    }

    public static String fromShort(short shortValue) {
        int length = Short.SIZE;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int val = ((short) (shortValue << i) < 0) ? 0x31 : 0x30;
            bytes[i] = (byte) val;
        }
        return new String(bytes);
    }

    public static String fromInt(int intValue) {
        int length = Integer.SIZE;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int val = ((intValue << i) < 0) ? 0x31 : 0x30;
            bytes[i] = (byte) val;
        }
        return new String(bytes);
    }

    public static String fromLong(long longValue) {
        int length = Long.SIZE;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int val = ((longValue << i) < 0) ? 0x31 : 0x30;
            bytes[i] = (byte) val;
        }
        return new String(bytes);
    }

    public static int str2int(String str) {
        int length = str.length();
        int result = 0;
        for (int i = 0; i < length; i++) {
            char ch = str.charAt(i);
            if (ch != '1' && ch != '0') continue;
            int bit = (ch == '1' ? 1 : 0);
            result = (result << 1) | bit;
        }
        return result;
    }
}
