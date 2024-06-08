package com.example.reffinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.reffinder.databinding.ActivityLoginBinding;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    FragmentTransaction fTrans;
    Log logPage;
    Register regPage;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    private static final int CONTENT_VIEW_ID = 10101010;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        binding.interactionPlace.setId(CONTENT_VIEW_ID);
        logPage = new Log();
        regPage = new Register();
        if (savedInstanceState == null){
            fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.add(CONTENT_VIEW_ID, logPage);
            fTrans.commit();
        }
    }

    public void loadRegister(){
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(CONTENT_VIEW_ID, regPage);
        fTrans.commit();
    }
    public void loadLoging(){
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(CONTENT_VIEW_ID, logPage);
        fTrans.commit();
    }
}