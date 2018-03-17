package paci.iut.classroomcommunity;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

class Answer implements Serializable{
    int id;
    String text;
    @SerializedName("is_right")
    int isRight;

    public Answer(int id, String text, int isRight) {
        this.id = id;
        this.text = text;
        this.isRight = isRight;
    }

    public Answer() {
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

    public boolean isRight() {
        if(isRight == 0)
            return false;
        else
            return true;
    }



    public void setRight(int right) {
        isRight = right;
    }


    public static Answer getAnswerFromJson(String json){
        Gson gson = new Gson();
        Answer answer = gson.fromJson(json, Answer.class);
        return answer;
    }

    public static List<Answer> getListOfAnswersFromJson(String json){
        //Creation des objets
        Gson gson = new Gson();
        Type type = new TypeToken<List<Answer>>(){}.getType();
        //Conversion
        List<Answer> answers = gson.fromJson(json, type);
        return answers;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", isRight=" + isRight() +
                '}';
    }
}
