package android.example.com.newsolutionbodymanager.recyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.example.com.newsolutionbodymanager.FoodConsumtion.Obecity;
import android.example.com.newsolutionbodymanager.MainActivity;
import android.example.com.newsolutionbodymanager.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DetailActivityFood extends AppCompatActivity implements View.OnClickListener{
TextView foodName,calorieInfo,carbsInfo,proteinInfo,fatInfo;
Button btnAddDetail;
ProgressBar progressBar;
ImageView imgFood;
    String waktuMakan;
    Toolbar mTopToolbar;
    String img;
    private static final String TAG = "DetailFoodAct";

private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth =FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        calorieInfo = findViewById(R.id.tv_calorie_info);
        carbsInfo = findViewById(R.id.carbo_info);
        proteinInfo = findViewById(R.id.tv_protein_info);
        fatInfo = findViewById(R.id.tv_fat_info);
        btnAddDetail= findViewById(R.id.btn_add_detail_food);
        imgFood = findViewById(R.id.img_food);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);


        btnAddDetail.setOnClickListener(this);
        //RECEIVE OUR DATA
        Intent i=getIntent();
        final String food=i.getExtras().getString("jangkrik");
         waktuMakan = i.getExtras().getString("waktuMakan");
        foodName = findViewById(R.id.foodName);
        foodName.setText(food);

        mTopToolbar = findViewById(R.id.toolbar_support);
        setSupportActionBar(mTopToolbar);

        TextView textView = mTopToolbar.findViewById(R.id.judul_toolbar);
        textView.setText(food);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();

        cekCalorie();

        Intent i=getIntent();
        final String food=i.getExtras().getString("jangkrik");

        DocumentReference docRef = db.collection("food").document(food);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Long calorie = document.getLong("calorie");
                        Long carbs = document.getLong("carbs");
                        Long protein = document.getLong("protein");
                        Long fat = document.getLong("fat");

                         img = document.getString("imageUrl");

                        calorieInfo.setText( String.valueOf(calorie)+" Calories");
                        carbsInfo.setText(String.valueOf(carbs)+"% Carbohidrate");
                        proteinInfo.setText(String.valueOf(protein)+"% Protein");
                        fatInfo.setText(String.valueOf(fat)+"% Fat");
                        Picasso.get()
                                .load(img)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(imgFood);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }


    private void AddFood(){
        final String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent i=getIntent();
        final String food=i.getExtras().getString("jangkrik");

        final DocumentReference dt = db.collection("users").document(uid);
        final DocumentReference docRef = db.collection("food").document(food);


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        final double calorie = document.getDouble("calorie");
                        db.runTransaction(new Transaction.Function<Double>() {
                            @Override
                            public Double apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                                DocumentSnapshot dashboard = transaction.get(dt);

                                double caloriedaily = dashboard.getDouble("consumedCalorie")+calorie;
                                //new
                                double dailyWaktumakan = dashboard.getDouble(waktuMakan)+calorie;
                                //new

                                transaction.update(dt, waktuMakan, dailyWaktumakan);

                                return dailyWaktumakan;
                                //new
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Double>() {
                            @Override
                            public void onSuccess(Double result) {
                                Log.d(TAG, "WOIRRRRRRRRRRRRRRRRRRRRR "+result);
                                progressBar.setVisibility(View.GONE);
                                Intent tent = new Intent(DetailActivityFood.this,MainActivity.class);
                                startActivity(tent);
                                writeHistory(calorie,food,img,waktuMakan);

                            }
                        });
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });




    }

    private void cekCalorie(){
        final String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Long daily = document.getLong("dailyCalorie");
                        Long goal = document.getLong("dailyCalorieGoal");

                        if(daily >= goal) {
                            Intent obecity = new Intent(DetailActivityFood.this, Obecity.class);
                            startActivity(obecity);
                        }

                        Log.d(TAG, "OBECITY CHECKER BERHASIL");

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


private void writeHistory(double calorie,String nama,String imgUrl,String waktuMakan){

    Map<String, Object> history = new HashMap<>();
    history.put("name", nama);
    history.put("calorie", calorie);
    history.put("imageUrl",imgUrl);
    history.put("waktuMakan",waktuMakan);
    String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
    db.collection("users")
            .document(uid).collection("historiMakanan").document()
            .set(history)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing document", e);
                }
            });

}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_detail_food:
                progressBar.setVisibility(View.VISIBLE);
                AddFood();

                break;

        }
    }
}
