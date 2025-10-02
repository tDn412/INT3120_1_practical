package com.example.unit3_pathway1_basiccomponent

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
import com.example.unit3_pathway1_basiccomponent.ui.theme.Unit3_Pathway1_BasicComponentTheme

enum class Difficulty {
    EASY, MEDIUM, HARD
}

data class Question<T>(
    val questionText: String,
    val answer: T,
    val difficulty: Difficulty
)

interface ProgressPrintable {
    val progressText: String
    fun printProgressBar()
}

class Quiz : ProgressPrintable {
    val question1 = Question<String>("Quoth the raven ___", "nevermore", Difficulty.MEDIUM)
    val question2 = Question<Boolean>("The sky is green. True or false", false, Difficulty.EASY)
    val question3 = Question<Int>("How many days are there between full moons?", 28, Difficulty.HARD)
    override val progressText: String
        get() = "${answered} of ${total} answered"
    override fun printProgressBar() {
        repeat(Quiz.answered) { print("▓") }
        repeat(Quiz.total - Quiz.answered) { print("▒") }
        println()
        println(progressText)
    }
    companion object StudentProgress {
        var total: Int = 10
        var answered: Int = 3

    }

    fun printQuiz() {
        question1.let {
            println(it.questionText)
            println(it.answer)
            println(it.difficulty)
        }
        println()
        question2.let {
            println(it.questionText)
            println(it.answer)
            println(it.difficulty)
        }
        println()
        question3.let {
            println(it.questionText)
            println(it.answer)
            println(it.difficulty)
        }
        println()
    }
}

//val Quiz.StudentProgress.progressText: String
//    get() = "${answered} of ${total} answered"

//fun Quiz.StudentProgress.printProgressBar() {
//    repeat(Quiz.answered) { print("▓") }
//    repeat(Quiz.total - Quiz.answered) { print("▒") }
//    println()
//    println(Quiz.progressText)
//}

fun main() {
//    val question1 = Question<String>("Quoth the raven ___", "nevermore", Difficulty.MEDIUM)
//    val question2 = Question<Boolean>("The sky is green. True or false", false, Difficulty.EASY)
//    val question3 = Question<Int>("How many days are there between full moons?", 28, Difficulty.HARD)
//    println(question1.toString())
//
//    println("${Quiz.answered} of ${Quiz.total} answered.")

//    println(Quiz.progressText)
    val quiz = Quiz()
//    quiz.printProgressBar()
    quiz.printQuiz()
    Quiz().apply {
        printQuiz()
    }
}