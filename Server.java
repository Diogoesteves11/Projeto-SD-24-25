import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class Server {
    private static final int PORT = 8080;
    private final int maxSessions;
    private final Map<String, byte[]> storage;
    private final Map<String, String> users;
    private int currentSessions;

    private final Lock storageLock;
    private final Lock sessionLock;
    private final Condition sessionAvailable;
    private final Map<String, Condition> keyConditions;

    public Server(int maxSessions) {
        this.maxSessions = maxSessions;
        this.storage = new HashMap<>();
        this.users = new HashMap<>();
        this.currentSessions = 0;

        this.storageLock = new ReentrantLock();
        this.sessionLock = new ReentrantLock();
        this.sessionAvailable = sessionLock.newCondition();
        this.keyConditions = new HashMap<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, this);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Métodos de gestão de sessões
    public boolean tryAcquireSession() {
        sessionLock.lock();
        try {
            while (currentSessions >= maxSessions) {
                try {
                    sessionAvailable.await();
                } catch (InterruptedException e) {
                    return false;
                }
            }
            currentSessions++;
            return true;
        } finally {
            sessionLock.unlock();
        }
    }

    public void releaseSession() {
        sessionLock.lock();
        try {
            if (currentSessions > 0) {
                currentSessions--;
                sessionAvailable.signal();
            }
        } finally {
            sessionLock.unlock();
        }
    }

    public void put(String key, byte[] value) {
        storageLock.lock();
        try {
            storage.put(key, value);

            // Notifica as threads aguardando pela condição dessa chave
            Condition condition = keyConditions.get(key);
            if (condition != null) {
                condition.signalAll();
            }
        } finally {
            storageLock.unlock();
        }
    }

    public byte[] get(String key) {
        storageLock.lock();
        try {
            return storage.get(key);
        } finally {
            storageLock.unlock();
        }
    }

    public void multiPut(Map<String, byte[]> pairs) {
        storageLock.lock();
        try {
            for (Map.Entry<String, byte[]> entry : pairs.entrySet()) {
                storage.put(entry.getKey(), entry.getValue());

                // Notifica as condições associadas
                Condition condition = keyConditions.get(entry.getKey());
                if (condition != null) {
                    condition.signalAll();
                }
            }
        } finally {
            storageLock.unlock();
        }
    }

    public Map<String, byte[]> multiGet(Set<String> keys) {
        Map<String, byte[]> result = new HashMap<>();

        storageLock.lock();
        try {
            for (String key : keys) {
                if (storage.containsKey(key)) {
                    result.put(key, storage.get(key));
                }
            }
        } finally {
            storageLock.unlock();
        }

        return result;
    }

    public byte[] getWhen(String key, String keyCond, byte[] valueCond) {
        storageLock.lock();
        try {
            keyConditions.putIfAbsent(keyCond, storageLock.newCondition());
            Condition condition = keyConditions.get(keyCond);

            while (true) {
                byte[] currentValue = storage.get(keyCond);

                if (currentValue != null && Arrays.equals(currentValue, valueCond)) {
                    return storage.get(key);
                }

                try {
                    condition.await(); // Bloqueia até a condição ser satisfeita
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrupted while waiting for condition", e);
                }
            }
        } finally {
            storageLock.unlock();
        }
    }

    public boolean registerUser(String username, String password) {
        storageLock.lock();
        try {
            if (!users.containsKey(username)) {
                users.put(username, password);
                return true;
            }
            return false;
        } finally {
            storageLock.unlock();
        }
    }

    public boolean authenticateUser(String username, String password) {
        storageLock.lock();
        try {
            return password.equals(users.get(username));
        } finally {
            storageLock.unlock();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Por favor, forneça o número máximo de sessões.");
            return;
        }

        int maxSessions;
        try {
            maxSessions = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("O número máximo de sessões deve ser um número inteiro válido.");
            return;
        }
        Server server = new Server(maxSessions);
        server.start();
    }
}
