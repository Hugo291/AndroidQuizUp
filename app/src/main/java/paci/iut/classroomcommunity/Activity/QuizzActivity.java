package paci.iut.classroomcommunity.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import paci.iut.classroomcommunity.modele.interfaces.OnScoreChange;
import paci.iut.classroomcommunity.Fragement.TimerQuizzScore;
import paci.iut.classroomcommunity.R;
import paci.iut.classroomcommunity.modele.Answer;
import paci.iut.classroomcommunity.modele.Friend;
import paci.iut.classroomcommunity.modele.Question;

public class QuizzActivity extends AppCompatActivity {

    private static final String TAG = "TAG_QuizzActivity";

    private Thread threadTime;

    //Le timer
    private TextView textViewTime;

    //Les boutons
    private Button[] answersTextView;

    //Le nom de l'adversaire
    private TextView friendNameTv;

    private TextView meNameTv;
    //Images
    //adversaire
    private ImageView friendImageAdv;
    //me
    private ImageView friendImageMe;

    //Les scores
    TextView[] Score;

    //La reponse actuel
    int CurrentResponse = -1;

    //la question
    TextView questionTextView;

    private Runnable run;

    private List<Question> questions;

    private int indexCurrentQuestion;

    private int rightAnswer;

    //Le bon
    private boolean isAsker;
    private int idMatch;

    //Le timer des scores
    private TimerQuizzScore timerQuizzScore;
    private ProgressBar progressTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        //Assignation des objet View
        init();

        chrono();

        Recuperation();

        setScore(0, 0);


        //Ajout des listener sur les boutons les uns apres les autres

