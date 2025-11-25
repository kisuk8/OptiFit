@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.optifit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

@Composable
fun PlannerScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
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
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
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
            // Tab Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, tab ->
                    Button(
                        onClick = { selectedTab = index },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceContainer
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            tab,
                            color = if (selectedTab == index) Color.White else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Content
            when (selectedTab) {
                0 -> WorkoutsTab(navController)
                1 -> MealsTab()
            }
        }
    }
}

@Composable
fun WorkoutsTab(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Today's Workout",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            PlannerWorkoutCard(
                day = "Day 1",
                name = "Full Body",
                status = "Complete",
                navController = navController,
                workoutId = "fullbody"
            )
        }

        item {
            Text(
                "This Week",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            PlannerWorkoutCard(
                day = "Day 2",
                name = "CARDIO & CORE",
                status = null,
                time = "04:90",
                navController = navController,
                workoutId = "cardiocore"
            )
        }

        item {
            PlannerWorkoutCard(
                day = "Day 3",
                name = "UPPER BODY",
                status = null,
                time = "04:00",
                navController = navController,
                workoutId = "upperbody"
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MealsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Today's Meals",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            MealCard("Breakfast", "Oatmeal & Berries", "350 cal")
            MealCard("Lunch", "Chicken Salad", "520 cal")
            MealCard("Dinner", "Grilled Fish & Veggies", "450 cal")
            MealCard("Snack", "Protein Bar", "200 cal")
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Daily Total", fontWeight = FontWeight.Bold)
                    Text("1,520 / 2,500 cal", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top = 4.dp))
                    LinearProgressIndicator(
                        progress = 0.61f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PlannerWorkoutCard(
    day: String,
    name: String,
    status: String? = null,
    time: String? = null,
    navController: NavController,
    workoutId: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(day, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Column {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    if (status != null) {
                        Text(status, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    } else if (time != null) {
                        Text(time, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun MealCard(mealType: String, name: String, calories: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(mealType, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(name, style = MaterialTheme.typography.labelSmall)
        }
        Text(calories, fontWeight = FontWeight.Bold)
    }
}