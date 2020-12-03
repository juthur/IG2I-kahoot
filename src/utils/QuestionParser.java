package utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionParser {
    public static List<Question> getAllQuestions() {
        File f = new File("./src/resources/");
        List<Question> questionList = new ArrayList<>();

        for(File tmp : f.listFiles()) {
            questionList.addAll(QuestionParser.getFileQuestions(tmp));
        }
        return questionList;
    }

    public static List<Question> getFileQuestions(File tmp) {
        List<Question> questionList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(tmp));
            JSONObject quizz = (JSONObject) jsonObject.get("quizz");
            quizz = (JSONObject) quizz.get("fr");

            JSONArray debutant = (JSONArray) quizz.get("débutant");
            for(Object jO : debutant) {
                JSONObject question = (JSONObject) jO;
                Reponse bonneReponse = new Reponse(0, (String) question.get("réponse"));

                JSONArray propositions = (JSONArray) question.get("propositions");
                Reponse[] lesPropositions = new Reponse[4];
                lesPropositions[0] = new Reponse(0, (String) propositions.get(0));
                lesPropositions[1] = new Reponse(1, (String) propositions.get(1));
                lesPropositions[2] = new Reponse(2, (String) propositions.get(2));
                lesPropositions[3] = new Reponse(3, (String) propositions.get(3));


                String theme = (String) jsonObject.get("thème");
                theme = theme.substring(0, theme.indexOf("(")-1);

                Question q = new Question(Integer.parseInt(question.get("id").toString()), (String) question.get("question"), theme, lesPropositions, bonneReponse);
                questionList.add(q);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return questionList;
    }

    public static Map<String, String> getThemes() {
        File f = new File("./src/resources/");
        Map<String, String> themes = new HashMap<>();

        for(File tmp : f.listFiles()) {
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(tmp));

                String theme = (String) jsonObject.get("thème");
                theme = theme.substring(0, theme.indexOf("(")-1);

                themes.put(themes.size()+". "+theme, tmp.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return themes;
    }
}
