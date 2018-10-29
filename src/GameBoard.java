import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class GameBoard {
    private ArrayList<ArrayList<Cell>> __array;
    private boolean full;

    public GameBoard() {
        this.__array = new ArrayList<>();
        this.full = false;
        initializeBoard();
    }

    public GameBoard(GameBoard object) {
        ArrayList test = new ArrayList<ArrayList<Cell>>();
        for (int i = 0; i < 8; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            test.add(row);
            for (int j = 0; j < 8; j++) {
                Cell cell = new Cell();
                row.add(cell);
                cell.setValue(object.getArray().get(i).get(j).getValue());
            }
        }
        this.__array = test;
        this.full = object.full;
        initializeBoard();
    }

    public void setConfig(int config) {
        if (config == 1) {
            getCell(3, 3).setValue('B');
            getCell(3, 4).setValue('W');
            getCell(4, 4).setValue('B');
            getCell(4, 3).setValue('W');
            getCell(3, 1).setValue('B');
            getCell(3, 2).setValue('W');
            getCell(4, 2).setValue('B');
            getCell(4, 1).setValue('W');
            //getCell(5, 5).setValue('B');
        } else if (config == 2) {
            getCell(3, 3).setValue('W');
            getCell(3, 4).setValue('B');
            getCell(4, 4).setValue('W');
            getCell(4, 3).setValue('B');
        }
        displayBoard(new ArrayList<int[]>());
    }

    public void setArray(ArrayList<ArrayList<Cell>> array) {
        this.__array = array;
    }

    public ArrayList<ArrayList<Cell>> getArray() {
        return this.__array;
    }

    public void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            this.__array.add(row);
            for (int j = 0; j < 8; j++) {
                Cell cell = new Cell();
                row.add(cell);
            }
        }
    }

    public boolean inHiglightList(ArrayList<int[]> highlightList, int i, int j) {
        for (int k = 0; k < highlightList.size(); k++) {
            if (highlightList.get(k)[0] == i && highlightList.get(k)[1] == j)
                return true;
        }
        return false;

    }

    public void displayBoard(ArrayList<int[]> highlightList) {
        System.out.print("  ");
        for (int i = 0; i < 8; i++) {
            System.out.print(String.valueOf((char)(i + 97)).toUpperCase()+" ");
        }
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print(i + 1+" ");
            for (int j = 0; j < 8; j++) {
                Cell cell = this.__array.get(i).get(j);
                if (inHiglightList(highlightList, i, j))
                    System.out.print("\u001B[35m"+cell.getValue()+" \u001B[0m");
                else
                    System.out.print(cell.getValue()+" ");
            }
            System.out.println();
        }
    }

    public String displayScore(int blackScore, int whiteScore) {
        String string = new String();
        System.out.println();
        System.out.println("Score");
        System.out.println("-----------");
        System.out.println("Black: " + blackScore);
        System.out.println("White: " + whiteScore);
        System.out.println();
        string = "Score" +"\n-----------" + "\nBlack: " + blackScore + "\nWhite: " + whiteScore;
        return string;
    }

    public int[] getScore() {
        int blackScore = 0;
        int whitescore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char value = this.__array.get(i).get(j).getValue();
                if (value == 'B')
                    blackScore += 1;
                else if (value == 'W')
                    whitescore += 1;
            }
        }
        return new int[] {blackScore, whitescore};
    }

    public boolean isBoardFull() {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char value = this.__array.get(i).get(j).getValue();
                if (value == 'B' || value == 'W')
                    count += 1;
            }
        }
        return (count == 63);
    }

    public ArrayList<String> getValidMoves(char currentColor) {
        ArrayList<String> validMoves = new ArrayList<>();

        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                    if (isValidPlacement(i, j, currentColor)) {
                        //char[] move = {(Character(j + 97), (i + 1)};
                        validMoves.add(String.valueOf((char) (j + 97)) + String.valueOf(i + 1));
                    }

            }
        }
        return validMoves;
    }


    public boolean isValidPlacement(int row, int col, char color) {
        //char color;
        char otherColor;
        if (color == 'W') {
            //color = 'W';
            otherColor = 'B';
        } else {
            //color = 'B';
            otherColor = 'W';
        }
        if(this.__array.get(row).get(col).getValue() == ' ') {
            if (row != 0) {  //bottom to top
                if (this.__array.get(row - 1).get(col).getValue() == otherColor) {
                    for (int i = row; i >= 0; i--) {
                        char value = this.__array.get(i).get(col).getValue();
                        if (value == ' ' && i != row)
                            break;
                        if (value == color && i != row)
                            return true;
                    }
                }
            }

            if (row != 7) {  //top to bottom
                if (this.__array.get(row + 1).get(col).getValue() == otherColor) {
                    for (int i = row; i <= 7; i++) {
                        char value = this.__array.get(i).get(col).getValue();
                        if (value == ' ' && i != row)
                            break;
                        if (value == color && i != row) {
                            return true;
                        }
                    }
                }
            }

            if (col != 0) {  //right to left
                if (this.__array.get(row).get(col - 1).getValue() == otherColor) {
                    for (int i = col; i >= 0; i--) {
                        char value = this.__array.get(row).get(i).getValue();
                        if (value == ' ' && i != col)
                            break;
                        if (value == color && i != col) {
                            return true;
                        }
                    }
                }
            }

            if (col != 7) {  //left to right
                if (this.__array.get(row).get(col + 1).getValue() == otherColor) {
                    for (int i = col; i <= 7; i++) {
                        char value = this.__array.get(row).get(i).getValue();
                        if (value == ' ' && i != col)
                            break;
                        if (value == color && i != col) {
                            return true;
                        }
                    }
                }
            }

            if (col != 0 && row != 0) {  // right to left on '\' facing diagonals
                if (this.__array.get(row - 1).get(col - 1).getValue() == otherColor) {
                    for (int i = row; i >= 0; i--) {
                        if (col + (i - row) < 0)
                            break;
                        char value = this.__array.get(i).get(col + (i - row)).getValue();
                        if (value == ' ' && i != row)
                            break;
                        if (value == color && i != row) {
                            return true;
                        }
                    }
                }
            }

            if (col != 7 && row != 7) {  // left to right on '\' facing diagonals
                if (this.__array.get(row + 1).get(col + 1).getValue() == otherColor) {
                    for (int i = row; i <=7; i++) {
                        if (col + (i - row) > 7)
                            break;
                        char value = this.__array.get(i).get(col + (i - row)).getValue();
                        if (value == ' ' && i != row)
                            break;
                        if (value == color && i != row) {
                            return true;
                        }
                    }
                }
            }


            if (col != 7 && row != 0) {  // right to left on '/' facing diagonals
                if (this.__array.get(row - 1).get(col + 1).getValue() == otherColor) {
                    for (int i = col; i <= 7; i++) {
                        if (row - (i - col) < 0)
                            break;
                        char value = this.__array.get(row - (i - col)).get(i).getValue();
                        if (value == ' ' && i != col)
                            break;
                        if (value == color && i != col) {
                            return true;
                        }
                    }
                }
            }

            if (col != 0 && row != 7) {  // left to right on '/' facing diagonals
                if (this.__array.get(row + 1).get(col - 1).getValue() == otherColor) {
                    for (int i = row; i <= 7; i++) {
                        if (col - (i - row) < 0)
                            break;
                        char value = this.__array.get(i).get(col - (i - row)).getValue();
                        if (value == ' ' && i != row)
                            break;
                        if (value == color && i != row) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<int[]> flipTokens(int row, int col) {
        ArrayList<int[]> highlightList = new ArrayList<>();
        char color;
        char otherColor;
        if (this.__array.get(row).get(col).getValue() == 'W') {
            color = 'W';
            otherColor = 'B';
        } else {
            color = 'B';
            otherColor = 'W';
        }

        if (row != 0) {  //bottom to top
            if (this.__array.get(row - 1).get(col).getValue() == otherColor) {
                for (int i = row; i >= 0; i--) {
                    char value = this.__array.get(i).get(col).getValue();
                    if (value == ' ')
                        break;
                    if (value == color && i != row) {
                        for (int k = row; k > i-1; k--) {
                            this.__array.get(k).get(col).setValue(color);
                            int[] list = {k, col};
                            highlightList.add(list);
                        }
                        break;
                    }
                }
            }
        }

        if (row != 7) {  //top to bottom
            if (this.__array.get(row + 1).get(col).getValue() == otherColor) {
                for (int i = row; i <= 7; i++) {
                    char value = this.__array.get(i).get(col).getValue();
                    if (value == ' ')
                        break;
                    if (value == color && i != row) {
                        for (int k = row; k < i + 1; k++) {
                            __array.get(k).get(col).setValue(color);
                            int[] list = {k, col};
                            highlightList.add(list);
                        }
                        break;
                    }
                }
            }
        }

        if (col != 0) {  //right to left
            if (this.__array.get(row ).get(col-1).getValue() == otherColor) {
                for (int i = col; i >= 0; i--) {
                    char value = this.__array.get(row).get(i).getValue();
                    if (value == ' ')
                        break;
                    if (value == color && i != col) {
                        for (int k = col; k > i-1; k--) {
                            this.__array.get(row).get(k).setValue(color);
                            int[] list = {row, k};
                            highlightList.add(list);
                        }
                        break;
                    }
                }
            }
        }

        if (col != 7) {  //left to right
            if (this.__array.get(row).get(col+1).getValue() == otherColor) {
                for (int i = col; i <= 7; i++) {
                    char value = this.__array.get(row).get(i).getValue();
                    if (value == ' ')
                        break;
                    if (value == color && i != col) {
                        for (int k = col; k < i+1; k++) {
                            this.__array.get(row).get(k).setValue(color);
                            int[] list = {row, k};
                            highlightList.add(list);
                        }
                        break;
                    }
                }
            }
        }

        if (col != 0 && row != 0) {  // right to left on '\' facing diagonals
            if (this.__array.get(row - 1).get(col-1).getValue() == otherColor) {
                for (int i = row; i >= 0; i--) {
                    if(col + (i - row) < 0)
                        break;
                    char value =this.__array.get(i).get(col+(i-row)).getValue();
                    if (value == ' ')
                        break;
                    if (value == color && i != row) {
                        for (int k = row; k > i-1; k--) {
                            this.__array.get(k).get(col+(k-row)).setValue(color);
                            int[] list = {k, col+(k-row)};
                            highlightList.add(list);
                        }
                        break;
                    }
                }
            }
        }

        if (col != 7 && row != 7) {  // left to right on '\' facing diagonals
            if (this.__array.get(row + 1).get(col+1).getValue() == otherColor) {
                for (int i = row; i <= 7; i++) {
                    if(col + (i - row) > 7)
                        break;
                    char value = this.__array.get(i).get(col+(i-row)).getValue();
                    if (value == ' ')
                        break;
                    if (value == color && i != row) {
                        for (int k = row; k < i+1; k++) {
                            this.__array.get(k).get(col+(k-row)).setValue(color);
                            int[] list = {k, col+(k-row)};
                            highlightList.add(list);
                        }
                        break;
                    }
                }
            }
        }

        if (col != 7 && row != 0) {  // right to left on '/' facing diagonals
            if (this.__array.get(row - 1).get(col+1).getValue() == otherColor) {
                for (int i = col; i <= 7; i++) {
                    if(row-(i-col) < 0)
                        break;
                    char value = this.__array.get(row-(i-col)).get(i).getValue();
                    if (value == ' ')
                        break;
                    if (value == color && i != col) {
                        for (int k = col; k < i+1; k++) {
                            this.__array.get(row-(k-col)).get(k).setValue(color);
                            int[] list = {row-(k-col), k};
                            highlightList.add(list);
                        }
                        break;
                    }
                }
            }
        }

        if (col != 0 && row != 7) {  // left to right on '/' facing diagonals
            if (this.__array.get(row + 1).get(col-1).getValue() == otherColor) {
                for (int i = row; i <= 7; i++) {
                    if(col-(i-row) < 0)
                        break;
                    char value = this.__array.get(i).get(col-(i-row)).getValue();
                    if (value == ' ')
                        break;
                    if (value == color && i != row) {
                        for (int k = row; k < i+1; k++) {
                            this.__array.get(k).get(col-(k-row)).setValue(color);
                            int[] list = {k, col-(k-row)};
                            highlightList.add(list);
                        }
                        break;
                    }
                }
            }
        }

        return (highlightList);
    }

    public Cell getCell(int row, int column) {
        return this.__array.get(row).get(column);
    }
}


