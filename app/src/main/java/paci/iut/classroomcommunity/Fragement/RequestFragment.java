package paci.iut.classroomcommunity.Fragement;

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

import java.util.ArrayList;
import java.util.List;

import paci.iut.classroomcommunity.Activity.QuizzActivity;
import paci.iut.classroomcommunity.R;
import paci.iut.classroomcommunity.modele.Friend;
import paci.iut.classroomcommunity.modele.interfaces.OnclickRequestListener;
import paci.iut.classroomcommunity.modele.Request;
import paci.iut.classroomcommunity.modele.adapters.RequestAdapter;


public class RequestFragment extends Fragment {

    ListView listRequestListView;
    List<Request> listeRequest;
    private RequestAdapter requestadp;
    public static final String TAG = "TAG_RequestFragment";
    SwipeRefreshLayout swipe;

    public RequestFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_request, container, false);

        getActivity().setTitle("Mes Demandes");

        listRequestListView = (ListView) view.findViewById(R.id.liste_request);

        //Refresh
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_request);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRequestList();
                swipe.setRefreshing(false);
            }
        });

        getRequestList();

        return view;
    }

    private void getRequestList() {
        final String url  = "http://192.168.43.144/siteWEB_classroom_renommerScript/getMatchs.php";
        Log.i(TAG , "Les requests : "+url);

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
                            List<Request> listeRequests = Request.getListOfRequestFromJson(response.getResult());
                            buildRequestList(listeRequests);
                            Toast.makeText(getContext(),"Chargement Terminé",Toast.LENGTH_SHORT).show();
                            break;
                        case 204:
                            buildRequestList( new ArrayList<Request>());
                            Toast.makeText(getContext(),"Aucune demeande",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Log.e(TAG , "Erreur : "+String.valueOf(response.getHeaders().code()));
                            break;
                    }

                }else{
                    Toast.makeText(getContext(), "Erreur pas de reponse getRequest ",Toast.LENGTH_SHORT).show();
                }
                }//end onCompleted

            });


    }

    private void buildRequestList(final List<Request> listeRequest){
        OnclickRequestListener onclickRequestListener = new OnclickRequestListener() {
            @Override
            public void onAccept(final Request request) {
                Log.i(TAG , "Accept : "+request.toString());
                Toast.makeText(getContext() , "Start du match",Toast.LENGTH_SHORT).show();

                String url="http://192.168.43.144/siteWEB_classroom_renommerScript/reponseMatch.php?idmatch="+request.getId()+"&type=1";
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
                                            Toast.makeText(getContext() ,"Le match a bien été accepté" , Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getActivity() , QuizzActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("friend_adv",Friend.SearchFriend(request.getIdAkser()));
                                            bundle.putBoolean("isAsker",false);
                                            bundle.putInt("idMatch",request.getId());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            break;
                                        default:
                                            Toast.makeText(getActivity(), "Erreur : "+response.getResult(),Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }else{
                                    Log.e(TAG , "no response");
                                }

                            }
                        });
            }

            @Override
            public void onRefuse(Request request) {
                Log.i(TAG , "Refuse  : "+request.toString());
                Toast.makeText(getContext() , "Match refusé",Toast.LENGTH_SHORT).show();
                String url="http://192.168.43.144/siteWEB_classroom_renommerScript/reponseMatch.php?idmatch="+request.getId()+"&type=2";
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
                                            Toast.makeText(getContext() ,"Le match a bien été refusé" , Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(getActivity(), "Erreur : "+response.getResult(),Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }else{
                                    Log.e(TAG , "no response");
                                }

                            }
                        });
            }
        };

        requestadp = new RequestAdapter(getActivity() , R.layout.item_request , listeRequest ,onclickRequestListener);
        listRequestListView.setAdapter(requestadp);



    }

}
