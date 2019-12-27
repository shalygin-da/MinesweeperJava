package shalygin.solver;


import javafx.util.Pair;
import shalygin.game.board.Cell;
import java.util.ArrayList;
import java.util.List;

class SolverBoard {

    SolverCell[][] solverBoard;
    private int w;
    private int h;

    SolverBoard(int[][] board){
        w = board.length;
        h = board[0].length;
    }

    List<Cell> createSolverBoard(int[][] board) {
        SolverCell[][] newBoard = new SolverCell[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (board[i][j] == -1) {
                    SolverCell solverCell = new SolverCell(i, j, false);
                    newBoard[i][j] = solverCell;
                } else if (board[i][j] >= 0) {
                    SolverCell solverCell = new SolverCell(i, j, true);
                    solverCell.setVal(board[i][j]);
                    newBoard[i][j] = solverCell;
                }
            }
        }
        solverBoard = newBoard;
        return update();
    }

    private List<Cell> update() {
        List<Cell> cellList = new ArrayList<>();
        updateSolverCells();

        for (int i = 0; i < w; i++) { //setting flags in obvious places
            for (int j = 0; j < h; j++) {
                SolverCell cell = solverBoard[i][j];
                if (cell.getVal() > 0 && cell.getVal() == cell.uncheckedAround) {
                    List<Pair<Integer, Integer>> arounders = getArounders(i, j);
                    for (Pair<Integer, Integer> arounder : arounders) {
                        if (arounder.getKey() < w && arounder.getKey() >= 0 &&
                                arounder.getValue() < h && arounder.getValue() >= 0 &&
                                !solverBoard[arounder.getKey()][arounder.getValue()].isChecked()) {
                            solverBoard[arounder.getKey()][arounder.getValue()].setFlag();
                            cellList.add(new Cell(arounder.getKey(), arounder.getValue()));
                        }
                    }
                    updateSolverCells();
                }
            }
        }

        for (int i = 0; i < w; i++) { //uncovering empty cells for flags placed
            for (int j = 0; j < h; j++) {
                SolverCell cell = solverBoard[i][j];
                if (cell.getVal() != 0 && cell.getVal() == cell.flagsAround) {
                    List<Pair<Integer, Integer>> arounders = getArounders(i, j);
                    for (Pair<Integer, Integer> arounder : arounders) {
                        if (arounder.getKey() < w && arounder.getKey() >= 0 &&
                                arounder.getValue() < h && arounder.getValue() >= 0  &&
                                !solverBoard[arounder.getKey()][arounder.getValue()].isChecked() &&
                                !solverBoard[arounder.getKey()][arounder.getValue()].isFlag()) {
                            solverBoard[arounder.getKey()][arounder.getValue()].setEmpty();
                        }
                    }
                    updateSolverCells();
                }
            }
        }

        for (int i = 0; i < w; i++) { //setting new chances for cells
            for (int j = 0; j < h; j++) {
                SolverCell cell = solverBoard[i][j];
                if (cell.getVal() != 0 && cell.isChecked()) {
                    List<Pair<Integer, Integer>> arounders = getArounders(i, j);
                    for (Pair<Integer, Integer> arounder : arounders) {
                        if (arounder.getKey() < w && arounder.getKey() >= 0 &&
                                arounder.getValue() < h && arounder.getValue() >= 0 &&
                                !solverBoard[arounder.getKey()][arounder.getValue()].isChecked()) {
                            double previousChance = solverBoard[arounder.getKey()][arounder.getValue()].getMineChance();
                            double newChance = previousChance +
                                    ((double) (cell.getVal() - cell.flagsAround)) /
                                            ((double) (cell.uncheckedAround - cell.flagsAround));
                            solverBoard[arounder.getKey()][arounder.getValue()].setMineChance(newChance);
                        }
                    }
                }
            }
        }
        return cellList;
    }

    private void updateSolverCells() {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                SolverCell cell = solverBoard[i][j];
                cell.reset();
                if (cell.getVal() != 0 && cell.isChecked()) {
                    List<Pair<Integer, Integer>> arounders = getArounders(i, j);
                    for (Pair<Integer, Integer> arounder : arounders) {
                        if (arounder.getKey() < w && arounder.getKey() >= 0 &&
                                arounder.getValue() < h && arounder.getValue() >= 0 ) {
                            if (solverBoard[arounder.getKey()][arounder.getValue()].isFlag()) cell.flagsAround++;
                            if (!solverBoard[arounder.getKey()][arounder.getValue()].isChecked())
                                cell.uncheckedAround++;
                        }
                    }
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


}
