package android.example.com.newsolutionbodymanager.bottomNav;


import android.content.Intent;
import android.example.com.newsolutionbodymanager.LoginAndFriend.LoginActivity;
import android.example.com.newsolutionbodymanager.R;
import android.example.com.newsolutionbodymanager.history.FoodHistory;
import android.example.com.newsolutionbodymanager.history.HistoryFoodAdapter;
import android.example.com.newsolutionbodymanager.history.HistorySportAdapter;
import android.example.com.newsolutionbodymanager.history.SportHistory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileDashboard";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    private HistoryFoodAdapter adapter;
    private HistorySportAdapter adapter2;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    Button btnchangeplan;
    Toolbar mTopToolbar;
    TextView tv_profile_name,tv_profile_age,tv_profile_height,tv_profile_weight,tv_daily_calorie,tv_daily_calorie_burned;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.new_fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_profile_name = view.findViewById(R.id.tv_profile_name);
        tv_profile_age = view.findViewById(R.id.tv_profile_age);
        tv_profile_weight = view.findViewById(R.id.tv_profile_weight);
        tv_profile_height = view.findViewById(R.id.tv_profile_height);
        tv_daily_calorie = view.findViewById(R.id.tv_daily_calorie);
        tv_daily_calorie_burned = view.findViewById(R.id.tv_daily_calorie_burned);

    btnchangeplan=view.findViewById(R.id.btnChangePlans);
    btnchangeplan.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mAuth.getInstance().signOut();
            Intent login = new Intent(getActivity(), LoginActivity.class);
            startActivity(login);

        }
    });

        setUpRecyclerView(view);
        setUpRecyclerViewOlahraga(view);
    }

    private void readDatabase() {
        final String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DocumentReference userRef = db.collection("users").document(uid);
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());


                    Long dailyCalorie = snapshot.getLong("dailyCalorie");
                    Long burned = snapshot.getLong("burnedCalorie");
                    String nama = snapshot.getString("name");
                    Long age = snapshot.getLong("age");
                    Long height = snapshot.getLong("height");
                    Long weight = snapshot.getLong("weight");

                    tv_profile_name.setText(nama);
                    tv_profile_age.setText(String.valueOf(age));
                    tv_profile_weight.setText(String.valueOf(weight));
                    tv_profile_height.setText(String.valueOf(height));
                    tv_daily_calorie.setText("+ "+String.valueOf(dailyCalorie)+" Calories");
                    tv_daily_calorie_burned.setText("- "+String.valueOf(burned)+" Calories");

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });


    }

    private void setUpRecyclerView(View view) {
        String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = db.collection("users").document(uid).collection("historiMakanan").orderBy("name").limit(10);

        FirestoreRecyclerOptions<FoodHistory> options = new FirestoreRecyclerOptions.Builder<FoodHistory>()
                .setQuery(query, FoodHistory.class)
                .build();

        adapter = new HistoryFoodAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.rv_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new HistoryFoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {
            }
        });


    }
    private void setUpRecyclerViewOlahraga(View view) {
        String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = db.collection("users").document(uid).collection("historiOlahraga").orderBy("name").limit(10);

        FirestoreRecyclerOptions<SportHistory> options = new FirestoreRecyclerOptions.Builder<SportHistory>()
                .setQuery(query, SportHistory.class)
                .build();

        adapter2 = new HistorySportAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.rv_history1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter2);


        adapter2.setOnItemClickListener(new HistorySportAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter2.startListening();
        readDatabase();

    }

    @Override
    public void onStop() {
        super.onStop();
        
    }
}
