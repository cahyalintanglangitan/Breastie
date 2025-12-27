package com.example.breastieproject.ui.screens.reminder

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.breastieproject.viewmodels.ReminderViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel = viewModel()
) {
    val reminders by viewModel.reminders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Local form states
    var scheduleName by remember { mutableStateOf("") }
    var scheduleDate by remember { mutableStateOf("") }
    var doctorName by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFB8E0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            WelcomeCard()
            Spacer(modifier = Modifier.height(24.dp))

            // ✅ UPDATED: Dynamic DateSelector with reminders
            DateSelector(reminders = reminders)

            Spacer(modifier = Modifier.height(24.dp))

            FormCard(
                scheduleName = scheduleName,
                onScheduleNameChange = { scheduleName = it },
                scheduleDate = scheduleDate,
                onScheduleDateChange = { scheduleDate = it },
                doctorName = doctorName,
                onDoctorNameChange = { doctorName = it },
                onSubmit = {
                    if (scheduleName.isNotEmpty() && scheduleDate.isNotEmpty() && doctorName.isNotEmpty()) {
                        viewModel.addReminder(
                            name = scheduleName,
                            date = scheduleDate,
                            doctor = doctorName,
                            onSuccess = {
                                messageText = "Schedule added successfully!"
                                showMessage = true

                                // Reset form
                                scheduleName = ""
                                scheduleDate = ""
                                doctorName = ""
                            }
                        )
                    } else {
                        messageText = "Please fill all fields!"
                        showMessage = true
                    }
                }
            )

            if (reminders.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                ScheduleListCard(
                    schedules = reminders,
                    onDelete = { reminderId ->
                        viewModel.deleteReminder(
                            reminderId = reminderId,
                            onSuccess = {
                                messageText = "Schedule deleted successfully!"
                                showMessage = true
                            }
                        )
                    }
                )
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    color = Color.White
                )
            }
        }

        if (showMessage || errorMessage != null) {
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                action = {
                    TextButton(onClick = {
                        showMessage = false
                        viewModel.clearError()
                    }) {
                        Text("OK", color = Color.White)
                    }
                },
                containerColor = if (messageText.contains("successfully"))
                    Color(0xFF4CAF50) else Color(0xFFFF5252)
            ) {
                Text(errorMessage ?: messageText)
            }

            LaunchedEffect(showMessage, errorMessage) {
                kotlinx.coroutines.delay(3000)
                showMessage = false
                viewModel.clearError()
            }
        }
    }
}

// ========================================
// ✅ NEW: DYNAMIC DATE SELECTOR
// ========================================

/**
 * Data class for calendar date
 */
private data class CalendarDate(
    val dayOfMonth: Int,
    val dayOfWeek: String,
    val month: String,
    val year: Int,
    val isToday: Boolean,
    val fullDate: String  // "28/12/2024" format
)

/**
 * Dynamic Date Selector with real dates and schedule indicators
 */
@Composable
private fun DateSelector(
    reminders: List<com.example.breastieproject.data.model.Reminder>
) {
    // ✅ Generate dates dynamically (7 days before + today + 7 days after)
    val dates = remember {
        generateCalendarDates(daysRange = 14)  // Show 2 weeks
    }

    // ✅ Create map of dates with reminders
    val datesWithReminders = remember(reminders) {
        reminders.map { it.date }.toSet()
    }

    // ✅ Scroll state with auto-scroll to today
    val scrollState = rememberScrollState()
    val todayIndex = dates.indexOfFirst { it.isToday }

    // ✅ Auto-scroll to today on first load
    LaunchedEffect(Unit) {
        if (todayIndex >= 0) {
            scrollState.animateScrollTo(todayIndex * 68) // 60dp width + 8dp spacing
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        dates.forEachIndexed { index, date ->
            DateItem(
                date = date,
                hasSchedule = datesWithReminders.contains(date.fullDate)
            )
        }
    }
}

/**
 * Generate calendar dates dynamically
 * @param daysRange: Total days to show (e.g., 14 = 2 weeks)
 */
private fun generateCalendarDates(daysRange: Int = 14): List<CalendarDate> {
    val calendar = Calendar.getInstance()
    val today = calendar.clone() as Calendar
    val dates = mutableListOf<CalendarDate>()

    // Start from (daysRange/2) days ago
    calendar.add(Calendar.DAY_OF_MONTH, -(daysRange / 2))

    // Generate dates
    repeat(daysRange) {
        val isToday = calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH) &&
                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)

        val dayOfWeek = SimpleDateFormat("EEE", Locale.ENGLISH).format(calendar.time)
        val month = SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.time)
        val fullDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)

        dates.add(
            CalendarDate(
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
                dayOfWeek = dayOfWeek,
                month = month,
                year = calendar.get(Calendar.YEAR),
                isToday = isToday,
                fullDate = fullDate
            )
        )

        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return dates
}

