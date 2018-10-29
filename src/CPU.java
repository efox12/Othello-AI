import java.util.*;
import java.util.concurrent.*;


public class CPU {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        GameBoard b = new GameBoard();
        GameStates s = new GameStates();

        System.out.print("Pick configuration 1-(B W W B), 2-(W B B W): ");
        int num = reader.nextInt(); // scans the next token of the input as an int.
        if (num == 1) {
            b.setConfig(1);
        } else {
            b.setConfig(2);
        }
        char localPlayer;
        System.out.print("Would you like to be Black or White? (b/w): ");
        String text = reader.next(); // Scans the next token of the input as an int.
        if (Objects.equals(text.toLowerCase(), "q"))  //don't let AI go
            return;
        if (Objects.equals(text.toUpperCase(), "B"))
            localPlayer = 'B';
        else
            localPlayer = 'W';
        char currentPlayer = 'B';

        while (!b.isBoardFull()) {
            boolean moveMade = false;
            int row = 0;
            int col = 0;
            if (b.getValidMoves(currentPlayer).size() > 0) {
                System.out.println(currentPlayer + "'s turn.");
                while (!moveMade) {
                    try {
                        if (currentPlayer != localPlayer) {  //it is the AI 's turn
                            System.out.print("I am making my move is this ok? (y/n): ");
                            //System.out.println(b.getValidMoves(currentPlayer));
                            text = reader.next(); // Scans the next token of the input as an int.

                            if (Objects.equals(text.toLowerCase(), "y")) {
                                boolean canContinue = false;
                                Timer timer = new Timer();
                                BestFirst bf = new BestFirst();
                                GameBoard temp = new GameBoard(b);

                                // This block of code is timed and gets terminated after 10 secongs
                                Callable<String> run = new Callable<String>(){
                                    @Override
                                    public String call() throws Exception {
                                        // Runs the alpha beta pruning
                                        bf.maxVal(temp,null,null,0, null, 'B');
                                        // Sort the moves based on the Heuristic
                                        Collections.sort(bf.moveList, new BestFirst.MoveComparator());
                                        String move = bf.moveList.get(0).getMove();
                                        return move;
                                    }
                                };
                                // Run the timed block of code
                                RunnableFuture<String> future = new FutureTask<>(run);
                                ExecutorService service = Executors.newSingleThreadExecutor();
                                service.execute(future);
                                String result = null;
                                try {
                                    result = String.valueOf(future.get(10, TimeUnit.SECONDS)); // wait 10 seconds
                                } catch (TimeoutException ex) {
                                    // timed out. Try to stop the code if possible.
                                    future.cancel(true);
                                    System.out.println("Could Not make a move in time.");
                                }
                                service.shutdown();

                                // Run the best move based on the heurisic
                                String move = bf.moveList.get(0).getMove();
                                System.out.println("Placed token at " + move);
                                //System.out.println("Test "+move);
                                col = move.charAt(0) - 'a';
                                row = move.charAt(1) - 49;

                            } else if (Objects.equals(text.toLowerCase(), "q")) //quit
                                return;
                            else if (Objects.equals(text.toLowerCase(), "u")) {
                                b = new GameBoard(s.undo(b));
                                currentPlayer = swapPlayers(currentPlayer);
                                continue;
                            } else if (Objects.equals(text.toLowerCase(), "r")) {
                                b = new GameBoard(s.redo(b));
                                currentPlayer = swapPlayers(currentPlayer);
                                continue;
                            }
                        } else {  //it is the players turn
                            b.displayBoard(new ArrayList<int[]>());
                            int[] score = b.getScore();
                            b.displayScore(score[0], score[1]);
                            System.out.println(b.getValidMoves(currentPlayer));
                            System.out.print("Enter a spot on the game board. (ex. 'A3'): ");
                            text = reader.next(); // Scans the next token of the input as an int.
                            if (Objects.equals(text.toLowerCase(), "q")) //quit
                                return;
                            else if (Objects.equals(text.toLowerCase(), "u")) {
                                b = new GameBoard(s.undo(b));
                                currentPlayer = swapPlayers(currentPlayer);
                                continue;
                            } else if (Objects.equals(text.toLowerCase(), "r")) {
                                b = new GameBoard(s.redo(b));
                                currentPlayer = swapPlayers(currentPlayer);
                                continue;
                            } else {
                                col = text.charAt(0) - 'a';
                                row = text.charAt(1) - 49;
                                // System.out.println((text.charAt(0) - 'a')+" "+text.charAt(1)+(Integer.valueOf(text.charAt(1)) - 1));
                            }
                        }
                        if (b.getCell(row, col).getValue() == ' ' && b.isValidPlacement(row, col, currentPlayer)) {
                            //create temporary gameboard to display potential move
                            GameBoard temp = new GameBoard(b);
                            //display if valid move
                            temp.getCell(row, col).setValue(currentPlayer);
                            ArrayList<int[]> highlightList = temp.flipTokens(row, col);

                            temp.displayBoard(highlightList);
                            int[] tempscore = temp.getScore();
                            temp.displayScore(tempscore[0], tempscore[1]);

                            System.out.print("This is the new configuration. Is this ok? (y/n): ");
                            text = reader.next(); // Scans the next token of the input as an int.

                            if (Objects.equals(text.toLowerCase(), "y")) {  //add changes to the board
                                s.addPastState(new GameBoard(b));

                                b.getCell(row, col).setValue(currentPlayer);
                                b.flipTokens(row, col);

                                moveMade = true;
                            } else if (Objects.equals(text.toLowerCase(), "q"))  //quit
                                return;
                            else if (Objects.equals(text.toLowerCase(), "u")) {
                                b = new GameBoard(s.undo(b));
                                currentPlayer = swapPlayers(currentPlayer);
                            } else if (Objects.equals(text.toLowerCase(), "r")) {
                                b = new GameBoard(s.redo(b));
                                currentPlayer = swapPlayers(currentPlayer);
                            }
                        } else
                            System.out.println("not a valid move " + row + col);
                    } catch (Exception e){
                        System.out.println("Not a valid input!");
                        //e.printStackTrace();
                        continue;
                    }
                }
            } else {
                System.out.println("No possible moves for " + currentPlayer);
                //break;
            }


            currentPlayer = swapPlayers(currentPlayer);
        }

    }

    public static char swapPlayers(char currentPlayer) {
        //swap players
        if (currentPlayer == 'B')
            currentPlayer = 'W';
        else
            currentPlayer = 'B';
        return  currentPlayer;
    }
}
