package org.hammernshield.springbootwebquizapi.model

data class QuizModel(
    val id: Int,
    val title: String,
    val text: String,
    val options: List<String>,
    val answer: List<Int>
)
