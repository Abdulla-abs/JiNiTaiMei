package game.example.jntm.view.chaojikunkun;

import android.graphics.Bitmap;

public class ZhangAi {

    private int type;
    private Bitmap im;
    private float x;
    private float y;
    private int width;
    private int height;

    public ZhangAi(int type, float x, float y) {
        this.type = type;
        this.im = Constants.wall;
        this.width = im.getWidth();
        this.height = im.getHeight();
        this.x = x;
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public Bitmap getIm() {
        return im;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
