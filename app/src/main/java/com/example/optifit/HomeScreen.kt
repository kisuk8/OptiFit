@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.optifit

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        // Containers the app title and mascot image
        topBar = {
            TopAppBar(
                title = {
                    // We use a Row to place the Image and Text side-by-side
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Need a box to group image and white circle behind mascot
                        Box(
                            modifier = Modifier
                                // Spacing between circle and text
                                .padding(end = 8.dp)
                                // Define the total size of the circle + turn square into circle
                                .size(40.dp)
                                .clip(CircleShape)
                                // Fill circle with color + add transparency
                                .background(colorResource(id = R.color.black).copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center // Ensure the image inside is centered
                        ) {
                            // Adding the mascot in the cirlce
                            Image(
                                // File was added in res/drawable
                                // Source: https://www.pixeltrue.com/premium/animals-and-pets-illustration-pack
                                painter = painterResource(id = R.drawable.mascot_german_shepherd),
                                contentDescription = "App Logo",
                                // Make the image slightly smaller than the box so you can see the circle border
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        // The title of the application at the top
                        Text("OptiFit", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        // Different sections of the app at the bottom. Routes have been set in NavController.kt
        bottomBar = {
            NavigationBar {
                // Home
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { }
                )
                // Profile / Preferences
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { navController.navigate("preferences") }
                )
                // Chat
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = false,
                    onClick = { navController.navigate("chat") }
                )
                // Planner
                NavigationBarItem(
                    icon = { Icon(Icons.Default.EventNote, contentDescription = "Planner") },
                    label = { Text("Planner") },
                    selected = false,
                    onClick = { navController.navigate("planner") }
                )
            }
        }
    ) { paddingValues ->
        // Using the Lazy Column to make make scrollable
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Level of the user and XP
            item {
                LevelCard()
            }

            // START OF THE STATS SECTION
            item {
                Text(
                    "Today's Stats",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            // Cards for calories and steps within stats section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("Calories", "520/2500", MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                    StatCard("Steps", "8,234", MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
                }
            }

            // Cards for streak and water within stats section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("Streak", "7 days 🔥", MaterialTheme.colorScheme.errorContainer, Modifier.weight(1f))
                    StatCard("Water", "6/8 cups", MaterialTheme.colorScheme.primaryContainer, Modifier.weight(1f))
                }
            }


            // Calorie dashboard
            item {
                Spacer(modifier = Modifier.height(8.dp))
                CaloriePredictionCard()
            }

            // Goal timeline
            item {
                Spacer(modifier = Modifier.height(8.dp))
                GoalTimelineCard()
            }

            // Separator before the next section
            item {
                HorizontalDivider()
            }

            // START OF RECOMMENDED WORKOUTS SECTIONS
            item {
                Text(
                    "Recommended Workouts",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            // First workout in recommended workouts section
            item {
                WorkoutCard(
                    name = "Full Body Workout",
                    difficulty = "Moderate",
                    duration = "45 min",
                    xp = "+150 XP",
                    navController = navController,
                    workoutId = "fullbody"
                )
            }

            // Second workout in recommeded workouts section
            item {
                WorkoutCard(
                    name = "Cardio & Core",
                    difficulty = "Hard",
                    duration = "30 min",
                    xp = "+200 XP",
                    navController = navController,
                    workoutId = "cardiocore"
                )
            }
            // Separator before the next section
            item {
                HorizontalDivider()
            }

            // Challenges section with Challenges title + mascot + challenge cards
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 0.dp),
                    verticalAlignment = Alignment.CenterVertically // This aligns the text and image centers
                )
                {
                    // Subheading
                    Text(
                        "Challenges",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    // Space between title and image
                    Spacer(modifier = Modifier.width(60.dp))

                    // Adding the mascot (with bubble included in the png)
                    Image(
                        painter = painterResource(id = R.drawable.mascot_german_shepherd_motivation),
                        contentDescription = "Mascot",
                        // Adjust this height to match your text size nicely
                        modifier = Modifier.height(100.dp),
                        contentScale = ContentScale.FillHeight
                    )
                }
                ChallengeCard("7-Day Consistency", "Complete 1 workout daily", "3/7 completed", 0.43f)
                ChallengeCard("Calorie Master", "Stay under 2,500 cal", "5/7 days", 0.71f)
            }
        }
    }
}
@Composable
fun LevelCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Level 5", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(
                    "2,450 XP / 3,000 XP",
                    style = MaterialTheme.typography.labelSmall
                )
                LinearProgressIndicator(
                    progress = 0.82f,
                    modifier = Modifier
                        .width(120.dp)
                        .padding(top = 8.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = "Trophy",
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

/**
 * StatCard: A simple square card displaying a label and a large value.
 * Used for Steps, Calories, Water, etc.
 */
@Composable
fun StatCard(label: String, value: String, backgroundColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

/**
 * ChallengeCard: Shows a specific challenge with a progress bar
 */
// Function that we can use to create a challenge card easily
@Composable
fun ChallengeCard(title: String, description: String, progress: String, progressValue: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold)
                    Text(description, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
                }
                Text(progress, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            LinearProgressIndicator(
                progress = progressValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
        }
    }
}

/**
 * WorkoutCard: Displays workout details and navigates to the workout screen when clicked.
 */
@Composable
fun WorkoutCard(
    name: String,
    difficulty: String,
    duration: String,
    xp: String,
    navController: NavController,
    workoutId: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("workout/$workoutId") },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(difficulty, style = MaterialTheme.typography.labelSmall)
                    Text(duration, style = MaterialTheme.typography.labelSmall)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(xp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        }
    }
}
/**
 * CaloriePredictionCard:
 * Displays a dynamic dashboard with a bar graph comparing Actual Calories vs Predicted Calories vs Goal.
 * It uses stacked Boxes to create the bar graph effect because it is Wizard of Oz currently
 */
