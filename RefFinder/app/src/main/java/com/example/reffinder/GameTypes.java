package com.example.reffinder;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.reffinder.databinding.FragmentGameTypesBinding;


public class GameTypes extends Fragment {

    FragmentGameTypesBinding binding;


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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout shooters = binding.shooter;
        LinearLayout horrors = binding.horror;
        LinearLayout races = binding.races;
        LinearLayout simulators = binding.simulators;

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
    }


}