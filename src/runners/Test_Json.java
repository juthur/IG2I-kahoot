package runners;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.Question;
import utils.QuestionParser;
import utils.Reponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Test_Json {
    public static void main(String[] args) {
        for(Question q : QuestionParser.getAllQuestions())
            System.out.println(q);
    }
}
