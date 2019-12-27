package shalygin.solver;

import shalygin.game.board.Board;
import shalygin.game.board.Cell;

import java.util.List;

import static java.lang.Math.random;

public class Solver {

    public Board board;
    private int w;
    private int h;
    private int fails;
    private int count;
    private SolverBoard solverBoard;

    public Solver(int w, int h, int mines) {
        this.w = w;
        this.h = h;
        board = new Board(w, h, mines);
        solverBoard = new SolverBoard(board.getBoard());
        count = 0;
    }

    public void solve() {
        if (count == 0) {
            makeRandomMove();
            return;
        }
        solverBoard.createSolverBoard(board.getBoard()); // finding the best move
        double minimumChance = Double.MAX_VALUE;
        Cell cell = new Cell(0, 0);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                SolverCell solverCell = solverBoard.solverBoard[i][j];
                if (solverCell.getMineChance() > 0 &&
                        solverCell.getMineChance() < minimumChance &&
                        !solverCell.isFlag()) {
                    cell = new Cell(solverCell.getX(), solverCell.getY());
                    minimumChance = solverCell.getMineChance();
                }
            }
        }
        if (fails > 2) {
            while (!makeRandomMove()) fails = 0;
        }
        if (!move(cell.getX(), cell.getY())) fails++;
    }

    private boolean move(int x, int y) {
        if (board.board[y][x].isUncovered() || board.board[y][x].isFlag()) {
            return false;
        }
        List<Cell> cellList;
        board.uncover(x, y);
        cellList = solverBoard.createSolverBoard(board.getBoard());
        for (Cell cell : cellList) {
            board.board[cell.getY()][cell.getX()].setFlag();
        }
        count++;
        return true;
    }

    private boolean makeRandomMove() {
        int x = (int) (random() * w);
        int y = (int) (random() * h);
        return move(x, y);
    }

}
