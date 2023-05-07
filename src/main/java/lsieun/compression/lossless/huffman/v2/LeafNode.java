package lsieun.compression.lossless.huffman.v2;

class LeafNode extends Node {
    public int value;

    public LeafNode(int value, int weight) {
        this.value = value;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}