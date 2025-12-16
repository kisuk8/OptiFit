@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.optifit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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

// -------------------- DATA MODEL --------------------
data class Workout(
    val day: String,
    val name: String,
    val time: String? = null,
    val status: String? = null,
    val id: String
)

// -------------------- MAIN SCREEN --------------------
@Composable
fun PlannerScreen(navController: NavController) {

    var selectedTab by remember { mutableStateOf(0) }
    var showAddWorkoutDialog by remember { mutableStateOf(false) }

    val tabs = listOf("Workouts", "Meals")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Planner", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (selectedTab == 0) {
                        IconButton(onClick = { showAddWorkoutDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Add Workout")
                        }
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

            // ---------- TAB SELECTOR ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, tab ->
                    Button(
                        onClick = { selectedTab = index },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {
                        Text(
                            tab,
                            color = if (selectedTab == index) Color.White else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ---------- CONTENT ----------
            when (selectedTab) {
                0 -> WorkoutsTab(
                    navController = navController,
                    showAddDialog = showAddWorkoutDialog,
                    onDismissDialog = { showAddWorkoutDialog = false }
                )
                1 -> MealsTab()
            }
        }
    }
}

// -------------------- WORKOUTS TAB --------------------
@Composable
fun WorkoutsTab(
    navController: NavController,
    showAddDialog: Boolean,
    onDismissDialog: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()

    var workouts by remember { mutableStateOf<List<Workout>>(emptyList()) }

    // LOAD workouts once
    LaunchedEffect(Unit) {
        workouts = WorkoutDataStore.loadWorkouts(context).ifEmpty {
            listOf(
                Workout("Day 1", "Full Body", status = "Complete", id = "full_body"),
                Workout("Day 2", "Cardio & Core", time = "40 min", id = "cardio_core"),
                Workout("Day 3", "Upper Body", time = "45 min", id = "upper_body")
            )
        }
    }

    // SAVE workouts whenever they change
    LaunchedEffect(workouts) {
        WorkoutDataStore.saveWorkouts(context, workouts)
    }

    if (showAddDialog) {
        AddWorkoutDialog(
            onDismiss = onDismissDialog,
            onAdd = { newWorkout ->
                workouts = workouts + newWorkout
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(workouts) { workout ->
            PlannerWorkoutCard(
                day = workout.day,
                name = workout.name,
                status = workout.status,
                time = workout.time,
                navController = navController,
                workoutId = workout.id
            )
        }
    }
}

// -------------------- ADD WORKOUT DIALOG --------------------
@Composable
fun AddWorkoutDialog(
    onDismiss: () -> Unit,
    onAdd: (Workout) -> Unit
) {

    var day by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Workout") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(value = day, onValueChange = { day = it }, label = { Text("Day") })
                TextField(value = name, onValueChange = { name = it }, label = { Text("Workout Name") })
                TextField(value = time, onValueChange = { time = it }, label = { Text("Duration (optional)") })
            }
        },
        confirmButton = {
            TextButton(
                enabled = day.isNotBlank() && name.isNotBlank(),
                onClick = {
                    onAdd(
                        Workout(
                            day = day,
                            name = name,
                            time = if (time.isBlank()) null else time,
                            id = name.lowercase().replace(" ", "_")
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// -------------------- MEALS TAB --------------------
@Composable
fun MealsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Today's Meals", fontWeight = FontWeight.Bold, fontSize = 20.sp) }

        item {
            MealCard("Breakfast", "Oatmeal & Berries", "350 cal")
            MealCard("Lunch", "Chicken Salad", "520 cal")
            MealCard("Dinner", "Grilled Fish & Veggies", "450 cal")
            MealCard("Snack", "Protein Bar", "200 cal")
        }
    }
}

// -------------------- COMPONENTS --------------------
@Composable
fun PlannerWorkoutCard(
    day: String,
    name: String,
    status: String?,
    time: String?,
    navController: NavController,
    workoutId: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(day, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(name, fontWeight = FontWeight.Bold)
                    if (status != null) Text(status, color = MaterialTheme.colorScheme.primary)
                    else if (time != null) Text(time)
                }
            }
        }
    }
}

@Composable
fun MealCard(mealType: String, name: String, calories: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(mealType, fontWeight = FontWeight.Bold)
            Text(name, style = MaterialTheme.typography.labelSmall)
        }
        Text(calories, fontWeight = FontWeight.Bold)
    }
}