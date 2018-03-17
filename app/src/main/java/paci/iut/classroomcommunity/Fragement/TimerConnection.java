package paci.iut.classroomcommunity.Fragement;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONObject;

import paci.iut.classroomcommunity.modele.interfaces.UserRequestResponseListener;


public class TimerConnection extends Thread {


    private static final String TAG = "TAG_TimerConnection";

    //En cours de process
    //false fin
    //true en cours
    private boolean running  = true;

    //Si le code en deja en execution
    private boolean isExecute = false;

    //L'id de match
    private int idMatch;
    private Activity activity;

    //le compteur
    private int count = 0;
    private int max = 3;

    UserRequestResponseListener tl;

    public TimerConnection(Activity activity , int idMatch ){
        this.activity = activity;
        this.idMatch =idMatch;
    }

    public void setListener(UserRequestResponseListener tl){
        this.tl = tl;

    }

    @Override
    public void run() {

        super.run();
        while(running){
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!running)
                break;

            if(!isExecute){
                getResponseMatch();
            }
            if(count>=max){
                sendNoResponse();
                Log.i(TAG , "Break the timer");
                running = false;
                break;
            }
            count++;

        }
        tl.onFinish();
        Log.i(TAG ,"End of the thread Timer");
    }

    public void close(){
        running = false;
    }

    private void getResponseMatch(){
        setExecute( true) ;
        final String url ="http://192.168.43.144/siteWEB_classroom_renommerScript/checkMatch.php?id="+idMatch;
        Log.i(TAG , "url Response Match "+url);
        Ion.with(activity)
                .load(url)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> response) {

                        if(response != null){
                            switch (response.getHeaders().code()){
                                case 200:
                                    if(running){
                                        //Log.i(TAG , "Succes : 200");
                                        //Toast.makeText(activity, "Start match",Toast.LENGTH_SHORT).show();

                                        //Log.i(TAG , "Result checkMatch : "+response.getResult());
                                        int accept  = 0;

                                        try {
                                            accept = new JSONObject(response.getResult().toString()).getInt("accept");
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        //Log.i(TAG , "Accept"+accept);
                                        if(accept != 0){

                                            if(accept == 1){
                                                tl.onSucces();
                                            }

                                            if(accept == 2){
                                                tl.onRefuse();
                                            }

                                            tl.onFinish();

                                            running = false;
                                        }

                                    }
                                    break;
                                default:
                                    //Log.e(TAG , "Erreur"+response.getHeaders().code());
                                    Toast.makeText(activity, "Erreur : "+response.getResult(),Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            // Log.d(TAG, "Http code: " + response.getHeaders().code());
                            //Log.d(TAG, "Result: " + response.getResult());
                        }else{
                            tl.onFinish();
                            Log.e(TAG , "no response");
                        }
                        setExecute( false) ;
                    }
                });
    }

    public void setExecute(boolean execute) {
        //Log.i(TAG , "SetExecute "+execute);
        isExecute = execute;
    }

    public void sendNoResponse(){
        final String url ="http://192.168.43.144/siteWEB_classroom_renommerScript/reponseMatch.php?idmatch="+idMatch+"&type=3";
        Log.i(TAG , "url Response Match "+url);
        Ion.with(activity)
                .load(url)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> response) {

                        if(response != null){
                            switch (response.getHeaders().code()){
                                case 200:
                                    Toast.makeText(activity , "Pas de reponse sauvegarder"  , Toast.LENGTH_SHORT).show();
                                    Log.i(TAG , "Pas de reponse : savegarder en base");
                                    break;
                                default:
                                    Toast.makeText(activity, "Erreur : "+response.getResult(),Toast.LENGTH_SHORT).show();
                                    Log.i(TAG , "Pas de reponse : pas sauvegarder");
                                    break;
                            }
                            // Log.d(TAG, "Http code: " + response.getHeaders().code());
                            //Log.d(TAG, "Result: " + response.getResult());
                        }else{
                            tl.onFinish();
                            Log.e(TAG , "no response");
                        }
                        setExecute( false) ;
                        Log.i(TAG , "url (Fonction : Pas de reponse) : "+url);

                    }
                });
    }
}
