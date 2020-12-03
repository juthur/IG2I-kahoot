package utils;

public class Option implements InformationGenerale {
    
    //region Attributs
    private int noOption;
    private String texteOption;
    public static int nbOptions;
    //endregion

    //region Getters et Setters
    public int getNoOption() {
        return noOption;
    }

    public void setNoOption(int noOption) {
        this.noOption = noOption;
    }

    public String getTexteOption() {
        return texteOption;
    }

    public void setTexteOption(String texteOption) {
        this.texteOption = texteOption;
    }
    //endregion

    //region Constructor et Destructor
    public Option(int noOption, String texteOption) {
        this.noOption = noOption;
        this.texteOption = texteOption;

        ++Option.nbOptions;
    }

    @Override
    public void finalize() {
        --Option.nbOptions;
    }
    //endregion

    //region Methods
    @Override
    public String toString() {
        return "[" + noOption + ":" + texteOption + "]";
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Option) {
            Option opt = (Option) o;
            return opt.texteOption.equals(this.texteOption);
        }
        return false;
    }

    public boolean estUneQuestion() {
        return texteOption.contains("?");
    }
    //endregion
}
