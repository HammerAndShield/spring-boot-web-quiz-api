package org.hammernshield.springbootwebquizapi.services

import org.hammernshield.springbootwebquizapi.model.QuizModel
import org.hammernshield.springbootwebquizapi.repository.QuizModelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class QuizService(@Autowired val quizModelRepository: QuizModelRepository) {

    fun areAnswersCorrect(
        userAnswers: Set<Int>,
        correctAnswers: List<Int>
    ): Boolean {
        val correctAnswersSet = correctAnswers.toSet()

        return userAnswers == correctAnswersSet
    }

    fun getAllQuizzes(): List<QuizModel> = quizModelRepository.findAll()

    fun getQuizById(id: Long): Optional<QuizModel> = quizModelRepository.findById(id)

    fun addQuiz(quiz: QuizModel): QuizModel = quizModelRepository.save(quiz)

}