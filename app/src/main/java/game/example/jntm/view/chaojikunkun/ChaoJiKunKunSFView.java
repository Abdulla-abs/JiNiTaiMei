package game.example.jntm.view.chaojikunkun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import game.example.jntm.R;
import game.example.jntm.utils.ScreenUtils;

public class ChaoJiKunKunSFView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private int mWidth;
    private int mHeight;

    private int index = 1;

    private Timer timer;
    /**
     * 当前画布
     */
    private Canvas mCanvas;
    /**
     * surface持有者
     */
    private SurfaceHolder mHolder;
    /**
     * 默认宽度设置为300dp
     */
    private static final int DEFAULT_WIDTH = 600;
    /**
     * 默认高度设置为400dp
     */
    private static final int DEFAULT_HEIGHT = 400;

    private boolean isDrawing = false;

    private Kun kun;
    private Paint paint;

    public ChaoJiKunKunSFView(Context context) {
        this(context, null);
    }

    public ChaoJiKunKunSFView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChaoJiKunKunSFView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        // 设置可以获取焦点
        setFocusable(true);
        // 进入触摸输入模式后,该控件是否还有获得焦点的能力
        setFocusableInTouchMode(true);
        // 是否保持屏幕常亮
        setKeepScreenOn(true);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.FILL);

        kun = new Kun(this);
        timer = new Timer();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        if (widthMode == MeasureSpec.AT_MOST) {
//            mWidth = dp2px(DEFAULT_WIDTH);
//        } else {
//            mWidth = Math.max(widthSize, dp2px(DEFAULT_WIDTH));
//        }
//        if (heightMode == MeasureSpec.AT_MOST) {
//            mHeight = dp2px(DEFAULT_HEIGHT);
//        } else {
//            mHeight = Math.max(heightSize, dp2px(DEFAULT_HEIGHT));
//        }
//        int width = wall.getWidth();
//        int height = wall.getHeight();
//        final float x = mWidth / 18;
//        final float y = x;
//        matrix.postScale(x/width,y/height);
//        wall = Bitmap.createBitmap(wall,0,0,wall.getWidth(),wall.getHeight(),matrix,true);
//        wallWidth = wall.getWidth();
//        wallHeight = wall.getHeight();

//        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        mHeight = bottom - top;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        // 开始绘画
        isDrawing = true;
        // 启动绘画线程
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        isDrawing = false;
    }

    @Override
    public void run() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isDrawing) timer.cancel();
                draw();
            }
        }, 100, 10);
    }

    private void draw() {
        mCanvas = mHolder.lockCanvas();
        //绘制人物
        if (mCanvas != null) {
            mCanvas.drawColor(0xFF33AECD);
            drawWall();
            mCanvas.drawBitmap(kun.getKun(), kun.getX() + kun.getxSpeed(), kun.getY() + kun.getySpeed(), null);
            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }


    private void drawWall() {
        final List<ZhangAi> zhangAis = Constants.G_ZA.get(index);
        for (ZhangAi zhangAi : Objects.requireNonNull(zhangAis)) {
            mCanvas.drawBitmap(zhangAi.getIm(), zhangAi.getX(), zhangAi.getY(), null);
        }
    }

    public Kun getKun() {
        return kun;
    }

    /**
     * dp转化为px工具
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }


}
