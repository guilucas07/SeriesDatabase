package com.guilhermelucas.model

import java.time.DayOfWeek

data class SeriesSchedule(
    val daysOfWeek: List<DayOfWeek>,
    val time: String
)
