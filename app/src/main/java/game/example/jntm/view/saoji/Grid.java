package game.example.jntm.view.saoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.Nullable;

import game.example.jntm.R;

public class Grid extends androidx.appcompat.widget.AppCompatTextView {

    private Pointer pointer;
    private boolean isBoom;
    private int roundBoomCount;

    private boolean showFlag = false;

    public Grid(Context context) {
        this(context, null);
    }

    public Grid(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Grid(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setTextSize(12);
        setTextColor(Color.WHITE);
        setGravity(Gravity.CENTER);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        if (pointer.isShow()) {
            if (isBoom) {
                setBackgroundResource(R.drawable.j);
            } else {
                setBackgroundResource(R.drawable.kun_avatar);
                setText(roundBoomCount == 0 ? "" : String.valueOf(roundBoomCount));
            }
        } else {
            if (showFlag){
                setBackgroundResource(R.drawable.ic_baseline_emoji_flags_24);
            }else {
                setBackgroundColor(Color.WHITE);
            }
        }
        super.onDraw(canvas);
    }

    public Pointer getPointer() {
        return pointer;
    }

    public void setPointer(Pointer pointer) {
        this.pointer = pointer;
    }

    public boolean isBoom() {
        return isBoom;
    }

    public void setBoom(boolean boom) {
        isBoom = boom;
    }

    public int getRoundBoomCount() {
        return roundBoomCount;
    }

    public void setRoundBoomCount(int roundBoomCount) {
        this.roundBoomCount = roundBoomCount;
    }

    public boolean isZero() {
        return roundBoomCount == 0;
    }

    public void toggleFlag() {
        showFlag = !showFlag;
        invalidate();
    }

    public boolean isShowFlag() {
        return showFlag;
    }
}
