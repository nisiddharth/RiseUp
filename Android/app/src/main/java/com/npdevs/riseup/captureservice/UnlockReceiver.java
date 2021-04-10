package com.npdevs.riseup.captureservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.FaceDetector;
import android.widget.Toast;

import com.cottacush.android.hiddencam.HiddenCam;
import com.cottacush.android.hiddencam.OnImageCapturedListener;
import com.google.gson.Gson;
import com.npdevs.riseup.api.responseModels.user.SaveEmotionResponse;
import com.npdevs.riseup.api.retrofit.RetrofitClient;
import com.npdevs.riseup.captureservice.predictivemodels.Classification;
import com.npdevs.riseup.captureservice.predictivemodels.TensorFlowClassifier;
import com.npdevs.riseup.utils.SharedPrefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnlockReceiver extends BroadcastReceiver {

    private static final String TAG = "MyBroadcastReceiver";
    private static HiddenCam hiddenCam;
    static boolean done = false;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (done) {
//            Toast.makeText(context, "Already done", Toast.LENGTH_SHORT).show();
            hiddenCam.stop();
            done = false;
            hiddenCam.destroy();
            return;
        }
        String system_picture_directory = "/storage/emulated/0/Android/data/io.github.nisiddharth.hiddencamera/files";
        if (hiddenCam != null) {
            hiddenCam.stop();
        }
        hiddenCam = new HiddenCam(context, new File(system_picture_directory), new OnImageCapturedListener() {
            @Override
            public void onImageCaptured(File file) {
//                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                Matrix matrix = new Matrix();
                matrix.postRotate(-90);
                Bitmap scaledBitMap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                bitmap = Bitmap.createBitmap(scaledBitMap, 0, 0, scaledBitMap.getWidth(), scaledBitMap.getHeight(), matrix, true);
                try (FileOutputStream out = new FileOutputStream("/storage/emulated/0/Android/data/io.github.nisiddharth.hiddencamera/files/out.jpg")) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                } catch (IOException e) {
                    e.printStackTrace();
                }
                process(bitmap, context);
                file.delete();
                done = true;
            }

            @Override
            public void onImageCaptureError(Throwable throwable) {
                Toast.makeText(context, "Failed, " + (throwable != null ? throwable.getMessage() : "Throwable null :("), Toast.LENGTH_LONG).show();
                done = true;
            }
        });

        hiddenCam.start();
        hiddenCam.captureImage();
        KillThread thread = new KillThread(hiddenCam);
        thread.start();
    }

    private void process(Bitmap bitmap, Context context) {
        FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), 1);
        FaceDetector.Face[] faces = new FaceDetector.Face[1];
        int count = faceDetector.findFaces(bitmap, faces);

        Toast.makeText(context, (count == -1 ? "No " : "") + "Face found", Toast.LENGTH_SHORT).show();

        if (count != -1) {
            FaceCropper faceCropper = new FaceCropper();
            Bitmap faceBmp = faceCropper.getCroppedImage(bitmap);
            try (FileOutputStream out = new FileOutputStream("/storage/emulated/0/Android/data/io.github.nisiddharth.hiddencamera/files/out.jpg")) {
                faceBmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                runModel(faceBmp, context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void runModel(Bitmap bitmap, Context context) {
        try {
            TensorFlowClassifier classifier = TensorFlowClassifier.create(context.getAssets(), "CNN",
                    "opt_em_convnet_5000.pb", "labels.txt", 48,
                    "input", "output_50", true, 7);

            Bitmap grayImage = toGrayscale(bitmap);
            Bitmap resizedImage = getResizedBitmap(grayImage, 48, 48);

            //Initialize the intArray with the same size as the number of pixels on the image
            int[] pixelarray = new int[resizedImage.getWidth() * resizedImage.getHeight()];

            //copy pixel data from the Bitmap into the 'intArray' array
            resizedImage.getPixels(pixelarray, 0, resizedImage.getWidth(), 0, 0, resizedImage.getWidth(), resizedImage.getHeight());

            float[] normalized_pixels = new float[pixelarray.length];
            for (int i = 0; i < pixelarray.length; i++) {
                // 0 for white and 255 for black
                int pix = pixelarray[i];
                int b = pix & 0xff;
                //  normalized_pixels[i] = (float)((0xff - b)/255.0);
                // normalized_pixels[i] = (float)(b/255.0);
                normalized_pixels[i] = (float) (b);
            }

            final Classification res = classifier.recognize(normalized_pixels);
            saveEmotion(context, res.getLabel());
            Toast.makeText(context, res.getLabel() + " " + res.getConf(), Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveEmotion(Context context, String label) {
        SharedPrefs prefs = new SharedPrefs(context);
        Date date = new Date();
        Map<String, List<List<String>>> body = new HashMap<>();

        body.put("emotion", new ArrayList<>(Arrays.asList(
                new ArrayList<String>(Arrays.asList(String.valueOf(date.getTime()), label)))));
        RetrofitClient.getClient().saveEmotion(prefs.getToken(),body).enqueue(new Callback<SaveEmotionResponse>() {
            @Override
            public void onResponse(Call<SaveEmotionResponse> call, Response<SaveEmotionResponse> response) {
                if(!response.isSuccessful()){
                    Gson gson = new Gson();
                    SaveEmotionResponse errorResponse = gson.fromJson(response.errorBody().charStream(), SaveEmotionResponse.class);
                    Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "Data sent!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SaveEmotionResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    private Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }
}

class KillThread extends Thread {

    HiddenCam hd;

    KillThread(HiddenCam hd) {
        this.hd = hd;
    }

    public void run() {
        while (true) {
            if (UnlockReceiver.done) {
                hd.stop();
//                hd.destroy();
                hd = null;
                break;
            }
            try {
                sleep(1000);
            } catch (Exception ex) {

            }
        }
    }
}

