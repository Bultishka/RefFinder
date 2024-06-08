package com.example.reffinder;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.reffinder.databinding.FragmentGameTypesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameTypes extends Fragment implements GameInfoCallback{

    FragmentGameTypesBinding binding;
    private Handler handler = new Handler(Looper.getMainLooper());


    DatabaseReference db;

    public GameTypes() {
    }

    public static GameTypes newInstance(String param1, String param2) {
        GameTypes fragment = new GameTypes();
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
        binding = FragmentGameTypesBinding.inflate(getLayoutInflater());
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(curUser.getUid());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Boolean isAdmin = dataSnapshot.child("admin").getValue(Boolean.class);
                    if (isAdmin != null && isAdmin) {
                        binding.btnAddGame.setVisibility(View.VISIBLE);
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout shooters = binding.shooter;
        LinearLayout horrors = binding.horror;
        LinearLayout races = binding.races;
        LinearLayout simulators = binding.simulators;
        LinearLayout other = binding.other;

        MainActivity activity = (MainActivity) getActivity();

        shooters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.loadList(1);
            }
        });

        horrors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.loadList(2);
            }
        });

        races.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.loadList(3);
            }
        });

        simulators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.loadList(4);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.loadList(5);
            }
        });

        binding.btnAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Добавить игру");

                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_add_game, null);
                builder.setView(dialogView);

                EditText editTextUrl = dialogView.findViewById(R.id.steam_link);

                builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = editTextUrl.getText().toString().trim();
                        if (!url.isEmpty()) {
                            try {
                                Spinner spinner = dialogView.findViewById(R.id.spinner_genre);
                                int selectedGenre = spinner.getSelectedItemPosition();
                                SteamGameScrapper gamedata = new SteamGameScrapper(editTextUrl.getText().toString(), GameTypes.this, selectedGenre);
                                gamedata.extractGameInfo();
                            } catch (Exception e) {
                                Toast.makeText(requireContext(), "Введите корректную ссылку", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Введите ссылку", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    @Override
    public void onGameFound(String title, String releaseDate, String logoUrl, int genre, String url) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String type = "";
                String tempType = "";
                switch (genre){
                    case 0:
                    {
                        tempType = "Other";
                        type = "Остальное";
                        break;
                    }
                    case 1:
                    {
                        tempType = "Shooters";
                        type = "Шутер";
                        break;
                    }
                    case 2:
                    {
                        tempType = "Horrors";
                        type = "Ужастик";
                        break;
                    }
                    case 3:
                    {
                        tempType = "Races";
                        type = "Гонка";
                        break;
                    }
                    case 4:
                    {
                        tempType = "Simulators";
                        type = "Симулятор";
                        break;
                    }
                }
                Game newGame = new Game(title, releaseDate, type, url, logoUrl);
                db = FirebaseDatabase.getInstance().getReference().child("Games").child(tempType).child(title);
                db.setValue(newGame);
                Toast.makeText(requireContext(), title + " добавлена в базу данных!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onGameNotFound(String error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}