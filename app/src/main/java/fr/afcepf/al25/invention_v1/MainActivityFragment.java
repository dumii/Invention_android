package fr.afcepf.al25.invention_v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    ProductsAdapter adapter;
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.invention_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            updateProduits();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateProduits() {

        FetchProduitTask produitTask = new FetchProduitTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        produitTask.execute(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateProduits();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<ProductDTO> arrayOfProducts = new ArrayList<ProductDTO>();
        adapter = new ProductsAdapter(getActivity(), arrayOfProducts);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listeView = (ListView) rootView.findViewById(R.id.listViewProduits);
        listeView.setAdapter(adapter);
        listeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ProductDTO produit = adapter.getItem(position);
                //Toast toast = Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT);
                //toast.show();

                // Executed in an Activity, so 'this' is the Context
                // The fileUrl is a string URL, such as "http://www.example.com/image.png"
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("productDTO", produit);
                startActivity(intent);
            }
        });
        return rootView;

    }

    public class ProductsAdapter extends ArrayAdapter<ProductDTO> {

        public ProductsAdapter(Context context, ArrayList<ProductDTO> produits){
            super(context, 0, produits);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ProductDTO produit = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_items_produits, parent, false);
            }
            TextView nomProduit = (TextView) convertView.findViewById(R.id.liste_item_produit_textview);
            nomProduit.setText(produit.getNomProduct());
            ImageView imageProduit = (ImageView) convertView.findViewById(R.id.liste_item_produit_imageview);
            String image = produit.getPhoto().replace("images/","");
            image = image.replace(".jpg","");
            image = image.replace(".png","");
            int resId = getResources().getIdentifier(image, "drawable", getContext().getPackageName());
            imageProduit.setImageResource(resId);
            return convertView;
        }
    }

    public class FetchProduitTask extends AsyncTask <Integer, Void , List<ProductDTO>> {

        private final String LOG_TAG = FetchProduitTask.class.getSimpleName();

        @Override
        protected List<ProductDTO> doInBackground(Integer... params){
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            // Will contain the raw JSON response as a string.
            String produitsJsonStr = null;

            Integer categorieAll = params[0];
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&APPID=888132ed4faac863a5b92b0f95ec0c22");

                final String FORECAST_BASE_URL = "http://192.168.100.200:9090/Inventor-Web2/services/rest/products?"; //afc ad
//                final String FORECAST_BASE_URL = "http://192.168.100.33:8081/Inventor-Web2/faces/services/rest/products?"; //afc kevin
//                final String FORECAST_BASE_URL = "http://192.168.0.22:9090/Inventor-Web2/faces/services/rest/products?"; //home
//                final String FORECAST_BASE_URL = "http://192.168.1.76:9090/Inventor-Web2/faces/services/rest/products?"; //chez ana
                final String QUERY_PARAM = "categorie";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, Integer.toString(categorieAll))
                        .build();
                URL url = new URL(builtUri.toString());
                Log.i(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                produitsJsonStr = buffer.toString();

                Log.i(LOG_TAG, "Produit JSON String " + produitsJsonStr);


            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
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
                return getProduitDataFromJson(produitsJsonStr);
            } catch (JSONException je){
                Log.e(LOG_TAG, je.getMessage(), je);
                je.printStackTrace();
            }
            return null;
        }

        //@Override
        protected void onPostExecute(List<ProductDTO> result) {
            Log.i(LOG_TAG, "Passage par le onPostExecute");
            if(result != null){
                adapter.clear();
                for(ProductDTO produit : result){
                    adapter.add(produit);
                }
            }
        }

        /* The date/time conversion code is going to be moved outside the asynctask later,
           * so for convenience we're breaking it out into its own method now.
           */
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

        private List<ProductDTO> getProduitDataFromJson(String produitsJsonStr)
                throws JSONException {
            JSONArray jsonArrayProduits = new JSONArray(produitsJsonStr);
            List<ProductDTO> listeProduits = new ArrayList<ProductDTO>();
            ProductDTO product = null;
            JSONObject jsonObj = null;
            for(int i =0; i< jsonArrayProduits.length(); i++){
                jsonObj = jsonArrayProduits.getJSONObject(i);
                product = new ProductDTO();
                product.setIdProduct(jsonObj.getInt("idProduct"));
                product.setNomProduct(jsonObj.getString("nomProduct"));
                //TODO faire enum pour categorie
                product.setCategorieProduct(jsonObj.getInt("categorieProduct"));
                product.setDateCreationProduct(getReadableDateString(jsonObj.getString("dateCreationProduct")));
                product.setPhoto(jsonObj.getString("photo"));
                try {
                    product.setDescriptionProduct(jsonObj.getString("description"));
                } catch (JSONException je){
                    product.setDescriptionProduct("");
                    je.printStackTrace();
                }
                try {
                    product.setPrixProduct(jsonObj.getDouble("prix"));
                } catch (JSONException je){
                    product.setPrixProduct(0.00);
                    je.printStackTrace();
                }

                listeProduits.add(product);
            }

            return listeProduits;
        }
    }
}
