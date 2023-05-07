package lsieun.compression.lossless.huffman.v01;

class HuffNode extends HuffTree {
    private final HuffTree left;
    private final HuffTree right;

    public HuffNode(int frequency, HuffTree left, HuffTree right) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    public HuffTree getLeft() {
        return left;
    }

    public HuffTree getRight() {
        return right;
    }

}