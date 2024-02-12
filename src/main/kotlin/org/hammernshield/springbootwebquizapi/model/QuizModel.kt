package org.hammernshield.springbootwebquizapi.model

import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode


@Entity
@Table(name = "quizzes")
data class QuizModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 255)
    val title: String,

    @Column(nullable = false, length = 500)
    val text: String,

    @ElementCollection(fetch = FetchType.LAZY)
    val options: List<String>,

    @ElementCollection(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    val answer: List<Int>
)
