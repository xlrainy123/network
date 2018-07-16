package bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {

    static class AcceptThread extends Thread{
        Socket socket = null;
        public BufferedReader in = null;

        public AcceptThread(Socket socket){
            this.socket = socket;
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void run(){

            while(true){
                String content = "";
                try{
                    content = in.readLine();
                    System.out.println("客户端接受数据："+content);
                }catch (IOException e){

                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 30001), 5000);
        Scanner in = new Scanner(System.in);
        PrintStream bw = new PrintStream(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        AcceptThread acceptThread = new AcceptThread(socket);
        acceptThread.start();

        while (in.hasNext()){
            String content = in.nextLine();
            System.out.println("开始发送数据："+content);
            bw.println(content);
           // bw.flush();
        }
    }
}
