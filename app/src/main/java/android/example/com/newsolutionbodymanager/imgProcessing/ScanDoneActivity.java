package android.example.com.newsolutionbodymanager.imgProcessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.com.newsolutionbodymanager.R;
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
        final Long calorie = i.getExtras().getLong("calorie");
        foodName = findViewById(R.id.tv_food_name);
        foodName.setText(food);
        calorieInfo = findViewById(R.id.tv_calorie);
    }

    @Override
    public void onClick(View view) {

    }
}
