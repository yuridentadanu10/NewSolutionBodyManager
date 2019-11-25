package android.example.com.newsolutionbodymanager.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.com.newsolutionbodymanager.MainActivity;
import android.example.com.newsolutionbodymanager.R;
import android.example.com.newsolutionbodymanager.bottomNav.ProfileFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener, TimePickerFragment.DialogTimeListener {

    final String DATE_PICKER_TAG = "DatePicker";
    final String TIME_PICKER_ONCE_TAG = "TimePickerOnce";
    final String TIME_PICKER_REPEAT_TAG = "TimePickerRepeat";
    TextView tvOnceTime,tvOnceTime1,tvOnceTime2;
    ImageButton btnOnceTime,btnOnceTime1,btnOnceTime2;
    Button btnSetOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        tvOnceTime1 = findViewById(R.id.tv_once_time1);
        btnOnceTime1 = findViewById(R.id.btn_once_time1);
        tvOnceTime2 = findViewById(R.id.tv_once_time2);
        btnOnceTime2 = findViewById(R.id.btn_once_time2);
        tvOnceTime = findViewById(R.id.tv_once_time);
        btnOnceTime = findViewById(R.id.btn_once_time);
        btnSetOnce = findViewById(R.id.btn_set_once_alarm);
        btnOnceTime.setOnClickListener(this);
        btnOnceTime1.setOnClickListener(this);
        btnOnceTime2.setOnClickListener(this);
        btnSetOnce.setOnClickListener(this);
    }

    @Override
    public void onDialogTimeSet(String tag, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        switch (tag) {
            case TIME_PICKER_ONCE_TAG:
                tvOnceTime.setText(dateFormat.format(calendar.getTime()));
                break;

            case DATE_PICKER_TAG:
                tvOnceTime1.setText(dateFormat.format(calendar.getTime()));
                break;

            case TIME_PICKER_REPEAT_TAG:
                tvOnceTime2.setText(dateFormat.format(calendar.getTime()));
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_once_time:
                TimePickerFragment timePickerFragmentOne = new TimePickerFragment();
                timePickerFragmentOne.show(getSupportFragmentManager(), TIME_PICKER_ONCE_TAG);
                break;

            case R.id.btn_once_time1:
                TimePickerFragment timePickerFragmentOne1 = new TimePickerFragment();
                timePickerFragmentOne1.show(getSupportFragmentManager(), DATE_PICKER_TAG);
                break;

            case R.id.btn_once_time2:
                TimePickerFragment timePickerFragmentOne2 = new TimePickerFragment();
                timePickerFragmentOne2.show(getSupportFragmentManager(), TIME_PICKER_REPEAT_TAG);
                break;


            case R.id.btn_set_once_alarm:
                startActivity(new Intent(ReminderActivity.this, MainActivity.class));
                break;

        }
    }
}
