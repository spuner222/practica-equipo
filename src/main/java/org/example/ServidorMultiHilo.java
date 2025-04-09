package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorMultiHilo {

    static final int PUERTO = 5000;

    public static void main(String[] args) {
        ServerSocket ss;
        System.out.print("Inicializando servidor... ");

        try {
            ss = new ServerSocket(PUERTO);
            System.out.println("\t[OK]");
            int idSession = 0;

            while (true) {
                Socket socket = ss.accept();
                System.out.println("Nueva conexión entrante: " + socket);

                ServidorMultiParlante hilo = new ServidorMultiParlante(socket, idSession);
                hilo.start();
                idSession++;
            }

        } catch (IOException ex) {
            Logger.getLogger(ServidorMultiHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
