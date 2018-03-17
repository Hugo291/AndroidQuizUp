package paci.iut.classroomcommunity.Fragement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONObject;

import java.util.List;

import paci.iut.classroomcommunity.Activity.MainActivity;
import paci.iut.classroomcommunity.Activity.QuizzActivity;
import paci.iut.classroomcommunity.R;
import paci.iut.classroomcommunity.modele.Friend;
import paci.iut.classroomcommunity.modele.adapters.FriendAdapter;
import paci.iut.classroomcommunity.modele.interfaces.UserRequestResponseListener;

import static paci.iut.classroomcommunity.modele.interfaces.UserRequestResponseListener.*;

public class FriendFragment extends Fragment {

    private SwipeRefreshLayout swipe;
    private static final String TAG = "TAG_FriendFragment";
    private ListView listview;
    private List<Friend> listFriend;

    private ProgressDialog pDialogFriendList;
    private ProgressDialog pDialogRequest;
    private TimerConnection tc;


    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //On create view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //On met le titre a l'activité
        getActivity().setTitle("Mes amis");
        View view =  inflater.inflate(R.layout.fragment_friend, container, false);
        listview = view.findViewById(R.id.lv);
        swipe = view.findViewById(R.id.swipe);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFriendsList();
                swipe.setRefreshing(false);
            }
        });

        getFriendsList();

        return view;
    }

    //Recuperation des amies
    private void getFriendsList() {

        pDialogFriendList = new ProgressDialog(getActivity());
        pDialogFriendList.setMessage("Getting list of friends...");
        pDialogFriendList.setIndeterminate(false);
        pDialogFriendList.setCancelable(true);
        pDialogFriendList.show();

        final String urlString ="http://192.168.43.144/siteWEB_classroom_renommerScript/getFriends.php?key="+ MainActivity.getCodeQr()+"&out="+MainActivity.getMe().getId();
        Log.i(TAG,urlString);
        Ion.with(getActivity())
                .load(urlString)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {

                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        if(response != null){
                            switch (response.getHeaders().code()){
                                case 200:

                                    //Log.i(TAG , "Succes getFriendsList: 200");
                                    Toast.makeText(getActivity(), "Succés getFriendsList",Toast.LENGTH_SHORT).show();
                                    //Log.i(TAG, "Http code: " + response.getHeaders().code());
                                    // Log.i(TAG, "Result getFriendsList: " + response.getResult());
                                    setListFriend(  Friend.getListOfFriendsFromJson(response.getResult()) );
                                    buildListFriend();

                                    break;
                                default:
                                    //Log.e(TAG , "Erreur getFriendsList "+response.getHeaders().code());
                                    // Log.e(TAG , "Test this url : "+urlString);
                                    Toast.makeText(getActivity(), "Erreur getFriendsList : "+response.getResult(),Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        }else{
                            Toast.makeText(getContext(), "Erreur pas de reponse getFriendsList",Toast.LENGTH_SHORT).show();
                        }
                        pDialogFriendList.dismiss();

                    }

                });
    }

    //Construction de la liste avec listener
    private void buildListFriend(){

        FriendAdapter adapter = new FriendAdapter( getActivity(), R.layout.item_friend , listFriend );
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if( listFriend.get(i).getPresent()){

                    initProgress(listFriend.get(i));

                    sendRequest(
                        MainActivity.getIdPlayer() ,
                        listFriend.get(i) ,
                        MainActivity.getCodeQr()
                    );

                }else{
                    Log.e(TAG,"L'amis doit etre connecté");
                }

            }
        });
    }

    //Demande de match du joueur
    private void sendRequest(int myId , final Friend friendAdv , String key ){

        String url = "http://192.168.43.144/siteWEB_classroom_renommerScript/AskMatch.php?key="+key+"&id="+myId+"&idFriend="+friendAdv.getId();
        Log.i(TAG, "Url sendAsk : "+url);

        Ion.with(getActivity())
                .load(url)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {

                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        if(response != null){
                            switch (response.getHeaders().code()){
                                case 200:
                                    //Log.i(TAG , "Succes setask: 200");
                                    Toast.makeText(getActivity(), " Demande en cours ",Toast.LENGTH_SHORT).show();

                                    final int idMatch  = getIdMatch(response.getResult());

                                    //Log.i(TAG , "Match"+idMatch);
                                    tc = new TimerConnection(getActivity() , idMatch);

                                    tc.setListener(new UserRequestResponseListener() {

                                        @Override
                                        public void onSucces() {
                                            Toast.makeText(getContext() ,"Le client a accepter",Toast.LENGTH_SHORT ).show();
                                            openMatch(friendAdv , idMatch);
                                        }

                                        @Override
                                        public void onRefuse() {
                                            Toast.makeText(getContext() ,"Le client a refuser",Toast.LENGTH_SHORT ).show();

                                        }

                                        @Override
                                        public void onFinish() {
                                            try {
                                                pDialogRequest.dismiss();
                                            }catch (Exception e){
                                                Log.i(TAG , "Exception pDialogRequest : "+e.getMessage());
                                            }
                                        }
                                    });

                                    tc.start();
                                    break;
                                default:
                                    //Log.e(TAG , "Erreur setAsk : "+response.getHeaders().code()+"resultat : " +response.getResult());
                                    Toast.makeText(getActivity(), "Erreur Demande impossible : "+response.getResult(),Toast.LENGTH_SHORT).show();
                                    pDialogRequest.dismiss();
                                    break;
                            }
                        }else{
                            Toast.makeText(getContext(), "Erreur pas de reponse getFriendsList",Toast.LENGTH_SHORT).show();
                        }


                    }



                });
    }

    //getter le match id
    private static int getIdMatch(String result) {
        try {
            JSONObject js = new JSONObject(result);
            return js.getInt("idMatch");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //intialisation du progresse
    private void initProgress(Friend friend){
        pDialogRequest = new ProgressDialog(getActivity());
        pDialogRequest.setTitle("Demande en cour");
        pDialogRequest.setMessage("En attente de la reponse de "+friend.getNomComplet());
        //Si on autorise le cancel ou pas
        pDialogRequest.setCancelable(false);
        pDialogRequest.show();
    }

    //Ouverture duu match et transmittion des données
    public void openMatch(Friend friend , int idMatch){

        //Intent
        Intent intent = new Intent(getActivity(),QuizzActivity.class);
        //Bundle
        Bundle bundle = new Bundle();
        //Transmission de l'ami
        bundle.putSerializable("friend_adv",friend);
        //Transmission de l'asker
        bundle.putBoolean("isAsker",false);
        //id du match
        bundle.putInt("idMatch",idMatch);
        //Insertion de l'extra
        intent.putExtras(bundle);
        //Start de l'activité QuizActivity
        startActivity(intent);
    }

    //Set le la liste d'amis
    public void setListFriend(List<Friend> listFriend) {
        this.listFriend = listFriend;
        MainActivity.setListFriend(listFriend);
    }
}
