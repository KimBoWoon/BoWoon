package com.rss_reader.util

fun String?.encodeString(): String? = this?.replace(" ", "\u00A0")
    ?.replace("&lt;", "<")
    ?.replace("&gt;", ">")
    ?.replace("&amp;", "&")
    ?.replace("&apos;", "'")
    ?.replace("&quot;", "\"")
    ?.replace("&middot;", "．")