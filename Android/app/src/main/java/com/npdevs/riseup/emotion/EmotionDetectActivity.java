package com.npdevs.riseup.emotion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;
import com.npdevs.riseup.R;
import com.npdevs.riseup.helper.CameraHelperActivity;
import com.npdevs.riseup.helper.EmotionData;
import com.npdevs.riseup.helper.FailedImageView;
import com.npdevs.riseup.helper.ImageHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmotionDetectActivity extends AppCompatActivity {

    // request code for taking a picture, currently no gallery pictures are supported
    private static final int REQUEST_TAKE_PICTURE = 0;
    // Compressing Value for bitmap of image taken
    private static final int COMPRESSION_BIT_MAP = 70;
    // Button which is displayed on main page to selected an image
    private Button selectImageButton;
    // The URI of the image selected to detect.
    private Uri imageUri;
    // The image selected to detect as a bitmap.
    private Bitmap imageBitMap;
    // client we use to send a web service request with the bytecode of the image
    private FaceServiceRestClient faceClient;
    //dialog for showing progress of analyzing image
    private ProgressDialog loadingDialog;
    // tag used for logging this activity
    private static final String logTag = "EmotionDetectActivity";

    private Byte[] failedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_detect);
        faceClient = new FaceServiceRestClient(getString(R.string.api_end_point),
                getString(R.string.face_subscription_key));
        selectImageButton = (Button) findViewById(R.id.buttonSelectImage);
        // show a warning that these images are sent to microsoft web services and may be stored
        Toast.makeText(this, getString(R.string.consent_warning),
                Toast.LENGTH_LONG).show();

    }
    private void startDetection() {
        //disable button and create progress dialog
        selectImageButton.setEnabled(false);
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setMessage(getString(R.string.loading_message));
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        // call inner class detectEmotion
        try {
            new DetectEmotion().execute();
        } catch (Exception e) {
            //
            // Log.d(logTag,e.getMessage());
        }
    }
    public void takePictureClick(View view) {
        //go to camera helper activity to take photo and send back the image Uri
        Intent toCameraHelper  = new Intent(EmotionDetectActivity.this,
                CameraHelperActivity.class);
        startActivityForResult(toCameraHelper, REQUEST_TAKE_PICTURE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //set imageUri field for use later
            imageUri = data.getData();
            //go to bitmap helper to get a bitmap that is scaled down
            imageBitMap = ImageHelper.compressBitMap(
                    imageUri, getContentResolver());
            Log.d(logTag, imageUri.getPath());
            Log.d(logTag, String.valueOf(imageBitMap.getByteCount()));
            ImageView imageView = findViewById(R.id.imageBit);
            imageView.setImageBitmap(imageBitMap);
            // if we get a bitmap back then go ahead and try to detect a face and emotion
            if (imageBitMap != null) {

                startDetection();
            } else {
                Toast.makeText(this, getString(R.string.image_processing_error)
                        , Toast.LENGTH_LONG).show();
                //Log.e(logTag,"The bit map was null coming from ImageHelper activity");
            }
            // delete the temporary file
            //deletePictures();
        }
    }
    private Face[] getResults() throws ClientException,
            IOException {

        // create byte array to send to emotion client
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        imageBitMap.compress(Bitmap.CompressFormat.JPEG, this.COMPRESSION_BIT_MAP, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());


        //for debugging bad images
        //  failedImage = output.toByteArray();

        // only get emotion response
        FaceServiceClient.FaceAttributeType[] faceAttributeTypes =
                new FaceServiceClient.FaceAttributeType[1];
        faceAttributeTypes[0] = FaceServiceClient.FaceAttributeType.Emotion;

        return faceClient.detect(inputStream, true, false,
                faceAttributeTypes);
    }
    private boolean deletePictures(){
        File file = new File(imageUri.getPath());
        return file.delete();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void toResults(Emotion emotionResults){
        Intent toResults = new Intent(EmotionDetectActivity.this,
                EmotionResultActivity.class);
        //add a bundle of the ArrayLists for scores and emotions
        ArrayList<EmotionData> emotionsSorted = this.orderedEmotionsToMap(emotionResults);
        // right now the graph only displays top 4 emotions, so don't need to send more than
        // 4 elements
        List<EmotionData> topFourEmotions = emotionsSorted.subList(0,4);
        // need to use ArrayList for sending to EmotionResultActivity, cannot use as a
        // Serializable object. Using new object to prevent concurrent modification exceptions.
        ArrayList<EmotionData> topFourArrayList = new ArrayList<EmotionData>();
        topFourArrayList.addAll(topFourEmotions);

        Bundle bundleToEmotionResults = new Bundle();
        bundleToEmotionResults.putSerializable("topFourEmotions", (Serializable)topFourArrayList);
        toResults.putExtra("emotionResultsBundle", bundleToEmotionResults);

        this.startActivity(toResults);
    }
    private ArrayList<EmotionData>  orderedEmotionsToMap (Emotion emotion){
        ArrayList<EmotionData> emotionDataList = new ArrayList<EmotionData>();
        // get all fields in the Emotion object
        Field[] emotionFields = emotion.getClass().getDeclaredFields();

        for(Field emotionField: emotionFields) {
            String tempFieldName = emotionField.getName();

            try {
                // get value of field
                Double emotionValue = emotionField.getDouble(emotion);
                emotionDataList.add(new EmotionData(tempFieldName, emotionValue));

            } catch (IllegalAccessException iae) {
                // should not happen - but if it does need to handle null value in calling method
                Log.e(logTag, iae.getMessage());
                return null;
            }
        }

        //sort the values by descending emotion value
        Collections.sort(emotionDataList);

        return emotionDataList;
    }
    private class DetectEmotion extends AsyncTask<String, String, Face[]> {
        // store the exception for use onPostExecute
        Exception exception;
        @Override
        protected Face[] doInBackground(String... args) {
            // try and get the results, store the errors
            try {
                Face[] temp = getResults();

                return temp;
            } catch (Exception e) {
                //Log.e(logTag,e.toString());
                exception = e;
            }

            return null;
        }
        @Override
        protected void onPostExecute(Face[] result) {
            super.onPostExecute(result);
            loadingDialog.dismiss();
            selectImageButton.setEnabled(true);
            if (exception != null) {

                //check to see if we have an internet connection
                if (!isNetworkAvailable()) {
                    //show Dialog for no internet connection
                    final AlertDialog finished = new AlertDialog.Builder(
                            EmotionDetectActivity.this).create();

                    finished.setTitle("No Internet");
                    finished.setMessage(getString(R.string.no_internet));


                    finished.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finished.dismiss();
                                }
                            });

                    finished.show();

                }

                // Log.e(logTag,exception.toString());
            } else {
                //if we got no results most likely a face is not in the picture
                if (result.length == 0) {

                    final AlertDialog finished = new AlertDialog.Builder
                            (EmotionDetectActivity.this).create();

                    finished.setTitle("No Mood");
                    finished.setMessage(getString(R.string.no_faces));
                    finished.setButton(AlertDialog.BUTTON_POSITIVE, "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finished.dismiss();
                                }
                            });

                    //finished.show();
                    Intent toFailedImage  = new Intent(EmotionDetectActivity.this,
                            FailedImageView.class);
                    // toFailedImage.putExtra("FAILED_IMAGE",imageBitMap);
                    startActivity(toFailedImage);
                    //a successful result
                } else if (result.length > 0) {
                    // get a ranked list of results
                    Log.d(logTag,"found Results!");
                    //emotionResults = result.get(0).scores.ToRankedList(Order.DESCENDING);
                    Emotion emotionResults = result[0].faceAttributes.emotion;
                    ArrayList<Double> emotionsOrdered = new ArrayList<Double>();
                    // add all eight emotions detected by Face API


                    toResults(emotionResults);

                }
            }

        }

    }

}
