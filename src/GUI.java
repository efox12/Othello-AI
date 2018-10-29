import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import sun.tools.jstat.Alignment;

import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.Random;

public class GUI extends Application {
    GameBoard b = new GameBoard();
    GameStates gameStates = new GameStates();
    GameManager gameManager = new GameManager();
    GridPane rowtext = new GridPane();

    GridPane coltext = new GridPane();
    GridPane gridpane = new GridPane();
    HBox root = new HBox();
    VBox vBox = new VBox();



    boolean isHighlighted = false;
    boolean moveMade = false;
    ArrayList<ArrayList<CellGUI>> array = new ArrayList<>();

   public void gameBoardToGUI(GameBoard b){
       for(int i = 0; i<8; i++)
           for(int j = 0; j<8; j++)
               array.get(i).get(j).setValue(b.getCell(j,i).getValue());
   }

    public void GUIToGameBoard(GameBoard b){
        for(int i = 0; i<8; i++)
            for(int j = 0; j<8; j++)
                b.getCell(j,i).setValue(array.get(i).get(j).getValue());
    }
    @Override
    public void start(Stage primaryStage) {
        //GameBoard Screen
        HBox hBox = new HBox();
        VBox score = new VBox();

        Text text = new Text();
        text.setText("Score");
        Text text5 = new Text();

        Button confirmButton = new Button();
        confirmButton.setText("Confirm Move");
        confirmButton.setDisable(true);

        Button AIbutton = new Button();
        AIbutton.setText("AI Make Move");
        AIbutton.setDisable(true);


        Button undoButton = new Button();
        undoButton.setText("Undo");
        if(gameStates.pastarray.size()>0){
            undoButton.setDisable(false);
        } else {
            undoButton.setDisable(true);
        }

        Button redoButton = new Button();
        redoButton.setText("Redo");
        if(gameStates.futurearray.size()>0){
            redoButton.setDisable(false);
        } else {
            redoButton.setDisable(true);
        }

        rowtext.setPadding(new Insets(0,3,0,3));
        for(int i = 0; i<8; i++){
            Text row = new Text();
            row.setTextAlignment(TextAlignment.CENTER);
            row.setFont(new Font(15));
            Text col = new Text();
            col.setFont(new Font(15));
            col.setTextAlignment(TextAlignment.CENTER);

            col.setText(String.valueOf((char)('A'+ i)));
            coltext.add(col,i,0);
            row.setText(String.valueOf(i+1));
            rowtext.add(row,0,i);
            GridPane.setHalignment(col, HPos.CENTER);
        }
        //coltext.setHgap(15);
        coltext.setMinWidth(25*8+2*7);
        coltext.setMaxWidth(25*8+2*7);

        //coltext.(Alignment.CENTER);
        rowtext.setMinHeight(25*8+2*7);
        rowtext.setMaxHeight(25*8+2*7);

        for (int j = 0; j < 8; j++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 8);
            coltext.getColumnConstraints().add(colConst);
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / 8);
            rowtext.getRowConstraints().add(rowConst);
        }

        gridpane.setHgap(2);
        gridpane.setVgap(2);
        gridpane.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        for (int i = 0; i < 8; i++) {
            ArrayList<CellGUI> row = new ArrayList<>();
            array.add(row);
            for (int j = 0; j < 8; j++) {
                CellGUI cellGUI = new CellGUI();
                row.add(cellGUI);
                Button btn = cellGUI.btn;

                btn.setPrefSize(25,25);
                btn.setMinSize(25,25);
                btn.setMaxSize(25,25);
                btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(cellGUI.value == ' ' && gameManager.currentPlayer == gameManager.localPlayer) {
                            if(gameManager.localPlayer == 'B')
                                btn.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                            else
                                btn.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

                            int row = GridPane.getRowIndex(btn);
                            int col = GridPane.getColumnIndex(btn);
                            GameBoard temp = new GameBoard(b);
                            //display if valid move
                            if(temp.isValidPlacement(row,col,gameManager.getCurrentPlayer()) && !isHighlighted){
                                temp.getCell(row, col).setValue(gameManager.getCurrentPlayer());
                                temp.flipTokens(row,col);
                                gameBoardToGUI(temp);
                            }
                        }
                    }
                });
                btn.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        gameBoardToGUI(b);
                        if(cellGUI.value == ' ' && gameManager.currentPlayer == gameManager.localPlayer) {
                            btn.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                });

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (gameManager.currentPlayer == gameManager.localPlayer) {
                            int row = GridPane.getRowIndex(btn);
                            int col = GridPane.getColumnIndex(btn);
                            if (b.isValidPlacement(row, col, gameManager.getCurrentPlayer()) && !isHighlighted) {
                                text5.setText(gameManager.currentPlayer + ", Confirm your move");
                                gameStates.addPastState(new GameBoard(b));
                                redoButton.setDisable(true);
                                b.getCell(row, col).setValue(gameManager.getCurrentPlayer());
                                b.flipTokens(row, col);
                                int[] score = b.getScore();
                                text.setText(b.displayScore(score[0], score[1]));
                                gameBoardToGUI(b);
                                isHighlighted = true;
                                moveMade = true;
                                confirmButton.setDisable(false);
                                undoButton.setDisable(true);
                            }
                        }
                    }
                });
                gridpane.add(btn,i,j);
            }
        }

        undoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                b = new GameBoard(gameStates.undo(b));
                gameManager.swapPlayers();
                int[] scores = b.getScore();
                text.setText(b.displayScore(scores[0],scores[1]));
                gameBoardToGUI(b);

                if(gameStates.pastarray.size()>0){
                    undoButton.setDisable(false);
                } else {
                    undoButton.setDisable(true);
                }
                if(gameStates.futurearray.size()>0){
                    redoButton.setDisable(false);
                } else {
                    redoButton.setDisable(true);
                }
                if(gameManager.currentPlayer!=gameManager.localPlayer)
                    AIbutton.setDisable(false);
                else
                    AIbutton.setDisable(true);
                confirmButton.setDisable(true);
                undoButton.setDisable(false);
            }
        });

        redoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                b = new GameBoard(gameStates.redo(b));
                gameManager.swapPlayers();
                int[] scores = b.getScore();
                text.setText(b.displayScore(scores[0],scores[1]));
                gameBoardToGUI(b);

                if(gameStates.pastarray.size()>0){
                    undoButton.setDisable(false);
                } else {
                    undoButton.setDisable(true);
                }

                if(gameStates.futurearray.size()>0){
                    redoButton.setDisable(false);
                } else {
                    redoButton.setDisable(true);
                }
                if(gameManager.currentPlayer!= gameManager.localPlayer) {
                    AIbutton.setDisable(false);
                    text5.setText(gameManager.currentPlayer + "'s turn. Tell AI to make a move.");
                }else {
                    AIbutton.setDisable(true);
                    text5.setText(gameManager.currentPlayer + "'s turn. Pick a spot.");
                }
                confirmButton.setDisable(true);
                undoButton.setDisable(false);
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Turn Ended");
                    if (gameManager.currentPlayer == gameManager.localPlayer) {
                        gameManager.swapPlayers();
                        text5.setText(gameManager.currentPlayer + "'s turn. Tell AI to make a move.");
                        moveMade = false;
                        isHighlighted = false;
                        confirmButton.setDisable(true);
                        undoButton.setDisable(false);
                        //runAI();
                        AIbutton.setDisable(false);

                    } else {
                        gameManager.swapPlayers();
                        text5.setText(gameManager.currentPlayer + "'s turn. Pick A spot");
                        confirmButton.setDisable(true);
                        undoButton.setDisable(false);
                    }
                if(gameStates.pastarray.size()>0){
                    undoButton.setDisable(false);
                } else {
                    undoButton.setDisable(true);
                }
                if(gameStates.futurearray.size()>0){
                    redoButton.setDisable(false);
                } else {
                    redoButton.setDisable(true);
                }
            }
        });

        AIbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean canContinue = false;
                long endTimeMillis = System.currentTimeMillis() + 10000;
                while (!canContinue) {  //continue trying to make move until timeout
                    Random rand = new Random();
                    int row = rand.nextInt((7 - 1) + 1) + 1;
                    int col = rand.nextInt((7 - 1) + 1) + 1;

                    if (b.isValidPlacement(row, col, gameManager.currentPlayer)) {
                        canContinue = true;
                        text5.setText(gameManager.currentPlayer + ", Confirm AI's move");
                        gameStates.addPastState(new GameBoard(b));
                        redoButton.setDisable(true);
                        b.getCell(row, col).setValue(gameManager.getCurrentPlayer());
                        b.flipTokens(row, col);
                        int[] score = b.getScore();
                        text.setText(b.displayScore(score[0], score[1]));
                        gameBoardToGUI(b);
                        confirmButton.setDisable(false);
                        undoButton.setDisable(true);
                        break;
                    }
                    if (System.currentTimeMillis() > endTimeMillis) {
                        System.out.print("I cannot make a move");
                        break;
                    }
                }
                AIbutton.setDisable(true);
            }
        });

        hBox.getChildren().add(undoButton);
        hBox.getChildren().add(redoButton);
        vBox.getChildren().add(coltext);
        vBox.getChildren().add(gridpane);
        vBox.getChildren().add(hBox);

        score.getChildren().add(text);
        score.getChildren().add(text5);
        score.getChildren().add(confirmButton);
        score.getChildren().add(AIbutton);
        root.getChildren().add(rowtext);
        root.setMargin(rowtext, new Insets(18,0,0,0));
        root.getChildren().add(vBox);
        root.getChildren().add(score);

        Scene scene = new Scene(root, 500, 300);


        //Startup Screen
        HBox hBox1 = new HBox();
        VBox startUpRoot = new VBox();

        Text text1 = new Text();
        text1.setText("Othello AI");
        text1.setFont(new Font(30));

        Text text2 = new Text();
        text2.setText("Erik Fox");
        text2.setFont(new Font(20));

        Text text3 = new Text();
        text3.setText("Configuration");
        text3.setFont(new Font(20));

        Text text4 = new Text();
        text4.setText("Color");
        text4.setFont(new Font(20));

        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "BW\nWB", "WB\nBW")
        );
        ChoiceBox cb2 = new ChoiceBox(FXCollections.observableArrayList(
                'B', 'W')
        );

        Button button = new Button();
        button.setText("Start Game");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!cb.getSelectionModel().isEmpty() && !cb2.getSelectionModel().isEmpty()) {
                    if(cb.getSelectionModel().isSelected(0)) {
                        b.setConfig(1);
                    } else {
                        b.setConfig(2);
                    }
                    char localPlayer;
                    if(cb2.getSelectionModel().isSelected(0)){
                        localPlayer = 'B';
                    } else {
                        localPlayer = 'W';
                    }
                    gameManager.setLocalPlayer(localPlayer);
                    int[] scores = b.getScore();
                    text.setText(b.displayScore(scores[0], scores[1]));
                    gameBoardToGUI(b);
                    if(gameManager.currentPlayer == gameManager.localPlayer) {
                        text5.setText(gameManager.currentPlayer + "'s turn. Pick A spot");
                        AIbutton.setDisable(true);
                    }else {
                        text5.setText(gameManager.currentPlayer + "'s turn. Tell AI to make a move.");
                        AIbutton.setDisable(false);
                    }
                    primaryStage.setScene(scene);
                }
            }
        });




        hBox1.getChildren().add(text3);
        hBox1.getChildren().add(cb);
        hBox1.getChildren().add(text4);
        hBox1.getChildren().add(cb2);
        hBox1.setAlignment(Pos.CENTER);
        HBox.setMargin(text4, new Insets(20,2,10,10));
        HBox.setMargin(cb2, new Insets(20,0,10,0));
        HBox.setMargin(cb, new Insets(20,0,10,0));
        HBox.setMargin(text3, new Insets(20,2,10,0));

        startUpRoot.getChildren().add(text1);
        startUpRoot.getChildren().add(text2);
        startUpRoot.getChildren().add(hBox1);
        startUpRoot.getChildren().add(button);
        startUpRoot.setAlignment(Pos.CENTER);

        Scene startupScene = new Scene(startUpRoot, 500, 300);

        // Set Scene
        primaryStage.setTitle("Othello AI");
        primaryStage.setScene(startupScene);
        primaryStage.show();
    }

    public void runAI(){

    }

    public static void main(String[] args) {
        launch(args);
    }
}
