package iqzal.app.bimbingan.ruangdosen;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityDosen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dosen);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_dosen);
        bottomNav.setItemIconTintList(null);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        final SavedIdClass globalVariable = (SavedIdClass) getApplicationContext();
        globalVariable.setChatWithName("");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_dosen,
                    new DashDosenFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_dash_dosen:
                            selectedFragment = new DashDosenFragment();
                            break;
                        case R.id.nav_chat_dosen:
                            selectedFragment = new ChatDosenFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_dosen,
                            selectedFragment).commit();

                    return true;
                }
            };
}
