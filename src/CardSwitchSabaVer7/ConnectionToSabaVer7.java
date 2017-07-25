package CardSwitchSabaVer7;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class ConnectionToSabaVer7 {

    static final TcpClient client = new TcpClient() {
        @Override protected void onRead(ByteBuffer buf) throws Exception { buf.position(buf.limit()); }
        @Override protected void onDisconnected() { }
        @Override protected void onConnected() throws Exception { }
    };
    public ConnectionToSabaVer7() throws InterruptedException {
        start();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                 //   start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void    start() throws InterruptedException {
        client.setAddress(new InetSocketAddress("10.40.56.4", 11355));  // babak insert destination IP here !!! sender
        try {

            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("client status:"+client.isConnected());
        while(!client.isConnected()) Thread.sleep(500);

    }
    public static   synchronized boolean sendOnSocket(String ISOMessage) throws InterruptedException {
        ByteBuffer buf = ByteBuffer.allocate(65535);
        buf.put(ISOMessage.getBytes());
        buf.flip();
        try {

            client.send(buf);
            Thread.sleep(10);
            buf.clear();
           // clean(buf);
            return true;
        } catch (Exception e) {
            while (!client.isConnected()) Thread.sleep(1000);
        }
        return false;

    }
    public static void clean(ByteBuffer bb) {
        if(bb == null) return;
        Cleaner cleaner = ((DirectBuffer) bb).cleaner();
        if (cleaner != null) cleaner.clean();
    }
    public abstract static class TcpClient implements Runnable {
        private static final long INITIAL_RECONNECT_INTERVAL = 500; // 500 ms.
        private static final long MAXIMUM_RECONNECT_INTERVAL = 30000; // 30 sec.
        private static final int READ_BUFFER_SIZE = 0x100000;
        private static final int WRITE_BUFFER_SIZE = 0x100000;

        private long reconnectInterval = INITIAL_RECONNECT_INTERVAL;

        private ByteBuffer readBuf = ByteBuffer.allocateDirect(READ_BUFFER_SIZE); // 1Mb
        private ByteBuffer writeBuf = ByteBuffer.allocateDirect(WRITE_BUFFER_SIZE); // 1Mb

        private final Thread thread = new Thread(this);
        private SocketAddress address;

        private Selector selector;
        private SocketChannel channel;

        private final AtomicBoolean connected = new AtomicBoolean(false);

        private AtomicLong bytesOut = new AtomicLong(0L);
        private AtomicLong bytesIn = new AtomicLong(0L);

        public TcpClient() {

        }

        @PostConstruct
        public void init() {
            assert address != null : "server address missing";
        }

        public void start() throws IOException {
            thread.start();
        }

        public void join() throws InterruptedException {
            if (Thread.currentThread().getId() != thread.getId()) thread.join();
        }

        public void stop() throws IOException, InterruptedException {
            thread.interrupt();
            selector.wakeup();
        }

        public boolean isConnected() {
            return connected.get();
        }


        public void send(ByteBuffer buffer) throws InterruptedException, IOException {
            if (!connected.get()) throw new IOException("not connected");
            synchronized (writeBuf) {
                // try direct write of what's in the buffer to free up space
                if (writeBuf.remaining() < buffer.remaining()) {
                    writeBuf.flip();
                    int bytesOp = 0, bytesTotal = 0;
                    while (writeBuf.hasRemaining() && (bytesOp = channel.write(writeBuf)) > 0) bytesTotal += bytesOp;
                    writeBuf.compact();
                }

                // if didn't help, wait till some space appears
                if (Thread.currentThread().getId() != thread.getId()) {
                    while (writeBuf.remaining() < buffer.remaining()) writeBuf.wait();
                } else {
                    if (writeBuf.remaining() < buffer.remaining())
                        throw new IOException("send buffer full"); // TODO: add reallocation or buffers chain
                }
                writeBuf.put(buffer);

                // try direct write to decrease the latency
                writeBuf.flip();
                int bytesOp = 0, bytesTotal = 0;
                while (writeBuf.hasRemaining() && (bytesOp = channel.write(writeBuf)) > 0) bytesTotal += bytesOp;
                writeBuf.compact();

                if (writeBuf.hasRemaining()) {
                    SelectionKey key = channel.keyFor(selector);
                    key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                    selector.wakeup();
                }
            }
        }


        protected abstract void onRead(ByteBuffer buf) throws Exception;


        protected abstract void onConnected() throws Exception;


        protected abstract void onDisconnected();

        private void configureChannel(SocketChannel channel) throws IOException {
            channel.configureBlocking(false);
            channel.socket().setSendBufferSize(0x100000); // 1Mb
            channel.socket().setReceiveBufferSize(0x100000); // 1Mb
            channel.socket().setKeepAlive(true);
            channel.socket().setReuseAddress(true);
            channel.socket().setSoLinger(false, 0);
            channel.socket().setSoTimeout(0);
            channel.socket().setTcpNoDelay(true);
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) { // reconnection loop
                    try {
                        selector = Selector.open();
                        channel = SocketChannel.open();
                        configureChannel(channel);

                        channel.connect(address);
                        channel.register(selector, SelectionKey.OP_CONNECT);

                        while (!thread.isInterrupted() && channel.isOpen()) { // events multiplexing loop
                            if (selector.select() > 0) processSelectedKeys(selector.selectedKeys());
                        }
                    } catch (Exception e) {
                    } finally {
                        connected.set(false);
                        onDisconnected();
                        writeBuf.clear();
                        readBuf.clear();
                        if (channel != null) channel.close();
                        if (selector != null) selector.close();
                    }

                    try {
                        Thread.sleep(reconnectInterval);
                        if (reconnectInterval < MAXIMUM_RECONNECT_INTERVAL) reconnectInterval *= 2;
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            } catch (Exception e) {
            }

        }

        private void processSelectedKeys(Set keys) throws Exception {
            Iterator itr = keys.iterator();
            while (itr.hasNext()) {
                SelectionKey key = (SelectionKey) itr.next();
                if (key.isReadable()) processRead(key);
                if (key.isWritable()) processWrite(key);
                if (key.isConnectable()) processConnect(key);
                if (key.isAcceptable()) ;
                itr.remove();
            }
        }

        private void processConnect(SelectionKey key) throws Exception {
            SocketChannel ch = (SocketChannel) key.channel();
            if (ch.finishConnect()) {
                key.interestOps(key.interestOps() ^ SelectionKey.OP_CONNECT);
                key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                reconnectInterval = INITIAL_RECONNECT_INTERVAL;
                connected.set(true);
                onConnected();
            }
        }

        private void processRead(SelectionKey key) throws Exception {
            ReadableByteChannel ch = (ReadableByteChannel) key.channel();

            int bytesOp = 0, bytesTotal = 0;
            while (readBuf.hasRemaining() && (bytesOp = ch.read(readBuf)) > 0) bytesTotal += bytesOp;

            if (bytesTotal > 0) {
                readBuf.flip();
                onRead(readBuf);
                readBuf.compact();
            } else if (bytesOp == -1) {
                ch.close();
            }

            bytesIn.addAndGet(bytesTotal);
        }

        private void processWrite(SelectionKey key) throws IOException {
            WritableByteChannel ch = (WritableByteChannel) key.channel();
            synchronized (writeBuf) {
                writeBuf.flip();

                int bytesOp = 0, bytesTotal = 0;
                while (writeBuf.hasRemaining() && (bytesOp = ch.write(writeBuf)) > 0) bytesTotal += bytesOp;

                bytesOut.addAndGet(bytesTotal);

                if (writeBuf.remaining() == 0) {
                    key.interestOps(key.interestOps() ^ SelectionKey.OP_WRITE);
                }

                if (bytesTotal > 0) writeBuf.notify();
                else if (bytesOp == -1) {
                    ch.close();
                }

                writeBuf.compact();
            }
        }

        public SocketAddress getAddress() {
            return address;
        }

        public void setAddress(SocketAddress address) {
            this.address = address;
        }

        public long getBytesOut() {
            return bytesOut.get();
        }

        public long getBytesIn() {
            return bytesIn.get();
        }

    }

}
