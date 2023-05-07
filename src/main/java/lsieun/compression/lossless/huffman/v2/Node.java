package lsieun.compression.lossless.huffman.v2;

abstract class Node implements Comparable<Node> {
    public Node parent;
    /**
     * The side of this node (with respect to the parent).
     */
    public int side;
    public int weight;

    public int compareTo(Node node) {
        return weight - node.weight;
    }
}