@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.optifit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState


@Composable
fun WorkoutTimerScreen(
    navController: NavController,
    workoutName: String
) {
    var isRunning by remember { mutableStateOf(true) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var showFeedbackDialog by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf(0) }
    var feedback by remember { mutableStateOf("") }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000)
            elapsedTime++
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Timer", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // 🟣 TIMER CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        workoutName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ⏱ TIMER DISPLAY
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            formatTime(elapsedTime),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        if (isRunning) "Workout in progress" else "Paused",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔘 ACTION BUTTONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(
                    onClick = { isRunning = !isRunning },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        if (isRunning) "Pause" else "Resume",
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        isRunning = false
                        showFeedbackDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        "Finish",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    if (showFeedbackDialog) {
        AlertDialog(
            onDismissRequest = { showFeedbackDialog = false },
            title = {
                Text(
                    "Workout Feedback",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text("How challenging was this workout?")

                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        (1..5).forEach { value ->
                            OutlinedButton(
                                onClick = { rating = value },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (rating == value)
                                        MaterialTheme.colorScheme.primary
                                    else Color.Transparent
                                )
                            ) {
                                Text(
                                    value.toString(),
                                    color = if (rating == value) Color.White else Color.Unspecified,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // 💬 Comments
                    OutlinedTextField(
                        value = feedback,
                        onValueChange = { feedback = it },
                        label = { Text("Additional comments (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = rating > 0,
                    onClick = {
                        WorkoutRepository.saveWorkout(
                            workoutName = workoutName,
                            durationSeconds = elapsedTime,
                            rating = rating,
                            feedback = feedback,
                            onSuccess = {
                                showFeedbackDialog = false
                                navController.popBackStack()
                            },
                            onError = { e ->
                                // You can show a Snackbar later
                                println("Error saving workout: ${e.message}")
                            }
                        )
                    }
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showFeedbackDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Skip")
                }
            }
        )
    }
}

fun formatTime(seconds: Long): String {
    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hrs, mins, secs)
}