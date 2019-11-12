package android.example.com.newsolutionbodymanager.bottomNav;


import android.example.com.newsolutionbodymanager.R;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener{
private TextView tvTanggal,tvCalorieConsumed,tv_caloriBurn,tvCurrentWeight,tvWeightGoal,tvDailyCalorie;
private ImageView btnwalking,btn_running,btnCycling,btnhiking;
private TextView btnEdit,btn_addBreakfast,btn_addLunch,btn_addDinner;

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
        tvWeightGoal= view.findViewById(R.id.tv_weight_goal);
        tvDailyCalorie= view.findViewById(R.id.tv_daily_calorie);
        btnwalking = view.findViewById(R.id.btn_walking);
        btn_running  = view.findViewById(R.id.btn_running);
        btnCycling = view.findViewById(R.id.btn_cycling);
        btnhiking = view.findViewById(R.id.btnhiking);
        btn_addBreakfast = view.findViewById(R.id.btn_add_breakfast);
        btn_addLunch = view.findViewById(R.id.btn_add_lunch);
        btn_addDinner= view.findViewById(R.id.btn_add_dinner);


    }


    @Override
    public void onStart() {
        super.onStart();
    }



    @Override
    public void onClick(View view) {

    }
}
