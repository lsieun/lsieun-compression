package lsieun.compression.lossless.huffman.v01;
/*File Huffman01.java

This program illustrates the encoding and later decoding of
a text message using the Huffman encoding technique.

This program is provided for educational purposes only. If
you use the program for any purpose, you use it at your
own risk.  The author of the program accepts no
responsibility for any damages that may result from your
use of the program.

We begin by instantiating an object of the HuffmanEncoder 
class and invoking the encode method on that object.

Inside the encode method, we invoke the createFreqData 
method to create a frequency chart that identifies each of 
the individual characters in the original message and the 
number of times (frequency) that each character appeared in
the message.

Next, we invoke the createLeaves method to create a 
HuffLeaf object for each character identified in the 
frequency chart.  We store the HuffLeaf objects in a 
TreeSet object.  Each HuffLeaf object encapsulates the 
character as well as the number of times that the 
character appeared in the original message (frequency).

Then we invoke the createHuffTree method to assemble the 
HuffLeaf objects into  a Huffman tree (a HuffTree object).
A Huffman tree is a special form of a binary tree  
consisting of properly linked HuffNode and HuffLeaf 
objects.

When the createHuffTree method returns, the HuffTree
object remains as the only object stored in the TreeSet 
object that previously contained all of the HuffLeaf 
objects.  This is because all of the HuffLeaf objects have 
been combined with HuffNode objects to form the tree.

Following that, we invoke the createBitCodes method. This
method uses the Huffman tree in a recursive manner to 
create a bit code for each character in the message.  The
bit codes are different lengths with the shorter codes 
corresponding to the characters with a high frequency 
value and the longer codes corresponding to the characters 
with the lower frequency values.  The  createBitCodes 
method populates a data structure that is required later 
to decode the encoded message.
    
At this point, we know the variable-length bitcode that is 
required to replace each character in the original message 
to produce a Huffman-encoded message.  (The compression 
provided by Huffman encoding depends on the frequently
used characters having short bitcodes and the less 
frequently used characters having longer bitcodes.)
    
Although we know the bitcode required to replace each
character in the original message, a direct transformation 
from characters in the message to a stream of contiguous 
bitcodes is something of a challenge.  The computer's 
memory is organized on 8-bit boundaries.  I am unaware of 
any capability in Java that allows the memory to be viewed 
simply as a continuous sequence of individual bits.

(Note that it may be possible to accomplish this by using 
a Java BitSet object.  I may give that a try someday when I
have the time.)
    
This program uses a solution to this challenge that is 
straightford, but is probably inefficient from both a speed
and memory requirement viewpoint.  The solution is to do a 
simple table lookup in order to create a long String object
consisting of only 1 and 0  characters.  Each character in 
the original message is represented by a substring that 
matches the required bitcode. This is easy to accomplish
because (unlike a long sequence of bits) there are no 
artificial boundaries requiring the length of the String to
be some multiple of a fixed number of characters.

We invoke the encodeToString method to encode the message 
into a String representation of the bits that will make up 
the final encoded message.      
    
After the String containing 1 and 0 characters representing
the bits in the Huffman-encoded message is created, this 
String is processed to produce the Huffman-encoded message
in a binary bit stream format.  This is accomplished using 
another lookup table containing 256 entries (the number of 
possible combinations of eight bits).

We invoke the buildEncodingBitMap method to populate a 
lookup table that relates eight bits represented as a 
String to every possible combination of eight actual bits.

Then we invoke the encodeStringToBits method to Encode the 
String representation of the bits that make up the encoded 
message to the actual bits that make up the encoded 
message.

Note that this method doesn't handle the end of the message
very gracefully for those cases where the number of 
required bits is not a multiple of 8.  The method simply 
adds enough "0" characters to the end of the String to 
cause the length to be a multiple of 8.  This will usually
result in extraneous characters at the end of the decoded
message later.  Some mechanism must be found to eliminate
the extraneous characters when decoding the message.  This
program assumes that the length of the original message is
preserved and provided to the decoding software along with
the decoding table.  Since the length of the decoded 
message must match the length of the original message, this
value is used to eliminate extraneous characters at the
end of the decoded message.

Then we return the binaryEncodedData from the encode method
to the main method.  The message has now been Huffman 
encoded.  We provide the capability to display the
binaryEncodedData in Hexadecimal format at this point for
comparison with the original message.

The program continues the demonstration by decoding and
displaying the Huffman-encoded message.

We begin the decoding process by instantiating a 
HuffmanDecoder object.

Then we invoke the decode method on the HuffmanDecoder 
object to decode the message.  We pass the encoded message 
along with a reference to a data structure containing 
encoding particulars and the length of the original 
message so that extraneous characters on the end can be 
eliminated.
    
Inside the decode method, we invoke the buildDecodingBitMap
method to create a decoding bit map, which is essentially 
the reverse of the encoding bit map that was used to encode 
the original message.

We invoke the decodeToBitsAsString method to decode the 
encoded message from a binary representation to a String of
1 and 0 characters that represent the actual bits in the 
encoded message.
    
We invoke the buildHuffDecodingTable method to create a 
Huffman decoding table by swapping the keys and the values 
from the Huffman encoding table received as an incoming 
parameter by the decode method.
    
Finally, we invoke the decodeStringBitsToCharacters method
to decode the String containing only 1 and 0 characters
that represent the bits in the encoded message. This
produces a replica of the original message that was
subjected to Huffman encoding.  We write the resulting
decoded message into a String object and return the 
decoded message with any extraneous characters at the end
having been removed.

**********************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Huffman01 {
    public static void main(String[] args) {
        String rawData = HuffmanSample.DATA2;
        int rawDataLen = rawData.length();

        System.out.println("Raw Data");
        display48(rawData);
        System.out.println("Number raw data bits: " + rawData.length() * 8);

        Map<Character, String> huffEncodeTable = new HashMap<>();
        HuffmanEncoder encoder = new HuffmanEncoder();
        ArrayList<Byte> binaryEncodedData = encoder.encode(rawData, huffEncodeTable);

        System.out.println("Number binary encoded data bits: " + binaryEncodedData.size() * 8);
        System.out.println("Compression factor: " + (double) rawData.length() / binaryEncodedData.size());

        System.out.println("Binary Encoded Data in Hexadecimal Format");
        hexDisplay48(binaryEncodedData);
        System.out.println();


        HuffmanDecoder decoder = new HuffmanDecoder();
        String decodedData = decoder.decode(binaryEncodedData, huffEncodeTable, rawDataLen);
        System.out.println("Decoded Data");
        display48(decodedData);
    }

    static void display48(String data) {
        for (int i = 0; i < data.length(); i += 48) {
            if ((i + 48) < data.length()) {
                System.out.println(data.substring(i, i + 48));
            }
            else {
                System.out.println(data.substring(i));
            }
        }
    }

    static void hexDisplay48(ArrayList<Byte> binaryEncodedData) {
        int i = 0;
        for (Byte element : binaryEncodedData) {
            System.out.print(Integer.toHexString((int) element & 0X00FF));
            i++;
            if (i % 24 == 0) {
                System.out.println();//new line
                i = 0;
            }
        }
    }

}
