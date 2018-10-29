import java.util.*;

public class BestFirst {
    ArrayList<GameBoard> pastStates = new ArrayList<>();
    ArrayList<Move> moveList = new ArrayList<>();
    private int depth;

    public Object maxVal(GameBoard node, Object alpha, Object beta, int depth, String move, char currentPlayer) {
        //System.out.println(heuristic(node,currentPlayer) + " " + depth + " " + move);
        // add potential move to list
        moveList.add(new Move(heuristic(node,currentPlayer),depth,move));
        ArrayList<GameBoard> childStates = generateChildStates(node, currentPlayer);

        // base case -> stop if max depth reached or no more states left
        if (depth >= 3 || childStates.size() == 0) {
            return heuristic(node, currentPlayer);
        } else {
            ArrayList<String> possibleMoves = node.getValidMoves(currentPlayer);
            depth += 1;
            Object v = Integer.MIN_VALUE;
            for (int i = 0; i < childStates.size(); i++) {
                // set the initial move for this branch
                if(depth == 1)
                    move = possibleMoves.get(i);

                //swap players
                if (currentPlayer == 'B')
                    currentPlayer = 'W';
                else
                    currentPlayer = 'B';

                // get value from min
                Object v1 = minVal(childStates.get(i), alpha, beta, depth, move, currentPlayer);

                // set value for max
                if (v1 != null)
                    if ((Integer) v1 > (Integer) v)
                        v = v1;
                if (beta != null && v1 != null)
                    if ((Integer) v1 >= (Integer) beta)
                        return v;
                if (alpha == null)
                    alpha = v1;
                else if (v1 != null)
                    if ((Integer) v1 > (Integer) alpha)
                        alpha = v1;
            }
            return v;
        }
    }

    public Object minVal(GameBoard node, Object alpha, Object beta, int depth, String move, char currentPlayer) {
        //System.out.println(heuristic(node,currentPlayer)+ " " + depth + " " + move);
        // add potential move to list
        moveList.add(new Move(heuristic(node,currentPlayer),depth,move));
        ArrayList<GameBoard> childStates = generateChildStates(node, currentPlayer);

        // base case -> stop if max depth reached or no more states left
        if (depth >= 3 || childStates.size() == 0){
            return heuristic(node, currentPlayer);
        } else {
            ArrayList<String> possibleMoves = node.getValidMoves(currentPlayer);
            depth += 1;
            Object v = Integer.MAX_VALUE;
            for (int i = 0; i < childStates.size(); i++) {
                // set the initial move for this branch
                if(depth == 1)
                    move = possibleMoves.get(i);

                // swap players
                if (currentPlayer == 'B')
                    currentPlayer = 'W';
                else
                    currentPlayer = 'B';

                // get value from max
                Object v1 = maxVal(childStates.get(i), alpha, beta, depth, move, currentPlayer);

                // set value for min
                if (v1 != null)
                    if ((Integer) v1 < (Integer) v)
                        v = v1;
                if (alpha != null && v1 != null)
                    if ((Integer) v1 <= (Integer) alpha)
                        return v;
                if (beta == null)
                    beta = v1;
                else if (v1 != null)
                    if ((Integer) v1 < (Integer) beta)
                        beta = v1;
            }
            return v;
        }
    }

    public ArrayList<GameBoard> generateChildStates(GameBoard currentState, char currentPlayer){
        ArrayList<GameBoard> gamestates = new ArrayList<>();
        ArrayList<String> possibleMoves = currentState.getValidMoves(currentPlayer);
        // create next possible states of the board
        for(int i = 0; i < possibleMoves.size(); i++){
            GameBoard nextState = new GameBoard(currentState);

            int col = possibleMoves.get(i).charAt(0) - 'a';
            int row = possibleMoves.get(i).charAt(1) - 49;
            nextState.getCell(row,col).setValue(currentPlayer);
            nextState.flipTokens(row, col);

            // only add state to tree if it is new
            if(isNewState(nextState)) {
                gamestates.add(nextState);
                pastStates.add(nextState);
            }
        }
        return gamestates;
    }

    // Return true if this is a new state
    public boolean isNewState(GameBoard currentState){
        for(int k = 0; k < pastStates.size(); k++){
            int count = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(currentState.getArray().get(i).get(j).getValue() == pastStates.get(k).getArray().get(i).get(j).getValue())
                        count++;
                }
            }
            if(count == 64)
                return false;
        }
        return true;
    }

    // The heuristic is the rating of a move. The goodness of the rating is
    // is how many points ahead the current player is
    public int heuristic(GameBoard currentState, Character currentPlayer){
        int cost;
        if(currentPlayer == 'B')
            cost = currentState.getScore()[0] - currentState.getScore()[1];
        else
            cost = currentState.getScore()[1] - currentState.getScore()[0];
        return cost;
    }
    static class MoveComparator implements Comparator<Move> {
        public int compare(Move m1, Move m2) {
            if (!Objects.equals(m1.getScore(), m2.getScore())) {
                return  (m1.getScore().compareTo(m2.getScore()));
            } else {
                return  (m1.getDepth().compareTo(m2.getDepth()));

            }
        }
    }
}

class Move{
    private Integer depth;
    private Integer score;
    private String move;

    public Move(Integer score, Integer depth, String move){
        this.depth = depth;
        this.score = score;
        this.move = move;
    }

    public Integer getDepth() {
        return depth;
    }

    public Integer getScore() {
        return score;
    }

    public String getMove() {
        return move;
    }
}

