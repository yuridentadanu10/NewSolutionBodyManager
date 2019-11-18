package android.example.com.newsolutionbodymanager.bottomNav;


import android.content.Intent;
import android.example.com.newsolutionbodymanager.LoginAndFriend.LoginActivity;
import android.example.com.newsolutionbodymanager.R;
import android.example.com.newsolutionbodymanager.history.FoodHistory;
import android.example.com.newsolutionbodymanager.history.HistoryFoodAdapter;
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
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Button btnchangeplan;
    Toolbar mTopToolbar;

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
                    Long dailyGoal = snapshot.getLong("dailyCalorieGoal");

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });


    }

    private void setUpRecyclerView(View view) {
        String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = db.collection("users").document(uid).collection("historiMakanan").orderBy("name").limit(3);

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

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        readDatabase();

    }

    @Override
    public void onStop() {
        super.onStop();
        
    }
}
