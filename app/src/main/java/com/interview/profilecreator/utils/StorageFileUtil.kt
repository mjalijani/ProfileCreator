package com.interview.profilecreator.utils


import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.regex.Pattern

typealias OnResult = (uri: Uri?, path: String) -> Unit

object StorageFileUtil {

    private fun createFileInSpecificDirectory(
        coroutineScope: CoroutineScope,
        context: Context,
        fileName: String,
        mimeType: String,
        relativePath: String,
        mediaTableUri: Uri,
        createNewIfExists: Boolean,
        onResult: OnResult
    ) {
        val parentDir = File("${Environment.getExternalStorageDirectory().path}/$relativePath")
        if (!parentDir.exists()) {
            parentDir.mkdirs()
        }

        val file = File(parentDir, fileName)

        val newFile = if (createNewIfExists) {
            getUniqueFile(file)
        } else {
            file
        }

        coroutineScope.launch(Dispatchers.IO) {

            MediaScannerConnection.scanFile(
                context,
                arrayOf(newFile.absolutePath),
                arrayOf(mimeType)
            ) { path, uri ->
                Log.d("SAEED", "onScanCompleted: $path uri: $uri")
                if (uri != null) {
                    // file exists
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        try {
                            // check can access to the file
                            val outputStream = context.contentResolver.openOutputStream(uri)
                            outputStream?.close()
                            onResult.invoke(uri, path)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            createFileInSpecificDirectory(
                                coroutineScope = coroutineScope,
                                context = context,
                                fileName = getUniqueFile(file).name,
                                mimeType = mimeType,
                                relativePath = relativePath,
                                mediaTableUri = mediaTableUri,
                                createNewIfExists = createNewIfExists,
                                onResult = onResult
                            )
                        }
                    } else {
                        onResult.invoke(uri, path)
                    }
                } else {
                    // file doesn't exists so create new file with media store
                    val values: ContentValues

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        values = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, newFile.name)
                            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                            put(
                                MediaStore.MediaColumns.DATE_ADDED,
                                System.currentTimeMillis() / 1000
                            )
//                put(MediaStore.MediaColumns.IS_PENDING, 1)
                            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
                        }
                    } else {
                        values = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, newFile.name)
                            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                            put(
                                MediaStore.MediaColumns.DATE_ADDED,
                                System.currentTimeMillis() / 1000
                            )
                            put(MediaStore.MediaColumns.DATA, newFile.path)
                        }
                    }

                    var newFileUri = try {
                        context.contentResolver.insert(mediaTableUri, values)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        null
                    }
                    if (newFileUri == null) {
                        getUriFromDisplayName(
                            context = context,
                            fileName = newFile.name,
                            mediaTableUri = mediaTableUri
                        )?.let { oldUri ->
                            context.contentResolver.delete(oldUri, null, null)
                        }
                        newFileUri = try {
                            context.contentResolver.insert(mediaTableUri, values)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            null
                        }
                    }
                    Log.d(
                        "SAEED",
                        "getUniqueFile: ${newFile.absolutePath} ${newFileUri?.path} $path"
                    )
                    onResult.invoke(newFileUri, path)
                }
            }


        }

    }

    private fun getUriFromDisplayName(
        context: Context,
        fileName: String,
        mediaTableUri: Uri
    ): Uri? {

        val projection = arrayOf(
            MediaStore.MediaColumns._ID
        )

        return context.contentResolver.query(
            mediaTableUri,
            projection,
            MediaStore.MediaColumns.DISPLAY_NAME + " LIKE ?",
            arrayOf(fileName),
            null
        )?.let { cursor ->
            cursor.moveToFirst()
            return if (cursor.count > 0) {
                val columnIndex = cursor.getColumnIndex(projection[0])
                val fileId = cursor.getLong(columnIndex)
                cursor.close()
                Uri.parse("$mediaTableUri/$fileId")
            } else {
                null
            }
        }

    }

    private fun createFileInSpecificImageDirectory(
        coroutineScope: CoroutineScope,
        context: Context,
        fileName: String,
        mimeType: String,
        relativePath: String,
        createNewIfExists: Boolean,
        onResult: OnResult
    ) {
        createFileInSpecificDirectory(
            coroutineScope,
            context,
            fileName,
            mimeType,
            relativePath,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            createNewIfExists,
            onResult
        )
    }


    fun createFileInPublicImageDirectory(
        coroutineScope: CoroutineScope,
        context: Context,
        fileName: String,
        mimeType: String,
        parentPath: String? = null,
        createNewIfExists: Boolean,
        onResult: OnResult
    ) {
        createFileInSpecificImageDirectory(
            coroutineScope,
            context,
            fileName,
            mimeType,
            "${Environment.DIRECTORY_PICTURES}${parentPath?.let { "/$it" } ?: ""}",
            createNewIfExists,
            onResult
        )
    }

    private fun getUniqueFile(file: File, incOnStart: Boolean = false): File {
        var mIncOnStart = incOnStart
        var newFile = file
        return try {
            while (newFile.exists() || mIncOnStart) {
                mIncOnStart = false
                val newFileName = newFile.name
                var baseName = newFileName.substring(0, newFileName.lastIndexOf("."))
                val extension = getExtension(newFileName)
                val pattern =
                    Pattern.compile("( \\(\\d+\\))\\.") // Find ' (###).' in the file name, if it exists
                val matcher = pattern.matcher(newFileName)
                var strDigits: String
                if (matcher.find()) {
                    baseName = baseName.substring(0, matcher.start(0)) // remove the (###)
                    strDigits = matcher.group(0) // grab the ### we'll want to increment
                    strDigits = strDigits.substring(
                        strDigits.indexOf("(") + 1,
                        strDigits.lastIndexOf(")")
                    ) // strip off the ' (' and ').' from the match
                    // increment the found digit and convert it back to a string
                    strDigits = (strDigits.toInt() + 1).toString()
                } else {
                    strDigits = "1" // if there is no (###) match then start with 1
                }
                newFile =
                    File(file.parent.toString() + "/" + baseName + " (" + strDigits + ")" + extension) // put the pieces back together
            }
            newFile
        } catch (e: Error) {
            file // Just overwrite the original file at this point...
        }
    }

    private fun getExtension(name: String): String {
        return name.substring(name.lastIndexOf("."))
    }
}