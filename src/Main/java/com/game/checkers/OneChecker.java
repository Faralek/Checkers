package com.game.checkers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.util.Objects;

import static com.game.checkers.Checkers.TILE_SIZE;

public class OneChecker extends StackPane {

    private final CheckerType type;

    private double mouseX, mouseY;
    private double oldX, oldY;

    private final Image whiteChecker1 = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("whiteChecker.png")));
    ImageView img2 = new ImageView(whiteChecker1);
    private final Image blackChecker1 = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("blackChecker.png")));
    ImageView img = new ImageView(blackChecker1);

    public CheckerType getType() {
        return type;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public OneChecker(CheckerType type, int x, int y) {
        this.type = type;

        move(x, y);

        FlowPane checker = new FlowPane();

        if (type == CheckerType.RED) {
            img.setFitWidth(100);
            img.setFitHeight(100);
            checker.getChildren().add(img);
        }
        if (type == CheckerType.WHITE) {
            img2.setFitWidth(100);
            img2.setFitHeight(100);
            checker.getChildren().add(img2);
        }

        getChildren().addAll(checker);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
    }

    public void move(int x, int y) {
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX, oldY);
    }

    public void abortMove() {
        relocate(oldX, oldY);
    }


}
