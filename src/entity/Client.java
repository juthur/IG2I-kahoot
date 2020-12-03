package entity;

import utils.Joueur;
import utils.RequeteKahoot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket client;

    public Client() {
        if(!this.connection()) {
            System.err.println("[SYSTEM] La connexion au serveur a échoué !");
            return;
        }

        System.out.println("[SYSTEM] Connecté");
        new Thread(() -> handle(client)).start();
    }

    private static void handle(final Socket client) {
        Scanner sc = null;
        PrintWriter pw = null;
        try {
            sc = new Scanner(client.getInputStream());
            pw = new PrintWriter(client.getOutputStream());

            Scanner clientIn = new Scanner(System.in);

            boolean exit = false;
            while(!exit) {
                String line = "";
                if(clientIn.hasNextLine())
                    line = clientIn.nextLine();

                if(sc.hasNextLine())
                    System.out.println(sc.nextLine());

                if(line.equalsIgnoreCase("exit"))
                    exit = true;
                else {
                    pw.write(line);
                    pw.flush();
                }
            }

            if(exit) {
                clientIn.close();
                sc.close();
                pw.close();
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean connection() {
        try {
            this.client = new Socket("127.0.0.1", 8080);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
