package run;

import lsieun.jdk.DeflateUtils;
import lsieun.utils.BitUtils;
import lsieun.utils.HexFormat;
import lsieun.utils.HexUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

//It was the best of times, it was the worst of times,

/**
 * It was the best of times,
 * it was the worst of times,
 * it was the age of wisdom,
 * it was the age of foolishness,
 * it was the epoch of belief,
 * it was the epoch of incredulity,
 * it was the season of Light,
 * it was the season of Darkness,
 * it was the spring of hope,
 * it was the winter of despair,
 * we had everything before us,
 * we had nothing before us,
 * we were all going direct to Heaven,
 * we were all going direct the other way
 */
public class DeflateRun {
    public static void main(String[] args) {
        String deflated_hex_str = "78 01 4B CA C9 4F 52 30 34 62 C8 48 CD C9 C9 57 28 CF 2F CA 49 E1 02 00 44 11 06 89";
        String inflated_hex_str = "62 6C 6F 62 20 31 32 00 68 65 6C 6C 6F 20 77 6F 72 6C 64 0A";
        byte[] deflated_bytes = HexUtils.parse(deflated_hex_str, HexFormat.FORMAT_FF_SPACE_FF);
        byte[] inflated_bytes = HexUtils.parse(inflated_hex_str, HexFormat.FORMAT_FF_SPACE_FF);

        System.out.println(new String(deflated_bytes, StandardCharsets.UTF_8));
        System.out.println(new String(inflated_bytes, StandardCharsets.UTF_8));

        int length = deflated_bytes.length;
        for (int i = 0; i < length; i++) {
            byte b = deflated_bytes[i];
            String bin_str = BitUtils.fromByte(b);
            String line = String.format("%03d: %s", i, bin_str);
            System.out.println(line);
//            String bin_str2 = BitUtils.fromByte(BitUtils.swap(b));
//            String line2 = String.format("%03d: %s", i, bin_str2);
//            System.out.println(line2);
        }

    }
}
