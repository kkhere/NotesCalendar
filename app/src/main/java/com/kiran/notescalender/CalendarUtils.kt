package com.kiran.notescalender

import java.time.LocalDate
import java.time.YearMonth

fun getMonthDays(date: LocalDate): List<LocalDate?> {
    val yearMonth = YearMonth.from(date)
    val firstDayOfMonth = date.withDayOfMonth(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Sunday = 0

    val days = mutableListOf<LocalDate?>()

    // Empty cells before 1st day
    for (i in 0 until firstDayOfWeek) {
        days.add(null)
    }

    for (i in 1..daysInMonth) {
        days.add(LocalDate.of(date.year, date.month, i))
    }

    return days
}
