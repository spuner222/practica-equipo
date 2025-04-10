package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloClienteParlante extends Thread {

    protected Socket sk;
    protected DataOutputStream dos;
    protected DataInputStream dis;
    private String nombre;
    private boolean conectado = false;

    public HiloClienteParlante(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {
        try {
            sk = new Socket(ClienteMultiHilo.HOST, ClienteMultiHilo.PUERTO);
            dos = new DataOutputStream(sk.getOutputStream());
            dis = new DataInputStream(sk.getInputStream());

            dos.writeUTF(nombre);

            conectado = true;

            new Thread(() -> {
                String mensaje;
                try {
                    while (conectado) {
                        mensaje = dis.readUTF();
                        System.out.println(mensaje);
                    }
                } catch (IOException ex) {
                    if (conectado) {
                        Logger.getLogger(HiloClienteParlante.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("Conexión con el servidor perdida.");
                    }
                    conectado = false;
                }
            }).start();

        } catch (IOException ex) {
            Logger.getLogger(HiloClienteParlante.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No se pudo conectar al servidor.");
        }
    }

    public void enviarMensaje(String mensaje) {
        if (!conectado) return;

        try {
            dos.writeUTF(mensaje);
            if (mensaje.equalsIgnoreCase("salir")) {
                desconectar();
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloClienteParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desconectar() {
        conectado = false;
        try {
            if (dis != null) dis.close();
            if (dos != null) dos.close();
            if (sk != null) sk.close();
        } catch (IOException ex) {
            Logger.getLogger(HiloClienteParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}