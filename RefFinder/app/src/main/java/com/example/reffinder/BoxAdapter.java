package com.example.reffinder;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BoxAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Game> objects;


    BoxAdapter(Context context, ArrayList<Game> games) {
        ctx = context;
        objects = games;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null) {
            view = lInflater.inflate(R.layout.game, parent, false);
        }

        Game p = getGame(position);
        ImageView poster = view.findViewById(R.id.game_img);
        Glide.with(ctx).load(p.image).into(poster);

        ((TextView) view.findViewById(R.id.game_name)).setText(p.Name);
        ((TextView) view.findViewById(R.id.game_type)).setText(p.Type + " ");
        return view;
    }

    private Game getGame(int position) {
        return ((Game) getItem(position));
    }

}