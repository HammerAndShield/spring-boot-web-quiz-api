package org.hammernshield.springbootwebquizapi.controllers

import jakarta.validation.Valid
import org.hammernshield.springbootwebquizapi.model.AnswerDTO
import org.hammernshield.springbootwebquizapi.model.QuizDTO
import org.hammernshield.springbootwebquizapi.model.QuizModel
import org.hammernshield.springbootwebquizapi.model.QuizDTOWithoutAnswer
import org.hammernshield.springbootwebquizapi.services.QuizService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("api/quizzes")
class QuizController(
    val quizService: QuizService
) {

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
            listOf(2)
        )
    }

    init {
        quizList.add(createQuiz())
    }

    @GetMapping
    fun getAllQuiz() : ResponseEntity<Any> {
        val response = quizList.map {quiz ->
            QuizDTOWithoutAnswer(
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
        @PathVariable id: Int,
        @RequestBody @Valid answers: AnswerDTO
    ) : ResponseEntity<Any> {
        val quizExists = quizList.indices.contains(id - 1)

        if (quizExists) {
            val quiz = quizList.find { it.id == id }
            val answerList = quiz?.answer ?: listOf()
            val answerCorrect = quizService.areAnswersCorrect(answers.answer, answerList)

            when (answerCorrect) {
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

    @PostMapping
    fun addQuiz(
        @RequestBody @Valid quiz: QuizDTO
    ) : ResponseEntity<QuizModel> {
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