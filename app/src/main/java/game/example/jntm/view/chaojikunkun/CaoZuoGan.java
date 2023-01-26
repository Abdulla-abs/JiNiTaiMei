package game.example.jntm.view.chaojikunkun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class CaoZuoGan extends View {

    private float cx;
    private float cy;

    private float dx;
    private float dy;

    private Paint controlPaint;
    private Paint circlePaint;

    private boolean isPress = false;
    private Timer timer;

    private OnSliderListener listener;

    public CaoZuoGan(Context context) {
        super(context);
        init();
    }

    public CaoZuoGan(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CaoZuoGan(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        controlPaint = new Paint();
        controlPaint.setAntiAlias(true);
        controlPaint.setColor(Color.BLUE);
        controlPaint.setStyle(Paint.Style.FILL);

        circlePaint = new Paint();
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(3f);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isPress){
                    if (listener!=null){
                        if (cx > getWidth()/2 + 20){
                            listener.onRight();
                        }else if (cx < getWidth()/2 -20){
                            listener.onLeft();
                        }
                    }
                }
            }
        },0,16);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 180, circlePaint);
        if (cx > getWidth() / 4 * 3) {
            cx = getWidth() / 4 * 3;
        } else if (cx < getWidth() / 4) {
            cx = getWidth() / 4;
        }
        if (cy > getHeight() / 4 * 3) {
            cy = getHeight() / 4 * 3;
        } else if (cy < getHeight() / 4) {
            cy = getHeight() / 4;
        }
        canvas.drawCircle(cx, cy, 80, controlPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            dx = event.getX();
            dy = event.getY();
            cx = event.getX();
            cy = event.getY();
            performClick();
            isPress = true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            cx = event.getX();
            cy = event.getY();
        } else if (action == MotionEvent.ACTION_UP) {
            cx = getWidth() / 2;
            cy = getHeight() / 2;
            isPress = false;
            if (listener!=null){
                listener.onStand();
            }
        }

        invalidate();
        return super.onTouchEvent(event);
    }

    public void setListener(OnSliderListener listener) {
        this.listener = listener;
    }

    public interface OnSliderListener{
        void onLeft();

        void onRight();

        void onStand();
    }
}
