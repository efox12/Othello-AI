public class GameManager {
    char currentPlayer;
    char localPlayer;
    public GameManager(){
        currentPlayer = 'B';
    }
    public void swapPlayers() {
        //swap players
        if (currentPlayer == 'B')
            currentPlayer = 'W';
        else
            currentPlayer = 'B';
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(char currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public char getLocalPlayer() {
        return localPlayer;
    }

    public void setLocalPlayer(char localPlayer) {
        this.localPlayer = localPlayer;
    }
}
