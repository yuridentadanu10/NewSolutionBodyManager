package android.example.com.newsolutionbodymanager.sportActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.example.com.newsolutionbodymanager.MainActivity;
import android.example.com.newsolutionbodymanager.R;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class SportActivity extends AppCompatActivity {

    private TextView txtName;
    private Button btnOpenDialog,btnCancel,btnConfirm;

    private Dialog customDialog;
    private EditText txtInputName;
    private Button btnInsertName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        initViews();
    }

    private void initViews(){
        initCustomDialog();
        initViewComponents();
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
            }
        });
        NumberPicker n2 = customDialog.findViewById(R.id.numberPicker2);
        n2.setMinValue(0);
        n2.setMaxValue(24);
        n2.setWrapSelectorWheel(true);

        n2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
            }
        });

        NumberPicker n3 = customDialog.findViewById(R.id.numberPicker3);
        n3.setMinValue(0);
        n3.setMaxValue(24);
        n3.setWrapSelectorWheel(true);
        n3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){

            }
        });

        btnCancel = customDialog.findViewById(R.id.dialog_cancel);
        btnConfirm=customDialog.findViewById(R.id.dialog_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    private void initViewComponents(){
        txtName = findViewById(R.id.txtName);
        btnOpenDialog = findViewById(R.id.btnOpenDialog);
        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.show();
            }
        });
    }
}
