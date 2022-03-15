package no.rubstubs.sudokuapi.models

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Sudoku")
data class SudokuModel(val sudoku: String)
