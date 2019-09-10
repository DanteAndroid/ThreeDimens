package com.dante.threedimens.utils

import android.os.Environment
import com.blankj.utilcode.util.FileUtils
import com.dante.threedimens.data.Image
import com.dante.threedimens.ui.detail.PictureDetailFragment
import java.io.File

/**
 * @author Du Wenyu
 * 2019-09-09
 */

fun PictureDetailFragment.saveImage(image: Image, file: File): Boolean {
    val dirFile = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val dest = File(dirFile, image.type + image.url.substringAfterLast("/"))
    return FileUtils.copyFile(file, dest) && dest.length() > 100
}

fun PictureDetailFragment.getImageFile(image: Image): File {
    val dirFile = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(dirFile, image.type + image.url.substringAfterLast("/"))
}