@Composable
fun CaloriePredictionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Calorie Pacer", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                "You are under budget!",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // The Graph Container
            Box(modifier = Modifier.fillMaxWidth().height(30.dp)) {
                // 1. Total Daily Budget (Background Bar) - 2500
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                // 2. Predicted / Paced Marker (The new 2105 value)
                // Math: 2105 / 2500 = 0.842 (84.2%)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.842f) // UPDATED FRACTION
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 15.dp, bottomStart = 15.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                )

                // 3. Actual Progress (Solid Bar) - 520 cal
                // Math: 520 / 2500 = 0.208
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.208f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )

                // 4. White Marker Lines
                Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Box(modifier = Modifier.fillMaxHeight().width(2.dp).background(Color.White))
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            // Labels under the graph
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("520 (Actual)", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)

                // UPDATED TEXT LABEL
                Text("2105 (Predicted)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Text("2500 Goal", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
/**
 * GoalTimelineCard:
 * Visualizes a weekly goal (e.g., "Work out 4 times a week").
 * It uses the 'DayMarker' helper to generate the 7 circles (Mon-Sun).
 */
@Composable
fun GoalTimelineCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 1. Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Weekly Goal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                // The status chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        "3 / 4 completed",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // 2. The Disclaimer (Requested)
            Text(
                "Calendar week starts on Monday.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // 3. The Visual Week Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // We manually place the 7 days for the demo
                // Status: 1 = Done (Green), 2 = Target (Outline/Focus), 3 = Future (Gray)

                DayMarker("M", true)   // Done
                DayMarker("T", true)   // Done
                DayMarker("W", true)   // Done
                DayMarker("T", false, isTarget = true) // <-- The 4th day target!
                DayMarker("F", false)
                DayMarker("S", false)
                DayMarker("S", false)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Motivational Text
            Text(
                "You're crushing it! Just 1 more workout to hit your weekly target.",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}

/**
 * DayMarker: Helper for the GoalTimelineCard.
 * Draws a circle that is either Filled (Done), Outlined (Target), or Gray (Future).
 */
@Composable
fun DayMarker(day: String, isDone: Boolean, isTarget: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isDone -> MaterialTheme.colorScheme.primary // Green/Filled
                        isTarget -> Color.Transparent // Empty but outlined
                        else -> MaterialTheme.colorScheme.surfaceVariant // Gray background
                    }
                )
                // Add a border if it's the Target day or a Future day
                .then(
                    if (isTarget) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            } else if (isTarget) {
                // If it's the target, show an empty ring
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = day,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if(isTarget) FontWeight.Bold else FontWeight.Normal
        )
    }
}
