package fr.afcepf.al25.invention_v1;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class CommandesActivity extends AppCompatActivity {

    private final String LOG_TAG = CommandesActivity.class.getSimpleName();
    String userEmailStr = "";
    String userMdpStr = "";
    Integer id = 0;
    FetchCommandeTask.CommandesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "La class CommandesActivity est appelee");
        setContentView(R.layout.commandes_client);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            userEmailStr = intent.getExtras().getString("userEmail");
            userMdpStr = intent.getExtras().getString("userPass");
            id = intent.getExtras().getInt("userId");
            Log.i(LOG_TAG, "Identifiants de connexion : id=" + id + " email=" + userEmailStr + " mdp=" + userMdpStr);
        }

//        updateCommandes(id, userEmailStr, userMdpStr);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new ConnexionActivityFragment())
//                    .commit();
//        }
    }

    private void updateCommandes(Integer id, String userEmailStr, String userMdpStr) {
        Log.i(LOG_TAG, "Update commande id=" + id);
        FetchCommandeTask commandeTask = new FetchCommandeTask(String.valueOf(id), userEmailStr, userMdpStr, CommandesActivity.this);
        commandeTask.execute();
//        fillAdapter();
    }
//    private void fillAdapter(){
//        ArrayList<CommandeDTO> arrayOfCommandes = new ArrayList<CommandeDTO>();
//        adapter = new CommandesAdapter(this, arrayOfCommandes);
//
//        ListView listeView = (ListView) findViewById(R.id.listViewCommandes);
//        listeView.setAdapter(adapter);
//    }

    @Override
    public void onStart() {
        super.onStart();
        updateCommandes(id, userEmailStr,userMdpStr);
    }

    @Override
    public void onStop() {
        super.onStop();
    }



