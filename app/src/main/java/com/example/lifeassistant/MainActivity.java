package com.example.lifeassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView userNameTextView;
    private TextView bmiValueTextView;
    private TextView bmiStatusTextView;
    private CircularProgressIndicator bmiIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize TextViews and BMI indicator
        userNameTextView = findViewById(R.id.user_name);
        // Set user name
        updateUserNameDisplay();


        // Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Apply Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true; // Already here
            }
            Intent intent = null;
            if (id == R.id.nav_profile) {
                intent = new Intent(MainActivity.this, UserProfileActivity.class);
            }


            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // <--- smoother transition
                startActivity(intent);
                return true;
            }

            return false;
        });

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the user name display when resuming the activity
        updateUserNameDisplay();
        // Update BMI display when returning from BMI calculator

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }




    private void updateUserNameDisplay() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            // If display name is available, use it; otherwise use email or a default text
            if (displayName != null && !displayName.isEmpty()) {
                userNameTextView.setText(displayName);
            } else if (currentUser.getEmail() != null) {
                // Use email up to @ symbol as fallback
                String email = currentUser.getEmail();
                int atIndex = email.indexOf('@');
                if (atIndex > 0) {
                    userNameTextView.setText(email.substring(0, atIndex));
                } else {
                    userNameTextView.setText(email);
                }
            } else {
                // Default text if neither display name nor email is available
                userNameTextView.setText("User");
            }
        } else {
            // YE HONA NAI CHAIYE BAS ADDITIONAL CHECK RKHA WA TO BE SURE
            // wapis login screen pr jaega
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu (this will add items to the action bar if it is present)
        getMenuInflater().inflate(R.menu.top_header_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            // Handle Logout Action
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}