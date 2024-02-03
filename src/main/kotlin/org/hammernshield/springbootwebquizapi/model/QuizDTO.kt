package org.hammernshield.springbootwebquizapi.model

data class QuizDTO(
    val title: String,
    val text: String,
    val options: List<String>,
    val answer: Int
)
