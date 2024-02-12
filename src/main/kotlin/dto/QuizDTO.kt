package dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class QuizDTO(

    @field:NotBlank
    val title: String,

    @field:NotBlank
    val text: String,

    @field:Size(min = 2)
    val options: List<String>,

    val answer: List<Int>?
)
