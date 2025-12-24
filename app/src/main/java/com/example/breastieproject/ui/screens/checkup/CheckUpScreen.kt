import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ===== COLORS (SESUAI STYLE APP) ===== */
private val BgPink = Color(0xFFFFEDFA)
private val PrimaryPink = Color(0xFFEC7FA9)
private val SoftPink = Color(0xFFFFE4F2)
private val TextGray = Color(0xFF6F6F6F)

/* ===== DATA ===== */
data class Message(val text: String, val isUser: Boolean)
data class Quick(val text: String, val used: Boolean)

/* ===== MAIN SCREEN ===== */
@Composable
fun CheckUpScreen() {

    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<Message>()) }

    var quicks by remember {
        mutableStateOf(
            listOf(
                Quick("What causes breast pain?", false),
                Quick("How to do breast self-exam?", false),
                Quick("When should I see a doctor?", false)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPink)
    ) {

        /* ===== AI PROFILE CARD ===== */
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(PrimaryPink, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "C",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text("Carey", fontWeight = FontWeight.Bold)
                Text(
                    "AI Breast Care Assistant",
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
        }

        /* ===== CHAT AREA ===== */
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(msg)
            }
        }

        /* ===== QUICK QUESTIONS ===== */
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            quicks.forEachIndexed { index, q ->
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .background(
                            if (q.used) Color.White else PrimaryPink,
                            RoundedCornerShape(50)
                        )
                        .border(1.dp, PrimaryPink, RoundedCornerShape(50))
                        .clickable(enabled = !q.used) {
                            messages = messages +
                                    Message(q.text, true) +
                                    Message(dummyAnswer(), false)

                            quicks = quicks.toMutableList().also {
                                it[index] = q.copy(used = true)
                            }
                        }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        q.text,
                        fontSize = 12.sp,
                        color = if (q.used) PrimaryPink else Color.White
                    )
                }
            }
        }

        /* ===== INPUT AREA ===== */
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(28.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Ask Carey anything...") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(PrimaryPink, CircleShape)
                    .clickable {
                        if (input.isNotBlank()) {
                            messages = messages +
                                    Message(input, true) +
                                    Message(dummyAnswer(), false)
                            input = ""
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
            }
        }

        Text(
            "General health information, not medical advice",
            fontSize = 11.sp,
            color = TextGray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            textAlign = TextAlign.Center
        )
    }
}

/* ===== CHAT BUBBLE ===== */
@Composable
fun ChatBubble(msg: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .background(
                    if (msg.isUser) PrimaryPink else Color.White,
                    RoundedCornerShape(18.dp)
                )
                .padding(12.dp)
                .widthIn(max = 260.dp)
        ) {
            Text(
                msg.text,
                color = if (msg.isUser) Color.White else Color.Black
            )
        }
    }
}

/* ===== DUMMY AI ANSWER ===== */
private fun dummyAnswer(): String =
    "I can provide general breast health information. If symptoms persist, please consult a healthcare professional."
