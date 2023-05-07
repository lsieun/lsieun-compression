package lsieun.compression.lossless.huffman.v03;

public class HuffmanTree implements Comparable<HuffmanTree> {
    public final int symbol;
    public final int freq;

    public HuffmanTree leftChild;
    public HuffmanTree rightChild;

    public HuffmanTree(int symbol, int freq) {
        this.symbol = symbol;
        this.freq = freq;
    }

    public boolean isLeaf() {
        return this.leftChild == null && this.rightChild == null;
    }

    public int getSize() {
        if (this.leftChild == null && this.rightChild == null) {
            return 1;
        }

        int size = 1;
        if (this.leftChild != null) {
            size += this.leftChild.getSize();
        }
        if (this.rightChild != null) {
            size += this.rightChild.getSize();
        }
        return size;
    }

    @Override
    public int compareTo(HuffmanTree another) {
        if (this.freq > another.freq) {
            return 1;
        }
        else if (this.freq < another.freq) {
            return -1;
        }
        else {
            return this.symbol - another.symbol;
        }
    }
}
