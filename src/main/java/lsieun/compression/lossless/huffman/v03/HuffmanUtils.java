package lsieun.compression.lossless.huffman.v03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HuffmanUtils {
    public static byte[] compress(byte[] bytes) {
        int[] frequencies = getFrequencies(bytes);
        normalize(frequencies);

        HuffmanTree huffmanTree = buildHuffmanTree(frequencies);
        HuffmanCode[] huffmanCodes = buildHuffmanTable(huffmanTree);

        int length = bytes.length;
        int count = length + Integer.BYTES + Const.COUNT;
        System.out.println("length = " + length + ", count = " + count);
        BitArrayOutputStream bao = new BitArrayOutputStream(count);
        bao.writeBits(length, Integer.SIZE);
        for (int i = 0; i < Const.COUNT; i++) {
            int frequency = frequencies[i];
            bao.writeBits(frequency, Byte.SIZE);
        }

        for (byte b : bytes) {
            int index = (b & 0xFF);
            HuffmanCode item = huffmanCodes[index];
            bao.writeBitsR(item.code, item.size);
        }

        return bao.toByteArray();
    }

    public static int[] getFrequencies(byte[] bytes) {
        int[] frequencies = new int[Const.COUNT];
        for (byte b : bytes) {
            int index = b & 0xFF;
            frequencies[index]++;
        }
        return frequencies;
    }

    public static void normalize(int[] frequencies) {
        int maxFrequency = 0;
        for (int frequency : frequencies) {
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
            }
        }

        int length = frequencies.length;
        for (int i = 0; i < length; i++) {
            int frequency = frequencies[i];
            int scale = (int) ((double) frequency / ((double) maxFrequency / 255));
            if (scale == 0 && frequency != 0) {
                frequencies[i] = 1;
            }
            else {
                frequencies[i] = scale;
            }
        }
    }

    public static HuffmanTree buildHuffmanTree(int[] frequencies) {
        List<HuffmanTree> list = new ArrayList<>();
        int length = frequencies.length;
        for (int i = 0; i < length; i++) {
            int frequency = frequencies[i];
            if (frequency == 0) continue;
            HuffmanTree item = new HuffmanTree(i, frequency);
            list.add(item);
        }

        while (list.size() > 1) {
            Collections.sort(list);
            HuffmanTree first = list.get(0);
            HuffmanTree second = list.get(1);
            list.remove(first);
            list.remove(second);

            int frequency = first.freq + second.freq;

            HuffmanTree item = new HuffmanTree(-1, frequency);
            item.leftChild = first;
            item.rightChild = second;
            list.add(item);
        }

        return list.get(0);
    }

    public static HuffmanCode[] buildHuffmanTable(HuffmanTree tree) {
        HuffmanCode[] table = new HuffmanCode[Const.COUNT];
        buildHuffmanTable(tree, 0, 0, table);
        return table;
    }

    private static void buildHuffmanTable(HuffmanTree tree, int code, int level, HuffmanCode[] table) {
        if (tree.leftChild == null && tree.rightChild == null) {
            HuffmanCode item = new HuffmanCode();
            item.used = true;
            item.code = code;
            item.size = level;
            table[tree.symbol] = item;
            return;
        }

        if (tree.leftChild != null) {
            buildHuffmanTable(tree.leftChild, code << 1, level + 1, table);
        }
        if (tree.rightChild != null) {
            buildHuffmanTable(tree.rightChild, (code << 1) | 1, level + 1, table);
        }
    }

    public static byte[] umcompress(byte[] bytes) {
        BitArrayInputStream bai = new BitArrayInputStream(bytes);
        int size = (int) bai.readUnsignedInt();
        int[] frequencies = new int[Const.COUNT];
        for (int i = 0; i < Const.COUNT; i++) {
            frequencies[i] = bai.readByte();
        }

        HuffmanTree huffmanTree = buildHuffmanTree(frequencies);
        byte[] out_bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            out_bytes[i] = (byte) getSymbol(huffmanTree, bai);
        }
        return out_bytes;
    }

    public static int getSymbol(HuffmanTree huffmanTree, BitArrayInputStream bai) {
        HuffmanTree tree = huffmanTree;
        while (!tree.isLeaf()) {
            int val = bai.readBits(1);

            if (val == 0) {
                tree = tree.leftChild;
            }
            else {
                tree = tree.rightChild;
            }
        }
        return tree.symbol;
    }

    public static void main(String[] args) {
        int[] array = new int[10];
        array[0]++;
        System.out.println(Arrays.toString(array));
    }
}
