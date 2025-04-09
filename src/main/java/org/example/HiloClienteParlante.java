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
    private int id;

    public HiloClienteParlante(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            sk = new Socket(ClienteMultiHilo.HOST, 5000);
            dos = new DataOutputStream(sk.getOutputStream());
            dis = new DataInputStream(sk.getInputStream());

            System.out.println("Cliente " + id + " envía saludo");
            dos.writeUTF("hola");

            String respuesta = dis.readUTF();
            System.out.println("Cliente " + id + " Servidor devuelve saludo: " + respuesta);

            dis.close();
            dos.close();
            sk.close();

        } catch (IOException ex) {
            Logger.getLogger(HiloClienteParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
