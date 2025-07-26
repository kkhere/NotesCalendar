package com.kiran.notescalender

//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import java.time.LocalDate
//import java.time.YearMonth
//
//@Composable
//fun CalendarMonthGrid(date: LocalDate) {
//    val days = remember(date) { getMonthDays(date) }
//    val today = LocalDate.now()
//
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(7),
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(vertical = 8.dp),
//        userScrollEnabled = false
//    ) {
//        items(days.size) { index ->
//            val day = days[index]
//            Box(
//                modifier = Modifier
//                    .aspectRatio(1f)
//                    .padding(2.dp)
//                    .background(
//                        if (day == today) Color(0xFF3F51B5) else Color.Transparent,
//                        shape = MaterialTheme.shapes.small
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = day?.dayOfMonth?.toString() ?: "",
//                    color = if (day == today) Color.White else Color.Black,
//                    fontSize = 16.sp,
//                    fontWeight = if (day == today) FontWeight.Bold else FontWeight.Normal
//                )
//            }
//        }
//    }
//}

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MacStyleCalendar(
    month: YearMonth = YearMonth.now(),
    today: LocalDate = LocalDate.now()
) {
    val daysInMonth = month.lengthOfMonth()
    val firstDayOfWeek = month.atDay(1).dayOfWeek.value % 7 // Sunday = 0

    val weeks = mutableListOf<List<LocalDate?>>()
    var dayCounter = 1

    for (week in 0 until 6) {
        val weekDays = mutableListOf<LocalDate?>()
        for (day in 0 until 7) {
            if (week == 0 && day < firstDayOfWeek) {
                weekDays.add(null)
            } else if (dayCounter <= daysInMonth) {
                weekDays.add(month.atDay(dayCounter))
                dayCounter++
            } else {
                weekDays.add(null)
            }
        }
        weeks.add(weekDays)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        BasicText(
            text = "${month.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${month.year}",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 26.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { dayName ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    BasicText(
                        text = dayName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        weeks.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    val isToday = date == today
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .background(
                                color = if (isToday) Color.Red else Color.Transparent,
                                shape = MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(
                            text = date?.dayOfMonth?.toString() ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                color = if (isToday) Color.White else Color.Black,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }
    }
}