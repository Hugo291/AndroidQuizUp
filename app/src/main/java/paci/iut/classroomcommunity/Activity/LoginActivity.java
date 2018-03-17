package paci.iut.classroomcommunity.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import paci.iut.classroomcommunity.R;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private static final String USER = "u";
    private static final String PASS = "p";

    EditText  idt ;
    EditText  mdp ;
    Button cn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idt = (EditText) findViewById(R.id.identifiant);
        idt.setText("u");

        mdp = (EditText) findViewById(R.id.motdepasse);
        mdp.setText("p");

        cn = (Button) findViewById(R.id.connexion);
        cn.setText("Connexion");

    }

    public void connect(View v) {

        Toast t ;
        if(idt.getText().toString().equals(USER) && mdp.getText().toString().equals(PASS)){

            t= Toast.makeText(v.getContext() , "Succés" , Toast.LENGTH_SHORT);
            Intent intent = new Intent(this , MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username",idt.getText().toString());
            bundle.putString("password",mdp.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);

        }else{
            t=Toast.makeText(v.getContext() , "Erreur" , Toast.LENGTH_SHORT);
        }
        t.show();

    }


}
