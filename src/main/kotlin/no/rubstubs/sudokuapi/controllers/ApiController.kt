package no.rubstubs.sudokuapi.controllers

import no.rubstubs.sudokuapi.models.SudokuModel
import no.rubstubs.sudokuapi.repos.SudokuRepo
import no.rubstubs.sudokuapi.services.SudokuService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import java.util.*
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

@RestController
@CrossOrigin("*")
class ApiController(
    @Autowired private val sudokuService: SudokuService,
    @Autowired private val sudokuRepo: SudokuRepo
) {

    @GetMapping("/")
    fun handleRoot(response: HttpServletResponse){
        response.status = 200;
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    fun returnNoFavicon() {
    }

    @GetMapping("/status")
    fun handleStatus(response: HttpServletResponse) {
        response.status = 200
    }

    @GetMapping("/getRandomSudoku")
    fun handleGetRandomSudoku(response: HttpServletResponse): HashMap<String, String>{
        response.status = 200
        val allSudokus = sudokuRepo.findAll()
        val randomSudoku: HashMap<String, String> = HashMap()
        randomSudoku["sudoku"] = allSudokus[(0 until allSudokus.size).random()].sudoku
        return randomSudoku
    }

    @PostMapping("/saveSudoku")
    fun handleGetRandomSudoku(
        @RequestParam sudoku: String,
        response: HttpServletResponse){

        if (sudokuService.solve(sudoku)["isSolved"] as Boolean)  {
            sudokuRepo.findAll().forEach {
                if (it.sudoku == sudoku)  {
                    response.status = 400
                    return
                }
            }
            sudokuRepo.save(SudokuModel(sudoku))
            response.status = 200
        } else {
            response.status = 400
        }
    }

    @GetMapping("/solveSudoku")
    fun handleSolveSudoku(
        @RequestParam sudoku: String,
        response: HttpServletResponse
    ): HashMap<String, Any> {
        var map = HashMap<String, Any>()

        try {
            map = sudokuService.solve(sudoku)
            response.status = 200
        } catch (
            e: Exception
        ) {
            println("invalid request")
            response.status = 400
            return map
        }

        return map
    }

    @GetMapping("/checkSudoku")
    fun handleCheckSudoku(
        @RequestParam sudoku: String,
        response: HttpServletResponse
    ): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        try {
            map["isSolved"] = sudokuService.isSolved(sudokuService.convertStringToIntArray(sudoku))
            map["isValid"] = sudokuService.isValid(sudokuService.convertStringToIntArray(sudoku))
            response.status = 200
        } catch (
            e: Exception
        ) {
            println("invalid request")
            response.status = 400
            return map
        }
        return map
    }
}