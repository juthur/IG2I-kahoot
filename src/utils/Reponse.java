package utils;

public class Reponse extends Option {
    //region Constructor
    public Reponse(int noOption, String texteOption) {
        super(noOption, texteOption);
    }
    //endregion

    //region Methods
    @Override
    public String toString() {
        return super.getTexteOption();
    }
    //endregion
}