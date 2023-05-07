package lsieun.compression.lossless.huffman.v01;

class HuffLeaf extends HuffTree {
    private final int value;

    public HuffLeaf(int value, int frequency) {
        this.value = value;
        this.frequency = frequency;
    }

    public int getValue() {
        return value;
    }
}