package nio;

import sun.nio.cs.ext.HKSCS;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * 这里对NIO中的一个重要的概念：channel进行简要的说明
 * getChannel()
 * read()
 * write()
 * map()
 */
public class ChannelDemo {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        File file = new File("/home/xlcheng/Desktop/github/network/src/main/java/bio/MyClient.java");
        /**
         * getChannel()
         */
        FileChannel in = new FileInputStream(file).getChannel();
        FileChannel out = new FileOutputStream("copy.java").getChannel();

        /**
         * map()
         */

        MappedByteBuffer mbb = in.map(FileChannel.MapMode.READ_ONLY, 0, file.length());

        /**
         * write()
         * 往channel中写数据，相当于从mbb中取数据
         */
        out.write(mbb);
        /**
         * 取完数据之后，position = limit
         * 通过clear()方法，将position = 0， limit = capacity
         */
        mbb.clear();

        System.out.println(ChannelDemo.convert(mbb, "utf-8"));
    }

    /**
     * 封装了一个ByteBuffer和String互相转换的方放
     * @param bb
     * @return
     * @throws CharacterCodingException
     */
    public static String convert(ByteBuffer bb, String coding) throws CharacterCodingException {
        Charset charset = Charset.forName(coding);
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer ch = decoder.decode(bb);
        return ch.toString();
    }
}
