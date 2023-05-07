package lsieun.compression.lossless.huffman.v03;

public class BitArrayInputStream {
    public byte buf[];

    public int pos;
    public int count;

    public BitArrayInputStream(byte buf[]) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }

    // region read bit
    public int bitVal = 0;
    public int bitPos = 0;

    public int readBits(int size) {
        int val = 0;
        for (int i = 0; i < size; i++) {
            if (bitPos == 0) {
                bitVal = readByte();
            }
            val |= ((bitVal >>> bitPos) & 1) << i;
            bitPos = (bitPos + 1) & 7;
        }
        return val;
    }

    public void clearBits() {
        bitVal = 0;
        bitPos = 0;
    }
    // endregion

    // region read byte, short, int
    public int readByte() {
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }

    public int readShort() {
        return (readByte() | readByte() << 8);
    }

    public int readInt() {
        return (readByte() | readByte() << 8 | readByte() << 16 | readByte() << 24);
    }

    public long readUnsignedInt() {
        return (readInt() & 0xFFFFFFFFL);
    }
    // endregion

    public long skipBytes(long n) {
        long k = count - pos;
        if (n < k) {
            k = n < 0 ? 0 : n;
        }

        pos += k;
        return k;
    }

    public int available() {
        return count - pos;
    }
}
