package org.hammernshield.springbootwebquizapi.controllers

import org.hammernshield.springbootwebquizapi.model.AnswerResponseModel
import org.hammernshield.springbootwebquizapi.model.QuizDTO
import org.hammernshield.springbootwebquizapi.model.QuizModel
import org.hammernshield.springbootwebquizapi.model.QuizModelWithoutAnswer
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/quizzes")
class QuizController {

    private val quizList = mutableListOf<QuizModel>()

    private fun createQuiz() : QuizModel {
        val title = "Zelda Cell Shade"
        val text = "What Zelda video game used a cell shaded art style?"
        val options = listOf("Twilight Princess", "Ocarina of Time", "Wind Waker", "Breath of the Wild")

        return QuizModel(
            1,
            title,
            text,
            options,
            2
        )
    }

    init {
        quizList.add(createQuiz())
    }

    @GetMapping()
    fun getAllQuiz() : ResponseEntity<Any> {
        val response = quizList.map {quiz ->
            QuizModelWithoutAnswer(
                id = quiz.id,
                title = quiz.title,
                text = quiz.text,
                options = quiz.options
            )
        }

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("{quizId}")
    fun getQuiz(@PathVariable quizId: Int): ResponseEntity<Any> {
        val quizExists = quizList.indices.contains(quizId - 1)

        if (quizExists) {
            val quiz = quizList.find { it.id == quizId }
            val response = mutableMapOf(
                "id" to quiz?.id,
                "title" to quiz?.title,
                "text" to quiz?.text,
                "options" to quiz?.options
            )
            return ResponseEntity(response, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("{id}/solve")
    fun solveQuiz(
        @RequestParam answer: Int,
        @PathVariable id: Int
    ) : ResponseEntity<Any> {
        val quizExists = quizList.indices.contains(id - 1)

        if (quizExists) {
            val quiz = quizList.find { it.id == id }
            val correctAnswer = quiz?.answer == answer

            when (correctAnswer) {
                true -> {
                    val response = mutableMapOf(
                        "success" to true,
                        "feedback" to "Congratulations, you're right!"
                    )
                    return ResponseEntity(response, HttpStatus.OK)
                }
                false -> {
                    val response = mutableMapOf(
                        "success" to false,
                        "feedback" to "Wrong answer! Please, try again."
                    )
                    return ResponseEntity(response, HttpStatus.OK)
                }
            }
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping()
    fun addQuiz(
        @RequestBody quiz: QuizDTO
    ) : ResponseEntity<Any> {
        val currentIndex = quizList.size + 1
        val quizToAdd = QuizModel(
            currentIndex,
            quiz.title,
            quiz.text,
            quiz.options,
            quiz.answer
        )
        quizList.add(quizToAdd)

        return ResponseEntity(quizToAdd, HttpStatus.OK)
    }

}