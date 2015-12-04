package com.example.sb500079.clientbdd_tp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Benjamin on 03/12/2015.
 */
public class UserAdapter extends BaseAdapter
{
    ArrayList<Personne> al = new ArrayList<Personne>();
    Context context;
    Personne user;
    LayoutInflater inflater;

    public UserAdapter(Context context, ArrayList<Personne> users) {
        this.context = context;
        this.al = users;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        if (al != null)
            return al.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        if (al != null)
            return al.get(position);
        else
            return 0;
    }

    @Override
    public long getItemId(int position) {
        if (al != null)
            return position;
        else
            return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_personne, parent, false);
        }

        if (al != null)
        {
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView id = (TextView) convertView.findViewById(R.id.id);
            TextView url = (TextView) convertView.findViewById(R.id.url);

            user = (Personne) getItem(position);

            name.setText(user.getName());
            id.setText(user.getId());
            url.setText(user.getHtml_url());
        }

        return convertView;
    }
}
