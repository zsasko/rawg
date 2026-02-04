package com.zsasko.rawg.ui.common


fun String.stripHtmlTags(): String {
    return this.replace(Regex(STRIP_HTML_REGEX), "")
}