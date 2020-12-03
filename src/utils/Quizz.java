package utils;

import java.util.*;

public class Quizz {
    public static void lancer(String mdp) {
        // Création Scanner
        Scanner in = new Scanner(System.in);

        RequeteKahoot req = new RequeteKahoot(mdp);

        List<Question> questionList = null;
        int score = 0;

        System.out.println("Quels thèmes voulez-vous ? ");

        List<Categorie> themesLibelle = new ArrayList<>();
        themesLibelle.addAll(req.getListeCategories());
        Collections.sort(themesLibelle);
        for(Categorie c : themesLibelle)
            System.out.println(c.getId() + ". " + c.getTexte());
        System.out.println("-1. Tous");
        System.out.println("Votre choix : ");

        int theme = in.nextInt();
        if (theme < 0) {
            questionList = req.getAllQuestions();
        } else {
            questionList = req.getAllQuestions(theme);
        }

        System.out.print("Combien de questions souhaitez-vous (chiffre/nombre) : ");
        int user_max_size = in.nextInt();

        Collections.shuffle(questionList);
        for(int i = 0; i < user_max_size && i < questionList.size(); ++i) {
            Question q = questionList.get(i);
            System.out.println(q);
            System.out.print("Votre réponse (chiffre) : ");
            int reponseJoueur = in.nextInt();

            if(q.getLesPropositions()[reponseJoueur].equals(q.getBonneReponse())) {
                System.out.println("Bonne réponse !");
                ++score;
            } else {
                System.out.println("Mauvaise réponse, la bonne réponse était '" + q.getBonneReponse().getTexteOption() + "'");
            }
            System.out.println("Score actuel : " + score);
            System.out.println("");
        }
        req.close();
        in.close();
        System.out.println("");
        System.out.println("Partie terminée !");
        System.out.println("Score : " + score + ", Score maximal : " + user_max_size);
    }

    public static boolean sendToDb(String mdp) {
        RequeteKahoot req = new RequeteKahoot(mdp);
        DatabaseSender sender = new DatabaseSender(req);

        boolean cat = sender.sendCategories();
        boolean ques = sender.sendQuestion();

        if(cat)
            System.out.println("Les catégories ont toutes été ajoutées !");

        if(ques)
            System.out.println("Les questions ont toutes été ajoutées !");

        return (cat && ques);
    }
}
