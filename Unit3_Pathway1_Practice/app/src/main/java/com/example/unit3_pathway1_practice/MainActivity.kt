package com.example.unit3_pathway1_practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.unit3_pathway1_practice.ui.theme.Unit3_Pathway1_PracticeTheme
import kotlin.compareTo

enum class Daypart {
    MORNING,
    AFTERNOON,
    EVENING,
}

data class Event(
    val title: String,
    val description: String? = null,
    val daypart: Daypart,
    val durationInMinutes: Int,
)

//7
val Event.durationOfEvent: String
    get() = if (this.durationInMinutes < 60) {
        "short"
    } else {
        "long"
    }

fun main() {
    val event = Event(
        title="Study Kotlin",
        description="Commit to studying Kotlin at least 15 minutes per day.",
        daypart=Daypart.AFTERNOON,
        durationInMinutes=15)
    println("Event: ${event.title}, Daypart: ${event.daypart}, Duration: ${event.durationInMinutes}")
    val event1 = Event(title = "Wake up", description = "Time to get up", daypart = Daypart.MORNING, durationInMinutes = 0)
    val event2 = Event(title = "Eat breakfast", daypart = Daypart.MORNING, durationInMinutes = 15)
    val event3 = Event(title = "Learn about Kotlin", daypart = Daypart.AFTERNOON, durationInMinutes = 30)
    val event4 = Event(title = "Practice Compose", daypart = Daypart.AFTERNOON, durationInMinutes = 60)
    val event5 = Event(title = "Watch latest DevBytes video", daypart = Daypart.AFTERNOON, durationInMinutes = 10)
    val event6 = Event(title = "Check out latest Android Jetpack library", daypart = Daypart.EVENING, durationInMinutes = 45)
    val events = mutableListOf<Event>(event1, event2, event3, event4, event5, event6)
    //4
    println("You have ${events.filter { it.durationInMinutes < 60 }.size} short events.")
    //5
    val groupedEvents = events.groupBy { it.daypart }
    groupedEvents.forEach { (daypart, events) ->
        println("$daypart: ${events.size} events")
    }
    //6
    println("Last event of the day: ${events.last().title}")
    //7
    println("Duration of first event of the day: ${events[0].durationOfEvent}")
}