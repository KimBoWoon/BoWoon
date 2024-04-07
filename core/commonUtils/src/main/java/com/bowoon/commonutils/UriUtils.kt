package com.bowoon.commonutils

import android.net.Uri

fun replaceUriParameter(uri: Uri, queryPair: List<Pair<String, String>>): Uri {
    val params = uri.getQueryParameterNames()
    val newUri = uri.buildUpon().clearQuery()
    queryPair.forEach { query ->
        val param = params.find { it == query.first }
        newUri.appendQueryParameter(
            param,
            if (param == query.first) query.second else uri.getQueryParameter(param)
        )
    }
    return newUri.build()
}