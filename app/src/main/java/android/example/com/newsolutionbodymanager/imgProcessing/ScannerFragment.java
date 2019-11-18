package android.example.com.newsolutionbodymanager.imgProcessing;


import android.content.Intent;
import android.example.com.newsolutionbodymanager.MainActivity;
import android.example.com.newsolutionbodymanager.R;
import android.example.com.newsolutionbodymanager.recyclerView.DetailActivityFood;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.Transaction;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.common.modeldownload.FirebaseRemoteModel;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScannerFragment extends BaseFragment implements View.OnClickListener{

    private ImageView mImageView;
    private TextView mTextView;
    private static final String TAG = "Image Scanner";
    private static final String REMOTE_MODEL_NAME = "LINE_FRIENDS";
    private static final String LOCAL_MODEL_NAME = "my_local_model";
    private FirebaseVisionImageLabeler labeler;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnPhoto, btnGallery,btnScanFood;
    public Long calorie;
    Toolbar mTopToolbar;
    public String  makanan;
    public   float deteksijelek =0;

    public ScannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageView = view.findViewById(R.id.image_view);
        mTextView = view.findViewById(R.id.text_view);
        btnGallery = view.findViewById(R.id.btn_galeri);
        btnPhoto = view.findViewById(R.id.btn_foto);

        mTopToolbar = view.findViewById(R.id.toolbar_support);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mTopToolbar);

        TextView textView = mTopToolbar.findViewById(R.id.judul_toolbar);
        textView.setText("Scan Food");

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);


        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder(LOCAL_MODEL_NAME)
                .setAssetFilePath("automl/manifest.json")
                .build();

        FirebaseRemoteModel remoteModel = new FirebaseRemoteModel.Builder(REMOTE_MODEL_NAME)
                .enableModelUpdates(true)
                .setInitialDownloadConditions(conditions)
                .setUpdatesDownloadConditions(conditions)
                .build();

        FirebaseModelManager manager = FirebaseModelManager.getInstance();
        manager.registerLocalModel(localModel);
        manager.registerRemoteModel(remoteModel);

        FirebaseVisionOnDeviceAutoMLImageLabelerOptions labelerOptions = new FirebaseVisionOnDeviceAutoMLImageLabelerOptions
                .Builder()
                .setLocalModelName(LOCAL_MODEL_NAME)
                .setRemoteModelName(REMOTE_MODEL_NAME)
                .setConfidenceThreshold(0.5f)
                .build();

        try {
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions);
        } catch (FirebaseMLException e) {
            mTextView.setTextColor(Color.RED);
            mTextView.setText(e.getMessage());
        }

        btnPhoto.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_STORAGE_PERMS1:
                case RC_STORAGE_PERMS2:
                    checkStoragePermission(requestCode);
                    break;
                case RC_SELECT_PICTURE:
                    Uri dataUri = data.getData();
                    String path = MyHelper.getPath(getContext(), dataUri);
                    if (path == null) {
                        bitmap = MyHelper.resizeImage(imageFile, getContext(), dataUri, mImageView);
                    } else {
                        bitmap = MyHelper.resizeImage(imageFile, path, mImageView);
                    }
                    if (bitmap != null) {
                        mTextView.setText(null);
                        mImageView.setImageBitmap(bitmap);
                        runMyModel(bitmap);
                    }
                    break;
                case RC_TAKE_PICTURE:
                    bitmap = MyHelper.resizeImage(imageFile, imageFile.getPath(), mImageView);

                    if (bitmap != null) {
                        mTextView.setText(null);
                        mImageView.setImageBitmap(bitmap);
                        runMyModel(bitmap);
                    }
                    break;
            }
        }
    }




    private void runMyModel(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                extractLabel(labels);
                Log.d(TAG, "onSuccess:JAAAAAAAAAAAAAAAAANGKTIK  "+ makanan);
                if(deteksijelek>0.5) {
                    cekdatabase(makanan);

                }
                else{

                    Toast.makeText(getContext(),
                            "Scan gambar gagal karena gambar yang anda ambil kurang bagus! Silahkan scan ulang ya :)  ", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "onSuccess: OOOOOOOOOOOOOOOOOOOOOOOOO"+calorie);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mTextView.setTextColor(Color.RED);
                mTextView.setText(e.getMessage());
            }
        });
    }


    private void extractLabel(List<FirebaseVisionImageLabel> labels) {
         float temp = 0;
        for (FirebaseVisionImageLabel label : labels) {

            mTextView.setText(label.getText() + "\n");
            mTextView.setText(label.getConfidence() + "\n\n");
            Log.d(TAG, "labels "+labels+" label ===="+label.getText() +" TEXTVIEW ==="+mTextView );


          String  makananSementara = label.getText();
          float  deteksijelekSementara = label.getConfidence();


            if(deteksijelekSementara>temp){
                       makanan = makananSementara;
                       deteksijelek = deteksijelekSementara;
                       temp =  deteksijelekSementara;
            }

        }

    }

    private void cekdatabase (final String makanan){

        DocumentReference docRef = db.collection("food").document(makanan);
        final String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DocumentReference dt = db.collection("users").document(uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        final double calorie = document.getDouble("calorie");
                        final String imgUrl = document.getString("imageUrl");
                        final String waktuMakan = "Lunch";
                        db.runTransaction(new Transaction.Function<Double>() {
                            @Override
                            public Double apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                                DocumentSnapshot dashboard = transaction.get(dt);

                                double caloriedaily = dashboard.getDouble("Lunch")+calorie;

                                transaction.update(dt, "Lunch", caloriedaily);

                                return caloriedaily;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Double>() {
                            @Override
                            public void onSuccess(Double result) {
                                Log.d(TAG, "WOIRRRRRRRRRRRRRRRRRRRRR "+result);
                                writeHistory(calorie,makanan,imgUrl,waktuMakan);
                                Toast.makeText(getContext(),
                                        "Kalori sebesar"+calorie+" berhasil ditambahkan ke dailyCalorie anda: ", Toast.LENGTH_SHORT).show();
                                Intent scanDone = new Intent(getActivity(), ScanDoneActivity.class);
                                //intent.putExtra("model", model);
                                scanDone.putExtra("foodName", makanan);
                                scanDone.putExtra("calorie",calorie);
                                startActivity(scanDone);



                            }
                        });
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());



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

    private void writeHistory(double calorie,String nama,String imgUrl,String waktuMakan){

        Map<String, Object> history = new HashMap<>();
        history.put("name", nama);
        history.put("calorie", calorie);
        history.put("imageUrl",imgUrl);
        history.put("waktuMakan",waktuMakan);
        String uid =   FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users")
                .document(uid).collection("historiMakanan").document()
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
            case R.id.btn_foto:
                checkStoragePermission(RC_STORAGE_PERMS1);
                break;
            case R.id.btn_galeri:
                checkStoragePermission(RC_STORAGE_PERMS2);
                break;

        }
    }
}
