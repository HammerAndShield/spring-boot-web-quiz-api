package org.hammernshield.springbootwebquizapi.model

data class QuizModelWithoutAnswer(
    val id: Int,
    val title: String,
    val text: String,
    val options: List<String>,
)
