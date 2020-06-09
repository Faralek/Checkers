import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Checkers extends Application {

    private final Image imageback = new Image("file:/IdeaProjects/Checkers/src/Main/resources/chessboard.png");
    private Image whiteChecker = new Image("file:/IdeaProjects/Checkers/src/Main/resources/whiteChecker.png");
    private Image blackChecker = new Image("file:/IdeaProjects/Checkers/src/Main/resources/blackChecker.png");
    private final FlowPane checkers = new FlowPane(Orientation.HORIZONTAL);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setPadding(new Insets(111, 12.5, 13.5, 72));
        grid.setHgap(8);
        grid.setVgap(8);
     //   grid.setBackground(background);
        grid.setGridLinesVisible(true);

        grid.getColumnConstraints().add(new ColumnConstraints(77));
        grid.getColumnConstraints().add(new ColumnConstraints(77));
        grid.getColumnConstraints().add(new ColumnConstraints(77));
        grid.getColumnConstraints().add(new ColumnConstraints(77));
        grid.getColumnConstraints().add(new ColumnConstraints(77));
        grid.getColumnConstraints().add(new ColumnConstraints(77));
        grid.getColumnConstraints().add(new ColumnConstraints(77));
        grid.getColumnConstraints().add(new ColumnConstraints(77));
        grid.getColumnConstraints().add(new ColumnConstraints(77));

        grid.getRowConstraints().add(new RowConstraints(77));
        grid.getRowConstraints().add(new RowConstraints(77));
        grid.getRowConstraints().add(new RowConstraints(77));
        grid.getRowConstraints().add(new RowConstraints(77));
        grid.getRowConstraints().add(new RowConstraints(77));
        grid.getRowConstraints().add(new RowConstraints(77));
        grid.getRowConstraints().add(new RowConstraints(77));
        grid.getRowConstraints().add(new RowConstraints(77));



        ImageView img = new ImageView(blackChecker);
     //   ImageView img2 = new ImageView(whiteChecker);
        checkers.getChildren().add(img);
      //  checkers.getChildren().add(img2);

        img.resize(60,60);

        checkers.setMaxSize(60,60);

        grid.add(checkers, 0, 0, 1, 1);

        Scene scene = new Scene(grid, 900, 900, Color.WHITE);

        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
