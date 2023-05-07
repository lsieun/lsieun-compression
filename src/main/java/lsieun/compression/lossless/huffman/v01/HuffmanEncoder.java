package lsieun.compression.lossless.huffman.v01;

import java.util.*;

// An object of this class can be used to encode a raw text message
// using the Huffman encoding methodology.
class HuffmanEncoder {
    String rawData;
    TreeSet<HuffTree> theTree = new TreeSet<>();
    ArrayList<Byte> binaryEncodedData = new ArrayList<>();
    Map<Character, Integer> frequencyData = new HashMap<>();
    StringBuffer code = new StringBuffer();
    Map<Character, String> huffEncodeTable;
    String stringEncodedData;
    Map<String, Byte> encodingBitMap = new HashMap<>();
    //-----------------------------------------------------//

    // This method encodes an incoming String message using the Huffman encoding methodology.
    // The method also receives a reference to an empty data structure.
    // This data structures is populated with encoding particulars required later by the decode method
    // to decode and transform the encoded message back into the original String message.
    // Note that in order to keep this method simple,
    // pad characters may be appended onto the end of the original message when it is encoded.
    // This is done to cause the number of bits in the encoded message to be a multiple of eight,
    // thus causing the length of the encoded message to be an integral number of bytes.
    // Additional code would be required to avoid this at this point.
    // However, it is easy to eliminate the extraneous characters during decoding
    // if the length of the original message is known.
    ArrayList<Byte> encode(String rawData, Map<Character, String> huffEncodeTable) {
        //Save the incoming parameters.
        this.rawData = rawData;
        this.huffEncodeTable = huffEncodeTable;

        System.out.println("Raw Data as Bits");
        displayRawDataAsBits();


        createFreqData();
        displayFreqData();

        // Create a HuffLeaf object for each character identified in the frequency chart.
        // Store the HuffLeaf objects in a TreeSet object.
        // Each HuffLeaf object encapsulates the character as well as the number of times
        // that the character appeared in the original message (the frequency).
        createLeaves();

        // Assemble the HuffLeaf objects into a Huffman tree (a HuffTree object).
        // A Huffman tree is a special form of a binary tree
        // consisting of properly linked HuffNode objects and HuffLeaf objects.
        // When the following method returns,
        // the HuffTree object remains as the only object stored in the TreeSet object
        // that previously contained all the HuffLeaf objects.
        // This is because all the HuffLeaf objects have been combined with HuffNode objects to form the tree.
        createHuffTree();

        // Use the Huffman tree in a recursive manner to create
        // a bit code for each character in the message.  The
        // bit codes are different lengths with the shorter
        // codes corresponding to the characters with a high
        // frequency value and the longer codes corresponding
        // to the characters with the lower frequency values.
        //Note that the method call extracts the reference to
        // the Huffman tree from the TreeSet object and passes
        // that reference to the method.  This is necessary
        // because the method is recursive and cannot
        // conveniently work with the TreeSet object.
        //This method populates the data structure that is
        // required later to decode the encoded message.
        createBitCodes(theTree.first());

        //For purposes of illustration only, enable the
        // following two statements to display a table showing
        // the relationship between the characters in the
        // original message and the bitcodes that will replace
        // those characters to produce the Huffman-encoded
        // message.

        System.out.println();
        displayBitCodes();

        // Encode the message into a String representation
        // of the bits that will make up the final encoded
        // message.  Also,the following method may optionally
        // display the String showing the bit values that will
        // appear in the final Huffman-encoded message.  This
        // is useful for comparing back against the bits in
        // the original message for purposes of evaluating the
        // amount of compression provided by encoding the
        // message.
        encodeToString();

        // Populate a lookup table
        // that relates eight bits represented as a String to every possible combination of eight actual bits.
        buildEncodingBitMap();

        //Encode the String representation of the bits that
        // make up the encoded message to the actual bits
        // that make up the encoded message.
        //Note that this method doesn't handle the end of the
        // message very gracefully for those cases where the
        // number of required bits is not a multiple of 8.  It
        // simply adds enough "0" characters to the end to
        // cause the length to be a multiple of 8.  This may
        // result in extraneous characters at the end of the
        // decoded message later.
        //For illustration purposes only, this method may also
        // display the extended version of the String
        // representation of the bits for comparison with the
        // non-extended version.
        encodeStringToBits();

        //Return the encoded message.
        return binaryEncodedData;
    }

