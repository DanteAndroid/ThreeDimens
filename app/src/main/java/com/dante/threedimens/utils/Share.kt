package com.dante.threedimens.utils

import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.IntentUtils
import com.dante.threedimens.R
import java.io.File


/**
 * Util to share data, make share intent, etc.
 */
object Share {

    fun shareText(context: Context, text: String) {
        context.startActivity(
            Intent.createChooser(
                IntentUtils.getShareTextIntent(text),
                context.getString(R.string.share_to)
            )
        )
    }

    fun shareImage(context: Context, file: File) {
        context.startActivity(
            Intent.createChooser(
                IntentUtils.getShareImageIntent("", file),
                context.getString(R.string.share_to)
            )
        )
    }
}
