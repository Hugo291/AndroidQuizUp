package paci.iut.classroomcommunity.Fragement;

import android.util.Log;

import paci.iut.classroomcommunity.modele.interfaces.OnScoreChange;


public class TimerQuizzScore extends Thread {

    private static final String TAG = "TAG_TimerQuizzScore";

    //En cours de process
    //false fin
    //true en cours
    private boolean running  = true;


    public void setOnScoreChange(OnScoreChange onScoreChange) {
        this.onScoreChange = onScoreChange;
    }

    OnScoreChange onScoreChange;

    //le compteur
    private int count = 0;
    private int max = 3;

    public TimerQuizzScore(){

    }

    @Override
    public void run() {

        super.run();
        while(running){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onScoreChange.onReception();

        }
        Log.i(TAG ,"End of the thread Timer");
    }

    public void close(){
        running = false;
    }



}
