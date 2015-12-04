package com.example.sb500079.clientbdd_tp2;

/**
 * Created by sb500079 on 17/11/2015.
 */
public class Repo
{
    private int id;
    private String name;
    private String full_name;
    private String html_url;

    public Repo(int id, String name, String full_name, String html_url) {
        this.id = id;
        this.name = name;
        this.full_name = full_name;
        this.html_url = html_url;
    }

    public Repo(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString()
    {

        return "Name :" + getName() + " ID : " + getId() + " FullName : " + getFull_name() + " URL: " + getHtml_url();
    }
}
