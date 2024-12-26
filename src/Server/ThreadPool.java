package Server;

import Client.Client;
import connectionProtocol.*;
import database.Database;
import connectionProtocol.Package;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {
    private final Database database;
    private final WorkerThread[] threads;
    private final Queue<Package> taskQueue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition taskAvailable = lock.newCondition();
    private volatile boolean isRunning = true;
    private final AtomicInteger currentClients = new AtomicInteger(0);

    public ThreadPool(int numThreads) {
        this.database = new Database();
        threads = new WorkerThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new WorkerThread();
            threads[i].start();
        }
    }

    public Package getProcessedPackage(String clientKey, int type) {
        lock.lock();
        try {
            Client client = database.getClient(clientKey);
            return new Package(clientKey, type);
        } finally {
            lock.unlock();
        }
    }

    public void submit(Package task) {
        lock.lock();
        try {
            currentClients.incrementAndGet();
            taskQueue.add(task);
            taskAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        isRunning = false;
        lock.lock();
        try {
            taskAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public int getCurrentClients() {
        return currentClients.get();
    }

    private void registerClient(Registo pkg) throws Exception {
        System.out.println("Teste1");
        Connection connection = pkg.getConnection();
        Client client = pkg.getClient();
        System.out.println("Teste2");

        boolean success = database.register(client);

        System.out.println("Client saved");

        RegisterResponse response = new RegisterResponse(success);
        connection.send(response.convertPackageToBytes());
        System.out.println("Sign Response sent");
    }

    private void authenticateClient(Login pkg) throws Exception {
        Connection connection = pkg.getConnection();
        String password = pkg.getPassword();
        String clientKey = pkg.getClientKey();

        LoginResponse response = null;
        Boolean sucess = false;
        if(this.database.authenticate(clientKey, password)) {
            Client c = this.database.getClient(clientKey);
            response =  new LoginResponse(true, c);
            sucess = true;
        } else {
            response = new LoginResponse(false);
            sucess = false;
        }

        connection.send(response.convertPackageToBytes());
        System.out.println("Login Response sent " + sucess);
    }

    private class WorkerThread extends Thread {
        public void run() {
            while (isRunning) {
                Package task = null;
                lock.lock();
                try {
                    while (taskQueue.isEmpty() && isRunning) {
                        taskAvailable.await();
                    }

                    if (!isRunning) {
                        return;
                    }

                    task = taskQueue.poll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }

                if (task != null) {
                    try {
                        if (task.isInitialConnection()) {
                            task.processConnection();
                            System.out.println("Package received");
                            task.covertInticialConnection();
                            submit(task);
                        } else {
                            switch (task.getTipo()) {
                                case 0:
                                    System.out.println("Registering client...");
                                    Registo register = Registo.deserialize(task.getConnection().getInputStream());
                                    registerClient(register);
                                    break;
                                case 1:
                                    authenticateClient((Login) task);
                                    break;
                                default:
                                    System.err.println("Unknown package type: " + task.getTipo());
                            }
                            }
                        } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        currentClients.decrementAndGet();
                    }
                }
            }
        }
    }
}