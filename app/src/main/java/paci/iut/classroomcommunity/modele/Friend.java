package paci.iut.classroomcommunity.modele;

import android.graphics.Color;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import paci.iut.classroomcommunity.Activity.MainActivity;

public class Friend  implements Serializable{

    @SerializedName("is_present")
    private int present = 0;

    @SerializedName("id")
    private int id;

    int color;

    @SerializedName("first_name")
    private String firstname;

    @SerializedName("last_name")
    private String lastname;

    public boolean getPresent() {
        if(present == 1)
            return true;
        return false;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public String getNomComplet(){
        return this.getFirstname()+" "+this.getLastname();
    }

    public Friend(int id , String  firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getInitials(){
        return firstname.charAt(0)+""+lastname.charAt(0);
    }

    private int getRandomColor(){

        if(color == 0){
            Random random = new Random();
            int min = 100;
            int max =180;
            int red = random.nextInt(max + 1 - min) + min;
            int green = random.nextInt(max + 1 - min) + min;
            int blue = random.nextInt(max + 1 - min) + min;
            color = Color.rgb(red,green,blue);
        }
        return color;

    }

    @Override
    public String toString() {
        return this.firstname()+" | "+this.id+" | "+this.present;
    }

    private String firstname() {
        return firstname;
    }

    public static Friend getFriendFromJson(String json){
        Gson gson = new Gson();
        Friend friend = gson.fromJson(json, Friend.class);
        return friend;
    }

    public static List<Friend> getListOfFriendsFromJson(String json){
        //Creation des objets
        Gson gson = new Gson();
        Type type = new TypeToken<List<Friend>>(){}.getType();
        //Conversion
        List<Friend> friends = gson.fromJson(json, type);
        return friends;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public TextDrawable getDrawableRandom(){
        return TextDrawable.builder().buildRound(this.getInitials(), this.getRandomColor());
    }

    public static Friend SearchFriend(int id){
        List<Friend>  list = MainActivity.getListFriend();

        for(int i =0;i<list.size();i++){
            if(list.get(i).getId() == id){
                return list.get(i);
            }
        }
        return MainActivity.getMe();

    }

}
