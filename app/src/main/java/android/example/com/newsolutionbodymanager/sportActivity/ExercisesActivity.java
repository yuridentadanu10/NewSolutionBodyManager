package android.example.com.newsolutionbodymanager.sportActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.com.newsolutionbodymanager.LoginAndFriend.LoginActivity;
import android.example.com.newsolutionbodymanager.LoginAndFriend.RegisterActivity;
import android.example.com.newsolutionbodymanager.R;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ExercisesActivity extends AppCompatActivity implements View.OnClickListener {
LinearLayout walking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        walking = findViewById(R.id.linear_Walking);
        walking.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_Walking:
                Intent kalo = new Intent(ExercisesActivity.this,SportActivity.class );
                String walking ="walking";
                kalo.putExtra("olahraga",walking);
                startActivity(kalo);

                break;
        }
    }
}