/**
 * Enhanced Date Item with schedule indicator
 */
@Composable
private fun DateItem(
    date: CalendarDate,
    hasSchedule: Boolean
) {
    // ✅ Dynamic colors based on state
    val backgroundColor = if (date.isToday) {
        Color(0xFFEC7FA9)  // Pink for today
    } else {
        Color(0xFFFFE4E9)  // Light pink for other days
    }

    val textColor = if (date.isToday) {
        Color.White
    } else {
        Color.Black
    }

    Box(
        modifier = Modifier
            .width(60.dp)
            .height(if (date.isToday) 80.dp else 70.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // ✅ Day number
                Text(
                    text = date.dayOfMonth.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                // ✅ Day of week
                Text(
                    text = date.dayOfWeek,
                    fontSize = 12.sp,
                    color = textColor
                )

                // ✅ Month label (only for today)
                if (date.isToday) {
                    Text(
                        text = date.month,
                        fontSize = 10.sp,
                        color = Color.White
                    )
                }
            }
        }

        // ✅ Schedule indicator (dot badge)
        if (hasSchedule) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (date.isToday) Color.White else Color(0xFFEC7FA9)
                    )
            )
        }
    }
}

// ========================================
// REST OF THE CODE (UNCHANGED)
// ========================================

@Composable
private fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Hello, amazing woman!",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Don't forget to add a new schedule!",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormCard(
    scheduleName: String,
    onScheduleNameChange: (String) -> Unit,
    scheduleDate: String,
    onScheduleDateChange: (String) -> Unit,
    doctorName: String,
    onDoctorNameChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Add Health Schedule Form",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Schedule to add:",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = scheduleName,
                onValueChange = onScheduleNameChange,
                placeholder = {
                    Text("e.g., Routine Checkup", color = Color.White.copy(alpha = 0.7f))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFEC7FA9),
                    unfocusedContainerColor = Color(0xFFEC7FA9),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(25.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Schedule date:",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            ) {
                OutlinedTextField(
                    value = scheduleDate,
                    onValueChange = { },
                    placeholder = {
                        Text("Select date", color = Color.White.copy(alpha = 0.7f))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = Color(0xFFEC7FA9),
                        disabledTextColor = Color.White,
                        disabledBorderColor = Color.Transparent,
                        disabledPlaceholderColor = Color.White.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(25.dp),
                    trailingIcon = {
                        Text("📅", fontSize = 20.sp)
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Doctor's name:",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = doctorName,
                onValueChange = onDoctorNameChange,
                placeholder = {
                    Text("e.g., Dr. Sarah", color = Color.White.copy(alpha = 0.7f))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFEC7FA9),
                    unfocusedContainerColor = Color(0xFFEC7FA9),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(25.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC05E88)
                )
            ) {
                Text(
                    text = "+ Add New Schedule",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                onScheduleDateChange(date)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
private fun ScheduleListCard(
    schedules: List<com.example.breastieproject.data.model.Reminder>,
    onDelete: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Health Schedule List",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            schedules.forEach { schedule ->
                ScheduleItem(
                    schedule = schedule,
                    onDelete = { onDelete(schedule.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = "Total: ${schedules.size} schedules saved",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun ScheduleItem(
    schedule: com.example.breastieproject.data.model.Reminder,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE4E9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${schedule.daysUntil}: ${schedule.name}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📅 ${schedule.date}",
                    fontSize = 14.sp,
                    color = Color(0xFFEC7FA9)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "👨‍⚕️ ${schedule.doctor}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onDelete) {
                Text(
                    text = "🗑️",
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
private fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    "%02d/%02d/%04d",
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )
                onDateSelected(formattedDate)
            },
            year,
            month,
            day
        )
    }

    DisposableEffect(Unit) {
        datePickerDialog.setOnDismissListener {
            onDismiss()
        }
        datePickerDialog.show()
        onDispose {
            datePickerDialog.dismiss()
        }
    }
}