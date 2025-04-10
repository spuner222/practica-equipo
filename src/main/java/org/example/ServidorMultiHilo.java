package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorMultiHilo {

    static final int PUERTO = 5000;
    private static List<ServidorMultiParlante> clientes = new ArrayList<>();

    public static void main(String[] args) {
        ServerSocket ss;
        System.out.print("Inicializando servidor... ");

        try {
            ss = new ServerSocket(PUERTO);
            System.out.println("\t[OK]");

            new Thread(() -> {
                try {
                    while (true) {
                        Socket socket = ss.accept();

                        ServidorMultiParlante hiloCliente = new ServidorMultiParlante(socket, clientes);
                        synchronized (clientes) {
                            clientes.add(hiloCliente);
                        }
                        hiloCliente.start();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ServidorMultiHilo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Escribe para enviar mensajes a todos los clientes:");

            while (true) {
                String mensaje = scanner.nextLine();
                if (!mensaje.isEmpty()) {
                    synchronized (clientes) {
                        for (ServidorMultiParlante cliente : clientes) {
                            cliente.enviarMensaje("Servidor: " + mensaje);
                        }
                    }
                    System.out.println("Mensaje enviado a todos los clientes");
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ServidorMultiHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void eliminarCliente(ServidorMultiParlante cliente) {
        synchronized (clientes) {
            clientes.remove(cliente);
        }
    }
}