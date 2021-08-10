package com.akounto.accountingsoftware.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.akounto.accountingsoftware.R
import java.io.File
import java.io.IOException
import java.io.InputStream

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.KITKAT)
object CameraUtils {

    fun captureImageFromCameraGallery(activity: Activity, imageName: String) {
        val items = arrayOf<CharSequence>(
            "Take Photo", "Select from Gallery", "Cancel"
        )

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Take Photo" -> {
                    cameraIntent(activity, imageName)
                }
                items[item] == "Select from Gallery" -> {
                    galleryIntent(activity)
                }
                items[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
    fun isDeviceSupportCamera(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(
            PackageManager.FEATURE_CAMERA
        )
    }


    //************************* Camera *************************

    fun cameraIntent(activity: Activity?, fileName: String) {
        val file = getOutputMediaFile(
            activity, Constants.MEDIA_TYPE_IMAGE,
            fileName, Constants.IMAGE_EXTENSION
        )
        val uri: Uri? = activity?.let { getOutputMediaFileUri(it, file) }

        var intent: Intent? = null
        intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        activity?.startActivityForResult(
            intent, Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE
        )
    }

    fun getOutputMediaFile(
        context: Context?, type: Int, name: String, extension: String
    ): File? {

        // External sdcard location
        val getImage = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val mediaStorageDir = File(
            getImage?.path,
            Constants.GALLERY_DIRECTORY_NAME
        )

        /*val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            Constants.GALLERY_DIRECTORY_NAME
        )*/

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(
                    Constants.GALLERY_DIRECTORY_NAME, "Oops! Failed create "
                            + Constants.GALLERY_DIRECTORY_NAME + " directory"
                )
                return null
            }
        }

        // Preparing media file naming convention
        // adds timestamp
        //val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val mediaFile: File = when (type) {
            Constants.MEDIA_TYPE_IMAGE -> {
                /*File(
                    mediaStorageDir.path + File.separator
                            + "TPM_IMAGE_" + timeStamp + "." + Constants.IMAGE_EXTENSION
                )*/

                File(
                    mediaStorageDir.path + File.separator
                            + name + "." + extension
                )
            }
            Constants.MEDIA_TYPE_VIDEO -> {
                File(
                    mediaStorageDir.path + File.separator
                            + name + "." + extension
                )
            }
            else -> {
                return null
            }
        }
        return mediaFile
    }

    fun getOutputMediaFileUri(context: Context, file: File?): Uri? {
        return file?.let {
            FileProvider.getUriForFile(
                context, context.packageName + ".provider", it
            )
        }
    }

    fun getPickerImageChooserIntent(context: Context?, uri: Uri?): Intent? {
        // Create a chooser from the main intent
        var chooserIntent: Intent? = null

        context?.let {
            // Determine Uri of camera image to save.
            //val outputFileUri = getCaptureImageOutputUri(context)
            val outputFileUri: Uri? = uri
            val allIntents: ArrayList<Intent> = ArrayList()
            val packageManager: PackageManager = context.packageManager

            // collect all camera intents
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val listCam: List<ResolveInfo> =
                packageManager.queryIntentActivities(captureIntent, 0)
            for (res in listCam) {
                val intent = Intent(captureIntent)
                intent.component =
                    ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.setPackage(res.activityInfo.packageName)
                if (outputFileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                }
                allIntents.add(intent)
            }

            // collect all gallery intents
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            val listGallery: List<ResolveInfo> =
                packageManager.queryIntentActivities(galleryIntent, 0)
            for (res in listGallery) {
                val intent = Intent(galleryIntent)
                intent.component =
                    ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.setPackage(res.activityInfo.packageName)
                allIntents.add(intent)
            }

            // the main intent is the last in the list (android) so pickup the useless one
            var mainIntent: Intent? = allIntents[allIntents.size - 1]
            for (intent in allIntents) {
                if (intent.component!!.className == "com.android.documentsui.DocumentsActivity") {
                    mainIntent = intent
                    break
                }
            }
            allIntents.remove(mainIntent)

            // Create a chooser from the main intent
            chooserIntent = Intent.createChooser(mainIntent, "Select source")

            // Add all other intents
            chooserIntent?.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                allIntents.toArray(arrayOfNulls<Parcelable>(allIntents.size))
            )
        }
        return chooserIntent
    }

    fun getMediaFilePath(context: Context?, fileName: String): File? {
        val file: File? = getOutputMediaFile(
            context, Constants.MEDIA_TYPE_IMAGE,
            fileName, Constants.IMAGE_EXTENSION
        )
        return file
    }


    //************************** Select from Gallery **************************

    var nopath: String? = "Select Video Only"

    fun galleryIntent(activity: Activity?) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activity?.startActivityForResult(
            Intent.createChooser(intent, "Select File"),
            Constants.GALLERY_IMAGE_REQUEST_CODE
        )
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("NewApi")
    fun getPath(context: Context, uri: Uri): String? {

        // check here to KITKAT or new version
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(
                    context, contentUri, selection,
                    selectionArgs
                )
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context, uri, null, null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return nopath
    }

    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri!!, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return nopath
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri
            .authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri
            .authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri
            .authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri
            .authority
    }


    //*********************** Display image in the imageview *****************************

    fun displayImage(context: Context?, file: File?, imageView: ImageView) {
        if (file != null) {
            val myBitmap: Bitmap? = optimizeBitmap(
                Constants.BITMAP_SAMPLE_SIZE,
                file.absolutePath
            )
            val uri = context?.let { getOutputMediaFileUri(it, file) }

            myBitmap?.let {
                var bitmap: Bitmap? = it
                bitmap = getResizeBitmap(it, 1000)

                try {
                    bitmap = uri?.let { it ->
                        bitmap?.let { it1 ->
                            rotateImageIfRequired(context, it1, it)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("CameraUtils", "Error ${e.message}")
                }
                imageView.setImageBitmap(bitmap)
            }
        } else {
            imageView.setImageDrawable(context?.getDrawable(R.drawable.logo3))
        }
    }

    private fun optimizeBitmap(sampleSize: Int, filePath: String?): Bitmap {
        // bitmap factory
        val options = BitmapFactory.Options()

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = sampleSize
        return BitmapFactory.decodeFile(filePath, options)
    }

    private fun getResizeBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    @Throws(IOException::class)
    fun rotateImageIfRequired(context: Context?, img: Bitmap, selectedImage: Uri): Bitmap? {
        val input: InputStream? =
            selectedImage.let { it1 -> context?.contentResolver?.openInputStream(it1) }

        val ei = input?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ExifInterface(it)
            } else {
                TODO("VERSION.SDK_INT < N")
            }
        }

        //val ei = ExifInterface(selectedImage.path.toString())
        val orientation: Int? =
            ei?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        //img.recycle()
        return Bitmap.createBitmap(img, 0, 0, img.width, img.height, Matrix().apply {
            postRotate(degree.toFloat())
        }, true)
    }
}