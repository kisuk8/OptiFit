package com.example.optifit

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("workout_store")

object WorkoutDataStore {

    private val WORKOUTS_KEY = stringPreferencesKey("workouts")
    private val gson = Gson()

    suspend fun saveWorkouts(context: Context, workouts: List<Workout>) {
        val json = gson.toJson(workouts)
        context.dataStore.edit { prefs ->
            prefs[WORKOUTS_KEY] = json
        }
    }

    suspend fun loadWorkouts(context: Context): List<Workout> {
        val prefs = context.dataStore.data.first()
        val json = prefs[WORKOUTS_KEY] ?: return emptyList()

        val type = object : TypeToken<List<Workout>>() {}.type
        return gson.fromJson(json, type)
    }
}