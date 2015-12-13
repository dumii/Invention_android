package fr.afcepf.al25.invention_v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parametres, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            if(intent != null){
                Bundle extras = intent.getExtras();
                if(extras != null){
                    ProductDTO produit = intent.getExtras().getParcelable("productDTO");
                    ((TextView)rootView.findViewById(R.id.detail_text_nom)).setText(produit.getNomProduct());
                    String image = produit.getPhoto().replace("images/", "");
                    image = image.replace(".jpg","");
                    image = image.replace(".png","");
                    int resId = getResources().getIdentifier(image, "drawable", getContext().getPackageName());
                    ((ImageView) rootView.findViewById(R.id.detail_imageview)).setImageResource(resId);
                    if(produit.getCategorieProduct()==1){
                        ((TextView)rootView.findViewById(R.id.detail_text_categorie)).setText("Conception");
                    } else {
                        if (produit.getCategorieProduct() == 2) {
                            ((TextView) rootView.findViewById(R.id.detail_text_categorie)).setText("Production");
                        } else {
                            ((TextView) rootView.findViewById(R.id.detail_text_categorie)).setText("Laboratoire");
                        }
                    }
                    ((TextView)rootView.findViewById(R.id.detail_text_description)).setText(produit.getDescriptionProduct());
                    ((TextView)rootView.findViewById(R.id.detail_text_prix)).setText(Double.toString(produit.getPrixProduct())+" €");
                }
            }
            return rootView;
        }
    }
}
