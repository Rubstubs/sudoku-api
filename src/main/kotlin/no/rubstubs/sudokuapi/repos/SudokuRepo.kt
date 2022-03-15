package no.rubstubs.sudokuapi.repos

import no.rubstubs.sudokuapi.models.SudokuModel
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SudokuRepo: MongoRepository<SudokuModel, String>