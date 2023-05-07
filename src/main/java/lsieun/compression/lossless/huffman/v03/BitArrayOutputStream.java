package lsieun.compression.lossless.huffman.v03;

import java.util.Arrays;

public class BitArrayOutputStream {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    protected byte[] buf;
    protected int count;

    public BitArrayOutputStream(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        buf = new byte[size];
    }

    // region write bit
    public int bitVal = 0;
    public int bitPos = 0;

    public void writeBits(int val, int size) {
        for (int i = 0; i < size; i++) {
            bitVal |= ((val >>> i) & 1) << bitPos;
            bitPos = (bitPos + 1) & 7;
            if (bitPos == 0) {
                writeByte(bitVal);
                bitVal = 0;
            }
        }
    }

    public void writeBitsR(int val, int size) {
        for (int i = size - 1; i >= 0; i--) {
            bitVal |= ((val >>> i) & 1) << bitPos;
            bitPos = (bitPos + 1) & 7;
            if (bitPos == 0) {
                writeByte(bitVal);
                bitVal = 0;
            }
        }
    }
    // endregion

    // region write byte, short, int, long
    public void writeByte(int b) {
        ensureCapacity(count + 1);
        buf[count] = (byte) b;
        count += 1;
    }

    public void writeByteArray(byte[] b) {
        writeByteArray(b, 0, b.length);
    }

    public void writeByteArray(byte b[], int off, int len) {
        if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) - b.length > 0)) {
            throw new IndexOutOfBoundsException();
        }
        ensureCapacity(count + len);
        System.arraycopy(b, off, buf, count, len);
        count += len;
    }

    public void writeShort(int val) {
        writeByte((byte) (val));
        writeByte((byte) (val >> 8));
    }

    public void writeInt(int val) {
        writeByte((byte) (val));
        writeByte((byte) (val >> 8));
        writeByte((byte) (val >> 16));
        writeByte((byte) (val >> 24));
    }

    public void writeUnsignedInt(long val) {
        writeByte((byte) (val));
        writeByte((byte) (val >> 8));
        writeByte((byte) (val >> 16));
        writeByte((byte) (val >> 24));
    }
    // endregion

    public int size() {
        return count;
    }

    public byte toByteArray()[] {
        return Arrays.copyOf(buf, count);
    }

    // region private methods
    private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - buf.length > 0) {
            grow(minCapacity);
        }
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = buf.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }

        buf = Arrays.copyOf(buf, newCapacity);
    }
    // endregion
}