//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        ArrayList<CommandeDTO> arrayOfCommandes = new ArrayList<CommandeDTO>();
//        adapter = new CommandesAdapter(this, arrayOfCommandes);
//
//        View rootView = getLayoutInflater().inflate(R.layout.commandes_client, context, false);
//        ListView listeView = (ListView) rootView.findViewById(R.id.listViewCommandes);
//        listeView.setAdapter(adapter);
//
//        return rootView;
//    }


    public class FetchCommandeTask extends AsyncTask<String, Void, List<CommandeDTO>> {

        private final String LOG_TAG = FetchCommandeTask.class.getSimpleName();

        String id;
        String email;
        String mdp;
        Activity activity;

        FetchCommandeTask() {
        }

        FetchCommandeTask(String id, String email, String mdp, Activity activity) {
            this.id = id;
            this.email = email;
            this.mdp = mdp;
            this.activity = activity;
        }

        @Override
        protected List<CommandeDTO> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String cmdsJsonStr = null;

            try {
                final String CONNECT_BASE_URL = "http://192.168.100.200:9090/Inventor-Web2/services/rest/commandes?"; //afc ad
//                final String CONNECT_BASE_URL = "http://192.168.0.22:9090/Inventor-Web2/faces/services/rest/users?"; //home
                final String QUERY_PARAM_1 = "id";
//                final String QUERY_PARAM_2 = "password";

                String builtUri = Uri.parse(CONNECT_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM_1, id)
//                        .appendQueryParameter(QUERY_PARAM_2, mdp)
                        .build().toString();
                builtUri = formatStringUri(builtUri);
                URL url = new URL(builtUri);
                Log.i(LOG_TAG, "Built URL connexion " + url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    Log.i(LOG_TAG, "Commandes pas trouvees (input).");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.i(LOG_TAG, "Commandes pas trouvees (line).");
                }

                if (buffer.length() == 0) {
                    Log.i(LOG_TAG, "Commandes pas trouvees (buffer).");
                    return null;
                }
                cmdsJsonStr = buffer.toString();

                Log.i(LOG_TAG, "Produit JSON String " + cmdsJsonStr);

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
//            return Arrays.asList(new CommandeDTO());

            try{
                return getCommandeDataFromJson(cmdsJsonStr);
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

        private List<CommandeDTO> getCommandeDataFromJson(String commandeJsonStr)
                throws JSONException {

            JSONArray jsonArrayCommandes = new JSONArray(commandeJsonStr);
            List<CommandeDTO> listeCommandes = new ArrayList<CommandeDTO>();
            CommandeDTO commande = null;
            JSONObject jsonObj = null;
            for(int i =0; i< jsonArrayCommandes.length(); i++){
                jsonObj = jsonArrayCommandes.getJSONObject(i);
                commande = new CommandeDTO();
                try {
                    commande.setIdOrder((jsonObj.getInt("idOrder")));
                } catch (JSONException je){
                    commande.setIdOrder(0);
                    je.printStackTrace();
                }
                try {
                    commande.setStateOrder(jsonObj.getString("stateOrder"));
                } catch (JSONException je){
                    commande.setStateOrder("En cours");
                    je.printStackTrace();
                }
                commande.setCreationDate((getReadableDateString(jsonObj.getString("creationDate"))));
                try {
                    commande.setTotalOrder(jsonObj.getDouble("totalOrder"));
                } catch (JSONException je){
                    commande.setTotalOrder(0.00);
                    je.printStackTrace();
                }

                listeCommandes.add(commande);
            }
            return listeCommandes;
        }

        private Date getReadableDateString(String time){
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = shortenedDateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }

        @Override
        protected void onPostExecute(List<CommandeDTO> commandeDTOs) {
            Log.i(LOG_TAG, "Post execute de FetchCommandeTask");

            ArrayList<CommandeDTO> arrayOfCommandes = new ArrayList<>();
            adapter = new CommandesAdapter(activity, commandeDTOs);
            ListView listeView = (ListView) findViewById(R.id.listViewCommandes);
            listeView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
//            if(commandeDTOs != null){
//                adapter.clear();
//                for(CommandeDTO cmd : commandeDTOs){
//                    adapter.add(cmd);
//                }
//            }
        }

        public class CommandesAdapter extends ArrayAdapter<CommandeDTO> {

            public CommandesAdapter(Context context, List<CommandeDTO> commandes) {
                super(context,0, commandes);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                CommandeDTO commande = getItem(position);
                Log.i(LOG_TAG, "COMMANDE: ref="+commande.getIdOrder()+" date="+commande.getCreationDate()+" prix="+commande.getTotalOrder()+" statut="+commande.getStateOrder());
                if(convertView == null){
                    //convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_items_commandes, parent, false);
                    convertView = activity.getLayoutInflater().inflate(R.layout.list_items_commandes, parent, false);
                }
                ImageView statusImg = (ImageView) convertView.findViewById(R.id.liste_item_commandes_imageview);
                if(commande.getStateOrder().equals("Expédiée")){
                    int resId = getResources().getIdentifier("icon_done", "drawable", getContext().getPackageName());
                    statusImg.setImageResource(resId);
                } else if(commande.getStateOrder().equals("Annulée")){
                    int resId = getResources().getIdentifier("icon_cancelled", "drawable", getContext().getPackageName());
                    statusImg.setImageResource(resId);
                } else {
                    int resId = getResources().getIdentifier("icon_progress", "drawable", getContext().getPackageName());
                    statusImg.setImageResource(resId);
                }
                TextView num = (TextView) convertView.findViewById(R.id.commande_numero_textview);
                num.setText(String.valueOf(commande.getIdOrder()));
                TextView dateTextView = (TextView) convertView.findViewById(R.id.commande_date_textview);
                dateTextView.setText(getPrettyDateFromDate(commande.getCreationDate()));
                TextView prixTextView = (TextView) convertView.findViewById(R.id.commande_prix_textview);
                prixTextView.setText(String.valueOf(commande.getTotalOrder()));
                return convertView;
            }
            private String getPrettyDateFromDate(Date date){
                Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = formatter.format(date);
                return dateStr;
            }
        }
    }
}