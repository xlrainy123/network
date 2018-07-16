package nio;

import java.nio.ByteBuffer;

/**
 * 在这里主要对NIO中的Buffer类进行简单的说明，以ByteBuffer为例
 * postion
 * limit
 * capacity
 * get()
 * put()
 * get(int)
 * clear()
 * flip()
 * allocate(int)
 */
public class BufferDemo {

    public static void main(String[] args){
        /**
         * 构造一个容量为8的ByteBuffer
         * position = 0
         */
        ByteBuffer bb = ByteBuffer.allocate(8);
        System.out.println("初始化之后的各项数值");
        System.out.println("position = "+bb.position()+", capacity = "+bb.capacity()+", limit = "+bb.limit());

        /**
         * 执行几个put操作
         */
        bb.put((byte) 1);
        bb.put((byte) 2);
        bb.put((byte) 3);
        System.out.println("执行put之后的各项数值");
        System.out.println("position = "+bb.position()+", capacity = "+bb.capacity()+", limit = "+bb.limit());


        /**
         * 执行flip()之后
         */
        bb.flip();
        System.out.println("执行flip()之后");
        System.out.println("position = "+bb.position()+", capacity = "+bb.capacity()+", limit = "+bb.limit());

        /**
         * 执行get()
         */
        System.out.println("get到的数值为："+bb.get());
        System.out.println("执行get()之后");
        System.out.println("position = "+bb.position()+", capacity = "+bb.capacity()+", limit = "+bb.limit());

        /**
         * 执行get(int)
         */
        System.out.println("get到的数值为："+bb.get(2));
        System.out.println("执行get(2)之后");
        System.out.println("position = "+bb.position()+", capacity = "+bb.capacity()+", limit = "+bb.limit());

        /**
         * 执行clear()
         */

        bb.clear();
        System.out.println("执行clear()之后");
        System.out.println("position = "+bb.position()+", capacity = "+bb.capacity()+", limit = "+bb.limit());
    }
}
