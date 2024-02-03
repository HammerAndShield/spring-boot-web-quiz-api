package org.hammernshield.springbootwebquizapi.services

import org.springframework.stereotype.Service

@Service
class QuizService {

    fun areAnswersCorrect(
        userAnswers: Set<Int>,
        correctAnswers: List<Int>
    ): Boolean {
        val correctAnswersSet = correctAnswers.toSet()

        return userAnswers == correctAnswersSet
    }

}