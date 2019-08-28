package com.example.threedimens.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.IntentUtils
import com.example.threedimens.R


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

    fun shareImage(context: Context, uri: Uri?) {
        if (uri == null) {
            return
        }
        context.startActivity(
            Intent.createChooser(
                IntentUtils.getShareImageIntent("", uri),
                context.getString(R.string.share_to)
            )
        )
    }
}
