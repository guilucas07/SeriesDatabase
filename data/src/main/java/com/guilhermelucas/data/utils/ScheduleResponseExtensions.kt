package com.guilhermelucas.data.utils

import com.guilhermelucas.data.model.Schedule
import com.guilhermelucas.model.SeriesSchedule
import java.time.DayOfWeek
import java.util.*

fun Schedule.toSchedule(): List<SeriesSchedule> {
    return days.map {
        SeriesSchedule(DayOfWeek.valueOf(it.toUpperCase(Locale.US)), time)
    }
}
