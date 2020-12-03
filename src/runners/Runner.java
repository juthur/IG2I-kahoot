package runners;

import utils.*;

public class Runner {

    public static void main(String[] args) {
//        System.out.println("Actualisation de la DB");
//        boolean toDb = Quizz.sendToDb();
//        System.out.println(toDb ? "DB OK" : "DB KO");
        Quizz.lancer(args[0]);
    }
}
