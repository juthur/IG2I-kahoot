package utils;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequeteKahoot {
    //region Attributs
    private Connection con;
    private String url = "jdbc:mysql://localhost:3306/poo2020";
    private String user = "root";
    private String mdp;
    //endregion

    //region Getters & Setters
    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
    //endregion

    public RequeteKahoot(String mdp) {
        this.mdp = mdp;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, mdp);
        } catch (SQLException throwables) {
            throwables.getStackTrace();
        } catch (ClassNotFoundException e) {
            e.getStackTrace();
        }
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // SELECT
    public ResultSet executeQuery(String query) {
        ResultSet res = null;
        try {
            PreparedStatement ps = con.prepareStatement(query);
            res = ps.executeQuery();
        } catch (SQLException throwables) {
            System.out.println("La requête SQL n'a pas fonctionnée");
        }

        return res;
    }

    // SELECT
    public ResultSet executeQuery(String query, List<? extends Object> args) {
        ResultSet res = null;
        try {
            PreparedStatement ps = con.prepareStatement(query);

            if(args != null) {
                for (int i = 1; i <= args.size(); ++i) {
                    ps.setObject(i, args.get(i-1));
                }
            }

            res = ps.executeQuery();
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        }
        return res;
    }

    // UPDATE
    public int updateQuery(String query, List<? extends Object> args) {
        int res = -1;

        try {
            PreparedStatement ps = con.prepareStatement(query);
            if (args != null)
                for (int i = 1; i <= args.size(); ++i)
                    ps.setObject(i, args.get(i - 1));

            res = ps.executeUpdate();
        } catch(MySQLIntegrityConstraintViolationException e) {
            System.out.println("[SQL] Il existe déjà une entrée avec ces valeurs dans la table");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    public ResultSet updateQuery(String query, List<? extends Object> args, int statementReturn) {
        ResultSet res = null;

        try {
            PreparedStatement ps = con.prepareStatement(query, statementReturn);
            if (args != null)
                for (int i = 1; i <= args.size(); ++i)
                    ps.setObject(i, args.get(i - 1));

            ps.executeUpdate();
            res = ps.getGeneratedKeys();
        } catch(MySQLIntegrityConstraintViolationException e) {
            System.out.println("[SQL] Il existe déjà une entrée avec ces valeurs dans la table");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    public List<Categorie> getListeCategories() {
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`categorie`;");

        List<Categorie> res = new ArrayList<>();
        try {
            while (result.next()) {
                res.add(new Categorie(result.getInt("idCATEGORIE"), result.getString("texteCATEGORIE")));
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return res;
    }

    public int getNbJoueurs() {
        ResultSet result = executeQuery("SELECT count(idCATEGORIE) AS count FROM `poo2020`.`categorie`;");
        int res = -1;
        try {
            result.next();
            res = result.getInt("count");
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        }
        return res;
    }

    public List<Joueur> getListeJoueurs() {
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`joueur`;");

        List<Joueur> res = new ArrayList<>();
        try {
            while (result.next()) {
                res.add(new Joueur(result.getInt("idJOUEUR"), result.getString("login"), result.getString("mdp")));
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return res;
    }

    public Joueur getJoueurById(int id) {
        List<Integer> args = new ArrayList<>();
        args.add(id);
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`joueur` WHERE `idJOUEUR`=?;", args);

        Joueur joueur = null;

        try {
            if(result != null) {
                result.next();
                if(!result.isAfterLast()) {
                    joueur = new Joueur(result.getInt("idJOUEUR"), result.getString("login"), result.getString("mdp"));
                }
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        }
        return joueur;
    }

    public Joueur getJoueurByLogin(String login) {
        List<String> args = new ArrayList<>();
        args.add(login);
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`joueur` WHERE `login`=?;", args);

        Joueur joueur = null;

        try {
            if(result != null) {
                result.next();

                joueur = new Joueur(result.getInt("idJOUEUR"), result.getString("login"), result.getString("mdp"));
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return joueur;
    }

    public Joueur getJoueur(String name, String mdp) {
        List<String> args = new ArrayList<>();
        args.add(name);
        args.add(mdp);
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`joueur` WHERE `login`=? AND `mdp`=?;", args);

        Joueur joueur = null;

        try {
            if(result != null) {
                result.next();

                joueur = new Joueur(result.getInt("idJOUEUR"), result.getString("login"), result.getString("mdp"));
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return joueur;
    }

    public int addJoueur(Joueur j) {
        List<Object> args = new ArrayList<>();
        args.add(j.getLogin());
        args.add(j.getMdp());

        ResultSet result = updateQuery("INSERT INTO `poo2020`.`joueur`(login, mdp) VALUES(?,?);", args, Statement.RETURN_GENERATED_KEYS);
        int id = -1;
        try {
            if(result != null) {
                result.next();
                id = result.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return id;
    }

    public int addCategorie(Categorie c) {
        List<Object> args = new ArrayList<>();
        args.add(c.getTexte());

        ResultSet result = updateQuery("INSERT INTO `poo2020`.`categorie`(texteCATEGORIE) VALUES(?);", args, Statement.RETURN_GENERATED_KEYS);
        int id = -1;
        try {
            if(result != null) {
                result.next();
                id = result.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return id;
    }

    public Categorie getCategorieById(int id) {
        List<Integer> args = new ArrayList<>();
        args.add(id);
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`categorie` WHERE `idCATEGORIE`=?;", args);

        Categorie categorie = null;

        try {
            if(result != null) {
                result.next();
                if(!result.isAfterLast()) {
                    categorie = new Categorie(result.getInt("idCATEGORIE"), result.getString("texteCATEGORIE"));
                }
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        }
        return categorie;
    }

    public Categorie getCategorieByText(String cat) {
        List<String> args = new ArrayList<>();
        args.add(cat);
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`categorie` WHERE `texteCATEGORIE`=?;", args);

        Categorie categorie = null;

        try {
            if(result != null) {
                result.next();

                categorie = new Categorie(result.getInt("idCATEGORIE"), result.getString("texteCATEGORIE"));
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return categorie;
    }

    public int addQuestion(Question q) {
        Reponse rep = getReponseByText(q.getBonneReponse().getTexteOption());
        Categorie cat = getCategorieByText(q.getCategorie());

        if(rep == null || cat == null)
            return -1;

        List<Object> args = new ArrayList<>();
        args.add(rep.getNoOption());
        args.add(cat.getId());
        args.add(q.getTexteOption());

        ResultSet result = updateQuery("INSERT INTO `poo2020`.`question`(ID_BONNE_REPONSE, ID_CATEGORIE, texteQUESTION) VALUES(?,?,?);", args, Statement.RETURN_GENERATED_KEYS);
        int id = -1;
        try {
            if(result != null) {
                result.next();
                id = result.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return id;
    }

    public Question getQuestionById(int id) {
        List<Integer> args = new ArrayList<>();
        args.add(id);
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`question` WHERE `ID_QUESTION`=?;", args);

        Question question = null;

        try {
            if(result != null) {
                result.next();
                if(!result.isAfterLast()) {
                    List<Reponse> reps = getPropositionsByQuestionId(result.getInt("ID_QUESTION"));
                    Reponse[] propositions = new Reponse[reps.size()];
                    for(int i = 0; i < propositions.length; ++i) {
                        propositions[i] = reps.get(i);
                    }

                    question = new Question(result.getInt("ID_QUESTION"), result.getString("texteQUESTION"), getCategorieById(result.getInt("ID_CATEGORIE")).getTexte(), propositions, getReponseById(result.getInt("ID_BONNE_REPONSE")));
                }
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        }
        return question;
    }

    public Question getQuestionByText(String rep) {
        List<String> args = new ArrayList<>();
        args.add(rep);
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`question` WHERE `texteQUESTION`=?;", args);

        Question question = null;

        try {
            if(result != null) {
                result.next();

                List<Reponse> reps = getPropositionsByQuestionId(result.getInt("ID_QUESTION"));
                Reponse[] propositions = new Reponse[reps.size()];
                reps.toArray(propositions);

                question = new Question(result.getInt("ID_QUESTION"), result.getString("texteQUESTION"), getCategorieById(result.getInt("ID_CATEGORIE")).getTexte(), propositions, getReponseById(result.getInt("ID_BONNE_REPONSE")));
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return question;
    }

    public int addReponse(Reponse r) {
        List<Object> args = new ArrayList<>();
        args.add(r.getTexteOption());

        ResultSet result = updateQuery("INSERT INTO `poo2020`.`reponse`(texteREPONSE) VALUES(?);", args, Statement.RETURN_GENERATED_KEYS);
        int id = -1;
        try {
            if(result != null) {
                result.next();
                id = result.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return id;
    }

    public Reponse getReponseById(int id) {
        List<Integer> args = new ArrayList<>();
        args.add(id);
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`reponse` WHERE `ID_REPONSE`=?;", args);

        Reponse reponse = null;

        try {
            if(result != null) {
                result.next();
                if(!result.isAfterLast()) {
                    reponse = new Reponse(result.getInt("ID_REPONSE"), result.getString("texteREPONSE"));
                }
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        }
        return reponse;
    }

    public Reponse getReponseByText(String rep) {
        List<String> args = new ArrayList<>();
        args.add(rep);
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`reponse` WHERE `texteREPONSE`=?;", args);

        Reponse reponse = null;

        try {
            boolean next = result.next();
            if(result != null && next) {
                reponse = new Reponse(result.getInt("ID_REPONSE"), result.getString("texteREPONSE"));
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return reponse;
    }

    public int addPropositions(int idQuestion, int idReponse, boolean isBonneReponse) {
        List<Object> args = new ArrayList<>();
        args.add(idQuestion);
        args.add(idReponse);
        args.add(isBonneReponse ? 1 : 0);

        ResultSet result = updateQuery("INSERT INTO `poo2020`.`propositions`(ID_QUESTION, ID_REPONSE, bonneReponse) VALUES(?,?,?);", args, Statement.RETURN_GENERATED_KEYS);
        int id = -1;
        try {
            if(result != null) {
                result.next();
                if(!result.isAfterLast()) {
                    id = result.getInt(1);
                }
            }
        } catch (SQLException throwables) {
            // ça gueule à cause du ResultSet mais c'est chelou
            //throwables.printStackTrace();
        } finally {
            try {
                if(result != null)
                    result.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return id;
    }

    public List<Reponse> getPropositionsByQuestionId(int id) {
        List<Integer> args = new ArrayList<>();
        args.add(id);
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`propositions` WHERE `ID_QUESTION`=?;", args);

        List <Reponse> reponses = new ArrayList<>();

        try {
            if(result != null) {
                while(result.next()) {
                    int idRep = result.getInt("ID_REPONSE");
                    reponses.add(getReponseById(idRep));
                }
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        }
        return reponses;
    }

    public List<Question> getAllQuestions() {
        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`question`;");

        List<Question> questions = new ArrayList<>();

        try {
            if(result != null) {

                while(result.next()) {
                    List<Reponse> reps = getPropositionsByQuestionId(result.getInt("ID_QUESTION"));
                    Reponse[] propositions = new Reponse[reps.size()];
                    for(int i = 0; i < propositions.length; ++i) {
                        propositions[i] = reps.get(i);
                    }

                    Question question = new Question(result.getInt("ID_QUESTION"), result.getString("texteQUESTION"), getCategorieById(result.getInt("ID_CATEGORIE")).getTexte(), propositions, getReponseById(result.getInt("ID_BONNE_REPONSE")));
                    questions.add(question);
                }
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        }
        return questions;
    }

    public List<Question> getAllQuestions(int cat) {
        List<Integer> args = new ArrayList<>();
        args.add(cat);

        ResultSet result = executeQuery("SELECT * FROM `poo2020`.`question` WHERE `ID_CATEGORIE`=?;", args);

        List<Question> questions = new ArrayList<>();

        try {
            if(result != null) {
                while(result.next()) {
                    List<Reponse> reps = getPropositionsByQuestionId(result.getInt("ID_QUESTION"));
                    Reponse[] propositions = new Reponse[reps.size()];
                    for(int i = 0; i < propositions.length; ++i) {
                        propositions[i] = reps.get(i);
                    }

                    Question question = new Question(result.getInt("ID_QUESTION"), result.getString("texteQUESTION"), getCategorieById(result.getInt("ID_CATEGORIE")).getTexte(), propositions, getReponseById(result.getInt("ID_BONNE_REPONSE")));
                    questions.add(question);
                }
            }
        } catch (SQLException throwables) {
            System.out.println("[SQL] La requête SQL n'a pas fonctionnée");
        }
        return questions;
    }
}
