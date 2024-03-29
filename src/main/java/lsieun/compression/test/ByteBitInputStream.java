package lsieun.compression.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


/**
 * A stream of bits that can be read. Because they come from an underlying byte stream,
 * the total number of bits is always a multiple of 8. The bits are read in little endian.
 * Mutable and not thread-safe.
 */
public final class ByteBitInputStream implements BitInputStream {

    /*---- Fields ----*/

    // The underlying byte stream to read from (not null).
    private InputStream input;

    // Either in the range [0x00, 0xFF] if bits are available, or -1 if end of stream is reached.
    private int currentByte;

    // Number of remaining bits in the current byte, always between 0 and 7 (inclusive).
    private int numBitsRemaining;



    /*---- Constructor ----*/

    /**
     * Constructs a bit input stream based on the specified byte input stream.
     *
     * @param in the byte input stream (not {@code null})
     * @throws NullPointerException if the input stream is {@code null}
     */
    public ByteBitInputStream(InputStream in) {
        input = Objects.requireNonNull(in);
        currentByte = 0;
        numBitsRemaining = 0;
    }



    /*---- Methods ----*/

    @Override
    public int getBitPosition() {
        if (numBitsRemaining < 0 || numBitsRemaining > 7)
            throw new AssertionError();
        return (8 - numBitsRemaining) % 8;
    }


    @Override
    public int readBitMaybe() throws IOException {
        if (currentByte == -1)
            return -1;
        if (numBitsRemaining == 0) {
            currentByte = input.read();
            if (currentByte == -1)
                return -1;
            numBitsRemaining = 8;
        }
        if (numBitsRemaining <= 0)
            throw new AssertionError();
        numBitsRemaining--;
        return (currentByte >>> (7 - numBitsRemaining)) & 1;
    }


    @Override
    public void close() throws IOException {
        input.close();
        currentByte = -1;
        numBitsRemaining = 0;
    }

}
