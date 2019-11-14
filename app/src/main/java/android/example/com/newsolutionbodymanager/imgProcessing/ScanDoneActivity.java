package android.example.com.newsolutionbodymanager.imgProcessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.com.newsolutionbodymanager.LoginAndFriend.LoginActivity;
import android.example.com.newsolutionbodymanager.LoginAndFriend.RegisterActivity;
import android.example.com.newsolutionbodymanager.MainActivity;
import android.example.com.newsolutionbodymanager.R;
import android.example.com.newsolutionbodymanager.bottomNav.DashboardFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScanDoneActivity extends AppCompatActivity implements View.OnClickListener{
Button btnConfirm;
    TextView foodName,calorieInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_done);



        //RECEIVE OUR DATA
        Intent i=getIntent();
        final String food=i.getExtras().getString("foodName");
        final Double calorie = i.getExtras().getDouble("calorie");
        foodName = findViewById(R.id.tv_food_name);
        foodName.setText(food);
        calorieInfo = findViewById(R.id.tv_calorie);
        calorieInfo.setText(String.valueOf(calorie)+" Calories");

        btnConfirm = findViewById(R.id.btn_confirm);

        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                startActivity(new Intent(ScanDoneActivity.this, MainActivity.class));
                break;

        }
    }
}
