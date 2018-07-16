package bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyServer {

    class EventThread extends Thread{

        public Socket socket = null;
        public BufferedReader br = null;
        public PrintStream bw = null;

        public EventThread(Socket socket) throws IOException{
            this.socket = socket;
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }


        public void run(){
            String content = "";
            System.out.println("开始监听");
            try{
                while(true){
                    content = br.readLine();
                    System.out.println("开始广播");
                    System.out.println(clientSockets);
                    for (Socket client : clientSockets){
                        bw = new PrintStream(client.getOutputStream());
                        bw.println(content);
                        System.out.println(content);
                        //bw.flush();
                    }
                }
            }catch (IOException e){
                clientSockets.remove(socket);
            }
        }
    }

    public ServerSocket serverSocket = null;
    public final int default_port = 30001;
    public List<Socket> clientSockets = Collections.synchronizedList(new ArrayList<Socket>());

    public MyServer(){
        try{
            serverSocket = new ServerSocket(default_port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void runServer() throws IOException {
        System.out.println("server start");
        while(true){
            Socket client = serverSocket.accept();
            System.out.println("连接成功！"+ client.getInetAddress());
            clientSockets.add(client);
            EventThread eventThread = new EventThread(client);
            eventThread.start();
        }
    }


    public static void main(String[] args) throws IOException{
        MyServer server = new MyServer();
        server.runServer();
    }
}
