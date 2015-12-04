package com.example.sb500079.clientbdd_tp2;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;



public class MainActivity extends Activity implements View.OnClickListener{
    public static final String BASE_URL = "https://api.github.com";
    public static final String[] preferences = {"NOM","ID","URL"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonP = (Button) findViewById(R.id.button_preference);
        buttonP.setOnClickListener(this);

        Button buttonR = (Button) findViewById(R.id.button_recherche);
        buttonR.setOnClickListener(this);

        Button buttonD = (Button) findViewById(R.id.button_del);
        buttonD.setOnClickListener(this);

        loadPreferences();


    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.button_preference)
        {
            rechercheUtilisateur(1);
        }
        else if(v.getId() == R.id.button_recherche)
        {
            rechercheUtilisateur(0);
        }
        else if(v.getId() == R.id.button_del)
        {
            deletePreferences();
        }
    }

    public void deletePreferences()
    {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        //preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(preferences[0]);
        editor.remove(preferences[1]);
        editor.remove(preferences[2]);
        editor.commit();

        loadPreferences();
    }
    public void loadPreferences()
    {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String nomP = sharedPref.getString(preferences[0], "preference " + preferences[0] + " null !");
        String idP = sharedPref.getString(preferences[1], "preference "+ preferences[1] + " null !");
        String urlP = sharedPref.getString(preferences[2], "preference "+ preferences[2] + " null !");

        TextView nomT = (TextView) findViewById(R.id.textView_nom_utilisateur);
        nomT.setText(nomP);

        TextView idT = (TextView) findViewById(R.id.textView_id_utilisateur);
        idT.setText(idP);

        TextView urlT = (TextView) findViewById(R.id.textView_url_utilisateur);
        urlT.setText(urlP);
    }
    public void rechercheUtilisateur(final int paramettre)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GithubService service = retrofit.create(GithubService.class);

        EditText getNom = (EditText) findViewById(R.id.editText_nom);
        String nomUtillisateur = getNom.getText().toString();

        Call<List<Repo>> repo = service.listRepos(nomUtillisateur);

        repo.enqueue(new Callback<List<Repo>>() {

            @Override
            public void onResponse(Response<List<Repo>> response, Retrofit retrofit)
            {
                TextView reponse = (TextView) findViewById(R.id.textView_reponse);
                try{


                    reponse.setText(response.body().get(0).toString());
                    Log.i("Message", "Message1:" + response.body().get(0).toString());

                    if (paramettre == 1)
                    {
                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                        //preference

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(preferences[0], response.body().get(0).getName());
                        editor.putString(preferences[1], response.body().get(0).getId() + "");
                        editor.putString(preferences[2], response.body().get(0).getHtml_url());

                        editor.commit();

                        loadPreferences();
                        Toast.makeText(getApplicationContext(), String.format("OK"), Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception ex)
                {
                    Toast.makeText(getApplicationContext(), String.format("KO"), Toast.LENGTH_SHORT).show();
                    reponse.setText("Utilisateur introuvable...");
                }

            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), String.format("KO"), Toast.LENGTH_SHORT).show();

            }
        });
    }
}