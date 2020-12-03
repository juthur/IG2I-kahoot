package utils;

public class Joueur {
    //region Attributs
    private int id;
    private String login;
    private String mdp;
    //endregion

    public Joueur(int id, String login, String mdp) {
        this.id = id;
        this.login = login;
        this.mdp = mdp;
    }

    //region Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
    //endregion

    //region Methods
    @Override
    public String toString() {
        return login;
    }
    //endregion
}
