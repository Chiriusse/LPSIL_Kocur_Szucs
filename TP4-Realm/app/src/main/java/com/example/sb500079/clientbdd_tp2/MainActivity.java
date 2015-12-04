package com.example.sb500079.clientbdd_tp2;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
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
    public final Context context = this;
    public Realm realm;
    public EditText inputBdd ;
    ArrayList<Personne> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonR = (Button) findViewById(R.id.button_recherche);
        buttonR.setOnClickListener(this);

        Button buttonD = (Button) findViewById(R.id.button_del);
        buttonD.setOnClickListener(this);

        inputBdd = (EditText) findViewById(R.id.editText_bdd);

        //ajouter evenement sur Edittext
        inputBdd.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    loadUser();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void afterTextChanged(Editable s) {
            }
        });

        //afficher list users
        loadUser();
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.button_recherche) // ajouter un users a la bdd
        {
            rechercheUtilisateur();
        }
        else if(v.getId() == R.id.button_del) //suprimer un/des utilisateurs de la Bdd
        {
            deleteBdd();
        }
    }

    private void deleteBdd()
    {
        //supression BDD avec chaine
        realm = Realm.getInstance(context);
        realm.beginTransaction();
        RealmResults<Personne> result;

        if (inputBdd.getText().toString() == "") //select all users
        {
            RealmQuery<Personne> query = realm.where(Personne.class);
            result = query.findAll();
        }
        else //select users by "value"
        {
             result = getResult(inputBdd.getText().toString());
        }

        //supprimer la selection d'users
        result.clear();
        realm.commitTransaction();

        //refresh list
        loadUser();
    }

    public void loadUser()
    {
        realm = Realm.getInstance(context);

        if (inputBdd.getText().toString() == "") // select all users
            al = getAllUsers();
        else                                    // select suivant chaine input
            al = getUsers(inputBdd.getText().toString());

        if (al != null)
        {
            UserAdapter userstBase = new UserAdapter(this, al);
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(userstBase);
        }
    }

    public ArrayList getAllUsers()
    {
        realm = Realm.getInstance(context);
        RealmQuery<Personne> query = realm.where(Personne.class);
        RealmResults<Personne> result = query.findAll();

        ArrayList listUsers = new ArrayList<>();

        for (Personne u : result)
        {
            listUsers.add(u);
        }

        return listUsers;
    }

    public RealmResults<Personne> getResult(String chaine)
    {
        RealmResults<Personne> result = realm.where(Personne.class)
                .beginGroup()
                .contains("name", chaine)
                .or()
                .contains("id", chaine)
                .or()
                .contains("full_name", chaine)
                .or()
                .contains("html_url", chaine)
                .endGroup()
                .findAll();

        return result;
    }
    public ArrayList getUsers(String chaine)
    {
        Realm realm = Realm.getInstance(context);
        RealmQuery<Personne> query = realm.where(Personne.class);
        RealmResults<Personne> result = getResult(chaine);

        ArrayList listUsers = new ArrayList<>();

        for (Personne u : result)
        {
            listUsers.add(u);
        }

        return listUsers;
    }

    public void rechercheUtilisateur()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubService service = retrofit.create(GithubService.class);

        EditText getNom = (EditText) findViewById(R.id.editText_nom);
        String nomUtillisateur = getNom.getText().toString();

        Call<List<Repo>> repo = service.listRepos(nomUtillisateur);

        //Récuperation des données par requete Get
        repo.enqueue(new Callback<List<Repo>>() {

            @Override
            public void onResponse(Response<List<Repo>> response, Retrofit retrofit)
            {
                try{
                    Log.i("Message", "Message1:" + response.body().get(0).getName());

                    //récuperation des variables de la réponse
                    String id  = response.body().get(0).getId().toLowerCase();
                    String name = response.body().get(0).getName().toLowerCase();
                    String full_name = response.body().get(0).getFull_name().toLowerCase();
                    String html_url = response.body().get(0).getHtml_url().toLowerCase();

                    //instantiation de l'objet personne qui va être enregistré en base
                    Personne user = new Personne(id, name, full_name, html_url);

                    realm = Realm.getInstance(context);
                    // Copy de l'objet dans realm
                    realm.beginTransaction();
                    Personne realmUser = realm.copyToRealm(user);
                    realm.commitTransaction();

                    //refresh list
                    loadUser();

                    Toast.makeText(getApplicationContext(), String.format("User ajouté"), Toast.LENGTH_SHORT).show();

                }catch(Exception ex)
                {
                    Toast.makeText(getApplicationContext(), String.format("Utilisateur introuvable..."), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), String.format("KO"), Toast.LENGTH_SHORT).show();

            }
        });
    }
}