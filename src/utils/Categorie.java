package utils;

public class Categorie implements Comparable<Categorie> {
    //region Attributs
    private int id;
    private String texte;
    //endregion

    public Categorie(int id, String texte) {
        this.id = id;
        this.texte = texte;
    }

    //region Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }
    //endregion

    //region Methods
    @Override
    public String toString() {
        return texte;
    }

    @Override
    public int compareTo(Categorie o) {
        return id - o.getId();
    }
    //endregion
}
