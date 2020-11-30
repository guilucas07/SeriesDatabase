package com.guilhermelucas.data.utils

fun String.removeHtmlTags(): String {
    return this.replace("<.*?>".toRegex(), "")
}
