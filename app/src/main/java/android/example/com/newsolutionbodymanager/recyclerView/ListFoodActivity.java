package android.example.com.newsolutionbodymanager.recyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.example.com.newsolutionbodymanager.MainActivity;
import android.example.com.newsolutionbodymanager.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ListFoodActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private static final String TAG = "ListFOod";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    private FoodAdapter adapter;
    public static final String MOVIE_ITEM = "film";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    TextView tv_waktuMakan;
    Toolbar mTopToolbar;
    String waktuMakan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Intent i=getIntent();
         waktuMakan=i.getExtras().getString("waktuMakan");


        mTopToolbar = findViewById(R.id.toolbar_support);
        setSupportActionBar(mTopToolbar);

        TextView textView = mTopToolbar.findViewById(R.id.judul_toolbar);
        textView.setText(waktuMakan);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setUpRecyclerView();


    }

    private void setUpRecyclerView() {
        Query query = db.collection("food").orderBy("priority", Query.Direction.ASCENDING);

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
                Intent intent = new Intent(ListFoodActivity.this, DetailActivityFood.class);
                //intent.putExtra("model", model);
                intent.putExtra("jangkrik", id);
                intent.putExtra("waktuMakan",waktuMakan);
                startActivity(intent);
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

