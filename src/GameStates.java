import java.util.ArrayList;

public class GameStates {
    ArrayList<GameBoard> pastarray;
    ArrayList<GameBoard> futurearray;
    private ArrayList<GameBoard> currentarrat;
    public GameStates() {
        this.pastarray = new ArrayList<GameBoard>();
        this.futurearray = new ArrayList<GameBoard>();
        this.currentarrat = new ArrayList<GameBoard>();
    }

    // undo to the previous game state
    public GameBoard undo(GameBoard currentarray) {
        System.out.println("undo...");
        if(pastarray.size() > 0) {
            GameBoard array = pastarray.remove(pastarray.size() - 1);
            futurearray.add(currentarray);
            return array;
        } else {
            System.out.println("Nothing to undo!");
            return currentarray;
        }
    }

    // redo an undo to the previous game state
    public GameBoard redo(GameBoard currentarray) {
        System.out.println("redo...");
        if(futurearray.size() > 0){
            GameBoard array = futurearray.remove(futurearray.size()-1);
            pastarray.add(currentarray);
            return array;
        } else {
            System.out.println("Nothing to undo!");
            return currentarray;
        }
    }

    // add a state to the list of past game states
    public void addPastState(GameBoard currentarray) {
        pastarray.add(currentarray);
        futurearray.clear();
    }
}
