package no.rubstubs.sudokuapi.services;

import no.rubstubs.sudokuapi.repos.SudokuRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Service
public class SudokuService {
    private boolean needsReiteration = false;
    private int prevIteration;

    public HashMap<String, Object> solve(String incompleteSudokuString) {
        printSudoku(convertStringToIntArray(incompleteSudokuString));
        int[][] incompleteSudoku = convertStringToIntArray(incompleteSudokuString);
        int[][] completeSudoku;
        HashMap<String, Object> completeSudokuMap = new HashMap<>();
        completeSudoku = iterateOnSudoku(incompleteSudoku);
        completeSudokuMap.put("isSolved", isSolved(completeSudoku));
        completeSudokuMap.put("isValid", isValid(completeSudoku.clone()));
        completeSudokuMap.put("sudoku", convertIntArrayToString(completeSudoku));
        printSudoku(completeSudoku);
        return completeSudokuMap;
    }

    private int[][] iterateOnSudoku(int[][] incompleteSudoku) {
        for (int i = 0; i < incompleteSudoku.length; i++) {
            for (int j = 0; j < incompleteSudoku[i].length; j++) {
                if (incompleteSudoku[i][j] != 0) continue; // Value already set, jump to next value

                ArrayList<Integer> potentialAnswers = calculateHorizontalAndVertical(incompleteSudoku, i, j);
                if (potentialAnswers.size() > 1) { // Too many options
                    potentialAnswers = calculateSquares(incompleteSudoku, i, j, potentialAnswers); // try checking the square to reduce number of options
                    if (potentialAnswers.size() == 1) { // Value is certain
                        incompleteSudoku[i][j] = potentialAnswers.get(0); //set value in potentialAnswers
                    } else {
                        needsReiteration = true; // Prepare for running iteration again after completion
                    }
                } else if (potentialAnswers.size() == 1)
                    incompleteSudoku[i][j] = potentialAnswers.get(0); // Value is certain, set value in potentialAnswers
            }
        }

        if (needsReiteration) {
            if (Arrays.deepHashCode(incompleteSudoku) == prevIteration) // If previous iteration yielded the same output as the current iteration
                return incompleteSudoku; // Return the incomplete sudoku
            needsReiteration = false; // Reset
            prevIteration = Arrays.deepHashCode(incompleteSudoku); // Set a hash value for prevIteration
            iterateOnSudoku(incompleteSudoku); // Iterate
        }
        return incompleteSudoku;
    }

    private ArrayList<Integer> calculateSquares(int[][] sudoku, int columnPos, int rowPos, ArrayList<Integer> potentialAnswers) {
        // Calculating the right square based on current field
        int columnPosStart = 0;
        if (columnPos >= 3 && columnPos <= 6) columnPosStart = 3;
        if (columnPos >= 6) columnPosStart = 6;
        int columnPosEnd = columnPosStart + 3;

        int rowPosStart = 0;
        if (rowPos >= 3 && rowPos <= 6) rowPosStart = 3;
        if (rowPos >= 6) rowPosStart = 6;
        int rowPosEnd = rowPosStart + 3;

        // Searching the square for possible answers
        ArrayList<Integer> newPotentialAnswers = new ArrayList<>();
        for (int i : potentialAnswers) {
            newPotentialAnswers.add(i); // Adding a potential answer (will be removed if inside the square)
            for (int j = columnPosStart; j < columnPosEnd; j++) {
                for (int k = rowPosStart; k < rowPosEnd; k++) {
                    if (sudoku[j][k] == i) {
                        newPotentialAnswers.remove((Integer) i); // Removing current potential answer if it's inside the square
                        break;
                    }
                }
            }
        }
        return newPotentialAnswers;
    }

    private ArrayList<Integer> calculateHorizontalAndVertical(int[][] sudoku, int columnPos, int rowPos) {
        ArrayList<Integer> potentialAnswers = new ArrayList<>();

        OUTER_LOOP:
        for (int i = 1; i <= sudoku.length; i++) {
            for (int j = 0; j < sudoku.length; j++) {
                // already in column or row
                if (sudoku[columnPos][j] == i || sudoku[j][rowPos] == i) continue OUTER_LOOP;
            }
            potentialAnswers.add(i);
        }
        return potentialAnswers;
    }

    public boolean isValid(int[][] sudokuIncoming) {
        String sudokuString = convertIntArrayToString(sudokuIncoming);
        int[][] sudoku = convertStringToIntArray(sudokuString);
        int filler = 1000;
        if (sudokuString.length() != 81) return false;

        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                temp.clear();
                for (int ii = i; ii < i+3; ii++) {
                    for (int jj = j; jj < j+3; jj++) {
                        if (sudoku[ii][jj] == 0) sudoku[ii][jj] = filler--;
                        if (temp.contains(sudoku[ii][jj])) return false;
                        temp.add(sudoku[ii][jj]);
                    }
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            temp.clear();
            // Horizontal
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] == 0) sudoku[i][j] = filler--;
                if (temp.contains(sudoku[i][j])) return false;
                temp.add(sudoku[i][j]);
            }

            // Vertical
            temp.clear();
            for (int j = 0; j < 9; j++) {
                if (sudoku[j][i] == 0) sudoku[j][i] = filler--;
                if (temp.contains(sudoku[j][i])) return false;
                temp.add(sudoku[j][i]);
            }
        }

        return true;
    }

    public boolean isSolved(int[][] sudoku) {
        int temp;

        // Squares
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                temp = 0;
                for (int ii = i; ii < i+3; ii++) {
                    for (int jj = j; jj < j+3; jj++) {
                        temp += sudoku[ii][jj];
                    }
                }
                if (temp != 45) return false;
            }
        }

        for (int i = 0; i < 9; i++) {
            temp = 0;
            // Horizontal
            for (int j = 0; j < 9; j++) {
                temp += sudoku[i][j];
            }
            if (temp != 45) return false;

            // Vertical
            temp = 0;
            for (int j = 0; j < 9; j++) {
                temp += sudoku[j][i];
            }
            if (temp != 45) return false;
        }
        return true;
    }

    private void printSudoku(int[][] sudoku) {

        StringBuilder sb = new StringBuilder();

        for (int[] column : sudoku) {
            for (int value : column) sb.append(value).append("  ");
            sb.append("\n");
        }

        System.out.println(sb);

    }

    private String getSudokuString(int[][] sudoku) {

        StringBuilder sb = new StringBuilder();

        for (int[] column : sudoku) {
            for (int value : column) sb.append(value).append("  ");
            sb.append("\n");
        }

        return sb.toString();

    }

    public int[][] convertStringToIntArray(String sudokuString) {
        int[][] array = new int[9][9];
        if (sudokuString.length() != 81) return null;
        int k = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                array[i][j] = Integer.parseInt(String.valueOf(sudokuString.charAt(k++)));
            }
        }
        return array;
    }

    public String convertIntArrayToString(int[][] sudokuArray) {
        if (sudokuArray == null) return "";
        StringBuilder sudokuStringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuStringBuilder.append(sudokuArray[i][j]);
            }
        }
        return sudokuStringBuilder.toString();
    }

}
