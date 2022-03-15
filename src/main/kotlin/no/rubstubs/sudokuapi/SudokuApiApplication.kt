package no.rubstubs.sudokuapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SudokuApiApplication

fun main(args: Array<String>) {
	runApplication<SudokuApiApplication>(*args)
}
