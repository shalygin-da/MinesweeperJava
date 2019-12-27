package shalygin.game.board;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.random;

public class Board {

    public Cell[][] board;
    private int w; //width
    private int h; //height
    private int emptyCells;
    private int uncoveredCells;
    public Map<Cell, Label> labelMap = new HashMap<>();
    public int end = 0;

    public Board(int w, int h, int mines) {
        this.w = w;
        this.h = h;
        this.emptyCells = w * h - mines;
        this.uncoveredCells = 0;
        createBoard(w, h, mines);
    }

    private void createBoard(int w, int h, int mines) {
        board = new Cell[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                board[i][j] = new Cell(i, j);
            }
        }
        for (int i = 0; i < mines; i++) {
            int x = (int) (random() * w);
            int y = (int) (random() * h);
            if (!board[x][y].isMine()) {
                board[x][y].setMine();
            } else i--;
            setValues();
        }
    }

    private void setValues() {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Cell cell = board[i][j];
                if (cell.isMine()) {
                    List<Pair<Integer, Integer>> arounders = getArounders(cell.getX(), cell.getY());
                    for (Pair<Integer, Integer> arounder : arounders) {
                        Cell cur;
                        if (arounder.getKey() < w && arounder.getKey() >= 0 &&
                                arounder.getValue() < h && arounder.getValue() >= 0) {
                            cur = board[arounder.getKey()][arounder.getValue()];
                            if (!cur.isMine()) cur.setVal(cur.getVal() + 1);
                        }
                    }
                }
            }
        }
    }

    private void resetValues() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].getVal() > 0) {
                    board[i][j].setVal(0);
                }
            }
        }
    }

    private List<Pair<Integer, Integer>> getArounders(int x, int y) {
        List<Pair<Integer, Integer>> arounders = new ArrayList<>();
        arounders.add(new Pair<>(x - 1, y - 1));
        arounders.add(new Pair<>(x - 1, y));
        arounders.add(new Pair<>(x - 1, y + 1));
        arounders.add(new Pair<>(x, y + 1));
        arounders.add(new Pair<>(x + 1, y + 1));
        arounders.add(new Pair<>(x + 1, y));
        arounders.add(new Pair<>(x + 1, y - 1));
        arounders.add(new Pair<>(x, y - 1));
        return arounders;
    }

    public void uncover(int x, int y) {
        Cell cell = board[x][y];
        if (!cell.isUncovered()) {
            cell.uncover();
            if (cell.isMine()) {
                if (uncoveredCells == 0) {
                    cell.removeMine();
                    int randomX = (int) (random() * w);
                    int randomY = (int) (random() * h);
                    if (!board[randomX][randomY].isMine() && (randomX != x || randomY != y)) {
                        board[randomX][randomY].setMine();
                        }
                    resetValues();
                    setValues();
                    uncover(x, y);
                    System.out.println("You hit a mine on a first move, so it was moved to another place");
                } else end = 2;
                return;
            } else {
                uncoveredCells++;
                if (cell.getVal() == 0) {
                    List<Pair<Integer, Integer>> arounders = getArounders(cell.getX(), cell.getY());
                    for (Pair<Integer, Integer> arounder : arounders) {
                        if (arounder.getKey() < w && arounder.getKey() >= 0 &&
                                arounder.getValue() < h && arounder.getValue() >= 0 &&
                                !board[arounder.getKey()][arounder.getValue()].isMine() &&
                                !board[arounder.getKey()][arounder.getValue()].isUncovered())
                            uncover(arounder.getKey(), arounder.getValue());
                      }
                  }
            }
        }
        if (uncoveredCells == emptyCells) {
            end = 1;
        }
    }

    public void createBoard(Pane inGameLayout) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Cell cell = board[i][j];
                Label label = new Label();
                if (cell.isUncovered()) {
                    String numberIcon = "-fx-background-color: white; -fx-border-color: black;" +
                            " -fx-font-size: 1.5em; -fx-font-weight: bold;";
                    switch (cell.getVal()) {
                        case 0:
                            label.setStyle("-fx-background-color: white; -fx-border-color: black;");
                            break;
                        case 1:
                            label.setText("1");
                            label.setAlignment(Pos.CENTER);

                            label.setStyle(numberIcon + "-fx-text-fill: blue");
                            break;
                        case 2:
                            label.setText("2");
                            label.setAlignment(Pos.CENTER);
                            label.setStyle(numberIcon + "-fx-text-fill: green");
                            break;
                        case 3:
                            label.setText("3");
                            label.setAlignment(Pos.CENTER);
                            label.setStyle(numberIcon + "-fx-text-fill: red");
                            break;
                        case 4:
                            label.setText("4");
                            label.setAlignment(Pos.CENTER);
                            label.setStyle(numberIcon + "-fx-text-fill: blueviolet");
                            break;
                        case 5:
                            label.setText("5");
                            label.setAlignment(Pos.CENTER);
                            label.setStyle(numberIcon + "-fx-text-fill: brown");
                            break;
                        case 6:
                            label.setText("6");
                            label.setAlignment(Pos.CENTER);
                            label.setStyle(numberIcon + "-fx-text-fill: cyan");
                            break;
                        case 7:
                            label.setText("7");
                            label.setAlignment(Pos.CENTER);
                            label.setStyle(numberIcon + "-fx-text-fill: magenta");
                            break;
                        case 8:
                            label.setText("8");
                            label.setAlignment(Pos.CENTER);
                            label.setStyle(numberIcon + "-fx-text-fill: black");
                            break;
                    }
                } else if (cell.isFlag()) {
                    label.setText("F");
                    label.setAlignment(Pos.CENTER);
                    label.setStyle("-fx-background-color: burlywood; -fx-border-color: black;" +
                            " -fx-font-size: 1.5em; -fx-font-weight: bold; -fx-text-fill: chocolate");
                } else if (cell.isMine() && cell.isUncovered()) {
                    label.setStyle("-fx-background-color: red; -fx-border-color: black;");
                } else { label.setStyle("-fx-background-color: gray; -fx-border-color: black;"); }
                label.setMinHeight(30);
                label.setMinWidth(30);
                label.setTranslateX(j * 30);
                label.setTranslateY(i * 30);
                labelMap.put(cell, label);
                inGameLayout.getChildren().addAll(label);
            }
        }
    }

    public int[][] getBoard() {
        int[][] outputBoard = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (board[i][j].isUncovered()) {
                    outputBoard[i][j] = board[i][j].getVal();
                } else {
                    outputBoard[i][j] = -1;
                }
            }
        }
        return outputBoard;
    }

}
