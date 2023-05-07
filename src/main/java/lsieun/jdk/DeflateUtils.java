package lsieun.jdk;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class DeflateUtils {
    public static byte[] inflate(byte[] input) {
        Inflater inflater = new Inflater();
        inflater.setInput(input);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int len = inflater.inflate(buffer);
                if (len == 0) {
                    break;
                }
                bao.write(buffer, 0, len);
            }
            inflater.end();
            return bao.toByteArray();
        }
        catch (DataFormatException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] deflate(byte[] input) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_SPEED);
        deflater.setInput(input);
        deflater.finish();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int len = deflater.deflate(buffer);
            bao.write(buffer, 0, len);
        }
        deflater.end();
        return bao.toByteArray();
    }
}
