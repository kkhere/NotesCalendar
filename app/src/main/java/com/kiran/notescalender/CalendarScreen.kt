package com.kiran.notescalender

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import kotlin.math.abs

@Composable
fun CalendarScreen() {
    var displayedMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var noteText by remember { mutableStateOf("") }
    val today = LocalDate.now()
    val firstOfMonth = displayedMonth.atDay(1)
    val daysInMonth = displayedMonth.lengthOfMonth()
    val firstDayOfWeek = firstOfMonth.dayOfWeek.value % 7

    val notes = remember { mutableStateMapOf<LocalDate, String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
            .pointerInput(displayedMonth) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (abs(dragAmount) > 50) {
                        if (dragAmount > 0) {
                            displayedMonth = displayedMonth.minusMonths(1)
                        } else {
                            displayedMonth = displayedMonth.plusMonths(1)
                        }
                    }
                }
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = displayedMonth.month.name.lowercase().replaceFirstChar { it.uppercase() } + " ${displayedMonth.year}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(
                    onClick = { displayedMonth = YearMonth.now() },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Blue)
                ) {
                    Text("Today", fontWeight = FontWeight.Bold)
                }
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Month",
                    modifier = Modifier
                        .clickable { displayedMonth = displayedMonth.minusMonths(1) }
                        .padding(horizontal = 8.dp)
                )

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Month",
                    modifier = Modifier
                        .clickable { displayedMonth = displayedMonth.plusMonths(1) }
                        .padding(horizontal = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                                .height(80.dp)
                                .border(0.5.dp, Color.LightGray)
                                .clickable {
                                    if (dayIndex in firstDayOfWeek until (daysInMonth + firstDayOfWeek)) {
                                        val day = dayIndex - firstDayOfWeek + 1
                                        selectedDate = displayedMonth.atDay(day)
                                        noteText = notes[selectedDate] ?: ""
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (dayIndex in firstDayOfWeek until (daysInMonth + firstDayOfWeek)) {
                                val date = dayIndex - firstDayOfWeek + 1
                                val thisDate = displayedMonth.atDay(date)
                                val isToday = today == thisDate

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

                                    if (notes.containsKey(thisDate)) {
                                        Text(
                                            text = "\u2022",
                                            fontSize = 14.sp,
                                            color = Color.Blue
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedDate?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(Color(0xFFEFEFEF))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Note for ${it.dayOfMonth} ${it.month.name.lowercase().replaceFirstChar { c -> c.uppercase() }} ${it.year}", fontWeight = FontWeight.SemiBold)
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Notes",
                        modifier = Modifier.clickable { selectedDate = null }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Note") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    if (noteText.isNotBlank()) notes[it] = noteText else notes.remove(it)
                    selectedDate = null
                }) {
                    Text("Save Notes")
                }
            }
        }
    }
}
