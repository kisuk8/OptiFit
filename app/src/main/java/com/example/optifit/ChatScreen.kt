@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.optifit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.optifit.ai.ChatGPT
import kotlinx.coroutines.launch

// -------------------- DATA MODEL --------------------
data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

// -------------------- CHAT SCREEN --------------------
@Composable
fun ChatScreen(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // ⚠️ Use BuildConfig.OPENAI_API_KEY in real app
    val chatGPT = remember { ChatGPT("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") }

    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    "Hi, I'm Flex! Upload a workout video and I’ll analyze your form 💪",
                    false
                )
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }

    // ---------------- VIDEO PICKER ----------------
    val videoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            selectedVideoUri = uri
        }

    // ---------------- HANDLE VIDEO UPLOAD ----------------
    LaunchedEffect(selectedVideoUri) {
        selectedVideoUri?.let { uri ->
            messages = messages + ChatMessage(
                "📹 Workout video uploaded. Analyzing your form...",
                true
            )

            scope.launch {
                // 1️⃣ Analyze video locally (post-set)
                val summary =
                    WorkoutVideoAnalyzer.analyze(context, uri)

                messages = messages + ChatMessage(
                    "📊 Analysis complete. Sending to coach...",
                    false
                )

                // 2️⃣ Build coaching prompt
                val prompt = """
                    You are a certified personal trainer.
                    Analyze the workout data below and give:
                    - Main form issues
                    - Injury risks
                    - Coaching cues
                    - One drill to improve

                    Be concise and encouraging.

                    $summary
                """.trimIndent()

                // 3️⃣ Send to ChatGPT
                val response = chatGPT.ask(prompt)

                messages = messages + ChatMessage(response, false)

                selectedVideoUri = null
            }
        }
    }

    // ---------------- UI ----------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // ---------------- CHAT MESSAGES ----------------
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Mascot header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(
                                            colorResource(id = R.color.black)
                                                .copy(alpha = 0.5f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(
                                            id = R.drawable.mascot_german_shepherd
                                        ),
                                        contentDescription = "AI Mascot",
                                        modifier = Modifier.size(78.dp)
                                    )
                                }
                                Text(
                                    text = "Hi, I'm Flex!\nUpload a workout video and I’ll coach you.",
                                    modifier = Modifier.padding(start = 12.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Messages
                items(messages) { message ->
                    if (message.isUser) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(12.dp)
                                    .widthIn(max = 280.dp)
                            ) {
                                Text(message.text, color = Color.White)
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(12.dp)
                                    .widthIn(max = 280.dp)
                            ) {
                                Text(message.text)
                            }
                        }
                    }
                }
            }

            // ---------------- INPUT BAR ----------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // 📎 Video upload
                IconButton(onClick = { videoPickerLauncher.launch("video/*") }) {
                    Text("📎", fontSize = 22.sp)
                }

                // Text input
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Message...") },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp)),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor =
                            MaterialTheme.colorScheme.surfaceContainer,
                        focusedContainerColor =
                            MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )
                )

                // Send
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val userText = inputText
                            inputText = ""

                            messages = messages + ChatMessage(userText, true)

                            scope.launch {
                                val response = chatGPT.ask(userText)
                                messages = messages + ChatMessage(response, false)
                            }
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Send, null, tint = Color.White)
                }
            }
        }
    }
}