package org.example;

import java.io.IOException;
import java.util.ArrayList;

public class ClienteMultiHilo {

    static final int MAX_HILOS = 10;
    public static final String HOST = "192.168.137.213";

    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<Thread> clients = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            clients.add(new HiloClienteParlante(i));
        }

        for (Thread thread : clients) {
            thread.start();
        }

        for (Thread thread : clients) {
            thread.join();
        }

        System.out.println("Todos los clientes terminaron.");
    }
}
