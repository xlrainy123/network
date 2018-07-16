# network

### BIO部分
* MyServer:服务端采用一连接一线程的方式，为每个连接维护一个EventThread线程用来接收客户端的消息并进行广播
* MyClient:客户端内部维护了两个线程，AcceptThread和SendThread，前者用于向服务端发送键盘键入的消息，后者用户接收服务端的广播消息
