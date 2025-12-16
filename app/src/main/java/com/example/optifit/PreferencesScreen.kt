package com.example.optifit


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(navController: NavController) {
    // STATE MANAGEMENT
    // State to hold the text input for the fitness sections
    var fitnessGoal by remember { mutableStateOf("") }

    // Diet state to hold the "Checked" status for every options
    val dietStates = remember {
        mutableStateMapOf(
            "Vegetarian" to false,
            "Vegan" to false,
            "Kosher" to true,       // Default true
            "Halal" to false,
            "Lactose-Free" to true, // Default true
            "Gluten-Free" to false,
            "Nut Allergy" to false
        )
    }

    // START OF UI STRUCTURE
    Scaffold(
        // The top bar with a back button
        topBar = {
            TopAppBar(
                title = { Text("Your Goals & Diet", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        // Using LazyColumn to make it scrollable
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Section indicating that we have integrated smart watches (fake feature because Wizard of Oz)
            item {
                Text("Connected Apps", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color.LightGray) // Subtle border
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Side: App Name and emoji
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Using a Heart icon as a placeholder for Google Fit
                            Icon(Icons.Default.Favorite, contentDescription = "Google Fit", tint = Color.Red)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Google Fit", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                        }

                        // Right Side: Green "Connected" Status
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Connected",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF4CAF50) // Google Green
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Separator before the next section
            item {
                HorizontalDivider()
            }

            // Section on fitness goal. Made it a text input because we feed it to our AI API
            item {
                Text("Fitness Goal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "Describe your goal in detail so our AI can tailor a plan for you.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = fitnessGoal,
                    onValueChange = { newText: String -> fitnessGoal = newText },
                    label = { Text("e.g. I want to lose 10lbs and run a 5k without stopping...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp), // Make it tall
                    minLines = 3,
                    maxLines = 10
                )
            }

            // Section on nutrition preferences. Hard coded common dietary restrictions
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // We loop through the keys so we don't have to copy-paste code
                    // or you can manually list them like before if you prefer specific order.
                    val options = listOf("Vegetarian", "Vegan", "Kosher", "Halal", "Lactose-Free", "Gluten-Free", "Nut Allergy")

                    options.forEach { dietName ->
                        DietSwitch(
                            label = dietName,
                            // READ the state from our map (default to false if missing)
                            checked = dietStates[dietName] ?: false,
                            // UPDATE the state when clicked
                            onCheckedChange = { isChecked -> dietStates[dietName] = isChecked }
                        )
                    }
                }
            }

            // Save Button that navigates back to the previous screen
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.popBackStack() }, // Go back after saving
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Save Changes")
                }
            }
        }
    }
}

/**
 * DietSwitch: A custom row containing a Label and a Toggle Switch.
 * It lifts the 'onCheckedChange' event up to the parent so state can be managed there.
 */
@Composable
fun DietSwitch(
    label: String,
    checked: Boolean,
    // YOU MISSING THIS LINE IN YOUR FUNCTION DEFINITION:
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            // Now this will work because the parameter exists
            onCheckedChange = onCheckedChange
        )
    }
}