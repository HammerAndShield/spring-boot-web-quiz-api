package org.hammernshield.springbootwebquizapi.model

data class QuizDTOWithoutAnswer(
    val id: Int,
    val title: String,
    val text: String,
    val options: List<String>,
)
