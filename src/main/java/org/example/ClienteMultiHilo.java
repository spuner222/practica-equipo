package org.example;

import java.io.IOException;
import java.util.Scanner;

public class ClienteMultiHilo {

    public static final String HOST = "192.168.137.213";
    public static final int PUERTO = 5000;

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingresa tu nombre: ");
        String nombre = scanner.nextLine();

        System.out.println("Conectando al servidor...");
        HiloClienteParlante cliente = new HiloClienteParlante(nombre);
        cliente.start();

        System.out.println("Escribe tus mensajes (escribe 'salir' para terminar):");
        String mensaje;
        while (true) {
            mensaje = scanner.nextLine();
            cliente.enviarMensaje(mensaje);

            if (mensaje.equalsIgnoreCase("salir")) {
                break;
            }
        }

        cliente.desconectar();
        System.out.println("Conexión terminada.");
    }
}