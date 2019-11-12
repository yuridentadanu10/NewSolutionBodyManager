package android.example.com.newsolutionbodymanager.imgProcessing;


import android.content.Intent;
import android.example.com.newsolutionbodymanager.R;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.List;

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
    Button btnPhoto, btnGallery;

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
        for (FirebaseVisionImageLabel label : labels) {

            mTextView.append(label.getText() + "\n");
            mTextView.append(label.getConfidence() + "\n\n");
            Log.d(TAG, "labels "+labels+" label ===="+label.getText() +" TEXTVIEW ==="+mTextView );
        
        }
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
