package utils;

public class Question extends Option implements Comparable<Question> {

    //region Attributs
    private String categorie;
    private Reponse[] lesPropositions;
    private Reponse bonneReponse;
    //endregion

    //region Getters et Setters

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Reponse[] getLesPropositions() {
        return lesPropositions;
    }

    public void setLesPropositions(Reponse[] lesPropositions) {
        this.lesPropositions = lesPropositions;
    }

    public Reponse getBonneReponse() {
        return bonneReponse;
    }

    public void setBonneReponse(Reponse bonneReponse) {
        this.bonneReponse = bonneReponse;
    }

    //endregion

    //region Constructor
    public Question(int noOption, String texteOption, String categorie, Reponse[] lesPropositions, Reponse bonneReponse) {
        super(noOption, texteOption);

        this.categorie = categorie;
        this.lesPropositions = lesPropositions;
        this.bonneReponse = bonneReponse;
    }
    //endregion

    //region Methods
    @Override
    public String toString() {
        String res = "[" + categorie + "] " + super.getTexteOption() + "\n";
        for(int i = 0; i < lesPropositions.length; ++i)
            res += "\t" + i + ". " + lesPropositions[i].toString() + "\n";

        return res;
    }

    @Override
    public int compareTo(Question o) {
        return getNoOption() - o.getNoOption();
    }
    //endregion
}
