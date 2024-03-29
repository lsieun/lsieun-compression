package lsieun.compression.lossless.huffman.v2;

import java.util.*;

/**
 * Implements a Huffman tree.
 */
public class HuffmanTree {
	/**
	 * The total number of symbols.
	 */
	private int numSymbols;
	
	/**
	 * A map of the leaves grouped by depth.
	 */
	private Map<Integer, List<LeafNode>> depthMap;
	
	/**
	 * The maximum depth of the tree.
	 */
	private int maxDepth;
	
	/**
	 * The root node.
	 */
	private Node root;
	
	/**
	 * Construct a Huffman tree from the given frequencies.
	 * @param freq The symbol frequencies
	 * @param limit The depth limit
	 */
	public HuffmanTree(int[] freq, int limit) {
		numSymbols = freq.length;
		depthMap = new TreeMap<>();
		maxDepth = 0;
		
		// Create leaf nodes
		PriorityQueue<Node> queue = new PriorityQueue<>();
		for (int i = 0; i < numSymbols; i++) {
			if (freq[i] > 0) {
				queue.add(new LeafNode(i, freq[i]));
			}
		}
		
		// Ensure that the tree has at least two leaves
		int index = 0;
		while (queue.size() < 2) {
			if (freq[index] == 0) {
				queue.add(new LeafNode(index, 1));
			}
			index++;
		}
		
		// Build the tree
		int n = queue.size();
		for (int i = 0; i < n - 1; i++) {
			Node left = queue.remove();
			Node right = queue.remove();
			queue.add(new InternalNode(left, right));
		}
		root = queue.remove();
		
		// Build the depth map
		traverse(root);
		
		// Balance the tree within limits
		while (maxDepth > limit) {
			LeafNode leafA = depthMap.get(maxDepth).get(0);                // Pick a leaf over the limit
			
			InternalNode parent1 = (InternalNode) leafA.parent;            // Go up one node
			LeafNode leafB;                                                // Examine the opposite leaf
			if (leafA.side == 0) {
				leafB = (LeafNode) parent1.right;
			} else {
				leafB = (LeafNode) parent1.left;
			}
			
			InternalNode parent2 = (InternalNode) parent1.parent;          // Move the opposite leaf upwards,
			if (parent1.side == 0) {                                       // removing the current leaf
				parent2.left = leafB;
				parent2.left.parent = parent2;
				parent2.left.side = 0;
			} else {
				parent2.right = leafB;
				parent2.right.parent = parent2;
				parent2.right.side = 1;
			}
			
			boolean moved = false;
			for (int i = maxDepth - 2; i >= 1; i--) {
				List<LeafNode> leaves = depthMap.get(i);
				if (leaves != null) {
					LeafNode leafC = leaves.get(0);                        // Pick a new leaf under the limit
					
					InternalNode parent3 = (InternalNode) leafC.parent;    // Move the new leaf downwards,
					if (leafC.side == 0) {                                 // reinserting the old leaf
						parent3.left = new InternalNode(leafA, leafC);
						parent3.left.parent = parent3;
						parent3.left.side = 0;
					} else {
						parent3.right = new InternalNode(leafA, leafC);
						parent3.right.parent = parent3;
						parent3.right.side = 1;
					}
					
					moved = true;
					break;
				}
			}
			if (!moved) {                                                  // Check that the leaf was reinserted
				throw new AssertionError("Can't balance the tree");
			}
			
			traverse(root);                                                // Rebuild depth map
		}
	}
	
	/**
	 * Traverse the tree from the root node.
	 * @param root The root node
	 */
	private void traverse(Node root) {
		depthMap.clear();
		maxDepth = 0;
		traverse(root, 0);
	}
	
	/**
	 * Traverse the tree from the current node.
	 * @param node The current node
	 * @param depth The current depth
	 */
	private void traverse(Node node, int depth) {
		// Find the maximum depth
		if (depth > maxDepth) maxDepth = depth;
		
		// Traverse the tree, adding leaves to the depth map
		if (node instanceof InternalNode) {
			traverse(((InternalNode) node).left, depth + 1);
			traverse(((InternalNode) node).right, depth + 1);
		} else if (node instanceof LeafNode) {
			if (depthMap.get(depth) == null) {
				depthMap.put(depth, new ArrayList<>());
			}
			depthMap.get(depth).add((LeafNode) node);
		}
	}
	
	/**
	 * Convert the current tree into a table of canonical codes.
	 * @return A Huffman table of the current tree
	 */
	public HuffmanTable getTable() {
		HuffmanTable table = new HuffmanTable(numSymbols);
		int[] code = table.code;
		int[] codelen = table.codeLen;
		
		// Go through each level of the tree, assigning minimal codes for each leaf
		int nextCode = 0;
		int lastShift = 0;
		for (Integer length : depthMap.keySet()) {
			nextCode <<= (length - lastShift);
			lastShift = length;
			
			// Sort leaves by value
			List<LeafNode> leaves = depthMap.get(length);
			Collections.sort(leaves, new Comparator<LeafNode>() {
				public int compare(LeafNode n1, LeafNode n2) {
					return n1.value - n2.value;
				}
			});
			
			// Assign codes to leaves
			for (LeafNode leaf : leaves) {
				code[leaf.value] = nextCode++;
				codelen[leaf.value] = length;
			}
		}
		
		return table;
	}

}