package com.rakamin.newsapp.ui.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String?.toFormattedDate(): String {
    if (this.isNullOrBlank()) return "Unknown date"

    return try {
        val instant = Instant.parse(this)

        val formatter = DateTimeFormatter
            .ofPattern("dd MMM yyyy, HH:mm", Locale.getDefault())
            .withZone(ZoneId.systemDefault())

        formatter.format(instant)
    } catch (exception: Exception) {
        this
    }
}
