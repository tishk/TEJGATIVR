import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 7/12/2016.
 */
public class Socket_NIO {

    public Socket_NIO(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void start(){
        try {
            QueueWorker worker = new QueueWorker();
            new Thread(worker).start();
            String host = "10.39.41.110"; //ip of socket server
            int port = 1364; //port of socket server
            InetAddress address = InetAddress.getByName(host);
            new Thread(new Server(address, port, worker)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class Server implements Runnable {
        private InetAddress hostAddress;
        private int port;
        private ServerSocketChannel serverChannel;
        private Selector selector;
        private ByteBuffer readBuffer = ByteBuffer.allocate(8192);
        private QueueWorker worker;
        private List pendingChanges = new LinkedList();
        private Map pendingData = new HashMap();
        public Server(InetAddress hostAddress, int port, QueueWorker worker) throws IOException {
            this.hostAddress = hostAddress;
            this.port = port;
            this.selector = this.initSelector();
            this.worker = worker;
        }
        public void send(SocketChannel socket, byte[] data) {
            synchronized (this.pendingChanges) {
                this.pendingChanges.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

                synchronized (this.pendingData) {
                    List queue = (List) this.pendingData.get(socket);
                    if (queue == null) {
                        queue = new ArrayList();
                        this.pendingData.put(socket, queue);
                    }
                    queue.add(ByteBuffer.wrap(data));
                }
            }

            this.selector.wakeup();
        }
        public void run() {
            while (true) {
                try {
                    synchronized (this.pendingChanges) {
                        Iterator changes = this.pendingChanges.iterator();
                        while (changes.hasNext()) {
                            ChangeRequest change = (ChangeRequest) changes.next();
                            switch (change.type) {
                                case ChangeRequest.CHANGEOPS:
                                    SelectionKey key = change.socket.keyFor(this.selector);
                                    key.interestOps(change.ops);
                            }
                        }
                        this.pendingChanges.clear();
                    }
                    this.selector.select();
                    Iterator selectedKeys = this.selector.selectedKeys().iterator();
                    while (selectedKeys.hasNext()) {
                        SelectionKey key = (SelectionKey) selectedKeys.next();
                        selectedKeys.remove();
                        if (!key.isValid()) {
                            continue;
                        }
                        if (key.isAcceptable()) {
                            this.accept(key);
                        } else if (key.isReadable()) {
                            this.read(key);
                        } else if (key.isWritable()) {
                            this.write(key);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        private void accept(SelectionKey key) throws IOException {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

            SocketChannel socketChannel = serverSocketChannel.accept();
            Socket socket = socketChannel.socket();
            socketChannel.configureBlocking(false);

            socketChannel.register(this.selector, SelectionKey.OP_READ);
        }
        private void read(SelectionKey key) throws IOException {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            this.readBuffer.clear();
            int numRead;
            try {
                numRead = socketChannel.read(this.readBuffer);
            } catch (IOException e) {
                key.cancel();
                socketChannel.close();
                return;
            }

            if (numRead == -1) {
                key.channel().close();
                key.cancel();
                return;
            }
            this.worker.processData(this, socketChannel, this.readBuffer.array(), numRead);
        }
        private void write(SelectionKey key) throws IOException {
            SocketChannel socketChannel = (SocketChannel) key.channel();

            synchronized (this.pendingData) {
                List queue = (List) this.pendingData.get(socketChannel);

                while (!queue.isEmpty()) {
                    ByteBuffer buf = (ByteBuffer) queue.get(0);
                    socketChannel.write(buf);
                    if (buf.remaining() > 0) {
                        break;
                    }
                    queue.remove(0);
                }

                if (queue.isEmpty()) {
                    key.interestOps(SelectionKey.OP_READ);
                }
            }
        }
        private Selector initSelector() throws IOException {
            Selector socketSelector = SelectorProvider.provider().openSelector();

            this.serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);

            InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
            serverChannel.socket().bind(isa);

            serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

            return socketSelector;
        }
    }
    class QueueWorker implements Runnable {
        private List queue = new LinkedList();
        public void processData(Server server, SocketChannel socket, byte[] data, int count) {
            byte[] dataCopy = new byte[count];
            System.arraycopy(data, 0, dataCopy, 0, count);
            synchronized(queue) {
                queue.add(new ServerDataEvent(server, socket, dataCopy));
                queue.notify();
            }
        }
        public void run() {
            ServerDataEvent dataEvent;
            while(true) {
                synchronized(queue) {
                    while(queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    dataEvent = (ServerDataEvent) queue.remove(0);
                }
                dataEvent.server.send(dataEvent.socket, dataEvent.data);
            }
        }
    }
    class ServerDataEvent {
        public Server server;
        public SocketChannel socket;
        String faxFile="";
        String faxNumber="";
        public byte[] data;
        public ServerDataEvent(Server server, SocketChannel socket, byte[] data) {
            this.server = server;
            this.socket = socket;
            this.data = data;
            processMessage(new String(data));
        }
        private  void processMessage(final String RCVMessage){

           // do something with receive message

        }
    }
    class ChangeRequest {
        public static final int REGISTER = 1;
        public static final int CHANGEOPS = 2;
        public SocketChannel socket;
        public int type;
        public int ops;
        public ChangeRequest(SocketChannel socket, int type, int ops) {
            this.socket = socket;
            this.type = type;
            this.ops = ops;
        }
    }

}
