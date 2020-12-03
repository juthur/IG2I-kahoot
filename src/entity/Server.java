package entity;

import utils.Categorie;
import utils.Question;
import utils.Quizz;
import utils.RequeteKahoot;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Server {

    private RequeteKahoot req;
    private ServerSocket server;
    List<Socket> clients = new ArrayList<>();
    private int[] score;
    private int user_max_size;

    private Scanner serverIn = new Scanner(System.in);
    private boolean endGame = false;
    private boolean startGame = false;

    public Server(String mdp) {
        this.req = new RequeteKahoot(mdp);
        if(!this.demarrer())
            return;

        this.partie();
    }

    private boolean demarrer() {
        try {
            this.server = new ServerSocket(8080);
            System.out.println("[SYSTEM] Serveur démarré");
            return true;
        } catch (IOException e) {
            System.err.println("[SYSTEM] Une erreur est survenue au lancement du serveur !");
            return false;
        }
    }

    private void partie() {
        while(!endGame) {
            try {
                clients.add(server.accept());
                System.out.println("Il y a " + clients.size() + " connecté");

                for(Socket s : clients)
                    if(!s.isConnected())
                        clients.remove(s);

                broadcast(clients, "Il y a " + clients.size() + " connecté");

                new Thread(() -> reading()).start();

            } catch (IOException e) {
                System.err.println("[SERVER] Un client a tenté de se connecter sans succès");
            }

            try {
                reading();
            } catch (Exception e) {
                System.err.println("[SYSTEM] Impossible de lire l'input du serveur !");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { } // Pas bien mais pas grave si ça fonctionne pas
        }

        if(startGame) {
            List<Question> questionList = null;

            System.out.println("Quels thèmes voulez-vous ? ");

            List<Categorie> themesLibelle = new ArrayList<>();
            themesLibelle.addAll(req.getListeCategories());
            Collections.sort(themesLibelle);
            for(Categorie c : themesLibelle)
                System.out.println(c.getId() + ". " + c.getTexte());
            System.out.println("-1. Tous");
            System.out.println("Votre choix : ");

            int theme = serverIn.nextInt();
            if (theme < 0) {
                questionList = req.getAllQuestions();
            } else {
                questionList = req.getAllQuestions(theme);
            }

            System.out.print("Combien de questions souhaitez-vous (chiffre/nombre) : ");
            user_max_size = serverIn.nextInt();

            Collections.shuffle(questionList);
            for(int i = 0; i < user_max_size && i < questionList.size(); ++i) {
                Question q = questionList.get(i);
                broadcast(clients, q.toString());
                broadcast(clients, "Votre réponse (chiffre) : ");

                askAll(clients, q);
            }
            broadcast(clients, "");
            displayScoreAll(clients);
            broadcast(clients, "Partie terminée !");

            endGame = true;
        }

        if(endGame) {
            broadcast(clients, "[SYSTEM] Le serveur est fermé");
            serverIn.close();
            closeServer();
        }
    }

    private void reading() {
        if(this.serverIn.hasNextLine()) {
            String cmd = this.serverIn.nextLine();
            if (cmd.equalsIgnoreCase("exit")) {
                this.endGame = true;
            } else if (cmd.equalsIgnoreCase("start")) {
                this.startGame = true;
                score = new int[clients.size()];
            }
        }
    }

    private void broadcast(List<Socket> clients, String message) {
        for(Socket s : clients) {
            try {
                PrintWriter writer = new PrintWriter(s.getOutputStream());
                writer.write(message);
                writer.close();
            } catch (IOException e) {} // OSEF
        }
    }

    private void askAll(List<Socket> clients, Question q) {
        for(int i = 0; i < clients.size(); ++i) {
            try {
                Socket s = clients.get(i);
                PrintWriter writer = new PrintWriter(s.getOutputStream());
                Scanner clientIn = new Scanner(s.getInputStream());

                int reponseJoueur = clientIn.nextInt();

                if (q.getLesPropositions()[reponseJoueur].equals(q.getBonneReponse())) {
                    writer.write("Bonne réponse !");
                    ++score[i];
                } else {
                    writer.write("Mauvaise réponse, la bonne réponse était '" + q.getBonneReponse().getTexteOption() + "'");
                }
                writer.write("Score actuel : " + score[i]);
                writer.write("");
            } catch (IOException e) { }
        }
    }

    private void displayScoreAll(List<Socket> clients) {
        for(Socket s : clients) {
            try {
                PrintWriter writer = new PrintWriter(s.getOutputStream());
                writer.write("Score : " + score + ", Score maximal : " + user_max_size);
            } catch (Exception e) {
            }
        }
    }

    private boolean closeServer() {
        try {
            server.close();
            req.close();
            return true;
        } catch (IOException e) {
            System.err.println("[SYSTEM] Impossible de fermer le serveur !");
            return false;
        }
    }
}
