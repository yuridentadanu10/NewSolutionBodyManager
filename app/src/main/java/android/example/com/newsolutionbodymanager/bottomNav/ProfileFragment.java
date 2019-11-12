package android.example.com.newsolutionbodymanager.bottomNav;


import android.content.Intent;
import android.example.com.newsolutionbodymanager.LoginAndFriend.LoginActivity;
import android.example.com.newsolutionbodymanager.R;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

Button btnchangeplan;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
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
    }
}
