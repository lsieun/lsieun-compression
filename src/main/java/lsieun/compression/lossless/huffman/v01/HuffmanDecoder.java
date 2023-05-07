package lsieun.compression.lossless.huffman.v01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// An object of this class can be used to decode a
// Huffman-encoded message given the encoded message, 
// a data structure containing particulars as to how the
// original message was encoded, and the length of the
// original message..
class HuffmanDecoder {
    Map<String, Character> huffDecodeTable = new HashMap<>();
    String stringDecodedData;
    String decodedData = "";
    Map<Byte, String> decodingBitMap = new HashMap<>();
    ArrayList<Byte> binaryEncodedData;

    //The following structure contains particulars as to how
    // the original message was encoded, and must be received
    // as an incoming parameter to the decode method along
    // with the encoded message and the length of the
    // original message.
    Map<Character, String> huffEncodeTable;
    //Used to eliminate the extraneous characters on the end.
    int rawDataLen;
    //-----------------------------------------------------//

    //This method receives a Huffman-encoded message along
    // with a data structure containing particulars as to how
    // the original message was encoded and the length of the
    // original message.  It decodes the original message and
    // returns the decoded version as a String object.
    String decode(ArrayList<Byte> binaryEncodedData, Map<Character, String> huffEncodeTable, int rawDataLen) {
        // Save the incoming parameters.
        this.binaryEncodedData = binaryEncodedData;
        this.huffEncodeTable = huffEncodeTable;
        this.rawDataLen = rawDataLen;

        // Create a decoding bit map,
        // which is essentially the reverse of the encoding bit map
        // that was used to encode the original message.
        buildDecodingBitMap();

        //Decode the encoded message from a binary
        // representation to a String of 1 and 0 characters
        // that represent the actual bits in the encoded
        // message.  Also, for illustration purposes only,
        // this method may optionally display the String.
        decodeToBitsAsString();

        //Create a Huffman decoding table by swapping the keys
        // and values from the Huffman encoding table received
        // as an incoming parameter by the decode method.
        buildHuffDecodingTable();

        //Decode the String containing only 1 and 0 characters
        // that represent the bits in the encoded message. This
        // produces a replica of the original message that was
        // subjected to Huffman encoding.  Write the resulting
        // decoded message into a String object referred to by
        // decoded data.
        decodeStringBitsToCharacters();

        //Return the decoded message.  Eliminate the extraneous
        // characters from the end of the message on the basis
        // of the known length of the original message.
        return decodedData.substring(0, rawDataLen);
    }

    void buildDecodingBitMap() {
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

            decodingBitMap.put((byte) (i), sb.toString());
        }
    }

    void decodeToBitsAsString() {
        StringBuilder sb = new StringBuilder();

        for (Byte element : binaryEncodedData) {
            byte wholeByte = element;
            sb.append(decodingBitMap.get(wholeByte));
        }

        //Convert from StringBuffer to String
        stringDecodedData = sb.toString();

        //For illustration purposes only, enable the following
        // two statements to display the String containing 1
        // and 0 characters that represent the bits in the
        // encoded message.
        System.out.println("String Decoded Data");
        display48(stringDecodedData);
    }

    void buildHuffDecodingTable() {
        Set<Map.Entry<Character, String>> entries = huffEncodeTable.entrySet();
        for (Map.Entry<Character, String> entry : entries) {
            Character key = entry.getKey();
            String value = entry.getValue();
            huffDecodeTable.put(value, key);
        }
    }

    void decodeStringBitsToCharacters() {
        StringBuilder output = new StringBuilder();
        StringBuilder workBuf = new StringBuilder();

        for (int i = 0; i < stringDecodedData.length(); i++) {
            workBuf.append(stringDecodedData.charAt(i));
            if (huffDecodeTable.containsKey(workBuf.toString())) {
                output.append(huffDecodeTable.get(workBuf.toString()));
                workBuf = new StringBuilder();
            }
        }

        decodedData = output.toString();
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
