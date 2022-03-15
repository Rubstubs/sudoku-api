# Soduku API

## URL
https://rubstubs-sudoku-api.herokuapp.com/

## End points

```
GET: /status
Response: 200 OK if server is up
```

```
GET /checkSoduku?sudoku=[string representation of sudoku]
Response: 
{
    "isSolved": boolean,
    "isValid": boolean
}
```
 
```
GET /solveSudoku?sudoku=[string representation of sudoku]
Response:
{
"isSolved": boolean,
"isValid": boolean,
"answer": "[string representation of sudoku solution]"
}
```

```
GET /randomSudoku
Response:
{
    "sudoku": "[string representation of sudoku]"
}
```

```
POST /saveSudoku?/sudoku=[string representation of sudoku]
Reponse:
200 OK if submitted
400 BAD REQUEST if not
```

## Notes
String representation of sudoku should be one continuous string, reading the sudoku line by line from top left.
Unknown fields marked as "0". Ex:

2  0  0 | 0  0  0 | 0  0  0  
5  0  4 | 0  0  6 | 0  0  1  
0  0  0 | 7  0  4 | 0  0  0  

0  0  0 | 0  8  0 | 7  4  0  
0  0  6 | 0  0  0 | 0  9  3  
0  0  1 | 0  0  0 | 6  0  2  

0  0  0 | 1  0  0 | 0  0  0  
7  2  3 | 8  0  0 | 0  0  0  
6  0  0 | 4  9  7 | 0  0  0

is the same as 200000000504006001000704000000080740006000093001000602000100000723800000600497000

