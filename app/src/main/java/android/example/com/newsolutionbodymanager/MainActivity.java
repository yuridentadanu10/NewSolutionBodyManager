package android.example.com.newsolutionbodymanager;


import android.example.com.newsolutionbodymanager.bottomNav.DashboardFragment;
import android.example.com.newsolutionbodymanager.bottomNav.ProfileFragment;
import android.example.com.newsolutionbodymanager.imgProcessing.ScannerFragment;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_FRAGMENT = "fragment";
    private Fragment pageContent = new DashboardFragment();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView mBottomNav=findViewById(R.id.navbottom);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        pageContent = new DashboardFragment();
                        break;
                    case R.id.scanner:
                        pageContent = new ScannerFragment();
                        break;
                    case R.id.profile:
                        pageContent = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.Layout_frame, pageContent).commit();
                return true;
            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Layout_frame, pageContent).commit();
        }
        else {
            pageContent = getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT);
            getSupportFragmentManager().beginTransaction().replace(R.id.Layout_frame, pageContent).commit();
        }
    }

}
