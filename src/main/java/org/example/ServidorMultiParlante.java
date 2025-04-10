package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorMultiParlante extends Thread {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String nombreCliente;
    private List<ServidorMultiParlante> clientes;
    private boolean conectado = false;

    public ServidorMultiParlante(Socket socket, List<ServidorMultiParlante> clientes) {
        this.socket = socket;
        this.clientes = clientes;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServidorMultiParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desconectar() {
        conectado = false;
        try {
            if (nombreCliente != null) {
                System.out.println("Cliente " + nombreCliente + " desconectado");
                broadcastMensaje("Cliente " + nombreCliente + " ha salido del chat");
            }

            ServidorMultiHilo.eliminarCliente(this);

            if (dis != null) dis.close();
            if (dos != null) dos.close();
            if (socket != null) socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorMultiParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            nombreCliente = dis.readUTF();
            conectado = true;

            System.out.println("Cliente " + nombreCliente + " conectado");
            broadcastMensaje("Cliente " + nombreCliente + " se ha unido al chat");

            String mensaje;
            while (conectado) {
                mensaje = dis.readUTF();

                if (mensaje.equalsIgnoreCase("salir")) {
                    desconectar();
                    break;
                }

                System.out.println("Cliente " + nombreCliente + ": " + mensaje);
                broadcastMensaje("Cliente " + nombreCliente + ": " + mensaje);
            }

        } catch (IOException ex) {
            if (conectado) {
                Logger.getLogger(ServidorMultiParlante.class.getName()).log(Level.SEVERE, null, ex);
            }
            desconectar();
        }
    }

    public void enviarMensaje(String mensaje) {
        if (!conectado) return;

        try {
            dos.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ServidorMultiParlante.class.getName()).log(Level.SEVERE, null, ex);
            desconectar();
        }
    }

    private void broadcastMensaje(String mensaje) {
        synchronized (clientes) {
            for (ServidorMultiParlante cliente : clientes) {
                if (cliente != this) {
                    cliente.enviarMensaje(mensaje);
                }
            }
        }
    }
}