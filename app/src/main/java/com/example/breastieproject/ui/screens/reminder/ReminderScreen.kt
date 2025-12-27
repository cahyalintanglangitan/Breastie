package com.example.breastieproject.ui.screens.reminder

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.breastieproject.viewmodels.ReminderViewModel
import java.util.*

@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel = viewModel()
) {
    val reminders by viewModel.reminders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Local form states
    var scheduleName by remember { mutableStateOf("") }      // ✅ jadwal → scheduleName
    var scheduleDate by remember { mutableStateOf("") }      // ✅ tanggal → scheduleDate
    var doctorName by remember { mutableStateOf("") }        // ✅ dokter → doctorName
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
            DateSelector()
            Spacer(modifier = Modifier.height(24.dp))

            FormCard(
                scheduleName = scheduleName,                 // ✅ Updated
                onScheduleNameChange = { scheduleName = it }, // ✅ Updated
                scheduleDate = scheduleDate,                 // ✅ Updated
                onScheduleDateChange = { scheduleDate = it }, // ✅ Updated
                doctorName = doctorName,                     // ✅ Updated
                onDoctorNameChange = { doctorName = it },    // ✅ Updated
                onSubmit = {
                    if (scheduleName.isNotEmpty() && scheduleDate.isNotEmpty() && doctorName.isNotEmpty()) {
                        viewModel.addReminder(
                            name = scheduleName,
                            date = scheduleDate,
                            doctor = doctorName,
                            onSuccess = {
                                messageText = "Schedule added successfully!" // ✅ English
                                showMessage = true

                                // Reset form
                                scheduleName = ""
                                scheduleDate = ""
                                doctorName = ""
                            }
                        )
                    } else {
                        messageText = "Please fill all fields!" // ✅ English
                        showMessage = true
                    }
                }
            )

            if (reminders.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                ScheduleListCard(                             // ✅ DaftarJadwalCard → ScheduleListCard
                    schedules = reminders,                    // ✅ daftarJadwal → schedules
                    onDelete = { reminderId ->
                        viewModel.deleteReminder(
                            reminderId = reminderId,
                            onSuccess = {
                                messageText = "Schedule deleted successfully!" // ✅ English
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
                containerColor = if (messageText.contains("successfully")) // ✅ berhasil → successfully
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
                text = "Hello, amazing woman!", // ✅ Halo, perempuan hebat!
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Don't forget to add a new schedule!", // ✅ Jangan lupa menambahkan jadwal baru!
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DateSelector() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DateItem("16", "Sun", false)
        DateItem("17", "Mon", true, "Dec")
        DateItem("18", "Tue", false)
        DateItem("19", "Wed", false)
        DateItem("20", "Thu", false)
        DateItem("21", "Fri", false)
        DateItem("22", "Sat", false)
    }
}

@Composable
private fun DateItem(date: String, day: String, isSelected: Boolean, month: String? = null) {
    Card(
        modifier = Modifier
            .width(60.dp)
            .height(if (isSelected) 80.dp else 70.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFEC7FA9) else Color(0xFFFFE4E9)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = date,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color.Black
            )
            Text(
                text = day,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else Color.Black
            )
            if (month != null) {
                Text(
                    text = month,
                    fontSize = 10.sp,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormCard(
    scheduleName: String,              // ✅ jadwal → scheduleName
    onScheduleNameChange: (String) -> Unit,
    scheduleDate: String,              // ✅ tanggal → scheduleDate
    onScheduleDateChange: (String) -> Unit,
    doctorName: String,                // ✅ dokter → doctorName
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
                text = "Add Health Schedule Form", // ✅ Form Tambah Jadwal Kesehatan
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Schedule to add:", // ✅ Jadwal yang ingin ditambahkan:
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = scheduleName,
                onValueChange = onScheduleNameChange,
                placeholder = {
                    Text("e.g., Routine Checkup", color = Color.White.copy(alpha = 0.7f)) // ✅ Contoh: Pemeriksaan Rutin
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
                text = "Schedule date:", // ✅ Tanggal jadwal:
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
                        Text("Select date", color = Color.White.copy(alpha = 0.7f)) // ✅ Pilih tanggal
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
                text = "Doctor's name:", // ✅ Pilihan dokter:
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = doctorName,
                onValueChange = onDoctorNameChange,
                placeholder = {
                    Text("e.g., Dr. Sarah", color = Color.White.copy(alpha = 0.7f)) // ✅ Contoh: Dr. Sarah
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
                    text = "+ Add New Schedule", // ✅ + Tambah Jadwal Baru
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
private fun ScheduleListCard(      // ✅ DaftarJadwalCard → ScheduleListCard
    schedules: List<com.example.breastieproject.data.model.Reminder>, // ✅ daftarJadwal → schedules
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
                text = "Health Schedule List", // ✅ Daftar Jadwal Kesehatan
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            schedules.forEach { schedule -> // ✅ jadwal → schedule
                ScheduleItem(           // ✅ JadwalItem → ScheduleItem
                    schedule = schedule,
                    onDelete = { onDelete(schedule.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = "Total: ${schedules.size} schedules saved", // ✅ Total: X jadwal tersimpan
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun ScheduleItem(           // ✅ JadwalItem → ScheduleItem
    schedule: com.example.breastieproject.data.model.Reminder, // ✅ jadwal → schedule
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