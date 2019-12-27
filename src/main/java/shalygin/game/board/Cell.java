package shalygin.game.board;

public class Cell {

    private int x;
    private int y;
    private boolean isMine = false;
    private boolean isFlag = false;
    private int val = 0;
    private boolean uncovered = false;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    boolean isMine() { return isMine; }
    void setMine() { isMine = true; }
    void removeMine() { isMine = false; }

    public boolean isFlag() { return isFlag; }
    public void setFlag() { isFlag = true; }
    public void removeFlag() { isFlag = false; }

    int getVal() { return val; }
    void setVal(int v) { this.val = v; }

    public boolean isUncovered() { return this.uncovered; }
    void uncover() { this.uncovered = true; }

    @Override
    public String toString() {
        return x + ", " + y + ", " + (isMine ? "Mine" : "Clear") + ", value: " + val;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return (this.getX() == cell.getX() && this.getY() == cell.getY());
    }
}
