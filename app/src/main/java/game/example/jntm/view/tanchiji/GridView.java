package game.example.jntm.view.tanchiji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import game.example.jntm.R;
import game.example.jntm.ui.TanChiJiActivity;

public class GridView extends View {

    private Pointer pointer;
    private int index = 0;

    private boolean isSnackBody = false;
    private boolean isSnackHead = false;

    private boolean isFood = false;

    public GridView(Context context) {
        this(context,null);
    }

    public GridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (isSnackHead){
            canvas.drawBitmap(SnackView.kun,null,new RectF(0,0,getWidth(),getHeight()),null);
        }else if (isSnackBody){
            canvas.drawBitmap(SnackView.kun,null,new RectF(0,0,getWidth(),getHeight()),null);
        }else if(isFood){
            canvas.drawBitmap(SnackView.j,null,new RectF(0,0,getWidth(),getHeight()),null);
        }else {
            canvas.drawColor(Color.WHITE);
        }
    }

    public Pointer getPointer() {
        return pointer;
    }

    public void setPointer(Pointer pointer) {
        this.pointer = pointer;
    }

    public void setSnackBody(boolean snackBody) {
        isSnackBody = snackBody;
        isSnackHead = false;
        isFood = false;
    }

    public void setSnackHead(boolean snackHead) {
        isSnackHead = snackHead;
        isSnackBody = false;
        isFood = false;
    }

    public void setNone(){
        isSnackHead = false;
        isSnackBody = false;
    }

    public void setFood(boolean food) {
        isFood = food;
        isSnackHead = false;
        isSnackBody = false;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
