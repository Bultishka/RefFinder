package com.example.reffinder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.reffinder.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Toolbar toolbar;

    FragmentTransaction fTrans;

    GameTypes gameTypesFrag;
    GameList gameList;

    private static final int CONTENT_VIEW_ID = 10101010;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        binding.fragmentPlace.setId(CONTENT_VIEW_ID);
        gameTypesFrag = new GameTypes();
        if (savedInstanceState == null){
            fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.add(CONTENT_VIEW_ID, gameTypesFrag);
            fTrans.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_account, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.about_author) {
            showAlertDialogWithIcon(R.string.headerAuthor, R.string.txt_author);
            return true;
        }
        else if (id == R.id.about_app) {
            showAlertDialog(R.string.headerApp, R.string.txt_about);
            return true;
        }
        else if (id == R.id.account_btn) {
            startActivityAfterCleanup(Cabinet.class);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void showAlertDialog(int titleResId, int messageResId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleResId)
                .setMessage(messageResId)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void startActivityAfterCleanup(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.putExtra("email", getIntent().getStringExtra("email"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); startActivity(intent);
    }

    private void showAlertDialogWithIcon(int titleResId, int messageResId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleResId)
                .setMessage(messageResId)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setIcon(R.drawable.baseline_adb_24)
                .show();
    }

    public void loadList(int list_type){
        fTrans = getSupportFragmentManager().beginTransaction();
        gameList = new GameList();
        gameList.gameType = list_type;
        fTrans.replace(CONTENT_VIEW_ID, gameList);
        fTrans.commit();
    }

    public void loadTypes(){
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(CONTENT_VIEW_ID, gameTypesFrag);
        fTrans.commit();
    }
}