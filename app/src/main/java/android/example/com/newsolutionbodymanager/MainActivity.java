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
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_FRAGMENT = "fragment";
    private Fragment pageContent = new DashboardFragment();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChipNavigationBar mBottomNav=findViewById(R.id.navbottom);
        mBottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener(){


            @Override
            public void onItemSelected(int i) {
                switch (i) {
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
