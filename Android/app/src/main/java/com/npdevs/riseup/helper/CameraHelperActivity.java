package com.npdevs.riseup.helper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.npdevs.riseup.R;

import java.io.File;
import java.io.IOException;

public class CameraHelperActivity extends AppCompatActivity {

    // only one type of request currently, to take a photo
    private static final int REQUEST_TAKE_PHOTO = 0;
    //tag used for logging
    private static final String logTag = "CameraHelperActivity";
    // The URI of photo taken with the camera
    private Uri uriPhotoTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_helper);
        this.takePhoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent toRecognize = new Intent();
            toRecognize.setData(uriPhotoTaken);
            setResult(RESULT_OK, toRecognize);
        }
        //finish this activity even if it was cancelled
        finish();
    }

    public void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //make sure we have the camera activity
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // save the photo taken to a temporary file.
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File tempPicFile = File.createTempFile("IMG_", ".jpg", storageDir);

                //get the uri from the temp file, in API > 24 this needs to be done via the
                //FileProvider class
                //uriPhotoTaken = Uri.fromFile(tempPicFile);
                String temp = getApplicationContext().getPackageName();

                Log.d(logTag, temp);
                uriPhotoTaken = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".provider",
                        tempPicFile);
                //put the URI as an extra
                Log.d(logTag, uriPhotoTaken.getPath());
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhotoTaken);
                // cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
            } catch (IOException ioe) {
                //
                //  Log.e(logTag, ioe.toString());
            }
        }
    }


}