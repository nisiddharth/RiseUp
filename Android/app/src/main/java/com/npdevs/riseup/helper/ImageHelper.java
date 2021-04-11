package com.npdevs.riseup.helper;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class ImageHelper {
    // max width of an image
    private static final int IMAGE_MAX_WIDTH = 1280;
    // max height of an image
    private static final int IMAGE_MAX_HEIGHT = 720;
    // log tag for logging
    private static final String logTag = "ImageHelper";

    public static Bitmap compressBitMap(Uri imageUri, ContentResolver contentResolver) {
        try {

            InputStream imageInputStream = contentResolver.openInputStream(imageUri);
            // get rotation angle first as conversion to bitmap strips EXIF metadata.
            int rotationAngle = getImageRotationAngle(imageInputStream);

            // For saving memory, only decode the image meta and get the side length.
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Rect outPadding = new Rect();
            BitmapFactory.decodeStream(imageInputStream, outPadding, options);

            // Calculate shrink rate when loading the image into memory.
            int maxSideLength =
                    options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
            options.inSampleSize = 1;
            options.inSampleSize = calculateSampleSize(maxSideLength, IMAGE_MAX_WIDTH);
            options.inJustDecodeBounds = false;

            if (imageInputStream != null) {
                imageInputStream.close();
            }

            // Load the bitmap and resize it to the expected size length
            imageInputStream = contentResolver.openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageInputStream, outPadding, options);
            maxSideLength = bitmap.getWidth() > bitmap.getHeight()
                    ? bitmap.getWidth() : bitmap.getHeight();
            double ratio = IMAGE_MAX_WIDTH / (double) maxSideLength;
            if (ratio < 1) {
                bitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        (int) (bitmap.getWidth() * ratio),
                        (int) (bitmap.getHeight() * ratio),
                        false);
            }

            return rotateBitmap(bitmap, rotationAngle);

        } catch (Exception e) {
            // Log.e(logTag,e.toString());
            return null;
        }

    }

    private static int calculateSampleSize(int imgWidth, int imgHeight) {
        int inSampleSize = 1;
        //compare half width and height to not scale down too much
        int halfWidth = imgWidth / 2;
        int halfHeight = imgHeight / 2;
        // increase sample size until we have met our image
        while (((halfWidth / inSampleSize) >= IMAGE_MAX_WIDTH) &&
                ((halfHeight / inSampleSize) >= IMAGE_MAX_HEIGHT)) {
            //sample size should be a power of two
            inSampleSize *= 2;
        }
        return inSampleSize;
    }

    private static int getImageRotationAngle(InputStream imageStream) {
        int angle = 0;
        ExifInterface exif;
        //get the orientation and return an angle which corresponds to it
        try {
            exif = new ExifInterface(imageStream);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                default:
                    break;
            }

        } catch (IOException ioe) {
            Log.e(logTag, ioe.toString());
        }

        return angle;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int angle) {

        if (angle != 0) {
            Matrix matrix = new Matrix();
            //rotate the matrix by left matrix multiplication
            matrix.postRotate(angle);
            //Log.d(logTag,"Image rotated by " + angle + " degrees");
            //create a new bitmap that is rotated into portrait orientation
            return Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap;
        }
    }

}

