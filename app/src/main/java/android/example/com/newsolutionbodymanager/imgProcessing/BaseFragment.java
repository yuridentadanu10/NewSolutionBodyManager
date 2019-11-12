package android.example.com.newsolutionbodymanager.imgProcessing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.example.com.newsolutionbodymanager.R;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;

public class BaseFragment extends Fragment {
    public static final int RC_STORAGE_PERMS1 = 101;
    public static final int RC_STORAGE_PERMS2 = 102;
    public static final int RC_SELECT_PICTURE = 103;
    public static final int RC_TAKE_PICTURE = 104;
    public static final String ACTION_BAR_TITLE = "action_bar_title";
    public File imageFile;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_STORAGE_PERMS1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectPicture();
                } else {
                    MyHelper.needPermission(getActivity(), requestCode, R.string.confirm_permission);
                }
                break;
            case RC_STORAGE_PERMS2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    MyHelper.needPermission(getActivity(), requestCode, R.string.confirm_camera);
                }
                break;
        }
    }

    public void checkStoragePermission(int requestCode) {
        switch (requestCode) {
            case RC_STORAGE_PERMS1:
                int hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                    selectPicture();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                }
                break;
            case RC_STORAGE_PERMS2:
                int hasWriteCameraPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
                if (hasWriteCameraPermission == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, requestCode);
                }
                break;
        }
    }


    private void selectPicture() {
        imageFile = MyHelper.createTempFile(imageFile);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RC_SELECT_PICTURE);
    }
    private void openCamera() {
        imageFile = MyHelper.createTempFile(imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photo = FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".provider", imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photo);
        startActivityForResult(intent, RC_TAKE_PICTURE);
    }
}
