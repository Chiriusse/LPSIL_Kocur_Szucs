package com.example.sb500079.clientbdd_tp2;

import io.realm.RealmObject;

/**
 * Created by Benjamin on 03/12/2015.
 */
/*
    Classe apparemment inutile mais indispensable car lorsque la class Repo extends de RealmObject
    impossible de réaliser une requete GET/POST sans planter l'application...
*/
public class Personne  extends RealmObject
{
    private String id;
    private String name;
    private String full_name;
    private String html_url;

    public Personne(String id, String name, String full_name, String html_url) {
        this.id = id;
        this.name = name;
        this.full_name = full_name;
        this.html_url = html_url;
    }

    public Personne()
    {
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

}
