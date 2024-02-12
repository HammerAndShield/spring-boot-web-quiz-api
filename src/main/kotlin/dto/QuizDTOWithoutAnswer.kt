package dto

data class QuizDTOWithoutAnswer(
    val id: Long,
    val title: String,
    val text: String,
    val options: List<String>,
)
