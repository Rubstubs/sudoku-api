package no.rubstubs.sudokuapi.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testSudokuService {

    @Test
    void shouldSolveEasy3x3Sudoku(){
        SudokuService sudokuService = new SudokuService();
        String sudokuString = "200000000504006001000704000000080740006000093001000602000100000723800000600497000";
        assertEquals("267918354584236971139754826392681745876542193451379682948123567723865419615497238", sudokuService.solve(sudokuString).get("answer"));
    }

    @Disabled
    void shouldSolveMedium3x3Sudoku(){
        SudokuService sudokuService = new SudokuService();
        String sudokuString = "200000000504006001000704000000080040006000093001000602000100000723800000600497000";
        assertEquals("267918354584236971139754826392681745876542193451379682948123567723865419615497238", sudokuService.solve(sudokuString).get("answer"));
    }

    @Test
    void shouldCheckIfSudokuIsSolved() {
        SudokuService sudokuService = new SudokuService();
        int[][] sudokuTrue = sudokuService.convertStringToIntArray("267918354584236971139754826392681745876542193451379682948123567723865419615497238");
        int[][] sudokuFalse1 = sudokuService.convertStringToIntArray("267918354584236222222222826392681745876542193451379682948123567723865419615497238");
        assertTrue(sudokuService.isSolved(sudokuTrue));
        assertFalse(sudokuService.isSolved(sudokuFalse1));
    }

    @Test
    void shouldValidateSudoku() {
        SudokuService sudokuService = new SudokuService();
        int[][] sudokuTrue = sudokuService.convertStringToIntArray("267918354584236971139754826392681745876542193451379682948123567723865419615497238");
        int[][] sudokuTrue2 = sudokuService.convertStringToIntArray("200000000000000001000704000000080740006000093001000602000000000723800000600497000");
        int[][] sudokuFalse1 = sudokuService.convertStringToIntArray("267918354584236222222222826392681745876542193451379682948123567723865419615497238");
        int[][] sudokuFalse2 = sudokuService.convertStringToIntArray("267918354584236222222222826392681745865497238");
        int[][] sudokuFalse3 = sudokuService.convertStringToIntArray("2679190#%&(/U¤;G M=)817458682948   12356772386541961549|||7238");
        int[][] sudokuFalse4 = sudokuService.convertStringToIntArray("267918354584236971139754826392681745876542193451379682948123567723865419165497238");
        assertTrue(sudokuService.isValid(sudokuTrue));
        assertTrue(sudokuService.isValid(sudokuTrue2));
        assertFalse(sudokuService.isValid(sudokuFalse1));
        assertFalse(sudokuService.isValid(sudokuFalse2));
        assertFalse(sudokuService.isValid(sudokuFalse3));
        assertFalse(sudokuService.isValid(sudokuFalse4));
    }
}
