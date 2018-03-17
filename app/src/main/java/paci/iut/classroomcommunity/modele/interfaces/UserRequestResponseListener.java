package paci.iut.classroomcommunity.modele.interfaces;

public interface UserRequestResponseListener {
    static int isFinish = 0;

    void onSucces();
    void onRefuse();
    void onFinish();

}