    // This method displays a message string as a series of
    // characters each having a value of 1 or 0.
    void displayRawDataAsBits() {
        for (int i = 0, count = 0; i < rawData.length(); i++, count++) {
            char theCharacter = rawData.charAt(i);
            String binaryString = Integer.toBinaryString(theCharacter);
            // Append leading zeros as necessary to show eight bits per character.
            while (binaryString.length() < 8) {
                binaryString = "0" + binaryString;
            }
            if (count % 6 == 0) {
                //Display 48 bits per line.
                count = 0;
                System.out.println();//new line
            }
            System.out.print(binaryString);
        }
        System.out.println();
    }

    void createFreqData() {
        for (int i = 0; i < rawData.length(); i++) {
            char key = rawData.charAt(i);
            if (frequencyData.containsKey(key)) {
                int value = frequencyData.get(key);
                value += 1;
                frequencyData.put(key, value);
            }
            else {
                frequencyData.put(key, 1);
            }
        }
    }


    void displayFreqData() {
        System.out.println("Frequency Data");
        for (Map.Entry<Character, Integer> entry : frequencyData.entrySet()) {
            Character key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key + " " + value);
        }
    }

    // This method creates a HuffLeaf object for each char identified in the frequency chart.
    // The HuffLeaf objects are stored in a TreeSet object.
    // Each HuffLeaf object encapsulates the character as well as the number of times
    // that the character appeared in the original message.
    void createLeaves() {
        for (Map.Entry<Character, Integer> entry : frequencyData.entrySet()) {
            Character key = entry.getKey();
            Integer value = entry.getValue();
            theTree.add(new HuffLeaf(key, value));
        }
    }


    //Assemble the HuffLeaf objects into a HuffTree object.
    // A HuffTree object is a special form of a binary tree
    // consisting of properly linked HuffNode objects and
    // HuffLeaf objects.
    //When the method terminates, the HuffTree object
    // remains as the only object stored in the TreeSet
    // object that previously contained all of the HuffLeaf
    // objects.  This is because all of the HuffLeaf
    // objects have been removed from the TreeSet object
    // and combined with HuffNode objects to form the
    // Huffman tree (as represented by the single HuffTree
    // object).
    //The method contains two sections of code that can be
    // enabled to display:
    // 1. The contents of the original TreeSet object.
    // 2. The contents of the TreeSet object for each
    //    iteration during which HuffLeaf objects are being
    //    combined with HuffNode objects to form the final
    //    HuffTree object.
    // This display is very useful for understanding how the
    // Huffman tree is constructed.  However, this code
    // should be enabled only for small trees because it
    // generates a very large amount of output.

    //The HuffTree object is constructed by:
    // 1. Extracting pairs of HuffLeaf or HuffNode objects
    //    from the TreeSet object in ascending order based
    //    on their frequency value.
    // 2. Using the pair of extracted objects to construct
    //    a new HuffNode object where the two extracted
    //    objects become children of the new HuffNode
    //    object, and where the frequency value stored in
    //    the new HuffNode object is the sum of the
    //    frequency values in the two child objects.
    // 3. Removing the two original HuffLeaf or HuffNode
    //    objects from the TreeSet and adding the new
    //    HuffNode object to the TreeSet object.  The
    //    position of the new HuffNode object in the Treeset
    //    object is determined by its frequency value
    //    relative to the other HuffNode or HuffLeaf objects
    //    in the collection. The new HuffNode object will
    //    eventually become a child of another new HuffNode
    //    object unless it ends up as the root of the
    //    HuffTree object.
    // 4. Continuing this process until the TreeSet object
    //    contains a single object of type HuffTree.
    void createHuffTree() {

        // Enable the following statements to see the original
        // contents of the TreeSet object. Do this only for
        // small trees because it generates lots of output.
//        System.out.println("Display Original TreeSet");
//        for (HuffTree huffTree : theTree) {
//            System.out.println("HuffNode, HuffLeaf, or HuffTree");
//            displayHuffTree(huffTree, 0);
//        }


        // Iterate on the size of the TreeSet object
        // until all the elements have been combined into a single element of type HuffTree
        while (theTree.size() > 1) {
            //Get, save, and remove the first two elements.
            HuffTree left = theTree.first();
            theTree.remove(left);
            HuffTree right = theTree.first();
            theTree.remove(right);

            // Combine the two saved elements into a new element of type HuffNode and add it to the TreeSet object.
            HuffNode tempNode = new HuffNode(left.getFrequency() + right.getFrequency(), left, right);
            theTree.add(tempNode);

            //Enable the following statements to see the HuffTree
            // being created from HuffNode and HuffLeaf objects.
            // Do this only for small trees because it will
            // generate a lot of output.

//            System.out.println("Display Working TreeSet");
//            for (HuffTree huffTree : theTree) {
//                System.out.println("HuffNode, HuffLeaf, or HuffTree");
//                displayHuffTree(huffTree, 0);
//            }
        }
    }

    // Recursive method to display a HufTree object.
    // The first call to this method should pass a value of 0 for recurLevel.
    void displayHuffTree(HuffTree tree, int recurLevel) {
        recurLevel++;
        if (tree instanceof HuffNode) {
            // This is a node, not a leaf.  Process it as a node.

            //Cast to type HuffNode.
            HuffNode node = (HuffNode) tree;
            // Get and save the left and right branches
            HuffTree left = node.getLeft();
            HuffTree right = node.getRight();

            // Display information that traces out the recursive
            // traversal of the tree in order to display the
            // contents of the leaves.
            System.out.print("  Left to " + recurLevel + " ");
            displayHuffTree(left, recurLevel);

            System.out.print("  Right to " + recurLevel + " ");
            displayHuffTree(right, recurLevel);

        }
        else {
            //This is a leaf.
            HuffLeaf leaf = (HuffLeaf) tree;
            System.out.println("  Leaf:" + (char) leaf.getValue());
        }
        System.out.print("  Back ");
    }

    //This method uses the Huffman tree in a recursive manner
    // to create a bitcode for each character in the message.
    // The bitcodes are different lengths with the shorter
    // bitcodes corresponding to the characters with a high
    // usage frequency value and the longer bitcodes
    // corresponding to the characters with the lower
    // frequency values.
    //Note that this method receives a reference to the
    // Huffman tree that was earlier contained as the only
    // object in the TreeSet object.  (Because this method is
    // recursive, it cannot conveniently work with the
    // TreeSet object.

    //This method creates a Huffman encoding table as a
    // Hashtable object that relates the variable length
    // bitcodes to the characters in the original message.
    // The bitcodes are constructed as objects of type
    // StringBuffer consisting of sequences of the characters
    // 1 and 0.
    //Each bitcode describes the traversal path from the root
    // of the Huffman tree to a leaf on that tree.  Each time
    // the path turns to the left, a 0 character is appended
    // onto the StringBuffer object and becomes part of the
    // resulting bitcode.  Each time the path turns to the
    // right, a 1 character is appended onto the
    // StringBuffer object.  When a leaf is reached, the
    // character stored in that leaf is retrieved and put
    // into the Hashtable object as a key.  A String
    // representation of the StringBuffer object is used as
    // the value for that key in the Hashtable.
    //At completion,the Hashtable object contains a series of
    // keys consisting of the original characters in the
    // message and a series of values as String objects
    // (consisting only of 1 and 0 characters) representing
    // the bitcodes that will eventually be used to encode
    // the original message.
    //Note that theHashtable object that is populated by this
    // method is the data structure that is required later
    // to decode the encoded message.
    void createBitCodes(HuffTree tree) {
        if (tree instanceof HuffNode) {
            HuffNode node = (HuffNode) tree;
            HuffTree left = node.getLeft();
            HuffTree right = node.getRight();

            code.append("0");
            createBitCodes(left);


            code.deleteCharAt(code.length() - 1);//Delete the 0.
            code.append("1");
            createBitCodes(right);

            code.deleteCharAt(code.length() - 1);
        }
        else {
            HuffLeaf leaf = (HuffLeaf) tree;
            huffEncodeTable.put((char) (leaf.getValue()), code.toString());
        }

    }

    //This method displays a table showing the relationship
    // between the characters in the original message and the
    // bitcodes that will ultimately replace each of those
    // characters to produce the Huffman-encoded message.
    void displayBitCodes() {
        System.out.println("Message Characters versus Huffman BitCodes");
        Set<Map.Entry<Character, String>> entries = huffEncodeTable.entrySet();
        for (Map.Entry<Character, String> entry : entries) {
            Character key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + " " + value);
        }
    }

    //This method encodes the message into a String
    // representation of the bits that will make up the final
    // encoded message.  The String consists of only 1 and 0
    // characters where each character represents the state
    // of one of the bits in the Huffman-encoded message.
    //Also for illustration purposes, this method optionally
    // displays the String showing the bit values that will
    // appear in the Huffman-encoded message.
    void encodeToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rawData.length(); i++) {
            sb.append(huffEncodeTable.get(rawData.charAt(i)));
        }
        stringEncodedData = sb.toString();

        System.out.println("String Encoded Data");
        display48(stringEncodedData);
    }

    //This method populates a lookup table that relates eight
    // bits represented as a String to eight actual bits for
    // all possible combinations of eight bits.
    void buildEncodingBitMap() {
        for (int i = 0; i <= 255; i++) {
            StringBuilder sb = new StringBuilder();
            if ((i & 128) > 0) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }

            if ((i & 64) > 0) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }

            if ((i & 32) > 0) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }

            if ((i & 16) > 0) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }

            if ((i & 8) > 0) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }

            if ((i & 4) > 0) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }

            if ((i & 2) > 0) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }

            if ((i & 1) > 0) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }

            encodingBitMap.put(sb.toString(), (byte) (i));
        }
    }

    //The purpose of this method is to create actual bit data
    // that matches the 1 and 0 characters in the
    // stringEncodedData that represents bits with the 1 and
    // 0 characters.
    //Note that this method doesn't handle the end of the
    // data very gracefully for those cases where the number
    // of required bits is not a multiple of 8.  It simply
    // adds enough "0" characters to the end to cause the
    // length to be a multiple of 8.  This may result in
    // extraneous characters at the end of the decoded
    // message later. However, it isn't difficult to remove
    // the extraneous characters at decode time as long as
    // the length of the original message is known.
    //For illustration purposes, this method may optionally
    // display the extended version of the stringEncodedData
    // for comparison with the non-extended version.
    //Note that the binary Huffman-encoded data produced by
    // this method is stored in a data structure of type
    // ArrayList <Byte>.
    void encodeStringToBits() {
        //Extend the length of the stringEncodedData to cause
        // it to be a multiple of 8.
        int remainder = stringEncodedData.length() % 8;
        for (int i = 0; i < (8 - remainder); i++) {
            stringEncodedData += "0";
        }

        //For illustration purposes only, enable the following
        // two statements to display the extended
        // stringEncodedData in the same format as the
        // original stringEncodedData.
        System.out.println("Extended String Encoded Data");
        display48(stringEncodedData);

        //Extract the String representations of the required
        // eight bits.  Generate eight actual matching bits by
        // looking the bit combination up in a table.
        for (int i = 0; i < stringEncodedData.length(); i += 8) {
            String strBits = stringEncodedData.substring(i, i + 8);
            byte realBits = encodingBitMap.get(strBits);
            binaryEncodedData.add(realBits);
        }
    }

    //Method to display a String 48 characters to the line.
    void display48(String data) {
        for (int i = 0; i < data.length(); i += 48) {
            if ((i + 48) < data.length()) {
                //Display 48 characters.
                System.out.println(data.substring(i, i + 48));
            }
            else {
                //Display the final line, which may be short.
                System.out.println(data.substring(i));
            }
        }
    }

}