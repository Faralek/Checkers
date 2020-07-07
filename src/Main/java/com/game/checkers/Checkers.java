package com.game.checkers;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Checkers extends Application {


    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private final Tile[][] board = new Tile[WIDTH][HEIGHT];

    private final Group pieceGroup = new Group();
    private final Group tileGroup = new Group();
    private final Group buttons = new Group();

    private final Image imageBack = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("chessboard.png")));
    private final Image imageLose = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("lose.png")));
    private final Image imageWin = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("win.png")));
    private final Image imageDoubleKill = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("doubleKill.png")));

    public static void main(String[] args) {
        launch(args);
    }

    private int toBoard(double pixel) {
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    private MoveResult tryMove(OneChecker checker, int newX, int newY) {
        if (board[newX][newY].hasChecker() || (newX + newY) % 2 == 0) {
            return new MoveResult(MoveType.NONE);
        }

        int x0 = toBoard(checker.getOldX()-20);
        int y0 = toBoard(checker.getOldY()-20);

        if (Math.abs(newX - x0) == 1 && newY - y0 == checker.getType().moveDir) {
            return new MoveResult(MoveType.NORMAL);
        } else if (Math.abs(newX - x0) == 2 && newY - y0 == checker.getType().moveDir * 2) {

            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if (board[x1][y1].hasChecker() && board[x1][y1].getChecker().getType() != checker.getType()) {
                return new MoveResult(MoveType.KILL, board[x1][y1].getChecker());
            }
        }

        return new MoveResult(MoveType.NONE);
    }

    private void moveComputer() {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        ArrayList<Move> killingMoves = new ArrayList<>();
        ArrayList<Move> normalMoves = new ArrayList<>();

        MoveResult moveResult = new MoveResult(MoveType.NONE);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (board[i][j].hasChecker() && board[i][j].getChecker().getType() == CheckerType.WHITE) {
                    for (int x = -2; x < 3; x++) {
                        for (int y = -2; y < 3; y++) {

                            int maxValue = 7;
                            int minValue = 0;

                            int a = i + x;
                            int b = j + y;

                            a = Math.min(a, maxValue);
                            a = Math.max(a, minValue);
                            b = Math.min(b, maxValue);
                            b = Math.max(b, minValue);

                            moveResult = tryMove(board[i][j].getChecker(), a, b);

                            if (moveResult.getType() == MoveType.KILL) {
                                Move move = new Move(i, j, a, b);
                                killingMoves.add(move);
                            }
                            if (moveResult.getType() == MoveType.NORMAL) {
                                Move move = new Move(i, j, a, b);
                                normalMoves.add(move);

                            }

                        }

                    }
                }
            }
        }
        if (killingMoves.size() > 0) {
            Collections.shuffle(killingMoves);
            System.out.println("kill " + killingMoves.size());

            OneChecker checker = board[killingMoves.get(0).positionX][killingMoves.get(0).positionY].getChecker();

            board[killingMoves.get(0).positionX][killingMoves.get(0).positionY].getChecker().move(killingMoves.get(0).directionX, killingMoves.get(0).directionY);

            board[killingMoves.get(0).positionX][killingMoves.get(0).positionY].setChecker(null);
            board[killingMoves.get(0).directionX][killingMoves.get(0).directionY].setChecker(checker);

            OneChecker otherChecker = board[(killingMoves.get(0).positionX + killingMoves.get(0).directionX) / 2][(killingMoves.get(0).positionY + killingMoves.get(0).directionY) / 2].getChecker();
            board[(killingMoves.get(0).positionX + killingMoves.get(0).directionX) / 2][(killingMoves.get(0).positionY + killingMoves.get(0).directionY) / 2].setChecker(null);
            pieceGroup.getChildren().remove(otherChecker);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }

            int i = killingMoves.get(0).directionX;
            int j = killingMoves.get(0).directionY;

            for (int x = -2; x < 3; x++) {
                for (int y = -2; y < 3; y++) {

                    int maxValue = 7;
                    int minValue = 0;

                    int a = i + x;
                    int b = j + y;

                    a = Math.min(a, maxValue);
                    a = Math.max(a, minValue);
                    b = Math.min(b, maxValue);
                    b = Math.max(b, minValue);

                    moveResult = tryMove(board[i][j].getChecker(), a, b);

                    if (moveResult.getType() == MoveType.KILL) {
                        killingMoves.clear();
                        Move move = new Move(i, j, a, b);
                        killingMoves.add(move);

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {

                        }

                        System.out.println("kill " + killingMoves.size());

                        OneChecker checker2 = board[killingMoves.get(0).positionX][killingMoves.get(0).positionY].getChecker();

                        board[killingMoves.get(0).positionX][killingMoves.get(0).positionY].getChecker().move(killingMoves.get(0).directionX, killingMoves.get(0).directionY);

                        board[killingMoves.get(0).positionX][killingMoves.get(0).positionY].setChecker(null);
                        board[killingMoves.get(0).directionX][killingMoves.get(0).directionY].setChecker(checker2);

                        OneChecker otherChecker2 = board[(killingMoves.get(0).positionX + killingMoves.get(0).directionX) / 2][(killingMoves.get(0).positionY + killingMoves.get(0).directionY) / 2].getChecker();
                        board[(killingMoves.get(0).positionX + killingMoves.get(0).directionX) / 2][(killingMoves.get(0).positionY + killingMoves.get(0).directionY) / 2].setChecker(null);
                        pieceGroup.getChildren().remove(otherChecker2);


                    }

                    return;

                }
            }
        }
        if (normalMoves.size() > 0) {
            Collections.shuffle(normalMoves);
            System.out.println("normal " + normalMoves.size());

            OneChecker checker = board[normalMoves.get(0).positionX][normalMoves.get(0).positionY].getChecker();
            board[normalMoves.get(0).positionX][normalMoves.get(0).positionY].getChecker().move(normalMoves.get(0).directionX, normalMoves.get(0).directionY);
            board[normalMoves.get(0).positionX][normalMoves.get(0).positionY].setChecker(null);
            board[normalMoves.get(0).directionX][normalMoves.get(0).directionY].setChecker(checker);

            return;

        }

        FlowPane winPane = new FlowPane();

        ImageView img2 = new ImageView(imageWin);
        img2.setFitHeight(300);
        img2.setFitWidth(300);
        winPane.getChildren().add(img2);
        winPane.relocate(250, 100);
        buttons.getChildren().add(winPane);

        Button endGameButton2 = new Button("Zakończ Grę");
        endGameButton2.setOnAction(r -> System.exit(0));
        endGameButton2.setAlignment(Pos.CENTER);
        endGameButton2.setPrefSize(200, 200);

        Button resetButton2 = new Button("Restart Game");
        resetButton2.setOnAction(r -> {
            tileGroup.getChildren().remove(0, tileGroup.getChildren().size());
            pieceGroup.getChildren().remove(0, pieceGroup.getChildren().size());
            makeChessBoard();
            buttons.getChildren().removeAll(resetButton2, endGameButton2, winPane);
        });

        resetButton2.setPrefSize(200, 200);
        resetButton2.setAlignment(Pos.CENTER);
        buttons.getChildren().add(resetButton2);
        resetButton2.relocate(200, 400);
        buttons.getChildren().add(endGameButton2);
        endGameButton2.relocate(400, 400);
    }

    private boolean doubleKillPlayer(int i, int j) {

        MoveResult moveResult = new MoveResult(MoveType.NONE);
        ArrayList<Move> killingMoves = new ArrayList<>();

        for (int x = -2; x < 3; x++) {
            for (int y = -2; y < 3; y++) {

                int maxValue = 7;
                int minValue = 0;

                int a = i + x;
                int b = j + y;

                a = Math.min(a, maxValue);
                a = Math.max(a, minValue);
                b = Math.min(b, maxValue);
                b = Math.max(b, minValue);

                moveResult = tryMove(board[i][j].getChecker(), a, b);

                if (moveResult.getType() == MoveType.KILL) {
                    killingMoves.clear();
                    Move move = new Move(i, j, a, b);
                    killingMoves.add(move);

                    if (killingMoves.contains(move)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void didComputerWin() {

        ArrayList<Move> killingMoves = new ArrayList<>();
        ArrayList<Move> normalMoves = new ArrayList<>();

        MoveResult moveResult = new MoveResult(MoveType.NONE);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (board[i][j].hasChecker() && board[i][j].getChecker().getType() == CheckerType.RED) {
                    for (int x = -2; x < 3; x++) {
                        for (int y = -2; y < 3; y++) {

                            int maxValue = 7;
                            int minValue = 0;

                            int a = i + x;
                            int b = j + y;

                            a = Math.min(a, maxValue);
                            a = Math.max(a, minValue);
                            b = Math.min(b, maxValue);
                            b = Math.max(b, minValue);

                            moveResult = tryMove(board[i][j].getChecker(), a, b);

                            if (moveResult.getType() == MoveType.KILL) {
                                Move move = new Move(i, j, a, b);
                                killingMoves.add(move);
                            }
                            if (moveResult.getType() == MoveType.NORMAL) {
                                Move move = new Move(i, j, a, b);
                                normalMoves.add(move);

                            }

                        }

                    }
                }
            }
        }
        if (killingMoves.size() > 0) {
            Collections.shuffle(killingMoves);
            System.out.println(" Player possible kill moves " + killingMoves.size());

            return;

        }
        if (normalMoves.size() > 0) {
            Collections.shuffle(normalMoves);
            System.out.println(" Player possible normal moves " + normalMoves.size());

            return;

        }

        FlowPane losePane = new FlowPane();

        ImageView img3 = new ImageView(imageLose);
        img3.setFitHeight(300);
        img3.setFitWidth(300);
        losePane.getChildren().add(img3);
        losePane.relocate(250, 100);
        buttons.getChildren().add(losePane);

        Button endGameButton2 = new Button("Zakończ Grę");
        endGameButton2.setOnAction(r -> System.exit(0));
        endGameButton2.setAlignment(Pos.CENTER);
        endGameButton2.setPrefSize(200, 200);

        Button resetButton2 = new Button("Restart Game");
        resetButton2.setOnAction(r -> {
            tileGroup.getChildren().remove(0, tileGroup.getChildren().size());
            pieceGroup.getChildren().remove(0, pieceGroup.getChildren().size());
            makeChessBoard();
            buttons.getChildren().removeAll(resetButton2, endGameButton2, losePane);
        });

        resetButton2.setPrefSize(200, 200);
        resetButton2.setAlignment(Pos.CENTER);
        buttons.getChildren().add(resetButton2);
        resetButton2.relocate(200, 400);
        buttons.getChildren().add(endGameButton2);
        endGameButton2.relocate(400, 400);
    }

    private MoveResult tryMovePlayer(OneChecker checker, int newX, int newY) {
        MoveResult move = new MoveResult(MoveType.NONE);
        if (checker.getType() == CheckerType.WHITE) {
            return new MoveResult(MoveType.NONE);
        } else {
            move = tryMove(checker, newX, newY);
        }
        return move;
    }

    private OneChecker makeChecker(CheckerType type, int x, int y) {

        OneChecker checker = new OneChecker(type, x, y);

        checker.setOnMouseReleased(e -> {
            int newX = toBoard(checker.getLayoutX());
            int newY = toBoard(checker.getLayoutY());

            MoveResult result;

            if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
                result = new MoveResult(MoveType.NONE);
            } else {
                result = tryMovePlayer(checker, newX, newY);
            }

            int x0 = toBoard(checker.getOldX());
            int y0 = toBoard(checker.getOldY());

            switch (result.getType()) {
                case NONE:
                    checker.abortMove();
                    break;
                case NORMAL:
                    checker.move(newX, newY);
                    board[x0][y0].setChecker(null);
                    board[newX][newY].setChecker(checker);
                    moveComputer();
                    didComputerWin();
                    break;
                case KILL:
                    checker.move(newX, newY);
                    board[x0][y0].setChecker(null);
                    board[newX][newY].setChecker(checker);

                    OneChecker otherChecker = result.getChecker();
                    board[toBoard(otherChecker.getOldX())][toBoard(otherChecker.getOldY())].setChecker(null);
                    pieceGroup.getChildren().remove(otherChecker);
                    if (doubleKillPlayer(newX, newY)) {
                        didComputerWin();
                        break;
                    }
                    moveComputer();
                    didComputerWin();
                    break;
            }
        });

        return checker;
    }

    private void makeChessBoard() {

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;

                tileGroup.getChildren().add(tile);

                OneChecker checker = null;


                if (y <= 2 && (x + y) % 2 != 0) {
                    checker = makeChecker(CheckerType.RED, x, y);
                }
                if (y >= 5 && (x + y) % 2 != 0) {
                    checker = makeChecker(CheckerType.WHITE, x, y);
                }
                if (checker != null) {
                    tile.setChecker(checker);
                    pieceGroup.getChildren().add(checker);
                }
            }
        }

    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageBack, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);


        GridPane root = new GridPane();
        Pane pane = new Pane();

        Button resetButton = new Button("Restart");
        resetButton.setOnAction(r -> {
            tileGroup.getChildren().remove(0, tileGroup.getChildren().size());
            pieceGroup.getChildren().remove(0, pieceGroup.getChildren().size());
            makeChessBoard();
        });

        Button endGameButton = new Button("Zakończ");
        endGameButton.setOnAction(r -> System.exit(0));

        endGameButton.setAlignment(Pos.CENTER);
        endGameButton.setPrefSize(100, 40);

        resetButton.setPrefSize(100, 40);
        resetButton.setAlignment(Pos.CENTER);

        buttons.getChildren().add(resetButton);
        resetButton.relocate(0, 0);
        buttons.getChildren().add(endGameButton);
        endGameButton.relocate(100, 0);

        pane.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE + 100);
        tileGroup.relocate(0, 50);
        pieceGroup.relocate(0, 50);
        pane.getChildren().addAll(tileGroup, pieceGroup, buttons);

        root.getChildren().add(pane);
        root.setBackground(background);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 920, 920, Color.WHITE);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();

        makeChessBoard();


    }
}
