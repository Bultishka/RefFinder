package com.example.reffinder;

import android.app.LocaleManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.reffinder.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Locale;
import java.util.Objects;

import models.User;

public class Register extends Fragment {

    FragmentRegisterBinding binding;

    public Register() {
        // Required empty public constructor
    }

    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
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
        binding = FragmentRegisterBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Login activity = (Login) getActivity();

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.mailEdt.getText().toString();
                String password = binding.passwordEdt.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()){
                    activity.auth.createUserWithEmailAndPassword(email, password).
                            addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    boolean admin = false;
                                    User user = new User(email, password, admin);
                                    activity.users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).
                                            getUid()).setValue(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getActivity(), "Добро пожаловать!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    intent.putExtra("email", email);
                                                    intent.putExtra("isAdmin", admin);
                                                    startActivity(intent);
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(getView(), e.getLocalizedMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.loadLoging();
            }
        });
    }
}