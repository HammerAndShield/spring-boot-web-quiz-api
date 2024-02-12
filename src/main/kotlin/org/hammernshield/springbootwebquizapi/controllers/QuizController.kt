package org.hammernshield.springbootwebquizapi.controllers

import jakarta.validation.Valid
import dto.AnswerDTO
import dto.QuizDTO
import org.hammernshield.springbootwebquizapi.model.QuizModel
import dto.QuizDTOWithoutAnswer
import org.hammernshield.springbootwebquizapi.services.QuizService
import org.springframework.beans.factory.annotation.Autowired
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
    @Autowired private val quizService: QuizService,
) {

    @GetMapping
    fun getAllQuiz() : ResponseEntity<Any> {
        val quizList = quizService.getAllQuizzes()

        val response = quizList.map {quiz ->
            QuizDTOWithoutAnswer(
                id = quiz.id ?: 0,
                title = quiz.title,
                text = quiz.text,
                options = quiz.options
            )
        }

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("{quizId}")
    fun getQuiz(@PathVariable quizId: Long): ResponseEntity<Map<String, Any?>>? {
        val optionalQuiz = quizService.getQuizById(quizId)

        return optionalQuiz.map { quiz ->
            ResponseEntity.ok(mapOf(
                "id" to quiz.id,
                "title" to quiz.title,
                "text" to quiz.text,
                "options" to quiz.options
            ))
        }.orElseGet {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("{id}/solve")
    fun solveQuiz(
        @PathVariable id: Long,
        @RequestBody answers: AnswerDTO
    ): ResponseEntity<Map<String, Any>> { // No need for nullable return type
        val optionalQuiz = quizService.getQuizById(id)

        return optionalQuiz.map { quiz ->
            val isCorrect = quizService.areAnswersCorrect(answers.answer, quiz.answer)
            val response: Map<String, Any> = if (isCorrect) {
                mapOf("success" to true, "feedback" to "Congratulations, you're right!")
            } else {
                mapOf("success" to false, "feedback" to "Wrong answer! Please, try again.")
            }
            ResponseEntity.ok(response)
        }.orElseGet {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "Quiz not found"))
        }
    }

    @PostMapping
    fun addQuiz(
        @RequestBody @Valid quiz: QuizDTO
    ) : ResponseEntity<QuizModel> {
        val quizToAdd = QuizModel(
            title = quiz.title,
            text = quiz.text,
            options = quiz.options,
            answer = quiz.answer ?: listOf()
        )
        quizService.addQuiz(quizToAdd)

        return ResponseEntity(quizToAdd, HttpStatus.OK)
    }

}