import SystemStatus.Status_All;
import utils.strUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 5/7/16.
 */
public class InternalListener {

    utils.strUtils strutils=new strUtils();

    public InternalListener(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {

                    startCapitalizeServer();
                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });
    }

    public  void start(){

        try {
            QueueWorker worker = new QueueWorker();
            new Thread(worker).start();
            String host = Properties_Monitoring.getMonitoring_listener_IP();
            int port = Integer.valueOf(Properties_Monitoring.getMonitoring_listener_Port());
            InetAddress address = InetAddress.getByName(host);
            new Thread(new Server(address, port, worker)).start();
            // System.out.println("_____________________________________");
            System.out.println("Monitoring Started...");
            System.out.println("_____________________________________");
        } catch (IOException e) {
            e.printStackTrace();
            // System.out.println("_____________________________________");
            System.out.println("Monitoring Start Error:"+e.toString());
            System.out.println("_____________________________________");
        }

    }
    private void loadSettings(){

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
                // Indicate we want the interest ops set changed
                this.pendingChanges.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

                // And queue the data we want written
                synchronized (this.pendingData) {
                    List queue = (List) this.pendingData.get(socket);
                    if (queue == null) {
                        queue = new ArrayList();
                        this.pendingData.put(socket, queue);
                    }
                    queue.add(ByteBuffer.wrap(data));
                }
            }

            // Finally, wake up our selecting thread so it can make the required changes
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
            // For an accept to be pending the channel must be a server socket channel.
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

            // Accept the connection and make it non-blocking
            SocketChannel socketChannel = serverSocketChannel.accept();
            Socket socket = socketChannel.socket();
            socketChannel.configureBlocking(false);

            // Register the new SocketChannel with our Selector, indicating
            // we'd like to be notified when there's data waiting to be read
            socketChannel.register(this.selector, SelectionKey.OP_READ);
        }
        private void read(SelectionKey key) throws IOException, ClassNotFoundException {
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

                // Write until there's not more data ...
                while (!queue.isEmpty()) {
                    ByteBuffer buf = (ByteBuffer) queue.get(0);
                    socketChannel.write(buf);
                    if (buf.remaining() > 0) {
                        // ... or the socket's buffer fills up
                        break;
                    }
                    queue.remove(0);
                }

                if (queue.isEmpty()) {
                    // We wrote away all data, so we're no longer interested
                    // in writing on this socket. Switch back to waiting for
                    // data.
                    key.interestOps(SelectionKey.OP_READ);
                }
            }
        }
        private Selector initSelector() throws IOException {
            // Create a new selector
            Selector socketSelector = SelectorProvider.provider().openSelector();

            // Create a new non-blocking server socket channel
            this.serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);

            // Bind the server socket to the specified address and port
            InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
            serverChannel.socket().bind(isa);

            // Register the server socket channel, indicating an interest in
            // accepting new connections
            serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

            return socketSelector;
        }
    }
    class QueueWorker implements Runnable {
        private List queue = new LinkedList();
        public void processData(Server server, SocketChannel socket, byte[] data, int count) throws IOException, ClassNotFoundException {
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
        public byte[] data;
        public ServerDataEvent(Server server, SocketChannel socket, byte[] data) throws IOException, ClassNotFoundException {
            this.server = server;
            this.socket = socket;
            this.data = data;

            ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(data));
            System.out.println("got Object:\n"+(String)ois.readObject());
            processMessage((Status_All) ois.readObject());
            server.send(socket,("got new message from is:"+socket.getRemoteAddress()+"\n").getBytes());

            ois.close();

        }
        public  String  getNowTimeWithSeparator(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss.SSS");
            String Now=DateFormat.format(Time);
            return Now;

        }

        private void processMessage(final Status_All statusAll){
            // System.out.println("Start Message Process...");
           // final ExecutorService executorService = Executors.newSingleThreadExecutor();
         //   Future future = executorService.submit(new Runnable() {
              //  public void run() {
                    //   System.out.println("_____________________________________");
                    try {
                        System.out.println(getNowTimeWithSeparator()+":receive message from :"+socket.getRemoteAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("_____________________________________");
                    try {
                        new DBUtils(statusAll);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        //executorService.shutdownNow();
                    }
//
            //    }
        //    });

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


    public class SimpleClient {
        public  void main(String args[]){
            try{
                Socket s = new Socket("localhost",2002);
                OutputStream os = s.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                Status_All to = new Status_All();
                oos.writeObject(to);
                oos.writeObject(new String("another object from the client"));
                oos.close();
                os.close();
                s.close();
            }catch(Exception e){System.out.println(e);}
        }
    }

    private  class Capitalizer extends Thread {
        private Socket socket;
        public Capitalizer(Socket socket, int clientNumber) {
            this.socket = socket;


        }
        public void run() {
            try {
                ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
                System.out.println("got Status from :"+socket.getRemoteSocketAddress());
                processMessage((Status_All) ois.readObject());
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {

                }

            }
        }
        public  String  getNowTimeWithSeparator(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss.SSS");
            String Now=DateFormat.format(Time);
            return Now;

        }
        private void processMessage(final Status_All statusAll){

            System.out.println(getNowTimeWithSeparator()+":receive message from :"+socket.getRemoteSocketAddress());
            System.out.println("_____________________________________");
            try {
                new DBUtils(statusAll);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }

    }
    private  void startCapitalizeServer() throws IOException {

        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(Integer.valueOf(Properties_Monitoring.getMonitoring_listener_Port()));
        try {
            while (true) {
                new Capitalizer(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }



}
