package game.example.jntm.view.biecaibaikuai;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import game.example.jntm.R;

public class GridGameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private boolean isDrawing;
    private Paint mPaint;
    /**
     * 被点击
     */
    private boolean isTouch;

    /**
     * surface持有者
     */
    private SurfaceHolder mHolder;
    /**
     * 当前画布
     */
    private Canvas mCanvas;
    /**
     * view的宽度
     */
    private int mWidth;
    /**
     * View的高度
     */
    private int mHeight;
    /**
     * 每格宽度
     */
    private int gridWidth;
    /**
     * 每格高度
     */
    private int gridHeight;
    /**
     * 每行格子个数
     */
    private int gridNumX = 4;
    /**
     * 每列格子个数
     */
    private int gridNumY = 5;
    /**
     * 偏移量
     */
    private float mOffSet;

    /**
     * 格子集合
     */
    private List<Integer> grids;

    /**
     * 生成随机位置的格子
     */
    private Random random;

    /**
     * 点击位置横坐标
     */
    private float pointX;
    /**
     * 点击位置纵坐标
     */
    private float pointY;
    /**
     * 被点击方块的下标
     */
    private int touchPosition;
    /**
     * 默认宽度设置为300dp
     */
    private static final int DEFAULT_WIDTH = 300;
    /**
     * 默认高度设置为400dp
     */
    private static final int DEFAULT_HEIGHT = 400;

    private float speed = 3;

    private Timer timer;

    private BieCaiBaiKuaiListener listener;

    private final Bitmap kun = BitmapFactory.decodeResource(getResources(), R.drawable.kun_avatar);

    public GridGameView(Context context) {
        this(context, null);
    }

    public GridGameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridGameView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        timer = new Timer();

        random = new Random();
        grids = new ArrayList<>();

        //初始化一组方块，屏幕外的上方下方个加一个方块
        for (int i = 0; i < gridNumY + 2; i++) {
            grids.add(random.nextInt(gridNumX));
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 设置一些变量的尺寸
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = dp2px(DEFAULT_WIDTH);
        } else {
            mWidth = Math.max(widthSize, dp2px(DEFAULT_WIDTH));
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = dp2px(DEFAULT_HEIGHT);
        } else {
            mHeight = Math.max(heightSize, dp2px(DEFAULT_HEIGHT));
        }
        gridWidth = mWidth / gridNumX;
        gridHeight = mHeight / gridNumY;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 开始绘画
        isDrawing = true;
        // 启动绘画线程
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
        if (kun != null && !kun.isRecycled()) {
            kun.recycle();
        }
    }

    @Override
    public void run() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isDrawing) timer.cancel();
                final long b = System.currentTimeMillis();
                mOffSet += speed;
                draw();
                if (mOffSet >= gridHeight) {
                    Log.e("Offset:", mOffSet + "|||||" + (mOffSet >= gridHeight));
                    if (grids.get(0) == gridNumX) {
                        Log.e("grid.get(0)::", grids.get(0) + "");
                        //移出屏幕外的黑块到达最大偏移量，游戏继续
                        mOffSet = 0f;
                        grids.remove(0);
                        grids.add(random.nextInt(gridNumX));
                    } else {
                        //屏幕内的黑块到达最大偏移量，游戏结束
                        isDrawing = false;
                        if (listener != null) {
                            listener.onOver();
                        }
                    }
                }
                speed += 0.1f;
                if (listener != null) {
                    listener.onSpeedAdd(speed);
                }
                final long l = System.currentTimeMillis();
                Log.e("time",l-b+"");
            }
        }, 1000, 10);
    }

    /**
     * 绘图
     */
    private void draw() {
        mCanvas = mHolder.lockCanvas();

        if (mCanvas != null) {
            // 设置画布背景为白色
            mCanvas.drawColor(0xffffffff);
            //绘制竖线
            drawLineY();
            //绘制横线
            drawLineX();
            //点击事件
            eventTouchDown();
            //画黑白块
            drawGrid();
            // 保证每次都将绘制的内容提交到服务器
            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    /**
     * 绘制纵向线
     */
    private void drawLineY() {
        mCanvas.save();
        for (int i = 0; i < gridNumX + 1; i++) {
            mCanvas.drawLine(gridWidth * i, 0, gridWidth * i, mHeight, mPaint);
        }
    }

    /**
     * 绘制横向线
     */
    private void drawLineX() {
        mCanvas.save();
        for (int i = 0; i < gridNumY + 1; i++) {
            mCanvas.drawLine(0, gridHeight * i + mOffSet, mWidth, gridHeight * i + mOffSet, mPaint);
        }
    }

    /**
     * 绘制格子
     */
    private void drawGrid() {
        mCanvas.save();
        for (int i = 0; i < grids.size(); i++) {
            mCanvas.drawBitmap(kun, null, new RectF(grids.get(i) * gridWidth, mHeight - gridHeight * (i + 1) + mOffSet, grids.get(i) * gridWidth + gridWidth,
                    mHeight - gridHeight * i + mOffSet), null);
        }
    }

    /**
     * 处理点击事件
     */
    private void eventTouchDown() {
        if (isTouch) {
            touchPosition = (int) (mHeight + mOffSet - pointY) / gridHeight;
            if (pointX - grids.get(touchPosition) * gridWidth > 0 && pointX - grids.get(touchPosition) * gridWidth < gridWidth) {
                //被点击的方块移出屏幕外
                grids.set(touchPosition, gridNumX);
            } else {
                //点击白块游戏结束
                isDrawing = false;
                if (listener != null) {
                    listener.onOver();
                }
            }
            isTouch = false;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointX = event.getX();
                pointY = event.getY();
                isTouch = true;
                if (listener != null) {
                    listener.onTouch();
                }
                break;
        }
        return true;
    }

    /**
     * dp转化为px工具
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setListener(BieCaiBaiKuaiListener listener) {
        this.listener = listener;
    }

    public boolean isDrawing() {
        return isDrawing;
    }
}