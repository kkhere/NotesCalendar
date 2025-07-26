package com.kiran.notescalender

//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.*
//import androidx.compose.material.Button
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.*
//import java.time.*
//import java.time.format.TextStyle
//import java.util.*
//
//@Composable
//fun CalendarScreen() {
//    var currentMonth by remember { mutableStateOf(LocalDate.now()) }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//
//        // Header with Month/Year and nav buttons
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Button(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
//                Text("<")
//            }
//
//            Text(
//                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${currentMonth.year}",
//                style = MaterialTheme.typography.h6
//            )
//
//            Button(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
//                Text(">")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Weekday headers
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
//                Text(text = it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(4.dp))
//
//        // Days Grid
//        CalendarMonthGrid(currentMonth)
//    }
//}

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen() {
    var displayedMonth by remember { mutableStateOf(YearMonth.now()) }
    val today = LocalDate.now()
    val firstOfMonth = displayedMonth.atDay(1)
    val daysInMonth = displayedMonth.lengthOfMonth()
    val firstDayOfWeek = firstOfMonth.dayOfWeek.value % 7 // Sunday = 0

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header with arrows
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Previous Month",
                modifier = Modifier.clickable {
                    displayedMonth = displayedMonth.minusMonths(1)
                }
            )

            Text(
                text = displayedMonth.month.name.lowercase().replaceFirstChar { it.uppercase() } + " ${displayedMonth.year}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next Month",
                modifier = Modifier.clickable {
                    displayedMonth = displayedMonth.plusMonths(1)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekdays
        Row(modifier = Modifier.fillMaxWidth()) {
            DayOfWeek.values().forEach {
                Text(
                    text = it.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Dates grid
        val totalCells = daysInMonth + firstDayOfWeek
        val rows = (totalCells + 6) / 7

        Column {
            for (row in 0 until rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0..6) {
                        val dayIndex = row * 7 + col
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .border(0.5.dp, Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            if (dayIndex in firstDayOfWeek until (daysInMonth + firstDayOfWeek)) {
                                val date = dayIndex - firstDayOfWeek + 1
                                val isToday = today.dayOfMonth == date &&
                                        today.month == displayedMonth.month &&
                                        today.year == displayedMonth.year

                                if (isToday) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.Red, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$date",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Text(text = "$date", fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}