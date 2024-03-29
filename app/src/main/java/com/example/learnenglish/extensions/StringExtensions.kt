package com.example.learnenglish.extensions

import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar

@RequiresApi(Build.VERSION_CODES.N)
fun ActionBar.setHtmlTitle(title: String, color: Int) {
    this.title = Html.fromHtml(
        "<font color=\"$color\">$title</font>",
        Html.FROM_HTML_MODE_LEGACY
    )
}
