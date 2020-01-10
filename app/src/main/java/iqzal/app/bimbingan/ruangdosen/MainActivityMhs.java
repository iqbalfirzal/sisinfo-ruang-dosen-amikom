package iqzal.app.bimbingan.ruangdosen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivityMhs extends AppCompatActivity implements DialogAboutMhs.ItemClickListener{

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mhs);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_mhs);
        bottomNav.setItemIconTintList(null);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        final SavedIdClass globalVariable = (SavedIdClass) getApplicationContext();
        globalVariable.setChatWithName("");
        final String getMyId = globalVariable.getId();
        FirebaseMessaging.getInstance().subscribeToTopic(getMyId);
        auth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_mhs,
                    new ListDosenFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_listdosen_mhs:
                            getSupportFragmentManager().popBackStack();
                            selectedFragment = new ListDosenFragment();
                            break;
                        case R.id.nav_chat_mhs:
                            getSupportFragmentManager().popBackStack();
                            selectedFragment = new ChatMhsFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_mhs,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public void onItemClick() {
        new LoginPrefManager(this).clearData();
        auth.signOut();
        Intent myIntent = new Intent(MainActivityMhs.this,
                Choose.class);
        startActivity(myIntent);
        finish();
    }
}
