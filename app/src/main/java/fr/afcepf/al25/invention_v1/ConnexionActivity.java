package fr.afcepf.al25.invention_v1;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class ConnexionActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivityFragment.FetchProduitTask.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG,"La class ConnexionActivity est appelee");
        setContentView(R.layout.connexion);
    }

    public void submitConnect(View view){
        EditText userId = (EditText) findViewById(R.id.idText);
        String userIdStr = userId.toString();
        EditText userMdp = (EditText) findViewById(R.id.mdpText);
        String userMdpStr = userMdp.toString();
        Log.i(LOG_TAG, "Les credentials du user : ID=" + userIdStr+"; MDP="+userMdpStr);
        checkUser(userIdStr, userMdpStr);
    }

    public void checkUser(String id, String mdp){
        EditText message = (EditText) findViewById(R.id.messageText);
        message.setText("Verification du user "+id);
        boolean userValid = false;
        //TODO connexion a l'appli et verification des id+mdp

        if(userValid == false){
            message.setText("Le user et le mot de passe ne sont pas valides.");
        } else {
            //TODO redirection vers la page des commandes, si c'est user ou vendeur
        }
    }

}