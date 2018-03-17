package paci.iut.classroomcommunity.Fragement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import paci.iut.classroomcommunity.Activity.MainActivity;
import paci.iut.classroomcommunity.R;
import paci.iut.classroomcommunity.modele.Friend;

public class PresenceFragment extends Fragment implements ZBarScannerView.ResultHandler {

    private static final String TAG = "TAG_PresenceFragment" ;
    ZBarScannerView scannerView;

    public PresenceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_presence, container, false);

        //Le nom de l'activité
        getActivity().setTitle("QrCode Presence");

        //Le code QR
        scannerView = new ZBarScannerView(getActivity());
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QRCODE);
        scannerView.setFormats(formats);

        FrameLayout contentFrame = (FrameLayout) view.findViewById(R.id.presenceFrame);
        //Ajout du qr dans la vue
        contentFrame.addView(scannerView);

        view.findViewById(R.id.QrCodeValidation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getConnect("paci.iut.1235", new Friend(7,"Jeremy" ,"Bourgis" ));
            }
        });

        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        String qrcode = result.getContents();
        Log.i(TAG, "Qrcode" + qrcode);
        Toast.makeText(getContext() , "Qrcode : "+qrcode , Toast.LENGTH_SHORT).show();
        getConnect( qrcode ,  new Friend(13,"Hugo","Ferreira") );
    }

    public  void getConnect(final String qrcode , final Friend friend){
        MainActivity.setMe(friend);
        final String file ="http://192.168.43.144/siteWEB_classroom_renommerScript/checkAttending.php?key="+qrcode+"&id="+friend.getId();
        Ion.with(getActivity())
                .load(file)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        if(response != null){
                            switch (response.getHeaders().code()){
                                case 200:
                                    //Log.i(TAG , "Succes : 200");
                                    Toast.makeText(getActivity(), "Succés Liste Amis",Toast.LENGTH_SHORT).show();

                                    //Set Qr et idPlayer
                                    MainActivity.setCodeQr(qrcode);
                                    MainActivity.setIdPlayer(friend.getId());

                                    getFriend();
                                    break;
                                default:
                                    //Log.e(TAG , "Erreur"+response.getHeaders().code());
                                    Toast.makeText(getActivity(), "Erreur : "+response.getResult(),Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            // Log.d(TAG, "Http code: " + response.getHeaders().code());
                            //Log.d(TAG, "Result: " + response.getResult());
                        }else{
                            Toast.makeText(getContext(), "Erreur no response",Toast.LENGTH_SHORT).show();
                            Log.e(TAG , "no response");

                        }

                    }
                });

    }


    public void getFriend(){
        MainActivity.nav.getMenu().performIdentifierAction(R.id.nav_friends, 0);
    }
}
