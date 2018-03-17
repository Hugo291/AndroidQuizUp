package paci.iut.classroomcommunity.Fragement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pedro.vlc.VlcListener;
import com.pedro.vlc.VlcVideoLibrary;

import paci.iut.classroomcommunity.R;

public class CameraFragment extends Fragment implements VlcListener {
    //public static String MRL_SERVER = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
    public static String MRL_SERVER = "rtsp://admin:Smart-2018@192.168.137.141:554/12";
    private  SurfaceView previewSV;
    VlcVideoLibrary vlcPlayer;

    public CameraFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        previewSV = (SurfaceView) view.findViewById(R.id.previewSV);
        vlcPlayer = new VlcVideoLibrary(getActivity(), this, previewSV);
        vlcPlayer.play(MRL_SERVER);
        return view;
    }


    @Override
    public void onComplete() {
        Toast.makeText(getActivity(), "Playing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        Toast.makeText(getActivity(), "Error, incorrect MRL", Toast.LENGTH_SHORT).show();
        vlcPlayer.stop();
    }
}
