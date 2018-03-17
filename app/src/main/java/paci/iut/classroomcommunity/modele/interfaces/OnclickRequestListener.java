package paci.iut.classroomcommunity.modele.interfaces;


import paci.iut.classroomcommunity.modele.Request;

public interface OnclickRequestListener {
    void onAccept(Request request);
    void onRefuse(Request request);
}
