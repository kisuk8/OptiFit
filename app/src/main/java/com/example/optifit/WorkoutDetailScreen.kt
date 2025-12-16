@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.optifit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Exercise(
    val name: String,
    val sets: String,
    val reps: String,
    val rest: String = "60s"
)

@Composable
fun WorkoutDetailScreen(navController: NavController, workoutId: String) {
    var isStarted by remember { mutableStateOf(false) }

    val workoutDetails = when (workoutId) {
        "fullbody" -> WorkoutDetail(
            name = "Full Body Workout",
            image = "💪",
            difficulty = "Moderate",
            duration = "45 min",
            xp = "+150 XP",
            description = "Complete full body strength training",
            exercises = listOf(
                Exercise("Squat", "3 sets", "10 reps"),
                Exercise("Push-Up", "3 sets", "12 reps"),
                Exercise("Bent Over Row", "3 sets", "10 reps"),
                Exercise("Dumbbell Press", "3 sets", "12 reps"),
                Exercise("Deadlift", "3 sets", "8 reps")
            )
        )
        "cardiocore" -> WorkoutDetail(
            name = "Cardio & Core",
            image = "🏃",
            difficulty = "Hard",
            duration = "30 min",
            xp = "+200 XP",
            description = "High intensity cardio with core focus",
            exercises = listOf(
                Exercise("Jump Rope", "3 sets", "30 reps"),
                Exercise("Plank", "3 sets", "45 sec"),
                Exercise("Mountain Climbers", "3 sets", "20 reps"),
                Exercise("Burpees", "3 sets", "15 reps"),
                Exercise("Russian Twists", "3 sets", "20 reps")
            )
        )
        else -> WorkoutDetail(
            name = "Upper Body",
            image = "🦾",
            difficulty = "Moderate",
            duration = "40 min",
            xp = "+175 XP",
            description = "Upper body strength and endurance",
            exercises = listOf(
                Exercise("Pull-ups", "3 sets", "8 reps"),
                Exercise("Bench Press", "4 sets", "8 reps"),
                Exercise("Barbell Rows", "3 sets", "10 reps"),
                Exercise("Shoulder Press", "3 sets", "12 reps")
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Header Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(workoutDetails.image, fontSize = 80.sp)
                }
            }

            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        workoutDetails.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        workoutDetails.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoChip(workoutDetails.difficulty, "Difficulty", modifier = Modifier.weight(1f))
                    InfoChip(workoutDetails.duration, "Duration", modifier = Modifier.weight(1f))
                    InfoChip(workoutDetails.xp, "Reward", modifier = Modifier.weight(1f))
                }
            }

            item {
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                Text(
                    "Exercises",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            items(workoutDetails.exercises.size) { index ->
                val exercise = workoutDetails.exercises[index]
                ExerciseItem(exercise, index + 1)
            }

            item {
                Button(
                    onClick = {
                        navController.navigate("timer/${workoutDetails.name}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "START WORKOUT",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class WorkoutDetail(
    val name: String,
    val image: String,
    val difficulty: String,
    val duration: String,
    val xp: String,
    val description: String,
    val exercises: List<Exercise>
)

@Composable
fun InfoChip(value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(60.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise, number: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(number.toString(), color = Color.White, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(exercise.name, fontWeight = FontWeight.Bold)
                Text(
                    "${exercise.sets} • ${exercise.reps}",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                exercise.rest,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}