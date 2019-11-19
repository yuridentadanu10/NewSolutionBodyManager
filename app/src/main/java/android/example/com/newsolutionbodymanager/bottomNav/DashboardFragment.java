package android.example.com.newsolutionbodymanager.bottomNav;


import android.content.Intent;
import android.example.com.newsolutionbodymanager.R;
import android.example.com.newsolutionbodymanager.recyclerView.ListFoodActivity;
import android.example.com.newsolutionbodymanager.sportActivity.ExercisesActivity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener{
private TextView tvTanggal,tvCalorieConsumed,tv_caloriBurn,tvCurrentWeight,tvWeightGoal,tvDailyCalorie_goal,tv_daily_calorie_breakfast,tv_daily_calorie_lunch,tv_daily_calorie_dinner,tv_daily_calorie_snack,tv_daily_calorie_burned;
private FancyButton btnwalking;
private FancyButton btnEdit,btn_addBreakfast,btn_addLunch,btn_addDinner,btn_addSnack;

    CircularProgressBar circularProgressBar;

    private static final String TAG = "Dashboard";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
    DocumentReference userRef ;
    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.new_fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_caloriBurn = view.findViewById(R.id.tv_calorie_burned);
        tvTanggal = view.findViewById(R.id.tv_date);
        btnEdit = view.findViewById(R.id.btn_edit_data_goal);
        tvCalorieConsumed = view.findViewById(R.id.tv_consumed_calori);
        tvCurrentWeight = view.findViewById(R.id.tv_current_weight);
        tvWeightGoal= view.findViewById(R.id.tv_weight_goal);
        tvDailyCalorie_goal= view.findViewById(R.id.tv_daily_calorie);
        btnwalking = view.findViewById(R.id.btn_walking);
        btn_addBreakfast = view.findViewById(R.id.btn_add_breakfast);
        btn_addLunch = view.findViewById(R.id.btn_add_lunch);
        btn_addDinner= view.findViewById(R.id.btn_add_dinner);
        btn_addSnack= view.findViewById(R.id.btn_add_snack);
        tv_daily_calorie_breakfast= view.findViewById(R.id.tv_daily_calorie_breakfast);
        tv_daily_calorie_dinner= view.findViewById(R.id.tv_daily_calorie_dinner);
        tv_daily_calorie_lunch= view.findViewById(R.id.tv_daily_calorie_lunch);
        tv_daily_calorie_burned=view.findViewById(R.id.tv_daily_calorie_burned);
        tv_daily_calorie_snack= view.findViewById(R.id.tv_daily_calorie_snack);
        btn_addBreakfast.setOnClickListener(this);
        btn_addLunch.setOnClickListener(this);
        btn_addDinner.setOnClickListener(this);
        btnwalking.setOnClickListener(this);
        btn_addSnack.setOnClickListener(this);
        circularProgressBar = view.findViewById(R.id.custom_progressbar);
    }


    @Override
    public void onStart() {
        super.onStart();

         userRef = db.collection("users").document(uid);
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


                    final Long dailyCalorie = snapshot.getLong("dailyCalorie");
                    final Long calorieConsumed = snapshot.getLong("consumedCalorie");
                    Long calorieBurned = snapshot.getLong("burnedCalorie");
                    Long currentWeight = snapshot.getLong("weight");
                    Long weightGoal = snapshot.getLong("weightGoal");
                    final Long dailyGoal = snapshot.getLong("dailyCalorieGoal");
                    final Long breakfast = snapshot.getLong("Breakfast");
                    final Long lunch = snapshot.getLong("Lunch");
                    final Long dinner = snapshot.getLong("Dinner");
                    final Long snack = snapshot.getLong("Snack");

                    circularProgressBar.setProgress(dailyCalorie);
                    circularProgressBar.setProgressMax(dailyGoal);
                    tvDailyCalorie_goal.setText(String.valueOf(dailyCalorie)+" / "+String.valueOf(dailyGoal));
                    tvCalorieConsumed.setText(String.valueOf(calorieConsumed)+" cal");
                    tv_caloriBurn.setText(String.valueOf(calorieBurned)+" cal");
                    tvCurrentWeight.setText(String.valueOf(currentWeight)+" kg");
                    tvWeightGoal.setText(String.valueOf(weightGoal)+" kg");
                    tv_daily_calorie_breakfast.setText("Today : "+ String.valueOf(breakfast)+" Cal");
                    tv_daily_calorie_lunch.setText("Today : "+ String.valueOf(lunch)+" Cal");
                    tv_daily_calorie_dinner.setText("Today : "+ String.valueOf(dinner)+" Cal");
                    tv_daily_calorie_snack.setText("Today : "+ String.valueOf(snack)+" Cal");
                    tv_daily_calorie_burned.setText("Today Exercises : "+ String.valueOf(calorieBurned)+" Cal");

                    if(dailyCalorie>=dailyGoal){
                        circularProgressBar.setProgressBarColor(Color.rgb(215,122,55));
                    }
                    else {
                        circularProgressBar.setProgressBarColor(Color.rgb(255,255,255));
                    }


                    db.runTransaction(new Transaction.Function<Double>() {
                        @Override
                        public Double apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(userRef);
                            //new
                            double kalorikonsum = snapshot.getDouble("consumedCalorie") ;

                            double totalCalorie = breakfast+lunch+dinner+snack;
                            transaction.update(userRef, "consumedCalorie", totalCalorie);
                            return totalCalorie;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Double>() {
                        @Override
                        public void onSuccess(Double result) {
                            dailyCalorie();
                            Log.d(TAG, "Transaction success: " + result);

                            if(dailyCalorie>=dailyGoal){
                                circularProgressBar.setProgressBarColor(Color.rgb(215,122,55));
                            }
                            else {
                                circularProgressBar.setProgressBarColor(Color.rgb(255,255,255));
                            }

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Transaction failure.", e);
                                }
                            });

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }



    private void dailyCalorie(){

        db.runTransaction(new Transaction.Function<Double>() {
            @Override
            public Double apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(userRef);
                double kaloriburned = snapshot.getDouble("burnedCalorie");

                //new
                double kalorikonsum = snapshot.getDouble("consumedCalorie") ;



                double total =  kalorikonsum-kaloriburned;

                transaction.update(userRef, "dailyCalorie", total);
                return total;
            }
        }).addOnSuccessListener(new OnSuccessListener<Double>() {
            @Override
            public void onSuccess(Double result) {
                Log.d(TAG, "Transaction success: " + result);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_breakfast:

                Intent intent = new Intent(getActivity(), ListFoodActivity.class);
                //intent.putExtra("model", model);
                intent.putExtra("waktuMakan","Breakfast");
                startActivity(intent);
                break;
            case R.id.btn_add_dinner:
                Intent intent1 = new Intent(getActivity(), ListFoodActivity.class);
                //intent.putExtra("model", model);
                intent1.putExtra("waktuMakan","Dinner");
                startActivity(intent1);
                break;
            case R.id.btn_add_lunch:
                Intent intent2 = new Intent(getActivity(), ListFoodActivity.class);
                //intent.putExtra("model", model);
                intent2.putExtra("waktuMakan","Lunch");
                startActivity(intent2);
                break;
            case R.id.btn_add_snack:
                Intent intent3 = new Intent(getActivity(), ListFoodActivity.class);
                //intent.putExtra("model", model);
                intent3.putExtra("waktuMakan","Snack");
                startActivity(intent3);
                break;
            case R.id.btn_walking:
                startActivity(new Intent(getActivity(), ExercisesActivity.class));
                break;


        }
    }
}
