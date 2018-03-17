package paci.iut.classroomcommunity.modele.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import java.util.List;

import paci.iut.classroomcommunity.Activity.MainActivity;
import paci.iut.classroomcommunity.R;
import paci.iut.classroomcommunity.modele.Friend;
import paci.iut.classroomcommunity.modele.Request;
import paci.iut.classroomcommunity.modele.interfaces.OnclickRequestListener;


public class RequestAdapter extends ArrayAdapter<Request> {

    private static final String TAG = "TAG_RequestAdapter";
    private Activity activity;
    private List<Request> items;
    private int itemResourceId;
    private OnclickRequestListener onclickRequestListener;


    public RequestAdapter(@NonNull Activity activity, int resource, @NonNull List<Request> objects ,OnclickRequestListener onclickRequestListener) {
        super(activity, resource, objects);
        this.activity = activity;
        this.items = objects;
        this.itemResourceId = resource;
        this.onclickRequestListener = onclickRequestListener;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =  convertView;

        if (convertView == null){

            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(this.itemResourceId, parent, false);
        }

        TextView tvNameMe = view.findViewById(R.id.friend_asker);

        //Log.i(TAG , "ASKER : "+items.get(position).getIdAkser());

        int idAsker = items.get(position).getIdAkser();
        //Log.i(TAG , Friend.SearchFriend(id).toString());
        tvNameMe.setText(""+Friend.SearchFriend(idAsker).getNomComplet());

        int idFriend = items.get(position).getIdFriend();

        TextView tvNameAdv = view.findViewById(R.id.friend_target);
        tvNameAdv.setText(""+Friend.SearchFriend(idFriend).getNomComplet());

        Button accept = view.findViewById(R.id.btn_positive);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           onclickRequestListener.onAccept(items.get(position));

            }
        });

        Button refuser = view.findViewById(R.id.btn_negative);
        refuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onclickRequestListener.onRefuse(items.get(position));
            }
        });

        return view;
    }

//    private void ReponseDemande(int type ,int idMatch){
//        String file="http://192.168.43.144/siteWEB_classroom_renommerScript/reponseMatch.php?idmatch="+idMatch+"&type="+type;
//        Log.i(TAG,file);
//        Ion.with(activity)
//                .load(file)
//                .asString()
//                .withResponse()
//                .setCallback(new FutureCallback<Response<String>>() {
//                    @Override
//                    public void onCompleted(Exception e, Response<String> response) {
//                        if(response != null){
//                            switch (response.getHeaders().code()){
//                                case 200:
//                                    //En cas de reussite
//                                    Toast.makeText(activity , "Le match va commencer",Toast.LENGTH_SHORT).show();
//                                    Log.i(TAG,"Le match va commencer");
//                                    break;
//                                default:
//                                    //Autre code http
//                                    Log.i(TAG,response.getResult()+response.getHeaders().code());
//                                    Toast.makeText(activity , response.getHeaders().code(),Toast.LENGTH_SHORT).show();
//                                    break;
//                            }
//                        }else{
//                            Toast.makeText(getContext(), "Erreur no response",Toast.LENGTH_SHORT).show();
//                            Log.e(TAG , "no response");
//                        }
//                    }
//                });
//    }



}
