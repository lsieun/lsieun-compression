package lsieun.compression.lossless.huffman.v2;

class InternalNode extends Node {
    public Node left;
    public Node right;

    public InternalNode(Node left, Node right) {
        left.parent = this;
        left.side = 0;
        this.left = left;

        right.parent = this;
        right.side = 1;
        this.right = right;

        weight = left.weight + right.weight;
    }

    @Override
    public String toString() {
        return "[" + left.toString() + ", " + right.toString() + "]";
    }
}