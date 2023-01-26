package game.example.jntm.view.chaojikunkun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import game.example.jntm.R;
import game.example.jntm.utils.ScreenUtils;

public class Kun implements Runnable{

    private int x = 0;
    private int y = 0;
    private int width;
    private int height;
    private int statue = Constants.KUN_STATUE_S;
    private Bitmap kun = Constants.kun.get(Constants.KUN_STATUE_S);
    private int xSpeed = 0;
    private int ySpeed = 0;
    private int jumpTimes = 0;
    private final List<ZhangAi> zhangAis = Constants.G_ZA.get(1);

    private ChaoJiKunKunSFView SF;

    public Kun(ChaoJiKunKunSFView SF) {
        width = kun.getWidth();
        height = kun.getHeight();
        this.SF = SF;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true){
            kun = Constants.kun.get(statue);

            boolean obt = false;
            boolean rl = false;
            for (ZhangAi zhangAi : Objects.requireNonNull(zhangAis)) {
                if ((zhangAi.getY() - y > 0 && zhangAi.getY() - y <= kun.getHeight()) && (x >= zhangAi.getX() && x <= zhangAi.getX()+zhangAi.getWidth())) {
                    obt = true;
                }
                if (x+width >= zhangAi.getX() ) {
                    rl = true;
                }
            }
            if (obt && jumpTimes == 0){
                ySpeed = 0;
            }else{
                if (jumpTimes != 0){//跳跃状态
                    jumpTimes--;
                }else {
                    fall();
                }
            }


            if (!rl){
                x+=xSpeed;
            }
            y+=ySpeed;

            SF.invalidate();

            try {
                Thread.sleep(32);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveLeft() {
        xSpeed = -6;
        statue = Constants.KUN_STATUE_L;
    }

    public void moveRight() {
        xSpeed = 6;
        statue = Constants.KUN_STATUE_R;
    }

    public void jump() {
        if (jumpTimes == 0 && statue != Constants.KUN_STATUE_F){
            jumpTimes = 30;
            ySpeed = -6;
            statue = Constants.KUN_STATUE_U;
        }
    }

    private void fall() {
        ySpeed = 6;
        statue = Constants.KUN_STATUE_F;
    }

    public void stand() {
        xSpeed = 0;
        statue = Constants.KUN_STATUE_S;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }


    public Bitmap getKun() {
        return kun;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }


}
