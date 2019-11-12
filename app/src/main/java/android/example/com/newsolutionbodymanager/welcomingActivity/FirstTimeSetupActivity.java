package android.example.com.newsolutionbodymanager.welcomingActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.com.newsolutionbodymanager.MainActivity;
import android.example.com.newsolutionbodymanager.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;


public class FirstTimeSetupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    Button btnSubmit;
    EditText etAge,etHeight,etWeight,etWeightGoal;
    Object item;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private static final String TAG = "firsttimesetup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_data_activity1);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btnSubmit = findViewById(R.id.btnSubmitAdditionalData);
        etAge = findViewById(R.id.et_age);
        etHeight = findViewById(R.id.et_height);
        etWeight =findViewById(R.id.et_weight);
        etWeightGoal =findViewById(R.id.et_weightGoal);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.kelamin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void AddData() {

        int age = Integer.parseInt( etAge.getText().toString() );
        String kelamin = item.toString();
        int dailygoal = 0;
        int height = Integer.parseInt( etHeight.getText().toString() );
        int weight = Integer.parseInt( etWeight.getText().toString() );
        int weightGoal = Integer.parseInt( etWeightGoal.getText().toString() );
        final String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "Your ID : " +uid);
        UserAdditionalData user = new UserAdditionalData(kelamin ,age, height,weight,weightGoal,dailygoal);
        db.collection("users")
                .document(uid)
                .set(user, SetOptions.merge())
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

        db.runTransaction(new Transaction.Function<Double>() {
            @Override
            public Double apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference exampleNoteRef = db.collection("users").document(uid);
                DocumentSnapshot exampleNoteSnapshot = transaction.get(exampleNoteRef);
                String kelamin = exampleNoteSnapshot.getString("kelamin");
                double totalcalorie = 0;

                if (kelamin.equals("Male")) {
                    double umur = exampleNoteSnapshot.getDouble("age")*6.8;
                    double berat = exampleNoteSnapshot.getDouble("weight")*13.7;
                    double tinggi = exampleNoteSnapshot.getDouble("height")*5;
                    totalcalorie = 66 + berat +tinggi-umur;
                    transaction.update(exampleNoteRef, "dailyCalorieGoal", totalcalorie);
                }
                if(kelamin.equals("Female")){
                    double umur = exampleNoteSnapshot.getDouble("age")*4.7;
                    double berat = exampleNoteSnapshot.getDouble("weight")*9.6;
                    double tinggi = exampleNoteSnapshot.getDouble("height")*1.8;
                    totalcalorie = 66 + berat +tinggi-umur;
                    transaction.update(exampleNoteRef, "dailyCalorieGoal", totalcalorie);
                }
                return totalcalorie;
            }
        }).addOnSuccessListener(new OnSuccessListener<Double>() {
            @Override
            public void onSuccess(Double result) {
                Log.d(TAG, "WOIRRRRRRRRRRRRRRRRRRRRR "+result);
                int dailyCalorie=0,consumedCalorie=0,burnedCalorie=0,currentWeight=0,weightGoal=0;
                setDashboard user = new setDashboard(dailyCalorie,consumedCalorie,burnedCalorie,currentWeight,weightGoal);
                db.collection("users")
                        .document(uid)
                        .set(user, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Dashboard Sampun di set ");
                                Intent dashboard = new Intent(FirstTimeSetupActivity.this, MainActivity.class);
                                startActivity(dashboard);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Dashboard gagal", e);
                            }
                        });

            }
        });


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmitAdditionalData:
                AddData();
                break;
        }
    }
}