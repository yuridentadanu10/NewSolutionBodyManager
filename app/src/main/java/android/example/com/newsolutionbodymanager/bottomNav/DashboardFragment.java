package android.example.com.newsolutionbodymanager.bottomNav;


import android.content.Intent;
import android.example.com.newsolutionbodymanager.FoodConsumtion.FoodConsumtion;
import android.example.com.newsolutionbodymanager.LoginAndFriend.LoginActivity;
import android.example.com.newsolutionbodymanager.LoginAndFriend.RegisterActivity;
import android.example.com.newsolutionbodymanager.MainActivity;
import android.example.com.newsolutionbodymanager.R;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener{
private TextView tvTanggal,tvCalorieConsumed,tv_caloriBurn,tvCurrentWeight,tvWeightGoal,tvDailyCalorie,tvCalorieGoal;
private ImageView btnwalking,btn_running,btnCycling,btnhiking;
private TextView btnEdit,btn_addBreakfast,btn_addLunch,btn_addDinner;

    private static final String TAG = "Dashboard";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_caloriBurn = view.findViewById(R.id.tv_calorie_burned);
        tvTanggal = view.findViewById(R.id.tv_date);
        btnEdit = view.findViewById(R.id.btn_edit_data_goal);
        tvCalorieConsumed = view.findViewById(R.id.tv_consumed_calori);
        tvCurrentWeight = view.findViewById(R.id.tv_current_weight);
        tvCalorieGoal = view.findViewById(R.id.tv_calorie_goal);
        tvWeightGoal= view.findViewById(R.id.tv_weight_goal);
        tvDailyCalorie= view.findViewById(R.id.tv_daily_calorie);
        btnwalking = view.findViewById(R.id.btn_walking);
        btn_running  = view.findViewById(R.id.btn_running);
        btnCycling = view.findViewById(R.id.btn_cycling);
        btnhiking = view.findViewById(R.id.btnhiking);
        btn_addBreakfast = view.findViewById(R.id.btn_add_breakfast);
        btn_addLunch = view.findViewById(R.id.btn_add_lunch);
        btn_addDinner= view.findViewById(R.id.btn_add_dinner);

        btn_addBreakfast.setOnClickListener(this);
        btn_addLunch.setOnClickListener(this);
        btn_addDinner.setOnClickListener(this);

    }


    @Override
    public void onStart() {
        super.onStart();

        final    DocumentReference userRef = db.collection("users").document(uid);
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    double dailyCalorieGoal = snapshot.getDouble("dailyCalorieGoal");
                    double dailyCalorie = snapshot.getDouble("dailyCalorie");
                    double calorieConsumed = snapshot.getDouble("consumedCalorie");
                    double calorieBurned = snapshot.getDouble("burnedCalorie");
                    double currentWeight = snapshot.getDouble("weight");
                    double weightGoal = snapshot.getDouble("weightGoal");

                    tvCalorieGoal.setText(String.valueOf(dailyCalorieGoal));
                    tvDailyCalorie.setText(String.valueOf(dailyCalorie));
                    tvCalorieConsumed.setText(String.valueOf(calorieConsumed));
                    tv_caloriBurn.setText(String.valueOf(calorieBurned));
                    tvCurrentWeight.setText(String.valueOf(currentWeight));
                    tvWeightGoal.setText(String.valueOf(weightGoal));
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_breakfast:
                startActivity(new Intent(getActivity(), FoodConsumtion.class));
                break;
            case R.id.btn_add_dinner:
                startActivity(new Intent(getActivity(), FoodConsumtion.class));
                break;
            case R.id.btn_add_lunch:
                startActivity(new Intent(getActivity(), FoodConsumtion.class));
                break;

        }
    }
}
