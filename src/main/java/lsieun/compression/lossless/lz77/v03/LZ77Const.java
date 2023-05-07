package lsieun.compression.lossless.lz77.v03;

public class LZ77Const {
    public static final int LZ77_TYPE_BITS = 1;
    public static final int LZ77_WINOFF_BITS = 12;
    public static final int LZ77_BUFLEN_BITS = 5;
    public static final int LZ77_NEXT_BITS = 8;
    public static final int LZ77_WINDOW_SIZE = 4096;
    public static final int LZ77_BUFFER_SIZE = 32;
    public static final int LZ77_PHRASE_BITS = LZ77_TYPE_BITS + LZ77_WINOFF_BITS + LZ77_NEXT_BITS + LZ77_BUFLEN_BITS;
    public static final int LZ77_SYMBOL_BITS = LZ77_TYPE_BITS + LZ77_NEXT_BITS;
}
