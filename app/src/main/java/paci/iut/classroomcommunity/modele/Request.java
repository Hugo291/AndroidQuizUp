package paci.iut.classroomcommunity.modele;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;


public class Request implements Serializable{

    @SerializedName("idmatch")
    int id;

    int idFriend;
    @SerializedName("idAsker")
    int idAkser;

    @SerializedName("accept")
    int accept;

    int score1;
    int score2;

    public static Request getResquestFromJson(String json){
        Gson gson = new Gson();
        Request request = gson.fromJson(json, Request.class);
        return request;
    }

    public static List<Request> getListOfRequestFromJson(String json){
        //Creation des objets
        Gson gson = new Gson();
        Type type = new TypeToken<List<Request>>(){}.getType();
        //Conversion
        List<Request> requests = gson.fromJson(json, type);
        return requests;
    }

    public Request() {
    }

    public Request(int id, int idFriend, int idAkser) {
        this.id = id;
        this.idFriend = idFriend;
        this.idAkser = idAkser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFriend() {
        return idFriend;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", idFriend=" + idFriend +
                ", idAkser=" + idAkser +" , accept="+accept+
                '}';
    }

    public void setIdFriend(int idFriend) {
        this.idFriend = idFriend;
    }

    public int getIdAkser() {
        return idAkser;
    }

    public void setIdAkser(int idAkser) {
        this.idAkser = idAkser;
    }
}
