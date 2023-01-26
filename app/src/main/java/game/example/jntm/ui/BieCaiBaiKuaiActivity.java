package game.example.jntm.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import game.example.jntm.MainActivity;
import game.example.jntm.R;
import game.example.jntm.base.BaseActivity;
import game.example.jntm.base.BaseActivity2;
import game.example.jntm.utils.SPUtils;
import game.example.jntm.utils.SoundPoolUtil;
import game.example.jntm.view.biecaibaikuai.BieCaiBaiKuaiListener;
import game.example.jntm.view.biecaibaikuai.GridGameView;
import game.example.jntm.view.saoji.Grid;

public class BieCaiBaiKuaiActivity extends BaseActivity {

    private GridGameView gridGameView;
    private TextView speedTip;

    private final SoundPoolUtil soundPoolUtil = SoundPoolUtil.getInstance();
    private TextView historyTv;

    private SPUtils spUtils;
    private float history;

    private int soundIndex = 0;

//    @Override
//    protected View getLayoutView() {
//        gridGameView = new GridGameView(this);
//        return gridGameView;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bie_cai_bai_kuai;
    }

    @Override
    protected void initView() {
//        speedTip = new TextView(this);
//        speedTip.setTextSize(22f);
//        speedTip.setGravity(Gravity.CENTER_HORIZONTAL);
//        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) speedTip.getLayoutParams();
//        layoutParams.setMargins(0,100,0,0);
//        speedTip.setLayoutParams(layoutParams);
//        final ViewGroup.LayoutParams layoutParams = speedTip.getLayoutParams();

//        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(speedTip.getMinHeight(),speedTip.getMinWidth());
//        layoutParams.height = speedTip.getMinHeight();
//        layoutParams.width = speedTip.getMinWidth();
//        speedTip.setLayoutParams(layoutParams);
//        addContentView(speedTip,layoutParams);

        gridGameView = (GridGameView) findViewById(R.id.gridGameView);
        speedTip = (TextView) findViewById(R.id.speed_tip);
        historyTv = (TextView) findViewById(R.id.history);

    }

    @Override
    protected void initData() {
        soundPoolUtil.loadR(this, MainActivity.SOUND_TAG[0], R.raw.ji);
        soundPoolUtil.loadR(this, MainActivity.SOUND_TAG[1], R.raw.ni);
        soundPoolUtil.loadR(this, MainActivity.SOUND_TAG[2], R.raw.tai);
        soundPoolUtil.loadR(this, MainActivity.SOUND_TAG[3], R.raw.mei);
        soundPoolUtil.loadR(this, MainActivity.SOUND_TAG[5], R.raw.niganma);

        spUtils = new SPUtils("biecaibaikuai");
        history = spUtils.getFloat("history", 0f);

        historyTv.setText("历史最高记录：" + history);
    }

    @Override
    protected void initEvent() {
        gridGameView.setListener(new BieCaiBaiKuaiListener() {
            @Override
            public void onSpeedAdd(float speed) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        speedTip.setText("Speed : " + speed);
                    }
                });
            }

            @Override
            public void onTouch() {
                soundPoolUtil.play(MainActivity.SOUND_TAG[soundIndex % 4]);
                soundIndex++;
            }

            @Override
            public void onOver() {
                soundPoolUtil.play(MainActivity.SOUND_TAG[5]);
                if (gridGameView.getSpeed() > history) {
                    spUtils.putFloat("history", gridGameView.getSpeed());
                }
            }
        });
    }


}