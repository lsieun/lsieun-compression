package run;

import lsieun.utils.BitUtils;
import lsieun.utils.FileUtils;
import lsieun.utils.HexFormat;
import lsieun.utils.HexUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HelloWorld {
    public static void main(String[] args) {
        String filepath = PathManager.getFilePath("output.txt");
        byte[] bytes = FileUtils.readBytes(filepath);
        System.out.println(bytes.length);
        System.out.println(HexUtils.format(bytes, HexFormat.FORMAT_FF_SPACE_FF_16));


        byte[] bytes2 = Arrays.copyOfRange(bytes, 20, bytes.length);
        System.out.println(HexUtils.format(bytes2, HexFormat.FORMAT_FF_SPACE_FF_16));

        int length = bytes2.length;
        for (int i = 0; i < length - 1; i++) {
            byte b = bytes2[i];
            String bin_str = BitUtils.fromByte(b);
            String line = String.format("%03d: %s", i, bin_str);
            System.out.println(line);
//            String bin_str2 = BitUtils.fromByte(BitUtils.swap(b));
//            String line2 = String.format("%03d: %s", i, bin_str2);
//            System.out.println(line2);
        }
    }
}
