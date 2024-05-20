package com.example.reffinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.reffinder.databinding.ActivityGamePageBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GamePage extends AppCompatActivity {

    private DatabaseReference gamesRef;
    private DatabaseReference game;
    int gameType;
    Toolbar toolbar;
    boolean admin = false;

    ActivityGamePageBinding binding;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gamesRef = FirebaseDatabase.getInstance().getReference().child("Games");
        binding = ActivityGamePageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        progressBar = findViewById(R.id.progressBar);
        showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoading();
            }
        }, 3000);

        Intent intent = getIntent();
        if (intent != null){
            gameType = intent.getIntExtra("Folder", 0);
            binding.tvName.setText(intent.getStringExtra("Name"));
            binding.tvType.setText("Жанр: " + intent.getStringExtra("Type"));
            binding.tvDesciption.setText(intent.getStringExtra("Description"));
            binding.tvLink.loadUrl(intent.getStringExtra("Link"));
            binding.tvLink.getSettings().setJavaScriptEnabled(true);
            binding.tvLink.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
                    if (request.getUrl().toString().equals(intent.getStringExtra("Link"))) {
                        return false;
                    } else {
                        return true;
                    }
                }
            });

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageFolderRef = storageRef.child(intent.getStringExtra("Name"));
            StorageReference imageRef = imageFolderRef.child("poster.png");

            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageUrl = uri.toString();
                    Glide.with(getBaseContext()).load(imageUrl).into(binding.ivPoster);
                }
            });
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    admin = snapshot.child("admin").getValue(Boolean.class);
                    if (admin) {
                        binding.changeDescr.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        binding.changeDescr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View editDescriptionView = getLayoutInflater().inflate(R.layout.edit_description_layout, null);
                EditText editDescriptionEditText = editDescriptionView.findViewById(R.id.edit_description);
                editDescriptionEditText.setText(binding.tvDesciption.getText().toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(GamePage.this);
                builder.setView(editDescriptionView);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button saveDescriptionBtn = editDescriptionView.findViewById(R.id.save_description_btn);
                saveDescriptionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newDescription = editDescriptionEditText.getText().toString();
                        binding.tvDesciption.setText(newDescription);
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db = db.child("Games");
                        if (gameType == 1){
                            db = db.child("Shooters");
                        } else if (gameType == 2) {
                            db = db.child("Horrors");
                        } else if (gameType == 3) {
                            db = db.child("Races");
                        } else if (gameType == 4) {
                            db = db.child("Simulator");
                        }
                        db = db.child(binding.tvName.getText().toString());
                        db.child("Description").setValue(newDescription);
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
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
        } else if (id == android.R.id.home) {
            onBackPressed();
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
}