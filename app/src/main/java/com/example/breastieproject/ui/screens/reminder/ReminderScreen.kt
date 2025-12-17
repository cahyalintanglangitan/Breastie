package com.example.breastieproject.screens.reminder

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
import java.util.*

// Data class untuk menyimpan jadwal
data class JadwalKesehatan(
    val id: String = UUID.randomUUID().toString(),
    val namaJadwal: String,
    val tanggal: String,
    val dokter: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun ReminderScreen() {
    var jadwal by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var dokter by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }

    // List untuk menyimpan semua jadwal
    var daftarJadwal by remember { mutableStateOf(listOf<JadwalKesehatan>()) }

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
                jadwal = jadwal,
                onJadwalChange = { jadwal = it },
                tanggal = tanggal,
                onTanggalChange = { tanggal = it },
                dokter = dokter,
                onDokterChange = { dokter = it },
                onSubmit = {
                    if (jadwal.isNotEmpty() && tanggal.isNotEmpty() && dokter.isNotEmpty()) {
                        // Simpan jadwal baru
                        val jadwalBaru = JadwalKesehatan(
                            namaJadwal = jadwal,
                            tanggal = tanggal,
                            dokter = dokter
                        )
                        daftarJadwal = daftarJadwal + jadwalBaru

                        // Tampilkan pesan sukses
                        messageText = "Jadwal berhasil ditambahkan!"
                        showMessage = true

                        // Reset form
                        jadwal = ""
                        tanggal = ""
                        dokter = ""
                    } else {
                        // Tampilkan pesan error
                        messageText = "Mohon lengkapi semua field!"
                        showMessage = true
                    }
                }
            )

            // Tampilkan daftar jadwal yang tersimpan
            if (daftarJadwal.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                DaftarJadwalCard(
                    daftarJadwal = daftarJadwal,
                    onDelete = { jadwalId ->
                        daftarJadwal = daftarJadwal.filter { it.id != jadwalId }
                        messageText = "Jadwal berhasil dihapus!"
                        showMessage = true
                    }
                )
            }
        }

        if (showMessage) {
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                action = {
                    TextButton(onClick = { showMessage = false }) {
                        Text("OK", color = Color.White)
                    }
                },
                containerColor = if (messageText.contains("berhasil")) Color(0xFF4CAF50) else Color(0xFFFF5252)
            ) {
                Text(messageText)
            }

            // Auto dismiss setelah 3 detik
            LaunchedEffect(showMessage) {
                kotlinx.coroutines.delay(3000)
                showMessage = false
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
                text = "Halo, perempuan hebat!",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Jangan lupa menambahkan jadwal baru!",
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
    jadwal: String,
    onJadwalChange: (String) -> Unit,
    tanggal: String,
    onTanggalChange: (String) -> Unit,
    dokter: String,
    onDokterChange: (String) -> Unit,
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
                text = "Form Tambah Jadwal Kesehatan",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Jadwal yang ingin ditambahkan :",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = jadwal,
                onValueChange = onJadwalChange,
                placeholder = {
                    Text("Contoh: Pemeriksaan Rutin", color = Color.White.copy(alpha = 0.7f))
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
                text = "Tanggal jadwal :",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            ) {
                OutlinedTextField(
                    value = tanggal,
                    onValueChange = { },
                    placeholder = {
                        Text("Pilih tanggal", color = Color.White.copy(alpha = 0.7f))
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
                text = "Pilihan dokter :",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = dokter,
                onValueChange = onDokterChange,
                placeholder = {
                    Text("Contoh: Dr. Sarah", color = Color.White.copy(alpha = 0.7f))
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
                    text = "+ Tambah Jadwal Baru",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                onTanggalChange(date)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
private fun DaftarJadwalCard(
    daftarJadwal: List<JadwalKesehatan>,
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
                text = "Daftar Jadwal Kesehatan",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            daftarJadwal.forEach { jadwal ->
                JadwalItem(jadwal = jadwal, onDelete = { onDelete(jadwal.id) })
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = "Total: ${daftarJadwal.size} jadwal tersimpan",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun JadwalItem(
    jadwal: JadwalKesehatan,
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
                    text = jadwal.namaJadwal,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📅 ${jadwal.tanggal}",
                    fontSize = 14.sp,
                    color = Color(0xFFEC7FA9)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "👨‍⚕️ ${jadwal.dokter}",
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