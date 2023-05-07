package lsieun.compression.lossless.huffman.v01;

// This class is the abstract superclass of the HuffNode and HuffLeaf classes.
// Objects instantiated from HuffNode and HuffLeaf are populated and used to create a Huffman tree.
abstract class HuffTree implements Comparable<HuffTree> {
    int frequency;

    public int getFrequency() {
        return frequency;
    }

    public int compareTo(HuffTree theTree) {
        if (frequency == theTree.frequency) {
            // The objects are in a tie based on the frequency value.
            // Return a tiebreaker value based on the relative hashCode values of the two objects.
            return (hashCode() - theTree.hashCode());
        }
        else {
            return frequency - theTree.frequency;
        }
    }
}
