package org.hammernshield.springbootwebquizapi.repository

import org.hammernshield.springbootwebquizapi.model.QuizModel
import org.springframework.data.jpa.repository.JpaRepository

interface QuizModelRepository : JpaRepository<QuizModel, Long> {

}