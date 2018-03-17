package paci.iut.classroomcommunity.modele.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

import paci.iut.classroomcommunity.R;
import paci.iut.classroomcommunity.modele.Friend;

public class FriendAdapter extends ArrayAdapter<Friend> {

    private static final String TAG = "TAG_FriendAdapter";
    List<Friend> items;
    Context context;
    int ressource;

    public FriendAdapter(@NonNull Context context, int resource, @NonNull List<Friend> objects) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
        this.ressource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View layout = convertView;

        //on test si la vue existe
        if (convertView == null){

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            layout = inflater.inflate(this.ressource, parent, false);
        }

        //on recupère les champs
        TextView nameTV = (TextView) layout.findViewById(R.id.name);
        TextView connect = (TextView) layout.findViewById(R.id.connect);

//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(items.get(position).getInitials(), items.get(position).getRandomColor());

        ImageView image = (ImageView) layout.findViewById(R.id.profil);
        image.setImageDrawable(items.get(position).getDrawableRandom());

        if(items.get(position).getPresent() ) {
            layout.setAlpha(1);
        }else{
            layout.setAlpha(0.2f);
        }
        //on assign les valeurs du nom du friend actuel
        nameTV.setText(items.get(position).getNomComplet() );
        //on met image du friend actuel

        return layout;
    }



}
