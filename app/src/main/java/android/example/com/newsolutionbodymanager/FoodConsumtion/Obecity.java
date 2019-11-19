package android.example.com.newsolutionbodymanager.FoodConsumtion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.com.newsolutionbodymanager.MainActivity;
import android.example.com.newsolutionbodymanager.R;
import android.example.com.newsolutionbodymanager.imgProcessing.ScanDoneActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Obecity extends AppCompatActivity implements View.OnClickListener{
    Button btnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obecity);

        btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                startActivity(new Intent(Obecity.this, MainActivity.class));
                break;

        }
    }
}