        for (int i = 0; i < 4; i++) {
            final int finalI = i;
            answersTextView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ExecuteResponse(finalI)){
                        sendScore(1);
                    }
                    sendScore(0);
                }
            });
        }

        getQuestions();

        timerQuizzScore = new TimerQuizzScore();
        timerQuizzScore.setOnScoreChange(new OnScoreChange() {
            @Override
            public void onReception() {
                try{
                    getScore();
                }catch (Exception e){
                    Log.e(TAG , "Erreur pendant le set du score");
                }
            }
        });
        timerQuizzScore.start();

        setEnableBoutons(true);
        //setQuestionDisplay(questions.get(0) , 0);


    }

    //Check de la reponse si correct
    public boolean ExecuteResponse(int userChoice) {

        CurrentResponse = userChoice;
        boolean resultResponse = false;

        for (int i = 0; i < 4; i++) {

            if (userChoice == rightAnswer && userChoice == i) {
                answersTextView[i].setBackgroundResource(R.color.valid);
                resultResponse = true;

            } else if (userChoice != rightAnswer && userChoice == i) {
                answersTextView[i].setBackgroundResource(R.color.error);
            } else {
                answersTextView[i].setBackgroundResource(R.color.disable);
            }
            answersTextView[i].setEnabled(false);

        }

        return resultResponse;

    }

    public void setQuestionDisplay(Question question , int index) {

        indexCurrentQuestion = index;
        questionTextView.setText(question.getText());

        for (int i = 0; i < 4; i++) {
            Answer answer = question.getAnswers().get(i);
            answersTextView[i].setText(answer.getText());
            if (answer.getIsRight())
                rightAnswer = i;
        }
    }

    public void setScore(int scorePlayer1, int scorePlayer2) {
        this.Score[0].setText("" + scorePlayer1);
        this.Score[1].setText("" + scorePlayer2);
    }

    private void init() {

        //Le timer
        textViewTime = (TextView) findViewById(R.id.temp);

        //Assignation des textView adversaire Image et Nom

        friendImageAdv = (ImageView) findViewById(R.id.image_adv);
        friendImageMe = (ImageView) findViewById(R.id.image_me);

        friendNameTv = (TextView) findViewById(R.id.nom2);
        meNameTv = (TextView) findViewById(R.id.nom1);
        //Assignation des textView scores
        Score = new TextView[2];
        Score[0] = (TextView) findViewById(R.id.score1);
        Score[1] = (TextView) findViewById(R.id.score2);

        questionTextView = findViewById(R.id.question);

        //Creation et  assignations des boutons
        answersTextView = new Button[4];
        answersTextView[0] = (Button) findViewById(R.id.answer1);
        answersTextView[1] = (Button) findViewById(R.id.answer2);
        answersTextView[2] = (Button) findViewById(R.id.answer3);
        answersTextView[3] = (Button) findViewById(R.id.answer4);

        progressTime = (ProgressBar) findViewById(R.id.progressBartemp);

    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        Log.i(TAG , "set currentResponse value  "+CurrentResponse);
//        outState.putInt("currentResponse" ,this.CurrentResponse);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
//        int current = savedInstanceState.getInt("currentResponse");
//        if(current > -1){
//            Log.i(TAG , "Set the current response : "+current);
//            ExecuteResponse(current);
//        }else{
//            Log.i(TAG , "Not set the current response : "+current);
//        }
//        super.onRestoreInstanceState(savedInstanceState, persistentState);
//    }

    //Quand on essaye de quitter
    public void onBackPressed() {

        Log.i(TAG, "Click on press");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialogContinuer);

        builder.setPositiveButton(R.string.CONTINUER, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                QuizzActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton(R.string.RESTER, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerQuizzScore.close();
    }

    //Le chrono
    private void chrono() {

        final Handler handler = new Handler();
        run = new Runnable() {

            int current = 10;
            boolean active = true;

            @Override
            public void run() {
                while (current >= 0 && active) {
                    //Log.i(TAG , ""+current);

                    try {
                        handler
                                .post(new Runnable() {

                                    @Override
                                    public void run() {
                                        if(CurrentResponse == -1){
                                            textViewTime.setText(String.valueOf(current));
                                        }
                                        progressTime.setProgress(current*10);
                                    }
                                });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    current--;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setEnableBoutons(false);
                    }
                });
                Log.i(TAG, "Fin du thread");

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Next();
                    }
                });

            }


        };

        threadTime = new Thread(run);
        threadTime.start();
    }

    public void Next(){
        indexCurrentQuestion++;
        if(questions.size() <= indexCurrentQuestion ){
            Toast.makeText(getBaseContext() , "Partie Terminée",Toast.LENGTH_LONG);
            finish();
            return ;
        }
        setQuestionDisplay(questions.get(indexCurrentQuestion),indexCurrentQuestion);
        chrono();
        setEnableBoutons(true);
        CurrentResponse = -1;
    }

    //Disable les reponnse
    public void setEnableBoutons(boolean status) {
        for (int i = 0; i < 4; i++) {
            answersTextView[i].setEnabled(status);
            if(status == true){
                answersTextView[i].setBackgroundResource(R.color.enable);
            }
        }
    }

    //Recuperation de des données depuis la page d'avant
    public void Recuperation() {

        //Recuperation de l'intent
        Intent intent = getIntent();
        //Recuperation du bundle
        Bundle bundle = intent.getExtras();
        Friend friend = (Friend) bundle.getSerializable("friend_adv");
        //Recuperation du nom de l'adversaire amie
        this.isAsker = bundle.getBoolean("isAsker");
        this.idMatch = bundle.getInt("idMatch");

        friendImageAdv.setImageDrawable(friend.getDrawableRandom());
        friendNameTv.setText(friend.getNomComplet());

        friendImageMe.setBackground(MainActivity.getMe().getDrawableRandom());
        meNameTv.setText(MainActivity.getMe().getNomComplet());
    }

    public int getIntIsAsker() {
        if (isAsker)
            return 1;
        return 0;
    }

   public void getScore(){
        String url="http://192.168.43.144/siteWEB_classroom_renommerScript/checkMatch.php?id="+this.idMatch;
        Log.i(TAG , "Url GetScore = "+url);
        Ion.with(this)
                .load(url)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {

                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        if (response != null) {
                            switch (response.getHeaders().code()) {
                                case 200:
                                    //Log.i(TAG , "getScore : "+response.getResult());
                                    JSONObject json = null;

                                    try {
                                        json = new JSONObject(response.getResult());
                                        int score1 = json.getInt("score1");
                                        int score2 = json.getInt("score2");
                                        Log.i(TAG,"Score1 = "+score1+" Score2 = "+score2);
                                        setScore(score1  , score2);

                                    } catch (JSONException e1) {
                                        Log.i(TAG,  "Error de recuperaiton de resultat");
                                        e1.printStackTrace();
                                    }

                                    break;
                                default:
                                    Log.i(TAG , "getScore : "+response.getResult());

                                    break;
                            }
                        } else {
                            Log.i(TAG , "getScore : "+response.getResult());

                        }
                    }
                });
    }

    //Send score au sserveur
    public void sendScore(int score) {
        Log.i(TAG , "Score set to server : "+score);
        String url = "http://192.168.43.144/siteWEB_classroom_renommerScript/setScore.php?idMatch="+this.idMatch+"&isAsker="+getIntIsAsker()+"&score="+score;

        Ion.with(this)
                .load(url)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {

                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        if (response != null) {
                            switch (response.getHeaders().code()) {
                                case 200:
                                    Log.i(TAG , "Succes lors de l'envoie");
                                    break;
                                default:
                                    Log.i(TAG , "Erreur lors de l'envoie");
                                    break;
                            }
                        } else {
                            Log.i(TAG , "Erreur lors de l'envoie NULL");
                        }
                    }
                });
    }

    //Obtenir la question
    public void getQuestions(){

        final String urlString = "http://192.168.43.144/siteWEB_classroom_renommerScript/getQuestions.php?key=paci.iut.1235";//+MainActivity.getCodeQr();

        Ion.with(this)
                .load(urlString)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {

                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        if (response != null) {
                            switch (response.getHeaders().code()) {
                                case 200:
                                    Log.i(TAG, "question");
                                    Log.i(TAG, response.getResult());
                                    questions = Question.getListOfQuestionFromJson(response.getResult());
                                    setQuestionDisplay(questions.get(0),0);
                                    break;
                                default:
                                    Log.e(TAG, response.getResult());
                                    break;
                            }

                        } else {
                            Log.e(TAG, "No response of server of " + urlString);
                        }

                    }

                });
    }




}