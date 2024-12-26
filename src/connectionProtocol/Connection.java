package connectionProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class Connection {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private ReentrantLock readLock;
    private ReentrantLock writeLock;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.readLock = new ReentrantLock();
        this.writeLock = new ReentrantLock();
    }

    public DataInputStream getInputStream() {
        return this.input;
    }

    public DataOutputStream getOutputStream() {
        return this.output;
    }

    public void send(byte[] data){
        writeLock.lock();
        try{
            output.writeInt(data.length);
            output.write(data);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public byte[] read() {
        readLock.lock();
        try {
            byte[] data = new byte[input.readInt()];
            input.readFully(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            readLock.unlock();
        }
    }

    public void close() throws Exception {
        this.socket.close();
    }
}