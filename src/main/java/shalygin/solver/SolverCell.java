package shalygin.solver;

class SolverCell {

    private int x;
    private int y;
    private int val;
    int uncheckedAround;
    int flagsAround;
    private double mineChance;
    private double notMineChance;
    private boolean mine;
    private boolean flag = false;
    private boolean checked;

    SolverCell(int x, int y, boolean checked) {
        this.x = x;
        this.y = y;
        this.checked = checked;
        val = 0;
        uncheckedAround = 0;
        flagsAround = 0;
        mineChance = 0;
        notMineChance = 0;
        mine = true;
    }

    int getX() { return x; }

    int getY() { return y; }

    int getVal() { return val; }
    void setVal(int val) { this.val = val; }


    double getMineChance() {
        if (!mine) return notMineChance;
        return mineChance;
    }
    void setMineChance(double chan) { mineChance = chan; }


    boolean isChecked() { return checked; }

    void setFlag() {
        mineChance = 100;
        flag = true;
    }

    boolean isFlag() {
        return flag;
    }


    void reset() {
        uncheckedAround = 0;
        flagsAround = 0;
    }


    void setEmpty() {
        notMineChance = 0.01;
        mine = false;
    }
}
