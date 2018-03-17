package paci.iut.classroomcommunity.modele;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class Question implements Serializable {
    int id;
    String text;
    int duration;
    List<Answer> answers;

    public Question() {
    }

    public Question(int id, String text, int duration, List<Answer> answers) {
        this.id = id;
        this.text = text;
        this.duration = duration;
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }


    public static Question getQuestionFromJson(String json){
        Gson gson = new Gson();
        Question question = gson.fromJson(json, Question.class);
        return question;
    }

    public static List<Question> getListOfQuestionFromJson(String json) {
        //Creation des objets
        Gson gson = new Gson();
        Type type = new TypeToken<List<Question>>() {
        }.getType();
        //Conversion
        List<Question> questions = gson.fromJson(json, type);
        return questions;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", duration=" + duration +
                ", answers=" + answers.get(0).toString() +
                '}';
    }
}
