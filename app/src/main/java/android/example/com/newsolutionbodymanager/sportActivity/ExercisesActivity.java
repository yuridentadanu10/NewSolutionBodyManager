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
LinearLayout walking,running,cycling,hiking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        walking = findViewById(R.id.linear_Walking);
        running = findViewById(R.id.linear_Running);
        cycling = findViewById(R.id.linear_Cycling);
        hiking = findViewById(R.id.linear_Hiking);
        walking.setOnClickListener(this);
        running.setOnClickListener(this);
        cycling.setOnClickListener(this);
        hiking.setOnClickListener(this);

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
            case R.id.linear_Running:
                Intent kalo1 = new Intent(ExercisesActivity.this,SportActivity.class );
                String walking1 ="running";
                kalo1.putExtra("olahraga",walking1);
                startActivity(kalo1);
                break;
            case R.id.linear_Cycling:
                Intent kalo2 = new Intent(ExercisesActivity.this,SportActivity.class );
                String walking2 ="cycling";
                kalo2.putExtra("olahraga",walking2);
                startActivity(kalo2);
                break;
            case R.id.linear_Hiking:
                Intent kalo3 = new Intent(ExercisesActivity.this,SportActivity.class );
                String walking3 ="hiking";
                kalo3.putExtra("olahraga",walking3);
                startActivity(kalo3);
                break;

        }
    }
}
