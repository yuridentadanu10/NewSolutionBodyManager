package android.example.com.newsolutionbodymanager.recyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.com.newsolutionbodymanager.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

public class ListFoodActivity extends AppCompatActivity {

    private static final String TAG = "ListFOod";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    private FoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = db.collection("food").orderBy("priority", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Food> options = new FirestoreRecyclerOptions.Builder<Food>()
                .setQuery(query, Food.class)
                .build();

        adapter = new FoodAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Toast.makeText(ListFoodActivity.this,
                        "Position: " +position + " ID: " +id, Toast.LENGTH_SHORT).show();
                final String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
               final DocumentReference dt = db.collection("users").document(uid);
                final DocumentReference dx = db.collection("food").document(id);
                db.runTransaction(new Transaction.Function<Double>() {
                    @Override
                    public Double apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                        DocumentSnapshot dashboard = transaction.get(dt);
                        DocumentSnapshot food = transaction.get(dx);
                        double calorie = food.getDouble("calorie");
                        double caloriedaily = dashboard.getDouble("dailyCalorie")+calorie;

                            transaction.update(dt, "dailyCalorie", caloriedaily);

                        return caloriedaily;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Double>() {
                    @Override
                    public void onSuccess(Double result) {
                        Log.d(TAG, "WOIRRRRRRRRRRRRRRRRRRRRR "+result);

                    }
                });
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

