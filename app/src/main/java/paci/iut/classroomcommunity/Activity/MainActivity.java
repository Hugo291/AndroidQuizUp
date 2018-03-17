package paci.iut.classroomcommunity.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.List;

import paci.iut.classroomcommunity.Fragement.AboutFragment;
import paci.iut.classroomcommunity.Fragement.CameraFragment;
import paci.iut.classroomcommunity.Fragement.FriendFragment;
import paci.iut.classroomcommunity.Fragement.ParametreFragment;
import paci.iut.classroomcommunity.Fragement.PresenceFragment;
import paci.iut.classroomcommunity.Fragement.RequestFragment;
import paci.iut.classroomcommunity.R;
import paci.iut.classroomcommunity.modele.Friend;
import paci.iut.classroomcommunity.modele.Request;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "TAG_MainActivity" ;

    private static Friend me;

    private static int idPlayer = 13;
    private static String CodeQr = null;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private FragmentManager fm;

    private TextView textViewUsername;
    public static NavigationView nav;

    private static TextView userNameTV;
    private static ImageView userAvatar;

    public static List<Friend> listFriend;


    public static Friend getMe() {
        return me;
    }

    public static void setMe(Friend me) {
        MainActivity.me = me;
        Log.i(TAG , "Set Friend "+me.toString());
        userNameTV.setText(me.getNomComplet());
        userAvatar.setBackground(me.getDrawableRandom());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //me = new Friend(13,"Hugo","Ferreira");

        //On recupere l'header du drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        //On recupere le champ username
        textViewUsername = (TextView) headerView.findViewById(R.id.user_name);

        //byId
        this.drawer = (DrawerLayout) findViewById(R.id.drawer);
        nav = (NavigationView) findViewById(R.id.nav_view);

        this.toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        this.drawer.addDrawerListener(toggle);
        this.toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Création du manager
        this.fm = getSupportFragmentManager();
        nav.setNavigationItemSelectedListener(this);

        //Lancement par defaut de la page friend
        nav.getMenu().performIdentifierAction(R.id.nav_presence, 0);

        Recuperation();

        userNameTV = ((TextView) headerView.findViewById(R.id.user_name));

        userAvatar = headerView.findViewById(R.id.user_avatar);


        //Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},99);
        }
        else {

        }

    }

    public void Recuperation(){
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        String username =  bundle.getString("username");
//        textViewUsername.setText(username);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_about:
                fm.beginTransaction().replace(R.id.contentFL , new AboutFragment() ).commit();
                break;
            case R.id.nav_friends:
                fm.beginTransaction().replace( R.id.contentFL , new FriendFragment() ).commit();
                break;
            case R.id.nav_logout:
                disconnect();

                break;
            case R.id.nav_presence:
                fm.beginTransaction().replace(R.id.contentFL , new PresenceFragment() ).commit();
                break;
            case R.id.nav_cam:
                fm.beginTransaction().replace(R.id.contentFL , new CameraFragment() ).commit();
                break;
            case R.id.nav_request:
                fm.beginTransaction().replace(R.id.contentFL , new RequestFragment() ).commit();
                break;
            case R.id.nav_param:
                fm.beginTransaction().replace(R.id.contentFL , new ParametreFragment() ).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void disconnect(){

        final String url  = "http://192.168.43.144/siteWEB_classroom_renommerScript/disconnect.php?id="+MainActivity.getMe().getId();
        Ion.with(this)
                .load(url)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {

                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        if(response != null){
                            switch (response.getHeaders().code()){
                                case 200:
                                    Toast.makeText(MainActivity.this,"Deconnecter",Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                default:
                                    Log.e(TAG , "Erreur : "+String.valueOf(response.getHeaders().code()));
                                    break;
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "Erreur pas de reponse getRequest ",Toast.LENGTH_SHORT).show();
                        }


                    }//end onCompleted

                });
    }

    //setters && getters

    public static List<Friend> getListFriend() {
        return listFriend;
    }

    public static void setListFriend(List<Friend> listFriend) {
        MainActivity.listFriend = listFriend;
        Log.i(TAG ,"Set listFriend"+listFriend);
    }

    public  static String getCodeQr() {
        return CodeQr;
    }

    public static void setCodeQr(String codeQr) {
        Log.i(TAG , "Qrcode set  : "+codeQr);
        CodeQr = codeQr;
    }

    public static int getIdPlayer() {
        return idPlayer;
    }

    public static void setIdPlayer(int idPlayer) {
        MainActivity.idPlayer = idPlayer;
    }
}
