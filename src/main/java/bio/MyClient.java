package bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {

    public AcceptThread at = null;
    public SendThread st = null;

    class AcceptThread extends Thread{

        Socket socket = null;
        public BufferedReader in = null;

        public AcceptThread(Socket socket){
            this.socket = socket;
        }

        public void preRun() throws IOException{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run(){

            while(true){
                String content = "";
                try{
                    content = in.readLine();
                    if (content == null || "".equals(content)) continue;
                    System.out.println("客户端接受数据："+content);
                }catch (IOException e){

                }
            }
        }
    }

    class SendThread extends Thread{

        Socket socket = null;
        PrintStream ps = null;
        Scanner in = null;

        public SendThread(Socket socket){
            this.socket = socket;
        }

        public void preRun() throws IOException{
            ps = new PrintStream(socket.getOutputStream());
            in = new Scanner(System.in);
        }

        @Override
        public void run(){
            System.out.println("可以输入内容试试");
            while (in.hasNext()){
                String content = in.nextLine();
                System.out.println("开始发送数据："+content);
                ps.println(content);
            }
        }

    }

    public Socket socket = null;
    public PrintStream ps = null;
    public BufferedReader br = null;

    public MyClient(Socket socket){
        this.socket = socket;
    }

    public void config(){
        try{
            ps = new PrintStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            e.printStackTrace();
        }
        at = new AcceptThread(socket);
        st = new SendThread(socket);
        try{
            at.preRun();
            st.preRun();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void connect() throws IOException{
        socket.connect(new InetSocketAddress("127.0.0.1", 30001));
    }
    public void startup(){
        at.start();
        st.start();
    }

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket();
        MyClient client = new MyClient(socket);
        client.connect();
        client.config();
        client.startup();

    }
}
