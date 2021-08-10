package com.akounto.accountingsoftware.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class FilePicker {

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int REQUEST_CODE_TAKE_IMAGE = 200;
    private static final int REQUEST_CODE_PICK_FILE = 300;

    private static String sLastCameraPhotoPath;

    private FilePicker() {
    }

    public static void pickCsvFile(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    public static void pickImage(Activity activity) {
        Intent intentPick = createPickImageIntent();
        // Ensure that there's an activity to handle the intent
        if (intentPick.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intentPick, REQUEST_CODE_PICK_IMAGE);
        } else {
            Toast.makeText(activity, "no_gallery", Toast.LENGTH_SHORT).show();
        }
    }

    public static String parseImagePath(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                return parseGalleryImagePath(intent);
            } else if (requestCode == REQUEST_CODE_TAKE_IMAGE) {
                return sLastCameraPhotoPath;
            }
        }
        return null;
    }

    public static String parseFilePath(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_FILE) {
                return parseFilePath(intent);
            }
        }
        return null;
    }

    private static String parseGalleryImagePath(Intent intent) {
        if (intent != null && intent.getData() != null) {
            return intent.getData().toString();
        }
        return null;
    }

    private static String parseFilePath(Intent intent) {
        if (intent != null && intent.getData() != null) {
            return intent.getData().getPath();
        }
        return null;
    }

    @SuppressLint("NewApi")
    private static Intent createPickImageIntent() {
        boolean kitkat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Intent intent = new Intent(kitkat? Intent.ACTION_OPEN_DOCUMENT: Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return intent;
    }


}
