package run;

import lsieun.compression.lossless.huffman.v03.HuffmanCode;
import lsieun.compression.lossless.huffman.v03.HuffmanTree;
import lsieun.compression.lossless.huffman.v03.HuffmanUtils;
import lsieun.utils.BitUtils;
import lsieun.utils.FileUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HuffmanRun {
    public static void main(String[] args) {
        String filepath = PathManager.getFilePath("chinese-text.txt");
        byte[] input_bytes = FileUtils.readBytes(filepath);
        System.out.println(input_bytes.length);
//        int[] frequencies = HuffmanUtils.getFrequencies(input_bytes);
//        System.out.println(Arrays.toString(frequencies));
//
//        HuffmanUtils.normalize(frequencies);
//        System.out.println(Arrays.toString(frequencies));
//
//        HuffmanTree tree = HuffmanUtils.buildHuffmanTree(frequencies);
//        System.out.println(tree.getSize());
//
//        HuffmanCode[] table = HuffmanUtils.buildHuffmanTable(tree);
//        int length = table.length;
//        for (int i = 0; i < length; i++) {
//            HuffmanCode item = table[i];
//            if (item == null) continue;
//            int code = item.code;
//            int size = item.size;
//            String binary_str = BitUtils.fromInt(code);
//            String line = String.format("%03d(%d): %s", i, frequencies[i], binary_str.substring(Integer.SIZE - size));
//            System.out.println(line);
//        }
        byte[] compressed_bytes = HuffmanUtils.compress(input_bytes);

        String output_filepath = PathManager.getFilePath("output.txt");
        FileUtils.writeBytes(output_filepath, compressed_bytes);

        System.out.println(compressed_bytes.length);
//        System.out.println(HexUtils.format(compressed_bytes, HexFormat.FORMAT_FF_SPACE_FF_16));
//        byte[] data_bytes = Arrays.copyOfRange(compressed_bytes, 260, 270);
//        System.out.println(Arrays.toString(data_bytes));
//        System.out.println("=====");
//        for (byte b : data_bytes) {
//            byte swap = BitUtils.swap(b);
//            String binary_str = BitUtils.fromByte(b);
//            System.out.println(binary_str);
//        }
//        System.out.println(Arrays.toString(data_bytes));

        byte[] output_bytes = HuffmanUtils.umcompress(compressed_bytes);
        System.out.println(output_bytes.length);
        String str2 = new String(output_bytes, StandardCharsets.UTF_8);
        System.out.println(str2);
    }
}
