package game.example.jntm.view.saoji;


public class Pointer {

    private boolean isShow = false;

    private int row; //行
    private int column; //列

    private int x;
    private int y;

    public Pointer(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Pointer(int row, int column, int x, int y) {
        this.row = row;
        this.column = column;
        this.x = x;
        this.y = y;
    }

    public Pointer(Pointer pointer) {
        this(pointer.row, pointer.column, pointer.getX(), pointer.getY());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    /**
     * 当x移动到最后，x归0，y移动到下一行
     *
     * @return 数据是否正常
     */
    public boolean moveToNextPosition() {
        if (x < row - 1) {
            x++;
        } else if (y < column - 1) {
            y++;
            x = 0;
        } else return false;

        return true;
    }

    @Override
    public String toString() {
        return "Pointer{" +
                "row=" + row +
                ", column=" + column +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pointer pointer = (Pointer) o;
        return x == pointer.x && y == pointer.y;
    }

}
