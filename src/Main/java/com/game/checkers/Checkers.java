package com.game.checkers;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

    private final Image imageBack = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("chessboard.png")));

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

        int x0 = toBoard(checker.getOldX());
        int y0 = toBoard(checker.getOldY());

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

    private MoveResult moveComputer() {

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

            return moveResult;

        }
        if (normalMoves.size() > 0) {
            Collections.shuffle(normalMoves);
            System.out.println("normal " + normalMoves.size());

            OneChecker checker = board[normalMoves.get(0).positionX][normalMoves.get(0).positionY].getChecker();
            board[normalMoves.get(0).positionX][normalMoves.get(0).positionY].getChecker().move(normalMoves.get(0).directionX, normalMoves.get(0).directionY);
            board[normalMoves.get(0).positionX][normalMoves.get(0).positionY].setChecker(null);
            board[normalMoves.get(0).directionX][normalMoves.get(0).directionY].setChecker(checker);

            return moveResult;

        }
        return moveResult;
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

                    break;
                case KILL:
                    checker.move(newX, newY);
                    board[x0][y0].setChecker(null);
                    board[newX][newY].setChecker(checker);

                    OneChecker otherChecker = result.getChecker();
                    board[toBoard(otherChecker.getOldX())][toBoard(otherChecker.getOldY())].setChecker(null);
                    pieceGroup.getChildren().remove(otherChecker);
                    moveComputer();

                    break;
            }
        });

        return checker;
    }

    private void resetChessBoard() {

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;

                tileGroup.getChildren().add(tile);

                OneChecker checker = null;

                if (y <= 2 && (x + y) % 2 != 0) {
                    checker = makeChecker(CheckerType.RED, x, y);
                } else {
                    if (tile.getChecker() != null) ;
                    pieceGroup.getChildren().remove(tile.getChecker());

                }
                if (y >= 5 && (x + y) % 2 != 0) {
                    checker = makeChecker(CheckerType.WHITE, x, y);
                } else {
                    if (tile.getChecker() != null) ;
                    pieceGroup.getChildren().remove(tile.getChecker());
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

        pane.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        pane.getChildren().addAll(tileGroup, pieceGroup);


        root.getChildren().add(pane);
        root.setBackground(background);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 930, 930, Color.WHITE);

        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();

        resetChessBoard();


    }
}