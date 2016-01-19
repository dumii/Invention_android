package fr.afcepf.al25.invention_v1;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ConnexionActivity extends AppCompatActivity {

    private final String LOG_TAG = ConnexionActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "La class ConnexionActivity est appelee");
        setContentView(R.layout.connexion);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new ConnexionActivityFragment())
//                    .commit();
//        }
    }
    public void submitConnect(View view){
        EditText message = (EditText) findViewById(R.id.messageText);
        message.setText("");
        EditText message2 = (EditText) findViewById(R.id.messageText2);
        message2.setText("");
        EditText userId = (EditText) findViewById(R.id.idText);
        String userIdStr = userId.getText().toString();
        EditText userMdp = (EditText) findViewById(R.id.mdpText);
        String userMdpStr = userMdp.getText().toString();
        Log.i(LOG_TAG, "Les credentials du user : ID=" + userIdStr + "; MDP=" + userMdpStr);

        FetchUserTask userTask = new FetchUserTask(userIdStr, userMdpStr, ConnexionActivity.this);
        AsyncTask result = userTask.execute();

        Log.i(LOG_TAG, "Le user est en cours de verification");
        message.setText("Vérification en cours...");

    }

    public class FetchUserTask extends AsyncTask<String, Void, UserDTO> {

        private final String LOG_TAG = FetchUserTask.class.getSimpleName();

        String id;
        String mdp;
        Activity activity;

        FetchUserTask() {
        }

        FetchUserTask(String id, String mdp, Activity activity) {
            this.id = id;
            this.mdp = mdp;
            this.activity = activity;
        }

        @Override
        protected UserDTO doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String userJsonStr = null;

            try {
                final String CONNECT_BASE_URL = "http://192.168.100.200:9090/Inventor-Web2/faces/services/rest/users?"; //afc ad
//            final String CONNECT_BASE_URL = "http://192.168.0.22:9090/Inventor-Web2/faces/services/rest/users?"; //home
                final String QUERY_PARAM_1 = "login";
                final String QUERY_PARAM_2 = "password";

                String builtUri = Uri.parse(CONNECT_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM_1, id)
                        .appendQueryParameter(QUERY_PARAM_2, mdp)
                        .build().toString();
                builtUri = formatStringUri(builtUri);
                URL url =new URL(builtUri);
                Log.i(LOG_TAG, "Built URL connexion " + url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    Log.i(LOG_TAG, "Le user n'est pas trouve (input).");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.i(LOG_TAG, "Le user n'est pas trouve (line).");
                }

                if (buffer.length() == 0) {
                    Log.i(LOG_TAG, "Le user n'est pas trouve (buffer).");
                    return null;
                }
                userJsonStr = buffer.toString();

                Log.i(LOG_TAG, "Produit JSON String " + userJsonStr);

            } catch (IOException e) {
                Log.e("FetchUserTask", "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }


            try{
                return getUserDataFromJson(userJsonStr);
            } catch (JSONException je){
                Log.e(LOG_TAG, je.getMessage(), je);
                je.printStackTrace();
            }

            return null;
        }

        public String formatStringUri(String builtUri) {
            String newUri = builtUri.replace("%40", "@");
            return newUri;
        }

        private UserDTO getUserDataFromJson(String userJsonStr)
                throws JSONException {
            JSONObject jsonObj = new JSONObject(userJsonStr);
            UserDTO user = new UserDTO();
            user.setIdUser(jsonObj.getInt("idUser"));
            user.setPrenom(jsonObj.getString("prenom"));
            user.setNom(jsonObj.getString("nom"));
            user.setIdCompte(jsonObj.getInt("idCompte"));
            user.setEmail(jsonObj.getString("email"));
            user.setPassword(jsonObj.getString("password"));
            user.setRole(jsonObj.getString("role"));
            Log.i(LOG_TAG, "Le user cree du json: Nom="+user.getNom());
            return user;
        }

        @Override
        protected void onPostExecute(UserDTO userDTO) {
            Log.i(LOG_TAG,"Post execute de FetchUserTask");
            EditText message2 = (EditText) findViewById(R.id.messageText2);
            if(userDTO != null){
                    Log.i(LOG_TAG, "Redirection vers la page mes commandes");
                    message2.setTextColor(Color.BLACK);
                    message2.setText("Redirection vers vos commandes");
                    Intent intent = new Intent(activity, CommandesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userId", userDTO.getIdUser());
                    intent.putExtra("userEmail", userDTO.getEmail());
                    intent.putExtra("userPass", userDTO.getPassword());
                    activity.getApplicationContext().startActivity(intent);
                }
            else{
                message2.setTextColor(Color.RED);
                message2.setText("User/Mot de passe erronés.");
            }
        }
    }


}