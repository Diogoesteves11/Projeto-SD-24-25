package server;

import client.Client;
import database.Database;
import connectionProtocol.Package;

import java.util.LinkedList;
import java.util.Queue;
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

    public ThreadPool(int numThreads) {
        this.database = new Database();
        threads = new WorkerThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new WorkerThread();
            threads[i].start();
        }
    }

    public Package getProcessedPackage(String clientKey){
        lock.lock();
        try{
            Client client = database.getClient(clientKey);
            return new Package(clientKey, client);
        } finally {
            lock.unlock();
        }
    }

    public void submit(Package task) {
        lock.lock();
        try {
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

    private class WorkerThread extends Thread {
        public void run() {
            while (isRunning) {
                Package task;
                lock.lock();
                try {
                    while (taskQueue.isEmpty() && isRunning) {
                        taskAvailable.await();
                    }

                    if (!isRunning) {
                        return;
                    }

                    task = taskQueue.poll();

                    if (task != null) {
                        try {
                            database.updateClientData(task);
                        } catch (RuntimeException e) {
                            System.err.println("Erro ao executar tarefa: " + e.getMessage());
                        }
                    }
                    taskQueue.remove(task);

                } catch (InterruptedException e) {
                    return;
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
