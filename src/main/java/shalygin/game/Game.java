package shalygin.game;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import shalygin.game.board.Cell;
import shalygin.solver.Solver;
import shalygin.game.board.Board;

import java.util.Map;

import static shalygin.game.Main.inGameLayout;

class Game {

    private static Board board;
    private Solver solver;
    boolean solve;

    private static void createPlayingBoard(Pane pane, int w, int h, int mines) {
        board = new Board(w, h, mines);
        board.createBoard(pane);
    }

    void start(int w, int h, int mines) {
        createPlayingBoard(new Pane(), w, h, mines);
        board.createBoard(inGameLayout);
        solver = new Solver(w, h, mines);
        System.out.println("\nNew solver started successfully!");

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (solve) {
                    Main.step.setOnAction(e -> {
                        if (board.end == 0) {
                            solver.solve();
                            if (board.end == 1) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("You won!");
                                alert.setHeaderText("Congratualtions!");
                                alert.showAndWait();
                            } else if (board.end == 2) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("You lost!");
                                alert.setHeaderText("You heckin' donkey!");
                                alert.show();
                            }
                            solver.board.createBoard(inGameLayout);
                        }
                    });
                } else {
                    for (Map.Entry<Cell, Label> entry : board.labelMap.entrySet()) {
                        if (board.end == 0) {
                            entry.getValue().setOnMouseClicked(event -> {
                                if (event.getButton() == MouseButton.PRIMARY &&
                                        !board.board[entry.getKey().getX()][entry.getKey().getY()].isFlag()) {
                                    board.uncover(entry.getKey().getX(), entry.getKey().getY());
                                    if (board.end == 2) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("You lost!");
                                        alert.setHeaderText("You tried your best, but my mines are superior!");
                                        alert.showAndWait();
                                        Main.window.setScene(Main.scene);
                                    } else if (board.end == 1) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("You won!");
                                        alert.setHeaderText("Congratulations!");
                                        alert.showAndWait();
                                        Main.window.setScene(Main.scene);
                                    }
                                }
                                if (event.getButton() == MouseButton.SECONDARY &&
                                        !board.board[entry.getKey().getX()][entry.getKey().getY()].isUncovered()) {
                                    if (board.board[entry.getKey().getX()][entry.getKey().getY()].isFlag()) {
                                        board.board[entry.getKey().getX()][entry.getKey().getY()].removeFlag();
                                    } else {
                                        board.board[entry.getKey().getX()][entry.getKey().getY()].setFlag();
                                    }
                                }
                                board.createBoard(inGameLayout);
                            });
                        }
                    }
                }
            }
        };
        timer.start();
    }
}