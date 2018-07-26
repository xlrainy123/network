package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public Selector selector = null;
    public ServerSocketChannel serverSocketChannel = null;
    public final int default_port = 30000;

    public EventThread eventThread;

    public NIOServer(){
        try{
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
        }catch (IOException e){
            e.printStackTrace();
        }
        eventThread = new EventThread(selector, serverSocketChannel);
    }

    public void configAndRegister() throws IOException{
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", default_port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void startup(){
        eventThread.start();
    }

    class EventThread extends Thread{

        public Selector selector = null;
        public ServerSocketChannel serverSocketChannel = null;

        public EventThread(Selector selector, ServerSocketChannel serverSocketChannel){
            this.selector = selector;
            this.serverSocketChannel = serverSocketChannel;
        }

        @Override
        public void run(){
            try {
                while(selector.select() > 0){
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    SelectionKey currentKey = null;
                    while(it.hasNext()){
                        currentKey = it.next();
                        it.remove();

                        handlerRequest(currentKey);

                        close(currentKey);
                    }
                }
            }catch (IOException e){

            }
        }

        public void handlerRequest(SelectionKey key) throws IOException{
            if (key.isAcceptable()){
                SocketChannel client = serverSocketChannel.accept();
                client.configureBlocking(false);
                //将client的socketchannel注册到selector上去
                client.register(selector, SelectionKey.OP_READ);
                //继续上得到的注册在selector上的serverSocketChannel监听accept事件
                key.interestOps(SelectionKey.OP_ACCEPT);
            }
            if (key.isReadable()){
                SocketChannel client = (SocketChannel)key.channel();
                ByteBuffer bb = ByteBuffer.allocate(1024);
                String content = readFromChannel(client, bb, "utf-8");
                System.out.println(content);
                key.interestOps(SelectionKey.OP_READ);

                broadcast(content);
            }

        }

        public void broadcast(String content) throws IOException{
            if (content == null || "".equals(content)) return ;
            Charset charset = Charset.forName("utf-8");
            for (SelectionKey key : selector.keys()){
                Channel channel = key.channel();
                if (channel instanceof SocketChannel){
                    ((SocketChannel) channel).write(charset.encode(content));
                }
            }
        }

        public String readFromChannel(SocketChannel client, ByteBuffer bb, String coding) throws IOException{
            String content = "";
            Charset charset = Charset.forName(coding);
            CharsetDecoder decoder = charset.newDecoder();
            while((client.read(bb)) > 0){
                //read()方法是往bb里put数据，之后要读的话，需要flip一下
                bb.flip();
                content += decoder.decode(bb);
                bb.clear();
            }
            return content;
        }

        public void close(SelectionKey key){
            try{
                if (key != null){
                    key.cancel();
                    if (key.channel() != null){
                        key.channel().close();
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException{
        NIOServer server = new NIOServer();
        server.configAndRegister();
        server.startup();
    }
}
