package android.example.com.newsolutionbodymanager.sportActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.example.com.newsolutionbodymanager.LoginAndFriend.LoginActivity;
import android.example.com.newsolutionbodymanager.LoginAndFriend.RegisterActivity;
import android.example.com.newsolutionbodymanager.MainActivity;
import android.example.com.newsolutionbodymanager.R;
import android.example.com.newsolutionbodymanager.recyclerView.DetailActivityFood;
import android.example.com.newsolutionbodymanager.welcomingActivity.FirstTimeSetupActivity;
import android.example.com.newsolutionbodymanager.welcomingActivity.setDashboard;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class SportActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "sportActivity";

    private Button btnCancel,btnConfirm,btnCancel1,btnConfirm1,btnsaveSport,btnCancelsport;
    TextView duration1,duration2,duration3,tv_distance,kaloriOlahraga,menitOlahraga;
    LinearLayout btnOpenDialog,btnOpendistance;
    private Dialog customDialog,distanceDialog;
    EditText et_distance;
    double hasil,calorieBurned,distance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth ;
     String olahraga;
    int value1,value2,value3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        kaloriOlahraga = findViewById(R.id.calorie_olahraga);
        menitOlahraga = findViewById(R.id.menit_olahraga);



        initViews();

    }


    @Override
    protected void onStart() {
        super.onStart();
        bacaDatabase();
        Log.d(TAG, "onStart: "+calorieBurned);
    }

    private void hitungHasil (){
        int konversiWaktu=  value1 * 60 + value2 + value3/60;
        double waktu = Double.valueOf(konversiWaktu);
        double multiplier = 1;
        double jarak = distance*0.001;
        if (distance<=0.1){
            multiplier = 0.5;
        }

        calorieBurned = jarak/waktu*660*multiplier;
        


    }


    private void initViews(){
        initDistanceDialog();
        initCustomDialog();
        initViewComponents();
    }

    private void initDistanceDialog(){
        distanceDialog = new Dialog(SportActivity.this);
        distanceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        distanceDialog.setContentView(R.layout.custom_distance_dialog);
        distanceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        distanceDialog.setCancelable(true);


        et_distance = distanceDialog.findViewById(R.id.et_distance);
        btnCancel1 = distanceDialog.findViewById(R.id.dialog_cancel_distance);
        btnConfirm1=distanceDialog.findViewById(R.id.dialog_confirm_distance);
        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distanceDialog.dismiss();
            }
        });

        btnConfirm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String distance0 = et_distance.getText().toString().trim();
                distance = Integer.parseInt(distance0);
                tv_distance = findViewById(R.id.tv_distance);
                tv_distance.setText(String.valueOf(distance)+" meter");
                distanceDialog.dismiss();
            }
        });
    }

    private void initCustomDialog(){
        customDialog = new Dialog(SportActivity.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.custom_dialog);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setCancelable(true);

        NumberPicker np =customDialog.findViewById(R.id.numberPicker1);
        np.setMinValue(0);
        np.setMaxValue(24);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                duration1 = findViewById(R.id.duration1);
                 value1 = picker.getValue();
                duration1.setText(String.valueOf(value1));
            }
        });
        NumberPicker n2 = customDialog.findViewById(R.id.numberPicker2);
        n2.setMinValue(0);
        n2.setMaxValue(24);
        n2.setWrapSelectorWheel(true);

        n2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                duration2 = findViewById(R.id.duration2);
                 value2 = picker.getValue();
                duration2.setText(String.valueOf(value2));
            }
        });

        NumberPicker n3 = customDialog.findViewById(R.id.numberPicker3);
        n3.setMinValue(0);
        n3.setMaxValue(24);
        n3.setWrapSelectorWheel(true);
        n3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                duration3 = findViewById(R.id.duration3);
                 value3 = picker.getValue();
                duration3.setText(String.valueOf(value3));
            }
        });

        btnCancel = customDialog.findViewById(R.id.dialog_cancel);
        btnConfirm=customDialog.findViewById(R.id.dialog_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });

    }


    private void bacaDatabase(){
        Intent i=getIntent();
         olahraga=i.getExtras().getString("olahraga");
        Log.d(TAG, "bacaDatabase: " +olahraga);

        DocumentReference docRef = db.collection("sport").document(olahraga);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Long calorie = document.getLong("calorie");
                        Long menit = document.getLong("time");

                        kaloriOlahraga.setText(calorie +" Calories");
                        menitOlahraga.setText(String.valueOf(menit)+" Minute");

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

    private void writeDatabase(){
        final String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
            hitungHasil();
        db.runTransaction(new Transaction.Function<Double>() {
            @Override
            public Double apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference exampleNoteRef = db.collection("users").document(uid);
                DocumentSnapshot exampleNoteSnapshot = transaction.get(exampleNoteRef);
                Double bakarkalori = null;
                
                if(calorieBurned<2000) {
                    bakarkalori = exampleNoteSnapshot.getDouble("burnedCalorie")+calorieBurned;
                    Log.d(TAG, "apply: " + calorieBurned);
                    transaction.update(exampleNoteRef, "burnedCalorie", bakarkalori);
                    return bakarkalori;
                }
                else {
                    Toast.makeText(SportActivity.this,
                            "Inputan anda tidak masuk akal", Toast.LENGTH_SHORT).show();
                    calorieBurned = 0;
                }
                return bakarkalori;
            }
        }).addOnSuccessListener(new OnSuccessListener<Double>() {
            @Override
            public void onSuccess(Double result) {
                writeHistory(calorieBurned,olahraga);
                Log.d(TAG, "WOIRRRRRRRRRRRRRRRRRRRRR "+result);
                Intent ijuk = new Intent(SportActivity.this,MainActivity.class);
                startActivity(ijuk);

            }
        });
    }
    private void initViewComponents(){
        btnOpenDialog = findViewById(R.id.btnOpenDialog);
        btnOpenDialog.setOnClickListener(this);
        btnOpendistance = findViewById(R.id.btn_linear_distance);
        btnOpendistance.setOnClickListener(this);

        btnsaveSport = findViewById(R.id.btn_save_sport);
        btnCancelsport = findViewById(R.id.btn_cancel_sport);
        btnCancelsport.setOnClickListener(this);
        btnsaveSport.setOnClickListener(this);
    }

    private Double cekDouble(double calorieBurned){
        if(Double.isInfinite(calorieBurned)== true)
        {
            calorieBurned = 0;
        }
       return calorieBurned;
    }

    private void writeHistory(double calorie,String nama){

        Map<String, Object> history = new HashMap<>();
        history.put("name", nama);
        history.put("calorie", calorie);
        String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users")
                .document(uid).collection("historiOlahraga").document()
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
            case R.id.btnOpenDialog:
                customDialog.show();
                break;
            case R.id.btn_linear_distance:
                distanceDialog.show();
                break;
            case R.id.btn_cancel_sport:

                break;
            case R.id.btn_save_sport:
                if (calorieBurned < 2000 ){
                    writeDatabase();
                }
                else{
                    Toast.makeText(SportActivity.this,
                            "Inputan anda tidak masuk akal", Toast.LENGTH_SHORT).show();
                    calorieBurned=0;
                }
                break;
        }

    }
}
