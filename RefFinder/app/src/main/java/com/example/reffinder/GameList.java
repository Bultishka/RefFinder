package com.example.reffinder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.reffinder.databinding.FragmentGameListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameList extends Fragment {

    private DatabaseReference gamesRef;

    int gameType = 1;

    FragmentGameListBinding binding;

    BoxAdapter boxAdapter;

    ArrayList<Game> games = new ArrayList<Game>();


    public GameList() {
        // Required empty public constructor
    }

    public static GameList newInstance(String param1, String param2) {
        GameList fragment = new GameList();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameListBinding.inflate(getLayoutInflater());
        MainActivity activity = (MainActivity) getActivity();

        binding.typeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.loadTypes();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gamesRef = FirebaseDatabase.getInstance().getReference().child("Games");
        fillData();
        boxAdapter = new BoxAdapter(getContext(), games);
        binding.list.setAdapter(boxAdapter);
        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Game selectedGame = games.get(i);
                Intent intent = new Intent(getActivity(), GamePage.class);
                intent.putExtra("Folder", gameType);
                intent.putExtra("Name", selectedGame.Name);
                intent.putExtra("Poster", selectedGame.image);
                intent.putExtra("Type", selectedGame.Type);
                intent.putExtra("Link", selectedGame.Link);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void fillData() {
        DatabaseReference shooters = gamesRef.child("Shooters");
        DatabaseReference horrors = gamesRef.child("Horrors");
        DatabaseReference races = gamesRef.child("Races");
        DatabaseReference simulators = gamesRef.child("Simulators");
        DatabaseReference other = gamesRef.child("Other");

        if(gameType == 1){
            shooters.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    games.clear();
                    for(DataSnapshot gameSnapshot : snapshot.getChildren()){
                        Game game = gameSnapshot.getValue(Game.class);
                        if(game != null){
                            games.add(game);
                            Log.d("GameData", "Name: " + game.Name + ", Type: " + game.Type);
                        } else{
                            Log.e("GameData", "Game object is null");
                        }
                    }
                    boxAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error loading data from Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(gameType == 2){
            horrors.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    games.clear();
                    for(DataSnapshot gameSnapshot : snapshot.getChildren()){
                        Game game = gameSnapshot.getValue(Game.class);
                        if(game != null){
                            games.add(game);
                            Log.d("GameData", "Name: " + game.Name + ", Type: " + game.Type);
                        } else{
                            Log.e("GameData", "Game object is null");
                        }
                    }
                    boxAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error loading data from Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(gameType == 3){
            races.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    games.clear();
                    for(DataSnapshot gameSnapshot : snapshot.getChildren()){
                        Game game = gameSnapshot.getValue(Game.class);
                        if(game != null){
                            games.add(game);
                            Log.d("GameData", "Name: " + game.Name + ", Type: " + game.Type);
                        } else{
                            Log.e("GameData", "Game object is null");
                        }
                    }
                    boxAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error loading data from Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(gameType == 4){
            simulators.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    games.clear();
                    for(DataSnapshot gameSnapshot : snapshot.getChildren()){
                        Game game = gameSnapshot.getValue(Game.class);
                        if(game != null){
                            games.add(game);
                            Log.d("GameData", "Name: " + game.Name + ", Type: " + game.Type);
                        } else{
                            Log.e("GameData", "Game object is null");
                        }
                    }
                    boxAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error loading data from Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(gameType == 5){
            other.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    games.clear();
                    for(DataSnapshot gameSnapshot : snapshot.getChildren()){
                        Game game = gameSnapshot.getValue(Game.class);
                        if(game != null){
                            games.add(game);
                            Log.d("GameData", "Name: " + game.Name + ", Type: " + game.Type);
                        } else{
                            Log.e("GameData", "Game object is null");
                        }
                    }
                    boxAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error loading data from Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}