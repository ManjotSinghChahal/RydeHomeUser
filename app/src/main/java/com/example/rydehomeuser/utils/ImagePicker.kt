package com.example.rydehomedriver.utils

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import android.util.Log
import android.widget.Toast

import com.example.rydehomedriver.ui.baseclasses.BaseActivity


import java.io.*

import android.accounts.AccountManager.KEY_INTENT
import com.example.rydehomeuser.R


abstract class ImagePicker : BaseActivity() {
    private val camera = "Camera"
    private val gallery = "Gallery"
    private val optionLabel = "Choose an option..."
    private val alert = "Alert!!!"
    private val cancel = "Cancel"
    private val openSettings = "Open Settings"
    private val ok = "Ok"
    private val permissionRequired = "Required camera and storage permission to access this functionality."
    internal var item0 = arrayOf(camera, gallery)
    internal var item1 = arrayOf(gallery, cancel)
    internal var bmpPic: Bitmap? = null
    internal var imageAbsolutePath: String? = " "
    internal var uid: String? = null
    private var mActivity: Activity? = null
    private var mCode = 0


    // code: 1 to show remove profile pic icon else 0.
    fun getImage(activity: Activity?, code: Int) {
        mActivity = activity
        mCode = code

        if (!cameraPermission(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    checkPermissionDenied(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    checkPermissionDenied(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                } else {
                    requestPermission()
                }
            }

        } else {

            image_dialog()

        }
    }


    private fun image_dialog() {
      /*  val dialog = Dialog(mActivity!!, R.style.Theme_Dialog)
        dialog.setContentView(R.layout.image_dialog)
        dialog.window!!.attributes.windowAnimations = R.style.MyAnimation
        val window = dialog.window
        window!!.setGravity(Gravity.BOTTOM)
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        val cancel = dialog.findViewById<View>(R.id.tv_cancel) as TextView
        cancel.setOnClickListener { dialog.dismiss() }
        val camera = dialog.findViewById<View>(R.id.camera) as TextView
        camera.setOnClickListener {
            dialog.dismiss()
            CaptureImage()
        }
        val gallery = dialog.findViewById<View>(R.id.gallery) as TextView
        gallery.setOnClickListener {
            dialog.dismiss()
            OpenGallery()
        }

        dialog.show()*/
    }


    private fun cameraPermission(permissions: Array<String>): Boolean {
        return ContextCompat.checkSelfPermission(
            mActivity!!,
            permissions[0]
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            mActivity!!,
            permissions[1]
        ) == PackageManager.PERMISSION_GRANTED
    }

    internal fun requestPermission() {
        ActivityCompat.requestPermissions(
            mActivity!!,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
            REQUEST_CODE
        )
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                val displayMetrics = resources.displayMetrics
                val display_Width = displayMetrics.widthPixels
                //                Log.e("displayWIdth", "" + display_Width);

                bmpPic = decodeSampledBitmapFromFile(imageAbsolutePath, display_Width / 2, display_Width / 2)
                val path = saveImageToExternalStorage(bmpPic)
                selectedImage(path)
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImage = data.data
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val c = this.contentResolver.query(selectedImage!!, filePath, null, null, null)
                c!!.moveToFirst()
                val columnIndex = c.getColumnIndex(filePath[0])
                //                imageAbsolutePath = c.getString(columnIndex);
                imageAbsolutePath = getPathFromUri(mActivity, selectedImage)
                val displayMetrics = resources.displayMetrics
                val display_Width = displayMetrics.widthPixels
                //                Log.e("displayWIdth", "" + display_Width);
                bmpPic = decodeSampledBitmapFromFile(imageAbsolutePath, display_Width / 2, display_Width / 2)
                val path = saveImageToExternalStorage(bmpPic)
                c.close()
                selectedImage(path)
            } else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == DRIVE_CODE && resultCode == Activity.RESULT_OK) {
            selectedImage(data!!.getStringExtra(KEY_INTENT))
        } else if (requestCode == GALLERY_REQUEST_CODE_PDF && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImage = data.data
                selectedImage(getPathFromUri(mActivity, selectedImage))
            } else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun getPathFromUri(context: Context?, uri: Uri?): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri!!)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return (Environment.getExternalStorageDirectory()).toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )

                return getDataColumn(context!!, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context!!, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri!!.scheme!!, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context!!, uri, null, null)

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }


    private fun decodeSampledBitmapFromFile(pathName: String?, reqWidth: Int, reqHeight: Int): Bitmap? {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeFile(pathName, options)// decodeResource(res,
        // resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        Log.i("Sample_Size", "" + options.inSampleSize)
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return getbitmap(pathName, options)
    }


    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        Log.i("height", "" + height)
        Log.i("width", "" + width)
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun saveImageToExternalStorage(bitmap: Bitmap?): String? {
        var ImageURi: String? = null
        try {
            val imageFile = File(imageAbsolutePath!!)
            val outputStream = FileOutputStream(imageFile)
            val quality = 60
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()
            ImageURi = imageFile.absolutePath
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return ImageURi
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImage(mActivity, mCode)
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                checkPermissionDenied(permissions)
            }
        }
    }

    private fun checkPermissionDenied(permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(permissions[0])) {
                val mBuilder = android.app.AlertDialog.Builder(mActivity)
                val dialog = mBuilder.setTitle(alert).setMessage(permissionRequired).setPositiveButton(
                    ok
                ) { dialog, which -> requestPermission() }.setNegativeButton(
                    cancel
                ) { dialog, which -> }.create()
                dialog.setOnShowListener {
                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(mActivity!!, R.color.colorPrimary))
                }
                dialog.show()
            } else {
                val builder = android.app.AlertDialog.Builder(mActivity)
                val dialog =
                    builder.setTitle(alert).setMessage(permissionRequired).setCancelable(false).setPositiveButton(
                        openSettings
                    ) { dialog, which ->
                        finish()
                        val intent = Intent(
                            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
                                "package",
                                packageName, null
                            )
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }.create()
                dialog.setOnShowListener(object : DialogInterface.OnShowListener {
                    override fun onShow(arg0: DialogInterface) {
                        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(ContextCompat.getColor(mActivity!!, R.color.colorPrimary))
                    }
                })
                dialog.show()
            }
        }
    }

    fun OpenGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, "Select a File"), GALLERY_REQUEST_CODE)

    }

    fun CaptureImage() {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile = createFileForImage()
            imageAbsolutePath = photoFile!!.absolutePath
            intent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(
                    applicationContext,
                    applicationContext.packageName + ".provider",
                    photoFile
                )
            )
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Throws(IOException::class)
    fun createFileForImage(): File? {
        val imageFileName = "image" + System.currentTimeMillis() + ".jpg"
        val der = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            mActivity!!.getString(R.string.app_name)
        )
        var image: File? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            if (!der.exists()) {
                der.mkdirs()
            }
            image = File(der, imageFileName)
            imageAbsolutePath = image.absolutePath
        }
        return image
    }

    abstract fun selectedImage(imagePath: String?)

    companion object {
        val REQUEST_CODE = 100
        private val DRIVE_CODE = 105
        private val GALLERY_REQUEST_CODE = 101
        private val GALLERY_REQUEST_CODE_PDF = 103
        private val CAMERA_REQUEST_CODE = 102

        fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {

            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                if (cursor != null) cursor.close()
            }
            return null
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }


        fun getbitmap(image_Path: String?, options: BitmapFactory.Options): Bitmap? {
            val bm: Bitmap?
            var bitmap: Bitmap? = null
            try {
                bm = BitmapFactory.decodeStream(FileInputStream(File(image_Path!!)), null, options)
                bitmap = bm

                val exif = ExifInterface(image_Path)

                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)

                val m = Matrix()

                if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                    exif.setAttribute(ExifInterface.TAG_ORIENTATION, "3")
                    exif.saveAttributes()

                    m.postRotate(180f)

                    bitmap = Bitmap.createBitmap(bm!!, 0, 0, bm.width, bm.height, m, true)

                    return bitmap
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    exif.setAttribute(ExifInterface.TAG_ORIENTATION, "6")
                    exif.saveAttributes()

                    m.postRotate(90f)

                    bitmap = Bitmap.createBitmap(bm!!, 0, 0, bm.width, bm.height, m, true)

                    return bitmap
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    exif.setAttribute(ExifInterface.TAG_ORIENTATION, "8")
                    exif.saveAttributes()
                    m.postRotate(270f)

                    bitmap = Bitmap.createBitmap(bm!!, 0, 0, bm.width, bm.height, m, true)
                    return bitmap
                }

            } catch (e: FileNotFoundException) {
                e.printStackTrace()

            } catch (e: IOException) {
                Log.i("IOException:", e.toString())
                e.printStackTrace()
            }

            return bitmap
        }
    }

}
