package android.example.com.newsolutionbodymanager.FoodConsumtion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.com.newsolutionbodymanager.R;

import android.example.com.newsolutionbodymanager.recyclerView.ListFoodActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FoodConsumtion extends AppCompatActivity {
ImageView btnList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_consumtion);
        btnList = findViewById(R.id.btn_list_breakfast);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list = new Intent(FoodConsumtion.this, ListFoodActivity.class);
                startActivity(list);
            }
        });
    }



}
