package game.example.jntm.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

import game.example.jntm.MainActivity;
import game.example.jntm.R;
import game.example.jntm.base.BaseActivity;
import game.example.jntm.utils.SoundPoolUtil;
import game.example.jntm.view.tanchiji.SnackView;
import game.example.jntm.view.tanchiji.SnakeListener;
import game.example.jntm.view.tanchiji.Pointer;


public class TanChiJiActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener,
        SnakeListener {

    private GestureDetector gestureDetector;

    private ConstraintLayout mainCons;
    private SnackView snackView;
    private TextView score;
    private TextView time;

    private final Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    private final SoundPoolUtil soundPoolUtil = SoundPoolUtil.getInstance();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tanchiji;
    }

    @Override
    protected void initView() {
        mainCons = (ConstraintLayout) findViewById(R.id.mainCons);
        snackView = (SnackView) findViewById(R.id.snackView);
        score = (TextView) findViewById(R.id.score);
        time = (TextView) findViewById(R.id.time);
    }

    @Override
    protected void initData() {
        gestureDetector = new GestureDetector(this, this);

        soundPoolUtil.loadR(this, MainActivity.SOUND_TAG[0],R.raw.ji);
        soundPoolUtil.loadR(this,MainActivity.SOUND_TAG[5],R.raw.niganma);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initEvent() {
        mainCons.setOnTouchListener(this);
        snackView.setSnakeListener(this,handler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (snackView.isRun()) {
            snackView.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!snackView.isRun()) {
            snackView.run();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        Log.d("TAG", "onTouch: " + x + " " + y);
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.e("onShowPress", e.getAction() + "");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.e("Scoll", e1.getAction() + "");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.e("onFling", "e1 Action"+e1.getAction() +"   " + velocityX + "    e2 Action "+e2.getAction() +"  " + velocityX+"");
        final float downX = e1.getX();
        final float downY = e1.getY();
        final float upX = e2.getX();
        final float upY = e2.getY();

        final float x = downX - upX;
        final float y = downY - upY;

        int action1 = SnackView.ACTION_RIGHT;

        //左滑
        if (x > 0){
            action1 = SnackView.ACTION_LEFT;
        }else {//右划
            action1 = SnackView.ACTION_RIGHT;
        }

        int action2 = SnackView.ACTION_DOWN;
        //上滑
        if (y > 0){
            action2 = SnackView.ACTION_TOP;
        }else {
            action2 = SnackView.ACTION_DOWN;
        }

        if (Math.abs(x) > Math.abs(y)){
            snackView.setAction(action1);
        }else {
            snackView.setAction(action2);
        }

        soundPoolUtil.play(MainActivity.SOUND_TAG[0]);

        return false;
    }

    @Override
    public void onSnakeLongAdd(List<Pointer> snake, int size) {
        final int relaSize = size - SnackView.snakeLong;
        score.setText("分数："+relaSize*5);

        soundPoolUtil.play(MainActivity.SOUND_TAG[0]);
    }

    @Override
    public void onSnakeDie(List<Pointer> snake) {

        soundPoolUtil.play(MainActivity.SOUND_TAG[5]);
//        new AlertDialog.Builder(this)
//                .setTitle("游戏结束")
//                .setMessage("重新开始？")
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(TanChiJiActivity.this, TanChiJiActivity.class));
//                        dialog.dismiss();
//                        TanChiJiActivity.this.finish();
//                    }
//                })
//                .create()
//                .show();
    }
}